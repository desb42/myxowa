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
package gplx.xowa.xtns.syntax_highlights; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.langs.htmls.*; import gplx.xowa.htmls.*;
class Synh_xtn_nde_ {
	public static void Make(Bry_bfr bfr, Xoae_app app, byte[] src, int src_bgn, int src_end, byte[] lang, boolean inline, byte[] style, boolean line_enabled, int start, Int_rng_mgr highlight_idxs, byte[] klass, byte[] id, byte[] dir) {
		if (dir == null || dir.length != 3 || dir[0] != 'r' || dir[1] != 't' || dir[2] != 'l')
			dir = Bry__ltr;
		if (inline) {	// put in <code>
			bfr.Add(Bry__code_bgn);
			addhtmlattrs(bfr, lang, style, klass, id, dir, line_enabled);
		}
		else {
			bfr.Add(Bry__div_bgn);
			addhtmlattrs(bfr, lang, style, klass, id, dir, line_enabled);
			Gfh_tag_.Bld_lhs_bgn(bfr, Gfh_tag_.Id__pre);
			Gfh_atr_.Add(bfr, Gfh_atr_.Bry__style, Bry__style__overflow__auto);
			if (Bry_.Len_gt_0(lang)) {
				Gfh_atr_.Add(bfr, Gfh_atr_.Bry__class, Bry_.Add(Bry__pretty_print, lang));
			}
			Gfh_tag_.Bld_lhs_end_nde(bfr);
		}
		int text_bgn = src_bgn;
		int text_end = Bry_find_.Find_bwd_while(src, src_end, -1, Byte_ascii.Space) + 1; // trim space from end; PAGE:en.w:Comment_(computer_programming) DATE:2014-06-23
		if (line_enabled || highlight_idxs != Int_rng_mgr_null.Instance) { // NOTE: if "highlight" specified without "line" highlight_idxs will not be null instance; add '<span style="background-color: #FFFFCC;">' below; ISSUE#:498; DATE:2019-06-22
			bfr.Add_byte_nl();
			byte[][] lines = Bry_split_.Split_lines(Bry_.Mid(src, text_bgn, text_end));
			int lines_len = lines.length;
			int line_idx = start;
			int line_end = (line_idx + lines_len) - 1; // EX: line_idx=9 line_len=1; line_end=9
			int digits_max = Int_.DigitCount(line_end);
			for (int i = 0; i < lines_len; i++) {
				byte[] line = lines[i]; if (i == 0 && Bry_.Len_eq_0(line)) continue;
				if (line_enabled) { // add '<span style="-moz-user-select:none;">1 </span>' if "line" is enabled
					bfr.Add(Xoh_consts.Span_bgn_open).Add(Xoh_consts.Style_atr).Add(Style_line).Add(Xoh_consts.__end_quote);
					int pad = digits_max - Int_.DigitCount(line_idx);
					if (pad > 0) bfr.Add_byte_repeat(Byte_ascii.Space, pad);
					bfr.Add_int_variable(line_idx++).Add_byte(Byte_ascii.Space);
					bfr.Add(Xoh_consts.Span_end);
				}
				bfr.Add(Xoh_consts.Span_bgn_open);
				if (highlight_idxs.Match(i))
					bfr.Add(Xoh_consts.Style_atr).Add(Style_highlight).Add(Xoh_consts.__end_quote);
				else
					bfr.Add(Xoh_consts.__end);
				Xox_mgr_base.Xtn_write_escape(app, bfr, line);
				bfr.Add(Xoh_consts.Span_end);
				bfr.Add_byte_nl();
			}
		}
		else
			Xox_mgr_base.Xtn_write_escape_pre(app, bfr, src, text_bgn, text_end);
		if (inline) {
			bfr.Add(Xoh_consts.Code_end);
		}
		else {
			bfr.Add(Xoh_consts.Pre_end);
			bfr.Add(Gfh_bldr_.Bry__div_rhs);
		}
	}
	private static void addhtmlattrs(Bry_bfr bfr, byte[] lang, byte[] style, byte[] klass, byte[] id, byte[] dir, boolean line_enabled) {
		if (klass != null) bfr.Add(klass).Add_byte(Byte_ascii.Space);
		bfr.Add(Bry__mw_highlight).Add_byte(Byte_ascii.Space);
		if (lang != null) bfr.Add(Bry__mw_highlight).Add(Bry__lang).Add(lang).Add_byte(Byte_ascii.Space);
		bfr.Add(Bry__content).Add(dir).Add_byte(Byte_ascii.Space);
		if (line_enabled) bfr.Add(Bry__mw_highlight).Add(Bry__lines).Add_byte(Byte_ascii.Space);
		bfr.Add_byte(Byte_ascii.Quote).Add_byte(Byte_ascii.Space);
		bfr.Add(Bry__dir).Add(dir).Add_byte(Byte_ascii.Quote).Add_byte(Byte_ascii.Space);
		if (style != null) bfr.Add(Xoh_consts.Style_atr).Add(style).Add_byte(Byte_ascii.Quote);
		bfr.Add_byte(Byte_ascii.Gt);
	}

	private static final    byte[] 
	  Enclose_none = Bry_.new_a7("none")
	, Style_line = Bry_.new_a7("-moz-user-select:none;"), Style_highlight = Bry_.new_a7("background-color: #FFFFCC;")
	, Bry__style__overflow__auto = Bry_.new_a7("overflow:auto")
	, Bry__pretty_print = Bry_.new_a7("prettyprint lang-")
	, Bry__div_bgn = Bry_.new_a7("<div class=\"mw-code ")
	, Bry__code_bgn = Bry_.new_a7("<code class=\"")
	, Bry__mw_highlight = Bry_.new_a7("mw-highlight")
	, Bry__lang = Bry_.new_a7("-lang-")
	, Bry__content = Bry_.new_a7("mw-content-")
	, Bry__lines = Bry_.new_a7("-lines")
	, Bry__dir = Bry_.new_a7("dir=\"")
	, Bry__ltr = Bry_.new_a7("ltr")
	;
}
