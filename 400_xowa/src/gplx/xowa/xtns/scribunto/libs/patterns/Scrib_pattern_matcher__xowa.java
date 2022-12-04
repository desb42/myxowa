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
package gplx.xowa.xtns.scribunto.libs.patterns;

import gplx.langs.regxs.Regx_group;
import gplx.langs.regxs.Regx_match;
import gplx.objects.strings.unicodes.Ustring;
import gplx.objects.strings.unicodes.Ustring_;
import gplx.xowa.xtns.scribunto.libs.Scrib_lib_ustring_gsub_mgr;
import org.luaj.vm2.lib.Match_state;
import org.luaj.vm2.lib.Str_find_mgr;
import org.luaj.vm2.lib.Str_find_mgr__xowa;
import gplx.List_adp;
import gplx.List_adp_;

class Scrib_pattern_matcher__xowa extends Scrib_pattern_matcher {
	public Scrib_pattern_matcher__xowa(byte[] page_url) {}

	public Str_find_mgr Find_mgr(Ustring src_ucs, String pat_str, int bgn_as_codes) {
		Str_find_mgr__xowa mgr = new Str_find_mgr__xowa(src_ucs, Ustring_.New_codepoints(pat_str), bgn_as_codes, false, false);
		gmatch_state = new Match_state(mgr);
		return mgr;
	}
	private Match_state gmatch_state;
	public Regx_match Match_with_mgr(Str_find_mgr smgr, int bgn_as_codes, Ustring src_ucs) {
		Str_find_mgr__xowa mgr = (Str_find_mgr__xowa)smgr;
		mgr.Set_pos(bgn_as_codes);
		mgr.Process_gmatch(gmatch_state);

		int find_bgn = mgr.Bgn();
		int find_end = mgr.End();
		boolean found = find_bgn != -1;

		Regx_group[] groups = Make_groups(src_ucs, mgr.Captures_ary());
		return new Regx_match(found, find_bgn, find_end, groups);
	}
	@Override public Regx_match Match_one(Ustring src_ucs, String pat_str, int bgn_as_codes, boolean replace) {
		Str_find_mgr__xowa mgr = new Str_find_mgr__xowa(src_ucs, Ustring_.New_codepoints(pat_str), bgn_as_codes, false, false);
		mgr.Process(false);
		
		int find_bgn = mgr.Bgn();
		int find_end = mgr.End();
		boolean found = find_bgn != -1;

		Regx_group[] groups = Make_groups(src_ucs, mgr.Captures_ary());
		return new Regx_match(found, find_bgn, find_end, groups);
	}

	@Override public String Gsub(Scrib_lib_ustring_gsub_mgr gsub_mgr, Ustring src_ucs, String pat_str, int bgn_as_codes) {
		// get src vars
		//String src_str = src_ucs.Src();
		int src_len = src_ucs.Len_in_data();
		// TOMBSTONE:do not return early if String.empty; allows `string.gsub('', '$', 'a')` ISSUE#:731; DATE:2020-07-20
		// if (src_len == 0) return src_str;
		int src_max = src_len + 1;
		
		// get pat vars
		//regx_converter.patternToRegex(pat_str, Scrib_regx_converter.Anchor_G, true);
		Ustring pat = Ustring_.New_codepoints(pat_str);
		int pat_len = pat.Len_in_data();
		final int pat_anchored = pat_len > 0 && pat.Get_data(0) == '^' ? 1 : 0;
		boolean isreplNull = gsub_mgr.IsReplNull();
//System.out.println("a s:" + src_str + " p:" + pat_str + " r:" + isreplNull);
//System.out.println("p:" + Scrib_lib_ustring_gsub_mgr.escapeChars(pat_str) );
//if (src_str.equals("3–28")) {
//    int a=1;
//}
		// get match vars
		//Bry_bfr tmp_bfr = Bry_bfr_.New();
		//Bry_bfr tmp_bfr = null;
		StringBuilder sb = null;
		int start = 0;
		Str_find_mgr__xowa match_mgr = new Str_find_mgr__xowa(src_ucs, pat, bgn_as_codes, false, false);
		Match_state ms = new Match_state(match_mgr);
		
		int src_pos = 0;
		int src_idx = 0;
		while (src_idx < src_max) {
			ms.reset();
			int res = ms.match(src_pos, pat_anchored);

			src_pos = ms.Stretch();
			// match found
			if (res != -1) {
				if (gsub_mgr.Repl_count__done()) break;
				src_idx++;

				if (sb == null)
					sb = new StringBuilder(src_len);
				sb.append(src_ucs.Substring(start, src_pos));
				//if (tmp_bfr == null)
				//	tmp_bfr = Bry_bfr_.New();
				//tmp_bfr.Add_str_u8(src_ucs.Substring(start, stretch));
				if (!isreplNull) {
					ms.push_captures(true, src_pos, res);

					Regx_group[] groups = Make_groups(src_ucs, match_mgr.Captures_ary());
					Regx_match match = new Regx_match(true, src_pos, res, groups);
					if (!gsub_mgr.Exec_repl_itm(sb, match)) {
						sb.append(src_ucs.Substring(match.Find_bgn(), match.Find_end()));
 						//tmp_bfr.Add_str_u8(src_ucs.Substring(match.Find_bgn(), match.Find_end()));
					}
				}

				gsub_mgr.Repl_count__add();
				if (src_pos == res) { // characters but still a match eg "()" or "$"
					start = src_pos;
					src_pos++;        // ?? 20211120
				}
				else {
					src_pos = res;
					start = src_pos;
				}
				if (src_pos >= src_len) // XOWA:assert src_pos is in bounds, else will throw ArrayIndexOutOfBounds exception; DATE:2016-09-20
					break; 
			}
			// no match; add current byte
			else if (src_pos < src_len) {
				src_pos++;
			}
			else
				break;

			if (pat_anchored > 0)
				break;

		}

		if (sb != null) {
			sb.append(src_ucs.Substring(start, src_len));
			return sb.toString();
		}
		//if (tmp_bfr != null) {
		//	tmp_bfr.Add_str_u8(src_ucs.Substring(start, src_len));
		//	return tmp_bfr.To_str_and_clear();
		//}
		else
			return src_ucs.Src();
	}

	private Regx_group[] Make_groups(Ustring src_ucs, int[] captures) {
		if (captures == null) {
			return Regx_group.Ary_empty;
		}

		int captures_len = captures.length;
		Regx_group[] groups = new Regx_group[captures_len / 2];
		for (int i = 0; i < captures_len; i += 2) {
			int capture_bgn = captures[i];
			int capture_end = captures[i + 1];
			// FOOTNOTE:REGX_GROUP
 			String val;
			if (capture_end < 0) // for ()
				val = "";
			else
				val = src_ucs.Substring(capture_bgn, capture_end);
			groups[i / 2] = new Regx_group(true, capture_bgn, capture_end, val);
		}
		return groups;
	}

	public List_adp Split(Ustring src_ucs, String pat_str) {
		// get src vars
		//String src_str = src_ucs.Src();
		int src_len = src_ucs.Len_in_data();
		
		// get pat vars
		Ustring pat = Ustring_.New_codepoints(pat_str);
		int pat_len = pat.Len_in_data();

		int start = 0;
		Str_find_mgr__xowa match_mgr = new Str_find_mgr__xowa(src_ucs, pat, 0, false, false);
		Match_state ms = new Match_state(match_mgr);
		List_adp list = List_adp_.New();
		
		int src_pos = 0;
		while (true) {
			ms.reset();
			int res = ms.match(src_pos, 0);

			src_pos = ms.Stretch();
			// match found?
			if (res != -1) {
				list.Add(src_ucs.Substring(start, src_pos));
				src_pos = res;
				start = res;
			}
			else
				src_pos++;
			if (src_pos >= src_len) // XOWA:assert src_pos is in bounds, else will throw ArrayIndexOutOfBounds exception; DATE:2016-09-20
				break; 
		}
		list.Add(src_ucs.Substring(start, src_len)); // the rest
		return list;
	}
}

/*
== FOOTNOTE:REGX_GROUP [ISSUE#:726; DATE:2020-05-17] ==
The XOWA Regx_group is a quasi-adapter for java.util.regex.Matcher and its group-related methods.

Consider a Regx_group with varName `grp` and a Matcher with varName `match`
* `grp.Bgn()` <- `match.start()`
* `grp.End()` <- `match.end()`
* `grp.Val()` <- `match.group(i)`

Note that all callers of `grp` would be expecting REGEX convention (not LUA pattern convention). As such:
* '''base0''': `grp.Bgn()` and `grp.End()` must be base0 not base1 (REGEX is base0)
** Fortunately, Str_find_mgr__xowa uses base0, so there is no need to convert from base1 to base0
** However, Scrib_lib_ustring_gsub_mgr will convert base0 to base1 in the gsub FunctionCallback code '''IF''' anypos is present in the pattern
* '''charIndexes''': `grp.Bgn()` and `grp.End()` should represent charIndexes, not byteIndexes (REGEX is chars)
** Str_find_mgr__xowa uses codepointIndexes b/c of Ustring_ucs
** In theory, should convert to charIndexes b/c REGEX uses charIndexes. However:
*** Regx_group.Bgn() is only used by anypos for LuaCallbacks
*** anypos needs codepointIndexes
*** so, be lazy, and don't bother double converting to charIndex only to convert back to codepointIndex

SEE:FOOTNOTE:CAPTURES
*/