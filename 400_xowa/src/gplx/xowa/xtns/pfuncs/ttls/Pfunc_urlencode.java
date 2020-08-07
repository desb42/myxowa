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
package gplx.xowa.xtns.pfuncs.ttls; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.apps.utls.Xoa_Urlencoders;
public class Pfunc_urlencode extends Pf_func_base {	// EX: {{urlencode:a b}} -> a+b
	@Override public int Id() {return Xol_kwd_grp_.Id_url_urlencode;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_urlencode().Name_(name);}
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) {
		int args_len = self.Args_len();
		byte[] argx = Eval_argx(ctx, src, caller, self); if (argx == Bry_.Empty) return;
		byte[] type = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, args_len, 0);
		if (type == null || type.length == 0) type = Bry_.new_a7("query");
		switch (type[0] | 32) {
			case 'w': // WIKI
				argx = Xoa_Urlencoders.wfUrlencode_with_space(argx);
				break;
			case 'p': // PATH
				argx = Xoa_Urlencoders.Raw_url_encode(argx);
				break;
			default: // QUERY
				argx = Xoa_Urlencoders.Url_encode(argx);
				break;
		}
//		Bry_bfr tmp_bfr = ctx.Wiki().Parser_mgr().Tmp_bfr();
//		gplx.langs.htmls.encoders.Gfo_url_encoder_.Http_url.Encode(tmp_bfr, argx);
//		bfr.Add_bfr_and_clear(tmp_bfr);
		bfr.Add(argx);
	}
}
