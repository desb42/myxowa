/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2021 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.scribunto.libs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.scribunto.*;
import gplx.xowa.xtns.scribunto.procs.*;
import gplx.xowa.xtns.proofreadPage.Pp_quality;
import gplx.xowa.wikis.caches.Xow_page_cache_itm;
public class Scrib_lib_proofread implements Scrib_lib { // REF.MW:https://github.com/wikimedia/????
	private Scrib_core core;
	public Scrib_lib_proofread(Scrib_core core) {
		this.core = core;
	}
	public String Key() {return "mw.ext.proofreadPage";}
	public Scrib_lua_mod Mod() {return mod;} private Scrib_lua_mod mod;
	public Scrib_lib Init() {
		procs.Init_by_lib(this, Proc__names);
		return this;
	}
	public Scrib_lib Clone_lib(Scrib_core core) {return new Scrib_lib_proofread(core);}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		//mod = core.RegisterInterface(this, "ProofreadPage.lua", core.Core_mgr().Get_text(script_dir, "ProofreadPage.lua")
		mod = core.RegisterInterface(this, "ProofreadPage.lua", core.Fsys_mgr().Get_or_null("ProofreadPage")
			, Keyval_.new_("NS_INDEX", core.Wiki().Ns_mgr().Ns_index_id())
			, Keyval_.new_("NS_PAGE", core.Wiki().Ns_mgr().Ns_page_id())

			, Keyval_.new_("qualityLevel", Keyval_.Ary(
				  Keyval_.new_("WITHOUT_TEXT", Pp_quality.Quality_without_text)
				, Keyval_.new_("NOT_PROOFREAD", Pp_quality.Quality_not_proofread)
				, Keyval_.new_("PROBLEMATIC", Pp_quality.Quality_problematic)
				, Keyval_.new_("PROOFREAD", Pp_quality.Quality_proofread)
				, Keyval_.new_("VALIDATED", Pp_quality.Quality_validated))
				)

			);
		return mod;
	}
	public Scrib_proc_mgr Procs() {return procs;} private Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc__doGetIndexProgress:                           return DoGetIndexProgress(args, rslt);
			case Proc__doGetIndexFields:                             return DoGetIndexFields(args, rslt);
			case Proc__doGetIndexCategories:                         return DoGetIndexCategories(args, rslt);
			case Proc__doGetNumberOfPages:                           return DoGetNumberOfPages(args, rslt);
			case Proc__doGetPageInIndex:                             return DoGetPageInIndex(args, rslt);
			case Proc__doGetIndexPageNumbers:                        return DoGetIndexPageNumbers(args, rslt);
			case Proc__doGetPageQuality:                             return DoGetPageQuality(args, rslt);
			case Proc__doGetIndexForPage:                            return DoGetIndexForPage(args, rslt);
			case Proc__doGetPageNumbering:                           return DoGetPageNumbering(args, rslt);
			default: throw Err_.new_unhandled(key);
		}
	}
	/**
	 * Return the index statistics for the given index name
	 *
	 * This function may be expensive, if the index has not been cached yet.
	 *
	 * @param string $indexName the index title to get stats for
	 * @return array the result table, in an array
	 * @throws Scribunto_LuaError if expensive function count exceeded
	 */
/*
	public function doGetIndexProgress( string $indexName ): array {
		$indexTitle = Title::makeTitleSafe( $this->context->getIndexNamespaceId(), $indexName );
		$statsLookup = $this->context->getIndexQualityStatsLookup();

		if ( !$statsLookup->isIndexTitleInCache( $indexTitle ) ) {
			$this->logger->debug( "Index stats cache miss: " . $indexTitle->getFullText() );
			$this->incrementExpensiveFunctionCount();
		}

		$indexStats = $statsLookup->getStatsForIndexTitle( $indexTitle );

		// Map stats to the Lua table
		$stats = [
			0 => $indexStats->getNumberOfPagesForQualityLevel( 0 ),
			1 => $indexStats->getNumberOfPagesForQualityLevel( 1 ),
			2 => $indexStats->getNumberOfPagesForQualityLevel( 2 ),
			3 => $indexStats->getNumberOfPagesForQualityLevel( 3 ),
			4 => $indexStats->getNumberOfPagesForQualityLevel( 4 ),
			"total" => $indexStats->getNumberOfPages(),
			"existing" => $indexStats->getNumberOfPagesWithAnyQualityLevel(),
			"missing" => $indexStats->getNumberOfPagesWithoutQualityLevel(),
		];

		return [ $stats ];
	}
*/
	private boolean DoGetIndexProgress(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Xoa_ttl ttl = Xoa_ttl.Parse(core.Wiki(), Bry_.Add(Bry_.new_a7("Index:"), ttl_bry));
		Db_index_stats index_stats = Db_index_stats.Null;
		if (ttl != Xoa_ttl.Null) {
			Xow_page_cache_itm cache_itm = core.Wiki().Cache_mgr().Page_cache().Get_itm_else_load_or_null(ttl, core.Wiki().Domain_str());
			if (cache_itm != null)
				index_stats = core.Wiki().Pr_index().getStatsForIndexId(cache_itm.Page_id());
		}
		Keyval[] rv = new Keyval[8];
		rv[ 0] = Keyval_.int_(0, index_stats.getNumberOfPagesForQualityLevel( 0 ));
		rv[ 1] = Keyval_.int_(1, index_stats.getNumberOfPagesForQualityLevel( 1 ));
		rv[ 2] = Keyval_.int_(2, index_stats.getNumberOfPagesForQualityLevel( 2 ));
		rv[ 3] = Keyval_.int_(3, index_stats.getNumberOfPagesForQualityLevel( 3 ));
		rv[ 4] = Keyval_.int_(4, index_stats.getNumberOfPagesForQualityLevel( 4 ));

		rv[ 5] = Keyval_.new_("total", index_stats.getNumberOfPages());
		rv[ 6] = Keyval_.new_("existing", index_stats.getNumberOfPagesWithAnyQualityLevel());
		rv[ 7] = Keyval_.new_("missing", index_stats.getNumberOfPagesWithoutQualityLevel());
		return rslt.Init_obj(rv);
	}
	private boolean DoGetIndexFields(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}
	private boolean DoGetIndexCategories(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}
	private boolean DoGetNumberOfPages(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Xoa_ttl ttl = Xoa_ttl.Parse(core.Wiki(), Bry_.Add(Bry_.new_a7("Index:"), ttl_bry));
		int numberofpages = 0;
		if (ttl != Xoa_ttl.Null) {
			Xow_page_cache_itm cache_itm = core.Wiki().Cache_mgr().Page_cache().Get_itm_else_load_or_null(ttl, core.Wiki().Domain_str());
			if (cache_itm != null) {
				Db_index_stats index_stats = core.Wiki().Pr_index().getStatsForIndexId(cache_itm.Page_id());
                                numberofpages = index_stats.getNumberOfPages();
                        }
                }
		return rslt.Init_obj(numberofpages);
	}
	private boolean DoGetPageInIndex(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}
	private boolean DoGetIndexPageNumbers(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}
	private boolean DoGetPageQuality(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}
	private boolean DoGetIndexForPage(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}
	private boolean DoGetPageNumbering(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_null();
	}

	private static final int 
	  Proc__doGetIndexProgress    = 0
	, Proc__doGetIndexFields      = 1
	, Proc__doGetIndexCategories  = 2
	, Proc__doGetNumberOfPages    = 3
	, Proc__doGetPageInIndex      = 4
	, Proc__doGetIndexPageNumbers = 5
	, Proc__doGetPageQuality      = 6
	, Proc__doGetIndexForPage     = 7
	, Proc__doGetPageNumbering    = 8
	;
	public static final String 
	  Invk__doGetIndexProgress    = "doGetIndexProgress"
	, Invk__doGetIndexFields      = "doGetIndexFields"
	, Invk__doGetIndexCategories  = "doGetIndexCategories"
	, Invk__doGetNumberOfPages    = "doGetNumberOfPages"
	, Invk__doGetPageInIndex      = "doGetPageInIndex"
	, Invk__doGetIndexPageNumbers = "doGetIndexPageNumbers"
	, Invk__doGetPageQuality      = "doGetPageQuality"
	, Invk__doGetIndexForPage     = "doGetIndexForPage"
	, Invk__doGetPageNumbering    = "doGetPageNumbering"
	;
	private static final    String[] Proc__names = String_.Ary
	( Invk__doGetIndexProgress
	, Invk__doGetIndexFields
	, Invk__doGetIndexCategories
	, Invk__doGetNumberOfPages
	, Invk__doGetPageInIndex
	, Invk__doGetIndexPageNumbers
	, Invk__doGetPageQuality
	, Invk__doGetIndexForPage
	, Invk__doGetPageNumbering
	);
}
