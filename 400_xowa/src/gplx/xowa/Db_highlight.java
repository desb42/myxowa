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
package gplx.xowa;
import gplx.*;
import gplx.core.envs.*;
import gplx.core.texts.Base64Converter;
public class Db_highlight {
	private Io_url src_url, trg_url;
	private Process_adp convert_cmd;
	public void Init_by_app(Xoae_app app) {
		// init
		src_url = app.Fsys_mgr().Root_dir().GenSubFil_nest("tmp", "src.file");
		trg_url = app.Fsys_mgr().Root_dir().GenSubFil_nest("tmp", "trg.file");
                
		Io_url convert_exe_url = app.Prog_mgr().App_highlight().Exe_url();
		convert_cmd = Process_adp.New(Gfo_usr_dlg_.Instance, app.Url_cmd_eval(), Process_adp.Run_mode_sync_timeout, 1 * 60, convert_exe_url.Raw(), String_.new_a7(app.Fsys_mgr().Bin_plat_dir().RawBry()) + "highlight\\syntaxhighlight.py ~{src} ~{trg} ~{dom} ~{ttl}", "src", "trg", "dom", "ttl");
		// clean up tmp dir
	}
	public byte[] Highlight(byte[] data, int wrk_id, byte[] domain, Xoa_ttl ttl) {
		Io_url srcfil = src_url.GenSubFil(Integer.toString(wrk_id));
		Io_url trgfil = trg_url.GenSubFil(Integer.toString(wrk_id));
		Io_mgr.Instance.SaveFilBry(srcfil, data);

		// highlight
		convert_cmd.Run(srcfil, trgfil, domain, ttl.Full_url());

		return Io_mgr.Instance.LoadFilBry(trgfil);
	}
	public void Highlight_bfr(Bry_bfr bfr, int wrk_id, byte[] domain, Xoa_ttl ttl) {
		Io_url srcfil = src_url.GenSubFil(Integer.toString(wrk_id));
		Io_url trgfil = trg_url.GenSubFil(Integer.toString(wrk_id));
		Io_mgr.Instance.SaveFilBry(srcfil, bfr.To_bry_and_clear());

		// highlight
		convert_cmd.Run(srcfil, trgfil, domain, ttl.Full_url());

		Io_mgr.Instance.LoadFilBryByBfr(trgfil, bfr);
	}
	public static byte[] RemoveSH(byte[] src, Xoae_page page) {
		int src_len = src.length;
		int startpos = 0;
		int pos = 0;
		Bry_bfr bfr = Bry_bfr_.New();
		while (pos < src_len) {
			byte b = src[pos++];
			if (b == '<' ) {
			// look for <syntaxhighlight
				int synpos_bgn = pos - 1;
				if (pos + 16 < src_len && src[pos] == 's' && src[pos+1] == 'y' && src[pos+2] == 'n' && src[pos+3] == 't'
				    && src[pos+4] == 'a' && src[pos+5] == 'x' && src[pos+6] == 'h' && src[pos+7] == 'i' 
				    && src[pos+8] == 'g' && src[pos+9] == 'h' && src[pos+10] == 'l' && src[pos+11] == 'i' 
				    && src[pos+12] == 'g' && src[pos+13] == 'h' && src[pos+14] == 't' && src[pos+15] == ' ') {
					// search for </syntaxhighlight>
					int synpos_end = -1;
					while (pos < src_len) {
						b = src[pos++];
						if (b == '<' ) {
							if (pos + 18 < src_len && src[pos] == '/' && src[pos+1] == 's' && src[pos+2] == 'y' && src[pos+3] == 'n' 
							    && src[pos+4] == 't' && src[pos+5] == 'a' && src[pos+6] == 'x' && src[pos+7] == 'h' 
							    && src[pos+8] == 'i' && src[pos+9] == 'g' && src[pos+10] == 'h' && src[pos+11] == 'l' 
							    && src[pos+12] == 'i' && src[pos+13] == 'g' && src[pos+14] == 'h' && src[pos+15] == 't' 
							    && src[pos+16] == '>') {
								synpos_end = pos + 17;
								break;
							}
						}
					}
					if (synpos_end > 0) {
						bfr.Add_mid(src, startpos, synpos_bgn);
						bfr.Add(Bry_.new_a7(Base64Converter.Encode(Bry_.Mid(src, synpos_bgn, synpos_end))));
						bfr.Add_str_a7("!!");
						startpos = synpos_end;
                                                page.Html_data().Syntaxhighlight_(true);
					}
				}
			}
		}
		if (startpos > 0) {
			bfr.Add_mid(src, startpos, src_len);
			return bfr.To_bry();
		}
		else
			return src;
	}
	public static byte[] Check_for_base64(byte[] src) {
		int src_len = src.length;
		if (src_len < 36) return src;
		Bry_bfr bfr = null;
		int pos = 0;
		int startpos = 0;
		while (pos < src_len) {
			byte b = src[pos++];
			// PHN5b
			if (b == 'P') {
				if (pos + 5 < src_len && src[pos] == 'H' && src[pos+1] == 'N' && src[pos+2] == '5' && src[pos+3] == 'b') {
					int b64_bgn = pos - 1;
					pos += 50;
					int b64_end = -1;
					while (pos < src_len) {
						b = src[pos++];
						if (b == '!' && src[pos] == '!') {
							b64_end = pos + 1;
							break;
						}
					}
					if (b64_end > 0) {
						if (startpos == 0)
							bfr = Bry_bfr_.New();
						bfr.Add_mid(src, startpos, b64_bgn);
						bfr.Add(Base64Converter.Decode(String_.new_a7(src, b64_bgn, b64_end - 2)));
						startpos = b64_end;
						pos = b64_end;
					}
				}
			}
		}
		if (startpos > 0) {
			bfr.Add_mid(src, startpos, src_len);
			return bfr.To_bry();
		}
		else
			return src;
	}
}
