/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2020 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.related; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.dbs.*;
import gplx.xowa.htmls.*;
import gplx.xowa.htmls.hxtns.pages.*; import gplx.xowa.htmls.hxtns.blobs.*; import gplx.xowa.htmls.hxtns.wikis.*;
public class Related_hxtn_page_wkr implements Hxtn_page_wkr {
	private final    Hxtn_blob_tbl page_text_tbl;
	public int Id() {return Hxtn_page_mgr.Id__related;}
	public String Key() {return KEY;}
	public Related_hxtn_page_wkr(Hxtn_blob_tbl page_text_tbl) {
		this.page_text_tbl = page_text_tbl;
	}
	public void Load_by_page(Xoh_page hpg, Xoa_ttl ttl, int page_id) {
		byte[] data = page_text_tbl.Select_text(Hxtn_blob_tbl.Blob_tid__wtxt, Id(), page_id);
		hpg.Props().Add(KEY, data);
	}
	public static final String KEY = "xowa.xtns.related";
}
