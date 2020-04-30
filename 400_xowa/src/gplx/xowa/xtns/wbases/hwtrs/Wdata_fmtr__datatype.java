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
package gplx.xowa.xtns.wbases.hwtrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*;
import gplx.core.brys.fmtrs.*;
import gplx.langs.htmls.*;
import gplx.xowa.langs.*; import gplx.xowa.wikis.*; import gplx.xowa.xtns.wbases.core.*; import gplx.xowa.apps.apis.xowa.html.*;
class Wdata_fmtr__datatype implements gplx.core.brys.Bfr_arg {
	private byte[] datatype;
	public void Init_by_wdoc(byte[] datatype) {
		this.datatype = datatype;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		fmtr.Bld_bfr_many(bfr, datatype);
	}
	private final    Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "<h2 class=\"wb-section-heading section-heading wikibase-propertypage-datatype\" dir=\"auto\">"
	, " <span class=\"mw-headline\" id=\"datatype\">Data type</span>"
	, "</h2>"
	, "<div class=\"wikibase-propertyview-datatype\">"
	, " <div class=\"wikibase-propertyview-datatype-value\">~{datatype}</div>"
	, "</div>"
	), "datatype"
	);
}
