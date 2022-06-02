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
import gplx.core.lists.*;
import java.util.Arrays;
public abstract class Wdata_list {
	protected final Json_nde list_nde;
	protected final byte[] qid;
	protected final Object[] links;
	protected boolean sorted;
	public Wdata_list(Json_nde list_nde, byte[] qid) {
		this.list_nde = list_nde;
		this.qid = qid;
		this.links = new Object[Count()];
		this.sorted = false;
	}
	public int Count() {
		if (list_nde == null)
			return 0;
		else
			return list_nde.Len();
	}
	public Object Get_by(byte[] key) {
		if (list_nde == null)
			return null;
		int list_len = list_nde.Len();
		if (sorted) {
			for (int i = 0; i < list_len; i++) {
				Wdata_list_itm itm = (Wdata_list_itm)links[i];
				if (Bry_.Eq(key, itm.Key())) {
					return links[i];
				}
			}
		}
		else {
			for (int i = 0; i < list_len; ++i) {
				Json_kv data_kv = Json_kv.Cast(list_nde.Get_at(i));
				byte[] lang_bry = data_kv.Key().Data_bry();
				if (Bry_.Eq(lang_bry, key)) {
					return Make_label(i, lang_bry, data_kv.Val());
				}
			}
		}
		return null;
	}
	public Object Get_at(int key) {
		Json_kv data_kv = Json_kv.Cast(list_nde.Get_at(key));
		byte[] lang_bry = data_kv.Key().Data_bry();
		return Make_label(key, lang_bry, data_kv.Val());
	}
	public void Sort_by(ComparerAble comparer) {
		int len = Count();
		for (int i = 0; i < len; i++) {
			if (links[i] == null)
				Get_at(i);
		}
		Arrays.sort(links, comparer);
		sorted = true;
	}
	public abstract Object Make_label(int pos, byte[] key, Json_itm val_itm);
}
