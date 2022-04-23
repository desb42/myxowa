/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2022 https://github.com/desb42

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/

package gplx.xowa;
//import gplx.*;
import gplx.core.intls.Gfo_case_mgr;
import gplx.xowa.langs.cases.Xol_case_itm;
//import gplx.xowa.langs.cases.Xol_case_itm_;
import gplx.core.intls.*;
public class DB_case_mgr implements Gfo_case_mgr {
	public DB_case_mgr(byte tid) {this.tid = tid;}
	private final byte tid;
	@Override public byte Tid() {return tid;}
	@Override public Gfo_case_itm Get_or_null(byte bgn_byte, byte[] src, int bgn, int end) {
		Object rv = DB_case_mgr_.Lower(src, bgn, end);
		return rv == null
			? (Gfo_case_itm)DB_case_mgr_.Upper(src, bgn, end)
			: (Gfo_case_itm)rv;
	}
	@Override public Gfo_case_itm Get_or_null(byte[] src, int bgn, int end) {
		Object rv = DB_case_mgr_.Lower(src, bgn, end);
		return rv == null
			? (Gfo_case_itm)DB_case_mgr_.Upper(src, bgn, end)
			: (Gfo_case_itm)rv;
	}

	public static byte[] Case_build_1st(boolean upper, byte[] src) {
		int len = src.length;
		if (len == 0) return src;	// upper "" -> ""
		
		byte b = src[0];
		int b_len = gplx.core.intls.Utf8_.Len_of_char_by_1st_byte(b);
		boolean change = false;
		if (b_len == 1) {
			byte bc = b;
			if (upper) {
				if (bc >= 'a' && bc <= 'z') {
					bc -= 32;
					change = true;
				}
			}
			else {
				if (bc >= 'A' && bc <= 'Z') {
					bc += 32;
					change = true;
				}
			}
			if (change) {
				byte[] rv = new byte[len];
				rv[0] = bc;
				for (int i = 1; i < len; i++)
					rv[i] = src[i];
				return rv;
			}
			else
				return src;
		}
		byte[] bca = null;
		if (upper) {
			Xol_case_itm rv = DB_case_mgr_.Upper(src, 0, len);
			if (rv == null)
				return src;
			bca = rv.Upper();
		}
		else {
			Xol_case_itm rv = DB_case_mgr_.Lower(src, 0, len);
			if (rv == null)
				return src;
			bca = rv.Lower();
		}
		int bca_len = bca.length;
		for (int i = 0; i < bca_len; i++) {
			if (bca[i] != src[i]) {
				change = true;
				break;
			}
		}
		if (change) {
			byte[] rv = new byte[bca_len + len - b_len]; // in case of asymmetry
			int i;
			for (i = 0; i < bca_len; i++) {
				rv[i] = bca[i];
			}
			for (int j = b_len; j < len; j++) {
				rv[i++] = src[j];
			}
			return rv;
		}
		return src;
	}
	public static byte[] Case_build_1st_reuse(boolean upper, byte[] src) {
		int len = src.length;
		if (len == 0) return src;	// upper "" -> ""
		
		byte b = src[0];
		int b_len = gplx.core.intls.Utf8_.Len_of_char_by_1st_byte(b);
		if (b_len == 1) {
			byte bc = b;
			if (upper) {
				if (bc >= 'a' && bc <= 'z') {
					bc -= 32;
				}
			}
			else {
				if (bc >= 'A' && bc <= 'Z') {
					bc += 32;
				}
			}
			src[0] = bc;
			return src;
		}
		byte[] bca;
		if (upper) {
			Xol_case_itm rv = DB_case_mgr_.Upper(src, 0, len);
			if (rv == null)
				return src;
			bca = rv.Upper();
		}
		else {
			Xol_case_itm rv = DB_case_mgr_.Lower(src, 0, len);
			if (rv == null)
				return src;
			bca = rv.Lower();
		}
		int bca_len = bca.length;
                if (bca_len == b_len) {
                    for (int i = 0; i < bca_len; i++)
                        src[i] = bca[i];
                    return src;
                }
			byte[] rv = new byte[bca_len + len - b_len]; // in case of asymmetry
			int i;
			for (i = 0; i < bca_len; i++) {
				rv[i] = bca[i];
			}
			for (int j = b_len; j < len; j++) {
				rv[i++] = src[j];
			}
			return rv;
	}
	public static byte[] Case_build_reuse(boolean upper, byte[] src) {
		int end = src.length;
		int pos = 0;
		boolean rebuild = false;
		while (true) {
			if (pos >= end) break;
			byte b = src[pos];
			int b_len = gplx.core.intls.Utf8_.Len_of_char_by_1st_byte(b);
			if (b_len == 1) { // must be ascii
				if (upper) {
					if (b >= 'a' && b <= 'z') {
						src[pos] = (byte)(b - 32);
					}
				}
				else {
					if (b >= 'A' && b <= 'Z') {
						src[pos] = (byte)(b + 32);
					}
				}
			}
			else {
				byte[] bca = null;
				if (upper) {
					Xol_case_itm rv = DB_case_mgr_.Upper(src, pos, end);
					if (rv != null)
						bca = rv.Upper();
				}
				else {
					Xol_case_itm rv = DB_case_mgr_.Lower(src, pos, end);
					if (rv != null)
						bca = rv.Lower();
				}
                                if (bca != null) {
				int bca_len = bca.length;
				if (bca_len == b_len) {
					for (int i = 0; i < b_len; i++)
						src[pos + i] = bca[i];
				}
				else {
					rebuild = true;
					break;
				}
                                }
			}
			pos += b_len;
		}
		if (rebuild) {
			// do it the hard way
			int a=1;
		}
		return src;
	}
	public static boolean Match_any_exists(byte[] src, int bgn_pos, int end_pos) {
		return DB_case_mgr_.Upper(src, bgn_pos, end_pos) != null
                        || DB_case_mgr_.Lower(src, bgn_pos, end_pos) != null;
	}
}