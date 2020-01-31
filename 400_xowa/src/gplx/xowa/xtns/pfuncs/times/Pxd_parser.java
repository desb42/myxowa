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
package gplx.xowa.xtns.pfuncs.times; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.core.brys.*; import gplx.core.brys.fmtrs.*; import gplx.core.btries.*; import gplx.core.log_msgs.*;
import gplx.xowa.parsers.*;
class Pxd_parser {
	private final    Btrie_rv trv = new Btrie_rv();
	private byte[] src; int cur_pos, tkn_bgn_pos, src_len, tkn_type;
	public Pxd_itm[] Tkns() {return tkns;} Pxd_itm[] tkns;
	public int Tkns_len() {return tkns_len;} private int tkns_len;
	public Pxd_itm[] Data_ary() {return data_ary;} Pxd_itm[] data_ary;
	public int Data_ary_len() {return data_ary_len;} private int data_ary_len;
	public int Colon_count;
	public boolean Has_date() { return hasdate; } private boolean hasdate;
	public void Has_date_() {
		if (hasdate)
			throw Err_.new_unhandled(0);
		hasdate = true;
	}
	public boolean Has_time() { return hastime; } private boolean hastime;
	public void Has_time_() {
		if (hastime)
			throw Err_.new_unhandled(0);
		hastime = true;
	}
	public int Data_idx_adj() { return dataidxadj;} public void Inc_data_idx_adj() { dataidxadj++; }
	private int dataidxadj;
	public int[] Seg_idxs() {return seg_idxs;} private int[] seg_idxs = new int[DateAdp_.SegIdx__max];	// temp ary for storing current state
	public byte[] Src() {return src;}
	public Pxd_parser(Xop_ctx ctx) {
		this.trie = Pxd_parser_.Trie(ctx);
                Dbx_scan_support.Init(trie);
	}
	public boolean Seg_idxs_chk(int... ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++)
			if (ary[i] == Pxd_itm_base.Seg_idx_null) return false;
		return true;
	}
	public void Seg_idxs_(Pxd_itm_int  itm, int seg_idx)				{Seg_idxs_(itm, seg_idx, itm.Val());}
	public void Seg_idxs_(Pxd_itm_base itm, int seg_idx, int val) {
		itm.Seg_idx_(seg_idx);
		if (seg_idx >= 0)	// ignore Seg_idx_null and Seg_idx_skip
			seg_idxs[seg_idx] = val;
	}
	public void Err_set(Gfo_msg_itm itm, Bfr_arg... args) {
		if (itm == null) return;
		Bry_fmtr fmtr = itm.Fmtr();
		fmtr.Bld_bfr(error_bfr, args);
	}	private Bry_bfr error_bfr = Bry_bfr_.New_w_size(32);
	public DateAdp Parse(byte[] src, Bry_bfr error_bfr, Xoa_ttl ttl) {
            return Dbx_scan_support.Parse(src);
/*		hasdate = false;
		hastime = false;
		dataidxadj = 0;
		System.out.println(String_.new_a7(src));
		//Tokenize(src);	// NOTE: should check if Tokenize failed, but want to be liberal as date parser is not fully implemented; this will always default to 1st day of year; DATE:2014-03-27
		// fake it!! [abcd, YYYY] => [YYYY abcd]
		int slen = src.length;
		int ofs = 0;
		if (slen > 21) {
			if (src[slen - 20] == ',')
				ofs = -20;
			if (src[slen - 11] == ',')
				ofs = -11;
			if (src[slen - 6] == ',')
				ofs = -6;
			if (src[slen - 5] == ',')
				ofs = -5;
		}
		else if (slen > 12) {
			if (src[slen - 11] == ',')
				ofs = -11;
			if (src[slen - 6] == ',')
				ofs = -6;
			if (src[slen - 5] == ',')
				ofs = -5;
		}
		else if (slen > 7) {
			if (src[slen - 6] == ',')
				ofs = -6;
			if (src[slen - 5] == ',')
				ofs = -5;
		}
		else if (slen > 6) {
			if (src[slen - 5] == ',')
				ofs = -5;
		}
*/
		/*if (ofs < 0) {
			if (src[slen - 4] >= '0' && src[slen - 4] <= '9') {
				src = Bry_.Add(Bry_.Mid(src, slen + ofs + 1, slen), Byte_ascii.Space_bry, Bry_.Mid(src, 0, slen + ofs));
			}
		}*/

/*		if (slen > 3) {
			if (src[0] == 'c' && src[1] == '.')
				src = Bry_.new_a7("now");
		}

		//System.out.println("* "+String_.new_a7(src));
		DateAdp dt = DateAdp_.MinValue;
		if (Tokenize(src)) {
                    Month_check();
			dt = Evaluate(src, error_bfr);
                }
		if (dt != DateAdp_.MinValue)
			return dt;
		// dont log some date errors
		boolean report = true;
		int src_len = src.length;
		if (src_len > 2) {
			if ((src[0] == '1' || src[0] == '2') && src[1] == ' ')
				report = false;
			if (src[0] == '{' && src[2] == '{')
				report = false;
			else if (src_len == 6 && Bry_.Eq(src, Bry_.new_a7("einval")))
				report = false;
		}
		else if (src_len == 1 && (src[0] == '1' || src[0] == '2'))
			report = false;
		if (report)
			Gfo_usr_dlg_.Instance.Warn_many("", "", "date parse err: ttl=~{0} txt=~{1}", ttl.Full_db(), src);
		Err_set(Pft_func_time_log.Invalid_day, Bfr_arg_.New_bry(src));
		return null;
*/
	}
	private boolean Tokenize(byte[] src) { 
		this.src = src; src_len = src.length;
		// trim the end of any ' ', '\t' or ')'
		while (src_len > 0) {
			byte b = src[src_len-1];
			if (b == ' ' || b == '\t' || b == ')')
				src_len--;
			else
				break;
		}
		cur_pos = 0;
                // trim start
		while (cur_pos < src_len) {
			byte b = src[cur_pos];
			if (b == ' ' || b == '\t' || b == ')')
				cur_pos++;
			else
				break;
		}
		tkns = new Pxd_itm[src_len]; tkns_len = 0;		
		tkn_type = Pxd_itm_.Tid_null; tkn_bgn_pos = -1;
		Colon_count = 0;
		error_bfr.Clear();
		for (int i = 0; i < DateAdp_.SegIdx__max; i++)
			seg_idxs[i] = Pxd_itm_base.Seg_idx_null;
		while (cur_pos < src_len) {
			byte b = src[cur_pos];
			switch (b) {
				// ignore '(' and ')'
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.Nl: case Byte_ascii.Paren_bgn: case Byte_ascii.Paren_end:
				case Byte_ascii.Comma:
					if (tkn_type != Pxd_itm_.Tid_ws) MakePrvTkn(cur_pos, Pxd_itm_.Tid_ws); break; // SEE:NOTE_1 for logic
				case Byte_ascii.Dash: case Byte_ascii.Dot: case Byte_ascii.Colon: case Byte_ascii.Slash:
					if (tkn_type != b) MakePrvTkn(cur_pos, b); break;
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					if (tkn_type != Pxd_itm_.Tid_int)	MakePrvTkn(cur_pos, Pxd_itm_.Tid_int); break;
				case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
				case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
				case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
				case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
				case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
				case Byte_ascii.Ltr_a: case Byte_ascii.Ltr_b: case Byte_ascii.Ltr_c: case Byte_ascii.Ltr_d: case Byte_ascii.Ltr_e:
				case Byte_ascii.Ltr_f: case Byte_ascii.Ltr_g: case Byte_ascii.Ltr_h: case Byte_ascii.Ltr_i: case Byte_ascii.Ltr_j:
				case Byte_ascii.Ltr_k: case Byte_ascii.Ltr_l: case Byte_ascii.Ltr_m: case Byte_ascii.Ltr_n: case Byte_ascii.Ltr_o:
				case Byte_ascii.Ltr_p: case Byte_ascii.Ltr_q: case Byte_ascii.Ltr_r: case Byte_ascii.Ltr_s: case Byte_ascii.Ltr_t:
				case Byte_ascii.Ltr_u: case Byte_ascii.Ltr_v: case Byte_ascii.Ltr_w: case Byte_ascii.Ltr_x: case Byte_ascii.Ltr_y: case Byte_ascii.Ltr_z:
				case Byte_ascii.At:
					MakePrvTkn(cur_pos, Pxd_itm_.Tid_null);			// first, make prv tkn
					Object o = trie.Match_at_w_b0(trv, b, src, cur_pos, src_len);	// now match String against tkn
					if (o == null) return false;	// unknown letter / word; exit now;
					// a valid timezone name can only be at the end
					if (o instanceof Pxd_itm_tz_abbr && trv.Pos() != src_len) return false;
					tkns[tkns_len] = ((Pxd_itm_prototype)o).MakeNew(tkns_len); 
					++tkns_len;
					cur_pos = trv.Pos() - 1; // -1 b/c trie matches to next char, and ++ below
					break;
				case Byte_ascii.Plus:
					if (tkns_len == 0) { // plus as first char
						tkns[tkns_len] = ((Pxd_itm_prototype)Pxd_itm_time_relative.Now).MakeNew(tkns_len); 
						++tkns_len;
					}
				/*	// FALL THROUGH
				case Byte_ascii.Comma:*/
					MakePrvTkn(cur_pos, Pxd_itm_.Tid_null);
					tkns[tkns_len] = new Pxd_itm_sym(tkns_len, b);
					++tkns_len;
					break;
				default:
					// invalid character
					throw Err_.new_unhandled(0);
			}
			++cur_pos;
		}
		MakePrvTkn(cur_pos, Pxd_itm_.Tid_null);
		return true;
	}
	private void MakePrvTkn(int cur_pos, int nxt_type) {
		Pxd_itm itm = null;
		switch (tkn_type) {
			case Pxd_itm_.Tid_int:
				int int_val = Bry_.To_int_or(src, tkn_bgn_pos, cur_pos, Int_.Min_value);
				if (int_val == Int_.Min_value) {} // FUTURE: warn
				int digits = cur_pos - tkn_bgn_pos;
				switch (digits) {
					case 14:	// yyyyMMddhhmmss
					case 12:	// yyyyMMddhhmm; PAGE:en.w:Boron; DATE:2015-07-29
					case 8:		// yyyyMMdd
						itm = new Pxd_itm_int_dmy_14(tkns_len, Bry_.Mid(src, tkn_bgn_pos, cur_pos), digits); break;
					case 6:
						itm = new Pxd_itm_int_mhs_6(tkns_len, Bry_.Mid(src, tkn_bgn_pos, cur_pos)); break;
					case 4:
						// 4 digits essentially mean 'gnunocolon' (either a year or hour/min)
						itm = new Pxd_itm_int(tkns_len, digits, int_val);
						break;
					default:
						itm = new Pxd_itm_int(tkns_len, digits, int_val); break;
				}
				break;
			case Pxd_itm_.Tid_ws: 		itm = new Pxd_itm_ws(tkns_len); break;
			case Pxd_itm_.Tid_dash:		itm = new Pxd_itm_dash(tkns_len); break;
			case Pxd_itm_.Tid_dot:		itm = new Pxd_itm_dot(tkns_len); break;
			case Pxd_itm_.Tid_colon:	itm = new Pxd_itm_colon(tkns_len); break;
			case Pxd_itm_.Tid_slash:	itm = new Pxd_itm_slash(tkns_len); break;
			case Pxd_itm_.Tid_null:	break; // NOOP
		}
		if (itm != null) {
			tkns[tkns_len] = itm;
			++tkns_len;
		}
		tkn_type = nxt_type;
		tkn_bgn_pos = cur_pos;
	}
	private int Skip_ws(int npos) {
		while (npos < tkns_len) {
			Pxd_itm itm = tkns[npos];
			int tid = itm.Tkn_tid();
			if (tid == Pxd_itm_.Tid_ws || tid == Pxd_itm_.Tid_sym || tid == Pxd_itm_.Tid_day_suffix)
				npos++;
			else
				return npos;
		}
		return -1;
	}
	private void Month_check() {
		int pos = 0;
                int npos;
		Pxd_itm itm;
		Pxd_itm itm_a = null;
		Pxd_itm itm_b = null;
		while (pos < tkns_len) {
			itm = tkns[pos++];
			if (itm.Tkn_tid() == Pxd_itm_.Tid_int) {
				itm_a = itm;
				npos = Skip_ws(pos);
				if (npos < 0) return;
				itm = tkns[npos++];
				if (itm.Tkn_tid() == Pxd_itm_.Tid_month_name) {
					npos = Skip_ws(npos);
					if (npos < 0) return;
					itm = tkns[npos++];
					if (itm.Tkn_tid() == Pxd_itm_.Tid_int)
						itm_b = itm;
                                }
				if (itm_b != null) ((Pxd_itm_int)itm_b).Makeyear();
				if (itm_a != null) ((Pxd_itm_int)itm_a).Makeyear();
				return;
			}
			else if (itm.Tkn_tid() == Pxd_itm_.Tid_month_name) {
				npos = Skip_ws(pos);
				if (npos < 0) return;
				itm = tkns[npos++];
				if (itm.Tkn_tid() == Pxd_itm_.Tid_int)
					itm_b = itm;
				if (itm_b != null) ((Pxd_itm_int)itm_b).Makeyear();
				return;
			}
		}
	}
	DateAdp Evaluate(byte[] src, Bry_bfr error) {
		if (tkns_len == 0) {
			Err_set(Pft_func_time_log.Invalid_day, Bfr_arg_.New_bry(src));
			return null;
		}
		Pxd_itm[] eval_ary = Pxd_itm_sorter.XtoAryAndSort(tkns, tkns_len);
		MakeDataAry();
		for (int i = 0; i < tkns_len; i++) {
			if (!eval_ary[i].Eval(this)) {
				error.Add_bfr_and_clear(error_bfr);
				return DateAdp_.MinValue;
			}
			if (error_bfr.Len() != 0) {
				error.Add_bfr_and_clear(error_bfr);
				return DateAdp_.MinValue;
			}
		}

		// build date
		DateAdp now = Datetime_now.Get();
		Pxd_date_bldr bldr = new Pxd_date_bldr(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0);
		for (int i = 0; i < tkns_len; i++) {
			Pxd_itm itm = (Pxd_itm)tkns[i];
			if (!itm.Time_ini(bldr)) {
				error.Add_str_a7("Invalid time");
				return null;
			}
		}
		return bldr.To_date();
	}
	private void MakeDataAry() {
		data_ary = new Pxd_itm[tkns_len]; data_ary_len = 0;
		for (int i = 0; i < tkns_len; i++) {
			Pxd_itm itm = tkns[i];
			switch (itm.Tkn_tid()) {
				case Pxd_itm_.Tid_month_name:
				case Pxd_itm_.Tid_int:
					itm.Data_idx_(data_ary_len);
					data_ary[data_ary_len++] = itm;
					break;
			}
		}
	}
	private static Btrie_slim_mgr trie;
}
/*
NOTE_1:parsing works by completing previous items and then setting current;
EX: "123  456"
 1: tkn_type = null; b = number
    complete tkn (note that tkn is null)
    set tkn_type to number; set tkn_bgn to 0
 2: b == number == tkn_type; noop
 3: b == number == tkn_type; noop
 4: b == space  != tkn_type;
    complete prv
	  create tkn with bgn = tkn_bgn and end = cur_pos
	set tkn_type to space; set tkn_bgn to cur_pos
 etc..
*/