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
package gplx.xowa.guis.views.redirects;
import gplx.*; import gplx.xowa.*; import gplx.xowa.guis.*; import gplx.xowa.guis.views.*;
import gplx.gfui.controls.elems.*; import gplx.gfui.draws.*;
import gplx.xowa.specials.xowa.default_tab.*;
public class Xog_redirect_mgr implements Gfo_invk {
	private Xoae_app app;
	private boolean enabled;
	public void Init_by_app(Xoae_app app) {
		this.app = app;
		app.Cfg().Sub_many_app(this
			, Cfg__enabled
			);
	}
	public void Init_by_kit(Xoae_app app) {
	}

	public boolean Enabled() {return enabled;}
	public void Enabled_by_cfg() {
		Enabled_(app.Cfg().Get_bool_app_or(Cfg__enabled, false));
	}
	public void Enabled_(boolean v) {
		// set enabled
		this.enabled = v;

		// set browser flag
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Cfg__enabled))	  {this.Enabled_(m.ReadBool("v"));}
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	public static final    String
	  Cfg__enabled     = "xowa.gui.redirect.enabled"
	;
}
