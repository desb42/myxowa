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
public class Db_quality_tots {
	private int[] qualitycount = new int[6]; // 5 quality levels plus unknown
	private int qualitytot;
	public Db_quality_tots() {
		this.qualitytot = 0;
	}
	public void Clear() {
		for (int i = 0; i < 6; i++)
			qualitycount[i] = 0;
		qualitytot = 0;
	}
	public void Increment(int slot) {
		if (slot >= 0)
			qualitycount[slot]++;
		else
			qualitycount[5]++;

		qualitytot++;
	}
	public int[] Qualitycounts() { return qualitycount; }
	public int Qualitycount() { return qualitytot; }
}
