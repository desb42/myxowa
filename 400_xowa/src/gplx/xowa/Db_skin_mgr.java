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
package gplx.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.*; import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.*;
import gplx.xowa.wikis.dbs.*; import gplx.xowa.wikis.data.tbls.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.msgs.*; import gplx.xowa.htmls.core.htmls.*; import gplx.core.intls.ucas.*;
import gplx.langs.htmls.Gfh_tag_;
import gplx.xowa.wikis.nss.*; import gplx.xowa.htmls.heads.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.fmts.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.dbs.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.urls.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.langs.*;
import gplx.xowa.mediawiki.includes.XomwDefines;
import gplx.xowa.xtns.categorytrees.*;
import java.util.concurrent.locks.*;
public class Db_skin_mgr implements Gfo_invk {
	private final Xow_wiki wiki;
	private String skin = "vector";
	private String wordmark = "*";
	private String wordmark_width = "";
	private String wordmark_height = "";
	private String tagline = "*";
	private String tagline_width = "";
	private String tagline_height = "";
	public Db_skin_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
	}
	public void Init_by_wiki(Xow_wiki wiki) {
		// ??
	}
	public String Get_skin() { return skin; }
	private void Set_skin(String skin) {
		this.skin = skin;
	}
	private void Set_wordmark(GfoMsg m) {
		int len = m.Args_count();
		if (len >= 3)
			wordmark_height = m.Args_getAt(2).Val_to_str_or_empty();
		if (len >= 2)
			wordmark_width = m.Args_getAt(1).Val_to_str_or_empty();
		if (len >= 1)
			wordmark = m.Args_getAt(0).Val_to_str_or_empty();
	}
	public String Get_wordmark_img() { return wordmark; }
	public String Get_wordmark_width() { return wordmark_width; }
	public String Get_wordmark_height() { return wordmark_height; }
	private void Set_tagline(GfoMsg m) {
		int len = m.Args_count();
		if (len >= 3)
			tagline_height = m.Args_getAt(2).Val_to_str_or_empty();
		if (len >= 2)
			tagline_width = m.Args_getAt(1).Val_to_str_or_empty();
		if (len >= 1)
			tagline = m.Args_getAt(0).Val_to_str_or_empty();
	}
	public String Get_tagline_img() { return tagline; }
	public String Get_tagline_width() { return tagline_width; }
	public String Get_tagline_height() { return tagline_height; }

	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk__set_))		Set_skin(m.ReadStr("v"));
		else if		(ctx.Match(k, Invk__set_wordmark_))		Set_wordmark(m);
		else if		(ctx.Match(k, Invk__set_tagline_))		Set_tagline(m);
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	private static final String
	  Invk__set_ = "set_"
	, Invk__set_wordmark_ = "set_wordmark_"
	, Invk__set_tagline_ = "set_tagline_"
	;
}
