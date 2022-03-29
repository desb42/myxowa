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
package gplx.xowa.parsers.utils; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.langs.htmls.*; import gplx.langs.htmls.encoders.*;import gplx.langs.jsons.Json_doc;
import gplx.langs.jsons.Json_nde;
import gplx.langs.jsons.Json_parser;
 import gplx.xowa.htmls.*; import gplx.xowa.htmls.hrefs.*; import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.msgs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.wikis.pages.redirects.*;
public class Xop_redirect_mgr {
	private final    Xowe_wiki wiki; private final    Gfo_url_encoder url_decoder; private Hash_adp_bry redirect_hash;
	public Xop_redirect_mgr(Xowe_wiki wiki) {this.wiki = wiki; this.url_decoder = gplx.langs.htmls.encoders.Gfo_url_encoder_.Http_url_ttl;}	// NOTE: must be Url_ttl, not Url; PAGE:en.w:Template:Positionskarte+ -> Template:Location_map+, not Template:Location_map DATE:2014-08-21
	public void Clear() {redirect_hash = null;}	// TEST:
	public boolean Is_redirect(byte[] text, int text_len) {return this.Extract_redirect(text, text_len) != null;}
	public Xoa_ttl Extract_redirect_loop(byte[] src) {
		Xoa_ttl rv = null;
		for (int i = 0; i < Redirect_max_allowed; i++) {
			rv = Extract_redirect(src);
			if (rv != null) return rv;
		}
		return null;
	}
	public Xoa_ttl Extract_redirect(byte[] src) {
		if (src == null) return Redirect_null_ttl;
		return Extract_redirect(src, src.length);
	}
	private int ttl_bgn, ttl_end, ttl_end_lnki; // used by Adjust_redirect
	private final Json_parser jsonParser = new Json_parser();
	private static final byte[] Bry__redirect = Bry_.new_a7("redirect"); // repeated from Wbase_doc_mgr
	public Xoa_ttl Extract_redirect(byte[] src, int src_len) {	// NOTE: this proc is called by every page. be careful of changes; DATE:2014-07-05
		if (src_len < 3) return Redirect_null_ttl;
		int bgn = Bry_find_.Find_fwd_while_not_ws(src, 0, src_len);
		if (bgn == src_len) return Redirect_null_ttl; // article is entirely whitespace
		// check for json (wikidata redirect)
		if (src[bgn] == '{') {
			if (src_len - bgn < 100 && src[bgn+1] != '{' && src[bgn+1] != '|') { // only small json data
				if (bgn > 0)
					src = Bry_.Mid(src, bgn);
				Json_doc jdoc = null;
				// only real json
				try {
					jdoc = jsonParser.Parse(src);
				}
				catch (Exception e) {
					return Redirect_null_ttl;
				}
				if (jdoc != null) {
					Json_nde jdoc_root = jdoc.Root_nde();
					byte[] redirect_ttl = jdoc_root.Get_as_bry_or(Bry__redirect, null);
					if (redirect_ttl != null) {
						return Xoa_ttl.Parse(wiki, redirect_ttl);
					}
				}
			}
			else // cannot be redirect
				return Redirect_null_ttl;
		}
		int kwd_end = Xop_redirect_mgr_.Get_kwd_end_or_end(src, bgn, src_len);
		if (kwd_end == src_len) return Redirect_null_ttl;
		if (redirect_hash == null) redirect_hash = Xol_kwd_mgr.hash_(wiki.Lang().Kwd_mgr(), Xol_kwd_grp_.Id_redirect);
		Object redirect_itm = redirect_hash.Get_by_mid(src, bgn, kwd_end);
		if (redirect_itm == null)		return Redirect_null_ttl; // not a redirect kwd
		ttl_bgn = Xop_redirect_mgr_.Get_ttl_bgn_or_neg1(src, kwd_end, src_len);
		if (ttl_bgn == Bry_find_.Not_found)	return Redirect_null_ttl;
		ttl_bgn += Xop_tkn_.Lnki_bgn.length;
		ttl_end = Bry_find_.Find_fwd(src, Xop_tkn_.Lnki_end, ttl_bgn); if (ttl_end == Bry_find_.Not_found)	return Redirect_null_ttl;
                ttl_end_lnki = ttl_end;
		int pipe_pos = Bry_find_.Find_fwd(src, Byte_ascii.Pipe, ttl_bgn); 
		if (	pipe_pos != Bry_find_.Not_found	// if pipe exists; PAGE:da.w:Middelaldercentret; DATE:2015-11-06
			&&	pipe_pos < ttl_end)				// and pipe is before ]]; do not take pipe from next lnki; PAGE:en.w:Template:pp-semi; DATE:2015-11-14
			ttl_end = pipe_pos;					// end ttl at pipe
		byte[] redirect_bry = Bry_.Mid(src, ttl_bgn, ttl_end);
		redirect_bry = url_decoder.Decode(redirect_bry);	// NOTE: url-decode links; PAGE: en.w:Watcher_(Buffy_the_Vampire_Slayer); DATE:2014-08-18
		return Xoa_ttl.Parse(wiki, redirect_bry);
	}
	public void Adjust_redirect(Xowe_wiki wiki, Xoae_page page, byte[] src) { // extract #REDIRECT link and add back to code
		Xoa_ttl ttl = Extract_redirect(src);
		if (ttl == Redirect_null_ttl) return;

		byte[] html = Bry_.Add(Xop_tkn_.Lnki_bgn			// "[["
				, Bry_.Mid(src, ttl_bgn, ttl_end)				// "Page"
				, Xop_tkn_.Lnki_end			// "]]"
				);

		byte[] redir = Bld_redirect_msg_to(wiki, html);

		// consume white space after redirect
		ttl_end_lnki += 2;
		while (ttl_end_lnki < src.length) {
			byte b = src[ttl_end_lnki];
			if (b == ' ' || b == '\t' || b == '\n')
				ttl_end_lnki++;
			else
				break;
		}

		src = Bry_.Add( redir, Bry_.Mid(src, ttl_end_lnki, src.length));
		page.Db().Text().Text_bry_(src);
		// need to add $out->addModuleStyles( 'mediawiki.action.view.redirectPage' );
	}
	public static final    Xoa_ttl Extract_redirect_is_null = null;
	public static final int Redirect_max_allowed = 4;
	public static final    Xoa_ttl	Redirect_null_ttl = null;
	public static final    byte[]	Redirect_null_bry = Bry_.Empty;
	private static final    byte[] Redirect_bry = Bry_.new_a7("#REDIRECT ");
	public static byte[] Make_redirect_text(byte[] redirect_to_ttl) {
		return Bry_.Add
			(	Redirect_bry				// "#REDIRECT "
			,	Xop_tkn_.Lnki_bgn			// "[["
			,	redirect_to_ttl				// "Page"
			,	Xop_tkn_.Lnki_end			// "]]"
			);
	}
	public static byte[] Bld_redirect_msg_from(Xoae_app app, Xowe_wiki wiki, Xopg_redirect_mgr redirect_mgr) {
		// NOTE: this assumes that redirect_mgr only has redirect_src, not redirect_trg; note that #REDIRECT [[A]] only adds redirect_src, whereas special redirects add redirect_trg; DATE:2016-07-31
		int len = redirect_mgr.Itms__len(); if (len == 0) return Bry_.Empty;
		Bry_bfr redirect_bfr = wiki.Utl__bfr_mkr().Get_b512();
		boolean dirty = false;
		for (int i = 0; i < len; i++) {
			Xopg_redirect_itm redirect_itm = redirect_mgr.Itms__get_at(i);
			if (!redirect_itm.By_wikitext()) continue;	// ignore Special:Redirects else Special:Random will always show "redirected from"; DATE:2016-07-05
			dirty = true;
			if (i != 0) redirect_bfr.Add(Bry_redirect_dlm);
			byte[] display_ttl = Xoa_ttl.Replace_unders(redirect_itm.Ttl().Full_db());
			redirect_bfr.Add(Gfh_bldr_.Bry__a_lhs_w_href)	// '<a href="'
				.Add(Xoh_href_.Bry__wiki)					// '/wiki/'
				.Add(redirect_itm.Ttl().Full_db_href())     // 'PageA'
				.Add(Bry_redirect_arg)						// ?redirect=no
				.Add(Gfh_bldr_.Bry__cls__nth)               // '" class="'
				.Add_str_a7("mw-redirect")                  // mw-redirect // NOTE:MW does this differently, but for now, manually add; REF.MW:https://github.com/wikimedia/mediawiki/blob/82311f8c2c79bc469cae14e17546fd79d3541b76/includes/linker/LinkRenderer.php#L479
				.Add(Gfh_bldr_.Bry__title__nth)				// '" title="'
				.Add(display_ttl)							// 'PageA'
				.Add(Gfh_bldr_.Bry__lhs_end_head_w_quote)	// '">'
				.Add(display_ttl)							// 'PageA'
				.Add(Gfh_bldr_.Bry__a_rhs);					// </a>
		}
		if (!dirty) {
			redirect_bfr.Mkr_rls();
			return Bry_.Empty; // ignore Special:Redirects else Special:Random will always show "redirected from"; DATE:2016-07-05
		}

		// build redirectedfrom span; NOTE: same implementation as MW; ISSUE#:642; DATE:2020-01-14; REF.MW:https://github.com/wikimedia/mediawiki/blob/master/includes/page/Article.php#L1115-L1117
		Bry_bfr fmt_bfr = wiki.Utl__bfr_mkr().Get_b512();
		fmt_bfr.Add_str_a7("<span class=\"mw-redirectedfrom\">");
		byte[] redirectedfrom_val = wiki.Msg_mgr().Val_by_key_args(Key_redirectedfrom, redirect_bfr.To_bry());
		fmt_bfr.Add(redirectedfrom_val);
		fmt_bfr.Add_str_a7("</span>");

		// release bfrs
		redirect_bfr.Clear().Mkr_rls();
		fmt_bfr.Mkr_rls();
		return fmt_bfr.To_bry_and_clear();
	}
	public static byte[] Bld_redirect_msg_sub(Xoae_app app, Xowe_wiki wiki, Xopg_redirect_mgr redirect_mgr) {
		return Bry_.Add(Bry_.new_a7("<span id=\"redirectsub\">")
			, wiki.Msg_mgr().Val_by_key_args(Bry_.new_a7("redirectpagesub"))
			, Bry_.new_a7("</span>"));
	}
	public static byte[] Bld_redirect_msg_to(Xowe_wiki wiki, byte[] html) {
		return Bry_.Add(Bry_.new_a7("<div class=\"redirectMsg\"><p>")
			, wiki.Msg_mgr().Val_by_key_args(Bry_.new_a7("redirectto"))
			, Bry_.new_a7("</p><ul class=\"redirectText\"><li>")
			//, <a href="/wiki/Casa_automobilistica" title="Casa automobilistica">Casa automobilistica</a>
			, html
			, Bry_.new_a7("</li></ul></div>"));
	}
	private static byte[]
	  Bry_redirect_dlm = Bry_.new_a7(" <--- ")
	, Bry_redirect_arg = Bry_.new_a7("?redirect=no")
	, Key_redirectedfrom = Bry_.new_a7("redirectedfrom")
	;
}
class Xop_redirect_mgr_ {
	public static int Get_kwd_end_or_end(byte[] src, int bgn, int end) {	// get end of kwd
		for (int i = bgn; i < end; ++i) {
			switch (src[i]) {
				case Byte_ascii.Nl: case Byte_ascii.Space: case Byte_ascii.Tab:
				case Byte_ascii.Brack_bgn: case Byte_ascii.Colon:
					return i;	// ASSUME: kwd does not have these chars
				default:
					break;
			}
		}
		return end;
	}
	public static int Get_ttl_bgn_or_neg1(byte[] src, int bgn, int end) {	// get bgn of ttl
		boolean colon_null = true;
		for (int i = bgn; i < end; ++i) {
			switch (src[i]) {
				case Byte_ascii.Nl: case Byte_ascii.Space: case Byte_ascii.Tab: break;	// skip all ws
				case Byte_ascii.Colon: // allow 1 colon
					if (colon_null)
						colon_null = false;
					else
						return Bry_find_.Not_found;
					break;
				default:
					break;
				case Byte_ascii.Brack_bgn:
					int nxt_pos = i + 1;
					if (nxt_pos >= end) return Bry_find_.Not_found;	// [ at eos
					return src[nxt_pos] == Byte_ascii.Brack_bgn ? i : Bry_find_.Not_found;
			}
		}
		return Bry_find_.Not_found;
	}
}
