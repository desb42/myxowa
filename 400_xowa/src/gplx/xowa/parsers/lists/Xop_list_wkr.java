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
package gplx.xowa.parsers.lists; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.xowa.parsers.tblws.*; import gplx.xowa.parsers.xndes.*;
public class Xop_list_wkr implements Xop_ctx_wkr {
	public void Ctor_ctx(Xop_ctx ctx) {}
	public void Page_bgn(Xop_ctx ctx, Xop_root_tkn root) {}
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {}
	public boolean List_dirty() {throw Err_.new_unimplemented();}
	public boolean Dd_chk() {return dd_chk;} public Xop_list_wkr Dd_chk_(boolean v) {dd_chk = v; return this;} private boolean dd_chk;
	public void AutoClose(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, Xop_tkn_itm tkn) {}
	public int MakeTkn_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {// REF.MW: Parser|doBlockLevels
		if (bgn_pos == Xop_parser_.Doc_bgn_bos) bgn_pos = 0;	// do not allow -1 pos

		// pop hdr if exists; EX: \n== a ==\n*b; \n* needs to close hdr
		int acsPos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_hdr);
		if (acsPos != -1) ctx.Stack_pop_til(root, src, acsPos, true, bgn_pos, cur_pos, Xop_tkn_itm_.Tid_list);

		// close apos
		ctx.Apos().End_frame(ctx, root, src, bgn_pos, false);

		// Multiple prefixes may abut each other for nested lists.
		while (cur_pos < src_len) {
			byte b = src[cur_pos];
			if (b == Byte_ascii.Star || b == Byte_ascii.Hash || b == Byte_ascii.Semic || b == Byte_ascii.Colon) {
                            cur_pos++;
                        }
			else
				break;
		}
		//Xop_list_tkn_new itm = tkn_mkr.List_bgn(bgn_pos, cur_pos, curSymAry[curSymLen - 1], curSymLen).List_path_(posBldr.XtoIntAry()).List_uid_(listId);
		// bgn_pos + 1 skips the nl char
		Xop_list_tkn_new itm = new Xop_list_tkn_new(bgn_pos + 1, cur_pos, ctx.Page().Prev_list_tkn());
		ctx.Subs_add_and_stack(root, itm);
		ctx.Page().Prev_list_tkn_(itm);
		return cur_pos;
	}
}
