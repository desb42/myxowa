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
import gplx.core.btries.Btrie_rv;
import gplx.Bry_;
public class Db_btrie_whitelist_style implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_whitelist_style(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("7d6a7a5adbc88d42f0330be165afbe4e"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;
		int c = b;

		switch (b) {
			case 47:
				if (ofs+1 < src_len && src[ofs+1] == '*') {
					found = ofs + 2;
					offset = 7; // ('/*', 7)
				}
				break;
			case 97:
			case 65:
				if (ofs+10 < src_len && (src[ofs+1] | 32) == 'c' && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'o' && (src[ofs+10] | 32) == 'r') {
					found = ofs + 11;
					offset = 2; // ('accelerator', 2)
				}
				break;
			case 101:
			case 69:
				if (ofs+9 < src_len && (src[ofs+1] | 32) == 'x' && (src[ofs+2] | 32) == 'p' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'n') {
					found = ofs + 10;
					offset = 0; // ('expression', 0)
				}
				break;
			case 102:
			case 70:
				if (ofs+5 < src_len && (src[ofs+1] | 32) == 'i' && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
					found = ofs + 6;
					offset = 1; // ('filter', 1)
				}
				break;
			case 105:
			case 73:
				if (ofs+4 < src_len && (src[ofs+1] | 32) == 'm' && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'e') {
					if (ofs+8 < src_len && src[ofs+5] == '-' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 't') {
						found = ofs + 9;
						offset = 6; // ('image-set', 6)
					}
					if (found == -1) {
						found = ofs + 5;
						offset = 5; // ('image', 5)
					}
				}
				break;
			case 117:
			case 85:
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'r' && (src[ofs+2] | 32) == 'l') {
					if (ofs+3 < src_len && (src[ofs+3] | 32) == 's') {
						found = ofs + 4;
						offset = 4; // ('urls', 4)
					}
					if (found == -1) {
						found = ofs + 3;
						offset = 3; // ('url', 3)
					}
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
