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
package gplx.xowa.xtns.wbases.stores; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*;
import gplx.core.lists.caches.*;
import gplx.core.logs.*;
public interface Wbase_doc_cache {
	void Add(byte[] qid, Wdata_doc doc);
	Wdata_doc Get_or_null(byte[] qid);
	void Clear();
	void Term();
}
class Wbase_doc_cache__null implements Wbase_doc_cache {
	public void Add(byte[] qid, Wdata_doc doc) {}
	public Wdata_doc Get_or_null(byte[] qid) {return null;}
	public void Clear() {}
	public void Term() {}
}
class Wbase_doc_cache__hash implements Wbase_doc_cache {
	private final    Hash_adp_bry hash = Hash_adp_bry.cs();
	public void Add(byte[] qid, Wdata_doc doc) {hash.Add(qid, doc);}
	public Wdata_doc Get_or_null(byte[] qid) {return (Wdata_doc)hash.Get_by_bry(qid);}
	public void Clear() {hash.Clear();}
	public void Term() {hash.Clear();}
}
class Wbase_doc_cache__mru implements Wbase_doc_cache {
	private final    Mru_cache_mgr cache;
	public Wbase_doc_cache__mru(long cache_max, long compress_size, long used_weight) {
		this.cache = Mru_cache_mgr.New_by_mb_secs(Gfo_log_wtr.New_dflt("wbase", "cache_mru_{0}.csv"), cache_max, compress_size, used_weight);
	}
	public void Add(byte[] qid, Wdata_doc doc) {cache.Add(String_.new_a7(qid), doc, doc.Jdoc_size());}
	public Wdata_doc Get_or_null(byte[] qid) {return (Wdata_doc)cache.Get_or_null(String_.new_a7(qid));}
	public void Clear() {}
	public void Term() {
		cache.Flush();
		cache.Print();
	}
}
class Wbase_doc_cache__sliding implements Wbase_doc_cache {
	private static int SLIDER_SIZE = 40;
	private final int[] obs_int = new int[SLIDER_SIZE];
	private final Wdata_doc[] obs_wdd = new Wdata_doc[SLIDER_SIZE];
	private int position = 0;
	private long hit = 0;
	private long miss = 0;
	public void Add(byte[] qid, Wdata_doc doc) {
		if (position >= SLIDER_SIZE)
			position = 0;
		obs_int[position] = Parse(qid, 1, qid.length);
		obs_wdd[position] = doc;
		position++;
	}
	public Wdata_doc Get_or_null(byte[] qid) {
		int key = Parse(qid, 1, qid.length);
		for (int i = 0; i < SLIDER_SIZE; i++) {
			if (obs_int[i] == key) {
				hit++;
				return obs_wdd[i];
			}
		}
		miss++;
		return null;
	}
	public void Clear() {
	// no point!
	}
	public void Term() {
		Clear();
		Gfo_usr_dlg_.Instance.Log_many("", "", "siliding_cache hits:~{0} misses:~{1}", hit, miss);
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