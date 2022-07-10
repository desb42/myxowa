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
package gplx.xowa.addons.wikis.hdump;

import gplx.*;
import gplx.core.brys.Int_flag_bldr;
import gplx.xowa.htmls.hxtns.pages.Hxtn_page_mgr;
import gplx.xowa.htmls.hxtns.blobs.Hxtn_blob_tbl;
import gplx.xowa.htmls.Xoh_page;
public class Hxtn_hdump {
	public static void HxtnSave(Hxtn_page_mgr html_data_mgr, int page_id
	    , int head_flag, int body_flag, byte[] display_ttl, byte[] sidebar_div, byte[] content_sub) {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		if (head_flag > 0) {
			tmp_bfr.Add_byte((byte)1).Add(encode(head_flag));
		}
		if (body_flag != 12) {
			tmp_bfr.Add_byte((byte)2).Add(encode(body_flag));
		}
		if (!Bry_.Len_eq_0(display_ttl)) {
			tmp_bfr.Add_byte((byte)3).Add(display_ttl);
		}
		if (!Bry_.Len_eq_0(content_sub)) {
			tmp_bfr.Add_byte((byte)4).Add(content_sub);
		}
		if (!Bry_.Len_eq_0(sidebar_div)) {
			tmp_bfr.Add_byte((byte)5).Add(sidebar_div);
		}
		if (tmp_bfr.Len() > 0) {
			html_data_mgr.Page_tbl__insert(page_id, Hxtn_page_mgr.Id__hdump, page_id);
			html_data_mgr.Blob_tbl__insert(Hxtn_blob_tbl.Blob_tid__wtxt, Hxtn_page_mgr.Id__hdump, page_id, tmp_bfr.To_bry());
		}
	}
	private static byte[] encode(int val) {
		return new byte[]{ (byte)val };
	}

	public static void Deserialise(Xoh_page hpg, Hash_adp props) {
		int head_flag = 0, body_flag = 12;
		Int_flag_bldr body_flag_bldr = new Int_flag_bldr().Pow_ary_bld_(3, 2);
		byte[] display_ttl = Bry_.Empty, content_sub = Bry_.Empty, sidebar_div = Bry_.Empty;
		byte[] data = (byte[])props.Get_by(Hdump_hxtn_page_wkr.KEY);
		if (data != null) {
			int len = data.length;
			int pos = 0;
			while (pos < len) {
				byte b = data[pos++];
				switch (b) {
				case 1:
					head_flag = data[pos++];
					break;
				case 2:
					body_flag = data[pos++];
					break;
				case 3:
					display_ttl = readbytes(data, pos, len);
					pos += display_ttl.length;
					break;
				case 4:
					content_sub = readbytes(data, pos, len);
					pos += content_sub.length;
					break;
				case 5:
					sidebar_div = readbytes(data, pos, len);
					pos += sidebar_div.length;
					break;
				default:
					// error!!
				}
			}
		}
		body_flag_bldr.Decode(body_flag);
		hpg.Ctor_by_db(head_flag, display_ttl, content_sub, sidebar_div, body_flag_bldr.Get_as_int(0), body_flag_bldr.Get_as_int(1));
	}
	private static byte[] readbytes(byte[] data, int pos, int len) {
		int start = pos;
		while (pos < len) {
			byte b = data[pos++];
			if (b <= 9)
				break;
		}
		return Bry_.Mid(data, start, pos);
	}
}