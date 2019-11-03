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
package gplx.xowa.wikis.data.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*; import gplx.xowa.wikis.data.*;
import gplx.dbs.*; import gplx.dbs.qrys.*;
import gplx.xowa.wikis.data.*;
public class Xowd_parent_tbl implements Db_tbl {
	public static final    String Fld_ttl = "ttl", Fld_parent = "parent";
	private final    Dbmeta_fld_list flds = new Dbmeta_fld_list();
	private final    String fld_ttl, fld_parent;
	private final    Db_conn conn; private final    Db_stmt_bldr stmt_bldr = new Db_stmt_bldr();
	public Xowd_parent_tbl(Db_conn conn) {
		this.conn = conn;
		this.tbl_name = TBL_NAME;
		fld_ttl		= flds.Add_str_pkey(Fld_ttl, 255);
		fld_parent	= flds.Add_str(Fld_parent, 255);
		stmt_bldr.Conn_(conn, tbl_name, flds, fld_ttl);
	}
	public String Tbl_name() {return tbl_name;} private final    String tbl_name; public static final String TBL_NAME = "parent";
	public void Create_tbl() {conn.Meta_tbl_create(Dbmeta_tbl_itm.New(tbl_name, flds));}
	public void Rls() {}

}
