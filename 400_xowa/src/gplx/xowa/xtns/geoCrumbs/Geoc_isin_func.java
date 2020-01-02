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
package gplx.xowa.xtns.geoCrumbs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Geoc_isin_func extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_geoCrumbs_isin;}
	@Override public Pf_func New(int id, byte[] name) {return new Geoc_isin_func().Name_(name);}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) {
		byte[] ttl_bry = Eval_argx(ctx, src, caller, self);
		Xowe_wiki wiki = ctx.Wiki();
		Xoa_ttl page_ttl = ctx.Page().Ttl();
		if (!page_ttl.Ns().Id_is_main())
			ttl_bry = Bry_.Add(page_ttl.Ns().Name_db_w_colon(), ttl_bry);
		Xoa_ttl ttl = Xoa_ttl.Parse(wiki, ttl_bry); if (ttl == null) return;
		if (wiki.App().Mode().Tid_is_cmd()) {
			ttl_bry = DB_case_cvt.Upper_1st(ttl_bry, 0, ttl_bry.length);
			// how to remove any trailing r-t-l or l-t-r marks?
			int tlen = ttl_bry.length;
			if (tlen > 3 && ttl_bry[tlen - 3] == 0xe2 && ttl_bry[tlen - 2] == 0x82 && (ttl_bry[tlen - 1] == 0x8f || ttl_bry[tlen - 1] == 0x8e)) {
				ttl_bry = Bry_.Mid(ttl_bry, 0, tlen - 3);
			}
			// insert into parent table
			wiki.Bread().Insert(ctx.Page().Ttl().Full_txt_raw(), ttl_bry);
			Gfo_usr_dlg_.Instance.Log_many("", "", "#isin: ttl=~{0} parent=~{1}", page_ttl.Full_db(), ttl_bry);
		}
		ctx.Page().Html_data().Pgbnr_isin_(ttl_bry);
	}
	public static final    Geoc_isin_func Instance = new Geoc_isin_func();
}
