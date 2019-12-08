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
package gplx.xowa.xtns.indicators; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.brys.fmtrs.*;
public class Indicator_html_bldr implements gplx.core.brys.Bfr_arg {
	private Indicator_html_bldr_itm bldr_itm = new Indicator_html_bldr_itm();
	private Ordered_hash list = Ordered_hash_.New();
	public void Enabled_(boolean v) {enabled = v;} private boolean enabled = Bool_.Y;
	public void Clear() {
		enabled = Bool_.Y;
		list.Clear();
	}
	public int Count() {return list.Count();}
	public boolean Has(String key) {return list.Has(key);}
	public void Add(Indicator_xnde xnde) {
		if (!enabled) return;				// do not add if disabled; called from <page>; PAGE:en.s:The_Parochial_System_(Wilberforce,_1838); DATE:2015-04-29
		list.Add_if_dupe_use_nth(xnde.Name(), xnde);	// Add_if_dupe_use_nth: 2nd indicator overwrites 1st; DATE:2015-04-29
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (list.Count() == 0) return;		// do not build html if no items; DATE:2015-04-29
		bldr_itm.Init(list);
		fmtr_grp.Bld_bfr_many(bfr, bldr_itm);
	}
	private static final	Bry_fmtr
	  fmtr_grp = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "  <div class=\"mw-indicators\">~{itms}"
	, "  </div>"
	), "itms")
	;
	public byte[] Serialise() {
		int list_len = list.Count();
		if (list_len == 0) return null;

		Bry_bfr tmp_bfr = Bry_bfr_.New();
		for (int i = 0; i < list_len; i++) {
			Indicator_xnde xnde = (Indicator_xnde)list.Get_at(i);
			tmp_bfr.Add(Binlen(xnde.Name().length()));
			tmp_bfr.Add_str_u8(xnde.Name());
			tmp_bfr.Add(Binlen(xnde.Html().length));
			tmp_bfr.Add(xnde.Html());
		}
		return tmp_bfr.To_bry();
	}
	private byte[] Binlen(int v) {
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++) {
			result[i] = (byte)(v & 0xff);
			v >>= 8;
		}
		return result;
	}
	private int Lenbin(byte[] bin, int ofs) {
		int result = 0;
		for (int i = 3; i >= 0; i--) {
			result <<= 8;
                        int c = bin[ofs + i];
                        // 2s compliment
                        if (c < 0)
                            c += 256;
			result += c;
		}
		return result;
	}
	public void Deserialise(byte[] serial) {
		Clear();
                if (serial == null) return;
		int len = serial.length;
		int i = 0;
		while (i < len) {
			Indicator_xnde xnde = new Indicator_xnde();
			int fsize = Lenbin(serial, i);
			i += 4;
			xnde.Name_(String_.new_u8(serial, i, i+fsize));
			i += fsize;
			fsize = Lenbin(serial, i);
			i += 4;
			xnde.Html_(Bry_.Mid(serial, i, i+fsize));
			i += fsize;
			list.Add_if_dupe_use_nth(xnde.Name(), xnde);
		}
	}
}
class Indicator_html_bldr_itm implements gplx.core.brys.Bfr_arg {
	private Ordered_hash list;
	public void Init(Ordered_hash list) {this.list = list;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int list_len = list.Count();
		//for (int i = list_len - 1; i > -1; --i) {	// reverse order
		for (int i = 0; i < list_len; i++) {
			Indicator_xnde xnde = (Indicator_xnde)list.Get_at(i);
			fmtr_itm.Bld_bfr_many(bfr, xnde.Name(), xnde.Html());
		}
	}
	private static final	Bry_fmtr
	 fmtr_itm = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "	<div id='mw-indicator-~{name}' class='mw-indicator'>~{html}</div>"
	), "name", "html")
	;
}
