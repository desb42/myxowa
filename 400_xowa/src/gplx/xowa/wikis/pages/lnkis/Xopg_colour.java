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
package gplx.xowa.wikis.pages.lnkis;
import gplx.xowa.wikis.data.tbls.*;
public class Xopg_colour {
	private String html_uid, colour_class;
        private Xowd_page_itm page_row;
	public Xopg_colour(Xowd_page_itm page_row, String html_uid) {
                this.page_row = page_row;
		this.html_uid = html_uid;
		this.colour_class = "";
	}
	public String Class() {return colour_class; }
	public void Class_(String colour_class) {this.colour_class = colour_class; }
        public Xowd_page_itm Page_row() {return page_row; }
        public String Html_uid() {return html_uid; }
}
