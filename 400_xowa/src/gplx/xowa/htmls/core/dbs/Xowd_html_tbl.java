/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.htmls.core.dbs;
import gplx.*;
import gplx.xowa.htmls.*;
import gplx.dbs.*;
import gplx.core.brys.*;
import gplx.xowa.wikis.caches.Db_html_body;
public class Xowd_html_tbl implements Db_tbl {
	private final String fld_page_id, fld_body;
	private Db_stmt stmt_select, stmt_insert, stmt_update;
	private final    Int_flag_bldr body_flag_bldr = Make_body_flag_bldr();
	public Xowd_html_tbl(Db_conn conn) {
		this.conn = conn;
		this.fld_page_id = flds.Add_int_pkey("page_id");
		this.fld_body    = flds.Add_bry("body");
		conn.Rls_reg(this);
	}
	public Db_conn Conn() {return conn;} private final    Db_conn conn;
	public String Tbl_name() {return tbl_name;} private final    String tbl_name = "html";
	public Dbmeta_fld_list Flds() {return flds;} private final    Dbmeta_fld_list flds = new Dbmeta_fld_list();
	public void Create_tbl() {conn.Meta_tbl_create(Dbmeta_tbl_itm.New(tbl_name, flds));}

	public void Insert_bgn() {conn.Txn_bgn("html__insert"); stmt_insert = conn.Stmt_insert(tbl_name, flds);}
	public void Insert_end() {conn.Txn_end(); stmt_insert = Db_stmt_.Rls(stmt_insert);}
	public void Insert(int page_id, byte[] body, Db_html_body html_body) {
		if (stmt_insert == null) stmt_insert = conn.Stmt_insert(tbl_name, flds);
		stmt_insert.Clear().Val_int(fld_page_id, page_id);
		if (html_body != null) {
			body = html_body.Add_entry(page_id, body);
		}
		Fill_stmt(stmt_insert, body);
		stmt_insert.Exec_insert();
	}
	public void Update(int page_id, byte[] body, Db_html_body html_body) {
		if (stmt_update == null) stmt_update = conn.Stmt_update_exclude(tbl_name, flds, fld_page_id);
		stmt_update.Clear();
		Fill_stmt(stmt_insert, body);
		stmt_update.Crt_int(fld_page_id, page_id).Exec_update();
	}
	public void Upsert(int page_id, byte[] body, Db_html_body html_body) {
		Db_rdr rdr = conn.Stmt_select(tbl_name, flds, fld_page_id).Clear().Crt_int(fld_page_id, page_id).Exec_select__rls_auto();
		boolean exists = rdr.Move_next();
		rdr.Rls();
		if (exists)
			Update(page_id, body, html_body);
		else
			Insert(page_id, body, html_body);
	}
	public void Delete(int page_id) {
		Gfo_usr_dlg_.Instance.Log_many("", "", "db.html: delete started: db=~{0} page_id=~{1}", conn.Conn_info().Raw(), page_id);
		conn.Stmt_delete(tbl_name, fld_page_id).Crt_int(fld_page_id, page_id).Exec_delete();
		Gfo_usr_dlg_.Instance.Log_many("", "", "db.html: delete done");
	}
	public void Update_page_id(int old_id, int new_id) {
		Gfo_usr_dlg_.Instance.Log_many("", "", "db.html: update page_id started: db=~{0} old_id=~{1} new_id=~{2}", conn.Conn_info().Raw(), old_id, new_id);
		conn.Stmt_update(tbl_name, String_.Ary(fld_page_id), fld_page_id).Val_int(fld_page_id, new_id).Crt_int(fld_page_id, old_id).Exec_update();
		Gfo_usr_dlg_.Instance.Log_many("", "", "db.html: update page_id done");
	}
	public boolean Select_by_page(Xoh_page hpg) {
		if (stmt_select == null) stmt_select = conn.Stmt_select(tbl_name, flds, fld_page_id);
		Db_rdr rdr = stmt_select.Clear().Crt_int(fld_page_id, hpg.Page_id()).Exec_select__rls_manual();
		try {
			if (rdr.Move_next()) {
				hpg.Ctor_by_db_body(rdr.Read_bry(fld_body));
				return true;
			}
			return false;
		}
		finally {rdr.Rls();}
	}
	public boolean Select_as_row(Xowd_html_row rv, int page_id) {
		if (stmt_select == null) stmt_select = conn.Stmt_select(tbl_name, flds, fld_page_id);
		Db_rdr rdr = stmt_select.Clear().Crt_int(fld_page_id, page_id).Exec_select__rls_manual();
		try {
			if (rdr.Move_next()) {
				rv.Load
				( page_id
				, rdr.Read_bry(fld_body)
				);
				return true;
			}
			return false;
		}
		finally {rdr.Rls();}
	}
	public void Rls() {
		stmt_insert = Db_stmt_.Rls(stmt_insert);
		stmt_select = Db_stmt_.Rls(stmt_select);
		stmt_update = Db_stmt_.Rls(stmt_update);
	}
	public void Fill_stmt(Db_stmt stmt, byte[] body) {
		stmt.Val_bry(fld_body, body);
	}
	public static Int_flag_bldr Make_body_flag_bldr() {return new Int_flag_bldr().Pow_ary_bld_(3, 2);}	// 8 different zip types; 4 different hzip types
}
