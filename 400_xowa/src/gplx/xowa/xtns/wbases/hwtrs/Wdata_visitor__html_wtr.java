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
package gplx.xowa.xtns.wbases.hwtrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*;
import gplx.core.brys.fmtrs.*;
import gplx.langs.htmls.Gfh_tag_;
import gplx.xowa.langs.*;
import gplx.xowa.xtns.wbases.core.*; import gplx.xowa.xtns.wbases.claims.*; import gplx.xowa.xtns.wbases.claims.enums.*; import gplx.xowa.xtns.wbases.claims.itms.*;
class Wdata_visitor__html_wtr implements Wbase_claim_visitor {
	private Wdata_wiki_mgr wdata_mgr; private Wdata_hwtr_msgs msgs; private Wdata_lbl_mgr lbl_mgr;
	private Xol_lang_itm lang;
	private byte[] ttl; private Bry_bfr tmp_bfr;
	private final    Bry_fmtr tmp_time_fmtr = Bry_fmtr.new_(); private final    Bry_bfr tmp_time_bfr = Bry_bfr_.New_w_size(32);
	public Wdata_visitor__html_wtr Init(Bry_bfr tmp_bfr, Wdata_wiki_mgr wdata_mgr, Wdata_hwtr_msgs msgs, Wdata_lbl_mgr lbl_mgr, Xol_lang_itm lang, byte[] ttl) {
		this.wdata_mgr = wdata_mgr; this.msgs = msgs; this.lbl_mgr = lbl_mgr; this.lang = lang;
		this.tmp_bfr = tmp_bfr; this.ttl = ttl;
		return this;
	}
	public void Visit_str(Wbase_claim_string itm) {
		tmp_bfr.Add(itm.Val_bry());
	}
	public void Visit_entity(Wbase_claim_entity itm) {
		int entity_id = itm.Entity_id();
		byte[] text = null;
		switch (itm.Entity_tid()) {
			case Wbase_claim_entity_type_.Tid__form:
				text = Getformstuff(itm);
				break;
			case Wbase_claim_entity_type_.Tid__sense:
				text = Getsensestuff(itm);
				break;
			case Wbase_claim_entity_type_.Tid__item:
				text = lbl_mgr.Get_text__qid(entity_id);
				break;
			case Wbase_claim_entity_type_.Tid__property:
				text = lbl_mgr.Get_text__pid(entity_id);
				break;
			case Wbase_claim_entity_type_.Tid__lexeme:
				text = lbl_mgr.Get_text__lid(entity_id);
				break;
//			case Wbase_claim_entity_type_.Tid__entityschema:
//				text = lbl_mgr.Get_text__eid(entity_id);
//				break;
		}
		if (text == null) {// handle incomplete wikidata dumps; DATE:2015-06-11
			Xoa_app_.Usr_dlg().Warn_many("", "", "wbase.html_visitor:page does not exists; page=~{0}", entity_id);
			return;
		}
		Wdata_hwtr_mgr.Write_link_wikidata(tmp_bfr, itm.Page_ttl_gui(), text);			
	}
	private byte[] Getformstuff(Wbase_claim_entity itm) {
		//Wdata_doc wdoc = wdata_mgr.Doc_mgr.Get_by_xid_or_null(itm.Ttl());
		//Form_list()itm.Entity_id_bry()
		return Bry_.new_a7("FORM");
	}
	private byte[] Getsensestuff(Wbase_claim_entity itm) {
		return Bry_.new_a7("SENSE");
	}
	public void Visit_monolingualtext(Wbase_claim_monolingualtext itm) {
		String langname = wdata_mgr.Wdata_wiki().App().Lang_mgr().Name_mgr().fetchLanguageName(String_.new_a7(itm.Lang()), lang.Key_str(), "", ttl);
		// "<span lang=\"$2\" class=\"wb-monolingualtext-value\">$1</span> <span class=\"wb-monolingualtext-language-name\" dir=\"auto\">($3)</span>"
		tmp_bfr.Add(Bry_.new_a7("<span lang=\""));
		tmp_bfr.Add(itm.Lang());
		tmp_bfr.Add(Bry_.new_a7("\" class=\"wb-monolingualtext-value\">"));
		tmp_bfr.Add(itm.Text());
		tmp_bfr.Add(Bry_.new_a7("</span> <span class=\"wb-monolingualtext-language-name\" dir=\"auto\">("));
		tmp_bfr.Add(Bry_.new_u8(langname));
		tmp_bfr.Add(Bry_.new_a7(")</span>"));
		//tmp_bfr.Add_byte(Byte_ascii.Space).Add_byte(Byte_ascii.Paren_bgn).Add(Bry_.new_u8(langname)/*itm.Lang()*/).Add_byte(Byte_ascii.Paren_end);
	}
	public void Visit_quantity(Wbase_claim_quantity itm) {
		Wdata_prop_val_visitor.Write_quantity(tmp_bfr, wdata_mgr, lang, itm.Amount(), itm.Lbound(), itm.Ubound(), itm.Unit());
	}
	public void Visit_globecoordinate(Wbase_claim_globecoordinate itm) {
		Wdata_prop_val_visitor.Write_geo(Bool_.Y, tmp_bfr, lbl_mgr, msgs, itm.Lat(), itm.Lng(), itm.Alt(), itm.Prc(), itm.Glb());
	}
	public void Visit_time(Wbase_claim_time itm) {
		itm.Write_to_bfr(tmp_bfr, tmp_time_bfr, tmp_time_fmtr, msgs, ttl);
	}
	public void Visit_system(Wbase_claim_value itm) {
		switch (itm.Snak_tid()) {
			case Wbase_claim_value_type_.Tid__somevalue:
                            tmp_bfr.Add(someval);
                            tmp_bfr.Add(msgs.Val_tid_somevalue()); 
                            tmp_bfr.Add(Gfh_tag_.Span_rhs);
                            break;
			case Wbase_claim_value_type_.Tid__novalue:
                            tmp_bfr.Add(noval);
                            tmp_bfr.Add(msgs.Val_tid_novalue());
                            tmp_bfr.Add(Gfh_tag_.Span_rhs);
                            break;
		}
	}
	private static byte[] someval = Bry_.new_a7("<span class=\"wikibase-snakview-variation-somevaluesnak\">");
	private static byte[] noval = Bry_.new_a7("<span class=\"wikibase-snakview-variation-novaluesnak\">");
}
