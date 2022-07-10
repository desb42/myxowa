package gplx.xowa;
import gplx.*;
import java.io.*;
public class Db_sql_mgr {
	private Hash_adp db_cache = Hash_adp_.New();
	
	public Db_sql_cursor Make_cursor(String root_dir, String filename) throws IOException {
		String fullname = root_dir + filename;
		Db_sql_main sql = null;
		synchronized (db_cache) {
			sql = (Db_sql_main)db_cache.Get_by(fullname);
			if (sql == null) {
				sql = new Db_sql_main(root_dir + filename);
				db_cache.Add(fullname, sql);
			}
		}
		int conn_id = sql.Get_connid();
		Db_sql_cursor conn = new Db_sql_cursor(sql, conn_id);
		return conn;
	}
}
