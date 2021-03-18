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
package gplx.xowa.xtns.pfuncs.pages; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.langs.kwds.*; import gplx.xowa.langs.cases.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Pfunc_displaytitle extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_page_displaytitle;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_displaytitle().Name_(name);}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) {
		byte[] val_dat_ary = Eval_argx(ctx, src, caller, self);
		Xowe_wiki wiki = ctx.Wiki(); Xop_parser parser = wiki.Parser_mgr().Main();
		Xop_ctx display_ttl_ctx = Xop_ctx.New__sub__reuse_page(ctx);
		Xop_root_tkn display_ttl_root = parser.Parse_text_to_wdom(display_ttl_ctx, val_dat_ary, false);
		Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_b512();
		boolean restrict = wiki.Cfg_parser().Display_title_restrict();
		Xoh_wtr_ctx hctx = restrict ? Xoh_wtr_ctx.Display_title : Xoh_wtr_ctx.Basic;	// restrict removes certain HTML (display:none)
		wiki.Html_mgr().Html_wtr().Write_tkn_to_html(tmp_bfr, display_ttl_ctx, hctx, display_ttl_root.Data_mid(), display_ttl_root, 0, display_ttl_root);
		byte[] val_html = tmp_bfr.To_bry_and_clear();
		if (restrict) {	// restrict only allows displayTitles which have text similar to the pageTitle; PAGE:de.b:Kochbuch/_Druckversion; DATE:2014-08-18
			Xoae_page page = ctx.Page();
			wiki.Html_mgr().Html_wtr().Write_tkn_to_html(tmp_bfr, display_ttl_ctx, Xoh_wtr_ctx.Alt, display_ttl_root.Data_mid(), display_ttl_root, 0, display_ttl_root);
			byte[] val_html_lc = tmp_bfr.To_bry_and_clear();
			Xol_case_mgr case_mgr = wiki.Lang().Case_mgr();
			val_html_lc = Standardize_displaytitle_text(case_mgr, val_html_lc);
			byte[] page_ttl_lc = Standardize_displaytitle_text(case_mgr, page.Ttl().Full_db()); // NOTE: must be .Full_db() to handle non-main ns; PAGE:en.w:Template:Infobox_opera; ISSUE#:277 DATE:2018-11-14;
			if (!Bry_.Eq(val_html_lc, page_ttl_lc)) {
				Xoa_app_.Usr_dlg().Warn_many("", "", "DISPLAYTITLE fail:~{0} not ~{1}", val_html_lc, page_ttl_lc);
				val_html = null;
			}
		}
		ctx.Page().Html_data().Display_ttl_(val_html);
		tmp_bfr.Mkr_rls();
	}
	private static byte[] Standardize_displaytitle_text(Xol_case_mgr case_mgr, byte[] val) {
		byte[] rv = case_mgr.Case_build_lower(val);							// lower-case
		rv = Replaceamp(rv);
		return Bry_.Replace(rv, Byte_ascii.Space, Byte_ascii.Underline);	// force underline; PAGE:de.w:Mod_qos DATE:2014-11-06
	}
	// replace &quot; and &amp;
	// cope with
	//  {{DISPLAYTITLE:''Rolling Stone Argentina''{{'}}s The 100 Greatest Albums of National Rock}} ie &#39;
	//  {{DISPLAYTITLE:List of ''Red vs. Blue'' episodes  (''The Chorus Trilogy'')}} ie double space
	//  {{Infobox ship begin| display title = SS ''De Klerk ''}} ie trailing space
	//  {{DISPLAYTITLE:&thinsp;''Gonimara''}} ie treat as space [[%C3%97_Gonimara]] - but still does not work
	// how to cope with {{DISPLAYTITLE:''Sleepless in __________''}} this will elimiminate trailing space
	// how to cope with {{DISPLAYTITLE:''N''-Isopropyl-''N'''-phenyl-1,4-phenylenediamine}} wxtra apost
	private static byte[] Replaceamp(byte[] src) {
		int len = src.length;
		int pos = 0;
		int sofar = 0;
		Bry_bfr bfr = null;
		// trim trailing space
		while (len > 0) {
			byte b = src[len-1];
			if (b == ' ' || b == '\t')
				len--;
			else
				break;
		}
		if (len < src.length)
			bfr = Bry_bfr_.New();

		while (pos < len) {
			byte b = src[pos++];
			if (b == '&') {
				int size = -1;
				byte rb = 0;
				if (pos + 7 < len && src[pos] == 't' && src[pos+1] == 'h' && src[pos+2] == 'i' && src[pos+3] == 'n' && src[pos+4] == 's' && src[pos+5] == 'p' && src[pos+6] == ';') {
					size = 8;
					rb = Byte_ascii.Space;
				}
				else if (pos + 4 < len && src[pos] == 'q' && src[pos+1] == 'u' && src[pos+2] == 'o' && src[pos+3] == 't' && src[pos+4] == ';') {
					size = 5;
					rb = Byte_ascii.Quote;
				}
				else if (pos + 3 < len) {
					if (src[pos] == 'a' && src[pos+1] == 'm' && src[pos+2] == 'p' && src[pos+3] == ';') {
						size = 4;
						rb = Byte_ascii.Amp;
					}
					else if (src[pos] == '#' && src[pos+1] == '3' && src[pos+2] == '9' && src[pos+3] == ';') {
						size = 4;
						rb = Byte_ascii.Apos;
					}
					else if (src[pos] == '#' && src[pos+1] == '3' && src[pos+2] == '2' && src[pos+3] == ';') {
						size = 4;
						rb = Byte_ascii.Space;
					}
				}
				else if (pos + 2 < len && src[pos] == 'l' && src[pos+1] == 't' && src[pos+2] == ';') {
					int close = pos;
					while (close < len) {
						byte c = src[close++];
						if (c == '&' && close + 2 < len && src[close] == 'g' && src[close+1] == 't' && src[close+2] == ';') {
							break;
						}
					}
					if (close < len) {
						size = close - pos + 3; //???
						rb = 0;
					}
				}
				if (size > 0) {
					if (bfr == null)
						bfr = Bry_bfr_.New();
					bfr.Add_mid(src, sofar, pos - 1);
					if (rb != 0)
						bfr.Add_byte(rb);
					pos += size;
					sofar = pos;
				}
			}
			else if (b == ' ' || b == '\t') {
				// also any number of spaces to a single space
				int spacepos = pos;
				while (spacepos < len) {
					b = src[spacepos];
					if (b != ' ' && b != '\t')
						break;
					else
						spacepos++;
				}
				if (spacepos > pos) {
					if (bfr == null)
						bfr = Bry_bfr_.New();
					bfr.Add_mid(src, sofar, pos - 1);
					bfr.Add_byte(Byte_ascii.Space);
					pos = spacepos;
					sofar = pos;
				}
			}
		}
		if (bfr != null) {
			bfr.Add_mid(src, sofar, len);
			return bfr.To_bry();
		}
		else
			return src;
	}
	public static final    Pfunc_displaytitle Instance = new Pfunc_displaytitle(); Pfunc_displaytitle() {}
}	
