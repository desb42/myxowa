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
package gplx.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.kwds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.tmpls.*;
public class Pfxtp_roman {
	public static void ToRoman(int num, Bry_bfr bfr) {
		ToRoman(num, bfr, false);
	}
	public static void ToRoman(int num, Bry_bfr bfr, boolean islower) {
		if (num > 3000 || num <= 0) {
			bfr.Add_int_variable(num);
			return;
		}
		int pow10 = 1000;
		for (int i = 3; i > -1; i--) {
			if (num >= pow10) {
				bfr.Add((islower ? NamesLc: NamesUc)[i][Math_.Trunc(num / pow10)]);
			}
			num %= pow10;
			pow10 /= 10;
		}
	}
	private static byte[][][] NamesUc = new byte[][][]
		{ Bry_dim2_new_("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
		, Bry_dim2_new_("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C")
		, Bry_dim2_new_("", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM", "M")
		, Bry_dim2_new_("", "M", "MM", "MMM")
		};
	private static byte[][][] NamesLc = new byte[][][]
		{ Bry_dim2_new_("", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x")
		, Bry_dim2_new_("", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc", "c")
		, Bry_dim2_new_("", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm", "m")
		, Bry_dim2_new_("", "m", "mm", "mmm")
		};
	private static byte[][] Bry_dim2_new_(String... names) {
		int len = names.length;
		byte[][] rv = new byte[len][];
		for (int i = 0; i < len; i++)
			rv[i] = Bry_.new_u8(names[i]);
		return rv;
	}
}
