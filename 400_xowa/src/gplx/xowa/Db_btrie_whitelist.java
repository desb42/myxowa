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
public class Db_btrie_whitelist implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_whitelist(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("db0c247e94b503dc396dde69c1d30bba"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;
		int c = b;

		switch (b) {
			case 97:
			case 65:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 98:
					case 66:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 98:
							case 66:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'r') {
									found = ofs + 4;
									offset = 41; // ('abbr', 41)
								}
								break;
							case 111:
							case 79:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'u' && (src[ofs+4] | 32) == 't') {
									found = ofs + 5;
									offset = 14; // ('about', 14)
								}
								break;
						}
						break;
					case 108:
					case 76:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 105:
							case 73:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'n') {
									found = ofs + 5;
									offset = 0; // ('align', 0)
								}
								break;
							case 116:
							case 84:
								found = ofs + 3;
								offset = 52; // ('alt', 52)
								break;
						}
						break;
					case 114:
					case 82:
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'a' && src[ofs+4] == '-') {
							if (ofs+5 < src_len) switch ((src[ofs+5])) {
								case 100:
								case 68:
									if (ofs+15 < src_len && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 's' && (src[ofs+8] | 32) == 'c' && (src[ofs+9] | 32) == 'r' && (src[ofs+10] | 32) == 'i' && (src[ofs+11] | 32) == 'b' && (src[ofs+12] | 32) == 'e' && (src[ofs+13] | 32) == 'd' && (src[ofs+14] | 32) == 'b' && (src[ofs+15] | 32) == 'y') {
										found = ofs + 16;
										offset = 7; // ('aria-describedby', 7)
									}
									break;
								case 102:
								case 70:
									if (ofs+10 < src_len && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'w' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'o') {
										found = ofs + 11;
										offset = 8; // ('aria-flowto', 8)
									}
									break;
								case 104:
								case 72:
									if (ofs+10 < src_len && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'd' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'n') {
										found = ofs + 11;
										offset = 9; // ('aria-hidden', 9)
									}
									break;
								case 108:
								case 76:
									if (ofs+9 < src_len && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'b' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 'l') {
										if (ofs+14 < src_len && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'd' && (src[ofs+13] | 32) == 'b' && (src[ofs+14] | 32) == 'y') {
											found = ofs + 15;
											offset = 11; // ('aria-labelledby', 11)
										}
										if (found == -1) {
											found = ofs + 10;
											offset = 10; // ('aria-label', 10)
										}
									}
									break;
								case 111:
								case 79:
									if (ofs+8 < src_len && (src[ofs+6] | 32) == 'w' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 's') {
										found = ofs + 9;
										offset = 12; // ('aria-owns', 12)
									}
									break;
							}
						}
						break;
					case 120:
					case 88:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 's') {
							found = ofs + 4;
							offset = 42; // ('axis', 42)
						}
						break;
				}
				break;
			case 98:
			case 66:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 103:
					case 71:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'r') {
							found = ofs + 7;
							offset = 38; // ('bgcolor', 38)
						}
						break;
					case 111:
					case 79:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
							found = ofs + 6;
							offset = 33; // ('border', 33)
						}
						break;
				}
				break;
			case 99:
			case 67:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 101:
					case 69:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l') {
							if (ofs+4 < src_len) switch ((src[ofs+4])) {
								case 112:
								case 80:
									if (ofs+10 < src_len && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'd' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'i' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 'g') {
										found = ofs + 11;
										offset = 37; // ('cellpadding', 37)
									}
									break;
								case 115:
								case 83:
									if (ofs+10 < src_len && (src[ofs+5] | 32) == 'p' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'i' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 'g') {
										found = ofs + 11;
										offset = 36; // ('cellspacing', 36)
									}
									break;
							}
						}
						break;
					case 105:
					case 73:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'e') {
							found = ofs + 4;
							offset = 24; // ('cite', 24)
						}
						break;
					case 108:
					case 76:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 's') {
									found = ofs + 5;
									offset = 2; // ('class', 2)
								}
								break;
							case 101:
							case 69:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'r') {
									found = ofs + 5;
									offset = 25; // ('clear', 25)
								}
								break;
						}
						break;
					case 111:
					case 79:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 108:
							case 76:
								if (ofs+3 < src_len) switch ((src[ofs+3])) {
									case 111:
									case 79:
										if (ofs+4 < src_len && (src[ofs+4] | 32) == 'r') {
											found = ofs + 5;
											offset = 62; // ('color', 62)
										}
										break;
									case 115:
									case 83:
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'n') {
											found = ofs + 7;
											offset = 46; // ('colspan', 46)
										}
										break;
								}
								break;
							case 110:
							case 78:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 't') {
									if (ofs+4 < src_len) switch ((src[ofs+4])) {
										case 101:
										case 69:
											if (ofs+6 < src_len && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 't') {
												found = ofs + 7;
												offset = 64; // ('content', 64)
											}
											break;
										case 114:
										case 82:
											if (ofs+7 < src_len && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 's') {
												found = ofs + 8;
												offset = 55; // ('controls', 55)
											}
											break;
									}
								}
								break;
						}
						break;
				}
				break;
			case 100:
			case 68:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 't') {
							if (ofs+3 < src_len) switch ((src[ofs+3])) {
								case 97:
								case 65:
									if (ofs+7 < src_len && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'y' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'e') {
										found = ofs + 8;
										offset = 17; // ('datatype', 17)
									}
									if (found == -1) {
										found = ofs + 4;
										offset = 65; // ('data', 65)
									}
									break;
								case 101:
								case 69:
									if (ofs+7 < src_len && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'e') {
										found = ofs + 8;
										offset = 27; // ('datetime', 27)
									}
									break;
							}
						}
						break;
					case 105:
					case 73:
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'r') {
							found = ofs + 3;
							offset = 5; // ('dir', 5)
						}
						break;
				}
				break;
			case 102:
			case 70:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 97:
					case 65:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'e') {
							found = ofs + 4;
							offset = 63; // ('face', 63)
						}
						break;
					case 114:
					case 82:
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'e') {
							found = ofs + 5;
							offset = 34; // ('frame', 34)
						}
						break;
				}
				break;
			case 104:
			case 72:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 101:
					case 69:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 's') {
									found = ofs + 7;
									offset = 43; // ('headers', 43)
								}
								break;
							case 105:
							case 73:
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 't') {
									found = ofs + 6;
									offset = 48; // ('height', 48)
								}
								break;
						}
						break;
					case 114:
					case 82:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'f') {
							found = ofs + 4;
							offset = 49; // ('href', 49)
						}
						break;
				}
				break;
			case 105:
			case 73:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 100:
					case 68:
						found = ofs + 2;
						offset = 1; // ('id', 1)
						break;
					case 116:
					case 84:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'm') {
							if (ofs+4 < src_len) switch ((src[ofs+4])) {
								case 105:
								case 73:
									if (ofs+5 < src_len && (src[ofs+5] | 32) == 'd') {
										found = ofs + 6;
										offset = 19; // ('itemid', 19)
									}
									break;
								case 112:
								case 80:
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'p') {
										found = ofs + 8;
										offset = 20; // ('itemprop', 20)
									}
									break;
								case 114:
								case 82:
									if (ofs+6 < src_len && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'f') {
										found = ofs + 7;
										offset = 21; // ('itemref', 21)
									}
									break;
								case 115:
								case 83:
									if (ofs+8 < src_len && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'p' && (src[ofs+8] | 32) == 'e') {
										found = ofs + 9;
										offset = 22; // ('itemscope', 22)
									}
									break;
								case 116:
								case 84:
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'y' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'e') {
										found = ofs + 8;
										offset = 23; // ('itemtype', 23)
									}
									break;
							}
						}
						break;
				}
				break;
			case 107:
			case 75:
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 'i' && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'd') {
					found = ofs + 4;
					offset = 59; // ('kind', 59)
				}
				break;
			case 108:
			case 76:
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'a') {
					if (ofs+2 < src_len) switch ((src[ofs+2])) {
						case 98:
						case 66:
							if (ofs+4 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'l') {
								found = ofs + 5;
								offset = 60; // ('label', 60)
							}
							break;
						case 110:
						case 78:
							if (ofs+3 < src_len && (src[ofs+3] | 32) == 'g') {
								found = ofs + 4;
								offset = 4; // ('lang', 4)
							}
							break;
					}
				}
				break;
			case 110:
			case 78:
				if (ofs+5 < src_len && (src[ofs+1] | 32) == 'o' && (src[ofs+2] | 32) == 'w' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'p') {
					found = ofs + 6;
					offset = 47; // ('nowrap', 47)
				}
				break;
			case 112:
			case 80:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 111:
					case 79:
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 's' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
							found = ofs + 6;
							offset = 57; // ('poster', 57)
						}
						break;
					case 114:
					case 82:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 101:
							case 69:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'd') {
									found = ofs + 7;
									offset = 56; // ('preload', 56)
								}
								break;
							case 111:
							case 79:
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'y') {
									found = ofs + 8;
									offset = 15; // ('property', 15)
								}
								break;
						}
						break;
				}
				break;
			case 114:
			case 82:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 101:
					case 69:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 108:
							case 76:
								found = ofs + 3;
								offset = 50; // ('rel', 50)
								break;
							case 115:
							case 83:
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'e') {
									found = ofs + 8;
									offset = 16; // ('resource', 16)
								}
								break;
							case 118:
							case 86:
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'd') {
									found = ofs + 8;
									offset = 30; // ('reversed', 30)
								}
								if (found == -1) {
									found = ofs + 3;
									offset = 51; // ('rev', 51)
								}
								break;
						}
						break;
					case 111:
					case 79:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 108:
							case 76:
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'e') {
									found = ofs + 4;
									offset = 13; // ('role', 13)
								}
								break;
							case 119:
							case 87:
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'n') {
									found = ofs + 7;
									offset = 45; // ('rowspan', 45)
								}
								break;
						}
						break;
					case 117:
					case 85:
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 's') {
							found = ofs + 5;
							offset = 35; // ('rules', 35)
						}
						break;
				}
				break;
			case 115:
			case 83:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 99:
					case 67:
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'e') {
							found = ofs + 5;
							offset = 44; // ('scope', 44)
						}
						break;
					case 105:
					case 73:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'z' && (src[ofs+3] | 32) == 'e') {
							found = ofs + 4;
							offset = 61; // ('size', 61)
						}
						break;
					case 112:
					case 80:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'n') {
							found = ofs + 4;
							offset = 39; // ('span', 39)
						}
						break;
					case 114:
					case 82:
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'c') {
							if (ofs+3 < src_len) switch ((src[ofs+3])) {
								case 108:
								case 76:
									if (ofs+6 < src_len && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'g') {
										found = ofs + 7;
										offset = 58; // ('srclang', 58)
									}
									break;
								case 115:
								case 83:
									if (ofs+5 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 't') {
										found = ofs + 6;
										offset = 54; // ('srcset', 54)
									}
									break;
							}
							if (found == -1) {
								found = ofs + 3;
								offset = 53; // ('src', 53)
							}
						}
						break;
					case 116:
					case 84:
						if (ofs+2 < src_len) switch ((src[ofs+2])) {
							case 97:
							case 65:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 't') {
									found = ofs + 5;
									offset = 29; // ('start', 29)
								}
								break;
							case 121:
							case 89:
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e') {
									found = ofs + 5;
									offset = 3; // ('style', 3)
								}
								break;
						}
						break;
					case 117:
					case 85:
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'm' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'y') {
							found = ofs + 7;
							offset = 32; // ('summary', 32)
						}
						break;
				}
				break;
			case 116:
			case 84:
				if (ofs+1 < src_len) switch ((src[ofs+1])) {
					case 105:
					case 73:
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e') {
							found = ofs + 5;
							offset = 6; // ('title', 6)
						}
						break;
					case 121:
					case 89:
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'p' && (src[ofs+3] | 32) == 'e') {
							if (ofs+5 < src_len && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'f') {
								found = ofs + 6;
								offset = 18; // ('typeof', 18)
							}
							if (found == -1) {
								found = ofs + 4;
								offset = 28; // ('type', 28)
							}
						}
						break;
				}
				break;
			case 118:
			case 86:
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'a' && (src[ofs+2] | 32) == 'l') {
					if (ofs+3 < src_len) switch ((src[ofs+3])) {
						case 105:
						case 73:
							if (ofs+5 < src_len && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'n') {
								found = ofs + 6;
								offset = 40; // ('valign', 40)
							}
							break;
						case 117:
						case 85:
							if (ofs+4 < src_len && (src[ofs+4] | 32) == 'e') {
								found = ofs + 5;
								offset = 31; // ('value', 31)
							}
							break;
					}
				}
				break;
			case 119:
			case 87:
				if (ofs+4 < src_len && (src[ofs+1] | 32) == 'i' && (src[ofs+2] | 32) == 'd' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'h') {
					found = ofs + 5;
					offset = 26; // ('width', 26)
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
