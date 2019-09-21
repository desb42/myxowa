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
package gplx.xowa.xtns.kartographers; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.logs.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*;
public class Maplink_xnde implements Xox_xnde, Mwh_atr_itm_owner2 {
	private byte[] latitude, longitude, text, zoom;
	private int json_bgn, json_end;
	public void Xatr__set(Xowe_wiki wiki, byte[] src, Mwh_atr_itm xatr, byte xatr_id) {
		switch (xatr_id) {
			case Maplink_xnde_atrs.Tid__latitude:   latitude = xatr.Val_as_bry(); break;
			case Maplink_xnde_atrs.Tid__longitude:  longitude = xatr.Val_as_bry(); break;
			case Maplink_xnde_atrs.Tid__text:       text = xatr.Val_as_bry(); break;
			case Maplink_xnde_atrs.Tid__zoom:       zoom = xatr.Val_as_bry(); break;
			default:
				break;
		}
	}
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xox_xnde_.Parse_xatrs(wiki, this, Maplink_xnde_atrs.Key_hash, src, xnde);
		ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_bgn);
		json_bgn = xnde.Tag_open_end();
		json_end = xnde.Tag_close_bgn();
		//boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_maplink, src, xnde);
		//ctx.Para().Process_block__xnde(xnde.Tag(), Xop_xnde_tag.Block_end);
	}
	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		bfr.Add(Bry_.Replace(text, Byte_ascii.Underline, Byte_ascii.Space));
	}
	public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;
}
