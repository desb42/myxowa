/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2021 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa;
import gplx.Bry_bfr;
import gplx.xowa.htmls.Xoh_page_wtr_mgr;
import gplx.xowa.htmls.portal.Xow_portal_mgr;
import gplx.xowa.parsers.Xop_ctx;
import gplx.xowa.htmls.core.htmls.Xoh_wtr_ctx;
import gplx.xowa.xtns.wbases.Wdata_xwiki_link_wtr;
public interface Db_skin {
	public String Skintags();
	public byte[] Content(Xow_portal_mgr portal_mgr, byte[] page_data, Xoae_page page, Xowe_wiki wiki, Xop_ctx ctx, Xoh_wtr_ctx hctx, byte html_gen_tid, byte[] pagename_for_h1, byte[] modified_on_msg, int page_mode, Bry_bfr tmp_bfr, boolean ispage_in_wikisource, Wdata_xwiki_link_wtr wdata_lang_wtr, boolean ctgs_enabled, Xoh_page_wtr_mgr mgr);
}
