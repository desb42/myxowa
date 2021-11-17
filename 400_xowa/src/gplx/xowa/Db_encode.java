/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2021 gnosygnu@gmail.com

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
import gplx.core.encoders.Hex_utl_;
public class Db_encode {
	public static byte[] Title(byte[] src) {
		if (src == null) return null;
		int len = src.length;
		int pos = 0;
		Db_encoder code = null;
		Bry_bfr bfr = null;
		int start = 0;
		while (pos < len) {
			byte b = src[pos++];
			if (b < 0) {
				if (bfr == null) {
					bfr = Bry_bfr_.New();
					code = new Db_encoder(pos);
				}
				bfr.Add_mid(src, start, pos-1);
				code.Convert(src, pos, b);
                                if (code.buffer[3] == 0) {
                                    int a=1;
                                }
				bfr.Add_mid(code.buffer, 0, code.size);
				pos = code.pos;
				start = pos;
			}
		}
		if (start > 0) {
			bfr.Add_mid(src, start, len);
			return bfr.To_bry();
		}
		else
			return src;
	}
}
class Db_encoder {
	int size;
	int pos;
	int len;
	byte[] buffer = new byte[9]; // &#xAAAAA;
	Db_encoder (int len) {
		buffer[0] = '&';
		buffer[1] = '#';
		buffer[2] = 'x';
		this.len = len;
	}
	public void Convert(byte[] src, int pos, byte b) {
		this.pos = pos;
		int val = Decode_to_int(src, b);
		if (val < 0x100) // &#xAAAA;
			size = 5;
		else if (val < 0x1000)
			size = 6;
		else if (val < 0x10000)
			size = 7;
		else if (val < 0x100000)
			size = 8;
		Hex_utl_.Write_lc(buffer, 3, size, val);
		buffer[size++] = ';';
	}
	private int Decode_to_int(byte[] ary, byte b0) {
		int val = 0;
		if ((b0 & 0xE0) == 0xC0) {
			val = ( b0 & 0x1f) << 6 | ( ary[pos] & 0x3f);
			pos += 1;
		}
		else if ((b0 & 0xF0) == 0xE0) {
			val = ( b0 & 0x0f) << 12 | ((ary[pos] & 0x3f) <<  6) | ( ary[pos + 1] & 0x3f);
			pos += 2;
		}
		else if ((b0 & 0xF8) == 0xF0) {
			val = ( b0 & 0x07) << 18 | ((ary[pos] & 0x3f) << 12) | ((ary[pos + 1] & 0x3f) << 6) | ( ary[pos + 2] & 0x3f);
			pos += 3;
		}
		else throw Err_.new_wo_type("invalid utf8 byte", "byte", b0);
		return val;
	}
}