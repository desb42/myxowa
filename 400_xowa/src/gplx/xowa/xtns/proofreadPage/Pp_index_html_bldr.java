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
package gplx.xowa.xtns.proofreadPage;

import gplx.Bry_;
import gplx.Bry_bfr;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xow_wiki;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.htmls.Xoh_page;
import gplx.xowa.htmls.core.htmls.Xoh_wtr_ctx;
import gplx.xowa.htmls.hxtns.blobs.Hxtn_blob_tbl;
import gplx.xowa.htmls.hxtns.pages.Hxtn_page_mgr;

import gplx.xowa.Xoa_ttl;
import gplx.xowa.parsers.Xop_ctx;
import gplx.Hash_adp;
import gplx.xowa.wikis.domains.Xow_domain_tid_;
public class Pp_index_html_bldr implements gplx.core.brys.Bfr_arg {
	private byte[] index = null;
	private Xoa_ttl ttl = null;
	private Xowe_wiki wiki;
	private Xop_ctx ctx;
	public void Clear() {
		index = null;
	}
	public boolean IsValid() {return index != null;}
	public void Add(byte[] index) {
		this.index = index;
		this.ttl = ttl;
	}
	public void Add(byte[] index, Xoa_ttl ttl) {
		this.index = index;
		this.ttl = ttl;
	}
	public void Set_ctx(Xop_ctx ctx) {
		this.ctx = ctx;
		this.wiki = ctx.Wiki();
	}
	public void Write(Bry_bfr bfr, Xoae_page page) {
		// if wikisource and main: namespace and there is an index page (how do we tell?)
		if (wiki.Domain_tid() == Xow_domain_tid_.Tid__wikisource) {
			// by the presence of pp_indexpage
			if (index == null) {
				// or look it up?
				Xoa_ttl ttl = wiki.Index_page().Get_index_page(page.Db().Page().Id());
				if (ttl != null)
					index = ttl.Full_db();
			}
			if (index != null) {
				bfr.Add(Bry_.new_a7("\n \"proofreadpage_source_href\" : \"\\u003Ca href=\\\"/wiki/"));
				bfr.Add(gplx.langs.htmls.encoders.Gfo_url_encoder_.Href.Encode(index));
				bfr.Add(Bry_.new_a7("\\\" title=\\\""));
				bfr.Add(wiki.Msg_mgr().Val_by_key_obj("proofreadpage_source_message"));
				bfr.Add(Bry_.new_a7("\\\"\\u003E"));
				bfr.Add(wiki.Msg_mgr().Val_by_key_obj("proofreadpage_source"));
				bfr.Add(Bry_.new_a7("\\u003C/a\\u003E\""));
			}
		}
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
	}

	public void HxtnSave(Xowe_wiki wiki, Hxtn_page_mgr hxtnPageMgr, Xoae_page page, int pageId) {
		// exit if empty
		if (index == null) return;

		// serialize and save to db
		byte[] crumb = Pp_index_serialCore.Save(index);
		hxtnPageMgr.Page_tbl__insert(pageId, Hxtn_page_mgr.Id__pp_indexpage, pageId);
		hxtnPageMgr.Blob_tbl__insert(Hxtn_blob_tbl.Blob_tid__wtxt, Hxtn_page_mgr.Id__pp_indexpage, pageId, crumb);
	}

	public void Deserialise(Xow_wiki wiki, Xoh_page hpg, Hash_adp props) {
		byte[] data = (byte[])props.Get_by(Pp_index_hxtn_page_wkr.KEY);
		// exit if empty
		if (Bry_.Len_eq_0(data)) return;

		// deserialize data
		index = Pp_index_serialCore.Load(data);
	}
}
