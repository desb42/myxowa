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

	public byte[] Content(Json_nde data, Bry_bfr tmp_bfr, Db_skin_ skin) {
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

    
		vector_search(data, is_legacy, wiki);

		//System.out.println(data.Print_as_json());

		Render_Content(wiki, tmp_bfr, data, is_legacy);
		return tmp_bfr.To_bry_and_clear();
	}

	public String[] MsgStrs() { return msgs; }
	private static String[] msgs = new String[] {
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

}

//Db_minerva_skin implements Db_skin

//		skintags = "stable skin-minerva action-view skin--responsive minerva--history-page-action-enabled";
