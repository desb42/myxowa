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
public class Db_btrie_xnde_tag_defn_ewv implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_xnde_tag_defn_ewv(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("45a97f9998ef7f184710cddf4c90f4a0"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;

		switch (b) {
			case 'a':
			case 'A':
				if (ofs+8 < src_len && (src[ofs+1] | 32) == 'b' && (src[ofs+2] | 32) == 's' && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 't') {
					found = ofs + 9;
					offset = 15; // ('Abschnitt', 15)
				}
				break;
			case 'b':
			case 'B':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'u' && (src[ofs+2] | 32) == 'y') {
					found = ofs + 3;
					offset = 27; // ('buy', 27)
				}
				break;
			case 'c':
			case 'C':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'y') {
							if (ofs+8 < src_len) switch ((src[ofs+8] | 32)) {
								case 'l':
									if (ofs+11 < src_len && (src[ofs+9] | 32) == 'i' && (src[ofs+10] | 32) == 's' && (src[ofs+11] | 32) == 't') {
										found = ofs + 12;
										offset = 20; // ('categoryList', 20)
									}
									break;
								case 't':
									if (ofs+11 < src_len && (src[ofs+9] | 32) == 'r' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'e') {
										found = ofs + 12;
										offset = 21; // ('categoryTree', 21)
									}
									break;
							}
						}
						break;
					case 'h':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'e':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'm') {
									found = ofs + 4;
									offset = 48; // ('chem', 48)
								}
								break;
							case 'o':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'e') {
									found = ofs + 6;
									offset = 41; // ('choose', 41)
								}
								break;
						}
						break;
				}
				break;
			case 'd':
			case 'D':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'o':
						found = ofs + 2;
						offset = 28; // ('do', 28)
						break;
					case 'r':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'k') {
							found = ofs + 5;
							offset = 29; // ('drink', 29)
						}
						break;
					case 'y':
						if (ofs+14 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'p' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 'g' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'l' && (src[ofs+12] | 32) == 'i' && (src[ofs+13] | 32) == 's' && (src[ofs+14] | 32) == 't') {
							found = ofs + 15;
							offset = 22; // ('dynamicPageList', 22)
						}
						break;
				}
				break;
			case 'e':
			case 'E':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'a' && (src[ofs+2] | 32) == 't') {
					found = ofs + 3;
					offset = 30; // ('eat', 30)
				}
				break;
			case 'g':
			case 'G':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'y') {
							found = ofs + 7;
							offset = 8; // ('gallery', 8)
						}
						break;
					case 'r':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'h') {
							found = ofs + 5;
							offset = 40; // ('graph', 40)
						}
						break;
				}
				break;
			case 'h':
			case 'H':
				if (ofs+4 < src_len && (src[ofs+1] | 32) == 'i' && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'o') {
					found = ofs + 5;
					offset = 11; // ('hiero', 11)
				}
				break;
			case 'i':
			case 'I':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'm':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'm' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'p') {
							found = ofs + 8;
							offset = 9; // ('imageMap', 9)
						}
						break;
					case 'n':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'c':
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'y') {
									found = ofs + 11;
									offset = 50; // ('includeonly', 50)
								}
								break;
							case 'd':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'r') {
									found = ofs + 9;
									offset = 39; // ('indicator', 39)
								}
								break;
							case 'p':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'u' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'b' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'x') {
									found = ofs + 8;
									offset = 12; // ('inputBox', 12)
								}
								break;
						}
						break;
				}
				break;
			case 'l':
			case 'L':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 's') {
							found = ofs + 9;
							offset = 25; // ('languages', 25)
						}
						break;
					case 'i':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 's' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'g') {
							found = ofs + 7;
							offset = 31; // ('listing', 31)
						}
						break;
				}
				break;
			case 'm':
			case 'M':
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'a') {
					if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
						case 'p':
							if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
								case 'f':
									if (ofs+7 < src_len && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'e') {
										found = ofs + 8;
										offset = 45; // ('mapframe', 45)
									}
									break;
								case 'l':
									if (ofs+6 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'k') {
										found = ofs + 7;
										offset = 46; // ('maplink', 46)
									}
									break;
							}
							break;
						case 't':
							if (ofs+3 < src_len && (src[ofs+3] | 32) == 'h') {
								found = ofs + 4;
								offset = 3; // ('math', 3)
							}
							break;
					}
				}
				break;
			case 'n':
			case 'N':
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'o') {
					if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
						case 'i':
							if (ofs+8 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'u' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'e') {
								found = ofs + 9;
								offset = 51; // ('noinclude', 51)
							}
							break;
						case 'w':
							if (ofs+5 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'k' && (src[ofs+5] | 32) == 'i') {
								found = ofs + 6;
								offset = 49; // ('nowiki', 49)
							}
							break;
					}
				}
				break;
			case 'o':
			case 'O':
				if (ofs+10 < src_len && (src[ofs+1] | 32) == 'n' && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'y' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'l' && (src[ofs+8] | 32) == 'u' && (src[ofs+9] | 32) == 'd' && (src[ofs+10] | 32) == 'e') {
					found = ofs + 11;
					offset = 52; // ('onlyinclude', 52)
				}
				break;
			case 'p':
			case 'P':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'g' && (src[ofs+3] | 32) == 'e') {
							if (ofs+4 < src_len) switch ((src[ofs+4] | 32)) {
								case 'l':
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 's' && (src[ofs+7] | 32) == 't') {
										found = ofs + 8;
										offset = 19; // ('pagelist', 19)
									}
									break;
								case 'q':
									if (ofs+10 < src_len && (src[ofs+5] | 32) == 'u' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'l' && (src[ofs+8] | 32) == 'i' && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'y') {
										found = ofs + 11;
										offset = 18; // ('pagequality', 18)
									}
									break;
								case 's':
									found = ofs + 5;
									offset = 13; // ('pages', 13)
									break;
							}
						}
						break;
					case 'o':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'm') {
							found = ofs + 4;
							offset = 2; // ('poem', 2)
						}
						break;
					case 'r':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'e') {
							found = ofs + 3;
							offset = 0; // ('pre', 0)
						}
						break;
				}
				break;
			case 'q':
			case 'Q':
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 'u' && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'z') {
					found = ofs + 4;
					offset = 38; // ('quiz', 38)
				}
				break;
			case 'r':
			case 'R':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'e':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'f') {
							if (ofs+9 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 's') {
								found = ofs + 10;
								offset = 5; // ('references', 5)
							}
							if (found == -1) {
								found = ofs + 3;
								offset = 4; // ('ref', 4)
							}
						}
						break;
					case 's':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 's') {
							found = ofs + 3;
							offset = 34; // ('rss', 34)
						}
						break;
				}
				break;
			case 's':
			case 'S':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'c':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'e') {
							found = ofs + 5;
							offset = 23; // ('score', 23)
						}
						break;
					case 'e':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'c':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'n') {
									found = ofs + 7;
									offset = 14; // ('section', 14)
								}
								break;
							case 'e':
								found = ofs + 3;
								offset = 32; // ('see', 32)
								break;
						}
						break;
					case 'l':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'p') {
							found = ofs + 5;
							offset = 33; // ('sleep', 33)
						}
						break;
					case 'o':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'u' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'e') {
							found = ofs + 6;
							offset = 6; // ('source', 6)
						}
						break;
					case 'y':
						if (ofs+14 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'x' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'g' && (src[ofs+9] | 32) == 'h' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'g' && (src[ofs+13] | 32) == 'h' && (src[ofs+14] | 32) == 't') {
							found = ofs + 15;
							offset = 7; // ('syntaxHighlight', 7)
						}
						break;
				}
				break;
			case 't':
			case 'T':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'b') {
							if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
								case 'b':
									if (ofs+5 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
										found = ofs + 6;
										offset = 42; // ('tabber', 42)
									}
									break;
								case 'v':
									if (ofs+6 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'w') {
										found = ofs + 7;
										offset = 43; // ('tabview', 43)
									}
									break;
							}
						}
						break;
					case 'e':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'm' && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'e') {
							if (ofs+8 < src_len) switch ((src[ofs+8] | 32)) {
								case 'd':
									if (ofs+11 < src_len && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 't' && (src[ofs+11] | 32) == 'a') {
										found = ofs + 12;
										offset = 26; // ('templateData', 26)
									}
									break;
								case 's':
									if (ofs+13 < src_len && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'y' && (src[ofs+11] | 32) == 'l' && (src[ofs+12] | 32) == 'e' && (src[ofs+13] | 32) == 's') {
										found = ofs + 14;
										offset = 47; // ('templatestyles', 47)
									}
									break;
							}
						}
						break;
					case 'i':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'm' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'e') {
							found = ofs + 8;
							offset = 10; // ('timeline', 10)
						}
						break;
					case 'r':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'e') {
									found = ofs + 9;
									offset = 24; // ('translate', 24)
								}
								break;
							case 'e':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'o') {
									found = ofs + 6;
									offset = 17; // ('trecho', 17)
								}
								break;
						}
						break;
				}
				break;
			case 'x':
			case 'X':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'o':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'w' && (src[ofs+3] | 32) == 'a' && src[ofs+4] == '_') {
							if (ofs+5 < src_len) switch ((src[ofs+5] | 32)) {
								case 'c':
									if (ofs+7 < src_len && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'd') {
										found = ofs + 8;
										offset = 1; // ('xowa_cmd', 1)
									}
									break;
								case 'h':
									if (ofs+8 < src_len && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'm' && (src[ofs+8] | 32) == 'l') {
										found = ofs + 9;
										offset = 35; // ('xowa_html', 35)
									}
									break;
								case 'w':
									if (ofs+14 < src_len && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'k' && (src[ofs+8] | 32) == 'i' && src[ofs+9] == '_' && (src[ofs+10] | 32) == 's' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 't' && (src[ofs+13] | 32) == 'u' && (src[ofs+14] | 32) == 'p') {
										found = ofs + 15;
										offset = 44; // ('xowa_Wiki_setup', 44)
									}
									break;
							}
						}
						break;
					case 't':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'g' && src[ofs+4] == '_') {
							if (ofs+5 < src_len) switch ((src[ofs+5] | 32)) {
								case 'b':
									if (ofs+7 < src_len && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'n') {
										found = ofs + 8;
										offset = 36; // ('xtag_bgn', 36)
									}
									break;
								case 'e':
									if (ofs+7 < src_len && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'd') {
										found = ofs + 8;
										offset = 37; // ('xtag_end', 37)
									}
									break;
							}
						}
						break;
				}
				break;
			case -41:
				if (ofs+5 < src_len && src[ofs+1] == -89 && src[ofs+2] == -41 && src[ofs+3] == -104 && src[ofs+4] == -41 && src[ofs+5] == -94) {
					found = ofs + 6;
					offset = 16; // ('×§×\x98×¢', 16)
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
