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
public class Db_btrie_wtxt_1 implements Db_btrie {
	private final Object[] objs;
	public Db_btrie_wtxt_1(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("1542566023a0d8d5ce27f3d323d63457"); }
	private Db_btrie_result Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		int found = -1;
		int offset = -1;

		switch (b) {
			case 9:
				found = ofs + 1;
				offset = 3; // ('\t', 3)
				break;
			case 10:
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 9:
						found = ofs + 2;
						offset = 82; // ('\n\t', 82)
						break;
					case ' ':
						found = ofs + 2;
						offset = 81; // ('\n ', 81)
						break;
					case '!':
						found = ofs + 2;
						offset = 77; // ('\n!', 77)
						break;
					case '#':
						found = ofs + 2;
						offset = 12; // ('\n#', 12)
						break;
					case '*':
						found = ofs + 2;
						offset = 11; // ('\n*', 11)
						break;
					case '-':
						if (ofs+4 < src_len && src[ofs+2] == '-' && src[ofs+3] == '-' && src[ofs+4] == '-') {
							found = ofs + 5;
							offset = 16; // ('\n----', 16)
						}
						break;
					case ':':
						found = ofs + 2;
						offset = 14; // ('\n:', 14)
						break;
					case ';':
						found = ofs + 2;
						offset = 13; // ('\n;', 13)
						break;
					case '=':
						found = ofs + 2;
						offset = 15; // ('\n=', 15)
						break;
					case '{':
						if (ofs+2 < src_len && src[ofs+2] == '|') {
							found = ofs + 3;
							offset = 73; // ('\n{|', 73)
						}
						break;
					case '|':
						if (ofs+2 < src_len) switch (src[ofs+2]) {
							case '+':
								found = ofs + 3;
								offset = 78; // ('\n|+', 78)
								break;
							case '-':
								found = ofs + 3;
								offset = 75; // ('\n|-', 75)
								break;
							case '}':
								found = ofs + 3;
								offset = 74; // ('\n|}', 74)
								break;
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 76; // ('\n|', 76)
						}
						break;
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 5; // ('\n', 5)
				}
				break;
			case ' ':
				found = ofs + 1;
				offset = 2; // (' ', 2)
				break;
			case '!':
				if (ofs+1 < src_len && src[ofs+1] == '!') {
					found = ofs + 2;
					offset = 80; // ('!!', 80)
				}
				break;
			case '&':
				if (ofs+4 < src_len && src[ofs+1] == '#' && src[ofs+2] == '0' && src[ofs+3] == '9' && src[ofs+4] == ';') {
					found = ofs + 5;
					offset = 4; // ('&#09;', 4)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 6; // ('&', 6)
				}
				break;
			case '\'':
				if (ofs+1 < src_len && src[ofs+1] == '\'') {
					found = ofs + 2;
					offset = 7; // ("''", 7)
				}
				break;
			case ':':
				found = ofs + 1;
				offset = 8; // (':', 8)
				break;
			case '<':
				if (ofs+3 < src_len && src[ofs+1] == '!' && src[ofs+2] == '-' && src[ofs+3] == '-') {
					found = ofs + 4;
					offset = 83; // ('<!--', 83)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 17; // ('<', 17)
				}
				break;
			case '=':
				found = ofs + 1;
				offset = 1; // ('=', 1)
				break;
			case '[':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case '/':
						if (ofs+2 < src_len && src[ofs+2] == '/') {
							found = ofs + 3;
							offset = 68; // ('[//', 68)
						}
						break;
					case '[':
						if (ofs+3 < src_len && src[ofs+2] == '/' && src[ofs+3] == '/') {
							found = ofs + 4;
							offset = 69; // ('[[//', 69)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 9; // ('[[', 9)
						}
						break;
					case 'b':
					case 'B':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && src[ofs+7] == ':') {
							found = ofs + 8;
							offset = 61; // ('[bicoin:', 61)
						}
						break;
					case 'f':
					case 'F':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case ':':
									found = ofs + 5;
									offset = 23; // ('[ftp:', 23)
									break;
								case 's':
								case 'S':
									if (ofs+5 < src_len && src[ofs+5] == ':') {
										found = ofs + 6;
										offset = 25; // ('[ftps:', 25)
									}
									break;
							}
						}
						break;
					case 'g':
					case 'G':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'e':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'o' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 67; // ('[geo:', 67)
								}
								break;
							case 'i':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 't' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 57; // ('[git:', 57)
								}
								break;
							case 'o':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && src[ofs+7] == ':') {
									found = ofs + 8;
									offset = 41; // ('[gopher:', 41)
								}
								break;
						}
						break;
					case 'h':
					case 'H':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p') {
							if (ofs+5 < src_len) switch (src[ofs+5]) {
								case ':':
									found = ofs + 6;
									offset = 19; // ('[http:', 19)
									break;
								case 's':
								case 'S':
									if (ofs+6 < src_len && src[ofs+6] == ':') {
										found = ofs + 7;
										offset = 21; // ('[https:', 21)
									}
									break;
							}
						}
						break;
					case 'i':
					case 'I':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'c') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case ':':
									found = ofs + 5;
									offset = 31; // ('[irc:', 31)
									break;
								case 's':
								case 'S':
									if (ofs+5 < src_len && src[ofs+5] == ':') {
										found = ofs + 6;
										offset = 33; // ('[ircs:', 33)
									}
									break;
							}
						}
						break;
					case 'm':
					case 'M':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'a':
								if (ofs+3 < src_len) switch ((src[ofs+3] | 32)) {
									case 'g':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'n' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 't' && src[ofs+7] == ':') {
											found = ofs + 8;
											offset = 63; // ('[magnet:', 63)
										}
										break;
									case 'i':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'o' && src[ofs+7] == ':') {
											found = ofs + 8;
											offset = 49; // ('[mailto:', 49)
										}
										break;
								}
								break;
							case 'm':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 's' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 59; // ('[mms:', 59)
								}
								break;
						}
						break;
					case 'n':
					case 'N':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p' && src[ofs+5] == ':') {
							found = ofs + 6;
							offset = 45; // ('[nntp:', 45)
						}
						break;
					case 's':
					case 'S':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'f':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p' && src[ofs+5] == ':') {
									found = ofs + 6;
									offset = 29; // ('[sftp:', 29)
								}
								break;
							case 'i':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'p') {
									if (ofs+4 < src_len) switch (src[ofs+4]) {
										case ':':
											found = ofs + 5;
											offset = 37; // ('[sip:', 37)
											break;
										case 's':
										case 'S':
											if (ofs+5 < src_len && src[ofs+5] == ':') {
												found = ofs + 6;
												offset = 39; // ('[sips:', 39)
											}
											break;
									}
								}
								break;
							case 'm':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 's' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 53; // ('[sms:', 53)
								}
								break;
							case 's':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'h' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 27; // ('[ssh:', 27)
								}
								break;
							case 'v':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'n' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 55; // ('[svn:', 55)
								}
								break;
						}
						break;
					case 't':
					case 'T':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'l') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case ':':
									found = ofs + 5;
									offset = 51; // ('[tel:', 51)
									break;
								case 'n':
								case 'N':
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 't' && src[ofs+7] == ':') {
										found = ofs + 8;
										offset = 43; // ('[telnet:', 43)
									}
									break;
							}
						}
						break;
					case 'u':
					case 'U':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'n' && src[ofs+4] == ':') {
							found = ofs + 5;
							offset = 65; // ('[urn:', 65)
						}
						break;
					case 'w':
					case 'W':
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'w' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'd' && src[ofs+10] == ':') {
							found = ofs + 11;
							offset = 47; // ('[worldwind:', 47)
						}
						break;
					case 'x':
					case 'X':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'm':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'p' && src[ofs+5] == ':') {
									found = ofs + 6;
									offset = 35; // ('[xmpp:', 35)
								}
								break;
							case 'o':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'a' && src[ofs+5] == '-' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'm' && (src[ofs+8] | 32) == 'd') {
									found = ofs + 9;
									offset = 71; // ('[xowa-cmd', 71)
								}
								break;
						}
						break;
				}
				break;
			case ']':
				if (ofs+1 < src_len && src[ofs+1] == ']') {
					found = ofs + 2;
					offset = 10; // (']]', 10)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 72; // (']', 72)
				}
				break;
			case '_':
				if (ofs+1 < src_len && src[ofs+1] == '_') {
					found = ofs + 2;
					offset = 84; // ('__', 84)
				}
				break;
			case 'b':
			case 'B':
				if (ofs+6 < src_len && (src[ofs+1] | 32) == 'i' && (src[ofs+2] | 32) == 'c' && (src[ofs+3] | 32) == 'o' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 'n' && src[ofs+6] == ':') {
					found = ofs + 7;
					offset = 60; // ('bicoin:', 60)
				}
				break;
			case 'f':
			case 'F':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 't' && (src[ofs+2] | 32) == 'p') {
					if (ofs+3 < src_len) switch (src[ofs+3]) {
						case ':':
							found = ofs + 4;
							offset = 22; // ('ftp:', 22)
							break;
						case 's':
						case 'S':
							if (ofs+4 < src_len && src[ofs+4] == ':') {
								found = ofs + 5;
								offset = 24; // ('ftps:', 24)
							}
							break;
					}
				}
				break;
			case 'g':
			case 'G':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'e':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'o' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 66; // ('geo:', 66)
						}
						break;
					case 'i':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 56; // ('git:', 56)
						}
						break;
					case 'o':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'p' && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r' && src[ofs+6] == ':') {
							found = ofs + 7;
							offset = 40; // ('gopher:', 40)
						}
						break;
				}
				break;
			case 'h':
			case 'H':
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 't' && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p') {
					if (ofs+4 < src_len) switch (src[ofs+4]) {
						case ':':
							found = ofs + 5;
							offset = 18; // ('http:', 18)
							break;
						case 's':
						case 'S':
							if (ofs+5 < src_len && src[ofs+5] == ':') {
								found = ofs + 6;
								offset = 20; // ('https:', 20)
							}
							break;
					}
				}
				break;
			case 'i':
			case 'I':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'r' && (src[ofs+2] | 32) == 'c') {
					if (ofs+3 < src_len) switch (src[ofs+3]) {
						case ':':
							found = ofs + 4;
							offset = 30; // ('irc:', 30)
							break;
						case 's':
						case 'S':
							if (ofs+4 < src_len && src[ofs+4] == ':') {
								found = ofs + 5;
								offset = 32; // ('ircs:', 32)
							}
							break;
					}
				}
				break;
			case 'm':
			case 'M':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'a':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'g':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'n' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 't' && src[ofs+6] == ':') {
									found = ofs + 7;
									offset = 62; // ('magnet:', 62)
								}
								break;
							case 'i':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'o' && src[ofs+6] == ':') {
									found = ofs + 7;
									offset = 48; // ('mailto:', 48)
								}
								break;
						}
						break;
					case 'm':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 's' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 58; // ('mms:', 58)
						}
						break;
				}
				break;
			case 'n':
			case 'N':
				if (ofs+4 < src_len && (src[ofs+1] | 32) == 'n' && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p' && src[ofs+4] == ':') {
					found = ofs + 5;
					offset = 44; // ('nntp:', 44)
				}
				break;
			case 's':
			case 'S':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'f':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p' && src[ofs+4] == ':') {
							found = ofs + 5;
							offset = 28; // ('sftp:', 28)
						}
						break;
					case 'i':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'p') {
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case ':':
									found = ofs + 4;
									offset = 36; // ('sip:', 36)
									break;
								case 's':
								case 'S':
									if (ofs+4 < src_len && src[ofs+4] == ':') {
										found = ofs + 5;
										offset = 38; // ('sips:', 38)
									}
									break;
							}
						}
						break;
					case 'm':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 's' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 52; // ('sms:', 52)
						}
						break;
					case 's':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'h' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 26; // ('ssh:', 26)
						}
						break;
					case 'v':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'n' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 54; // ('svn:', 54)
						}
						break;
				}
				break;
			case 't':
			case 'T':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 'e' && (src[ofs+2] | 32) == 'l') {
					if (ofs+3 < src_len) switch (src[ofs+3]) {
						case ':':
							found = ofs + 4;
							offset = 50; // ('tel:', 50)
							break;
						case 'n':
						case 'N':
							if (ofs+6 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 't' && src[ofs+6] == ':') {
								found = ofs + 7;
								offset = 42; // ('telnet:', 42)
							}
							break;
					}
				}
				break;
			case 'u':
			case 'U':
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 'r' && (src[ofs+2] | 32) == 'n' && src[ofs+3] == ':') {
					found = ofs + 4;
					offset = 64; // ('urn:', 64)
				}
				break;
			case 'w':
			case 'W':
				if (ofs+9 < src_len && (src[ofs+1] | 32) == 'o' && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'w' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'd' && src[ofs+9] == ':') {
					found = ofs + 10;
					offset = 46; // ('worldwind:', 46)
				}
				break;
			case 'x':
			case 'X':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'm':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'p' && (src[ofs+3] | 32) == 'p' && src[ofs+4] == ':') {
							found = ofs + 5;
							offset = 34; // ('xmpp:', 34)
						}
						break;
					case 'o':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'w' && (src[ofs+3] | 32) == 'a' && src[ofs+4] == '-' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'd') {
							found = ofs + 8;
							offset = 70; // ('xowa-cmd', 70)
						}
						break;
				}
				break;
			case '|':
				if (ofs+1 < src_len && src[ofs+1] == '|') {
					found = ofs + 2;
					offset = 79; // ('||', 79)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 0; // ('|', 0)
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