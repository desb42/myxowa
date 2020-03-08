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

	private int nowikicheck(byte[] src, int src_len, int pos) {
            return pos;
	}
	private int findclosingsquiggle(byte[] src, int src_len, int pos) {
		int levelcount = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '{':
					levelcount++;
					break;
				case '}':
					if (levelcount == 0) {
						return pos;
					}
					levelcount--;
					break;
				case '<':
					pos = nowikicheck(src, src_len, pos);
					break;
			}
		}
		if (levelcount != 0)
                {int a = 1;}
			// report/note an error
		return pos;
	}
	private boolean check_ns(byte[] src, int bgn, int end) {
		int sz = end - bgn;
		if (sz > 6 && ((src[bgn+1] | 32) == 'f') && src[bgn+5] ==':') { // File:
			return true;
		}
		else if (sz > 7 && ((src[bgn+1] | 32) == 'i') && src[bgn+6] ==':') { // Image:
			return true;
		}
		else if (sz > 10 && ((src[bgn+1] | 32) == 'c') && src[bgn+9] ==':') { // Category:
			return true;
		}
		return false;
	}
	private int findclosingsquare(byte[] src, int src_len, int pos, Bry_bfr bfr) {
		int levelcount = 0;
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
					if (levelcount == 0) {
						endpos = pos;
						pos = src_len; // terminate loop
					}
					levelcount--;
					break;
				case '|':
					barpos = pos;
					break;
			}
		}
		if (levelcount != 0)
                {int a = 1;}
			// report/note an error
		// check for File: and Category: [what about ther langage wikis?]
		if (!check_ns(src, startpos, endpos)) {
			if (barpos > 0)
				bfr.Add_mid(src, barpos, endpos-2);
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
			else if (b == ' ') { // find close (check for self closing)
				nameend = pos - 1;
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
		}
		// now find the close (or a nesting!)
		while (pos < src_len) {
			// find next '<'
			while (pos < src_len) {
				b = src[pos++];
				if (b == '<')
					break;
			}
			if (pos >= src_len)
				return pos; //??
			boolean isclose = false;
			if (src[pos] == '/') {
				isclose = true;
				pos++;
			}
			// is this the same as the opener?
			if (pos + nameend - namestart >= src_len)
				return src_len;
			int i;
			for (i = namestart; i < nameend; i++) {
				if ((src[i] | 32) != (src[pos++] | 32))
					break;
			}
			if (i < nameend) // no match
				continue;
			b = src[pos++];
			if (isclose) {
				if (b != '>')
					return src_len; // fail: skip to end
				if (levelcount == 0)
					return pos;
				levelcount--;
			}
			else {
				if (b == ' ' || b == '>') { // matching keyword
					levelcount++;
				}
				else {
					continue;
				}
			}
		}
                return pos;
	}

	public byte[] Strip_wiki(byte[] src) {
		Bry_bfr bfr = Bry_bfr_.New();
		int src_len = src.length;
		int startpos = 0;
		int pos = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '{':
					b = src[pos];
					if (b == '|') { // check for a table
						bfr.Add_mid(src, startpos, pos-1);
						while (pos < src_len) { // find close table
							b = src[pos++];
							if (b == '|' && pos < src_len && src[pos] == '}') {
								pos++;
								break;
							}
						}
						startpos = pos;
					}
					else if (b == '{') {
						bfr.Add_mid(src, startpos, pos-1);
						pos = findclosingsquiggle(src, src_len, pos);
						startpos = pos;
					}
					break;
				case '[':
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
					break;
				case '<':
					bfr.Add_mid(src, startpos, pos-1);
					pos = findclosingangle(src, src_len, pos);
					startpos = pos;
					break;
				case '=':
					if ((pos > 1 && src[pos-2] == '\n') || (pos == 1)) { // '\n='
						bfr.Add_mid(src, startpos, pos-1);
						while (pos < src_len) { // find next '=\n'
							if (src[pos++] == '=' && src[pos] == '\n')
								break;
						}
						startpos = pos;
					}
					break;
				case '\n':
					int listcount = 0;
					int listpos = pos;
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
						if (src[pos] == ' ')
							pos++;
						bfr.Add_mid(src, startpos, listpos);
						startpos = pos;
					}
					break;
			}
		}
		bfr.Add_mid(src, startpos, src_len);
		return bfr.To_bry();
	}
	public byte[] Search_text(byte[] src) {
		src = Strip_wiki(src);
		// now remove '', ''', () and &...; and multiple newlines
		Bry_bfr bfr = Bry_bfr_.New();
		int src_len = src.length;
		int startpos = 0;
		int pos = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			switch (b) {
				case '\'':
					b = src[pos];
					if (b == '\'') { // more than one '
						bfr.Add_mid(src, startpos, pos-1);
						while (pos < src_len) {
							b = src[pos++];
							if (b != '\'')
								break;
						}
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
					if (src[pos] == ')') {
						bfr.Add_mid(src, startpos, pos-1);
						startpos = pos + 1;
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
	public byte[] First_para(byte[] src) {
		src = Strip_wiki(src);
		// now change '' and '''
		Bry_bfr bfr = Bry_bfr_.New();
		int src_len = src.length;
		int startpos = 0;
		int pos = 0;
		boolean inbold = false;
		boolean initalic = false;
		boolean firstbracket = true;
		boolean startpara = false;
		bfr.Add(Gfh_tag_.P_lhs);
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
						else if (apos_count == 3) {
							if (inbold) {
								bfr.Add(Gfh_tag_.B_rhs);
								inbold = false;
							}
							else {
								bfr.Add(Gfh_tag_.B_lhs);
								inbold = true;
							}
						}
						// could be 5!!
						startpos = pos - 1;
					}
					break;
				case '(':
					if (firstbracket) {
						bfr.Add_mid(src, startpos, pos-1);
						firstbracket = false;
						while (pos < src_len) {
							if (src[pos++] == ')')
								break;
						}
						startpos = pos;
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
						else if (b == ' ') { // allow for lines just with spaces
							int lpos = pos + 1;
							while (lpos < src_len) {
								b = src[lpos++];
								if (b != ' ')
									break;
							}
							if (b == '\n') {
								nlcount++;
								pos = lpos + 1;
							}
							else
								break;
						}
						else
							break;
					}
					if (nlcount > 1) {
						bfr.Add_mid(src, startpos, nlpos+1);
						if (startpara)
							pos = src_len; // break out
						else {
							startpara = true;
							startpos = pos;
						}
					}
					break;
			}
		}
		bfr.Add(Gfh_tag_.P_rhs);
		return bfr.To_bry();
	}
}
