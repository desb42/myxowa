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
package gplx.xowa.xtns.proofreadPage; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.primitives.*;
import gplx.xowa.wikis.nss.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.logs.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.lnkis.*; import gplx.xowa.parsers.tmpls.*;
class Pp_index_page {
	public Pp_index_page() {}
	public byte[] Src() {return src;} public Pp_index_page Src_(byte[] v) {src = v; return this;} private byte[] src;
	public List_adp		Pagelist_xndes()	{return pagelist_xndes;} private List_adp pagelist_xndes = List_adp_.New();
	public List_adp		Page_ttls()			{return page_ttls;} private List_adp page_ttls = List_adp_.New();
	public List_adp		Main_lnkis()		{return main_lnkis;} private List_adp main_lnkis = List_adp_.New();
	public List_adp		Invk_args()			{return invk_args;} private List_adp invk_args = List_adp_.New();
	public boolean		Is_jpg() {return is_jpg;} private boolean is_jpg = false;
	//public Xoa_ttl[] Get_ttls_rng(Xowe_wiki wiki, int ns_page_id, byte[] bgn_page_bry, byte[] end_page_bry, Int_obj_ref bgn_page_ref, Int_obj_ref end_page_ref) {
	public List_adp Get_ttls_rng(Xowe_wiki wiki, int ns_page_id, byte[] bgn_page_bry, byte[] end_page_bry, Int_obj_ref bgn_page_ref, Int_obj_ref end_page_ref) {
		List_adp rv = List_adp_.New();
		//int list_len = page_ttls.Count(); if (list_len == 0) return Pp_pages_nde.Ttls_null;
		int list_len = page_ttls.Count(); if (list_len == 0) return rv;
		Xoa_ttl bgn_page_ttl = new_ttl_(wiki, ns_page_id, bgn_page_bry), end_page_ttl = new_ttl_(wiki, ns_page_id, end_page_bry);
		boolean add = bgn_page_ttl == Xoa_ttl.Null;		// if from is missing, default to bgn; EX: <pages index=A to="A/5"/>
		for (int i = 0; i < list_len; i++) {			// REF.MW:ProofreadPageRenderer|renderPages
			Xoa_ttl ttl = (Xoa_ttl)page_ttls.Get_at(i);
			// check for '.jpg/1'
			if (list_len == 1) {
				byte[] raw = ttl.Raw();
				int ttl_len = raw.length;
				if (ttl_len > 7 && raw[ttl_len-6] == '.'
				    && (raw[ttl_len-5] | 32) == 'j'
				    && (raw[ttl_len-4] | 32) == 'p'
				    && (raw[ttl_len-3] | 32) == 'g'
				    && raw[ttl_len-2] == '/'
				    && raw[ttl_len-1] == '1'
				    )
                                    add = Bool_.Y;
                                else if (ttl_len > 5 && raw[ttl_len-4] == '.'
				    && (raw[ttl_len-3] | 32) == 'j'
				    && (raw[ttl_len-2] | 32) == 'p'
				    && (raw[ttl_len-1] | 32) == 'g'
				    )
                                    add = Bool_.Y;
			}
			if (ttl.Eq_page_db(bgn_page_ttl)) {
				add = Bool_.Y;
				bgn_page_ref.Val_(i);
			}
			if (add) rv.Add(new Pp_pages_file(ttl, i+1));
			if (end_page_ttl != Xoa_ttl.Null  // if to is missing default to end;
				  && ttl.Eq_page_db(end_page_ttl)
				  ) {
				add = Bool_.N;
				end_page_ref.Val_(i);
			}
		}
		if (bgn_page_ref.Val() == -1) bgn_page_ref.Val_(0);				// NOTE: set "from" which will be passed to {{MediaWiki:Proofreadpage_header_template}}; DATE:2014-05-21
		if (end_page_ref.Val() == -1) end_page_ref.Val_(list_len - 1);  // NOTE: set "to"   which will be passed to {{MediaWiki:Proofreadpage_header_template}}; DATE:2014-05-21
		//if (rv.Count() == 0) return Pp_pages_nde.Ttls_null;
		//return (Xoa_ttl[])rv.To_ary(Xoa_ttl.class);
		return rv;
	}
	private static Xoa_ttl new_ttl_(Xowe_wiki wiki, int ns_page_id, byte[] bry) {return bry == null ? Xoa_ttl.Null : Xoa_ttl.Parse(wiki, ns_page_id, Pp_pages_nde.Decode_as_dot_bry(bry));}
	public static final    Pp_index_page Null = new Pp_index_page();
}