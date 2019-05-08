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
package gplx.xowa.addons.wikis.ctgs.htmls.catpages; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.*; import gplx.xowa.addons.wikis.ctgs.*; import gplx.xowa.addons.wikis.ctgs.htmls.*;
import gplx.xowa.wikis.dbs.*; import gplx.xowa.wikis.data.tbls.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.msgs.*; import gplx.xowa.htmls.core.htmls.*; import gplx.core.intls.ucas.*;
import gplx.langs.htmls.Gfh_tag_;
import gplx.xowa.wikis.nss.*; import gplx.xowa.htmls.heads.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.doms.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.fmts.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.dbs.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.urls.*; import gplx.xowa.addons.wikis.ctgs.htmls.catpages.langs.*;
import gplx.xowa.mediawiki.includes.XomwDefines;
import gplx.xowa.xtns.categorytrees.*;public class Xoctg_catpage_mgr implements Gfo_invk {
	private final Xow_wiki wiki;
	private final Hash_adp_bry cache = Hash_adp_bry.cs();
	private final Xoctg_catpage_loader loader = new Xoctg_catpage_loader();
	private final Xoctg_fmt_grp fmt_subcs = Xoctg_fmt_grp.New__subc(), fmt_pages = Xoctg_fmt_grp.New__page(), fmt_files = Xoctg_fmt_grp.New__file();
	private final Uca_ltr_extractor ltr_extractor = new Uca_ltr_extractor(true);
	private String missing_cls = Str__missing_cls__red;
	private final	Bry_bfr tmp_bfr = Bry_bfr_.New();
	public int Grp_max() {return grp_max;} private int grp_max = Grp_max_dflt;
	public Xoctg_catpage_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
		this.collation_mgr = new Xoctg_collation_mgr(wiki);
	}
	public Xoctg_collation_mgr Collation_mgr() {return collation_mgr;} private Xoctg_collation_mgr collation_mgr;
	public Xoctg_fmt_grp Fmt(byte tid) {
		switch (tid) {
			case Xoa_ctg_mgr.Tid__subc: return fmt_subcs;
			case Xoa_ctg_mgr.Tid__page: return fmt_pages;
			case Xoa_ctg_mgr.Tid__file: return fmt_files;
			default: throw Err_.new_unhandled(tid);
		}
	}
	public byte[] Missing_ctg_cls_css() {
		if		(String_.Eq(missing_cls, Str__missing_cls__normal))		return Css__missing_cls__normal;
		else if (String_.Eq(missing_cls, Str__missing_cls__hide))		return Css__missing_cls__hide;
		else if (String_.Eq(missing_cls, Str__missing_cls__red))		return Css__missing_cls__red;
		else															return Bry_.Empty;	// NOTE: do not throw error, else fatal error when regen'ing cfg db; DATE:2016-12-27
	}
	public void Init_by_wiki(Xow_wiki wiki) {
		wiki.App().Cfg().Bind_many_wiki(this, wiki, Cfg__missing_class);
	}
	public void Free_mem_all() {cache.Clear();}
	public Xoctg_catpage_ctg Get_or_load_or_null(byte[] page_ttl, Xoctg_catpage_url catpage_url, Xoa_ttl cat_ttl, int limit) {
		// load categories from cat dbs; exit if not found
		Xoctg_catpage_ctg ctg = (Xoctg_catpage_ctg)cache.Get_by(cat_ttl.Full_db());
		if (ctg == null) {
			if (gplx.core.envs.Env_.Mode_testing()) return null;	// needed for dpl test
			synchronized (thread_lock) {	// LOCK:used by multiple wrks; DATE:2016-09-12
				ctg = loader.Load_ctg_or_null(wiki, page_ttl, this, catpage_url, cat_ttl, limit);
			}
			if (ctg == null) return null;	// not in cache or db; exit
			if (limit == Int_.Max_value)	// only add to cache if Max_val (DynamicPageList); for regular catpages, always retrieve on demand
				cache.Add(cat_ttl.Full_db(), ctg);
		}
		return ctg;
	}
	public void Write_catpage(Bry_bfr bfr, Xoa_page page) {
		try	{
			// get catpage_url
			Xoctg_catpage_url catpage_url = Xoctg_catpage_url_parser.Parse(page.Url());

			// load categories from cat dbs; exit if not found
			Xoctg_catpage_ctg ctg = Get_or_load_or_null(page.Ttl().Page_db(), catpage_url, page.Ttl(), grp_max);
			if (ctg == null) return;

			// write html
			Xol_lang_itm lang = page.Lang();
			fmt_subcs.Write_catpage_grp(bfr, wiki, lang, ltr_extractor, ctg, grp_max);
			fmt_pages.Write_catpage_grp(bfr, wiki, lang, ltr_extractor, ctg, grp_max);
			fmt_files.Write_catpage_grp(bfr, wiki, lang, ltr_extractor, ctg, grp_max);

			page.Html_data().Head_mgr().Itm__categorytree().Enabled_y_();
		}
		catch (Exception e) {
			Xoa_app_.Usr_dlg().Warn_many("", "", "failed to generate category: title=~{0} err=~{1}", page.Url_bry_safe(), Err_.Message_gplx_log(e));
		}
	}
	public void Cache__add(byte[] ttl, Xoctg_catpage_ctg ctg) {
		cache.Del(ttl);
		cache.Add(ttl, ctg);
	}
	public void Grp_max_(int v) {grp_max = v;}	// TEST:
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk__collation_))		collation_mgr.Collation_name_(m.ReadStr("v"));
		else if (ctx.Match(k, Cfg__missing_class))		missing_cls = m.ReadStr("v");
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}	private static final String Invk__collation_ = "collation_";

	public static int Grp_max_dflt = 200;
	private static final Object thread_lock = new Object();

	private static final String Cfg__missing_class = "xowa.addon.category.catpage.missing_class";
	private static final String Str__missing_cls__normal = "normal", Str__missing_cls__hide = "hide", Str__missing_cls__red = "red_link";
	private static final byte[] Css__missing_cls__normal = Bry_.new_a7(".xowa-missing-category-entry {}"), Css__missing_cls__hide = Bry_.new_a7(".xowa-missing-category-entry {display: none;}"), Css__missing_cls__red = Bry_.new_a7(".xowa-missing-category-entry {color: red;}");

	public void Bld_cat_itm(Bry_bfr bfr, Xow_wiki wiki, int count_subcs, int count_pages, int count_files, Xoa_ttl ttl, Categorytree_params_ params, boolean showli, byte[] child_text, byte[] data_ct_mode) {
		byte[] itm_href = wiki.Html__href_wtr().Build_to_bry(wiki, ttl);
		Xow_msg_mgr msg_mgr = wiki.Msg_mgr();
		byte[] ttl_page;
		int ns = ttl.Ns().Id();
		byte[] attr_class = attr_treebullet;
		byte[] count_string = null; // empty?
		byte[] bullet = null;
		int allcount = count_subcs + count_pages + count_files;
		int count = -1;
		boolean prefixhide;

		if (params.Hideprefix() == Categorytree_itm_.Hide__ALWAYS ) {
			prefixhide = true;
		} else if ( params.Hideprefix() == Categorytree_itm_.Hide__AUTO ) {
			prefixhide = ( params.Mode() == Categorytree_itm_.Mode__CATEGORIES );
		} else if ( params.Hideprefix() == Categorytree_itm_.Hide__CATEGORIES ) {
			prefixhide = ( ns == XomwDefines.NS_CATEGORY );
		} else {
			prefixhide = true;
		}

		// when showing only categories, omit namespace in label unless we explicitely defined the
		// configuration setting
		// patch contributed by Manuel Schneider <manuel.schneider@wikimedia.ch>, Bug 8011
		if ( prefixhide ) {
			ttl_page = ttl.Page_txt();
		} else {
			ttl_page = ttl.Page_db(); // Not sure this is correct
		}

		byte[] labelClass;
		if ( ns == XomwDefines.NS_CATEGORY ) {
			labelClass = label_category;
		} else {
			labelClass = label_page;
		}

		if (ns == XomwDefines.NS_CATEGORY) {
			if ( params.Mode() == Categorytree_itm_.Mode__CATEGORIES ) {
				count = count_subcs;
			} else if ( params.Mode() == Categorytree_itm_.Mode__PAGES ) {
				count = allcount - count_files;
			} else {
				count = allcount;
			}
			if (count == 0) {
				bullet = msg_mgr.Val_by_key_obj("categorytree-empty-bullet");
				attr_class = attr_treeempty;
			} else {
				byte[] txt, link_data;
				if ( child_text.length == 0 ) {
					txt = msg_mgr.Val_by_key_obj("categorytree-expand-bullet");
					link_data = datact_collapsed;
				} else {
					txt = msg_mgr.Val_by_key_obj("categorytree-collapse-bullet");
					link_data = datact_expanded;
				}
				Fmt__bullet.Bld_many(tmp_bfr, ttl.Page_db(), ttl_page, link_data, txt);
				bullet = tmp_bfr.To_bry_and_clear();
			}
		} else {
			bullet = msg_mgr.Val_by_key_obj("categorytree-page-bullet");
		}

		byte[] cstyle;
		if (child_text.length > 0) {
			cstyle = style_block;
		} else {
			cstyle = style_none;
		}
		if (params.Showcount())
			count_string = Create_count_string(msg_mgr, count_subcs, count_pages, count_files);
		byte[] startli, endli;
		if (showli) {
			startli = Gfh_tag_.Li_lhs;
			endli = Gfh_tag_.Li_rhs;
		} else if (data_ct_mode != Bry_.Empty) {
			startli = data_ct_mode;
			endli = Gfh_tag_.Div_rhs;
		} else {
			startli = Bry_.Empty;
			endli = Bry_.Empty;
		}
		Fmt__cat.Bld_many(bfr, startli, attr_class, bullet, itm_href, ns, labelClass, ttl_page, count_string, cstyle, child_text, endli);
	}
	//see CategoryTree createCountString()
	private byte[] Create_count_string(Xow_msg_mgr msg_mgr, int count_subcs, int count_pages, int count_files) {
		int allcount = count_subcs + count_pages + count_files;
		byte[] contains_title = msg_mgr.Val_by_id_args(Xol_msg_itm_.Id_ctgtree_subc_counts, count_subcs, count_pages, count_files, allcount);
		byte[] members = Member_nums(msg_mgr, count_subcs, count_pages, count_files, allcount);
		// Only ~{4} is actually used in the default message.
		// Other arguments can be used in a customized message.
		byte[] msg = msg_mgr.Val_by_key_args(key_cmn,  count_subcs, count_pages, count_files, allcount, members);
		byte[] dir = wiki.Lang().Dir_ltr_bry();
		Fmt__count_string.Bld_many(tmp_bfr, contains_title, dir, msg);
		return tmp_bfr.To_bry_and_clear();
	}
	private byte[] Member_nums(Xow_msg_mgr msg_mgr, int count_subcs, int count_pages, int count_files, int allcount) {
		if (allcount == 0) {
			return msg_mgr.Val_by_key_obj("categorytree-num-empty");
		}
		Bld_contains_text_itm(tmp_bfr, msg_mgr, Xol_msg_itm_.Id_ctgtree_subc_counts_ctg, count_subcs);
		Bld_contains_text_itm(tmp_bfr, msg_mgr, Xol_msg_itm_.Id_ctgtree_subc_counts_page, count_pages);
		Bld_contains_text_itm(tmp_bfr, msg_mgr, Xol_msg_itm_.Id_ctgtree_subc_counts_file, count_files);
		return tmp_bfr.To_bry_and_clear();
	}
	private void Bld_contains_text_itm(Bry_bfr bfr, Xow_msg_mgr msg_mgr, int msg_id, int val) {
		if (val == 0) return;
		if (bfr.Len() > 1) bfr.Add(Bld_contains_text_itm_dlm);	// NOTE: 1 b/c Paren_bgn is always added
		bfr.Add(msg_mgr.Val_by_id_args(msg_id, val));
	}
	private static final	byte[] Bld_contains_text_itm_dlm = Bry_.new_a7(", "); // should be language sensitive!

	private static final	Bry_fmt
	  Fmt__cat = Bry_fmt.Auto_nl_skip_last
	( ""
	, "			  ~{startli}<div class=\"CategoryTreeSection\">"
	, "				<div class=\"CategoryTreeItem\">"
	, "				  <span class=\"~{attr_class}\">"
	, "					~{bullet}"
	, "				  </span>"
	, "				  <a href=\"~{itm_href}\" class=\"CategoryTreeLabel CategoryTreeLabelNs~{ns} ~{label}\">~{itm_text}"
	, "				  </a>"
	, "				  ~{count_string}"
	, "				</div>"
	, "				<div class=\"CategoryTreeChildren\" style=\"display:~{cstyle}\">~{child_text}</div>"
	, "			  </div>~{endli}"
	);
	private static final	Bry_fmt
	  Fmt__count_string = Bry_fmt.Auto_nl_skip_last
	( ""
	, "<span title=\"~{itm_contains_title}\" dir=\"~{dir}\">~{itm_contains_text}"
	, "</span>"
	);
	private static final	Bry_fmt
	  Fmt__bullet = Bry_fmt.Auto_nl_skip_last
	( ""
	, "					<span class=\"CategoryTreeToggle\" data-ct-title=\"~{itm_data_title}\" title=\"~{itm_title}\" ~{data-ct}>"
	, "					~{txt}</span> "
	);
	private static byte[]
	  datact_expanded = Bry_.new_a7("data-ct-loaded=\"1\" data-ct-state=\"expanded\"")
	, datact_collapsed = Bry_.new_a7("data-ct-state=\"collapsed\"")
	, attr_treebullet = Bry_.new_a7("CategoryTreeBullet")
	, attr_treeempty = Bry_.new_a7("CategoryTreeEmptyBullet")
	, style_block = Bry_.new_a7("block")
	, style_none = Bry_.new_a7("none")
	, label_category = Bry_.new_a7("CategoryTreeLabelCategory")
	, label_page = Bry_.new_a7("CategoryTreeLabelPage")
	, key_cmn = Bry_.new_a7("categorytree-member-num")
	;

	public void Renderchild(Bry_bfr bfr, byte[] src, int bgn, int end, Categorytree_params_ params) {
		Xoa_ttl ttl;
		// write out cattree
		int grp_max = 200;
		Xoctg_catpage_url catpage_url = Xoctg_catpage_url_parser.Parse(null);

		Bry_bfr local_tmp_bfr = Bry_bfr_.New();
		local_tmp_bfr.Add_str_a7("Category:");
		local_tmp_bfr.Add_mid(src, bgn, end);
		ttl = Xoa_ttl.Parse(wiki, local_tmp_bfr.To_bry_and_clear_and_rls());

		Xoctg_catpage_ctg ctg = Get_or_load_or_null(ttl.Page_db(), catpage_url, ttl, grp_max);
		if (ctg == null) return;

		Fmt__data_ct.Bld_many(local_tmp_bfr, params.Mode(), params.Hideprefix(), params.Showcount(), params.Namespaces());
		byte[] data_ct_mode = local_tmp_bfr.To_bry_and_clear();

		// write html
		Build_cattree(local_tmp_bfr, wiki, ctg, params, data_ct_mode);
		if (params.Hideroot() == false) {
			byte[] inner = local_tmp_bfr.To_bry_and_clear();
			Bld_cat_itm(local_tmp_bfr, wiki, ctg.Subcs().Count_all(), ctg.Pages().Count_all(), ctg.Files().Count_all(), ttl, params, false, inner, data_ct_mode);
		}
		bfr.Add_bfr_and_clear(local_tmp_bfr);
	}

	public void Build_cattree(Bry_bfr bfr, Xow_wiki wiki, Xoctg_catpage_ctg ctg, Categorytree_params_ params, byte[] data_ct_mode) {
		// first categories
		Xoctg_catpage_grp grp = ctg.Subcs();
		// init vars
		int grp_end = grp.Itms__len();

		// loop over itms; 
		for (int i = 0; i < grp_end; i++) {
			Xoctg_catpage_itm itm = grp.Itms__get_at(i);
	
			Xoa_ttl itm_ttl = itm.Page_ttl();
			if (itm_ttl == Xoa_ttl.Null)
				Fmt__missing.Bld_many(bfr, itm.Page_id());
			else
				Bld_cat_itm(bfr, wiki, itm.Count_subcs(), itm.Count_pages(), itm.Count_files(), itm_ttl, params, false, Bry_.Empty, data_ct_mode);
		}
		// if mode pages  - add pages as well (but noc ounts)
		if (params.Mode() == Categorytree_itm_.Mode__PAGES) {
			params.Showcount_(false);
			grp = ctg.Pages();
			grp_end = grp.Itms__len();
	
			// loop over itms; 
			for (int i = 0; i < grp_end; i++) {
				Xoctg_catpage_itm itm = grp.Itms__get_at(i);
		
				Xoa_ttl itm_ttl = itm.Page_ttl();
				if (itm_ttl == Xoa_ttl.Null)
					Fmt__missing.Bld_many(bfr, itm.Page_id());
				else
					Bld_cat_itm(bfr, wiki, itm.Count_subcs(), itm.Count_pages(), itm.Count_files(), itm_ttl, params, false, Bry_.Empty, data_ct_mode);
			}
		}
	}
	private static final	Bry_fmt
	  Fmt__missing = Bry_fmt.Auto_nl_skip_last
	( ""
	, "			<li class=\"xowa-missing-category-entry\"><span title=\"id not found: #~{itm_id} might be talk/user page\">missing page (~{itm_id})</li>"
	);
	private static final	Bry_fmt
	  Fmt__data_ct = Bry_fmt.Auto_nl_skip_last
	( ""
	, "<div class=\"CategoryTreeTag\" data-ct-mode=\"~{mode}\" data-ct-options=\"{&quot;mode&quot;:~{mode},&quot;hideprefix&quot;:~{hide},&quot;showcount&quot;:~{showcount},&quot;namespaces&quot;:~{namespaces}}\">"
	);
}
