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
package gplx.xowa.htmls.core.htmls;

import gplx.Bool_;
import gplx.Bry_bfr;
import gplx.Byte_ascii;
import gplx.Err_;
import gplx.Gfo_usr_dlg_;
import gplx.langs.htmls.Gfh_tag_;
import gplx.langs.htmls.entitys.Gfh_entity_;
import gplx.xowa.Xoae_app;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.htmls.Xow_html_mgr;
import gplx.xowa.htmls.core.wkrs.hdrs.Xoh_hdr_html;
import gplx.xowa.htmls.core.wkrs.lnkes.Xoh_lnke_html;
import gplx.xowa.htmls.core.wkrs.magics.Xoh_magic_html;
import gplx.xowa.htmls.core.wkrs.lnkis.htmls.Xoh_lnki_wtr;
import gplx.xowa.langs.kwds.Xol_kwd_grp_;
import gplx.xowa.parsers.Xop_ctx;
import gplx.xowa.parsers.Xop_root_tkn;
import gplx.xowa.parsers.Xop_tkn_grp;
import gplx.xowa.parsers.Xop_tkn_itm;
import gplx.xowa.parsers.Xop_tkn_itm_;
import gplx.xowa.parsers.amps.Xop_amp_tkn_ent;
import gplx.xowa.parsers.amps.Xop_amp_tkn_num;
import gplx.xowa.parsers.apos.Xop_apos_tkn;
import gplx.xowa.parsers.apos.Xop_apos_tkn_;
import gplx.xowa.parsers.hdrs.Xop_hdr_tkn;
import gplx.xowa.parsers.htmls.Mwh_atr_itm;
import gplx.xowa.parsers.htmls.Mwh_atr_itm_;
import gplx.xowa.parsers.lists.Xop_list_tkn;
import gplx.xowa.parsers.lists.Xop_list_tkn_;
import gplx.xowa.parsers.lnkes.Xop_lnke_tkn;
import gplx.xowa.parsers.magics.Xop_magic_tkn;
import gplx.xowa.parsers.lnkis.Xop_lnki_tkn;
import gplx.xowa.parsers.miscs.Xop_bry_tkn;
import gplx.xowa.parsers.miscs.Xop_hr_tkn;
import gplx.xowa.parsers.miscs.Xop_under_tkn;
import gplx.xowa.parsers.paras.Xop_nl_tkn;
import gplx.xowa.parsers.tblws.Xop_tblw_tkn;
import gplx.xowa.parsers.tblws.Xop_tblw_wkr;
import gplx.xowa.parsers.uniqs.Xop_uniq_tkn;
import gplx.xowa.parsers.xndes.Xop_xatr_whitelist_mgr;
import gplx.xowa.parsers.xndes.Xop_xnde_tag;
import gplx.xowa.parsers.xndes.Xop_xnde_tag_;
import gplx.xowa.parsers.xndes.Xop_xnde_tkn;
import gplx.xowa.wikis.domains.Xow_domain_tid_;
import gplx.xowa.xtns.Xox_mgr_base;
import gplx.xowa.xtns.Xox_xnde;
import gplx.xowa.xtns.cites.Ref_html_wtr;

import gplx.xowa.parsers.lists.Xop_list_tkn_new;
import gplx.Bry_;
import gplx.Bry_bfr_;
import gplx.String_;
import gplx.xowa.Xoa_app_;
public class Xoh_html_wtr {
	private final    Xoae_app app; private final    Xowe_wiki wiki; private final    Xow_html_mgr html_mgr; private final    Xop_xatr_whitelist_mgr whitelist_mgr;
	private Xoae_page page;
	private int indent_level;
	private int stack_counter;
	public Xoh_html_wtr(Xowe_wiki wiki, Xow_html_mgr html_mgr) {
		this.wiki = wiki; this.app = wiki.Appe(); 
		this.html_mgr = html_mgr; this.whitelist_mgr = html_mgr.Whitelist_mgr();			
		this.lnki_wtr = new Xoh_lnki_wtr(this, wiki, html_mgr, cfg);
		this.ref_wtr = new Ref_html_wtr(wiki);
	}
	public Xoh_html_wtr_cfg		Cfg() {return cfg;} private final    Xoh_html_wtr_cfg cfg = new Xoh_html_wtr_cfg();
	public Xoh_lnke_html		Wkr__lnke() {return wkr__lnke;} private final    Xoh_lnke_html wkr__lnke = new Xoh_lnke_html();
	public Xoh_hdr_html			Wkr__hdr()	{return wkr__hdr;}	private final    Xoh_hdr_html wkr__hdr = new Xoh_hdr_html();
	public Xoh_lnki_wtr			Lnki_wtr() {return lnki_wtr;} private final    Xoh_lnki_wtr lnki_wtr;
	public Ref_html_wtr			Ref_wtr() {return ref_wtr;} private final    Ref_html_wtr ref_wtr;
	public Xoh_magic_html		Wkr__magic() {return wkr__magic;} private final    Xoh_magic_html wkr__magic = new Xoh_magic_html();

	public void Init_by_wiki(Xowe_wiki wiki) {
		cfg.Toc__show_(Bool_.Y).Lnki__title_(true).Lnki__visited_y_().Lnki__id_(Bool_.Y);	// NOTE: set during Init_by_wiki, b/c all tests assume these are false
		ref_wtr.Init_by_wiki(wiki);
		lnki_wtr.Init_by_wiki(wiki);
	}
	public void Init_by_page(Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xoae_page page) {
		this.page = page;
		lnki_wtr.Init_by_page(ctx, hctx, src, page);
	}
	public void Init_per_page() {
		ref_wtr.Init_per_page();
	}

	public void Write_doc(Bry_bfr rv, Xop_ctx ctx, byte[] src, Xop_root_tkn root) {Write_doc(rv, ctx, Xoh_wtr_ctx.Basic, src, root);}
	public void Write_doc(Bry_bfr rv, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_root_tkn root) {			
		// init
		this.indent_level = 0;
		this.stack_counter = 0;
		this.page = ctx.Page();
		page.Slink_list().Clear();	// HACK: always clear langs; necessary for reload
		lnki_wtr.Init_by_page(ctx, hctx, src, ctx.Page());
		
		// init paragraphs
		lastParagraph = PARA_NONE;
		DTopen = false;
		inPre = false;
		// write document starting from root
		Write_tkn(rv, ctx, hctx, src, null, -1, root);
	}
	public void Write_tkn_to_html(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_tkn_grp grp, int sub_idx, Xop_tkn_itm tkn) {
		lastParagraph = PARA_NONE;
		DTopen = false;
		inPre = false;
		is_colon_inline = false;
		this.Write_tkn(bfr, ctx, hctx, src, grp, sub_idx, tkn);
	}
	private void Write_tkn(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_tkn_grp grp, int sub_idx, Xop_tkn_itm tkn) {
		if (tkn.Ignore()) return;
		if (++stack_counter > 1500) {	// NOTE:some deeply nested pages can go to 1500; PAGE:cs.s:Page:Hejčl,_Jan_-_Pentateuch.pdf/128 DATE:2016-09-01; PAGE:en.w:Wikipedia:People_by_year/Reports/Stats; DATE:2016-09-11
			Gfo_usr_dlg_.Instance.Warn_many("", "", "stack overflow while generating html; wiki=~{0} page=~{1}", ctx.Wiki().Domain_bry(), ctx.Page().Ttl().Full_db());
			return;
		}
		switch (tkn.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_arg_itm:
			case Xop_tkn_itm_.Tid_root:
				int subs_len = tkn.Subs_len();
				for (int i = 0; i < subs_len; i++) {
					Xop_tkn_itm nxt_tkn = tkn.Subs_get(i);
					Write_tkn(bfr, ctx, hctx, src, tkn, i, nxt_tkn);
				}
				break;
			case Xop_tkn_itm_.Tid_ignore:			break;
			case Xop_tkn_itm_.Tid_html_ncr:			Html_ncr	(bfr, ctx, hctx, src, (Xop_amp_tkn_num)tkn); break;
			case Xop_tkn_itm_.Tid_html_ref:			Html_ref	(bfr, ctx, hctx, src, (Xop_amp_tkn_ent)tkn); break;
			case Xop_tkn_itm_.Tid_hr:				Hr			(bfr, ctx, hctx, src, (Xop_hr_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_apos:				Apos		(bfr, ctx, hctx, src, (Xop_apos_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_list:				List		(bfr, ctx, hctx, src, (Xop_list_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_xnde:				Xnde		(bfr, ctx, hctx, src, (Xop_xnde_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_under:			Under		(bfr, ctx, hctx, src, (Xop_under_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_tblw_tb:			Tblw		(bfr, ctx, hctx, src, (Xop_tblw_tkn)tkn, Gfh_tag_.Table_lhs_bgn		, Gfh_tag_.Table_rhs, true); break;
			case Xop_tkn_itm_.Tid_tblw_tr:			Tblw		(bfr, ctx, hctx, src, (Xop_tblw_tkn)tkn, Gfh_tag_.Tr_lhs_bgn		, Gfh_tag_.Tr_rhs, false); break;
			case Xop_tkn_itm_.Tid_tblw_td:			Tblw		(bfr, ctx, hctx, src, (Xop_tblw_tkn)tkn, Gfh_tag_.Td_lhs_bgn		, Gfh_tag_.Td_rhs, false); break;
			case Xop_tkn_itm_.Tid_tblw_th:			Tblw		(bfr, ctx, hctx, src, (Xop_tblw_tkn)tkn, Gfh_tag_.Th_lhs_bgn		, Gfh_tag_.Th_rhs, false); break;
			case Xop_tkn_itm_.Tid_tblw_tc:			Tblw		(bfr, ctx, hctx, src, (Xop_tblw_tkn)tkn, Gfh_tag_.Caption_lhs_bgn	, Gfh_tag_.Caption_rhs, false); break;
			case Xop_tkn_itm_.Tid_newLine:			New_line	(bfr, ctx, hctx, src, (Xop_nl_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_bry:				Bry			(bfr, ctx, hctx, src, (Xop_bry_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_uniq:				Uniq        (bfr, ctx, hctx, src, (Xop_uniq_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_lnki:				lnki_wtr.Write_lnki(bfr, hctx, src, (Xop_lnki_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_lnke:				wkr__lnke.Write_html(bfr, html_mgr, this, hctx, ctx, src, (Xop_lnke_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_magic:			wkr__magic.Write_html(bfr, html_mgr, this, hctx, ctx, src, (Xop_magic_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_hdr:				wkr__hdr.Write_html(bfr, this, wiki, page, ctx, hctx, cfg, grp, sub_idx, src, (Xop_hdr_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_para:
			case Xop_tkn_itm_.Tid_pre:
			case Xop_tkn_itm_.Tid_space:
			case Xop_tkn_itm_.Tid_escape:
				tkn.Html__write(bfr, this, wiki, page, ctx, hctx, cfg, grp, sub_idx, src); break;
			case Xop_tkn_itm_.Tid_list_new:				List_new		(bfr, ctx, hctx, src, (Xop_list_tkn_new)tkn, grp, sub_idx); break;
//			case Xop_tkn_itm_.Tid_eq: //goes to default
//				Xoh_html_wtr_escaper.Escape(app.Parser_amp_mgr(), bfr, src, tkn.Src_bgn(), tkn.Src_end(), true, false);	// NOTE: always escape text including (a) lnki_alt text; and (b) any other text, especially failed xndes; DATE:2013-06-18
//                        break;
			case Xop_tkn_itm_.Tid_colon:
				if (is_colon_inline) {
					nextItem( bfr, Byte_ascii.Colon );
					Trimspace(grp, sub_idx);
					break;
				}
				// fall thru
			default:
				Xoh_html_wtr_escaper.Escape(app.Parser_amp_mgr(), bfr, src, tkn.Src_bgn(), tkn.Src_end(), true, false);	// NOTE: always escape text including (a) lnki_alt text; and (b) any other text, especially failed xndes; DATE:2013-06-18
				break;
		}
		--stack_counter;
	}
	private void Html_ncr(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_amp_tkn_num tkn)	{
		bfr.Add_byte(Byte_ascii.Amp).Add_byte(Byte_ascii.Hash).Add_int_variable(tkn.Val()).Add_byte(Byte_ascii.Semic);	// NOTE: do not literalize, else browser may not display multi-char bytes properly; EX: &#160; gets added as &#160; not as {192,160}; DATE:2013-12-09
	}
	private void Html_ref(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_amp_tkn_ent tkn) {
		if (tkn.Itm_is_custom())	// used by <nowiki>; EX:<nowiki>&#60;</nowiki> -> &xowa_lt; DATE:2014-11-07
			tkn.Print_literal(bfr);
		else
			tkn.Print_ncr(bfr);
	}
	private void Hr(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_hr_tkn tkn)				{bfr.Add(Gfh_tag_.Hr_inl);}
	private void Apos(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_apos_tkn apos) {
		if (hctx.Mode_is_alt()) return;	// ignore apos if alt; EX: [[File:A.png|''A'']] should have alt of A; DATE:2013-10-25
		int literal_apos = apos.Apos_lit();
		if (literal_apos > 0)
			bfr.Add_byte_repeat(Byte_ascii.Apos, literal_apos);
		switch (apos.Apos_cmd()) {
			case Xop_apos_tkn_.Cmd_b_bgn:			bfr.Add(Gfh_tag_.B_lhs); break;
			case Xop_apos_tkn_.Cmd_b_end:			bfr.Add(Gfh_tag_.B_rhs); break;
			case Xop_apos_tkn_.Cmd_i_bgn:			bfr.Add(Gfh_tag_.I_lhs); break;		
			case Xop_apos_tkn_.Cmd_i_end:			bfr.Add(Gfh_tag_.I_rhs); break;
			case Xop_apos_tkn_.Cmd_bi_bgn:			bfr.Add(Gfh_tag_.B_lhs).Add(Gfh_tag_.I_lhs); break;		
			case Xop_apos_tkn_.Cmd_ib_end:			bfr.Add(Gfh_tag_.I_rhs).Add(Gfh_tag_.B_rhs); break;
			case Xop_apos_tkn_.Cmd_ib_bgn:			bfr.Add(Gfh_tag_.I_lhs).Add(Gfh_tag_.B_lhs); break;		
			case Xop_apos_tkn_.Cmd_bi_end:			bfr.Add(Gfh_tag_.B_rhs).Add(Gfh_tag_.I_rhs);; break;
			case Xop_apos_tkn_.Cmd_bi_end__b_bgn:	bfr.Add(Gfh_tag_.B_rhs).Add(Gfh_tag_.I_rhs).Add(Gfh_tag_.B_lhs); break;
			case Xop_apos_tkn_.Cmd_ib_end__i_bgn:	bfr.Add(Gfh_tag_.I_rhs).Add(Gfh_tag_.B_rhs).Add(Gfh_tag_.I_lhs); break;
			case Xop_apos_tkn_.Cmd_b_end__i_bgn:	bfr.Add(Gfh_tag_.B_rhs).Add(Gfh_tag_.I_lhs); break;		
			case Xop_apos_tkn_.Cmd_i_end__b_bgn:	bfr.Add(Gfh_tag_.I_rhs).Add(Gfh_tag_.B_lhs); break;
			case Xop_apos_tkn_.Cmd_nil:				break;
			default: throw Err_.new_unhandled(apos.Apos_cmd());
		}
	}
	private boolean is_colon_inline;
// -------------------------------
	private static final int PARA_NONE = 0, PARA_P = 1, PARA_PRE = 2;
	// lastParagraph either 'p' or 'pre'
	// pendingPTag either '<p>'or '</p><p>'
	private static final int PENDING_NONE = 0, PENDING_P = 1, PENDING_P_P = 2;
	private static final int PREG_OPENBLOCK = 1, PREG_CLOSEBLOCK = 2, PREG_COLON = 3, PREG_OPENA = 4, PREG_OPENL = 5, PREG_CLOSEL = 6;

	private byte[] text;
	private boolean inPre;
	private boolean DTopen;
	private int lastParagraph;
	private int pendingPTag;

	private static final byte[]
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
		byte[] result = Bry_.Empty;
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
	private int getCommon( int st1_bgn, int st1_len, int st2_bgn, int st2_end) {
		int shorter = st1_len < st2_end - st2_bgn ? st1_len : st2_end - st2_bgn;
		int i;
		for ( i = 0; i < shorter; ++i ) {
                    byte a = text[st1_bgn + i]; // prefix
                    byte b = text[st2_bgn + i]; // lastprefix
                    if (b == ';') // treat ':' and ';' same in lastprefix
                        b = ':';
			if ( a != b ) {
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
	private void nextItem( Bry_bfr bfr, byte chr ) {
		// BIG HACK - remove preceeding Nl if there
		int buf_ofs = bfr.Len() - 1;
		while (buf_ofs > 0) {
			byte b = bfr.Bfr()[buf_ofs];
			if (b == '\n') {
				//bfr.Len_(buf_ofs);
				bfr.Del_by_1();
				buf_ofs--;
			}
			else
				break;
		}
		switch (chr) {
			case '*': case '#':
                            // HACK if bfr ends with <li> dont add anything
                            int bfr_ofs = bfr.Len() - 4;
                            byte[] bfr_buf = bfr.Bfr();
                            if (bfr_ofs > 0 && bfr_buf[bfr_ofs] == '<' &&
                                    bfr_buf[bfr_ofs+1] == 'l' &&
                                    bfr_buf[bfr_ofs+2] == 'i' &&
                                    bfr_buf[bfr_ofs+3] == '>')
                                break;
				bfr.Add(ni_star);
				break;
			case ':': case ';':
				if ( DTopen ) {
					bfr.Add(ni_dt_nl);
				}
				else {
					bfr.Add(ni_dd_nl);
				}
				if ( chr == ';' ) {
					DTopen = true;
					bfr.Add(ni_dt);
				} else {
					DTopen = false;
					bfr.Add(ni_dd);
				}
				break;
			default:
				bfr.Add(ni_error);
		}
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
		} else if ( chr == ':' || chr == ';') {
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

	private void Trimspace(Xop_tkn_grp grp, int sub_idx) {
		int subs_len = grp.Subs_len();
		Xop_tkn_itm nxt_tkn;
			if (sub_idx + 1 < subs_len) {
				nxt_tkn = grp.Subs_get(sub_idx + 1);
				if (nxt_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_space)
					nxt_tkn.Ignore_y_();
			}
			// try to find the other end - should be another list_new
			for (int i = sub_idx + 1; i < subs_len; i++) {
				nxt_tkn = grp.Subs_get(i);
				if (nxt_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_list_new) {
					// found the end look at the previous
					nxt_tkn = grp.Subs_get(i - 1);
					if (nxt_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_space) {
						nxt_tkn.Ignore_y_();
					}
				}
			}
	}
	private void List_new(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_list_tkn_new list, Xop_tkn_grp grp, int sub_idx) {
		// List generation
		is_colon_inline = false;
		int linestart = list.Src_bgn();
		int prefixLength = list.Src_end() - linestart;
		this.text = src;
		Bry_bfr output = bfr; // do at formal params

		int lastPrefix_bgn = 0;
		int lastPrefix_end = 0;
		int lastPrefixLength = 0;
		Xop_list_tkn_new prev_link_tkn = list.Prev_list();
		if (prev_link_tkn != null) {
			lastPrefix_bgn = prev_link_tkn.Src_bgn();
			if (lastPrefix_bgn > src.length) {
				Xoa_app_.Usr_dlg().Warn_many("", "", "strange new link; wiki=~{0} page=~{1}", ctx.Wiki().Domain_bry(), ctx.Page().Ttl().Full_db());
				return;
			}
			lastPrefix_end = prev_link_tkn.Src_end();
			if (lastPrefix_bgn < linestart || linestart == 0) {// extra check
				lastPrefixLength = lastPrefix_end - lastPrefix_bgn;
			} else {
				lastPrefix_bgn = lastPrefix_end = 0;
			}
		}

		if (prefixLength > 0 && compare_prefix(lastPrefix_bgn, lastPrefix_end, linestart, prefixLength) ) {
			// Same as the last item, so no need to deal with nesting or opening stuff
			byte lastprefixchar = src[linestart + prefixLength - 1];
			nextItem( output, lastprefixchar );
			pendingPTag = PENDING_NONE;
			if ( lastprefixchar == ';' ) {
				// The one nasty exception: definition lists work like this:
				// ; title : definition text
				// So we check for : in the remainder text to split up the
				// title and definition, without b0rking links.
				is_colon_inline = true;
			}
		} else if ( prefixLength > 0 || lastPrefixLength > 0 ) {
			// We need to open or close prefixes, or both.
			// Either open or close a level...
			int commonPrefixLength = getCommon( linestart, prefixLength, lastPrefix_bgn, lastPrefix_end );
			pendingPTag = PENDING_NONE;
			// Close all the prefixes which aren't shared.
			while ( commonPrefixLength < lastPrefixLength ) {
				if (lastPrefix_bgn + lastPrefixLength - 1 > text.length) {
                                System.out.println("eek");
				}
				// BIG HACK - remove preceeding Nl if there
				byte b = output.Bfr()[output.Len() - 1];
				if (b == '\n')
					//output.Len_(output.Len() - 1);
					output.Del_by_1();
				output.Add(closeList( text[lastPrefix_bgn + lastPrefixLength - 1] ));
				--lastPrefixLength;
			}
			// Continue the current prefix if appropriate.
			if ( prefixLength <= commonPrefixLength && commonPrefixLength > 0 ) {
				nextItem( output, text[linestart + commonPrefixLength - 1] );
			}
			// Close an open <dt> if we have a <dd> (":") starting on this line
			if ( DTopen && commonPrefixLength > 0 && text[linestart + commonPrefixLength - 1] == ':' ) {
				nextItem( output, Byte_ascii.Colon );
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
					//t = colon_semicolon(output, t);
					is_colon_inline = true;
				}
				++commonPrefixLength;
			}
			if ( prefixLength == 0 && lastPrefix_end - lastPrefix_bgn > 0 ) {
				//output.Add_byte(Byte_ascii.Nl);
                                // (re)set para start
                                //ctx.Para().Process_block__xnde(null, Xop_xnde_tag.Block_bgn);
			}
		}
		// do some trimming
		Trimspace(grp, sub_idx);
	}
// -------------------------------
	private void List(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_list_tkn list) {
		if (hctx.Mode_is_alt()) {					// alt; add literally; EX: "*" for "\n*"; note that \n is added in New_line()
			if (list.List_bgn() == Bool_.Y_byte) {	// bgn tag
				bfr.Add_byte(list.List_itmTyp());	// add literal byte
			}
			else {}									// end tag; ignore
		}
		else {
			byte list_itm_type = list.List_itmTyp();
			if (list.List_bgn() == Bool_.Y_byte) {
				if (list.List_sub_first()) List_grp_bgn(bfr, ctx, hctx, src, list_itm_type);
				List_itm_bgn(bfr, ctx, hctx, src, list_itm_type);
			}
			else {
				List_itm_end(bfr, ctx, hctx, src, list_itm_type);
				if (list.List_sub_last() == Bool_.Y_byte) List_grp_end(bfr, ctx, hctx, src, list_itm_type);
			}
		}
	}
	private void List_grp_bgn(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, byte type) {
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol: tag = Gfh_tag_.Ol_lhs; break;
			case Xop_list_tkn_.List_itmTyp_ul: tag = Gfh_tag_.Ul_lhs; break;
			case Xop_list_tkn_.List_itmTyp_dd:
			case Xop_list_tkn_.List_itmTyp_dt: tag = Gfh_tag_.Dl_lhs; break;
			default: throw Err_.new_unhandled(type);
		}
		if (!page.Html_data().Writing_hdr_for_toc()) {
			if (bfr.Len() > 0) bfr.Add_byte_nl();	// NOTE: do not add newLine if start 
			if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		}
		bfr.Add(tag);
		++indent_level;
	}
	private void List_itm_bgn(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, byte type) {
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol:
			case Xop_list_tkn_.List_itmTyp_ul: tag = Gfh_tag_.Li_lhs; break;
			case Xop_list_tkn_.List_itmTyp_dt: tag = Gfh_tag_.Dt_lhs; break;
			case Xop_list_tkn_.List_itmTyp_dd: tag = Gfh_tag_.Dd_lhs; break;
			default: throw Err_.new_unhandled(type);
		}
		if (!page.Html_data().Writing_hdr_for_toc()) {
			bfr.Add_byte_nl();
			if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		}
		bfr.Add(tag);
		++indent_level;
	}
	private void List_grp_end(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, byte type) {
		--indent_level;
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol: tag = Gfh_tag_.Ol_rhs; break;
			case Xop_list_tkn_.List_itmTyp_ul: tag = Gfh_tag_.Ul_rhs; break;
			case Xop_list_tkn_.List_itmTyp_dd:
			case Xop_list_tkn_.List_itmTyp_dt: tag = Gfh_tag_.Dl_rhs; break;
			default: throw Err_.new_unhandled(type);
		}
		if (!page.Html_data().Writing_hdr_for_toc()) {
			bfr.Add_byte_nl();
			if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		}
		bfr.Add(tag);
	}
	
	private void List_itm_end(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, byte type) {
		--indent_level;
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol:
			case Xop_list_tkn_.List_itmTyp_ul: tag = Gfh_tag_.Li_rhs; break;
			case Xop_list_tkn_.List_itmTyp_dt: tag = Gfh_tag_.Dt_rhs; break;
			case Xop_list_tkn_.List_itmTyp_dd: tag = Gfh_tag_.Dd_rhs; break;
			default: throw Err_.new_unhandled(type);
		}
//		if (!page.Html_data().Writing_hdr_for_toc()) {
//			bfr.Add_byte_if_not_last(Byte_ascii.Nl);
//			if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
//		}
		bfr.Add(tag);
	}
	private void New_line(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_nl_tkn tkn) {
		if (hctx.Mode_is_alt())
			bfr.Add_byte_space();
		else {
			if (tkn.Nl_tid() == Xop_nl_tkn.Tid_char) {
				bfr.Add_byte_if_not_last(Byte_ascii.Nl);
			}
		}
	}
	private void Bry(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_bry_tkn bry) {
		bfr.Add(bry.Val());
	}
	private void Uniq(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_uniq_tkn tkn) {
		byte[] val = wiki.Parser_mgr().Uniq_mgr().Get(tkn.Key());
		Xoh_html_wtr_escaper.Escape(app.Parser_amp_mgr(), bfr, val, 0, val.length, true, false);
	}
	private void Under(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_under_tkn under) {
		if (hctx.Mode_is_alt()) return;
		switch (under.Under_tid()) {
			case Xol_kwd_grp_.Id_toc:
				if (cfg.Toc__show())
                                    page.Html_data().Toc_mgr().Toc_bgn_(bfr.Len());
                                    page.Html_data().Toc_mgr().Exists_y_();
				break;
			case Xol_kwd_grp_.Id_forcetoc:
                            ctx.Page_data().Hdr_forcetoc_y_();
                            page.Html_data().Toc_mgr().Hdr_forcetoc_y_();
                            break;
			case Xol_kwd_grp_.Id_notoc:
                            ctx.Page_data().Hdr_notoc_y_();
                            page.Html_data().Toc_mgr().Hdr_notoc_y_();
                            break;
			case Xol_kwd_grp_.Id_noeditsection:		break;	// ignore; not handling edit sections
			case Xol_kwd_grp_.Id_nocontentconvert:	ctx.Page_data().Lang_convert_content_(false); break;
			case Xol_kwd_grp_.Id_notitleconvert:	ctx.Page_data().Lang_convert_title_(false); break;
		}
	}
	private void Xnde(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_xnde_tkn xnde) {
		if (hctx.Mode_is_alt()) {
			if (xnde.Tag_close_bgn() > 0) // NOTE: some tags are not closed; WP.EX: France; <p>
				Xoh_html_wtr_escaper.Escape(app.Parser_amp_mgr(), bfr, src, xnde.Tag_open_end(), xnde.Tag_close_bgn(), true, false);
			else
				Xnde_subs(bfr, ctx, hctx, src, xnde);
			return;
		}
		Xop_xnde_tag tag = xnde.Tag();
		int tag_id = tag.Id();
		switch (tag_id) {
			case Xop_xnde_tag_.Tid__br:
				if (xnde.Src_end() - xnde.Src_bgn() < 4
					|| xnde.Src_bgn() == -1) 
                                    bfr.Add(Gfh_tag_.Br_inl); 
                                else 
                                    bfr.Add_mid(src, xnde.Src_bgn(), xnde.Src_end()); break;
//			case Xop_xnde_tag_.Tid__hr: bfr.Add(Gfh_tag_.Hr_inl); break;
			case Xop_xnde_tag_.Tid__includeonly:	// NOTE: do not write tags or content
				break;
			case Xop_xnde_tag_.Tid__noinclude:		// NOTE: do not write tags
			case Xop_xnde_tag_.Tid__onlyinclude:	
				Xnde_subs_escape(bfr, ctx, hctx, src, xnde, false, false);
				break;
			case Xop_xnde_tag_.Tid__nowiki:
				Xnde_subs_escape(bfr, ctx, hctx, src, xnde, false, true);
				break;
			case Xop_xnde_tag_.Tid__hr:
			case Xop_xnde_tag_.Tid__b: case Xop_xnde_tag_.Tid__strong:
			case Xop_xnde_tag_.Tid__i: case Xop_xnde_tag_.Tid__em: case Xop_xnde_tag_.Tid__cite: case Xop_xnde_tag_.Tid__dfn: case Xop_xnde_tag_.Tid__var:
			case Xop_xnde_tag_.Tid__u: case Xop_xnde_tag_.Tid__ins: case Xop_xnde_tag_.Tid__abbr:
			case Xop_xnde_tag_.Tid__strike: case Xop_xnde_tag_.Tid__s: case Xop_xnde_tag_.Tid__del:
			case Xop_xnde_tag_.Tid__sub: case Xop_xnde_tag_.Tid__sup: case Xop_xnde_tag_.Tid__big: case Xop_xnde_tag_.Tid__small:
			case Xop_xnde_tag_.Tid__code: case Xop_xnde_tag_.Tid__tt: case Xop_xnde_tag_.Tid__kbd: case Xop_xnde_tag_.Tid__samp:
			case Xop_xnde_tag_.Tid__font: case Xop_xnde_tag_.Tid__center:
			case Xop_xnde_tag_.Tid__p: case Xop_xnde_tag_.Tid__span:
			case Xop_xnde_tag_.Tid__h1: case Xop_xnde_tag_.Tid__h2: case Xop_xnde_tag_.Tid__h3: case Xop_xnde_tag_.Tid__h4: case Xop_xnde_tag_.Tid__h5: case Xop_xnde_tag_.Tid__h6:
			case Xop_xnde_tag_.Tid__dt: case Xop_xnde_tag_.Tid__dd: case Xop_xnde_tag_.Tid__ol: case Xop_xnde_tag_.Tid__ul:
			case Xop_xnde_tag_.Tid__table: case Xop_xnde_tag_.Tid__tr: case Xop_xnde_tag_.Tid__td: case Xop_xnde_tag_.Tid__th: case Xop_xnde_tag_.Tid__caption: case Xop_xnde_tag_.Tid__tbody:
			case Xop_xnde_tag_.Tid__ruby: case Xop_xnde_tag_.Tid__rt: case Xop_xnde_tag_.Tid__rb: case Xop_xnde_tag_.Tid__rp: 
			case Xop_xnde_tag_.Tid__time: case Xop_xnde_tag_.Tid__bdi: case Xop_xnde_tag_.Tid__data: case Xop_xnde_tag_.Tid__mark: case Xop_xnde_tag_.Tid__wbr: case Xop_xnde_tag_.Tid__bdo:	// HTML 5: write literally and let browser handle them
			case Xop_xnde_tag_.Tid__q:
			// added new tags below; ISSUE#:636 DATE:2020-03-08; FIX:NPE;ISSUE#:730; DATE:2020-05-27
			case Xop_xnde_tag_.Tid__audio:
			case Xop_xnde_tag_.Tid__video:
			case Xop_xnde_tag_.Tid__track:
			case Xop_xnde_tag_.Tid__rtc:
			case Xop_xnde_tag_.Tid__figure:
			case Xop_xnde_tag_.Tid__figure_inline:
			case Xop_xnde_tag_.Tid__figcaption:
				Write_xnde(bfr, ctx, hctx, xnde, tag, tag_id, src);
				break;
			case Xop_xnde_tag_.Tid__blockquote:
				Write_xnde(bfr, ctx, hctx, xnde, tag, tag_id, src);
				break;
			case Xop_xnde_tag_.Tid__dl:
			case Xop_xnde_tag_.Tid__div:
                                //ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_end);
				Write_xnde(bfr, ctx, hctx, xnde, tag, tag_id, src);
                                // (re)set para start - maybe??
                                ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_bgn);
				break;
			case Xop_xnde_tag_.Tid__pre: {
				if (xnde.Tag_open_end() == xnde.Tag_close_bgn()) return; // ignore empty tags, else blank pre line will be printed; DATE:2014-03-12
				byte[] name = tag.Name_bry();
				bfr.Add_byte(Byte_ascii.Angle_bgn).Add(name);
				if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, hctx, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
				bfr.Add_byte(Byte_ascii.Angle_end);
				Xnde_subs_escape(bfr, ctx, hctx, src, xnde, false, true);
				Gfh_tag_.Bld_rhs(bfr, name);
				break;
			}
			case Xop_xnde_tag_.Tid__li: {
				byte[] name = tag.Name_bry();
				int bfr_len = bfr.Len();
				if (!page.Html_data().Writing_hdr_for_toc()) {
					if (bfr_len > 0 && bfr.Bfr()[bfr_len - 1] != Byte_ascii.Nl) bfr.Add_byte_nl();	// NOTE: always add nl before li else some lists will merge and force long horizontal bar; EX:w:Music
				}
				if (xnde.Tag_visible()) {
					bfr.Add_byte(Byte_ascii.Angle_bgn).Add(name);
					if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, hctx, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
					bfr.Add_byte(Byte_ascii.Angle_end);
				}
				Xnde_subs(bfr, ctx, hctx, src, xnde);
				if (xnde.Tag_visible())
					Gfh_tag_.Bld_rhs(bfr, name);	// NOTE: inline is never written as <b/>; will be written as <b></b>; SEE: NOTE_1
				break;
			}
			case Xop_xnde_tag_.Tid__timeline: {
				bfr.Add_str_a7("<div class=\"timeline-wrapper\" style=\"line-height:0;font-size:0\">");
				bfr.Add(gplx.xowa.htmls.core.wkrs.addons.timelines.Xoh_timeline_data.Hook_bry);
				Xox_mgr_base.Xtn_write_escape(app, bfr, src, xnde.Tag_open_end(), xnde.Tag_close_bgn());	// NOTE: do not embed <timeline> tag inside pre, else timeline will render in black; EX:<pre><timeline>a</timeline></pre> will fail; DATE:2014-05-22
				bfr.Add_str_a7("</pre>");
				bfr.Add_str_a7("</div>");
				break;
			}
			case Xop_xnde_tag_.Tid__gallery:
			case Xop_xnde_tag_.Tid__poem:
			case Xop_xnde_tag_.Tid__hiero:
			case Xop_xnde_tag_.Tid__score:
			case Xop_xnde_tag_.Tid__ref:
			case Xop_xnde_tag_.Tid__references:
			case Xop_xnde_tag_.Tid__inputBox:
			case Xop_xnde_tag_.Tid__imageMap:
			case Xop_xnde_tag_.Tid__pages:
			case Xop_xnde_tag_.Tid__pagequality:
			case Xop_xnde_tag_.Tid__pagelist:
			case Xop_xnde_tag_.Tid__section:
			case Xop_xnde_tag_.Tid__translate:
			case Xop_xnde_tag_.Tid__dynamicPageList:
			case Xop_xnde_tag_.Tid__languages:
			case Xop_xnde_tag_.Tid__templateData:
			case Xop_xnde_tag_.Tid__source:	// DATE:2015-09-29
			case Xop_xnde_tag_.Tid__syntaxHighlight:
			case Xop_xnde_tag_.Tid__listing_buy:
			case Xop_xnde_tag_.Tid__listing_do:
			case Xop_xnde_tag_.Tid__listing_drink:
			case Xop_xnde_tag_.Tid__listing_eat:
			case Xop_xnde_tag_.Tid__listing_listing:
			case Xop_xnde_tag_.Tid__listing_see:
			case Xop_xnde_tag_.Tid__listing_sleep:
			case Xop_xnde_tag_.Tid__xowa_cmd:
			case Xop_xnde_tag_.Tid__rss:
			case Xop_xnde_tag_.Tid__quiz:
			case Xop_xnde_tag_.Tid__math:
			case Xop_xnde_tag_.Tid__chem:
			case Xop_xnde_tag_.Tid__indicator:
			case Xop_xnde_tag_.Tid__xowa_html:
			case Xop_xnde_tag_.Tid__xowa_wiki_setup:
			case Xop_xnde_tag_.Tid__graph:
			case Xop_xnde_tag_.Tid__random_selection:
			case Xop_xnde_tag_.Tid__tabber:
			case Xop_xnde_tag_.Tid__tabview:
			case Xop_xnde_tag_.Tid__mapframe:
			case Xop_xnde_tag_.Tid__maplink:
			case Xop_xnde_tag_.Tid__template_styles:
			case Xop_xnde_tag_.Tid__categoryTree:
				Xox_xnde xtn = xnde.Xnde_xtn();
				xtn.Xtn_write(bfr, app, ctx, this, hctx, page, xnde, src);
				break;
			// do not write <meta/> <link/>; PAGE:fr.s:La_Dispute DATE:2017-05-28
			case Xop_xnde_tag_.Tid__meta:
			case Xop_xnde_tag_.Tid__link:
				break;
			case Xop_xnde_tag_.Tid__xowa_tag_bgn:
			case Xop_xnde_tag_.Tid__xowa_tag_end:
				break;
			default:	// unknown tag
				if (tag.Restricted()) {	// a; img; script; etc..
					if (	!page.Html_data().Html_restricted()								// page is not marked restricted (only [[Special:]])
						||	page.Wiki().Domain_tid() == Xow_domain_tid_.Tid__home) {		// page is in home wiki
						bfr.Add_mid(src, xnde.Src_bgn(), xnde.Src_end());
						return;
					}
				}
				bfr.Add_byte(Byte_ascii.Angle_bgn).Add(tag.Name_bry());	// escape bgn
				if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, hctx, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
				switch (xnde.CloseMode()) {
					case Xop_xnde_tkn.CloseMode_inline:
						bfr.Add_byte(Byte_ascii.Slash).Add_byte(Byte_ascii.Angle_end);
						break;
					default:	// NOTE: close tag, even if dangling; EX: <center>a -> <center>a</center>
						bfr.Add_byte(Byte_ascii.Angle_end);
						Xnde_subs(bfr, ctx, hctx, src, xnde);
						bfr.Add_byte(Byte_ascii.Angle_bgn).Add_byte(Byte_ascii.Slash).Add(tag.Name_bry()).Add_byte(Byte_ascii.Angle_end);
						break;
				}
				break;
		}
	}
	private void Write_xnde(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xop_xnde_tkn xnde, Xop_xnde_tag tag, int tag_id, byte[] src) {
		byte[] name = tag.Name_bry();
		bfr.Add_byte(Byte_ascii.Angle_bgn).Add(name);
		if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, hctx, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
		bfr.Add_byte(Byte_ascii.Angle_end);
		int subs_len = xnde.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = xnde.Subs_get(i);
			if (sub.Ignore()) continue;
			Write_tkn(bfr, ctx, hctx, src, xnde, i, sub);
		}
		Gfh_tag_.Bld_rhs(bfr, name);										// NOTE: inline is never written as <b/>; will be written as <b></b>; SEE: NOTE_1
/* IGNORE THIS (for now 20200216)
		boolean at_bgn = true;
		Bry_bfr ws_bfr = wiki.Utl__bfr_mkr().Get_b512();						// create separate ws_bfr to handle "a<b> c </b>d" -> "a <b>c</b> d"
		int subs_len = xnde.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = xnde.Subs_get(i);
			if (sub.Ignore()) continue;
			byte tkn_tid = sub.Tkn_tid();
			switch (tkn_tid) {
				case Xop_tkn_itm_.Tid_space:									// space; add to ws_bfr;
					ws_bfr.Add_mid(src, sub.Src_bgn(), sub.Src_end());
					break;
				default:
					if (tkn_tid == Xop_tkn_itm_.Tid_html_ncr) {					// html_entity &#32; needed for fr.wikipedia.org and many spans with <span>&#32;</span>; DATE:2013-06-18
						Xop_amp_tkn_num ncr_tkn = (Xop_amp_tkn_num)sub;
						if (ncr_tkn.Val() == Byte_ascii.Space
							|| ncr_tkn.Val() == 160
							) {

							ws_bfr.Add_mid(src, ncr_tkn.Src_bgn(), ncr_tkn.Src_end());
							continue;											// just add entity; don't process rest;
						}
					}
					if (ws_bfr.Len() > 0) bfr.Add_bfr_and_clear(ws_bfr);		// dump ws_bfr to real bfr
					if (at_bgn) {												// 1st non-ws tkn; add open tag; <b>
						at_bgn = false;
						bfr.Add_byte(Byte_ascii.Angle_bgn).Add(name);
						if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, hctx, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
						bfr.Add_byte(Byte_ascii.Angle_end);
					}
					Write_tkn(bfr, ctx, hctx, src, xnde, i, sub);				// NOTE: never escape; <p>, <table>, <center> etc may have nested nodes
					break;
			}
		}
		if (at_bgn) {	// occurs when xnde is empty; EX: <b></b>
			bfr.Add_byte(Byte_ascii.Angle_bgn).Add(name);
			if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, hctx, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
			bfr.Add_byte(Byte_ascii.Angle_end);
		}
		Gfh_tag_.Bld_rhs(bfr, name);										// NOTE: inline is never written as <b/>; will be written as <b></b>; SEE: NOTE_1
		if (ws_bfr.Len() > 0) bfr.Add_bfr_and_clear(ws_bfr);				// dump any leftover ws to bfr; handles "<b>c </b>" -> "<b>c</b> "
		ws_bfr.Mkr_rls();
*/
	}
	private void Xnde_atrs(int tag_id, Xoh_wtr_ctx hctx, byte[] src, int bgn, int end, Mwh_atr_itm[] ary, Bry_bfr bfr) {
		if (ary == null) return;	// NOTE: some nodes will have null xatrs b/c of whitelist; EX: <pre style="overflow:auto">a</pre>; style is not on whitelist so not xatr generated, but xatr_bgn will != -1
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Mwh_atr_itm atr = ary[i];
			if (atr.Invalid()) continue;
			if (!whitelist_mgr.Chk(tag_id, atr)) continue;
			Xnde_atr_write(bfr, app, hctx, src, atr);
		}
	}
	private static void Xnde_atr_write(Bry_bfr bfr, Xoae_app app, Xoh_wtr_ctx hctx, byte[] src, Mwh_atr_itm atr) {
		byte[] atr_key = atr.Key_bry();
		if (	hctx.Mode_is_display_title()
			&&	Xoh_display_ttl_wtr.Is_style_restricted(bfr, hctx, src, atr, atr_key))
			return;
	
		bfr.Add_byte(Byte_ascii.Space);	// add space before every attribute
		if (atr_key != null) {
			bfr.Add(atr_key);
			bfr.Add_byte(Byte_ascii.Eq);
		}
		byte quote_byte = atr.Qte_byte(); if (quote_byte == Byte_ascii.Null) quote_byte = Byte_ascii.Quote;
		bfr.Add_byte(quote_byte);
		if (atr.Key_tid() == Mwh_atr_itm_.Key_tid__id) {	// ids should not have spaces; DATE:2013-04-01
			if (atr.Val_bry() == null)
				Xnde_atr_write_id(bfr, app, atr.Src(), atr.Val_bgn(), atr.Val_end());
			else {
				byte[] atr_val = atr.Val_bry();
				Xnde_atr_write_id(bfr, app, atr_val, 0, atr_val.length);
			}
		}
		else {
			if (atr.Val_bry() == null)
				bfr.Add_mid(atr.Src(), atr.Val_bgn(), atr.Val_end());
			else
				bfr.Add(atr.Val_bry());
		}
		bfr.Add_byte(quote_byte);
	}
	private static void Xnde_atr_write_id(Bry_bfr bfr, Xoae_app app, byte[] bry, int bgn, int end) {gplx.langs.htmls.encoders.Gfo_url_encoder_.Id.Encode(bfr, bry, bgn, end);}
	private void Xnde_subs(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_xnde_tkn xnde) {
		int subs_len = xnde.Subs_len();
		for (int i = 0; i < subs_len; i++)
			Write_tkn(bfr, ctx, hctx, src, xnde, i, xnde.Subs_get(i));
	}
	private void Xnde_subs_escape(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_xnde_tkn xnde, boolean amp_enable, boolean nowiki) {
		int xndesubs_len = xnde.Subs_len();
		for (int i = 0; i < xndesubs_len; i++) {
			Xop_tkn_itm sub = xnde.Subs_get(i);
			switch (sub.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn sub_xnde = (Xop_xnde_tkn)sub;
					switch (sub_xnde.Tag().Id()) {
						case Xop_xnde_tag_.Tid__noinclude:
						case Xop_xnde_tag_.Tid__onlyinclude:
						case Xop_xnde_tag_.Tid__includeonly:
							break;
						default:
							byte[] tag_name = sub_xnde.Tag().Name_bry();
							bfr.Add(Gfh_entity_.Lt_bry).Add(tag_name);
							if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(sub_xnde.Tag().Id(), hctx, src, sub_xnde.Atrs_bgn(), sub_xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
							bfr.Add(Gfh_entity_.Gt_bry);
							break;
					}
					Xnde_subs_escape(bfr, ctx, hctx, src, sub_xnde, amp_enable, false);
					break;
				case Xop_tkn_itm_.Tid_txt:
					if (amp_enable)
						bfr.Add_mid(src, sub.Src_bgn(), sub.Src_end());
					else
						Xoh_html_wtr_escaper.Escape(app.Parser_amp_mgr(), bfr, src, sub.Src_bgn(), sub.Src_end(), true, nowiki);
					break;
				default:
					Write_tkn(bfr, ctx, hctx, src, xnde, i, sub);
					break;
			}
		}
	}
	private void Tblw(Bry_bfr bfr, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte[] src, Xop_tblw_tkn tkn, byte[] bgn, byte[] end, boolean tblw_bgn) {
		if (hctx.Mode_is_alt())			// add \s for each \n
			bfr.Add_byte_space();
		else {
			if (!page.Html_data().Writing_hdr_for_toc()) {
				bfr.Add_byte_if_not_last(Byte_ascii.Nl);
				if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
			}
			bfr.Add(bgn);
			int atrs_bgn = tkn.Atrs_bgn();
			if (atrs_bgn != -1) Xnde_atrs(tkn.Tblw_tid(), hctx, src, atrs_bgn, tkn.Atrs_end(), tkn.Atrs_ary(), bfr); //bfr.Add_byte(Byte_ascii.Space).Add_mid(src, atrs_bgn, tkn.Atrs_end());
			bfr.Add_byte(Byte_ascii.Angle_end);
			++indent_level;
		}
		int subs_len = tkn.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = tkn.Subs_get(i);
			if (sub.Ignore()) continue;
			Write_tkn(bfr, ctx, hctx, src, tkn, i, sub);
		}
		if (hctx.Mode_is_alt()) {
			if (tblw_bgn)			// only add \s for closing table; |} -> "\s"
				bfr.Add_byte_space();
		}
		else {
			--indent_level;
			//if (!page.Html_data().Writing_hdr_for_toc()) {
			//	bfr.Add_byte_if_not_last(Byte_ascii.Nl);
			//	if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
			//}
			if (end == Gfh_tag_.Caption_rhs) // required by en.wikipedia.org/wiki/EXOC3L2
				bfr.Add_byte_nl();
			bfr.Add(end);
			if (!page.Html_data().Writing_hdr_for_toc()) {
				bfr.Add_byte_if_not_last(Byte_ascii.Nl);
			}
		}
	}
	public static final int Sub_idx_null = -1;	// DELETE: placeholder for sub_idx; WHEN: need to remove Sub_grp
}
/*
NOTE_1:inline always written as <tag></tag>, not <tag/>
see WP:Permian–Triassic extinction event
this will cause firefox to swallow up rest of text
<div id="ScaleBar" style="width:1px; float:left; height:38em; padding:0; background-color:#242020" />
this will not
<div id="ScaleBar" style="width:1px; float:left; height:38em; padding:0; background-color:#242020"  ></div>
*/