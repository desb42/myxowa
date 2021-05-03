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
package gplx.xowa.apps.utls; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*;
public class Xoa_pagename_encoder {	// see:https://www.mediawiki.org/wiki/Manual:PAGENAMEE_encoding
	private final Bry_bfr bfr = Bry_bfr_.New();
	public byte[] Encode(byte[] src) {
		int src_len = src.length;
		int pos = 0;
		int sofar = 0;
		for (int i = 0; i < src_len; ++i) {
			byte b = src[i];
			byte[] repl = null;
			switch (b) {
				case Byte_ascii.Amp:   repl = Bry__amp; break;
				case Byte_ascii.Apos:  repl = Bry__apos; break;
				case Byte_ascii.Quote: repl = Bry__quot; break;
			}
			if (repl != null) {
				bfr.Add_mid(src, sofar, i);
				bfr.Add(repl);
				sofar = i + 1;
			}
		}
		if (sofar > 0) {
			bfr.Add_mid(src, sofar, src_len);
			return bfr.To_bry_and_clear();
		}
		else
			return src;
	}
	private static final byte[]
	  Bry__amp = Bry_.new_a7("&#38;")
	, Bry__apos = Bry_.new_a7("&#39;")
	, Bry__quot = Bry_.new_a7("&#34;")
	;
}
