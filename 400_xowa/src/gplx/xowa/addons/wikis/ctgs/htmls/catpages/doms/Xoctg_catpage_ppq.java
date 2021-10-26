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
package gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.*;
import gplx.dbs.*; import gplx.xowa.wikis.nss.*;
public class Xoctg_catpage_ppq {
	Xoctg_catpage_ppq(int from_id, int to_id) {
		this.from_id = from_id;
		this.to_id = to_id;
	}
	public int					From_id()			{return from_id;}			private final    int from_id;
	public int					To_id()			{return to_id;}			private final    int to_id;

	public static final    Xoctg_catpage_ppq[] Ary_empty = new Xoctg_catpage_ppq[0];
	public static Xoctg_catpage_ppq New_by_rdr(Xow_wiki wiki, Db_rdr rdr) {
		Xoctg_catpage_ppq rv = new Xoctg_catpage_ppq(rdr.Read_int("cl_from"), rdr.Read_int("cl_to_id"));
		return rv;
	}
}
