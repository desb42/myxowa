/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2020 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa;
import gplx.core.btries.Btrie_rv;
import gplx.Bry_;
public class Db_btrie_make_tkn_cs_en implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_make_tkn_cs_en(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("6750adbe740c9d143df06eff7006f1ea"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;

		switch (b) {
			case 'F':
				if (ofs+9 < src_len && src[ofs+1] == 'O' && src[ofs+2] == 'R' && src[ofs+3] == 'C' && src[ofs+4] == 'E' && src[ofs+5] == 'T' && src[ofs+6] == 'O' && src[ofs+7] == 'C' && src[ofs+8] == '_' && src[ofs+9] == '_') {
					found = ofs + 10;
					offset = 2; // ('FORCETOC__', 2)
				}
				break;
			case 'N':
				if (ofs+1 < src_len && src[ofs+1] == 'O') {
					if (ofs+2 < src_len) switch (src[ofs+2]) {
						case 'C':
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case 'C':
									if (ofs+5 < src_len && src[ofs+4] == '_' && src[ofs+5] == '_') {
										found = ofs + 6;
										offset = 9; // ('NOCC__', 9)
									}
									break;
								case 'O':
									if (ofs+17 < src_len && src[ofs+4] == 'N' && src[ofs+5] == 'T' && src[ofs+6] == 'E' && src[ofs+7] == 'N' && src[ofs+8] == 'T' && src[ofs+9] == 'C' && src[ofs+10] == 'O' && src[ofs+11] == 'N' && src[ofs+12] == 'V' && src[ofs+13] == 'E' && src[ofs+14] == 'R' && src[ofs+15] == 'T' && src[ofs+16] == '_' && src[ofs+17] == '_') {
										found = ofs + 18;
										offset = 8; // ('NOCONTENTCONVERT__', 8)
									}
									break;
							}
							break;
						case 'E':
							if (ofs+14 < src_len && src[ofs+3] == 'D' && src[ofs+4] == 'I' && src[ofs+5] == 'T' && src[ofs+6] == 'S' && src[ofs+7] == 'E' && src[ofs+8] == 'C' && src[ofs+9] == 'T' && src[ofs+10] == 'I' && src[ofs+11] == 'O' && src[ofs+12] == 'N' && src[ofs+13] == '_' && src[ofs+14] == '_') {
								found = ofs + 15;
								offset = 5; // ('NOEDITSECTION__', 5)
							}
							break;
						case 'G':
							if (ofs+10 < src_len && src[ofs+3] == 'A' && src[ofs+4] == 'L' && src[ofs+5] == 'L' && src[ofs+6] == 'E' && src[ofs+7] == 'R' && src[ofs+8] == 'Y' && src[ofs+9] == '_' && src[ofs+10] == '_') {
								found = ofs + 11;
								offset = 3; // ('NOGALLERY__', 3)
							}
							break;
						case 'H':
							if (ofs+9 < src_len && src[ofs+3] == 'E' && src[ofs+4] == 'A' && src[ofs+5] == 'D' && src[ofs+6] == 'E' && src[ofs+7] == 'R' && src[ofs+8] == '_' && src[ofs+9] == '_') {
								found = ofs + 10;
								offset = 4; // ('NOHEADER__', 4)
							}
							break;
						case 'T':
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case 'C':
									if (ofs+5 < src_len && src[ofs+4] == '_' && src[ofs+5] == '_') {
										found = ofs + 6;
										offset = 7; // ('NOTC__', 7)
									}
									break;
								case 'I':
									if (ofs+15 < src_len && src[ofs+4] == 'T' && src[ofs+5] == 'L' && src[ofs+6] == 'E' && src[ofs+7] == 'C' && src[ofs+8] == 'O' && src[ofs+9] == 'N' && src[ofs+10] == 'V' && src[ofs+11] == 'E' && src[ofs+12] == 'R' && src[ofs+13] == 'T' && src[ofs+14] == '_' && src[ofs+15] == '_') {
										found = ofs + 16;
										offset = 6; // ('NOTITLECONVERT__', 6)
									}
									break;
								case 'O':
									if (ofs+6 < src_len && src[ofs+4] == 'C' && src[ofs+5] == '_' && src[ofs+6] == '_') {
										found = ofs + 7;
										offset = 1; // ('NOTOC__', 1)
									}
									break;
							}
							break;
					}
				}
				break;
			case 'T':
				if (ofs+4 < src_len && src[ofs+1] == 'O' && src[ofs+2] == 'C' && src[ofs+3] == '_' && src[ofs+4] == '_') {
					found = ofs + 5;
					offset = 0; // ('TOC__', 0)
				}
				break;
		}
	}

	@Override public Object Match_expand(Btrie_rv rv, byte[] src, int ofs, int src_len) {
		// this check should have been made by parent call
		//if (ofs >= src_len) {
		//	rv.Init(ofs, null);
		//	return null;
		//}
		Match_with_b(src[ofs], src, ofs, src_len);
		if (found == -1) {
			rv.Init(ofs, null);
			return null;
		}
		else {
			Object rv_obj = objs[offset];
			rv.Init(found, rv_obj);
			return rv_obj;
		}
	}
	@Override public Object Match_bgn(byte[] src, int bgn_pos, int end_pos) {
		// this check should have been made by parent call
		//if (bgn_pos >= end_pos)
		//	return null;
		Match_with_b(src[bgn_pos], src, bgn_pos, end_pos);
		if (found == -1) {
			return null;
		}
		else {
			Object rv_obj = objs[offset];
			return rv_obj;
		}
	}
	@Override public Object Match_at_w_b0(Btrie_rv rv, byte b, byte[] src, int bgn_pos, int end_pos) {
		// this check should have been made by parent call
		//if (ofs >= src_len) {
		//	rv.Init(ofs, null);
		//	return null;
		//}
		Match_with_b(b, src, bgn_pos, end_pos);
		if (found == -1) {
			rv.Init(bgn_pos, null);
			return null;
		}
		else {
			Object rv_obj = objs[offset];
			rv.Init(found, rv_obj);
			return rv_obj;
		}
	}
}
