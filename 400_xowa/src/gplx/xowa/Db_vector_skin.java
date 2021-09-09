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
import gplx.xowa.xtns.wbases.Wdata_xwiki_link_wtr;

import gplx.xowa.langs.msgs.Xow_msg_mgr;
import gplx.xowa.Db_expand;
import gplx.langs.jsons.*;
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
import gplx.Gfo_usr_dlg_;
import gplx.xowa.htmls.Xoh_page_wtr_wkr_;
import gplx.xowa.htmls.Xoh_page_wtr_mgr;
public class Db_vector_skin implements Db_skin {
	private String skintags = "";
	public String Skintags() {return skintags;}

	public byte[] Content(Xow_portal_mgr portal_mgr, byte[] page_data, Xoae_page page, Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte html_gen_tid, byte[] pagename_for_h1, byte[] modified_on_msg, int page_mode, Bry_bfr tmp_bfr, boolean ispage_in_wikisource, Wdata_xwiki_link_wtr wdata_lang_wtr, boolean ctgs_enabled, Xoh_page_wtr_mgr mgr) {
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();
                Xoae_app app = wiki.Appe();
		Xoa_ttl page_ttl = page.Ttl();

		Json_nde data = Json_nde.NewByVal();

		boolean is_legacy = !page.Page_skin().equals("vector-new");

		data.AddKvBool("is-consolidated-user-links", false);
//			'is-consolidated-user-links' => $this->shouldConsolidateUserLinks(),

		data.AddKvBool("is-article", true); // strictly Special: is not an article
//			'is-article' => (bool)$out->isArticle(),

		data.AddKvBool("is-mainpage", Bry_.Eq(page.Ttl().Page_db(), wiki.Props().Main_page()));
//			'is-mainpage' => $title->isMainPage(),
			// Remember that the string '0' is a valid title.
			// From OutputPage::getPageTitle, via ::setPageTitle().
// 			'html-title' => $out->getPageTitle(),// this is set in Xopg_page_heading

		if (page_mode == Xopg_view_mode_.Tid__read) // only generate categories if READ
			data.AddKvStr("html-categories", Categories(portal_mgr, wiki, ctx, hctx, page, html_gen_tid, wdata_lang_wtr, ctgs_enabled));
//			'html-categories' => $skin->getCategories(),

		data.AddKvStr("input-location", is_legacy ? "header-navigation" : "header-moved");
//			'input-location' => $this->getSearchBoxInputLocation(),

		data.AddKvBool("sidebar-visible", true); // for skin.mustache
//			'sidebar-visible' => $this->isSidebarVisible(),

		data.AddKvBool("is-language-in-header", false); // for now
//?			'is-language-in-header' => $this->isLanguagesInHeader(),

		data.AddKvBool("should-search-expand", false); // for now
//?			'should-search-expand' => $this->shouldSearchExpand(),

		data.AddKvStr("html-user-language-attributes", "");

		Xopg_page_heading ph = page.Html_data().Page_heading();
		ph.Init(wiki, html_gen_tid == Xopg_view_mode_.Tid__read, page.Html_data(), page.Ttl().Full_db(), pagename_for_h1, page.Lang().Key_bry());
		ph.Build_json(data);

		data.AddKvStr("html-subtitle", Xoh_page_wtr_wkr_.Bld_page_content_sub(app, wiki, page, tmp_bfr, isnoredirect));
		data.AddKvStr("html-body-content", Pagebody(portal_mgr, page_data, page, page_mode, wiki, mgr));

		page.Html_data().Indicators().Build_json(data);
		portal_mgr.Sidebar_mgr().Build_json(data);

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

		data.AddKvStr("portal_div_footer", portal_mgr.Div_footer(modified_on_msg, Xoa_app_.Version, Xoa_app_.Build_date));

		if (wiki.Domain_tid() == Xow_domain_tid_.Tid__wikivoyage)
			data.AddKvStr("html-after-content", "<div id='mw-data-after-content'>\n	<div class=\"read-more-container\"></div>\n</div>\n");

		build_msg(data, wiki);

		Build_json_logos(data, String_.new_a7(page.Lang().Key_bry()));
		Build_json_search(data, wiki);

		//System.out.println(data.Print_as_json());

		Render_Content(wiki, tmp_bfr, data, is_legacy);
		return tmp_bfr.To_bry_and_clear();
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
		return String_.new_u8(wiki.Lang().Msg_mgr().Val_by_str_or_empty(wiki, msgkey));
	}
	private String tooltip(String tipkey, Xowe_wiki wiki) {
		return String_.new_u8(wiki.Msg_mgr().Val_html_accesskey_and_title(tipkey));
	}

	private static String[] msgs = new String[] {
						"tagline",
						"search",
						"vector-opt-out-tooltip",
						"vector-opt-out",
						"navigation-heading",
						"vector-action-toggle-sidebar",
						"vector-jumptonavigation",
						"vector-jumptosearch",
						"vector-jumptocontent",
						"sitesubtitle",
						"sitetitle"
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
				data.AddKvStr("msg-" + msg, msg_mgr.Val_by_str_or_empty(wiki, msg));
		}
	}
	private boolean once = true;
	private Mustache_tkn_itm root_legacy;
	private Mustache_tkn_itm root_new;
	private void Render_Content(Xowe_wiki wiki, Bry_bfr bfr, Json_nde data, boolean is_legacy) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates");
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root);
			root_legacy = parser.Parse("xowa-legacy"); // aka skin-legacy
			//parser = new Mustache_tkn_parser(template_root);
			//// it seem frwiki is at a different stage of development 20210525
			//template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates_xowa");
			//parser = new Mustache_tkn_parser(template_root);
			root_new = parser.Parse("xowa"); // aka skin
		}
		Mustache_render_ctx mctx = new Mustache_render_ctx().Init(new JsonMustacheNde(data));
		Mustache_bfr mbfr = Mustache_bfr.New_bfr(bfr);
		if (is_legacy) {
			root_legacy.Render(mbfr, mctx);
			skintags = "skin-vector action-view skin-vector-legacy";
		}
		else {
			root_new.Render(mbfr, mctx);
			//vectortags = " skin-vector-max-width skin-vector-search-header";
			// as of 20210526
			skintags = "skin-vector action-view skin-vector-search-vue";
		}
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
	private Json_nde dl = null;
	private void Build_json_logos(Json_nde data, String lang) {
/* to produce
"data-logos": {
	"link-mainpage":"/wiki/",
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

			dl.AddKvStr("link-mainpage", "/wiki/");
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
		private static final byte[] aref = Bry_.new_a7("<a href=\"")
		, aref2 = Bry_.new_a7("\">")
		, endaref = Bry_.new_a7("</a>")
	, Key_lastmodifiedat = Bry_.new_a7("lastmodifiedat")
	, Key_retrieved = Bry_.new_a7("retrievedfrom")
		;

}

//Db_minerva_skin implements Db_skin

//		skintags = "stable skin-minerva action-view skin--responsive minerva--history-page-action-enabled";
