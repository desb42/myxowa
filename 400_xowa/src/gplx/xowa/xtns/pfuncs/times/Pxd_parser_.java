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
import gplx.core.btries.*;
import gplx.xowa.parsers.*;
public class Pxd_parser_ {
	public static Btrie_slim_mgr Trie(Xop_ctx ctx) {
		if (trie == null) {
			trie = Btrie_slim_mgr.ci_a7();	// NOTE:ci.ascii:MW_const.en
			Init(ctx);
		}
		return trie;
	}
	private static Btrie_slim_mgr trie;
	private static final       String[] Names_month_full		= {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
	private static final       String[] Names_month_abrv		= {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
	private static final       String[] Names_month_roman		= {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII"};
	private static final       String[] Names_day_suffix		= {"st", "nd", "rd", "th"};
	private static final       String[] Names_day_full			= {"sunday", "monday", "tuesday", "wednesday" , "thursday", "friday", "saturday"};
	private static final       String[] Names_day_abrv			= {"sun", "mon", "tue", "wed" , "thu", "fri", "sat"};
	//TODO_OLD:
	private static final       String[] Names_day_text		= {"weekday", "weekdays"};
	private static final       String[] Names_ordinal_num		= {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"};
	
	private static void Init_unit(int seg_idx, String... name_ary) {Init_unit(seg_idx, 1, name_ary);}
	private static void Init_unit(int seg_idx, int seg_val, String... name_ary) {
		int name_ary_len = name_ary.length;
		for (int i = 0; i < name_ary_len; i++) {
			byte[] name_bry = Bry_.new_u8(name_ary[i]);
			trie.Add_obj(name_bry, new Pxd_itm_unit(-1, name_bry, seg_idx, seg_val));
		}
	}
	public static final    byte[] 
	  Unit_name_month		= Bry_.new_a7("month")
	, Unit_name_day			= Bry_.new_a7("day")
	, Unit_name_hour		= Bry_.new_a7("hour")
	;
	private static void Init(Xop_ctx ctx) {
		Init_reg_months(Names_month_full);
		Init_reg_months(Names_month_abrv);
		Init_reg_months(Names_month_roman);
		Init_reg_month("sept", 9);
		Init_reg_days_of_week(Names_day_full);
		Init_reg_days_of_week(Names_day_abrv);
		//Init_unit(DateAdp_.SegIdx_us	, "\xc2\xb5s", "usec", "usecs", "\xc2\xb5sec", "\xc2\xb5secs", "microsecond", "microseconds");
		//Init_unit(DateAdp_.SegIdx_us, 1000	, "ms", "msecs", "millisecond", "milliseconds");
		Init_unit(DateAdp_.SegIdx_second	, "sec", "secs", "second", "seconds");
		Init_unit(DateAdp_.SegIdx_minute	, "min", "mins", "minute", "minutes");
		Init_unit(DateAdp_.SegIdx_hour  	, "hour", "hours");
		Init_unit(DateAdp_.SegIdx_day   	, "day", "days");
		Init_unit(DateAdp_.SegIdx_day, 14	, "fortnight", "fortnights", "forthnight", "forthnights");
		Init_unit(DateAdp_.SegIdx_month 	, "month", "months");
		Init_unit(DateAdp_.SegIdx_year  	, "year", "years");
		Init_unit(DateAdp_.SegIdx_day,  7	, "week", "weeks");
		Init_reg_tz_abbr(ctx);
		trie.Add_obj(Pxd_itm_ago.Name_ago, new Pxd_itm_ago(-1, -1));
		Init_suffix(Names_day_suffix);
		Init_relative();
		trie.Add_obj(Pxd_itm_unixtime.Name_const, new Pxd_itm_unixtime(-1, -1));
		trie.Add_obj(Pxd_itm_iso8601_t.Name_const, new Pxd_itm_iso8601_t(-1, -1));
/**/		Init_meridian(Bool_.N, "am", "a.m", "am.", "a.m.");
/**/		Init_meridian(Bool_.Y, "pm", "p.m", "pm.", "p.m.");
/**/		Init_Weekofyear();
		Init_reg_ordinal(Names_ordinal_num);
		Init_reg_weekdays(Names_day_text);
}
	private static void Init_reg_months(String[] names) {
		for (int i = 0; i < names.length; i++)
			Init_reg_month(names[i], i + Int_.Base1);	// NOTE: Months are Base1: 1-12
	}
	private static void Init_reg_month(String name_str, int seg_val) {
		byte[] name_ary = Bry_.new_u8(name_str);
		trie.Add_obj(name_ary, new Pxd_itm_month_name(-1, name_ary, DateAdp_.SegIdx_month, seg_val));
	}
	private static void Init_reg_days_of_week(String[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			byte[] itm_bry = Bry_.new_u8(ary[i]);
			trie.Add_obj(itm_bry, new Pxd_itm_dow_name(-1, itm_bry, i));	// NOTE: days are base0; 0-6
		}
	}
	private static void Init_reg_ordinal(String[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			byte[] itm_bry = Bry_.new_u8(ary[i]);
			trie.Add_obj(itm_bry, new Pxd_itm_ordinal(-1, itm_bry, i + 1));
		}
	}
	private static void Init_reg_weekdays(String[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			byte[] itm_bry = Bry_.new_u8(ary[i]);
			trie.Add_obj(itm_bry, new Pxd_itm_weekdays(-1, itm_bry));
		}
	}
	private static void Init_reg_tz_abbr(Xop_ctx ctx) {
		if (ctx == null) return; // for testing
		Db_timezone.Setup_timezones(trie);
	}
	private static void Init_suffix(String[] suffix_ary) {
		int len = suffix_ary.length;
		for (int i = 0; i < len; i++) {
			String suffix = suffix_ary[i];
			trie.Add_obj(suffix, Pxd_itm_day_suffix.Instance);
		}
	}
	private static void Init_relative() {
		trie.Add_obj("today", Pxd_itm_day_relative.Today);
		trie.Add_obj("tomorrow", Pxd_itm_day_relative.Tomorrow);
		trie.Add_obj("yesterday", Pxd_itm_day_relative.Yesterday);
		trie.Add_obj("now", Pxd_itm_time_relative.Now);
		trie.Add_obj("next", Pxd_itm_unit_relative.Next);
		trie.Add_obj("last", Pxd_itm_unit_relative.Prev);
		trie.Add_obj("previous", Pxd_itm_unit_relative.Prev);
		trie.Add_obj("this", Pxd_itm_unit_relative.This);
	}
	private static void Init_meridian(boolean is_pm, String... ary) {
		Pxd_itm_meridian meridian = new Pxd_itm_meridian(-1, is_pm);
		for (String itm : ary)
			trie.Add_obj(itm, meridian);
	}
	private static void Init_Weekofyear() {
		for (int i = 1; i < 54; i++) {
			String itm = "W" + Integer.toString(i / 10) + Integer.toString(i % 10);
			Pxd_itm_weekofyear weekofyear = new Pxd_itm_weekofyear(-1, i);
			trie.Add_obj(itm, weekofyear);
		}
	}
}
