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
package gplx.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
import gplx.core.primitives.*; import gplx.core.net.*; import gplx.core.net.qargs.*;
import gplx.xowa.apps.urls.*;
import gplx.xowa.addons.wikis.ctgs.htmls.catpages.*;import gplx.xowa.apps.servers.http.Http_server_page;
import gplx.xowa.xtns.categorytrees.*;
import gplx.xowa.wikis.data.tbls.Xowd_page_itm;
import gplx.xowa.files.Xof_ext;
import gplx.xowa.files.Xof_ext_;
import gplx.xowa.files.Xof_file_wkr_;
import gplx.core.brys.fmtrs.Bry_fmtr;
public class Db_special_api {
	private Gfo_qarg_mgr_old arg_hash = new Gfo_qarg_mgr_old();
	private Xoctg_catpage_mgr cat_mgr;
	private Categorytree_params_ params;
	public byte[] Gen_json_xowa(Xowe_wiki wiki, String stitle, String soptions) {
		byte[] category = Bry_.new_u8(stitle);
		byte[] options = Bry_.new_u8(soptions);

		Bry_bfr bfr = wiki.Utl__bfr_mkr().Get_m001();
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		bfr.Add_str_a7("{\"categorytree\":{\"html\":\"");
		Categorytree(wiki, tmp_bfr, category, options);
		return tidyup(bfr, tmp_bfr, "\"}}");
	}

	public byte[] Gen_json(Xowe_wiki wiki, Xoae_page page, Xoa_url url, Xoa_ttl ttl, Xowe_wiki wdata_wiki) {
		arg_hash.Load(url.Qargs_ary());
		byte[] cmd_type_bry = arg_hash.Get_val_bry_or(Arg_action, null);
		if (cmd_type_bry == null) {
			Xoa_app_.Usr_dlg().Warn_many("", "", "special.cmd; no action: url=~{0}", url.Raw());
			return Bry_.Empty;
		}
		Byte_obj_val cmd_type_val = (Byte_obj_val)type_hash.Get_by_bry(cmd_type_bry);
		if (cmd_type_val == null) {
			Xoa_app_.Usr_dlg().Warn_many("", "", "special.cmd; bad type: url=~{0}", url.Raw());
			return Bry_.Empty;
		}
		Bry_bfr bfr = wiki.Utl__bfr_mkr().Get_m001();
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		switch (cmd_type_val.Val()) {
			case Type_categorytree:
				bfr.Add_str_a7("{\"categorytree\":{\"html\":\"");
				byte[] category = arg_hash.Get_val_bry_or(Arg_category, null);
				byte[] options = arg_hash.Get_val_bry_or(Arg_options, null);
				Categorytree(wiki, tmp_bfr, category, options);
				return tidyup(bfr, tmp_bfr, "\"}}");

			case Type_query:
				bfr.Add_str_a7("{\"batchcomplete\":true,\"query\":{");
				byte[] titles = arg_hash.Get_val_bry_or(Arg_titles, null);
				Related(wiki, bfr, titles, wdata_wiki);
				bfr.Add_str_a7("}}");
				return bfr.To_bry_and_clear();
		}
		return Bry_.Empty;
	}

	private byte[] tidyup(Bry_bfr bfr, Bry_bfr tmp_bfr, String end) {
		// convert to json (ugh!)
//				String str = String_.new_u8(tmp_bfr.To_bry_and_clear());
//				str = str.replace("\"", "\\\"").replace("\n","").replace("\t","");
//				bfr.Add_str_u8(str);
		int tlen = tmp_bfr.Len();
		byte[] buf = tmp_bfr.Bfr();
		for (int i = 0; i < tlen; i++) {
			byte b = buf[i];
			if (b == '"') {
				buf[i] = '\'';
			}
			else if (b == '\n' || b == '\t') {
				buf[i] = ' ';
			}
		}
		bfr.Add_bfr_and_clear(tmp_bfr);

		bfr.Add_str_a7(end);
		return bfr.To_bry_and_clear();
	}
	private void Related(Xowe_wiki wiki, Bry_bfr bfr, byte[] titles, Xowe_wiki wdata_wiki) {
		int len = titles.length;
		int pos;
		int bgn;
		// first 'normalised'
		pos = 0;
		bgn = 0;
		bfr.Add_str_a7("\"normalized\":[");
		while (pos < len) {
			byte b = titles[pos++];
			if (b == '|') {
				Build_one_normalized(wiki, bfr, titles, bgn, pos - 1);
				bfr.Add_str_a7(",");
				bgn = pos;
			}
		}
		Build_one_normalized(wiki, bfr, titles, bgn, pos);
		// next 'pages'
		pos = 0;
		bgn = 0;
		bfr.Add_str_a7("],\"pages\":[");
		while (pos < len) {
			byte b = titles[pos++];
			if (b == '|') {
				Build_one_page(wiki, bfr, titles, bgn, pos - 1, wdata_wiki);
				bfr.Add_str_a7(",");
				bgn = pos;
			}
		}
		Build_one_page(wiki, bfr, titles, bgn, pos, wdata_wiki);
		bfr.Add_str_a7("]");
	}
	private void Build_one_normalized(Xowe_wiki wiki, Bry_bfr bfr, byte[] titles, int bgn, int end) {
		byte[] ttl_bry = Bry_.Mid(titles, bgn, end);
		bfr.Add_str_a7("{\"fromencoded\":false,\"to\":\"");
		bfr.Add(Xoa_ttl.Replace_unders(ttl_bry));
		bfr.Add_str_a7("\",\"from\":\"");
		bfr.Add(Xoa_ttl.Replace_spaces(ttl_bry));
		bfr.Add_str_a7("\"}");
	}
	private void Build_one_page(Xowe_wiki wiki, Bry_bfr bfr, byte[] titles, int bgn, int end, Xowe_wiki wdata_wiki) {
		Xoa_ttl ttl = Xoa_ttl.Parse(wiki, titles, bgn, end);
		// need
		Xowd_page_itm tmp_page = wiki.Data__core_mgr().Db__core().Tbl__page().Select_by_ttl_as_itm_or_null(ttl, wiki.Wrk_id());
		bfr.Add_str_a7("{\"pageid\":");
		//  page_id
		bfr.Add_int_variable(tmp_page.Id());
		bfr.Add_str_a7(",\"ns\":");
		//  page_namespace
		bfr.Add_int_variable(ttl.Ns().Id());
		bfr.Add_str_a7(",\"title\":\"");
		bfr.Add(Xoa_ttl.Replace_unders(Bry_.Mid(titles, bgn, end)));
		bfr.Add_str_a7("\",");
		Db_page_image_ pi = wiki.Page_image().Get_page_image(tmp_page.Id());
		if (pi.height > 0) {
			Xoa_ttl pi_ttl = Xoa_ttl.Parse(wiki, pi.pi_title);
			Xof_ext ext = Xof_ext_.new_by_ttl_(pi.pi_title);
			byte[] fname = pi_ttl.Full_db_href();
			byte[] site = Bry_.new_a7("commons");
			if (pi.site_id == 1 || pi.site_id == 11)
				site = Bry_.new_a7("en"); // should be the wiki language?
			byte[] md5 = Xof_file_wkr_.Md5(pi.pi_title);
			byte[] md5_subdir = Bry_.Add(Bry_.Mid(md5, 0, 1), Byte_ascii.Slash_bry, Bry_.Mid(md5, 0, 2));
			byte[] extension = Xof_ext_.Id_view(ext.Id()) == Xof_ext_.Id_png ? Bry_.new_a7(".png") : Bry_.Empty;
			thumb_fmtr.Bld_bfr_many(bfr, site, md5_subdir, fname, extension, 160, 160.*pi.width/pi.height);
		}
		bfr.Add_str_a7("\"description\":\"");
		//  description - from wikidata
		byte[] desc = wdata_wiki.Db_mgr().Load_mgr().Load_qid_desc(wiki.Wdata_wiki_abrv(), ttl.Ns().Id(), ttl.Page_db());
		if (desc != null)
			bfr.Add(desc);
		bfr.Add_str_a7("\",\"descriptionsource\":\"central\"}");
	}
	private static final Bry_fmtr
	 thumb_fmtr = Bry_fmtr.new_("\"thumbnail\": {\"source\": \"/xowa/api/wikipedia/~{site}/thumb/~{md5}/~{fname}/160px-~{fname}~{ext}\",\"width\":~{width},\"height\":~{height}},",
	              "site", "md5", "fname", "ext", "width", "height");
	private void Categorytree(Xowe_wiki wiki, Bry_bfr bfr, byte[] category, byte[] options) {
		cat_mgr = wiki.Ctg__catpage_mgr();
		params = new Categorytree_params_();
		//params.Depth_(0);
		// need to take the parsed in parameters into accout (at a later date)
		Checkparams(options);
		params.Hideroot_(true);
		//params.Showcount_(true);
		params.Isjson_(true);
		cat_mgr.Renderchild(bfr, category, 0, category.length, params);
	}
	private static final byte[]
	  Arg_action = Bry_.new_a7("action")
	, Arg_category = Bry_.new_a7("category")
	, Arg_options = Bry_.new_a7("options")

	, Arg_titles = Bry_.new_a7("titles")
	;
	private static final byte
	  Type_categorytree = 1
	, Type_query = 2
	;
	private static final Hash_adp_bry type_hash = Hash_adp_bry.cs()
	.Add_str_byte("categorytree", Type_categorytree)
	.Add_str_byte("query", Type_query)
	;

	static public byte[] Gen(Http_server_page page, Xowe_wiki wdata_wiki) {
		Db_special_api api = new Db_special_api();
		return api.Gen_json(page.Wiki(), page.Page(), page.Url(), page.Ttl(), wdata_wiki);
	}

	static public byte[] Gen_gui(Xowe_wiki wiki, String title, String options) {
		Db_special_api api = new Db_special_api();
		return api.Gen_json_xowa(wiki, title, options);
	}

	private static final int
	  State_wanting_quote = 1
	, State_key = 2
	, State_wanting_colon = 3
	, State_value = 4
	, State_quoted_value = 5
	, State_wanting_comma = 6
	, State_wanting_opencurl = 7
	;
	private int keybgn, keyend, valbgn, valend;
	private byte[] m_src;
	private void Checkparams(byte[] src ) {
		m_src = src;
		int len = src.length;
		int pos = 0;
		int state = State_wanting_opencurl;
		keybgn = keyend = valbgn = valend = -1;
		while (pos < len) {
			byte b = src[pos++];
			switch (state) {
				case State_wanting_opencurl:
					if (b == '{') {
						state = State_wanting_quote;
					}
					else
						return; // BAD exit
					break;
				case State_wanting_quote:
					if (b == '"') {
						state = State_key;
						keybgn = pos;
					}
					else
						return; // BAD exit
					break;
				case State_key:
					if (b == '"') {
						state = State_wanting_colon;
						keyend = pos - 1;
					}
					break;
				case State_wanting_colon:
					if (b == ':') {
						state = State_value;
						valbgn = pos;
					}
					else
						return; // BAD exit
					break;
				case State_value:
					// possibly check for quoted values???
					if (b == '"') {
						state = State_quoted_value;
						valbgn = pos;
					}
					else if (b == ',') {
						state = State_wanting_quote;
						valend = pos - 1;
						process();
					}
					else if (b == '}') {
						state = -1;
						valend = pos - 1;
						process();
					}
					break;
				case State_quoted_value:
					if (b == '"') {
						state = State_wanting_comma;
						valend = pos - 1;
						process();
					}
					break;
				case State_wanting_comma:
					if (b != ',')
						return; // BAD exit
					state = State_wanting_quote;
					break;
			}
		}
	}
	private void process() {
		byte[] key = Bry_.Mid(m_src, keybgn, keyend);
		byte[] val = Bry_.Mid(m_src, valbgn, valend);
		cat_mgr.Update_params(key, val, params);
	}
}
