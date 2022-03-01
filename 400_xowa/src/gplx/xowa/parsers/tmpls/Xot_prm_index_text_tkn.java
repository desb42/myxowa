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
public class Xot_prm_index_text_tkn extends Xot_prm_tkn {
	private byte[] txt;
	public Xot_prm_index_text_tkn(int prm_idx, byte[] txt) {
		super(0,0); //ugh!
		this.prm_idx = prm_idx;
		this.txt = txt;
	}
	
	@Override public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Bry_bfr bfr) {
		Arg_nde_tkn arg_nde = caller.Args_get_by_key(src, prm_key);
		if (arg_nde == null) {
			bfr.Add(txt);
		}
		else {
			Xot_prm_tkn.Tmpl_expand_args(ctx, src, caller, bfr, arg_nde);
		}
		return true;
	}
}
