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
import gplx.xowa.*;
class Pp_quality {
/*
get page_ids for  Mediawiki:proofreadpage_quality0_category to proofreadpage_quality4_category
enwikisource
	
 0 - Without text	476128
 1 - Not proofread   143843
 2 - Problematic	 143846
 3 - Proofread	   143844
 4 - Validated	   143845

*/
	private static boolean initialised = false;
	private static int qual0 = 476128;
	private static int qual1 = 143843;
	private static int qual2 = 143846;
	private static int qual3 = 143844;
	private static int qual4 = 143845;
	
	public static final int
	  Quality_unknown = -1
	, Quality_without_text = 0
	, Quality_not_proofread = 1
	, Quality_problematic = 2
	, Quality_proofread = 3
	, Quality_validated = 4
	;

	public static void initialiseit() {
/*
		qual0 = getmedia("Mediawiki:proofreadpage_quality0_category")
		qual1 = getmedia("Mediawiki:proofreadpage_quality1_category")
		qual2 = getmedia("Mediawiki:proofreadpage_quality2_category")
		qual3 = getmedia("Mediawiki:proofreadpage_quality3_category")
		qual4 = getmedia("Mediawiki:proofreadpage_quality4_category")
*/
	}

	public static int getQualityFromCatlink(int page_id) {
		if (!initialised) {
			initialiseit();
			initialised = true;
		}
		//int catqual = getsql("select cl_to_id from cat_link where cl_from=?", page_id);
		int catqual = 143845;
		return cvtqual(catqual);
	}

	public static int getQualityFromCatlink(Xoa_ttl ttl) {
		if (!initialised) {
			initialiseit();
			initialised = true;
		}
		//int catqual = getsql("select cl_to_id, cl_from from cat_link cl join page p on p.page_id=cl.cl_from where page_namespace=104 and page_title=?", ttl.Base_txt());
		int catqual = 143845;
		return cvtqual(catqual);
	}

	private static int cvtqual(int catqual) {
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
