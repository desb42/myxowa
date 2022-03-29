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
import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.wbases.*;
import gplx.core.primitives.*;
import gplx.xowa.wikis.domains.*;
public class Wbase_qid_mgr_redis extends Wbase_qid_mgr {// EX: "enwiki|0|Earth" -> "Q2"
	public Wbase_qid_mgr_redis(Wdata_wiki_mgr wbase_mgr) {
		super(wbase_mgr);
	}
	@Override public void Enabled_(boolean v) {this.enabled = v;} private boolean enabled;
	@Override public void Clear() {
	}
	@Override public byte[] Get_qid_or_null(Xowe_wiki wiki, Xoa_ttl ttl) {return Get_qid_or_null(wiki.Wdata_wiki_abrv(), ttl);}
	@Override public byte[] Get_qid_or_null(byte[] wdata_wiki_abrv, Xoa_ttl ttl)	{
		if (!enabled) return null;
		if (Bry_.Len_eq_0(wdata_wiki_abrv)) return null;			// "other" wikis will never call wikidata

		// make key; EX: "enwiki|014|Earth"
		byte[] key = Bry_.Add(wdata_wiki_abrv, Byte_ascii.Pipe_bry, ttl.Ns().Num_bry(), Byte_ascii.Pipe_bry, ttl.Page_db());
		byte[] rv = Db_redis.Getfrompool(key);
		if (rv == null) {
			synchronized (this) {
				rv = Db_redis.Getfrompool(key);
				if (rv == null) {
					rv = wbase_mgr.Wdata_wiki().Db_mgr().Load_mgr().Load_qid(wdata_wiki_abrv, ttl.Ns().Id(), ttl.Page_db());
					if (rv == null) {
						Db_redis.Setfrompool(key, Bry_.Empty);
						return null;
					}
					else
						Db_redis.Setfrompool(key, rv);
				}
				else if (rv.length == 0)
					return null;
				return rv;
			}
		}
		else if (rv.length == 0)
			return null;
		return rv;
	}
	@Override public void Add(Bry_bfr bfr, byte[] lang_key, int wiki_tid, byte[] ns_num, byte[] ttl, byte[] qid) {// TEST:
		Xow_abrv_wm_.To_abrv(bfr, lang_key, Int_obj_ref.New_zero().Val_(wiki_tid));
		byte[] qids_key = bfr.Add_byte(Byte_ascii.Pipe).Add(ns_num).Add_byte(Byte_ascii.Pipe).Add(ttl).To_bry();
		Db_redis.Setfrompool(qids_key, qid);
	}
}
