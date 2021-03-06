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
package gplx.xowa.xtns.related;

import gplx.Bool_;
import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.Bry_bfr_;
import gplx.Int_;
import gplx.Ordered_hash;
import gplx.Ordered_hash_;
import gplx.String_;
import gplx.core.brys.fmtrs.Bry_fmtr;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xow_wiki;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.htmls.Xoh_page;
import gplx.xowa.htmls.core.htmls.Xoh_wtr_ctx;
import gplx.xowa.htmls.hxtns.blobs.Hxtn_blob_tbl;
import gplx.xowa.htmls.hxtns.pages.Hxtn_page_mgr;
import gplx.xowa.parsers.Xop_parser_;

import gplx.xowa.Xoa_ttl;
import gplx.xowa.parsers.Xop_ctx;
import gplx.List_adp;
import gplx.List_adp_;
import gplx.Byte_ascii;
import gplx.Hash_adp;
import gplx.xowa.parsers.tmpls.Xop_tkn_;
public class Related_html_bldr implements gplx.core.brys.Bfr_arg {
	private List_adp related = List_adp_.New();
	private Xowe_wiki wiki;
	private Xop_ctx ctx;
	private Bry_fmtr div_after_fmtr;
	public void Clear() { related.Clear(); }
	public void Add(Object obj) { related.Add(obj); }
	public int Count() { return related.Count(); }
	public List_adp List() { return related; }
	public void Set_fmtr(Bry_fmtr div_after_fmtr) {
		this.div_after_fmtr = div_after_fmtr;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int len = related.Count();
		if (len == 0) return;
		div_after_fmtr.Bld_bfr_many(bfr, Bry_.new_a7("<div class=\"read-more-container\"></div>"));
		//bfr.Add_str_a7("<div class=\"read-more-container\"></div>");
	}

	public void HxtnSave(Xowe_wiki wiki, Hxtn_page_mgr hxtnPageMgr, Xoae_page page, int pageId) {
		int len = related.Count();
		// exit if empty
		if (len == 0) return;

		// serialize and save to db
		byte[] rel = RelatedSerialCore.Save(related);
		hxtnPageMgr.Page_tbl__insert(pageId, Hxtn_page_mgr.Id__related, pageId);
		hxtnPageMgr.Blob_tbl__insert(Hxtn_blob_tbl.Blob_tid__wtxt, Hxtn_page_mgr.Id__related, pageId, rel);
	}

	public void Deserialise(Xow_wiki wiki, Xoh_page hpg, Hash_adp props) {
		byte[] data = (byte[])props.Get_by(Related_hxtn_page_wkr.KEY);
		// exit if empty
		if (Bry_.Len_eq_0(data)) return;

		// deserialize data
		this.related = RelatedSerialCore.Load(data);
	}
}
