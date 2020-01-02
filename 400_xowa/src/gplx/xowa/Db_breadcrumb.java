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
import gplx.xowa.parsers.Xop_ctx;
import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.wikis.data.Xow_db_mgr;
import gplx.xowa.wikis.data.tbls.Xowd_page_tbl;
public class Db_breadcrumb {
	private Db_conn conn;
	private Xow_wiki wiki;
	private boolean initialised = false;
	private boolean hasdata = true;
	public Db_breadcrumb(Xow_wiki wiki) {
		this.wiki = wiki;
	}
	private void Init() {
		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		if (db_mgr != null) {
			Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
			this.conn = page_tbl.Conn();
		}
		// need to check that the table 'parent' exists and contains at least one row
		initialised = true;
	}
	public byte[] Get_breadcrumbs(Xowe_wiki wiki, Xop_ctx ctx, byte[] ttl, byte[] isin, byte[] redirect_msg) {
		if (!initialised) Init();
		List_adp breadcrumb_list = List_adp_.New();
		if (hasdata) {
			Db_stmt stmt = conn.Stmt_sql(sql);
			stmt.Crt_bry_as_str("ttl", ttl);
			Db_rdr rdr = stmt.Exec_select__rls_auto();
			try {
				while (rdr.Move_next()) {
					byte[] parent = rdr.Read_str("parent").getBytes();
					breadcrumb_list.Add(parent);
				}
			} finally {rdr.Rls();}

		} else {
			breadcrumb_list.Add(isin);
		}

		Bry_bfr tmp_bfr = Bry_bfr_.New();
		int len = breadcrumb_list.Count();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				byte[] parent = (byte[])breadcrumb_list.Get_at(i);
				if (i > 0)
					tmp_bfr.Add_str_a7( " > " );
				int colon = Bry_find_.Find_fwd(parent, Byte_ascii.Colon);
				byte[] lnk;
				if (colon > 0)
					lnk = Bry_.Add(Xop_tkn_.Lnki_bgn, Byte_ascii.Colon_bry, parent, Byte_ascii.Pipe_bry, Bry_.Mid(parent, colon+1), Xop_tkn_.Lnki_end);		// make "[[xx:ttl ttl]]"
				else
					lnk = Bry_.Add(Xop_tkn_.Lnki_bgn, parent, Xop_tkn_.Lnki_end);		// make "[[ttl]]"
				tmp_bfr.Add( lnk );
			}
			if (len > 0)
				tmp_bfr.Add_str_a7( " > " );
			int colon = Bry_find_.Find_fwd(ttl, Byte_ascii.Colon);
			if (colon > 0)
				tmp_bfr.Add_mid(ttl, colon+1, ttl.length);
			else
				tmp_bfr.Add(ttl);
		}
		byte[] bread = tmp_bfr.To_bry_and_clear();
		wiki.Parser_mgr().Main().Parse_text_to_html(tmp_bfr, ctx, ctx.Page(), false, bread);
		bread = tmp_bfr.To_bry_and_clear();
		tmp_bfr.Add_str_a7("<div class=\"ext-wpb-pagebanner-subtitle\">");
		if (redirect_msg.length != 0) {
			tmp_bfr.Add(redirect_msg).Add_str_a7("<br/>\n");
		}
		tmp_bfr.Add(bread);
		tmp_bfr.Add_str_a7("</div>");
		return tmp_bfr.To_bry_and_clear();
	}
	public void Insert(byte[] ttl, byte[] isin) {
		// insert into parent values(ttl, isin)
	}
	private static String sql = String_.Concat_lines_nl_skip_last
	( "WITH RECURSIVE bread(count, parent) AS ("
	, "  SELECT 1, parent FROM parent WHERE ttl=?"
	, "  UNION ALL"
	, "  SELECT bread.count+1, parent.parent FROM parent, bread WHERE bread.parent=parent.ttl)"
	, "SELECT parent FROM bread ORDER BY count DESC;"
	);
}
