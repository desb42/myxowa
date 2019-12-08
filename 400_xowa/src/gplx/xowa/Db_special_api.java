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
public class Db_special_api {
	private Gfo_qarg_mgr_old arg_hash = new Gfo_qarg_mgr_old();
	private Xoctg_catpage_mgr cat_mgr;
	private Categorytree_params_ params;
	public byte[] Gen_json(Xowe_wiki wiki, Xoae_page page, Xoa_url url, Xoa_ttl ttl) {
		cat_mgr = wiki.Ctg__catpage_mgr();
		params = new Categorytree_params_();
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
		bfr.Add_str_a7("{\"categorytree\":{\"html\":\"");
		switch (cmd_type_val.Val()) {
			case Type_categorytree:	Categorytree(tmp_bfr, wiki.App(), url, arg_hash); break;
		}
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

		bfr.Add_str_a7("\"}}");
		return bfr.To_bry_and_clear();
	}
	private void Categorytree(Bry_bfr bfr, Xoa_app app, Xoa_url url, Gfo_qarg_mgr_old arg_hash) {
		byte[] category = arg_hash.Get_val_bry_or(Arg_category, null);
		byte[] options = arg_hash.Get_val_bry_or(Arg_options, null);
		//params.Depth_(0);
		// need to take the parsed in parameters into accout (at a later date)
                Checkparams(options);
		params.Hideroot_(true);
		//params.Showcount_(true);
		params.Isjson_(true);
		cat_mgr.Renderchild(bfr, category, 0, category.length, params);
	}
	private static final	byte[]
	  Arg_action = Bry_.new_a7("action")
	, Arg_category = Bry_.new_a7("category")
	, Arg_options = Bry_.new_a7("options")
	;
	private static final byte Type_categorytree = 1; //, Type_fs_check = 2, Type_sql_dump = 3;
	private static final	Hash_adp_bry type_hash = Hash_adp_bry.cs()
	.Add_str_byte("categorytree"		, Type_categorytree)
	;

	static public byte[] Gen(Http_server_page page) {
		Db_special_api api = new Db_special_api();
		return api.Gen_json(page.Wiki(), page.Page(), page.Url(), page.Ttl());
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
