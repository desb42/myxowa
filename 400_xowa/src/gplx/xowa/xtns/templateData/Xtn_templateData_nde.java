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
package gplx.xowa.xtns.templateData; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*; import gplx.xowa.parsers.htmls.*;
import gplx.xowa.parsers.lnkis.files.*;
import gplx.langs.jsons.*;
import gplx.xowa.langs.msgs.*;
public class Xtn_templateData_nde implements Xox_xnde {
	private Json_doc jdoc = null;
	private List_adp list = List_adp_.New();
	private int param_len = 0;
	private Xow_msg_mgr msg_mgr;
	Xop_ctx ctx;
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public void Xatr__set(Xowe_wiki wiki, byte[] src, Mwh_atr_itm xatr, Object xatr_id_obj) {}
	// some logic from mediawiki\extensions\TemplateData\includes\TemplateDataBlob.php
	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		this.xnde = xnde;
		this.ctx = ctx;
		msg_mgr = wiki.Msg_mgr();
		int itm_bgn = xnde.Tag_open_end(), itm_end = xnde.Tag_close_bgn();
		if (itm_bgn == src.length) return;  // NOTE: handle inline where there is no content to parse; EX: <templatedata/>
		if (itm_bgn >= itm_end) return;  // NOTE: handle inline where there is no content to parse; EX: a<templatedata/>b
		//jdoc = wiki.App().Utl__json_parser().Parse(Bry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn()));
		jdoc = wiki.App().Utl__json_parser().Parse(src, xnde.Tag_open_end(), xnde.Tag_close_bgn());
		//System.out.println(String_.new_u8(Bry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn())));
		// do some field parsing now (due to 'inherits' keyword
		if (jdoc != null) {
			Json_nde params = Json_nde.Cast(jdoc.Get_grp(Bry_.new_a7("params")));
			if (params == null)
				param_len = 0;
			else
				param_len = params.Len();
			for (int i = 0; i < param_len; i++) {
				Json_kv param = params.Get_at_as_kv(i);
				byte[] param_key = param.Key_as_bry();
				Json_nde param_val = param.Val_as_nde();
				Template_row tr = new Template_row(param_val, param_key);
				if (tr.badkey) {
					Xoa_app_.Usr_dlg().Warn_many("", "", "bad argument to templatedata: page=~{0} arg=~{1}", ctx.Page().Url().To_str(), String_.new_u8(tr.fld_key));
				}
				list.Add(tr);
			}
		}
	}

	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		if (jdoc == null) return;

		Json_ary paramorder = (Json_ary)jdoc.Get_grp(Bry_.new_a7("paramOrder"));
				// also 'sets' and 'maps'?
		Json_itm descgrp = jdoc.Find_nde(Bry_.new_a7("description"));
		byte[] desc;
		if (descgrp == null)
			desc = null;
		else
			desc = get_text_value(descgrp); //(jdoc.Get_val_as_bry_or(Bry_.new_a7("description"), null);
		byte[] format = jdoc.Get_val_as_bry_or(Bry_.new_a7("format"), null);
		byte[] icon = m_settings;
		byte[] formatMsg;
		if (format == null ) {
			formatMsg = null;
		} else if (format[0] == 'b') { // block
			formatMsg = format;
			icon = f_block;
		} else if (format[0] == 'i') { // or inline
			formatMsg = format;
			icon = f_inline;
		} else {
			formatMsg = m_custom;
		}
		bfr.Add_str_a7("<div class=\"mw-templatedata-doc-wrap\"><p class=\"mw-templatedata-doc-desc");
		if (desc == null) {
			bfr.Add_str_a7(" mw-templatedata-doc-muted");
		}
		bfr.Add_str_a7("\">");
		if (desc == null) {
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-desc-empty"));
		} else {
			bfr.Add(desc);
		}
		bfr.Add_str_a7("</p>\n<table class=\"wikitable mw-templatedata-doc-params sortable\">\n<caption><p>");
		bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-params"));
		bfr.Add_str_a7("</p>");
		if (formatMsg != null) {
			bfr.Add_str_a7("<p><span aria-disabled=\"false\" class=\"oo-ui-widget oo-ui-widget-enabled oo-ui-iconElement-icon oo-ui-icon-");
			bfr.Add(icon);
			bfr.Add_str_a7(" oo-ui-iconElement oo-ui-labelElement-invisible oo-ui-iconWidget\"></span>");
			bfr.Add_str_a7("<span class=\"mw-templatedata-format\">");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-format-" + String_.new_a7(formatMsg)));
			bfr.Add_str_a7("</span></p>");
		}
		bfr.Add_str_a7("</caption>\n<thead><tr>\n");
		bfr.Add_str_a7("<th colspan=\"2\">");
		bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-name"));
		bfr.Add_str_a7("</th>\n<th>");
		bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-desc"));
		bfr.Add_str_a7("</th>\n<th>");
		bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-type"));
		bfr.Add_str_a7("</th>\n<th>");
		bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-status"));
		bfr.Add_str_a7("</th><thead>\n<tbody>");

		if (param_len == 0) {
			bfr.Add_str_a7("<tr><td class=\"mw-templatedata-doc-muted\" colspan=\"7\">");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-no-params-set"));
			bfr.Add_str_a7("</td></tr>\n");
		}
		else {

			if (paramorder != null) {
				int paramorder_len = paramorder.Len(); // should be the same as params
				for (int i = 0; i < paramorder_len; i++) {
					Json_itm paramord = paramorder.Get_at(i);
					byte[] orderkey = paramord.Data_bry();
					// find the key
					for (int j = 0; j < param_len; j++) {
						Template_row tr = (Template_row)list.Get_at(j);
						byte[] tr_key = tr.fld_key;
						if (Bry_.Eq(tr_key, orderkey)) {
							Make_row(bfr, tr);
							break;
						}
					}
				}
			} else {
				for (int i = 0; i < param_len; i++) {
					Template_row tr = (Template_row)list.Get_at(i);
					Make_row(bfr, tr);
				}
			}
		}

		bfr.Add_str_a7("</tbody></table></div>");

		//jdoc.Root_grp().Print_as_json(bfr, 0);
	}
	private void Make_row(Bry_bfr bfr, Template_row tr) {
		if (tr.fld_inherit != null) {
			// inherit from another field definition
			int list_len = list.Len();
			for (int i = 0; i < list_len; i++) {
				Template_row itr = (Template_row)list.Get_at(i);
				byte[] itr_key = itr.fld_key;
				if (Bry_.Eq(itr_key, tr.fld_inherit)) {
					// now copy values (but do not overwrite)
					if (itr.fld_aliases != null && tr.fld_aliases == null)
						tr.fld_aliases = itr.fld_aliases;
					if (itr.fld_autovalue != Bry_.Empty && tr.fld_autovalue == Bry_.Empty)
						tr.fld_autovalue = itr.fld_autovalue;
					if (itr.fld_default != Bry_.Empty && tr.fld_default == Bry_.Empty)
						tr.fld_default = itr.fld_default;
					if (itr.fld_deprecated != false && tr.fld_deprecated == false)
						tr.fld_deprecated = itr.fld_deprecated;
					if (itr.fld_description != Bry_.Empty && tr.fld_description == Bry_.Empty)
						tr.fld_description = itr.fld_description;
					if (itr.fld_example != Bry_.Empty && tr.fld_example == Bry_.Empty)
						tr.fld_example = itr.fld_example;
					if (itr.fld_label != null && tr.fld_label == null)
						tr.fld_label = itr.fld_label;
					if (itr.fld_required != false && tr.fld_required == false)
						tr.fld_required = itr.fld_required;
					if (itr.fld_suggested != false && tr.fld_suggested == false)
						tr.fld_suggested = itr.fld_suggested;
					if (itr.fld_suggestedvalues != null && tr.fld_suggestedvalues == null)
						tr.fld_suggestedvalues = itr.fld_suggestedvalues;
					if (itr.fld_type != Bry_.Empty && tr.fld_type == Bry_.Empty)
						tr.fld_type = itr.fld_type;
				}
			}
		}

		byte[] statusClass = Bry_.Empty;
		byte[] status;
		if ( tr.fld_deprecated ) {
			status = tdps_deprecated;
		} else if ( tr.fld_required ) {
			status = tdps_required;
			statusClass = mw_tdps_required;
		} else if ( tr.fld_suggested ) {
			status = tdps_suggested;
		} else {
			status = tdps_optional;
		}

		bfr.Add_str_a7("<tr><th>");
		if (tr.fld_label == null) {
			if (tr.fld_key != Bry_.Empty) {
				// not any more 20210517
				//byte b = tr.fld_key[0];
				//if (b >= 'a' && b <= 'z') {
				//	tr.fld_key[0] = (byte)(b - 32); // uppercase 1st letter
				//}
				bfr.Add(tr.fld_key);
				//tr.fld_key[0] = b; // restore
			}
		}
		else
			bfr.Add(tr.fld_label);
		bfr.Add_str_a7("</th><td class=\"mw-templatedata-doc-param-name\"><code>");
		bfr.Add(tr.fld_key);
		bfr.Add_str_a7("</code>");
		if (tr.fld_aliases != null) {
			Bry_bfr tmp_bfr = Bry_bfr_.New();
			int ary_len = tr.fld_aliases.Len();
			for (int k = 0; k < ary_len; k++) {
				tmp_bfr.Add(msg_mgr.Val_by_key_obj("word-separator"));
				tmp_bfr.Add_str_a7_null("<code class=\"mw-templatedata-doc-param-alias\">");
				tmp_bfr.Add(tr.fld_aliases.Get_at(k).Data_bry());
				tmp_bfr.Add_str_a7_null("</code>");
			}
			bfr.Add(tmp_bfr.To_bry_and_clear());
		}
		bfr.Add_str_a7("</td><td");
		if (tr.fld_description == Bry_.Empty) {
			bfr.Add_str_a7(" class=\"mw-templatedata-doc-muted\"");
		}
		bfr.Add_str_a7("><p>");
		if (tr.fld_description == Bry_.Empty) {
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-desc-empty"));
		} else {
			bfr.Add(tr.fld_description);
		}
		bfr.Add_str_a7("</p><dl>");
		// suggested values
		if (tr.fld_suggestedvalues != null) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-suggestedvalues"));
			bfr.Add_str_a7("</dt><dd>");
			int ary_len = tr.fld_suggestedvalues.Len();
			for (int k = 0; k < ary_len; k++) {
				bfr.Add(msg_mgr.Val_by_key_obj("word-separator"));
				bfr.Add_str_a7_null("<code class=\"mw-templatedata-doc-param-alias\">");
				bfr.Add(tr.fld_suggestedvalues.Get_at(k).Data_bry());
				bfr.Add_str_a7_null("</code>");
			}
			bfr.Add_str_a7("</dd>");
		}
		//default
		if (tr.fld_default != Bry_.Empty) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-default"));
			bfr.Add_str_a7("</dt><dd>");
			bfr.Add(tr.fld_default);
			bfr.Add_str_a7("</dd>");
		}
		// example
		if (tr.fld_example != Bry_.Empty) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-example"));
			bfr.Add_str_a7("</dt><dd>");
			bfr.Add(tr.fld_example);
			bfr.Add_str_a7("</dd>");
		}
		// autovalue
		if (tr.fld_autovalue != Bry_.Empty) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-autovalue"));
			bfr.Add_str_a7("</dt><dd><code>");
			bfr.Add(tr.fld_autovalue);
			bfr.Add_str_a7("</code></dd>");
		}
		bfr.Add_str_a7("</dl></td><td class=\"mw-templatedata-doc-param-type");
		if (tr.fld_type == Bry_.Empty) {
			bfr.Add_str_a7(" mw-templatedata-doc-muted");
		}
		bfr.Add_str_a7("\">");
		if (tr.fld_type == Bry_.Empty) {
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-type-unknown"));
		} else {
			String stype;
			// check for comptibility - starting 'string/'
			if (Bry_.Match(tr.fld_type, 0, 7, string_start)) {
				stype = String_.new_a7(Bry_.Mid(tr.fld_type, 7));
			}
			else
				stype = String_.new_a7(tr.fld_type);
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-type-" + stype));
		}
		bfr.Add_str_a7("</td><td");
		if (statusClass != Bry_.Empty) {
			bfr.Add_str_a7(" class=\"");
			bfr.Add(statusClass);
			bfr.Add_str_a7("\"");
		}
		bfr.Add_str_a7(">");
		bfr.Add(msg_mgr.Val_by_key_obj(status));
		bfr.Add_str_a7("</td></tr>");
	}

	public static byte[] get_text_value(Json_itm itm) {
		if (itm == null) return Bry_.Empty;
		// ((Json_itm_str)val).Data_bry();
		if (itm instanceof Json_itm_str)
			return ((Json_itm_str)itm).Data_bry();
		Json_nde langs = Json_nde.Cast(itm);
		int langs_len = langs.Len();
		for (int j = 0; j < langs_len; j++) {
			Json_kv kv = Json_kv.Cast(langs.Get_at(j));
			byte[] key = kv.Key_as_bry();
			Json_itm val = kv.Val();
			if (key[0] == 'e' && key[1] == 'n')
				return ((Json_itm_str)val).Data_bry();
		}
		return Bry_.Empty;
	}
	public static boolean get_boolean_value(Json_itm itm) { // should be with a language tag
		if (itm == null) return false;
		if (itm instanceof Json_itm_bool)
			return ((Json_itm_bool)itm).Data_as_bool();
		byte[] word = ((Json_itm_str)itm).Data_bry();
		if (word.length > 0) {
			if (word[0] == 'Y' || word[0] == 'y') return true;
			if (word[0] == '1') return true;
		}
		return false;
	}
	private static final byte[]
	  tdps_deprecated = Bry_.new_a7("templatedata-doc-param-status-deprecated")
	, tdps_required = Bry_.new_a7("templatedata-doc-param-status-required")
	, tdps_suggested = Bry_.new_a7("templatedata-doc-param-status-suggested")
	, tdps_optional = Bry_.new_a7("templatedata-doc-param-status-optional")
	, mw_tdps_required = Bry_.new_a7("mw-templatedata-doc-param-status-required")
	, m_custom = Bry_.new_a7("custom")
	, m_settings = Bry_.new_a7("settings")
	, f_block = Bry_.new_a7("template-format-block")
	, f_inline = Bry_.new_a7("template-format-inline")
	, string_start = Bry_.new_a7("string/")
	;
}
// suggestedvalues added Apr 2021 (in mw)
class Template_row {
	byte[] fld_key; 
	Json_ary fld_aliases;
	byte[] fld_autovalue; 
	byte[] fld_default;
	boolean fld_deprecated = false;
	byte[] fld_description;
	byte[] fld_example;
	byte[] fld_label;
	boolean fld_required;
	boolean fld_suggested;
	Json_ary fld_suggestedvalues;
	byte[] fld_type;
	byte[] fld_inherit;
	boolean badkey = false;

	Template_row(Json_nde param_val, byte[] param_key) {
		fld_key = param_key;
		fld_aliases = null;
		fld_autovalue = Bry_.Empty; 
		fld_default = Bry_.Empty;
		fld_deprecated = false;
		fld_description = Bry_.Empty;
		fld_example = Bry_.Empty;
		fld_label = null;
		fld_required = false;
		fld_suggested = false;
		fld_suggestedvalues = null;
		fld_type = Bry_.Empty;
		fld_inherit = null;
		int param_val_len = param_val.Len();
		for (int j = 0; j < param_val_len; j++) {
			Json_kv itm = Json_kv.Cast(param_val.Get_at(j));
			byte[] key = itm.Key_as_bry();
			Json_itm val = itm.Val();
			int len = key.length;
			switch (key[0]) {
				case 'a':
					if (len == 7 && key[1] == 'l') // aliases
						fld_aliases = (Json_ary)val;
					else if (len == 9 && key[1] == 'u') // autovalue
						fld_autovalue = ((Json_itm_str)val).Data_bry();
					else
						badkey = true;
					break;
				case 'd':
					if (len == 7 && key[2] == 'f') // default
						fld_default = Xtn_templateData_nde.get_text_value(val); //((Json_itm_str)val).Data_bry();
					else if (len == 10 && key[2] == 'p') // deprecated
						//fld_deprecated = ((Json_itm_bool)val).Data_as_bool();
						fld_deprecated = Xtn_templateData_nde.get_boolean_value(val); //(Json_itm_str)val).Data_bry();
					else if (len == 11 && key[2] == 's') // description
						fld_description = Xtn_templateData_nde.get_text_value(val); //((Json_itm_str)val).Data_bry();
					else
						badkey = true;
					break;
				case 'e':
					if (len == 7) // example
						fld_example = Xtn_templateData_nde.get_text_value(val); //((Json_itm_str)val).Data_bry();
					else
						badkey = true;
					break;
				case 'l':
					if (len == 5) // label
						fld_label = Xtn_templateData_nde.get_text_value(val); //((Json_itm_str)val).Data_bry();
					else
						badkey = true;
					break;
				case 'r':
					if (len == 8) // required
						fld_required = Xtn_templateData_nde.get_boolean_value(val); //((Json_itm_bool)val).Data_as_bool();
					else
						badkey = true;
					break;
				case 's':
					if (len == 9 && key[8] == 'd') // suggested
						fld_suggested = Xtn_templateData_nde.get_boolean_value(val); //((Json_itm_bool)val).Data_as_bool();
					else if (len == 15 && key[14] == 's') // suggestedvalues
						fld_suggestedvalues = (Json_ary)val;
					else
						badkey = true;
					break;
				case 't':
					if (len == 4) // type
						fld_type = ((Json_itm_str)val).Data_bry();
					else
						badkey = true;
					break;
				case 'i':
					if (len == 8) { // inherits
						fld_inherit = ((Json_itm_str)val).Data_bry();
					}
					break;
				default:
					badkey = true;
					break;
			}
		}
	}
}