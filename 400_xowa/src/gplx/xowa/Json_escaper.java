/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa; import gplx.*;
public class Json_escaper {
	public static byte[] Escape(byte[] val) {
		Bry_bfr bfr = Bry_bfr_.New();
		int len = val.length;
		int pos = 0;
		int start = 0;
		while (pos < len) {
			byte b = val[pos++];
			switch (b) {
			case '"':
				bfr.Add_mid(val, start, pos-1);
				bfr.Add(Bry_.new_a7("\\u0022"));
				start = pos;
				break;
			case '\n':
				bfr.Add_mid(val, start, pos-1);
				bfr.Add(Bry_.new_a7("\\n"));
				start = pos;
				break;
			case '\t':
				bfr.Add_mid(val, start, pos-1);
				bfr.Add(Bry_.new_a7("\\t"));
				start = pos;
				break;
			case '&':
				if (val[pos+1] == 'n' && val[pos+2] == 'b' && val[pos+3] == 's' && val[pos+4] == 'p' && val[pos+5] == ';') {
					bfr.Add_mid(val, start, pos-1);
					bfr.Add(Bry_.new_a7("\\u00A0"));
					pos += 5;
					start = pos;
				}
				break;
			}
		}
		if (start > 0) {
			bfr.Add_mid(val, start, len);
			return bfr.To_bry();
		}
		return val;
	}
}