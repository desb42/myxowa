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
package gplx.xowa.htmls.core.wkrs.hdrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
import gplx.core.brys.*; import gplx.core.primitives.*; import gplx.core.brys.fmtrs.*; import gplx.core.threads.poolables.*; import gplx.core.brys.args.*;
import gplx.langs.htmls.*; import gplx.xowa.htmls.core.wkrs.bfr_args.*;
import gplx.xowa.htmls.sections.*; import gplx.xowa.htmls.core.hzips.*;
public class Xoh_hdr_wtr implements gplx.core.brys.Bfr_arg, Xoh_wtr_itm {
	private int hdr_num; private byte[] hdr_id, hdr_content, hdr_capt_rhs;
        private byte[] atrs;
	private Xoh_page hpg;
	public boolean Init_by_parse(Bry_bfr bfr, Xoh_page hpg, Xoh_hdoc_ctx hctx, byte[] src, Xoh_hdr_data data) {
		if (!Init_by_decode(hpg, hctx, src, data)) return false;
		this.Bfr_arg__add(bfr);
		return true;
	}
	public boolean Init_by_decode(Xoh_page hpg, Xoh_hdoc_ctx hctx, byte[] src, Xoh_data_itm data_itm) {
		this.hpg = hpg;
		Xoh_hdr_data data = (Xoh_hdr_data)data_itm;			
		this.hdr_num = data.Hdr_level();
                this.atrs = data.Atrs();
		this.hdr_content = Bry_.Mid(src, data.Capt_bgn(), data.Capt_end());
                Checkanchor();
		if (data.Anch_is_diff())
			hdr_id = Bry_.Mid(src, data.Anch_bgn(), data.Anch_end());
		else
			hdr_id = Bry_.Replace(hdr_content, Byte_ascii.Space, Byte_ascii.Underline);
		hdr_capt_rhs  = data.Capt_rhs_exists() ? Bry_.Mid(src, data.Capt_rhs_bgn(), data.Capt_rhs_end()) : Bry_.Empty;
		hpg.Html_data().Toc_mgr().Add(hdr_num, hdr_content);
		return true;
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		Xoh_section_mgr section_mgr = hpg.Section_mgr();
		int section_len = section_mgr.Len();
		if (section_len != 0)	// guard against -1 index; should not happen
			section_mgr.Set_content(section_len - 1, bfr.Bfr(), bfr.Len() - 1);	// close previous section; -1 to skip "\n" before "<h2>"
		fmtr.Bld_bfr_many(bfr, hdr_num, atrs, hdr_id, hdr_content, hdr_capt_rhs);
		section_mgr.Add(section_len, hdr_num, hdr_id, hdr_content).Content_bgn_(bfr.Len() + 1); // +1 to skip "\n" after "</h2>"
	}
	public void				Pool__rls	() {pool_mgr.Rls_fast(pool_idx);} private Gfo_poolable_mgr pool_mgr; private int pool_idx;
	public Gfo_poolable_itm	Pool__make	(Gfo_poolable_mgr mgr, int idx, Object[] args) {Xoh_hdr_wtr rv = new Xoh_hdr_wtr(); rv.pool_mgr = mgr; rv.pool_idx = idx; return rv;}
	private static final    Bry_fmtr fmtr = Bry_fmtr.new_
	( "<h~{lvl}~{atrs}><span class=\"mw-headline\" id=\"~{id}\">~{content}</span>~{capt_rhs}</h~{lvl}>"
	, "lvl", "atrs", "id", "content", "capt_rhs");
        private void Checkanchor() {
            int len = hdr_content.length - 5;
            for (int i = 0; i < len; i++) {
                if (hdr_content[i] == '<' && hdr_content[i+1] == 'a' && hdr_content[i+2] == ' ' && hdr_content[i+3] == 'h') {
                    Gfo_usr_dlg_.Instance.Log_many("", "", "header with anchr: ttl=~{0}", hpg.Ttl());
                    return;
                }
            }
        }
}
