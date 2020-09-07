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
package gplx.core.encoders; import gplx.*;
public class Urlencoders {	// NOTE: from mediawiki\includes\parser\CoreParserFunctions.php
	public static byte[] wfUrlencode_with_space(byte[] src) { return encode(src, Byte_ascii.Underline, false, true); }
	public static byte[] wfUrlencode(byte[] src) { return encode(src, (byte)0, false, true); }
	public static byte[] Raw_url_encode(byte[] src) { return encode(src, (byte)0, true, false); }
	public static byte[] Url_encode(byte[] src) { return encode(src, Byte_ascii.Plus, false, false); }

	// space can be 0, '_', '+'
	// wfUrlencode - encode(src, null, false, true)
	// wfUrlencode_with_space - encode(src, '_', false, true)
	// rawurlencode - encode(src, null, true, false)
	// urlencode - encode(src, '+', false, false)
	public static byte[] encode(byte[] src, byte space, boolean raw, boolean wfenc) {
		byte b;
		Bry_bfr bfr = null;
		int len = src.length;
		int pos = 0;
		boolean dirty = false;

		while (pos < len) {
			b = src[pos++];
			if (b == ' ' && space != (byte)0) {
				// if clean, add everything before cur_pos to bfr
				if (!dirty) {
					bfr = Bry_bfr_.New();
					bfr.Add_mid(src, 0, pos-1);
					dirty = true;
				}
				bfr.Add_byte(space);
			}
			else if ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') || (b >= '0' && b <= '9') || b == '_' || b == '-' || b == '.') {
				if (dirty)
					bfr.Add_byte(b);
			}
			else if (!raw && b == '~') {
				if (dirty)
					bfr.Add_byte(b);
			}
			else if (wfenc && (b == ';' || b == '@' || b == '$' || b == '!' || b == '*' || b == '(' || b == ')' || b == ',' || b == '/' || b== '~' || b == ':')) {
				if (dirty)
					bfr.Add_byte(b);
			}
			else {
				// if clean, add everything before cur_pos to bfr
				if (!dirty) {
					bfr = Bry_bfr_.New();
					bfr.Add_mid(src, 0, pos-1);
					dirty = true;
				}
				bfr.Add_byte(Byte_ascii.Percent);
				bfr.Add_byte(hexchars[(b & 0xff) >> 4]);
				bfr.Add_byte(hexchars[b & 15]);
			}
		}
		if (dirty)
			return bfr.To_bry();
		else
			return src;
	}
	private static byte[] hexchars = Bry_.new_a7("0123456789ABCDEF"); 
}
