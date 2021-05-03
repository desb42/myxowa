/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.wikis.caches; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*;
import gplx.xowa.wikis.data.tbls.*;
import java.io.*; import java.util.concurrent.locks.*;
public class Xow_page_cache_itm implements Xowd_text_bry_owner {
	private final    Object thread_lock = new Object(); // NOTE: thread-safety needed for xomp since one page-cache is shared across all wkrs
	private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private long access_count;
	private long cache_len;
	public long Access_count() { return access_count; }
	public void Access_count_increment() { access_count++; }
	public int Page_len() {return orig_page_len;} private int orig_page_len;
	public long Offset() {return orig_page_text_offset;} private long orig_page_text_offset;
	public Xow_page_cache_itm(boolean cache_permanently, int page_id, Xoa_ttl ttl, byte[] wtxt__direct, byte[] wtxt__redirect, int page_len, long page_text_offset) {
		this.cache_permanently = cache_permanently;
		this.page_id = page_id; this.ttl = ttl; this.wtxt__redirect = wtxt__redirect;
		this.access_count = 1;
		Set_text_bry_by_db(wtxt__direct);
		if (page_len > 0)
			this.cache_len = page_len;
		this.orig_page_len = page_len;
		this.orig_page_text_offset = page_text_offset;
	}
	public Xoa_ttl Ttl() {return ttl;} private Xoa_ttl ttl;
	private byte[] wtxt__direct;
	public byte[] Wtxt__direct() {
		return wtxt__direct;
	}
	private byte[] wtxt__redirect;
	public byte[] Wtxt__redirect() {
		return wtxt__redirect;
	}
	public byte[] Wtxt__redirect_or_direct() {
		return wtxt__redirect == null ? wtxt__direct : wtxt__redirect;
	}
	public boolean   Cache_permanently() {return cache_permanently;} private final    boolean cache_permanently;
	public long Cache_len() {return cache_len;}

// used by xomp; Scrib_ttl
	public int Page_id() {return page_id;} private int page_id = -1;
	public boolean Page_exists() {return page_id != -1;}
	public int Redirect_id() {return redirect_id;} private int redirect_id = -1;
	public boolean Redirect_exists() {return redirect_id != -1;}
	public void Set_text_bry_by_db(byte[] v) {
		if (v == null)
			cache_len = 0;
		else {
			wtxt__direct = v;
			cache_len = v.length;
		}
	}
	public void Redirect_id_(int v) {this.redirect_id = v;}
	public void Set_redirect(Xoa_ttl ttl, byte[] trg_wtxt) {
		this.ttl = ttl;
		Set_text_bry_by_db(trg_wtxt);
		this.wtxt__redirect = wtxt__direct;
	}

	public static void SetRandomAccessFile(RandomAccessFile rafx) {
		//raf = rafx;
	}
	public static void Reset() {
		//maxofs = 0; // reset the filepointer (as part of a cache clear)
	}

	public static final    Xow_page_cache_itm Null = null;
	public static final    Xow_page_cache_itm Missing = new Xow_page_cache_itm(false, -1, null, null, null, 0, 0);

	public static void convertIntToByteArray(byte[] bytes, int value, int ofs) {
		bytes[ofs] = (byte)(value >> 24);
		bytes[ofs+1] = (byte)(value >> 16);
		bytes[ofs+2] = (byte)(value >> 8);
		bytes[ofs+3] = (byte)value;
	}
	public static int convertByteArrayToInt(byte[] bytes, int ofs) {
		return ((bytes[ofs] & 0xFF) << 24) |
		        ((bytes[ofs+1] & 0xFF) << 16) |
		        ((bytes[ofs+2] & 0xFF) << 8) |
		        ((bytes[ofs+3] & 0xFF) << 0);
	}
}
