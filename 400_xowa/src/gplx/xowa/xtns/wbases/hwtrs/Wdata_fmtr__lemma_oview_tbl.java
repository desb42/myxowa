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
import gplx.langs.htmls.encoders.*;
import gplx.xowa.xtns.wbases.core.*; import gplx.xowa.apps.apis.xowa.xtns.*;
class Wdata_fmtr__lemma_oview_tbl implements gplx.core.brys.Bfr_arg {
	private Xoapi_wikibase wikibase_api; private Gfo_url_encoder href_encoder;
        private Wdata_wiki_mgr wdata_mgr;
	private Bry_bfr tmp_bfr = Bry_bfr_.New_w_size(255);
	private Wdata_doc wdoc;
	public void Init_by_ctor(Xoapi_wikibase wikibase_api, Wdata_wiki_mgr wdata_mgr) {
            this.wikibase_api = wikibase_api; this.wdata_mgr = wdata_mgr;
        }
	public void Init_by_lang(byte[] lang_0, Wdata_hwtr_msgs msgs) {
	}
	public void Init_by_wdoc(Wdata_doc wdoc) {
		this.wdoc = wdoc;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		byte[] lang_code = Bry_.new_a7("en");
		byte[] name = wdata_mgr.Doc_name(wdoc);
		byte[] lang_qcode = wdoc.Jdoc().Get_val_as_bry_or(Bry_.new_a7("language"), Bry_.Empty);
		Wdata_doc lang_wdoc = wdata_mgr.Doc_mgr.Get_by_xid_or_null(lang_qcode);
		byte[] lang_name = wdata_mgr.Doc_name(lang_wdoc);
		byte[] lexcat = wdoc.Jdoc().Get_val_as_bry_or(Bry_.new_a7("lexicalCategory"), Bry_.Empty);
		Wdata_doc lexcat_wdoc = wdata_mgr.Doc_mgr.Get_by_xid_or_null(lexcat);
		byte[] lexcat_name = wdata_mgr.Doc_name(lexcat_wdoc);

		row_fmtr.Bld_bfr_many(bfr, wdoc.Qid(), lang_code, name, lang_qcode, lang_name, lexcat, lexcat_name);
	}
	private Bry_fmtr row_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "<div id=\"wb-lexeme-header\" class=\"wb-lexeme-header\">"
	, "  <div id=\"wb-lexeme-header-lemmas\">"
	, "    <div class=\"wb-lexeme-header_id\">(~{id})</div>"
	, "    <div class=\"wb-lexeme-header_lemma-widget\">"
	, "      <div id=\"lemmas-widget\">"
	, "        <div class=\"lemma-widget\">"
	, "          <ul class=\"lemma-widget_lemma-list\">"
	, "            <li class=\"lemma-widget_lemma\">"
	, "              <span class=\"lemma-widget_lemma-value\" lang=\"~{lang_code}\">~{name}</span>"
	, "              <span class=\"lemma-widget_lemma-language\">~{lang_code}</span>"
	, "            </li>"
	, "          </ul>"
	, "        </div>"
	, "      </div>"
	, "    </div>"
	, "  </div>"
	, "  <div>"
	, "    <div class=\"language-lexical-category-widget\">"
	, "      <div>"
	, "        <div>"
	, "          <span>Language</span>"
	, "          <span class=\"language-lexical-category-widget_language\"><a title=\"~{lang_qcode}\" href=\"/wiki/~{lang_qcode}\">~{lang_name}</a></span>"
	, "        </div>"
	, "        <div>"
	, "          <span>Lexical category</span>"
	, "          <span class=\"language-lexical-category-widget_lexical-category\"><a title=\"~{lexcat}\" href=\"/wiki/~{lexcat}\">~{lexcat_name}</a></span>"
	, "        </div>"
	, "      </div>"
	, "    </div>"
	, "  </div>"
	, "</div>"
	), "id", "lang_code", "name", "lang_qcode", "lang_name", "lexcat", "lexcat_name"
	);
}
