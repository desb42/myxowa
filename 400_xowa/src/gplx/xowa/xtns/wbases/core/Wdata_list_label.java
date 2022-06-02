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
public class Wdata_list_label extends Wdata_list {
	public Wdata_list_label(Json_nde list_nde, byte[] qid) {
		super(list_nde, qid);
	}
	@Override public Object Make_label(int pos, byte[] key, Json_itm val_itm) {
		if (links[pos] != null)
			return links[pos];
		byte[] val;
		if (val_itm instanceof Json_nde) {
			Json_nde data_nde = Json_nde.Cast(val_itm);
			Json_kv text_kv = null;
			int data_nde_len = data_nde.Len();
			for (int j = 0; j < data_nde_len; ++j) {
				Json_kv sub = Json_kv.Cast(data_nde.Get_at(j));
				byte tid = Wdata_dict_langtext.Reg.Get_tid_or_max_and_log(qid, sub.Key().Data_bry()); if (tid == Byte_.Max_value_127) continue;
				switch (tid) {
					case Wdata_dict_langtext.Tid__language:		break;
					case Wdata_dict_langtext.Tid__value:		text_kv	= Json_kv.Cast(sub); break;
				}
			}
			val = text_kv.Val().Data_bry();
		}
		else {
			val = val_itm.Data_bry();
		}
		Wdata_langtext_itm rv = new Wdata_langtext_itm(key, val);
		links[pos] = rv;
		return rv;
	}
	public byte[] Get_text_or_empty(byte[][] langs) {
		Wdata_langtext_itm itm = Get_itm_or_null(langs);
		return itm == null ? Bry_.Empty : itm.Text();
	}
	public Wdata_langtext_itm Get_itm_or_null(byte[][] langs) {
		if (Count() == 0) return null;
		int langs_len = langs.length;
		for (int i = 0; i < langs_len; ++i) {
			Object itm_obj = Get_by(langs[i]);
			if (itm_obj != null) return (Wdata_langtext_itm)itm_obj;
		}
		return null;
	}
	public Wdata_langtext_itm Get_itm_core_or_any() {
		if (Count() == 0) return null;
		Object itm_obj = Get_by(Bry_.new_a7("en"));
		if (itm_obj != null) return (Wdata_langtext_itm)itm_obj;
		return (Wdata_langtext_itm)Get_at(0);
	}
}
