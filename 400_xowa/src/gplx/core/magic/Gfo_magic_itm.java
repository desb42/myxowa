/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2021-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.core.magic;
import gplx.*;
public class Gfo_magic_itm {
	public Gfo_magic_itm(byte tid, String text) {
		this.tid = tid;
		this.text_bry = Bry_.new_u8(text);
		this.text_str = text;
	}
	public byte Tid() {return tid;} private byte tid;
	public byte[] Text_bry() {return text_bry;} private byte[] text_bry;									// http://
	public String Text_str() {return text_str;} private String text_str;
	public boolean Text_ends_w_colon() {return text_ends_w_colon;} private boolean text_ends_w_colon;
	public static final byte // REF.MW:DefaultSettings|$wgEnableMagicLinks; 
	  Tid_ISBN    = 0
	, Tid_PMID    = 1
	, Tid_RFC     = 2
	;
	public static final Ordered_hash Regy = Ordered_hash_.New_bry();
	public static final Gfo_magic_itm 
	  Itm_ISBN    = new_(Tid_ISBN,    "ISBN ")
	, Itm_PMID    = new_(Tid_PMID,    "PMID ")
	, Itm_RFC     = new_(Tid_RFC,     "RFC ")
	;
	public static Gfo_magic_itm Get_or(byte tid, Gfo_magic_itm or) {
		Gfo_magic_itm[] ary = Ary();
		return tid >= ary.length ? or : ary[tid];
	}
	public static Gfo_magic_itm[] Ary() {
		if (magic_itm_ary == null) {
			int len = Regy.Count();
			magic_itm_ary = new Gfo_magic_itm[len];
			for (int i = 0; i < len; i++)
				magic_itm_ary[i] = (Gfo_magic_itm)Regy.Get_at(i);
		}
		return magic_itm_ary;
	}	private static Gfo_magic_itm[] magic_itm_ary;
	public static String[] Magic_str_ary() {
		if (magic_str_ary == null) {
			int len = Regy.Count();
			magic_str_ary = new String[len];
			for (int i = 0; i < len; i++)
				magic_str_ary[i] = ((Gfo_magic_itm)Regy.Get_at(i)).Text_str();
		}
		return magic_str_ary;
	}
	private static String[] magic_str_ary;
	private static Gfo_magic_itm new_(byte tid, String text) {
		Gfo_magic_itm rv = new Gfo_magic_itm(tid, text);
		Regy.Add(rv.Text_bry(), rv);
		return rv;
	}
}
