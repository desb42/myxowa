/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2022 https://github.com/desb42

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/

package gplx.xowa;
import gplx.*;
import gplx.core.intls.Gfo_case_mgr;
import gplx.xowa.langs.cases.Xol_case_itm;
import gplx.xowa.langs.cases.Xol_case_itm_;
import gplx.core.intls.*;
public class DB_case_mgr implements Gfo_case_mgr {
	public DB_case_mgr(byte tid) {this.tid = tid;}
	private byte tid;
	public byte Tid() {return tid;}
	public Gfo_case_itm Get_or_null(byte bgn_byte, byte[] src, int bgn, int end) {
		Object rv = DB_case_mgr_.Lower(src, bgn, end);
		return rv == null
			? (Gfo_case_itm)DB_case_mgr_.Upper(src, bgn, end)
			: (Gfo_case_itm)rv;
	}
	@Override public Gfo_case_itm Get_or_null(byte[] src, int bgn, int end) {
		Object rv = DB_case_mgr_.Lower(src, bgn, end);
		return rv == null
			? (Gfo_case_itm)DB_case_mgr_.Upper(src, bgn, end)
			: (Gfo_case_itm)rv;
	}
}