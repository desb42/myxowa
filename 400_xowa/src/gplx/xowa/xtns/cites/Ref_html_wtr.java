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
package gplx.xowa.xtns.cites; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.brys.*; import gplx.core.brys.fmtrs.*; import gplx.core.brys.args.*;
import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.langs.Xol_lang_stub_;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*; import gplx.langs.htmls.*;
public class Ref_html_wtr {
	private final    Xoh_ref_list_fmtr grp_list_fmtr = new Xoh_ref_list_fmtr();
	private final    Bfr_arg__bry_fmtr itm_id_fmtr = Bfr_arg_.New_bry_fmtr__null(), grp_id_fmtr = Bfr_arg_.New_bry_fmtr__null();
	private final    Cite_mgr mgr;
	public Ref_html_wtr(Xowe_wiki wiki) {
		cfg = Ref_html_wtr_cfg.new_();
		mgr = new Cite_mgr(wiki);
	}
	public void Xnde_ref(Xoh_wtr_ctx opts, Bry_bfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		Bry_bfr tmp_ref = Bry_bfr_.New();
		Ref_nde itm = (Ref_nde)xnde.Xnde_xtn();
		if (itm == null) return;
		if (itm.Follow_y()) return;	// NOTE: "follow" is always appended to preceding ref; will never generate its own ^ a  
		// cite-ref followed by cite-note
		cfg.Itm_html().Bld_bfr_many(tmp_ref
			, Itm_id(itm, true, cfg.Itm_crlp(), cfg.Itm_crls())
			, Grp_id(itm, cfg.Itm_crslp(), cfg.Itm_crsls())
			, mgr.getLinkLabel(itm.Idx_major() + 1, itm.Group())
			);
		bfr.Add(Db_expand.Extracheck(tmp_ref, cfg.Itm_accessibility_label()));
	}
	public Ref_html_wtr_cfg Cfg() {return cfg;} private Ref_html_wtr_cfg cfg;
	public void Init_by_wiki(Xowe_wiki wiki) {
		cfg.Init_by_wiki(wiki);
	}
	public void Init_per_page() {
		mgr.Clear();
	}
	private Bfr_arg Itm_id(Ref_nde itm, boolean caller_is_ref, String pre, String suf) {
		if (itm.Name() == Bry_.Empty)
			return itm_id_fmtr.Set(cfg.Itm_id_uid(), pre, itm.Uid(), suf);
		else {
			if (caller_is_ref)
				return itm_id_fmtr.Set(cfg.Itm_id_key_one(), pre, itm.Name(), itm.Idx_major(), itm.Idx_minor(), suf);
			else
				return itm_id_fmtr.Set(cfg.Itm_id_key_many(), pre, itm.Name(), itm.Idx_major(), suf);
		}
	}
	private Bfr_arg Grp_id(Ref_nde itm, String pre, String suf) {
		return itm.Name() == Bry_.Empty	// name is blank >>> uid 
			? grp_id_fmtr.Set(cfg.Grp_id_uid(), pre, itm.Uid(), suf)
			: grp_id_fmtr.Set(cfg.Grp_id_key(), pre, itm.Name(), itm.Idx_major(), suf);
	}
	private int List_len(Ref_nde itm) {
		int len = itm.Related_len();
		int rv = len;
		for (int i = 0; i < len; i++) {
			Ref_nde list_itm = itm.Related_get(i);
			if (list_itm.Nested()) --rv;
		}
		return rv;
	}
	public void Xnde_references(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_wtr_ctx opts, Xoae_page wpg, Bry_bfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		References_nde references = (References_nde)xnde.Xnde_xtn();
		Ref_itm_lst lst = wpg.Ref_mgr().Lst_get(references.Group(), references.List_idx());	// get group; EX: <references group="note"/>
		if (lst == null) return;	// NOTE: possible to have a grouped references without references; EX: Infobox planet; <references group=note> in sidebar, but no refs 
		if (lst.Itms_len() == 0) return;
		boolean response_wrap; // default case (depends on wiki!!)
		int langid = ctx.Wiki().Lang().Lang_id();
		if (langid == Xol_lang_stub_.Id_de || langid == Xol_lang_stub_.Id_it)
			response_wrap = false;
		else
			response_wrap = true;
                    
		if (references.Responsive() != Bry_.Empty) {
			if  (references.Responsive()[0] == '0')
				response_wrap = false;
			else
				response_wrap = true;
		}
		if (response_wrap) {
			bfr.Add(Bry_.new_a7("<div class=\"mw-references-wrap"));
			if (lst.Itms_len() > 10) {
				bfr.Add(Bry_.new_a7(" mw-references-columns"));
			}
			bfr.Add(Bry_.new_a7("\">"));
		}
		bfr.Add(cfg.Grp_bgn());
		int itms_len = lst.Itms_len();
		Bry_bfr tmp_ref = Bry_bfr_.New();
		for (int j = 0; j < itms_len; j++) {	// iterate over itms in grp
			Bry_bfr tmp = Bry_bfr_.New();
			Ref_nde head_itm = lst.Itms_get_at(j);
			int list_len = List_len(head_itm);
			grp_list_fmtr.Init(ctx.Wiki(), cfg, head_itm);
			Ref_nde text_itm = grp_list_fmtr.Identify_main_ref();// find the item that has the text (there should only be 0 or 1)
			if (text_itm.Body() != null) {
				// extra span
				tmp.Add_str_a7("<span class=\"reference-text\">");
				wtr.Write_tkn_to_html(tmp, ctx, opts, text_itm.Body().Root_src(), null, Xoh_html_wtr.Sub_idx_null, text_itm.Body());
				tmp.Add(Gfh_tag_.Span_rhs);
			}

			// add follows
			int related_len = head_itm.Related_len();
			for (int k = 0; k < related_len; k++) {
				Ref_nde related_itm = head_itm.Related_get(k);
				if (related_itm.Follow_y()) {	// NOTE: both follow and related are in the related list; only add follow
					// add a space if...
					if (tmp.Len_gt_0() // tmp has text; (ignores 0th)
						&& related_itm.Body() != null && related_itm.Body().Subs_len() > 0) // this item has text (ignore blank items)
						tmp.Add_byte_space();// add space; REF.MW:Cite_body.php;$this->mRefs[$group][$follow]['text'] = $this->mRefs[$group][$follow]['text'] . ' ' . $str;
					// not sure about the extra span
					if (related_itm.Body() != null) { // only seen in de.wikisource.org/wiki/RE:Ilici
						tmp.Add_str_a7("<span class=\"reference-text\">");
						wtr.Write_tkn_to_html(tmp, ctx, opts, related_itm.Body().Root_src(), null, Xoh_html_wtr.Sub_idx_null, related_itm.Body());
						tmp.Add(Gfh_tag_.Span_rhs);
					}
				}
			}

			// cite-note followed by cite-ref
			if (list_len == 0) {		// ref has 0 list_itms or 1 list_itm but nested; EX: "123 ^ text"
				cfg.Grp_html_one().Bld_bfr_many(tmp_ref
					, Grp_id(head_itm, cfg.Itm_crslp(), cfg.Itm_crsls())	// NOTE: use head_itm for back ref to work (^ must link to same id)
					, Itm_id(head_itm, true, cfg.Itm_crlp(), cfg.Itm_crls())
					, tmp
					, null // 20200409 (fourth arg) strictly this should be $extraAttributes = Html::expandAttributes( [ 'class' => 'mw-cite-dir-' . $dir ] );
					);
			}
			else {  // ref has 1+ itms; EX: "123 ^ a b c text"
				cfg.Grp_html_many().Bld_bfr_many(tmp_ref
					, Itm_id(text_itm, false, cfg.Itm_crslp(), cfg.Itm_crsls())
					, grp_list_fmtr
					, tmp
					, null // 20200409 (fourth arg) strictly this should be $extraAttributes = Html::expandAttributes( [ 'class' => 'mw-cite-dir-' . $dir ] );
					);
			}
			bfr.Add(Db_expand.Extracheck(tmp_ref, cfg.Itm_accessibility_label()));
		}
		bfr.Add(cfg.Grp_end());
		if (response_wrap) {
			bfr.Add(Bry_.new_a7("</div>\n"));
		}
		lst.Itms_clear(); // so as not to reuse
	}
	public void Xnde_ref_x(Bry_bfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		Bry_bfr tmp_ref = Bry_bfr_.New();
		Ref_nde itm = (Ref_nde)xnde.Xnde_xtn();
		if (itm == null) return;
		if (itm.Follow_y()) return;	// NOTE: "follow" is always appended to preceding ref; will never generate its own ^ a  
		// cite-ref followed by cite-note
		cfg.Itm_html().Bld_bfr_many(tmp_ref
			, Itm_id(itm, true, cfg.Itm_crlp(), cfg.Itm_crls())
			, Grp_id(itm, cfg.Itm_crslp(), cfg.Itm_crsls())
			, mgr.getLinkLabel(itm.Idx_major() + 1, itm.Group())
			);
		bfr.Add(Db_expand.Extracheck(tmp_ref, cfg.Itm_accessibility_label()));
	}
}
