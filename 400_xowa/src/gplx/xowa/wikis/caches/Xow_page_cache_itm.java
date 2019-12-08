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
	private static RandomAccessFile raf;
	private static File tempFile;
	private static long      maxofs = 0;
	private static boolean openfile = true;
	private long access_count;
	private long cache_len;
	private long fileoffset;
	public long Access_count() { return access_count; }
	public void Access_count_increment() { access_count++; }
	public Xow_page_cache_itm(boolean cache_permanently, int page_id, Xoa_ttl ttl, byte[] wtxt__direct, byte[] wtxt__redirect) {
		if (openfile) {
			try {
				tempFile = File.createTempFile("prefix-", "-suffix");
				tempFile.deleteOnExit();
				raf = new RandomAccessFile(tempFile, "rw");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			openfile = false;
		}
		this.cache_permanently = cache_permanently;
		this.page_id = page_id; this.ttl = ttl; this.wtxt__redirect = wtxt__redirect;
		this.access_count = 1;
		Set_text_bry_by_db(wtxt__direct);
	}
	public Xoa_ttl Ttl() {return ttl;} private Xoa_ttl ttl;
	public byte[] Wtxt__direct() {
		if (cache_len == 0) return Bry_.Empty;
		byte[] data = new byte[(int)cache_len];
		try {
			rwl.writeLock().lock();
			raf.seek(fileoffset);
			long page_id = raf.readLong();
			long size = raf.readLong();
			if (page_id != this.page_id || size != cache_len)
				size = 1;
			raf.read(data);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			rwl.writeLock().unlock();
		}
		return data;
	}
	public byte[] Wtxt__redirect()	{return wtxt__redirect;} private byte[] wtxt__redirect;
	public byte[] Wtxt__redirect_or_direct() {
		return wtxt__redirect == null ? Wtxt__direct() : wtxt__redirect;
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
			try {
				cache_len = v.length;
				rwl.writeLock().lock();
				raf.seek(maxofs);
				raf.writeLong(page_id);
				raf.writeLong(cache_len);
				raf.write(v);
				fileoffset = maxofs;
				maxofs += cache_len + 8 + 8;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			finally {
				rwl.writeLock().unlock();
			}
		}
	}
	public void Redirect_id_(int v) {this.redirect_id = v;}
	public void Set_redirect(Xoa_ttl ttl, byte[] trg_wtxt) {
		this.ttl = ttl;
		Set_text_bry_by_db(trg_wtxt);
		this.wtxt__redirect = Wtxt__direct();
	}

	public static void SetRandomAccessFile(RandomAccessFile rafx) {
		raf = rafx;
	}
	public static void Reset() {
		maxofs = 0; // reset the filepointer (as part of a cache clear)
	}

	public static final    Xow_page_cache_itm Null = null;
	public static final    Xow_page_cache_itm Missing = new Xow_page_cache_itm(false, -1, null, null, null);
}
