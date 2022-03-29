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
import gplx.xowa.xtns.wbases.core.*;
import gplx.xowa.wikis.caches.Xow_page_cache_itm;
public class Wbase_pid_mgr_redis extends Wbase_pid_mgr {	// EX: "en|road_map" -> 15 ("Property:P15")
	public Wbase_pid_mgr_redis(Wdata_wiki_mgr wbase_mgr) {
		super(wbase_mgr);
	}
	@Override public void Enabled_(boolean v) {this.enabled = v;} private boolean enabled;
	@Override public void Clear() {
	}
	@Override public void Add(byte[] pid_key, int pid_id) {
		byte[] txt = new byte[4];
		Xow_page_cache_itm.convertIntToByteArray(txt, pid_id, 0);
		Db_redis.Setfrompool(pid_key, txt);
	}
	@Override public int Get_pid_or_neg1(byte[] lang_key, byte[] pid_name) {
		if (!enabled) return Wbase_pid.Id_null;

		// make key; EX: "en|road_map"
		byte[] key = Bry_.Add(lang_key, Byte_ascii.Pipe_bry, pid_name);

		byte[] txt = Db_redis.Getfrompool(key);
		if (txt == null) {
			synchronized (this) {
				txt = Db_redis.Getfrompool(key);
				if (txt == null) {
					int rv = wbase_mgr.Wdata_wiki().Db_mgr().Load_mgr().Load_pid(lang_key, pid_name);
					txt = new byte[4];
					Xow_page_cache_itm.convertIntToByteArray(txt, rv, 0);
					Db_redis.Setfrompool(key, txt);
					return rv;
				}
			}
		}
		return Xow_page_cache_itm.convertByteArrayToInt(txt, 0);
	}
}
