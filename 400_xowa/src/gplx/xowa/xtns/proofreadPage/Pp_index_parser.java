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
class Pp_index_parser {
	public static Pp_index_page Parse(Xowe_wiki wiki, Xop_ctx ctx, Xoa_ttl index_ttl, int ns_page_id) {
		byte[] src = wiki.Cache_mgr().Page_cache().Get_or_load_as_src(index_ttl);
		if (src == null) return Pp_index_page.Null;
		Xop_parser sub_parser = Xop_parser.new_(wiki, wiki.Parser_mgr().Main().Tmpl_lxr_mgr(), wiki.Parser_mgr().Main().Wtxt_lxr_mgr());
		Xop_ctx sub_ctx = Xop_ctx.New__sub__reuse_page(ctx);
		Xop_tkn_mkr tkn_mkr = sub_ctx.Tkn_mkr();
		Xop_root_tkn index_root = tkn_mkr.Root(src);
		byte[] mid_text = sub_parser.Expand_tmpl(index_root, sub_ctx, tkn_mkr, src);
		Pp_index_page rv = new Pp_index_page();
		Inspect_tmpl(rv, src, index_root, index_root.Subs_len(), ns_page_id, 1);
		sub_parser.Parse_wtxt_to_wdom(index_root, sub_ctx, tkn_mkr, mid_text, Xop_parser_.Doc_bgn_bos);
		rv.Src_(mid_text);
		Inspect_wiki(rv, mid_text, index_root, index_root.Subs_len(), ns_page_id, 1);	// changed from src to mid_text; DATE:2014-07-14
		return rv;
	}
	private static void Inspect_tmpl(Pp_index_page rv, byte[] src, Xop_tkn_itm_base owner, int owner_len, int ns_page_id, int depth) {
		for (int i = 0; i < owner_len; i++) {
			Xop_tkn_itm sub = owner.Subs_get(i);
			int sub_tid = sub.Tkn_tid();
			switch (sub_tid) {
				case Xop_tkn_itm_.Tid_tmpl_invk: {
					if (depth == 1) { // NOTE: only look at tmpls directly beneath root; note that this should be fine b/c [[Index:]] pages have a constrained form-fields GUI; ProofreadPage takes the form fields, and builds a template from it; DATE:2014-01-25
						Xot_invk_tkn invk = (Xot_invk_tkn)sub;
						int args_len = invk.Args_len();
						for (int j = 0; j < args_len; j++) {
							Arg_nde_tkn nde_tkn = invk.Args_get_by_idx(j);
							byte[] key = Get_bry(src, nde_tkn.Key_tkn());
							byte[] val = Get_bry(src, nde_tkn.Val_tkn());
							rv.Invk_args().Add(new Pp_index_arg(key, val));
						}
					}
					break;
				}
			}
			int sub_subs_len = sub.Subs_len();
			if (sub_subs_len > 0)
				Inspect_tmpl(rv, src, (Xop_tkn_itm_base)sub, sub_subs_len, ns_page_id, depth + 1);
		}
	}
	private static void Inspect_wiki(Pp_index_page rv, byte[] src, Xop_tkn_itm_base owner, int owner_len, int ns_page_id, int depth) {
		for (int i = 0; i < owner_len; i++) {
			Xop_tkn_itm sub = owner.Subs_get(i);
			int sub_tid = sub.Tkn_tid();
			switch (sub_tid) {
				case Xop_tkn_itm_.Tid_lnki: {
					Xop_lnki_tkn lnki = (Xop_lnki_tkn)sub;
					int sub_ns_id = lnki.Ns_id();
					if		(sub_ns_id == ns_page_id)		rv.Page_ttls().Add(lnki.Ttl());
					else if	(sub_ns_id == Xow_ns_.Tid__main)	rv.Main_lnkis().Add(lnki);
					break;
				}
				case Xop_tkn_itm_.Tid_xnde: {
					Xop_xnde_tkn xnde = (Xop_xnde_tkn)sub;
					if (xnde.Tag().Id() == Xop_xnde_tag_.Tid__pagelist)
						rv.Pagelist_xndes().Add(xnde);
					break;
				}
			}
			int sub_subs_len = sub.Subs_len();
			if (sub_subs_len > 0)
				Inspect_wiki(rv, src, (Xop_tkn_itm_base)sub, sub_subs_len, ns_page_id, depth + 1);
		}
	}
	private static byte[] Get_bry(byte[] src, Arg_itm_tkn itm) {
		return Bry_.Mid(src, itm.Dat_bgn(), itm.Dat_end());
	}
}
class Pp_index_arg {
	public Pp_index_arg(byte[] key, byte[] val) {this.key = key; this.val = val;}
	public byte[] Key() {return key;} private byte[] key;
	public byte[] Val() {return val;} private byte[] val;
}
