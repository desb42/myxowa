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
package gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.*; import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.*;
import gplx.dbs.*; import gplx.xowa.wikis.nss.*;
public class Xoctg_dynamic_itm extends Xoctg_catpage_itm {
	public Xoctg_dynamic_itm(byte grp_tid, int page_id, byte[] sortkey_prefix, byte[] sortkey_binary, String cat_date, String touch_date) {
		super((byte)4, grp_tid, page_id, sortkey_prefix, sortkey_binary, 0, 0, 0);
		this.cat_date = cat_date;
		this.touch_date = touch_date;
	}
	public String				Cat_date()			{return cat_date;}			private String cat_date;
	public String				Touch_date()			{return touch_date;}			private String touch_date;

	public static Xoctg_dynamic_itm New_by_rdr(Xow_wiki wiki, Db_rdr rdr) {
		byte[] sortkey_binary = rdr.Read_bry("cl_sortkey");
		byte[] sortkey_prefix = rdr.Read_bry_by_str("cl_sortkey_prefix");
		String cat_date = rdr.Read_str("cl_timestamp");
		String touch_date = rdr.Read_str("page_touched");
		Xoctg_dynamic_itm rv = new Xoctg_dynamic_itm(rdr.Read_byte("cl_type_id"), rdr.Read_int("cl_from")
			, sortkey_prefix, sortkey_binary, cat_date, touch_date
			);

		String ttl_str = rdr.Read_str("page_title");
		if (ttl_str != null) {// NOTE: ttl_str will be NULL if LEFT JOIN fails on page_db.page
			rv.Page_ttl_(wiki.Ttl_parse(rdr.Read_int("page_namespace"), Bry_.new_u8(ttl_str)));
		}
		return rv;
	}
}
