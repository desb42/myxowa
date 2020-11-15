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
public class Db_btrie_ci_trie_de implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_ci_trie_de(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("567c1a170f4c552d47b40345c0c64885"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;

		switch (b) {
			case '#':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'b':
								if (ofs+9 < src_len && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'h' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 't') {
									if (ofs+11 < src_len && src[ofs+10] == '-' && (src[ofs+11] | 32) == 'x') {
										found = ofs + 12;
										offset = 90; // ('#abschnitt-x', 90)
									}
									if (found == -1) {
										found = ofs + 10;
										offset = 87; // ('#abschnitt', 87)
									}
								}
								break;
							case 's':
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'm' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 't') {
									found = ofs + 11;
									offset = 115; // ('#assessment', 115)
								}
								break;
							case 'u':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'f' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'f' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n') {
									found = ofs + 9;
									offset = 93; // ('#aufrufen', 93)
								}
								break;
						}
						break;
					case 'c':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+12 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r' && (src[ofs+8] | 32) == 'y' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'e') {
									found = ofs + 13;
									offset = 114; // ('#categorytree', 114)
								}
								break;
							case 'o':
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 's') {
									found = ofs + 12;
									offset = 80; // ('#coordinates', 80)
								}
								break;
						}
						break;
					case 'd':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 't') {
									if (ofs+4 < src_len) switch ((src[ofs+4] | 32)) {
										case 'e':
											if (ofs+10 < src_len && (src[ofs+5] | 32) == 'f' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r' && (src[ofs+8] | 32) == 'm' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 't') {
												found = ofs + 11;
												offset = 33; // ('#dateformat', 33)
											}
											break;
										case 'u':
											if (ofs+12 < src_len && (src[ofs+5] | 32) == 'm' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'f' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'r' && (src[ofs+10] | 32) == 'm' && (src[ofs+11] | 32) == 'a' && (src[ofs+12] | 32) == 't') {
												found = ofs + 13;
												offset = 31; // ('#datumsformat', 31)
											}
											break;
									}
								}
								break;
							case 'd':
								if (ofs+6 < src_len && src[ofs+3] == '2' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'm' && (src[ofs+6] | 32) == 's') {
									found = ofs + 7;
									offset = 106; // ('#dd2dms', 106)
								}
								break;
							case 'e':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'g' && src[ofs+4] == '2' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'd') {
									found = ofs + 7;
									offset = 105; // ('#deg2dd', 105)
								}
								break;
						}
						break;
					case 'e':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'i':
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'h' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 'f' && (src[ofs+11] | 32) == 't') {
									found = ofs + 12;
									offset = 95; // ('#eigenschaft', 95)
								}
								break;
							case 'r':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 's':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'z' && (src[ofs+7] | 32) == 'e') {
											found = ofs + 8;
											offset = 120; // ('#ersetze', 120)
										}
										break;
									case 'w':
										if (ofs+11 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 'u' && (src[ofs+10] | 32) == 'n' && (src[ofs+11] | 32) == 'g') {
											found = ofs + 12;
											offset = 48; // ('#erweiterung', 48)
										}
										break;
								}
								break;
							case 'x':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'r') {
									found = ofs + 5;
									offset = 58; // ('#expr', 58)
								}
								break;
						}
						break;
					case 'f':
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'e') {
							found = ofs + 11;
							offset = 32; // ('#formatdate', 32)
						}
						break;
					case 'g':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'k') {
							found = ofs + 8;
							offset = 107; // ('#geolink', 107)
						}
						break;
					case 'i':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'f':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'e') {
									if (ofs+4 < src_len) switch ((src[ofs+4] | 32)) {
										case 'q':
											found = ofs + 5;
											offset = 60; // ('#ifeq', 60)
											break;
										case 'r':
											if (ofs+7 < src_len && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'r') {
												found = ofs + 8;
												offset = 61; // ('#iferror', 61)
											}
											break;
										case 'x':
											if (ofs+5 < src_len) switch ((src[ofs+5] | 32)) {
												case 'i':
													if (ofs+7 < src_len && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 't') {
														found = ofs + 8;
														offset = 63; // ('#ifexist', 63)
													}
													break;
												case 'p':
													if (ofs+6 < src_len && (src[ofs+6] | 32) == 'r') {
														found = ofs + 7;
														offset = 62; // ('#ifexpr', 62)
													}
													break;
											}
											break;
									}
								}
								if (found == -1) {
									found = ofs + 3;
									offset = 59; // ('#if', 59)
								}
								break;
							case 'n':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 's':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'r') {
											found = ofs + 8;
											offset = 110; // ('#insider', 110)
										}
										break;
									case 'v':
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'k' && (src[ofs+6] | 32) == 'e') {
											found = ofs + 7;
											offset = 94; // ('#invoke', 94)
										}
										break;
								}
								break;
							case 's':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'n') {
									found = ofs + 5;
									offset = 108; // ('#isin', 108)
								}
								break;
						}
						break;
					case 'l':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'g' && (src[ofs+8] | 32) == 'e') {
									found = ofs + 9;
									offset = 51; // ('#language', 51)
								}
								break;
							case 's':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 't') {
									if (ofs+4 < src_len) switch ((src[ofs+4] | 32)) {
										case 'h':
											found = ofs + 5;
											offset = 91; // ('#lsth', 91)
											break;
										case 'x':
											found = ofs + 5;
											offset = 89; // ('#lstx', 89)
											break;
									}
									if (found == -1) {
										found = ofs + 4;
										offset = 86; // ('#lst', 86)
									}
								}
								break;
						}
						break;
					case 'n':
						if (ofs+13 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'w' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'w' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'k') {
							found = ofs + 14;
							offset = 122; // ('#newwindowlink', 122)
						}
						break;
					case 'p':
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'y') {
							found = ofs + 9;
							offset = 96; // ('#property', 96)
						}
						break;
					case 'r':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'l') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case '2':
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'b' && (src[ofs+7] | 32) == 's') {
										found = ofs + 8;
										offset = 64; // ('#rel2abs', 64)
									}
									break;
								case 'a':
								case 'A':
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'd') {
										found = ofs + 8;
										offset = 109; // ('#related', 109)
									}
									break;
							}
						}
						break;
					case 's':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'e':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'n') {
									if (ofs+8 < src_len && src[ofs+8] == '-') {
										if (ofs+9 < src_len) switch ((src[ofs+9] | 32)) {
											case 'h':
												found = ofs + 10;
												offset = 92; // ('#section-h', 92)
												break;
											case 'x':
												found = ofs + 10;
												offset = 88; // ('#section-x', 88)
												break;
										}
									}
									if (found == -1) {
										found = ofs + 8;
										offset = 85; // ('#section', 85)
									}
								}
								break;
							case 'p':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 'e') {
									found = ofs + 8;
									offset = 50; // ('#sprache', 50)
								}
								break;
							case 't':
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 's') {
									found = ofs + 11;
									offset = 116; // ('#statements', 116)
								}
								break;
							case 'w':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'h') {
									found = ofs + 7;
									offset = 66; // ('#switch', 66)
								}
								break;
						}
						break;
					case 't':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'g':
										found = ofs + 4;
										offset = 49; // ('#tag', 49)
										break;
									case 'r':
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 't') {
											found = ofs + 7;
											offset = 111; // ('#target', 111)
										}
										break;
								}
								break;
							case 'i':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'm':
										if (ofs+4 < src_len && (src[ofs+4] | 32) == 'e') {
											if (ofs+5 < src_len && (src[ofs+5] | 32) == 'l') {
												found = ofs + 6;
												offset = 68; // ('#timel', 68)
											}
											if (found == -1) {
												found = ofs + 5;
												offset = 67; // ('#time', 67)
											}
										}
										break;
									case 't':
										if (ofs+10 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 's') {
											found = ofs + 11;
											offset = 69; // ('#titleparts', 69)
										}
										break;
								}
								break;
							case 'r':
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'i' && (src[ofs+10] | 32) == 'o' && (src[ofs+11] | 32) == 'n') {
									found = ofs + 12;
									offset = 117; // ('#translation', 117)
								}
								break;
						}
						break;
					case 'u':
						if (ofs+12 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'k' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'd' && (src[ofs+9] | 32) == 'i' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'r' && (src[ofs+12] | 32) == 't') {
							found = ofs + 13;
							offset = 121; // ('#urldekodiert', 121)
						}
						break;
					case 'w':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'e') {
							found = ofs + 8;
							offset = 65; // ('#wechsle', 65)
						}
						break;
					case 'x':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'o':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'a') {
									if (ofs+8 < src_len && src[ofs+5] == '_' && (src[ofs+6] | 32) == 'd' && (src[ofs+7] | 32) == 'b' && (src[ofs+8] | 32) == 'g') {
										found = ofs + 9;
										offset = 79; // ('#xowa_dbg', 79)
									}
									if (found == -1) {
										found = ofs + 5;
										offset = 104; // ('#xowa', 104)
									}
								}
								break;
							case 'x':
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'x' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'd') {
									found = ofs + 11;
									offset = 118; // ('#xxxrelated', 118)
								}
								break;
						}
						break;
					case 'z':
						if (ofs+6 < src_len && src[ofs+2] == -61 && src[ofs+3] == -92 && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'e') {
							found = ofs + 7;
							offset = 119; // ('#zÃ¤hle', 119)
						}
						break;
				}
				break;
			case '_':
				if (ofs+1 < src_len && src[ofs+1] == '_') {
					if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
						case 'a':
							if (ofs+30 < src_len && (src[ofs+3] | 32) == 'b' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'i' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 't' && (src[ofs+11] | 32) == 'e' && src[ofs+12] == '_' && (src[ofs+13] | 32) == 'n' && (src[ofs+14] | 32) == 'i' && (src[ofs+15] | 32) == 'c' && (src[ofs+16] | 32) == 'h' && (src[ofs+17] | 32) == 't' && src[ofs+18] == '_' && (src[ofs+19] | 32) == 'b' && (src[ofs+20] | 32) == 'e' && (src[ofs+21] | 32) == 'a' && (src[ofs+22] | 32) == 'r' && (src[ofs+23] | 32) == 'b' && (src[ofs+24] | 32) == 'e' && (src[ofs+25] | 32) == 'i' && (src[ofs+26] | 32) == 't' && (src[ofs+27] | 32) == 'e' && (src[ofs+28] | 32) == 'n' && src[ofs+29] == '_' && src[ofs+30] == '_') {
								found = ofs + 31;
								offset = 9; // ('__abschnitte_nicht_bearbeiten__', 9)
							}
							break;
						case 'n':
							if (ofs+16 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 's' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'c' && (src[ofs+11] | 32) == 't' && (src[ofs+12] | 32) == 'i' && (src[ofs+13] | 32) == 'o' && (src[ofs+14] | 32) == 'n' && src[ofs+15] == '_' && src[ofs+16] == '_') {
								found = ofs + 17;
								offset = 10; // ('__noeditsection__', 10)
							}
							break;
					}
				}
				break;
			case 'a':
			case 'A':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'n':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'c':
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'c' && (src[ofs+9] | 32) == 'o' && (src[ofs+10] | 32) == 'd' && (src[ofs+11] | 32) == 'e') {
									found = ofs + 12;
									offset = 46; // ('anchorencode', 46)
								}
								break;
							case 'k':
								if (ofs+13 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'k' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'd' && (src[ofs+10] | 32) == 'i' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'r' && (src[ofs+13] | 32) == 't') {
									found = ofs + 14;
									offset = 44; // ('ankerenkodiert', 44)
								}
								break;
						}
						break;
					case 'r':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'i') {
							if (ofs+4 < src_len) switch ((src[ofs+4] | 32)) {
								case 'c':
									if (ofs+10 < src_len && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'p' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'h') {
										found = ofs + 11;
										offset = 3; // ('articlepath', 3)
									}
									break;
								case 'k':
									if (ofs+10 < src_len && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'l' && (src[ofs+7] | 32) == 'p' && (src[ofs+8] | 32) == 'f' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 'd') {
										found = ofs + 11;
										offset = 2; // ('artikelpfad', 2)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'c':
			case 'C':
				if (ofs+11 < src_len && (src[ofs+1] | 32) == 'a' && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'l' && (src[ofs+9] | 32) == 'u' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'l') {
					if (ofs+12 < src_len && (src[ofs+12] | 32) == 'e') {
						found = ofs + 13;
						offset = 84; // ('canonicalurle', 84)
					}
					if (found == -1) {
						found = ofs + 12;
						offset = 82; // ('canonicalurl', 82)
					}
				}
				break;
			case 'd':
			case 'D':
				if (ofs+8 < src_len && (src[ofs+1] | 32) == 'a' && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'p' && (src[ofs+6] | 32) == 'f' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'd') {
					found = ofs + 9;
					offset = 42; // ('dateipfad', 42)
				}
				break;
			case 'e':
			case 'E':
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 'r' && (src[ofs+2] | 32) == 's' && src[ofs+3] == ':') {
					found = ofs + 4;
					offset = 70; // ('ers:', 70)
				}
				break;
			case 'f':
			case 'F':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'i':
					case 'I':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'h') {
							found = ofs + 8;
							offset = 43; // ('filepath', 43)
						}
						break;
					case 'o':
					case 'O':
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'u' && (src[ofs+8] | 32) == 'm') {
							found = ofs + 9;
							offset = 30; // ('formatnum', 30)
						}
						break;
					case 'u':
					case 'U':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'l') {
							if (ofs+7 < src_len && (src[ofs+7] | 32) == 'e') {
								found = ofs + 8;
								offset = 41; // ('fullurle', 41)
							}
							if (found == -1) {
								found = ofs + 7;
								offset = 39; // ('fullurl', 39)
							}
						}
						break;
					case -61:
						if (ofs+6 < src_len && src[ofs+2] == -68 && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'n') {
							if (ofs+7 < src_len) switch ((src[ofs+7] | 32)) {
								case 'l':
									if (ofs+11 < src_len && (src[ofs+8] | 32) == 'i' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 'k' && (src[ofs+11] | 32) == 's') {
										found = ofs + 12;
										offset = 25; // ('fÃ¼llenlinks', 25)
									}
									break;
								case 'r':
									if (ofs+12 < src_len && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 'c' && (src[ofs+10] | 32) == 'h' && (src[ofs+11] | 32) == 't' && (src[ofs+12] | 32) == 's') {
										found = ofs + 13;
										offset = 27; // ('fÃ¼llenrechts', 27)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'g':
			case 'G':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'e':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'n':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
									found = ofs + 6;
									offset = 57; // ('gender', 57)
								}
								break;
							case 's':
								if (ofs+9 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'h' && (src[ofs+9] | 32) == 't') {
									found = ofs + 10;
									offset = 56; // ('geschlecht', 56)
								}
								break;
						}
						break;
					case 'r':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'm' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'a') {
									if (ofs+6 < src_len) switch ((src[ofs+6] | 32)) {
										case 'r':
											found = ofs + 7;
											offset = 55; // ('grammar', 55)
											break;
										case 't':
											if (ofs+8 < src_len && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'k') {
												found = ofs + 9;
												offset = 54; // ('grammatik', 54)
											}
											break;
									}
								}
								break;
							case 'o':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 's') {
									found = ofs + 5;
									offset = 21; // ('gross', 21)
								}
								break;
						}
						break;
				}
				break;
			case 'i':
			case 'I':
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'n') {
					if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
						case 'i':
							if (ofs+7 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'l' && src[ofs+7] == '_') {
								if (ofs+8 < src_len) switch ((src[ofs+8] | 32)) {
									case 'g':
										if (ofs+12 < src_len && (src[ofs+9] | 32) == 'r' && (src[ofs+10] | 32) == 'o' && (src[ofs+11] | 32) == 's' && (src[ofs+12] | 32) == 's') {
											found = ofs + 13;
											offset = 23; // ('initial_gross', 23)
										}
										break;
									case 'k':
										if (ofs+12 < src_len && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'n') {
											found = ofs + 13;
											offset = 19; // ('initial_klein', 19)
										}
										break;
								}
							}
							break;
						case 't':
							found = ofs + 3;
							offset = 53; // ('int', 53)
							break;
					}
				}
				break;
			case 'k':
			case 'K':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+13 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'h' && (src[ofs+9] | 32) == 'e' && src[ofs+10] == '_' && (src[ofs+11] | 32) == 'u' && (src[ofs+12] | 32) == 'r' && (src[ofs+13] | 32) == 'l') {
							if (ofs+15 < src_len && src[ofs+14] == '_' && (src[ofs+15] | 32) == 'c') {
								found = ofs + 16;
								offset = 83; // ('kanonische_url_c', 83)
							}
							if (found == -1) {
								found = ofs + 14;
								offset = 81; // ('kanonische_url', 81)
							}
						}
						break;
					case 'e':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'e') {
							if (ofs+5 < src_len) switch (src[ofs+5]) {
								case '_':
									if (ofs+25 < src_len && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'x' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'n' && (src[ofs+12] | 32) == 'e' && (src[ofs+13] | 32) == 'n' && src[ofs+14] == '_' && (src[ofs+15] | 32) == 's' && (src[ofs+16] | 32) == 'p' && (src[ofs+17] | 32) == 'r' && (src[ofs+18] | 32) == 'a' && (src[ofs+19] | 32) == 'c' && (src[ofs+20] | 32) == 'h' && (src[ofs+21] | 32) == 'l' && (src[ofs+22] | 32) == 'i' && (src[ofs+23] | 32) == 'n' && (src[ofs+24] | 32) == 'k' && (src[ofs+25] | 32) == 's') {
										found = ofs + 26;
										offset = 98; // ('keine_externen_sprachlinks', 98)
									}
									break;
								case 'e':
								case 'E':
									if (ofs+23 < src_len && (src[ofs+6] | 32) == 'x' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 'r' && (src[ofs+10] | 32) == 'n' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 's' && (src[ofs+14] | 32) == 'p' && (src[ofs+15] | 32) == 'r' && (src[ofs+16] | 32) == 'a' && (src[ofs+17] | 32) == 'c' && (src[ofs+18] | 32) == 'h' && (src[ofs+19] | 32) == 'l' && (src[ofs+20] | 32) == 'i' && (src[ofs+21] | 32) == 'n' && (src[ofs+22] | 32) == 'k' && (src[ofs+23] | 32) == 's') {
										found = ofs + 24;
										offset = 97; // ('keineexternensprachlinks', 97)
									}
									break;
							}
						}
						break;
					case 'l':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'n') {
							found = ofs + 5;
							offset = 17; // ('klein', 17)
						}
						break;
				}
				break;
			case 'l':
			case 'L':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'c':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 't') {
							found = ofs + 7;
							offset = 20; // ('lcfirst', 20)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 18; // ('lc', 18)
						}
						break;
					case 'o':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'c':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'l') {
									if (ofs+8 < src_len && (src[ofs+8] | 32) == 'e') {
										found = ofs + 9;
										offset = 37; // ('localurle', 37)
									}
									if (found == -1) {
										found = ofs + 8;
										offset = 35; // ('localurl', 35)
									}
								}
								break;
							case 'k':
								if (ofs+9 < src_len && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'e' && src[ofs+6] == '_' && (src[ofs+7] | 32) == 'u' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 'l') {
									if (ofs+11 < src_len && src[ofs+10] == '_' && (src[ofs+11] | 32) == 'c') {
										found = ofs + 12;
										offset = 36; // ('lokale_url_c', 36)
									}
									if (found == -1) {
										found = ofs + 10;
										offset = 34; // ('lokale_url', 34)
									}
								}
								break;
						}
						break;
				}
				break;
			case 'm':
			case 'M':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 's' && (src[ofs+2] | 32) == 'g') {
					if (ofs+4 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'w') {
						found = ofs + 5;
						offset = 78; // ('msgnw', 78)
					}
					if (found == -1) {
						found = ofs + 3;
						offset = 77; // ('msg', 77)
					}
				}
				break;
			case 'n':
			case 'N':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'h' && (src[ofs+8] | 32) == 't') {
							found = ofs + 9;
							offset = 52; // ('nachricht', 52)
						}
						break;
					case 'o':
						if (ofs+18 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'x' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'a' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'g' && (src[ofs+14] | 32) == 'l' && (src[ofs+15] | 32) == 'i' && (src[ofs+16] | 32) == 'n' && (src[ofs+17] | 32) == 'k' && (src[ofs+18] | 32) == 's') {
							found = ofs + 19;
							offset = 99; // ('noexternallanglinks', 99)
						}
						break;
					case 'r':
						if (ofs+5 < src_len && src[ofs+2] == '_' && (src[ofs+3] | 32) == 'u' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'l') {
							found = ofs + 6;
							offset = 13; // ('nr_url', 13)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 11; // ('nr', 11)
						}
						break;
					case 's':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'e') {
							found = ofs + 3;
							offset = 14; // ('nse', 14)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 12; // ('ns', 12)
						}
						break;
				}
				break;
			case 'p':
			case 'P':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'd':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'l':
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'f' && (src[ofs+6] | 32) == 't') {
											found = ofs + 7;
											offset = 26; // ('padleft', 26)
										}
										break;
									case 'r':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 't') {
											found = ofs + 8;
											offset = 28; // ('padright', 28)
										}
										break;
								}
								break;
							case 'g':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'e') {
									if (ofs+4 < src_len) switch ((src[ofs+4] | 32)) {
										case 'i':
											if (ofs+5 < src_len && (src[ofs+5] | 32) == 'd') {
												found = ofs + 6;
												offset = 103; // ('pageid', 103)
											}
											break;
										case 's':
											if (ofs+23 < src_len && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'g' && (src[ofs+10] | 32) == 'p' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'd' && (src[ofs+14] | 32) == 'i' && (src[ofs+15] | 32) == 'n' && (src[ofs+16] | 32) == 'g' && (src[ofs+17] | 32) == 'c' && (src[ofs+18] | 32) == 'h' && (src[ofs+19] | 32) == 'a' && (src[ofs+20] | 32) == 'n' && (src[ofs+21] | 32) == 'g' && (src[ofs+22] | 32) == 'e' && (src[ofs+23] | 32) == 's') {
												found = ofs + 24;
												offset = 113; // ('pagesusingpendingchanges', 113)
											}
											break;
									}
								}
								break;
						}
						break;
					case 'e':
						if (ofs+17 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'h' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 'n' && (src[ofs+11] | 32) == 'g' && (src[ofs+12] | 32) == 'e' && (src[ofs+13] | 32) == 'l' && (src[ofs+14] | 32) == 'e' && (src[ofs+15] | 32) == 'v' && (src[ofs+16] | 32) == 'e' && (src[ofs+17] | 32) == 'l') {
							found = ofs + 18;
							offset = 112; // ('pendingchangelevel', 112)
						}
						break;
					case 'l':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'u' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'l') {
							found = ofs + 6;
							offset = 47; // ('plural', 47)
						}
						break;
				}
				break;
			case 'r':
			case 'R':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'w') {
							found = ofs + 3;
							offset = 76; // ('raw', 76)
						}
						break;
					case 'o':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'h':
								found = ofs + 3;
								offset = 75; // ('roh', 75)
								break;
							case 'o':
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 'm' && (src[ofs+11] | 32) == 'e') {
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
				}
				break;
			case 's':
			case 'S':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'b' && (src[ofs+7] | 32) == 's' && (src[ofs+8] | 32) == 't' && src[ofs+9] == ':') {
							found = ofs + 10;
							offset = 74; // ('safesubst:', 74)
						}
						break;
					case 'c':
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'h') {
							found = ofs + 10;
							offset = 5; // ('scriptpath', 5)
						}
						break;
					case 'e':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'n') {
							if (ofs+6 < src_len) switch ((src[ofs+6] | 32)) {
								case 'i':
									if (ofs+7 < src_len && (src[ofs+7] | 32) == 'd') {
										found = ofs + 8;
										offset = 101; // ('seitenid', 101)
									}
									break;
								case 'k':
									if (ofs+12 < src_len && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'n' && (src[ofs+10] | 32) == 'u' && (src[ofs+11] | 32) == 'n' && (src[ofs+12] | 32) == 'g') {
										found = ofs + 13;
										offset = 102; // ('seitenkennung', 102)
									}
									break;
							}
						}
						break;
					case 'i':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
							if (ofs+6 < src_len) switch (src[ofs+6]) {
								case '_':
									if (ofs+10 < src_len && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 's' && src[ofs+10] == ':') {
										found = ofs + 11;
										offset = 72; // ('sicher_ers:', 72)
									}
									break;
								case 'e':
								case 'E':
									if (ofs+9 < src_len && (src[ofs+7] | 32) == 'r' && (src[ofs+8] | 32) == 's' && src[ofs+9] == ':') {
										found = ofs + 10;
										offset = 73; // ('sicherers:', 73)
									}
									break;
							}
						}
						break;
					case 'k':
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'p' && (src[ofs+7] | 32) == 'f' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 'd') {
							found = ofs + 10;
							offset = 4; // ('skriptpfad', 4)
						}
						break;
					case 'p':
						if (ofs+19 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'u' && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'g' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 'k' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 'n' && (src[ofs+13] | 32) == 'k' && (src[ofs+14] | 32) == 'o' && (src[ofs+15] | 32) == 'd' && (src[ofs+16] | 32) == 'i' && (src[ofs+17] | 32) == 'e' && (src[ofs+18] | 32) == 'r' && (src[ofs+19] | 32) == 't') {
							found = ofs + 20;
							offset = 45; // ('sprungmarkeenkodiert', 45)
						}
						break;
					case 't':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'i':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'f' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'd') {
									found = ofs + 8;
									offset = 6; // ('stilpfad', 6)
								}
								break;
							case 'y':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'p') {
									if (ofs+6 < src_len) switch ((src[ofs+6] | 32)) {
										case 'a':
											if (ofs+8 < src_len && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'h') {
												found = ofs + 9;
												offset = 8; // ('stylepath', 8)
											}
											break;
										case 'f':
											if (ofs+8 < src_len && (src[ofs+7] | 32) == 'a' && (src[ofs+8] | 32) == 'd') {
												found = ofs + 9;
												offset = 7; // ('stylepfad', 7)
											}
											break;
									}
								}
								break;
						}
						break;
					case 'u':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'b' && (src[ofs+3] | 32) == 's' && (src[ofs+4] | 32) == 't' && src[ofs+5] == ':') {
							found = ofs + 6;
							offset = 71; // ('subst:', 71)
						}
						break;
				}
				break;
			case 'u':
			case 'U':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'c':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'f' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 's' && (src[ofs+6] | 32) == 't') {
							found = ofs + 7;
							offset = 24; // ('ucfirst', 24)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 22; // ('uc', 22)
						}
						break;
					case 'r':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'n') {
							if (ofs+5 < src_len) switch ((src[ofs+5] | 32)) {
								case 'c':
									if (ofs+8 < src_len && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'e') {
										found = ofs + 9;
										offset = 16; // ('urlencode', 16)
									}
									break;
								case 'k':
									if (ofs+11 < src_len && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'i' && (src[ofs+9] | 32) == 'e' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 't') {
										found = ofs + 12;
										offset = 15; // ('urlenkodiert', 15)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'v':
			case 'V':
				if (ofs+16 < src_len && (src[ofs+1] | 32) == 'o' && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 't' && src[ofs+6] == -61 && src[ofs+7] == -92 && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'd' && (src[ofs+10] | 32) == 'i' && (src[ofs+11] | 32) == 'g' && (src[ofs+12] | 32) == 'e' && src[ofs+13] == '_' && (src[ofs+14] | 32) == 'u' && (src[ofs+15] | 32) == 'r' && (src[ofs+16] | 32) == 'l') {
					if (ofs+18 < src_len && src[ofs+17] == '_' && (src[ofs+18] | 32) == 'c') {
						found = ofs + 19;
						offset = 40; // ('vollstÃ¤ndige_url_c', 40)
					}
					if (found == -1) {
						found = ofs + 17;
						offset = 38; // ('vollstÃ¤ndige_url', 38)
					}
				}
				break;
			case 'w':
			case 'W':
				if (ofs+18 < src_len && (src[ofs+1] | 32) == 'b' && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'o' && (src[ofs+10] | 32) == 'r' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'u' && (src[ofs+13] | 32) == 'm' && (src[ofs+14] | 32) == 's' && (src[ofs+15] | 32) == 'n' && (src[ofs+16] | 32) == 'a' && (src[ofs+17] | 32) == 'm' && (src[ofs+18] | 32) == 'e') {
					found = ofs + 19;
					offset = 100; // ('wbrepositoriumsname', 100)
				}
				break;
			case 'z':
			case 'Z':
				if (ofs+11 < src_len && (src[ofs+1] | 32) == 'a' && (src[ofs+2] | 32) == 'h' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'f' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'r' && (src[ofs+9] | 32) == 'm' && (src[ofs+10] | 32) == 'a' && (src[ofs+11] | 32) == 't') {
					found = ofs + 12;
					offset = 29; // ('zahlenformat', 29)
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
