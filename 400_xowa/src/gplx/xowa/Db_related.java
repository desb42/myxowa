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
public class Db_related {
	private List_adp related = List_adp_.New();
	//public Db_related Related() { return related; }
	public void Clear() { related.Clear(); }
	public void Add(Object obj) { related.Add(obj); }
	public int Count() { return related.Count(); }
	public List_adp List() { return related; }
	public byte[] Serialise() {
		int len = related.Count();
		if (len == 0) return null;

		Bry_bfr tmp_bfr = Bry_bfr_.New();
		for (int i = 0; i < len; i++) {
			if (i > 0)
				tmp_bfr.Add_byte(Byte_ascii.Pipe);
			tmp_bfr.Add((byte[])related.Get_at(i));
		}
		return tmp_bfr.To_bry();
	}
	public void Deserialise(byte[] related_bry) {
		Clear();
		if (related_bry == null) return;
		int len = related_bry.length;
		int i = 0;
		int bgn = 0;
		while (i < len) {
			byte b = related_bry[i++];
			if (b == Byte_ascii.Pipe) {
				related.Add(Bry_.Mid(related_bry, bgn, i - 1));
				bgn = i;
			}
		}
		related.Add(Bry_.Mid(related_bry, bgn, len));
	}
}
