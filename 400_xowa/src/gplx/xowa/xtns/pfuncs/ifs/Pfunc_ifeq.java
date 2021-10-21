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
package gplx.xowa.xtns.pfuncs.ifs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.parsers.amps.*;
import gplx.core.btries.*;
import gplx.langs.htmls.entitys.*;
public class Pfunc_ifeq extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) {
		int self_args_len = self.Args_len(); if (self_args_len < 2) return; // no equal/not_equal clauses defined; return; EX: {{#if:a}} {{#if:a|b}}
		byte[] lhs = cvtamp( Eval_argx(ctx, src, caller, self), ctx);
		byte[] rhs = cvtamp( Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, self_args_len, 0), ctx);
		if (Pf_func_.Eq(ctx, lhs, rhs))
			bfr.Add(Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, self_args_len, 1));
		else
			bfr.Add(Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, self_args_len, 2));
	}
	@Override public int Id() {return Xol_kwd_grp_.Id_xtn_ifeq;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_ifeq().Name_(name);}
	private byte[] cvtamp(byte[] src, Xop_ctx ctx) {
		int pos = 0;
		int len = src.length;
		Bry_bfr bfr = null;
		Btrie_slim_mgr amp_trie = null;
		Btrie_rv trv = null;
		Xop_amp_mgr amp_mgr = null;
		int start= 0;
		while (pos < len) {
			byte b = src[pos++];
			switch (b) {
				case '&': // convert & to utf-8 equiv
					if (bfr == null) {
						bfr = Bry_bfr_.New();
						amp_mgr = ctx.Wiki().App().Parser_amp_mgr();
						amp_trie = amp_mgr.Amp_trie();
						trv = new Btrie_rv();
					}
					bfr.Add_mid(src, start, pos-1);
					Object html_ent_obj = amp_trie.Match_at(trv, src, pos, len);
					if (html_ent_obj != null) {
						Gfh_entity_itm amp_itm = (Gfh_entity_itm)html_ent_obj;
						if (amp_itm.Tid() == Gfh_entity_itm.Tid_name_std) {
							bfr.Add(amp_itm.U8_bry());
							pos = trv.Pos();
						}
						else {
							Xop_amp_mgr_rslt amp_rv = new Xop_amp_mgr_rslt();
							amp_mgr.Parse_ncr(amp_rv, amp_itm.Tid() == Gfh_entity_itm.Tid_num_hex, src, len, pos, trv.Pos());
							if (amp_rv.Pass()) {
								int val = amp_rv.Val();
								bfr.Add(gplx.core.intls.Utf16_.Encode_int_to_bry(val));
								pos = amp_rv.Pos();
							}
						}
						start = pos;
					}
					else {
						start = pos - 1;
					}
					break;
			}
		}
		if (start != 0) {
			bfr.Add_mid(src, start, len);
			return bfr.To_bry();
		}
		else
			return src;
	}
}	
