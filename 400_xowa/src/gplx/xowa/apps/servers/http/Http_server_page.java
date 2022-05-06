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
package gplx.xowa.apps.servers.http;

import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_bfr_;
import gplx.Io_mgr;
import gplx.String_;
import gplx.core.envs.Runtime_;
import gplx.xowa.Db_readwrite;
import gplx.xowa.Xoa_ttl;
import gplx.xowa.Xoa_url;
import gplx.xowa.Xoae_app;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.Xowe_wiki_;
import gplx.xowa.apps.servers.Gxw_html_server;
import gplx.xowa.guis.views.Xog_tab_itm;
import gplx.xowa.specials.Xow_special_meta_;
import gplx.xowa.specials.xowa.errors.Xoerror_special;

import gplx.xowa.wikis.nss.Xow_ns_;
import gplx.xowa.Db_special_api;
import gplx.xowa.wikis.pages.Xopg_view_mode_;
import gplx.xowa.wikis.pages.dbs.Xopg_db_page;
import gplx.xowa.wikis.pages.lnkis.Xopg_redlink_mgr;
public class Http_server_page {
	private final    Xoae_app app;
	private Http_url_parser url_parser;
	public Http_server_page(Xoae_app app, Http_url_parser url_parser) {
		this.app = app;
		this.url_parser = url_parser;
	}
	public Xowe_wiki Wiki() {return wiki;} private Xowe_wiki wiki;
	public Xoa_url Url() {return url;} private Xoa_url url;
	public Xoa_ttl Ttl() {return ttl;} private Xoa_ttl ttl;
	public byte[] Ttl_bry() {return ttl_bry;} private byte[] ttl_bry;
	public Xog_tab_itm Tab() {return tab;} private Xog_tab_itm tab;
	public Xoae_page Page() {return page;} private Xoae_page page;
	public String Html() {return html;} private String html;
	public String Redlink() {return redlink;} private String redlink;
	public byte[] Redirect() {return redirect;} private byte[] redirect;
	public byte[] Content_type() {return content_type;} private byte[] content_type = Bry_.new_a7("Content-Type: text/html; charset=utf-8\n"); // default
	public static Http_server_page Make(Xoae_app app, Http_data__client data__client, Http_url_parser url_parser, byte retrieve_mode) {
	//Http_data__client data__client, byte[] wiki_domain, byte[] ttl_bry, byte[] qarg, byte retrieve_mode, byte mode, boolean popup_enabled, String popup_mode, String popup_id) {
		Http_server_page page = new Http_server_page(app, url_parser);
		if (!page.Make_url(url_parser.Wiki())) return page; // exit early if xwiki
		// check for Special:Api
		if (page.ttl.Ns().Id() == Xow_ns_.Tid__special) {
			if (Bry_.Has_at_bgn(page.ttl.Base_txt(), Bry_.new_a7("Api"))) {
                            //app.Wiki_mgr().Wdata_mgr().Wdata_wiki()
				page.html = String_.new_u8(Db_special_api.Gen(page, app.Wiki_mgr().Wdata_mgr().Wdata_wiki()));
				page.content_type = json_mime;
				return page;
			}
		}
		page.Make_page(data__client);
		// special first para processing
		if (url_parser.Action() == Xopg_view_mode_.Tid__firstpara) {
			page.content_type = json_mime;
			return page;
		}
		page.Make_html(retrieve_mode);
		return page;
	}
	public static final byte[] json_mime = Bry_.new_a7("Content-Type: application/json; charset=utf-8\n");
	public boolean Make_url(byte[] wiki_domain) {

		// get wiki
		wiki = (Xowe_wiki)app.Wiki_mgr().Get_by_or_make_init_y(wiki_domain); // assert init for Main_Page; EX:click zh.w on wiki sidebar; DATE:2015-07-19
/*            // check for a valid directory
		Io_url domain_dir = wiki.Fsys_mgr().Root_dir();
		if (!Io_mgr.Instance.ExistsDir(domain_dir)) {
                    this.redirect = Bry_.Add(Bry_.new_a7("fsys/nosuchdomain.html?link="), wiki_domain);
                    return false;
                }
*/
		if (!wiki.Installed()) {
			this.ttl = wiki.Ttl_parse(Xow_special_meta_.Itm__error.Ttl_bry());
			this.url = wiki.Utl__url_parser().Parse(Xoerror_special.Make_url__invalidWiki(wiki_domain));
			return true;
		}
		if (Runtime_.Memory_total() > Io_mgr.Len_gb) Xowe_wiki_.Rls_mem(wiki, true); // release memory at 1 GB; DATE:2015-09-11

		// get url
		// empty title returns main page; EX: "" -> "Main_Page"
		this.ttl_bry = url_parser.Page();
		if (Bry_.Len_eq_0(ttl_bry) || Bry_.Eq(ttl_bry, wiki.Props().Main_page())) {
			this.ttl_bry = wiki.Props().Main_page();
			url_parser.Is_main_page_set();
		}
		// generate ttl of domain/wiki/page; needed for pages with leading slash; EX: "/abcd" -> "en.wikipedia.org/wiki//abcd"; ISSUE#:301; DATE:2018-12-16
		else {
			Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_m001();
			try {
				tmp_bfr.Add(wiki.Domain_bry()).Add(gplx.xowa.htmls.hrefs.Xoh_href_.Bry__wiki).Add(ttl_bry).Add_safe(url_parser.Qarg());
				this.ttl_bry = tmp_bfr.To_bry_and_clear();
			} finally {tmp_bfr.Mkr_rls();}
		}

		// get url
		this.url = wiki.Utl__url_parser().Parse(ttl_bry);
		if (!Bry_.Eq(url.Wiki_bry(), wiki.Domain_bry())) { // handle xwiki; EX: en.wikipedia.org/wiki/it:Roma; ISSUE#:600; DATE:2019-11-02
			this.redirect = url.To_bry();
			return false;
		}

		// get ttl
		this.ttl = wiki.Ttl_parse(url.To_bry_page_w_anch()); // changed from ttl_bry to page_w_anch; DATE:2017-07-24
		if (ttl == null) { // handle invalid titles like "Earth]"; ISSUE#:480; DATE:2019-06-02
			this.ttl = wiki.Ttl_parse(Xow_special_meta_.Itm__error.Ttl_bry());
			this.url = wiki.Utl__url_parser().Parse(Xoerror_special.Make_url__invalidTitle(ttl_bry));
		}
		return true;
	}
	public void Make_page(Http_data__client data__client) {
		// get the page
		this.redlink = "";
		this.tab = Gxw_html_server.Assert_tab2(app, wiki);	// HACK: assert tab exists
		this.page = wiki.Page_mgr().Load_page(url, ttl, tab, url_parser.Display(), url_parser.Action());
		this.page.Page_skin_(url_parser.Useskin());
		app.Gui_mgr().Browser_win().Active_page_(page);	// HACK: init gui_mgr's page for output (which server ordinarily doesn't need)
		if (page.Db().Page().Exists_n()) { // if page does not exist, replace with message; else null_ref error; DATE:2014-03-08
			page.Db().Text().Text_bry_(Bry_.new_a7("'''Page not found.'''"));
			wiki.Parser_mgr().Parse(page, false);
		}
		page.Html_data().Head_mgr().Itm__server().Init_by_http(data__client).Enabled_y_();
	}
	public void Make_html(byte retrieve_mode) {
// byte retrieve_mode, byte mode, boolean popup_enabled, String popup_mode, String popup_id
		byte[] page_html;
		if (url_parser.Popup()) {
			String popup_id = url_parser.Popup_id();
			if (String_.Eq(url_parser.Popup_mode(), "more"))
				page_html = wiki.Html_mgr().Head_mgr().Popup_mgr().Show_more(popup_id);
			else
				page_html = wiki.Html_mgr().Head_mgr().Popup_mgr().Show_init(popup_id, ttl_bry, ttl_bry, url_parser.Popup_link());
		}
		else {
			// NOTE: generates HTML, but substitutes xoimg tags for <img>; ISSUE#:686; DATE:2020-06-27
//			byte[] page_html = wiki.Html_mgr().Page_wtr_mgr().Gen(page, mode);
//			page_html = Bry_.Replace_many(page_html, app.Fsys_mgr().Root_dir().To_http_file_bry(), Http_server_wkr.Url__fsys);
//			this.html = String_.new_u8(page_html); // NOTE: must generate HTML now in order for "wait" and "async_server" to work with text_dbs; DATE:2016-07-10

			byte mode = url_parser.Action();
			page_html = wiki.Html_mgr().Page_wtr_mgr().Gen(page, mode);
			if (page.Html_data().Syntaxhighlight())
				page_html = app.SyntaxHighlighter().Highlight(page_html, wiki.Wrk_id(), wiki.Domain_bry(), page.Ttl());
			switch (retrieve_mode) {
				case File_retrieve_mode.Mode_skip:	// noop
					break;
				case File_retrieve_mode.Mode_async_server:
					app.Gui_mgr().Browser_win().Page__async__bgn(tab);
					break;
				case File_retrieve_mode.Mode_wait:
					gplx.xowa.guis.views.Xog_async_wkr.Async(page, tab.Html_itm());
					break;
			}
//                        if (page.Db().Page().Html_db_id() == Xopg_db_page.HTML_DB_ID_NULL) // already done if HTML page
			// NOTE: substitutes xoimg tags for actual file; ISSUE#:686; DATE:2020-06-27

//			Db_readwrite.writeFile(String_.new_u8(page_html), "d:/des/xowa_x/pre_html.htm");

			if (!this.page.Done_hdoc_parse())
				page_html = wiki.Html__hdump_mgr().Load_mgr().Parse(page_html, this.page);
			/* dont run redlinks - 20211103
			byte[] redlinks = null;
			Bry_bfr tmp_bfr = Bry_bfr_.New();
			Xopg_redlink_mgr red_mgr = new Xopg_redlink_mgr(page, null);
			red_mgr.Redlink(tmp_bfr);
			redlinks = tmp_bfr.To_bry_and_clear();
			//System.out.println(String_.new_u8(redlinks));
			this.redlink = String_.new_u8(redlinks);
			*/
		}
		page_html = Http_server_wkr.Replace_fsys_hack(page_html);
		this.html = String_.new_u8(page_html);
	}
}
