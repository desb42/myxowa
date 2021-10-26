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
package gplx.xowa.addons.wikis.ctgs.htmls.catpages.fmts; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.*;
import gplx.xowa.htmls.core.htmls.*; import gplx.xowa.htmls.hrefs.*;
import gplx.xowa.langs.msgs.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms.*;
import gplx.xowa.users.history.*;

import gplx.xowa.mediawiki.includes.*; import gplx.xowa.xtns.categorytrees.*;

class Xoctg_fmt_itm_subc extends Xoctg_fmt_itm_base {
	@Override public void Bld_html(Bry_bfr bfr, Xow_wiki wiki, Xou_history_mgr history_mgr, Xoh_href_parser href_parser, Xoctg_catpage_itm itm, Xoa_ttl ttl) {
            Categorytree_params_ params = new Categorytree_params_();
            params.Showcount_(true); // make sure the counts are visible
            wiki.Ctg__catpage_mgr().Bld_cat_itm(bfr, wiki, itm.Count_subcs(), itm.Count_pages(), itm.Count_files(), ttl, params, true, Bry_.Empty, Bry_.Empty);
	}
}
