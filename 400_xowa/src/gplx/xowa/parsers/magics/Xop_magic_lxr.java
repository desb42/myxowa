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
import gplx.core.btries.*; import gplx.xowa.langs.*;
import gplx.core.magic.*;
public class Xop_magic_lxr implements Xop_lxr {
	Xop_magic_lxr(byte[] magic, byte tid) {
		this.magic = magic; this.tid = tid;
	}
	private byte[] magic; 
	private byte tid;
	public int Lxr_tid() {return Xop_lxr_.Tid_magic;}
	public void Init_by_wiki(Xowe_wiki wiki, Btrie_fast_mgr core_trie) {
		Gfo_magic_itm[] ary = Gfo_magic_itm.Ary();
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Gfo_magic_itm itm = ary[i];
			Ctor_lxr_add(core_trie, itm.Text_bry(), itm.Tid());
		}
	}
	public void Init_by_lang(Xol_lang_itm lang, Btrie_fast_mgr core_trie) {}
	public void Term(Btrie_fast_mgr core_trie) {}
	private void Ctor_lxr_add(Btrie_fast_mgr core_trie, byte[] magic_bry, byte tid) {
		core_trie.Add(magic_bry, new Xop_magic_lxr(magic_bry, tid));
//??		core_trie.Add(Bry_.Add(Byte_ascii.Brack_bgn, magic_bry), new Xop_magic_lxr(Xop_magic_tkn.magic_typ_brack, magic_bry, tid));
	}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		byte b = src[cur_pos];
		// must be followed by a Number to be valid
		if (b < '0' || b > '9') return ctx.Lxr_make_txt_(cur_pos);
		return ctx.Magic().MakeTkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, magic, tid);
	}
	public static final Xop_magic_lxr Instance = new Xop_magic_lxr(); Xop_magic_lxr() {}
}
