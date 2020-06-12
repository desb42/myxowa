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
package gplx.xowa.xtns.dynamicPageList; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.primitives.*; import gplx.core.lists.*;
import gplx.xowa.wikis.dbs.*;
import gplx.xowa.wikis.data.tbls.*;
import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.urls.*;
import gplx.xowa.wikis.data.Xow_db_mgr;
import gplx.dbs.*; import gplx.xowa.wikis.data.*; import gplx.xowa.addons.wikis.ctgs.dbs.*;
import gplx.core.times.*;
class Dpl_page_finder {
	private final    DateAdp_parser dateParser = DateAdp_parser.new_();
	private final    Dpl_itm itm;
	private final    Xowe_wiki wiki;

	public Dpl_page_finder(Dpl_itm itm, Xowe_wiki wiki) {
		this.itm = itm;
		this.wiki = wiki;
	}
	public List_adp Find() {
		// get include_ttls
		List_adp include_ttls = itm.Ctg_includes();
		List_adp exclude_ttls = itm.Ctg_excludes();
		if (include_ttls == null && exclude_ttls == null) return null; // exit early if none exists

		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
		int len;
		int[] cat_ids;
		if (include_ttls == null)
			len = 0;
		else
			len = include_ttls.Len();
		cat_ids = new int[len];
		int realincludecats = 0;
		for (int i = 0; i < len; i++) {
			// get cat page ids
			int id = Get_ctg_ttl_page_id(include_ttls, i, page_tbl);
			if (id != -1)
				realincludecats++;
			cat_ids[i] = id;
		}
		if (realincludecats == 0 && exclude_ttls == null)
			return null; // exit early if none exists
		itm.Ctg_include_ids_(cat_ids);
		if (exclude_ttls == null)
			len = 0;
		else
			len = exclude_ttls.Len();
		cat_ids = new int[len];
		int realexcludecats = 0;
		for (int i = 0; i < len; i++) {
			int id = Get_ctg_ttl_page_id(exclude_ttls, i, page_tbl);
			if (id != -1)
				realexcludecats++;
			cat_ids[i] = id;
		}
		if (realincludecats == 0 && realexcludecats == 0)
			return null; // exit early if none exists
		itm.Ctg_exclude_ids_(cat_ids);

		//Xoctg_catpage_ctg ctg = wiki.Ctg__catpage_mgr().Get_by_cache_or_null(page_ttl, Xoctg_catpage_url.New__blank(), cat_ttl, 10000, itm);

		Xowd_cat_core_tbl cat_core_tbl = Xodb_cat_db_.Get_cat_core_or_fail(db_mgr);

		return Dpl_loader(wiki, page_tbl, db_mgr, cat_core_tbl.Conn());
	}

	private int Get_ctg_ttl_page_id(List_adp list, int i, Xowd_page_tbl page_tbl) {// helper method to extract ttl from list
		// get ttl
		byte[] ttl_bry = (byte[])list.Get_at(i);
		Xoa_ttl ttl = wiki.Ttl_parse(gplx.xowa.wikis.nss.Xow_ns_.Tid__category, ttl_bry);

		// log if invalid; NOTE: pages in en.n will pass "{{{2}}}" as category title; PAGE:en.b:Category:Egypt DATE:2016-10-18
		if (ttl == null) {
			Gfo_usr_dlg_.Instance.Log_many("", "", "category title is invalid; wiki=~{0} page=~{1} ttl=~{2}", wiki.Domain_str(), itm.Page_ttl(), ttl_bry);
		}
		if (ttl == null) return -1;

		Xowd_page_itm page_itm = page_tbl.Select_by_ttl_as_itm_or_null(ttl);
		if (page_itm == null) {
			Gfo_usr_dlg_.Instance.Log_many("", "", "dpl category does not exist in page table; wiki=~{0} page=~{1} ttl=~{2}", wiki.Domain_str(), "?", ttl.Full_db());	// Log instead of Warn b/c happens many times in en.d, en.b, en.u; DATE:2016-10-22
			return -1;
		}
		return page_itm.Id();
	}

	private String Generate_sql_dpl(Dpl_itm itm, int limit, int link_db_id, Db_attach_mgr attach_mgr) {
		StringBuilder where = new StringBuilder();
		java.util.Formatter where_fmt = new java.util.Formatter(where);
		where.append(" where 1");
		StringBuilder table = new StringBuilder();
		java.util.Formatter table_fmt = new java.util.Formatter(table);

		String fields = String_.Concat_lines_nl
		( "SELECT  c1.cl_to_id"
		, ",       c1.cl_from"
		, ",       c1.cl_type_id"
		, ",       datetime(c1.cl_timestamp_unix, 'unixepoch') as cl_timestamp"
		, ",       c1.cl_sortkey"
		, ",       c1.cl_sortkey_prefix"
		, ",       p.page_namespace"
		, ",       p.page_title"
		, ",       p.page_touched"
		);
		
		switch ( itm.Redirects_mode() ) {
			case Dpl_redirect.Tid_only:
				where.append(" and page_is_redirect=1");
				break;
			case Dpl_redirect.Tid_exclude:
				where.append(" and page_is_redirect=0");
				break;
		}

		// filter by namespace
		where.append(" and page_namespace=" + Integer.toString(itm.Ns_filter()));

		if ( itm.IgnoreSubpages() ) {
			where.append(" and page_title NOT like %/%");
		}

		int currentTableNumber = 1;
		
		table.append("\nfrom <page_db>page p\n");

		int i;
		int catCount = itm.Ctg_include_ids().length;
		for ( i = 0; i < catCount; i++ ) {
			int id = itm.Ctg_include_ids()[i];
			if (id == -1) continue; // ignore invalid entries
			table_fmt.format("INNER JOIN <link_db_%d>cat_link c%d on p.page_id = c%d.cl_from AND c%d.cl_to_id=%d\n",
				link_db_id, currentTableNumber, currentTableNumber, currentTableNumber, itm.Ctg_include_ids()[i]);

			currentTableNumber++;
		}

		int excludeCatCount = itm.Ctg_exclude_ids().length;
		for ( i = 0; i < excludeCatCount; i++ ) {
			table_fmt.format("LEFT OUTER JOIN <link_db_%d>cat_link c%d on p.page_id = c%d.cl_from AND c%d.cl_to_id=%d\n",
				link_db_id, currentTableNumber, currentTableNumber, currentTableNumber, itm.Ctg_exclude_ids()[i]);
			where_fmt.format(" and c%d.cl_to_id is null", currentTableNumber);
			currentTableNumber++;
		}

		String sqlOrder;
		if ( itm.Sort_ascending() == Bool_.Y_byte ) {
			sqlOrder = "ASC";
		} else {
			sqlOrder = "DESC";
		}

		String sqlSort;
		switch ( itm.Sort_tid() ) {
			case Dpl_sort.Tid_lastedit:
				sqlSort = "page_touched";
				break;
			case Dpl_sort.Tid_length:
				sqlSort = "page_len";
				break;
			case Dpl_sort.Tid_created:
				sqlSort = "page_id"; // Since they're never reused and increasing
				break;
			case Dpl_sort.Tid_categorysortkey:
				sqlSort = "c1.cl_type_id " + sqlOrder + ", c1.cl_sortkey";
				break;
			case Dpl_sort.Tid_popularity:
				sqlSort = "page_counter";
				break;
			default:
			case Dpl_sort.Tid_categoryadd:
				sqlSort = "c1.cl_timestamp_unix";
				break;
		}
		String order_by = "\norder by " + sqlSort + " " + sqlOrder;
		String options;
		if (itm.Offset() > 0)
			options = "\nlimit " + Integer.toString(itm.Offset()) + "," + Integer.toString(limit);
		else
			options = "\nlimit " + Integer.toString(limit);
		String sql = fields + table.toString() + where.toString() + order_by + options; 
		return attach_mgr.Resolve_sql(sql);
	}
/* copied from 400_xowa\src\gplx\xowa\addons\wikis\ctgs\htmls\catpages\dbs\Xoctg_catlink_loader.java */
	private void Load_catlinks(List_adp catlink_list, String sql, Db_attach_mgr attach_mgr) {
		synchronized (this) {
			Db_rdr rdr = Db_rdr_.Empty;
			int count = 0;
			try {
				attach_mgr.Attach();
				rdr = attach_mgr.Conn_main().Stmt_sql(sql).Exec_select__rls_auto();
				while (rdr.Move_next()) {
					Xoctg_dynamic_itm itm = Xoctg_dynamic_itm.New_by_rdr(wiki, rdr);
					catlink_list.Add(itm);
					if (count >= 1000 && (count % 1000) == 0) Gfo_usr_dlg_.Instance.Prog_many("", "", "loading cat_links: count=~{0}", count);
					count++;
				}
			}
			catch (Exception e) {
				throw Err_.new_exc(e, "db", "db.xxxxxx failed", "sql", sql);}
			finally {
				rdr.Rls();
				attach_mgr.Detach();
			}
		}
	}
	public List_adp Dpl_loader(Xow_wiki wiki, Xowd_page_tbl page_tbl, Xow_db_mgr db_mgr, Db_conn cat_core_conn) {
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
				case Xow_db_file_.Tid__core:		// only use core if all or few
					if (db_mgr.Props().Layout_text().Tid_is_lot()) 
						continue;
					else
						break;
				default:							// skip all other files
					continue;
			}

			// add to db_list
			if (db_1st == null) db_1st = cl_db.Conn();
			db_list.Add(new Db_attach_itm(key, cl_db.Conn()));
		}

		// make attach_mgr
		byte version = 4;
		int link_dbs_len = db_list.Len() - 1; // dont count 'core'
		if (cat_core_conn.Meta_tbl_exists("cat_sort")) {
			version = 3;
			db_1st = cat_core_conn;
		}

		// add page_db
		db_list.Add(new Db_attach_itm("page_db", page_tbl.Conn()));

		Db_attach_mgr attach_mgr = new Db_attach_mgr(db_1st, (Db_attach_itm[])db_list.To_ary_and_clear(Db_attach_itm.class));

		List_adp catlink_list = List_adp_.New();
		for (int i = 0; i < link_dbs_len; i++) {
			String sql = Generate_sql_dpl(itm, 200, i + 1, attach_mgr);
			Load_catlinks(catlink_list, sql, attach_mgr);
		}

		List_adp result_pages = List_adp_.New();
			len = catlink_list.Count();
			for (int i = 0; i < len; i++) {
				Xoctg_dynamic_itm cp_itm = (Xoctg_dynamic_itm)catlink_list.Get_at(i);
				int itm_page_id = cp_itm.Page_id();

				Xowd_page_itm page = new Xowd_page_itm();
				if (cp_itm.Page_ttl() == null) continue; // cat_link can exist without entry in page_db.page
				page.Id_(itm_page_id);
				page.Ttl_(cp_itm.Page_ttl());
				if (itm.Show_date())
					page.Modified_on_(DateAdp_.seg_(dateParser.Parse_iso8651_like(cp_itm.Cat_date())));
				result_pages.Add(page);
			}
		return result_pages;
	}
}
