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
/*
get page_ids for  Mediawiki:proofreadpage_quality0_category to proofreadpage_quality4_category
enwikisource
	
 Mediawiki:proofreadpage_quality0_category - Without text - 476128
 Mediawiki:proofreadpage_quality1_category - Not proofread - 143843
 Mediawiki:proofreadpage_quality2_category - Problematic - 143846
 Mediawiki:proofreadpage_quality3_category - Proofread - 143844
 Mediawiki:proofreadpage_quality4_category - Validated - 143845

*/
	private boolean initialised = false;
	private int qual0;
	private int qual1;
	private int qual2;
	private int qual3;
	private int qual4;
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
/*
		qual0 = getmedia("Mediawiki:proofreadpage_quality0_category")
		qual1 = getmedia("Mediawiki:proofreadpage_quality1_category")
		qual2 = getmedia("Mediawiki:proofreadpage_quality2_category")
		qual3 = getmedia("Mediawiki:proofreadpage_quality3_category")
		qual4 = getmedia("Mediawiki:proofreadpage_quality4_category")
*/
	qual0 = 476128;
	qual1 = 143843;
	qual2 = 143846;
	qual3 = 143844;
	qual4 = 143845;
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
		//int catqual = getsql("select cl_to_id from cat_link where cl_from=?", page_id);
		//int catqual = 143845;
		int catqual = loader.Run_ppq_id(page_id);
		return cvtqual(catqual);
	}

	public int getQualityFromCatlink(Xoa_ttl ttl, Xow_wiki wiki) {
		if (!initialised) {
			initialiseit(wiki);
		}
		//int catqual = getsql("select cl_to_id, cl_from from cat_link cl join page p on p.page_id=cl.cl_from where page_namespace=104 and page_title=?", ttl.Base_txt());
		//int catqual = 143845;
		int catqual = loader.Run_ppq_ttl(ttl);
		return cvtqual(catqual);
	}

	private int cvtqual(int catqual) {
			if (catqual == qual0)
				return Quality_without_text;
			else if (catqual == qual1)
				return Quality_not_proofread;
			else if (catqual == qual2)
				return Quality_problematic;
			else if (catqual == qual3)
				return Quality_proofread;
			else if (catqual == qual4)
				return Quality_validated;
			else
				return Quality_unknown;
	}
}
