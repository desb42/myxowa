/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2021-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.htmls.core.wkrs.magics;
import gplx.*;
import gplx.xowa.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.magics.*;	
import gplx.xowa.htmls.core.htmls.*;
import gplx.core.brys.fmtrs.*;
import gplx.core.magic.Gfo_magic_itm;
public class Xoh_magic_html {
	public void Write_html(Bry_bfr bfr, Xow_html_mgr html_mgr, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xop_ctx ctx, byte[] src, Xop_magic_tkn magic) {
		Bry_fmtr fmt;
		byte[] value = null;
		switch (magic.Magic_typ()) {
			case Gfo_magic_itm.Tid_ISBN:
				fmt = isbn_fmt;
				value = Bry_.Mid(src, magic.Src_bgn()+5, magic.Src_end());
				break;
			case Gfo_magic_itm.Tid_PMID:
				fmt = pmid_fmt;
				value = Bry_.Mid(src, magic.Src_bgn()+5, magic.Src_end());
				break;
			default:
			case Gfo_magic_itm.Tid_RFC:
				fmt = rfc_fmt;
				value = Bry_.Mid(src, magic.Src_bgn()+4, magic.Src_end());
				break;
		}
		fmt.Bld_bfr_many(bfr, value);
	}

	private final Bry_fmtr isbn_fmt = Bry_fmtr.new_("<a href=\"/wiki/Special:BookSources/~{isbn}\" class=\"internal mw-magiclink-isbn\">ISBN ~{isbn}</a>", "isbn");
	private final Bry_fmtr rfc_fmt = Bry_fmtr.new_("<a class=\"external mw-magiclink-rfc\" rel=\"nofollow\" href=\"https://tools.ietf.org/html/rfc~{rfc}\">RFC ~{rfc}</a>", "rfc");
	private final Bry_fmtr pmid_fmt = Bry_fmtr.new_("<a class=\"external mw-magiclink-pmid\" rel=\"nofollow\" href=\"//www.ncbi.nlm.nih.gov/pubmed/~{pmid}?dopt=Abstract\">PMID ~{pmid}</a>", "pmid");
}
