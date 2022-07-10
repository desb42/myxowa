package gplx.xowa;
import gplx.*;
import java.io.*;
public class Db_sql_main {
		private byte[] Magic_Header_String;
		public int Page_Size;
		private byte File_format_write_version;
		private byte File_format_read_version;
		private byte Reserved_bytes_per_page;
		private byte Maximum_embedded_payload_fraction;
		private byte Minimum_embedded_payload_fraction;
		private byte Leaf_payload_fraction;
		private int File_change_counter;
		private int In_header_database_size;
		private int Free_page_list;
		private int Free_page_count;
		private int Schema_cookie;
		private int Schema_format_number;
		private int Suggested_cache_size;
		private int Incremental_vacuum_settings;
		private int Text_encoding;
		private int User_version_number;
		private int Incremental_vacuum_mode;
		private int Application_ID;
		private int Version_valid_for_number;
		private int Sqlite_version_number;
		private int maxLocal;
		private int minLocal;
		private int maxLeaf;
		private int minLeaf;
		private Hash_adp master;
		private RandomAccessFile f;
		private String filename;
		private List_adp connections;
		private boolean found;

	public Db_sql_main(String filename) throws IOException {
            this.filename = filename;
		try {
			File file = new File(filename);
			f = new RandomAccessFile(file, "r");
                        found = true;
		}
                catch (java.io.FileNotFoundException e) {
                    this.found = false;
                    return;
                }
                catch (IOException ex) {
			ex.printStackTrace();
		}
		byte[] pagedata = new byte[100];
		f.read(pagedata);
		Magic_Header_String = Bry_.Mid(pagedata, 0, 16);
		int ofs = 16;
		Page_Size = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		File_format_write_version = pagedata[ofs++];
		File_format_read_version = pagedata[ofs++];
		Reserved_bytes_per_page = pagedata[ofs++];
		Maximum_embedded_payload_fraction = pagedata[ofs++];
		Minimum_embedded_payload_fraction = pagedata[ofs++];
		Leaf_payload_fraction = pagedata[ofs++];
		File_change_counter = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		In_header_database_size = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Free_page_list = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Free_page_count = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Schema_cookie = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Schema_format_number = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Suggested_cache_size = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Incremental_vacuum_settings = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Text_encoding = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		User_version_number = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Incremental_vacuum_mode = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Application_ID = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		ofs += 20;
		// ">16sHBBBBBBIIIIIIIIIIII20sII"
		Version_valid_for_number = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		Sqlite_version_number = ((pagedata[ofs++] & 0xff) << 24) + ((pagedata[ofs++] & 0xff) << 16) + ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		maxLocal = (Page_Size - 12)*64/255 - 23;
		minLocal = (Page_Size - 12)*34/255 - 23;
		maxLeaf = Page_Size - 35;
		minLeaf = minLocal;

		if (!Bry_.Eq(Magic_Header_String, Bry_.new_a7("SQLite format 3\0"))) {
			throw Err_.new_("SQL", "not an Sqlite3 database");
		}
		f.seek(0); // restart
		pagedata = new byte[Page_Size];
		f.read(pagedata);
		master = load_master(pagedata, 100);
	}

	private Hash_adp load_master(byte[] pagedata, int ofs) throws IOException {
		Hash_adp master = Hash_adp_.New();
		byte page_type = pagedata[ofs++];
		int free_start = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		int cell_count = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		int content_start = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
		int frag_count = pagedata[ofs++];
		if (page_type != 13) {
			throw Err_.new_("SQL", "not the master page");
		}
		Db_varval varval;
		Db_record rec;
		for (int i = 0; i < cell_count; i++) {
			int cell_ofs = ((pagedata[ofs++] & 0xff) << 8) + (pagedata[ofs++] & 0xff);
			varval = Db_varint.getVarint(pagedata, cell_ofs);
			int header_size = (int)varval.val;
			cell_ofs = varval.ofs;
			varval = Db_varint.getVarint(pagedata, cell_ofs);
			int rowid = (int)varval.val;
			cell_ofs = varval.ofs;
			rec = new Db_record(pagedata, cell_ofs, header_size, f, Page_Size);
			rec.Add(Hash_adp_.New());
			master.Add( rec.Get_at(1), rec);
		}
		return master;
	}
	public int Get_connid() {
            if (!found)
                return 0;
		if (connections == null) {
			connections = List_adp_.New();
			connections.Add(f);
		}
		else {
			RandomAccessFile local_f = null;
			try {
				File file = new File(filename);
				local_f = new RandomAccessFile(file, "r");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			connections.Add(local_f);
		}
		return connections.Len();
	}
        private void error() {
			int a=1;            
        }
	public int Seek_index(int conn_id, String index_table, Object[] keys) {
            if (!found)
                return 0;
		RandomAccessFile local_f = (RandomAccessFile)connections.Get_at(conn_id - 1);
		Db_record root = find_root(index_table);
		if (root == null)
			error();
		int pageno = (int)root.Get_at(3);
		Hash_adp cache = (Hash_adp)root.Get_at(5);
		int recid = 0;
		while (pageno > 0) {
			Db_cache_itm cache_itm = get_cache_page(local_f, pageno, cache);
			pageno = 0;
			byte[] pagedata = cache_itm.pagedata;
			byte page_type = cache_itm.page_type;
			Db_varval varval;
			switch (page_type) {
			case 2:
				for (int i = 0; i < cache_itm.cell_count; i++) {
					int pofs = cache_itm.cell_list[i];
					int left_child_ofs = pofs;
					pofs += 4;
					varval = Db_varint.getVarint(pagedata, pofs);
					int payload_size = (int)varval.val;
					pofs = varval.ofs;
					int match = process_record_key(pagedata, pofs, keys, payload_size);
					if (match < 0) {
						pageno = ((pagedata[left_child_ofs++] & 0xff) << 24) + ((pagedata[left_child_ofs++] & 0xff) << 16) + ((pagedata[left_child_ofs++] & 0xff) << 8) + (pagedata[left_child_ofs++] & 0xff);
						break;
					}
					if (match > 0) {
						recid = match;
						break;
					}
				}
				if (pageno == 0)
					pageno = cache_itm.rightmostpage;
				break;
			case 10:
				for (int i = 0; i < cache_itm.cell_count; i++) {
					int pofs = cache_itm.cell_list[i];
					varval = Db_varint.getVarint(pagedata, pofs);
					int payload_size = (int)varval.val;
					pofs = varval.ofs;
					int match = process_record_key(pagedata, pofs, keys, payload_size);
					if (match < 0)
						break; // not found
					if (match > 0) {
						recid = match;
						break;
					}
				}
				break;
			default:
				error();
			}
		}
		return recid;
	}
	public Db_record Seek_rowid(int conn_id, String table, int rowid) {
            if (!found)
                return null;
		RandomAccessFile local_f = (RandomAccessFile)connections.Get_at(conn_id - 1);
		Db_record root = find_root(table);
		if (root == null)
			error();
		Db_record rec = null;
		int pageno = (int)root.Get_at(3);
		Hash_adp cache = (Hash_adp)root.Get_at(5);
		while (pageno > 0) {
			Db_cache_itm cache_itm = get_cache_page(local_f, pageno, cache);
			pageno = 0;
			byte[] pagedata = cache_itm.pagedata;
			byte page_type = cache_itm.page_type;
			Db_varval varval;
			switch (page_type) {
			case 5:
				for (int i = 0; i < cache_itm.cell_count; i++) {
					if (rowid <= cache_itm.keys[i]) {
						int pofs = cache_itm.cell_list[i];
						pageno = ((pagedata[pofs++] & 0xff) << 24) + ((pagedata[pofs++] & 0xff) << 16) + ((pagedata[pofs++] & 0xff) << 8) + (pagedata[pofs++] & 0xff);
						break;
					}
				}
/*				for (int i = 0; i < cache_itm.cell_count; i++) {
					int pofs = cache_itm.cell_list[i];
					int left_child_ofs = pofs;
					pofs += 4;
					varval = Db_varint.getVarint(pagedata, pofs);
					int integer_key = (int)varval.val;
					pofs = varval.ofs;
					if (rowid <= integer_key) {
						pageno = ((pagedata[left_child_ofs++] & 0xff) << 24) + ((pagedata[left_child_ofs++] & 0xff) << 16) + ((pagedata[left_child_ofs++] & 0xff) << 8) + (pagedata[left_child_ofs++] & 0xff);
						break;
					}
				}*/
				if (pageno == 0)
					pageno = cache_itm.rightmostpage;
				break;
			case 13:
				for (int i = 0; i < cache_itm.cell_count; i++) {
					int pofs = cache_itm.cell_list[i];
					int baseofs = pofs;
					varval = Db_varint.getVarint(pagedata, pofs);
					int header_size = (int)varval.val;
					pofs = varval.ofs;
					varval = Db_varint.getVarint(pagedata, pofs);
					int thisrowid = (int)varval.val;
					pofs = varval.ofs;
					if (rowid < thisrowid) {
						// record not found
						break;
					}
					if (rowid == thisrowid) {
						if (header_size > maxLocal)
							header_size = cache_itm.cell_size(i) - pofs + baseofs;
						try {
							rec = new Db_record(pagedata, pofs, header_size, f, Page_Size);
						}
						catch (IOException e) {
							error();
						}
						break;
					}
				}
				break;
			default:
				error();
			}
		}
		return rec;
	}
	private Db_cache_itm get_cache_page(RandomAccessFile local_f, int pageno, Hash_adp cache) {
		Db_cache_itm itm = null;
		synchronized (this) {
			itm = (Db_cache_itm)cache.Get_by(pageno);
		}
		if (itm == null) {
			try {
				itm = new Db_cache_itm(local_f, pageno, this);
		if (itm.page_type <= 5) {
			synchronized (this) {
				cache.Add(pageno, itm);
			}
		}
			}
			catch (Exception e) {
				error();
			}
		}
		return itm;
	}
	private Db_record find_root(String index_table) {
		return (Db_record)master.Get_by(index_table);
	}
	private int process_record_key(byte[] pagedata, int pofs, Object[] keys, int payload_size) {
		Db_record rec = null;
		try {
			rec = new Db_record(pagedata, pofs, payload_size, f, Page_Size);
		}
		catch (IOException e) {
			error();
		}
		int keys_len = keys.length;
		int rec_len = rec.Len() - 1;
		if (rec_len != keys_len)
			return 0; // not found
		boolean matched = false;
		for (int i = 0; i < keys_len; i++) {
			Object key = keys[i];
			Object r = rec.Get_at(i);
                        Class<?> o_type = key.getClass();
			if (o_type != r.getClass())
				return -1;
			if (o_type == Integer.class) {
				if ((int)key < (int)r)
					return -1;
				if (r == key)
					matched = true;
				else {
					matched = false;
					break;
				}
			}
			else if	(o_type == String.class) {
				int res = ((String)key).compareTo((String)r);
				if (res < 0)
					return -1;
				if (res == 0)
					matched = true;
				else {
					matched = false;
					break;
				}
			}
		}
		if (matched)
			return (int)rec.Get_at(rec_len);
		else
			return 0; // not matched
	}
}
