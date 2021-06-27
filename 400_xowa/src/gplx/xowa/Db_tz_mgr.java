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
import gplx.*;
import java.time.ZoneId;
public class Db_tz_mgr implements Gfo_invk {
	private final Xow_wiki wiki;
	private String tz_region = "UTC";
	private ZoneId zoneid;
	public Db_tz_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
		this.zoneid = ZoneId.of(tz_region);
	}
	public void Init_by_wiki(Xow_wiki wiki) {
		// ??
	}
	public ZoneId Get_zoneid() { return zoneid; }
	private void Set_tz(String region) {
		tz_region = region;
		this.zoneid = ZoneId.of(region);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk__set_))		Set_tz(m.ReadStr("v"));
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	private static final String Invk__set_ = "set_";
}
