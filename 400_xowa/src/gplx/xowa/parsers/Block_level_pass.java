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
package gplx.xowa.parsers; import gplx.*; import gplx.xowa.*;
public class Block_level_pass {
	private static int PARA_NONE = 0, PARA_P = 1, PARA_PRE = 2;
	// lastParagraph either 'p' or 'pre'
	// pendingPTag either '<p>'or '</p><p>'
	private static int PENDING_NONE = 0, PENDING_P = 1, PENDING_P_P = 2;
        private static int PREG_OPENBLOCK = 1, PREG_CLOSEBLOCK = 2, PREG_COLON = 3, PREG_OPENA = 4, PREG_OPENL = 5, PREG_CLOSEL = 6;
        // State constants for the definition list colon extraction
	private static final int
          COLON_STATE_TEXT = 0
	, COLON_STATE_TAG = 1
	, COLON_STATE_TAGSTART = 2
	, COLON_STATE_CLOSETAG = 3
	, COLON_STATE_TAGSLASH = 4
	, COLON_STATE_COMMENT = 5
	, COLON_STATE_COMMENTDASH = 6
	, COLON_STATE_COMMENTDASHDASH = 7
	, COLON_STATE_LC = 8
	;

	private byte[] text;
	private boolean inPre;
	private boolean DTopen;
        private int lastParagraph;

        private static byte[]
          close_p_nl = Bry_.new_a7("</p>\n")
        , close_pre_nl = Bry_.new_a7("</pre>\n")
        , close_p = Bry_.new_a7("</p>")
        , close_pre = Bry_.new_a7("</pre>")
        , open_pre = Bry_.new_a7("<pre>")
        , open_p = Bry_.new_a7("<p>")
        , open_p_p = Bry_.new_a7("</p><p>")
        , br_elem = Bry_.new_a7("<br />")
        , ol_star = Bry_.new_a7("<ul><li>")
        , ol_hash = Bry_.new_a7("<ol><li>")
        , ol_colon = Bry_.new_a7("<dl><dd>")
        , ol_semi = Bry_.new_a7("<dl><dt>")
        , ol_error = Bry_.new_a7("<!-- ERR 1 -->")
        , ni_star = Bry_.new_a7("</li>\n<li>")
        , ni_dt_nl = Bry_.new_a7("</dt>\n")
        , ni_dd_nl = Bry_.new_a7("</dd>\n")
        , ni_dt = Bry_.new_a7("<dt>")
        , ni_dd = Bry_.new_a7("<dd>")
        , ni_error = Bry_.new_a7("<!-- ERR 2 -->")
        , cl_star = Bry_.new_a7("</li></ul>")
        , cl_hash = Bry_.new_a7("</li></ol>")
        , cl_dt = Bry_.new_a7("</dt></dl>")
        , cl_dd = Bry_.new_a7("</dd></dl>")
        , cl_error = Bry_.new_a7("<!-- ERR 3 -->")
        ;
	private boolean hasOpenParagraph() {
		return lastParagraph != PARA_NONE;
	}

	/**
	 * If a pre or p is open, return the corresponding close tag and update
	 * the state. If no tag is open, return an empty string.
	 * @param bool $atTheEnd Omit trailing newline if we've reached the end.
	 * @return string
	 */
	private byte[] closeParagraph() { return closeParagraph(false); }
	private byte[] closeParagraph( boolean atTheEnd ) {
		byte[] result = null;
		if ( hasOpenParagraph() ) {
			if (atTheEnd) {
				if (lastParagraph == PARA_P)
					result = close_p_nl;
				else
					result = close_pre_nl;
			}
			else {
				if (lastParagraph == PARA_P)
					result = close_p;
				else
					result = close_pre;
			}
		}
		inPre = false;
		lastParagraph = PARA_NONE;
		return result;
	}

	/**
	 * getCommon() returns the length of the longest common substring
	 * of both arguments, starting at the beginning of both.
	 *
	 * @param string $st1
	 * @param string $st2
	 *
	 * @return int
	 */
	private int getCommon( int st1_bgn, int st1_end, int st2_bgn, int st2_end) {
		int shorter = (st1_end - st1_bgn < st2_end - st2_end ? st1_end - st1_bgn : st2_end - st2_bgn);
		int i;
		for ( i = 0; i < shorter; ++i ) {
			if ( text[st1_bgn + i] != text[st2_bgn + i] ) {
				break;
			}
		}
		return i;
	}
	/**
	 * Open the list item element identified by the prefix character.
	 *
	 * @param string $char
	 *
	 * @return string
	 */
	private byte[] openList( byte chr ) {
		Bry_bfr result = Bry_bfr_.New();
		result.Add(closeParagraph());
		if ( chr == '*' ) {
			result.Add(ol_star);
		} else if ( chr == '#' ) {
			result.Add(ol_hash);
		} else if ( chr == ':' ) {
			result.Add(ol_colon);
		} else if ( chr == ';' ) {
			result.Add(ol_semi);
			DTopen = true;
		} else {
			result.Add(ol_error);
		}
		return result.To_bry_and_clear();
	}
	/**
	 * Close the current list item and open the next one.
	 * @param string $char
	 *
	 * @return string
	 */
	private byte[] nextItem( byte chr ) {
		if ( chr == '*' || chr == '#' ) {
			return ni_star;
		} else if ( chr == ':' || chr == ';' ) {
			Bry_bfr close = Bry_bfr_.New();
			if ( DTopen ) {
				close.Add(ni_dt_nl);
			}
			else {
				close.Add(ni_dd_nl);
			}
			if ( chr == ';' ) {
				DTopen = true;
				close.Add(ni_dt);
			} else {
				DTopen = false;
				close.Add(ni_dd);
			}
			return close.To_bry_and_clear();
		}
		return ni_error;
	}
	/**
	 * Close the current list item identified by the prefix character.
	 * @param string $char
	 *
	 * @return string
	 */
	private byte[] closeList( byte chr ) {
		byte[] text;
		if ( chr == '*' ) {
			text = cl_star;
		} else if ( chr == '#' ) {
			text = cl_hash;
		} else if ( chr == ':' ) {
			if ( DTopen ) {
				DTopen = false;
				text = cl_dt;
			} else {
				text = cl_dd;
			}
		} else {
			return cl_error;
		}
		return text;
	}

	private boolean compare_prefix(int lastPrefix_bgn, int lastPrefix_end, int prefix_bgn, int prefixLength) {
		if (lastPrefix_end - lastPrefix_bgn != prefixLength)
			return false;
		for (int i = 0; i < prefixLength; i++) {
			byte l = text[lastPrefix_bgn + i];
			byte p = text[prefix_bgn + i];
			if (l != p) {
				if ((l == ';' && p == ':') || (l == ':' && p == ';'))
					continue;
				return false;
			}
		}
		return true;
	}
	private int before_end, after_bgn;
	private Line colon_semicolon(Bry_bfr output, Line t) {
		if ( findColonNoLinks( t ) ) {
			Line t2 = new Line(text, t.Linestart(), before_end);
			t2.Trim();
			output.Add_mid(text, t2.Trimstart(), t2.Trimend()).Add(nextItem( Byte_ascii.Colon ));
			return new Line(text, after_bgn, t.Lineend());
		}
		return t;
	}

	private byte[] execute(byte[] text, boolean lineStart) {
		// Parsing through the text line by line.  The main thing
		// happening here is handling of block-level elements p, pre,
		// and making lists from lines starting with * # : etc.
		int lastPrefix_bgn = -1;
		int lastPrefix_end = -1;
		Bry_bfr output = Bry_bfr_.New();
		DTopen = false;
		this.text = text;
		boolean inBlockElem = false;
		int prefixLength = 0;
		int pendingPTag = PENDING_NONE;
		boolean inBlockquote = false;
		int textlen = text.length;
		int startpoint = 0;
		int linestart = 0;
		int lineend = 0;
		while (startpoint < textlen) {

			linestart = startpoint;
			while (lineend < textlen && text[lineend] != '\n') { lineend++; }
			// here with inputLine starting at linestart and ending lineend
			boolean notLastLine = (lineend != textlen);
			startpoint = lineend + 1;

			// Fix up $lineStart
			if ( !lineStart ) {
				output.Add_mid(text, linestart, lineend);
				lineStart = true;
				continue;
			}
			// * = ul
			// # = ol
			// ; = dt
			// : = dd
			int lastPrefixLength = lastPrefix_end - lastPrefix_bgn;

/*
			// scan line for all tags
			// in the process finding <pre and/or </pre			
			for (int k = linestart; k < lineend; k++) {
				if (text[k] == '<') {
					
				}
			}
			$preCloseMatch = preg_match( '/<\\/pre/i', $inputLine );
			$preOpenMatch = preg_match( '/<pre/i', $inputLine );
*/
			matchpre(linestart, lineend);
			
			Line t; // rest of text
                        int linelength = lineend - linestart;
			// If not in a <pre> element, scan for and figure out what prefixes are there.
			if ( !inPre ) {
				// Multiple prefixes may abut each other for nested lists.
				for (prefixLength = 0; prefixLength < linelength; prefixLength++) {
					byte b = text[linestart + prefixLength];
					if (b == '*' || b == '#' || b == ':' || b == ';')
						continue;
					else
						break;
				}
				// eh?
				// ; and : are both from definition-lists, so they're equivalent
				//  for the purposes of determining whether or not we need to open/close
				//  elements.
				t = new Line(text, linestart + prefixLength, lineend);
				inPre = preOpenMatch;
			} else {
				// Don't interpret any other prefixes in preformatted text
				prefixLength = 0;
				t = new Line(text, linestart, lineend);
			}
			// List generation
			if ( prefixLength > 0 && compare_prefix(lastPrefix_bgn, lastPrefix_end, linestart, prefixLength) ) {
				// Same as the last item, so no need to deal with nesting or opening stuff
				byte lastprefixchar = text[linestart + prefixLength - 1];
				output.Add(nextItem( lastprefixchar ));
				pendingPTag = PENDING_NONE;
				if ( lastprefixchar == ';' ) {
					// The one nasty exception: definition lists work like this:
					// ; title : definition text
					// So we check for : in the remainder text to split up the
					// title and definition, without b0rking links.
					t = colon_semicolon(output, t);
				}
			} else if ( prefixLength > 0 || lastPrefixLength > 0 ) {
				// We need to open or close prefixes, or both.
				// Either open or close a level...
				int commonPrefixLength = getCommon( linestart, prefixLength, lastPrefix_bgn, lastPrefix_end );
				pendingPTag = PENDING_NONE;
				// Close all the prefixes which aren't shared.
				while ( commonPrefixLength < lastPrefixLength ) {
					output.Add(closeList( text[lastPrefix_bgn + lastPrefixLength - 1] ));
					--lastPrefixLength;
				}
				// Continue the current prefix if appropriate.
				if ( prefixLength <= commonPrefixLength && commonPrefixLength > 0 ) {
					output.Add(nextItem( text[linestart + commonPrefixLength - 1] ));
				}
				// Close an open <dt> if we have a <dd> (":") starting on this line
				if ( DTopen && commonPrefixLength > 0 && text[linestart + commonPrefixLength - 1] == ':' ) {
					output.Add(nextItem( Byte_ascii.Colon ));
				}
				// Open prefixes where appropriate.
				if ( lastPrefix_end - lastPrefix_bgn > 0 && prefixLength > commonPrefixLength ) {
					output.Add_byte(Byte_ascii.Nl);
				}
				while ( prefixLength > commonPrefixLength ) {
					byte c = text[linestart + commonPrefixLength];
					output.Add(openList( c ));
					if ( c == ';' ) {
						// @todo FIXME: This is dupe of code above
						t = colon_semicolon(output, t);
					}
					++commonPrefixLength;
				}
				if ( prefixLength == 0 && lastPrefix_end - lastPrefix_bgn > 0 ) {
					output.Add_byte(Byte_ascii.Nl);
				}
				lastPrefix_bgn = linestart;
				lastPrefix_end = linestart + prefixLength;
			}
			// If we have no prefixes, go to paragraph mode.
			if ( prefixLength == 0 ) {
				// No prefix (not in list)--go to paragraph mode
				// @todo consider using a stack for nestable elements like span, table and div
				// P-wrapping and indent-pre are suppressed inside, not outside
				//$blockElems = 'table|h1|h2|h3|h4|h5|h6|pre|p|ul|ol|dl';
				// P-wrapping and indent-pre are suppressed outside, not inside
				/*$antiBlockElems = 'td|th';
				$openMatch = preg_match(
					'/<('
						. "({$blockElems})|\\/({$antiBlockElems})|"
						// Always suppresses
						. '\\/?(tr|dt|dd|li)'
						. ')\\b/iS',
					$t
				);
				$closeMatch = preg_match(
					'/<('
						. "\\/({$blockElems})|({$antiBlockElems})|"
						// Never suppresses
						. '\\/?(center|blockquote|div|hr|mw:)'
						. ')\\b/iS',
					$t
				);*/
				match_open_close(t);
				// Any match closes the paragraph, but only when `!$closeMatch`
				// do we enter block mode.  The oddities with table rows and
				// cells are to avoid paragraph wrapping in interstitial spaces
				// leading to fostered content.
				if ( openMatch || closeMatch ) {
					pendingPTag = PENDING_NONE;
					// Only close the paragraph if we're not inside a <pre> tag, or if
					// that <pre> tag has just been opened
					if ( !inPre || preOpenMatch ) {
						// @todo T7718: paragraph closed
						output.Add( closeParagraph() );
					}
					if ( preOpenMatch && !preCloseMatch ) {
						inPre = true;
					}
					int bqOffset = t.Linestart();
					while ( match3(t, bqOffset) ) {
						inBlockquote = (type == PREG_OPENBLOCK); // is this a close tag?
						bqOffset = pos + size;
					}
					inBlockElem = !closeMatch;
				} else if ( !inBlockElem && !inPre ) {
					t.Trim();
					if ( text[t.Linestart()] == ' '
						&& ( lastParagraph == PARA_PRE || t.Trimlen() != 0 )
						&& !inBlockquote
					) {
						// pre
						if ( lastParagraph != PARA_PRE ) {
							pendingPTag = PENDING_NONE;
							output.Add( closeParagraph() ).Add(open_pre);
							lastParagraph = PARA_PRE;
						}
						t.Increment();
					} else if ( match4(t) ) {
						// T186965: <style> or <link> by itself on a line shouldn't open or close paragraphs.
						// But it should clear $pendingPTag.
						if ( pendingPTag != PENDING_NONE ) {
							output.Add( closeParagraph() );
							pendingPTag = PENDING_NONE;
						}
					} else {
						// paragraph
						if ( t.Trimlen() == 0 ) {
							if ( pendingPTag != PENDING_NONE) {
								if (pendingPTag == PENDING_P)
									output.Add(open_p);
								else
									output.Add(open_p_p);
								output.Add(br_elem);
								pendingPTag = PENDING_NONE;
								lastParagraph = PARA_P;
							} else if ( lastParagraph != PARA_P ) {
								output.Add( closeParagraph() );
								pendingPTag = PENDING_P;
							} else {
								pendingPTag = PENDING_P_P;
							}
						} else if ( pendingPTag != PENDING_NONE) {
								if (pendingPTag == PENDING_P)
									output.Add(open_p);
								else
									output.Add(open_p_p);
							pendingPTag = PENDING_NONE;
							lastParagraph = PARA_P;
						} else if ( lastParagraph != PARA_P ) {
							output.Add( closeParagraph() ).Add(open_p);
							lastParagraph = PARA_P;
						}
					}
				}
			}
			// somewhere above we forget to get out of pre block (T2785)
			if ( preCloseMatch && inPre ) {
				inPre = false;
			}
			if ( pendingPTag == PENDING_NONE ) {
				if ( prefixLength == 0 ) {
					output.Add_mid(text, t.Linestart(), t.Lineend());
					// Add a newline if there's an open paragraph
					// or we've yet to reach the last line.
					if ( notLastLine || hasOpenParagraph() ) {
						output.Add_byte(Byte_ascii.Nl);
					}
				} else {
					// Trim whitespace in list items
					t.Trim();
					output.Add_mid(text, t.Trimstart(), t.Trimend());
				}
			}
		}
		while ( prefixLength > 0 ) {
			/*output.Add( closeList( $prefix2[$prefixLength - 1] ) );*/
			output.Add( closeList( text[linestart + prefixLength - 1] ) );
			--prefixLength;
			// Note that a paragraph is only ever opened when `prefixLength`
			// is zero, but we'll choose to be overly cautious.
			if ( prefixLength == 0 && hasOpenParagraph() ) {
				output.Add_byte(Byte_ascii.Nl);
			}
		}
		output.Add( closeParagraph( true ) );
		return output.To_bry_and_clear();
	}
	/**
	 * Split up a string on ':', ignoring any occurrences inside tags
	 * to prevent illegal overlapping.
	 *
	 * @param string $str The string to split
	 * @param string &$before Set to everything before the ':'
	 * @param string &$after Set to everything after the ':'
	 * @throws MWException
	 * @return string The position of the ':', or false if none found
	 */
//	 before_end, after_bgn
//	private function findColonNoLinks( $str, &$before, &$after ) {
	private boolean findColonNoLinks( Line t ) {
            int ltLevel, lcLevel, len;
		if ( !match1(t) ) {
			// Nothing to find!
			return false;
		}
		if ( type == PREG_COLON ) {
			// Easy; no tag nesting to worry about
			before_end = pos;
			after_bgn = pos + 1;
			return true;
		}
		// Ugly state machine to walk through avoiding tags.
		int state = COLON_STATE_TEXT;
		ltLevel = 0;
		lcLevel = 0;
                pos = t.Linestart();
		len = t.Lineend() - pos;
		for ( int i = pos; i < len; i++ ) {
			byte c = text[i];
			switch ( state ) {
				case COLON_STATE_TEXT:
					switch ( c ) {
						case '<':
							// Could be either a <start> tag or an </end> tag
							state = COLON_STATE_TAGSTART;
							break;
						case ':':
							if ( ltLevel == 0 ) {
								// We found it!
								before_end = i;
								after_bgn = i + 1;
								return true;
							}
							// Embedded in a tag; don't break it.
							break;
						default:
							// Skip ahead looking for something interesting
//							if ( !preg_match( '/:|<|-\{/', $str, $m, PREG_OFFSET_CAPTURE, $i ) ) {
							if ( !match1(t, i) ) {
								// Nothing else interesting
								return false;
							}
							if ( type == PREG_OPENL ) {
								state = COLON_STATE_LC;
								lcLevel++;
								i = pos + 1;
							} else {
								// Skip ahead to next interesting character.
								i = pos - 1;
							}
							break;
					}
					break;
				case COLON_STATE_LC:
					// In language converter markup -{ ... }-
//					if ( !preg_match( '/-\{|\}-/', $str, $m, PREG_OFFSET_CAPTURE, $i ) ) {
						if ( !match2(t, i) ) {
						// Nothing else interesting to find; abort!
						// We're nested in language converter markup, but there
						// are no close tags left.  Abort!
						i = len;
						break;
					} else if ( type == PREG_OPENL ) {
						i = pos + 1;
						lcLevel++;
					} else if ( type == PREG_CLOSEL ) {
						i = pos + 1;
						lcLevel--;
						if ( lcLevel == 0 ) {
							state = COLON_STATE_TEXT;
						}
					}
					break;
				case COLON_STATE_TAG:
					// In a <tag>
					switch ( c ) {
						case '>':
							ltLevel++;
							state = COLON_STATE_TEXT;
							break;
						case '/':
							// Slash may be followed by >?
							state = COLON_STATE_TAGSLASH;
							break;
						default:
							// ignore
					}
					break;
				case COLON_STATE_TAGSTART:
					switch ( c ) {
						case '/':
							state = COLON_STATE_CLOSETAG;
							break;
						case '!':
							state = COLON_STATE_COMMENT;
							break;
						case '>':
							// Illegal early close? This shouldn't happen D:
							state = COLON_STATE_TEXT;
							break;
						default:
							state = COLON_STATE_TAG;
					}
					break;
				case COLON_STATE_CLOSETAG:
					// In a </tag>
					if ( c == '>' ) {
						if ( ltLevel > 0 ) {
							ltLevel--;
						} else {
							// ignore the excess close tag, but keep looking for
							// colons. (This matches Parsoid behavior.)
							/*wfDebug( __METHOD__ . ": Invalid input; too many close tags\n" );*/
						}
						state = COLON_STATE_TEXT;
					}
					break;
				case COLON_STATE_TAGSLASH:
					if ( c == '>' ) {
						// Yes, a self-closed tag <blah/>
						state = COLON_STATE_TEXT;
					} else {
						// Probably we're jumping the gun, and this is an attribute
						state = COLON_STATE_TAG;
					}
					break;
				case COLON_STATE_COMMENT:
					if ( c == '-' ) {
						state = COLON_STATE_COMMENTDASH;
					}
					break;
				case COLON_STATE_COMMENTDASH:
					if ( c == '-' ) {
						state = COLON_STATE_COMMENTDASHDASH;
					} else {
						state = COLON_STATE_COMMENT;
					}
					break;
				case COLON_STATE_COMMENTDASHDASH:
					if ( c == '>' ) {
						state = COLON_STATE_TEXT;
					} else {
						state = COLON_STATE_COMMENT;
					}
					break;
				default:
					/* ??throw new MWException( "State machine error in " . __METHOD__ );*/
                                    break;
			}
		}
		if ( ltLevel > 0 || lcLevel > 0 ) {
			/*wfDebug(
				__METHOD__ . ": Invalid input; not enough close tags " .
				"(level $ltLevel/$lcLevel, state $state)\n"
			);*/
			return false;
		}
		return false;
	}

	private int pos;
	private int size;
	private int type;
	private boolean match1(Line t) { 	return match1(t, t.Linestart()); }	// preg_match( '/:|<|-\{/'
	private boolean match1(Line t, int starting) {
		int ending = t.Lineend();
		pos = starting;
		while (pos < ending) {
			byte b = text[pos];
			if (b == ':') {
				type = PREG_COLON;
				size = 1;
				return true;
			}
			if (b == '<') {
				type = PREG_OPENA;
				size = 1;
				return true;
			}
			if (b == '-') {
				if (text[pos+1] == '{') {
					type = PREG_OPENL;
					size = 2;
					return true;
				}
			}
			pos++;
		}
		return false;
	}

	private boolean match2(Line t) { 	return match1(t, t.Linestart()); }	// preg_match( '/-\{|\}-/'
	private boolean match2(Line t, int starting) {
		int ending = t.Lineend();
		pos = starting;
		while (pos < ending) {
			byte b = text[pos];
			if (b == '-') {
				if (text[pos+1] == '{') {
					type = PREG_OPENL;
					size = 2;
					return true;
				}
			}
			if (b == '}') {
				if (text[pos+1] == '-') {
					type = PREG_CLOSEL;
					size = 2;
					return true;
				}
			}
			pos++;
		}
		return false;
	}

        private byte[] bq = Bry_.new_a7("blockquote");
        private int bqlen = 10;
//		while ( preg_match( '/<(\\/?)blockquote[\s>]/i', $t, $bqMatch, PREG_OFFSET_CAPTURE, $bqOffset )
	private boolean match3(Line t) { 	return match1(t, t.Linestart()); }	// preg_match( '/<(\\/?)blockquote[\s>]/i'
	private boolean match3(Line t, int starting) {
            boolean closing = false;
		int ending = t.Lineend();
		pos = starting;
		while (pos < ending) {
			byte b = text[pos];
			size = 1;
			if (b == '<') {
				if (text[pos + 1] == '/') {
					size = 2;
					closing = true;
				}
				// bq = 'blockquote'
				// bqlen = 10
                                int i;
				for (i = 0; i < bqlen; i++) {
					byte a = text[pos+size];
					byte c = bq[i];
					if (a == c || (a | 32) == (c | 32)) { // case insensitive
						size++;
						continue;
					}
					break;
				}
				if (i != bqlen) {
					pos += size;
					break;
				}
				while (pos+size < ending && text[pos+size] != '>') {
					size++;
				}
				// could run out of text and not find '>'
				if (closing)
					type = PREG_CLOSEBLOCK;
				else
					type = PREG_OPENBLOCK;
				return true;
			}
			pos++;
		}
		return false;
	}
//preg_match( '/^(?:<style\\b[^>]*>.*?<\\/style>\s*|<link\\b[^>]*>\s*)+$/iS', $t )
	private boolean match4(Line t) {
            boolean closing = false;
		int ending = t.Lineend();
		pos = t.Linestart();
                byte b = text[pos];
                // must start with '<'
                if (b != '<')
                    return false;
                // change the foloowing code!!!!!!!!!!!!!!!!
		while (pos < ending) {
			b = text[pos];
			size = 1;
			if (b == '<') {
				if (text[pos + 1] == '/') {
					size = 2;
					closing = true;
				}
				// bq = 'blockquote'
				// bqlen = 10
                                int i;
				for (i = 0; i < bqlen; i++) {
					byte a = text[pos+size];
					byte c = bq[i];
					if (a == c || (a | 32) == (c | 32)) { // case insensitive
						size++;
						continue;
					}
					break;
				}
				if (i != bqlen) {
					pos += size;
					break;
				}
				while (pos+size < ending && text[pos+size] != '>') {
					size++;
				}
				// could run out of text and not find '>'
				if (closing)
					type = PREG_CLOSEBLOCK;
				else
					type = PREG_OPENBLOCK;
				return true;
			}
			pos++;
		}
		return false;
	}

	private boolean preCloseMatch, preOpenMatch;
	private void matchpre(int starting, int ending) {
            boolean closing = false;
		preCloseMatch = false;
		preOpenMatch = false;
		pos = starting;
		while (pos < ending) {
			byte b = text[pos];
			size = 1;
			if (b == '<') {
				if (text[pos + 1] == '/') {
					size = 2;
					closing = true;
				}
				// pre = 'pre'
				// prelen = 3
                                int i;
				for (i = 0; i < 3; i++) {
					byte a = text[pos+size];
					byte c = open_pre[i];
					if (a == c || (a | 32) == (c | 32)) { // case insensitive
						size++;
						continue;
					}
					break;
				}
				if (i != 3) {
					pos += size;
					break;
				}
				if (closing)
					preCloseMatch = true;
				else
					preOpenMatch = true;
				if (preCloseMatch && preOpenMatch)
					return;
				
			//$preCloseMatch = preg_match( '/<\\/pre/i', $inputLine );
			//$preOpenMatch = preg_match( '/<pre/i', $inputLine );
			}
			pos++;
		}
	}
/*
Block
	'table|h1|h2|h3|h4|h5|h6|pre|p|ul|ol|dl'
Antiblock
	'td|th'
Always suppress
	'\\/?(tr|dt|dd|li)'
Never suppress
 '\\/?(center|blockquote|div|hr|mw:)'
 
	if (b == 'b') => lockquote
	if (b == 'c') => enter
	if (b == 'd') => l  ,t,d  ,iv
	if (b == 'h') => 1,2,3,4,5,6  ,r
	if (b == 'm') => w:
	if (b == 'o') => l
	if (b == 'p') => '', 're'
	if (b == 't') => able  ,d,h,r
	if (b == 'u') => l
*/
	private static final int CLASS_NONE = 0, CLASS_BLOCK_ELEM = 1, CLASS_ANTI_BLOCK_ELEM = 2, CLASS_SUPPRESS = 3, CLASS_NEVER = 4;
	private boolean openMatch, closeMatch;

	private void match_open_close(Line t) {
		boolean closing = false;
		openMatch = false;
		closeMatch = false;
		int ending = t.Lineend();
		pos = t.Linestart();
		byte b2;
		while (pos < ending) {
			byte b = text[pos++];
			if (b == '<') {
				if (text[pos] == '/') {
					pos++;
					closing = true;
				}
				int tkn_class = CLASS_NONE;
				b = text[pos]; // position NOT moved
				switch (b) {
					case 'b': case 'B':
						b2 = text[++pos];
						if (b2 == 'l' || b2 == 'L') {
							b2 = text[++pos];
							if (b2 == 'o' || b2 == 'O') {
								b2 = text[++pos];
								if (b2 == 'c' || b2 == 'C') {
									b2 = text[++pos];
									if (b2 == 'k' || b2 == 'K') {
										b2 = text[++pos];
										if (b2 == 'q' || b2 == 'Q') {
											b2 = text[++pos];
											if (b2 == 'u' || b2 == 'U') {
												b2 = text[++pos];
												if (b2 == 'o' || b2 == 'O') {
													b2 = text[++pos];
													if (b2 == 't' || b2 == 'T') {
														b2 = text[++pos];
														if (b2 == 'e' || b2 == 'E') {
															tkn_class = CLASS_NEVER;
														}
													}
												}
											}
										}
									}
								}
							}
						}
						break;
					case 'c': case 'C':
						b2 = text[++pos];
						if (b2 == 'e' || b2 == 'E') {
							b2 = text[++pos];
							if (b2 == 'n' || b2 == 'N') {
								b2 = text[++pos];
								if (b2 == 't' || b2 == 'T') {
									b2 = text[++pos];
									if (b2 == 'e' || b2 == 'E') {
										b2 = text[++pos];
										if (b2 == 'r' || b2 == 'R') {
											tkn_class = CLASS_NEVER;
										}
									}
								}
							}
						}
						break;
					case 'd': case 'D':
						switch (text[++pos]) {
							case 'l': case 'L': tkn_class = CLASS_BLOCK_ELEM; break;
							case 't': case 'T':
							case 'd': case 'D': tkn_class = CLASS_SUPPRESS; break;
							case 'i': case 'I':
								b2 = text[++pos];
								if (b2 == 'v' || b2 == 'V')
									tkn_class = CLASS_NEVER;
								break;
						}
						break;
					case 'h': case 'H':
						switch (text[++pos]) {
							case '1': case '2': case '3': case '4': case '5': case '6': tkn_class = CLASS_BLOCK_ELEM; break;
							case 'r': case 'R': tkn_class = CLASS_NEVER; break;
						}
						break;
					case 'm': case 'M':
						b2 = text[++pos];
						if (b2 == 'w' || b2 == 'W') {
							if (text[++pos] == ':') {
								tkn_class = CLASS_NEVER;
								// find forward '>' and drop back one (for the test later)
							}
						}
						break;
					case 'o': case 'O':
						b2 = text[++pos];
						if (b2 == 'l' || b2 == 'L') {
							tkn_class = CLASS_BLOCK_ELEM;
						}
						break;
					case 'p': case 'P':
						b2 = text[++pos];
						if (b2 == ' ' || b2 == '\t' || b2 == '>') {
							tkn_class = CLASS_BLOCK_ELEM;
							pos--; // for the test
						}
						else if (b2 == 'r' || b2 == 'R') {
							b2 = text[++pos];
							if (b2 == 'e' || b2 == 'E') {
								tkn_class = CLASS_BLOCK_ELEM;
							}
						}
						break;
					case 't': case 'T':
						switch (text[++pos]) {
							case 'a': case 'A':
								b2 = text[++pos];
								if (b2 == 'b' || b2 == 'B') {
									b2 = text[++pos];
									if (b2 == 'l' || b2 == 'L') {
										b2 = text[++pos];
										if (b2 == 'e' || b2 == 'E') {
											tkn_class = CLASS_BLOCK_ELEM;
										}
									}
								}
								break;
							case 'd': case 'D':
							case 'h': case 'H': tkn_class = CLASS_ANTI_BLOCK_ELEM; break;
							case 'r': case 'R': tkn_class = CLASS_SUPPRESS; break;
						}
						break;
					case 'u': case 'U':
						b2 = text[++pos];
						if (b2 == 'l' || b2 == 'L') {
							tkn_class = CLASS_BLOCK_ELEM;
						}
						break;
				}
				b2 = text[pos];
				// must end properly
				if (b2 != ' ' && b2 != '\t' && b2 != '>') {
					tkn_class = CLASS_NONE;
				}
			
				if (tkn_class != CLASS_NONE) {
					if (tkn_class == CLASS_SUPPRESS)
						openMatch = true;
					else if (tkn_class == CLASS_NEVER)
						closeMatch = true;
					else {
						if (closing) {
							if (tkn_class == CLASS_ANTI_BLOCK_ELEM)
								openMatch = true;
							else if (tkn_class == CLASS_BLOCK_ELEM)
								closeMatch = true;
						} else {
							if (tkn_class == CLASS_ANTI_BLOCK_ELEM)
								closeMatch = true;
							else if (tkn_class == CLASS_BLOCK_ELEM)
								openMatch = true;
						}
					}
					if (openMatch && closeMatch)
						return;  // break out early
				}
			}
		}
	}

}
class Line {
	private static int linestart, lineend, linelen;
	private byte[] text;
	private static int trimstart, trimend, trimlen;
	public int Linestart() { return linestart; }
	public int Lineend() { return lineend; }
	public int Trimstart() { return trimstart; }
	public int Trimend() { return trimend; }
	public int Trimlen() { return trimlen; }
	Line(byte[] text, int linestart, int lineend) {
		this.linestart = linestart;
		this.lineend = lineend;
		linelen = lineend - linestart;
		this.text = text;
	}
	// Trim whitespace
	public void Trim() {
		trimstart = linestart;
		while (trimstart < lineend) {
			byte b = text[trimstart];
			if (b == ' ' || b == '\t')
				trimstart++;
			else
				break;
		}
		int t3 = lineend - 1;
		while (t3 > trimstart) {
			byte b = text[t3];
			if (b == ' ' || b == '\t')
				t3--;
			else
				break;
		}
		trimend = t3 + 1;
		trimlen = trimend - trimstart;
	}
	public void Increment() { linestart++; }
}

