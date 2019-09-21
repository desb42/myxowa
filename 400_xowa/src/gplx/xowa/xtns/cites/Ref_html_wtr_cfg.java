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
package gplx.xowa.xtns.cites; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.brys.fmtrs.*;
public class Ref_html_wtr_cfg {
	private Bry_bfr itm_tmp_bfr = Bry_bfr_.New(), grp_tmp_bfr = Bry_bfr_.New();
	private Xowe_wiki wiki;
	public Bry_fmtr Itm_html() 			{return itm_html;} 			private Bry_fmtr itm_html; 			public Ref_html_wtr_cfg Itm_html_(String v) {itm_html 				= Bry_fmtr.new_(v, "itm_id", "grp_id", "grp_key"); return this;}
	public Bry_fmtr Itm_id_uid() 		{return itm_id_uid;} 		private Bry_fmtr itm_id_uid; 		public Ref_html_wtr_cfg Itm_id_uid_(String v) {itm_id_uid 			= Bry_fmtr.new_(v, "pre", "uid", "suf"); return this;}
	public Bry_fmtr Itm_id_key_one() 	{return itm_id_key_one;} 	private Bry_fmtr itm_id_key_one; 	public Ref_html_wtr_cfg Itm_id_key_one_(String v) {itm_id_key_one 	= Bry_fmtr.new_(v, "pre", "itm_key", "uid", "minor", "suf"); return this;}
	public Bry_fmtr Itm_id_key_many() 	{return itm_id_key_many;} 	private Bry_fmtr itm_id_key_many; 	public Ref_html_wtr_cfg Itm_id_key_many_(String v) {itm_id_key_many	= Bry_fmtr.new_(v, "pre", "itm_key", "uid", "suf"); return this;}
	public Bry_fmtr Itm_grp_text() 		{return itm_grp_text;} 		private Bry_fmtr itm_grp_text; 		public Ref_html_wtr_cfg Itm_grp_text_(String v) {itm_grp_text 		= Bry_fmtr.new_(v, "grp_key", "major"); return this;}
	public Bry_fmtr Grp_html_one() 		{return grp_html_one;} 		private Bry_fmtr grp_html_one; 		public Ref_html_wtr_cfg Grp_html_one_(String v) {grp_html_one 		= Bry_fmtr.new_(v, "grp_id", "itm_id", "text"); return this;}
	public Bry_fmtr Grp_html_many() 	{return grp_html_many;}		private Bry_fmtr grp_html_many; 	public Ref_html_wtr_cfg Grp_html_many_(String v) {grp_html_many 	= Bry_fmtr.new_(v, "grp_id", "related_ids", "text"); return this;}
	public Bry_fmtr Grp_html_list()		{return grp_html_list;}		private Bry_fmtr grp_html_list;		public Ref_html_wtr_cfg Grp_html_list_(String v) {grp_html_list		= Bry_fmtr.new_(v, "itm_id", "backlabel"); return this;}
	public Bry_fmtr Grp_id_uid() 		{return grp_id_uid;} 		private Bry_fmtr grp_id_uid; 		public Ref_html_wtr_cfg Grp_id_uid_(String v) {grp_id_uid 			= Bry_fmtr.new_(v, "pre", "uid", "suf"); return this;}
	public Bry_fmtr Grp_id_key() 		{return grp_id_key;} 		private Bry_fmtr grp_id_key; 		public Ref_html_wtr_cfg Grp_id_key_(String v) {grp_id_key 			= Bry_fmtr.new_(v, "pre", "itm_key", "major", "suf"); return this;}
	public String Grp_many_and() 		{return grp_many_and;} 		private String grp_many_and; 		public void Grp_many_and_(String v) {grp_many_and = v;}
	public String Grp_many_sep() 		{return grp_many_sep;} 		private String grp_many_sep; 		public void Grp_many_sep_(String v) {grp_many_sep = v;}
	public String Itm_crlp() 		{return itm_ref_prefix;} 		private String itm_ref_prefix; 		public void Itm_crlp_(String v) {itm_ref_prefix = v;}
	public String Itm_crls() 		{return itm_ref_suffix;} 		private String itm_ref_suffix; 		public void Itm_crls_(String v) {itm_ref_suffix = v;}
	public String Itm_crslp() 		{return itm_refs_prefix;} 		private String itm_refs_prefix; 		public void Itm_crslp_(String v) {itm_refs_prefix = v;}
	public String Itm_crsls() 		{return itm_refs_suffix;} 		private String itm_refs_suffix; 		public void Itm_crsls_(String v) {itm_refs_suffix = v;}
	public String Itm_accessibility_label() 		{return itm_accessibility_label;} 		private String itm_accessibility_label; 		public void Itm_accessibility_label_(String v) {itm_accessibility_label = v;}
	public byte[][] Backlabels() {return backlabels;} private byte[][] backlabels;
	public int Backlabels_len() {return backlabels_len;} private int backlabels_len;
	public byte[] Grp_bgn() {return grp_bgn;} private byte[] grp_bgn;
	public byte[] Grp_end() {return grp_end;} private byte[] grp_end;
	public void Init_by_wiki(Xowe_wiki wiki) {
		this.wiki = wiki;
		byte[] ref1_bry, ref2_bry;
                
		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_backlabels);
		Backlabels_(Ref_backlabels_xby_bry(ref1_bry));
                
		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crlp);
		//System.out.println("crlp" + String_.new_u8(ref1_bry));
		Itm_crlp_(cvt(String_.new_u8(ref1_bry)));
		ref2_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crls);
		Itm_crls_(cvt(String_.new_u8(ref2_bry)));
		
		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslp);
		Itm_crslp_(cvt(String_.new_u8(ref1_bry)));
		ref2_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crsls);
		Itm_crsls_(cvt(String_.new_u8(ref2_bry)));

		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crl);
		//System.out.println("crl" + String_.new_u8(ref1_bry));
		Itm_html_(cvt(String_.new_u8(ref1_bry)));

		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslo);
		//System.out.println("crslo" + String_.new_u8(ref1_bry));
		Grp_html_one_(cvt(String_.new_u8(ref1_bry)));

		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslm);
		//System.out.println("crslm" + String_.new_u8(ref1_bry));
		Grp_html_many_(cvt(String_.new_u8(ref1_bry)));
                
		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslma);
		//System.out.println("crslma" + String_.new_u8(ref1_bry));
		Grp_many_and_(cvt(String_.new_u8(ref1_bry)));
		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslms);
		//System.out.println("crslms" + String_.new_u8(ref1_bry));
		Grp_many_sep_(cvt(String_.new_u8(ref1_bry)));
                
		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslmf);
		//System.out.println("crslmf" + String_.new_u8(ref1_bry));
		Grp_html_list_(cvt(String_.new_u8(ref1_bry)));

		ref1_bry = wiki.Msg_mgr().Val_by_key_obj(Msg_crslal);
		//System.out.println("crslal" + String_.new_u8(ref1_bry));
		if (ref1_bry != Bry_.Empty)
			Itm_accessibility_label_(cvt(" title=\"" + String_.new_u8(ref1_bry) + "\""));
	}
	public byte[] Get_itm(Ref_nde itm, boolean caller_is_ref) {
		itm_tmp_bfr.Clear();
		if (itm.Name() == Bry_.Empty)
		    itm_tmp_bfr.Add_long_variable(itm.Uid());
		else {
			itm_tmp_bfr.Add(itm.Name());
			if (caller_is_ref) {
				itm_tmp_bfr.Add_byte(Byte_ascii.Underline);
				itm_tmp_bfr.Add_long_variable(itm.Idx_major());
				itm_tmp_bfr.Add_byte(Byte_ascii.Dash);
				itm_tmp_bfr.Add_long_variable(itm.Idx_minor());
			} else {
				itm_tmp_bfr.Add_byte(Byte_ascii.Dash);
				itm_tmp_bfr.Add_long_variable(itm.Idx_major());
			}
		}
		return itm_tmp_bfr.To_bry_and_clear();
	}
	public byte[] Get_grp(Ref_nde itm) {
		grp_tmp_bfr.Clear();
		if (itm.Name() == Bry_.Empty)
		    grp_tmp_bfr.Add_long_variable(itm.Uid());
		else {
			grp_tmp_bfr.Add(itm.Name());
			grp_tmp_bfr.Add_byte(Byte_ascii.Dash);
			grp_tmp_bfr.Add_long_variable(itm.Idx_major());
		}
		return grp_tmp_bfr.To_bry_and_clear();
	}
	public void Build_ref(Ref_nde itm, boolean caller_is_ref, byte[] txt) {
		byte [] bitm = Get_itm(itm, caller_is_ref);
		byte [] bgrp = Get_grp(itm);
		byte[] ref = wiki.Msg_mgr().Val_by_key_args(Msg_crl, bitm, bgrp, txt);
	}

	public void Backlabels_(byte[][] v) {
		backlabels		= v;
		backlabels_len	= v.length;
	}
	public static final byte[]
	  Msg_backlabels_err = Bry_.new_a7("cite_error_no_link_label_group")
	, Msg_backlabels = Bry_.new_a7("cite_references_link_many_format_backlink_labels")
	, Msg_crlp = Bry_.new_a7("cite_reference_link_prefix")
	, Msg_crls = Bry_.new_a7("cite_reference_link_suffix")
	, Msg_crslp = Bry_.new_a7("cite_references_link_prefix")
	, Msg_crsls = Bry_.new_a7("cite_references_link_suffix")
	, Msg_crl = Bry_.new_a7("cite_reference_link")
	, Msg_crsnl = Bry_.new_a7("cite_references_no_link")
	, Msg_crslo = Bry_.new_a7("cite_references_link_one")
	, Msg_crslm = Bry_.new_a7("cite_references_link_many")
	, Msg_crslmf = Bry_.new_a7("cite_references_link_many_format")
	, Msg_crslma = Bry_.new_a7("cite_references_link_many_and")
	, Msg_crslms = Bry_.new_a7("cite_references_link_many_sep")
	, Msg_crslal = Bry_.new_a7("cite_references_link_accessibility_label")
	;
	public static final    byte[] Note_href_bgn = Bry_.new_a7("#cite_note-");	// for TOC
	public static Ref_html_wtr_cfg new_() {
		Ref_html_wtr_cfg rv = new Ref_html_wtr_cfg();
		rv.Itm_html_		("<sup id=\"~{0}\" class=\"reference\"><a href=\"#~{1}\">[~{2}]</a></sup>");
		rv.Itm_id_uid_		("~{pre}~{uid}~{suf}");
		rv.Itm_id_key_one_	("~{pre}~{itm_key}_~{uid}-~{minor}~{suf}");
		rv.Itm_id_key_many_	("~{pre}~{itm_key}-~{uid}~{suf}");
		rv.Itm_grp_text_	("~{grp_key} ~{major}");
		rv.Grp_html_one_	("<li id=\"cite_note-~{grp_id}\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-~{itm_id}\">^</a></b></span> <span class=\"reference-text\">~{text}</span></li>\n");
		rv.Grp_html_many_	("<li id=\"cite_note-~{grp_id}\"><span class=\"mw-cite-backlink\">^~{related_ids}</span> <span class=\"reference-text\">~{text}</span></li>\n");
		rv.Grp_html_list_	("<sup><i><b>[[#~{0}|~{2}]]</b></i></sup>");
		rv.Grp_id_uid_		("~{pre}~{uid}~{suf}");
		rv.Grp_id_key_		("~{pre}~{itm_key}-~{major}~{suf}");
		rv.Grp_many_sep_        (" ");
		rv.Grp_many_sep_        ("");
		rv.Itm_accessibility_label_ ("");
		rv.grp_bgn = Bry_.new_a7("<ol class=\"references\">\n");
		rv.grp_end = Bry_.new_a7("</ol>\n");
		rv.Backlabels_		(Ref_backlabels_default);
		return rv;
	}
	Ref_html_wtr_cfg() {}
	private static final    byte[][] Ref_backlabels_default = Ref_backlabels_xby_str_ary(String_.Ary	// TEST:default backlabels for test only; actual backlabels will be overrriden by MediaWiki:Cite_references_link_many_format_backlink_labels; DATE:2014-06-07
	(  "a",  "b",  "c",  "d",  "e",  "f",  "g",  "h",  "i",  "j",  "k",  "l",  "m",  "n",  "o",  "p",  "q",  "r",  "s",  "t",  "u",  "v",  "w",  "x",  "y",  "z"
	, "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az"
	, "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz"
	, "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz"
	, "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj", "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv", "dw", "dx", "dy", "dz"
	, "ea", "eb", "ec", "ed", "ee", "ef", "eg", "eh", "ei", "ej", "ek", "el", "em", "en", "eo", "ep", "eq", "er", "es", "et", "eu", "ev", "ew", "ex", "ey", "ez"
	, "fa", "fb", "fc", "fd", "fe", "ff", "fg", "fh", "fi", "fj", "fk", "fl", "fm", "fn", "fo", "fp", "fq", "fr", "fs", "ft", "fu", "fv", "fw", "fx", "fy", "fz"
	, "ga", "gb", "gc", "gd", "ge", "gf", "gg", "gh", "gi", "gj", "gk", "gl", "gm", "gn", "go", "gp", "gq", "gr", "gs", "gt", "gu", "gv", "gw", "gx", "gy", "gz"
	, "ha", "hb", "hc", "hd", "he", "hf", "hg", "hh", "hi", "hj", "hk", "hl", "hm", "hn", "ho", "hp", "hq", "hr", "hs", "ht", "hu", "hv", "hw", "hx", "hy", "hz"
	, "ia", "ib", "ic", "id", "ie", "if", "ig", "ih", "ii", "ij", "ik", "il", "im", "in", "io", "ip", "iq", "ir", "is", "it", "iu", "iv", "iw", "ix", "iy", "iz"
	, "ja", "jb", "jc", "jd", "je", "jf", "jg", "jh", "ji", "jj", "jk", "jl", "jm", "jn", "jo", "jp", "jq", "jr", "js", "jt", "ju", "jv", "jw", "jx", "jy", "jz"
	, "ka", "kb", "kc", "kd", "ke", "kf", "kg", "kh", "ki", "kj", "kk", "kl", "km", "kn", "ko", "kp", "kq", "kr", "ks", "kt", "ku", "kv", "kw", "kx", "ky", "kz"
	, "la", "lb", "lc", "ld", "le", "lf", "lg", "lh", "li", "lj", "lk", "ll", "lm", "ln", "lo", "lp", "lq", "lr", "ls", "lt", "lu", "lv", "lw", "lx", "ly", "lz"
	, "ma", "mb", "mc", "md", "me", "mf", "mg", "mh", "mi", "mj", "mk", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz"
	, "na", "nb", "nc", "nd", "ne", "nf", "ng", "nh", "ni", "nj", "nk", "nl", "nm", "nn", "no", "np", "nq", "nr", "ns", "nt", "nu", "nv", "nw", "nx", "ny", "nz"
	, "oa", "ob", "oc", "od", "oe", "of", "og", "oh", "oi", "oj", "ok", "ol", "om", "on", "oo", "op", "oq", "or", "os", "ot", "ou", "ov", "ow", "ox", "oy", "oz"
	, "pa", "pb", "pc", "pd", "pe", "pf", "pg", "ph", "pi", "pj", "pk", "pl", "pm", "pn", "po", "pp", "pq", "pr", "ps", "pt", "pu", "pv", "pw", "px", "py", "pz"
	, "qa", "qb", "qc", "qd", "qe", "qf", "qg", "qh", "qi", "qj", "qk", "ql", "qm", "qn", "qo", "qp", "qq", "qr", "qs", "qt", "qu", "qv", "qw", "qx", "qy", "qz"
	, "ra", "rb", "rc", "rd", "re", "rf", "rg", "rh", "ri", "rj", "rk", "rl", "rm", "rn", "ro", "rp", "rq", "rr", "rs", "rt", "ru", "rv", "rw", "rx", "ry", "rz"
	, "sa", "sb", "sc", "sd", "se", "sf", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sp", "sq", "sr", "ss", "st", "su", "sv", "sw", "sx", "sy", "sz"
	, "ta", "tb", "tc", "td", "te", "tf", "tg", "th", "ti", "tj", "tk", "tl", "tm", "tn", "to", "tp", "tq", "tr", "ts", "tt", "tu", "tv", "tw", "tx", "ty", "tz"
	, "ua", "ub", "uc", "ud", "ue", "uf", "ug", "uh", "ui", "uj", "uk", "ul", "um", "un", "uo", "up", "uq", "ur", "us", "ut", "uu", "uv", "uw", "ux", "uy", "uz"
	, "va", "vb", "vc", "vd", "ve", "vf", "vg", "vh", "vi", "vj", "vk", "vl", "vm", "vn", "vo", "vp", "vq", "vr", "vs", "vt", "vu", "vv", "vw", "vx", "vy", "vz"
	, "wa", "wb", "wc", "wd", "we", "wf", "wg", "wh", "wi", "wj", "wk", "wl", "wm", "wn", "wo", "wp", "wq", "wr", "ws", "wt", "wu", "wv", "ww", "wx", "wy", "wz"
	, "xa", "xb", "xc", "xd", "xe", "xf", "xg", "xh", "xi", "xj", "xk", "xl", "xm", "xn", "xo", "xp", "xq", "xr", "xs", "xt", "xu", "xv", "xw", "xx", "xy", "xz"
	, "ya", "yb", "yc", "yd", "ye", "yf", "yg", "yh", "yi", "yj", "yk", "yl", "ym", "yn", "yo", "yp", "yq", "yr", "ys", "yt", "yu", "yv", "yw", "yx", "yy", "yz"
	, "za", "zb", "zc", "zd", "ze", "zf", "zg", "zh", "zi", "zj", "zk", "zl", "zm", "zn", "zo", "zp", "zq", "zr", "zs", "zt", "zu", "zv", "zw", "zx", "zy", "zz"
	));
	private static byte[][] Ref_backlabels_xby_str_ary(String[] ary) {
		int ary_len = ary.length;
		byte[][] rv = new byte[ary_len][];
		for (int i = 0; i < ary_len; i++)
			rv[i] = Bry_.new_a7(ary[i]);
		return rv;
	}
	public static byte[][] Ref_backlabels_xby_bry(byte[] raw) {
		if (raw == null) return Ref_backlabels_default;
		List_adp list = List_adp_.New();
		int len = raw.length, pos = 0, bgn = -1;
		while (true) {
			boolean last = pos == len;
			byte b = last ? Byte_ascii.Space : raw[pos];
			switch (b) {
				case Byte_ascii.Space: case Byte_ascii.Nl: case Byte_ascii.Tab:
					if (bgn != -1) { // guard against leading ws, or multiple ws; EX: "bos\s\s" or "\s\s"
						list.Add(Bry_.Mid(raw, bgn, pos));
						bgn = -1;
					}
					break;
				default:
					if (bgn == -1)
						bgn = pos;
					break;
			}
			if (last) break;
			++pos;
		}
		return (byte[][])list.To_ary_and_clear(byte[].class);
	}
	private String cvt(String str) {
		str = str.replaceAll("'''''([^']*)'''''", "<b><i>$1</i></b>");
		str = str.replaceAll("'''([^']*)'''", "<b>$1</b>");
		str = str.replaceAll("''([^']*)''", "<i>$1</i>");
		return str;
	}
}
