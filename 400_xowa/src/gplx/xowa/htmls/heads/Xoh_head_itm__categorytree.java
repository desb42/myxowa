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
package gplx.xowa.htmls.heads; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*;
public class Xoh_head_itm__categorytree extends Xoh_head_itm__base {
	@Override public byte[] Key() {return Xoh_head_itm_.Key__categorytree;}
	@Override public int Flags() {return Flag__js_head_global;}
	@Override public void Write_js_head_global(Xoae_app app, Xowe_wiki wiki, Xoae_page page, Xoh_head_wtr wtr) {
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_collapse);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_expand);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_collapse_expand);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_expand_bullet);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_load);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_loading);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_nothing_found);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_no_subs);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_no_parent);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_no_pages);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_error);
		wtr.Write_js_global_ini_atr_msg(wiki			, Key_retry);
		wtr.Write_js_global_ini_atr_obj(Key_wgat		, Bry_.new_a7("{\"mode\":0,\"hideprefix\":20,\"showcount\":true,\"namespaces\":false}"));
	}
	private static final    byte[]
	  Key_collapse = Bry_.new_a7("categorytree-collapse")
	, Key_expand = Bry_.new_a7("categorytree-expand")
	, Key_collapse_expand = Bry_.new_a7("categorytree-collapse-bullet")
	, Key_expand_bullet = Bry_.new_a7("categorytree-expand-bullet")
	, Key_load = Bry_.new_a7("categorytree-load")
	, Key_loading = Bry_.new_a7("categorytree-loading")
	, Key_nothing_found = Bry_.new_a7("categorytree-nothing-found")
	, Key_no_subs = Bry_.new_a7("categorytree-no-subcategories")
	, Key_no_parent = Bry_.new_a7("categorytree-no-parent-categories")
	, Key_no_pages = Bry_.new_a7("categorytree-no-pages")
	, Key_error = Bry_.new_a7("categorytree-error")
	, Key_retry = Bry_.new_a7("categorytree-retry")
	, Key_wgat = Bry_.new_a7("wgCategoryTreePageCategoryOptions")
	;
}
