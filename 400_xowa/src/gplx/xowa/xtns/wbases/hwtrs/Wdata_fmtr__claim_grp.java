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
import gplx.xowa.langs.*;
import gplx.xowa.xtns.wbases.core.*; import gplx.xowa.xtns.wbases.claims.*; import gplx.xowa.xtns.wbases.claims.enums.*; import gplx.xowa.xtns.wbases.claims.itms.*;
import gplx.xowa.apps.apis.xowa.html.*;
import gplx.xowa.xtns.wbases.dbs.Xowb_prop_tbl_itm;
import gplx.xowa.xtns.wbases.stores.Wbase_prop_mgr;
class Wdata_fmtr__claim_grp implements gplx.core.brys.Bfr_arg {
	private Wdata_fmtr__claim_tbl fmtr_tbl = new Wdata_fmtr__claim_tbl(); private boolean is_empty;
	private Xoapi_toggle_itm toggle_itm;
	private Wdata_toc_data toc_data;
	private int type;
	private Wdata_wiki_mgr wdata_mgr;
	public Wdata_fmtr__claim_grp(int type) { this.type = type; }
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_toc_data toc_data, Xoapi_toggle_mgr toggle_mgr, Wdata_lbl_mgr lbl_mgr) {
		this.toc_data = toc_data;
		this.toggle_itm = toggle_mgr.Get_or_new("wikidatawiki-claim-" + Integer.toString(type));
		fmtr_tbl.Init_by_ctor(wdata_mgr, lbl_mgr);
		this.wdata_mgr = wdata_mgr;
	}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {
		if (type == 1)
			toc_data.Orig_(msgs.Statements_tbl_hdr());
		else if (type == 2)
			toc_data.Orig_(msgs.Indicators_tbl_hdr());
		else if (type == 3)
			toc_data.Orig_(msgs.Constraints_tbl_hdr());
		toggle_itm.Init_msgs(msgs.Toggle_title_y(), msgs.Toggle_title_n());
		fmtr_tbl.Init_by_lang(lang, msgs);
	}
	public void Init_by_wdoc(byte[] ttl, Ordered_hash list) {
		int list_count = list.Count();
		Ordered_hash new_list = Ordered_hash_.New();
		for (int i = 0; i < list_count; i++) {
			Wbase_claim_grp grp = (Wbase_claim_grp)list.Get_at(i);
			Wbase_claim_base itm = grp.Get_at(0);
			int pid = itm.Pid();
			Wbase_prop_mgr prop_mgr = wdata_mgr.Prop_mgr();
			Xowb_prop_tbl_itm tbl_itm = prop_mgr.Get_or_null("p" + Integer.toString(pid), null);
			if (pid == 2302) { // Constraint
				if (type == 3)
					new_list.Add(grp.Id_ref(), grp);
				else
					continue;
			}
			if (type == 1 && tbl_itm.Datatype() != 11)
				new_list.Add(grp.Id_ref(), grp);
			if (type == 2 && tbl_itm.Datatype() == 11)
				new_list.Add(grp.Id_ref(), grp);
		}
		list_count = new_list.Count();
		this.is_empty = list_count == 0; if (is_empty) return;
		toc_data.Make(list_count);
		fmtr_tbl.Init_by_wdoc(ttl, new_list);
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (is_empty) return;
		fmtr.Bld_bfr_many(bfr, toc_data.Href(), toc_data.Text(), toggle_itm.Html_toggle_btn(), toggle_itm.Html_toggle_hdr(), fmtr_tbl);
	}
	private Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "  <h2 class=\"wb-section-heading\" dir=\"auto\" id=\"~{hdr_href}\">~{hdr_text}~{toggle_btn}</h2>"
	, "  <div class=\"wb-claimlistview\"~{toggle_hdr}>"
	, "    <div class=\"wikibase-statementgrouplistview\">"
	, "      <div class=\"wikibase-listview\">~{tbls}"
	, "      </div>"
	, "    </div>"
	, "  </div>"
	), "hdr_href", "hdr_text", "toggle_btn", "toggle_hdr", "tbls");
}
class Wdata_fmtr__claim_tbl implements gplx.core.brys.Bfr_arg {
	private Wdata_fmtr__claim_row fmtr_row = new Wdata_fmtr__claim_row(); private Wdata_lbl_mgr lbl_mgr; 
	private Ordered_hash list; private byte[] ttl;
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_lbl_mgr lbl_mgr) {this.lbl_mgr = lbl_mgr; fmtr_row.Init_by_ctor(wdata_mgr, lbl_mgr);}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {
		fmtr_row.Init_by_lang(lang, msgs);
	}
	public void Init_by_wdoc(byte[] ttl, Ordered_hash list) {
		this.list = list;
		this.ttl = ttl;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int len = list.Count();
		for (int i = 0; i < len; ++i) {
			Wbase_claim_grp grp = (Wbase_claim_grp)list.Get_at(i);
			if (grp.Len() == 0) continue;	// NOTE: group will be empty when claims are empty; EX: "claims": []; PAGE:wd.p:585; DATE:2014-10-03
			int pid = grp.Id();
			byte[] pid_lbl = lbl_mgr.Get_text__pid(pid);
			fmtr_row.Init_by_grp(ttl, grp);
			fmtr.Bld_bfr_many(bfr, pid, pid_lbl, fmtr_row);
		}
	}
	// https://github.com/wikimedia/Wikibase/view/resources/templates.php L55-63
	private Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "<div class=\"wikibase-statementgroupview\" id=\"P~{pid}\" data-property-id=\"P~{pid}\">"
	, "  <div class=\"wikibase-statementgroupview-property\">"
	, "    <div class=\"wikibase-statementgroupview-property-label\" dir=\"auto\">"
	, "      P~{pid}&nbsp;-&nbsp;<a href=\"/wiki/Property:P~{pid}\" title=\"Property:P~{pid}\">~{pid_lbl}</a>"
	, "    </div>"
	, "  </div>"
	, "  ~{itms}"
	, "</div>"
	), "pid", "pid_lbl", "itms");
}
class Wdata_fmtr__claim_link implements gplx.core.brys.Bfr_arg {
	private byte[] val;
	private Bry_fmtr link_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "<a href=\"~{href}\" class=\"~{klass} external\">~{lnk}</a>"
	), "href", "lnk", "klass"
	);
	public void Init_by_claim(byte[] val) {
		this.val = val;
	}

	public void Bfr_arg__add(Bry_bfr bfr) {
		int val_len = val.length - 1; //NB
		if (val_len > 2 && val[0] == '[' && val[val_len] == ']') {
			for (int i = 1; i < val_len; i++){
				if (val[i] == ' ') {
					link_fmtr.Bld_bfr_many(bfr, Bry_.Mid(val, 1, i), Bry_.Mid(val, i+1, val_len), Bry_.Empty);
					return;
				}
			}
		}
		if (val_len > 8) {
			if (val[0] == 'h' && val[1] == 't' && val[2] == 't' && val[3] == 'p') {
				if (val[4] == ':' || (val[4] == 's' && val[5] == ':')) {
					link_fmtr.Bld_bfr_many(bfr, val, val, Bry_.Empty);
					return;
				}
			} 
		}
		bfr.Add(val);
	}
}
class Wdata_fmtr__claim_row implements gplx.core.brys.Bfr_arg {
	private byte[] ttl;
	private Wdata_visitor__html_wtr claim_html_wtr = new Wdata_visitor__html_wtr();
	private Wdata_fmtr__qual_tbl fmtr_qual = new Wdata_fmtr__qual_tbl();
	private Wdata_fmtr__ref_tbl fmtr_ref = new Wdata_fmtr__ref_tbl();
	private Wdata_fmtr__claim_link fmtr_link = new Wdata_fmtr__claim_link();
	private Wdata_wiki_mgr wdata_mgr; private Wdata_lbl_mgr lbl_mgr; private Wdata_hwtr_msgs msgs;
	private Xol_lang_itm lang;
	private Wbase_claim_grp claim_grp; private Bry_bfr tmp_bfr = Bry_bfr_.Reset(255);
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_lbl_mgr lbl_mgr) {
		this.lbl_mgr = lbl_mgr;
		this.wdata_mgr = wdata_mgr;
		fmtr_qual.Init_by_ctor(wdata_mgr, lbl_mgr);
		fmtr_ref.Init_by_ctor(wdata_mgr, lbl_mgr);
	}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {
		this.lang = lang;
		this.msgs = msgs;
		fmtr_qual.Init_by_lang(lang, msgs);
		fmtr_ref.Init_by_lang(lang, msgs);
	}
	public void Init_by_grp(byte[] ttl, Wbase_claim_grp claim_grp) {
		this.ttl = ttl; this.claim_grp = claim_grp;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int len = claim_grp.Len();
		claim_html_wtr.Init(tmp_bfr, wdata_mgr, msgs, lbl_mgr, lang, ttl);
		for (int i = 0; i < len; ++i) {
			Wbase_claim_base itm = claim_grp.Get_at(i);
			itm.Welcome(claim_html_wtr, true ); // ???!!!???
			fmtr_link.Init_by_claim(tmp_bfr.To_bry_and_clear());
			//byte[] val = tmp_bfr.To_bry_and_clear();
                        //val = checklink(val);
			fmtr_qual.Init_by_claim(ttl, itm);
			fmtr_ref.Init_by_claim(ttl, itm);
			row_fmtr.Bld_bfr_many(bfr, Wbase_claim_rank_.Reg.Get_str_or_fail(itm.Rank_tid()), fmtr_link, fmtr_qual, fmtr_ref);
		}
	}
	private Bry_fmtr row_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "          <div class=\"wikibase-statementlistview\">"
	, "            <div class=\"wikibase-statementlistview-listview\">"
	, "              <div class=\"wikibase-statementview wikibase-statement\">"
	, "                <div class=\"wikibase-statementview-rankselector\">"
	, "                  <div class=\"wikibase-rankselector ui-state-disabled\">"
	, "                    <span class=\"ui-icon ui-icon-rankselector wikibase-rankselector-~{rank_name}\" title=\"~{rank_name} rank\"></span>"
	, "                  </div>"
	, "                </div>"
	, "                <div class=\"wikibase-statementview-mainsnak-container\">"	// omit -Q2$e8ba1188-4aec-9e37-a75e-f79466c1913e
	, "                  <div class=\"wikibase-statementview-mainsnak\" dir=\"auto\">"
	, "                    <div class=\"wikibase-snakview\">"
	, "                      <div class=\"wikibase-snakview-property-container\">"
	, "                        <div class=\"wikibase-snakview-property\" dir=\"auto\"></div>"
	, "                      </div>"
	, "                      <div class=\"wikibase-snakview-value-container\" dir=\"auto\">"
	, "                        <div class=\"wikibase-snakview-typeselector\"></div>"
	, "                        <div class=\"wikibase-snakview-value wikibase-snakview-variation-valuesnak\">~{value}"
	, "                        </div>"
	, "                      </div>"
	, "                    </div>"
	, "                  </div>~{qualifiers}"
	, "                </div>~{references}"
	, "              </div>"
	, "            </div>"
	, "          </div>"
	), "rank_name", "value", "qualifiers", "references"
	);
}
class Wdata_fmtr__qual_tbl implements gplx.core.brys.Bfr_arg {
	private Wdata_fmtr__qual_row fmtr_row = new Wdata_fmtr__qual_row(); private Wbase_claim_base claim;
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_lbl_mgr lbl_mgr) {fmtr_row.Init_by_ctor(wdata_mgr, lbl_mgr);}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {
		fmtr_row.Init_by_lang(lang, msgs);
	}
	public void Init_by_claim(byte[] ttl, Wbase_claim_base claim) {
		this.claim = claim;
		fmtr_row.Init_by_grp(ttl, claim.Qualifiers());
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (claim.Qualifiers() == null || claim.Qualifiers().Len() == 0) return;
		fmtr.Bld_bfr_many(bfr, fmtr_row);
	}
	private Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "                  <div class=\"wikibase-statementview-qualifiers\">"
	, "                    <div class=\"wikibase-listview\">~{itms}"
	, "                    </div>"
	, "                  </div>"
	), "itms");
}
class Wdata_fmtr__qual_row implements gplx.core.brys.Bfr_arg {
	private byte[] ttl; private Wdata_visitor__html_wtr claim_html_wtr = new Wdata_visitor__html_wtr();
	private Wdata_wiki_mgr wdata_mgr; private Wdata_lbl_mgr lbl_mgr; private Wdata_hwtr_msgs msgs;
	private Xol_lang_itm lang;
	private Wbase_claim_grp_list quals; private Bry_bfr tmp_bfr = Bry_bfr_.Reset(255);
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_lbl_mgr lbl_mgr) {this.wdata_mgr = wdata_mgr; this.lbl_mgr = lbl_mgr;}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {this.lang = lang; this.msgs = msgs;}
	public void Init_by_grp(byte[] ttl, Wbase_claim_grp_list quals) {this.ttl = ttl; this.quals = quals;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int len = quals.Len();
		claim_html_wtr.Init(tmp_bfr, wdata_mgr, msgs, lbl_mgr, lang, ttl);
		for (int i = 0; i < len; ++i) {
			Wbase_claim_grp grp = quals.Get_at(i);
			int grp_len = grp.Len();
			for (int j = 0; j < grp_len; ++j) {
				Wbase_claim_base itm = grp.Get_at(j);
				itm.Welcome(claim_html_wtr, false);
				byte[] val = tmp_bfr.To_bry_and_clear();
				row_fmtr.Bld_bfr_many(bfr, grp.Id(), lbl_mgr.Get_text__pid(grp.Id()), val);
			}
		}
	}
	private Bry_fmtr row_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "                      <div class=\"wikibase-snaklistview\"'>"
	, "                        <div class=\"wikibase-snaklistview-listview\">"
	, "                          <div class=\"wikibase-snakview\">"
	, "                            <div class=\"wikibase-snakview-property-container\">"
	, "                              <div class=\"wikibase-snakview-property\" dir=\"auto\">"
	, "                                <a href=\"/wiki/Property:P~{pid}\" title=\"Property:P~{pid}\">~{pid_lbl}</a>"
	, "                              </div>"
	, "                            </div>"
	, "                            <div class=\"wikibase-snakview-value-container\" dir=\"auto\">"
	, "                              <div class=\"wikibase-snakview-typeselector\"></div>"
	, "                              <div class=\"wikibase-snakview-value wikibase-snakview-variation-valuesnak\">~{value}"
	, "                              </div>"
	, "                            </div>"
	, "                          </div>"
	, "                        </div>"
	, "                      </div>"
	), "pid", "pid_lbl", "value"
	);
}
class Wdata_fmtr__ref_tbl implements gplx.core.brys.Bfr_arg {
	private Wdata_fmtr__ref_row fmtr_row = new Wdata_fmtr__ref_row(); private Wbase_claim_base claim;
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_lbl_mgr lbl_mgr) {fmtr_row.Init_by_ctor(wdata_mgr, lbl_mgr);}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {
		fmtr_row.Init_by_lang(lang, msgs);
	}
	public void Init_by_claim(byte[] ttl, Wbase_claim_base claim) {
		this.claim = claim;
		fmtr_row.Init_by_grp(ttl, claim.References());
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (claim.References() == null) return;
		fmtr.Bld_bfr_many(bfr, fmtr_row);
	}
	private Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
//		, "                <div class='wb-statement-references-heading'>1 reference</div>"
	, "                <div class=\"wikibase-statementview-references\">"
	, "                  <div class=\"wikibase-listview\">"
	, "                    <div class=\"wikibase-referenceview\">"	// OMIT: wb-referenceview-8e7d51e38606193465d2a1e9d41ba490e06682a6
	, "                      <div class=\"wikibase-referenceview-heading\"></div>"
	, "                      <div class=\"wikibase-referenceview-listview\">"
	, "                        <div class=\"wikibase-snaklistview\">"
	, "                          <div class=\"wikibase-snaklistview-listview\">~{itms}"
	, "                          </div>"
	, "                        </div>"
	, "                      </div>"
	, "                    </div>"
	, "                  </div>"
	, "                </div>"
	), "itms");
}
class Wdata_fmtr__ref_row implements gplx.core.brys.Bfr_arg {
	private byte[] ttl; private Wdata_visitor__html_wtr claim_html_wtr = new Wdata_visitor__html_wtr();
	private Wdata_wiki_mgr wdata_mgr; private Wdata_lbl_mgr lbl_mgr; private Wdata_hwtr_msgs msgs;
	private Xol_lang_itm lang;
	private Bry_bfr tmp_bfr = Bry_bfr_.Reset(255);
	private Wbase_references_grp[] ref_grps;
	public void Init_by_ctor(Wdata_wiki_mgr wdata_mgr, Wdata_lbl_mgr lbl_mgr) {this.wdata_mgr = wdata_mgr; this.lbl_mgr = lbl_mgr;}
	public void Init_by_lang(Xol_lang_itm lang, Wdata_hwtr_msgs msgs) {this.lang = lang; this.msgs = msgs;}
	public void Init_by_grp(byte[] ttl, Wbase_references_grp[] ref_grps) {this.ttl = ttl; this.ref_grps = ref_grps;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int len = ref_grps.length;
		claim_html_wtr.Init(tmp_bfr, wdata_mgr, msgs, lbl_mgr, lang, ttl);
		for (int i = 0; i < len; ++i) {
			Wbase_references_grp grp_itm = ref_grps[i];
			Wbase_claim_grp_list grp = grp_itm.Snaks();
			int grp_len = grp.Len();
			for (int j = 0; j < grp_len; ++j) {
				Wbase_claim_grp grp2 = grp.Get_at(j);
				int grp2_len = grp2.Len();
				for (int k = 0; k < grp2_len; ++k) {
					Wbase_claim_base itm = grp2.Get_at(k);
					itm.Welcome(claim_html_wtr, false);
					byte[] val = tmp_bfr.To_bry_and_clear();
					String snakViewCssClass = Wbase_claim_value_type_.Reg.Get_str_or_fail(itm.Snak_tid());
					row_fmtr.Bld_bfr_many(bfr, grp2.Id(), lbl_mgr.Get_text__pid(grp2.Id()), val, snakViewCssClass);
				}
			}
		}
	}
	private Bry_fmtr row_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
/*
	, "                            <div class='wikibase-snakview'>"
	, "                              <div class='wikibase-snakview-property-container'>"
	, "                                <div class='wikibase-snakview-property' dir='auto'>"
	, "                                  <a href='/wiki/Property:P~{pid}' title='Property:P~{pid}'>~{pid_lbl}</a>"
	, "                                </div>"
	, "                              </div>"
	, "                              <div class='wikibase-snakview-value-container' dir='auto'>"
	, "                                <div class='wikibase-snakview-typeselector'></div>"
	, "                                <div class='wikibase-snakview-value wikibase-snakview-variation-valuesnak'>~{value}"
	, "                                </div>"
	, "                              </div>"
	, "                            </div>"
	), "pid", "pid_lbl", "value"
*/
	, "<div class=\"wikibase-snakview wikibase-snakview-~{hash}\">"
	, "  <div class=\"wikibase-snakview-property-container\">"
	, "    <div class=\"wikibase-snakview-property\" dir=\"auto\">"
	, "      <a href=\"/wiki/Property:P~{pid}\" title=\"Property:P~{pid}\">~{pid_lbl}</a>"
	, "    </div>"
	, "  </div>"
	, "  <div class=\"wikibase-snakview-value-container\" dir=\"auto\">"
	, "    <div class=\"wikibase-snakview-typeselector\"></div>"
	, "    <div class=\"wikibase-snakview-body\">"
	, "      <div class=\"wikibase-snakview-value ~{cssclass}\">~{value}</div>"
	, "      <div class=\"wikibase-snakview-indicators\"></div>"
	, "    </div>"
	, "  </div>"
	, "</div>"
	), "pid", "pid_lbl", "value", "cssclass", "hash"
	);
}
