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
package gplx.xowa;
import gplx.Byte_ascii;
import gplx.Err_;
import gplx.Int_;
import gplx.List_adp;
import gplx.List_adp_;
import gplx.Keyval;
import gplx.Keyval_;
public class Db_regex {
	public Keyval[] Capt_ary() {
		return capt_list.Count() == 0 ? null : (Keyval[])capt_list.To_ary(Keyval.class);
	}
	private final List_adp capt_list = List_adp_.New();
	public void patternToRegex(String pat_str) {
		capt_list.Clear();
		int depth = 0;

		int len = pat_str.length();
		int grp_idx = 0;

		for (int i = 0; i < len; i++) {
			int i_end = i + 1;
			int cur = pat_str.charAt(i);
			switch (cur) {
				case Byte_ascii.Paren_bgn: {
					// fail if "(EOS"
					if (i + 1 >= len)
						throw Err_.new_wo_type("Unmatched open-paren at pattern character " + Int_.To_str(i_end));
					grp_idx = capt_list.Count() + 1;
					depth++;

					boolean is_empty_capture = pat_str.charAt(i + 1) == Byte_ascii.Paren_end;
					capt_list.Add(Keyval_.int_(grp_idx, is_empty_capture));
					break;
				}
				case Byte_ascii.Paren_end:
					depth--;
					break;
				case Byte_ascii.Percent:
					i++;
					if (i >= len)
						throw Err_.new_wo_type("malformed pattern (ends with '%')");
					break;
				case Byte_ascii.Brack_bgn:
					i = bracketedCharSetToRegex(pat_str, i, len);
					break;
				case Byte_ascii.Brack_end:
					throw Err_.new_wo_type("Unmatched close-bracket at pattern character " + Int_.To_str(i_end));
				default:
					break;
			}
		}
		// fail if ")" without preceding "("
		if (depth > 0)
			throw Err_.new_wo_type("Unmatched close-paren");
	}
	private int bracketedCharSetToRegex(String pat, int i, int len) {
		i++;
		if (i < len && pat.charAt(i) == Byte_ascii.Pow) {	// ^
			i++;
		}
		for (int j = i; i < len && (j == i || pat.charAt(i) != Byte_ascii.Brack_end); i++) {
			if (pat.charAt(i) == Byte_ascii.Percent) {
				i++;
				if (i >= len) {
					break;
				}
			}
			else if (i + 2 < len && pat.charAt(i + 1) == Byte_ascii.Dash && pat.charAt(i + 2) != Byte_ascii.Brack_end && pat.charAt(i + 2) != Byte_ascii.Hash) {
				i += 2;
			}
		}
		if (i > len) throw Err_.new_wo_type("Missing close-bracket for character set beginning at pattern character $nxt_pos");

		return i;
	}
}