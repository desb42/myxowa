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
package gplx.xowa.bldrs.css; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.xowa.files.downloads.*;
class Xob_css_parser__comment {
	public Xob_css_tkn__base Parse(byte[] src, int src_len, int tkn_bgn, int tkn_end) {	// comment '/*' .... */
		int end_pos = Bry_find_.Find_fwd(src, Tkn_end_comment, tkn_bgn);	// skip to end of comment
		if (end_pos == Bry_find_.Not_found)
			end_pos = src_len;
		else
			end_pos += Tkn_end_comment.length;
		return Xob_css_tkn__comment.new_(tkn_bgn, end_pos);
	}
	private static final byte[] Tkn_end_comment = Bry_.new_a7("*/");
}
