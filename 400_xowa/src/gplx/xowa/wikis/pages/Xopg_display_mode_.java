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
package gplx.xowa.wikis.pages; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*;
public class Xopg_display_mode_ {
	public static final byte
	  Tid__none = 0
	, Tid__wikitextver = 1
	, Tid__htmlver = 2;
	public static final    byte[]
	  Bry__wikitextver = Bry_.new_a7("wikitextver")
	, Bry__htmlver = Bry_.new_a7("htmlver");
	public static byte[] To_bry(byte tid) {
		switch (tid) {
			case Tid__none: return Bry_.Empty;
			case Tid__wikitextver: return Bry__wikitextver;
			case Tid__htmlver: return Bry__htmlver;
			default: throw Err_.new_unhandled_default(tid);
		}
	}
}
