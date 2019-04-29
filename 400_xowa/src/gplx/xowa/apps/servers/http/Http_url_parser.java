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
package gplx.xowa.apps.servers.http; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*; import gplx.xowa.apps.servers.*;
import gplx.core.net.*; import gplx.core.net.qargs.*;
import gplx.langs.htmls.encoders.*;
import gplx.xowa.htmls.hrefs.*;
import gplx.xowa.wikis.pages.*;
class Http_url_parser {
	public byte[] Wiki() {return wiki;} public Http_url_parser Wiki_(String v) {this.wiki = Bry_.new_u8(v); return this;} private byte[] wiki;
	public byte[] Page() {return page;} public Http_url_parser Page_(String v) {this.page = Bry_.new_u8(v); return this;} private byte[] page;
	public byte Action() {return action;} public Http_url_parser Action_(byte v) {this.action = v; return this;} private byte action;
	public String Popup_mode() {return popup_mode;} public Http_url_parser Popup_mode_(String v) {this.popup_mode = v; return this;} private String popup_mode;
	public boolean Popup() {return popup;} public Http_url_parser Popup_(boolean v) {this.popup = v; return this;} private boolean popup;
	public String Popup_id() {return popup_id;} public Http_url_parser Popup_id_(String v) {this.popup_id = v; return this;} private String popup_id;
	public String Err_msg() {return err_msg;} public Http_url_parser Err_msg_(String v) {this.err_msg = v; return this;} private String err_msg;
	public Gfo_qarg_mgr Qarg_mgr() {return qarg_mgr;} private Gfo_qarg_mgr qarg_mgr;

	public String To_str() {
		Bry_bfr bfr = Bry_bfr_.New();
		bfr.Add_str_a7("wiki=").Add_safe(wiki).Add_byte_nl();
		bfr.Add_str_a7("page=").Add_safe(page).Add_byte_nl();
		bfr.Add_str_a7("action=").Add_byte_variable(action).Add_byte_nl();
		bfr.Add_str_a7("popup=").Add_yn(popup).Add_byte_nl();
		bfr.Add_str_a7("err_msg=").Add_str_u8_null(err_msg).Add_byte_nl();
		return bfr.To_str_and_clear();
	}

	// Parse urls of form "/wiki_name/wiki/Page_name?action=val"
	public boolean Parse(byte[] url) {
		try {
			// initial validations
			if (url == null) return Fail(null, "invalid url; url is null");
			int url_len = url.length;
			if (url_len == 0) return Fail(null, "invalid url; url is empty");
			if (url[0] != Byte_ascii.Slash) return Fail(url, "invalid url; must start with '/'");

			Gfo_url_parser url_parser = new Gfo_url_parser();
			Gfo_url url_obj = url_parser.Parse(url);
			this.wiki = url_obj.Segs()[0];

			int segs_len = url_obj.Segs().length;
			Bry_bfr bfr = Bry_bfr_.New();
			int ofs = 1;
			if (segs_len > 1) {
				if (Bry_.Has_at_bgn(url_obj.Segs()[1], wikiname))
					ofs = 2;
				for (int i = ofs; i < segs_len; i++) {
					if (i != ofs) bfr.Add_byte_slash();
					bfr.Add(url_obj.Segs()[i]);
				}
			}
                        // tack the querystring back on
                        int arg_len = url_obj.Qargs().length;
                        if (arg_len > 0) {
                            for (int i = 0; i < arg_len; i++) {
                                if (i == 0)
                                    bfr.Add_byte(Byte_ascii.Question);
                                else
                                    bfr.Add_byte(Byte_ascii.Amp);
                                Gfo_qarg_itm itm = url_obj.Qargs()[i];
                                bfr.Add(itm.Key_bry());
                                bfr.Add_byte_eq();
                                bfr.Add(itm.Val_bry());
                            }
                        }
//                            bfr.Add_mid(url, this.wiki.length+6+bfr.Len()+1, url.length); 
                        this.page = bfr.To_bry_and_clear();
                        
			// Special: pages need to be passed through!
			if (!Bry_.Has_at_bgn(this.page, specialname)) {
				qarg_mgr = new Gfo_qarg_mgr().Init(url_obj.Qargs());
				byte[] action_val = qarg_mgr.Read_bry_or("action", Bry_.Empty);
				if      (Bry_.Eq(action_val, Xoa_url_.Qarg__action__read))
					this.action = Xopg_view_mode_.Tid__read;
				else if (Bry_.Eq(action_val, Xoa_url_.Qarg__action__edit))
					this.action = Xopg_view_mode_.Tid__edit;
				else if (Bry_.Eq(action_val, Xoa_url_.Qarg__action__html))
					this.action = Xopg_view_mode_.Tid__html;
				else if (Bry_.Eq(action_val, Qarg__action__popup)) {
					this.popup = true;
					this.popup_id = qarg_mgr.Read_str_or_null(Bry_.new_a7("popup_id"));
					this.popup_mode = qarg_mgr.Read_str_or_null(Bry_.new_a7("popup_mode"));
				}
			}

			return true;
		}
		catch (Exception e) {
			this.err_msg = Err_.Message_gplx_log(e);
			return false;
		}
	}
	private boolean Fail(byte[] url, String err_msg) {
		this.wiki = null;
		this.page = null;
		this.err_msg = err_msg;
		if (url != null)
			this.err_msg += "; url=" + String_.new_u8(url);
		return false;
	}
	private static final    byte[]
	  Qarg__action__frag = Bry_.Add(Byte_ascii.Question_bry, Xoa_url_.Qarg__action, Byte_ascii.Eq_bry) // "?action="
	, Qarg__action__popup = Bry_.new_a7("popup")
	, wikiname = Bry_.new_a7("wiki")
	, specialname = Bry_.new_a7("Special:")
	;
}
