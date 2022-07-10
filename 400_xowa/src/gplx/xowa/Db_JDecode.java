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
package gplx.xowa; import gplx.*;
import gplx.dbs.*; import gplx.dbs.engines.sqlite.*;
import gplx.langs.jsons.*;
public class Db_JDecode {
	private byte[] src;
	private int src_len;
	private int ver;
	private int pos;
	private int maxindent;
	private Result r;
	//private Txt t;
	private Txt[] string_table;
	private	Json_doc doc;
	public Db_JDecode(byte[] src) {
		this.src = src;
		this.src_len = src.length;
		this.pos = 0;
		ver = 0;
		byte b = src[0];
		if (b == (byte)0xd2) ver = 2;
		else if (b == (byte)0xd2) ver = 1;
		if (ver > 0) pos = 1;
		this.maxindent = 0;
		this.r = new Result();
		//this.t = new Txt();
		this.doc = new Json_doc();
	}

	private Result getcode() {
		if (this.pos < this.src_len) {
			byte b = this.src[this.pos++];
			r.code = b & 0xF0;
			r.size = b & 0x0F;
			return r;
		}
		throw Err_.new_("json", "eos in getcode", "pos", pos);
	}

	private Txt gettext(int size) {
		if (this.pos + size <= this.src_len) {
			Txt txt = new Txt();
			txt.bgn = pos;
			txt.end = pos + size;
			this.pos += size;
			return txt;
		}
		throw Err_.new_("json", "eos in gettext", "pos", pos);
	}

	private Txt getstr() {
		int ln = getcint();
		Txt txt = gettext(ln);
		return txt;
	}

	private int getcint() {
		int i = 0;
		boolean found = false;
		while (this.pos < this.src_len) {
			byte v = this.src[this.pos++];
			int c = v & 0x7f;
			i = (i << 7) + c;
			if ((v & 0x80) == 0) {
				found = true;
				break;
			}
		}
		if (found)
			return i;
		throw Err_.new_("json", "eos in getcint", "pos", pos);
	}

	public Json_doc Decode() {
		Result res = getcode();
		if (res.code != STRINGS) {
			throw Err_.new_("json", "not a JStream");
		}
		int size;
		if (res.size > 0)
			size = res.size;
		else
			size = getcint();
		string_table = new Txt[size];
		for (int i = 0; i < size; i++)
			string_table[i] = getstr();
		Json_itm root = decode_nde(0);
		doc.Ctor(src, (Json_grp)root);
		return doc;
	}

	private Json_itm decode_nde(int indent) {
		if (indent > this.maxindent)
			this.maxindent = indent;
		Result res = getcode();
		// easy ones first
		switch(res.code) {
			case KV:
				Json_itm key = decode_nde(indent+1);
				if (ver < 2) {
					res = getcode();
					if (res.code != VAL)
						throw Err_.new_("json", "not VAL");
				}
				Json_itm val = decode_nde(indent+1);
				return new Json_kv(key, val);
			case NULL:
				return Json_itm_null.Null;
			case BOOL:
				if (res.size > 0)
					return Json_itm_bool.Bool_y;
				else
					return Json_itm_bool.Bool_n;
		}
		Txt txt;
		int size_idx;
		if (res.size != 15)
			size_idx = res.size;
		else
			size_idx = getcint();
		switch(res.code) {
			case NDE:
				Json_nde nde = new Json_nde(doc, pos, size_idx);
				for (int i = 0; i < size_idx; i++)
					nde.Add(decode_nde(indent+1));
				return nde;
			case ARY:
				Json_ary ary = Json_ary.NewByDoc(doc, pos, pos, size_idx);
				for (int i = 0; i < size_idx; i++)
					ary.Add(decode_nde(indent+1));
				return ary;
			case INT:
				txt = string_table[size_idx];
				return Json_itm_int.NewByDoc(doc, txt.bgn, txt.end);
			case LONG:
				txt = string_table[size_idx];
				return Json_itm_long.NewByDoc(doc, txt.bgn, txt.end);
			case DECIMAL:
				txt = string_table[size_idx];
				return Json_itm_decimal.NewByDoc(doc, txt.bgn, txt.end);
			case STR:
			case STR_EXACT:
				txt = string_table[size_idx];
				return Json_itm_str.NewByDoc(doc, txt.bgn-1, txt.end+1, res.code == STR_EXACT);
			default:
				throw Err_.new_("json", "unknown code", "pos", pos);
		}
		//return null;
	}
	private static final int
	  STRINGS   =  1 << 4
	, NDE       =  2 << 4
	, ARY       =  3 << 4
	, KV        =  4 << 4
	, VAL       =  5 << 4
	, INT       =  6 << 4
	, LONG      =  7 << 4
	, DECIMAL   =  8 << 4
	, STR       =  9 << 4
	, STR_EXACT = 10 << 4
	, NULL      = 11 << 4
	, BOOL      = 12 << 4
	;
}
class Result {
	int code;
	int size;
}
class Txt {
	int bgn;
	int end;
}
