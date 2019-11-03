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
package gplx.xowa;
import gplx.xowa.xtns.pfuncs.times.*;
public class Db_time_formats {
	private Pft_fmt_itm[][] formats;
	public Pft_fmt_itm[] Time_format(int i) { return formats[i]; }

	public Db_time_formats(byte[] time, byte[] date, byte[] both, byte[] monthonly, byte[] pretty) {
            formats = new Pft_fmt_itm[5][];
		formats[0] = Pft_fmt_itm_.Parse(time);
		formats[1] = Pft_fmt_itm_.Parse(date);
		formats[2] = Pft_fmt_itm_.Parse(both);
		formats[3] = Pft_fmt_itm_.Parse(monthonly);
		formats[4] = Pft_fmt_itm_.Parse(pretty);
	}
        public static final int
          Tid__time = 0
        , Tid__date = 1
        , Tid__both = 2
        , Tid__monthonly = 3
        , Tid__pretty = 4
        ;
}
