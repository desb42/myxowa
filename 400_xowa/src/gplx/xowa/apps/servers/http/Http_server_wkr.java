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
package gplx.xowa.apps.servers.http; import gplx.*;
import gplx.xowa.*; import gplx.xowa.apps.*; import gplx.xowa.apps.servers.*;
import gplx.core.ios.*; import gplx.core.ios.streams.*;
import gplx.core.primitives.*; import gplx.core.net.*; import gplx.langs.htmls.encoders.*;
import gplx.xowa.apps.*;
import gplx.xowa.htmls.js.*;
import gplx.xowa.wikis.pages.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.OutputStream; import java.io.FileOutputStream; import java.io.File; import java.io.IOException;
import java.io.BufferedReader; import java.io.*;
import java.util.Stack;
import gplx.xowa.htmls.*;
import gplx.xowa.xtns.pfuncs.times.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import gplx.core.brys.fmtrs.*;
import gplx.xowa.files.Xof_file_wkr_;
import gplx.xowa.files.*;
import gplx.xowa.langs.Xol_lang_stub_;
import gplx.xowa.langs.Xol_lang_itm;
import gplx.xowa.bldrs.xmls.Xob_xml_parser;
import gplx.xowa.htmls.core.makes.Xoh_make_mgr;
import gplx.xowa.guis.views.Xog_tab_itm;
import gplx.langs.jsons.*;
import gplx.xowa.htmls.core.wkrs.lnkis.*;
import gplx.xowa.wikis.nss.Xow_ns;
import gplx.xowa.wikis.pages.lnkis.Xopg_redlink_mgr;
import gplx.core.encoders.Hex_utl_;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class Http_server_wkr implements Gfo_invk {
	private static String rootdir;
	private final    int uid;
	private final    Http_server_mgr server_mgr;
	private final    Http_server_wtr server_wtr;
	private final    Http_client_wtr client_wtr = Http_client_wtr_.new_stream();
	private final    Http_client_rdr client_rdr = Http_client_rdr_.new_stream();
	private final    Http_request_parser request_parser;
	private final    Gfo_url_encoder url_encoder;
	private final    Xoae_app app;
	private final    String root_dir_http;
	private static   byte[] root_dir_fsys;
	private final    Bry_bfr tmp_bfr = Bry_bfr_.New_w_size(64);
	private Socket_adp socket;
	private Http_data__client data__client;
	private Xosrv_http_wkr_ response;
	private final    Gfo_url_parser url_parser = new Gfo_url_parser();
	public Http_server_wkr(Http_server_mgr server_mgr, int uid){
		this.server_mgr = server_mgr; this.uid = uid;
		this.app = server_mgr.App(); this.server_wtr = server_mgr.Server_wtr(); this.url_encoder = server_mgr.Encoder();
		this.root_dir_http = app.Fsys_mgr().Root_dir().To_http_file_str();
		this.root_dir_fsys = app.Fsys_mgr().Root_dir().RawUnixBry();
		this.request_parser = server_mgr.Request_parser();
		rootdir = app.Fsys_mgr().Root_dir().toString();
	}
	public void Init_by_thread(Socket_adp socket) {
		this.socket = socket;
	}
	public void Run(){
		synchronized (app) { // LOCK:else http_server may sometimes deadlock when serving multiple parallel requests; // DATE:2018-03-11
			Http_request_itm request = null;
			try {
				client_rdr.Stream_(socket.Get_input_stream());
				client_wtr.Stream_(socket.Get_output_stream());
				request = request_parser.Parse(client_rdr);
				this.data__client = new Http_data__client(request.Host(), socket.Ip_address());
                                this.response = new Xosrv_http_wkr_();
				byte[] url_bry = request.Url();
				if (Bry_.Eq(url_bry, Url__home)) url_bry = server_mgr.Home();	// "localhost:8080" comes thru as url of "/"; transform to custom home page; DATE:2015-10-11
				switch (request.Type()) {
					case Http_request_itm.Type_get:		Process_get(request, url_bry); break;
					case Http_request_itm.Type_post:	Process_post(request, url_bry); break;
				}
				client_wtr.Rls(); // client_rdr.Rls(); socket.Rls();
			}
			catch (Exception e) {
				String request_str = request == null ? "<<NULL>>" : request.To_str(Bool_.N);
				server_wtr.Write_str_w_nl(String_.Format("failed to process request;\nrequest={0}\nerr_msg={1}", request_str, Err_.Message_gplx_full(e)));
			}
			finally {
				if (uid != -1) {	// only release if uid was acquired; DATE:2015-10-11
					server_mgr.Wkr_pool().Del(uid);
					server_mgr.Uid_pool().Del(uid);
				}
			}
		}
	}
	private void Process_get(Http_request_itm request, byte[] url) {
		server_wtr.Write_str_w_nl(String_.new_u8(request.Host()) + "|GET|" + String_.new_u8(request.Url()));	// use request url
		if		(Bry_.Has_at_bgn(url, Url__fsys) || Bry_.Has_at_bgn(url, Url__static))	Serve_file(url);
		else if (Bry_.Has_at_bgn(url, Url__exec))	Exec_exec(url, Url__exec);
		else if (Bry_.Has_at_bgn(url, Url__exec_2))	Exec_exec(url, Url__exec_2);
		else										Write_wiki(url);
	}
	private void Serve_file(byte[] url) {
		tmp_bfr.Clear().Add(root_dir_fsys);	// add "C:\xowa\"
		int question_pos = Bry_find_.Find_fwd(url, Byte_ascii.Question);
		int url_bgn = Bry_.Has_at_bgn(url, Url__fsys) ? Url__fsys_len : 0;	// most files will have "/fsys/" at start, but Mathjax will not
		int url_end = question_pos == Bry_find_.Not_found ? url.length : question_pos;	// ignore files with query params; EX: /file/A.png?key=val
		byte[] path = url_encoder.Decode(Bool_.N, url, url_bgn, url_end);		// decode url to actual chars; note that XOWA stores on fsys in UTF-8 chars; "�" not "%C3"
		if (gplx.core.envs.Op_sys.Cur().Tid_is_wnt()) path = Bry_.Replace(path, Byte_ascii.Backslash, Byte_ascii.Slash);
		client_wtr.Write_bry(Xosrv_http_wkr_.Rsp__http_ok);
		// 	client_wtr.Write_str("Expires: Sun, 17-Jan-2038 19:14:07 GMT\n");
		String mime_type = String_.new_u8(Http_file_utl.To_mime_type_by_path_as_bry(path));
		client_wtr.Write_str("Content-Type: " + mime_type + "\n\n");
		Io_stream_rdr file_stream = Io_stream_rdr_.New_by_url(Io_url_.new_fil_(String_.new_u8(path))).Open();
		client_wtr.Write_stream(file_stream);
		file_stream.Rls(); client_rdr.Rls(); socket.Rls();
	}
	private void Exec_exec(byte[] url, byte[] tkn_bgn) {
		byte[] cmd = Bry_.Mid(url, tkn_bgn.length);
		app.Http_server().Run_xowa_cmd(app, String_.new_u8(cmd));
	}
	private void Write_wiki(byte[] req) {
		Http_url_parser url_parser = new Http_url_parser();
		String page_html = "";
		if (!url_parser.Parse(req)) {
			page_html = url_parser.Err_msg();
		}
		else {
			byte[] wikitext = null;
			Db_wikistrip ws = new Db_wikistrip();
			Http_server_page page = app.Http_server().Parse_page_to_html(data__client, url_parser);
			if (page.Redirect() != null) {
				response.Write_redirect(client_wtr, page.Redirect());
				return;
			}
			else if (url_parser.Action() == Xopg_view_mode_.Tid__firstpara) {
		synchronized (app) {
				if (page.Page() != null) {
					wikitext = page.Page().Db().Text().Text_bry();
					page.Wiki().Parser_mgr().Ctx().Page().Ttl_(page.Ttl());	// NOTE: must set cur_page, else page-dependent templates won't work; EX: {{FULLPAGENAME}};
				}
				byte[] thumb = null;
				byte[] orig = null;
				Db_page_image_ pi = page.Wiki().Page_image().Get_page_image(page.Page().Db().Page().Id());
				Bry_bfr bfr = Bry_bfr_.New();
				byte[] stype = Bry_.new_a7("standard");
				if (pi.height > 0) {
					Xoa_ttl ttl = Xoa_ttl.Parse(page.Wiki(), pi.pi_title);
					Xof_ext ext = Xof_ext_.new_by_ttl_(pi.pi_title);
					byte[] fname = ttl.Full_db_href();
					byte[] site = Bry_.new_a7("commons");
					if (pi.site_id == 1 || pi.site_id == 11)
						site = Bry_.new_a7("en"); // should be the wiki language?
					byte[] md5 = Xof_file_wkr_.Md5(pi.pi_title);
					byte[] md5_subdir = Bry_.Add(Bry_.Mid(md5, 0, 1), Byte_ascii.Slash_bry, Bry_.Mid(md5, 0, 2));
					int view_id = Xof_ext_.Id_view(ext.Id());
					byte[] extension = view_id == Xof_ext_.Id_png ? Bry_.new_a7(".png") : 
						view_id == Xof_ext_.Id_jpg ? Bry_.new_a7(".jpg") : Bry_.Empty;
					thumb_fmtr.Bld_bfr_many(bfr, site, md5_subdir, fname, extension, pi.width/2, pi.height/2);
					thumb = bfr.To_bry_and_clear();
    //thumb = Bry_.new_u8("\"thumbnail\": {\"source\": \"/xowa/api/wikipedia/en/thumb/1/12/Flag_of_Poland.svg/40px-Flag_of_Poland.svg.png\",\"width\":640,\"height\":400},");
					orig_fmtr.Bld_bfr_many(bfr, site, md5_subdir, fname, pi.width, pi.height);
					orig = bfr.To_bry_and_clear();
    //orig = Bry_.new_u8("\"originalimage\":{\"source\":\"/xowa/api/wikipedia/en/1/12/Flag_of_Poland.svg\",\"width\":1280,\"height\":800},");
				}
				else if (pi.height < 0) {
					stype = Bry_.new_a7("disambiguation");
				}
				byte[] page_title = page.Ttl().Full_db_wo_ns();
				wikitext = ws.First_para(wikitext, page.Ttl(), page.Wiki());
				json_fmtr.Bld_bfr_many(bfr, 
					    stype, page_title, page.Page().Db().Page().Id(), 
					    thumb, orig, wikitext,
					    page.Wiki().Lang().Key_bry(), page.Wiki().Lang().Dir_ltr_bry());

				page_html = String_.new_u8(bfr.To_bry());
		}
			}
			else {
				page_html = page.Html();
				boolean show_stripped = /*true;*/false;
				if (show_stripped) {
					byte[] stripped = null;
					if (page.Page() != null && page.Page().Db().Page().Format() == Xob_xml_parser.Format_wiki) { //1 <format>text/x-wiki</format>
						wikitext = page.Page().Db().Text().Text_bry();
						page.Wiki().Parser_mgr().Ctx().Clear_all();
						page.Wiki().Parser_mgr().Ctx().Page().Ttl_(page.Ttl());	// NOTE: must set cur_page, else page-dependent templates won't work; EX: {{FULLPAGENAME}};
						stripped = ws.Search_text(wikitext, page.Ttl(), page.Wiki()); //.Strip_wiki(wikitext);
					}
					if (stripped != null) {
						page_html += "----\n" + String_.new_u8(stripped);
						page_html += "----\n" + String_.new_u8(ws.First_para(wikitext, page.Ttl(), page.Wiki()));
					}
				}
			}
			if (page_html == null) {
				page_html = "Strange! no data";
			} else {
                //test_redis();
                //test_sql(page.Ttl(), app);

				page_html = Convert_page(page_html, root_dir_http, String_.new_u8(url_parser.Wiki()), page.Redlink());
                                
                //Db_Nav_template nt = new Db_Nav_template();
                //nt.Init(page.Wiki());

//20220214					page.Wiki().Cache_mgr().Free_mem__page();
//					page.Wiki().Parser_mgr().Scrib().Core_term();
//					page.Wiki().Appe().Wiki_mgr().Wdata_mgr().Clear();

                if (url_parser.Action() == Xopg_view_mode_.Tid__edit) { // change some more things
					//page_html = String_.Replace(page_html, "name=\"editform\">"	, "name=\"editform\" method=\"post\" enctype=\"multipart/form-data\" action=\"/" + String_.new_u8(url_parser.Wiki()) + "/wiki/" + String_.new_u8(url_parser.Page()) + "?action=submit\">");
					page_html = String_.Replace(page_html, "name=\"editform\">"	, "name=\"editform\" method=\"post\" enctype=\"multipart/form-data\">");
					page_html = String_.Replace(page_html, "<a href=\"/exec/app.gui.main_win.page_edit_preview;\""	, "<input type='submit' tabindex='4' title='Preview your changes. Please use this before saving.' name='wpPreview' id='wpPreview' value='Show preview' class='oo-ui-inputWidget-input oo-ui-buttonElement-button' /><a href=\"/exec/app.gui.main_win.page_edit_preview;\"");
					page_html = String_.Replace(page_html, "<textarea", "<textarea name=\"wpTextbox1\"");
				}
			}
			response.Set_content_type(page.Content_type());
			response.Set_content_lang(page.Wiki().Lang());
                        if (page.Wiki().Utl__bfr_mkr().Check()) {
                            int a=1;
                        }
/*//testing*/                        page.Wiki().Cache_mgr().Page_cache().Cleanup();
/*//testing*/		Xowe_wiki_.Rls_mem(page.Wiki(), true);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!test
		}
		Db_readwrite.writeFile(page_html, rootdir + "html.htm");
		response.Write_response_as_html(client_wtr, Bool_.N, page_html);
	}
	private void Process_post(Http_request_itm request, byte[] url_bry) {
		int question_pos = Bry_find_.Find_fwd(url_bry, Byte_ascii.Question);
		if (question_pos == Bry_find_.Not_found)
			Process_exec_post(request);
		else
			Process_other_post(request, url_bry, question_pos);
	}
	private void Process_other_post(Http_request_itm request, byte[] url_bry, int question_pos) {
		Http_url_parser url_parser = new Http_url_parser();
		String page_html = "";
		if (!url_parser.Parse(url_bry)) {
			page_html = url_parser.Err_msg();
		}
		else if (request.Post_data_hash().Has(Key__tbox)) {
			byte[] msg = request.Post_data_hash().Get_by(Key__tbox).Val();
			
			byte[] req = url_encoder.Decode(Bool_.N, url_bry, 0, question_pos);
			String wiki_domain = ""; String page_name = "";
			String[] req_split = String_.Split(String_.new_u8(req), "/");
			if(req_split.length >= 1){
				wiki_domain = req_split[1];
			}
			if (req_split.length >= 4) {
				page_name = req_split[3];
				for(int i = 4; i <= req_split.length-1; i++){
					page_name += "/" + req_split[i];
				}
			}
			server_wtr.Write_str_w_nl(String_.new_u8(request.Host()) + "|POST|" + page_name);
			page_html = app.Http_server().Preview_page_to_html(data__client, Bry_.new_u8(wiki_domain), Bry_.new_u8(page_name), msg);
			page_html = Convert_page(page_html, root_dir_http, wiki_domain, "");
			response.Write_response_as_html(client_wtr, Bool_.N, page_html);
		}
		else if (url_parser.Redlink_mode()) {
			byte[] msg = request.Post_data_hash().Get_by(Bry_.new_a7("data")).Val();
			Json_parser jdoc_parser = new Json_parser();
			Json_doc jdoc = jdoc_parser.Parse(msg);
			Json_nde root = jdoc.Root_nde();
			int nlen = root.Len();
			page_html = "[";
			int redcount = 0;
			for (int i = 0; i < nlen; i++) {
				Json_kv org = root.Get_at_as_kv(i);
				Xowe_wiki wiki = (Xowe_wiki) app.Wiki_mgr().Get_by_or_make_init_y(org.Key_as_bry());
				byte[] ttl_bry = wiki.Props().Main_page();
				Xoa_url url = wiki.Utl__url_parser().Parse(ttl_bry);
				Xoa_ttl ttl = wiki.Ttl_parse(url.To_bry_page_w_anch());
				Xoae_page page = Xoae_page.New(wiki, ttl);
	
				Json_ary ary = org.Val_as_ary();
				int alen = ary.Len();
				for (int j = 0; j < alen; j++) {
					Json_nde nde = ary.Get_at_as_nde(j);
					Json_kv kv_name = nde.Get_at_as_kv(0); // "href"
					Json_kv kv_item = nde.Get_at_as_kv(1); // "item"
					byte[] itm_ttl_bry = kv_name.Val_as_bry();
					Xoa_ttl itmttl = Xoa_ttl.Parse(wiki, itm_ttl_bry);
					if (itmttl == null) {
						continue;
					}
					int itmno = Integer.parseInt(String_.new_a7(kv_item.Val_as_bry())); // ?? find the real way of doing this
					Xopg_lnki_itm__hdump itm = new Xopg_lnki_itm__hdump(itmttl);
					itm.Html_uid_(itmno);
					// repeated from page.Html_data().Redlink_list()
					Xow_ns ns = itmttl.Ns();
					if ( ns.Id_is_file_or_media()						// ignore files which will usually not be in local wiki (most are in commons), and whose html is built up separately
						|| (ns.Id_is_ctg() && !itmttl.ForceLiteralLink())		// ignore ctgs which have their own html builder, unless it is literal; EX: [[:Category:A]]; DATE:2014-02-24
						|| ns.Id_is_special()								// ignore special, especially Search; EX: Special:Search/Earth
						|| itmttl.Anch_bgn() == 1			// anchor only link; EX: [[#anchor]]
						|| itmttl.Wik_itm() != null							// xwiki lnki; EX: simplewiki links in homewiki; [[simplewiki:Earth]]
						|| itmttl.Qarg_bgn() != -1
						|| itmttl.Full_db()[0] == '/'
					) {
						// nothing
					}
					else
						page.Html_data().Redlink_list().Add_direct(itm);
				}
				byte[] redlinks = null;
				Bry_bfr tmp_bfr = Bry_bfr_.New();
				Xopg_redlink_mgr red_mgr = new Xopg_redlink_mgr(page, null);
				red_mgr.Redlink(tmp_bfr);
				redlinks = tmp_bfr.To_bry_and_clear();
				int redlen = redlinks.length;
				if (redlen > 0) {
					if (redcount > 0)
						page_html += ",";
					page_html += String_.new_a7(redlinks, 1, redlen);
					redcount++;
				}
			}
			page_html += "]";
			response.Set_content_type(Http_server_page.json_mime);
			response.Write_response_as_html(client_wtr, Bool_.N, page_html);
		}
		else if (url_parser.Image_mode()) {
			Xowe_wiki wiki = (Xowe_wiki) app.Wiki_mgr().Get_by_or_make_init_y(url_parser.Wiki());
			Xoh_make_mgr make_mgr = Xoh_make_mgr.New_make();
			Xoh_page pge = new Xoh_page();
			pge.Ctor_by_hview(wiki, Xoa_url.Null, Xoa_ttl.Parse(wiki, Bry_.new_a7("Test")), 0);
			
			byte[] ttl_bry = wiki.Props().Main_page();
//		url_parser.Is_main_page_set();
			Xoa_url url = wiki.Utl__url_parser().Parse(ttl_bry);
			Xoa_ttl ttl = wiki.Ttl_parse(url.To_bry_page_w_anch());
			Xog_tab_itm tab = Gxw_html_server.Assert_tab2(app, wiki);
			Xoae_page page = Xoae_page.New(wiki, ttl);

			byte[] msg = request.Post_data_hash().Get_by(Bry_.new_a7("data")).Val();
			Json_parser jdoc_parser = new Json_parser();
			Json_doc jdoc = jdoc_parser.Parse(msg);
			Json_ary ary = jdoc.Root_ary();
			int len = ary.Len();
			page_html = "[";
			for (int i = 0; i < len; i++) {
				Json_nde nde = ary.Get_as_nde(i);
				Json_kv kv_txt = nde.Get_at_as_kv(0);
				Json_kv kv_itm = nde.Get_at_as_kv(1);
                               
			//Xoae_page page = wiki.Page_mgr().Load_page(url, ttl, tab, url_parser.Display(), url_parser.Action());
			
				int j = 0;
				byte[] html = null;
				String page_item = "{\"error\":\"no match\",\"item\":\"" + String_.new_a7(kv_itm.Val_as_bry()) + "\"}";

				while (j < 2) {
					page.File_queue().Clear();
					pge.Img_mgr().Clear();
					html = make_mgr.Parse(kv_txt.Val_as_bry(), wiki, pge);
					Xoh_img_mgr src_imgs = pge.Img_mgr();
					int xlen = src_imgs.Len();
					if (xlen == 1) {
						gplx.xowa.files.Xof_fsdb_itm itm = src_imgs.Get_at(0);
						if (itm.Orig_ttl() == null) {
							break;
						}
                                                //else if (itm.Html_w() <= 0) {
                                                //    // report as bad!
                                                //    System.out.println("width 0- " + String_.new_u8(itm.Lnki_ttl()));
                                                //    break;
                                                //}
						if (!Io_mgr.Instance.ExistsFil(itm.Html_view_url())) {
							page.File_queue().Add(itm);
							page.File_queue().Exec(wiki, page);
						} else {
							int root_len = wiki.App().Fsys_mgr().File_dir().Raw().length() + 8; // 'file:///'
							System.out.println("exist " + String_.new_u8(itm.Html_view_url().To_http_file_bry()));
							page_item = "{\"width\":" + Integer.toString(itm.Html_w())
							        + ",\"height\":" + Integer.toString(itm.Html_h())
							        + ",\"src\":\"/xowa/fsys/file/" + String_.new_u8(Bry_.Mid(itm.Html_view_url().To_http_file_bry(), root_len))
							        + "\",\"item\":\"" + String_.new_a7(kv_itm.Val_as_bry()) + "\"}";
							break;
						}
					}
					j += 1;
				}
				if (i > 0)
					page_html += ",";
				page_html += page_item;
			}
			page_html += "]";
		//Db_readwrite.writeFile(page_html, rootdir + "images.json");
			response.Set_content_type(Http_server_page.json_mime);
			response.Write_response_as_html(client_wtr, Bool_.N, page_html);
		}
	}
	private static final    byte[] Key__tbox = Bry_.new_a7("wpTextbox1");
	private void Process_exec_post(Http_request_itm request) {
		byte[] msg = request.Post_data_hash().Get_by(Key__msg).Val();
		byte[] app_mode = request.Post_data_hash().Get_by(Key__app_mode).Val();
		Xoa_app_mode app_mode_itm = Xoa_app_mode.parse(String_.new_u8(app_mode));
		server_wtr.Write_str_w_nl(String_.new_u8(request.Host()) + "|POST|" + String_.new_u8(msg));
		Object url_tid_obj = post_url_hash.Get_by_bry(request.Url());
                if (url_tid_obj == null)
                    throw Err_.new_wo_type("unknown url", "url", request.Url(), "request", request.To_str(Bool_.N));
		String rv = null;
		switch (((Int_obj_val)url_tid_obj).Val()) {
			case Tid_post_url_json:
				rv = app.Html__bridge_mgr().Cmd_mgr().Exec(msg);
				break;
			case Tid_post_url_gfs:
				rv = Object_.Xto_str_strict_or_null_mark(app.Gfs_mgr().Run_str(String_.new_u8(msg)));
				break;
		}
		if (app_mode_itm.Tid_is_http())
			rv = Convert_page(rv, root_dir_http, "<<MISSING_WIKI>>", "");
		response.Write_response_as_html(client_wtr, app_mode_itm.Tid() == Xoa_app_mode.Itm_file.Tid(), rv);
	}
	private static final    byte[] Key__msg = Bry_.new_a7("msg"), Key__app_mode = Bry_.new_a7("app_mode");
	private static final int Tid_post_url_json = 1, Tid_post_url_gfs = 2;
	private static final    Hash_adp_bry post_url_hash = Hash_adp_bry.ci_a7()
	.Add_str_int("/exec/json"	, Tid_post_url_json)
	.Add_str_int("/exec/gfs"	, Tid_post_url_gfs)
	;
	private static String karto = "<span title=\"Map for this &#39;listing&#39; marker\"><a class=\"mw-kartographer-maplink mw-kartographer-autostyled\" mw-data=\"interface\" data-style=\"osm-intl\" href=\"/wiki/Special:Map/17/37.8013/-122.3988/en\" data-zoom=\"17\" data-lat=\"37.8013\" data-lon=\"-122.3988\" style=\"background: #228B22;\" data-overlays=\"[&quot;mask&quot;,&quot;around&quot;,&quot;buy&quot;,&quot;city&quot;,&quot;do&quot;,&quot;drink&quot;,&quot;eat&quot;,&quot;go&quot;,&quot;listing&quot;,&quot;other&quot;,&quot;see&quot;,&quot;sleep&quot;,&quot;vicinity&quot;,&quot;view&quot;,&quot;black&quot;,&quot;blue&quot;,&quot;brown&quot;,&quot;chocolate&quot;,&quot;forestgreen&quot;,&quot;gold&quot;,&quot;gray&quot;,&quot;grey&quot;,&quot;lime&quot;,&quot;magenta&quot;,&quot;maroon&quot;,&quot;mediumaquamarine&quot;,&quot;navy&quot;,&quot;red&quot;,&quot;royalblue&quot;,&quot;silver&quot;,&quot;steelblue&quot;,&quot;teal&quot;,&quot;fuchsia&quot;]\">1</a>&#32;</span>";
	//private static Pattern pwiki = Pattern.compile("https?://(commons\\.wikimedia|de\\.wikipedia|en\\.wikibooks|en\\.wikinews|en\\.wikipedia|en\\.wikiquote|en\\.wikisource|en\\.wikiversity|en\\.wikivoyage|en\\.wiktionary|fr\\.wikipedia|fr\\.wikisource|he\\.wikipedia|it\\.wikipedia|it\\.wikisource|ja\\.wikipedia|simple\\.wikipedia|species\\.wikimedia|www\\.wikidata)\\.org");
	//private static Matcher mwiki = pwiki.matcher("");
	private static String Convert_page(String page_html, String root_dir_http, String wiki_domain, String redlink) {
		//page_html = String_.Replace(page_html, root_dir_http		, "/fsys/");
		page_html = String_.Replace(page_html, "xowa-cmd:"			, "/exec/");
		//page_html = String_.Replace(page_html, " href=\"/wiki/"	, " href=\"/xowa/" + wiki_domain + "/wiki/");
		page_html = String_.Replace(page_html, "\"/wiki/"	, "\"/xowa/" + wiki_domain + "/wiki/");
		page_html = String_.Replace(page_html, " href='/wiki/"	, " href='/xowa/" + wiki_domain + "/wiki/");
		//page_html = String_.Replace(page_html, "<area href=\"/wiki/"	, "<area href=\"/" + wiki_domain + "/wiki/");
		//page_html = String_.Replace(page_html, "action=\"/wiki/"	, "action=\"/xowa/" + wiki_domain + "/wiki/");
		page_html = String_.Replace(page_html, "/site"				, "/xowa");
		// big HACK to remove a page break at the beginning (20210522)
//20220207		page_html = String_.Replace(page_html, "<div class=\"mw-parser-output\">\n\n<p><br />\n</p>", "<div class=\"mw-parser-output\">");
		// should check to see if these have been downloaded somehow
		//page_html = page_html.replaceAll("https?://(commons\\.wikimedia|de\\.wikipedia|en\\.wikibooks|en\\.wikinews|en\\.wikipedia|en\\.wikiquote|en\\.wikisource|en\\.wikiversity|en\\.wikivoyage|en\\.wiktionary|fr\\.wikipedia|fr\\.wikisource|he\\.wikipedia|it\\.wikipedia|it\\.wikisource|ja\\.wikipedia|simple\\.wikipedia|species\\.wikimedia|www\\.wikidata)\\.org" , "/xowa/$1.org");
		//page_html = mwiki.reset(page_html).replaceAll("/xowa/$1.org");
		//page_html = String_.Replace(page_html, "https://www.wikidata.org"	, "/www.wikidata.org");
		//page_html = String_.Replace(page_html, "https://commons.wikimedia.org"	, "/commons.wikimedia.org");
		//page_html = String_.Replace(page_html, "https://en.wikipedia.org"	, "/en.wikipedia.org"); // eg en.wikiquote.org/wiki/Leo_Varadkar
		//page_html = String_.Replace(page_html, "https://" + wiki_domain	, "/" + wiki_domain);
		//page_html = page_html.replaceAll("(https?:)?//upload.wikimedia.org" , "/xowa/fsys/bin/any/xowa/upload.wikimedia.org"); // handled in Template_style_nde.java

		//page_html = String_.Replace(page_html, "/fsys/file/commons.wikimedia.org/thumb/2/1/1/9/Speaker_Icon.svg/20px.png", "/fsys/file/commons.wikimedia.org/orig/2/1/1/9/Speaker_Icon.svg");
		//page_html = page_html.replaceAll("\n +", "\n");
		page_html = String_.Replace(page_html, ".3B", ";");
		page_html = String_.Replace(page_html, "\\3B", ";");
		page_html = String_.Replace(page_html, "\\3A", ":");
		//page_html = collapser(page_html);
		page_html = tablecaption(page_html);
		page_html = page_html.replaceAll("</p>\\s*<p>", "</p><p>");
		page_html = page_html.replaceAll("</div>\\s*<div", "</div><div");
		page_html = page_html.replaceAll("(<pre[^>]*?>)\n", "$1");

                page_html = page_html.replaceAll("<nowiki>(.*?)</nowiki>", "$1"); // HACK until <nowiki> sorted
		//[replaced]page_html = page_html.replaceAll("\"file\\:.*?/file/", "\"/fsys/file/");

		//page_html = page_html.replaceAll("\"/xowa/fsys/file/([^\"]*?\\.svg)/([0-9]*)px.*?\"", "\"/SVG/$1\"");
		//page_html = page_html.replaceAll("\"/xowa/fsys/file/([^\"]*?\\.djvu)/440px-([0-9]*).jpg\"", "\"/DJVU/$1-$2\""); // djvu/440px-16.jpg

		//page_html = blockquote(page_html);
		//page_html = String_.Replace(page_html, "\"mw-parser-output\">", karto);
		//Xoh_css_minify mini = new Xoh_css_minify();
		//String css = mini.readFileAsString(rootdir + "csstest.txt");
		//System.out.println(mini.cssmin(css, 0));
		//perform();
                //page_html += test_jdecode();
                //test_scanner();
                /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
		Date d = null;
		try 	{d = sdf.parse("2016-02-01 18:34:08");}
		catch 	(ParseException e) { }

                DateAdp dte = DateAdp_.parse_fmt("2016-02-01 18:34:08", "yyyy-MM-dd HH:mm:ss");
                long timestamp = dte.Timestamp_unix();*/
                //page_html += test_wikistrip();
                
                // TEST case insensitivity
                //Db_btrie_ci_trie_de de = new Db_btrie_ci_trie_de(null);
                //byte[] bb = Bry_.new_u8("füllenrechts");
                //byte[] bb = Bry_.new_u8("FÜLLENRECHTS");
                //byte[] bb = Bry_.new_u8("dateipfad");
                //byte[] bb = Bry_.new_u8("vollständige_url");
                //byte[] bb = Bry_.new_u8("VOLLSTÄNDIGE_URL");
                //de.Match_bgn(bb, 0, bb.length);
                
                page_html = page_html.replace("redlinks = [\"\"]", "redlinks = [\"\"" + redlink + "]");
                // 20211214 somehow this is happening?!
                page_html = page_html.replaceAll(" »(\\s|<|\\))", "&#160;»$1");
                page_html = page_html.replaceAll("(\\s|>|\\()« ", "$1«&#160;");
		return page_html;
	}
        private static String collapser(String html)
        {
            String typ;
            String rep;
            Pattern p = Pattern.compile("( collapsible ([^ ]*)|\"collapsible ([^\"]*)\")");
            Matcher m = p.matcher(html);
            StringBuffer sb = new StringBuffer();
            int colcount = 0;
            while (m.find())
            {
                if (m.group(2) != null)
                {
                    System.out.println(m.group(2));
                    typ = m.group(2);
                    colcount++;
                    if (typ.equals("collapsed"))
                        rep = " collapsible mw-collapsed";
                    else if (typ.equals("autocollapse") && colcount > 1)
                        rep = " collapsible mw-collapsed";
                    else
                        rep = " collapsible " + typ;
                }
                else
                {
                    typ = m.group(3);
                    if (typ.equals("collapsed"))
                        rep = " collapsible mw-collapsed";
                    else
                        rep = " collapsible " + typ + "\"";
                }
                m.appendReplacement(sb, rep);
            }
            m.appendTail(sb);
            return sb.toString();
        }
        private static String tablecaption(String html)
        {
            String rep;
            //Pattern p = Pattern.compile("<tr([^>]*)>([^<]*)<\\/tr>[^<]*<caption>(.*?)<\\/caption>[^<]*<tr>(.*?)</table>", Pattern.DOTALL);
            //Pattern p = Pattern.compile("<tr([^>]*)>([^<]*)<\\/tr>[^<]*<caption>(.*?)<\\/caption>[^<]*<tr>", Pattern.DOTALL);
            Pattern p = Pattern.compile("<tr([^>]*)>([^<]*)<\\/tr>[^<]*<caption>(.*?)<\\/caption>[^<]*<tr([^>]*)?>", Pattern.DOTALL);
            Matcher m = p.matcher(html);
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            while (m.find())
            {
                found = true;
                //rep = "<caption>" + m.group(3) + "</caption>\n<tbody><tr" + m.group(1) + ">" + m.group(2) + m.group(4) + "</tbody></table>";
                if (m.group(4) == "") {
            	  rep = "<caption>" + m.group(3) + "</caption>\n<tr" + m.group(1) + ">" + m.group(2);
                } else {
            	  rep = "<caption>" + m.group(3) + "</caption>\n<tr" + m.group(4) + ">";
                }
                m.appendReplacement(sb, rep);
            }
            if (found) {
                m.appendTail(sb);
                return sb.toString();
            } else {
                return html;
            }
        }
        private static String blockquote(String html)
        {
            String typ;
            String rep;
            Pattern p = Pattern.compile("<blockquote([^>]*)>(.*?)</blockquote>", Pattern.DOTALL);
            Matcher m = p.matcher(html);
            StringBuffer sb = new StringBuffer();
            boolean found = false;
            while (m.find())
            {
                found = true;
                String str = m.group(2);
                str = str.replaceAll("<br( /)?>", "\n\n");
                str = str.replaceAll("\n\n", "</p><p>");
                str = "<blockquote" + m.group(1) + "><p>" + str + "</p></blockquote>";
                m.appendReplacement(sb, str);
            }
            if (found) {
                m.appendTail(sb);
                return sb.toString();
            } else {
                return html;
            }
        }
    // time that tick() was called
    static long tickTime;

    // called at start of operation, for timing
    static void tick () {
        tickTime = System.nanoTime();
    }

    // called at end of operation, prints message and time since tick().
    static void tock (String action) {
        long mstime = (System.nanoTime() - tickTime) / 1000000;
        System.out.println(action + ": " + mstime + "ms");
    }
	public static void test_sql(Xoa_ttl ttl, Xoae_app app) {
		try {
                    Db_sql_mgr mgr = app.Sqlx_mgr();
                    //String wiki = "simple.wikipedia.org";
                    String wiki = "en.wikipedia.org";
			String rootdr = "D:/des/xowa_x/wiki/" + wiki + "/";
			String coredb = wiki + "-core.xowa";
                        Db_sql_cursor cur = mgr.Make_cursor(rootdr, coredb);
			int page_id = cur.Seek_index("page__title", ttl.Page_db_as_str(), ttl.Ns().Id());
			if (page_id == 0)
				return; // not found
			Db_record page_rec = cur.Seek_rowid("page", page_id);
			int redirect_id = (int)page_rec.Get_at(9); // page_redirect_id 9
			if (redirect_id > 0) {
                            page_id = redirect_id;
				page_rec = cur.Seek_rowid("page", redirect_id);
                        }
			// page_text_db_id 7      page_html_db_id 8
			// page_len 5             page_html_len 13
			// page_text_db_offset 12 page_html_db_offset 14
			byte[] wikizip;
			int page_text_db_id = (int)page_rec.Get_at(7);
			int page_text_db_offset = (int)page_rec.Get_at(12);
			if (page_text_db_offset > 0) {
				int page_len = (int)page_rec.Get_at(5);
				//wikizip = core.Textfind(page_text_db_id, page_text_db_offset, page_len);
                                wikizip = null;
			}
			else {
				Db_record xowa_db_rec = cur.Seek_rowid("xowa_db", page_text_db_id);
				Db_sql_cursor text_cur = mgr.Make_cursor(rootdr, (String)xowa_db_rec.Get_at(2));
				Db_record text_rec = text_cur.Seek_rowid("text", page_id);
				wikizip = (byte[])text_rec.Get_at(1);
			}
                        byte[] wikitext = app.Zip_mgr().Unzip(Io_stream_tid_.Tid__gzip, wikizip);
                        int a=1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static byte[] Load_from_file_as_bry(String url_str) {
		// get reader for file
		InputStream stream = null;		
		try 	{stream = new FileInputStream(url_str);}
		catch 	(FileNotFoundException e) {
			throw Err_.new_exc(e, "io", "file not found", "file", url_str).Trace_ignore_add_1_();
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int read = 0;
		try {
			while ((read = stream.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, read);
			}
			buffer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}
	private static void test_scanner() {
            DateAdp ret = Dbx_scan_support.Parse(Bry_.new_a7("10 jun 2010"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("jun 12"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("10a.m."));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("12 jun"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("8451-W48-1"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("+1year"));
            // hour12 ":" minutelz ":" secondlz [:.] [0-9]+ meridian
            ret = Dbx_scan_support.Parse(Bry_.new_a7("9:32:45.345 am"));
            //ret = Dbx_scan_support.Checkdatstr(Bry_.new_a7("first day of june"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("second day of june"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("09:32:45.345 am"));
            ret = Dbx_scan_support.Parse(Bry_.new_a7("june +2 weeks 1day"));
            int a = 1;
        }
	/*private static String test_jdecode() {
            byte[] jstream = Load_from_file_as_bry(rootdir + "json_test.dat");
            Db_JDecode dc = new Db_JDecode(jstream);
            gplx.langs.jsons.Json_doc jdoc = dc.Decode();
            Bry_bfr tmp_bfr = Bry_bfr_.Reset(255);
            jdoc.Root_nde().Print_as_json(tmp_bfr, 0);
            return tmp_bfr.To_str();
	}*/
        private JedisPool redisPool;
	private void test_redis() {
       //Connecting to Redis server on localhost 
       if (redisPool == null) {
       redisPool = new JedisPool(new JedisPoolConfig());
       }
/*
      Jedis jedis = new Jedis(); 
      System.out.println("Connection to server sucessfully"); 
      //check whether server is running or not 
      System.out.println("Server is running: "+jedis.ping()); 
      //set the data in redis string 
      jedis.set("tutorial-name", "Redis tutorial"); 
      // Get the stored data and print it 
      System.out.println("Stored string in redis:: "+ jedis.get("tutorial-name")); 
 */
        setfrompool(Bry_.new_a7("fredkey"), Bry_.new_a7("teststring"));
        System.out.println(String_.new_u8(getfrompool(Bry_.new_a7("fredkey"))));
        }
	private void setfrompool(byte[] key, byte[] value) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			redis.set(key, value);
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
                        System.out.println("no connection SET");
			//throw e;
                        return;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}

	private byte[] getfrompool(byte[] key) {
		Jedis redis = null;
		try {
			redis = redisPool.getResource();
			return redis.get(key);
		} catch (JedisConnectionException e) {
			if (redis != null) {
				redisPool.returnBrokenResource(redis);
				redis = null;
			}
                        System.out.println("no connection GET");
			//throw e;
                        return null;
		} finally {
			if (redis != null) {
				redisPool.returnResource(redis);
			}
		}
	}

        private static String test_wikistrip() {
            byte[] wiki = Load_from_file_as_bry(rootdir + "wiki_test.txt");
            Db_wikistrip ws = new Db_wikistrip();
            byte[] stripped = ws.Strip_wiki(wiki, false, null);
            return String_.new_u8(stripped);
	}
        private static void perform() {
                Xoh_css_minify mini = new Xoh_css_minify();
                String css = mini.readFileAsString(rootdir + "csstest.txt");
                //String css = mini.readFileAsString("D:\\des\\mediawiki\\extensions\\Kartographer\\lib\\mapbox\\style.css");
                
                String c1 = "", c2 = "";
                tick();
                for (int i = 0; i < 10; i++) {
                    c1 = mini.cssmin(css, 0);
                }
                tock("v1 10 times");
                Xoh_css_minify_v3 mini2 = new Xoh_css_minify_v3();
                //String css = mini.readFileAsString("d:/des/xowa_x/csstest.txt");
                tick();
                for (int i = 0; i < 10; i++) {
                    c2 = mini2.cssmin(css, 0);
                }
                tock("v2 10 times");
                System.out.println(c1.equals(c2));
                System.out.println(c1);
        }

	private static final    byte[]
	  Bry__file_lhs = Bry_.new_a7("file:///")
	, Bry__file_mid = Bry_.new_a7("/file/")
	, Bry__file_fsys = Bry_.new_a7("/xowa/fsys")
	, Bry__xowa_bit = Bry_.new_a7("/xowa/")
	;

	public static byte[] Replace_fsys_hack1(byte[] html_bry) {
		// init
		Bry_bfr bfr = Bry_bfr_.New();
		int len = html_bry.length;
		int pos = 0;
		//writeFile(String_.new_u8(html_bry), rootdir + "xxhtml.htm");

		// loop while finding "file:///.*/file/"
		// or root_dir_fsys
		while (true) {
			// find "file:"
			int lhs_bgn = Bry_find_.Find_fwd(html_bry, Bry__file_lhs, pos);

			// exit if nothing found
			if (lhs_bgn == Bry_find_.Not_found) 
				break;

			// set lhs_end (after "file:///")
			int lhs_end = lhs_bgn + Bry__file_lhs.length;

			// skip if page literally starts with "file:///"
			if (lhs_bgn == 0) {
				bfr.Add_mid(html_bry, pos, lhs_end);
				pos = lhs_end;
				continue;
			}

			int mid_bgn;
			// is this the local root
			if (Bry_.Has_at_bgn(html_bry, root_dir_fsys, lhs_end, len)) {
				mid_bgn = lhs_end + root_dir_fsys.length - 1;
			} else {
				// find "/file/"
				mid_bgn = Bry_find_.Find_fwd(html_bry, Bry__file_mid, lhs_bgn, len);
				
				// skip if no "/file/"
				if (mid_bgn == Bry_find_.Not_found) {
					mid_bgn = -2;
				}
				// skip if "'file:/// ... '" is too long. should be no more than 300
				if (mid_bgn - lhs_end > 300) {
					mid_bgn = -2;
				}
			}

			// add everything up to "file:"
			bfr.Add_mid(html_bry, pos, lhs_bgn);

			// have we a valid mid_bgn
			if (mid_bgn == -2) {
				bfr.Add_mid(html_bry, lhs_bgn, lhs_end);
				pos = lhs_end;
			} else {
				// add "/fsys/"
				bfr.Add(Bry__file_fsys);
				
				// move pos forward
				pos = mid_bgn;
			}
		}

		// add rest
		bfr.Add_mid(html_bry, pos, len);
		return bfr.To_bry_and_clear();
	}
	public static byte[] Replace_fsys_hack(byte[] html_bry) {
		// init
                String ss = String_.new_u8(html_bry);
		Bry_bfr bfr = Bry_bfr_.New();
		if (html_bry == null) return null;
		int len = html_bry.length;
		int pos = 0;
	  int bgn = 0;
		byte b;
		while (pos < len) {
			b = html_bry[pos++];
			if (b == 'f') { // check for file:/// et al
				if (pos + 7 < len && html_bry[pos] == 'i' && html_bry[pos+1] == 'l' && html_bry[pos+2] == 'e' && html_bry[pos+3] == ':' && html_bry[pos+4] == '/' && html_bry[pos+5] == '/' && html_bry[pos+6] == '/') {
	
					// set lhs_end (after "file:///")
					int lhs_end = pos + 7;
		
					int mid_bgn;
					// is this the local root
					if (Bry_.Has_at_bgn(html_bry, root_dir_fsys, lhs_end, len)) {
						mid_bgn = lhs_end + root_dir_fsys.length - 1;
					} else {
						// find "/file/"
						mid_bgn = Bry_find_.Find_fwd(html_bry, Bry__file_mid, lhs_end, len);
						
						// skip if no "/file/"
						if (mid_bgn == Bry_find_.Not_found) {
							mid_bgn = -2;
						}
						// skip if "'file:/// ... '" is too long. should be no more than 300
						if (mid_bgn - lhs_end > 300) {
							mid_bgn = -2;
						}
					}
		
					// have we a valid mid_bgn
					if (mid_bgn == -2) {
						pos = lhs_end;
					} else {
						// add everything up to "file:"
						bfr.Add_mid(html_bry, bgn, pos - 1);
	
						// add [/xowa/]/fsys/
						bfr.Add(Bry__file_fsys);
						
						// move pos forward
						pos = mid_bgn;
						bgn = mid_bgn;
					}
				}
	
			} else if (b == 'h') { // check for https?:// et al
				if (pos + 6 < len && html_bry[pos] == 't' && html_bry[pos+1] == 't' && html_bry[pos+2] == 'p') {
					int ofs = -1;
					if (html_bry[pos+3] == 's' && html_bry[pos+4] == ':' && html_bry[pos+5] == '/' && html_bry[pos+6] == '/')
						ofs = pos+7;
					else if (html_bry[pos+3] == ':' && html_bry[pos+4] == '/' && html_bry[pos+5] == '/')
						ofs = pos+6;
					if (ofs > 0) {
						int found = -1;
						b = html_bry[ofs++];
/* xxx bgn */
	switch (b) {
		case 'c':
			if (html_bry[ofs] == 'o' && html_bry[ofs+1] == 'm' && html_bry[ofs+2] == 'm' && html_bry[ofs+3] == 'o' && html_bry[ofs+4] == 'n' && html_bry[ofs+5] == 's' && html_bry[ofs+6] == '.' && html_bry[ofs+7] == 'w' && html_bry[ofs+8] == 'i' && html_bry[ofs+9] == 'k' && html_bry[ofs+10] == 'i' && html_bry[ofs+11] == 'm' && html_bry[ofs+12] == 'e' && html_bry[ofs+13] == 'd' && html_bry[ofs+14] == 'i' && html_bry[ofs+15] == 'a')
				found = ofs + 16;
			break;
		case 'd':
			if (html_bry[ofs] == 'e' && html_bry[ofs+1] == '.' && html_bry[ofs+2] == 'w' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'k' && html_bry[ofs+5] == 'i') {
				switch (html_bry[ofs+6]) {
					case 'p':
						if (html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'd' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'a')
							found = ofs + 11;
						break;
					case 's':
						if (html_bry[ofs+7] == 'o' && html_bry[ofs+8] == 'u' && html_bry[ofs+9] == 'r' && html_bry[ofs+10] == 'c' && html_bry[ofs+11] == 'e')
							found = ofs + 12;
						break;
				}
			}
		case 'e':
			if (html_bry[ofs] == 'n' && html_bry[ofs+1] == '.' && html_bry[ofs+2] == 'w' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'k') {
				switch (html_bry[ofs+5]) {
					case 'i':
							switch (html_bry[ofs+6]) {
								case 'b':
									if (html_bry[ofs+7] == 'o' && html_bry[ofs+8] == 'o' && html_bry[ofs+9] == 'k' && html_bry[ofs+10] == 's')
										found = ofs + 11;
									break;
								case 'n':
									if (html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'w' && html_bry[ofs+9] == 's')
										found = ofs + 10;
									break;
								case 'p':
									if (html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'd' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'a')
										found = ofs + 11;
									break;
								case 'q':
									if (html_bry[ofs+7] == 'u' && html_bry[ofs+8] == 'o' && html_bry[ofs+9] == 't' && html_bry[ofs+10] == 'e')
										found = ofs + 11;
									break;
								case 's':
									if (html_bry[ofs+7] == 'o' && html_bry[ofs+8] == 'u' && html_bry[ofs+9] == 'r' && html_bry[ofs+10] == 'c' && html_bry[ofs+11] == 'e')
										found = ofs + 12;
									break;
								case 'v':
										switch (html_bry[ofs+7]) {
											case 'e':
												if (html_bry[ofs+8] == 'r' && html_bry[ofs+9] == 's' && html_bry[ofs+10] == 'i' && html_bry[ofs+11] == 't' && html_bry[ofs+12] == 'y')
													found = ofs + 13;
												break;
											case 'o':
												if (html_bry[ofs+8] == 'y' && html_bry[ofs+9] == 'a' && html_bry[ofs+10] == 'g' && html_bry[ofs+11] == 'e')
													found = ofs + 12;
												break;
										}
							}
					case 't':
						if (html_bry[ofs+6] == 'i' && html_bry[ofs+7] == 'o' && html_bry[ofs+8] == 'n' && html_bry[ofs+9] == 'a' && html_bry[ofs+10] == 'r' && html_bry[ofs+11] == 'y')
							found = ofs + 12;
						break;
				}
			}
		case 'f':
			if (html_bry[ofs] == 'r' && html_bry[ofs+1] == '.' && html_bry[ofs+2] == 'w' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'k' && html_bry[ofs+5] == 'i') {
				switch (html_bry[ofs+6]) {
					case 'p':
						if (html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'd' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'a')
							found = ofs + 11;
						break;
					case 's':
						if (html_bry[ofs+7] == 'o' && html_bry[ofs+8] == 'u' && html_bry[ofs+9] == 'r' && html_bry[ofs+10] == 'c' && html_bry[ofs+11] == 'e')
							found = ofs + 12;
						break;
				}
			}
		case 'h':
			if (html_bry[ofs] == 'e' && html_bry[ofs+1] == '.' && html_bry[ofs+2] == 'w' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'k' && html_bry[ofs+5] == 'i' && html_bry[ofs+6] == 'p' && html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'd' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'a')
				found = ofs + 11;
			break;
		case 'i':
			if (html_bry[ofs] == 't' && html_bry[ofs+1] == '.' && html_bry[ofs+2] == 'w' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'k' && html_bry[ofs+5] == 'i') {
				switch (html_bry[ofs+6]) {
					case 'p':
						if (html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'd' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'a')
							found = ofs + 11;
						break;
					case 's':
						if (html_bry[ofs+7] == 'o' && html_bry[ofs+8] == 'u' && html_bry[ofs+9] == 'r' && html_bry[ofs+10] == 'c' && html_bry[ofs+11] == 'e')
							found = ofs + 12;
						break;
				}
			}
		case 'j':
			if (html_bry[ofs] == 'a' && html_bry[ofs+1] == '.' && html_bry[ofs+2] == 'w' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'k' && html_bry[ofs+5] == 'i' && html_bry[ofs+6] == 'p' && html_bry[ofs+7] == 'e' && html_bry[ofs+8] == 'd' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'a')
				found = ofs + 11;
			break;
		case 's':
				switch (html_bry[ofs+0]) {
					case 'i':
						if (html_bry[ofs+1] == 'm' && html_bry[ofs+2] == 'p' && html_bry[ofs+3] == 'l' && html_bry[ofs+4] == 'e' && html_bry[ofs+5] == '.' && html_bry[ofs+6] == 'w' && html_bry[ofs+7] == 'i' && html_bry[ofs+8] == 'k' && html_bry[ofs+9] == 'i' && html_bry[ofs+10] == 'p' && html_bry[ofs+11] == 'e' && html_bry[ofs+12] == 'd' && html_bry[ofs+13] == 'i' && html_bry[ofs+14] == 'a')
							found = ofs + 15;
						break;
					case 'p':
						if (html_bry[ofs+1] == 'e' && html_bry[ofs+2] == 'c' && html_bry[ofs+3] == 'i' && html_bry[ofs+4] == 'e' && html_bry[ofs+5] == 's' && html_bry[ofs+6] == '.' && html_bry[ofs+7] == 'w' && html_bry[ofs+8] == 'i' && html_bry[ofs+9] == 'k' && html_bry[ofs+10] == 'i' && html_bry[ofs+11] == 'm' && html_bry[ofs+12] == 'e' && html_bry[ofs+13] == 'd' && html_bry[ofs+14] == 'i' && html_bry[ofs+15] == 'a')
							found = ofs + 16;
						break;
				}
		case 'w':
			if (html_bry[ofs] == 'w' && html_bry[ofs+1] == 'w' && html_bry[ofs+2] == '.' && html_bry[ofs+3] == 'w' && html_bry[ofs+4] == 'i' && html_bry[ofs+5] == 'k' && html_bry[ofs+6] == 'i' && html_bry[ofs+7] == 'd' && html_bry[ofs+8] == 'a' && html_bry[ofs+9] == 't' && html_bry[ofs+10] == 'a')
				found = ofs + 11;
			break;
	}
/* xxx end */
						if (found > 0) {
							if (html_bry[found] == '.' && html_bry[found+1] == 'o' && html_bry[found+2] == 'r' && html_bry[found+3] == 'g') {
								// add everything up to "http"
								bfr.Add_mid(html_bry, bgn, pos - 1);
			
								// add [/xowa/]
								bfr.Add(Bry__xowa_bit);
								
								// move pos forward
								pos = found + 4;
								bgn = ofs - 1; // back to '/'
							}
						}
					}
				}
//			} else if (b == '"' || b == '\'') { // ="/wiki/ or ='/wiki/
//				//page_html = String_.Replace(page_html, "\"/wiki/"	, "\"/xowa/" + wiki_domain + "/wiki/");
//				//page_html = String_.Replace(page_html, " href='/wiki/"	, " href='/xowa/" + wiki_domain + "/wiki/");
//				if (pos > 1) {
//					if (src[pos - 2] == '=') {
//						if (src[pos] == '/' && src[pos+1] == 'w' && src[pos+2] == 'i' && src[pos+3] == 'k' && src[pos+4] == 'i' && src[pos+5] == '/') {
//							?? where to get wiki_domain from?
//						}
//					}
//				} 
			}
		}
		if (bgn > 0) {
			// add rest
			bfr.Add_mid(html_bry, bgn, len);
			return bfr.To_bry_and_clear();
		} else
			return html_bry;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_run)) {this.Run();}
		else	return Gfo_invk_.Rv_unhandled;
		return this;
	}	public static final String Invk_run = "run";
	private static final    byte[] 
	  Url__home = Bry_.new_a7("/")
	, Url__exec = Bry_.new_a7("/exec/"), Url__exec_2 = Bry_.new_a7("/xowa-cmd:")
	;
	public static final    byte[]
	  Url__fsys = Bry_.new_a7("/fsys/")
	, Url__static = Bry_.new_a7("/static/")
	;
	private static final    int Url__fsys_len = Url__fsys.length;
	private static final    Bry_fmtr
	  json_fmtr = Bry_fmtr.new_
	("{\"type\":\"~{stype}\",\"title\":\"~{page_title}\",\"displaytitle\":\"~{page_title}\",\"namespace\":{\"id\":0,\"text\":\"\"},\"wikibase_item\":\"Q820802\",\"titles\":{\"canonical\":\"Berkane_Province\",\"normalized\":\"~{page_title}\",\"display\":\"~{page_title}\"},\"pageid\":~{page_id},~{thumb}~{orig}\"lang\":\"~{lang}\",\"dir\":\"~{dir}\",\"revision\":\"922573281\",\"tid\":\"1005aac0-f526-11e9-b822-5d02d224142b\",\"timestamp\":\"2019-10-22T23:45:38Z\",\"description\":\"Province in Oriental, Morocco\",\"coordinates\":{\"lat\":34.917614,\"lon\":-2.317469},\"content_urls\":{\"desktop\":{\"page\":\"https://en.wikipedia.org/wiki/Berkane_Province\",\"revisions\":\"https://en.wikipedia.org/wiki/Berkane_Province?action=history\",\"edit\":\"https://en.wikipedia.org/wiki/Berkane_Province?action=edit\",\"talk\":\"https://en.wikipedia.org/wiki/Talk:Berkane_Province\"},\"mobile\":{\"page\":\"https://en.m.wikipedia.org/wiki/Berkane_Province\",\"revisions\":\"https://en.m.wikipedia.org/wiki/Special:History/Berkane_Province\",\"edit\":\"https://en.m.wikipedia.org/wiki/Berkane_Province?action=edit\",\"talk\":\"https://en.m.wikipedia.org/wiki/Talk:Berkane_Province\"}},\"api_urls\":{\"summary\":\"https://en.wikipedia.org/api/rest_v1/page/summary/Berkane_Province\",\"metadata\":\"https://en.wikipedia.org/api/rest_v1/page/metadata/Berkane_Province\",\"references\":\"https://en.wikipedia.org/api/rest_v1/page/references/Berkane_Province\",\"media\":\"https://en.wikipedia.org/api/rest_v1/page/media/Berkane_Province\",\"edit_html\":\"https://en.wikipedia.org/api/rest_v1/page/html/Berkane_Province\",\"talk_page_html\":\"https://en.wikipedia.org/api/rest_v1/page/html/Talk:Berkane_Province\"},\"extract\":\"abcdfe\",\"extract_html\":\"~{para}\"}",
                "stype", "page_title", "page_id", "thumb", "orig", "para", "lang", "dir")
	, thumb_fmtr = Bry_fmtr.new_("\"thumbnail\": {\"source\": \"/xowa/api/wikipedia/~{site}/thumb/~{md5}/~{fname}/40px-~{fname}~{ext}\",\"width\":~{width},\"height\":~{height}},",
	              "site", "md5", "fname", "ext", "width", "height")
	, orig_fmtr = Bry_fmtr.new_("\"originalimage\":{\"source\":\"/xowa/api/wikipedia/~{site}/~{md5}/~{fname}\",\"width\":~{width},\"height\":~{height}},",
	              "site", "md5", "fname", "width", "height")
	;
}
class Xosrv_http_wkr_ {
	private byte[] lang_rsp;
	public Xosrv_http_wkr_() {
		lang_rsp = null;
	}
	public void Set_content_lang(Xol_lang_itm lang) {
		if (lang.Lang_id() != Xol_lang_stub_.Id_en) {
			lang_rsp = Bry_.Add(Rsp__lang, lang.Key_bry(), Byte_ascii.Nl_bry);
		}
	}
	public void Set_content_type(byte[] content_type) {content_type_html = content_type;}
	public void Write_response_as_html(Http_client_wtr client_wtr, boolean cross_domain, String html) {Write_response_as_html(client_wtr, cross_domain, Bry_.new_u8(html));}
	public void Write_response_as_html(Http_client_wtr client_wtr, boolean cross_domain, byte[] html) {
		try{
			// TODO_OLD: add command-line argument to allow testing from local file
			// if (cross_domain)
			//	client_wtr.Write_str("Access-Control-Allow-Origin: *\n");	// No 'Access-Control-Allow-Origin' header is present on the requested resource.
			client_wtr.Write_bry
			( Bry_.Add
			(   Rsp__http_ok
			,   content_type_html
			,   lang_rsp
			,   Byte_ascii.Nl_bry
			,   html
			));
		} catch (Exception err) {
			client_wtr.Write_str("Site not found. Check address please, or see console log.\n" + Err_.Message_lang(err));
			client_wtr.Rls();
		}		
	}
	public void Write_redirect(Http_client_wtr client_wtr, byte[] redirect) {
		try{
			client_wtr.Write_bry
			(   Bry_.Add
				( Rsp__http_redirect
				, Rsp__location
				, Bry_.new_a7("xowa/")
				, redirect
				, Byte_ascii.Nl_bry
				, Byte_ascii.Nl_bry // proxy servers like nginx require 2 line breaks; ISSUE#:600; DATE:2019-11-05
				)
			);
		} catch (Exception err) {
			client_wtr.Write_str("Redirect failed. Check address please, or see console log.\n" + Err_.Message_lang(err));
			client_wtr.Rls();
		}
	}
	public byte[] content_type_html;
	public static final    byte[]
	  Rsp__http_ok				= Bry_.new_a7("HTTP/1.1 200 OK:\n")
	, Rsp__http_redirect        = Bry_.new_a7("HTTP/1.1 302 Found:\n")
	, Rsp__location             = Bry_.new_a7("Location: /") // "/" to start from root
	, Rsp__lang                 = Bry_.new_a7("Content-language: ")
	;
}
