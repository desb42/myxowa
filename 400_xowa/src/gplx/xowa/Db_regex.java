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
import gplx.xowa.xtns.scribunto.libs.Scrib_regx_grp_mgr;
import gplx.Keyval;
import gplx.Byte_ascii;
import gplx.Err_;
import gplx.Int_;
import gplx.objects.strings.unicodes.*;
public class Db_regex {
	private final Scrib_regx_grp_mgr grp_mgr = new Scrib_regx_grp_mgr();
	public Keyval[] Capt_ary() {return grp_mgr.Capt__to_ary();}
	public void patternToRegex(String pat_str) {
		Ustring pat_ucs = Ustring_.New_codepoints(pat_str);
		grp_mgr.Clear();

		int len = pat_ucs.Len_in_data();
		int grps_len = 0;
		int bct = 0;

		for (int i = 0; i < len; i++) {
			int i_end = i + 1;
			int cur = pat_ucs.Get_data(i);
			switch (cur) {
				case Byte_ascii.Paren_bgn: {
					// fail if "(EOS"
					if (i + 1 >= len)
						throw Err_.new_wo_type("Unmatched open-paren at pattern character " + Int_.To_str(i_end));
					int grp_idx = grp_mgr.Capt__len() + 1;

					// check for "()"; enables anypos flag
					boolean is_empty_capture = pat_ucs.Get_data(i + 1) == Byte_ascii.Paren_end;
					grp_mgr.Capt__add__real(grp_idx, is_empty_capture);
					break;
				}
				case Byte_ascii.Paren_end:
					// fail if ")" without preceding "("
					if (grp_mgr.Open__len() <= 0)
						throw Err_.new_wo_type("Unmatched close-paren at pattern character " + Int_.To_str(i_end));
					grp_mgr.Open__pop();
					break;
				case Byte_ascii.Percent:
					i++;
					if (i >= len)
						throw Err_.new_wo_type("malformed pattern (ends with '%')");
					switch(pat_ucs.Get_data(i)) {
						case Byte_ascii.Ltr_b:	// EX: "%b()"
							i += 2;
							if (i >= len) throw Err_.new_wo_type("malformed pattern (missing arguments to '%b')");
							break;
						case Byte_ascii.Ltr_f: 	// EX: lua frontier pattern; "%f[%a]"; DATE:2015-07-21
							if (i + 1 >= len || pat_ucs.Get_data(++i) != Byte_ascii.Brack_bgn)
								throw Err_.new_("scribunto", "missing '[' after %f in pattern at pattern character " + Int_.To_str(i_end));
							// %f always followed by bracketed term; convert lua bracketed term to regex
							i = bracketedCharSetToRegex(pat_ucs, i, len);
							break;
					}
                                        break;
				case Byte_ascii.Brack_bgn:
					i = bracketedCharSetToRegex(pat_ucs, i, len);
					break;
				case Byte_ascii.Brack_end:
					throw Err_.new_wo_type("Unmatched close-bracket at pattern character " + Int_.To_str(i_end));
				default:
					break;
			}
		}
	}
	private int bracketedCharSetToRegex(Ustring pat_ucs, int i, int len) {
		i++;
		if (i < len && pat_ucs.Get_data(i) == Byte_ascii.Pow) {	// ^
			i++;
		}
		for (int j = i; i < len && (j == i || pat_ucs.Get_data(i) != Byte_ascii.Brack_end); i++) {
			if (pat_ucs.Get_data(i) == Byte_ascii.Percent) {
				i++;
				if (i >= len) {
					break;
				}
			}
			else if (i + 2 < len && pat_ucs.Get_data(i + 1) == Byte_ascii.Dash && pat_ucs.Get_data(i + 2) != Byte_ascii.Brack_end && pat_ucs.Get_data(i + 2) != Byte_ascii.Hash) {
				i += 2;
			}
		}
		if (i > len) throw Err_.new_wo_type("Missing close-bracket for character set beginning at pattern character $nxt_pos");

		return i;
	}
}