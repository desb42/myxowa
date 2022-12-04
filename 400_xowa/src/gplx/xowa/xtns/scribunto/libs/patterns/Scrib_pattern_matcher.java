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
package gplx.xowa.xtns.scribunto.libs.patterns;
import gplx.*;
import gplx.xowa.xtns.scribunto.libs.*;
import gplx.objects.strings.unicodes.*;
import gplx.langs.regxs.*;
import org.luaj.vm2.lib.Str_find_mgr;
public abstract class Scrib_pattern_matcher {
	//protected final    Scrib_regx_converter regx_converter = new Scrib_regx_converter();
	//public Keyval[] Capt_ary() {return regx_converter.Capt_ary();}
	public abstract Regx_match Match_one(Ustring src_ucs, String pat_str, int bgn_as_codes, boolean replace);
	public abstract String Gsub(Scrib_lib_ustring_gsub_mgr gsub_mgr, Ustring src_ucs, String pat_str, int bgn_as_codes);
	public abstract List_adp Split(Ustring src_ucs, String pat_str);

	public static boolean Mode_is_xowa() {return Bool_.Y;} 
	public static Scrib_pattern_matcher New(byte[] page_url) {
            return (Scrib_pattern_matcher)new Scrib_pattern_matcher__xowa(page_url);
//		return Mode_is_xowa()
//			? (Scrib_pattern_matcher)new Scrib_pattern_matcher__xowa(page_url)
//			: (Scrib_pattern_matcher)new Scrib_pattern_matcher__regx(page_url)
//			;
	}
	public abstract Str_find_mgr Find_mgr(Ustring src_ucs, String pat_str, int bgn_as_codes);
	public abstract Regx_match Match_with_mgr(Str_find_mgr mgr, int bgn_as_codes, Ustring src_ucs);
}
