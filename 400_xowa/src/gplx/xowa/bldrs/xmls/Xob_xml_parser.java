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
package gplx.xowa.bldrs.xmls; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.core.btries.*; import gplx.core.ios.*; import gplx.core.times.*;
import gplx.xowa.wikis.data.tbls.*; import gplx.xowa.wikis.nss.*;
public class Xob_xml_parser {
	Btrie_fast_mgr trie = Xob_xml_parser_.trie_(); Bry_bfr data_bfr = Bry_bfr_.New(); DateAdp_parser date_parser = DateAdp_parser.new_();
	Xob_xml_parse_model_format model_format = new Xob_xml_parse_model_format();
	public Xob_xml_parser Tag_len_max_(int v) {tag_len_max = v; return this;} private int tag_len_max = 255; // max size of any (a) xml tag, (b) int or (c) date; everything else goes into a data_bfr
	public Xob_xml_parser Data_bfr_len_(int v) {data_bfr.Resize(v); return this;} // PERF: resize data_bfr once to large size, rather than grow incremently to it
	public Xob_xml_parser Trie_tab_del_() {trie.Del(Xob_xml_parser_.Bry_tab); return this;}
	public int Parse_page(Xowd_page_itm rv, Gfo_usr_dlg usr_dlg, Io_buffer_rdr fil, byte[] src, int pos, Xow_ns_mgr ns_mgr) {
		rv.Clear();
		int src_len = fil.Bfr_len(), data_bgn = -1, page_bgn = -1;
		boolean data_bfr_add = false, page_id_needed = true, title_needed = true, reading = true;
		int[] modified_on_ary = new int[7];
		while (reading) {
			if (	pos + tag_len_max > src_len			// near end of src
				&&	!fil.Fil_eof())	 {					// not at fil end
				int refill_pos = 0;
				if (page_bgn == -1) {					// keep page in same data_bfr; NOTE: needed else timestamp/id may fail
					refill_pos = pos;
					pos = 0;
				}
				else {
					refill_pos = page_bgn;
					data_bgn -= page_bgn;
					pos -= page_bgn;
				}
				fil.Bfr_load_from(refill_pos);		// refill src from pos; 
				src_len = fil.Bfr_len();
			}
			if (pos >= src_len) return Bry_find_.Not_found;	// no more src left; should only happen at end of file
			byte b = src[pos];
			Object o = trie.Match_bgn_w_byte(b, src, pos, src_len);
			if (o == null) {								// text_data; not an xml_nde (<id>), xml_escape (&lt;), or tab
				if (data_bfr_add) data_bfr.Add_byte(b);		// add to src if data_bfr_add is on (only happens for <title>, <text>)
				++pos;
			}
			else {											// is xml_nde, xml_escape, or tab
				Xob_xml_parser_itm itm = (Xob_xml_parser_itm)o;
				int hook_bgn = pos;							// mark old pos
				pos += itm.Hook_len();						// calc new pos
				switch (itm.Tid()) {
					case Xob_xml_parser_.Id_page_bgn:		page_bgn = hook_bgn; break;
					case Xob_xml_parser_.Id_id_bgn:			if (page_id_needed) data_bgn = pos; break;	// only flag if first <id>; note that 1st <id> always belongs to <page>;
					case Xob_xml_parser_.Id_id_end:	
						if (page_id_needed) {
							int page_id = Bry_.To_int_or(src, data_bgn, hook_bgn, -1); if (page_id == -1) usr_dlg.Warn_many(GRP_KEY, "page_id_invalid", "page_id_is_invalid: ~{0}", String_.new_u8(src, data_bgn, hook_bgn));
							rv.Id_(page_id);
							page_id_needed = false;		// turn off for other <id> tags (<contributor>; <revision>)
						}
						break;
					case Xob_xml_parser_.Id_timestamp_bgn:  data_bgn = pos; break;
					case Xob_xml_parser_.Id_timestamp_end:
						date_parser.Parse_iso8651_like(modified_on_ary, src, data_bgn, hook_bgn);
						rv.Modified_on_(DateAdp_.seg_(modified_on_ary));
						break;
					case Xob_xml_parser_.Id_model_bgn:  data_bgn = pos; break;
					case Xob_xml_parser_.Id_model_end:
						rv.Model_( model_format.Parse_model(src, data_bgn, hook_bgn, rv.Ttl_full_db()) );
						break;
					case Xob_xml_parser_.Id_format_bgn:  data_bgn = pos; break;
					case Xob_xml_parser_.Id_format_end:
						rv.Format_( model_format.Parse_format(src, data_bgn, hook_bgn, rv.Ttl_full_db()) );
						break;
					case Xob_xml_parser_.Id_title_bgn:		if (title_needed) data_bfr_add = true; break;
					case Xob_xml_parser_.Id_text_bgn:		data_bfr_add = true; break;
					case Xob_xml_parser_.Id_title_end:
						if (title_needed) {
							data_bfr_add = false;
							byte[] ttl = data_bfr.To_bry_and_clear();
							Bry_.Replace_reuse(ttl, Byte_ascii.Space, Byte_ascii.Underline);
							rv.Ttl_(ttl, ns_mgr);
							title_needed = false;
						}
						break;
					case Xob_xml_parser_.Id_text_end:		data_bfr_add = false; rv.Text_(data_bfr.To_bry_and_clear()); break;
					case Xob_xml_parser_.Id_amp: case Xob_xml_parser_.Id_quot: case Xob_xml_parser_.Id_lt: case Xob_xml_parser_.Id_gt:
					case Xob_xml_parser_.Id_cr_nl: case Xob_xml_parser_.Id_cr:
						if (data_bfr_add) data_bfr.Add_byte(itm.Subst_byte());
						break;
					case Xob_xml_parser_.Id_tab: 
						if (data_bfr_add) data_bfr.Add(itm.Subst_ary());	// NOTE: tab can exist in xml; see en.wiktionary.org_20120109: "<page>\n    <title>Thread:User talk:Yair rand/newentrywiz.js/quiashed</title>\n    <id>2578382</id>\n<DiscussionThreading>\n\t<ThreadSubject>
						break;
					case Xob_xml_parser_.Id_page_end:		reading = false; page_bgn = -1; break;
					case Xob_xml_parser_.Id_page_bgn_frag:
						pos = Find_gt(src, src_len, pos);
						/*warn*/
						break;
					case Xob_xml_parser_.Id_title_bgn_frag:
						if (title_needed) {
							pos = Find_gt(src, src_len, pos) + 1;	// +1 to get next character 
							if (!gt_was_inline) data_bfr_add = true;
						}
						break;
					case Xob_xml_parser_.Id_text_bgn_frag:
						pos = Find_gt(src, src_len, pos) + 1;	// +1 to get next character 
						if (!gt_was_inline) data_bfr_add = true;
						break;
					case Xob_xml_parser_.Id_id_bgn_frag: case Xob_xml_parser_.Id_timestamp_bgn_frag:
						data_bgn = pos; /*warn*/
						break;
					default:								throw Err_.new_unhandled(itm.Tid());	// shouldn't happen
				}
			}
		}
		return pos;
	}
	int Find_gt(byte[] src, int src_len, int src_pos) {
		gt_was_inline = false;
		while (src_pos < src_len) {
			switch (src[src_pos]) {
				case Byte_ascii.Slash:	gt_was_inline = true; break;
				case Byte_ascii.Gt:		return src_pos;
				default:				gt_was_inline = false; break;
			}
			++src_pos;
		}
		return -1;
	}	boolean gt_was_inline = false;
	static final String GRP_KEY = "xowa.bldrs.xmls.xml_parser";
}
class Xob_xml_parse_model_format {

//1      <model>wikitext</model>
//2      <model>Scribunto</model>
//3      <model>css</model>
//4      <model>javascript</model>
//5      <model>sanitized-css</model>
//6      <model>MassMessageListContent</model> // species
//7      <model>flow-board</model> // species, wikidata
//8      <model>proofread-page</model> // wikisource
//9      <model>EntitySchema</model> // wikidata
//10     <model>Campaign</model> // commons
//11     <model>Tabular.JsonConfig</model> // commons
//
//1      <format>text/x-wiki</format>
//2      <format>text/plain</format>
//3      <format>text/css</format>
//4      <format>text/javascript</format>

	public int Parse_model(byte[] src, int bgn, int end, byte[] ttl) {
		byte b = src[bgn];
		switch (b) {
			case 'w': return 1;
			case 'S': return 2;
			case 'c': return 3;
			case 'j': return 4;
			case 's': return 5;
			case 'M': return 6;
			case 'f': return 7;
			case 'p': return 8;
			case 'E': return 9;
			case 'C': return 10; // Campaign
			case 'T': return 11; // Tabular.JsonConfig
			default:
				Gfo_usr_dlg_.Instance.Log_many("", "", "Model unknown: model=~{0} ttl=~{1}", Bry_.Mid(src, bgn, end), ttl);
				return 12;
		}
	}
	public int Parse_format(byte[] src, int bgn, int end, byte[] ttl) {
		byte b = src[bgn + 5];
		switch (b) {
			case 'x': return 1;
			case 'p': return 2;
			case 'c': return 3;
			case 'j': return 4;
			default:
				Gfo_usr_dlg_.Instance.Log_many("", "", "Format unknown: format=~{0} ttl=~{1}", Bry_.Mid(src, bgn, end), ttl);
				return 5;
		}
	}
}
