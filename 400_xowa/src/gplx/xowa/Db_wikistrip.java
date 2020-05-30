/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa; import gplx.*;
import gplx.langs.htmls.*;
/*
remove __TOC__
remove {{ .... }} matching
remove <ref ... </ref>
remove any elements? <b> ... </b>??
remove [[Categoy:..]]
remove \n(=)* ... (=)* (ie headings)
cvt [[xyz|abc]] -> abc
stored with '''
first line is used for the popup system
all lines used for full text image searches

cvt ''' -> nothing
cvt '' -> nothing

<nowiki> can rear its ugly head
search for '{' or '<' or '[' or '\n='

could get lucene to store the page (for highliting)

allow for nesting
if '{' need to find balancing '}' taking <nowiki> into account
if '<' need to check if its self closing or find the balancing close taking <nowiki> into account
if '[' only if two '[[' find '|' or matching ']' assuming <nowiki> not involved
if '=' check previous char for '\n', if so find next '\n'
*/
public class Db_wikistrip {

	private Xoa_ttl ttl;
	private int nowikicheck(byte[] src, int src_len, int pos) {
		return findclosingangle(src, src_len, pos);
/*		if (pos < src_len) {
			byte b = src[pos++];
			if (b == '!') { // assume a comment
				while (pos < src_len) {
					b = src[pos++];
					if (b == '-' && pos+2 < src_len) {
						if (src[pos] == '-' && src[pos+1] == '>')
							return pos + 2;
					}
				}
				return pos;
			}
		}
            return pos;*/
	}
	// find matching two {{ or three {{{
	private int findclosingsquiggle(byte[] src, int src_len, int pos) {
		int startpos = pos;
		int scount = 2;
		while (pos < src_len) {
			if (src[pos] == '{') {
				pos++;
				scount++;
			}
			else
				break;
		}
		if (scount >= 3) {
			pos = findclosingsquiggle(src, src_len, pos - scount + 3, 3);
		}
		else
			pos = findclosingsquiggle(src, src_len, pos, 2);
		if (pos < 0) {
			// report/note an error
			int beg = startpos - 10;
			int end = startpos + 10;
			if (beg < 0) beg = 0;
			if (end >= src_len) end = src_len;
			Gfo_usr_dlg_.Instance.Warn_many("", "", "unclosed squiggle { ttl=~{0} src=~{1}", ttl.Full_db(), Bry_.Mid(src, beg, end));
			return startpos;
		}
		return pos;
	}

	private int findclosingsquiggle(byte[] src, int src_len, int pos, int count) {
		int scount;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '{':
					scount = 1;
					while (pos < src_len) {
						if (src[pos] == '{') {
							pos++;
							scount++;
						}
						else
							break;
					}
					if (scount == 1)
						break; // do nutting
					if (scount >= 3) {
						pos = findclosingsquiggle(src, src_len, pos - scount + 3, 3);
					}
					else
						pos = findclosingsquiggle(src, src_len, pos, 2);
					break;
				case '}':
					scount = 1;
					while (pos < src_len) {
						if (src[pos] == '}') {
							pos++;
							scount++;
						}
						else
							break;
					}
					if (scount == 1)
						break; // do nutting
					if (scount == count)
						return pos;
					return pos - scount + count;
				case '<':
					pos = nowikicheck(src, src_len, pos);
					break;
			}
		}
		return pos;
	}
	private int findclosingtable(byte[] src, int src_len, int pos) {
		int levelcount = 1;
		int endpos = 0;
		int startpos = pos;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '{':
					if (pos < src_len && src[pos] == '|') { // must be {|
						levelcount++;
						pos++;
					}
					break;
				case '}':
					if (src[pos-2] == '|') {
						levelcount--;
						if (levelcount == 0) {
							endpos = pos;
							pos = src_len; // terminate loop
						}
					}
					break;
				case '<':
					pos = nowikicheck(src, src_len, pos);
					break;
			}
		}
		if (levelcount != 0 || endpos == 0) {
			// report/note an error
			int beg = startpos - 10;
			int end = startpos + 10;
			if (beg < 0) beg = 0;
			if (end >= src_len) end = src_len;
			Gfo_usr_dlg_.Instance.Warn_many("", "", "unclosed table ttl=~{0} src=~{1}", ttl.Full_db(), Bry_.Mid(src, beg, end));
			return startpos + 1; // skip the '{'
		}
		return endpos;
	}
	private int findclosingbracket(byte[] src, int src_len, int pos) {
		int levelcount = 1;
		int endpos = 0;
		int startpos = pos;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '(':
					levelcount++;
					break;
				case ')':
					levelcount--;
					if (levelcount == 0) {
						endpos = pos;
						pos = src_len; // terminate loop
					}
					break;
			}
		}
		if (levelcount != 0 || endpos == 0) {
			// report/note an error
			int beg = startpos - 10;
			int end = startpos + 10;
			if (beg < 0) beg = 0;
			if (end >= src_len) end = src_len;
			Gfo_usr_dlg_.Instance.Warn_many("", "", "unclosed bracket ( ttl=~{0} src=~{1}", ttl.Full_db(), Bry_.Mid(src, beg, end));
			return startpos + 1; // skip the '('
		}
		return endpos;
	}
	private boolean check_ns(byte[] src, int bgn, int end) {
		int sz = end - bgn;
		if (sz > 6 && ((src[bgn+1] | 32) == 'f') && src[bgn+5] == ':') { // File:
			return true;
		}
		else if (sz > 7 && (
		  (((src[bgn+1] | 32) == 'i') && src[bgn+6] == ':') // Image:
		  || (((src[bgn+1] | 32) == 'd') && src[bgn+6] == ':') // Datei:
		  )) {
			return true;
		}
		else if (sz > 10 && (
		  (((src[bgn+1] | 32) == 'c') && src[bgn+9] == ':') // Category:
		  || (src[bgn+1] == -41 && src[bgn+9] == ':') // %d7%a7%d7%95%d7%91%d7%a5: hebrew
		  )) {
			return true;
		}
		return false;
	}
	private int findclosingsquare(byte[] src, int src_len, int pos, Bry_bfr bfr) {
		int levelcount = 1;
		int endpos = 0;
		int startpos = pos;
		int barpos = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '[':
					levelcount++;
					break;
				case ']':
					levelcount--;
					if (levelcount == 0) {
						endpos = pos;
						pos = src_len; // terminate loop
					}
					break;
				case '|':
					barpos = pos;
					break;
			}
		}
		if (levelcount != 0 || endpos == 0) {
			// report/note an error
			int beg = startpos - 10;
			int end = startpos + 10;
			if (beg < 0) beg = 0;
			if (end >= src_len) end = src_len;
			Gfo_usr_dlg_.Instance.Warn_many("", "", "unclosed [ ttl=~{0} pos=~{2} src=~{1}", ttl.Full_db(), Bry_.Mid(src, beg, end), startpos);
			return startpos + 1; // skip the '['
		}
		// check for File: and Category: [what about other langage wikis?]
		if (!check_ns(src, startpos, endpos)) {
			// beware bad links
			// beware pipe trick (currently use full link)
			if (barpos > 0 && barpos < endpos - 3) {
				bfr.Add_mid(src, barpos, endpos-2);
			}
			else
				bfr.Add_mid(src, startpos+1, endpos-2);
		}
		return endpos;
	}
	
	private int findclosingangle(byte[] src, int src_len, int pos) {
		int levelcount = 0;
		int namestart = pos;
		int nameend = 0;
		byte b;
		while (pos < src_len) {
			b = src[pos++];
			if (b == '!') { // assume a comment
				while (pos < src_len) {
					b = src[pos++];
					if (b == '-' && pos+2 < src_len) {
						if (src[pos] == '-' && src[pos+1] == '>')
							return pos + 2;
					}
				}
				return pos;
			}
			else if (b == '>') {
				nameend = pos - 1;
				break;
			}
			else if (b == ' ' || b == '\t' || b == '\n') { // find close (check for self closing)
				nameend = pos - 1;
				if (nameend - namestart == 0)
					return pos;
				while (pos < src_len) {
					b = src[pos++];
					if (b == '>') {
						if (src[pos-2] == '/') // strictly should search backwards ignoring whitepsace
							return pos; // self close
						break;
					}
				}
				break;
			}
			else if (b == '/') {
				while (pos < src_len) {
					b = src[pos++];
					if (b == '>')
						return pos;
				}
			}
			else if (pos - namestart > 15) // no more than 15 character name!
				return namestart;
			else if ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z')) // allow only alpha?????????
				continue;
			else
				return namestart; // ignore the '<'
		}
		int tagend = pos;
		// check for special <br > <hr > <img >
		//System.out.println(String_.new_u8(Bry_.Mid(src, namestart, nameend+40)));
		if (nameend - namestart == 2) {
			if ((src[namestart] | 32) == 'b' && (src[namestart+1] | 32) == 'r')
				return pos;
			if ((src[namestart] | 32) == 'h' && (src[namestart+1] | 32) == 'r')
				return pos;
		}
		else if (nameend - namestart == 3) {
			if ((src[namestart] | 32) == 'i' && (src[namestart+1] | 32) == 'm' && (src[namestart+1] | 32) == 'g')
				return pos;
		}
		// now find the close (or a nesting!)
		while (pos < src_len) {
			// find next '<'
			while (pos < src_len) {
				b = src[pos++];
				if (b == '<')
					break;
			}
			if (pos >= src_len) { //no other angle found at all - skip this entity
				break;
			}
			boolean isclose = false;
			if (src[pos] == '/') {
				isclose = true;
				pos++;
			}
			// is this the same as the opener?
			if (pos + nameend - namestart >= src_len) { // not enough chars
				break;
			}
			int i;
			for (i = namestart; i < nameend; i++) {
				if ((src[i] | 32) != (src[pos++] | 32))
					break;
			}
			if (i < nameend) // no match
				continue;
			b = src[pos++];
			if (isclose) {
				if (b != '>') {  // fail: skip tag
					break;
				}
				if (levelcount == 0)
					return pos;
				levelcount--;
			}
			else {
				if (b == ' ' || b == '>') { // matching keyword
					levelcount++;
				}
			}
		}
		// report/note an error
		int beg = namestart - 10;
		int end = tagend + 10;
		if (beg < 0) beg = 0;
		if (end >= src_len) end = src_len;
		Gfo_usr_dlg_.Instance.Warn_many("", "", "unclosed angle { ttl=~{0} src=~{1}", ttl.Full_db(), Bry_.Mid(src, beg, end));
		return tagend;
	}

	public byte[] Strip_wiki(byte[] src, boolean firstparaonly) {
		Bry_bfr bfr = Bry_bfr_.New();
		int src_len = src.length;
		int startpos = 0;
		int pos = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '{':
					if (pos < src_len) {
						b = src[pos];
						if (b == '|') {
							bfr.Add_mid(src, startpos, pos-1);
							pos = findclosingtable(src, src_len, pos + 1);
							startpos = pos;
						}
						else if (b == '{') {
							bfr.Add_mid(src, startpos, pos-1);
							int namestart = pos + 1;
							pos = findclosingsquiggle(src, src_len, pos + 1);
							if (pos - startpos > 10) {
								int npos = 0; 
								int textstart = -1;
								if (src[namestart] == 'l' && src[namestart+1] == 'a' && src[namestart+2] == 'n' && src[namestart+3] == 'g' && src[namestart+4] == '|') { // lang
									npos = namestart + 5;
									while (npos < pos) {
										b = src[npos++];
										if (b == '|') {
											textstart = npos;
											break;
										}
									}
								}
								else if (src[namestart] == 'n' && src[namestart+1] == 'i' && src[namestart+2] == 'h' && src[namestart+3] == 'o' && src[namestart+4] == 'n' && src[namestart+5] == 'g' && src[namestart+6] == 'o' && src[namestart+7] == '|') { // nihongo
									npos = namestart + 8;
									textstart = npos;
								}
								else if (src[namestart] == 'd' && src[namestart+1] == 'a' && src[namestart+2] == 't' && src[namestart+3] == 'e') { // fr:template:date
									npos = namestart + 5;
									textstart = npos;
								}
								if (textstart > 0) {
									while (npos < pos) {
										b = src[npos++];
										if (b == '|' || b == '}') {
											break;
										}
									}
									bfr.Add_mid(src, textstart, npos-1);
								}
							}
							startpos = pos;
						}
					}
					break;
				case '[':
					if (pos < src_len) {
						// check for [http...]
						b = src[pos];
						if (b == 'h') {
							if (pos + 4 < src_len) {
								if (src[pos+1] == 't' && src[pos+2] == 't' && src[pos+3] == 'p') {
									bfr.Add_mid(src, startpos, pos-1);
									while (pos < src_len) { // find ' ' or close
										b = src[pos++];
										if (b == ']')
											break;
										else if (b == ' ') {
											// copy to ']'
											startpos = pos;
											while (pos < src_len) { // find ']'
												b = src[pos++];
												if (b == ']')
													break;
											}
											bfr.Add_mid(src, startpos, pos-1);
											break;
										}
									}
									startpos = pos;
								}
							}
						}
						else if (b == '[') {
							bfr.Add_mid(src, startpos, pos-1);
							pos = findclosingsquare(src, src_len, pos, bfr);
							startpos = pos;
						}
					}
					break;
				case '<':
					bfr.Add_mid(src, startpos, pos-1);
					pos = findclosingangle(src, src_len, pos);
					startpos = pos;
					break;
				case '=':
					if ((pos > 1 && src[pos-2] == '\n') || (pos == 1)) { // '\n='
						bfr.Add_mid(src, startpos, pos-1);
						while (pos + 1 < src_len) { // find next '=\n'
							if (src[pos++] == '=' && src[pos] == '\n')
								break;
						}
						startpos = pos;
						if (firstparaonly)
							return bfr.To_bry();
					}
					break;
				case '_':
					if ((pos > 1 && src[pos-2] == '\n') || (pos == 1)) { // '\n_'
						if (pos < src_len && src[pos] == '_')
						bfr.Add_mid(src, startpos, pos-1);
						while (pos + 1 < src_len) { // find next '_\n'
							if (src[pos++] == '_' && src[pos] == '\n')
								break;
						}
						startpos = pos;
					}
					break;
				case '\n':
					int listpos = pos;
					int listcount = 0;
					while (pos < src_len) { 
						b = src[pos];
						if (b == '*' || b == '#' || b == ';' || b == ':') { // remove list characters
							listcount++;
							pos++;
						}
						else
							break;
					}
					if (listcount > 0) {
						if (pos < src_len && src[pos] == ' ')
							pos++;
						bfr.Add_mid(src, startpos, listpos);
						startpos = pos;
					}
					break;
			}
		}
                if (startpos < src_len)
		bfr.Add_mid(src, startpos, src_len);
		return bfr.To_bry();
	}
	public byte[] Search_text(byte[] src, Xoa_ttl ttl) {
		this.ttl = ttl;
		src = Strip_wiki(src, false);
		// now remove '', ''', () and &...; and multiple newlines
		Bry_bfr bfr = Bry_bfr_.New();
		int src_len = src.length;
		int startpos = 0;
		int pos = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '\'':
					int apos_count = 1;
					while (pos < src_len) {
						if (src[pos++] == '\'') {
							apos_count++;
						}
						else
							break;
					}
					if (apos_count > 1) {
						bfr.Add_mid(src, startpos, pos - apos_count - 1);
						if (apos_count == 4) // equiv ''' '
							pos--;
						startpos = pos - 1;
					}
					break;
				case '&': // replace all &...; with a space (upto a max of 10 chars)
					bfr.Add_mid(src, startpos, pos-1);
					startpos = pos;
					while (pos < src_len && pos - startpos < 12) {
						b = src[pos++];
						if (b == ';')
							break;
					}
					if (b == ';') {
						bfr.Add_byte(Byte_ascii.Space);
						startpos = pos;
					}
					else {
						bfr.Add_byte(Byte_ascii.Amp);
						pos = startpos;
					}
					break;
				case '(':
                                    if (pos < src_len) {
					if (src[pos] == ')') {
						bfr.Add_mid(src, startpos, pos-1);
						startpos = pos + 1;
					}
                                    }
					break;
				case '\n':
					// check for multiple \n (only \n\n)
					int nlcount = 1;
					int nlpos = pos;
					while (pos < src_len) { 
						b = src[pos];
						if (b == '\n') {
							nlcount++;
							pos++;
						}
						else
							break;
					}
					if (nlcount > 2) {
						bfr.Add_mid(src, startpos, nlpos+1);
						startpos = pos;
					}
					break;
			}
		}
		bfr.Add_mid(src, startpos, src_len);
		return bfr.To_bry();
	}
	public byte[] First_para(byte[] src, Xoa_ttl ttl) {
		this.ttl = ttl;
		src = Strip_wiki(src, true);
		// now change '' and '''
		Bry_bfr bfr = Bry_bfr_.New();
		int src_len = src.length;
		int startpos;
		int pos = 0;
		boolean inbold = false;
		boolean initalic = false;
		boolean firstbracket = true;
		bfr.Add(Gfh_tag_.P_lhs);
		byte b;
		// remove initial '\n's
		while (pos < src_len) {
			b = src[pos];
			if (b != '\n' && b != '\t' && b != ' ' && b != '|' && b != '}') 
				break;
			pos++;
		}
		startpos = pos;
		while (pos < src_len) {
			b = src[pos++];
			switch (b) {
				case '\'':
					int apos_count = 1;
					while (pos < src_len) {
						if (src[pos++] == '\'') {
							apos_count++;
						}
						else
							break;
					}
					if (apos_count > 1) {
						bfr.Add_mid(src, startpos, pos - apos_count - 1);
						if (apos_count == 2) {
							if (initalic) {
								bfr.Add(Gfh_tag_.I_rhs);
								initalic = false;
							}
							else {
								bfr.Add(Gfh_tag_.I_lhs);
								initalic = true;
							}
						}
						else if (apos_count == 3 || apos_count == 4) {
							if (inbold) {
								bfr.Add(Gfh_tag_.B_rhs);
								inbold = false;
							}
							else {
								bfr.Add(Gfh_tag_.B_lhs);
								inbold = true;
							}
							if (apos_count == 4)
								pos--;	// bold plus apos
						}
						else if (apos_count == 5) {
							if (inbold && initalic) {
								bfr.Add(Gfh_tag_.I_rhs);
								initalic = false;
								bfr.Add(Gfh_tag_.B_rhs);
								inbold = false;
							}
							else {
								bfr.Add(Gfh_tag_.B_lhs);
								inbold = true;
								bfr.Add(Gfh_tag_.I_lhs);
								initalic = true;
							}
							// there are other combos!!
						}
						startpos = pos - 1;
					}
					if (pos < src_len)
						pos--; // because we looked ahead
					break;
				case '(':
					int epos = findclosingbracket(src, src_len, pos);
					if (firstbracket && !inbold) { // Egyptian_Air_Force
						firstbracket = false;
						bfr.Add_mid(src, startpos, pos-1);
						// find matching close bracket
						pos = epos;
						// look for a successive '('
						if (pos + 3 < src_len) {
							if (src[pos] == ' ' && src[pos+1] == '(')
								pos = findclosingbracket(src, src_len, pos + 2);
						}
						startpos = pos;
						//pos--;
					}
					else {
						// check for empty!! 
						epos--;
						int opos = pos;
						boolean spaced = true;
						while (pos < epos) {
							b = src[pos++];
							if (b != ' ' && b != ';' && b != ',' && b != '\'') {
								spaced = false;
								break;
							}
						}
						if (spaced) {
							bfr.Add_mid(src, startpos, opos-1);
							startpos = epos + 1;
							pos = startpos;
						}
						else
							pos = opos;
					}
					break;
				case ',':
				{
					bfr.Add_mid(src, startpos, pos-1);
					int blen = bfr.Len();
					if (blen > 0 && bfr.Bfr()[blen-1] == ' ')
						bfr.Len_(blen - 1);
					startpos = pos - 1;
					break;
				}
				case '"': // convet to &quot;
					bfr.Add_mid(src, startpos, pos-1);
					bfr.Add_str_a7("&quot;");
					startpos = pos;
					break;
				case '\n':
					// ignore
					bfr.Add_mid(src, startpos, pos-1);
					// check for multiple \n (only \n\n)
					int nlcount = 1;
					while (pos < src_len) { 
						b = src[pos];
						if (b == '\n')
							nlcount++;
						else if (b != ' ' && b != '\t') // allow for lines just with spaces
							break;
						pos++;
					}
					if (nlcount > 1) {
						pos = src_len; // break out
					}
					else
						startpos = pos;
					break;
				case '&': // replace all &...; with a space (upto a max of 10 chars)
					bfr.Add_mid(src, startpos, pos-1);
					startpos = pos;
					while (pos < src_len && pos - startpos < 12) {
						b = src[pos++];
						if (b == ';')
							break;
					}
					if (b == ';') {
						bfr.Add_byte(Byte_ascii.Space);
						startpos = pos;
					}
					else {
						bfr.Add_byte(Byte_ascii.Amp);
						pos = startpos;
					}
					break;
			}
		}
		bfr.Add(Gfh_tag_.P_rhs);
		return bfr.To_bry();
	}
}
