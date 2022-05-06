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
import gplx.Hash_adp;
import gplx.Hash_adp_;
import gplx.xowa.htmls.Xoh_page_wtr_wkr_;
import gplx.xowa.htmls.Xoh_page_wtr_mgr;
public class Db_minerva_skin implements Db_skin {
	private String skintags = "stable skin-minerva action-view skin--responsive minerva--history-page-action-enabled";
	public String Skintags() {return skintags;}

	public byte[] Content(Json_nde data, Bry_bfr tmp_bfr, Db_skin_ skin) {
		Xowe_wiki wiki = skin.Wiki();

//		$banners = [ '<div id="siteNotice"></div>' ]; // includes\Skins\SkinMinerva.php
		data.AddKvStr("banners", Bry_.new_a7("<div id=\"siteNotice\"></div>"));

		data.AddKvBool("isAnon", false);
/*
			$content = Html::openElement( 'div', [
				'id' => 'bodyContent',
				'class' => 'content',
			] );
			$content .= $data[ 'bodytext' ];
			if ( isset( $data['subject-page'] ) ) {
				$content .= $data['subject-page'];
			}
*/
		String bodycontent = data.Get_as_str("html-body-content");

		data.AddKvStr("contenthtml", "<div id=\"bodyContent\" class=\"content\">\n" + bodycontent + "</div>\n");

//			'placeholder' => wfMessage( 'mobile-frontend-placeholder' )
		data.AddKvStr("placeholder", skin.msgvalue("mobile-frontend-placeholder", wiki));
//			'main-menu-tooltip' => $this->getMsg( 'mobile-frontend-main-menu-button-tooltip' ),
		data.AddKvStr("main-menu-tooltip", skin.msgvalue("mobile-frontend-main-menu-button-tooltip", wiki));
//			'mainPageURL' => Title::newMainPage()->getLocalURL(),
		data.AddKvStr("mainPageURL", "/wiki/");
//			'userNavigationLabel' => wfMessage( 'minerva-user-navigation' ),
		data.AddKvStr("main-menu-tooltip", skin.msgvalue("minerva-user-navigation", wiki));
/*
			'searchButton' => Html::rawElement( 'button', [
				'id' => 'searchIcon',
				'class' => MinervaUI::iconClass(
					'search-base20', 'element', 'skin-minerva-search-trigger', 'wikimedia'
				)
			], wfMessage( 'searchbutton' )->escaped() ),
*/
		data.AddKvStr("searchButton", "<button id=\"searchIcon\" class=\"mw-ui-icon mw-ui-icon-element mw-ui-icon-wikimedia-search-base20 skin-minerva-search-trigger mw-ui-button mw-ui-quiet\" type=\"submit\">Search</button>");
		// the word 'Search' should be wiki language sensitive

//			// Remember that the string '0' is a valid title.
//			// From OutputPage::getPageTitle, via ::setPageTitle().
//// 			'html-title' => $out->getPageTitle(),// this is set in Xopg_page_heading
//
//		if (page_mode == Xopg_view_mode_.Tid__read) // only generate categories if READ
//			data.AddKvStr("html-categories", Categories(portal_mgr, wiki, ctx, hctx, page, html_gen_tid, wdata_lang_wtr, ctgs_enabled));
////			'html-categories' => $skin->getCategories(),
//
//		data.AddKvStr("input-location", is_legacy ? "header-navigation" : "header-moved");
////			'input-location' => $this->getSearchBoxInputLocation(),
//
//		data.AddKvBool("sidebar-visible", true); // for skin.mustache
////			'sidebar-visible' => $this->isSidebarVisible(),
//
//		data.AddKvBool("is-language-in-header", false); // for now
////?			'is-language-in-header' => $this->isLanguagesInHeader(),
//
//		data.AddKvBool("should-search-expand", false); // for now
////?			'should-search-expand' => $this->shouldSearchExpand(),
//
//		data.AddKvStr("html-user-language-attributes", "");
//
		Xopg_page_heading ph = skin.Page().Html_data().Page_heading();

		data.AddKvStr("headinghtml", "<h1 id=\"section_0\">" + String_.new_u8(ph.getPageTitle()) + "</h1>");

		data.AddKvBool("hasheadingholder", true); // for now
//
//		data.AddKvStr("html-subtitle", Xoh_page_wtr_wkr_.Bld_page_content_sub(app, wiki, page, tmp_bfr, isnoredirect));
//
//		page.Html_data().Indicators().Build_json(data);
//		portal_mgr.Sidebar_mgr().Build_json(data);
//
//		Json_nde portlets = Json_nde.NewByVal();
//		//data.AddKvStr("portal_div_personal", portal_mgr.Div_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect));
//		portlets.AddKvNde("data-personal",
//			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("personal"), Bry_.new_a7("Personal"), portal_mgr.Txt_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect))
//		);
//
//		//data.AddKvStr("portal_div_ns", portal_mgr.Div_ns_bry(wiki, page_ttl, ispage_in_wikisource, page));
//		portlets.AddKvNde("data-namespaces",
//			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("namespaces"), Bry_.new_a7("Namespaces"), portal_mgr.Txt_ns_bry(wiki, page_ttl, ispage_in_wikisource, page))
//		);
//
//		// possibly data-variants
//
//		//data.AddKvStr("portal_div_view", portal_mgr.Div_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect));
//		portlets.AddKvNde("data-views",
//			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("views"), Bry_.new_a7("Views"), portal_mgr.Txt_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect))
//		);
//
//		//  possibly data-actions
//
//		data.AddKvNde("data-portlets", portlets);
//
//		if (wiki.Domain_tid() == Xow_domain_tid_.Tid__wikivoyage)
//			data.AddKvStr("html-after-content", "<div id='mw-data-after-content'>\n	<div class=\"read-more-container\"></div>\n</div>\n");
//
//		build_msg(data, wiki);
//
//		Build_json_logos(data, String_.new_a7(page.Lang().Key_bry()));
//		Build_json_search(data, wiki);

		//System.out.println(data.Print_as_json());

		Render_Content(wiki, tmp_bfr, data);
		return tmp_bfr.To_bry_and_clear();
	}
//
//        private String makeSearchButton(String mode, Xowe_wiki wiki) {
//		boolean isgo = mode.equals("go");
//		String input_html = "<input name=\""
//			+ mode
//			+ "\" type=\"submit\" id=\"" 
//			+ (isgo ? "searchButton" : "mw-searchButton")
//			+ "\"" + tooltip( "search-" + mode, wiki ) + " class=\""
//			+ (isgo ? "searchButton" : "searchButton mw-fallbackSearchButton")
//			+ "\" value=\""
//			+ msgvalue(isgo ? "searcharticle" : "searchbutton", wiki)
//			+ "\" />";
//
//		return input_html;
//	}
//	private String msgvalue(String msgkey, Xowe_wiki wiki) {
//		return String_.new_u8(wiki.Lang().Msg_mgr().Val_by_str_or_empty(wiki, msgkey));
//	}
//	private String tooltip(String tipkey, Xowe_wiki wiki) {
//		return String_.new_u8(wiki.Msg_mgr().Val_html_accesskey_and_title(tipkey));
//	}

	public String[] MsgStrs() { return msgs; }
	private static String[] msgs = new String[] {
						"tagline",
						"search",
						"navigation-heading",
						"sitesubtitle",
						"sitetitle"
					};

	private boolean once = true;
	private Mustache_tkn_itm skin_root;
	private void Render_Content(Xowe_wiki wiki, Bry_bfr bfr, Json_nde data) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-MinervaNeue", "templates");
                        Hash_adp hash = Hash_adp_.New();
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root, hash);
			skin_root = parser.Parse("skin"); // aka xowa
		}
		Mustache_render_ctx mctx = new Mustache_render_ctx().Init(new JsonMustacheNde(data));
		Mustache_bfr mbfr = Mustache_bfr.New_bfr(bfr);

		skin_root.Render(mbfr, mctx);
	}
	public void getTemplateData(Json_nde data, Db_skin_ skin, Xow_portal_mgr portal_mgr) {
		Xowe_wiki wiki = skin.Wiki();
		Xoae_page page = skin.Page();
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();
		Xoa_ttl page_ttl = page.Ttl();
		Json_nde portlets = Json_nde.NewByVal();
		//data.AddKvStr("portal_div_personal", portal_mgr.Div_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect));
/*		Json_nde personal = Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("personal"), Bry_.new_a7("Personal"), portal_mgr.Txt_personal_bry(page.Html_data().Hdump_exists(), page_ttl, skin.Html_gen_tid(), isnoredirect), is_legacy);
		portlets.AddKvNde("data-personal",
			personal
		);

		//data.AddKvStr("portal_div_ns", portal_mgr.Div_ns_bry(wiki, page_ttl, ispage_in_wikisource, page));
		portlets.AddKvNde("data-namespaces",
			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("namespaces"), Bry_.new_a7("Namespaces"), portal_mgr.Txt_ns_bry(wiki, page_ttl, skin.Ispage_in_wikisource(), page), is_legacy)
		);

		// possibly data-variants

		//data.AddKvStr("portal_div_view", portal_mgr.Div_view_bry(wiki.Utl__bfr_mkr(), html_gen_tid, page.Html_data().Xtn_search_text(), page_ttl, isnoredirect));
		portlets.AddKvNde("data-views",
			Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("views"), Bry_.new_a7("Views"), portal_mgr.Txt_view_bry(wiki.Utl__bfr_mkr(), skin.Html_gen_tid(), page.Html_data().Xtn_search_text(), page_ttl, isnoredirect), is_legacy)
		);

		//  possibly data-actions
*/
		data.AddKvNde("data-portlets", portlets);
		
	}
}

//Db_minerva_skin implements Db_skin

//		skintags = "stable skin-minerva action-view skin--responsive minerva--history-page-action-enabled";
