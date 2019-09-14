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
package gplx.xowa.xtns.wbases.parsers; import gplx.*; import gplx.xowa.xtns.wbases.senses.*;
import gplx.langs.jsons.*; import gplx.xowa.xtns.wbases.core.*;
class Wdata_senses_parser {
	Wdata_doc_parser_v2 doc_parser;
	byte[] doc_src;
	public void Make_sense_itms(byte[] qid, List_adp sense_itms_list, byte[] src, Json_nde sense_grp, Wdata_doc_parser_v2 doc_parser) {
		this.doc_parser = doc_parser;
		this.doc_src = src;
		int sense_itms_len = sense_grp.Len();
		for (int i = 0; i < sense_itms_len; ++i) {
			Json_nde sense_itm_nde = Json_nde.cast(sense_grp.Get_at(i));
			Wbase_sense_base itm = Parse_sense_itm(qid, sense_itm_nde);
			sense_itms_list.Add(itm);
		}
	}

	private Wbase_sense_base Parse_sense_itm(byte[] qid, Json_nde nde) {
		int len = nde.Len();
		byte[] id = null;
		Ordered_hash claims = null;
		Ordered_hash glosses = null;
		for (int i = 0; i < len; ++i) {
			Json_kv sub = Json_kv.cast(nde.Get_at(i));
			byte tid = Wdata_dict_sense.Reg.Get_tid_or_max_and_log(qid, sub.Key().Data_bry()); if (tid == Byte_.Max_value_127) continue;
			switch (tid) {
				case Wdata_dict_sense.Tid__id:			id = sub.Data_bry(); break;
				case Wdata_dict_sense.Tid__claims:		claims = doc_parser.Parse_local_claims(qid, Json_nde.cast(sub.Val()), doc_src); break;
				case Wdata_dict_sense.Tid__glosses:		glosses = doc_parser.Parse_local_langval(qid, Json_nde.cast(sub.Val())); break;
			}
		}
		return new Wbase_sense_base(id, claims, glosses);
	}
}
