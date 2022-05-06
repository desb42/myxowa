/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

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
import gplx.langs.jsons.*;
import gplx.langs.mustaches.JsonMustacheNde;
import gplx.langs.mustaches.Mustache_bfr;
import gplx.langs.mustaches.Mustache_render_ctx;
import gplx.langs.mustaches.Mustache_tkn_itm;
import gplx.langs.mustaches.Mustache_tkn_parser;
import gplx.Io_url;
import gplx.Hash_adp;
import gplx.Hash_adp_;
import gplx.xowa.addons.htmls.sidebars.Db_Nav_template;
import gplx.xowa.htmls.portal.Xow_portal_mgr;
public class Db_vector_skin implements Db_skin {
	private String skintags = "";
	@Override public String Skintags() {return skintags;}

	@Override public byte[] Content(Json_nde data, Bry_bfr tmp_bfr, Db_skin_ skin) {
		Xowe_wiki wiki = skin.Wiki();

		boolean is_legacy = !skin.Page().Page_skin().equals("vector-new");

		data.AddKvBool("is-consolidated-user-links", false);
//			'is-consolidated-user-links' => $this->shouldConsolidateUserLinks(),

		data.AddKvStr("input-location", is_legacy ? "header-navigation" : "header-moved");
//			'input-location' => $this->getSearchBoxInputLocation(),

		data.AddKvBool("sidebar-visible", true); // for skin.mustache
//			'sidebar-visible' => $this->isSidebarVisible(),

		data.AddKvBool("is-language-in-header", false); // for now
//?			'is-language-in-header' => $this->isLanguagesInHeader(),

		data.AddKvBool("should-search-expand", false); // for now
//?			'should-search-expand' => $this->shouldSearchExpand(),

    
//			'is-language-in-content' => $this->isLanguagesInContent(),
//			'is-language-in-content-top' => $this->isLanguagesInContentAt( 'top' ),
//			'is-language-in-content-bottom' => $this->isLanguagesInContentAt( 'bottom' ),
		data.AddKvBool("is-language-in-content", false);
		data.AddKvBool("is-language-in-content-top", false);
		data.AddKvBool("is-language-in-content-bottom", false);

		vector_search(data, is_legacy, wiki);

		//System.out.println(data.Print_as_json());

		Render_Content(wiki, tmp_bfr, data, is_legacy);
		return tmp_bfr.To_bry_and_clear();
	}

	@Override public String[] MsgStrs() { return msgs; }
	private static final String[] msgs = new String[] {
						"tagline",
						"search",
						"navigation-heading",
						"sitesubtitle",
						"sitetitle",
						"vector-opt-out-tooltip",
						"vector-opt-out",
						"vector-action-toggle-sidebar",
						"vector-jumptonavigation",
						"vector-jumptosearch",
						"vector-jumptocontent",
						"vector-action-toggle-sidebar",
						"vector-main-menu-tooltip",
						"vector-menu-checkbox-expanded",
						"vector-menu-checkbox-collapsed"
					};

	private boolean once = true;
	private Mustache_tkn_itm root_legacy;
	private Mustache_tkn_itm root_new;
	private void Render_Content(Xowe_wiki wiki, Bry_bfr bfr, Json_nde data, boolean is_legacy) {
		if (once) {
			once = false;
			Io_url template_root = wiki.Appe().Fsys_mgr().Bin_any_dir().GenSubDir_nest("xowa", "xtns", "Skin-Vector", "templates");
                        Hash_adp hash = Hash_adp_.New();
			Mustache_tkn_parser parser = new Mustache_tkn_parser(template_root, hash);
			root_legacy = parser.Parse("skin-legacy"); // aka xowa-legacy
			root_new = parser.Parse("skin"); // aka xowa
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
	private void vector_search(Json_nde data, boolean is_legacy, Xowe_wiki wiki) {

		Json_nde ds = data.Get_as_nde("data-search-box");
		
		ds.AddKvBool("is-collapsible", !is_legacy);
		if (!is_legacy)
			// definitely vector-search-box-collapses
			// see https://github.com/wikimedia/Vector/blob/405b52054f26eb40b1a2aaee57d10ece55b401e5/includes/SkinVector.php#L459
			ds.AddKvStr("class", "vector-search-box-vue  vector-search-box-collapses  vector-search-box-show-thumbnail");
		ds.AddKvBool("is-primary", true);
		ds.AddKvBool("is-legacy", is_legacy);

		Json_nde dci = Json_nde.NewByVal();
		dci.AddKvStr("href", "Special:XowaSearch");
		dci.AddKvStr("label", wiki.Lang().Msg_mgr().Val_by_str_or_empty(wiki, "search"));
		dci.AddKvStr("icon", "wikimedia-search");
		dci.AddKvBool("is-quiet", true);
		dci.AddKvStr("class", "search-toggle");
		ds.AddKvNde("data-collapse-icon", dci);
	}
	public void getTemplateData(Json_nde data, Db_skin_ skin, Xow_portal_mgr portal_mgr) {
		Xowe_wiki wiki = skin.Wiki();
		Xoae_page page = skin.Page();
		boolean isnoredirect = page.Url().Qargs_mgr().IsNoRedirect();
		boolean is_legacy = !page.Page_skin().equals("vector-new");
		Xoa_ttl page_ttl = page.Ttl();
		Json_nde portlets = Json_nde.NewByVal();
		//data.AddKvStr("portal_div_personal", portal_mgr.Div_personal_bry(page.Html_data().Hdump_exists(), page_ttl, html_gen_tid, isnoredirect));
		Json_nde personal = Db_Nav_template.Build_Menu(wiki, Bry_.new_a7("personal"), Bry_.new_a7("Personal"), portal_mgr.Txt_personal_bry(page.Html_data().Hdump_exists(), page_ttl, skin.Html_gen_tid(), isnoredirect), is_legacy);
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

		data.AddKvNde("data-portlets", portlets);
		
		// and some new stuff
		if (!is_legacy) {
			Json_nde dvul = Json_nde.NewByVal();
			dvul.AddKvNde("data-user-more", personal);
			data.AddKvNde("data-vector-user-links", dvul);
		}
	}
}

//Db_minerva_skin implements Db_skin

//		skintags = "stable skin-minerva action-view skin--responsive minerva--history-page-action-enabled";
