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
package gplx.xowa.parsers.lists; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
public class Xop_list_tkn_new extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_list_new;}
	public Xop_list_tkn_new Prev_list() {return prev_list;} private Xop_list_tkn_new prev_list;
	public Xop_list_tkn_new(int bgn, int end, Xop_list_tkn_new prev_list) {this.Tkn_ini_pos(false, bgn, end); this.prev_list = prev_list;}

	public static final    Xop_list_tkn_new Null = new Xop_list_tkn_new(); Xop_list_tkn_new() {}
}
