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
package gplx;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
public class DateAdp implements CompareAble, Gfo_invk {
	public int compareTo(Object obj) {
		DateAdp comp = (DateAdp)obj;
		return under.compareTo(comp.under);
	}
	@Override public String toString() {return XtoStr_gplx_long();}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m)  {
		if (ctx.Match(k, Invk_XtoStr_fmt)) return XtoStr_fmt("yyyy-MM-dd HH:mm:ss");
		else if (ctx.Match(k, Invk_AddDays)) {
			int days = m.ReadInt("days");
			if (ctx.Deny()) return this;
			return this.Add_day(days);
		}
		else
			return Gfo_invk_.Rv_unhandled;
	}
	public static final String Invk_XtoStr_fmt = "XtoStr_fmt", Invk_AddDays = "Add_day";
	public int Segment(int segmentIdx) {
		switch (segmentIdx) {
			case DateAdp_.SegIdx_year:			return this.Year();
			case DateAdp_.SegIdx_month:			return this.Month();
			case DateAdp_.SegIdx_day:			return this.Day();
			case DateAdp_.SegIdx_hour:			return this.Hour();
			case DateAdp_.SegIdx_minute:		return this.Minute();
			case DateAdp_.SegIdx_second:		return this.Second();
			case DateAdp_.SegIdx_frac:			return this.Frac();
			case DateAdp_.SegIdx_dayOfWeek:		return this.DayOfWeek();
			case DateAdp_.SegIdx_weekOfYear:	return this.WeekOfYear();
			case DateAdp_.SegIdx_dayOfYear:		return this.DayOfYear();
			default: throw Err_.new_unhandled(segmentIdx);
		}
	}
	public int[] XtoSegAry() {
		int[] rv = new int[7];
		rv[DateAdp_.SegIdx_year]	= this.Year();
		rv[DateAdp_.SegIdx_month]	= this.Month();
		rv[DateAdp_.SegIdx_day]		= this.Day();
		rv[DateAdp_.SegIdx_hour]	= this.Hour();
		rv[DateAdp_.SegIdx_minute]	= this.Minute();
		rv[DateAdp_.SegIdx_second]	= this.Second();
		rv[DateAdp_.SegIdx_frac]	= this.Frac();
		return rv;
	}
	public String XtoStr_gplx()						{return XtoStr_fmt("yyyyMMdd_HHmmss.fff");}
	public String XtoStr_gplx_long()				{return XtoStr_fmt("yyyy-MM-dd HH:mm:ss.fff");}
	public String XtoStr_fmt_HHmmss()				{return XtoStr_fmt("HH:mm:ss");}
	public String XtoStr_fmt_HHmm()					{return XtoStr_fmt("HH:mm");}
	public String XtoStr_fmt_yyyy_MM_dd()			{return XtoStr_fmt("yyyy-MM-dd");}
	public String XtoStr_fmt_yyyyMMdd_HHmmss()		{return XtoStr_fmt("yyyyMMdd_HHmmss");}
	public String XtoStr_fmt_yyyyMMdd_HHmmss_fff()	{return XtoStr_fmt("yyyyMMdd_HHmmss.fff");}
	public String XtoStr_fmt_yyyyMMdd_HHmm()		{return XtoStr_fmt("yyyyMMdd_HHmm");}
	public String XtoStr_fmt_yyyy_MM_dd_HH_mm()		{return XtoStr_fmt("yyyy-MM-dd HH:mm");}
	public String XtoStr_fmt_yyyy_MM_dd_HH_mm_ss()	{return XtoStr_fmt("yyyy-MM-dd HH:mm:ss");}
	public String XtoStr_fmt_iso_8561()				{return XtoStr_fmt("yyyy-MM-dd HH:mm:ss");}
	public String XtoStr_fmt_iso_8561_w_tz()		{return XtoStr_fmt("yyyy-MM-dd'T'HH:mm:ss'Z'");}
	public static int Timezone_offset_test = Int_.Min_value;
	public ZonedDateTime UnderDateTime() 		{return under;} private ZonedDateTime under;
	public int Year() {return under.getYear();}
	public int Month() {return under.getMonthValue();}
	public int Day() {return under.getDayOfMonth();}
	public int Hour() {return under.getHour();}
	public int Minute() {return under.getMinute();}
	public int Second() {return under.getSecond();}
	public int DayOfWeek() {
		int day = under.getDayOfWeek().getValue(); // now returns 1-7 (Mon->Sun)
		if (day == 7) day = 0;
		return day;	// -1 : Base0; NOTE: dotnet/php is also Sunday=0
	}
	public int DayOfYear() {return under.getDayOfYear();}
	public int Timezone_offset() {
		return under.getOffset().getTotalSeconds();
	}
	public String Timezone_id() {
		ZoneId zoneid = under.getZone();
		return zoneid.getId();
	}
	public boolean Timezone_dst() {
		//java.util.TimeZone tz = under.getTimeZone();
		//java.util.Date date = under.getTime();
		//return tz.inDaylightTime(date);
		ZoneId z = under.getZone();
		ZoneRules zoneRules = z.getRules();
		return zoneRules.isDaylightSavings( under.toInstant() );
	}
	public DateAdp XtoZone(ZoneId zoneid) {
//		return new DateAdp(under.withZoneSameLocal(zoneid));
		return new DateAdp(under.toInstant().atZone(zoneid));
//		ZoneRules rules = zoneid.getRules();
//		LocalDateTime loc = under.toLocalDateTime();
//		ZoneOffset ofs = rules.getOffset(loc);
//		return new DateAdp(under.plusSeconds(ofs.getTotalSeconds()));
	}
	public DateAdp XtoUtc() {
//		return new DateAdp(under.withZoneSameLocal(ZoneId.of("UTC")));
		return new DateAdp(under.toInstant().atZone(ZoneId.of("UTC")));
	}
	//public DateAdp XtoLocal() {
		// for the moment same as above (does not work if machine local time is not GMT)
	//	return new DateAdp(under.withZoneSameLocal(ZoneId.of("UTC")));
	//}
	public long Timestamp_unix() {
		return under.toEpochSecond(); // should not be zone dependent
	}
	public int WeekOfYear() {return Math.floorDiv(under.getDayOfYear(), 7) + 1;} //.get(Calendar.WEEK_OF_YEAR);}
	public int Frac() {return under.getNano()/1000;} // MILLISECONDs
	public DateAdp Add_frac(int val) {return CloneAndAdd(under.plusNanos(val * 1000));}
	public DateAdp Add_second(int val) {return CloneAndAdd(under.plusSeconds(val));}
	public DateAdp Add_minute(int val) {return CloneAndAdd(under.plusMinutes(val));}
	public DateAdp Add_hour(int val) {return CloneAndAdd(under.plusHours(val));}
	public DateAdp Add_day(int val) {return CloneAndAdd(under.plusDays(val));}
	public DateAdp Add_month(int val) {return CloneAndAdd(under.plusMonths(val));}
	public DateAdp Add_year(int val) {return CloneAndAdd(under.plusYears(val));}
	DateAdp CloneAndAdd(ZonedDateTime zdt) {
		return new DateAdp(zdt);
	}
	public String XtoStr_fmt(String fmt)	{
		fmt = fmt.replace("f", "S"); // ????? is this needed??????????
		return under.format(DateTimeFormatter.ofPattern(fmt));
	}
	public String XtoStr_fmt_rfc1123() {
		return under.format(DateTimeFormatter.RFC_1123_DATE_TIME);
	}
	public String XtoStr_tz()	{
		//SimpleDateFormat sdf = new SimpleDateFormat("Z");
		//String time_zone = sdf.format(under.getTime());
		//return String_.Mid(time_zone, 0, 3) + ":" + String_.Mid(time_zone, 3, String_.Len(time_zone));
		return under.format(DateTimeFormatter.ofPattern("Z"));
	}
	public boolean Eq(DateAdp v)			{DateAdp comp = v; return under.toEpochSecond() == comp.under.toEpochSecond();}
	public int Diff_days(DateAdp prev) {
		long diff = this.under.toEpochSecond() - prev.under.toEpochSecond();
		return (int)(diff / (1000 * 60 * 60 * 24));
	}
	public Time_span Diff(DateAdp earlier) {
		long diff = this.under.toEpochSecond() - earlier.under.toEpochSecond();
		return Time_span_.fracs_(diff);
	}
	public DateAdp() {this.under = Instant.now().atZone(ZoneId.of("UTC"));}
	public DateAdp(ZoneId zoneid) {this.under = Instant.now().atZone(zoneid);}
        
	public DateAdp(ZonedDateTime under) {this.under = under;}
	public DateAdp(int year, int month, int day, int hour, int minute, int second, int frac) {
		MakeDate(year, month, day, hour, minute, second, frac, ZoneId.of("UTC"));
	}
	public DateAdp(int year, int month, int day, int hour, int minute, int second, int frac, ZoneId zoneid) {
		MakeDate(year, month, day, hour, minute, second, frac, zoneid);
	}
	private void MakeDate(int year, int month, int day, int hour, int minute, int second, int frac, ZoneId zoneid) {
		this.under = ZonedDateTime.of(year, month, day, hour, minute, second, frac, zoneid);
		//System.out.println("make " + under.getTime() + " " + under.getTimeZone());
		//under.setTimeZone(tz);
		//System.out.println(under.getTimeZone());
	}
	public void SetTimeZone(ZoneId zoneid) {
		under = under.withZoneSameLocal(zoneid);
	}
	public void SetTzOffset(int offset) {
		under = under.plusSeconds(offset*-1);
	}
	public static final int Month_base0adj = 1;
}
