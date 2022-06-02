/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.wbases.core;
import gplx.*;
public class Wdata_langtext_itm extends Wdata_list_itm implements Wdata_lang_sortable {
	public Wdata_langtext_itm(byte[] lang, byte[] text) {
            super(lang);
            this.text = text;
        }  
	public byte[] Lang() {return key;}
	public byte[] Text() {return text;} private final    byte[] text;
	public byte[] Lang_code() {return key;}
	public int Lang_sort() {return lang_sort;} public void Lang_sort_(int v) {lang_sort = v;} private int lang_sort = Wdata_lang_sorter.Sort_null;
	@Override public String toString() {// TEST:
		return String_.Concat_with_str("|", String_.new_u8(key), String_.new_u8(text));
	}
}
