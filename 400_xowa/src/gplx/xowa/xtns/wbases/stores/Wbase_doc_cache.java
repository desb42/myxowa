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
package gplx.xowa.xtns.wbases.stores;
import gplx.*;
import gplx.xowa.xtns.wbases.*;
import gplx.core.lists.caches.*;
import gplx.core.logs.*;
public interface Wbase_doc_cache {
	void Add(byte[] qid, Wdata_doc doc);
	Wdata_doc Get_or_null(byte[] qid);
	void Clear();
	void Term();
}
class Wbase_doc_cache__null implements Wbase_doc_cache {
	@Override public void Add(byte[] qid, Wdata_doc doc) {}
	@Override public Wdata_doc Get_or_null(byte[] qid) {return null;}
	@Override public void Clear() {}
	@Override public void Term() {}
}
class Wbase_doc_cache__hash implements Wbase_doc_cache {
	private final    Hash_adp_bry hash = Hash_adp_bry.cs();
	@Override public void Add(byte[] qid, Wdata_doc doc) {hash.Add(qid, doc);}
	@Override public Wdata_doc Get_or_null(byte[] qid) {return (Wdata_doc)hash.Get_by_bry(qid);}
	@Override public void Clear() {hash.Clear();}
	@Override public void Term() {hash.Clear();}
}
class Wbase_doc_cache__mru implements Wbase_doc_cache {
	private final    Mru_cache_mgr cache;
	public Wbase_doc_cache__mru(long cache_max, long compress_size, long used_weight) {
		this.cache = Mru_cache_mgr.New_by_mb_secs(Gfo_log_wtr.New_dflt("wbase", "cache_mru_{0}.csv"), cache_max, compress_size, used_weight);
	}
	@Override public void Add(byte[] qid, Wdata_doc doc) {cache.Add(String_.new_a7(qid), doc, doc.Jdoc_size());}
	@Override public Wdata_doc Get_or_null(byte[] qid) {return (Wdata_doc)cache.Get_or_null(String_.new_a7(qid));}
	@Override public void Clear() {}
	@Override public void Term() {
		cache.Flush();
		cache.Print();
	}
}
class Wbase_doc_cache__sliding implements Wbase_doc_cache {
	private final Slide_bucket qbucket = new Slide_bucket(40);
	private final Slide_bucket pbucket = new Slide_bucket(30);
	private final Slide_bucket lbucket = new Slide_bucket(20);
	@Override public void Add(byte[] qid, Wdata_doc doc) {
		Slide_bucket bucket;
		switch (qid[0] | 32 ) {
			case 'p': bucket = pbucket; break;
			case 'l': bucket = lbucket; break;
			default: /*case 'q':*/ bucket = qbucket; break;
		}
		bucket.Add(qid, doc);
	}
	@Override public Wdata_doc Get_or_null(byte[] qid) {
		Slide_bucket bucket;
		switch (qid[0] | 32 ) {
			case 'p': bucket = pbucket; break;
			case 'l': bucket = lbucket; break;
			default: /*case 'q':*/ bucket = qbucket; break;
		}
		return bucket.Get_or_null(qid);
	}
	@Override public void Clear() {
	// no point!
	}
	@Override public void Term() {
		Clear();
		Gfo_usr_dlg_.Instance.Log_many("", "", "siliding_cache q hits:~{0} misses:~{1}", qbucket.hit, qbucket.miss);
		Gfo_usr_dlg_.Instance.Log_many("", "", "siliding_cache p hits:~{0} misses:~{1}", pbucket.hit, pbucket.miss);
		Gfo_usr_dlg_.Instance.Log_many("", "", "siliding_cache l hits:~{0} misses:~{1}", lbucket.hit, lbucket.miss);
	}
}
class Slide_bucket {
	private final int[] obs_int;
	private final Wdata_doc[] obs_wdd;
	private int position = 0;
	private final int slider_size;
	public long hit = 0;
	public long miss = 0;
	public Slide_bucket(int slider_size) {
		obs_int = new int[slider_size];
		obs_wdd = new Wdata_doc[slider_size];
		this.slider_size = slider_size;
	}
	public void Add(byte[] qid, Wdata_doc doc) {
		if (position >= slider_size)
			position = 0;
		obs_int[position] = Parse(qid, 1, qid.length);
		obs_wdd[position] = doc;
		position++;
	}
	public Wdata_doc Get_or_null(byte[] qid) {
		int key = Parse(qid, 1, qid.length);
		for (int i = 0; i < slider_size; i++) {
			if (obs_int[i] == key) {
				hit++;
				return obs_wdd[i];
			}
		}
		miss++;
		return null;
	}
	private int Parse(byte[] raw, int bgn, int end) {
		// process args
		if (raw == null) return 0;
		int raw_len = end - bgn;
		if (raw_len == 0) return 0;

		// check neg once
		byte cur = raw[bgn];
		boolean neg = false;
		if (cur == '-') {
			neg = true;
			cur = raw[++bgn];
		}
		int rv = 0;
		while (true) {
			if (cur >= '0' && cur <= '9')
				rv = rv*10 + (cur - '0');
			else
				return 0;
			if (++bgn < end)
				cur = raw[bgn];
			else
				break;
		}
		if (neg)
			rv *= -1;
		return rv;
	}
}
/*
class Slider {
	public byte[] key;
	public int len;
	public Wdata_doc doc;
	public Slider(byte[] key, Wdata_doc doc) {
		this.key = key;
		this.doc = doc;
		this.len = key.length;
	}
}
class Wbase_doc_cache__sliding implements Wbase_doc_cache {
	private final Slider[] obs = new Slider[20];
	private int position = 0;
	private long hit = 0;
	private long miss = 0;
	public void Add(byte[] qid, Wdata_doc doc) {
		if (position >= 20)
			position = 0;
		obs[position++] = new Slider(qid, doc);
	}
	public Wdata_doc Get_or_null(byte[] qid) {
		int qid_len = qid.length;
		for (int i = 0; i < 20; i++) {
			Slider sl = obs[i];
			if (sl != null && qid_len == sl.len) {
				byte[] key = sl.key;
				int j;
				for (j = 0; j < qid_len; j++) {
					if (key[j] != qid[j])
						break;
				}
				if (j == qid_len) {
					hit++;
					return sl.doc;
				}
			}
		}
		miss++;
		return null;
	}
	public void Clear() {
		for (int i = 0; i < 20; i++) {
			obs[i] = null;
		}
	}
	public void Term() {
		Clear();
		Gfo_usr_dlg_.Instance.Log_many("", "", "siliding_cache hits:~{0} misses:~{1}", hit, miss);
	}
}
*/
class Wbase_doc_cache__per_thread implements Wbase_doc_cache {
	private byte[][] thread_names = new byte[2][];
	private Hash_adp_bry[] thread_hashes = new Hash_adp_bry[2];
	private int names_len = 0;
	private int names_max = 2;

	private Hash_adp_bry getThreadHash() {
		String name = Thread.currentThread().getName();
                int len = name.length();
		byte b1 = (byte)name.charAt(len - 3);
		byte b2 = (byte)name.charAt(len - 2);
		byte b3 = (byte)name.charAt(len - 1);
		for (int i = 0; i < names_len; i++) {
			if (thread_names[i][0] == b1 && thread_names[i][1] == b2 && thread_names[i][2] == b3) {
				return thread_hashes[i];
			}
		}
		// add to thread names
		if (names_len + 1 > names_max) {
			// increase
			byte[][] names = new byte[names_max*2][];
			Hash_adp_bry[] hashes = new Hash_adp_bry[names_max*2];
			for (int i = 0; i < names_max; i++)
				names[i] = thread_names[i];
			for (int i = 0; i < names_max; i++)
				hashes[i] = thread_hashes[i];
			names_max *= 2;
                        thread_names = names;
                        thread_hashes = hashes;
		}
		thread_names[names_len] = new byte[]{b1, b2, b3};
		thread_hashes[names_len] = Hash_adp_bry.cs();
		return thread_hashes[names_len++];
	}
	
	@Override public void Add(byte[] qid, Wdata_doc doc) {
		Hash_adp_bry hash = getThreadHash();
		hash.Add(qid, doc);
	}
	@Override public Wdata_doc Get_or_null(byte[] qid) {
		Hash_adp_bry hash = getThreadHash();
		return (Wdata_doc)hash.Get_by_bry(qid);
	}
	@Override public void Clear() {
		Hash_adp_bry hash = getThreadHash();
		hash.Clear();
	}
	@Override public void Term() {
		Clear();
	}
}
