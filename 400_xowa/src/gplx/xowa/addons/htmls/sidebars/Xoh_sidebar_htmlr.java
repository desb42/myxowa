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
package gplx.xowa.addons.htmls.sidebars;

import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_bfr_;
import gplx.Bry_fmt;
import gplx.List_adp;
import gplx.core.brys.Bfr_arg;
import gplx.xowa.Xowe_wiki;

import gplx.langs.jsons.Json_nde;
import gplx.langs.jsons.Json_ary;
class Xoh_sidebar_htmlr {
	public static Json_nde To_json(Xowe_wiki wiki, List_adp grps) {
		Xoh_sidebar_itms_fmtr itms_fmtr = new Xoh_sidebar_itms_fmtr();
		int len = grps.Count();
		boolean popups_enabled = wiki.Html_mgr().Head_mgr().Popup_mgr().Enabled();
		Bry_bfr tmp_bfr = Bry_bfr_.New();

		Json_nde top = Json_nde.NewByVal();
		Json_ary portlets_rest = Json_ary.NewByVal();
		Json_nde jd;

		for (int i = 0; i < len; ++i) {
			Xoh_sidebar_itm grp = (Xoh_sidebar_itm)grps.Get_at(i);
			itms_fmtr.Init_by_grp(popups_enabled, grp);
			itms_fmtr.Bfr_arg__add(tmp_bfr);
			jd = Db_Nav_template.Build_Sidebar_json(wiki, grp.Id(), grp.Text(), tmp_bfr.To_bry_and_clear(), i);
			if (i == 0) {
				top.AddKvNde("data-portlets-first", jd);
			}
			else {
				portlets_rest.Add(jd);
			}
		}
		// dummy toolbox
		// id="p-tb" used by some js
		jd = Db_Nav_template.Build_Sidebar_json(wiki, Bry_.new_a7("p-tb"), Bry_.new_a7("Toolbar"), Bry_.Empty, 1);
		portlets_rest.Add(jd);
		byte[] buf = Bry_.new_u8(wiki.Appe().Gui_mgr().Html_mgr().Portal_mgr().Wikis().Itms_as_html());
		jd = Db_Nav_template.Build_Sidebar_json(wiki, Bry_.new_a7("p-xowa-wiki"), Bry_.new_a7("Wikis"), buf, 1);
		portlets_rest.Add(jd);
		top.AddKvAry("array-portlets-rest", portlets_rest);
		buf = Bry_.Add(wiki.Msg_mgr().Val_html_accesskey_and_title("p-logo")
			, Bry_.new_a7(" class=\"mw-wiki-logo xowa-hover-off\" href=\"/wiki/\""));
		top.AddKvStr("html-logo-attributes", buf);
		return top;
	}
	private static final Bry_fmt fmt = Bry_fmt.Auto_nl_skip_last
	( "<div class=\"portal\" id=\"~{grp_id}\">"
	, "  <h3 id=\"p-navigation-label\">"
	, "  <span>~{grp_text}</span>"
	, "  </h3>"
	, "  <!-- Please do not use the .body class, it is deprecated. -->"
	, "  <div class=\"body vector-menu-content\">"
	, "  <!-- Please do not use the .menu class, it is deprecated. -->"
	, "    <ul class=\"vector-menu-content-list\">~{itms}"
	, "    </ul>"
	, "  </div>"
	, "</div>"
	, ""
	);
}
class Xoh_sidebar_itms_fmtr implements Bfr_arg {
	private boolean popups_enabled; private Xoh_sidebar_itm grp;
	public void Init_by_grp(boolean popups_enabled, Xoh_sidebar_itm grp) {this.popups_enabled = popups_enabled; this.grp = grp;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		String itm_cls = popups_enabled ? " class='xowa-hover-off'" : "";
		int len = grp.Subs__len();
		for (int i = 0; i < len; ++i) {
			Xoh_sidebar_itm itm = grp.Subs__get_at(i);
			fmt.Bld_many(bfr, itm.Id(), itm.Href(), itm_cls, itm.Atr_accesskey_and_title(), itm.Text());
		}		
	}
	private final Bry_fmt fmt = Bry_fmt.Auto_nl_skip_last
	( ""
	, "      <li id=\"~{itm_id}\"><a href=\"~{itm_href}\"~{itm_cls}~{itm_accesskey_and_title}>~{itm_text}</a></li>"
	); 
}
