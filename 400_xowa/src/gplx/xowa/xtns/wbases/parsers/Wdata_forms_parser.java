/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2019 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.wbases.parsers; import gplx.*; import gplx.xowa.xtns.wbases.forms.*;
import gplx.langs.jsons.*; import gplx.xowa.xtns.wbases.core.*;
class Wdata_forms_parser {
	//??private final    Wbase_form_factory factory = new Wbase_form_factory();
	Wdata_doc_parser_v2 doc_parser;
        byte[] doc_src;
	public void Make_form_itms(byte[] qid, List_adp form_itms_list, byte[] src, Json_nde form_grp, Wdata_doc_parser_v2 doc_parser) {
		this.doc_parser = doc_parser;
		this.doc_src = src;
		int form_itms_len = form_grp.Len();
		for (int i = 0; i < form_itms_len; ++i) {
			Json_nde form_itm_nde = Json_nde.Cast(form_grp.Get_at(i));
			Wbase_form_base itm = Parse_form_itm(qid, form_itm_nde);
			form_itms_list.Add(itm);
		}
	}

	private Wbase_form_base Parse_form_itm(byte[] qid, Json_nde nde) {
		int len = nde.Len();
		byte[] id = null;
		Ordered_hash claims = null;
		Ordered_hash reps = null;
		int[] grams = null;
		for (int i = 0; i < len; ++i) {
			Json_kv sub = Json_kv.Cast(nde.Get_at(i));
			byte tid = Wdata_dict_form.Reg.Get_tid_or_max_and_log(qid, sub.Key().Data_bry()); if (tid == Byte_.Max_value_127) continue;
			switch (tid) {
				case Wdata_dict_form.Tid__id:			id = sub.Data_bry(); break;
				case Wdata_dict_form.Tid__claims:		claims = doc_parser.Parse_local_claims(qid, Json_nde.Cast(sub.Val()), doc_src); break;
				case Wdata_dict_form.Tid__representations:		reps = doc_parser.Parse_local_langval(qid, Json_nde.Cast(sub.Val())); break;
				case Wdata_dict_form.Tid__grammaticalFeatures:		grams = Parse_gramatical(Json_nde.Cast(sub.Val())); break;
			}
		}
		return new Wbase_form_base(id, claims, reps, grams);
	}
//  , "grammaticalFeatures":
//    [ "Q110786"
//    ]
	private int[] Parse_gramatical(Json_nde gram_nde) {
		if (gram_nde == null) return null;
		int len = gram_nde.Len();
		int[] rv = new int[len];
		for (int i = 0; i < len; ++i) {
			Json_itm gram_itm = gram_nde.Get_at(i);
			rv[i] = Parse_gram(gram_itm.Data_bry());
		}
		return rv;
	}
	private static int Parse_gram(byte[] gram_bry) {
		int rv = Bry_.To_int_or(gram_bry, 1, gram_bry.length, -1);
		if (rv == -1)
			throw Err_.new_wo_type("invalid grammaticalFeature", "qid", String_.new_u8(gram_bry));
		return rv;
	}
}
