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
package gplx.xowa.htmls.heads; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*;
import gplx.xowa.guis.*;
import gplx.xowa.bldrs.css.Xoa_css_extractor;
public class Xoh_head_itm__common_styles extends Xoh_head_itm__base {
	@Override public byte[] Key() {return Xoh_head_itm_.Key__common_styles;}
	@Override public int Flags() {return Flag__css_include;}
	@Override public void Write_css_include(Xoae_app app, Xowe_wiki wiki, Xoae_page page, Xoh_head_wtr wtr) {
		// do not cache
		byte[] common_css;
		byte[] wiki_css;
		common_css = app.Fsys_mgr().Wiki_css_dir(wiki.Domain_str()).GenSubFil(Xoa_css_extractor.Css_common_name).To_http_file_bry();
		wiki_css = app.Fsys_mgr().Wiki_css_dir(wiki.Domain_str()).GenSubFil(Xoa_css_extractor.Css_wiki_name).To_http_file_bry();

		wtr.Write_css_include(common_css);
		wtr.Write_css_include(wiki_css);
	}
}
