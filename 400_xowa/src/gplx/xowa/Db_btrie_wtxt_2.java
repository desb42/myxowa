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
public class Db_btrie_wtxt_2 implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_wtxt_2(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("27cd78abc8d67b38e1c15b444e559abc"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;

		switch (b) {
			case 9:
				found = ofs + 1;
				offset = 3; // ('\t', 3)
				break;
			case 10:
				found = ofs + 1;
				offset = 5; // ('\n', 5)
				break;
			case ' ':
				found = ofs + 1;
				offset = 2; // (' ', 2)
				break;
			case '&':
				if (ofs+4 < src_len && src[ofs+1] == '#' && src[ofs+2] == '0' && src[ofs+3] == '9' && src[ofs+4] == ';') {
					found = ofs + 5;
					offset = 4; // ('&#09;', 4)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 8; // ('&', 8)
				}
				break;
			case '\'':
				if (ofs+1 < src_len && src[ofs+1] == '\'') {
					found = ofs + 2;
					offset = 10; // ("''", 10)
				}
				break;
			case ':':
				found = ofs + 1;
				offset = 9; // (':', 9)
				break;
			case '<':
				found = ofs + 1;
				offset = 70; // ('<', 70)
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
							offset = 65; // ('[//', 65)
						}
						break;
					case '[':
						if (ofs+3 < src_len && src[ofs+2] == '/' && src[ofs+3] == '/') {
							found = ofs + 4;
							offset = 66; // ('[[//', 66)
						}
						if (found == -1) {
							found = ofs + 2;
							offset = 11; // ('[[', 11)
						}
						break;
					case 'b':
					case 'B':
						if (ofs+8 < src_len && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'c' && (src[ofs+5] | 32) == 'o' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && src[ofs+8] == ':') {
							found = ofs + 9;
							offset = 56; // ('[bitcoin:', 56)
						}
						break;
					case 'f':
					case 'F':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case ':':
									found = ofs + 5;
									offset = 18; // ('[ftp:', 18)
									break;
								case 's':
								case 'S':
									if (ofs+5 < src_len && src[ofs+5] == ':') {
										found = ofs + 6;
										offset = 20; // ('[ftps:', 20)
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
									offset = 62; // ('[geo:', 62)
								}
								break;
							case 'i':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 't' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 52; // ('[git:', 52)
								}
								break;
							case 'o':
								if (ofs+7 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'h' && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 'r' && src[ofs+7] == ':') {
									found = ofs + 8;
									offset = 36; // ('[gopher:', 36)
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
									offset = 14; // ('[http:', 14)
									break;
								case 's':
								case 'S':
									if (ofs+6 < src_len && src[ofs+6] == ':') {
										found = ofs + 7;
										offset = 16; // ('[https:', 16)
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
									offset = 26; // ('[irc:', 26)
									break;
								case 's':
								case 'S':
									if (ofs+5 < src_len && src[ofs+5] == ':') {
										found = ofs + 6;
										offset = 28; // ('[ircs:', 28)
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
											offset = 58; // ('[magnet:', 58)
										}
										break;
									case 'i':
										if (ofs+7 < src_len && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 't' && (src[ofs+6] | 32) == 'o' && src[ofs+7] == ':') {
											found = ofs + 8;
											offset = 44; // ('[mailto:', 44)
										}
										break;
								}
								break;
							case 'm':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 's' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 54; // ('[mms:', 54)
								}
								break;
						}
						break;
					case 'n':
					case 'N':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'n' && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p' && src[ofs+5] == ':') {
							found = ofs + 6;
							offset = 40; // ('[nntp:', 40)
						}
						break;
					case 'r':
					case 'R':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'e' && (src[ofs+3] | 32) == 'd' && (src[ofs+4] | 32) == 'i' && (src[ofs+5] | 32) == 's' && src[ofs+6] == ':') {
							found = ofs + 7;
							offset = 64; // ('[redis:', 64)
						}
						break;
					case 's':
					case 'S':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'f':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 't' && (src[ofs+4] | 32) == 'p' && src[ofs+5] == ':') {
									found = ofs + 6;
									offset = 24; // ('[sftp:', 24)
								}
								break;
							case 'i':
								if (ofs+3 < src_len && (src[ofs+3] | 32) == 'p') {
									if (ofs+4 < src_len) switch (src[ofs+4]) {
										case ':':
											found = ofs + 5;
											offset = 32; // ('[sip:', 32)
											break;
										case 's':
										case 'S':
											if (ofs+5 < src_len && src[ofs+5] == ':') {
												found = ofs + 6;
												offset = 34; // ('[sips:', 34)
											}
											break;
									}
								}
								break;
							case 'm':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 's' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 48; // ('[sms:', 48)
								}
								break;
							case 's':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'h' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 22; // ('[ssh:', 22)
								}
								break;
							case 'v':
								if (ofs+4 < src_len && (src[ofs+3] | 32) == 'n' && src[ofs+4] == ':') {
									found = ofs + 5;
									offset = 50; // ('[svn:', 50)
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
									offset = 46; // ('[tel:', 46)
									break;
								case 'n':
								case 'N':
									if (ofs+7 < src_len && (src[ofs+5] | 32) == 'e' && (src[ofs+6] | 32) == 't' && src[ofs+7] == ':') {
										found = ofs + 8;
										offset = 38; // ('[telnet:', 38)
									}
									break;
							}
						}
						break;
					case 'u':
					case 'U':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'n' && src[ofs+4] == ':') {
							found = ofs + 5;
							offset = 60; // ('[urn:', 60)
						}
						break;
					case 'w':
					case 'W':
						if (ofs+10 < src_len && (src[ofs+2] | 32) == 'o' && (src[ofs+3] | 32) == 'r' && (src[ofs+4] | 32) == 'l' && (src[ofs+5] | 32) == 'd' && (src[ofs+6] | 32) == 'w' && (src[ofs+7] | 32) == 'i' && (src[ofs+8] | 32) == 'n' && (src[ofs+9] | 32) == 'd' && src[ofs+10] == ':') {
							found = ofs + 11;
							offset = 42; // ('[worldwind:', 42)
						}
						break;
					case 'x':
					case 'X':
						if (ofs+2 < src_len) switch ((src[ofs+2] | 32)) {
							case 'm':
								if (ofs+5 < src_len && (src[ofs+3] | 32) == 'p' && (src[ofs+4] | 32) == 'p' && src[ofs+5] == ':') {
									found = ofs + 6;
									offset = 30; // ('[xmpp:', 30)
								}
								break;
							case 'o':
								if (ofs+8 < src_len && (src[ofs+3] | 32) == 'w' && (src[ofs+4] | 32) == 'a' && src[ofs+5] == '-' && (src[ofs+6] | 32) == 'c' && (src[ofs+7] | 32) == 'm' && (src[ofs+8] | 32) == 'd') {
									found = ofs + 9;
									offset = 68; // ('[xowa-cmd', 68)
								}
								break;
						}
						break;
				}
				break;
			case ']':
				if (ofs+1 < src_len && src[ofs+1] == ']') {
					found = ofs + 2;
					offset = 12; // (']]', 12)
				}
				if (found == -1) {
					found = ofs + 1;
					offset = 69; // (']', 69)
				}
				break;
			case 'b':
			case 'B':
				if (ofs+7 < src_len && (src[ofs+1] | 32) == 'i' && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'c' && (src[ofs+4] | 32) == 'o' && (src[ofs+5] | 32) == 'i' && (src[ofs+6] | 32) == 'n' && src[ofs+7] == ':') {
					found = ofs + 8;
					offset = 55; // ('bitcoin:', 55)
				}
				break;
			case 'f':
			case 'F':
				if (ofs+2 < src_len && (src[ofs+1] | 32) == 't' && (src[ofs+2] | 32) == 'p') {
					if (ofs+3 < src_len) switch (src[ofs+3]) {
						case ':':
							found = ofs + 4;
							offset = 17; // ('ftp:', 17)
							break;
						case 's':
						case 'S':
							if (ofs+4 < src_len && src[ofs+4] == ':') {
								found = ofs + 5;
								offset = 19; // ('ftps:', 19)
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
							offset = 61; // ('geo:', 61)
						}
						break;
					case 'i':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 't' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 51; // ('git:', 51)
						}
						break;
					case 'o':
						if (ofs+6 < src_len && (src[ofs+2] | 32) == 'p' && (src[ofs+3] | 32) == 'h' && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 'r' && src[ofs+6] == ':') {
							found = ofs + 7;
							offset = 35; // ('gopher:', 35)
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
							offset = 13; // ('http:', 13)
							break;
						case 's':
						case 'S':
							if (ofs+5 < src_len && src[ofs+5] == ':') {
								found = ofs + 6;
								offset = 15; // ('https:', 15)
							}
							break;
					}
				}
				break;
			case 'i':
			case 'I':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'r':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'c') {
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case ':':
									found = ofs + 4;
									offset = 25; // ('irc:', 25)
									break;
								case 's':
								case 'S':
									if (ofs+4 < src_len && src[ofs+4] == ':') {
										found = ofs + 5;
										offset = 27; // ('ircs:', 27)
									}
									break;
							}
						}
						break;
					case 's':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'b' && (src[ofs+3] | 32) == 'n' && src[ofs+4] == ' ') {
							found = ofs + 5;
							offset = 71; // ('ISBN ', 71)
						}
						break;
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
									offset = 57; // ('magnet:', 57)
								}
								break;
							case 'i':
								if (ofs+6 < src_len && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 't' && (src[ofs+5] | 32) == 'o' && src[ofs+6] == ':') {
									found = ofs + 7;
									offset = 43; // ('mailto:', 43)
								}
								break;
						}
						break;
					case 'm':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 's' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 53; // ('mms:', 53)
						}
						break;
				}
				break;
			case 'n':
			case 'N':
				if (ofs+4 < src_len && (src[ofs+1] | 32) == 'n' && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p' && src[ofs+4] == ':') {
					found = ofs + 5;
					offset = 39; // ('nntp:', 39)
				}
				break;
			case 'p':
			case 'P':
				if (ofs+4 < src_len && (src[ofs+1] | 32) == 'm' && (src[ofs+2] | 32) == 'i' && (src[ofs+3] | 32) == 'd' && src[ofs+4] == ' ') {
					found = ofs + 5;
					offset = 72; // ('PMID ', 72)
				}
				break;
			case 'r':
			case 'R':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'e':
						if (ofs+5 < src_len && (src[ofs+2] | 32) == 'd' && (src[ofs+3] | 32) == 'i' && (src[ofs+4] | 32) == 's' && src[ofs+5] == ':') {
							found = ofs + 6;
							offset = 63; // ('redis:', 63)
						}
						break;
					case 'f':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'c' && src[ofs+3] == ' ') {
							found = ofs + 4;
							offset = 73; // ('RFC ', 73)
						}
						break;
				}
				break;
			case 's':
			case 'S':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'f':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 't' && (src[ofs+3] | 32) == 'p' && src[ofs+4] == ':') {
							found = ofs + 5;
							offset = 23; // ('sftp:', 23)
						}
						break;
					case 'i':
						if (ofs+2 < src_len && (src[ofs+2] | 32) == 'p') {
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case ':':
									found = ofs + 4;
									offset = 31; // ('sip:', 31)
									break;
								case 's':
								case 'S':
									if (ofs+4 < src_len && src[ofs+4] == ':') {
										found = ofs + 5;
										offset = 33; // ('sips:', 33)
									}
									break;
							}
						}
						break;
					case 'm':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 's' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 47; // ('sms:', 47)
						}
						break;
					case 's':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'h' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 21; // ('ssh:', 21)
						}
						break;
					case 'v':
						if (ofs+3 < src_len && (src[ofs+2] | 32) == 'n' && src[ofs+3] == ':') {
							found = ofs + 4;
							offset = 49; // ('svn:', 49)
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
							offset = 45; // ('tel:', 45)
							break;
						case 'n':
						case 'N':
							if (ofs+6 < src_len && (src[ofs+4] | 32) == 'e' && (src[ofs+5] | 32) == 't' && src[ofs+6] == ':') {
								found = ofs + 7;
								offset = 37; // ('telnet:', 37)
							}
							break;
					}
				}
				break;
			case 'u':
			case 'U':
				if (ofs+3 < src_len && (src[ofs+1] | 32) == 'r' && (src[ofs+2] | 32) == 'n' && src[ofs+3] == ':') {
					found = ofs + 4;
					offset = 59; // ('urn:', 59)
				}
				break;
			case 'w':
			case 'W':
				if (ofs+9 < src_len && (src[ofs+1] | 32) == 'o' && (src[ofs+2] | 32) == 'r' && (src[ofs+3] | 32) == 'l' && (src[ofs+4] | 32) == 'd' && (src[ofs+5] | 32) == 'w' && (src[ofs+6] | 32) == 'i' && (src[ofs+7] | 32) == 'n' && (src[ofs+8] | 32) == 'd' && src[ofs+9] == ':') {
					found = ofs + 10;
					offset = 41; // ('worldwind:', 41)
				}
				break;
			case 'x':
			case 'X':
				if (ofs+1 < src_len) switch ((src[ofs+1] | 32)) {
					case 'm':
						if (ofs+4 < src_len && (src[ofs+2] | 32) == 'p' && (src[ofs+3] | 32) == 'p' && src[ofs+4] == ':') {
							found = ofs + 5;
							offset = 29; // ('xmpp:', 29)
						}
						break;
					case 'o':
						if (ofs+7 < src_len && (src[ofs+2] | 32) == 'w' && (src[ofs+3] | 32) == 'a' && src[ofs+4] == '-' && (src[ofs+5] | 32) == 'c' && (src[ofs+6] | 32) == 'm' && (src[ofs+7] | 32) == 'd') {
							found = ofs + 8;
							offset = 67; // ('xowa-cmd', 67)
						}
						break;
				}
				break;
			case '{':
				if (ofs+1 < src_len && src[ofs+1] == '{') {
					found = ofs + 2;
					offset = 6; // ('{{', 6)
				}
				break;
			case '|':
				found = ofs + 1;
				offset = 0; // ('|', 0)
				break;
			case '}':
				if (ofs+1 < src_len && src[ofs+1] == '}') {
					found = ofs + 2;
					offset = 7; // ('}}', 7)
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
