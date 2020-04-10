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
public class Db_index_page {
	private Db_conn conn;
	private Xow_wiki wiki;
	public Db_index_page(Xow_wiki wiki) {
		this.wiki = wiki;
	}
	public Xoa_ttl Get_index_page(int page_id) {
		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		if (db_mgr != null) {
			Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
			this.conn = page_tbl.Conn();
		}
		Xoa_ttl ttl = null;
		String sql_fmt = String_.Concat_lines_nl_skip_last
		( "SELECT page_id, page_namespace, page_title FROM index_link il"
		, "JOIN page p ON p.page_id=il.src_id"
		, "WHERE trg_id={0};"
		);

		String sql = String_.Format(sql_fmt, page_id);
                try {
		Db_rdr rdr = conn.Stmt_sql(sql).Exec_select__rls_auto();
		try {
			while (rdr.Move_next()) {
				int index_page_id = rdr.Read_int("page_id");
				int page_namespace = rdr.Read_int("page_namespace");
				byte[] page_title = rdr.Read_bry_by_str("page_title");
				ttl = Xoa_ttl.Parse(wiki, page_namespace, page_title);
				break;
			}
		} finally {rdr.Rls();}
                } catch (Exception e)	{} // ignore all sql errors!
		return ttl;
	}
	public void Create() {
		/*
-- cut down page_link table
create table index_link (
trg_id integer primary key,
src_id integer
);

insert into index_link (trg_id, src_id)
select z.trg_id, z.src_id from 
(select trg_id, src_id from page_link pl
join page p on p.page_id=pl.trg_id
where src_id in
(select page_id from page where page_namespace=106)
and (p.page_namespace=0
or p.page_namespace=104)
) z
group by z.trg_id
having count(*) = 1

-- lookup
select page_namespace, page_title, * from index_link il
join page p on p.page_id=il.src_id
where trg_id=130409
		*/
	}
}
