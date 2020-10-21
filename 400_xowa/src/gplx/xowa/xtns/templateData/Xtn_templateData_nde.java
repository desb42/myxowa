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
		if (itm_bgn == src.length)	return;  // NOTE: handle inline where there is no content to parse; EX: <templatedata/>
		if (itm_bgn >= itm_end)		return;  // NOTE: handle inline where there is no content to parse; EX: a<templatedata/>b
		//jdoc = wiki.App().Utl__json_parser().Parse(Bry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn()));
		jdoc = wiki.App().Utl__json_parser().Parse(src, xnde.Tag_open_end(), xnde.Tag_close_bgn());
	}

	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		if (jdoc == null) return;

		Json_nde params = Json_nde.Cast(jdoc.Get_grp(Bry_.new_a7("params")));
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
		int param_len;
		if (params == null)
			param_len = 0;
		else
			param_len = params.Len();
		if (param_len == 0) {
			bfr.Add_str_a7("<tr><td class=\"mw-templatedata-doc-muted\" colspan=\"7\">");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-no-params-set"));
			bfr.Add_str_a7("</td></tr>\n");
		}

		if (paramorder != null) {
			int paramorder_len = paramorder.Len(); // should be the same as params
			for (int i = 0; i < paramorder_len; i++) {
				Json_itm paramord = paramorder.Get_at(i);
				byte[] orderkey = paramord.Data_bry();
				// find the key
				for (int j = 0; j < param_len; j++) {
					Json_kv param = params.Get_at_as_kv(j);
					byte[] param_key = param.Key_as_bry();
					if (Bry_.Eq(param_key, orderkey)) {
						Json_nde param_val = param.Val_as_nde();
						Make_row(bfr, param_val, param_key);
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < param_len; i++) {
				Json_kv param = params.Get_at_as_kv(i);
				byte[] param_key = param.Key_as_bry();
				Json_nde param_val = param.Val_as_nde();
				Make_row(bfr, param_val, param_key);
			}
		}

		bfr.Add_str_a7("</tbody></table></div>");

		//jdoc.Root_grp().Print_as_json(bfr, 0);
	}
	private void Make_row(Bry_bfr bfr, Json_nde param_val, byte[] param_key) {
		Json_ary fld_aliases;
		byte[] fld_autovalue; 
		byte[] fld_default;
		boolean fld_deprecated = false;
		byte[] fld_description;
		byte[] fld_example;
		byte[] fld_label;
		boolean fld_required;
		boolean fld_suggested;
		byte[] fld_type;
			fld_aliases = null;
			fld_autovalue = Bry_.Empty; 
			fld_default = Bry_.Empty;
			fld_deprecated = false;
			fld_description = Bry_.Empty;
			fld_example = Bry_.Empty;
			fld_label = null;
			fld_required = false;
			fld_suggested = false;
			fld_type = Bry_.Empty;


		int param_val_len = param_val.Len();
		for (int j = 0; j < param_val_len; j++) {
			Json_kv itm = Json_kv.Cast(param_val.Get_at(j));
			byte[] key = itm.Key_as_bry();
			Json_itm val = itm.Val();
			boolean badkey = false;
			if (key[0] == 'a') {
				if (key[1] == 'l') // aliases
					fld_aliases = (Json_ary)val;
				else if (key[1] == 'u') // autovalue
					fld_autovalue = ((Json_itm_str)val).Data_bry();
				else
					badkey = true;
			} else if (key[0] == 'd') {
				if (key[2] == 'f') // default
					fld_default = get_text_value(val); //((Json_itm_str)val).Data_bry();
				else if (key[2] == 'p') // deprecated
					//fld_deprecated = ((Json_itm_bool)val).Data_as_bool();
					fld_deprecated = get_boolean_value(val); //(Json_itm_str)val).Data_bry();
				else if (key[2] == 's') // description
					fld_description = get_text_value(val); //((Json_itm_str)val).Data_bry();
				else
					badkey = true;
			} else if (key[0] == 'e') { // example
				fld_example = get_text_value(val); //((Json_itm_str)val).Data_bry();
			} else if (key[0] == 'l') { // label
				fld_label = get_text_value(val); //((Json_itm_str)val).Data_bry();
			} else if (key[0] == 'r') { // required
				fld_required = get_boolean_value(val); //((Json_itm_bool)val).Data_as_bool();
			} else if (key[0] == 's') { // suggested
				fld_suggested = get_boolean_value(val); //((Json_itm_bool)val).Data_as_bool();
			} else if (key[0] == 't') { // type
				fld_type = ((Json_itm_str)val).Data_bry();
			} else
				badkey = true;
			if (badkey) {
				Xoa_app_.Usr_dlg().Warn_many("", "", "bad argument to templatedata: page=~{0} arg=~{1}", ctx.Page().Url().To_str(), String_.new_u8(key));
			}
		}

		byte[] statusClass = Bry_.Empty;
		byte[] status;
		if ( fld_deprecated ) {
			status = tdps_deprecated;
		} else if ( fld_required ) {
			status = tdps_required;
			statusClass = mw_tdps_required;
		} else if ( fld_suggested ) {
			status = tdps_suggested;
		} else {
			status = tdps_optional;
		}

		bfr.Add_str_a7("<tr><th>");
		if (fld_label == null) {
			if (param_key != Bry_.Empty) {
				byte b = param_key[0];
				if (b >= 'a' && b <= 'z') {
					param_key[0] = (byte)(b - 32); // uppercase 1st letter
				}
				bfr.Add(param_key);
				param_key[0] = b; // restore
			}
		}
		else
			bfr.Add(fld_label);
		bfr.Add_str_a7("</th><td class=\"mw-templatedata-doc-param-name\"><code>");
		bfr.Add(param_key);
		bfr.Add_str_a7("</code>");
		if (fld_aliases != null) {
			Bry_bfr tmp_bfr = Bry_bfr_.New();
			int ary_len = fld_aliases.Len();
			for (int k = 0; k < ary_len; k++) {
				tmp_bfr.Add(msg_mgr.Val_by_key_obj("word-separator"));
				tmp_bfr.Add_str_a7_null("<code class=\"mw-templatedata-doc-param-alias\">");
				tmp_bfr.Add(fld_aliases.Get_at(k).Data_bry());
				tmp_bfr.Add_str_a7_null("</code>");
			}
			bfr.Add(tmp_bfr.To_bry_and_clear());
		}
		bfr.Add_str_a7("</td><td");
		if (fld_description == Bry_.Empty) {
			bfr.Add_str_a7(" class=\"mw-templatedata-doc-muted\"");
		}
		bfr.Add_str_a7("><p>");
		if (fld_description == Bry_.Empty) {
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-desc-empty"));
		} else {
			bfr.Add(fld_description);
		}
		bfr.Add_str_a7("</p><dl>");
		if (fld_default != Bry_.Empty) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-default"));
			bfr.Add_str_a7("</dt><dd>");
			bfr.Add(fld_default);
			bfr.Add_str_a7("</dd>");
		}
		// example
		if (fld_example != Bry_.Empty) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-example"));
			bfr.Add_str_a7("</dt><dd>");
			bfr.Add(fld_example);
			bfr.Add_str_a7("</dd>");
		}
		// autovalue
		if (fld_autovalue != Bry_.Empty) {
			bfr.Add_str_a7("<dt>");
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-autovalue"));
			bfr.Add_str_a7("</dt><dd><code>");
			bfr.Add(fld_autovalue);
			bfr.Add_str_a7("</code></dd>");
		}
		bfr.Add_str_a7("</dl></td><td class=\"mw-templatedata-doc-param-type");
		if (fld_type == Bry_.Empty) {
			bfr.Add_str_a7(" mw-templatedata-doc-muted");
		}
		bfr.Add_str_a7("\">");
		if (fld_type == Bry_.Empty) {
			bfr.Add(msg_mgr.Val_by_key_obj("templatedata-doc-param-type-unknown"));
		} else {
			String stype;
			// check for comptibility - starting 'string/'
			if (Bry_.Match(fld_type, 0, 7, string_start)) {
				stype = String_.new_a7(Bry_.Mid(fld_type, 7));
			}
			else
				stype = String_.new_a7(fld_type);
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

	private byte[] get_text_value(Json_itm itm) {
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
	private boolean get_boolean_value(Json_itm itm) { // should be with a language tag
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
