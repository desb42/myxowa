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
package gplx.xowa.xtns.proofreadPage; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*;
import java.util.Dictionary;
import java.util.Hashtable;
import gplx.xowa.parsers.lnkis.files.*;
public class Pp_pagelist_nde implements Xox_xnde {	// TODO_OLD:
	private Xop_root_tkn xtn_root;
	private Page_list lst;
	private int maxpagecount;
	public void Xatr__set(Xowe_wiki wiki, byte[] src, Mwh_atr_itm xatr, Object xatr_id_obj) {}
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		//?boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_hiero, src, xnde);
		lst = Parse(xnde);
		Xoae_page page = ctx.Page();
		xtn_root = null;
				
		if (page.Ttl().Ns().Id() == 106) { // Index namespace only

			Bry_bfr full_bfr = wiki.Utl__bfr_mkr().Get_m001();
			try {
				Hash_adp_bry lst_page_regy = ctx.Lst_page_regy(); 
				if (lst_page_regy == null) lst_page_regy = Hash_adp_bry.cs();	// SEE:NOTE:page_regy; DATE:2014-01-01
				byte[] page_bry = Bld_wikitext(full_bfr, page.Ttl());
				if (page_bry != null)
					xtn_root = Bld_root_nde(full_bfr, lst_page_regy, ctx, wiki, page_bry);	// NOTE: this effectively reparses page twice; needed b/c of "if {| : ; # *, auto add new_line" which can build different tokens
			} finally {
				full_bfr.Mkr_rls();
			}
		}
	}
	//public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;
	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		if (xtn_root == null) return;	// xtn_root is null when Xtn_parse exits early; occurs for recursion; DATE:2014-05-21
		html_wtr.Write_tkn_to_html(bfr, ctx, hctx, xtn_root.Root_src(), xnde, Xoh_html_wtr.Sub_idx_null, xtn_root);
	}

	// expose getFormattedPageNumber
	public byte[] FormattedPageNumber(int i) {
		Page_number pagnum = lst.getNumber(i);
		return pagnum.getFormattedPageNumber();
	}
	private byte[] Bld_wikitext(Bry_bfr bfr, Xoa_ttl ttl) {
		int countsize;
		if (maxpagecount < 10)
			countsize = 1;
		else if (maxpagecount < 100)
			countsize = 2;
		else if (maxpagecount < 1000)
			countsize = 3;
		else
			countsize = 4;

		for (int i = 1; i <= maxpagecount; i++) {
			Page_number pagnum = lst.getNumber(i);
			bfr.Add_str_a7("[[Page:");
			bfr.Add(ttl.Base_txt()).Add_byte_slash().Add_long_variable(i).Add_byte_pipe();
			byte[] view = pagnum.getFormattedPageNumber();
			switch(pagnum.Mode()) {
				case Page_list_row.Display_highroman:
				case Page_list_row.Display_roman:
					//bfr.Add_byte((byte)160); // &#160;
					bfr.Add_str_a7("&#160;");
					break;
				case Page_list_row.Display_normal:
					int paddingSize = countsize - view.length;
					boolean isNumeric = true;
					for (int j = 0; j < view.length; j++) {
						if (view[j] < Byte_ascii.Num_0 || view[j] > Byte_ascii.Num_9)
							isNumeric = false;
					}
					if (isNumeric && paddingSize > 0) {
						bfr.Add_str_a7("<span style=\"visibility:hidden;\">");
						for (int j = 0; j < paddingSize; j++)
							bfr.Add_byte(Byte_ascii.Num_0); // strictly this should be wiki language dependent
						bfr.Add_str_a7("</span>");
					}
					break;
			}

			bfr.Add(view);
			bfr.Add_str_a7("]]\n");
		}
		return bfr.To_bry_and_clear();
	}
	private Xop_root_tkn Bld_root_nde(Bry_bfr page_bfr, Hash_adp_bry lst_page_regy, Xop_ctx ctx, Xowe_wiki wiki, byte[] wikitext) {
		Xop_ctx tmp_ctx = Xop_ctx.New__sub__reuse_lst(wiki, ctx, lst_page_regy);
		tmp_ctx.Page().Ttl_(ctx.Page().Ttl());					// NOTE: must set tmp_ctx.Ttl to ctx.Ttl; EX: Flatland and First World; DATE:2013-04-29
		tmp_ctx.Lnki().File_logger_(Xop_file_logger_.Noop);	// NOTE: set file_wkr to null, else items will be double-counted
		tmp_ctx.Parse_tid_(Xop_parser_tid_.Tid__defn);
		Xop_parser tmp_parser = Xop_parser.new_(wiki, wiki.Parser_mgr().Main().Tmpl_lxr_mgr(), wiki.Parser_mgr().Main().Wtxt_lxr_mgr());
		Xop_root_tkn rv = tmp_ctx.Tkn_mkr().Root(wikitext);
		tmp_parser.Parse_text_to_wdom(rv, tmp_ctx, tmp_ctx.Tkn_mkr(), wikitext, Xop_parser_.Doc_bgn_bos);
		return rv;
	}

	Page_list Parse(Xop_xnde_tkn xnde) {
		List_adp rows = List_adp_.New();
		maxpagecount = 0;
		Mwh_atr_itm[] atrs_ary = xnde.Atrs_ary();
		int atrs_len = atrs_ary.length;
		byte[] key, val;
		for (int i = 0; i < atrs_len; i++) {
			Mwh_atr_itm atr = atrs_ary[i];
			if (atr.Eql_pos()< 0)
				continue;
			if (atr.Valid()) {
				key = atr.Key_bry();
				val = atr.Val_as_bry();
			} else {
				if (atr.Eql_pos() > 0) {
					key = Bry_.Mid(atr.Src(), atr.Atr_bgn(), atr.Eql_pos());
					val = Bry_.Mid(atr.Src(), atr.Eql_pos() + 1, atr.Atr_end());
				}
				else
					continue;
			}
			Page_list_row plr = new Page_list_row(key, val);
			rows.Add(plr);
			if (plr.To() > maxpagecount)
				maxpagecount = plr.To();
		}
		return new Page_list(rows);
	}
}

class Page_list_row {
	private byte[] displaytext;
	private int type;
	private int display;
	private int from, to;
	private int offset;
	public int Display() { return display; }
	public int Mode() { return type; }
	public int From() { return from; }
	public int To() { return to; }
	public int Offset() { return offset; }
	public byte[] Displaytext() { return displaytext; }

	private int parseInt(byte[] src, int bgn, int end) {
		return Bry_.To_int_or(src, bgn, end, 0);
	}
	private boolean compare(byte[] src, int bgn, byte[] txt) {
			if (src.length < txt.length) return false; // exact match
		return Bry_.Has_at_bgn(src, txt, bgn, bgn + txt.length);
	}
	Page_list_row(byte[] key , byte[] val) {
		displaytext = null;
		display = Display_normal;
		type = Mode_unknown;
		int i, end = key.length;
		int pos = 0;
		for (i = 0; i < end; i++) {
			if (!Byte_ascii.Is_num(key[i])) {
				break;
			}
		}
		if (i >= end) {
			from = parseInt(key, 0, i);
			to = from;
			type = Mode_numeric;
		} else {
			if (key[i] == 't' && key[i+1] == 'o') {
				from = parseInt(key, 0, i);
				pos = i+2;
				for (i = pos; i < end; i++) {
					if (!Byte_ascii.Is_num(key[i])) {
						break;
					}
				}
				if (i == pos) return; // no digits
				to = parseInt(key, pos, i);
				if (i == end) {
					type = Mode_range;
				} else {
					if (i + 4 < end && key[i] == 'e' && key[i+1] == 'v' && key[i+2] == 'e' && key[i+3] == 'n') {
						type = Mode_even;
					}
					else if (i + 3 < end && key[i] == 'o' && key[i+1] == 'd' && key[i+2] == 'd') {
						type = Mode_odd;
					}
				}
		  }
		}
		if (type == Mode_unknown) return;
		end = val.length;
		if (type == Mode_numeric) {
			for (i = 0; i < end; i++) {
				if (!Byte_ascii.Is_num(val[i])) {
					break;
				}
			}
			if (i >= end) {
				offset = parseInt(val, 0, i);
				return;
			}
			type = Mode_range;
		}
		if (compare(val, 0, Bry_.new_a7("normal")))
			display = Display_normal;
		else if (compare(val, 0, Bry_.new_a7("roman")))
			display = Display_roman;
		else if (compare(val, 0, Bry_.new_a7("highroman")))
			display = Display_highroman;
		else if (compare(val, 0, Bry_.new_a7("folio")))
			display = Display_folio;
		else if (compare(val, 0, Bry_.new_a7("folioroman")))
			display = Display_folioroman;
		else if (compare(val, 0, Bry_.new_a7("foliohighroman")))
			display = Display_foliohighroman;
		else if (compare(val, 0, Bry_.new_a7("empty")))
			display = Display_empty;
		else {
			display = Display_normal;
			displaytext = new byte[end];
			for (i = 0; i < end; i++)
				displaytext[i] = val[i];
		}
	}
	public static final int
	  Display_normal = 0
	, Display_roman = 1
	, Display_highroman = 2
	, Display_folio = 3
	, Display_folioroman = 4
	, Display_foliohighroman = 5
	, Display_empty = 6
	, Mode_unknown = 0
	, Mode_numeric = 1
	, Mode_range = 2
	, Mode_even = 3
	, Mode_odd = 4
	;
}
class Page_number {
	private int displayednumber;
	private byte[] displayedtext;
	private int mode;
	private boolean isEmpty;
	private boolean isRecto;
	public int Mode() { return mode; }

	Page_number( int displayednumber, byte[] displayedtext, int mode, boolean isEmpty, boolean isRecto ) {
		this.displayednumber = displayednumber;
		this.displayedtext = displayedtext;
		this.mode = mode;
		this.isEmpty = isEmpty;
		this.isRecto = isRecto;
	}
	// Returns the formatted page number as it should be displayed
	public byte[] getFormattedPageNumber() { // should be language sensitive
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		switch ( mode ) {
			case Page_list_row.Display_highroman:
				Pfxtp_roman.ToRoman(displayednumber, tmp_bfr, false);
				break;
			case Page_list_row.Display_roman:
				Pfxtp_roman.ToRoman(displayednumber, tmp_bfr, true);
				break;
			case Page_list_row.Display_normal:
				if (displayedtext != null)
					tmp_bfr.Add(displayedtext);
				else
					tmp_bfr.Add_long_variable(displayednumber);
				break;
			case Page_list_row.Display_folio:
				tmp_bfr.Add_long_variable(displayednumber);
				formatRectoVerso(tmp_bfr);
				break;
			case Page_list_row.Display_foliohighroman:
				Pfxtp_roman.ToRoman(displayednumber, tmp_bfr, false);
				formatRectoVerso(tmp_bfr);
				break;
			case Page_list_row.Display_folioroman:
				Pfxtp_roman.ToRoman(displayednumber, tmp_bfr, true);
				formatRectoVerso(tmp_bfr);
				break;
			default:
				tmp_bfr.Add(displayedtext);
				break;
		}
		return tmp_bfr.To_bry_and_clear_and_rls();
	}
	private void formatRectoVerso(Bry_bfr tmp_bfr) {
		if (isRecto)
			tmp_bfr.Add_str_a7("<sup>r</sup>");
		else
			tmp_bfr.Add_str_a7("<sup>v</sup>");
	}
	private void rawRectoVerso(Bry_bfr tmp_bfr) {
		if (isRecto)
			tmp_bfr.Add_str_a7("r");
		else
			tmp_bfr.Add_str_a7("v");
	}
}
class Page_list {
	static List_adp pagelistrows;
	static Dictionary<Integer, Page_number> pagenumbers;

	Page_list(List_adp pagelist) {
		this.pagelistrows = pagelist;
		pagenumbers = new Hashtable<Integer, Page_number> ();
	}
	public static Page_number getNumber(int num) {
		Page_number pn = pagenumbers.get(num);
		if (pn != null)
			return pn;
		pn = buildNumber(num);
		pagenumbers.put(num, pn);
		return pn;
	}
	private static Page_number buildNumber(int num) {
		int mode = Page_list_row.Display_normal; // default mode
		int offset = 0;
		byte [] displayedpagetext = null;
		int displayednumber = 0;
		boolean isEmpty = false;
		boolean isRecto = true;
		int len = pagelistrows.Count();
		for (int i = 0; i < len; i++) {
			Page_list_row pnum = (Page_list_row)pagelistrows.Get_at(i);
			if (pnum.Mode() != Page_list_row.Mode_unknown) {
				if (pnum.Mode() == Page_list_row.Mode_numeric && pnum.From() <= num) {
					offset = pnum.From() - pnum.Offset();
				}
				
				if (numberInRange(pnum, num)) {
					if (pnum.Display() == Page_list_row.Display_empty)
						isEmpty = true;
					else {
						mode = pnum.Display();
						displayedpagetext = pnum.Displaytext();
					} 

					if (pnum.Display() == Page_list_row.Display_folio ||
						pnum.Display() == Page_list_row.Display_folioroman ||
						pnum.Display() == Page_list_row.Display_foliohighroman) {
						int folioStart = pnum.From();
						displayednumber = folioStart - offset + (num - folioStart)/2;
						isRecto = (num - folioStart) % 2 == 0;
					}
				}
			}
		}
		// displayedpagenumber check!!!
		if (displayednumber == 0)
			displayednumber = num - offset;
		return new Page_number( displayednumber, displayedpagetext, mode, isEmpty, isRecto );
	}

	private static boolean numberInRange( Page_list_row pnum, int num ) {
		// in range?
		if (num < pnum.From()) return false;
		if (pnum.To() < num) return false;
		if (pnum.Mode() == Page_list_row.Mode_range) return true;
		// if even must be an even
		if (pnum.Mode() == Page_list_row.Mode_even && num % 2 == 0) return true;
		// check for odd
		if (pnum.Mode() == Page_list_row.Mode_odd && num % 2 == 1) return true;
		return false;
	}
}
