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
package gplx.xowa.parsers.tmpls; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
public class Xot_prm_keyword_expansion_tkn extends Xot_prm_tkn {
	public Xot_prm_keyword_expansion_tkn(byte[] prm_key, Arg_itm_tkn dflt_tkn, Arg_itm_tkn find_tkn) {
		super(0,0); //ugh!
		this.prm_key = prm_key;
		this.dflt_tkn = dflt_tkn;
		this.find_tkn = find_tkn;
	}
	
	@Override public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Bry_bfr bfr) {
		Arg_nde_tkn arg_nde = caller.Args_get_by_key(src, prm_key);
		if (arg_nde == null) {
			Xot_prm_tkn.Tmpl_write_missing(ctx, src, caller, bfr, dflt_tkn, find_tkn);
		}
		else {
			Xot_prm_tkn.Tmpl_expand_args(ctx, src, caller, bfr, arg_nde);
		}
		return true;
	}
}
