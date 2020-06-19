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
package gplx.xowa.xtns.geoCrumbs;

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
public class GeoCrumbs_html_bldr implements gplx.core.brys.Bfr_arg {
	private byte[] isin = null;
	private byte[] redirect_msg = Bry_.Empty;
	private Xoa_ttl ttl = null;
	private Xowe_wiki wiki;
	private Xop_ctx ctx;
	public void Enabled_(boolean v) {enabled = v;} private boolean enabled = Bool_.Y;
	public void Clear() {
		enabled = Bool_.Y;
		isin = null;
	}
	public int Count() {return isin == null ? 0 : 1;}
//	public boolean Has(String key) {return list.Has(key);}
	public void Add(byte[] redirect_msg) {
		this.redirect_msg = redirect_msg;
	}
	public void Set_ctx(Xop_ctx ctx) {
		this.ctx = ctx;
                this.wiki = ctx.Wiki();
	}
	public void Add(byte[] isin, Xoa_ttl ttl) {
		if (!enabled) return;  // ??
		this.isin = isin;
		this.ttl = ttl;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (isin == null) return;		// do not build html if no item
		List_adp list = wiki.Bread().Get_breadcrumbs(ttl, isin);

		int nsid = ttl.Ns().Id();
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		int len = list.Count();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				byte[] parent = (byte[])list.Get_at(i);
				if (i > 0)
					tmp_bfr.Add_str_a7( " > " );
				byte[] lnk;
				if (nsid > 0) // not Main:
					lnk = Bry_.Add(Xop_tkn_.Lnki_bgn, Byte_ascii.Colon_bry, ttl.Ns().Name_db_w_colon(), parent, Byte_ascii.Pipe_bry, parent, Xop_tkn_.Lnki_end);		// make "[[xx:ttl ttl]]"
				else
					lnk = Bry_.Add(Xop_tkn_.Lnki_bgn, parent, Xop_tkn_.Lnki_end);		// make "[[ttl]]"
				tmp_bfr.Add( lnk );
			}
			if (len > 0)
				tmp_bfr.Add_str_a7( " > " );
			tmp_bfr.Add(ttl.Page_txt());
		}
		byte[] bread = tmp_bfr.To_bry_and_clear();
		wiki.Parser_mgr().Main().Parse_text_to_html(tmp_bfr, ctx, ctx.Page(), false, bread);
		bread = tmp_bfr.To_bry_and_clear();

//		bfr.Add_str_a7("<div class=\"ext-wpb-pagebanner-subtitle\">");
		if (redirect_msg.length != 0) {
			bfr.Add(redirect_msg).Add_str_a7("<br/>\n");
		}
		bfr.Add(bread);
//		bfr.Add_str_a7("</div>");
		bfr.Add_str_a7("<br />");
	}
	public byte[] Generate() {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		Bfr_arg__add(tmp_bfr);

		return tmp_bfr.To_bry();
	}

	public void HxtnSave(Xowe_wiki wiki, Hxtn_page_mgr hxtnPageMgr, Xoae_page page, int pageId) {
		// exit if empty
		if (isin == null) return;

		// serialize and save to db
		byte[] crumb = GeoCrumbsSerialCore.Save(isin);
		hxtnPageMgr.Page_tbl__insert(pageId, Hxtn_page_mgr.Id__geocrumb, pageId);
		hxtnPageMgr.Blob_tbl__insert(Hxtn_blob_tbl.Blob_tid__wtxt, Hxtn_page_mgr.Id__geocrumb, pageId, crumb);
	}

	public void Deserialise(Xow_wiki wiki, Xoh_page hpg, Hash_adp props) {
            ttl = hpg.Ttl();
            byte[] data = (byte[])props.Get_by(GeoCrumbs_hxtn_page_wkr.KEY);
		// exit if empty
		if (Bry_.Len_eq_0(data)) return;

		// deserialize data
		this.isin = GeoCrumbsSerialCore.Load(data);
	}
}
