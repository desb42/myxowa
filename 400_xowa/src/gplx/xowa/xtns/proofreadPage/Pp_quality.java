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
package gplx.xowa.xtns.proofreadPage;
import gplx.xowa.*; import gplx.*; import gplx.dbs.*; import gplx.xowa.wikis.dbs.*; import gplx.dbs.qrys.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.dbs.*;
import gplx.xowa.wikis.data.*; import gplx.xowa.wikis.data.tbls.*; import gplx.xowa.addons.wikis.ctgs.dbs.*;
import gplx.xowa.wikis.data.*;
public class Pp_quality {
	private boolean initialised = false;
        public boolean Not_colouring() { return !colouring; } private boolean colouring;
	
	public static final int
	  Quality_unknown = -1
	, Quality_without_text = 0
	, Quality_not_proofread = 1
	, Quality_problematic = 2
	, Quality_proofread = 3
	, Quality_validated = 4
	;

        public Pp_quality(boolean iswikisource) {
            colouring = iswikisource;
        }
	public void initialiseit(Xow_wiki wiki) {
            Init(wiki);
			initialised = true;
	}

	private Xoctg_catlink_loader_ppq loader;
	private void Init(Xow_wiki wiki) {
		// get cat_id from page_tbl
		Xow_db_mgr db_mgr = wiki.Data__core_mgr();
		Xowd_page_tbl page_tbl = db_mgr.Db__core().Tbl__page();
		// get cat_core_itm from cat_core_tbl
		Xowd_cat_core_tbl cat_core_tbl = Xodb_cat_db_.Get_cat_core_or_fail(db_mgr);

		loader = Xoctg_catlink_loader_ppq.Create(wiki, page_tbl, db_mgr, cat_core_tbl.Conn());
	}
	public int getQualityFromCatlink(int page_id, Xow_wiki wiki) {
		if (!initialised) {
			initialiseit(wiki);
		}
		return loader.Run_ppq_id(page_id);
	}

	public int getQualityFromCatlink(Xoa_ttl ttl, Xow_wiki wiki) {
		if (!initialised) {
			initialiseit(wiki);
		}
		return loader.Run_ppq_ttl(ttl);
	}
}
