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
public class Db_btrie_cs_trie_en implements Db_btrie {
	private final Object[] objs;
	public Db_btrie_cs_trie_en(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("21ea4dc6570900e3ec30f7812e452c3b"); }
	private Db_btrie_result Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		int found = -1;
		int offset = -1;

		switch (b) {
			case '!':
				found = ofs + 1;
				offset = 87; // ('!', 87)
				break;
			case '#':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'c':
						if (ofs+5 < src_len && src[ofs+2] == 'o' && src[ofs+3] == 'u' && src[ofs+4] == 'n' && src[ofs+5] == 't') {
							found = ofs + 6;
							offset = 97; // ('#count', 97)
						}
						break;
					case 'e':
						if (ofs+7 < src_len && src[ofs+2] == 'x' && src[ofs+3] == 'p' && src[ofs+4] == 'l' && src[ofs+5] == 'o' && src[ofs+6] == 'd' && src[ofs+7] == 'e') {
							found = ofs + 8;
							offset = 99; // ('#explode', 99)
						}
						break;
					case 'l':
						if (ofs+3 < src_len && src[ofs+2] == 'e' && src[ofs+3] == 'n') {
							found = ofs + 4;
							offset = 93; // ('#len', 93)
						}
						break;
					case 'p':
						if (ofs+3 < src_len && src[ofs+2] == 'o' && src[ofs+3] == 's') {
							found = ofs + 4;
							offset = 94; // ('#pos', 94)
						}
						break;
					case 'r':
						if (ofs+2 < src_len) switch (src[ofs+2]) {
							case 'e':
								if (ofs+7 < src_len && src[ofs+3] == 'p' && src[ofs+4] == 'l' && src[ofs+5] == 'a' && src[ofs+6] == 'c' && src[ofs+7] == 'e') {
									found = ofs + 8;
									offset = 98; // ('#replace', 98)
								}
								break;
							case 'p':
								if (ofs+4 < src_len && src[ofs+3] == 'o' && src[ofs+4] == 's') {
									found = ofs + 5;
									offset = 95; // ('#rpos', 95)
								}
								break;
						}
						break;
					case 's':
						if (ofs+3 < src_len && src[ofs+2] == 'u' && src[ofs+3] == 'b') {
							found = ofs + 4;
							offset = 96; // ('#sub', 96)
						}
						break;
					case 'u':
						if (ofs+9 < src_len && src[ofs+2] == 'r' && src[ofs+3] == 'l' && src[ofs+4] == 'd' && src[ofs+5] == 'e' && src[ofs+6] == 'c' && src[ofs+7] == 'o' && src[ofs+8] == 'd' && src[ofs+9] == 'e') {
							found = ofs + 10;
							offset = 100; // ('#urldecode', 100)
						}
						break;
				}
				break;
			case 'A':
				if (ofs+6 < src_len && src[ofs+1] == 'R' && src[ofs+2] == 'T' && src[ofs+3] == 'I' && src[ofs+4] == 'C' && src[ofs+5] == 'L' && src[ofs+6] == 'E') {
					if (ofs+7 < src_len) switch (src[ofs+7]) {
						case 'P':
							if (ofs+8 < src_len && src[ofs+8] == 'A') {
								if (ofs+9 < src_len) switch (src[ofs+9]) {
									case 'G':
										if (ofs+14 < src_len && src[ofs+10] == 'E' && src[ofs+11] == 'N' && src[ofs+12] == 'A' && src[ofs+13] == 'M' && src[ofs+14] == 'E') {
											if (ofs+15 < src_len && src[ofs+15] == 'E') {
												found = ofs + 16;
												offset = 55; // ('ARTICLEPAGENAMEE', 55)
											}
											if (found == -1) {
												found = ofs + 15;
												offset = 53; // ('ARTICLEPAGENAME', 53)
											}
										}
										break;
									case 'T':
										if (ofs+10 < src_len && src[ofs+10] == 'H') {
											found = ofs + 11;
											offset = 61; // ('ARTICLEPATH', 61)
										}
										break;
								}
							}
							break;
						case 'S':
							if (ofs+11 < src_len && src[ofs+8] == 'P' && src[ofs+9] == 'A' && src[ofs+10] == 'C' && src[ofs+11] == 'E') {
								if (ofs+12 < src_len && src[ofs+12] == 'E') {
									found = ofs + 13;
									offset = 41; // ('ARTICLESPACEE', 41)
								}
								if (found == -1) {
									found = ofs + 12;
									offset = 39; // ('ARTICLESPACE', 39)
								}
							}
							break;
					}
				}
				break;
			case 'B':
				if (ofs+11 < src_len && src[ofs+1] == 'A' && src[ofs+2] == 'S' && src[ofs+3] == 'E' && src[ofs+4] == 'P' && src[ofs+5] == 'A' && src[ofs+6] == 'G' && src[ofs+7] == 'E' && src[ofs+8] == 'N' && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
					if (ofs+12 < src_len && src[ofs+12] == 'E') {
						found = ofs + 13;
						offset = 49; // ('BASEPAGENAMEE', 49)
					}
					if (found == -1) {
						found = ofs + 12;
						offset = 47; // ('BASEPAGENAME', 47)
					}
				}
				break;
			case 'C':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+15 < src_len && src[ofs+2] == 'S' && src[ofs+3] == 'C' && src[ofs+4] == 'A' && src[ofs+5] == 'D' && src[ofs+6] == 'I' && src[ofs+7] == 'N' && src[ofs+8] == 'G' && src[ofs+9] == 'S' && src[ofs+10] == 'O' && src[ofs+11] == 'U' && src[ofs+12] == 'R' && src[ofs+13] == 'C' && src[ofs+14] == 'E' && src[ofs+15] == 'S') {
							found = ofs + 16;
							offset = 102; // ('CASCADINGSOURCES', 102)
						}
						break;
					case 'O':
						if (ofs+10 < src_len && src[ofs+2] == 'N' && src[ofs+3] == 'T' && src[ofs+4] == 'E' && src[ofs+5] == 'N' && src[ofs+6] == 'T' && src[ofs+7] == 'L' && src[ofs+8] == 'A' && src[ofs+9] == 'N' && src[ofs+10] == 'G') {
							if (ofs+14 < src_len && src[ofs+11] == 'U' && src[ofs+12] == 'A' && src[ofs+13] == 'G' && src[ofs+14] == 'E') {
								found = ofs + 15;
								offset = 64; // ('CONTENTLANGUAGE', 64)
							}
							if (found == -1) {
								found = ofs + 11;
								offset = 65; // ('CONTENTLANG', 65)
							}
						}
						break;
					case 'U':
						if (ofs+6 < src_len && src[ofs+2] == 'R' && src[ofs+3] == 'R' && src[ofs+4] == 'E' && src[ofs+5] == 'N' && src[ofs+6] == 'T') {
							if (ofs+7 < src_len) switch (src[ofs+7]) {
								case 'D':
									if (ofs+8 < src_len) switch (src[ofs+8]) {
										case 'A':
											if (ofs+9 < src_len && src[ofs+9] == 'Y') {
												if (ofs+10 < src_len) switch (src[ofs+10]) {
													case '2':
														found = ofs + 11;
														offset = 4; // ('CURRENTDAY2', 4)
														break;
													case 'N':
														if (ofs+13 < src_len && src[ofs+11] == 'A' && src[ofs+12] == 'M' && src[ofs+13] == 'E') {
															found = ofs + 14;
															offset = 14; // ('CURRENTDAYNAME', 14)
														}
														break;
												}
												if (found == -1) {
													found = ofs + 10;
													offset = 5; // ('CURRENTDAY', 5)
												}
											}
											break;
										case 'O':
											if (ofs+9 < src_len && src[ofs+9] == 'W') {
												found = ofs + 10;
												offset = 10; // ('CURRENTDOW', 10)
											}
											break;
									}
									break;
								case 'H':
									if (ofs+10 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'U' && src[ofs+10] == 'R') {
										found = ofs + 11;
										offset = 6; // ('CURRENTHOUR', 6)
									}
									break;
								case 'M':
									if (ofs+11 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'N' && src[ofs+10] == 'T' && src[ofs+11] == 'H') {
										if (ofs+12 < src_len) switch (src[ofs+12]) {
											case '1':
												found = ofs + 13;
												offset = 3; // ('CURRENTMONTH1', 3)
												break;
											case '2':
												found = ofs + 13;
												offset = 2; // ('CURRENTMONTH2', 2)
												break;
											case 'A':
												if (ofs+17 < src_len && src[ofs+13] == 'B' && src[ofs+14] == 'B' && src[ofs+15] == 'R' && src[ofs+16] == 'E' && src[ofs+17] == 'V') {
													found = ofs + 18;
													offset = 11; // ('CURRENTMONTHABBREV', 11)
												}
												break;
											case 'N':
												if (ofs+15 < src_len && src[ofs+13] == 'A' && src[ofs+14] == 'M' && src[ofs+15] == 'E') {
													if (ofs+18 < src_len && src[ofs+16] == 'G' && src[ofs+17] == 'E' && src[ofs+18] == 'N') {
														found = ofs + 19;
														offset = 13; // ('CURRENTMONTHNAMEGEN', 13)
													}
													if (found == -1) {
														found = ofs + 16;
														offset = 12; // ('CURRENTMONTHNAME', 12)
													}
												}
												break;
										}
										if (found == -1) {
											found = ofs + 12;
											offset = 1; // ('CURRENTMONTH', 1)
										}
									}
									break;
								case 'T':
									if (ofs+10 < src_len && src[ofs+8] == 'I' && src[ofs+9] == 'M' && src[ofs+10] == 'E') {
										if (ofs+15 < src_len && src[ofs+11] == 'S' && src[ofs+12] == 'T' && src[ofs+13] == 'A' && src[ofs+14] == 'M' && src[ofs+15] == 'P') {
											found = ofs + 16;
											offset = 8; // ('CURRENTTIMESTAMP', 8)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 7; // ('CURRENTTIME', 7)
										}
									}
									break;
								case 'V':
									if (ofs+13 < src_len && src[ofs+8] == 'E' && src[ofs+9] == 'R' && src[ofs+10] == 'S' && src[ofs+11] == 'I' && src[ofs+12] == 'O' && src[ofs+13] == 'N') {
										found = ofs + 14;
										offset = 68; // ('CURRENTVERSION', 68)
									}
									break;
								case 'W':
									if (ofs+10 < src_len && src[ofs+8] == 'E' && src[ofs+9] == 'E' && src[ofs+10] == 'K') {
										found = ofs + 11;
										offset = 9; // ('CURRENTWEEK', 9)
									}
									break;
								case 'Y':
									if (ofs+10 < src_len && src[ofs+8] == 'E' && src[ofs+9] == 'A' && src[ofs+10] == 'R') {
										found = ofs + 11;
										offset = 0; // ('CURRENTYEAR', 0)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'D':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'E':
						if (ofs+6 < src_len && src[ofs+2] == 'F' && src[ofs+3] == 'A' && src[ofs+4] == 'U' && src[ofs+5] == 'L' && src[ofs+6] == 'T') {
							if (ofs+7 < src_len) switch (src[ofs+7]) {
								case 'C':
									if (ofs+18 < src_len && src[ofs+8] == 'A' && src[ofs+9] == 'T' && src[ofs+10] == 'E' && src[ofs+11] == 'G' && src[ofs+12] == 'O' && src[ofs+13] == 'R' && src[ofs+14] == 'Y' && src[ofs+15] == 'S' && src[ofs+16] == 'O' && src[ofs+17] == 'R' && src[ofs+18] == 'T') {
										found = ofs + 19;
										offset = 84; // ('DEFAULTCATEGORYSORT', 84)
									}
									break;
								case 'S':
									if (ofs+10 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'R' && src[ofs+10] == 'T') {
										if (ofs+13 < src_len && src[ofs+11] == 'K' && src[ofs+12] == 'E' && src[ofs+13] == 'Y') {
											found = ofs + 14;
											offset = 83; // ('DEFAULTSORTKEY', 83)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 82; // ('DEFAULTSORT', 82)
										}
									}
									break;
							}
						}
						break;
					case 'I':
						if (ofs+2 < src_len) switch (src[ofs+2]) {
							case 'R':
								if (ofs+3 < src_len) switch (src[ofs+3]) {
									case 'E':
										if (ofs+12 < src_len && src[ofs+4] == 'C' && src[ofs+5] == 'T' && src[ofs+6] == 'I' && src[ofs+7] == 'O' && src[ofs+8] == 'N' && src[ofs+9] == 'M' && src[ofs+10] == 'A' && src[ofs+11] == 'R' && src[ofs+12] == 'K') {
											found = ofs + 13;
											offset = 66; // ('DIRECTIONMARK', 66)
										}
										break;
									case 'M':
										if (ofs+6 < src_len && src[ofs+4] == 'A' && src[ofs+5] == 'R' && src[ofs+6] == 'K') {
											found = ofs + 7;
											offset = 67; // ('DIRMARK', 67)
										}
										break;
								}
								break;
							case 'S':
								if (ofs+11 < src_len && src[ofs+3] == 'P' && src[ofs+4] == 'L' && src[ofs+5] == 'A' && src[ofs+6] == 'Y' && src[ofs+7] == 'T' && src[ofs+8] == 'I' && src[ofs+9] == 'T' && src[ofs+10] == 'L' && src[ofs+11] == 'E') {
									found = ofs + 12;
									offset = 81; // ('DISPLAYTITLE', 81)
								}
								break;
						}
						break;
				}
				break;
			case 'F':
				if (ofs+11 < src_len && src[ofs+1] == 'U' && src[ofs+2] == 'L' && src[ofs+3] == 'L' && src[ofs+4] == 'P' && src[ofs+5] == 'A' && src[ofs+6] == 'G' && src[ofs+7] == 'E' && src[ofs+8] == 'N' && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
					if (ofs+12 < src_len && src[ofs+12] == 'E') {
						found = ofs + 13;
						offset = 45; // ('FULLPAGENAMEE', 45)
					}
					if (found == -1) {
						found = ofs + 12;
						offset = 44; // ('FULLPAGENAME', 44)
					}
				}
				break;
			case 'L':
				if (ofs+4 < src_len && src[ofs+1] == 'O' && src[ofs+2] == 'C' && src[ofs+3] == 'A' && src[ofs+4] == 'L') {
					if (ofs+5 < src_len) switch (src[ofs+5]) {
						case 'D':
							if (ofs+6 < src_len) switch (src[ofs+6]) {
								case 'A':
									if (ofs+7 < src_len && src[ofs+7] == 'Y') {
										if (ofs+8 < src_len) switch (src[ofs+8]) {
											case '2':
												found = ofs + 9;
												offset = 19; // ('LOCALDAY2', 19)
												break;
											case 'N':
												if (ofs+11 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
													found = ofs + 12;
													offset = 29; // ('LOCALDAYNAME', 29)
												}
												break;
										}
										if (found == -1) {
											found = ofs + 8;
											offset = 20; // ('LOCALDAY', 20)
										}
									}
									break;
								case 'O':
									if (ofs+7 < src_len && src[ofs+7] == 'W') {
										found = ofs + 8;
										offset = 25; // ('LOCALDOW', 25)
									}
									break;
							}
							break;
						case 'H':
							if (ofs+8 < src_len && src[ofs+6] == 'O' && src[ofs+7] == 'U' && src[ofs+8] == 'R') {
								found = ofs + 9;
								offset = 21; // ('LOCALHOUR', 21)
							}
							break;
						case 'M':
							if (ofs+9 < src_len && src[ofs+6] == 'O' && src[ofs+7] == 'N' && src[ofs+8] == 'T' && src[ofs+9] == 'H') {
								if (ofs+10 < src_len) switch (src[ofs+10]) {
									case '1':
										found = ofs + 11;
										offset = 18; // ('LOCALMONTH1', 18)
										break;
									case '2':
										found = ofs + 11;
										offset = 17; // ('LOCALMONTH2', 17)
										break;
									case 'A':
										if (ofs+15 < src_len && src[ofs+11] == 'B' && src[ofs+12] == 'B' && src[ofs+13] == 'R' && src[ofs+14] == 'E' && src[ofs+15] == 'V') {
											found = ofs + 16;
											offset = 26; // ('LOCALMONTHABBREV', 26)
										}
										break;
									case 'N':
										if (ofs+13 < src_len && src[ofs+11] == 'A' && src[ofs+12] == 'M' && src[ofs+13] == 'E') {
											if (ofs+16 < src_len && src[ofs+14] == 'G' && src[ofs+15] == 'E' && src[ofs+16] == 'N') {
												found = ofs + 17;
												offset = 28; // ('LOCALMONTHNAMEGEN', 28)
											}
											if (found == -1) {
												found = ofs + 14;
												offset = 27; // ('LOCALMONTHNAME', 27)
											}
										}
										break;
								}
								if (found == -1) {
									found = ofs + 10;
									offset = 16; // ('LOCALMONTH', 16)
								}
							}
							break;
						case 'T':
							if (ofs+8 < src_len && src[ofs+6] == 'I' && src[ofs+7] == 'M' && src[ofs+8] == 'E') {
								if (ofs+13 < src_len && src[ofs+9] == 'S' && src[ofs+10] == 'T' && src[ofs+11] == 'A' && src[ofs+12] == 'M' && src[ofs+13] == 'P') {
									found = ofs + 14;
									offset = 23; // ('LOCALTIMESTAMP', 23)
								}
								if (found == -1) {
									found = ofs + 9;
									offset = 22; // ('LOCALTIME', 22)
								}
							}
							break;
						case 'W':
							if (ofs+8 < src_len && src[ofs+6] == 'E' && src[ofs+7] == 'E' && src[ofs+8] == 'K') {
								found = ofs + 9;
								offset = 24; // ('LOCALWEEK', 24)
							}
							break;
						case 'Y':
							if (ofs+8 < src_len && src[ofs+6] == 'E' && src[ofs+7] == 'A' && src[ofs+8] == 'R') {
								found = ofs + 9;
								offset = 15; // ('LOCALYEAR', 15)
							}
							break;
					}
				}
				break;
			case 'N':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+8 < src_len && src[ofs+2] == 'M' && src[ofs+3] == 'E' && src[ofs+4] == 'S' && src[ofs+5] == 'P' && src[ofs+6] == 'A' && src[ofs+7] == 'C' && src[ofs+8] == 'E') {
							if (ofs+9 < src_len) switch (src[ofs+9]) {
								case 'E':
									found = ofs + 10;
									offset = 37; // ('NAMESPACEE', 37)
									break;
								case 'N':
									if (ofs+14 < src_len && src[ofs+10] == 'U' && src[ofs+11] == 'M' && src[ofs+12] == 'B' && src[ofs+13] == 'E' && src[ofs+14] == 'R') {
										found = ofs + 15;
										offset = 101; // ('NAMESPACENUMBER', 101)
									}
									break;
							}
							if (found == -1) {
								found = ofs + 9;
								offset = 36; // ('NAMESPACE', 36)
							}
						}
						break;
					case 'U':
						if (ofs+7 < src_len && src[ofs+2] == 'M' && src[ofs+3] == 'B' && src[ofs+4] == 'E' && src[ofs+5] == 'R' && src[ofs+6] == 'O' && src[ofs+7] == 'F') {
							if (ofs+8 < src_len) switch (src[ofs+8]) {
								case 'A':
									if (ofs+9 < src_len) switch (src[ofs+9]) {
										case 'C':
											if (ofs+18 < src_len && src[ofs+10] == 'T' && src[ofs+11] == 'I' && src[ofs+12] == 'V' && src[ofs+13] == 'E' && src[ofs+14] == 'U' && src[ofs+15] == 'S' && src[ofs+16] == 'E' && src[ofs+17] == 'R' && src[ofs+18] == 'S') {
												found = ofs + 19;
												offset = 75; // ('NUMBEROFACTIVEUSERS', 75)
											}
											break;
										case 'D':
											if (ofs+13 < src_len && src[ofs+10] == 'M' && src[ofs+11] == 'I' && src[ofs+12] == 'N' && src[ofs+13] == 'S') {
												found = ofs + 14;
												offset = 76; // ('NUMBEROFADMINS', 76)
											}
											break;
										case 'R':
											if (ofs+15 < src_len && src[ofs+10] == 'T' && src[ofs+11] == 'I' && src[ofs+12] == 'C' && src[ofs+13] == 'L' && src[ofs+14] == 'E' && src[ofs+15] == 'S') {
												found = ofs + 16;
												offset = 70; // ('NUMBEROFARTICLES', 70)
											}
											break;
									}
									break;
								case 'E':
									if (ofs+12 < src_len && src[ofs+9] == 'D' && src[ofs+10] == 'I' && src[ofs+11] == 'T' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 72; // ('NUMBEROFEDITS', 72)
									}
									break;
								case 'F':
									if (ofs+12 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'L' && src[ofs+11] == 'E' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 71; // ('NUMBEROFFILES', 71)
									}
									break;
								case 'P':
									if (ofs+12 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'G' && src[ofs+11] == 'E' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 69; // ('NUMBEROFPAGES', 69)
									}
									break;
								case 'U':
									if (ofs+12 < src_len && src[ofs+9] == 'S' && src[ofs+10] == 'E' && src[ofs+11] == 'R' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 74; // ('NUMBEROFUSERS', 74)
									}
									break;
								case 'V':
									if (ofs+12 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'E' && src[ofs+11] == 'W' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 73; // ('NUMBEROFVIEWS', 73)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'P':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+3 < src_len && src[ofs+2] == 'G' && src[ofs+3] == 'E') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case 'B':
									if (ofs+9 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'N' && src[ofs+7] == 'N' && src[ofs+8] == 'E' && src[ofs+9] == 'R') {
										found = ofs + 10;
										offset = 89; // ('PAGEBANNER', 89)
									}
									break;
								case 'L':
									if (ofs+11 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'N' && src[ofs+7] == 'G' && src[ofs+8] == 'U' && src[ofs+9] == 'A' && src[ofs+10] == 'G' && src[ofs+11] == 'E') {
										found = ofs + 12;
										offset = 91; // ('PAGELANGUAGE', 91)
									}
									break;
								case 'N':
									if (ofs+7 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'M' && src[ofs+7] == 'E') {
										if (ofs+8 < src_len && src[ofs+8] == 'E') {
											found = ofs + 9;
											offset = 48; // ('PAGENAMEE', 48)
										}
										if (found == -1) {
											found = ofs + 8;
											offset = 46; // ('PAGENAME', 46)
										}
									}
									break;
								case 'S':
									if (ofs+5 < src_len && src[ofs+5] == 'I') {
										if (ofs+6 < src_len) switch (src[ofs+6]) {
											case 'N':
												if (ofs+9 < src_len && src[ofs+7] == 'C' && src[ofs+8] == 'A' && src[ofs+9] == 'T') {
													if (ofs+14 < src_len && src[ofs+10] == 'E' && src[ofs+11] == 'G' && src[ofs+12] == 'O' && src[ofs+13] == 'R' && src[ofs+14] == 'Y') {
														found = ofs + 15;
														offset = 85; // ('PAGESINCATEGORY', 85)
													}
													if (found == -1) {
														found = ofs + 10;
														offset = 86; // ('PAGESINCAT', 86)
													}
												}
												break;
											case 'Z':
												if (ofs+7 < src_len && src[ofs+7] == 'E') {
													found = ofs + 8;
													offset = 78; // ('PAGESIZE', 78)
												}
												break;
										}
									}
									break;
							}
						}
						break;
					case 'R':
						if (ofs+9 < src_len && src[ofs+2] == 'O' && src[ofs+3] == 'T' && src[ofs+4] == 'E' && src[ofs+5] == 'C' && src[ofs+6] == 'T' && src[ofs+7] == 'I' && src[ofs+8] == 'O' && src[ofs+9] == 'N') {
							if (ofs+10 < src_len) switch (src[ofs+10]) {
								case 'E':
									if (ofs+15 < src_len && src[ofs+11] == 'X' && src[ofs+12] == 'P' && src[ofs+13] == 'I' && src[ofs+14] == 'R' && src[ofs+15] == 'Y') {
										found = ofs + 16;
										offset = 90; // ('PROTECTIONEXPIRY', 90)
									}
									break;
								case 'L':
									if (ofs+14 < src_len && src[ofs+11] == 'E' && src[ofs+12] == 'V' && src[ofs+13] == 'E' && src[ofs+14] == 'L') {
										found = ofs + 15;
										offset = 80; // ('PROTECTIONLEVEL', 80)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'R':
				if (ofs+7 < src_len && src[ofs+1] == 'E' && src[ofs+2] == 'V' && src[ofs+3] == 'I' && src[ofs+4] == 'S' && src[ofs+5] == 'I' && src[ofs+6] == 'O' && src[ofs+7] == 'N') {
					if (ofs+8 < src_len) switch (src[ofs+8]) {
						case 'D':
							if (ofs+10 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'Y') {
								if (ofs+11 < src_len && src[ofs+11] == '2') {
									found = ofs + 12;
									offset = 33; // ('REVISIONDAY2', 33)
								}
								if (found == -1) {
									found = ofs + 11;
									offset = 34; // ('REVISIONDAY', 34)
								}
							}
							break;
						case 'I':
							if (ofs+9 < src_len && src[ofs+9] == 'D') {
								found = ofs + 10;
								offset = 77; // ('REVISIONID', 77)
							}
							break;
						case 'M':
							if (ofs+12 < src_len && src[ofs+9] == 'O' && src[ofs+10] == 'N' && src[ofs+11] == 'T' && src[ofs+12] == 'H') {
								if (ofs+13 < src_len && src[ofs+13] == '1') {
									found = ofs + 14;
									offset = 32; // ('REVISIONMONTH1', 32)
								}
								if (found == -1) {
									found = ofs + 13;
									offset = 31; // ('REVISIONMONTH', 31)
								}
							}
							break;
						case 'S':
							if (ofs+11 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'Z' && src[ofs+11] == 'E') {
								found = ofs + 12;
								offset = 88; // ('REVISIONSIZE', 88)
							}
							break;
						case 'T':
							if (ofs+16 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'M' && src[ofs+11] == 'E' && src[ofs+12] == 'S' && src[ofs+13] == 'T' && src[ofs+14] == 'A' && src[ofs+15] == 'M' && src[ofs+16] == 'P') {
								found = ofs + 17;
								offset = 35; // ('REVISIONTIMESTAMP', 35)
							}
							break;
						case 'U':
							if (ofs+11 < src_len && src[ofs+9] == 'S' && src[ofs+10] == 'E' && src[ofs+11] == 'R') {
								found = ofs + 12;
								offset = 79; // ('REVISIONUSER', 79)
							}
							break;
						case 'Y':
							if (ofs+11 < src_len && src[ofs+9] == 'E' && src[ofs+10] == 'A' && src[ofs+11] == 'R') {
								found = ofs + 12;
								offset = 30; // ('REVISIONYEAR', 30)
							}
							break;
					}
				}
				break;
			case 'S':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'C':
						if (ofs+9 < src_len && src[ofs+2] == 'R' && src[ofs+3] == 'I' && src[ofs+4] == 'P' && src[ofs+5] == 'T' && src[ofs+6] == 'P' && src[ofs+7] == 'A' && src[ofs+8] == 'T' && src[ofs+9] == 'H') {
							found = ofs + 10;
							offset = 62; // ('SCRIPTPATH', 62)
						}
						break;
					case 'E':
						if (ofs+5 < src_len && src[ofs+2] == 'R' && src[ofs+3] == 'V' && src[ofs+4] == 'E' && src[ofs+5] == 'R') {
							if (ofs+9 < src_len && src[ofs+6] == 'N' && src[ofs+7] == 'A' && src[ofs+8] == 'M' && src[ofs+9] == 'E') {
								found = ofs + 10;
								offset = 59; // ('SERVERNAME', 59)
							}
							if (found == -1) {
								found = ofs + 6;
								offset = 60; // ('SERVER', 60)
							}
						}
						break;
					case 'H':
						if (ofs+8 < src_len && src[ofs+2] == 'O' && src[ofs+3] == 'R' && src[ofs+4] == 'T' && src[ofs+5] == 'D' && src[ofs+6] == 'E' && src[ofs+7] == 'S' && src[ofs+8] == 'C') {
							found = ofs + 9;
							offset = 92; // ('SHORTDESC', 92)
						}
						break;
					case 'I':
						if (ofs+7 < src_len && src[ofs+2] == 'T' && src[ofs+3] == 'E' && src[ofs+4] == 'N' && src[ofs+5] == 'A' && src[ofs+6] == 'M' && src[ofs+7] == 'E') {
							found = ofs + 8;
							offset = 58; // ('SITENAME', 58)
						}
						break;
					case 'T':
						if (ofs+8 < src_len && src[ofs+2] == 'Y' && src[ofs+3] == 'L' && src[ofs+4] == 'E' && src[ofs+5] == 'P' && src[ofs+6] == 'A' && src[ofs+7] == 'T' && src[ofs+8] == 'H') {
							found = ofs + 9;
							offset = 63; // ('STYLEPATH', 63)
						}
						break;
					case 'U':
						if (ofs+2 < src_len && src[ofs+2] == 'B') {
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case 'J':
									if (ofs+6 < src_len && src[ofs+4] == 'E' && src[ofs+5] == 'C' && src[ofs+6] == 'T') {
										if (ofs+7 < src_len) switch (src[ofs+7]) {
											case 'P':
												if (ofs+14 < src_len && src[ofs+8] == 'A' && src[ofs+9] == 'G' && src[ofs+10] == 'E' && src[ofs+11] == 'N' && src[ofs+12] == 'A' && src[ofs+13] == 'M' && src[ofs+14] == 'E') {
													if (ofs+15 < src_len && src[ofs+15] == 'E') {
														found = ofs + 16;
														offset = 54; // ('SUBJECTPAGENAMEE', 54)
													}
													if (found == -1) {
														found = ofs + 15;
														offset = 52; // ('SUBJECTPAGENAME', 52)
													}
												}
												break;
											case 'S':
												if (ofs+11 < src_len && src[ofs+8] == 'P' && src[ofs+9] == 'A' && src[ofs+10] == 'C' && src[ofs+11] == 'E') {
													if (ofs+12 < src_len && src[ofs+12] == 'E') {
														found = ofs + 13;
														offset = 40; // ('SUBJECTSPACEE', 40)
													}
													if (found == -1) {
														found = ofs + 12;
														offset = 38; // ('SUBJECTSPACE', 38)
													}
												}
												break;
										}
									}
									break;
								case 'P':
									if (ofs+10 < src_len && src[ofs+4] == 'A' && src[ofs+5] == 'G' && src[ofs+6] == 'E' && src[ofs+7] == 'N' && src[ofs+8] == 'A' && src[ofs+9] == 'M' && src[ofs+10] == 'E') {
										if (ofs+11 < src_len && src[ofs+11] == 'E') {
											found = ofs + 12;
											offset = 51; // ('SUBPAGENAMEE', 51)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 50; // ('SUBPAGENAME', 50)
										}
									}
									break;
							}
						}
						break;
				}
				break;
			case 'T':
				if (ofs+3 < src_len && src[ofs+1] == 'A' && src[ofs+2] == 'L' && src[ofs+3] == 'K') {
					if (ofs+4 < src_len) switch (src[ofs+4]) {
						case 'P':
							if (ofs+11 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'G' && src[ofs+7] == 'E' && src[ofs+8] == 'N' && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
								if (ofs+12 < src_len && src[ofs+12] == 'E') {
									found = ofs + 13;
									offset = 57; // ('TALKPAGENAMEE', 57)
								}
								if (found == -1) {
									found = ofs + 12;
									offset = 56; // ('TALKPAGENAME', 56)
								}
							}
							break;
						case 'S':
							if (ofs+8 < src_len && src[ofs+5] == 'P' && src[ofs+6] == 'A' && src[ofs+7] == 'C' && src[ofs+8] == 'E') {
								if (ofs+9 < src_len && src[ofs+9] == 'E') {
									found = ofs + 10;
									offset = 43; // ('TALKSPACEE', 43)
								}
								if (found == -1) {
									found = ofs + 9;
									offset = 42; // ('TALKSPACE', 42)
								}
							}
							break;
					}
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
