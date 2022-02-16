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
import gplx.xowa.parsers.amps.*;
import gplx.core.btries.*;
import gplx.langs.htmls.entitys.*;
public class Pfunc_displaytitle extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_page_displaytitle;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_displaytitle().Name_(name);}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) {
		if (ctx.Page().Html_data().Display_ttl() != null) return; // only once
		byte[] val_dat_ary = /*remove_slash(*/Eval_argx(ctx, src, caller, self)/*)*/;
		Xowe_wiki wiki = ctx.Wiki(); Xop_parser parser = wiki.Parser_mgr().Main();
		Xop_amp_mgr amp_mgr = wiki.App().Parser_amp_mgr();
		Xop_ctx display_ttl_ctx = Xop_ctx.New__sub__reuse_page(ctx);
		Xop_root_tkn display_ttl_root = parser.Parse_text_to_wdom(display_ttl_ctx, val_dat_ary, false);
		Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_b512();
		boolean restrict = wiki.Cfg_parser().Display_title_restrict();
		Xoh_wtr_ctx hctx = restrict ? Xoh_wtr_ctx.Display_title : Xoh_wtr_ctx.Basic;	// restrict removes certain HTML (display:none)
		wiki.Html_mgr().Html_wtr().Write_tkn_to_html(tmp_bfr, display_ttl_ctx, hctx, display_ttl_root.Data_mid(), display_ttl_root, 0, display_ttl_root);
		byte[] val_html = removesupref(tmp_bfr.To_bry_and_clear());
		if (restrict) {	// restrict only allows displayTitles which have text similar to the pageTitle; PAGE:de.b:Kochbuch/_Druckversion; DATE:2014-08-18
			Xoae_page page = ctx.Page();
			wiki.Html_mgr().Html_wtr().Write_tkn_to_html(tmp_bfr, display_ttl_ctx, Xoh_wtr_ctx.Alt, display_ttl_root.Data_mid(), display_ttl_root, 0, display_ttl_root);
			byte[] val_html_lc = tmp_bfr.To_bry_and_clear();
			Xol_case_mgr case_mgr = wiki.Lang().Case_mgr();
			val_html_lc = Standardize_displaytitle_text(case_mgr, val_html_lc, Bool_.Y, amp_mgr);
			byte[] page_ttl_lc = Standardize_displaytitle_text(case_mgr, page.Ttl().Full_txt(), Bool_.Y, amp_mgr);
			if (!Bry_.Eq(val_html_lc, page_ttl_lc)) {
				Xoa_app_.Usr_dlg().Warn_many("", "", "DISPLAYTITLE fail:~{0} not ~{1}", val_html_lc, page_ttl_lc);
				val_html = null;
			}
		}
		ctx.Page().Html_data().Display_ttl_(val_html);
		tmp_bfr.Mkr_rls();
	}
	private static byte[] Standardize_displaytitle_text(Xol_case_mgr case_mgr, byte[] val, boolean change_underscore, Xop_amp_mgr amp_mgr) {
		byte[] rv = Replaceamp(val, change_underscore, amp_mgr);
		rv = case_mgr.Case_build_lower(rv);							// lower-case
		if (rv.length > 1 && rv[0] == -50 && rv[1] == -68) { // U+03BC	µ	ce bc	GREEK SMALL LETTER MU
			rv[0] = -62; // U+00B5	µ	c2 b5	MICRO SIGN
			rv[1] = -75; // change from GREEK SMALL LETTER MU to MICRO SIGN
		}
		return Bry_.Replace(rv, Byte_ascii.Space, Byte_ascii.Underline);	// force underline; PAGE:de.w:Mod_qos DATE:2014-11-06
	}
	// replace &quot; and &amp;
	// cope with
	//  {{DISPLAYTITLE:''Rolling Stone Argentina''{{'}}s The 100 Greatest Albums of National Rock}} ie &#39;
	//  {{DISPLAYTITLE:List of ''Red vs. Blue'' episodes  (''The Chorus Trilogy'')}} ie double space
	//  {{Infobox ship begin| display title = SS ''De Klerk ''}} ie trailing space
	//  {{DISPLAYTITLE:&thinsp;''Gonimara''}} ie treat as space [[%C3%97_Gonimara]] - but still does not work
	//  {{DISPLAYTITLE:''iTunes Originals&nbsp;? R.E.M.''}} &nsbp; goes to &#160; and then should be space
	// how to cope with {{DISPLAYTITLE:''Sleepless in __________''}} this will elimiminate trailing space
	// how to cope with {{DISPLAYTITLE:''N''-Isopropyl-''N'''-phenyl-1,4-phenylenediamine}} wxtra apost
	private static byte[] Replaceamp(byte[] src, boolean change_underscore, Xop_amp_mgr amp_mgr) {
		int len = src.length;
		int pos = 0;
		int sofar = 0;
		Bry_bfr bfr = null;
		Btrie_slim_mgr amp_trie = amp_mgr.Amp_trie();
		Btrie_rv trv = new Btrie_rv();
		byte[] b_ary = null;
		byte rb = 0;
		// trim trailing space
		while (len > 0) {
			byte b = src[len-1];
			if (b == ' ' || b == '\t' || b == '\n')
				len--;
			else
				break;
		}
		if (len < src.length)
			bfr = Bry_bfr_.New();

		while (pos < len) {
			byte b = src[pos++];
			int size = -1;
			switch (b) {
				case '&':
					// &amp;
					// &lt;
					// &quot;
					// &tinysp;
					Object html_ent_obj = amp_trie.Match_at(trv, src, pos, len);
					if (html_ent_obj != null) {
						Gfh_entity_itm amp_itm = (Gfh_entity_itm)html_ent_obj;
						size = trv.Pos() - pos;
						if (amp_itm.Tid() == Gfh_entity_itm.Tid_name_std) {
							switch (amp_itm.Char_int()) {
								case 160:
									rb = Byte_ascii.Space;
									break;
								case Byte_ascii.Amp:
									b_ary = Byte_ascii.Amp_bry;
									break;
								case Byte_ascii.Quote:
									rb = Byte_ascii.Quote;
									break;
								case Gfh_entity_itm.Char_int_null:	// &#xx;
									b_ary = amp_itm.Xml_name_bry();
									break;
								case Byte_ascii.Lt:
								//case Byte_ascii.Gt:
									int close = trv.Pos();
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
									break;
								default:
									b_ary = specials(amp_itm.Char_int());
									if (b_ary == null)
										b_ary = amp_itm.U8_bry();
									break;
							}
						}
						else {
							Xop_amp_mgr_rslt amp_rv = new Xop_amp_mgr_rslt();
							amp_mgr.Parse_ncr(amp_rv, amp_itm.Tid() == Gfh_entity_itm.Tid_num_hex, src, len, pos, trv.Pos());
							if (amp_rv.Pass()) {
								int val = amp_rv.Val();
								b_ary = specials(val);
							}
							size = amp_rv.Pos() - pos; // assume the semicolon?!
						}
					}
					break;
/*						case 't':
								if (pos + 5 < len && src[cpos] == 'h' && src[cpos+1] == 'i' && src[cpos+2] == 'n' && src[cpos+3] == 's' && src[cpos+4] == 'p' && src[cpos+5] == ';') {
									size = 7;
									rb = Byte_ascii.Space;
								}
								break;
						}
					}
					break;
*/
				case ' ':
				case '\t':
				case '_':
					if (b == '_' && !change_underscore)
						break;
					// also any number of spaces to a single space (drop leading and trailing)
					int spacepos = pos;
					while (spacepos < len) {
						b = src[spacepos];
						if (change_underscore && b == '_')
							b = ' ';
						if (b != ' ' && b != '\t')
							break;
						else
							spacepos++;
					}
					if (pos == 1 || spacepos == len) { // ignore leading space(s)/ trailing
						size = spacepos - pos;
						break;
					}
					else {
						// could have got here with a previous space (ie %c2%a0)
						if (sofar == pos-1 && bfr != null && bfr.Len() > 1 && bfr.Bfr()[bfr.Len()-1] == ' ') {
							sofar = spacepos;
							break;
						}
						if (spacepos > pos) {
							size = spacepos - pos;
							rb = Byte_ascii.Space;
						}
					}
					break;
				case ':': // remove excess colons?
					// and following space??? if a real namespace???
//					if (src[pos] == ' ') {
//						size = 1;
//						break;
//					}
					// fall through
				case '\'': // remove excess single quotes
				case '/': // remove excess slash
					size = 0;
					break;
				case '<': // tags - strip tag (or if <ref> find the close </ref>
 					int close = pos;
					while (close < len) {
						byte c = src[close++];
						if (c == '>') {
							break;
						}
					}
					if (close < len) {
						size = close - pos;
						if (size > 5) {
							if (src[pos] == 'r' && src[pos+1] == 'e' && src[pos+2] == 'f') {
								byte c = src[pos+3];
								if ((c == ' ' && src[close-2] != '/') || c == '>') {
									// find </ref>
									while (close < len) {
										c = src[close++];
										if (c != '<') {
											if (src[close] == '/' && src[close+1] == 'r' && src[close+2] == 'e' && src[close+3] == 'f' && src[close+4] == '>') {
												size = close + 5 - pos;
												break;
											}
										}
									}
								}
							}
						}
					}
					break;
				case '{':
					if (src[pos] == '{') {
	 					close = pos;
						while (close < len) {
							byte c = src[close++];
							if (c == '}' && src[close] == '}') {
								close++;
								break;
							}
						}
						size = close - pos;
					}
				case -30: // \xE2
					if (pos + 1 < len && src[pos] == -128 && src[pos+1] == -109) { // \xE2\x80\x93 &ndash;
						size = 2;
						rb = Byte_ascii.Dash;
					}
					else if (pos + 1 < len && src[pos] == -120 && src[pos+1] == -110) { // \xE2\x88\x92
						size = 2;
						rb = Byte_ascii.Dash;
					}
					else if (pos + 1 < len && src[pos] == -103 && src[pos+1] == -83) { // \xE2\x99\xAD
						size = 2;
						rb = Byte_ascii.Dash;
					}
					break;
//				case -49: // \xCF
//					if (pos + 1 < len && src[pos] == -128) { // \xCF\x80 lowercase PI
//						size = 1;
//						rb = Byte_ascii.Dash;
//					}
//					break;
				case -62: // \xC2
					if (pos + 1 < len && src[pos] == -96) { // \xC2\xA0 -> &nbsp; -> space
						size = 1;
						rb = Byte_ascii.Space;
					}
					break;
			}
			if (size >= 0) {
				if (bfr == null)
					bfr = Bry_bfr_.New();
				bfr.Add_mid(src, sofar, pos - 1);
				if (rb != 0) {
					bfr.Add_byte(rb);
					rb = 0;
				}
				if (b_ary != null) {
					bfr.Add(b_ary);
					b_ary = null;
				}
				pos += size;
				sofar = pos;
			}
		}
		if (bfr != null) {
			bfr.Add_mid(src, sofar, len);
			return bfr.To_bry();
		}
		else
			return src;
	}
	private byte[] remove_slash(byte[] src) {
		int len = src.length;
		int pos = 0;
		int sofar = 0;
		Bry_bfr bfr = null;
		while (pos < len) {
			byte b = src[pos++];
			if (b == '/') {
				if (bfr == null)
					bfr = Bry_bfr_.New();
				bfr.Add_mid(src, sofar, pos - 1);
				sofar = pos;
			}
		}
		if (bfr != null) {
			bfr.Add_mid(src, sofar, len);
			return bfr.To_bry();
		}
		else
			return src;
	}
	// &#160;
	// &#32;
	// &#34; -> '"'
	// &#38; -> '&'
	// &#39;
	// &#8201; (&thinsp) -> ' '
	// &#8202; (Hair Space) -> ' '
	// &#8204; (Zero Width Non-Joiner) -> ''
	// &#8206; (Left-To-Right Mark) -> ''
	// &#8207; (Right-To-Left Mark) -> ''
	// &#8211; (&ndash;) -> '-'
	// &#8214; (&mdash;) -> '-'
	// &#8722; (&minus;) -> '-'
	// &#928; - Capital PI -> '-' ???20210815
	// &#x266d;, 0xE2 0x99 0xAD, &#9837;(music flat) -> '-'
	private static byte[] specials(int val) {
		byte rb = 0;
		byte[] b_ary = null;
		switch(val) {
			case 160: case 32:
			case 8201: // (&thinsp) -> ' '
			case 8202: // (Hair Space) -> ' '
				rb = Byte_ascii.Space;
				break;
			case 8211: // (&ndash;) -> '-'
			case 8214: // (&mdash;) -> '-'
			case 8722: // (&minus;) -> '-'
			//case 928: // - Capital PI -> '-' ???20210815
			case 9837: // (music flat) -> '-'
				rb = Byte_ascii.Dash;
				break;
			case 34: // &#34; -> "
				rb = Byte_ascii.Quote;
				break;
			case 38: // &#38; -> &
				rb = Byte_ascii.Amp;
				break;
			case 39: // &#39; -> ' - ignore
			case 8204: // (Zero Width Non-Joiner) -> ''
			case 8206: // (Left-To-Right Mark) -> ''
			case 8207: // (Right-To-Left Mark) -> ''
				break;
			default:
				b_ary = gplx.core.intls.Utf16_.Encode_int_to_bry(val);
				break;
		}
		if (rb != 0) {
			b_ary = new byte[1];
			b_ary[0] = rb;
		}
		return b_ary;
	}
	public static final    Pfunc_displaytitle Instance = new Pfunc_displaytitle(); Pfunc_displaytitle() {}

	private static byte[] removesupref(byte[] src) {
		int len = src.length;
		int pos = 0;
		int sofar = 0;
		Bry_bfr bfr = null;
		while (pos < len) {
			byte b = src[pos++];
			if (b == '<') {
				if (pos + 5 < len && src[pos] == 's' && src[pos+1] == 'u' && src[pos+2] == 'p' && src[pos+3] == ' ') {
					if (bfr == null)
						bfr = Bry_bfr_.New();
					bfr.Add_mid(src, sofar, pos - 1);
					// find </sub>
					while (pos < len) {
						b = src[pos++];
						if (b == '<') {
							if (src[pos] == '/' && src[pos+1] == 's' && src[pos+2] == 'u' && src[pos+3] == 'p' && src[pos+4] == '>') {
								pos += 5;
								sofar = pos;
								break;
							}
						}
					}
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
}
