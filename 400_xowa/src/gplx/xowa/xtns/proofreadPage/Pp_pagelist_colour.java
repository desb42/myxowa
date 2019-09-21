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
import gplx.core.primitives.*; import gplx.core.brys.fmtrs.*;
import gplx.xowa.apps.cfgs.*;
import gplx.langs.htmls.entitys.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.wikis.nss.*;
import gplx.xowa.xtns.lst.*; import gplx.xowa.wikis.pages.*; import gplx.xowa.wikis.data.tbls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.amps.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*; import gplx.xowa.parsers.lnkis.*; import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.parsers.lnkis.files.*;
import gplx.xowa.wikis.pages.lnkis.Xopg_colour;
public class Pp_pagelist_colour {
	public static void Colour(List_adp list, Bry_bfr bfr, Xow_wiki wiki) {
		// only check on Page: pages
		int len = list.Count();
		for (int i = 0; i < len; i++) {
			Xopg_colour colour = (Xopg_colour)list.Get_at(i);
			Xowd_page_itm page_row = colour.Page_row();
			if (page_row.Ns_id() != 104) // Namespace Page:
				continue;
			// either here or in Xopg_colour
			//  should lookup pagequality in cat_link table
			//
			String quality;
			int catqual = Pp_quality.getQualityFromCatlink(page_row.Id(), wiki);
			if (catqual >= 0 && catqual <= 4)
				quality = String.valueOf(catqual);
			else
				continue;
			String colour_class = "prp-pagequality-" + quality + " quality" + quality;
			//String colour_class = colour.Class();
			//colour_class = "prp-pagequality-4 quality4";
			if (colour_class != "") {
				bfr.Add_byte_comma().Add_byte(Byte_ascii.Brack_bgn);
				bfr.Add_byte_quote().Add_str_u8(colour.Html_uid()).Add_byte_quote();
				bfr.Add_byte_comma();
				bfr.Add_byte_quote().Add_str_u8(colour_class).Add_byte_quote();
				bfr.Add_byte(Byte_ascii.Brack_end);
			}
		}
	}
}