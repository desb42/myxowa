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
package gplx.xowa.xtns.proofreadPage; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*;
import gplx.xowa.parsers.lnkis.files.*;
public class Pp_pagequality_nde implements Xox_xnde {
	private Xop_root_tkn xtn_root;
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		int itm_bgn = xnde.Src_bgn()+12, itm_end = xnde.Src_end()-1; // size of '<pagequality' and '/>'
		if (itm_bgn == src.length)	return;  // NOTE: handle inline where there is no content to parse; EX: <pagequality/>
		if (itm_bgn >= itm_end)		return;  // NOTE: handle inline where there is no content to parse; EX: a<pagequality/>b
		Parse_line(src, itm_bgn, itm_end);
                xtn_root = null;
 		Bry_bfr full_bfr = wiki.Utl__bfr_mkr().Get_m001();
		try {
			Hash_adp_bry lst_page_regy = ctx.Lst_page_regy(); 
			if (lst_page_regy == null) lst_page_regy = Hash_adp_bry.cs();	// SEE:NOTE:page_regy; DATE:2014-01-01
			byte[] page_bry = Bld_wikitext(full_bfr, ctx);
			if (page_bry.length > 0)
				xtn_root = Bld_root_nde(full_bfr, lst_page_regy, ctx, wiki, page_bry);	// NOTE: this effectively reparses page twice; needed b/c of "if {| : ; # *, auto add new_line" which can build different tokens
		} finally {
			full_bfr.Mkr_rls();
		}
	}

	private byte[] Bld_wikitext(Bry_bfr bfr, Xop_ctx ctx) {
		if (level> 0) {
                    Bry_bfr tmp_bfr = Bry_bfr_.New();
			tmp_bfr.Add(qualitystart);
			tmp_bfr.Add_byte(level);
			tmp_bfr.Add(qualityend);

			bfr.Add(divopen);
			bfr.Add_byte(level);
			bfr.Add(divtween);
			bfr.Add(ctx.Wiki().Msg_mgr().Val_by_key_args(tmp_bfr.To_bry_and_clear_and_rls()));
			bfr.Add(divclose);
                        // from title extract name up to / (if any)
                        // if .djvu/ then build the getter else?
                        // strictly should be width of 1024px (but this is supposedly offline copy - keep small)
                        Xoa_ttl ttl = ctx.Page().Ttl();
                        byte[] base = ttl.Base_txt();
                        int len = base.length;
                        if (len > 5 && base[len-5] == '.' && base[len-1] == 'u') {
                        bfr.Add_str_a7("<div>[[File:");
                        bfr.Add(base);
                        bfr.Add_str_a7("|page=");
                        bfr.Add(ttl.Leaf_txt());
                        bfr.Add_str_a7("|frameless|class=ws-cover]]</div>");
                        }
		}
		return bfr.To_bry_and_clear();
	}
	// repeated from Pp_pagelist_nde.java
	private Xop_root_tkn Bld_root_nde(Bry_bfr page_bfr, Hash_adp_bry lst_page_regy, Xop_ctx ctx, Xowe_wiki wiki, byte[] wikitext) {
		Xop_ctx tmp_ctx = Xop_ctx.New__sub__reuse_lst(wiki, ctx, lst_page_regy);
		tmp_ctx.Page().Ttl_(ctx.Page().Ttl());					// NOTE: must set tmp_ctx.Ttl to ctx.Ttl; EX: Flatland and First World; DATE:2013-04-29
		tmp_ctx.Lnki().File_logger_(Xop_file_logger_.Noop);	// NOTE: set file_wkr to null, else items will be double-counted
		tmp_ctx.Parse_tid_(Xop_parser_tid_.Tid__defn);
		Xop_parser tmp_parser = Xop_parser.new_(wiki, wiki.Parser_mgr().Main().Tmpl_lxr_mgr(), wiki.Parser_mgr().Main().Wtxt_lxr_mgr());
		Xop_root_tkn rv = tmp_ctx.Tkn_mkr().Root(wikitext);
		tmp_parser.Parse_text_to_wdom(rv, tmp_ctx, tmp_ctx.Tkn_mkr(), wikitext, Xop_parser_.Doc_bgn_bos);
		return rv;
	}

	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
//		if (level> 0) {
//			Bry_bfr tmp_bfr = Bry_bfr_.New();
//			tmp_bfr.Add(qualitystart);
//			tmp_bfr.Add_byte(level);
//			tmp_bfr.Add(qualityend);
//
//			bfr.Add(divopen);
//			bfr.Add_byte(level);
//			bfr.Add(divtween);
//			bfr.Add(ctx.Wiki().Msg_mgr().Val_by_key_args(tmp_bfr.To_bry_and_clear_and_rls()));
//			bfr.Add(divclose);
//		}
			if (xtn_root == null) return;	// xtn_root is null when Xtn_parse exits early; occurs for recursion; DATE:2014-05-21
			html_wtr.Write_tkn_to_html(bfr, ctx, hctx, xtn_root.Root_src(), xnde, Xoh_html_wtr.Sub_idx_null, xtn_root);
                        bfr.Add_str_a7_null("<div class=\"pagetext\"><div class=\"mw-parser-output\">");
                        // set a flag (shomehow/somewhere?)
	}


	private byte level;

	private void Parse_line(byte[] src, int src_bgn, int src_end) {
		level = 0;
		int line_bgn = src_bgn;
		while (line_bgn < src_end) {
			int eq_pos = Bry_find_.Find_fwd(src, Byte_ascii.Eq, line_bgn, src_end);
			if (eq_pos == Bry_find_.Not_found)
				break;
			// skip any leading whitespace
			int term_bgn = Bry_find_.Find_fwd_while_space_or_tab(src, line_bgn, src_end);
			int term_end = Bry_find_.Trim_bwd_space_tab(src, eq_pos, term_bgn);
			Lowercase(src, term_bgn, term_end);
			// move past '=' and skip whitespace
			eq_pos = Bry_find_.Find_fwd_while_space_or_tab(src, eq_pos+1, src_end);
			byte c = src[eq_pos];
			if (c == Byte_ascii.Quote || c == Byte_ascii.Apos) {
				int cq_pos = Bry_find_.Find_fwd(src, c, eq_pos+1, src_end);
				if (cq_pos != Bry_find_.Not_found) {
					if (cq_pos - eq_pos - 1 == 1)
						level = src[eq_pos+1];
				}
			}
			line_bgn = eq_pos;
		}
	}

	private void Lowercase(byte[] src, int src_bgn, int src_end) {
		for (int i = src_bgn; i < src_end; i++) {
			byte b = src[i];
			if (b > 64 && b < 91)
				src[i] += 32;	// lowercase
		}
	}

	private static byte[]
	  pagequality_level = Bry_.new_a7("level")
	, divopen = Bry_.new_a7("<div class=\"prp-page-qualityheader quality")
	, divtween = Bry_.new_a7("\">")
	, divclose = Bry_.new_a7("</div>")
	, qualitystart = Bry_.new_a7("proofreadpage_quality")
	, qualityend = Bry_.new_a7("_message")
	;
}
