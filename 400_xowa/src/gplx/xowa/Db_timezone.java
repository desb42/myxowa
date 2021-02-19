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
import gplx.dbs.*; import gplx.dbs.engines.sqlite.*;
import gplx.xowa.xtns.pfuncs.times.*;
import gplx.xowa.wikis.data.Xow_db_mgr;
import gplx.xowa.wikis.data.tbls.Xowd_page_tbl;
import gplx.core.btries.*;
import gplx.xowa.parsers.Xop_ctx;
import java.io.*;
public class Db_timezone {
	private static Db_conn conn;
	private static Xowe_wiki wiki = null;
	private static Xoae_app app;
	private static boolean initialised = false;
	public static void Set_wiki(Xowe_wiki w, Xoae_app xapp) {
		wiki = w;
		app = xapp;
	}
	private static final String create_sql = String_.Concat_lines_nl
	( "CREATE TABLE if not exists timezone ("
	, "id      integer primary key autoincrement,"
	, "code    varchar(256),"
	, "dst     integer,"
	, "offset  integer,"
	, "tz_name varchar(256)"
	, ");"
	);
	public static void Init() {
		if (initialised) return;
		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		if (db_mgr != null) {
			Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
			conn = page_tbl.Conn();
		}
		boolean found = false;
		// need to check that the table 'timezone' exists and has at least one entry
		try {
			Db_stmt stmt = conn.Stmt_sql("select * from timezone limit 1;");
			Db_rdr rdr = stmt.Exec_select__rls_auto();
			try {
				while (rdr.Move_next()) {
					byte[] code = rdr.Read_str("code").getBytes();
					int id = rdr.Read_int("id");
				found = true;
				}
			} 
			finally {rdr.Rls();}
		}
		catch (Exception e) { }
		if (!found) {
			Sqlite_engine_.Tbl_create(conn, "timezone", create_sql);
			try {
				String fname = app.Fsys_mgr().Bin_any_dir().GenSubFil_nest("xowa", "file", "timezones.sql").toString();//.To_http_file_str();
				File file = new File(fname);    //creates a new file instance
				FileReader fr = new FileReader(file);   //reads the file
				BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
				String line;
				conn.Txn_bgn("bulk_insert_timezone");
				while((line = br.readLine()) != null)   {
					Db_stmt stmt = conn.Stmt_sql(line);
					boolean res = stmt.Exec_insert(); // ugh!
				}
				fr.close();    //closes the stream and release the resources
			}
			catch (Exception e) {
				int a = 1;
			}
			finally {
				conn.Txn_end();
			}
			// read timezone file (possibly timezone.h)
			// break into lines
			// add to table
		}
		initialised = true;
	}
	// need sqlite >= 3.25
	private static final String codes_sql = String_.Concat_lines_nl
	( "SELECT t.code, t.id, t.offset FROM timezone t"
	, "JOIN"
	, "( SELECT timezone.*"
	, ", ROW_NUMBER()OVER(PARTITION BY code ORDER BY ID) RN"
	, "  FROM timezone) z"
	, "ON z.id=t.id AND z.rn=1 "
	);
	public static void Setup_timezones(Btrie_slim_mgr trie ) {
		Init();
		Db_stmt stmt = conn.Stmt_sql(codes_sql);
		Db_rdr rdr = stmt.Exec_select__rls_auto();
		try {
			while (rdr.Move_next()) {
				byte[] code = rdr.Read_str("code").getBytes();
				int id = rdr.Read_int("id");
				int offset = rdr.Read_int("offset");
				trie.Add_obj(code, new Pxd_itm_tz_abbr(-1, code, id, offset));
			}
		} finally {rdr.Rls();}
	}
	private static final String sql = "SELECT offset FROM timezone WHERE id=?;";
	public static int Get_offset(int id) {
		Init();
		int offset = 0;
		Db_stmt stmt = conn.Stmt_sql(sql);
		stmt.Crt_int("id", id);
		Db_rdr rdr = stmt.Exec_select__rls_auto();
		try {
			while (rdr.Move_next()) {
				offset = rdr.Read_int("id");
				break;
			}
		} finally {rdr.Rls();}
		return offset;
	}
	private static final String tz_sql = "SELECT upper(code) as code FROM timezone WHERE tz_name=? and offset=?;";
	public static byte[] Get_code(String tz_name, int offset) {
		Init();
		byte[] code = null;
		Db_stmt stmt = conn.Stmt_sql(tz_sql);
		stmt.Crt_str("tz_name", tz_name);
		stmt.Crt_int("offset", offset);
		Db_rdr rdr = stmt.Exec_select__rls_auto();
		try {
			while (rdr.Move_next()) {
				code = rdr.Read_str("code").getBytes();
				break;
			}
		} finally {rdr.Rls();}
		return code;
	}
}
