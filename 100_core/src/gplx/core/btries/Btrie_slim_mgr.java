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
package gplx.core.btries; import gplx.*;
import gplx.core.primitives.*; import gplx.core.threads.poolables.*;
import gplx.core.encoders.Urlencoders;
import gplx.core.security.algos.*;
public class Btrie_slim_mgr implements Btrie_mgr {
	public Btrie_slim_mgr(boolean case_match) {root = new Btrie_slim_itm(Byte_.Zero, null, !case_match);}	private Btrie_slim_itm root;
	public int Count() {return count;} private int count;
	//public int Match_pos() {return match_pos;} private int match_pos;

	public Object Match_at(Btrie_rv rv, byte[] src, int bgn_pos, int end_pos) {
		if (bgn_pos < end_pos)
			return Match_at_w_b0(rv, src[bgn_pos], src, bgn_pos, end_pos);
		else { // handle out of bounds gracefully; EX: Match_bgn("abc", 3, 3) should (a) return null not fail; (b) return a pos of bgn_pos, not 0; DATE:2018-04-12
			rv.Init(bgn_pos, null);
			return null;
		}
	}

	public void Dumplevel(int level, Btrie_slim_itm cur) {
		Btrie_slim_itm[] ary = cur.Ary();
		int len = cur.ary_len;
		for (int i = 0; i < len; i++) {
			Btrie_slim_itm itm = ary[i];
			for (int j = 0; j < level; j++)
				System.out.print(" ");
			if (itm.Key_byte() > 127 || itm.Key_byte() < 20)
				System.out.println(itm.Key_byte() + " " + itm.Case_any());
			else
				System.out.println("'" + String.valueOf((char)itm.Key_byte()) + "' " + itm.Case_any());
			Dumplevel(level + 1, itm);
		}
	}
	private boolean once = true;
	public void Dumpit(String triename) {
		if (once) {
			once = false;
			System.out.println(triename);
			//md5_algo.To_hash_bry();
			for (int i = 0; i < subs_len; i++) {
				//md5_algo.Update_digest(subs[i], 0, subs[i].length);
				//System.out.println(String_.new_u8(Xoa_Urlencoders.Raw_url_encode(subs[i])) + " " + String_.new_a7(md5_algo.To_hash_bry()));
				System.out.println(String_.new_u8(Urlencoders.Raw_url_encode(subs[i])));
			}
			//System.out.println();
			//Btrie_slim_itm cur = root;
			//Dumplevel(0, cur);
		}
	}
	private byte[][] subs = new byte[1][];
	protected Object[] objs = new Object[1];
	public Object[] Objs() { return objs; }
	private Hash_algo md5_algo = Hash_algo_.New__md5();
	public byte[] Md5() { if (md5 == null) md5 = md5_algo.To_hash_bry(); return md5; }
	private byte[] md5 = null;
	private int subs_len = 0;
	private int subs_max = 0;
	private void Add_val(byte[] itm, Object val) {
		// overwrite value
		for (int i = 0; i < subs_len; i++) {
			if (Bry_.Eq(itm, subs[i])) {
				//System.out.println("slim overwrite " + String_.new_u8(itm));
				objs[i] = val;
				return;
			}
		}
//                System.out.println("slim " + this + "-" + String_.new_u8(itm));
		md5_algo.Update_digest(itm, 0, itm.length);
		int new_len = subs_len + 1;
		if (new_len > subs_max) {	// ary too small >>> expand
			subs_max = new_len * 2;
			byte[][] new_subs = new byte[subs_max][];
			Array_.Copy_to(subs, 0, new_subs, 0, subs_len);
			subs = new_subs;
			Object[] new_objs = new Object[subs_max];
			Array_.Copy_to(objs, 0, new_objs, 0, subs_len);
			objs = new_objs;
		}
		subs[subs_len] = itm;
		objs[subs_len] = val;
		subs_len = new_len;
	}

	public Object Match_at_w_b0(Btrie_rv rv, byte b, byte[] src, int bgn_pos, int src_end) {
		Object rv_obj = null; 
		int rv_pos = bgn_pos;
		int cur_pos = bgn_pos;
		Btrie_slim_itm cur = root;
		while (true) {
			Btrie_slim_itm nxt = cur.Ary_find(b);
			if (nxt == null) {
				rv.Init(rv_pos, rv_obj);			// nxt does not have b; return rv_obj;
				return rv_obj;
			}
			++cur_pos;
			if (nxt.Ary_is_empty()) {
				rv_obj = nxt.Val();
				rv.Init(cur_pos, rv_obj);			// nxt is leaf; return nxt.Val() (which should be non-null)
				return rv_obj;
			}
			Object nxt_val = nxt.Val();
			if (nxt_val != null) {rv_pos = cur_pos; rv_obj = nxt_val;}							// nxt is node; cache rv_obj (in case of false match)
			if (cur_pos == src_end) {					
				rv.Init(rv_pos, rv_obj);			// increment cur_pos and exit if src_end
				return rv_obj;
			}
			b = src[cur_pos];
			cur = nxt;
		}
	}
	public Object Match_exact(byte[] src) {return src == null ? null : Match_exact(src, 0, src.length);}
	public Object Match_exact(byte[] src, int bgn_pos, int end_pos) {
		if (bgn_pos == end_pos) return null;	// NOTE:handle empty String; DATE:2016-04-21
		Btrie_result rv = Match_bgn_w_byte(src[bgn_pos], src, bgn_pos, end_pos);
		return rv.o == null ? null : rv.match_pos - bgn_pos == end_pos - bgn_pos ? rv.o : null;
	}
	public Object Match_bgn(byte[] src, int bgn_pos, int end_pos) {
	if (bgn_pos < end_pos) { // handle out of bounds gracefully; EX: Match_bgn("abc", 3, 3) should return null not fail
		Btrie_result r = Match_bgn_w_byte(src[bgn_pos], src, bgn_pos, end_pos);
		return r.o;
	}
	else
		return null;
	}
	public Btrie_result Match_bgn_w_byte(byte b, byte[] src, int bgn_pos, int src_end) {
		Object rv = null;
		int cur_pos = bgn_pos;
		int match_pos = bgn_pos;
		Btrie_slim_itm cur = root;
		while (true) {
			Btrie_slim_itm nxt = cur.Ary_find(b);
			if (nxt == null) return new Btrie_result(rv, match_pos);	// nxt does not have b; return rv and pos;
			++cur_pos;
			if (nxt.Ary_is_empty()) {
				return new Btrie_result(nxt.Val(), cur_pos);	// nxt is leaf; return nxt.Val() (which should be non-null)
			}
			Object nxt_val = nxt.Val();
			if (nxt_val != null) {match_pos = cur_pos; rv = nxt_val;}			// nxt is node; cache rv (in case of false match)
			if (cur_pos == src_end) return new Btrie_result(rv, match_pos);									// increment cur_pos and exit if src_end
			b = src[cur_pos];
			cur = nxt;
		}
	}
	public byte Match_byte_or(byte b, byte[] src, int bgn, int end, byte or) {
		Btrie_result rv_obj = Match_bgn_w_byte(b, src, bgn, end);
		return rv_obj.o == null ? or : ((Byte_obj_val)rv_obj.o).Val();
	}
	public byte Match_byte_or(byte[] src, int bgn, int end, byte or) {
		Object rv_obj = Match_bgn(src, bgn, end);
		return rv_obj == null ? or : ((Byte_obj_val)rv_obj).Val();
	}
	public byte Match_byte_or(Btrie_rv trv, byte b, byte[] src, int bgn, int end, byte or) {
		Object rv_obj = Match_at_w_b0(trv, b, src, bgn, end);
		return rv_obj == null ? or : ((Byte_obj_val)rv_obj).Val();
	}
	public byte Match_byte_or(Btrie_rv trv, byte[] src, int bgn, int end, byte or) {
		Object rv_obj = Match_at(trv, src, bgn, end);
		return rv_obj == null ? or : ((Byte_obj_val)rv_obj).Val();
	}
	public byte Match_byte_or(Btrie_rv trv, byte[] src, byte or) {
		Object rv_obj = Match_at(trv, src, 0, src.length);
		return rv_obj == null ? or : ((Byte_obj_val)rv_obj).Val();
	}
	public Btrie_slim_mgr Add_bry_tid(byte[] bry, byte tid)			{return (Btrie_slim_mgr)Add_obj(bry, Byte_obj_val.new_(tid));}
	public Btrie_slim_mgr Add_bry_int(byte[] key, int val)			{return (Btrie_slim_mgr)Add_obj(key, new Int_obj_val(val));}
	public Btrie_slim_mgr Add_str_byte(String key, byte val)		{return (Btrie_slim_mgr)Add_obj(Bry_.new_u8(key), Byte_obj_val.new_(val));}
	public Btrie_slim_mgr Add_str_int(String key, int val)			{return (Btrie_slim_mgr)Add_obj(Bry_.new_u8(key), new Int_obj_val(val));}
	public Btrie_slim_mgr Add_bry(String key, String val)			{return (Btrie_slim_mgr)Add_obj(Bry_.new_u8(key), Bry_.new_u8(val));}
	public Btrie_slim_mgr Add_bry(String key, byte[] val)			{return (Btrie_slim_mgr)Add_obj(Bry_.new_u8(key), val);}
	public Btrie_slim_mgr Add_bry(byte[] v)							{return (Btrie_slim_mgr)Add_obj(v, v);}
	public Btrie_slim_mgr Add_str_str(String key, String val)		{return (Btrie_slim_mgr)Add_obj(Bry_.new_u8(key), Bry_.new_u8(val));}
	public Btrie_slim_mgr Add_bry_bry(byte[] key, byte[] val)		{return (Btrie_slim_mgr)Add_obj(key, val);}
	public Btrie_slim_mgr Add_bry_byte(byte b, byte val)			{return (Btrie_slim_mgr)Add_obj(new byte[] {b}, Byte_obj_val.new_(val));}
	public Btrie_slim_mgr Add_bry_byte(byte[] bry, byte val)		{return (Btrie_slim_mgr)Add_obj(bry, Byte_obj_val.new_(val));}
	public Btrie_slim_mgr Add_str_byte__many(byte val, String... ary) {
		int ary_len = ary.length;
		Byte_obj_val bval = Byte_obj_val.new_(val);
		for (int i = 0; i < ary_len; i++)
			Add_obj(Bry_.new_u8(ary[i]), bval);
		return this;
	}
	public Btrie_slim_mgr Add_many_str(String... ary) {
		int len = ary.length;			
		for (int i = 0; i < len; i++) {
			byte[] itm = Bry_.new_u8(ary[i]);
			Add_obj(itm, itm);
		}
		return this;
	}
	public Btrie_slim_mgr Add_many_bry(byte[]... ary) {
		int len = ary.length;			
		for (int i = 0; i < len; i++) {
			byte[] itm = ary[i];
			Add_obj(itm, itm);
		}
		return this;
	}
	public Btrie_slim_mgr Add_many_int(int val, String... ary) {return Add_many_int(val, Bry_.Ary(ary));}
	public Btrie_slim_mgr Add_many_int(int val, byte[]... ary) {
		int len = ary.length;
		Int_obj_val obj = new Int_obj_val(val);
		for (int i = 0; i < len; i++)
			Add_obj(ary[i], obj);
		return this;
	}
	public Btrie_slim_mgr Add_ary_byte(byte... ary) {
		int len = ary.length;
		for (int i = 0; i < len; ++i) {
			byte b = ary[i];
			Byte_obj_val bval = Byte_obj_val.new_(b);
			Add_obj(Bry_.New_by_byte(b), bval);
		}
		return this;
	}
	public Btrie_slim_mgr Add_replace_many(String trg_str, String... src_ary) {return Add_replace_many(Bry_.new_u8(trg_str), src_ary);}
	public Btrie_slim_mgr Add_replace_many(byte[] trg_bry, String... src_ary) {
		int len = src_ary.length;
		for (int i = 0; i < len; i++)
			Add_obj(Bry_.new_u8(src_ary[i]), trg_bry);
		return this;
	}
	public Btrie_slim_mgr Add_stub(String key, byte val)		{byte[] bry = Bry_.new_u8(key); return (Btrie_slim_mgr)Add_obj(bry, new Btrie_itm_stub(val, bry));}
	public Btrie_slim_mgr Add_stubs(byte[][] ary)				{return Add_stubs(ary, ary.length);}
	public Btrie_slim_mgr Add_stubs(byte[][] ary, int ary_len) {
		for (byte i = 0; i < ary_len; i++) {
			byte[] bry = ary[i];
			Add_obj(bry, new Btrie_itm_stub(i, bry));
		}
		return this;
	}
	public Btrie_mgr Add_obj(String key, Object val) {return Add_obj(Bry_.new_u8(key), val);}
	public Btrie_mgr Add_obj(byte[] key, Object val) {
		if (val == null) throw Err_.new_wo_type("null objects cannot be registered", "key", String_.new_u8(key));
		int key_len = key.length; int key_end = key_len - 1;
		Btrie_slim_itm cur = root;
		for (int i = 0; i < key_len; i++) {
			byte b = key[i];
			if (root.Case_any() && (b > 64 && b < 91)) b += 32;
			Btrie_slim_itm nxt = cur.Ary_find(b);
			if (nxt == null)
				nxt = cur.Ary_add(b, null);
			if (i == key_end)
				nxt.Val_set(val);
			cur = nxt;
		}
		count++; // FUTURE: do not increment if replacing value
                Add_val(key, val); // db code
		return this;
	}
	public void Del(byte[] key) {
		int key_len = key.length;
		Btrie_slim_itm cur = root;
		for (int i = 0; i < key_len; i++) {
			byte b = key[i];
			Btrie_slim_itm nxt = cur.Ary_find(b);
			if (nxt == null) break;
			Object nxt_val = nxt.Val();
			if (nxt_val == null)	// cur is end of chain; remove entry; EX: Abc and at c
				cur.Ary_del(b);
			else					// cur is mid of chain; null out entry
				nxt.Val_set(null);
			cur = nxt;
		}
		count--; // FUTURE: do not decrement if not found
	}
	public byte[] Replace(Bry_bfr tmp_bfr, byte[] src, int bgn, int end) {
		int pos = bgn;
		boolean dirty = false;
		while (pos < end) {
			byte b = src[pos];
			Btrie_result r = this.Match_bgn_w_byte(b, src, pos, end);
			if (r.o == null) {
				if (dirty)
					tmp_bfr.Add_byte(b);
				pos++;
			}
			else {
				if (!dirty) {
					tmp_bfr.Add_mid(src, bgn, pos);
					dirty = true;
				}
				tmp_bfr.Add((byte[])r.o);
				pos = r.match_pos;
			}
		}
		return dirty ? tmp_bfr.To_bry_and_clear() : src;
	}
	public void Clear() {
		root.Clear();
		count = 0;

                once = true;
		subs_max = 0;
		subs_len = 0;
		subs = new byte[1][];
		objs = new Object[1];
		md5_algo = Hash_algo_.New__md5();
		md5 = null;
	}
	public void Clear_root() { // removes excess if using hardcode
//		root.Clear();
//		count = 0;
//		subs = new byte[1][];
	}
	public void Clear_subs() { // removes excess if using btrie
//		subs = new byte[1][];
//		objs = new Object[1];
	}
	public static Btrie_slim_mgr cs()				{return new Btrie_slim_mgr(Bool_.Y);}
	public static Btrie_slim_mgr ci_a7()			{return new Btrie_slim_mgr(Bool_.N);}
	public static Btrie_slim_mgr ci_u8()			{return new Btrie_slim_mgr(Bool_.N);}
	public static Btrie_slim_mgr new_(boolean cs)		{return new Btrie_slim_mgr(cs);}
}
