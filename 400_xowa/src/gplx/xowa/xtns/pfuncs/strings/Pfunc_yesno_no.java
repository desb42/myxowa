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
package gplx.xowa.xtns.pfuncs.strings;
import gplx.*;
import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Pfunc_yesno_no extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_yesno_no;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_yesno_no().Name_(name);}
	@Override public boolean Func_require_colon_arg() {return false;}
	// EX: {{yesno_no|y|yes=Yeah}}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) { // hard coding of Template:yesno
		Pfunc_yesno.Process_yesno(bfr, ctx, caller, self, src, Pfunc_yesno.NONO);
	}
}

/*
{{safesubst:<noinclude />yesno|{{{1}}}|yes={{{yes|yes}}}|no={{{no|no}}}|blank={{{blank|no}}}|�={{{�|no}}}|def={{{def|no}}}}}

'�'.encode("utf-8") => b'\xc2\xac' => -62, -84
*/
