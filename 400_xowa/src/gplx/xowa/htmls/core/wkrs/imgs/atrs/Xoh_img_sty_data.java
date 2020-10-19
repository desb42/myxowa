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
package gplx.xowa.htmls.core.wkrs.imgs.atrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*; import gplx.xowa.htmls.core.wkrs.imgs.*;
import gplx.core.brys.*; import gplx.core.btries.*;
import gplx.langs.htmls.*; import gplx.langs.htmls.docs.*;
public class Xoh_img_sty_data implements Bfr_arg_clearable {
	private final    Btrie_rv trv = new Btrie_rv();
	private byte[] src;
	private byte[] style;
	public void Clear() {
		style = null;
		src = null;
	}
	public void Init_by_decode(byte[] src, byte[] style) { // cannot happen!
		this.src = src;
		this.style = style;
	}
	public void Init_by_parse(Bry_err_wkr err_wkr, byte[] src, Gfh_tag tag) {
		this.Clear();
		Gfh_atr atr = tag.Atrs__get_by_or_empty(Gfh_atr_.Bry__style);		// EX: style="align-vertical: top"
		int src_bgn = atr.Val_bgn();
		if (src_bgn == -1) {	// style does not exist; defaults to none and exit;
			style = null;
		}
		else {
			int src_end = atr.Val_end();
			style = Bry_.Mid(src, src_bgn, src_end);
		}
	}
	public void Bfr_arg__clear()	{this.Clear();}
	public boolean Bfr_arg__missing()	{return style == null;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (Bfr_arg__missing()) return;
		if (style != null)
			bfr.Add(style);
	}
}
