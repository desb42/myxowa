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
package gplx.xowa.parsers.magics;
import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.core.net.*; import gplx.xowa.apps.urls.*;
import gplx.xowa.apps.progs.*; import gplx.xowa.wikis.xwikis.*;	
public class Xop_magic_wkr implements Xop_ctx_wkr {
	public void Ctor_ctx(Xop_ctx ctx) { }
	public void Page_bgn(Xop_ctx ctx, Xop_root_tkn root) {}
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {}
	public int MakeTkn_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, byte[] magic, byte magic_tid) {
		if (!Xop_parser_.Valid_word_break(ctx, src, src_len, bgn_pos, cur_pos)	// tkn is part of work; EX: " ttl:" vs "attl:"
			)
			return ctx.Lxr_make_txt_(cur_pos - 1);						// -1 to ignore ":" in making text colon; needed to process ":" for list like "; attl: b" PAGE:de.w:Mord_(Deutschland)#Besonders_verwerfliche_Begehungsweise; DATE:2015-01-09
		// can only get here if a number has been found
		while (cur_pos < src_len) {
			byte b = src[cur_pos];
			if ((b >= '0' && b <= '9') || b == '-' || b == 'x' || b == 'X')
				cur_pos++;
			else
				break;
		}
		Xop_magic_tkn tkn = tkn_mkr.Magic(bgn_pos, cur_pos, magic_tid);
		ctx.Subs_add(root, tkn);
		return cur_pos;
	}
}
