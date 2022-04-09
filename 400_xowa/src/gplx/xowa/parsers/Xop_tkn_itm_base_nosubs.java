/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.parsers; import gplx.*; import gplx.xowa.*;
import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.htmls.core.htmls.*;
public abstract class Xop_tkn_itm_base_nosubs implements Xop_tkn_itm {
	@Override public abstract byte Tkn_tid();
	@Override public Xop_tkn_grp Tkn_grp() {return grp == null ? this : grp;} private Xop_tkn_grp grp;	// NOTE: not sure about this; need to handle null refs when tkns are manipulated but not yet added to a group
	@Override public Xop_tkn_itm Tkn_ini_pos(boolean immutable, int bgn, int end) {this.immutable = immutable; this.src_bgn = bgn; this.src_end = end; return this;}
	@Override public Xop_tkn_itm Tkn_grp_(Xop_tkn_grp grp, int sub_idx) {this.grp = grp; this.tkn_sub_idx = sub_idx; return this;}
	@Override @gplx.Virtual public Xop_tkn_itm Tkn_clone(Xop_ctx ctx, int bgn, int end) {throw Err_.new_wo_type("tkn_clone not implemented", "name", Xop_tkn_itm_.Tid__names[this.Tkn_tid()]);}
	@Override public boolean Tkn_immutable() {return immutable;} private boolean immutable;
	@Override public int Tkn_sub_idx() {return tkn_sub_idx;} private int tkn_sub_idx = -1;
	@Override public int Src_bgn() {return src_bgn;} private int src_bgn = -1;
	@Override public int Src_end() {return src_end;} private int src_end = -1;
	@Override public void Src_end_(int v) {src_end = v;}
	@Override public int Src_bgn_grp(Xop_tkn_grp grp, int sub_idx) {return immutable ? grp.Subs_src_bgn(sub_idx) : src_bgn;}
	@Override public int Src_end_grp(Xop_tkn_grp grp, int sub_idx) {return immutable ? grp.Subs_src_end(sub_idx) : src_end;}
	private void badcall(String func) {
            throw Err_.new_wo_type("should not be called here " + func);
        }
	@Override public int Subs_src_bgn(int sub_idx) {
            badcall("Subs_src_bgn");
                    return -1;
        }
	@Override public int Subs_src_end(int sub_idx) {
            badcall("Subs_src_end");
            return -1;
        }
        @Override public void Subs_src_pos_(int sub_idx, int bgn, int end) {
            badcall("Subs_src_pos_");
	}
	@Override public boolean Ignore() {return ignore;} private boolean ignore;
	@Override public Xop_tkn_itm Ignore_y_() {
		ignore = true;
		return this;
	}
	@Override public int Subs_len() {
            // badcall("Subs_len"); - acceptable
            return 0;
        }
	public Xop_tkn_itm[] Subs() {
            badcall("Subs");
            return null;
        }
	@Override public Xop_tkn_itm Subs_get(int i) {
            badcall("Subs_get");
            return null;
        }
	public Xop_tkn_itm Subs_get_or_null(int i) {
            badcall("Subs_get_or_null");
            return null;
        }
	@Override public void Subs_add(Xop_tkn_itm sub) {
            badcall("Subs_add");
	}
        
	@Override public void Subs_add_grp(Xop_tkn_itm sub, Xop_tkn_grp old_grp, int old_sub_idx) {
            badcall("Subs_add_grp");
	}
	@Override public void Subs_del_after(int tkn_sub_idx) {
            badcall("Subs_del_after");
	}
	public void Subs_del_between(Xop_ctx ctx, int idx_bgn, int idx_end) {
            badcall("Subs_del_between");
	}
	@Override public void Subs_clear() {
            badcall("Subs_clear");
	}
	@Override public void Subs_move(Xop_tkn_itm tkn) {
            badcall("Subs_move tkn");
	}
	public void Subs_move(Xop_tkn_itm owner, int sub_idx, int subs_len) {
            badcall("Subs_move owner");
	}
	@Override public Xop_tkn_itm Immutable_clone(Xop_ctx ctx, Xop_tkn_itm tkn, int sub_idx) {
            badcall("Immutable_clone");
            return null;
	}
	@Override public void Src_end_grp_(Xop_ctx ctx, Xop_tkn_grp grp, int sub_idx, int src_end) {
            badcall("Src_end_grp_");
	}
	@Override public void Ignore_y_grp_(Xop_ctx ctx, Xop_tkn_grp grp, int sub_idx) {
		Xop_tkn_itm tkn = this;
		if (immutable) tkn = grp.Immutable_clone(ctx, this, sub_idx);
		tkn.Ignore_y_();
	}
	public void Subs_grp_(Xop_ctx ctx, Xop_tkn_itm tkn, Xop_tkn_grp grp, int sub_idx) {
//			if (tkn.Tkn_immutable()) tkn = Subs_immutable_clone(ctx, tkn);
//			tkn.Tkn_grp_(grp, sub_idx);
	}
        public void Subs_ignore_whitespace() {
            /*
            Xop_tkn_itm sub;
            while (subs_len >= 0) {
                sub = subs[subs_len - 1];
                    if (sub instanceof Xop_space_tkn || sub instanceof Xop_para_tkn) {
			subs[subs_len - 1] = null;
                        subs_len--;
                    }
                    else
                        break;
            }*/
        }
	@gplx.Virtual public void Reset() {
		src_bgn = src_end = tkn_sub_idx = -1; ignore = false;  tmpl_static = false;
	}
	@Override @gplx.Virtual public void Html__write(Bry_bfr bfr, Xoh_html_wtr wtr, Xowe_wiki wiki, Xoae_page page, Xop_ctx ctx, Xoh_wtr_ctx hctx, Xoh_html_wtr_cfg cfg, Xop_tkn_grp grp, int sub_idx, byte[] src) {throw Err_.new_unimplemented(90);}
	@Override public void Clear() {
		src_bgn = src_end = tkn_sub_idx = -1; ignore = false;  tmpl_static = false;
	}
	@Override @gplx.Virtual public void Tmpl_fmt(Xop_ctx ctx, byte[] src, Xot_fmtr fmtr) {
            badcall("Tmpl_fmt");
        }
	@Override @gplx.Virtual public void Tmpl_compile(Xop_ctx ctx, byte[] src, Xot_compile_data prep_data) {
		if (!ignore) tmpl_static = true;
            //badcall("Tmpl_compile"); // not a bad call - just ignore
	}
        boolean tmpl_static = false;
	@Override @gplx.Virtual public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Bry_bfr bfr) {
		if (tmpl_static) bfr.Add_mid(src, src_bgn, src_end);
            //badcall("Tmpl_evaluate"); // not a bad call
		return true;
	}
}
