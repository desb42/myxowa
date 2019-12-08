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
package gplx.xowa.langs.timeformats; import gplx.*; import gplx.xowa.*;
import gplx.xowa.xtns.pfuncs.times.*;
public class Xol_time_format_mgr implements Gfo_invk {
	private Db_time_format_group fmt_grp;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if	(ctx.Match(k, Invk_load_text))		Exec_load_text(m.ReadBry("v"));
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}
	private void Exec_load_text(byte[] raw) {
		byte[][] lines = Bry_split_.Split(raw, Byte_ascii.Nl);
		int lines_len = lines.length;
		int num_of_formats = lines_len/6;
		fmt_grp = new Db_time_format_group(num_of_formats);
		Db_time_formats[] times = fmt_grp.Times();
		byte[][] names = fmt_grp.Names();
		int group_count = 0;
		for (int i = 0; i < lines_len; i += 6) {
			names[group_count] = lines[i];
			times[group_count] = new Db_time_formats(format(lines[i+1]), 
						format(lines[i+2]), format(lines[i+3]), format(lines[i+4]), format(lines[i+5]));
			group_count++;
		}
	}
	private byte[] format(byte[] lne) {
		int len = lne.length;
		for (int i = 0; i < len; i++) {
			if (lne[i] == '|')
				return Bry_.Mid(lne, i+1, len);
		}
		return Bry_.Empty;
	}
	public static final String Invk_lang = "lang", Invk_load_text = "load_text";

	public byte[] Get_date_defaultfmt(Xowe_wiki wiki, DateAdp dte) {
		// check if any ititialisation
		if (fmt_grp == null) { // fake it
			fmt_grp = new Db_time_format_group(1);
			fmt_grp.Names()[0] = default_name;
			fmt_grp.Times()[0] = new Db_time_formats(default_time, 
						default_date, default_both, default_monthonly, default_pretty);
		}
		Pft_fmt_itm[] format = fmt_grp.Times()[0].Time_format(Db_time_formats.Tid__date);
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		wiki.Parser_mgr().Date_fmt_bldr().Format(tmp_bfr, wiki, wiki.Lang(), dte, format);
		return tmp_bfr.To_bry_and_clear();
	}
	public byte[] Get_time_defaultfmt(Xowe_wiki wiki, DateAdp dte) {
		Pft_fmt_itm[] format = fmt_grp.Times()[0].Time_format(Db_time_formats.Tid__time);
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		wiki.Parser_mgr().Date_fmt_bldr().Format(tmp_bfr, wiki, wiki.Lang(), dte, format);
		return tmp_bfr.To_bry_and_clear();
	}
	public Pft_fmt_itm[] Get_date_format(byte[] fmt_name) {
		int len = fmt_name.length;
		byte[][] names = fmt_grp.Names();
		int names_len= names.length;
		for (int i = 0; i < names_len; i++) {
			if (Bry_.Eq(names[i], fmt_name)) {
				return fmt_grp.Times()[i].Time_format(Db_time_formats.Tid__date);
			}
		}
		// not found (including "default") return default
		return fmt_grp.Times()[0].Time_format(Db_time_formats.Tid__date);
	}
	private static final byte[]
	  default_name = Bry_.new_a7("dmy")
	, default_time = Bry_.new_a7("H:i")
	, default_date = Bry_.new_a7("j F Y")
	, default_both = Bry_.new_a7("H:i, j F Y")
	, default_monthonly = Bry_.new_a7("F Y")
	, default_pretty = Bry_.new_a7("j F")
	;
}