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
import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Pfunc_yesno extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_yesno;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_yesno().Name_(name);}
	@Override public boolean Func_require_colon_arg() {return false;}
	// EX: {{yesno|y|yes=Yeah}}
	@Override public void Func_evaluate(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src) { // hard coding of Template:yesno
		Process_yesno(bfr, ctx, caller, self, src, YESNO);
	}

	public static void Process_yesno(Bry_bfr bfr, Xop_ctx ctx, Xot_invk caller, Xot_invk self, byte[] src, int yesno) {
		byte[] match = null;
		Arg_nde_tkn yes_arg = null;
		Arg_nde_tkn no_arg = null;
		Bry_bfr tmp = null;
		int rv = BLANK;
		int self_args_len = self.Args_len();
		if (self_args_len != 0) {
			tmp = Bry_bfr_.New();
			byte[] argx = null;
			Arg_nde_tkn arg;
			rv = NULL;
			boolean indexfound = false;
			// check for 1=
			for (int i = 0; i < self_args_len; i++) {
				arg = self.Args_get_by_idx(i);
				if (arg.KeyTkn_exists()) {								// = exists; EX: "|a=1|"
					byte[] case_key = Get_or_eval(ctx, src, caller, arg.Key_tkn(), tmp);
					if (case_key.length == 1 && case_key[0] == '1') {
						argx = Get_or_eval(ctx, src, caller, arg.Val_tkn(), tmp);
						indexfound = true;
						break;
					}
				}
			}
			if (!indexfound) {
				arg = self.Args_get_by_idx(0);
				if (!arg.KeyTkn_exists()) {
					argx = Get_or_eval(ctx, src, caller, arg.Val_tkn(), tmp);
					indexfound = true;
				}
			}
			if (indexfound) {
				argx = DB_case_mgr.Case_build_reuse(false, argx);
				rv = BLANK;
				int bgn = 0;
				int end = argx.length - 1;
				while (bgn < end + 1) {
					byte b = argx[bgn];
					if (b == ' ' || b == '\t' || b == '\n' || b == '\r')
						bgn++;
					else
						break;
				}
				while (end > bgn) {
					byte b = argx[end];
					if (b == ' ' || b == '\t' || b == '\n' || b == '\r')
						end--;
					else
						break;
				}
				int src_len = end - bgn + 1;
				if (src_len > 0) {
					rv = DEF;
					byte b = argx[bgn];
					switch (b) {
						case 'n':
							if (src_len == 1 || (src_len == 2 && argx[bgn + 1] == 'o'))
								rv = NO;
							break;
						case 'f':
							if (src_len == 1 || (src_len == 5 && argx[bgn + 1] == 'a' && argx[bgn + 2] == 'l' && argx[bgn + 3] == 's' && argx[bgn + 4] == 'e'))
								rv = NO;
							break;
						case 'o':
							if (src_len == 3 && argx[bgn + 1] == 'f' && argx[bgn + 2] == 'f')
								rv = NO;
							if (src_len == 1 || (src_len == 2 && argx[bgn + 1] == 'n'))
								rv = YES;
							break;
						case '0': // any combo of zeros 0, 00, 000, 0.0, 0. OR any number of zeros ending in 1 (for yes)
							rv = NO;
							boolean dotseen = false;
							while (++bgn < src_len) {
								b = argx[bgn];
								if (b == '0')
									continue;
								if (b == '.' && dotseen == false) {
									dotseen = true;
									continue;
								}
								if (b == '1' && bgn == end && dotseen == false)
									rv = YES;
								else
									rv = DEF;
								break;
							}
							break;
		
						case 't':
							if (src_len == 1 || (src_len == 4 && argx[bgn + 1] == 'r' && argx[bgn + 2] == 'u' && argx[bgn + 3] == 'e'))
								rv = YES;
							break;
						case 'y':
							if (src_len == 1 || (src_len == 3 && argx[bgn + 1] == 'e' && argx[bgn + 2] == 's'))
								rv = YES;
							break;
						case '1':
							if (src_len == 1)
								rv = YES;
							break;
						case -62: //¬
							if (src_len == 2 && argx[bgn + 1] == -84)
								rv = NULL;
							break;
					}
				}
			}
			// go through all the args
			for (int i = 0; i < self_args_len; i++) {
				arg = self.Args_get_by_idx(i);
				if (arg.KeyTkn_exists()) {								// = exists; EX: "|a=1|"
					byte[] case_key = Get_or_eval(ctx, src, caller, arg.Key_tkn(), tmp);
					if (Pf_func_.Eq(ctx, case_key, keywords[rv])) { // case_key matches
						match = Get_or_eval(ctx, src, caller, arg.Val_tkn(), tmp);
						break;											// stop iterating; explicit match found;
					}
					else if (rv != YES && Pf_func_.Eq(ctx, case_key, keywords[YES]))
						yes_arg = arg;
					else if (rv != NO && Pf_func_.Eq(ctx, case_key, keywords[NO]))
						no_arg = arg;
				}
			}
		}
		if (match == null) { // no match; will either use #default
			switch (yesno) {
				case YESNO:
					switch (rv) {
						case DEF:
							if (yes_arg != null)
								match = Get_or_eval(ctx, src, caller, yes_arg.Val_tkn(), tmp);
							else
								match = keywords[YES];
							break;
						case BLANK: // but BLANK will never happen because of {{{1|¬}}}
							if (no_arg != null)
								match = Get_or_eval(ctx, src, caller, no_arg.Val_tkn(), tmp);
							break;
						case NO:
							break;
						case YES:
							match = keywords[YES];
							break;
					}
					break;
				case YESYES:
					switch (rv) {
						case BLANK:
						case DEF:
						case YES:
						case NULL:
							match = keywords[YES];
							break;
						case NO:
							match = keywords[NO];
							break;
					}
					break;
				case NONO:
					switch (rv) {
						case BLANK:
						case DEF:
						case NO:
						case NULL:
							match = keywords[NO];
							break;
						case YES:
							match = keywords[YES];
							break;
					}
					break;
			}
		}
		if (match != null)
			bfr.Add(match);
	}
	// repeated from Pfunc_switch.java
	private static byte[] Get_or_eval(Xop_ctx ctx, byte[] src, Xot_invk caller, Arg_itm_tkn itm, Bry_bfr tmp) {
		if (itm.Itm_static() == Bool_.Y_byte)
			return Bry_.Trim(src, itm.Dat_bgn(), itm.Dat_end());
		else {
			itm.Tmpl_evaluate(ctx, src, caller, tmp);
			return tmp.To_bry_and_clear_and_trim();
		}
	}
	private static final byte[][] keywords = new byte[][] {
	  Bry_.new_a7("no")
	, Bry_.new_a7("blank")
	, Bry_.new_a7("yes")
	, Bry_.new_a7("def")
	, Bry_.new_u8("¬")
	};
	private static final int
	  NO = 0
	, BLANK = 1
	, YES = 2
	, DEF = 3
	, NULL = 4
	;
	public static final int
	  YESNO = 1
	, YESYES = 2
	, NONO = 3
	;
}

/*
{{<includeonly>safesubst:</includeonly>#switch: {{<includeonly>safesubst:</includeonly>lc: {{{1|¬}}} }}
 |no
 |n
 |f
 |false
 |off
 |0        = {{{no|<!-- null -->}}}
 |         = {{{blank|{{{no|<!-- null -->}}}}}}
 |¬        = {{{¬|}}}
 |yes
 |y
 |t
 |true
 |on
 |1        = {{{yes|yes}}}
 |#default = {{{def|{{{yes|yes}}}}}}
}}

'¬'.encode("utf-8") => b'\xc2\xac' => -62, -84
*/
