/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.files;
import gplx.*;
import gplx.xowa.*;
import java.io.*;
import gplx.Io_url;

public class Db_body /*implements Xowd_text_bry_owner*/ {
	private RandomAccessFile raf;
	private int file_count;
	private long MAXSIZE=(long)2*1024*1024*1024;
	private Xoa_app app;
	private String domain;
	private long maxofs = 0;
	public int Wkr_id() {return wkr_id;} private int wkr_id = -1;
	private String fix;
	public Db_body(String fix) {
		this.fix = fix;
	}
	public void Init(Xoa_app app, String domain, int wkr_id) {
		if (this.wkr_id != wkr_id) {
			if (this.wkr_id != -1)
				Close();
			this.wkr_id = wkr_id;
			Io_url filename = app.Fsys_mgr().Wiki_dir().GenSubFil_nest(domain, domain + fix + Integer.toString(wkr_id) + ".dat");

			try {
				File file = new File(filename.Raw());
				boolean v = file.createNewFile();
				raf = new RandomAccessFile(file, "rw");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	public void Close() {
		try {
			raf.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public byte[] Read(long page_id, long ofs, int page_size) {
		synchronized (this) {
			if (page_size == 0) return Bry_.Empty;
			byte[] data = new byte[page_size];
//			byte[] bytes = new byte[8];
			try {
				raf.seek(ofs);
	
//				raf.seek(ofs - 8); // should do checks??????
//				raf.read(bytes);
//				long on_disk_page_id = convertByteArrayToInt(bytes, 0);
//				long on_disk_size = convertByteArrayToInt(bytes, 4);
//				if (on_disk_page_id != page_id || on_disk_size != page_size)
//					on_disk_size = 1;
				raf.read(data);
//				if (data[0] != 31)
//					on_disk_size = 2;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return data;
		}
	}
	public void Create(Xoa_app app, String domain, int file_start) {
		this.file_count = file_start;
		this.app = app;
		this.domain = domain;
		Generate_next_file();
	}
	private void Generate_next_file() {
		if (raf != null)
			Close();

		Io_url filename = app.Fsys_mgr().Wiki_dir().GenSubFil_nest(domain, domain + fix + Integer.toString(++file_count) + ".dat");

		try {
			File file = new File(filename.Raw());
			boolean v = file.createNewFile();
			raf = new RandomAccessFile(file, "rw");
			maxofs = 0;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public Db_body_itm Write(long page_id, byte[] v) {
		long current_ofs = maxofs;
		if (v != null) {
			try {
				int page_size = v.length;
				raf.seek(maxofs);
				byte[] bytes = new byte[8];
				convertIntToByteArray(bytes, (int)page_id, 0);
				convertIntToByteArray(bytes, page_size, 4);
				raf.write(bytes);
				raf.write(v);
				maxofs += page_size + 4 + 4; // or raf.tell????
				if (maxofs > MAXSIZE)
					Generate_next_file();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return new Db_body_itm(file_count, current_ofs + 8);
	}
	public byte[] Add_entry(long page_id, byte[] v) {
		Db_body_itm itm = Write(page_id, v);
		int len = v.length;
		byte[] body = new byte[1+1+4+8+4];
		body[0] = 0x1e;
		body[1] = (byte)file_count;
		Db_html_body.convertIntToByteArray(body, (int)page_id, 2);
		Db_html_body.convertLongToByteArray(body, itm.Ofs(), 6);
		Db_html_body.convertIntToByteArray(body, len, 14);
		return body;
	}
	public static void convertIntToByteArray(byte[] bytes, int value, int ofs) {
		bytes[ofs] = (byte)(value >> 24);
		bytes[ofs+1] = (byte)(value >> 16);
		bytes[ofs+2] = (byte)(value >> 8);
		bytes[ofs+3] = (byte)value;
	}
	public static void convertLongToByteArray(byte[] bytes, long value, int ofs) {
		bytes[ofs]   = (byte)(value >> 56);
		bytes[ofs+1] = (byte)(value >> 48);
		bytes[ofs+2] = (byte)(value >> 40);
		bytes[ofs+3] = (byte)(value >> 32);
		bytes[ofs+4] = (byte)(value >> 24);
		bytes[ofs+5] = (byte)(value >> 16);
		bytes[ofs+6] = (byte)(value >> 8);
		bytes[ofs+7] = (byte)value;
	}
	public static int convertByteArrayToInt(byte[] bytes, int ofs) {
		/*return ((bytes[ofs] & 0xFF) << 24) |
		        ((bytes[ofs+1] & 0xFF) << 16) |
		        ((bytes[ofs+2] & 0xFF) << 8) |
		        ((bytes[ofs+3] & 0xFF) << 0);*/
		        int v = 0;
		        for (int i = ofs; i < ofs + 4; i++) {
		        	v = (v << 8) + (bytes[i] & 0xFF);
		        }
		        return v;
	}
	public static long convertByteArrayToLong(byte[] bytes, int ofs) {
		/*return  ((bytes[ofs] & 0xFF) << 56) |
		        ((bytes[ofs+1] & 0xFF) << 48) |
		        ((bytes[ofs+2] & 0xFF) << 40) |
		        ((bytes[ofs+3] & 0xFF) << 32) |
		        ((bytes[ofs+4] & 0xFF) << 24) |
		        ((bytes[ofs+5] & 0xFF) << 16) |
		        ((bytes[ofs+6] & 0xFF) << 8) |
		        ((bytes[ofs+7] & 0xFF) << 0);*/
		        long v = 0;
		        for (int i = ofs; i < ofs + 8; i++) {
		        	v = (v << 8) + (bytes[i] & 0xFF);
		        }
		        return v;
	}
	public void Set_text_bry_by_db(byte[] b) {}
	public int Page_id() {return -1;}
}
