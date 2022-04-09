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
public class Db_btrie_ci_trie_en implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_ci_trie_en(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("9baaf930121fbdae3314ca4a56c8ad5c"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;
		int c = b;

		switch (b) {
			case 35:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 's' && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'm' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 't') {
							found = ofs + 11;
							offset = 71; // ('#assessment', 71)
						}
						break;
					case 99:
					case 67:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+12 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r' && (src[ofs+8] | 32) == 'y' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'e') {
									found = ofs + 13;
									offset = 70; // ('#categorytree', 70)
								}
								break;
							case 111:
							case 79:
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 's') {
									found = ofs + 12;
									offset = 44; // ('#coordinates', 44)
								}
								break;
						}
						break;
					case 100:
					case 68:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'f' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r' && (src[ofs+8] | 32) == 'm' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 't') {
									found = ofs + 11;
									offset = 14; // ('#dateformat', 14)
								}
								break;
							case 100:
							case 68:
								if (ofs+6 < src_len && src[ofs+3] == '2' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'm' && (src[ofs+6] | 32) == 's') {
									found = ofs + 7;
									offset = 61; // ('#dd2dms', 61)
								}
								break;
							case 101:
							case 69:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'g' && src[ofs+4] == '2' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'd') {
									found = ofs + 7;
									offset = 60; // ('#deg2dd', 60)
								}
								break;
						}
						break;
					case 101:
					case 69:
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'x' && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'r') {
							found = ofs + 5;
							offset = 27; // ('#expr', 27)
						}
						break;
					case 102:
					case 70:
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'e') {
							found = ofs + 11;
							offset = 13; // ('#formatdate', 13)
						}
						break;
					case 103:
					case 71:
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'k') {
							found = ofs + 8;
							offset = 62; // ('#geolink', 62)
						}
						break;
					case 105:
					case 73:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 102:
							case 70:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'e') {
									if (ofs+4 < src_len) switch ((src[ofs+4])) {
										case 113:
										case 81:
											found = ofs + 5;
											offset = 29; // ('#ifeq', 29)
											break;
										case 114:
										case 82:
											if (ofs+7 < src_len && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r') {
												found = ofs + 8;
												offset = 30; // ('#iferror', 30)
											}
											break;
										case 120:
										case 88:
											if (ofs+5 < src_len) switch ((src[ofs+5])) {
												case 105:
												case 73:
													if (ofs+7 < src_len && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 't') {
														found = ofs + 8;
														offset = 32; // ('#ifexist', 32)
													}
													break;
												case 112:
												case 80:
													if (ofs+6 < src_len && (src[ofs+6] | 32) == 'r') {
														found = ofs + 7;
														offset = 31; // ('#ifexpr', 31)
													}
													break;
											}
											break;
									}
								}
								if (found == -1) {
									found = ofs + 3;
									offset = 28; // ('#if', 28)
								}
								break;
							case 110:
							case 78:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 115:
									case 83:
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'r') {
											found = ofs + 8;
											offset = 65; // ('#insider', 65)
										}
										break;
									case 118:
									case 86:
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'k' && (src[ofs+6] | 32) == 'e') {
											found = ofs + 7;
											offset = 53; // ('#invoke', 53)
										}
										break;
								}
								break;
							case 115:
							case 83:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'n') {
									found = ofs + 5;
									offset = 63; // ('#isin', 63)
								}
								break;
						}
						break;
					case 108:
					case 76:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'g' && (src[ofs+8] | 32) == 'e') {
									found = ofs + 9;
									offset = 23; // ('#language', 23)
								}
								break;
							case 115:
							case 83:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 't') {
									if (ofs+4 < src_len) switch ((src[ofs+4])) {
										case 104:
										case 72:
											found = ofs + 5;
											offset = 51; // ('#lsth', 51)
											break;
										case 120:
										case 88:
											found = ofs + 5;
											offset = 49; // ('#lstx', 49)
											break;
									}
									if (found == -1) {
										found = ofs + 4;
										offset = 47; // ('#lst', 47)
									}
								}
								break;
						}
						break;
					case 110:
					case 78:
						if (ofs+13 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'w' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'w' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'k') {
							found = ofs + 14;
							offset = 75; // ('#newwindowlink', 75)
						}
						break;
					case 112:
					case 80:
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'y') {
							found = ofs + 9;
							offset = 54; // ('#property', 54)
						}
						break;
					case 114:
					case 82:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'l') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case 50:
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'b' && (src[ofs+7] | 32) == 's') {
										found = ofs + 8;
										offset = 33; // ('#rel2abs', 33)
									}
									break;
								case 97:
								case 65:
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'd') {
										found = ofs + 8;
										offset = 64; // ('#related', 64)
									}
									break;
							}
						}
						break;
					case 115:
					case 83:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 101:
							case 69:
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'n') {
									if (ofs+8 < src_len && src[ofs+8] == '-') {
										if (ofs+9 < src_len) switch ((src[ofs+9])) {
											case 104:
											case 72:
												found = ofs + 10;
												offset = 52; // ('#section-h', 52)
												break;
											case 120:
											case 88:
												found = ofs + 10;
												offset = 50; // ('#section-x', 50)
												break;
										}
									}
									if (found == -1) {
										found = ofs + 8;
										offset = 48; // ('#section', 48)
									}
								}
								break;
							case 116:
							case 84:
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 's') {
									found = ofs + 11;
									offset = 72; // ('#statements', 72)
								}
								break;
							case 119:
							case 87:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'h') {
									found = ofs + 7;
									offset = 34; // ('#switch', 34)
								}
								break;
						}
						break;
					case 116:
					case 84:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 103:
									case 71:
										found = ofs + 4;
										offset = 22; // ('#tag', 22)
										break;
									case 114:
									case 82:
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 't') {
											found = ofs + 7;
											offset = 66; // ('#target', 66)
										}
										break;
								}
								break;
							case 105:
							case 73:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 109:
									case 77:
										if (ofs+4 < src_len && (src[ofs+4] | 32) == 'e') {
											if (ofs+5 < src_len && (src[ofs+5] | 32) == 'l') {
												found = ofs + 6;
												offset = 36; // ('#timel', 36)
											}
											if (found == -1) {
												found = ofs + 5;
												offset = 35; // ('#time', 35)
											}
										}
										break;
									case 116:
									case 84:
										if (ofs+10 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 's') {
											found = ofs + 11;
											offset = 37; // ('#titleparts', 37)
										}
										break;
								}
								break;
							case 114:
							case 82:
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'i' && (src[ofs+10] | 32) == 'o' && (src[ofs+11] | 32) == 'n') {
									found = ofs + 12;
									offset = 73; // ('#translation', 73)
								}
								break;
						}
						break;
					case 120:
					case 88:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 111:
							case 79:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'a') {
									if (ofs+8 < src_len && src[ofs+5] == '_' && (src[ofs+6] | 32) == 'd' && (src[ofs+7] | 32) == 'b' && (src[ofs+8] | 32) == 'g') {
										found = ofs + 9;
										offset = 43; // ('#xowa_dbg', 43)
									}
									if (found == -1) {
										found = ofs + 5;
										offset = 59; // ('#xowa', 59)
									}
								}
								break;
							case 120:
							case 88:
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'x' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'd') {
									found = ofs + 11;
									offset = 74; // ('#xxxrelated', 74)
								}
								break;
						}
						break;
				}
				break;
			case 95:
				if (ofs+16 < src_len && src[ofs+1] == '_' && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 's' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'c' && (src[ofs+11] | 32) == 't' && (src[ofs+12] | 32) == 'i' && (src[ofs+13] | 32) == 'o' && (src[ofs+14] | 32) == 'n' && src[ofs+15] == '_' && src[ofs+16] == '_') {
					found = ofs + 17;
					offset = 2; // ('__noeditsection__', 2)
				}
				break;
			case 97:
			case 65:
				if (ofs+11 < src_len && (src[ofs+1] | 32) == 'n' && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'c' && (src[ofs+9] | 32) == 'o' && (src[ofs+10] | 32) == 'd' && (src[ofs+11] | 32) == 'e') {
					found = ofs + 12;
					offset = 20; // ('anchorencode', 20)
				}
				break;
			case 99:
			case 67:
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'a') {
					if (ofs+2 < src_len) switch ((src[ofs+2])) {
						case 110:
						case 78:
							if (ofs+11 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'l' && (src[ofs+9] | 32) == 'u' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'l') {
								if (ofs+12 < src_len && (src[ofs+12] | 32) == 'e') {
									found = ofs + 13;
									offset = 46; // ('canonicalurle', 46)
								}
								if (found == -1) {
									found = ofs + 12;
									offset = 45; // ('canonicalurl', 45)
								}
							}
							break;
						case 115:
						case 83:
							if (ofs+15 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'g' && (src[ofs+9] | 32) == 's' && (src[ofs+10] | 32) == 'o' && (src[ofs+11] | 32) == 'u' && (src[ofs+12] | 32) == 'r' && (src[ofs+13] | 32) == 'c' && (src[ofs+14] | 32) == 'e' && (src[ofs+15] | 32) == 's') {
								found = ofs + 16;
								offset = 67; // ('cascadingsources', 67)
							}
							break;
					}
				}
				break;
			case 102:
			case 70:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 105:
					case 73:
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'h') {
							found = ofs + 8;
							offset = 19; // ('filepath', 19)
						}
						break;
					case 111:
					case 79:
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'u' && (src[ofs+8] | 32) == 'm') {
							found = ofs + 9;
							offset = 12; // ('formatnum', 12)
						}
						break;
					case 117:
					case 85:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'l') {
							if (ofs+7 < src_len && (src[ofs+7] | 32) == 'e') {
								found = ofs + 8;
								offset = 18; // ('fullurle', 18)
							}
							if (found == -1) {
								found = ofs + 7;
								offset = 17; // ('fullurl', 17)
							}
						}
						break;
				}
				break;
			case 103:
			case 71:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 101:
					case 69:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
							found = ofs + 6;
							offset = 26; // ('gender', 26)
						}
						break;
					case 114:
					case 82:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'r') {
							found = ofs + 7;
							offset = 25; // ('grammar', 25)
						}
						break;
				}
				break;
			case 105:
			case 73:
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'n' && (src[ofs+2] | 32) == 't') {
					found = ofs + 3;
					offset = 24; // ('int', 24)
				}
				break;
			case 108:
			case 76:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 99:
					case 67:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 't') {
							found = ofs + 7;
							offset = 7; // ('lcfirst', 7)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 6; // ('lc', 6)
						}
						break;
					case 111:
					case 79:
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'l') {
							if (ofs+8 < src_len && (src[ofs+8] | 32) == 'e') {
								found = ofs + 9;
								offset = 16; // ('localurle', 16)
							}
							if (found == -1) {
								found = ofs + 8;
								offset = 15; // ('localurl', 15)
							}
						}
						break;
				}
				break;
			case 109:
			case 77:
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 's' && (src[ofs+2] | 32) == 'g') {
					if (ofs+4 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'w') {
						found = ofs + 5;
						offset = 42; // ('msgnw', 42)
					}
					if (found == -1) {
						found = ofs + 3;
						offset = 41; // ('msg', 41)
					}
				}
				break;
			case 110:
			case 78:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+14 < src_len && (src[ofs+2] | 32) == 'm' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'p' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 'u' && (src[ofs+11] | 32) == 'm' && (src[ofs+12] | 32) == 'b' && (src[ofs+13] | 32) == 'e' && (src[ofs+14] | 32) == 'r') {
							found = ofs + 15;
							offset = 57; // ('namespacenumber', 57)
						}
						break;
					case 111:
					case 79:
						if (ofs+18 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'x' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'a' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'g' && (src[ofs+14] | 32) == 'l' && (src[ofs+15] | 32) == 'i' && (src[ofs+16] | 32) == 'n' && (src[ofs+17] | 32) == 'k' && (src[ofs+18] | 32) == 's') {
							found = ofs + 19;
							offset = 55; // ('noexternallanglinks', 55)
						}
						break;
					case 115:
					case 83:
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'e') {
							found = ofs + 3;
							offset = 4; // ('nse', 4)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 3; // ('ns', 3)
						}
						break;
				}
				break;
			case 112:
			case 80:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 100:
							case 68:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 108:
									case 76:
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'f' && (src[ofs+6] | 32) == 't') {
											found = ofs + 7;
											offset = 10; // ('padleft', 10)
										}
										break;
									case 114:
									case 82:
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 't') {
											found = ofs + 8;
											offset = 11; // ('padright', 11)
										}
										break;
								}
								break;
							case 103:
							case 71:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'e') {
									if (ofs+4 < src_len) switch ((src[ofs+4])) {
										case 105:
										case 73:
											if (ofs+5 < src_len && (src[ofs+5] | 32) == 'd') {
												found = ofs + 6;
												offset = 58; // ('pageid', 58)
											}
											break;
										case 115:
										case 83:
											if (ofs+23 < src_len && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'g' && (src[ofs+10] | 32) == 'p' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'd' && (src[ofs+14] | 32) == 'i' && (src[ofs+15] | 32) == 'n' && (src[ofs+16] | 32) == 'g' && (src[ofs+17] | 32) == 'c' && (src[ofs+18] | 32) == 'h' && (src[ofs+19] | 32) == 'a' && (src[ofs+20] | 32) == 'n' && (src[ofs+21] | 32) == 'g' && (src[ofs+22] | 32) == 'e' && (src[ofs+23] | 32) == 's') {
												found = ofs + 24;
												offset = 69; // ('pagesusingpendingchanges', 69)
											}
											break;
									}
								}
								break;
						}
						break;
					case 101:
					case 69:
						if (ofs+17 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'h' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 'n' && (src[ofs+11] | 32) == 'g' && (src[ofs+12] | 32) == 'e' && (src[ofs+13] | 32) == 'l' && (src[ofs+14] | 32) == 'e' && (src[ofs+15] | 32) == 'v' && (src[ofs+16] | 32) == 'e' && (src[ofs+17] | 32) == 'l') {
							found = ofs + 18;
							offset = 68; // ('pendingchangelevel', 68)
						}
						break;
					case 108:
					case 76:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'u' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'l') {
							found = ofs + 6;
							offset = 21; // ('plural', 21)
						}
						break;
				}
				break;
			case 114:
			case 82:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'w') {
							found = ofs + 3;
							offset = 40; // ('raw', 40)
						}
						break;
					case 111:
					case 79:
						if (ofs+11 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 'm' && (src[ofs+11] | 32) == 'e') {
							if (ofs+12 < src_len && (src[ofs+12] | 32) == 'e') {
								found = ofs + 13;
								offset = 0; // ('rootpagenamee', 0)
							}
							if (found == -1) {
								found = ofs + 12;
								offset = 1; // ('rootpagename', 1)
							}
						}
						break;
				}
				break;
			case 115:
			case 83:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'b' && (src[ofs+7] | 32) == 's' && (src[ofs+8] | 32) == 't' && src[ofs+9] == ':') {
							found = ofs + 10;
							offset = 39; // ('safesubst:', 39)
						}
						break;
					case 117:
					case 85:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'b' && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 't' && src[ofs+5] == ':') {
							found = ofs + 6;
							offset = 38; // ('subst:', 38)
						}
						break;
				}
				break;
			case 117:
			case 85:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 99:
					case 67:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 't') {
							found = ofs + 7;
							offset = 9; // ('ucfirst', 9)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 8; // ('uc', 8)
						}
						break;
					case 114:
					case 82:
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'e') {
							found = ofs + 9;
							offset = 5; // ('urlencode', 5)
						}
						break;
				}
				break;
			case 119:
			case 87:
				if (ofs+9 < src_len && (src[ofs+1] | 32) == 'b' && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'm' && (src[ofs+9] | 32) == 'e') {
					found = ofs + 10;
					offset = 56; // ('wbreponame', 56)
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
