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
public class Db_btrie_src_end_b implements Db_btrie {
	private final Object[] objs;
	public Db_btrie_src_end_b(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("8ddd0b726eba7d9a868db95154729ee7"); }
	private Db_btrie_result Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		int found = -1;
		int offset = -1;

		switch (b) {
			case 9:
				found = ofs + 1;
				offset = 4; // ('\t', 4)
				break;
			case 10:
				found = ofs + 1;
				offset = 6; // ('\n', 6)
				break;
			case 13:
				found = ofs + 1;
				offset = 14; // ('\r', 14)
				break;
			case ' ':
				found = ofs + 1;
				offset = 3; // (' ', 3)
				break;
			case '&':
				if (ofs+4 < src_len && src[ofs+1] == '#' && src[ofs+2] == '0' && src[ofs+3] == '9' && src[ofs+4] == ';') {
					found = ofs + 5;
					offset = 5; // ('&#09;', 5)
				}
				break;
			case ':':
				found = ofs + 1;
				offset = 2; // (':', 2)
				break;
			case '<':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case '!':
						if (ofs+3 < src_len && src[ofs+2] == '-' && src[ofs+3] == '-') {
							found = ofs + 4;
							offset = 11; // ('<!--', 11)
						}
						break;
					case 't':
					case 'T':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'v' && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'r' && src[ofs+5] == '|') {
							found = ofs + 6;
							offset = 13; // ('<tvar|', 13)
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 12; // ('<', 12)
				}
				break;
			case '=':
				found = ofs + 1;
				offset = 1; // ('=', 1)
				break;
			case '[':
				if (ofs+1 < src_len && src[ofs+1] == '[') {
					found = ofs + 2;
					offset = 9; // ('[[', 9)
				}
				break;
			case ']':
				if (ofs+1 < src_len && src[ofs+1] == ']') {
					found = ofs + 2;
					offset = 10; // (']]', 10)
				}
				break;
			case '_':
				if (ofs+1 < src_len && src[ofs+1] == '_') {
					found = ofs + 2;
					offset = 15; // ('__', 15)
				}
				break;
			case '{':
				if (ofs+1 < src_len && src[ofs+1] == '{') {
					found = ofs + 2;
					offset = 7; // ('{{', 7)
				}
				break;
			case '|':
				found = ofs + 1;
				offset = 0; // ('|', 0)
				break;
			case '}':
				if (ofs+1 < src_len && src[ofs+1] == '}') {
					found = ofs + 2;
					offset = 8; // ('}}', 8)
				}
				break;
		}
		return new Db_btrie_result(found, offset);
	}

	@Override public Object Match_expand(Btrie_rv rv, byte[] src, int ofs, int src_len) {
		// this check should have been made by parent call
		//if (ofs >= src_len) {
		//	rv.Init(ofs, null);
		//	return null;
		//}
		Db_btrie_result res = Match_with_b(src[ofs], src, ofs, src_len);
		if (res.found == -1) {
			rv.Init(ofs, null);
			return null;
		}
		else {
			Object rv_obj = objs[res.offset];
			rv.Init(res.found, rv_obj);
			return rv_obj;
		}
	}
	@Override public Object Match_bgn(byte[] src, int bgn_pos, int end_pos) {
		// this check should have been made by parent call
		//if (bgn_pos >= end_pos)
		//	return null;
		Db_btrie_result res = Match_with_b(src[bgn_pos], src, bgn_pos, end_pos);
		if (res.found == -1) {
			return null;
		}
		else {
			Object rv_obj = objs[res.offset];
			return rv_obj;
		}
	}
	@Override public Object Match_at_w_b0(Btrie_rv rv, byte b, byte[] src, int bgn_pos, int end_pos) {
		// this check should have been made by parent call
		//if (ofs >= src_len) {
		//	rv.Init(ofs, null);
		//	return null;
		//}
		Db_btrie_result res = Match_with_b(b, src, bgn_pos, end_pos);
		if (res.found == -1) {
			rv.Init(bgn_pos, null);
			return null;
		}
		else {
			Object rv_obj = objs[res.offset];
			rv.Init(res.found, rv_obj);
			return rv_obj;
		}
	}
}
