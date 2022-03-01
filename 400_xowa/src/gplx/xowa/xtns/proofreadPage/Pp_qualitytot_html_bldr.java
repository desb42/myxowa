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
import gplx.Bry_bfr_;
import gplx.xowa.Xoae_page;
import gplx.xowa.Xow_wiki;
import gplx.xowa.Xowe_wiki;
import gplx.xowa.htmls.Xoh_page;
import gplx.xowa.htmls.hxtns.blobs.Hxtn_blob_tbl;
import gplx.xowa.htmls.hxtns.pages.Hxtn_page_mgr;

import gplx.xowa.Xoa_ttl;
import gplx.Hash_adp;
import gplx.String_;
import gplx.Ordered_hash;
import gplx.Ordered_hash_;
public class Pp_qualitytot_html_bldr implements gplx.core.brys.Bfr_arg {
	private int[] qualitycount = new int[6]; // 5 quality levels plus unknown
	Ordered_hash page_hash = Ordered_hash_.New_bry();
	private int qualitytot;
	public void Clear() {
		for (int i = 0; i < 6; i++)
			qualitycount[i] = 0;
		qualitytot = 0;
		page_hash.Clear();
	}

	public int Check_quality(Xoa_ttl ttl, Xowe_wiki wiki) {
		byte[] ttl_bry = ttl.Full_db();
		int quality = wiki.Quality().getQualityFromCatlink(ttl, wiki);
		if (page_hash.Has(ttl_bry)) return quality;
		//System.out.println(String_.new_u8(ttl.Full_db()));
		
		if (quality >= 0)
			qualitycount[quality]++;
		else
			qualitycount[5]++;

		qualitytot++;

		page_hash.Add(ttl_bry, quality);
		return quality;
	}
	public byte[] Generate_quality(Xowe_wiki wiki) {
		if (qualitytot == 0) return null;
		Bry_bfr bfr = Bry_bfr_.New();
		int q0 = qualitycount[0] * 100 / qualitytot;
		int q1 = qualitycount[1] * 100 / qualitytot;
		int q2 = qualitycount[2] * 100 / qualitytot;
		int q3 = qualitycount[3] * 100 / qualitytot;
		int q4 = qualitycount[4] * 100 / qualitytot;
		int qe = qualitycount[5] * 100 / qualitytot;
		byte[] textualAlternative = wiki.Msg_mgr().Val_by_key_args(Bry_.new_a7("proofreadpage-indexquality-alt"), qualitycount[4], qualitycount[3], qualitycount[1] );
		bfr.Add_str_a7("<table class=\"pr_quality noprint\" title=\"").Add(textualAlternative);
		bfr.Add_str_a7("\">\n<tr>\n<td class=\"quality4\" style=\"width:").Add_long_variable(q4);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality3\" style=\"width:").Add_long_variable(q3);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality2\" style=\"width:").Add_long_variable(q2);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality1\" style=\"width:").Add_long_variable(q1);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality0\" style=\"width:").Add_long_variable(q0);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"qualitye\" style=\"width:").Add_long_variable(qe);
		bfr.Add_str_a7("%;\"></td>\n");
		if (qe > 0)
			bfr.Add_str_a7("<td class=\"qualitye\" style=\"width:").Add_long_variable(qe).Add_str_a7("%;\"></td>\n");
		bfr.Add_str_a7("</tr>\n</table>\n");
		return bfr.To_bry();
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
	}

	public void HxtnSave(Xowe_wiki wiki, Hxtn_page_mgr hxtnPageMgr, Xoae_page page, int pageId) {
		// exit if empty
		if (qualitytot == 0) return;

		// serialize and save to db
		byte[] crumb = Pp_quality_serialCore.Save(qualitycount);
		hxtnPageMgr.Page_tbl__insert(pageId, Hxtn_page_mgr.Id__pp_pagequality, pageId);
		hxtnPageMgr.Blob_tbl__insert(Hxtn_blob_tbl.Blob_tid__wtxt, Hxtn_page_mgr.Id__pp_pagequality, pageId, crumb);
	}

	public void Deserialise(Xow_wiki wiki, Xoae_page wpg, Hash_adp props) {
		byte[] data = (byte[])props.Get_by(Pp_quality_hxtn_page_wkr.KEY);
		// exit if empty
		if (Bry_.Len_eq_0(data)) return;

		// deserialize data
		wpg.Html_data().Quality_tots().Set_quality(Pp_quality_serialCore.Load(data));
	}
	public void Set_quality(int[] qualitycount) {
		int len = qualitycount.length;
		if (len != 6) return; // sanity check
		qualitytot = 0;
		for (int i = 0; i < 6; i++)
			qualitytot += qualitycount[i];
		this.qualitycount = qualitycount;
	}
}
