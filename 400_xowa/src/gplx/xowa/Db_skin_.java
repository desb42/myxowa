/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2021 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa;
import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_fmt;
import gplx.Bry_bfr_;
import gplx.String_;
import gplx.langs.jsons.*;
import gplx.xowa.wikis.pages.Xopg_page_heading;
import gplx.xowa.wikis.pages.Xopg_view_mode_;
import gplx.xowa.htmls.Xoh_page_wtr_wkr_;
import gplx.xowa.htmls.Xoh_page_wtr_mgr;
import gplx.xowa.htmls.portal.Xow_portal_mgr;
import gplx.xowa.xtns.wbases.Wdata_xwiki_link_wtr;
import gplx.xowa.parsers.Xop_ctx;
import gplx.xowa.htmls.core.htmls.Xoh_wtr_ctx;
import gplx.xowa.wikis.domains.Xow_domain_tid_;
import gplx.xowa.langs.msgs.Xol_msg_mgr;
import gplx.xowa.addons.htmls.sidebars.Db_Nav_template;
import gplx.core.brys.fmtrs.Bry_fmtr;
public class Db_skin_ {
	private final Xow_portal_mgr portal_mgr;
	private final byte[] page_data;
	private final Xoae_page page;
	private final Xowe_wiki wiki;
	private final Xop_ctx ctx;
	private final Xoh_wtr_ctx hctx;
	private final byte html_gen_tid;
	private final byte[] pagename_for_h1;
	private final byte[] modified_on_msg;
	private final int page_mode;
	private final boolean ispage_in_wikisource;
	private final Wdata_xwiki_link_wtr wdata_lang_wtr;
	private final boolean ctgs_enabled;
	private final Xoh_page_wtr_mgr mgr;
	private final String[] msgs;
	public Xoae_page Page() { return page; }
	public Xowe_wiki Wiki() { return wiki; }

	public Db_skin_(Xow_portal_mgr portal_mgr, byte[] page_data, Xoae_page page, Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte html_gen_tid, byte[] pagename_for_h1, byte[] modified_on_msg, int page_mode, boolean ispage_in_wikisource, Wdata_xwiki_link_wtr wdata_lang_wtr, boolean ctgs_enabled, Xoh_page_wtr_mgr mgr, String[] msgs) {
		this.portal_mgr = portal_mgr;
		this.page_data = page_data;
		this.page = page;
		this.wiki = wiki;
		this.ctx = ctx;
		this.hctx = hctx;
		this.html_gen_tid = html_gen_tid;
		this.pagename_for_h1 = pagename_for_h1;
		this.modified_on_msg = modified_on_msg;
		this.page_mode = page_mode;
		this.ispage_in_wikisource = ispage_in_wikisource;
		this.wdata_lang_wtr = wdata_lang_wtr;
		this.ctgs_enabled = ctgs_enabled;
		this.mgr = mgr;
		this.msgs = msgs;
    }
// from https://github.com/wikimedia/mediawiki/blob/6b115843ed93d8b2eb0a309caa9b3ae52b24bf86/includes/skins/SkinMustache.php
// and
// from https://github.com/wikimedia/mediawiki/blob/292fa3ae54008ed03e0ea74863291a268b4a2636/includes/skins/Skin.php

	public Json_nde getTemplateData() {
		Xoae_app app = wiki.Appe();
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();

		Json_nde data = Json_nde.NewByVal();
		Xopg_page_heading ph = page.Html_data().Page_heading();
		ph.Init(wiki, html_gen_tid == Xopg_view_mode_.Tid__read, page.Html_data(), page.Ttl().Full_db(), pagename_for_h1, page.Lang().Key_bry());
		Bry_bfr tmp_bfr = Bry_bfr_.New();

		// https://github.com/wikimedia/mediawiki/blob/292fa3ae54008ed03e0ea74863291a268b4a2636/includes/skins/SkinTemplate.php#L416
		byte[] bodytext = Pagebody(portal_mgr, page_data, page, page_mode, wiki, mgr);
		data.AddKvStr("bodytext", bodytext);

		// https://github.com/wikimedia/mediawiki/blob/292fa3ae54008ed03e0ea74863291a268b4a2636/includes/skins/SkinTemplate.php#L451
		data.AddKvStr("bodycontent", bodytext);

		// https://github.com/wikimedia/mediawiki/blob/6b115843ed93d8b2eb0a309caa9b3ae52b24bf86/includes/skins/SkinMustache.php#L88
		data.AddKvStr("html-body-content", bodytext);

		data.AddKvAry("array-indicators", page.Html_data().Indicators().Build_json());

		// HTML strings
		//data.AddKvStr("html-site-notice", null);
		//data.AddKvStr("html-user-message", null);
/*			'html-title-heading' => Html::rawElement(
				'h1',
				[
					'id' => 'firstHeading',
					'class' => 'firstHeading mw-first-heading',
					'style' => $blankedHeading ? 'display: none' : null
				] + $this->getUserLanguageAttributes(),
				$htmlTitle
			),*/
		Bry_fmtr tmp_fmtr = Bry_fmtr.New__tmp().Fail_when_invalid_escapes_(false);
		tmp_fmtr.Fmt_("<h1 id=\"firstHeading\" class=\"firstHeading mw-first-heading\">~{0}</h1>").Bld_bfr_many(tmp_bfr, ph.getPageTitle());
		data.AddKvStr("html-title-heading", tmp_bfr.To_str_and_clear());
			
		data.AddKvStr("html-title", ph.getPageTitle());
		data.AddKvStr("html-subtitle", Xoh_page_wtr_wkr_.Bld_page_content_sub(app, wiki, page, tmp_bfr, isnoredirect));
		data.AddKvStr("html-body-content", Pagebody(portal_mgr, page_data, page, page_mode, wiki, mgr));
		if (page_mode == Xopg_view_mode_.Tid__read) // only generate categories if READ
			data.AddKvStr("html-categories", Categories(portal_mgr, wiki, ctx, hctx, page, html_gen_tid, wdata_lang_wtr, ctgs_enabled));

		if (wiki.Domain_tid() == Xow_domain_tid_.Tid__wikivoyage)
			data.AddKvStr("html-after-content", "<div id='mw-data-after-content'>\n	<div class=\"read-more-container\"></div>\n</div>\n");

		//data.AddKvStr("html-undelete-link", null);
		//data.AddKvStr("html-user-language-attributes", "");

		// links
		data.AddKvStr("link-mainpage", "/wiki/");

		//all messages should be preprocessed (per language) as $data["msg-{$message}"] = $this->msg( $message )->text();
		Xol_msg_mgr msg_mgr = wiki.Lang().Msg_mgr();
		int msg_len = msgs.length;
		for (int i = 0; i < msg_len; i++) {
			String msg = msgs[i];
			if (msg.equals("tagline"))
				data.AddKvStr("msg-" + msg, wiki.Tagline());
			else
				data.AddKvStr("msg-" + msg, msg_mgr.Val_by_str_or_empty(wiki, msg));
		}

		getTemplateData(data);

		return data;
	}
// https://github.com/wikimedia/mediawiki/blob/292fa3ae54008ed03e0ea74863291a268b4a2636/includes/skins/SkinTemplate.php#283
	private void getTemplateData(Json_nde data) {

		// Data objects
		data.AddKvNde("data-search-box", buildSearchProps());
		data.AddKvNde("data-logos", getLogoData());

		boolean isarticle = true;
		// check for Special:
		
		// Boolean values
		data.AddKvBool("is-anon", true);
		data.AddKvBool("is-article", isarticle); // strictly Special: is not an article
		data.AddKvBool("is-mainpage", Bry_.Eq(page.Ttl().Page_db(), wiki.Props().Main_page()));
		data.AddKvBool("is-specialpage", !isarticle);

//		] + $this->getPortletsTemplateData() + $this->getFooterTemplateData();
		getPortletsTemplateData(data);

		data.AddKvStr("portal_div_footer", portal_mgr.Div_footer(modified_on_msg, Xoa_app_.Version, Xoa_app_.Build_date));

	}
	private Json_nde buildSearchProps() {

		Json_nde ds = Json_nde.NewByVal();
		ds.AddKvStr("form-action", "/wiki/Special:XowaSearch");
		ds.AddKvStr("html-button-search-fallback", makeSearchButton("fulltext", wiki));
		ds.AddKvStr("html-button-search", makeSearchButton("go", wiki));
		ds.AddKvStr("html-input", "<input type=\"search\" name=\"search\" id=\"searchInput\"/>");
		ds.AddKvStr("page-title", "Special:XowaSearch");
		ds.AddKvStr("html-button-go-attributes", "class=\"searchButton\" type=\"submit\" name=\"go\" " + tooltip( "search-go", wiki ));
		ds.AddKvStr("html-button-fulltext-attributes", "class=\"searchButton mw-fallbackSearchButton\" type=\"submit\" name=\"fulltext\" " + tooltip( "search-fulltext", wiki ));
		ds.AddKvStr("html-input-attributes", "type=\"search\" name=\"search\" placeholder=\""
			 + msgvalue("searchsuggest-search", wiki) 
			 + "\"" + tooltip( "search", wiki ));
		return ds;
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
	private byte[] Pagebody(Xow_portal_mgr portal_mgr, byte[] page_data, Xoae_page page, int page_mode, Xowe_wiki wiki, Xoh_page_wtr_mgr mgr) {
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
				// "edit_div_editnotices", "edit_div_preview", "edit_lang", "edit_lang_ltr", "edit_div_rename", "page_data", "page_text", "page_ttl_full"
				portal_mgr.Txt_pageedit_fmtr().Bld_bfr_many(tmp_bfr
					, Editnotices(page.Ttl().Ns().Id(), wiki)
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
	private byte[] Categories(Xow_portal_mgr portal_mgr, Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page page, byte html_gen_tid, Wdata_xwiki_link_wtr wdata_lang_wtr, boolean ctgs_enabled) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		byte[] printfooter = Printfooter(wiki, ctx, hctx, page, html_gen_tid, ctgs_enabled);
		portal_mgr.Txt_categoties_fmtr().Bld_bfr_many(tmp_bfr, printfooter, wdata_lang_wtr);
		return tmp_bfr.To_bry_and_clear();
	}
	private byte[] Printfooter(Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoae_page page, byte html_gen_tid, boolean ctgs_enabled) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		// Add retrieved from for printing and as a hook for wikisource (and others)
		tmp_bfr.Add(aref);
		tmp_bfr.Add(gplx.xowa.htmls.hrefs.Xoh_href_.Bry__wiki);
		tmp_bfr.Add(page.Ttl().Full_txt_raw_unders()); //.Full_db_w_anch());
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

		page.Stat_itm().Add_stats(tmp_bfr);

		return tmp_bfr.To_bry_and_clear();
	}
	private Json_nde getLogoData() {
		Json_nde dl = Json_nde.NewByVal();
		Json_nde wm = Json_nde.NewByVal();
		Json_nde tl = Json_nde.NewByVal();

		String wikikey = wiki.Domain_itm().Domain_type().Key_str();
		String wikiimg;
		if (wikikey.equals("wikipedia"))
			wikiimg = wikikey + ".png";
		else
			wikiimg = wikikey + ".svg";

		String imgroot = wiki.App().Fsys_mgr().Root_dir().To_http_file_str();
		dl.AddKvStr("icon", imgroot + "static/images/mobile/copyright/" + wikiimg);

		String img = wiki.Skin_mgr().Get_wordmark_img();
		if (img.equals("*"))
			dl.AddKvBool("wordmark", false);
		else {
			wm.AddKvStr("src", imgroot + img);
			wm.AddKvStr("width", wiki.Skin_mgr().Get_wordmark_width());
			wm.AddKvStr("height", wiki.Skin_mgr().Get_wordmark_height());
			dl.AddKvNde("wordmark", wm);
		}

		img = wiki.Skin_mgr().Get_tagline_img();
		if (img.equals("*"))
			dl.AddKvBool("tagline", false);
		else {
			tl.AddKvStr("src", imgroot + img);
			tl.AddKvStr("width", wiki.Skin_mgr().Get_tagline_width());
			tl.AddKvStr("height", wiki.Skin_mgr().Get_tagline_height());
			dl.AddKvNde("tagline", tl);
		}
		return dl;
	}
	public String msgvalue(String msgkey, Xowe_wiki wiki) {
		return String_.new_u8(wiki.Lang().Msg_mgr().Val_by_str_or_empty(wiki, msgkey));
	}
	public String tooltip(String tipkey, Xowe_wiki wiki) {
		return String_.new_u8(wiki.Msg_mgr().Val_html_accesskey_and_title(tipkey));
	}

	private byte[] Editnotices(int ns, Xowe_wiki wiki) {
		String en = "editnotice-" + Integer.toString(ns);
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		byte[] editnotices = Db_expand.Extracheck(wiki.Msg_mgr().Val_by_key_obj(en), ""); // this should be the full blown expansion!
		if (editnotices.length > 0) {
			Bry_fmt en_fmt = Bry_fmt.Auto("<div class=\"mw-editnotice mw-editnotice-namespace mw-~{0}\">~{1}</div>");
			en_fmt.Bld_many(tmp_bfr, Bry_.new_a7(en), editnotices);
		}
		return tmp_bfr.To_bry_and_clear();
	}
	private static final byte[] aref = Bry_.new_a7("<a href=\"")
	, aref2 = Bry_.new_a7("\">")
	, endaref = Bry_.new_a7("</a>")
	, Key_lastmodifiedat = Bry_.new_a7("lastmodifiedat")
	, Key_retrieved = Bry_.new_a7("retrievedfrom")
	;
	private void getPortletsTemplateData(Json_nde data) {
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();
		Xoa_ttl page_ttl = page.Ttl();
		Json_nde portlets = Json_nde.NewByVal();
		//data.AddKvStr("portal_div_personal", portal_mgr.Div_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect));
		portlets.AddKvNde("data-personal",
			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("personal"), Bry_.new_a7("Personal"), portal_mgr.Txt_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect))
		);

		//data.AddKvStr("portal_div_ns", portal_mgr.Div_ns_bry(wiki, page_ttl, ispage_in_wikisource, page));
		portlets.AddKvNde("data-namespaces",
			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("namespaces"), Bry_.new_a7("Namespaces"), portal_mgr.Txt_ns_bry(wiki, page_ttl, ispage_in_wikisource, page))
		);

		// possibly data-variants

		//data.AddKvStr("portal_div_view", portal_mgr.Div_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect));
		portlets.AddKvNde("data-views",
			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("views"), Bry_.new_a7("Views"), portal_mgr.Txt_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect))
		);

		//  possibly data-actions

		data.AddKvNde("data-portlets", portlets);
		portal_mgr.Sidebar_mgr().Build_json(data);

	}
}
