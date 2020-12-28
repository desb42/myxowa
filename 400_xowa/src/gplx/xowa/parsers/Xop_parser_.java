/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2021-2020 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.parsers;

import gplx.Byte_ascii;
import gplx.Bry_bfr;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.htmls.core.htmls.Xoh_wtr_ctx;

public class Xop_parser_ {
	public static final int Doc_bgn_bos = -1, Doc_bgn_char_0 = 0;
	public static byte[] Parse_text_to_html(Xowe_wiki wiki, Xop_ctx owner_ctx, Xoae_page page, byte[] src, boolean para_enabled) {
		return Parse_text_to_html(wiki, owner_ctx, Xoh_wtr_ctx.Basic, page, src, para_enabled);
	}
	public static byte[] Parse_text_to_html(Xowe_wiki wiki, Xop_ctx owner_ctx, Xoh_wtr_ctx hctx, Xoae_page page, byte[] src, boolean para_enabled) {	// NOTE: must pass in same page instance; do not do Xoa_page_.new_(), else img_idx will get reset to 0; DATE:2015-02-08
		// init
		Xop_ctx ctx = Xop_ctx.New__sub(wiki, owner_ctx, page);
		Xop_tkn_mkr tkn_mkr = ctx.Tkn_mkr();
		Xop_root_tkn root = tkn_mkr.Root(src);
		Xop_parser parser = wiki.Parser_mgr().Main();

		// expand template; EX: {{A}} -> wikitext
		byte[] wtxt = parser.Expand_tmpl(root, ctx, tkn_mkr, src);

		// parse wikitext
		root.Reset();
		ctx.Para().Enabled_(para_enabled);
		parser.Parse_wtxt_to_wdom(root, ctx, ctx.Tkn_mkr(), wtxt, Xop_parser_.Doc_bgn_bos);

		// write html
		Bry_bfr bfr = wiki.Utl__bfr_mkr().Get_b512();
		wiki.Html_mgr().Html_wtr().Write_doc(bfr, ctx, hctx, wtxt, root);
		return bfr.To_bry_and_rls();
	}
	public static boolean Valid_word_break(Xop_ctx ctx, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (bgn_pos == Xop_parser_.Doc_bgn_char_0) return true;	// starts at 0; always true
		int prv_pos = bgn_pos - 1; 
		byte prv_byte = src[prv_pos];
		switch (prv_byte) {
			case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
			case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
			case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
			case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
			case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
			case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
			case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
			case Byte_ascii.Ltr_a: case Byte_ascii.Ltr_b: case Byte_ascii.Ltr_c: case Byte_ascii.Ltr_d: case Byte_ascii.Ltr_e:
			case Byte_ascii.Ltr_f: case Byte_ascii.Ltr_g: case Byte_ascii.Ltr_h: case Byte_ascii.Ltr_i: case Byte_ascii.Ltr_j:
			case Byte_ascii.Ltr_k: case Byte_ascii.Ltr_l: case Byte_ascii.Ltr_m: case Byte_ascii.Ltr_n: case Byte_ascii.Ltr_o:
			case Byte_ascii.Ltr_p: case Byte_ascii.Ltr_q: case Byte_ascii.Ltr_r: case Byte_ascii.Ltr_s: case Byte_ascii.Ltr_t:
			case Byte_ascii.Ltr_u: case Byte_ascii.Ltr_v: case Byte_ascii.Ltr_w: case Byte_ascii.Ltr_x: case Byte_ascii.Ltr_y: case Byte_ascii.Ltr_z:
				return false;	// alpha-numerical is invalid; EX: "titel:" should not generate a lnke for "tel:"
		}
		if (prv_byte >= Byte_ascii.Ascii_min && prv_byte <= Byte_ascii.Ascii_max) return true;	// consider all other ASCII chars as true; EX: \t\n !, etc; 
		prv_pos = gplx.core.intls.Utf8_.Get_prv_char_pos0_old(src, prv_pos);
		prv_byte = src[prv_pos];
		boolean prv_char_is_letter = ctx.Lang().Case_mgr().Match_any_exists(prv_byte, src, prv_pos, bgn_pos);
		return !prv_char_is_letter;
	}
}
