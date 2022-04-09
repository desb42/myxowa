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
public class Db_btrie_ci_trie_en2 implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_ci_trie_en2(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("75abde8d3a1be28afeebcab69173067e"); }
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
							offset = 74; // ('#assessment', 74)
						}
						break;
					case 99:
					case 67:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+12 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r' && (src[ofs+8] | 32) == 'y' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'e') {
									found = ofs + 13;
									offset = 73; // ('#categorytree', 73)
								}
								break;
							case 111:
							case 79:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 111:
									case 79:
										if (ofs+11 < src_len && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 's') {
											found = ofs + 12;
											offset = 49; // ('#coordinates', 49)
										}
										break;
									case 117:
									case 85:
										if (ofs+5 < src_len && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 't') {
											found = ofs + 6;
											offset = 82; // ('#count', 82)
										}
										break;
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
									offset = 19; // ('#dateformat', 19)
								}
								break;
							case 100:
							case 68:
								if (ofs+6 < src_len && src[ofs+3] == '2' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'm' && (src[ofs+6] | 32) == 's') {
									found = ofs + 7;
									offset = 65; // ('#dd2dms', 65)
								}
								break;
							case 101:
							case 69:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'g' && src[ofs+4] == '2' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'd') {
									found = ofs + 7;
									offset = 64; // ('#deg2dd', 64)
								}
								break;
						}
						break;
					case 101:
					case 69:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'x' && (src[ofs+3] | 32) == 'p') {
							if (ofs+4 < src_len) switch ((src[ofs+4])) {
								case 108:
								case 76:
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'd' && (src[ofs+7] | 32) == 'e') {
										found = ofs + 8;
										offset = 84; // ('#explode', 84)
									}
									break;
								case 114:
								case 82:
									found = ofs + 5;
									offset = 32; // ('#expr', 32)
									break;
							}
						}
						break;
					case 102:
					case 70:
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'e') {
							found = ofs + 11;
							offset = 18; // ('#formatdate', 18)
						}
						break;
					case 103:
					case 71:
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'k') {
							found = ofs + 8;
							offset = 66; // ('#geolink', 66)
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
											offset = 34; // ('#ifeq', 34)
											break;
										case 114:
										case 82:
											if (ofs+7 < src_len && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r') {
												found = ofs + 8;
												offset = 35; // ('#iferror', 35)
											}
											break;
										case 120:
										case 88:
											if (ofs+5 < src_len) switch ((src[ofs+5])) {
												case 105:
												case 73:
													if (ofs+7 < src_len && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 't') {
														found = ofs + 8;
														offset = 37; // ('#ifexist', 37)
													}
													break;
												case 112:
												case 80:
													if (ofs+6 < src_len && (src[ofs+6] | 32) == 'r') {
														found = ofs + 7;
														offset = 36; // ('#ifexpr', 36)
													}
													break;
											}
											break;
									}
								}
								if (found == -1) {
									found = ofs + 3;
									offset = 33; // ('#if', 33)
								}
								break;
							case 110:
							case 78:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 115:
									case 83:
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'r') {
											found = ofs + 8;
											offset = 69; // ('#insider', 69)
										}
										break;
									case 118:
									case 86:
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'k' && (src[ofs+6] | 32) == 'e') {
											found = ofs + 7;
											offset = 58; // ('#invoke', 58)
										}
										break;
								}
								break;
							case 115:
							case 83:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'n') {
									found = ofs + 5;
									offset = 67; // ('#isin', 67)
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
									offset = 28; // ('#language', 28)
								}
								break;
							case 101:
							case 69:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'n') {
									found = ofs + 4;
									offset = 78; // ('#len', 78)
								}
								break;
							case 115:
							case 83:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 't') {
									if (ofs+4 < src_len) switch ((src[ofs+4])) {
										case 104:
										case 72:
											found = ofs + 5;
											offset = 56; // ('#lsth', 56)
											break;
										case 120:
										case 88:
											found = ofs + 5;
											offset = 54; // ('#lstx', 54)
											break;
									}
									if (found == -1) {
										found = ofs + 4;
										offset = 52; // ('#lst', 52)
									}
								}
								break;
						}
						break;
					case 110:
					case 78:
						if (ofs+13 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'w' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'w' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'k') {
							found = ofs + 14;
							offset = 86; // ('#newwindowlink', 86)
						}
						break;
					case 112:
					case 80:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 111:
							case 79:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 's') {
									found = ofs + 4;
									offset = 79; // ('#pos', 79)
								}
								break;
							case 114:
							case 82:
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'y') {
									found = ofs + 9;
									offset = 59; // ('#property', 59)
								}
								break;
						}
						break;
					case 114:
					case 82:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 101:
							case 69:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 108:
									case 76:
										if (ofs+4 < src_len) switch (src[ofs+4]) {
											case 50:
												if (ofs+7 < src_len && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'b' && (src[ofs+7] | 32) == 's') {
													found = ofs + 8;
													offset = 38; // ('#rel2abs', 38)
												}
												break;
											case 97:
											case 65:
												if (ofs+7 < src_len && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'd') {
													found = ofs + 8;
													offset = 68; // ('#related', 68)
												}
												break;
										}
										break;
									case 112:
									case 80:
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'e') {
											found = ofs + 8;
											offset = 83; // ('#replace', 83)
										}
										break;
								}
								break;
							case 112:
							case 80:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 's') {
									found = ofs + 5;
									offset = 80; // ('#rpos', 80)
								}
								break;
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
												offset = 57; // ('#section-h', 57)
												break;
											case 120:
											case 88:
												found = ofs + 10;
												offset = 55; // ('#section-x', 55)
												break;
										}
									}
									if (found == -1) {
										found = ofs + 8;
										offset = 53; // ('#section', 53)
									}
								}
								break;
							case 116:
							case 84:
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 's') {
									found = ofs + 11;
									offset = 75; // ('#statements', 75)
								}
								break;
							case 117:
							case 85:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'b') {
									found = ofs + 4;
									offset = 81; // ('#sub', 81)
								}
								break;
							case 119:
							case 87:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'h') {
									found = ofs + 7;
									offset = 39; // ('#switch', 39)
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
										offset = 27; // ('#tag', 27)
										break;
									case 114:
									case 82:
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 't') {
											found = ofs + 7;
											offset = 70; // ('#target', 70)
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
												offset = 41; // ('#timel', 41)
											}
											if (found == -1) {
												found = ofs + 5;
												offset = 40; // ('#time', 40)
											}
										}
										break;
									case 116:
									case 84:
										if (ofs+10 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 's') {
											found = ofs + 11;
											offset = 42; // ('#titleparts', 42)
										}
										break;
								}
								break;
							case 114:
							case 82:
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'i' && (src[ofs+10] | 32) == 'o' && (src[ofs+11] | 32) == 'n') {
									found = ofs + 12;
									offset = 76; // ('#translation', 76)
								}
								break;
						}
						break;
					case 117:
					case 85:
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'd' && (src[ofs+9] | 32) == 'e') {
							found = ofs + 10;
							offset = 85; // ('#urldecode', 85)
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
										offset = 48; // ('#xowa_dbg', 48)
									}
									if (found == -1) {
										found = ofs + 5;
										offset = 63; // ('#xowa', 63)
									}
								}
								break;
							case 120:
							case 88:
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'x' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'd') {
									found = ofs + 11;
									offset = 77; // ('#xxxrelated', 77)
								}
								break;
						}
						break;
				}
				break;
			case 95:
				if (ofs+16 < src_len && src[ofs+1] == '_' && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 's' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'c' && (src[ofs+11] | 32) == 't' && (src[ofs+12] | 32) == 'i' && (src[ofs+13] | 32) == 'o' && (src[ofs+14] | 32) == 'n' && src[ofs+15] == '_' && src[ofs+16] == '_') {
					found = ofs + 17;
					offset = 7; // ('__noeditsection__', 7)
				}
				break;
			case 97:
			case 65:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 110:
					case 78:
						if (ofs+11 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'c' && (src[ofs+9] | 32) == 'o' && (src[ofs+10] | 32) == 'd' && (src[ofs+11] | 32) == 'e') {
							found = ofs + 12;
							offset = 25; // ('anchorencode', 25)
						}
						break;
					case 114:
					case 82:
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'p' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'h') {
							found = ofs + 11;
							offset = 4; // ('articlepath', 4)
						}
						break;
				}
				break;
			case 99:
			case 67:
				if (ofs+11 < src_len && (src[ofs+1] | 32) == 'a' && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'l' && (src[ofs+9] | 32) == 'u' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'l') {
					if (ofs+12 < src_len && (src[ofs+12] | 32) == 'e') {
						found = ofs + 13;
						offset = 51; // ('canonicalurle', 51)
					}
					if (found == -1) {
						found = ofs + 12;
						offset = 50; // ('canonicalurl', 50)
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
							offset = 24; // ('filepath', 24)
						}
						break;
					case 111:
					case 79:
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'u' && (src[ofs+8] | 32) == 'm') {
							found = ofs + 9;
							offset = 17; // ('formatnum', 17)
						}
						break;
					case 117:
					case 85:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'l') {
							if (ofs+7 < src_len && (src[ofs+7] | 32) == 'e') {
								found = ofs + 8;
								offset = 23; // ('fullurle', 23)
							}
							if (found == -1) {
								found = ofs + 7;
								offset = 22; // ('fullurl', 22)
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
							offset = 31; // ('gender', 31)
						}
						break;
					case 114:
					case 82:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'r') {
							found = ofs + 7;
							offset = 30; // ('grammar', 30)
						}
						break;
				}
				break;
			case 105:
			case 73:
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'n' && (src[ofs+2] | 32) == 't') {
					found = ofs + 3;
					offset = 29; // ('int', 29)
				}
				break;
			case 108:
			case 76:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 99:
					case 67:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 't') {
							found = ofs + 7;
							offset = 12; // ('lcfirst', 12)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 11; // ('lc', 11)
						}
						break;
					case 111:
					case 79:
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'l') {
							if (ofs+8 < src_len && (src[ofs+8] | 32) == 'e') {
								found = ofs + 9;
								offset = 21; // ('localurle', 21)
							}
							if (found == -1) {
								found = ofs + 8;
								offset = 20; // ('localurl', 20)
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
						offset = 47; // ('msgnw', 47)
					}
					if (found == -1) {
						found = ofs + 3;
						offset = 46; // ('msg', 46)
					}
				}
				break;
			case 110:
			case 78:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 111:
					case 79:
						if (ofs+18 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'x' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'a' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'g' && (src[ofs+14] | 32) == 'l' && (src[ofs+15] | 32) == 'i' && (src[ofs+16] | 32) == 'n' && (src[ofs+17] | 32) == 'k' && (src[ofs+18] | 32) == 's') {
							found = ofs + 19;
							offset = 60; // ('noexternallanglinks', 60)
						}
						break;
					case 115:
					case 83:
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'e') {
							found = ofs + 3;
							offset = 9; // ('nse', 9)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 8; // ('ns', 8)
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
											offset = 15; // ('padleft', 15)
										}
										break;
									case 114:
									case 82:
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 't') {
											found = ofs + 8;
											offset = 16; // ('padright', 16)
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
												offset = 62; // ('pageid', 62)
											}
											break;
										case 115:
										case 83:
											if (ofs+23 < src_len && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'g' && (src[ofs+10] | 32) == 'p' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'd' && (src[ofs+14] | 32) == 'i' && (src[ofs+15] | 32) == 'n' && (src[ofs+16] | 32) == 'g' && (src[ofs+17] | 32) == 'c' && (src[ofs+18] | 32) == 'h' && (src[ofs+19] | 32) == 'a' && (src[ofs+20] | 32) == 'n' && (src[ofs+21] | 32) == 'g' && (src[ofs+22] | 32) == 'e' && (src[ofs+23] | 32) == 's') {
												found = ofs + 24;
												offset = 72; // ('pagesusingpendingchanges', 72)
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
							offset = 71; // ('pendingchangelevel', 71)
						}
						break;
					case 108:
					case 76:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'u' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'l') {
							found = ofs + 6;
							offset = 26; // ('plural', 26)
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
							offset = 45; // ('raw', 45)
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
							offset = 44; // ('safesubst:', 44)
						}
						break;
					case 99:
					case 67:
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'h') {
							found = ofs + 10;
							offset = 5; // ('scriptpath', 5)
						}
						break;
					case 101:
					case 69:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'v' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
							if (ofs+9 < src_len && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'm' && (src[ofs+9] | 32) == 'e') {
								found = ofs + 10;
								offset = 2; // ('servername', 2)
							}
							if (found == -1) {
								found = ofs + 6;
								offset = 3; // ('server', 3)
							}
						}
						break;
					case 116:
					case 84:
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'y' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'p' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'h') {
							found = ofs + 9;
							offset = 6; // ('stylepath', 6)
						}
						break;
					case 117:
					case 85:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'b' && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 't' && src[ofs+5] == ':') {
							found = ofs + 6;
							offset = 43; // ('subst:', 43)
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
							offset = 14; // ('ucfirst', 14)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 13; // ('uc', 13)
						}
						break;
					case 114:
					case 82:
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'e') {
							found = ofs + 9;
							offset = 10; // ('urlencode', 10)
						}
						break;
				}
				break;
			case 119:
			case 87:
				if (ofs+9 < src_len && (src[ofs+1] | 32) == 'b' && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'm' && (src[ofs+9] | 32) == 'e') {
					found = ofs + 10;
					offset = 61; // ('wbreponame', 61)
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
