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
package gplx.xowa.addons.wikis.ctgs.htmls.catpages.dbs; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.*; import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.*;
import gplx.dbs.*; import gplx.xowa.wikis.data.*; import gplx.xowa.wikis.data.tbls.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.langs.*;
public class Xoctg_catlink_loader_ppq {
	private final    Xow_wiki wiki;
	private final    Xowd_page_tbl page_tbl;
	private final    Db_attach_mgr attach_mgr;
	private final    int link_dbs_len;
	Xoctg_catlink_loader_ppq(Xow_wiki wiki, Xowd_page_tbl page_tbl, int link_dbs_len, Db_attach_mgr attach_mgr) {
		this.wiki = wiki;
		this.page_tbl = page_tbl;
		this.link_dbs_len = link_dbs_len;
		this.attach_mgr = attach_mgr;
	}

	public int Run_ppq_id(int page_id) {
		List_adp catlink_list = List_adp_.New();
		for (int i = 0; i < link_dbs_len; i++) {
			String sql = Generate_sql_ppq(page_id, i + 1);
			Load_catlinks_ppq(catlink_list, sql);
		}

		// no catlinks; exit
		if (catlink_list.Len() == 0) return 0;
		Xoctg_catpage_ppq itm = (Xoctg_catpage_ppq)catlink_list.Get_at(0);
		return itm.To_id();
	}

	public int Run_ppq_ttl(Xoa_ttl ttl) {
		List_adp catlink_list = List_adp_.New();
		for (int i = 0; i < link_dbs_len; i++) {
			String sql = Generate_sql_ppq_ttl(ttl, i + 1);
			Load_catlinks_ppq(catlink_list, sql);
		}

		// no catlinks; exit
		if (catlink_list.Len() == 0) return 0;
		Xoctg_catpage_ppq itm = (Xoctg_catpage_ppq)catlink_list.Get_at(0);
		return itm.To_id();
	}

	private String Generate_sql_ppq(int cat_id, int link_db_id) {
		// build sql;
		Bry_bfr bfr = Bry_bfr_.New();
		bfr.Add_str_u8_fmt(String_.Concat_lines_nl
		( "SELECT  cl_to_id"
		, ",       cl_from"
		, "FROM    <link_db_{0}>cat_link cl"
		, "WHERE   cl_from={1}"
		), link_db_id, cat_id);
		return attach_mgr.Resolve_sql(bfr.To_str_and_clear());
	}
	private String Generate_sql_ppq_ttl(Xoa_ttl ttl, int link_db_id) {
		// build sql;
		Bry_bfr bfr = Bry_bfr_.New();
		bfr.Add_str_u8_fmt(String_.Concat_lines_nl
 		( "SELECT  cl_to_id"
		, ",       cl_from"
		, "FROM    <link_db_{0}>cat_link cl"
                , "JOIN <page_db>page p ON p.page_id = cl.cl_from"
		, "WHERE   page_namespace=104 AND page_title='{1}'"
		), link_db_id, ttl.Full_db_as_str().substring(5));
		return attach_mgr.Resolve_sql(bfr.To_str_and_clear());
	}
	private void Load_catlinks_ppq(List_adp catlink_list, String sql) {
		Db_rdr rdr = Db_rdr_.Empty;
		int count = 0;
		try {
			attach_mgr.Attach();
			rdr = attach_mgr.Conn_main().Stmt_sql(sql).Exec_select__rls_auto();
			while (rdr.Move_next()) {
				Xoctg_catpage_ppq itm = Xoctg_catpage_ppq.New_by_rdr(wiki, rdr);
				catlink_list.Add(itm);
				count++;
			}
		}
		catch (Exception e) {
                    throw Err_.new_exc(e, "db", "db.yyyy failed", "sql", sql);}
		finally {
			rdr.Rls();
			attach_mgr.Detach();
		}
	}
	public static Xoctg_catlink_loader_ppq Create(Xow_wiki wiki, Xowd_page_tbl page_tbl, Xow_db_mgr db_mgr, Db_conn cat_core_conn) {
		// init db vars
		List_adp db_list = List_adp_.New();
		Db_conn db_1st = null;
		int db_idx = 0;

		// fill db_list by looping over each db unless (a) cat_link_db or (b) core_db (if all or few)
		int len = db_mgr.Dbs__len();
		for (int i = 0; i < len; ++i) {
			Xow_db_file cl_db = db_mgr.Dbs__get_at(i);
			String key = "";
			switch (cl_db.Tid()) {
				case Xow_db_file_.Tid__cat_link:	// always use cat_link db
					key = "link_db_" + ++db_idx;
					break;
				case Xow_db_file_.Tid__cat_core:
					key = "cat_core";
					break;
				default:							// skip all other files
					continue;
			}

			// add to db_list
			if (db_1st == null) db_1st = cl_db.Conn();
			db_list.Add(new Db_attach_itm(key, cl_db.Conn()));
		}

		// make attach_mgr
		int link_dbs_len = db_list.Len() - 1; // dont count 'core'

		// add page_db
		db_list.Add(new Db_attach_itm("page_db", page_tbl.Conn()));

		Db_attach_mgr attach_mgr = new Db_attach_mgr(db_1st, (Db_attach_itm[])db_list.To_ary_and_clear(Db_attach_itm.class));
		return new Xoctg_catlink_loader_ppq(wiki, page_tbl, link_dbs_len, attach_mgr);
	}
}
