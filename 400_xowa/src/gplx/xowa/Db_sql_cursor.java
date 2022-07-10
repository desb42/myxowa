package gplx.xowa;
import gplx.*;
public class Db_sql_cursor {
	private int conn_id;
	private Db_sql_main sql;
	public Db_sql_cursor(Db_sql_main sql, int conn_id) {
		this.sql = sql;
		this.conn_id = conn_id;
	}
	
	public Db_record Seek_rowid(String table_name, int key) {
		return sql.Seek_rowid(conn_id, table_name, key);
	}
	public int Seek_index(String index_table_name, Object... lst) {
		return sql.Seek_index(conn_id, index_table_name, lst);
	}
}
