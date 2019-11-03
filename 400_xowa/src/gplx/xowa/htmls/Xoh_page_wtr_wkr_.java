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
package gplx.xowa.htmls; import gplx.*; import gplx.xowa.*;
import gplx.langs.htmls.*; import gplx.xowa.xtns.relatedSites.*;
import gplx.xowa.wikis.nss.*; import gplx.xowa.wikis.pages.*; import gplx.xowa.wikis.pages.tags.*;
import gplx.xowa.parsers.utils.*;	
public class Xoh_page_wtr_wkr_ {
	/*private static int[] qualitycount;
	private static int qualitytot;
	public static void Reset_quality() {
		qualitytot = 0;
	}
	public static void Set_quality(int[] counts, int tot) {
		qualitycount = counts;
		qualitytot = tot;
	}*/
	public static byte[] Bld_page_content_sub(Xoae_app app, Xowe_wiki wiki, Xoae_page page, Bry_bfr tmp_bfr) {
		byte[] subpages = app.Html_mgr().Page_mgr().Subpages_bldr().Bld(wiki.Ns_mgr(), page.Ttl());
		byte[] page_content_sub = page.Html_data().Content_sub();		// contentSub exists; SEE: {{#isin}}
		byte[] quality_table = Generate_quality(wiki, page);
		byte[] redirect_msg = Xop_redirect_mgr.Bld_redirect_msg(app, wiki, page.Redirect_trail());
		return Bry_.Add(subpages, page_content_sub, quality_table, redirect_msg);
	}
	public static byte[] Bld_page_name(Bry_bfr tmp_bfr, Xoa_ttl ttl, byte[] display_ttl) {
		if (Bry_.Len_gt_0(display_ttl)) return display_ttl;		// display_ttl explicitly set; use it
		if (ttl.Ns().Id() == Xow_ns_.Tid__special) {			// special: omit query args, else excessively long titles: EX:"Special:Search/earth?fulltext=y&xowa page index=1"
			tmp_bfr.Add(ttl.Ns().Name_ui_w_colon()).Add(ttl.Page_txt_wo_qargs());
			return tmp_bfr.To_bry_and_clear();
		}
		else
			return ttl.Full_txt_w_ttl_case();				// NOTE: include ns with ttl as per defect d88a87b3
	}
	public static void Bld_head_end(Bry_bfr html_bfr, Bry_bfr tmp_bfr, Xoae_page page) {
		byte[] head_end = Xopg_tag_wtr.Write(tmp_bfr, Bool_.Y, Xopg_tag_wtr_cbk_.Basic, page.Html_data().Custom_head_tags());
		if (Bry_.Len_eq_0(head_end)) return;
		int insert_pos = Bry_find_.Find_fwd(html_bfr.Bfr(), Gfh_tag_.Head_rhs);
		if (insert_pos == Bry_find_.Not_found) {
			Gfo_usr_dlg_.Instance.Warn_many("", "", "could not find </head>");
			return;
		}
		html_bfr.Insert_at(insert_pos, head_end);
	}
	public static void Bld_html_end(Bry_bfr html_bfr, Bry_bfr tmp_bfr, Xoae_page page) {
		byte[] html_end = Xopg_tag_wtr.Write(tmp_bfr, Bool_.Y, Xopg_tag_wtr_cbk_.Basic, page.Html_data().Custom_tail_tags());
		if (html_end == null) return;
		int insert_pos = Bry_find_.Find_bwd(html_bfr.Bfr(), Gfh_tag_.Html_rhs, html_bfr.Len());
		if (insert_pos == Bry_find_.Not_found) {
			Gfo_usr_dlg_.Instance.Warn_many("", "", "could not find </html>");
			return;
		}
		html_bfr.Insert_at(insert_pos, html_end);
	}
	private static byte[] Generate_quality(Xowe_wiki wiki, Xoae_page page) {
		int qualitytot = page.Quality_tots().Qualitycount();
		if (qualitytot == 0) return null;
		int[] qualitycount = page.Quality_tots().Qualitycounts();
		Bry_bfr bfr = Bry_bfr_.New();
		int q0 = qualitycount[0] * 100 / qualitytot;
		int q1 = qualitycount[1] * 100 / qualitytot;
		int q2 = qualitycount[2] * 100 / qualitytot;
		int q3 = qualitycount[3] * 100 / qualitytot;
		int q4 = qualitycount[4] * 100 / qualitytot;
		int qe = qualitycount[5] * 100 / qualitytot;
		byte[] textualAlternative = wiki.Msg_mgr().Val_by_key_args(Bry_.new_a7("proofreadpage-indexquality-alt"), qualitycount[4], qualitycount[3], qualitycount[1] );
		bfr.Add_str_a7("<table class=\"pr_quality noprint\" title=\"").Add(textualAlternative);
		bfr.Add_str_a7("\">\n<tr>\n<td class=\"quality4\" style=\"width:").Add_long_variable(q4);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality3\" style=\"width:").Add_long_variable(q3);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality2\" style=\"width:").Add_long_variable(q2);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality1\" style=\"width:").Add_long_variable(q1);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality0\" style=\"width:").Add_long_variable(q0);
		bfr.Add_str_a7("%;\"></td>\n");
		if (qe > 0)
			bfr.Add_str_a7("<td class=\"qualitye\" style=\"width:").Add_long_variable(qe).Add_str_a7("%;\"></td>\n");
		bfr.Add_str_a7("</tr>\n</table>\n");
		return bfr.To_bry();
	}
}
