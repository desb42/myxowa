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
package gplx.xowa.xtns.pfuncs.times; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
class Pft_fmt_itm_thai_ {
	public static byte[] tsToYear( int cName, DateAdp date ) {
		int gm = date.Month();
		int gd = date.Day();
                int gy = date.Year();
                int gy_offset = 0;
                int gy_gannen = 0;
                Bry_bfr gy_buf = Bry_bfr_.New();
		if ( cName == Tid__thai ) {
			// Thai solar dates
			// Add 543 years to the Gregorian calendar
			// Months and days are identical
			gy_offset = gy + 543;
			// fix for dates between 1912 and 1941
			// https://en.wikipedia.org/?oldid=836596673#New_year
			if ( gy >= 1912 && gy <= 1940 ) {
				if ( gm <= 3 ) {
					gy_offset--;
				}
				gm = ( gm - 3 ) % 12;
                                // somehow need tochange the underlying month??
			}
		} else if ( cName == Tid__minguo || cName == Tid__juche ) {
			// Minguo dates
			// Deduct 1911 years from the Gregorian calendar
			// Months and days are identical
			gy_offset = gy - 1911;
		} else if ( cName == Tid__tenno ) {
			// Nengo dates up to Meiji period
			// Deduct years from the Gregorian calendar
			// depending on the nengo periods
			// Months and days are identical
			if ( ( gy < 1912 )
				|| ( ( gy == 1912 ) && ( gm < 7 ) )
				|| ( ( gy == 1912 ) && ( gm == 7 ) && ( gd < 31 ) )
			) {
				// Meiji period
				gy_gannen = gy - 1868 + 1;
				gy_buf.Add(bry_Meiji);
				if ( gy_gannen == 1 ) {
					gy_buf.Add(bry_First);
				}
				else {
					gy_buf.Add_int_variable(gy_gannen);
				}
			} else if (
				( ( gy == 1912 ) && ( gm == 7 ) && ( gd == 31 ) ) ||
				( ( gy == 1912 ) && ( gm >= 8 ) ) ||
				( ( gy > 1912 ) && ( gy < 1926 ) ) ||
				( ( gy == 1926 ) && ( gm < 12 ) ) ||
				( ( gy == 1926 ) && ( gm == 12 ) && ( gd < 26 ) )
			) {
				// Taishō period
				gy_gannen = gy - 1912 + 1;
				gy_buf.Add(bry_Taisho);
				if ( gy_gannen == 1 ) {
					gy_buf.Add(bry_First);
				}
				else {
					gy_buf.Add_int_variable(gy_gannen);
				}
			} else if (
				( ( gy == 1926 ) && ( gm == 12 ) && ( gd >= 26 ) ) ||
				( ( gy > 1926 ) && ( gy < 1989 ) ) ||
				( ( gy == 1989 ) && ( gm == 1 ) && ( gd < 8 ) )
			) {
				// Shōwa period
				gy_gannen = gy - 1926 + 1;
				gy_buf.Add(bry_Showa);
				if ( gy_gannen == 1 ) {
					gy_buf.Add(bry_First);
				}
				else {
					gy_buf.Add_int_variable(gy_gannen);
				}
			} else if (
				( ( gy == 1989 ) && ( gm == 1 ) && ( gd >= 8 ) ) ||
				( ( gy > 1989 ) && ( gy < 2019 ) ) ||
				( ( gy == 2019 ) && ( gm < 5 ) )
			) {
				// Heisei period
				gy_gannen = gy - 1989 + 1;
				gy_buf.Add(bry_Heisei);
				if ( gy_gannen == 1 ) {
					gy_buf.Add(bry_First);
				}
				else {
					gy_buf.Add_int_variable(gy_gannen);
				}
			} else {
				// Reiwa period
				gy_gannen = gy - 2019 + 1;
				gy_buf.Add(bry_Reiwa);
				if ( gy_gannen == 1 ) {
					gy_buf.Add(bry_First);
				}
				else {
					gy_buf.Add_int_variable(gy_gannen);
				}
			}
		} else {
			gy_offset = gy;
		}
		if (gy_buf.Len() == 0){
			gy_buf.Add_int_variable(gy_offset);
		}
		return gy_buf.To_bry_and_clear(); //[ gy_offset, gm, gd ];
	}

	private static final byte[]
	  bry_First = Bry_.new_u8("元")
	, bry_Meiji = Bry_.new_u8("明治")
	, bry_Taisho = Bry_.new_u8("大正")
	, bry_Showa = Bry_.new_u8("昭和")
	, bry_Heisei = Bry_.new_u8("平成")
	, bry_Reiwa = Bry_.new_u8("令和")
	;
        public static int
          Tid__thai = 0
        , Tid__minguo = 1
        , Tid__juche = 2
        , Tid__tenno = 3
        ;
}
