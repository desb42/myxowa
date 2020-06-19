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
package gplx.xowa.xtns.pagebanners;

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
import gplx.Byte_ascii;
import gplx.Hash_adp;
import gplx.xowa.parsers.tmpls.Xop_tkn_;
public class Pgbnr_html_bldr implements gplx.core.brys.Bfr_arg {
	private Pgbnr_itm pgbnr = null;
	private Xoa_ttl ttl = null;
	private Xowe_wiki wiki;
	private Xop_ctx ctx;
        private Xoh_wtr_ctx hctx;
	public void Clear() {
		pgbnr = null;
	}
	public boolean IsValid() {return pgbnr != null;}
	public Pgbnr_itm Get_itm() { return pgbnr; }
	public void Add(Pgbnr_itm pgbnr, Xoa_ttl ttl) {
		this.pgbnr = pgbnr;
		this.ttl = ttl;
	}
	public void Set_ctx(Xop_ctx ctx) {
		this.ctx = ctx;
		this.wiki = ctx.Wiki();
	}
	public void Add(Xoh_wtr_ctx hctx) {
		this.hctx = hctx;
	}
	public boolean Show_toc_in_html() {
		if (pgbnr == null) return true;
		return pgbnr.Show_toc_in_html();
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (pgbnr == null) return;		// do not build html if no item
		if (pgbnr.Precoded())
			bfr.Add(pgbnr.Pgbnr_bry());
		else {
			ctx.Wiki().Xtn_mgr().Xtn_pgbnr().Write_html(ctx.Page(), ctx, hctx).Bfr_arg__add(bfr);
		}
	}
	public byte[] Generate() {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		Bfr_arg__add(tmp_bfr);

		return tmp_bfr.To_bry();
	}

	public void HxtnSave(Xowe_wiki wiki, Hxtn_page_mgr hxtnPageMgr, Xoae_page page, int pageId) {
		// exit if empty
		if (pgbnr == null) return;

		// serialize and save to db
		byte[] crumb = PgbnrSerialCore.Save(Generate());
		hxtnPageMgr.Page_tbl__insert(pageId, Hxtn_page_mgr.Id__pgbnr, pageId);
		hxtnPageMgr.Blob_tbl__insert(Hxtn_blob_tbl.Blob_tid__wtxt, Hxtn_page_mgr.Id__pgbnr, pageId, crumb);
	}

	public void Deserialise(Xow_wiki wiki, Xoh_page hpg, Hash_adp props) {
		byte[] data = (byte[])props.Get_by(Pgbnr_hxtn_page_wkr.KEY);
		// exit if empty
		if (Bry_.Len_eq_0(data)) return;

		// deserialize data
                data = PgbnrSerialCore.Load(data);
                byte[] html = wiki.Html__hdump_mgr().Load_mgr().Make_mgr().Parse(data, wiki, hpg);
                pgbnr = new Pgbnr_itm();
                pgbnr.Pgbnr_bry_(html);
	}
}
