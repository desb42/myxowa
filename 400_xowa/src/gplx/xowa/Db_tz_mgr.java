/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

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
import java.util.TimeZone;
public class Db_tz_mgr implements Gfo_invk {
	private final Xow_wiki wiki;
	private String tz_region = "UTC";
	private TimeZone tz;
	public Db_tz_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
		this.tz = TimeZone.getTimeZone("UTC");
	}
	public void Init_by_wiki(Xow_wiki wiki) {
		// ??
	}
	public TimeZone Get_timezone() { return tz; }
	private void Set_tz(String region) {
		tz_region = region;
		this.tz = TimeZone.getTimeZone(region);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk__set_))		Set_tz(m.ReadStr("v"));
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	private static final String Invk__set_ = "set_";
}
