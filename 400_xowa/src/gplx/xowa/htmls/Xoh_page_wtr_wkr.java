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
package gplx.xowa.htmls;

import gplx.Bool_;
import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_bfr_;
import gplx.DateAdp;
import gplx.DateAdp_;
import gplx.core.brys.fmtrs.Bry_fmtr;
import gplx.langs.htmls.Gfh_utl;
import gplx.xowa.Xoa_app_;
import gplx.xowa.Xoa_ttl;
import gplx.xowa.Xoae_app;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.apps.gfs.Gfs_php_converter;
import gplx.xowa.htmls.core.Xow_hdump_mode;
import gplx.xowa.htmls.core.htmls.Xoh_html_wtr_escaper;
import gplx.xowa.htmls.core.htmls.Xoh_wtr_ctx;
import gplx.xowa.htmls.hxtns.pages.Hxtn_page_mgr;
import gplx.xowa.htmls.portal.Xoh_page_body_cls;
import gplx.xowa.htmls.portal.Xow_portal_mgr;
import gplx.xowa.langs.vnts.Xol_vnt_mgr;
import gplx.xowa.parsers.Xop_ctx;
import gplx.xowa.wikis.Xow_page_tid;
import gplx.xowa.wikis.domains.Xow_domain_tid_;
import gplx.xowa.wikis.nss.Xow_ns_;
import gplx.xowa.wikis.pages.Xopg_view_mode_;
import gplx.xowa.wikis.pages.skins.Xopg_xtn_skin_fmtr_arg;
import gplx.xowa.wikis.pages.skins.Xopg_xtn_skin_itm_tid;
import gplx.xowa.xtns.pfuncs.times.Pft_func_formatdate;
import gplx.xowa.xtns.wbases.Wdata_xwiki_link_wtr;

import gplx.xowa.addons.wikis.ctgs.htmls.pageboxs.Xoctg_pagebox_itm;
import gplx.xowa.langs.msgs.Xow_msg_mgr;
import gplx.xowa.Db_expand;
import gplx.xowa.wikis.pages.lnkis.Xopg_redlink_mgr;
import gplx.xowa.htmls.hxtns.blobs.Hxtn_blob_tbl;
import gplx.langs.jsons.*;
//import gplx.langs.jsons.Json_doc;
//import gplx.langs.jsons.Json_nde;
//import gplx.langs.jsons.Json_kv;
//import gplx.langs.jsons.Json_ary;
import gplx.Bry_fmt;
import gplx.xowa.Db_lua_comp;
import gplx.xowa.parsers.utils.Xop_redirect_mgr;
import gplx.String_;
import gplx.xowa.addons.htmls.sidebars.Db_Nav_template;
import gplx.xowa.parsers.logs.stats.Xop_log_stat;
import gplx.xowa.wikis.pages.Xopg_page_heading;
import gplx.xowa.langs.msgs.Xol_msg_mgr;
import gplx.langs.mustaches.JsonMustacheNde;
import gplx.langs.mustaches.Mustache_bfr;
import gplx.langs.mustaches.Mustache_render_ctx;
import gplx.langs.mustaches.Mustache_tkn_itm;
import gplx.langs.mustaches.Mustache_tkn_parser;
import gplx.Io_url;
public class Xoh_page_wtr_wkr {
	private boolean ispage_in_wikisource = false;
	private final	Object thread_lock_1 = new Object(), thread_lock_2 = new Object();
	private final	Bry_bfr tmp_bfr = Bry_bfr_.Reset(255); 
	private final	Xoh_page_wtr_mgr mgr; private final	byte page_mode;
	private final	Wdata_xwiki_link_wtr wdata_lang_wtr = new Wdata_xwiki_link_wtr();	// In other languages
	private final	gplx.xowa.addons.apps.scripts.Xoscript_mgr scripting_mgr = new gplx.xowa.addons.apps.scripts.Xoscript_mgr();
	private Xoae_app app; private Xowe_wiki wiki; private Xoae_page page; private byte[] root_dir_bry;
	public Xoh_page_wtr_wkr(Xoh_page_wtr_mgr mgr, byte page_mode) {this.mgr = mgr; this.page_mode = page_mode;}		
	public Xoh_page_wtr_wkr Ctgs_enabled_(boolean v) {ctgs_enabled = v; return this;} private boolean ctgs_enabled = true;
	private String vectortags = "";
	public void Write_page(Bry_bfr rv, Xoae_page page, Xop_ctx ctx, Xoh_page_html_source page_html_source) {
		synchronized (thread_lock_1) {
			this.page = page; this.wiki = page.Wikie(); this.app = wiki.Appe();
			ctx.Page_(page); // HACK: must update page for toc_mgr; WHEN: Xoae_page rewrite
			Bry_fmtr fmtr = mgr.Page_read_fmtr();
			Set_ispage_in_wikisource(page);
			if (mgr.Html_capable()) {
				wdata_lang_wtr.Page_(page);
				byte view_mode = page_mode;
				switch (page_mode) {
					case Xopg_view_mode_.Tid__html: view_mode = Xopg_view_mode_.Tid__read; break; // set view_mode to read, so that "read" is highlighted in HTML
				}
				Bry_bfr page_bfr = wiki.Utl__bfr_mkr().Get_m001();	// NOTE: get separate page rv to output page; do not reuse tmp_bfr b/c it will be used inside Fmt_do
				try {
					Xoh_wtr_ctx hctx = null;
					if (page_mode == Xopg_view_mode_.Tid__html 
						&& wiki.Html__hdump_mgr().Load_mgr().Html_mode().Tid() == Xow_hdump_mode.Hdump_save.Tid()) {
						hctx = Xoh_wtr_ctx.Hdump;
						Write_body(page_bfr, ctx, hctx, page);
						byte[] html_bry = page_bfr.To_bry_and_clear();
						Write_page_by_tid(ctx, hctx, page_mode, rv, mgr.Page_html_fmtr(), Gfh_utl.Escape_html_as_bry(html_bry));
					}
					else {
						// NOTE: if HTTP, generate hdump html b/c of async image download; HTTP will later call make_mgr to substitute out <xoimg>; ISSUE#:686; DATE:2020-06-27
						hctx = app.Mode().Tid_is_http() ? Xoh_wtr_ctx.HttpServer : Xoh_wtr_ctx.Basic;
						Write_body(page_bfr, ctx, hctx, page);
						Write_page_by_tid(ctx, hctx, view_mode, rv, fmtr, page_bfr.To_bry_and_rls());
						scripting_mgr.Write(rv, wiki, page);
						if (page_mode == Xopg_view_mode_.Tid__html)	// if html, write page again, but wrap it in html skin this time
							Write_page_by_tid(ctx, hctx, page_mode, rv, fmtr, Gfh_utl.Escape_html_as_bry(rv.To_bry_and_clear()));
						wdata_lang_wtr.Page_(null);
					}
				} finally {page_bfr.Mkr_rls();}
			}
			else
				Write_body(rv, ctx, Xoh_wtr_ctx.Basic, page);
			this.page = null;
		}
	}
	private void Write_page_by_tid(Xop_ctx ctx, Xoh_wtr_ctx hctx, byte html_gen_tid, Bry_bfr bfr, Bry_fmtr fmtr, byte[] page_data) {
		// if custom_html, use it and exit; needed for Default_tab
		byte[] custom_html = page.Html_data().Custom_html();
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();
		if (custom_html != null) {bfr.Add(custom_html); return;}
		// temp variables
		if (root_dir_bry == null) this.root_dir_bry = app.Fsys_mgr().Root_dir().To_http_file_bry();
		Xoa_ttl page_ttl = page.Ttl(); int page_ns_id = page_ttl.Ns().Id();
		byte page_tid = Xow_page_tid.Identify(wiki.Domain_tid(), page_ns_id, page_ttl.Page_db());

		// write modified_on; handle invalid dates
		DateAdp modified_on = page.Db().Page().Modified_on();
		byte[] modified_on_msg = Bry_.Empty;
		if (modified_on != DateAdp_.MinValue && wiki.Installed()) {
			modified_on = modified_on.XtoZone(wiki.Tz_mgr().Get_timezone());
			modified_on_msg = wiki.Msg_mgr().Val_by_key_args(Key_lastmodifiedat, 
						wiki.Lang().Time_format_mgr().Get_date_defaultfmt(wiki, modified_on),
						wiki.Lang().Time_format_mgr().Get_time_defaultfmt(wiki, modified_on));
			modified_on_msg = Db_expand.Extracheck(modified_on_msg, "");
		}
		if (page.Html_data().GeoCrumb().Count() > 0) {
			byte[] redirect_msg = Xop_redirect_mgr.Bld_redirect_msg_from(app, wiki, page.Redirect_trail());
			page.Redirect_trail().Clear(); // so it does not show up later
			page.Html_data().GeoCrumb().Add(redirect_msg);
		}
		page.Html_data().Pagebanner().Add(hctx);
		page.Html_data().GeoCrumb().Set_ctx(ctx);
		page.Html_data().Pagebanner().Set_ctx(ctx);
		page.Html_data().Pp_indexpage().Set_ctx(ctx);
		byte[] page_body_class = Xoh_page_body_cls.Calc(tmp_bfr, page_ttl, page_tid);
		// byte[] html_content_editable = wiki.Gui_mgr().Cfg_browser().Content_editable() ? Content_editable_bry : Bry_.Empty;
		byte[] html_content_editable = Bry_.Empty;
		byte[] page_content_sub = Xoh_page_wtr_wkr_.Bld_page_content_sub(app, wiki, page, tmp_bfr, isnoredirect);
		byte[] js_edit_toolbar_bry = html_gen_tid == Xopg_view_mode_.Tid__edit ? wiki.Fragment_mgr().Html_js_edit_toolbar() : Bry_.Empty;
		Xol_vnt_mgr vnt_mgr = wiki.Lang().Vnt_mgr();
		if (vnt_mgr.Enabled()) {
			byte[] converted_title = vnt_mgr.Convert_lang().Converted_title();	// prefer converted title
			if (converted_title == null)	// converted title does not exist; use regular page title and convert it
				converted_title = vnt_mgr.Convert_lang().Auto_convert(vnt_mgr.Cur_itm(), page_ttl.Page_txt());
			page_ttl = Xoa_ttl.Parse(wiki, page_ttl.Ns().Id(), converted_title);
		}

		byte[] page_name = Xoh_page_wtr_wkr_.Bld_page_name(tmp_bfr, page_ttl, null);		// NOTE: page_name does not show display_title (<i>). always pass in null
		// get pagename for <h1 id="firstHeading" class="firstHeading"></h1>
		byte[] pagename_for_h1;
		if (wiki.Domain_tid() == Xow_domain_tid_.Tid__wikidata && page_data.length > 40 &&
		   (page_ttl.Ns().Id_is_main() || page_ttl.Ns().Id() == 120 || page_ttl.Ns().Id() == 146 || page_ttl.Ns().Id() == 640)) { // short pages use orig title, main, property, lexeme, or entityschema
			pagename_for_h1 = app.Wiki_mgr().Wdata_mgr().Page_display_title();
			page_name = app.Wiki_mgr().Wdata_mgr().Overview_label();
		}
		else
			pagename_for_h1 = Xoh_page_wtr_wkr_.Bld_page_name(tmp_bfr, page_ttl, page.Html_data().Display_ttl());
		page.Html_data().Custom_tab_name_(page_name);	// set tab_name to page_name; note that if null, gui code will ignore and use Ttl.Page_txt; PAGE: zh.w:釣魚臺列嶼主權問題 DATE:2015-10-05

		Xow_msg_mgr msg_mgr = wiki.Msg_mgr();
		byte[] page_mask_msg = Bry_.new_a7("pagetitle");
		if (Bry_.Eq(Xoa_ttl.Replace_unders(page.Ttl().Raw()), wiki.Props().Main_page()))
			page_mask_msg = Bry_.new_a7("pagetitle-view-mainpage");
		byte[] page_title = Db_expand.Extracheck( msg_mgr.Val_by_key_args(page_mask_msg, page_name), "");

		byte[] redlinks = null;
/*		if (wiki.App().Mode().Tid_is_http()) {
			Xopg_redlink_mgr red_mgr = new Xopg_redlink_mgr(page, null);
			red_mgr.Redlink(tmp_bfr);
			redlinks = tmp_bfr.To_bry_and_clear();
		}*/
		byte[] edit_lang = page.Lang().Key_bry();
		byte[] edit_lang_ltr = page.Lang().Dir_ltr_bry();
		// if editing page and editing a module always english
		if (page_mode == Xopg_view_mode_.Tid__edit && page.Ttl().Ns().Id() == 828) {
			edit_lang = Bry_.new_a7("en");
			edit_lang_ltr = Bry_.new_a7("ltr");
		}

		// main build
		Xow_portal_mgr portal_mgr = wiki.Html_mgr().Portal_mgr().Init_assert();
		boolean nightmode_enabled = app.Gui_mgr().Nightmode_mgr().Enabled();
		page.Html_data().Related().Set_fmtr(portal_mgr.Div_after_fmtr());
		fmtr.Bld_bfr_many(bfr
		, Bry_.new_a7("app_icon.png")
		, root_dir_bry
		, Content(portal_mgr, page_data, page, wiki, ctx, hctx, html_gen_tid, pagename_for_h1, modified_on_msg)
		, html_content_editable
		, mgr.Css_common_bry()
		, mgr.Css_night_bry(nightmode_enabled)
		, mgr.Css_wiki_bry()
		, page_body_class
		, page.Db().Page().Id()
		, page.Lang().Dir_ltr_bry()
		, page.Lang().Key_bry()
		, page_title
		, redlinks
		, vectortags // instance variable set by Content()
		, null
		, page.Html_data().Head_mgr().Init(app, wiki, page).Init_dflts(html_gen_tid)

//		, root_dir_bry, Xoa_app_.Version, Xoa_app_.Build_date, app.Tcp_server().Running_str()
//		, page.Db().Page().Id(), page.Ttl().Full_db_href(), page_title
//		, page.Html_data().Page_heading().Init(wiki, html_gen_tid == Xopg_view_mode_.Tid__read, page.Html_data(), page.Ttl().Full_db(), pagename_for_h1, page.Lang().Key_bry())
//		, modified_on_msg
//		, mgr.Css_common_bry(), mgr.Css_wiki_bry()
//		, mgr.Css_night_bry(nightmode_enabled)
//		, page.Html_data().Head_mgr().Init(app, wiki, page).Init_dflts(html_gen_tid)
//		, page.Lang().Dir_ltr_bry(), /*page.Html_data().Indicators()*/null, page_content_sub
//		, wiki.Html_mgr().Portal_mgr().Div_jump_to()
//		, Bry_.Empty /*page.Html_data().Pagebanner()  // Pagebanner(ctx, hctx)*/
//		, page_body_class, html_content_editable
//		, Content(portal_mgr, page_data, page, wiki, ctx, hctx, html_gen_tid, pagename_for_h1, modified_on_msg) //Pagebody(portal_mgr, page_data, page)
//		, wdata_lang_wtr
//		, portal_mgr.Div_footer(modified_on_msg, Xoa_app_.Version, Xoa_app_.Build_date)
//		, page.Html_data().Related()  // portal_mgr.Div_after_bry(page)
//
//		// sidebar divs
//		, portal_mgr.Div_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect)
//		//, portal_mgr.Div_ns_bry(wiki.Utl__bfr_mkr(), page_ttl, wiki.Ns_mgr())
//		, portal_mgr.Div_ns_bry(wiki, page_ttl, ispage_in_wikisource, page)
//		, portal_mgr.Div_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect)
//		, /*portal_mgr.Div_logo_bry(nightmode_enabled)*/null, portal_mgr.Div_home_bry(), new Xopg_xtn_skin_fmtr_arg(page, Xopg_xtn_skin_itm_tid.Tid_sidebar)
//		, portal_mgr.Div_sync_bry(tmp_bfr, wiki.Page_mgr().Sync_mgr().Manual_enabled(), wiki, page)
//		, portal_mgr.Div_wikis_bry(wiki.Utl__bfr_mkr())
//		, portal_mgr.Sidebar_mgr().Html_bry()
//		, mgr.Edit_rename_div_bry(page_ttl), page.Html_data().Edit_preview_w_dbg(), js_edit_toolbar_bry
//		, page.Lang().Key_bry()
//						, edit_lang, edit_lang_ltr
//						, redlinks
//						, Categories(portal_mgr, wiki, ctx, hctx, page, html_gen_tid)
//						, Bry_.Empty/*page.Html_data().GeoCrumb()*/
//						, Bry_.new_a7("app_icon.png")
//						, wiki.Tagline()
//						, vectortags // instance variable set by Content()
		);
		Xoh_page_wtr_wkr_.Bld_head_end(bfr, tmp_bfr, page);	// add after </head>
		Xoh_page_wtr_wkr_.Bld_html_end(bfr, tmp_bfr, page);	// add after </html>
	}
	public void Write_hdump(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page wpg) {
		this.wiki = ctx.Wiki();
		int page_id = wpg.Db().Page().Id();
		Hxtn_page_mgr html_data_mgr = wpg.Wikie().Hxtn_mgr();
		Set_ispage_in_wikisource(wpg);

		this.Write_body(bfr, ctx, hctx, wpg);

		if (!hctx.Mode_is_hdump_wo_db()) {
			wpg.Html_data().Indicators().HxtnSave(wpg.Wikie(), html_data_mgr, wpg, page_id);
			wpg.Html_data().GeoCrumb().HxtnSave(wpg.Wikie(), html_data_mgr, wpg, page_id);
			wpg.Html_data().Pagebanner().Add(hctx);
			wpg.Html_data().Pagebanner().Set_ctx(ctx);
			wpg.Html_data().Pagebanner().HxtnSave(wpg.Wikie(), html_data_mgr, wpg, page_id);
			wpg.Html_data().Related().HxtnSave(wpg.Wikie(), html_data_mgr, wpg, page_id);
			wpg.Html_data().Quality_tots().HxtnSave(wpg.Wikie(), html_data_mgr, wpg, page_id);
			wpg.Html_data().Pp_indexpage().HxtnSave(wpg.Wikie(), html_data_mgr, wpg, page_id);
		}
	}
	public void Write_body(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page page) {
		synchronized (thread_lock_2) {
			this.page = page; this.wiki = page.Wikie(); this.app = wiki.Appe();
			Xoa_ttl page_ttl = page.Ttl(); int page_ns_id = page_ttl.Ns().Id();
			byte page_tid = Xow_page_tid.Identify(wiki.Domain_tid(), page_ns_id, page_ttl.Page_db());	// NOTE: can't cache page_tid b/c Write_body is called directly; DATE:2014-10-02
			byte[] data_raw = page.Db().Text().Text_bry();
			int bfr_page_bgn = bfr.Len();
			boolean page_tid_uses_pre = false;
			if (page_mode == Xopg_view_mode_.Tid__edit) {
				data_raw = Db_lua_comp.Text(data_raw);
				Write_body_edit(bfr, data_raw, page_ns_id, page_tid);
                        }
			else {
				switch (page_tid) {
					case Xow_page_tid.Tid_msg:
					case Xow_page_tid.Tid_js:
					case Xow_page_tid.Tid_css:
					case Xow_page_tid.Tid_lua:
						Write_body_pre(bfr, app, wiki, hctx, data_raw, tmp_bfr, page_tid);
						page_tid_uses_pre = true;
						break;
					case Xow_page_tid.Tid_json:
						if (page_ns_id != Xow_ns_.Tid__data)
							app.Wiki_mgr().Wdata_mgr().Write_json_as_html(bfr, page_ttl, data_raw);
						else {
							if (data_raw[0] == '\'') // is it ''''page not found'''
								bfr.Add(data_raw);
							else {
								Json_doc jdoc = app.Utl__json_parser().Parse(data_raw);
								Jdoc_data_writer(bfr, jdoc);
								bfr.Add_str_a7("<pre>\n");
								jdoc.Root_grp().Print_as_json(bfr, 0);
								bfr.Add_str_a7("</pre>\n");
							//Write_body_pre(bfr, app, wiki, hctx, data_raw, tmp_bfr, page_tid);
							//page_tid_uses_pre = true;
								//Write_body_wikitext(bfr, app, wiki, wikitext, ctx, hctx, page, page_tid, page_ns_id);
							}
						}
						break;
					case Xow_page_tid.Tid_wikitext:
						Write_body_wikitext(bfr, app, wiki, data_raw, ctx, hctx, page, page_tid, page_ns_id);
						break;
				}
			}
			if (	wiki.Domain_tid() != Xow_domain_tid_.Tid__home	// allow home wiki to use javascript
				&&  !page.Html_data().Js_enabled()				  // allow special pages to use js
				&&  !page_tid_uses_pre) {							// if .js, .css or .lua, skip test; may have js fragments, but entire text is escaped and put in pre; don't show spurious warning; DATE:2013-11-21
				wiki.Html_mgr().Js_cleaner().Clean_bfr(wiki, page_ttl, bfr, bfr_page_bgn);
			}
		}
	}
	private void Write_body_wikitext(Bry_bfr bfr, Xoae_app app, Xowe_wiki wiki, byte[] data_raw, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page page, byte page_tid, int ns_id) {
		// dump and exit if pre-generated html from html dumps
		byte[] hdump_data = page.Db().Html().Html_bry();
		if (Bry_.Len_gt_0(hdump_data)) {
//			bfr.Add(hdump_data);
			Bry_bfr tidy_bfr = wiki.Utl__bfr_mkr().Get_m001();
			tidy_bfr.Add(hdump_data);
			long tidy_time = gplx.core.envs.System_.Ticks();
			wiki.Html_mgr().Tidy_mgr().Exec_tidy(tidy_bfr, !hctx.Mode_is_hdump(), page.Url_bry_safe());
			page.Stat_itm().Tidy_time = gplx.core.envs.System_.Ticks__elapsed_in_frac(tidy_time);
			bfr.Add_bfr_and_clear(tidy_bfr);
			return;
		}

		// dump and exit if MediaWiki message;
		if	(ns_id == Xow_ns_.Tid__mediawiki) {	// if MediaWiki and wikitext, must be a message; convert args back to php; DATE:2014-06-13
			bfr.Add(Gfs_php_converter.Xto_php(tmp_bfr, Bool_.N, data_raw));
			return;
		}

		// if [[File]], add boilerplate header; note that html is XOWA-generated so does not need to be tidied
		if (ns_id == Xow_ns_.Tid__file) app.Ns_file_page_mgr().Bld_html(wiki, ctx, page, bfr, page.Ttl(), wiki.Cfg_file_page(), page.File_queue());

		// get separate bfr; note that bfr already has <html> and <head> written to it, so this can't be passed to tidy; DATE:2014-06-11
		Bry_bfr tidy_bfr = wiki.Utl__bfr_mkr().Get_m001();
		//tidy_bfr.Add_str_a7("<body>");

		try {
			// write wikitext
			if (page.Html_data().Skip_parse()) {
				tidy_bfr.Add(page.Html_data().Custom_body());
			}
			else {
				if (page.Root() != null) {	// NOTE: will be null if blank; occurs for one test: Logo_has_correct_main_page; DATE:2015-09-29
					page.Html_data().Toc_mgr().Clear();	// NOTE: always clear tocs before writing html; toc_itms added when writing html_hdr; DATE:2016-07-17
					if (ispage_in_wikisource) { // Page:
						tidy_bfr.Add_str_a7("<div class=\"prp-page-container\"><div class=\"prp-page-content\"><div class=\"mw-parser-output\">");
					}
					else {
						tidy_bfr.Add_str_a7("<div class=\"mw-parser-output\">");
					}
					wiki.Html_mgr().Html_wtr().Write_doc(tidy_bfr, ctx, hctx, page.Root().Data_mid(), page.Root());
					if (wiki.Html_mgr().Html_wtr().Cfg().Toc__show()
						&& (page.Html_data().Pagebanner().Show_toc_in_html())) // do not write TOC in HTML body if pageBanner is enabled
						gplx.xowa.htmls.core.wkrs.tocs.Xoh_toc_wtr.Write_toc(tidy_bfr, page, hctx);
					if (ispage_in_wikisource) { // Page:
						tidy_bfr.Add_str_a7("</div></div>");
						tidy_bfr.Add_str_a7("</div></div><div class=\"prp-page-image\"></div>");
						//Pp_image_page(...);
					}
					tidy_bfr.Add_str_a7("</div>");
				}
			}
			
			// if [[Category]], add catpage data
			if (ns_id == Xow_ns_.Tid__category) tidy_bfr.Add_safe(page.Html_data().Catpage_data());
			// if (ns_id == Xow_ns_.Tid__category) wiki.Ctg__catpage_mgr().Write_catpage(tidy_bfr, page, hctx);

			// tidy html
			if (ns_id != Xow_ns_.Tid__special) { // skip Special b/c
				long tidy_time = gplx.core.envs.System_.Ticks();
				wiki.Html_mgr().Tidy_mgr().Exec_tidy(tidy_bfr, !hctx.Mode_is_hdump(), page.Url_bry_safe());
				page.Stat_itm().Tidy_time = gplx.core.envs.System_.Ticks__elapsed_in_frac(tidy_time);
			}

			// add back to main bfr
			bfr.Add_bfr_and_clear(tidy_bfr);
		} finally {
			tidy_bfr.Mkr_rls();
		}
				
		// translate if variants are enabled
		Xol_vnt_mgr vnt_mgr = wiki.Lang().Vnt_mgr();
		if (vnt_mgr.Enabled()) bfr.Add(vnt_mgr.Convert_lang().Parse_page(vnt_mgr.Cur_itm(), page.Db().Page().Id(), bfr.To_bry_and_clear()));

		// handle uniqs
		wiki.Parser_mgr().Uniq_mgr().Parse(bfr);
	}
	private void Write_body_pre(Bry_bfr bfr, Xoae_app app, Xowe_wiki wiki, Xoh_wtr_ctx hctx, byte[] data_raw, Bry_bfr tmp_bfr, int page_tid) {
		byte[] lang = Bry_.Empty;
		switch (page_tid) {
			case Xow_page_tid.Tid_msg: lang = Bry_.new_a7("msg"); break;
			case Xow_page_tid.Tid_js: lang = Bry_.new_a7("js"); break;
			case Xow_page_tid.Tid_css: lang = Bry_.new_a7("css"); break;
			case Xow_page_tid.Tid_lua:
				lang = Bry_.new_a7("lua");
				data_raw = Db_lua_comp.Text(data_raw);
				break;
		}
		Xoh_html_wtr_escaper.Escape(app.Parser_amp_mgr(), tmp_bfr, data_raw, 0, data_raw.length, false, false);
//		if (hctx.Mode_is_hdump())
//			bfr.Add(data_raw);
//		else {
			Bry_fmt content_code_fmt = Bry_fmt.Auto("<pre class=\"prettyprint lang-~{lang} linenums\">~{page_text}</pre>");
			content_code_fmt.Bld_many(bfr, lang, tmp_bfr);
			//app.Html_mgr().Page_mgr().Content_code_fmt().Bld_many(bfr, tmp_bfr);
//		}
		tmp_bfr.Clear();
	}
	private void Write_body_edit(Bry_bfr bfr, byte[] data_raw, int ns_id, byte page_tid) {
		if	(	ns_id == Xow_ns_.Tid__mediawiki			// if MediaWiki and wikitext, must be a message; convert args back to php; DATE:2014-06-13
			&&	page_tid == Xow_page_tid.Tid_wikitext
			) {
			data_raw = Gfs_php_converter.Xto_php(tmp_bfr, Bool_.N, data_raw);
		}
		int data_raw_len = data_raw.length;
		if (mgr.Html_capable()) {
			data_raw = wiki.Parser_mgr().Hdr__section_editable__mgr().Slice_section(page.Url(), page.Ttl(), data_raw);
			data_raw_len = data_raw.length;
			Xoh_html_wtr_escaper.Escape(page.Wikie().Appe().Parser_amp_mgr(), bfr, data_raw, 0, data_raw_len, false, false);	// NOTE: must escape; assume that browser will automatically escape (&lt;) (which Mozilla does)
		}
		else
			bfr.Add(data_raw);
		if (data_raw_len > 0)		// do not add nl if empty String
			bfr.Add_byte_nl();		// per MW:EditPage.php: "Ensure there's a newline at the end, otherwise adding lines is awkward."
	}
	// private static final	byte[] Content_editable_bry = Bry_.new_a7(" contenteditable=\"true\"");
		private static final byte[] aref = Bry_.new_a7("<a href=\"")
		, aref2 = Bry_.new_a7("\">")
		, endaref = Bry_.new_a7("</a>")
	, Key_lastmodifiedat = Bry_.new_a7("lastmodifiedat")
	, Key_retrieved = Bry_.new_a7("retrievedfrom")
		;

	private Json_nde dl = null;
	private void Build_json_logos(Json_nde data, String lang) {
/* to produce
"data-logos": {
	"main-page-href":"/wiki/",
	"icon":"wikipedia.png",
	"wordmark":{
		"src":"wikipedia-wordmark-en.svg",
		"width":"119",
		"height":"18",
	},
	"tagline":{
		"src":"wikipedia-tagline-en.svg",
		"width":"113",
		"height":"13",
	},
}
*/
		if (dl == null) {
			dl = Json_nde.NewByVal();
			Json_nde wm = Json_nde.NewByVal();
			Json_nde tl = Json_nde.NewByVal();
	
			dl.AddKvStr("main-page-href", "/wiki/");
			dl.AddKvStr("icon", "/xowa/static/images/mobile/copyright/wikipedia.png");
	
			wm.AddKvStr("src", "/xowa/static/images/mobile/copyright/wikipedia-wordmark-" + lang +".svg");
			wm.AddKvStr("width", "119");
			wm.AddKvStr("height", "18");
			dl.AddKvNde("wordmark", wm);
			
			tl.AddKvStr("src", "/xowa/static/images/mobile/copyright/wikipedia-tagline-" + lang +".svg");
			tl.AddKvStr("width", "113");
			tl.AddKvStr("height", "13");
			dl.AddKvNde("tagline", tl);
		}
		data.AddKvNde("data-logos", dl);
        }
	private void Build_json_search(Json_nde data, Xowe_wiki wiki) {

		Json_nde ds = Json_nde.NewByVal();
		ds.AddKvStr("form-action", "/wiki/Special:XowaSearch");
		ds.AddKvStr("html-input", "<input type=\"search\" name=\"search\" placeholder=\""
			 + msgvalue("searchsuggest-search", wiki) 
			 + "\"" + tooltip( "search", wiki ) + " id=\"searchInput\"/>");
		ds.AddKvStr("page-title", "Special:XowaSearch");
		ds.AddKvStr("html-button-search-fallback", makeSearchButton("fulltext", wiki));
		ds.AddKvStr("html-button-search", makeSearchButton("go", wiki));
		data.AddKvNde("data-search-box", ds);
	}

	private String makeSearchButton(String mode, Xowe_wiki wiki) {
            boolean isgo = mode.equals("go");
		String input_html = "<input name=\""
			+ mode
			+ "\" type=\"submit\" id=\"" 
			+ (isgo ? "searchButton" : "mw-searchButton")
			+ "\"" + tooltip( "search-" + mode, wiki ) + " class=\""
			+ (isgo ? "searchButton" : "searchButton mw-fallbackSearchButton")
			+ "\" value=\""
			+ msgvalue(isgo ? "searcharticle" : "searchbutton", wiki)
			+ "\" />";

		return input_html;
	}
	private String msgvalue(String msgkey, Xowe_wiki wiki) {
		return String_.new_u8(wiki.Lang().Msg_mgr().Val_by_str_or_empty(msgkey));
	}
	private String tooltip(String tipkey, Xowe_wiki wiki) {
		return String_.new_u8(wiki.Msg_mgr().Val_html_accesskey_and_title(tipkey));
	}

	private static String[] msgs = new String[] {
						"vector-opt-out-tooltip",
						"vector-opt-out",
						"navigation-heading",
						"vector-action-toggle-sidebar",
						"vector-jumptonavigation",
						"vector-jumptosearch",
						"vector-jumptocontent",
						"sitesubtitle",
						"sitetitle",
						"search",
						"tagline"
					};

	//all thes messages should be preprocessed (per language) as $data["msg-{$message}"] = $this->msg( $message )->text();
	private void build_msg(Json_nde data, Xowe_wiki wiki) {
		Xol_msg_mgr msg_mgr = wiki.Lang().Msg_mgr();
		int msg_len = msgs.length;
		for (int i = 0; i < msg_len; i++) {
			String msg = msgs[i];
			if (msg.equals("tagline"))
				data.AddKvStr("msg-" + msg, wiki.Tagline());
			else
				data.AddKvStr("msg-" + msg, msg_mgr.Val_by_str_or_empty(msg));
		}
	}
	private boolean once = true;
	private Mustache_tkn_itm root_legacy;
	private Mustache_tkn_itm root_new;
	public void Render_Content(Xowe_wiki wiki, Bry_bfr bfr, Json_nde data) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates");
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root);
			root_legacy = parser.Parse("content-test");
			//parser = new Mustache_tkn_parser(template_root);
			root_new = parser.Parse("content-test-new");
		}
		Mustache_render_ctx mctx = new Mustache_render_ctx().Init(new JsonMustacheNde(data));
		Mustache_bfr mbfr = Mustache_bfr.New_bfr(bfr);
		if (wiki.Skin_mgr().Get_skin().equals("vector-new")) {
			root_new.Render(mbfr, mctx);
			vectortags = " skin-vector-max-width skin-vector-search-header";
		}
		else {
			root_legacy.Render(mbfr, mctx);
			vectortags = " skin-vector-legacy";
		}
	}
	private byte[] Content(Xow_portal_mgr portal_mgr, byte[] page_data, Xoae_page page, Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte html_gen_tid, byte[] pagename_for_h1, byte[] modified_on_msg) {
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();
		Xoa_ttl page_ttl = page.Ttl();

		Json_nde data = Json_nde.NewByVal();

		data.AddKvStr("page-langcode", "en");
		data.AddKvBool("page-isarticle", true);
		data.AddKvBool("sidebar-visible", true); // for skin.mustache
		data.AddKvStr("html-user-language-attributes", "");

		Xopg_page_heading ph = page.Html_data().Page_heading();
		ph.Init(wiki, html_gen_tid == Xopg_view_mode_.Tid__read, page.Html_data(), page.Ttl().Full_db(), pagename_for_h1, page.Lang().Key_bry());
		ph.Build_json(data);

		data.AddKvStr("html-subtitle", Xoh_page_wtr_wkr_.Bld_page_content_sub(app, wiki, page, tmp_bfr, isnoredirect));
		data.AddKvStr("html-body-content", Pagebody(portal_mgr, page_data, page));
		if (page_mode == Xopg_view_mode_.Tid__read) // only generate categories if READ
			data.AddKvStr("html-categories", Categories(portal_mgr, wiki, ctx, hctx, page, html_gen_tid));

		page.Html_data().Indicators().Build_json(data);
		portal_mgr.Sidebar_mgr().Build_json(data);

		data.AddKvStr("portal_div_personal", portal_mgr.Div_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect));
		data.AddKvStr("portal_div_ns", portal_mgr.Div_ns_bry(wiki, page_ttl, ispage_in_wikisource, page));
		data.AddKvStr("portal_div_view", portal_mgr.Div_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect));
		data.AddKvStr("portal_div_footer", portal_mgr.Div_footer(modified_on_msg, Xoa_app_.Version, Xoa_app_.Build_date));

		build_msg(data, wiki);

		Build_json_logos(data, String_.new_a7(page.Lang().Key_bry()));
                Build_json_search(data, wiki);
		data.AddKvBool("is-search-in-header", true);

                //System.out.println(data.Print_as_json());

		Render_Content(wiki, tmp_bfr, data);
		return tmp_bfr.To_bry_and_clear();
	}
	private byte[] Pagebody(Xow_portal_mgr portal_mgr, byte[] page_data, Xoae_page page) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		switch (page_mode) {
			case Xopg_view_mode_.Tid__read:
				portal_mgr.Txt_pageread_fmtr().Bld_bfr_many(tmp_bfr, page.Lang().Key_bry(), page.Lang().Dir_ltr_bry(), page_data);
				break;
			case Xopg_view_mode_.Tid__edit:
				byte[] edit_lang = page.Lang().Key_bry();
				byte[] edit_lang_ltr = page.Lang().Dir_ltr_bry();
				// if editing page and editing a module always english
				if (page.Ttl().Ns().Id() == 828) {
					edit_lang = Bry_.new_a7("en");
					edit_lang_ltr = Bry_.new_a7("ltr");
				}
				// "edit_div_preview", "edit_lang", "edit_lang_ltr", "edit_div_rename", "page_data", "page_text", "page_ttl_full"
				portal_mgr.Txt_pageedit_fmtr().Bld_bfr_many(tmp_bfr
					, page.Html_data().Edit_preview_w_dbg()
					, edit_lang
					, edit_lang_ltr
					, mgr.Edit_rename_div_bry(page.Ttl())
					, page_data
					, Bry_.Empty // ???
					, page.Ttl().Full_db_href()
					);
				break;
			case Xopg_view_mode_.Tid__html:
				portal_mgr.Txt_pagehtml_fmtr().Bld_bfr_many(tmp_bfr, page.Lang().Key_bry(), page.Lang().Dir_ltr_bry(), page_data);
				break;
		}
		return tmp_bfr.To_bry_and_clear();
	}
	private byte[] Categories(Xow_portal_mgr portal_mgr, Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page page, byte html_gen_tid) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		byte[] printfooter = Printfooter(wiki, ctx, hctx, page, html_gen_tid);
		portal_mgr.Txt_categoties_fmtr().Bld_bfr_many(tmp_bfr, printfooter, wdata_lang_wtr);
		return tmp_bfr.To_bry_and_clear();
	}
	private byte[] Printfooter(Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page page, byte html_gen_tid) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		// Add retrieved from for printing and as a hook for wikisource (and others)
		tmp_bfr.Add(aref);
		tmp_bfr.Add(gplx.xowa.htmls.hrefs.Xoh_href_.Bry__wiki);
		tmp_bfr.Add(page.Ttl().Full_db_w_anch());
		tmp_bfr.Add(aref2);
		tmp_bfr.Add(page.Ttl().Full_url());
		tmp_bfr.Add(endaref);
		byte[] retrieved_msg = wiki.Msg_mgr().Val_by_key_args(Key_retrieved, tmp_bfr.To_bry_and_clear());
		Bry_fmt retreive_fmt = Bry_fmt.Auto("<div class=\"printfooter\">~{0}</div>");
		retreive_fmt.Bld_many(tmp_bfr, retrieved_msg);

		// handle Categories at bottom of page;
		//int ctgs_len = page.Wtxt().Ctgs__len();
		if (	ctgs_enabled
			//&&	ctgs_len > 0						// skip if no categories found while parsing wikitext
			&&	!wiki.Html_mgr().Importing_ctgs()	// do not show categories if importing categories, page will wait for category import to be done; DATE:2014-10-15
			&&	!hctx.Mode_is_hdump_only()				// do not dump categories during hdump; DATE:2016-10-12
			&& page.Ttl().Ns().Id() >= 0 // not Special
			&& html_gen_tid != Xopg_view_mode_.Tid__edit // not Edit page
			) {
			//if (app.Mode().Tid_is_gui()) app.Usr_dlg().Prog_many("", "", "loading categories: count=~{0}", ctgs_len);
			//wiki.Ctg__pagebox_wtr().Write_pagebox(tmp_bfr, wiki, page);
			wiki.Ctg__pagebox_wtr().Write_pagebox(tmp_bfr, page);
		}

		Add_stats(tmp_bfr, page);

		return tmp_bfr.To_bry_and_clear();
	}
	private byte[] Get_text(Json_nde titles_nde) {
		int titles_len = titles_nde.Len();
		for (int k = 0; k < titles_len; ++k) {
			Json_kv title_itm = Json_kv.Cast(titles_nde.Get_at(k));
			String langkey = title_itm.Key_as_str();
			if (langkey.equals("en"))
				return title_itm.Val_as_bry();
		}
		if (titles_len > 0) {
			Json_kv title_itm = Json_kv.Cast(titles_nde.Get_at(0));
			return title_itm.Val_as_bry();
		}
		return Bry_.Empty;
	}
// logic from https://github.com/wikimedia/mediawiki-extensions-JsonConfig/includes/JCTabularContentView.php
	public void Jdoc_data_writer(Bry_bfr bfr, Json_doc jdoc) {
		Json_nde desc_nde = Json_nde.Cast(jdoc.Get_grp(Bry_.new_a7("description")));
		if (desc_nde == null)
			return;
		Json_nde list_nde = Json_nde.Cast(jdoc.Get_grp(Bry_.new_a7("schema")));
		if (list_nde == null)
			return;
		Json_ary data_ary = Json_ary.cast(jdoc.Get_grp(Bry_.new_a7("data")));
		if (data_ary == null)
			return;
		byte[] sources = jdoc.Get_val_as_bry_or(Bry_.new_a7("sources"), Bry_.Empty);
		byte[] license = jdoc.Get_val_as_bry_or(Bry_.new_a7("license"), Bry_.Empty);
		Xow_msg_mgr msg_mgr = wiki.Msg_mgr();
		byte[] tmp = Bry_.Add(Bry_.new_a7("jsonconfig-license-name-"), license);
		byte[] license_name = Db_expand.Extracheck( msg_mgr.Val_by_key_args(tmp), "");
		tmp = Bry_.Add(Bry_.new_a7("jsonconfig-license-url-"), license);
		byte[] license_url = Db_expand.Extracheck( msg_mgr.Val_by_key_args(tmp), "");
		byte[] license_a = Bry_.Add(
		  Bry_.new_a7("<a href=\"")
		, license_url
		, Bry_.new_a7("\">")
		, license_name
		, Bry_.new_a7("</a>")
		);
		byte[] license_txt = Bry_.Add(
		  Bry_.new_a7("<p class=\"mw-jsonconfig-license\">")
		, Db_expand.Extracheck( msg_mgr.Val_by_key_args(Bry_.new_a7("jsonconfig-license"), license_a), "")
		, Bry_.new_a7("</p>")
		);
		byte[] sources_txt = Bry_.Add(
		  Bry_.new_a7("<p class=\"mw-jsonconfig-sources\">")
		, Db_expand.Extracheck( sources, "")
		, Bry_.new_a7("</p>")
		);

		int data_rows_len = data_ary.Len();
		// check enough keys
		Json_kv fields_nde = Json_kv.Cast(list_nde.Get_at(0));
		// check key is 'fields'
		//String key = fields_nde.Key_as_str();
		Json_ary fields_itms_ary = Json_ary.cast_or_null(fields_nde.Val());
		int fields_itms_len = fields_itms_ary.Len();
		data_head[] dh = new data_head[fields_itms_len];
		for (int i = 0; i < fields_itms_len; ++i) {
			dh[i] = new data_head();
			Json_nde field_itm_nde = Json_nde.Cast(fields_itms_ary.Get_at(i));
			int field_itm_len = field_itm_nde.Len();
			for (int j = 0; j < field_itm_len; ++j) {
				Json_kv itm_nde = Json_kv.Cast(field_itm_nde.Get_at(j));
				String itmkey = itm_nde.Key_as_str();
				if (itmkey.equals("name"))
					dh[i].Name_(itm_nde.Val_as_bry());
				else if (itmkey.equals("type"))
					dh[i].type = itm_nde.Val_as_bry();
				else if (itmkey.equals("title")) {
					Json_nde title_nde = itm_nde.Val_as_nde();
					dh[i].title = Get_text(title_nde);
				}
			}

			//String jtxt = field_itm_nde.Print_as_json();
			//System.out.print(jtxt);
		}
		bfr.Add_str_a7("<p class=\"mw-jsonconfig-description\">");
		bfr.Add(Get_text(desc_nde));
		bfr.Add_str_a7("</p>\n<table class=\"mw-tabular sortable\">\n");
		bfr.Add_str_a7("<tr class=\"mw-tabular-row-key\">");
		for (int i = 0; i < fields_itms_len; ++i) {
			bfr.Add_str_a7("<th data-name=\"");
			bfr.Add(dh[i].name);
			bfr.Add_str_a7("\" data-type=\"");
			bfr.Add(dh[i].type);
			bfr.Add_str_a7("\">");
			bfr.Add(dh[i].name);
			bfr.Add_str_a7("</th>");
		}
		bfr.Add_str_a7("</tr>\n<tr class=\"mw-tabular-row-type\">");
		for (int i = 0; i < fields_itms_len; ++i) {
			bfr.Add_str_a7("<th data-name=\"");
			bfr.Add(dh[i].name);
			bfr.Add_str_a7("\" data-type=\"");
			bfr.Add(dh[i].type);
			bfr.Add_str_a7("\">");
			bfr.Add(dh[i].type);
			bfr.Add_str_a7("</th>");
		}
		bfr.Add_str_a7("</tr>\n<tr class=\"mw-tabular-row-name\">");
		for (int i = 0; i < fields_itms_len; ++i) {
			bfr.Add_str_a7("<th data-name=\"");
			bfr.Add(dh[i].name);
			bfr.Add_str_a7("\" data-type=\"");
			bfr.Add(dh[i].type);
			bfr.Add_str_a7("\">");
			if (dh[i].title == null)
				bfr.Add(dh[i].name);
			else
				bfr.Add(dh[i].title);
			bfr.Add_str_a7("</th>");
		}
		bfr.Add_str_a7("</tr>\n");
		for (int j = 0; j < data_rows_len; ++j) {
			Json_ary itms = Json_ary.cast(data_ary.Get_at(j));
			bfr.Add_str_a7("<tr>");
			for (int i = 0; i < fields_itms_len; ++i) {
				Json_itm itm = itms.Get_at(i);
				bfr.Add_str_a7("<td data-type=\"");
				bfr.Add(dh[i].type);
				if (itm instanceof Json_itm_null)
					bfr.Add_str_a7("\" class=\"mw-tabular-value-null");
				bfr.Add_str_a7("\">");
				if (itm instanceof Json_itm_str || itm instanceof Json_itm_int || itm instanceof Json_itm_long || itm instanceof Json_itm_decimal)
					bfr.Add(itm.Data_bry());
				else if (itm instanceof Json_nde) {
					bfr.Add(Get_text((Json_nde)itm));
					bfr.Add_str_a7("<span class=\"mw-tabular-value-info\">(");
					bfr.Add_int_variable(((Json_nde) itm).Len());
					bfr.Add_str_a7(")</span>");
				}
				else if (itm instanceof Json_itm_bool) {
					if (((Json_itm_bool)itm).Data_as_bool())
						bfr.Add_str_u8("☑");
					else
						bfr.Add_str_u8("☐");
				}
				bfr.Add_str_a7("</td>");
			}
			bfr.Add_str_a7("</tr>\n");
		}
		bfr.Add_str_a7("</table>\n");
		bfr.Add(sources_txt);
		bfr.Add(license_txt);

		//String oput = bfr.To_str_and_clear();
		//System.out.print(oput);
	}
	private void Set_ispage_in_wikisource(Xoae_page page) {
		if (wiki.Domain_tid() == Xow_domain_tid_.Tid__wikisource && page.Ttl().Ns().Id() == wiki.Ns_mgr().Ns_page_id())
			ispage_in_wikisource = true;
		else
			ispage_in_wikisource = false;
	}
	private void Add_stats(Bry_bfr bfr, Xoae_page page) {
		Xop_log_stat stats = page.Stat_itm();
		counts_fmtr.Bld_bfr_many(bfr
			, stats.Tidy_time
			, stats.Image_count
			, stats.Audio_count
			, stats.Video_count
			, stats.Media_count
			, stats.Hdr_count
			, stats.Lnki_count
			, stats.Lnke_count
			, stats.Math_count
			, stats.Imap_count
			, stats.Hiero_count
			, stats.Gallery_count
			, stats.Gallery_packed_count
			, stats.Scrib().Time()
			, stats.Scrib().Count()
			, stats.Scrib().Depth_max()
			, stats.Scrib().PageTime()
		);
	}
	private static final Bry_fmtr counts_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "<!--"
	, "Counts:"
	, " Tidy_time: ~{t1}"
	, " Image_count: ~{t2}"
	, " Audio_count: ~{t3}"
	, " Video_count: ~{t4}"
	, " Media_count: ~{t5}"
	, " Hdr_count: ~{t6}"
	, " Lnki_count: ~{t7}"
	, " Lnke_count: ~{t8}"
	, " Math_count: ~{t9}"
	, " Imap_count: ~{t10}"
	, " Hiero_count: ~{t11}"
	, " Gallery_count: ~{t12}"
	, " Gallery_packed_count: ~{t13}"
	, "Scribunto:"
	, " Time: ~{s1}"
	, " Count: ~{s2}"
	, " Max_depth: ~{s3}"
	, "Total time:"
	, "  ~{p1}"
	, "-->"
	), "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11", "t12", "t13", "s1", "s2", "s3", "p1");
}
class data_head {
	public void Name_(byte[] val) {name = val;}; byte[] name;
	public byte[] type;
	public byte[] title;
	public data_head() {
		name = null; type = null; title = null;
	}
	public void Init() {
		name = null; type = null; title = null;
	}
}
