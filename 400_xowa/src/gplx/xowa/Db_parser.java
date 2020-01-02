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
package gplx.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Db_parser {
	private byte[] m_src;
	private int m_src_end, m_pos, m_start, m_tail;
	private Bry_bfr m_newsrc = Bry_bfr_.New();
	private Xop_ctx ctx;

	public byte[] stripcomments(byte[] src) {
		m_src = src;
		m_src_end = src.length;
		m_newsrc.Clear();
		m_pos = 0;
		byte[] nsrc = removecomments();
		return nsrc;
	}
	private byte[] removecomments() {
		int start_text = m_pos;
		while (m_pos < m_src_end) {
			byte b = m_src[m_pos];
			if (b == '<') {
				int curpos = m_pos;
				if (match_opencomment()) {
					addtext(start_text, curpos);
					// skip the comment
					start_text = m_pos;
				}
				else
					m_pos++;
			}
			else
				m_pos++;
		}
		if (start_text > 0) {
			addtext(start_text, m_src_end);
			return m_newsrc.To_bry_and_clear();
		}
		return m_src;
	}

	public byte[] firstpass(Xop_ctx ctx, byte[] src) {
		m_src = src;
		this.ctx = ctx;
		m_src_end = src.length;
		m_newsrc = Bry_bfr_.New();
		m_pos = 0;
		byte[] nsrc = pass1();
		m_src = nsrc;
		m_src_end = nsrc.length;
		m_pos = 0;
		m_newsrc.Clear();
		nsrc = pass2();
		return nsrc;
	}
	
	private boolean match_opencomment() {
		boolean startnl = false;
		int savedpos = m_pos;
		if (savedpos == 0 || m_src[savedpos-1] == '\n')
			startnl = true;
		m_pos++;
		m_start = -1;
		if (m_pos + 3 >= m_src_end) return false;
		if (m_src[m_pos] == '!' && m_src[m_pos+1] == '-' && m_src[m_pos+2] == '-') {
			m_pos += 3;
			m_start = m_pos;
			while (m_pos < m_src_end) {
				byte b = m_src[m_pos];
				if (b == '-' && m_pos + 3 < m_src_end) {
					if (m_src[m_pos+1] == '-' && m_src[m_pos+2] == '>') {
						m_tail = m_pos;
						m_pos += 3;
						// is it on its own line
						if (m_pos < m_src_end && m_src[m_pos] == '\n' && startnl) {
							m_pos++;
							// chew up all following blank lines???????
							while (m_pos < m_src_end) {
								int cur_pos = m_pos;
								while (cur_pos < m_src_end && m_src[cur_pos] == ' ') 
									cur_pos++;
								if (m_src[cur_pos] == '\n')
									m_pos = cur_pos + 1;
								else
									break;
							}
						}
						return true;
					}
				}
				m_pos++;
			}
		}
		m_pos = savedpos;
		return false;
	}

	private boolean match(byte[] element) {
		int element_end = element.length;
		for (int j = 0; j < element_end; j++) {
		  byte c = element[j];
		  byte s = m_src[m_pos];
			if (c != s && (c | 32) != (s | 32))
				return false;
			else
				m_pos++;
		}
		// skip whitespace
		byte s = m_src[m_pos];
		while (m_pos < m_src_end) {
			if (s == ' ' || s == '\n' || s == '\t') {
				m_pos++;
				s = m_src[m_pos];
			}
			else
				break;
		}
		if (s != Byte_ascii.Angle_end)
			return false;
		else {
			m_pos++;
			m_start = m_pos;
			return true;
		}
	}

	private boolean scan_for(byte[] element) {
		int depth = 0;
		int lastclose_bgn = -1, lastclose_end = -1;
		int element_len = element.length; // '</' element '>'
		int last_pos = m_src_end - element_len  - 3; // no point in searching after this pos
		int i = m_pos, j;
		boolean closer;
		int closepos;
		while (i < last_pos) {
			byte b = m_src[i];
			if (b != Byte_ascii.Angle_bgn) {
				i++;
				continue;
			}
			closepos = i;
			i++;
			b = m_src[i];
			if (b == Byte_ascii.Slash) {// must be a close
				closer = true;
				i++;
			}
			else
				closer = false;
			// allow for different case
			for (j = 0; j < element_len; j++) {
				byte c = element[j];
				byte s = m_src[i];
				if (c != s && (c | 32) != (s | 32))
					break;
				else
					i++;
			}
			if (j == element_len) {
				if (closer) {
					// skip whitespace
					byte s = m_src[i];
					while (i < last_pos) {
						if (s == ' ' || s == '\n' || s == '\t') {
							i++;
							s = m_src[i];
						}
						else
							break;
					}
					if (s != Byte_ascii.Angle_end)
						continue; // not what we are looking for
					i++;
					if (depth == 0) {
						m_tail = closepos;
						m_pos = i;
						return true;
					}
					else {
						--depth;
						lastclose_bgn = closepos;
						lastclose_end = i;
					}
				}
				else { // must be opener
					// forward for close angle
					// strictly - could be a prefix of another element
					b = m_src[i];
					if (b == Byte_ascii.Angle_end)
						i++;
					else {
						if (b != ' ' && b != '\t' && b != '\n') // strictly whitespace
							continue; // not what we ar looking for
						while (i < m_src_end) { // NB whole of src
							if (m_src[i] != Byte_ascii.Angle_end)
								i++;
							else
								break;
						}
					}
					if (i == m_src_end) { // not found at all
						break;
					}
					if (m_src[i -2] == Byte_ascii.Slash) {}
					else
						++depth;
				}
			}
		}
		if (lastclose_bgn > 0) {
			m_tail = lastclose_bgn;
			m_pos = lastclose_end;
			return true;
		}
		return false;
	}

	private static final byte[]
	  bry_nowiki = Bry_.new_a7("nowiki")
	, bry_noinclude = Bry_.new_a7("noinclude")
	, bry_includeonly = Bry_.new_a7("includeonly")
	, bry_onlyinclude = Bry_.new_a7("onlyinclude")
        ;
	private boolean match_it(byte[] element) {
		int savedpos = m_pos;
		m_pos++; // skip '<'
		m_start = -1;
		if (m_pos + element.length + 1 >= m_src_end) return false;

		if (match(element)) {
			if (scan_for(element))
				return true;
		}
		m_pos = savedpos;
		return false;
	}
	private boolean match_nowiki() {
		int savedpos = m_pos;
		m_start = -1;
		if (m_pos + 7 >= m_src_end) return false;
		if (match(bry_nowiki)) {
			if (scan_for(bry_nowiki))
				return true;
		}
		m_pos = savedpos;
		return false;
	}

	private boolean match_noinclude() {
		int savedpos = m_pos;
		m_start = -1;
		if (m_pos + 10 >= m_src_end) return false;
		if (match(bry_noinclude)) {
			if (scan_for(bry_noinclude))
				return true;
		}
		m_pos = savedpos;
		return false;
	}

	private void addtext(int start_pos, int end_pos) {
		m_newsrc.Add_mid(m_src, start_pos, end_pos);
	}
	private void adduniq(int start_pos, int end_pos) {
		m_newsrc.Add_mid(m_src, start_pos, end_pos);
	}
	private byte[] pass1() {
		int start_text = m_pos;
		while (m_pos < m_src_end) {
			byte b = m_src[m_pos];
			if (b == '<') {
				int curpos = m_pos;
				if (match_opencomment()) {
					addtext(start_text, curpos);
					// skip the comment
					start_text = m_pos;
				}
				else if (match_it(bry_nowiki)) {
					addtext(start_text, curpos);
					adduniq(m_start, m_tail);
					start_text = m_pos;
				}
				else
					m_pos++;
			}
			else
				m_pos++;
		}
		if (start_text > 0) {
			addtext(start_text, m_src_end);
			return m_newsrc.To_bry_and_clear();
		}
		return m_src;
	}

	private byte[] pass2() {
		int start_text = m_pos;
		while (m_pos < m_src_end) {
			byte b = m_src[m_pos];
			if (b == '<') {
				int curpos = m_pos;
				if (match_it(bry_noinclude)) {
					addtext(start_text, curpos);
					adduniq(m_start, m_tail);
					start_text = m_pos;
				}
				else if (match_it(bry_includeonly)) {
					addtext(start_text, curpos);
					adduniq(m_start, m_tail);
					start_text = m_pos;
				}
				else if (match_it(bry_onlyinclude)) {
					addtext(start_text, curpos);
					adduniq(m_start, m_tail);
					start_text = m_pos;
				}
				else
					m_pos++; // no matching element
			}
			else
				m_pos++;
		}
		if (start_text > 0) {
			addtext(start_text, m_src_end);
			return m_newsrc.To_bry_and_clear();
		}
		return m_src;
	}

	private void gentext(int start_pos, int end_pos) {
		m_newsrc.Add_mid(m_src, start_pos, end_pos);
	}
	private void gencomment(int start_pos, int end_pos) {
		m_newsrc.Add_mid(m_src, start_pos, end_pos);
	}
	private void gennowiki(int start_pos, int end_pos) {
		m_newsrc.Add_mid(m_src, start_pos, end_pos);
	}
	private byte[] genpass1() {
		int start_text = m_pos;
		while (m_pos < m_src_end) {
			byte b = m_src[m_pos];
			if (b == '<') {
				gentext(start_text, m_pos);
				if (match_opencomment()) {
					gencomment(m_start, m_tail);
					start_text = m_pos;
				}
				else if (match_nowiki()) {
					gennowiki(m_start, m_tail);
					start_text = m_pos;
				}
			}
			else
				m_pos++;
		}
		if (start_text > 0) {
			gentext(start_text, m_src_end);
			return m_newsrc.To_bry_and_clear();
		}
		return m_src;
	}

	private byte[] genpass2() {
		while (m_pos + 2 < m_src_end) {
/*			byte code = m_src[m_pos++];
			int siz = getsize();
			if (siz < 0)
				break;
			if (code != TEXT)
				continue;
			if (findkeyword()) {
				copyupto();
				gen new text upto the text(if any)
				
				need to find closing keyword
				 either within the sam text block (single keyspan)
				 else across multiple ones (multi keyspan)
			}*/
		}
		return m_src;
	}
}