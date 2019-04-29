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
package gplx.xowa.parsers.lnkis; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
public class Xop_lnki_align_v_ {
	public static final byte Null = 0, None = 1, Top = 2, Middle = 3, Bottom = 4, Super = 5, Sub = 6, TextTop = 7, TextBottom = 8, Baseline = 9;	// SERIALIZED
	public static final byte[][] Html_names = new byte[][]
	{ Object_.Bry__null
	, Bry_.new_a7("")
	, Bry_.new_a7("top")
	, Bry_.new_a7("middle")
	, Bry_.new_a7("bottom")
	, Bry_.new_a7("super")
	, Bry_.new_a7("sub")
	, Bry_.new_a7("text-top")
	, Bry_.new_a7("text-bottom")
	, Bry_.new_a7("baseline")
	};
	public static byte[] To_bry(int v) {return Html_names[v];}
}
