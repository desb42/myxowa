//
package gplx.xowa;
import gplx.*;
import java.io.*;
public class Db_record {
	private List_adp row = List_adp_.New();

	public Db_record(byte[] pagedata, int pofs, int payload_size, RandomAccessFile f, int Page_Size) throws IOException {
		int baseofs = pofs;
		Db_varval varval = Db_varint.getVarint(pagedata, pofs);
		int recheadsize = (int)varval.val;
		pofs = varval.ofs;
		int dataofs = baseofs + recheadsize;
		int dataptr = dataofs;
		int inreclength = payload_size - recheadsize;
		while (pofs < dataofs) {
			varval = Db_varint.getVarint(pagedata, pofs);
			int coltype = (int)varval.val;
			pofs = varval.ofs;
			int v;
			long vl;
			switch (coltype) {
			case 0:
				row.Add("*key*");
				break;
			case 1:
				v = pagedata[dataptr++] & 0xff;
				if ((v & 0x80) == 0x80)
					v = (v ^ 0xff) - 1;
				row.Add(v);
				inreclength--;
				break;
			case 2: // 16
				v = ((pagedata[dataptr++] & 0xff) << 8) + (pagedata[dataptr++] & 0xff);
				row.Add(v);
				inreclength -= 2;
				break;
			case 3: // 24
				v = ((pagedata[dataptr++] & 0xff) << 16) + ((pagedata[dataptr++] & 0xff) << 8) + (pagedata[dataptr++] & 0xff);
				row.Add(v);
				inreclength -= 3;
				break;
			case 4: // 32
				v = ((pagedata[dataptr++] & 0xff) << 24) + ((pagedata[dataptr++] & 0xff) << 16) + ((pagedata[dataptr++] & 0xff) << 8) + (pagedata[dataptr++] & 0xff);
				row.Add(v);
				inreclength -= 4;
				break;
			case 5: // 48
				vl = ((pagedata[dataptr++] & 0xff) << 40) + ((pagedata[dataptr++] & 0xff) << 32) + ((pagedata[dataptr++] & 0xff) << 24) + ((pagedata[dataptr++] & 0xff) << 16) + ((pagedata[dataptr++] & 0xff) << 8) + (pagedata[dataptr++] & 0xff);
				row.Add(vl);
				inreclength -= 6;
				break;
			case 6: // 64
				vl = ((pagedata[dataptr++] & 0xff) << 56) + ((pagedata[dataptr++] & 0xff) << 48) + ((pagedata[dataptr++] & 0xff) << 40) + ((pagedata[dataptr++] & 0xff) << 32) + ((pagedata[dataptr++] & 0xff) << 24) + ((pagedata[dataptr++] & 0xff) << 16) + ((pagedata[dataptr++] & 0xff) << 8) + (pagedata[dataptr++] & 0xff);
				row.Add(vl);
				inreclength -= 8;
				break;
			case 7: // 8byte double
				row.Add("double");
				dataptr += 8;
				inreclength -= 8;
				break;
			case 8:
				row.Add(0);
				break;
			case 9:
				row.Add(1);
				break;
			case 10: case 11: case 12:
				row.Add("coltype " + Integer.toString(coltype));
				break;
			default:
				int length = (coltype - 12)/2;
				byte[] data;
				int data_size = 0;
				if (length > inreclength) {
					data = new byte[length];
					int size = copydata(data, 0, pagedata, dataptr, dataptr + inreclength - 4);
					data_size += size;
					dataptr += inreclength - 4;
					while (true) {
						length -= size;
						int nextpage = ((pagedata[dataptr++] & 0xff) << 24) + ((pagedata[dataptr++] & 0xff) << 16) + ((pagedata[dataptr++] & 0xff) << 8) + (pagedata[dataptr++] & 0xff);
						dataptr = 0;
						f.seek((long)(nextpage-1)*Page_Size);
						pagedata = new byte[Page_Size];
						f.read(pagedata);
						if (length > Page_Size - 4) {
							size = copydata(data, data_size, pagedata, 4, Page_Size);
							data_size += size;
						}
						else {
							data_size += copydata(data, data_size, pagedata, 4, length + 4);
							break;
						}
					}
				}
				else {
					data = new byte[length];
					data_size += copydata(data, 0, pagedata, dataptr, dataptr + length);
					dataptr += length;
					inreclength -= length;
				}
				if (coltype % 2 == 0)
					row.Add(data);
				else
					row.Add(String_.new_u8(data));
				break;
			}
		}
	}
	private int copydata(byte[] data, int data_ofs, byte[] pagedata, int bgn, int end) {
		int size = end - bgn;
		for (int i = 0; i < size; i++)
			data[data_ofs++] = pagedata[bgn++];
		return size;
	}
	public Object Get_at(int i) {
		return row.Get_at(i);
	}
	public void Add(Object o) {
		row.Add(o);
	}
	public int Len() {
		return row.Len();
	}
}
