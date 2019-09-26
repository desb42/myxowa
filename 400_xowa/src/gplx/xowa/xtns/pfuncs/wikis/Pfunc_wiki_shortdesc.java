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
package gplx.xowa.xtns.pfuncs.wikis; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Pfunc_wiki_shortdesc extends Pf_func_base {
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) {
		int args_len = self.Args_len();
		byte[] argx = Eval_argx(ctx, src, caller, self); if (argx == null) return; // no argx; return empty
		// get second part if any
		byte[] second = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, args_len, 0);
                //Xoae_page page = ctx.Page();
                // if shortdesc has already been set
                //  and if second is 'noreplace' - ignore
                //if (page.Short_desc() != null && second[0] == 'n') return;
                // else
                // add to shortdesc holder
                // something needs to convert the wikitext to html (here or elsewhere)
                //page.Short_desc_(argx);
	}
	public Pfunc_wiki_shortdesc(int id) {this.id = id;}
	@Override public int Id() {return id;} private int id;
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_wiki_shortdesc(id).Name_(name);}
	public static final    Pfunc_wiki_shortdesc Instance = new Pfunc_wiki_shortdesc(-1);
}
