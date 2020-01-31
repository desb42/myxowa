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
import gplx.core.brys.*;
public class Pxd_itm_weekofyear extends Pxd_itm_base implements Pxd_itm_prototype {
	private final int weekofyear;
	private int dayofyear;
	public Pxd_itm_weekofyear(int ary_idx, int weekofyear) {this.Ctor(ary_idx); this.weekofyear = weekofyear;}
	@Override public byte Tkn_tid() {return Pxd_itm_.Tid_weekofyear;} 
	@Override public int Eval_idx() {return 21;}
	@Override public boolean Time_ini(Pxd_date_bldr bldr) {
		DateAdp dte = DateAdp_.DateByDayofYear(bldr.Seg_get(DateAdp_.SegIdx_year), dayofyear);
		bldr.Seg_set(DateAdp_.SegIdx_year, dte.Year());
		bldr.Seg_set(DateAdp_.SegIdx_month, dte.Month());
		bldr.Seg_set(DateAdp_.SegIdx_day, dte.Day());
		return true;
	}
	public Pxd_itm MakeNew(int ary_idx) {return new Pxd_itm_weekofyear(ary_idx, weekofyear);}
	@Override public boolean Eval(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		int data_ary_len = state.Data_ary_len();
		Pxd_itm_int itm_0;
		Pxd_itm_int itm_1;
		int id = 1;
		switch (data_ary_len) {
			case 0:
				return false;
			case 1:
				itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
				if (itm_0 == null) {return false;} // trie: fail
				break;
			default: 
				itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
				itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
				if (itm_0 == null || itm_1 == null) {return false;} // trie: fail
				if (itm_0.Digits() != 4) {return false;}
					if (itm_1.Seg_idx() == Pxd_itm_base.Seg_idx_skip) {
						// not the one for us
						itm_1 = null;
					}
					else {
						id = itm_1.Val();
						itm_1.Seg_idx_(Pxd_itm_base.Seg_idx_skip);
					}
				break;
		}
		/* Figure out the dayofweek for y-1-1 */
		DateAdp yearstart = DateAdp_.FirstDayofYear(itm_0.Val()); // should timezone be involved?
		int dow = yearstart.DayOfWeek();
		//dow = timelib_day_of_week(iy, 1, 1);
		/* then use that to figure out the offset for day 1 of week 1 */
		int day = 0 - (dow > 4 ? dow - 7 : dow);
	
		/* Add weeks and days */
		dayofyear = day + ((weekofyear - 1) * 7) + id + 1; //??
		return true;
	}
}
