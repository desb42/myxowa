/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.addons.wikis.ctgs.htmls.catpages.fmts;
import gplx.*; import gplx.xowa.*;
import gplx.core.intls.ucas.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms.*;	
public class Xoctg_fmt_ltr implements gplx.core.brys.Bfr_arg {	// "A", "B", "C cont."
	private final    Xoctg_fmt_itm_base itm_fmt;
	private Xoctg_catpage_grp grp;
	//private byte[] msg__list_continues;
	public Xoctg_fmt_ltr(Xoctg_fmt_itm_base itm_fmt) {
		this.itm_fmt = itm_fmt;
	}
	public void Init_from_grp(Xow_wiki wiki, Xoctg_catpage_grp grp) {
		this.grp = grp;
		//this.msg__list_continues = wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_list_continues);
		itm_fmt.Init_from_ltr(wiki, grp);
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int itm_idx = 0;
		int itm_end = grp.Itms__len();
		int itms_len = itm_end - itm_idx; if (itms_len == 0) return;	// no items; exit

		// according to mediawiki/includes/CategoryViewer.php cutoff default is 6
		byte[] startdiv, enddiv;
		if (itm_end > 6) {
			startdiv = Bry_.new_a7("<div class=\"mw-category-group\">");
			enddiv = Bry_.new_a7("</div>");
		} else {
			startdiv = Bry_.new_a7("<div class=\"mw-category-group\">");
			enddiv = Bry_.new_a7("</div>");
		}
		// loop itms until no more itms
		while (itm_idx < itm_end) {
			Xoctg_catpage_itm itm = grp.Itms__get_at(itm_idx);

/*
                        // get ltr_head; EX: "C" or "C cont."
			byte[] itm_sortkey = itm.Sortkey_handle();
			// byte[] ltr_cur = gplx.core.intls.Utf8_.Get_char_at_pos_as_bry(itm_sortkey, 0);
			byte[] ltr_head = ltr_extractor.Get_1st_ltr(itm_sortkey);
			ltr_head = DB_case_mgr.Case_build_1st(true, ltr_head);
*/
                        byte[] ltr_head = itm.First_char();

			// set ltr and idx
			itm_fmt.Set_ltr_and_bgn(ltr_head, itm_idx);

			if (ltr_head[0] == ' ')
				ltr_head = Bry_.new_a7("&nbsp;");
			// loop until (a) end of ltr
			Fmt__tbl.Bld_many(bfr, startdiv, ltr_head, itm_fmt, enddiv);
			itm_idx = itm_fmt.Loop_end_idx();
		}
	}
	private static final    Bry_fmt
	 Fmt__tbl = Bry_fmt.Auto_nl_skip_last
	( ""
	, "          ~{startdiv}"
	, "          <h3>~{ltr_head}</h3>"	// EX: "A", "A cont."
	, "          <ul>~{itms}"
	, "          </ul>"
	, "          ~{enddiv}"
	);
}
