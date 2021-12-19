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
package gplx.xowa.xtns.wbases.claims.itms.times; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*; import gplx.xowa.xtns.wbases.claims.*; import gplx.xowa.xtns.wbases.claims.itms.*;
import gplx.core.brys.fmtrs.*;
import gplx.xowa.xtns.wbases.hwtrs.*;
public class Wbase_date_ {
	public static Wbase_date Parse(byte[] date, int precision, int before, int after, boolean calendar_is_julian) {// EX:+00000002001-02-03T04:05:06Z
		int year_sign = 1;
		switch (date[0]) {
			case Byte_ascii.Plus:	break;
			case Byte_ascii.Dash:	year_sign = -1; break;
			default:				throw Err_.new_unhandled(date[0]);
		}
		int year_end = Bry_find_.Find_fwd(date, Byte_ascii.Dash, 1);
		long year		= Long_.parse_or(String_.new_a7(date, 1, year_end), -1); if (year == -1) throw Err_.new_wo_type("parse failed", "raw", String_.new_a7(date));
		int month		= Bry_.To_int_or(date, year_end +  1, year_end +  3, -1);
		int day			= Bry_.To_int_or(date, year_end +  4, year_end +  6, -1);
		int hour		= Bry_.To_int_or(date, year_end +  7, year_end +  9, -1);
		int minute		= Bry_.To_int_or(date, year_end + 10, year_end + 12, -1);
		int second		= Bry_.To_int_or(date, year_end + 13, year_end + 15, -1);
		return new Wbase_date(year * year_sign, month, day, hour, minute, second, precision, before, after, calendar_is_julian);
	}
	public final static int
	  PRECISION_YEAR1G = 0
	, PRECISION_YEAR100M = 1
	, PRECISION_YEAR10M = 2
	, PRECISION_YEAR1M = 3
	, PRECISION_YEAR100K = 4
	, PRECISION_YEAR10K = 5
	, PRECISION_YEAR1K = 6
	, PRECISION_YEAR100 = 7
	, PRECISION_YEAR10 = 8
	, PRECISION_YEAR = 9
	, PRECISION_MONTH = 10
	, PRECISION_DAY = 11
	, PRECISION_HOUR = 12
	, PRECISION_MINUTE = 13
	, PRECISION_SECOND = 14
	;
	public final static int
                  CALENDAR_JULIAN = 0
                , CALENDAR_GREGORIAN = 1
                ;
	public static Wbase_date To_julian(Wbase_date date) {
		long a = (long)Math_.Floor((14 - date.Month() / 12));
		long y = date.Year() + 4800 - a;
		long m = date.Month() + 12 * a - 3;
		long julian = date.Day() + (long)Math_.Floor((153 * m + 2) / 5) + 365 * y + (long)Math_.Floor(y / 4) - (long)Math_.Floor(y / 100) + (long)Math_.Floor(y / 400) - 32045;
		long c = julian + 32082;
		long d = (long)Math_.Floor((4 * c + 3) / 1461);
		long e = c - (long)Math_.Floor((1461 * d) / 4);
		long n = (long)Math_.Floor((5 * e + 2) / 153);
		long new_y = d - 4800 + (long)Math_.Floor(n / 10);
		int new_m = (int)(n + 3 - 12 * (long)Math_.Floor(n / 10));
		int new_d = (int)(e - (long)Math_.Floor((153 * n + 2) / 5) + 1);
		return new Wbase_date(new_y, new_m, new_d, date.Hour(), date.Minute(), date.Second(), date.Precision(), date.Before(), date.After(), date.Calendar_is_julian());
	}
	public static void To_bfr(Bry_bfr bfr, Bry_fmtr tmp_fmtr, Bry_bfr tmp_bfr, Wdata_hwtr_msgs msgs, Wbase_date date) {
		// TOMBSTONE: use "actual" date; do not do conversion to julian; DATE:2016-11-10
		// boolean calendar_is_julian = date.Calendar_is_julian();
		// if (calendar_is_julian) date = To_julian(date);
		//long year = date.Year();
		int months_bgn = msgs.Month_bgn_idx();
		byte[][] months = msgs.Ary();
		int precision = date.Precision();
		byte[] year_bry = get_localised_year(tmp_fmtr, tmp_bfr, msgs, date, precision);
		byte[] time_spr = msgs.Sym_time_spr();
		switch (precision) {
			case Wbase_date.Fmt_ym:					// EX: "Feb 2001"
				bfr.Add(months[months_bgn + date.Month() - List_adp_.Base1]);
				bfr.Add_byte_space();
				bfr.Add(year_bry);
				break;
			case Wbase_date.Fmt_ymd: 				// EX: "3 Feb 2001"
				bfr.Add_int_variable(date.Day());
				bfr.Add_byte_space();
				bfr.Add(months[months_bgn + date.Month() - List_adp_.Base1]);
				bfr.Add_byte_space();
				bfr.Add(year_bry);
				break;
			case Wbase_date.Fmt_ymdh:				// EX: "4:00 3 Feb 2011"
				bfr.Add_int_variable(date.Hour());
				bfr.Add(time_spr);
				bfr.Add_int_fixed(0, 2);
				bfr.Add_byte_space();
				bfr.Add_int_variable(date.Day());
				bfr.Add_byte_space();
				bfr.Add(months[months_bgn + date.Month() - List_adp_.Base1]);
				bfr.Add_byte_space();
				bfr.Add(year_bry);
				break;
			case Wbase_date.Fmt_ymdhn:				// EX: "4:05 3 Feb 2011"
				bfr.Add_int_variable(date.Hour());
				bfr.Add(time_spr);
				bfr.Add_int_fixed(date.Minute(), 2);
				bfr.Add_byte_space();
				bfr.Add_int_variable(date.Day());
				bfr.Add_byte_space();
				bfr.Add(months[months_bgn + date.Month() - List_adp_.Base1]);
				bfr.Add_byte_space();
				bfr.Add(year_bry);
				break;
			default: 
				if (precision <= 9)					// y, round to (9 - prec)
					bfr.Add(year_bry);
				else {								// EX: "4:05:06 3 Feb 2011"
					bfr.Add_int_variable(date.Hour());
					bfr.Add(time_spr);
					bfr.Add_int_fixed(date.Minute(), 2);
					bfr.Add(time_spr);
					bfr.Add_int_fixed(date.Second(), 2);
					bfr.Add_byte_space();
					bfr.Add_int_variable(date.Day());
					bfr.Add_byte_space();
					bfr.Add(months[months_bgn + date.Month() - List_adp_.Base1]);
					bfr.Add_byte_space();
					bfr.Add(year_bry);
				}
				break;
		}
		if ( calendarNameNeeded( date ) ) {
			bfr.Add(Bry_.new_a7("<sup class=\"wb-calendar-name\">"));
			bfr.Add(formatCalendarName( date.Calendar_is_julian() ));
			bfr.Add(Bry_.new_a7("</sup>"));
		}
		// TOMBSTONE: use "actual" date; do not do conversion to julian; DATE:2016-11-10
		// if (calendar_is_julian)
		// 	bfr.Add(msgs.Time_julian());
		Xto_str_beforeafter(bfr, tmp_fmtr, tmp_bfr, msgs, date);
	}
	private static byte[] formatCalendarName(boolean isJulian) {
		if (isJulian)
			return Bry_.new_a7("Julian");
		else
			return Bry_.new_a7("Gregorian");
	}
	private static void Xto_str_beforeafter(Bry_bfr bfr, Bry_fmtr tmp_fmtr, Bry_bfr tmp_bfr, Wdata_hwtr_msgs msgs, Wbase_date date) {
		byte[] bry = null;
		int before = date.Before();
		int after = date.After();
		if (before == 0) {
			if (after != 0)
				bry = tmp_bfr.Add(msgs.Sym_plus()).Add_int_variable(after).To_bry_and_clear();
		}
		else {
			if		(after == 0)
				bry = tmp_bfr.Add(msgs.Sym_minus()).Add_int_variable(before).To_bry_and_clear();
			else if (before == after)
				bry = tmp_bfr.Add(msgs.Sym_plusminus()).Add_int_variable(before).To_bry_and_clear();
			else
				bry = tmp_bfr.Add(msgs.Sym_minus()).Add_int_variable(before).Add(msgs.Sym_list_comma()).Add(msgs.Sym_plus()).Add_int_variable(after).To_bry_and_clear();
		}
		if (bry != null) {
			bry = tmp_fmtr.Fmt_(msgs.Sym_fmt_parentheses()).Bld_bry_many(tmp_bfr, bry);
			bfr.Add_byte_space().Add(bry);
		}
	}
	public final static int
	  FUNC_CEIL = 0
	, FUNC_FLOOR = 1
	, FUNC_ROUND = 2
	;
	public final static int
	  MSG_GANNUM = 0
	, MSG_GANNUM_BCE = 1
	, MSG_MANNUM = 2
	, MSG_MANNUM_BCE = 3
	, MSG_ANNUM = 4
	, MSG_ANNUM_BCE = 5
	, MSG_MILLENNIUM = 6
	, MSG_MILLENNIUM_BCE = 7
	, MSG_CENTURY = 8
	, MSG_CENTURY_BCE = 9
	, MSG_10ANNUM = 10
	, MSG_10ANNUM_BCE = 11
	, MSG_WIKIBASE_TIME_PRECISION_CE = 12
	, MSG_WIKIBASE_TIME_PRECISION_BCE = 13
	, MSG_ELSE = 14
	;

	private static void Xto_str_fmt_y(Bry_bfr bfr, Bry_fmtr tmp_fmtr, Bry_bfr tmp_bfr, Wdata_hwtr_msgs msgs, Wbase_date date, int precision) {
		byte[] year_bry =	get_localised_year(tmp_fmtr, tmp_bfr, msgs, date, precision);
		bfr.Add(year_bry);
	}
	private static byte[] get_localised_year(Bry_fmtr tmp_fmtr, Bry_bfr tmp_bfr, Wdata_hwtr_msgs msgs, Wbase_date date, int precision) {
		long year = date.Year();
		boolean isBCE = false;
		if (year < 0) {
			isBCE = true;
			year *= -1;
		}
		double shift = 1;
		double unshift = 1;
		int func = FUNC_ROUND;
		int msg = MSG_ELSE;

		switch ( precision ) {
			case PRECISION_YEAR1G:
				msg = MSG_GANNUM;
				shift = 1e+9;
				break;
			case PRECISION_YEAR100M:
				msg = MSG_MANNUM;
				shift = 1e+8;
				unshift = 1e+2;
				break;
			case PRECISION_YEAR10M:
				msg = MSG_MANNUM;
				shift = 1e+7;
				unshift = 1e+1;
				break;
			case PRECISION_YEAR1M:
				msg = MSG_MANNUM;
				shift = 1e+6;
				break;
			case PRECISION_YEAR100K:
				msg = MSG_ANNUM;
				shift = 1e+5;
				unshift = 1e+5;
				break;
			case PRECISION_YEAR10K:
				msg = MSG_ANNUM;
				shift = 1e+4;
				unshift = 1e+4;
				break;
			case PRECISION_YEAR1K:
				msg = MSG_MILLENNIUM;
				func = FUNC_CEIL;
				shift = 1e+3;
				break;
			case PRECISION_YEAR100:
				msg = MSG_CENTURY;
				func = FUNC_CEIL;
				shift = 1e+2;
				break;
			case PRECISION_YEAR10:
				msg = MSG_10ANNUM;
				func = FUNC_FLOOR;
				shift = 1e+1;
				unshift = 1e+1;
				break;
		}

		long shifted = shiftNumber( year, func, shift, unshift );
		if ( shifted == 0
			&& ( precision < PRECISION_YEAR
				|| ( isBCE && precision == PRECISION_YEAR )
			)
		) {
			// Year too small for precision, fall back to year.
			msg = MSG_ELSE;
		} else {
			year = shifted;
		}

		//$year = str_pad( ltrim( $year, '0' ), 1, '0', STR_PAD_LEFT );
		// TODO: The year should be localized via Language::formatNum() at this point, but currently
		// can't because not all relevant time parsers unlocalize numbers.

		if ( msg == MSG_ELSE ) {
			if ( isBCE )
				msg = MSG_WIKIBASE_TIME_PRECISION_BCE;
			else if ( year < 100 )
				msg = MSG_WIKIBASE_TIME_PRECISION_CE;
		}
		else if (isBCE)
			msg++;

		byte[] year_fmt = msgs.Ary()[msgs.Time_year_idx() + msg];
		return tmp_fmtr.Fmt_(year_fmt).Bld_bry_many(tmp_bfr, year);
	}

	private static long shiftNumber( long year, int function, double shift, double unshift ) {
		if ( shift == 1 && unshift == 1 ) {
			return year;
		}

		double number = year;
		double shifted;
		switch ( function ) {
			case FUNC_CEIL:
				shifted = Math.ceil( number / shift ) * unshift;
				break;
			case FUNC_FLOOR:
				shifted = Math.floor( number / shift ) * unshift;
				break;
			default:
				shifted = Math.round( number / shift ) * unshift;
		}

		return (long)shifted;
	}
	private static boolean calendarNameNeeded( Wbase_date value ) {
		// Do not care about possibly wrong calendar models with precision 10 years and more.
		if ( value.Precision() <= PRECISION_YEAR10 ) {
			return false;
		}

		// Loose check if the timestamp string is ISO-ish and starts with a year.
//		if ( !preg_match( '/^[-+]?\d+\b/', $value->getTime(), $matches ) ) {
//			return true;
//		}

		// NOTE: PHP limits overly large values to PHP_INT_MAX. No overflow or wrap-around occurs.
		long year = value.Year();
		int guessedCalendar = getDefaultCalendar( year );

		// Always show the calendar if it's different from the "guessed" default.
		if ( value.Calendar_is_julian() ^ (guessedCalendar == CALENDAR_JULIAN) ) {
			return true;
		}

		// If precision is year or less precise, don't show the calendar.
		if ( value.Precision() <= PRECISION_YEAR ) {
			return false;
		}

		// If the date is inside the "critical" range where Julian and Gregorian were used
		// in parallel, always show the calendar. Gregorian was made "official" in October 1582 but
		// may already be used earlier. Julian continued to be official until the 1920s in Russia
		// and Greece, see https://en.wikipedia.org/wiki/Julian_calendar.
		if ( year > 1580 && year < 1930 ) {
			return true;
		}

		// Otherwise, the calendar is "unsurprising", so don't show it.
		return false;
	}
	private static int getDefaultCalendar( long year ) {
		// The Gregorian calendar was introduced in October 1582,
		// so we'll default to Julian for all years before 1583.
		return year <= 1582 ? CALENDAR_JULIAN : CALENDAR_GREGORIAN;
	}

/*		int year_pow = 9 - precision;
		byte[] year_fmt = msgs.Ary()[msgs.Time_year_idx() + year_pow];
		long year = date.Year();
		byte[] repl_fmt = null;
		if (year <= 0) {					// negative
			if (year_pow < 4)				// negative years < 999 get "BC"
				repl_fmt = msgs.Time_relative_bc();
			else 							// negative years > 999 get "ago"
				repl_fmt = msgs.Time_relative_ago();
		}
		else {
			if (year_pow > 4)				// positive years > 999 get "in time"
				repl_fmt = msgs.Time_relative_in();
		}
		if (repl_fmt != null)
			year_fmt = tmp_fmtr.Fmt_(repl_fmt).Bld_bry_many(tmp_bfr, year_fmt);
		if (year <= 0)
			year *= -1;						// convert negative to positive; note that negative year will be reported with "BC" / "ago"
		switch (year_pow) {
			case 0:		break; // noop
			default:
				year = (int)(year / Math_.Pow(10, year_pow));
				break;
		}
		byte[] year_bry = tmp_fmtr.Fmt_(year_fmt).Bld_bry_many(tmp_bfr, year);
		bfr.Add(year_bry);
	}
*/
}
