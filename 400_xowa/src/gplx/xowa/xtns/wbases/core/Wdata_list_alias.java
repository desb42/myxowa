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
public class Wdata_list_alias extends Wdata_list {
	public Wdata_list_alias(Json_nde list_nde, byte[] qid) {
		super(list_nde, qid);
	}
	@Override public Object Make_label(int pos, byte[] key, Json_itm val_itm) {
		if (links[pos] != null)
			return links[pos];
		byte[] val;
		Json_ary vals_ary	= Json_ary.cast_or_null(val_itm);
		int vals_len = vals_ary.Len();
		byte[][] vals = new byte[vals_len][];
		for (int j = 0; j < vals_len; ++j) {
			Json_nde lang_nde = Json_nde.Cast(vals_ary.Get_at(j));
			int k_len = lang_nde.Len();
			for (int k = 0; k < k_len; ++k) {
				Json_kv sub = Json_kv.Cast(lang_nde.Get_at(k));
				byte tid = Wdata_dict_langtext.Reg.Get_tid_or_max_and_log(qid, sub.Key().Data_bry()); if (tid == Byte_.Max_value_127) continue;
				switch (tid) {
					case Wdata_dict_langtext.Tid__language:		break;
					case Wdata_dict_langtext.Tid__value:		vals[j] = sub.Val().Data_bry(); break;
				}
			}
		}
		Wdata_alias_itm itm = new Wdata_alias_itm(key, vals);
		links[pos] = itm;
		return itm;
	}
}
