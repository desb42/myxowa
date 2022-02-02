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
public class Db_btrie_cs_trie_de implements Db_btrie {
	private final Object[] objs;
	private int found;
	private int offset;
	public Db_btrie_cs_trie_de(Object[] objs) {this.objs = objs; }
	public static byte[] Hash() { return Bry_.new_a7("26cfd6b51b84da2b2a87340e201f92fe"); }
	private void Match_with_b(byte b, byte[] src, int ofs, int src_len) {
		found = -1;
		offset = -1;

		switch (b) {
			case '!':
				found = ofs + 1;
				offset = 187; // ('!', 187)
				break;
			case '#':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'e':
						if (ofs+7 < src_len && src[ofs+2] == 'x' && src[ofs+3] == 'p' && src[ofs+4] == 'l' && src[ofs+5] == 'o' && src[ofs+6] == 'd' && src[ofs+7] == 'e') {
							found = ofs + 8;
							offset = 197; // ('#explode', 197)
						}
						break;
					case 'l':
						if (ofs+3 < src_len && src[ofs+2] == 'e' && src[ofs+3] == 'n') {
							found = ofs + 4;
							offset = 193; // ('#len', 193)
						}
						break;
					case 'p':
						if (ofs+3 < src_len && src[ofs+2] == 'o' && src[ofs+3] == 's') {
							found = ofs + 4;
							offset = 194; // ('#pos', 194)
						}
						break;
					case 'r':
						if (ofs+4 < src_len && src[ofs+2] == 'p' && src[ofs+3] == 'o' && src[ofs+4] == 's') {
							found = ofs + 5;
							offset = 195; // ('#rpos', 195)
						}
						break;
					case 's':
						if (ofs+3 < src_len && src[ofs+2] == 'u' && src[ofs+3] == 'b') {
							found = ofs + 4;
							offset = 196; // ('#sub', 196)
						}
						break;
				}
				break;
			case 'A':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'D':
						if (ofs+10 < src_len && src[ofs+2] == 'M' && src[ofs+3] == 'I' && src[ofs+4] == 'N' && src[ofs+5] == 'A' && src[ofs+6] == 'N' && src[ofs+7] == 'Z' && src[ofs+8] == 'A' && src[ofs+9] == 'H' && src[ofs+10] == 'L') {
							found = ofs + 11;
							offset = 159; // ('ADMINANZAHL', 159)
						}
						break;
					case 'K':
						if (ofs+14 < src_len && src[ofs+2] == 'T' && src[ofs+3] == 'I' && src[ofs+4] == 'V' && src[ofs+5] == 'E' && src[ofs+6] == '_' && src[ofs+7] == 'B' && src[ofs+8] == 'E' && src[ofs+9] == 'N' && src[ofs+10] == 'U' && src[ofs+11] == 'T' && src[ofs+12] == 'Z' && src[ofs+13] == 'E' && src[ofs+14] == 'R') {
							found = ofs + 15;
							offset = 157; // ('AKTIVE_BENUTZER', 157)
						}
						break;
					case 'R':
						if (ofs+3 < src_len && src[ofs+2] == 'T' && src[ofs+3] == 'I') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case 'C':
									if (ofs+6 < src_len && src[ofs+5] == 'L' && src[ofs+6] == 'E') {
										if (ofs+7 < src_len) switch (src[ofs+7]) {
											case 'P':
												if (ofs+14 < src_len && src[ofs+8] == 'A' && src[ofs+9] == 'G' && src[ofs+10] == 'E' && src[ofs+11] == 'N' && src[ofs+12] == 'A' && src[ofs+13] == 'M' && src[ofs+14] == 'E') {
													if (ofs+15 < src_len && src[ofs+15] == 'E') {
														found = ofs + 16;
														offset = 127; // ('ARTICLEPAGENAMEE', 127)
													}
													if (found == -1) {
														found = ofs + 15;
														offset = 122; // ('ARTICLEPAGENAME', 122)
													}
												}
												break;
											case 'S':
												if (ofs+11 < src_len && src[ofs+8] == 'P' && src[ofs+9] == 'A' && src[ofs+10] == 'C' && src[ofs+11] == 'E') {
													if (ofs+12 < src_len && src[ofs+12] == 'E') {
														found = ofs + 13;
														offset = 95; // ('ARTICLESPACEE', 95)
													}
													if (found == -1) {
														found = ofs + 12;
														offset = 92; // ('ARTICLESPACE', 92)
													}
												}
												break;
										}
									}
									break;
								case 'K':
									if (ofs+12 < src_len && src[ofs+5] == 'E' && src[ofs+6] == 'L' && src[ofs+7] == 'A' && src[ofs+8] == 'N' && src[ofs+9] == 'Z' && src[ofs+10] == 'A' && src[ofs+11] == 'H' && src[ofs+12] == 'L') {
										found = ofs + 13;
										offset = 148; // ('ARTIKELANZAHL', 148)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'B':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+11 < src_len && src[ofs+2] == 'S' && src[ofs+3] == 'E' && src[ofs+4] == 'P' && src[ofs+5] == 'A' && src[ofs+6] == 'G' && src[ofs+7] == 'E' && src[ofs+8] == 'N' && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
							if (ofs+12 < src_len && src[ofs+12] == 'E') {
								found = ofs + 13;
								offset = 113; // ('BASEPAGENAMEE', 113)
							}
							if (found == -1) {
								found = ofs + 12;
								offset = 109; // ('BASEPAGENAME', 109)
							}
						}
						break;
					case 'E':
						if (ofs+2 < src_len) switch (src[ofs+2]) {
							case 'A':
								if (ofs+17 < src_len && src[ofs+3] == 'R' && src[ofs+4] == 'B' && src[ofs+5] == 'E' && src[ofs+6] == 'I' && src[ofs+7] == 'T' && src[ofs+8] == 'U' && src[ofs+9] == 'N' && src[ofs+10] == 'G' && src[ofs+11] == 'S' && src[ofs+12] == 'A' && src[ofs+13] == 'N' && src[ofs+14] == 'Z' && src[ofs+15] == 'A' && src[ofs+16] == 'H' && src[ofs+17] == 'L') {
									found = ofs + 18;
									offset = 152; // ('BEARBEITUNGSANZAHL', 152)
								}
								break;
							case 'N':
								if (ofs+13 < src_len && src[ofs+3] == 'U' && src[ofs+4] == 'T' && src[ofs+5] == 'Z' && src[ofs+6] == 'E' && src[ofs+7] == 'R' && src[ofs+8] == 'A' && src[ofs+9] == 'N' && src[ofs+10] == 'Z' && src[ofs+11] == 'A' && src[ofs+12] == 'H' && src[ofs+13] == 'L') {
									found = ofs + 14;
									offset = 155; // ('BENUTZERANZAHL', 155)
								}
								break;
						}
						break;
				}
				break;
			case 'C':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+15 < src_len && src[ofs+2] == 'S' && src[ofs+3] == 'C' && src[ofs+4] == 'A' && src[ofs+5] == 'D' && src[ofs+6] == 'I' && src[ofs+7] == 'N' && src[ofs+8] == 'G' && src[ofs+9] == 'S' && src[ofs+10] == 'O' && src[ofs+11] == 'U' && src[ofs+12] == 'R' && src[ofs+13] == 'C' && src[ofs+14] == 'E' && src[ofs+15] == 'S') {
							found = ofs + 16;
							offset = 186; // ('CASCADINGSOURCES', 186)
						}
						break;
					case 'O':
						if (ofs+10 < src_len && src[ofs+2] == 'N' && src[ofs+3] == 'T' && src[ofs+4] == 'E' && src[ofs+5] == 'N' && src[ofs+6] == 'T' && src[ofs+7] == 'L' && src[ofs+8] == 'A' && src[ofs+9] == 'N' && src[ofs+10] == 'G') {
							if (ofs+14 < src_len && src[ofs+11] == 'U' && src[ofs+12] == 'A' && src[ofs+13] == 'G' && src[ofs+14] == 'E') {
								found = ofs + 15;
								offset = 139; // ('CONTENTLANGUAGE', 139)
							}
							if (found == -1) {
								found = ofs + 11;
								offset = 140; // ('CONTENTLANG', 140)
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
														offset = 10; // ('CURRENTDAY2', 10)
														break;
													case 'N':
														if (ofs+13 < src_len && src[ofs+11] == 'A' && src[ofs+12] == 'M' && src[ofs+13] == 'E') {
															found = ofs + 14;
															offset = 33; // ('CURRENTDAYNAME', 33)
														}
														break;
												}
												if (found == -1) {
													found = ofs + 10;
													offset = 13; // ('CURRENTDAY', 13)
												}
											}
											break;
										case 'O':
											if (ofs+9 < src_len && src[ofs+9] == 'W') {
												found = ofs + 10;
												offset = 24; // ('CURRENTDOW', 24)
											}
											break;
									}
									break;
								case 'H':
									if (ofs+10 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'U' && src[ofs+10] == 'R') {
										found = ofs + 11;
										offset = 15; // ('CURRENTHOUR', 15)
									}
									break;
								case 'M':
									if (ofs+11 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'N' && src[ofs+10] == 'T' && src[ofs+11] == 'H') {
										if (ofs+12 < src_len) switch (src[ofs+12]) {
											case '1':
												found = ofs + 13;
												offset = 7; // ('CURRENTMONTH1', 7)
												break;
											case '2':
												found = ofs + 13;
												offset = 5; // ('CURRENTMONTH2', 5)
												break;
											case 'A':
												if (ofs+17 < src_len && src[ofs+13] == 'B' && src[ofs+14] == 'B' && src[ofs+15] == 'R' && src[ofs+16] == 'E' && src[ofs+17] == 'V') {
													found = ofs + 18;
													offset = 26; // ('CURRENTMONTHABBREV', 26)
												}
												break;
											case 'N':
												if (ofs+15 < src_len && src[ofs+13] == 'A' && src[ofs+14] == 'M' && src[ofs+15] == 'E') {
													if (ofs+18 < src_len && src[ofs+16] == 'G' && src[ofs+17] == 'E' && src[ofs+18] == 'N') {
														found = ofs + 19;
														offset = 31; // ('CURRENTMONTHNAMEGEN', 31)
													}
													if (found == -1) {
														found = ofs + 16;
														offset = 28; // ('CURRENTMONTHNAME', 28)
													}
												}
												break;
										}
										if (found == -1) {
											found = ofs + 12;
											offset = 4; // ('CURRENTMONTH', 4)
										}
									}
									break;
								case 'T':
									if (ofs+10 < src_len && src[ofs+8] == 'I' && src[ofs+9] == 'M' && src[ofs+10] == 'E') {
										if (ofs+15 < src_len && src[ofs+11] == 'S' && src[ofs+12] == 'T' && src[ofs+13] == 'A' && src[ofs+14] == 'M' && src[ofs+15] == 'P') {
											found = ofs + 16;
											offset = 19; // ('CURRENTTIMESTAMP', 19)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 17; // ('CURRENTTIME', 17)
										}
									}
									break;
								case 'V':
									if (ofs+13 < src_len && src[ofs+8] == 'E' && src[ofs+9] == 'R' && src[ofs+10] == 'S' && src[ofs+11] == 'I' && src[ofs+12] == 'O' && src[ofs+13] == 'N') {
										found = ofs + 14;
										offset = 145; // ('CURRENTVERSION', 145)
									}
									break;
								case 'W':
									if (ofs+10 < src_len && src[ofs+8] == 'E' && src[ofs+9] == 'E' && src[ofs+10] == 'K') {
										found = ofs + 11;
										offset = 22; // ('CURRENTWEEK', 22)
									}
									break;
								case 'Y':
									if (ofs+10 < src_len && src[ofs+8] == 'E' && src[ofs+9] == 'A' && src[ofs+10] == 'R') {
										found = ofs + 11;
										offset = 1; // ('CURRENTYEAR', 1)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'D':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+10 < src_len && src[ofs+2] == 'T' && src[ofs+3] == 'E' && src[ofs+4] == 'I' && src[ofs+5] == 'A' && src[ofs+6] == 'N' && src[ofs+7] == 'Z' && src[ofs+8] == 'A' && src[ofs+9] == 'H' && src[ofs+10] == 'L') {
							found = ofs + 11;
							offset = 150; // ('DATEIANZAHL', 150)
						}
						break;
					case 'E':
						if (ofs+6 < src_len && src[ofs+2] == 'F' && src[ofs+3] == 'A' && src[ofs+4] == 'U' && src[ofs+5] == 'L' && src[ofs+6] == 'T') {
							if (ofs+7 < src_len) switch (src[ofs+7]) {
								case 'C':
									if (ofs+18 < src_len && src[ofs+8] == 'A' && src[ofs+9] == 'T' && src[ofs+10] == 'E' && src[ofs+11] == 'G' && src[ofs+12] == 'O' && src[ofs+13] == 'R' && src[ofs+14] == 'Y' && src[ofs+15] == 'S' && src[ofs+16] == 'O' && src[ofs+17] == 'R' && src[ofs+18] == 'T') {
										found = ofs + 19;
										offset = 177; // ('DEFAULTCATEGORYSORT', 177)
									}
									break;
								case 'S':
									if (ofs+10 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'R' && src[ofs+10] == 'T') {
										if (ofs+13 < src_len && src[ofs+11] == 'K' && src[ofs+12] == 'E' && src[ofs+13] == 'Y') {
											found = ofs + 14;
											offset = 176; // ('DEFAULTSORTKEY', 176)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 175; // ('DEFAULTSORT', 175)
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
											offset = 142; // ('DIRECTIONMARK', 142)
										}
										break;
									case 'M':
										if (ofs+6 < src_len && src[ofs+4] == 'A' && src[ofs+5] == 'R' && src[ofs+6] == 'K') {
											found = ofs + 7;
											offset = 143; // ('DIRMARK', 143)
										}
										break;
								}
								break;
							case 'S':
								if (ofs+3 < src_len) switch (src[ofs+3]) {
									case 'K':
										if (ofs+4 < src_len) switch (src[ofs+4]) {
											case 'U':
												if (ofs+10 < src_len && src[ofs+5] == 'S' && src[ofs+6] == 'S' && src[ofs+7] == 'I' && src[ofs+8] == 'O' && src[ofs+9] == 'N' && src[ofs+10] == 'S') {
													if (ofs+11 < src_len) switch (src[ofs+11]) {
														case 'N':
															if (ofs+20 < src_len && src[ofs+12] == 'A' && src[ofs+13] == 'M' && src[ofs+14] == 'E' && src[ofs+15] == 'N' && src[ofs+16] == 'S' && src[ofs+17] == 'R' && src[ofs+18] == 'A' && src[ofs+19] == 'U' && src[ofs+20] == 'M') {
																if (ofs+24 < src_len && src[ofs+21] == '_' && src[ofs+22] == 'U' && src[ofs+23] == 'R' && src[ofs+24] == 'L') {
																	found = ofs + 25;
																	offset = 99; // ('DISKUSSIONSNAMENSRAUM_URL', 99)
																}
																if (found == -1) {
																	found = ofs + 21;
																	offset = 96; // ('DISKUSSIONSNAMENSRAUM', 96)
																}
															}
															break;
														case 'S':
															if (ofs+15 < src_len && src[ofs+12] == 'E' && src[ofs+13] == 'I' && src[ofs+14] == 'T' && src[ofs+15] == 'E') {
																if (ofs+19 < src_len && src[ofs+16] == '_' && src[ofs+17] == 'U' && src[ofs+18] == 'R' && src[ofs+19] == 'L') {
																	found = ofs + 20;
																	offset = 131; // ('DISKUSSIONSSEITE_URL', 131)
																}
																if (found == -1) {
																	found = ofs + 16;
																	offset = 128; // ('DISKUSSIONSSEITE', 128)
																}
															}
															break;
													}
												}
												break;
											case '_':
												if (ofs+5 < src_len) switch (src[ofs+5]) {
													case 'N':
														if (ofs+6 < src_len && src[ofs+6] == 'R') {
															if (ofs+10 < src_len && src[ofs+7] == '_' && src[ofs+8] == 'U' && src[ofs+9] == 'R' && src[ofs+10] == 'L') {
																found = ofs + 11;
																offset = 100; // ('DISK_NR_URL', 100)
															}
															if (found == -1) {
																found = ofs + 7;
																offset = 97; // ('DISK_NR', 97)
															}
														}
														break;
													case 'U':
														if (ofs+7 < src_len && src[ofs+6] == 'R' && src[ofs+7] == 'L') {
															found = ofs + 8;
															offset = 132; // ('DISK_URL', 132)
														}
														break;
												}
												break;
										}
										if (found == -1) {
											found = ofs + 4;
											offset = 129; // ('DISK', 129)
										}
										break;
									case 'P':
										if (ofs+11 < src_len && src[ofs+4] == 'L' && src[ofs+5] == 'A' && src[ofs+6] == 'Y' && src[ofs+7] == 'T' && src[ofs+8] == 'I' && src[ofs+9] == 'T' && src[ofs+10] == 'L' && src[ofs+11] == 'E') {
											found = ofs + 12;
											offset = 173; // ('DISPLAYTITLE', 173)
										}
										break;
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
						offset = 105; // ('FULLPAGENAMEE', 105)
					}
					if (found == -1) {
						found = ofs + 12;
						offset = 103; // ('FULLPAGENAME', 103)
					}
				}
				break;
			case 'H':
				if (ofs+4 < src_len && src[ofs+1] == 'A' && src[ofs+2] == 'U' && src[ofs+3] == 'P' && src[ofs+4] == 'T') {
					if (ofs+5 < src_len) switch (src[ofs+5]) {
						case 'N':
							if (ofs+14 < src_len && src[ofs+6] == 'A' && src[ofs+7] == 'M' && src[ofs+8] == 'E' && src[ofs+9] == 'N' && src[ofs+10] == 'S' && src[ofs+11] == 'R' && src[ofs+12] == 'A' && src[ofs+13] == 'U' && src[ofs+14] == 'M') {
								if (ofs+18 < src_len && src[ofs+15] == '_' && src[ofs+16] == 'U' && src[ofs+17] == 'R' && src[ofs+18] == 'L') {
									found = ofs + 19;
									offset = 93; // ('HAUPTNAMENSRAUM_URL', 93)
								}
								if (found == -1) {
									found = ofs + 15;
									offset = 90; // ('HAUPTNAMENSRAUM', 90)
								}
							}
							break;
						case 'S':
							if (ofs+9 < src_len && src[ofs+6] == 'E' && src[ofs+7] == 'I' && src[ofs+8] == 'T' && src[ofs+9] == 'E') {
								if (ofs+10 < src_len) switch (src[ofs+10]) {
									case 'N':
										if (ofs+14 < src_len && src[ofs+11] == 'N' && src[ofs+12] == 'A' && src[ofs+13] == 'M' && src[ofs+14] == 'E') {
											if (ofs+18 < src_len && src[ofs+15] == '_' && src[ofs+16] == 'U' && src[ofs+17] == 'R' && src[ofs+18] == 'L') {
												found = ofs + 19;
												offset = 123; // ('HAUPTSEITENNAME_URL', 123)
											}
											if (found == -1) {
												found = ofs + 15;
												offset = 118; // ('HAUPTSEITENNAME', 118)
											}
										}
										break;
									case '_':
										if (ofs+13 < src_len && src[ofs+11] == 'U' && src[ofs+12] == 'R' && src[ofs+13] == 'L') {
											found = ofs + 14;
											offset = 125; // ('HAUPTSEITE_URL', 125)
										}
										break;
								}
								if (found == -1) {
									found = ofs + 10;
									offset = 120; // ('HAUPTSEITE', 120)
								}
							}
							break;
					}
				}
				break;
			case 'I':
				if (ofs+13 < src_len && src[ofs+1] == 'N' && src[ofs+2] == 'H' && src[ofs+3] == 'A' && src[ofs+4] == 'L' && src[ofs+5] == 'T' && src[ofs+6] == 'S' && src[ofs+7] == 'S' && src[ofs+8] == 'P' && src[ofs+9] == 'R' && src[ofs+10] == 'A' && src[ofs+11] == 'C' && src[ofs+12] == 'H' && src[ofs+13] == 'E') {
					found = ofs + 14;
					offset = 138; // ('INHALTSSPRACHE', 138)
				}
				break;
			case 'J':
				if (ofs+6 < src_len && src[ofs+1] == 'E' && src[ofs+2] == 'T' && src[ofs+3] == 'Z' && src[ofs+4] == 'I' && src[ofs+5] == 'G' && src[ofs+6] == 'E') {
					if (ofs+7 < src_len) switch (src[ofs+7]) {
						case 'R':
							if (ofs+8 < src_len && src[ofs+8] == '_') {
								if (ofs+9 < src_len) switch (src[ofs+9]) {
									case 'K':
										if (ofs+19 < src_len && src[ofs+10] == 'A' && src[ofs+11] == 'L' && src[ofs+12] == 'E' && src[ofs+13] == 'N' && src[ofs+14] == 'D' && src[ofs+15] == 'E' && src[ofs+16] == 'R' && src[ofs+17] == 'T' && src[ofs+18] == 'A' && src[ofs+19] == 'G') {
											if (ofs+21 < src_len && src[ofs+20] == '_' && src[ofs+21] == '2') {
												found = ofs + 22;
												offset = 8; // ('JETZIGER_KALENDERTAG_2', 8)
											}
											if (found == -1) {
												found = ofs + 20;
												offset = 11; // ('JETZIGER_KALENDERTAG', 11)
											}
										}
										break;
									case 'M':
										if (ofs+13 < src_len && src[ofs+10] == 'O' && src[ofs+11] == 'N' && src[ofs+12] == 'A' && src[ofs+13] == 'T') {
											if (ofs+14 < src_len) switch (src[ofs+14]) {
												case 'S':
													if (ofs+18 < src_len && src[ofs+15] == 'N' && src[ofs+16] == 'A' && src[ofs+17] == 'M' && src[ofs+18] == 'E') {
														if (ofs+19 < src_len && src[ofs+19] == '_') {
															if (ofs+20 < src_len) switch (src[ofs+20]) {
																case 'G':
																	if (ofs+22 < src_len && src[ofs+21] == 'E' && src[ofs+22] == 'N') {
																		if (ofs+26 < src_len && src[ofs+23] == 'I' && src[ofs+24] == 'T' && src[ofs+25] == 'I' && src[ofs+26] == 'V') {
																			found = ofs + 27;
																			offset = 29; // ('JETZIGER_MONATSNAME_GENITIV', 29)
																		}
																		if (found == -1) {
																			found = ofs + 23;
																			offset = 30; // ('JETZIGER_MONATSNAME_GEN', 30)
																		}
																	}
																	break;
																case 'K':
																	if (ofs+23 < src_len && src[ofs+21] == 'U' && src[ofs+22] == 'R' && src[ofs+23] == 'Z') {
																		found = ofs + 24;
																		offset = 25; // ('JETZIGER_MONATSNAME_KURZ', 25)
																	}
																	break;
															}
														}
														if (found == -1) {
															found = ofs + 19;
															offset = 27; // ('JETZIGER_MONATSNAME', 27)
														}
													}
													break;
												case '_':
													if (ofs+15 < src_len) switch (src[ofs+15]) {
														case '1':
															found = ofs + 16;
															offset = 6; // ('JETZIGER_MONAT_1', 6)
															break;
														case '2':
															found = ofs + 16;
															offset = 3; // ('JETZIGER_MONAT_2', 3)
															break;
													}
													break;
											}
											if (found == -1) {
												found = ofs + 14;
												offset = 2; // ('JETZIGER_MONAT', 2)
											}
										}
										break;
									case 'T':
										if (ofs+11 < src_len && src[ofs+10] == 'A' && src[ofs+11] == 'G') {
											if (ofs+13 < src_len && src[ofs+12] == '_' && src[ofs+13] == '2') {
												found = ofs + 14;
												offset = 9; // ('JETZIGER_TAG_2', 9)
											}
											if (found == -1) {
												found = ofs + 12;
												offset = 12; // ('JETZIGER_TAG', 12)
											}
										}
										break;
									case 'W':
										if (ofs+17 < src_len && src[ofs+10] == 'O' && src[ofs+11] == 'C' && src[ofs+12] == 'H' && src[ofs+13] == 'E' && src[ofs+14] == 'N' && src[ofs+15] == 'T' && src[ofs+16] == 'A' && src[ofs+17] == 'G') {
											if (ofs+22 < src_len && src[ofs+18] == '_' && src[ofs+19] == 'Z' && src[ofs+20] == 'A' && src[ofs+21] == 'H' && src[ofs+22] == 'L') {
												found = ofs + 23;
												offset = 23; // ('JETZIGER_WOCHENTAG_ZAHL', 23)
											}
											if (found == -1) {
												found = ofs + 18;
												offset = 32; // ('JETZIGER_WOCHENTAG', 32)
											}
										}
										break;
									case 'Z':
										if (ofs+19 < src_len && src[ofs+10] == 'E' && src[ofs+11] == 'I' && src[ofs+12] == 'T' && src[ofs+13] == 'S' && src[ofs+14] == 'T' && src[ofs+15] == 'E' && src[ofs+16] == 'M' && src[ofs+17] == 'P' && src[ofs+18] == 'E' && src[ofs+19] == 'L') {
											found = ofs + 20;
											offset = 18; // ('JETZIGER_ZEITSTEMPEL', 18)
										}
										break;
								}
							}
							break;
						case 'S':
							if (ofs+12 < src_len && src[ofs+8] == '_' && src[ofs+9] == 'J' && src[ofs+10] == 'A' && src[ofs+11] == 'H' && src[ofs+12] == 'R') {
								found = ofs + 13;
								offset = 0; // ('JETZIGES_JAHR', 0)
							}
							break;
						case '_':
							if (ofs+8 < src_len) switch (src[ofs+8]) {
								case 'K':
									if (ofs+20 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'L' && src[ofs+11] == 'E' && src[ofs+12] == 'N' && src[ofs+13] == 'D' && src[ofs+14] == 'E' && src[ofs+15] == 'R' && src[ofs+16] == 'W' && src[ofs+17] == 'O' && src[ofs+18] == 'C' && src[ofs+19] == 'H' && src[ofs+20] == 'E') {
										found = ofs + 21;
										offset = 20; // ('JETZIGE_KALENDERWOCHE', 20)
									}
									break;
								case 'S':
									if (ofs+13 < src_len && src[ofs+9] == 'T' && src[ofs+10] == 'U' && src[ofs+11] == 'N' && src[ofs+12] == 'D' && src[ofs+13] == 'E') {
										found = ofs + 14;
										offset = 14; // ('JETZIGE_STUNDE', 14)
									}
									break;
								case 'U':
									if (ofs+14 < src_len && src[ofs+9] == 'H' && src[ofs+10] == 'R' && src[ofs+11] == 'Z' && src[ofs+12] == 'E' && src[ofs+13] == 'I' && src[ofs+14] == 'T') {
										found = ofs + 15;
										offset = 16; // ('JETZIGE_UHRZEIT', 16)
									}
									break;
								case 'V':
									if (ofs+14 < src_len && src[ofs+9] == 'E' && src[ofs+10] == 'R' && src[ofs+11] == 'S' && src[ofs+12] == 'I' && src[ofs+13] == 'O' && src[ofs+14] == 'N') {
										found = ofs + 15;
										offset = 144; // ('JETZIGE_VERSION', 144)
									}
									break;
								case 'W':
									if (ofs+12 < src_len && src[ofs+9] == 'O' && src[ofs+10] == 'C' && src[ofs+11] == 'H' && src[ofs+12] == 'E') {
										found = ofs + 13;
										offset = 21; // ('JETZIGE_WOCHE', 21)
									}
									break;
							}
							break;
					}
				}
				break;
			case 'K':
				if (ofs+14 < src_len && src[ofs+1] == 'A' && src[ofs+2] == 'S' && src[ofs+3] == 'K' && src[ofs+4] == 'A' && src[ofs+5] == 'D' && src[ofs+6] == 'E' && src[ofs+7] == 'N' && src[ofs+8] == 'Q' && src[ofs+9] == 'U' && src[ofs+10] == 'E' && src[ofs+11] == 'L' && src[ofs+12] == 'L' && src[ofs+13] == 'E' && src[ofs+14] == 'N') {
					found = ofs + 15;
					offset = 185; // ('KASKADENQUELLEN', 185)
				}
				break;
			case 'L':
				if (ofs+1 < src_len && src[ofs+1] == 'O') {
					if (ofs+2 < src_len) switch (src[ofs+2]) {
						case 'C':
							if (ofs+4 < src_len && src[ofs+3] == 'A' && src[ofs+4] == 'L') {
								if (ofs+5 < src_len) switch (src[ofs+5]) {
									case 'D':
										if (ofs+6 < src_len) switch (src[ofs+6]) {
											case 'A':
												if (ofs+7 < src_len && src[ofs+7] == 'Y') {
													if (ofs+8 < src_len) switch (src[ofs+8]) {
														case '2':
															found = ofs + 9;
															offset = 44; // ('LOCALDAY2', 44)
															break;
														case 'N':
															if (ofs+11 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
																found = ofs + 12;
																offset = 67; // ('LOCALDAYNAME', 67)
															}
															break;
													}
													if (found == -1) {
														found = ofs + 8;
														offset = 47; // ('LOCALDAY', 47)
													}
												}
												break;
											case 'O':
												if (ofs+7 < src_len && src[ofs+7] == 'W') {
													found = ofs + 8;
													offset = 58; // ('LOCALDOW', 58)
												}
												break;
										}
										break;
									case 'H':
										if (ofs+8 < src_len && src[ofs+6] == 'O' && src[ofs+7] == 'U' && src[ofs+8] == 'R') {
											found = ofs + 9;
											offset = 49; // ('LOCALHOUR', 49)
										}
										break;
									case 'M':
										if (ofs+9 < src_len && src[ofs+6] == 'O' && src[ofs+7] == 'N' && src[ofs+8] == 'T' && src[ofs+9] == 'H') {
											if (ofs+10 < src_len) switch (src[ofs+10]) {
												case '1':
													found = ofs + 11;
													offset = 41; // ('LOCALMONTH1', 41)
													break;
												case '2':
													found = ofs + 11;
													offset = 39; // ('LOCALMONTH2', 39)
													break;
												case 'A':
													if (ofs+15 < src_len && src[ofs+11] == 'B' && src[ofs+12] == 'B' && src[ofs+13] == 'R' && src[ofs+14] == 'E' && src[ofs+15] == 'V') {
														found = ofs + 16;
														offset = 60; // ('LOCALMONTHABBREV', 60)
													}
													break;
												case 'N':
													if (ofs+13 < src_len && src[ofs+11] == 'A' && src[ofs+12] == 'M' && src[ofs+13] == 'E') {
														if (ofs+16 < src_len && src[ofs+14] == 'G' && src[ofs+15] == 'E' && src[ofs+16] == 'N') {
															found = ofs + 17;
															offset = 65; // ('LOCALMONTHNAMEGEN', 65)
														}
														if (found == -1) {
															found = ofs + 14;
															offset = 62; // ('LOCALMONTHNAME', 62)
														}
													}
													break;
											}
											if (found == -1) {
												found = ofs + 10;
												offset = 38; // ('LOCALMONTH', 38)
											}
										}
										break;
									case 'T':
										if (ofs+8 < src_len && src[ofs+6] == 'I' && src[ofs+7] == 'M' && src[ofs+8] == 'E') {
											if (ofs+13 < src_len && src[ofs+9] == 'S' && src[ofs+10] == 'T' && src[ofs+11] == 'A' && src[ofs+12] == 'M' && src[ofs+13] == 'P') {
												found = ofs + 14;
												offset = 53; // ('LOCALTIMESTAMP', 53)
											}
											if (found == -1) {
												found = ofs + 9;
												offset = 51; // ('LOCALTIME', 51)
											}
										}
										break;
									case 'W':
										if (ofs+8 < src_len && src[ofs+6] == 'E' && src[ofs+7] == 'E' && src[ofs+8] == 'K') {
											found = ofs + 9;
											offset = 56; // ('LOCALWEEK', 56)
										}
										break;
									case 'Y':
										if (ofs+8 < src_len && src[ofs+6] == 'E' && src[ofs+7] == 'A' && src[ofs+8] == 'R') {
											found = ofs + 9;
											offset = 35; // ('LOCALYEAR', 35)
										}
										break;
								}
							}
							break;
						case 'K':
							if (ofs+5 < src_len && src[ofs+3] == 'A' && src[ofs+4] == 'L' && src[ofs+5] == 'E') {
								if (ofs+6 < src_len) switch (src[ofs+6]) {
									case 'R':
										if (ofs+7 < src_len && src[ofs+7] == '_') {
											if (ofs+8 < src_len) switch (src[ofs+8]) {
												case 'K':
													if (ofs+18 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'L' && src[ofs+11] == 'E' && src[ofs+12] == 'N' && src[ofs+13] == 'D' && src[ofs+14] == 'E' && src[ofs+15] == 'R' && src[ofs+16] == 'T' && src[ofs+17] == 'A' && src[ofs+18] == 'G') {
														if (ofs+20 < src_len && src[ofs+19] == '_' && src[ofs+20] == '2') {
															found = ofs + 21;
															offset = 42; // ('LOKALER_KALENDERTAG_2', 42)
														}
														if (found == -1) {
															found = ofs + 19;
															offset = 45; // ('LOKALER_KALENDERTAG', 45)
														}
													}
													break;
												case 'M':
													if (ofs+12 < src_len && src[ofs+9] == 'O' && src[ofs+10] == 'N' && src[ofs+11] == 'A' && src[ofs+12] == 'T') {
														if (ofs+13 < src_len) switch (src[ofs+13]) {
															case 'S':
																if (ofs+17 < src_len && src[ofs+14] == 'N' && src[ofs+15] == 'A' && src[ofs+16] == 'M' && src[ofs+17] == 'E') {
																	if (ofs+18 < src_len && src[ofs+18] == '_') {
																		if (ofs+19 < src_len) switch (src[ofs+19]) {
																			case 'G':
																				if (ofs+21 < src_len && src[ofs+20] == 'E' && src[ofs+21] == 'N') {
																					if (ofs+25 < src_len && src[ofs+22] == 'I' && src[ofs+23] == 'T' && src[ofs+24] == 'I' && src[ofs+25] == 'V') {
																						found = ofs + 26;
																						offset = 63; // ('LOKALER_MONATSNAME_GENITIV', 63)
																					}
																					if (found == -1) {
																						found = ofs + 22;
																						offset = 64; // ('LOKALER_MONATSNAME_GEN', 64)
																					}
																				}
																				break;
																			case 'K':
																				if (ofs+22 < src_len && src[ofs+20] == 'U' && src[ofs+21] == 'R' && src[ofs+22] == 'Z') {
																					found = ofs + 23;
																					offset = 59; // ('LOKALER_MONATSNAME_KURZ', 59)
																				}
																				break;
																		}
																	}
																	if (found == -1) {
																		found = ofs + 18;
																		offset = 61; // ('LOKALER_MONATSNAME', 61)
																	}
																}
																break;
															case '_':
																if (ofs+14 < src_len) switch (src[ofs+14]) {
																	case '1':
																		found = ofs + 15;
																		offset = 40; // ('LOKALER_MONAT_1', 40)
																		break;
																	case '2':
																		found = ofs + 15;
																		offset = 37; // ('LOKALER_MONAT_2', 37)
																		break;
																}
																break;
														}
														if (found == -1) {
															found = ofs + 13;
															offset = 36; // ('LOKALER_MONAT', 36)
														}
													}
													break;
												case 'T':
													if (ofs+10 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'G') {
														if (ofs+12 < src_len && src[ofs+11] == '_' && src[ofs+12] == '2') {
															found = ofs + 13;
															offset = 43; // ('LOKALER_TAG_2', 43)
														}
														if (found == -1) {
															found = ofs + 11;
															offset = 46; // ('LOKALER_TAG', 46)
														}
													}
													break;
												case 'W':
													if (ofs+16 < src_len && src[ofs+9] == 'O' && src[ofs+10] == 'C' && src[ofs+11] == 'H' && src[ofs+12] == 'E' && src[ofs+13] == 'N' && src[ofs+14] == 'T' && src[ofs+15] == 'A' && src[ofs+16] == 'G') {
														if (ofs+21 < src_len && src[ofs+17] == '_' && src[ofs+18] == 'Z' && src[ofs+19] == 'A' && src[ofs+20] == 'H' && src[ofs+21] == 'L') {
															found = ofs + 22;
															offset = 57; // ('LOKALER_WOCHENTAG_ZAHL', 57)
														}
														if (found == -1) {
															found = ofs + 17;
															offset = 66; // ('LOKALER_WOCHENTAG', 66)
														}
													}
													break;
												case 'Z':
													if (ofs+18 < src_len && src[ofs+9] == 'E' && src[ofs+10] == 'I' && src[ofs+11] == 'T' && src[ofs+12] == 'S' && src[ofs+13] == 'T' && src[ofs+14] == 'E' && src[ofs+15] == 'M' && src[ofs+16] == 'P' && src[ofs+17] == 'E' && src[ofs+18] == 'L') {
														found = ofs + 19;
														offset = 52; // ('LOKALER_ZEITSTEMPEL', 52)
													}
													break;
											}
										}
										break;
									case 'S':
										if (ofs+11 < src_len && src[ofs+7] == '_' && src[ofs+8] == 'J' && src[ofs+9] == 'A' && src[ofs+10] == 'H' && src[ofs+11] == 'R') {
											found = ofs + 12;
											offset = 34; // ('LOKALES_JAHR', 34)
										}
										break;
									case '_':
										if (ofs+7 < src_len) switch (src[ofs+7]) {
											case 'K':
												if (ofs+19 < src_len && src[ofs+8] == 'A' && src[ofs+9] == 'L' && src[ofs+10] == 'E' && src[ofs+11] == 'N' && src[ofs+12] == 'D' && src[ofs+13] == 'E' && src[ofs+14] == 'R' && src[ofs+15] == 'W' && src[ofs+16] == 'O' && src[ofs+17] == 'C' && src[ofs+18] == 'H' && src[ofs+19] == 'E') {
													found = ofs + 20;
													offset = 54; // ('LOKALE_KALENDERWOCHE', 54)
												}
												break;
											case 'S':
												if (ofs+12 < src_len && src[ofs+8] == 'T' && src[ofs+9] == 'U' && src[ofs+10] == 'N' && src[ofs+11] == 'D' && src[ofs+12] == 'E') {
													found = ofs + 13;
													offset = 48; // ('LOKALE_STUNDE', 48)
												}
												break;
											case 'U':
												if (ofs+13 < src_len && src[ofs+8] == 'H' && src[ofs+9] == 'R' && src[ofs+10] == 'Z' && src[ofs+11] == 'E' && src[ofs+12] == 'I' && src[ofs+13] == 'T') {
													found = ofs + 14;
													offset = 50; // ('LOKALE_UHRZEIT', 50)
												}
												break;
											case 'W':
												if (ofs+11 < src_len && src[ofs+8] == 'O' && src[ofs+9] == 'C' && src[ofs+10] == 'H' && src[ofs+11] == 'E') {
													found = ofs + 12;
													offset = 55; // ('LOKALE_WOCHE', 55)
												}
												break;
										}
										break;
								}
							}
							break;
					}
				}
				break;
			case 'N':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+3 < src_len && src[ofs+2] == 'M' && src[ofs+3] == 'E') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case 'N':
									if (ofs+9 < src_len && src[ofs+5] == 'S' && src[ofs+6] == 'R' && src[ofs+7] == 'A' && src[ofs+8] == 'U' && src[ofs+9] == 'M') {
										if (ofs+10 < src_len) switch (src[ofs+10]) {
											case 'N':
												if (ofs+15 < src_len && src[ofs+11] == 'U' && src[ofs+12] == 'M' && src[ofs+13] == 'M' && src[ofs+14] == 'E' && src[ofs+15] == 'R') {
													found = ofs + 16;
													offset = 183; // ('NAMENSRAUMNUMMER', 183)
												}
												break;
											case '_':
												if (ofs+13 < src_len && src[ofs+11] == 'U' && src[ofs+12] == 'R' && src[ofs+13] == 'L') {
													found = ofs + 14;
													offset = 88; // ('NAMENSRAUM_URL', 88)
												}
												break;
										}
										if (found == -1) {
											found = ofs + 10;
											offset = 86; // ('NAMENSRAUM', 86)
										}
									}
									break;
								case 'S':
									if (ofs+8 < src_len && src[ofs+5] == 'P' && src[ofs+6] == 'A' && src[ofs+7] == 'C' && src[ofs+8] == 'E') {
										if (ofs+9 < src_len) switch (src[ofs+9]) {
											case 'E':
												found = ofs + 10;
												offset = 89; // ('NAMESPACEE', 89)
												break;
											case 'N':
												if (ofs+14 < src_len && src[ofs+10] == 'U' && src[ofs+11] == 'M' && src[ofs+12] == 'B' && src[ofs+13] == 'E' && src[ofs+14] == 'R') {
													found = ofs + 15;
													offset = 184; // ('NAMESPACENUMBER', 184)
												}
												break;
										}
										if (found == -1) {
											found = ofs + 9;
											offset = 87; // ('NAMESPACE', 87)
										}
									}
									break;
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
												offset = 158; // ('NUMBEROFACTIVEUSERS', 158)
											}
											break;
										case 'D':
											if (ofs+13 < src_len && src[ofs+10] == 'M' && src[ofs+11] == 'I' && src[ofs+12] == 'N' && src[ofs+13] == 'S') {
												found = ofs + 14;
												offset = 160; // ('NUMBEROFADMINS', 160)
											}
											break;
										case 'R':
											if (ofs+15 < src_len && src[ofs+10] == 'T' && src[ofs+11] == 'I' && src[ofs+12] == 'C' && src[ofs+13] == 'L' && src[ofs+14] == 'E' && src[ofs+15] == 'S') {
												found = ofs + 16;
												offset = 149; // ('NUMBEROFARTICLES', 149)
											}
											break;
									}
									break;
								case 'E':
									if (ofs+12 < src_len && src[ofs+9] == 'D' && src[ofs+10] == 'I' && src[ofs+11] == 'T' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 153; // ('NUMBEROFEDITS', 153)
									}
									break;
								case 'F':
									if (ofs+12 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'L' && src[ofs+11] == 'E' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 151; // ('NUMBEROFFILES', 151)
									}
									break;
								case 'P':
									if (ofs+12 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'G' && src[ofs+11] == 'E' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 147; // ('NUMBEROFPAGES', 147)
									}
									break;
								case 'U':
									if (ofs+12 < src_len && src[ofs+9] == 'S' && src[ofs+10] == 'E' && src[ofs+11] == 'R' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 156; // ('NUMBEROFUSERS', 156)
									}
									break;
								case 'V':
									if (ofs+12 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'E' && src[ofs+11] == 'W' && src[ofs+12] == 'S') {
										found = ofs + 13;
										offset = 154; // ('NUMBEROFVIEWS', 154)
									}
									break;
							}
						}
						break;
				}
				break;
			case 'O':
				if (ofs+8 < src_len && src[ofs+1] == 'B' && src[ofs+2] == 'E' && src[ofs+3] == 'R' && src[ofs+4] == 'S' && src[ofs+5] == 'E' && src[ofs+6] == 'I' && src[ofs+7] == 'T' && src[ofs+8] == 'E') {
					if (ofs+12 < src_len && src[ofs+9] == '_' && src[ofs+10] == 'U' && src[ofs+11] == 'R' && src[ofs+12] == 'L') {
						found = ofs + 13;
						offset = 112; // ('OBERSEITE_URL', 112)
					}
					if (found == -1) {
						found = ofs + 9;
						offset = 108; // ('OBERSEITE', 108)
					}
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
										offset = 189; // ('PAGEBANNER', 189)
									}
									break;
								case 'L':
									if (ofs+11 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'N' && src[ofs+7] == 'G' && src[ofs+8] == 'U' && src[ofs+9] == 'A' && src[ofs+10] == 'G' && src[ofs+11] == 'E') {
										found = ofs + 12;
										offset = 191; // ('PAGELANGUAGE', 191)
									}
									break;
								case 'N':
									if (ofs+7 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'M' && src[ofs+7] == 'E') {
										if (ofs+8 < src_len && src[ofs+8] == 'E') {
											found = ofs + 9;
											offset = 111; // ('PAGENAMEE', 111)
										}
										if (found == -1) {
											found = ofs + 8;
											offset = 107; // ('PAGENAME', 107)
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
														offset = 181; // ('PAGESINCATEGORY', 181)
													}
													if (found == -1) {
														found = ofs + 10;
														offset = 182; // ('PAGESINCAT', 182)
													}
												}
												break;
											case 'Z':
												if (ofs+7 < src_len && src[ofs+7] == 'E') {
													found = ofs + 8;
													offset = 165; // ('PAGESIZE', 165)
												}
												break;
										}
									}
									break;
							}
						}
						break;
					case 'R':
						if (ofs+2 < src_len && src[ofs+2] == 'O') {
							if (ofs+3 < src_len) switch (src[ofs+3]) {
								case 'J':
									if (ofs+10 < src_len && src[ofs+4] == 'E' && src[ofs+5] == 'K' && src[ofs+6] == 'T' && src[ofs+7] == 'N' && src[ofs+8] == 'A' && src[ofs+9] == 'M' && src[ofs+10] == 'E') {
										found = ofs + 11;
										offset = 134; // ('PROJEKTNAME', 134)
									}
									break;
								case 'T':
									if (ofs+9 < src_len && src[ofs+4] == 'E' && src[ofs+5] == 'C' && src[ofs+6] == 'T' && src[ofs+7] == 'I' && src[ofs+8] == 'O' && src[ofs+9] == 'N') {
										if (ofs+10 < src_len) switch (src[ofs+10]) {
											case 'E':
												if (ofs+15 < src_len && src[ofs+11] == 'X' && src[ofs+12] == 'P' && src[ofs+13] == 'I' && src[ofs+14] == 'R' && src[ofs+15] == 'Y') {
													found = ofs + 16;
													offset = 190; // ('PROTECTIONEXPIRY', 190)
												}
												break;
											case 'L':
												if (ofs+14 < src_len && src[ofs+11] == 'E' && src[ofs+12] == 'V' && src[ofs+13] == 'E' && src[ofs+14] == 'L') {
													found = ofs + 15;
													offset = 171; // ('PROTECTIONLEVEL', 171)
												}
												break;
										}
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
									offset = 79; // ('REVISIONDAY2', 79)
								}
								if (found == -1) {
									found = ofs + 11;
									offset = 82; // ('REVISIONDAY', 82)
								}
							}
							break;
						case 'I':
							if (ofs+9 < src_len && src[ofs+9] == 'D') {
								found = ofs + 10;
								offset = 163; // ('REVISIONID', 163)
							}
							break;
						case 'M':
							if (ofs+12 < src_len && src[ofs+9] == 'O' && src[ofs+10] == 'N' && src[ofs+11] == 'T' && src[ofs+12] == 'H') {
								if (ofs+13 < src_len && src[ofs+13] == '1') {
									found = ofs + 14;
									offset = 76; // ('REVISIONMONTH1', 76)
								}
								if (found == -1) {
									found = ofs + 13;
									offset = 73; // ('REVISIONMONTH', 73)
								}
							}
							break;
						case 'S':
							if (ofs+9 < src_len) switch (src[ofs+9]) {
								case 'B':
									if (ofs+16 < src_len && src[ofs+10] == 'E' && src[ofs+11] == 'N' && src[ofs+12] == 'U' && src[ofs+13] == 'T' && src[ofs+14] == 'Z' && src[ofs+15] == 'E' && src[ofs+16] == 'R') {
										found = ofs + 17;
										offset = 166; // ('REVISIONSBENUTZER', 166)
									}
									break;
								case 'I':
									if (ofs+10 < src_len) switch (src[ofs+10]) {
										case 'D':
											found = ofs + 11;
											offset = 161; // ('REVISIONSID', 161)
											break;
										case 'Z':
											if (ofs+11 < src_len && src[ofs+11] == 'E') {
												found = ofs + 12;
												offset = 188; // ('REVISIONSIZE', 188)
											}
											break;
									}
									break;
								case 'J':
									if (ofs+12 < src_len && src[ofs+10] == 'A' && src[ofs+11] == 'H' && src[ofs+12] == 'R') {
										found = ofs + 13;
										offset = 68; // ('REVISIONSJAHR', 68)
									}
									break;
								case 'M':
									if (ofs+13 < src_len && src[ofs+10] == 'O' && src[ofs+11] == 'N' && src[ofs+12] == 'A' && src[ofs+13] == 'T') {
										if (ofs+14 < src_len && src[ofs+14] == '1') {
											found = ofs + 15;
											offset = 74; // ('REVISIONSMONAT1', 74)
										}
										if (found == -1) {
											found = ofs + 14;
											offset = 71; // ('REVISIONSMONAT', 71)
										}
									}
									break;
								case 'T':
									if (ofs+11 < src_len && src[ofs+10] == 'A' && src[ofs+11] == 'G') {
										if (ofs+12 < src_len && src[ofs+12] == '2') {
											found = ofs + 13;
											offset = 77; // ('REVISIONSTAG2', 77)
										}
										if (found == -1) {
											found = ofs + 12;
											offset = 80; // ('REVISIONSTAG', 80)
										}
									}
									break;
								case 'Z':
									if (ofs+19 < src_len && src[ofs+10] == 'E' && src[ofs+11] == 'I' && src[ofs+12] == 'T' && src[ofs+13] == 'S' && src[ofs+14] == 'T' && src[ofs+15] == 'E' && src[ofs+16] == 'M' && src[ofs+17] == 'P' && src[ofs+18] == 'E' && src[ofs+19] == 'L') {
										found = ofs + 20;
										offset = 83; // ('REVISIONSZEITSTEMPEL', 83)
									}
									break;
							}
							break;
						case 'T':
							if (ofs+16 < src_len && src[ofs+9] == 'I' && src[ofs+10] == 'M' && src[ofs+11] == 'E' && src[ofs+12] == 'S' && src[ofs+13] == 'T' && src[ofs+14] == 'A' && src[ofs+15] == 'M' && src[ofs+16] == 'P') {
								found = ofs + 17;
								offset = 85; // ('REVISIONTIMESTAMP', 85)
							}
							break;
						case 'U':
							if (ofs+11 < src_len && src[ofs+9] == 'S' && src[ofs+10] == 'E' && src[ofs+11] == 'R') {
								if (ofs+12 < src_len && src[ofs+12] == '2') {
									found = ofs + 13;
									offset = 169; // ('REVISIONUSER2', 169)
								}
								if (found == -1) {
									found = ofs + 12;
									offset = 168; // ('REVISIONUSER', 168)
								}
							}
							break;
						case 'Y':
							if (ofs+11 < src_len && src[ofs+9] == 'E' && src[ofs+10] == 'A' && src[ofs+11] == 'R') {
								found = ofs + 12;
								offset = 70; // ('REVISIONYEAR', 70)
							}
							break;
					}
				}
				break;
			case 'S':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'C':
						if (ofs+11 < src_len && src[ofs+2] == 'H' && src[ofs+3] == 'U' && src[ofs+4] == 'T' && src[ofs+5] == 'Z' && src[ofs+6] == 'S' && src[ofs+7] == 'T' && src[ofs+8] == 'A' && src[ofs+9] == 'T' && src[ofs+10] == 'U' && src[ofs+11] == 'S') {
							found = ofs + 12;
							offset = 170; // ('SCHUTZSTATUS', 170)
						}
						break;
					case 'E':
						if (ofs+2 < src_len) switch (src[ofs+2]) {
							case 'I':
								if (ofs+5 < src_len && src[ofs+3] == 'T' && src[ofs+4] == 'E' && src[ofs+5] == 'N') {
									if (ofs+6 < src_len) switch (src[ofs+6]) {
										case 'A':
											if (ofs+11 < src_len && src[ofs+7] == 'N' && src[ofs+8] == 'Z' && src[ofs+9] == 'A' && src[ofs+10] == 'H' && src[ofs+11] == 'L') {
												found = ofs + 12;
												offset = 146; // ('SEITENANZAHL', 146)
											}
											break;
										case 'G':
											if (ofs+12 < src_len && src[ofs+7] == 'R' && src[ofs+8] == -61 && src[ofs+9] == -106 && src[ofs+10] == 'S' && src[ofs+11] == 'S' && src[ofs+12] == 'E') {
												found = ofs + 13;
												offset = 164; // ('SEITENGRÃ\x96SSE', 164)
											}
											break;
										case 'I':
											if (ofs+10 < src_len && src[ofs+7] == 'N' && src[ofs+8] == 'K' && src[ofs+9] == 'A' && src[ofs+10] == 'T') {
												found = ofs + 11;
												offset = 180; // ('SEITENINKAT', 180)
											}
											break;
										case 'N':
											if (ofs+9 < src_len && src[ofs+7] == 'A' && src[ofs+8] == 'M' && src[ofs+9] == 'E') {
												if (ofs+13 < src_len && src[ofs+10] == '_' && src[ofs+11] == 'U' && src[ofs+12] == 'R' && src[ofs+13] == 'L') {
													found = ofs + 14;
													offset = 110; // ('SEITENNAME_URL', 110)
												}
												if (found == -1) {
													found = ofs + 10;
													offset = 106; // ('SEITENNAME', 106)
												}
											}
											break;
										case 'T':
											if (ofs+10 < src_len && src[ofs+7] == 'I' && src[ofs+8] == 'T' && src[ofs+9] == 'E' && src[ofs+10] == 'L') {
												found = ofs + 11;
												offset = 172; // ('SEITENTITEL', 172)
											}
											break;
										case '_':
											if (ofs+7 < src_len) switch (src[ofs+7]) {
												case 'I':
													if (ofs+18 < src_len && src[ofs+8] == 'N' && src[ofs+9] == '_' && src[ofs+10] == 'K' && src[ofs+11] == 'A' && src[ofs+12] == 'T' && src[ofs+13] == 'E' && src[ofs+14] == 'G' && src[ofs+15] == 'O' && src[ofs+16] == 'R' && src[ofs+17] == 'I' && src[ofs+18] == 'E') {
														found = ofs + 19;
														offset = 178; // ('SEITEN_IN_KATEGORIE', 178)
													}
													break;
												case 'K':
													if (ofs+9 < src_len && src[ofs+8] == 'A' && src[ofs+9] == 'T') {
														found = ofs + 10;
														offset = 179; // ('SEITEN_KAT', 179)
													}
													break;
											}
											break;
									}
								}
								break;
							case 'R':
								if (ofs+5 < src_len && src[ofs+3] == 'V' && src[ofs+4] == 'E' && src[ofs+5] == 'R') {
									if (ofs+9 < src_len && src[ofs+6] == 'N' && src[ofs+7] == 'A' && src[ofs+8] == 'M' && src[ofs+9] == 'E') {
										found = ofs + 10;
										offset = 136; // ('SERVERNAME', 136)
									}
									if (found == -1) {
										found = ofs + 6;
										offset = 137; // ('SERVER', 137)
									}
								}
								break;
						}
						break;
					case 'H':
						if (ofs+8 < src_len && src[ofs+2] == 'O' && src[ofs+3] == 'R' && src[ofs+4] == 'T' && src[ofs+5] == 'D' && src[ofs+6] == 'E' && src[ofs+7] == 'S' && src[ofs+8] == 'C') {
							found = ofs + 9;
							offset = 192; // ('SHORTDESC', 192)
						}
						break;
					case 'I':
						if (ofs+7 < src_len && src[ofs+2] == 'T' && src[ofs+3] == 'E' && src[ofs+4] == 'N' && src[ofs+5] == 'A' && src[ofs+6] == 'M' && src[ofs+7] == 'E') {
							found = ofs + 8;
							offset = 135; // ('SITENAME', 135)
						}
						break;
					case 'O':
						if (ofs+9 < src_len && src[ofs+2] == 'R' && src[ofs+3] == 'T' && src[ofs+4] == 'I' && src[ofs+5] == 'E' && src[ofs+6] == 'R' && src[ofs+7] == 'U' && src[ofs+8] == 'N' && src[ofs+9] == 'G') {
							found = ofs + 10;
							offset = 174; // ('SORTIERUNG', 174)
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
														offset = 126; // ('SUBJECTPAGENAMEE', 126)
													}
													if (found == -1) {
														found = ofs + 15;
														offset = 121; // ('SUBJECTPAGENAME', 121)
													}
												}
												break;
											case 'S':
												if (ofs+11 < src_len && src[ofs+8] == 'P' && src[ofs+9] == 'A' && src[ofs+10] == 'C' && src[ofs+11] == 'E') {
													if (ofs+12 < src_len && src[ofs+12] == 'E') {
														found = ofs + 13;
														offset = 94; // ('SUBJECTSPACEE', 94)
													}
													if (found == -1) {
														found = ofs + 12;
														offset = 91; // ('SUBJECTSPACE', 91)
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
											offset = 117; // ('SUBPAGENAMEE', 117)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 115; // ('SUBPAGENAME', 115)
										}
									}
									break;
							}
						}
						break;
				}
				break;
			case 'T':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'A':
						if (ofs+3 < src_len && src[ofs+2] == 'L' && src[ofs+3] == 'K') {
							if (ofs+4 < src_len) switch (src[ofs+4]) {
								case 'P':
									if (ofs+11 < src_len && src[ofs+5] == 'A' && src[ofs+6] == 'G' && src[ofs+7] == 'E' && src[ofs+8] == 'N' && src[ofs+9] == 'A' && src[ofs+10] == 'M' && src[ofs+11] == 'E') {
										if (ofs+12 < src_len && src[ofs+12] == 'E') {
											found = ofs + 13;
											offset = 133; // ('TALKPAGENAMEE', 133)
										}
										if (found == -1) {
											found = ofs + 12;
											offset = 130; // ('TALKPAGENAME', 130)
										}
									}
									break;
								case 'S':
									if (ofs+8 < src_len && src[ofs+5] == 'P' && src[ofs+6] == 'A' && src[ofs+7] == 'C' && src[ofs+8] == 'E') {
										if (ofs+9 < src_len && src[ofs+9] == 'E') {
											found = ofs + 10;
											offset = 101; // ('TALKSPACEE', 101)
										}
										if (found == -1) {
											found = ofs + 9;
											offset = 98; // ('TALKSPACE', 98)
										}
									}
									break;
							}
						}
						break;
					case 'E':
						if (ofs+14 < src_len && src[ofs+2] == 'X' && src[ofs+3] == 'T' && src[ofs+4] == 'A' && src[ofs+5] == 'U' && src[ofs+6] == 'S' && src[ofs+7] == 'R' && src[ofs+8] == 'I' && src[ofs+9] == 'C' && src[ofs+10] == 'H' && src[ofs+11] == 'T' && src[ofs+12] == 'U' && src[ofs+13] == 'N' && src[ofs+14] == 'G') {
							found = ofs + 15;
							offset = 141; // ('TEXTAUSRICHTUNG', 141)
						}
						break;
				}
				break;
			case 'U':
				if (ofs+9 < src_len && src[ofs+1] == 'N' && src[ofs+2] == 'T' && src[ofs+3] == 'E' && src[ofs+4] == 'R' && src[ofs+5] == 'S' && src[ofs+6] == 'E' && src[ofs+7] == 'I' && src[ofs+8] == 'T' && src[ofs+9] == 'E') {
					if (ofs+13 < src_len && src[ofs+10] == '_' && src[ofs+11] == 'U' && src[ofs+12] == 'R' && src[ofs+13] == 'L') {
						found = ofs + 14;
						offset = 116; // ('UNTERSEITE_URL', 116)
					}
					if (found == -1) {
						found = ofs + 10;
						offset = 114; // ('UNTERSEITE', 114)
					}
				}
				break;
			case 'V':
				if (ofs+1 < src_len) switch (src[ofs+1]) {
					case 'E':
						if (ofs+7 < src_len && src[ofs+2] == 'R' && src[ofs+3] == 'S' && src[ofs+4] == 'I' && src[ofs+5] == 'O' && src[ofs+6] == 'N' && src[ofs+7] == 'S') {
							if (ofs+8 < src_len) switch (src[ofs+8]) {
								case 'B':
									if (ofs+15 < src_len && src[ofs+9] == 'E' && src[ofs+10] == 'N' && src[ofs+11] == 'U' && src[ofs+12] == 'T' && src[ofs+13] == 'Z' && src[ofs+14] == 'E' && src[ofs+15] == 'R') {
										found = ofs + 16;
										offset = 167; // ('VERSIONSBENUTZER', 167)
									}
									break;
								case 'I':
									if (ofs+9 < src_len && src[ofs+9] == 'D') {
										found = ofs + 10;
										offset = 162; // ('VERSIONSID', 162)
									}
									break;
								case 'J':
									if (ofs+11 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'H' && src[ofs+11] == 'R') {
										found = ofs + 12;
										offset = 69; // ('VERSIONSJAHR', 69)
									}
									break;
								case 'M':
									if (ofs+12 < src_len && src[ofs+9] == 'O' && src[ofs+10] == 'N' && src[ofs+11] == 'A' && src[ofs+12] == 'T') {
										if (ofs+13 < src_len && src[ofs+13] == '1') {
											found = ofs + 14;
											offset = 75; // ('VERSIONSMONAT1', 75)
										}
										if (found == -1) {
											found = ofs + 13;
											offset = 72; // ('VERSIONSMONAT', 72)
										}
									}
									break;
								case 'T':
									if (ofs+10 < src_len && src[ofs+9] == 'A' && src[ofs+10] == 'G') {
										if (ofs+11 < src_len && src[ofs+11] == '2') {
											found = ofs + 12;
											offset = 78; // ('VERSIONSTAG2', 78)
										}
										if (found == -1) {
											found = ofs + 11;
											offset = 81; // ('VERSIONSTAG', 81)
										}
									}
									break;
								case 'Z':
									if (ofs+18 < src_len && src[ofs+9] == 'E' && src[ofs+10] == 'I' && src[ofs+11] == 'T' && src[ofs+12] == 'S' && src[ofs+13] == 'T' && src[ofs+14] == 'E' && src[ofs+15] == 'M' && src[ofs+16] == 'P' && src[ofs+17] == 'E' && src[ofs+18] == 'L') {
										found = ofs + 19;
										offset = 84; // ('VERSIONSZEITSTEMPEL', 84)
									}
									break;
							}
						}
						break;
					case 'O':
						if (ofs+2 < src_len) switch (src[ofs+2]) {
							case 'L':
								if (ofs+16 < src_len && src[ofs+3] == 'L' && src[ofs+4] == 'E' && src[ofs+5] == 'R' && src[ofs+6] == '_' && src[ofs+7] == 'S' && src[ofs+8] == 'E' && src[ofs+9] == 'I' && src[ofs+10] == 'T' && src[ofs+11] == 'E' && src[ofs+12] == 'N' && src[ofs+13] == 'N' && src[ofs+14] == 'A' && src[ofs+15] == 'M' && src[ofs+16] == 'E') {
									if (ofs+20 < src_len && src[ofs+17] == '_' && src[ofs+18] == 'U' && src[ofs+19] == 'R' && src[ofs+20] == 'L') {
										found = ofs + 21;
										offset = 104; // ('VOLLER_SEITENNAME_URL', 104)
									}
									if (found == -1) {
										found = ofs + 17;
										offset = 102; // ('VOLLER_SEITENNAME', 102)
									}
								}
								break;
							case 'R':
								if (ofs+10 < src_len && src[ofs+3] == 'D' && src[ofs+4] == 'E' && src[ofs+5] == 'R' && src[ofs+6] == 'S' && src[ofs+7] == 'E' && src[ofs+8] == 'I' && src[ofs+9] == 'T' && src[ofs+10] == 'E') {
									if (ofs+14 < src_len && src[ofs+11] == '_' && src[ofs+12] == 'U' && src[ofs+13] == 'R' && src[ofs+14] == 'L') {
										found = ofs + 15;
										offset = 124; // ('VORDERSEITE_URL', 124)
									}
									if (found == -1) {
										found = ofs + 11;
										offset = 119; // ('VORDERSEITE', 119)
									}
								}
								break;
						}
						break;
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
