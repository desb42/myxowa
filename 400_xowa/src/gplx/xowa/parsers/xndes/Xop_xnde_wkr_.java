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
package gplx.xowa.parsers.xndes; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.core.primitives.*; import gplx.core.btries.*;
import gplx.xowa.xtns.pfuncs.strings.*;
class Xop_xnde_wkr_ {
	public static void AutoClose_handle_dangling_nde_in_caption(Xop_root_tkn root, Xop_tkn_itm owner) {
		int subs_bgn = -1, subs_len = owner.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub_itm = owner.Subs_get(i);
			if (sub_itm.Tkn_tid() == Xop_tkn_itm_.Tid_pipe) {	// tkn is "|"; assume that caption should end here
				subs_bgn = i;
				break;
			}
		}
		if (subs_bgn != -1) 
			root.Subs_move(owner, subs_bgn, subs_len);			// move everything after "|" back to root
	}
	public static int Find_xtn_end(Xop_ctx ctx, byte[] src, int open_end, int src_end, byte[] open_bry, byte[] close_bry) {
	//public static int Find_xtn_end(Xop_ctx ctx, byte[] src, int open_end, int src_end, byte[] close_bry) {
		int depth = 0;
		int lastclose = -1;
		int close_end = close_bry.length;
		int last_pos = src_end - close_end; // no point in searching after this pos
		int i = open_end, j;
		boolean closer;
		int closepos;
		while (i < last_pos) {
			byte b = src[i];
			if (b != Byte_ascii.Angle_bgn) {
				i++;
				continue;
			}
			closepos = i;
			i++;
			b = src[i];
			if (b == Byte_ascii.Slash) {// must be a close
				closer = true;
				i++;
			}
			else
				closer = false;
                        // allow for different case
			for (j = 2; j < close_end; j++) {
                            byte c = close_bry[j];
                            byte s = src[i];
				if (c != s && (c | 32) != (s | 32))
					break;
				else
					i++;
			}
			if (j == close_end) {
				if (closer) {
                                    // skip whitespace
                                    byte s = src[i];
                                    while (i < last_pos) {
                                        if (s == ' ' || s == '\n' || s == '\t') {
                                            i++;
                                            s = src[i];
                                        }
                                        else
                                            break;
                                    }
					if (s != Byte_ascii.Angle_end)
						continue; // not what we are looking for
					i++;
					if (depth == 0)
						return closepos;
					else {
						--depth;
						lastclose = closepos;
					}
				}
				else { // must be opener
					// forward for close angle
					// strictly - could be a prefix of another element
					b = src[i];
					if (b == Byte_ascii.Angle_end)
						i++;
					else {
						if (b != ' ' && b != '\t' && b != '\n') // strictly whitespace
							continue; // not what we ar looking for
						while (i < src_end) { // NB whole of src
							if (src[i++] == Byte_ascii.Angle_end)
								break;
						}
					}
					if (i == src_end) { // not found at all
						break;
					}
					if (src[i - 2] == Byte_ascii.Slash) {}
					else
						++depth;
				}
			}
		}
		if (lastclose > 0)
			return lastclose; // at least there was a close somewhere
                //if (ctx.Parse_tid() == Xop_parser_tid_.Tid__tmpl)
                //    return src_end;
                int extra_bytes = open_end + 30;
                if (extra_bytes > src_end)
                    extra_bytes = src_end;
		Xoa_app_.Usr_dlg().Warn_many("", "", "parser.xtn: could not find angle_end: page=~{0} close_bry=~{1} excerpt=~{2}", ctx.Page().Url().To_str(), close_bry, String_.new_u8(src, open_end, extra_bytes));
		//Xoa_app_.Usr_dlg().Warn_many("", "", "parser.xtn: could not find angle_end: page=~{0} close_bry=~{1}", ctx.Page().Url().To_str(), close_bry);
                //Gfo_usr_dlg_.Instance.Log_many("", "", "mid_bry=~{0}", String_.new_u8(src));
		return Bry_find_.Not_found;
	}
	private static final int Find_xtn_end__tid__bgn = 0, Find_xtn_end__tid__end = 1;//, Find_xtn_end__tid__xtag = 2;
	private static final	Int_obj_val 
	  Find_xtn_end__key__bgn  = new Int_obj_val(Find_xtn_end__tid__bgn)
	, Find_xtn_end__key__end  = new Int_obj_val(Find_xtn_end__tid__end)
	;
}
