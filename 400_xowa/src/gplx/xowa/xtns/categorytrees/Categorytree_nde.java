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
package gplx.xowa.xtns.categorytrees; import gplx.*; import gplx.xowa.*;import gplx.xowa.addons.wikis.ctgs.Xoa_ctg_mgr;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.Xoctg_catpage_mgr;
import gplx.xowa.xtns.*;
import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*;
public class Categorytree_nde implements Xox_xnde {
	private Xoa_page page;
	private Categorytree_params_ params;
	private Xoctg_catpage_mgr cat_mgr;
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		// extract category to play
		cat_mgr = wiki.Ctg__catpage_mgr();
		params = new Categorytree_params_();
		page = ctx.Page();
		// have we any args?
		Mwh_atr_itm[] atrs_ary = xnde.Atrs_ary();
		int atrs_len = atrs_ary.length;
		for (int i = 0; i < atrs_len; i++) {
			Mwh_atr_itm atr = atrs_ary[i];
			byte[] key = atr.Key_bry();
			byte[] val = atr.Val_as_bry();
                        cat_mgr.Update_params(key, val, params);
		}
	}
	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		int bgn = xnde.Tag_open_end(), end = xnde.Tag_close_bgn();
		if (bgn >= end) return; //nothing to do
		cat_mgr.Renderchild(bfr, src, bgn, end, params);
		page.Html_data().Head_mgr().Itm__categorytree().Enabled_y_();
	}
}
