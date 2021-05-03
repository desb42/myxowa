/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2020 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.htmls.portal;

import gplx.Bool_;
import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_bfr_;
import gplx.GfoMsg;
import gplx.Gfo_invk;
import gplx.Gfo_invk_;
import gplx.GfsCtx;
import gplx.Io_url;
import gplx.String_;
import gplx.core.brys.Bfr_arg;
import gplx.core.brys.Bry_bfr_mkr;
import gplx.core.brys.fmtrs.Bry_fmtr;
import gplx.core.brys.fmtrs.Bry_fmtr_eval_mgr;
import gplx.langs.htmls.encoders.Gfo_url_encoder;
import gplx.langs.htmls.encoders.Gfo_url_encoder_;
import gplx.xowa.Xoa_page;
import gplx.xowa.Xoa_ttl;
import gplx.xowa.Xoa_url_;
import gplx.xowa.Xow_wiki;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.addons.htmls.sidebars.Xoh_sidebar_mgr;
import gplx.xowa.apps.apis.xowa.html.Xoapi_toggle_itm;
import gplx.xowa.htmls.hrefs.Xoh_href_;
import gplx.xowa.htmls.hrefs.Xoh_href_wtr;
import gplx.xowa.htmls.portal.vnts.Vnt_mnu_grp_fmtr;
import gplx.xowa.langs.Xol_lang_itm;
import gplx.xowa.langs.msgs.Xow_msg_mgr;
import gplx.xowa.langs.vnts.Xol_vnt_mgr;
import gplx.xowa.wikis.domains.Xow_domain_tid;
import gplx.xowa.wikis.domains.Xow_domain_tid_;
import gplx.xowa.wikis.nss.Xow_ns;
import gplx.xowa.wikis.nss.Xow_ns_;
import gplx.xowa.wikis.nss.Xow_ns_mgr;
import gplx.xowa.wikis.pages.Xopg_view_mode_;

import gplx.xowa.Xoae_page;
import gplx.Bry_fmt;
import gplx.List_adp;
import gplx.Byte_ascii;
import gplx.xowa.wikis.nss.Xow_ns_canonical_;
import gplx.xowa.xtns.proofreadPage.Pp_index_parser;
public class Xow_portal_mgr implements Gfo_invk {
	private Xowe_wiki wiki; private boolean lang_is_rtl; private Xoapi_toggle_itm toggle_itm;
	private final    Vnt_mnu_grp_fmtr vnt_menu_fmtr = new Vnt_mnu_grp_fmtr();
	private final    Gfo_url_encoder fsys_lnx_encoder = Gfo_url_encoder_.New__fsys_lnx().Make();
	private boolean sidebar_enabled;
	private boolean indicators_pagesource_enabled = false;
	private byte[]
	  indicators_pagesource_wtxt = Bry_.new_a7("<ul><li>WIKITEXT</li></ul>")
	, indicators_pagesource_html = Bry_.new_a7("<ul><li>HTML</li></ul>")
	;
	public Xow_portal_mgr(Xowe_wiki wiki) {
		this.wiki = wiki;
		this.sidebar_mgr = new Xoh_sidebar_mgr(wiki);
		this.missing_ns_cls = Bry_.Eq(wiki.Domain_bry(), Xow_domain_tid_.Bry__home) ? Missing_ns_cls_hide : null;	// if home wiki, set missing_ns to application default; if any other wiki, set to null; will be overriden during init
	}
	public byte[] Missing_ns_cls() {return missing_ns_cls;} public Xow_portal_mgr Missing_ns_cls_(byte[] v) {missing_ns_cls = v; return this;} private byte[] missing_ns_cls;	// NOTE: must be null due to Init check above
	public Xoh_sidebar_mgr Sidebar_mgr() {return sidebar_mgr;} private Xoh_sidebar_mgr sidebar_mgr;
	public Xow_portal_mgr Init_assert() {if (init_needed) Init(); return this;}
	public byte[] Div_jump_to() {return div_jump_to;} private byte[] div_jump_to = Bry_.Empty;
	public void Init_by_lang(Xol_lang_itm lang) {
		lang_is_rtl = !lang.Dir_ltr();
	}
	public void Init_by_wiki(Xowe_wiki wiki) {
		wiki.App().Cfg().Bind_many_wiki(this, wiki
			, Cfg__divs__footer, Cfg__missing_class
			, Cfg__sidebar_enabled__desktop, Cfg__sidebar_enabled__server
			, Cfg__hdumps_indicators_enabled, Cfg__hdumps_indicators_wtxt, Cfg__hdumps_indicators_html
			);
	}
	private void Sidebar_enabled_(boolean is_desktop, boolean val) {
		// set sidebar_enabled if (a) is_gui and is_desktop; or (b) is_server and !is_desktop
		if (wiki.App().Mode().Tid_is_gui()) {
			if (is_desktop)
				this.sidebar_enabled = val;
		}
		else {
			if (!is_desktop)
				this.sidebar_enabled = val;
		}
	}
	public void Init() {
		init_needed = false;
		if (missing_ns_cls == null)	{// if missing_ns_cls not set for wiki, use the home wiki's
			Missing_ns_cls_(wiki.Appe().Usere().Wiki().Html_mgr().Portal_mgr().Missing_ns_cls());
		}
		Bry_fmtr_eval_mgr eval_mgr = wiki.Eval_mgr();
		Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_b512();

		Init_fmtr(tmp_bfr, eval_mgr, txt_categories_fmtr);
		Init_fmtr(tmp_bfr, eval_mgr, txt_pageread_fmtr);
		Init_fmtr(tmp_bfr, eval_mgr, txt_pageedit_fmtr);
		Init_fmtr(tmp_bfr, eval_mgr, txt_pagehtml_fmtr);
		Init_fmtr(tmp_bfr, eval_mgr, txt_ns_fmtr);
		Init_fmtr(tmp_bfr, eval_mgr, txt_view_fmtr);

		// logo
		byte[] main_page_href_bry = tmp_bfr.Add(Xoh_href_.Bry__site).Add(wiki.Domain_bry()).Add(Xoh_href_.Bry__wiki).To_bry_and_clear();	// NOTE: build /site/en.wikipedia.org/wiki/ href; no Main_Page, as that will be inserted by Xoh_href_parser
		Io_url wiki_css_dir = wiki.Appe().Usere().Fsys_mgr().Wiki_root_dir().GenSubDir_nest(wiki.Domain_str(), "html");
		div_logo_day = Init_fmtr(tmp_bfr, eval_mgr, div_logo_fmtr, main_page_href_bry, fsys_lnx_encoder.Encode_to_file_protocol(wiki_css_dir.GenSubFil_nest("logo.png")));

		// night-mode logo; check if wiki-version exists, else use app-version
		Io_url night_logo = wiki.App().Fsys_mgr().Url_finder().Find_by_css_or(wiki.Domain_str(), "logo_night.png", String_.Ary("bin", "any", "xowa", "html", "css", "nightmode"), true);
		div_logo_night = Init_fmtr(tmp_bfr, eval_mgr, div_logo_fmtr, main_page_href_bry, fsys_lnx_encoder.Encode_to_file_protocol(night_logo));

		div_wikis_fmtr.Eval_mgr_(eval_mgr);
		div_after_fmtr.Eval_mgr_(eval_mgr);
		Xow_msg_mgr msg_mgr = wiki.Msg_mgr();
		div_jump_to = Div_jump_to_fmtr.Bld_bry_many(tmp_bfr, msg_mgr.Val_by_key_obj("jumpto"), msg_mgr.Val_by_key_obj("jumptonavigation"), msg_mgr.Val_by_key_obj("jumptosearch"));
		tmp_bfr.Mkr_rls();
		sidebar_mgr.Init_by_wiki();
	}
	private boolean init_needed = true;
	private byte[] Init_fmtr(Bry_bfr tmp_bfr, Bry_fmtr_eval_mgr eval_mgr, Bry_fmtr fmtr, Object... fmt_args) {
		fmtr.Eval_mgr_(eval_mgr);
		fmtr.Bld_bfr_many(tmp_bfr, fmt_args);
		return tmp_bfr.To_bry_and_clear();
	}

	// div_footer
	private Bry_fmtr div_footer_fmtr = Bry_fmtr.keys_("page_modified_on_msg", "app_version", "app_build_date");
	private byte[] div_footer_bry = Bry_.Empty;
	public byte[] Div_footer(byte[] page_modified_on_msg, String app_version, String app_build_date) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		div_footer_bry = Init_fmtr(tmp_bfr, wiki.Eval_mgr(), div_footer_fmtr, page_modified_on_msg, app_version, app_build_date);
		return div_footer_bry;
	}

	// assuming Txt_personal_bry is followed by Txt_ns_bry is followed by Txt_view_bry
	private byte[] subj_href = null;
	private byte[] talk_href = null;
	public byte[] Txt_personal_bry(boolean from_hdump, Xoa_ttl ttl, byte html_gen_tid, boolean isnoredirect) {
		Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_b512();
		Xoh_href_wtr href_wtr = wiki.Html__href_wtr();
		byte[] wiki_user_name = wiki.User().Name();
		byte[] user_href = href_wtr.Build_to_bry(wiki, Bry_.Add(wiki.Ns_mgr().Ids_get_or_null(Xow_ns_.Tid__user).Name_db_w_colon(), wiki_user_name));
		talk_href = href_wtr.Build_to_bry(wiki, Bry_.Add(wiki.Ns_mgr().Ids_get_or_null(Xow_ns_.Tid__user_talk).Name_db_w_colon(), wiki_user_name));
		byte[] rv = Init_fmtr(tmp_bfr, wiki.Eval_mgr(), txt_personal_fmtr
			, user_href
			, wiki_user_name
			, Ns_cls_by_id(wiki.Ns_mgr(), Xow_ns_.Tid__user)
			, talk_href
			, Ns_cls_by_id(wiki.Ns_mgr(), Xow_ns_.Tid__user_talk)
			, indicators_pagesource_enabled
				? display_type(from_hdump, ttl, html_gen_tid)
				: Bry_.Empty
			);
		tmp_bfr.Mkr_rls();
		return rv;
	}

	public byte[] Txt_ns_bry(Xowe_wiki wiki, Xoa_ttl ttl, boolean ispage_in_wikisource, Xoae_page page) {
		Bry_bfr_mkr bfr_mkr = wiki.Utl__bfr_mkr();
		Xow_ns_mgr ns_mgr = wiki.Ns_mgr();
		Xow_ns ns = ttl.Ns();
		byte[] subj_cls = Ns_cls_by_ord(ns_mgr, ns.Ord_subj_id()), talk_cls = Ns_cls_by_ord(ns_mgr, ns.Ord_talk_id());
		if (ns.Id_is_talk())
			talk_cls = Xow_portal_mgr.Cls_selected_y;
		else
			subj_cls = Xow_portal_mgr.Cls_selected_y;
		Bfr_arg vnt_menu = null;
		Xol_vnt_mgr vnt_mgr = wiki.Lang().Vnt_mgr();	// VNT; DATE:2015-03-03
		if (vnt_mgr.Enabled()) {
			vnt_menu_fmtr.Init(vnt_mgr.Regy(), wiki.Domain_bry(), ttl.Full_db(), vnt_mgr.Cur_itm().Key());
			vnt_menu = wiki.Lang().Vnt_mgr().Enabled() ? vnt_menu_fmtr : null;
		}

		// NOTE: need to escape args href for Search page b/c user can enter in quotes and apos; EX:localhost:8080/en.wikipedia.org/wiki/Special:XowaSearch?search=title:(%2Breturn%20%2B"abc") ; DATE:2017-07-16
		Bry_bfr tmp_bfr = bfr_mkr.Get_k004();
		//byte[] talk_href = Xoh_html_wtr_escaper.Escape(Xop_amp_mgr.Instance, tmp_bfr, Bry_.Add(Xoh_href_.Bry__wiki, ttl.Talk_txt()));
		//byte[] talk_href = Bry_.Add(Xoh_href_.Bry__wiki, ttl.Talk_href());

		// Adds namespace links
		Xow_msg_mgr msg_mgr = wiki.Msg_mgr();
		byte[] subjectId = Xow_ns_canonical_.To_canonical_or_local_as_bry(ttl.Ns());
		if (subjectId.length == 0)
			subjectId = Bry_.new_a7("main");
		else
			subjectId = Bry_.Lcase__all(subjectId);
		String subjectKey = "nstab-" + String_.new_u8(subjectId);
//		if (Bry_.Has_at_end(wiki.Props().Siteinfo_mainpage(), ttl.Page_db()))
		if (Bry_.Eq(Xoa_ttl.Replace_unders(page.Ttl().Raw()), wiki.Props().Main_page()))
			subjectKey = "mainpage-nstab";
		byte[] portal_main = msg_mgr.Val_by_key_obj(subjectKey);
		// if no mapping use the Namespace name
		if (portal_main.length == 0)
			portal_main = ttl.Ns().Name_db();

		// if this is a wikisource Page then need to calculate next,prev and index
		byte[] extra = Bry_.Empty;
		if (ispage_in_wikisource) {
			Xoa_ttl idx_ttl = Xoa_ttl.Parse(wiki, wiki.Ns_mgr().Ns_index_id(), ttl.Root_txt());
			Xoae_page idx_page = wiki.Data_mgr().Load_page_by_ttl(idx_ttl);

			byte[] lnk_txt, lnk;
			int currentpageno;
			Bry_bfr bfr = Bry_bfr_.New();

			if (!idx_page.Db().Page().Exists()) { // does not exist
				// need another strategy
				idx_ttl = wiki.Index_page().Get_index_page(page.Db().Page().Id());
				if (idx_ttl != null) {
					idx_page = wiki.Data_mgr().Load_page_by_ttl(idx_ttl);
					List_adp lnks = Pp_index_parser.Parse_pages_param(wiki, idx_page.Db().Text().Text_bry());
					int maxpagecount = lnks.Count();
					byte[] pagename = page.Ttl().Full_db();
					int i;
					for (i = 0; i < maxpagecount; i++) {
						Xoa_ttl lnkttl = (Xoa_ttl)lnks.Get_at(i);
						if (Bry_.Eq(pagename, lnkttl.Full_db()))
							break;
					}
					currentpageno = i; // range 0..maxpagecount-1
	
					if (currentpageno - 1 >= 0) {
						lnk = ((Xoa_ttl)lnks.Get_at(currentpageno - 1)).Page_href();
						lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_prevpage");
						Fmt__prev_lnk.Bld_many(bfr, lnk, lnk_txt);
					}
					if (currentpageno + 1 < maxpagecount) {
						lnk = ((Xoa_ttl)lnks.Get_at(currentpageno + 1)).Page_href();
						lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_nextpage");
						Fmt__next_lnk.Bld_many(bfr, lnk, lnk_txt);
					}
	
					lnk = idx_ttl.Full_url();
					lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_index");
					Fmt__index_lnk.Bld_many(bfr, lnk, lnk_txt);
				}
			}
			else {
				int maxpagecount = wiki.Maxpage().Get_maxpage(idx_page.Db().Page().Id());
	
				lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_image");
			//lnk = ??
			//Fmt__image.Bld_many(bfr, lnk, lnk_txt);

				currentpageno = 1;
				if (ttl.Leaf_bgn() > 0)
					currentpageno = Bry_.To_int(ttl.Leaf_txt());
	
				lnk = idx_ttl.Page_href(); //remove Index: (that is, not the Leaf)
				if (currentpageno - 1 > 0) {
					lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_prevpage");
					Fmt__prev.Bld_many(bfr, lnk, currentpageno - 1, lnk_txt);
				}
				if (currentpageno + 1 < maxpagecount) {
					lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_nextpage");
					Fmt__next.Bld_many(bfr, lnk, currentpageno + 1, lnk_txt);
				}

				lnk_txt = msg_mgr.Val_by_key_obj("proofreadpage_index");
				Fmt__index.Bld_many(bfr, lnk, lnk_txt);
			}

			extra = bfr.To_bry_and_clear();
		}

		// "portal_ns_subj_href", "portal_ns_subj_cls", "portal_ns_talk_href", "portal_ns_talk_cls", "portal_div_vnts", "portal_main", "portal_ca", "portal_extra"
		txt_ns_fmtr.Bld_bfr_many(tmp_bfr, subj_href, subj_cls, talk_href, talk_cls, vnt_menu, portal_main, subjectId, extra);
		return tmp_bfr.To_bry_and_rls();
	}
	private static final Bry_fmt
	 Fmt__image = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPageScanLink\"><span><a href=\"~{img}\" class=\"xowa-hover-off\">~{img_txt}</a></span></li>"
	 )
	,Fmt__prev = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPagePrevLink\" class=\"icon\"><span><a href=\"/wiki/Page:~{lnk}/~{cnt}\" class=\"xowa-hover-off\" rel=\"prev\" title=\"~{lnk_txt}\">~{lnk_txt}</a></span></li>"
	 )
	,Fmt__prev_lnk = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPagePrevLink\" class=\"icon\"><span><a href=\"/wiki/Page:~{lnk}\" class=\"xowa-hover-off\" rel=\"prev\" title=\"~{lnk_txt}\">~{lnk_txt}</a></span></li>"
	 )
	,Fmt__next = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPageNextLink\" class=\"icon\"><span><a href=\"/wiki/Page:~{lnk}/~{cnt}\" class=\"xowa-hover-off\" rel=\"next\" title=\"~{lnk_txt}\">~{lnk_txt}</a></span></li>"
	 )
	,Fmt__next_lnk = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPageNextLink\" class=\"icon\"><span><a href=\"/wiki/Page:~{lnk}\" class=\"xowa-hover-off\" rel=\"next\" title=\"~{lnk_txt}\">~{lnk_txt}</a></span></li>"
	 )
	,Fmt__index = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPageIndexLink\" class=\"icon\"><span><a href=\"/wiki/Index:~{lnk}\" class=\"xowa-hover-off\" title=\"~{img_txt}\">~{img_txt}</a></span></li>"
	 )
	,Fmt__index_lnk = Bry_fmt.Auto_nl_skip_last
	 ( "<li id=\"ca-proofreadPageIndexLink\" class=\"icon\"><span><a href=\"/wiki/~{lnk}\" class=\"xowa-hover-off\" title=\"~{img_txt}\">~{img_txt}</a></span></li>"
	 )
	;
	private byte[] Ns_cls_by_ord(Xow_ns_mgr ns_mgr, int ns_ord) {
		Xow_ns ns = ns_mgr.Ords_get_at(ns_ord);
		return ns == null || ns.Exists() ? Bry_.Empty : missing_ns_cls;
	}
	private byte[] Ns_cls_by_id(Xow_ns_mgr ns_mgr, int ns_id) {
		Xow_ns ns = ns_mgr.Ids_get_or_null(ns_id);
		return ns == null || ns.Exists() ? Bry_.Empty : missing_ns_cls;			
	}
	public byte[] Txt_view_bry(Bry_bfr_mkr bfr_mkr, byte output_tid, byte[] search_text, Xoa_ttl ttl, boolean isnoredirect) {
		Bry_bfr tmp_bfr = bfr_mkr.Get_k004();
		byte[] read_cls = Bry_.Empty, edit_cls = Bry_.Empty, html_cls = Bry_.Empty;
		switch (output_tid) {
			case Xopg_view_mode_.Tid__read: read_cls = Cls_selected_y; break;
			case Xopg_view_mode_.Tid__edit: edit_cls = Cls_selected_y; break;
			case Xopg_view_mode_.Tid__html: html_cls = Cls_selected_y; break;
		}

		// build url_fragment with action query argument; EX: "/wiki/Page_name?action="
		//byte[] url_frag_w_action_qarg = Bry_.Add(Xoh_href_.Bry__wiki, ttl.Full_db(), Byte_ascii.Question_bry, Xoa_url_.Qarg__action, Byte_ascii.Eq_bry);
		byte[] url_frag_w_action_qarg = Bry_.Add(subj_href, Byte_ascii.Question_bry, Xoa_url_.Qarg__action, Byte_ascii.Eq_bry);
		byte[] noredirect = isnoredirect 
			? Bry_.Add(Byte_ascii.Amp_bry, Xoa_url_.Qarg__redirect, Byte_ascii.Eq_bry, Xoa_url_.Qarg__redirect__no)
			: Bry_.Empty;

		txt_view_fmtr.Bld_bfr_many(tmp_bfr, read_cls, edit_cls, html_cls, search_text
			, subj_href //Bry_.Add(Xoh_href_.Bry__wiki, ttl.Full_db())
			, Bry_.Add(url_frag_w_action_qarg, Xoa_url_.Qarg__action__edit, noredirect)
			, Bry_.Add(url_frag_w_action_qarg, Xoa_url_.Qarg__action__html, noredirect)
			, wiki.Props().Site_name()
			);
		return tmp_bfr.To_bry_and_rls();
	}
	public static final    byte[] Cls_selected_y = Bry_.new_a7("selected"), Cls_new = Bry_.new_a7("new"), Cls_display_none = Bry_.new_a7("xowa_display_none");
	public byte[] Div_logo_bry(boolean nightmode) {
            return nightmode ? div_logo_night : div_logo_day;} 
        private byte[] div_logo_day = Bry_.Empty, div_logo_night = Bry_.Empty;
	public byte[] Div_home_bry() {return sidebar_enabled ? div_home_bry : Bry_.Empty;} private byte[] div_home_bry = Bry_.Empty;
	public byte[] Div_sync_bry(Bry_bfr tmp_bfr, boolean manual_enabled, Xow_wiki wiki, Xoa_page page) {
		// only show update_html if wmf; DATE:2016-08-31
		if (	wiki.Domain_itm().Domain_type().Src() == Xow_domain_tid.Src__wmf
			&&	manual_enabled) {
			div_sync_fmtr.Bld_bfr_many(tmp_bfr, page.Ttl().Full_url());
			return tmp_bfr.To_bry_and_clear();
		}
		return Bry_.Empty;
	}
	public byte[] Div_wikis_bry(Bry_bfr_mkr bfr_mkr) {
		if (toggle_itm == null)	// TEST:lazy-new b/c Init_by_wiki
			toggle_itm = wiki.Appe().Api_root().Html().Page().Toggle_mgr().Get_or_new("offline-wikis").Init(Bry_.new_a7("Wikis"));
		Bry_bfr tmp_bfr = bfr_mkr.Get_k004();
		div_wikis_fmtr.Bld_bfr_many(tmp_bfr, toggle_itm.Html_toggle_btn(), toggle_itm.Html_toggle_hdr());
		return tmp_bfr.To_bry_and_rls();
	}
	public Bry_fmtr Div_after_fmtr() {
		return div_after_fmtr;
	}

	private final    Bry_fmtr 
	  div_logo_fmtr = Bry_fmtr.new_("", "portal_nav_main_href", "portal_logo_url")
	, div_sync_fmtr = Bry_fmtr.new_("", "page_url")
	, div_wikis_fmtr = Bry_fmtr.new_("", "toggle_btn", "toggle_hdr")
	, div_after_fmtr = Bry_fmtr.new_("", "content")
	, txt_categories_fmtr = Bry_fmtr.new_("", "printfooter", "page_langs")
	, txt_pageread_fmtr = Bry_fmtr.new_("", "page_lang", "page_lang_ltr", "page_data")
	, txt_pageedit_fmtr = Bry_fmtr.new_("", "edit_div_editnotices", "edit_div_preview", "edit_lang", "edit_lang_ltr", "edit_div_rename", "page_data", "page_text", "page_ttl_full")
	, txt_pagehtml_fmtr = Bry_fmtr.new_("", "page_lang", "page_lang_ltr", "page_data")
	, txt_ns_fmtr = Bry_fmtr.new_("~{portal_ns_subj_href};~{portal_ns_subj_cls};~{portal_ns_talk_href};~{portal_ns_talk_cls};~{portal_div_vnts};~{portal_main};~{portal_ca};~{portal_extra}", "portal_ns_subj_href", "portal_ns_subj_cls", "portal_ns_talk_href", "portal_ns_talk_cls", "portal_div_vnts", "portal_main", "portal_ca", "portal_extra")
	, txt_view_fmtr = Bry_fmtr.new_("", "portal_view_read_cls", "portal_view_edit_cls", "portal_view_html_cls", "search_text", "portal_view_read_href", "portal_view_edit_href", "portal_view_html_href", "sitename")
	, txt_personal_fmtr = Bry_fmtr.new_("~{portal_personal_subj_href};~{portal_personal_subj_text};~{portal_personal_talk_cls};~{portal_personal_talk_href};~{portal_personal_talk_cls};~{portal_indicators_pagesource}", "portal_personal_subj_href", "portal_personal_subj_text", "portal_personal_subj_cls", "portal_personal_talk_href", "portal_personal_talk_cls", "portal_indicators_pagesource")
	;
	public Bry_fmtr Div_logo_fmtr() {return div_logo_fmtr;} // TEST:
	public Bry_fmtr Txt_categoties_fmtr() {return txt_categories_fmtr;}
	public Bry_fmtr Txt_pageread_fmtr() {return txt_pageread_fmtr;}
	public Bry_fmtr Txt_pageedit_fmtr() {return txt_pageedit_fmtr;}
	public Bry_fmtr Txt_pagehtml_fmtr() {return txt_pagehtml_fmtr;}

	private boolean Nothex(byte b) {
		if (b >= Byte_ascii.Num_0 && b <= Byte_ascii.Num_9) return false;
		if (b >= Byte_ascii.Ltr_A && b <= Byte_ascii.Ltr_F) return false;
		if (b >= Byte_ascii.Ltr_a && b <= Byte_ascii.Ltr_f) return false;
		return true;
	}
	private byte[] Checkpercent(byte[] src) {
		// search for '%'
		// if followed by two valid hex chars (upper  and lowercase) - ignore
		// else insert '25'
		boolean changed = false;
		int len = src.length;
		int bgn = 0;
		int pos = 0;
		Bry_bfr tmp_bfr = null;
		while (pos < len) {
			if (src[pos++] == '%') {
				if (pos + 2 > len ||
				    Nothex(src[pos]) ||
				    Nothex(src[pos+1]) ) {
					if (!changed) {
						tmp_bfr = Bry_bfr_.New();
						changed = true;
					}
					tmp_bfr.Add_mid(src, bgn, pos);
					tmp_bfr.Add_byte(Byte_ascii.Num_2).Add_byte(Byte_ascii.Num_5);
					bgn = pos;
				}
				else
					pos += 2;
			}
		}
		if (changed) {
			tmp_bfr.Add_mid(src, bgn, len);
			return tmp_bfr.To_bry_and_clear();
		}
		else
			return src;
	}
	private byte[] display_type(boolean from_hdump, Xoa_ttl ttl, byte html_gen_tid) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		Xoh_href_wtr href_wtr = wiki.Html__href_wtr();
		subj_href = href_wtr.Build_to_bry(wiki, ttl.Subj_txt()); // or ttl.Full_url()?????
		//subj_href = Xoh_html_wtr_escaper.Escape(Xop_amp_mgr.Instance, tmp_bfr, Bry_.Add(Xoh_href_.Bry__wiki, ttl.Subj_url()));
		//subj_href = Bry_.Add(Xoh_href_.Bry__wiki, ttl.Full_url());
		//subj_href = Bry_.Replace(subj_href, Bry_.new_a7("%"), Bry_.new_a7("%25"));
		subj_href = Checkpercent(subj_href);
		if (html_gen_tid != 0) return Bry_.Empty; // edit & html are irrelevant
		boolean read_from_html_db_preferred = wiki.Html__hdump_mgr().Load_mgr().Read_preferred();
		if (from_hdump) {
			//html
			tmp_bfr.Add_str_a7( "<li>" );
			tmp_bfr.Add_str_a7( "<a href=\"" );
			tmp_bfr.Add( subj_href );
			tmp_bfr.Add_str_a7( "?action=wikitextver\">" );
			if (read_from_html_db_preferred) tmp_bfr.Add_str_a7( "<b>" );
			tmp_bfr.Add_str_a7( "HTML" );
			if (read_from_html_db_preferred) tmp_bfr.Add_str_a7( "</b>" );
			tmp_bfr.Add_str_a7( "</a></li>" );
		} else {
			//wikitext
			tmp_bfr.Add_str_a7( "<li>" );
			if (subj_href != null) {
				tmp_bfr.Add_str_a7( "<a href=\"" );
				tmp_bfr.Add( subj_href );
				tmp_bfr.Add_str_a7( "?action=htmlver\">" );
			}
			if (!read_from_html_db_preferred) tmp_bfr.Add_str_a7( "<b>" );
			tmp_bfr.Add_str_a7( "WIKITEXT" );
			if (!read_from_html_db_preferred) tmp_bfr.Add_str_a7( "</b>" );
			if (subj_href != null ) tmp_bfr.Add_str_a7( "</a>" );
			tmp_bfr.Add_str_a7( "</li>" );
		}
		return tmp_bfr.To_bry_and_clear();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if      (ctx.Match(k, Invk_div_logo_))						div_logo_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_div_sync_))						div_sync_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_div_wikis_))                                         div_wikis_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_div_after_))                                         div_after_fmtr.Fmt_(m.ReadBry("v"));

		else if (ctx.Match(k, Cfg__missing_class))					missing_ns_cls = m.ReadBry("v");
		else if (ctx.Match(k, Cfg__sidebar_enabled__desktop))		Sidebar_enabled_(Bool_.Y, m.ReadYn("v"));
		else if (ctx.Match(k, Cfg__sidebar_enabled__server))		Sidebar_enabled_(Bool_.N, m.ReadYn("v"));
		else if (ctx.Match(k, Cfg__divs__footer))					div_footer_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Cfg__hdumps_indicators_enabled))      indicators_pagesource_enabled = m.ReadYn("v");
		else if (ctx.Match(k, Cfg__hdumps_indicators_wtxt))         indicators_pagesource_wtxt = m.ReadBry("v");
		else if (ctx.Match(k, Cfg__hdumps_indicators_html))         indicators_pagesource_html = m.ReadBry("v");

		else if (ctx.Match(k, Invk_txt_categories))        txt_categories_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_txt_pageread))          txt_pageread_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_txt_pageedit))          txt_pageedit_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_txt_pagehtml))          txt_pagehtml_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_txt_ns_))               txt_ns_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_txt_view_))             txt_view_fmtr.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_txt_personal_))         txt_personal_fmtr.Fmt_(m.ReadBry("v"));

		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	private static final String 
	  Invk_div_sync_ = "div_sync_"
	, Invk_div_wikis_ = "div_wikis_"
	, Invk_div_after_ = "div_after_"
	, Invk_txt_categories = "txt_categories_"
	, Invk_txt_pageread = "txt_pageread_"
	, Invk_txt_pageedit = "txt_pageedit_"
	, Invk_txt_pagehtml = "txt_pagehtml_"
	, Invk_txt_ns_ = "txt_ns_"
	, Invk_txt_view_ = "txt_view_"
	, Invk_txt_personal_ = "txt_personal_"
	;
	public static final String Invk_div_logo_ = "div_logo_";
	private static final    byte[] Missing_ns_cls_hide = Bry_.new_a7("xowa_display_none");

	// NOTE: emulate recent change but support backward compatibility; ISSUE#:394; REF.MW:https://phabricator.wikimedia.org/source/Vector/browse/master/includes/templates/index.mustache DATE:2019-03-20
	// TODO: use "vector-jumptosearch", but need to update language.gfs files
	private static final    Bry_fmtr Div_jump_to_fmtr = Bry_fmtr.new_(String_.Concat
	( "\n    <div id=\"jump-to-nav\" class=\"mw-jump\">" // NOTE:class=mw-jump is for backward compatibility
	, "\n    <a class=\"mw-jump-link\" href=\"#mw-head\">~{jumpto}~{jumptonavigation}</a>"
	, "\n    <a class=\"mw-jump-link\" href=\"#p-search\">~{jumpto}~{jumptosearch}</a>"
	, "\n    </div>" // NOTE: </div> is for backward compatibility; current MW places right after jump-to-nav
	), "jumpto", "jumptonavigation", "jumptosearch");

	private static final String
	  Cfg__missing_class				= "xowa.html.portal.missing_class"
	, Cfg__sidebar_enabled__desktop		= "xowa.html.portal.sidebar_enabled_desktop"
	, Cfg__sidebar_enabled__server		= "xowa.html.portal.sidebar_enabled_server"
	, Cfg__divs__footer					= "xowa.html.divs.footer"
	, Cfg__hdumps_indicators_enabled    = "xowa.wiki.hdumps.indicators.enabled"
	, Cfg__hdumps_indicators_wtxt       = "xowa.wiki.hdumps.indicators.html_if_wtxt"
	, Cfg__hdumps_indicators_html       = "xowa.wiki.hdumps.indicators.html_if_html"
	;
}
