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
package gplx.xowa.xtns.cites; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.brys.*; import gplx.core.brys.fmtrs.*; import gplx.core.brys.args.*;
class Xoh_ref_list_fmtr implements gplx.core.brys.Bfr_arg {
	private Xowe_wiki wiki; private Ref_html_wtr_cfg cfg; private Ref_nde itm;
	private final    Bfr_arg__bry_fmtr fmtr = Bfr_arg_.New_bry_fmtr__null();
	public void Init(Xowe_wiki wiki, Ref_html_wtr_cfg cfg, Ref_nde itm) {
		this.wiki = wiki; this.cfg = cfg; this.itm = itm;
	}
	public Ref_nde Identify_main_ref() {
		if (HasTxt(itm)) return itm;
		int itm_related_len = itm.Related_len();
		for (int i = 0; i < itm_related_len; i++) {
			Ref_nde rel = itm.Related_get(i);
			if (rel.Follow_y()) continue; // follow should not be the main item; will be picked up in separate loop later; ISSUE#:555; DATE:2019-09-01
			if (HasTxt(rel)) return rel;
		}
		return itm; // no itm has text; TODO_OLD:WARN
	}
	private boolean HasTxt(Ref_nde v) {return v.Body() != null && v.Body().Root_src().length > 0;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int related_len = itm.Related_len();
		Bry_fmtr itm_fmtr = cfg.Grp_html_list();
		Fmt(itm_fmtr, wiki, bfr, itm, -1, related_len);
		for (int i = 0; i < related_len; i++) {
			Ref_nde link_itm = itm.Related_get(i);
			if (link_itm.Nested()) continue;
			if (i == related_len-1)
				bfr.Add_str_u8(cfg.Grp_many_and());
			else
				bfr.Add_str_u8(cfg.Grp_many_sep());
			Fmt(itm_fmtr, wiki, bfr, link_itm, i, related_len);
		}
	}
	private void Fmt(Bry_fmtr itm_fmtr, Xowe_wiki wiki, Bry_bfr trg, Ref_nde itm, int i, int count) {
		int itm_idx_minor = itm.Idx_minor();
		if (itm_idx_minor < 0) return;	// HACK: <ref follow created a negative index; ignore these references for now; de.wikisource.org/wiki/Seite:Die Trunksucht.pdf/63; DATE:2013-06-22
		byte[] backlabel 
			= itm_idx_minor < cfg.Backlabels_len()
			? cfg.Backlabels()[itm.Idx_minor()]
			: wiki.Parser_mgr().Main().Parse_text_to_html(wiki.Parser_mgr().Ctx(), wiki.Msg_mgr().Val_by_key_args(Ref_html_wtr_cfg.Msg_backlabels_err, itm.Idx_minor()))
			;
		byte[] backref = referencesFormatEntryNumericBacklinkLabel(itm.Idx_major() + 1, i + 1, count);
		itm_fmtr.Bld_bfr_many(trg
			, fmtr.Set(cfg.Itm_id_key_one(), cfg.Itm_crlp(), itm.Name(), itm.Idx_major(), itm.Idx_minor(), cfg.Itm_crls())
			, backref
			//, Bry_.Empty // extra field see https://github.com/wikimedia/mediawiki-extensions-Cite/includes/Cite.php
			             // $this->referencesFormatEntryNumericBacklinkLabel( $val['number'], $i, $val['count'] ),
			, backlabel
			);
	}
	private byte[] referencesFormatEntryNumericBacklinkLabel(int base, int offset, int max) {
		Bry_bfr bfr = Bry_bfr_.New();
		bfr.Add_int_variable(base); // language sensitive
		bfr.Add_byte(Byte_ascii.Dot); // or comma!!! language sensitive
		int maxwidth;
		if (max < 10)
			maxwidth = 1;
		else if (max < 100)
			maxwidth = 2;
		else if (max < 1000)
			maxwidth = 3;
		else
			maxwidth = 4;
		int width;
		if (offset < 10)
			width = 1;
		else if (offset < 100)
			width = 2;
		else if (offset < 1000)
			width = 3;
		else
			width = 4;
		int zeros = maxwidth - width;
		for (int i = 0; i < zeros; i++)
			bfr.Add_byte(Byte_ascii.Num_0);
		bfr.Add_int_variable(offset);
		return bfr.To_bry();
	}
}
