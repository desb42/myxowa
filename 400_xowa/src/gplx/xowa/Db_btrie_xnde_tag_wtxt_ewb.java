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
public class Db_btrie_xnde_tag_wtxt_ewb implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_xnde_tag_wtxt_ewb(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("72882aca6414cde69b8cf3c878adc401"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;

		switch (b) {
			case 'a':
			case 'A':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'b':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'b':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'r') {
									found = ofs + 4;
									offset = 9; // ('abbr', 9)
								}
								break;
							case 's':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 't') {
									found = ofs + 9;
									offset = 75; // ('Abschnitt', 75)
								}
								break;
						}
						break;
					case 'u':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'd' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'o') {
							found = ofs + 5;
							offset = 112; // ('audio', 112)
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 52; // ('a', 52)
				}
				break;
			case 'b':
			case 'B':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'd':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'i':
								found = ofs + 3;
								offset = 94; // ('bdi', 94)
								break;
							case 'o':
								found = ofs + 3;
								offset = 98; // ('bdo', 98)
								break;
						}
						break;
					case 'i':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'g') {
							found = ofs + 3;
							offset = 15; // ('big', 15)
						}
						break;
					case 'l':
						if (ofs+9 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'k' && (src[ofs+5] | 32) == 'q' && (src[ofs+6] | 32) == 'u' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'e') {
							found = ofs + 10;
							offset = 21; // ('blockquote', 21)
						}
						break;
					case 'r':
						found = ofs + 2;
						offset = 29; // ('br', 29)
						break;
					case 'u':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'n') {
							found = ofs + 6;
							offset = 84; // ('button', 84)
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 0; // ('b', 0)
				}
				break;
			case 'c':
			case 'C':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'p':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'n') {
									found = ofs + 7;
									offset = 49; // ('caption', 49)
								}
								break;
							case 't':
								if (ofs+11 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'g' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'r' && (src[ofs+7] | 32) == 'y' && (src[ofs+8] | 32) == 't' && (src[ofs+9] | 32) == 'r' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'e') {
									found = ofs + 12;
									offset = 78; // ('categoryTree', 78)
								}
								break;
						}
						break;
					case 'e':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r') {
							found = ofs + 6;
							offset = 24; // ('center', 24)
						}
						break;
					case 'h':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'm') {
							found = ofs + 4;
							offset = 119; // ('chem', 119)
						}
						break;
					case 'i':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'e') {
							found = ofs + 4;
							offset = 4; // ('cite', 4)
						}
						break;
					case 'o':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'd':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'e') {
									found = ofs + 4;
									offset = 17; // ('code', 17)
								}
								break;
							case 'l':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'u' && (src[ofs+7] | 32) == 'p') {
									found = ofs + 8;
									offset = 50; // ('colgroup', 50)
								}
								if (found == -1) {
									found = ofs + 3;
									offset = 51; // ('col', 51)
								}
								break;
						}
						break;
				}
				break;
			case 'd':
			case 'D':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'a') {
							found = ofs + 4;
							offset = 95; // ('data', 95)
						}
						break;
					case 'd':
						found = ofs + 2;
						offset = 38; // ('dd', 38)
						break;
					case 'e':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'l') {
							found = ofs + 3;
							offset = 11; // ('del', 11)
						}
						break;
					case 'f':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'n') {
							found = ofs + 3;
							offset = 5; // ('dfn', 5)
						}
						break;
					case 'i':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'v') {
							found = ofs + 3;
							offset = 27; // ('div', 27)
						}
						break;
					case 'l':
						found = ofs + 2;
						offset = 41; // ('dl', 41)
						break;
					case 't':
						found = ofs + 2;
						offset = 37; // ('dt', 37)
						break;
					case 'y':
						if (ofs+14 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'm' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'p' && (src[ofs+8] | 32) == 'a' && (src[ofs+9] | 32) == 'g' && (src[ofs+10] | 32) == 'e' && (src[ofs+11] | 32) == 'l' && (src[ofs+12] | 32) == 'i' && (src[ofs+13] | 32) == 's' && (src[ofs+14] | 32) == 't') {
							found = ofs + 15;
							offset = 79; // ('dynamicPageList', 79)
						}
						break;
				}
				break;
			case 'e':
			case 'E':
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'm') {
					found = ofs + 2;
					offset = 3; // ('em', 3)
				}
				break;
			case 'f':
			case 'F':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'i':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'g') {
							if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
								case 'c':
									if (ofs+9 < src_len && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'p' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'o' && (src[ofs+9] | 32) == 'n') {
										found = ofs + 10;
										offset = 118; // ('figcaption', 118)
									}
									break;
								case 'u':
									if (ofs+5 < src_len && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e') {
										if (ofs+12 < src_len && src[ofs+6] == '-' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'i' && (src[ofs+11] | 32) == 'n' && (src[ofs+12] | 32) == 'e') {
											found = ofs + 13;
											offset = 117; // ('figure-inline', 117)
										}
										if (found == -1) {
											found = ofs + 6;
											offset = 116; // ('figure', 116)
										}
									}
									break;
							}
						}
						break;
					case 'o':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'n':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 't') {
									found = ofs + 4;
									offset = 23; // ('font', 23)
								}
								break;
							case 'r':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'm') {
									found = ofs + 4;
									offset = 90; // ('form', 90)
								}
								break;
						}
						break;
				}
				break;
			case 'g':
			case 'G':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'y') {
							found = ofs + 7;
							offset = 69; // ('gallery', 69)
						}
						break;
					case 'r':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'h') {
							found = ofs + 5;
							offset = 105; // ('graph', 105)
						}
						break;
				}
				break;
			case 'h':
			case 'H':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case '1':
						found = ofs + 2;
						offset = 30; // ('h1', 30)
						break;
					case '2':
						found = ofs + 2;
						offset = 31; // ('h2', 31)
						break;
					case '3':
						found = ofs + 2;
						offset = 32; // ('h3', 32)
						break;
					case '4':
						found = ofs + 2;
						offset = 33; // ('h4', 33)
						break;
					case '5':
						found = ofs + 2;
						offset = 34; // ('h5', 34)
						break;
					case '6':
						found = ofs + 2;
						offset = 35; // ('h6', 35)
						break;
					case 'i':
					case 'I':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'o') {
							found = ofs + 5;
							offset = 72; // ('hiero', 72)
						}
						break;
					case 'r':
					case 'R':
						found = ofs + 2;
						offset = 28; // ('hr', 28)
						break;
				}
				break;
			case 'i':
			case 'I':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'm':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'm' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 'p') {
									found = ofs + 8;
									offset = 70; // ('imageMap', 70)
								}
								break;
							case 'g':
								found = ofs + 3;
								offset = 53; // ('img', 53)
								break;
						}
						break;
					case 'n':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'c':
								if (ofs+10 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'l' && (src[ofs+10] | 32) == 'y') {
									found = ofs + 11;
									offset = 58; // ('includeonly', 58)
								}
								break;
							case 'd':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'o' && (src[ofs+8] | 32) == 'r') {
									found = ofs + 9;
									offset = 103; // ('indicator', 103)
								}
								break;
							case 'p':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'u' && (src[ofs+4] | 32) == 't') {
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'b' && (src[ofs+6] | 32) == 'o' && (src[ofs+7] | 32) == 'x') {
										found = ofs + 8;
										offset = 73; // ('inputBox', 73)
									}
									if (found == -1) {
										found = ofs + 5;
										offset = 81; // ('input', 81)
									}
								}
								break;
							case 's':
								found = ofs + 3;
								offset = 8; // ('ins', 8)
								break;
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 2; // ('i', 2)
				}
				break;
			case 'k':
			case 'K':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'b' && (src[ofs+2] | 32) == 'd') {
					found = ofs + 3;
					offset = 19; // ('kbd', 19)
				}
				break;
			case 'l':
			case 'L':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'g' && (src[ofs+4] | 32) == 'u' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'g' && (src[ofs+7] | 32) == 'e' && (src[ofs+8] | 32) == 's') {
							found = ofs + 9;
							offset = 92; // ('languages', 92)
						}
						break;
					case 'i':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 'k') {
							found = ofs + 4;
							offset = 110; // ('link', 110)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 36; // ('li', 36)
						}
						break;
				}
				break;
			case 'm':
			case 'M':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'p':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'f':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'e') {
											found = ofs + 8;
											offset = 107; // ('mapframe', 107)
										}
										break;
									case 'l':
										if (ofs+6 < src_len && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'k') {
											found = ofs + 7;
											offset = 108; // ('maplink', 108)
										}
										break;
								}
								break;
							case 'r':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'k') {
									found = ofs + 4;
									offset = 96; // ('mark', 96)
								}
								break;
							case 't':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'h') {
									found = ofs + 4;
									offset = 64; // ('math', 64)
								}
								break;
						}
						break;
					case 'e':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'a') {
							found = ofs + 4;
							offset = 109; // ('meta', 109)
						}
						break;
				}
				break;
			case 'n':
			case 'N':
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'o') {
					if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
						case 'i':
							if (ofs+8 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'u' && (src[ofs+7] | 32) == 'd' && (src[ofs+8] | 32) == 'e') {
								found = ofs + 9;
								offset = 59; // ('noinclude', 59)
							}
							break;
						case 'w':
							if (ofs+5 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'k' && (src[ofs+5] | 32) == 'i') {
								found = ofs + 6;
								offset = 61; // ('nowiki', 61)
							}
							break;
					}
				}
				break;
			case 'o':
			case 'O':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'l':
						found = ofs + 2;
						offset = 39; // ('ol', 39)
						break;
					case 'n':
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 'l' && (src[ofs+3] | 32) == 'y' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'l' && (src[ofs+8] | 32) == 'u' && (src[ofs+9] | 32) == 'd' && (src[ofs+10] | 32) == 'e') {
							found = ofs + 11;
							offset = 60; // ('onlyinclude', 60)
						}
						break;
					case 'p':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 't') {
							if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
								case 'g':
									if (ofs+7 < src_len && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'u' && (src[ofs+7] | 32) == 'p') {
										found = ofs + 8;
										offset = 87; // ('optgroup', 87)
									}
									break;
								case 'i':
									if (ofs+5 < src_len && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'n') {
										found = ofs + 6;
										offset = 86; // ('option', 86)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'p':
			case 'P':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'o':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'm') {
							found = ofs + 4;
							offset = 63; // ('poem', 63)
						}
						break;
					case 'r':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'e') {
							found = ofs + 3;
							offset = 22; // ('pre', 22)
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 25; // ('p', 25)
				}
				break;
			case 'q':
			case 'Q':
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 'u' && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'z') {
					found = ofs + 4;
					offset = 102; // ('quiz', 102)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 104; // ('q', 104)
				}
				break;
			case 'r':
			case 'R':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'b':
						found = ofs + 2;
						offset = 56; // ('rb', 56)
						break;
					case 'e':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'f') {
							if (ofs+9 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'r' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'c' && (src[ofs+8] | 32) == 'e' && (src[ofs+9] | 32) == 's') {
								found = ofs + 10;
								offset = 66; // ('references', 66)
							}
							if (found == -1) {
								found = ofs + 3;
								offset = 65; // ('ref', 65)
							}
						}
						break;
					case 'p':
						found = ofs + 2;
						offset = 57; // ('rp', 57)
						break;
					case 't':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'c') {
							found = ofs + 3;
							offset = 115; // ('rtc', 115)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 55; // ('rt', 55)
						}
						break;
					case 'u':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'b' && (src[ofs+3] | 32) == 'y') {
							found = ofs + 4;
							offset = 54; // ('ruby', 54)
						}
						break;
				}
				break;
			case 's':
			case 'S':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'm' && (src[ofs+3] | 32) == 'p') {
							found = ofs + 4;
							offset = 20; // ('samp', 20)
						}
						break;
					case 'c':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'o':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'e') {
									found = ofs + 5;
									offset = 83; // ('score', 83)
								}
								break;
							case 'r':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 'p' && (src[ofs+5] | 32) == 't') {
									found = ofs + 6;
									offset = 88; // ('script', 88)
								}
								break;
						}
						break;
					case 'e':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'c':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'n') {
									found = ofs + 7;
									offset = 74; // ('section', 74)
								}
								break;
							case 'l':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 't') {
									found = ofs + 6;
									offset = 85; // ('select', 85)
								}
								break;
						}
						break;
					case 'm':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'l') {
							found = ofs + 5;
							offset = 16; // ('small', 16)
						}
						break;
					case 'o':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'u' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'e') {
							found = ofs + 6;
							offset = 67; // ('source', 67)
						}
						break;
					case 'p':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'a' && (src[ofs+3] | 32) == 'n') {
							found = ofs + 4;
							offset = 26; // ('span', 26)
						}
						break;
					case 't':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'r':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'i':
										if (ofs+5 < src_len && (src[ofs+4] | 32) == 'k' && (src[ofs+5] | 32) == 'e') {
											found = ofs + 6;
											offset = 10; // ('strike', 10)
										}
										break;
									case 'o':
										if (ofs+5 < src_len && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'g') {
											found = ofs + 6;
											offset = 1; // ('strong', 1)
										}
										break;
								}
								break;
							case 'y':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e') {
									found = ofs + 5;
									offset = 89; // ('style', 89)
								}
								break;
						}
						break;
					case 'u':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'b':
								found = ofs + 3;
								offset = 13; // ('sub', 13)
								break;
							case 'p':
								found = ofs + 3;
								offset = 14; // ('sup', 14)
								break;
						}
						break;
					case 'y':
						if (ofs+14 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'x' && (src[ofs+6] | 32) == 'h' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'g' && (src[ofs+9] | 32) == 'h' && (src[ofs+10] | 32) == 'l' && (src[ofs+11] | 32) == 'i' && (src[ofs+12] | 32) == 'g' && (src[ofs+13] | 32) == 'h' && (src[ofs+14] | 32) == 't') {
							found = ofs + 15;
							offset = 68; // ('syntaxHighlight', 68)
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 12; // ('s', 12)
				}
				break;
			case 't':
			case 'T':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'b' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'e') {
							found = ofs + 5;
							offset = 42; // ('table', 42)
						}
						break;
					case 'b':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'y') {
							found = ofs + 5;
							offset = 48; // ('tbody', 48)
						}
						break;
					case 'd':
						found = ofs + 2;
						offset = 44; // ('td', 44)
						break;
					case 'e':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'm':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'a' && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'e') {
									if (ofs+8 < src_len) switch ((src[ofs+8] | 32)) {
										case 'd':
											if (ofs+11 < src_len && (src[ofs+9] | 32) == 'a' && (src[ofs+10] | 32) == 't' && (src[ofs+11] | 32) == 'a') {
												found = ofs + 12;
												offset = 93; // ('templateData', 93)
											}
											break;
										case 's':
											if (ofs+13 < src_len && (src[ofs+9] | 32) == 't' && (src[ofs+10] | 32) == 'y' && (src[ofs+11] | 32) == 'l' && (src[ofs+12] | 32) == 'e' && (src[ofs+13] | 32) == 's') {
												found = ofs + 14;
												offset = 111; // ('templatestyles', 111)
											}
											break;
									}
								}
								break;
							case 'x':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'a' && (src[ofs+5] | 32) == 'r' && (src[ofs+6] | 32) == 'e' && (src[ofs+7] | 32) == 'a') {
									found = ofs + 8;
									offset = 82; // ('textarea', 82)
								}
								break;
						}
						break;
					case 'f':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 't') {
							found = ofs + 5;
							offset = 47; // ('tfoot', 47)
						}
						break;
					case 'h':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'a' && (src[ofs+4] | 32) == 'd') {
							found = ofs + 5;
							offset = 46; // ('thead', 46)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 45; // ('th', 45)
						}
						break;
					case 'i':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'm' && (src[ofs+3] | 32) == 'e') {
							if (ofs+7 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'e') {
								found = ofs + 8;
								offset = 71; // ('timeline', 71)
							}
							if (found == -1) {
								found = ofs + 4;
								offset = 80; // ('time', 80)
							}
						}
						break;
					case 'r':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'c':
										if (ofs+4 < src_len && (src[ofs+4] | 32) == 'k') {
											found = ofs + 5;
											offset = 114; // ('track', 114)
										}
										break;
									case 'n':
										if (ofs+8 < src_len && (src[ofs+4] | 32) == 's' && (src[ofs+5] | 32) == 'l' && (src[ofs+6] | 32) == 'a' && (src[ofs+7] | 32) == 't' && (src[ofs+8] | 32) == 'e') {
											found = ofs + 9;
											offset = 91; // ('translate', 91)
										}
										break;
								}
								break;
							case 'e':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'o') {
									found = ofs + 6;
									offset = 77; // ('trecho', 77)
								}
								break;
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 43; // ('tr', 43)
						}
						break;
					case 't':
						found = ofs + 2;
						offset = 18; // ('tt', 18)
						break;
				}
				break;
			case 'u':
			case 'U':
				if (ofs+1 < src_len && (src[ofs+1] | 32) == 'l') {
					found = ofs + 2;
					offset = 40; // ('ul', 40)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 7; // ('u', 7)
				}
				break;
			case 'v':
			case 'V':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'r') {
							found = ofs + 3;
							offset = 6; // ('var', 6)
						}
						break;
					case 'i':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'd' && (src[ofs+3] | 32) == 'e' && (src[ofs+4] | 32) == 'o') {
							found = ofs + 5;
							offset = 113; // ('video', 113)
						}
						break;
				}
				break;
			case 'w':
			case 'W':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'b' && (src[ofs+2] | 32) == 'r') {
					found = ofs + 3;
					offset = 97; // ('wbr', 97)
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
										offset = 62; // ('xowa_cmd', 62)
									}
									break;
								case 'h':
									if (ofs+8 < src_len && (src[ofs+6] | 32) == 't' && (src[ofs+7] | 32) == 'm' && (src[ofs+8] | 32) == 'l') {
										found = ofs + 9;
										offset = 99; // ('xowa_html', 99)
									}
									break;
								case 'w':
									if (ofs+14 < src_len && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'k' && (src[ofs+8] | 32) == 'i' && src[ofs+9] == '_' && (src[ofs+10] | 32) == 's' && (src[ofs+11] | 32) == 'e' && (src[ofs+12] | 32) == 't' && (src[ofs+13] | 32) == 'u' && (src[ofs+14] | 32) == 'p') {
										found = ofs + 15;
										offset = 106; // ('xowa_Wiki_setup', 106)
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
										offset = 100; // ('xtag_bgn', 100)
									}
									break;
								case 'e':
									if (ofs+7 < src_len && (src[ofs+6] | 32) == 'n' && (src[ofs+7] | 32) == 'd') {
										found = ofs + 8;
										offset = 101; // ('xtag_end', 101)
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
					offset = 76; // ('×§×\x98×¢', 76)
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
