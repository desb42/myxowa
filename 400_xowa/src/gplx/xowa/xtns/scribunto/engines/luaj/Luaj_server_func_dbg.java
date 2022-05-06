/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.scribunto.engines.luaj;
import gplx.*;
import gplx.xowa.xtns.scribunto.*;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import gplx.xowa.parsers.tmpls.Xot_invk;
public class Luaj_server_func_dbg extends VarArgFunction {
	private final Scrib_core core;
	public Luaj_server_func_dbg(Scrib_core v) {this.core = v;}	
	@Override public Varargs invoke(Varargs args) {
		// init bfrs
		byte dbg_separator = Byte_ascii.Tab; 
		Bry_bfr dbg_bfr = Bry_bfr_.New();
		Bry_bfr html_bfr = Bry_bfr_.New();
                Xot_invk cf = core.Frame_current();
		byte[] ttl;
                if (cf == null)
                    ttl = Bry_.new_a7("<outside>");
                else
                    ttl = core.Frame_current().Frame_ttl();
//                if (Bry_.Eq(ttl, Bry_.new_a7("Module:CountryData")))
//                    return NONE;Thread.currentThread().getName()+"-defn_cache-" + 
		dbg_bfr.Add_str_a7(Thread.currentThread().getName()+"-dbg-").Add_byte(dbg_separator);
		dbg_bfr.Add(ttl).Add_byte(dbg_separator);
		html_bfr.Add_str_a7("<span class='xowa_dbg' style='color:red'>");
		
		// loop args and add to bfrs
		int len = args.narg();
		for (int i = 1; i <= len; ++i) {
			String s = args.arg(i).toString();
			if (i != 1) dbg_bfr.Add_byte(dbg_separator);
                        // handle UNIQs
                        s = String_.new_u8(core.Wiki().Parser_mgr().Uniq_mgr().Parse(Bry_.new_u8(s)));
			dbg_bfr.Add_str_u8(s);
			html_bfr.Add_str_u8(gplx.langs.htmls.Gfh_utl.Escape_html_as_str(s) + "&nbsp;");
		}
		
		// term bfrs and print 
		html_bfr.Add_str_a7("</span><br/>");
		core.Page().Html_data().Xtn_scribunto_dbg_(html_bfr.To_bry_and_clear());
		gplx.core.consoles.Console_adp__sys.Instance.Write_str_w_nl_utf8(dbg_bfr.To_str_and_clear());
                //if (Bry_.Eq(ttl, Bry_.new_a7("Module:Navbox"))) {
                //    int a=1;
                //}
		return NONE;
	}
}
