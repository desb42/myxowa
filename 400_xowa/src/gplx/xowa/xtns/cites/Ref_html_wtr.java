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
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*;
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
		bfr.Add(checkextra(tmp_ref));
	}
	public Ref_html_wtr_cfg Cfg() {return cfg;} private Ref_html_wtr_cfg cfg;
	public void Init_by_wiki(Xowe_wiki wiki) {
		cfg.Init_by_wiki(wiki);
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
		if (ctx.Wiki().Lang().Lang_id() == Xol_lang_stub_.Id_de)
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
				wtr.Write_tkn_to_html(tmp, ctx, opts, text_itm.Body().Root_src(), null, Xoh_html_wtr.Sub_idx_null, text_itm.Body());
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
					wtr.Write_tkn_to_html(tmp, ctx, opts, related_itm.Body().Root_src(), null, Xoh_html_wtr.Sub_idx_null, related_itm.Body());
				}
			}

                        // cite-note followed by cite-ref
			if (list_len == 0) {		// ref has 0 list_itms or 1 list_itm but nested; EX: "123 ^ text"
				cfg.Grp_html_one().Bld_bfr_many(tmp_ref
					, Grp_id(head_itm, cfg.Itm_crslp(), cfg.Itm_crsls())	// NOTE: use head_itm for back ref to work (^ must link to same id)
					, Itm_id(head_itm, true, cfg.Itm_crlp(), cfg.Itm_crls())
					, tmp
					);
			}
			else {							// ref has 1+ itms; EX: "123 ^ a b c text"
				cfg.Grp_html_many().Bld_bfr_many(tmp_ref
					, Itm_id(text_itm, false, cfg.Itm_crslp(), cfg.Itm_crsls())
					, grp_list_fmtr
					, tmp
					);
			}
			bfr.Add(checkextra(tmp_ref));
		}
		bfr.Add(cfg.Grp_end());
		if (response_wrap) {
			bfr.Add(Bry_.new_a7("</div>\n"));
		}
	}

	private byte[][] uniq = new byte[10][];
	private int uniqcount;
	private void substitute(Bry_bfr tmp, int uniqofs) {
		tmp.Add(uniq[uniqofs]);
	}
	private void expand(Bry_bfr tmp, byte[] src, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			if (b == 0)
				substitute(tmp, src[++i]);
			else
				tmp.Add_byte(b);
		}
	}
	private byte[] checkextra(Bry_bfr bfr) {
		// big hack for lnki [[.aa.|.bb.]] or [[.cc.]] AND <nowiki>x</nowiki> - arrrgh
		// scan for <nowik> first
		// reuse the bfr
		byte b;
		int nowiki_bgn, nowiki_end, lnki_bgn, lnki_end, bar_pos;
		byte[] src = bfr.Bfr();
		int len = bfr.Len();
		int pos = 0;
		int ofs = 0;
		uniqcount = 0;

		while (pos < len) {
			b = src[pos++];
			if (b == '<') {
				if (pos + 7 < len && src[pos] == 'n' && src[pos+1] == 'o' && src[pos+2] == 'w' && src[pos+3] == 'i' && src[pos+4] == 'k' && src[pos+5] == 'i' && src[pos+6] == '>') {
					// <nowiki> found
					pos += 7;
					nowiki_bgn = pos;
					nowiki_end = -1;
					while (pos < len) {
						b = src[pos++];
						if (b == '<') {
							if (pos + 8 < len && src[pos] == '/' && src[pos+1] == 'n' && src[pos+2] == 'o' && src[pos+3] == 'w' && src[pos+4] == 'i' && src[pos+5] == 'k' && src[pos+6] == 'i' && src[pos+7] == '>') {
								// </nowiki> found
								nowiki_end = pos - 1;
								pos += 8;
								break;
							}
						}
					}
					if (nowiki_end > 0) {
						if (ofs == 0)
							ofs = nowiki_bgn - 8;
						int size = nowiki_end - nowiki_bgn;
						byte[] ustr = new byte[size];
						for (int i = 0; i < size; i++) {
							ustr[i] = src[nowiki_bgn + i];
						}
						uniq[uniqcount] = ustr;
						src[ofs++] = 0;
						src[ofs++] = (byte)uniqcount;
						uniqcount++;
					}
				}
				else if (ofs > 0) {
					src[ofs++] = b;
				}
			}
			else if (ofs > 0) {
				src[ofs++] = b;
			}
		}
		if (ofs > 0)
			bfr.Len_(ofs);

		// now go thru looking for [[ .. ]]
		len = bfr.Len();
		pos = 0;
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		while (pos < len) {
			b = src[pos++];
			if (b == '[') {
				if (pos < len && src[pos] == '[') {
					// found '[['
					pos++;
					lnki_bgn = pos;
					lnki_end = -1;
					bar_pos = -1;
					while (pos < len) {
						b = src[pos++];
						if (b == ']') {
							if (pos < len && src[pos] == ']') {
								// ']]' found
								lnki_end = pos - 1;
								pos++;
								break;
							}
						}
						else if (b == '|')
							bar_pos = pos - 1;
					}
					if (lnki_end > 0) {
						tmp_bfr.Add_str_a7("<a href=\"");
						if (bar_pos > 0)
							expand(tmp_bfr, src, lnki_bgn, bar_pos);
						else
							expand(tmp_bfr, src, lnki_bgn, lnki_end);
						tmp_bfr.Add_str_a7("\"");
						tmp_bfr.Add_str_a7(cfg.Itm_accessibility_label());
						tmp_bfr.Add_byte(Byte_ascii.Angle_end);
						if (bar_pos > 0)
							expand(tmp_bfr, src, bar_pos+1, lnki_end);
						else
							expand(tmp_bfr, src, lnki_bgn, lnki_end);
						tmp_bfr.Add_str_a7("</a>");
					}
				}
				else
					tmp_bfr.Add_byte(b);
			}
			else if (b == 0) {
				substitute(tmp_bfr, src[pos++]);
			}
			else {
				tmp_bfr.Add_byte(b);
			}
		}
		for (int i = 0; i < uniqcount; i++) {
			uniq[i] = null;
		}
		bfr.Clear();
		return tmp_bfr.To_bry_and_clear();
	}
}
