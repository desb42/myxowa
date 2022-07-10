package gplx.xowa;
import java.io.*;
public class Db_cache_itm {
	public byte page_type;
	public int free_start;
	public int cell_count;
	public int content_start;
	public byte frag_count;
	public int rightmostpage;
	public int[] cell_list;
	public byte[] pagedata;
	private int[] sizes;
	public int[] keys;
    
	public Db_cache_itm(RandomAccessFile f, int page_no, Db_sql_main sql) throws IOException {
		long seek_ofs = (long)(page_no - 1)*sql.Page_Size;
		f.seek(seek_ofs);
		pagedata = new byte[sql.Page_Size];
		f.read(pagedata);
		int ofs = 0;

		page_type = pagedata[ofs++];
		free_start = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		cell_count = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		content_start = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		frag_count = pagedata[ofs++];
		rightmostpage = -1;
		if (page_type <= 5) // interior b-tree pages
			rightmostpage = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		if (page_type == 5)
			keys = new int[cell_count];
		cell_list = new int[cell_count];
		for (int i = 0; i < cell_count; i++) {
			int pofs = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
			cell_list[i] = pofs;
			if (page_type == 5) {
				Db_varval varval = Db_varint.getVarint(pagedata, pofs + 4);
				keys[i] = (int)varval.val;
			}
		}
	}
	public int cell_size(int i) {
		if (free_start == 0) {
			if (i == 0)
				return pagedata.length - cell_list[0];
			else
				return cell_list[i-1] - cell_list[i];
		}
		if (sizes == null)
			buildcellsizes();
		return sizes[i];
	}
	private void buildcellsizes() {
		int freelist_ptr = free_start;
		int[] sizes = new int[cell_count];
		int free_count = 0;
		while (freelist_ptr > 0) {
			freelist_ptr = ((pagedata[freelist_ptr++] & 0xff) << 8) + (pagedata[freelist_ptr++] & 0xff);
			free_count++;
		}
		sortcell[] cell_pos = new sortcell[cell_count + free_count + 1];
		int i;
		for (i = 0; i < cell_count; i++)
			cell_pos[i] = new sortcell(cell_list[i], i);
		freelist_ptr = free_start;
		while (freelist_ptr > 0) {
			cell_pos[i] = new sortcell(freelist_ptr, -1);
			freelist_ptr = ((pagedata[freelist_ptr++] & 0xff) << 8) + (pagedata[freelist_ptr++] & 0xff);
		}
		cell_pos[cell_pos.length - 1] = new sortcell(pagedata.length, -1);
        //cell_pos.sort();
		for (i = 0; i < cell_count + free_count + 1; i++) {
			int pos = cell_pos[i].pos;
			if (pos >= 0)
				sizes[pos] = cell_pos[i+1].ofs - cell_pos[i].ofs;
		}
	}
}
class sortcell {
	public int ofs;
	public int pos;
	public sortcell(int ofs, int pos) {
		this.ofs = ofs;
		this.pos = pos;
	}
}
