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
package gplx.xowa; import gplx.*;
import gplx.dbs.*;
import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.wikis.data.Xow_db_mgr;
import gplx.xowa.wikis.data.tbls.Xowd_page_tbl;
public class Db_page_image {
	private Db_conn conn;
	private Xow_wiki wiki;
	public Db_page_image(Xow_wiki wiki) {
		this.wiki = wiki;
	}
	public Db_page_image_ Get_page_image(int page_id) {
		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		if (db_mgr != null) {
			Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
			this.conn = page_tbl.Conn();
		}
		Xoa_ttl ttl = null;
		String sql_fmt = String_.Concat_lines_nl_skip_last
		( "SELECT pi_title, width, height, site_id"
		, "FROM page_image"
		, "WHERE page_id={0};"
		);

		byte[] pi_title = null;
		int width = 0;
		int height = 0;
		int site_id = 0;
		String sql = String_.Format(sql_fmt, page_id);
                try {
		Db_rdr rdr = conn.Stmt_sql(sql).Exec_select__rls_auto();
		try {
			while (rdr.Move_next()) {
				pi_title = rdr.Read_bry_by_str("pi_title");
				width = rdr.Read_int("width");
				height = rdr.Read_int("height");
				site_id = rdr.Read_int("site_id");
				//ttl = Xoa_ttl.Parse(wiki, page_namespace, page_title);
				break;
			}
		} finally {rdr.Rls();}
                } catch (Exception e)	{} // ignore all sql errors!
		return new Db_page_image_(pi_title, width, height, site_id);
	}
}
