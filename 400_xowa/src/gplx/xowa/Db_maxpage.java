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
public class Db_maxpage {
	private Db_conn conn;
	private Xow_wiki wiki;
	private boolean initialised = false;
	private boolean hasdata = false;
	public Db_maxpage(Xow_wiki wiki) {
		this.wiki = wiki;
	}
	private void Init() {
		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		if (db_mgr != null) {
			Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
			this.conn = page_tbl.Conn();
		}
		// need to check that the table 'maxpage' exists and contains at least one row
		hasdata = false;
		String sql_fmt = String_.Concat_lines_nl_skip_last
		( "SELECT count(*) as cnt FROM maxpage;"
		);

		String sql = sql_fmt;
		Db_rdr rdr = conn.Stmt_sql(sql).Exec_select__rls_auto();
		int cnt = 0;
		try {
			while (rdr.Move_next()) {
				cnt = rdr.Read_int("cnt");
				break;
			}
			hasdata = true;
		} catch (Exception e)	{
			System.out.println("no maxpage");
		} finally {rdr.Rls();}
		initialised = true;
	}
	public int Get_maxpage(int page_id) {
		if (!initialised) Init();
		int max = -1;
		if (hasdata) {
			String sql_fmt = String_.Concat_lines_nl_skip_last
			( "SELECT total FROM maxpage WHERE page_id={0};"
			);

			String sql = String_.Format(sql_fmt, page_id);
			Db_rdr rdr = conn.Stmt_sql(sql).Exec_select__rls_auto();
			try {
				while (rdr.Move_next()) {
					max = rdr.Read_int("total");
					break;
				}
			} finally {rdr.Rls();}

		}

		return max;
	}
	public void Create() {
		/*
create table if not exists maxpage (
page_id integer primary key,
total  integer
);
sql = """
select page_id, page_title from page
where page_title like '%/%'
and page_title not like '%.djvu%'
and page_is_redirect=0
and page_namespace=104 -- can be other numbers!!!!!!!!
order by page_title
"""
see buildpagemax.py
		*/
	}
}
