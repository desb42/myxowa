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
package gplx.xowa.xtns.wbases.core;
import gplx.*;
import gplx.langs.jsons.*;
public class Wdata_sitelink {
	private final Json_nde list_nde;
	private final byte[] qid;
	private final Wdata_sitelink_itm[] links;
	public Wdata_sitelink(Json_nde list_nde, byte[] qid) {
		this.list_nde = list_nde;
		this.qid = qid;
		this.links = new Wdata_sitelink_itm[Count()];
	}
	public int Count() {
		if (list_nde == null)
			return 0;
		else
			return list_nde.Len();
	}
	public Wdata_sitelink_itm Get_by(byte[] key) {
		if (list_nde == null)
			return null;
		int list_len = list_nde.Len();
		for (int i = 0; i < list_len; ++i) {
			Json_kv data_kv = Json_kv.Cast(list_nde.Get_at(i));
			Json_nde data_nde = Json_nde.Cast(data_kv.Val());
			int data_nde_len = data_nde.Len();
			for (int j = 0; j < data_nde_len; ++j) {
				Json_kv sub = Json_kv.Cast(data_nde.Get_at(j));
				if (sub.Key().Data_bry()[0] == 's') { // site?
					byte[] site_bry = Json_kv.Cast(sub).Val().Data_bry();
					if (Bry_.Eq(site_bry, key)) {
						return Make_sitelink(i, data_nde, data_nde_len);
					}
					break;
				}
			}
		}
		return null;
	}
	public Wdata_sitelink_itm Get_at(int key) {
		Json_kv data_kv = Json_kv.Cast(list_nde.Get_at(key));
		Json_nde data_nde = Json_nde.Cast(data_kv.Val());
		int data_nde_len = data_nde.Len();
		return Make_sitelink(key, data_nde, data_nde_len);
	}
	private Wdata_sitelink_itm Make_sitelink(int pos, Json_nde data_nde, int data_nde_len) {
		if (links[pos] != null)
			return links[pos];
		Json_kv site_kv = null, name_kv = null; Json_ary badges_ary = null;
		for (int j = 0; j < data_nde_len; ++j) {
			Json_kv sub = Json_kv.Cast(data_nde.Get_at(j));
			byte tid = Wdata_dict_sitelink.Reg.Get_tid_or_max_and_log(qid, sub.Key().Data_bry()); if (tid == Byte_.Max_value_127) continue;
			switch (tid) {
				case Wdata_dict_sitelink.Tid__site:			site_kv	= Json_kv.Cast(sub); break;
				case Wdata_dict_sitelink.Tid__title:		name_kv	= Json_kv.Cast(sub); break;
				case Wdata_dict_sitelink.Tid__badges:		badges_ary = Json_ary.cast_or_null(Json_kv.Cast(sub).Val()); break;
			}
		}
		if (site_kv != null && name_kv != null && badges_ary != null) {
			byte[] site_bry = site_kv.Val().Data_bry();
			Wdata_sitelink_itm rv = new Wdata_sitelink_itm(site_bry, name_kv.Val().Data_bry(), badges_ary.Xto_bry_ary());
			links[pos] = rv;
			return rv;
		}
		else
			return null;
	}
}
