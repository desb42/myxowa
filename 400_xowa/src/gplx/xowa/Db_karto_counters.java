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
package gplx.xowa; import gplx.*;
import gplx.dbs.*;
import gplx.xowa.parsers.Xop_ctx;
import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.wikis.data.Xow_db_mgr;
import gplx.xowa.wikis.data.tbls.Xowd_page_tbl;
public class Db_karto_counters {
	private List_adp lst = List_adp_.New();
	//public void Add(Object v) {lst.Add(v);}
	public void Clear() {lst.Clear();}
	public byte[] Get_or_set(byte[] key) {
		int len = lst.Len();
		Karto_counter cntr;
		for (int i = 0; i < len; i++) {
			cntr = (Karto_counter)lst.Get_at(i);
			if (Bry_.Eq(cntr.Key(), key)) {
				return cntr.Increment();
			}
		}
		cntr = Karto_counter.Try_new(key);
		if (cntr == null)
			return null;
		lst.Add(cntr);
		return cntr.Increment();
	}
}
class Karto_counter {
	private int counter = 0;
	private boolean isNumber;
	private byte[] key;
	public Karto_counter(byte[] key, boolean type) {
		this.key = key;
		this.isNumber = type;
	}
	public byte[] Key() { return key; }
	public byte[] Increment() {
		byte[] countbytes;
		if (isNumber) {
			if (counter < 99)
				counter++;
				if (counter > 9) {
					countbytes = new byte[2];
					countbytes[0] = (byte)(counter/10 + '0');
					countbytes[1] = (byte)(counter%10 + '0');
				}
				else {
					countbytes = new byte[1];
					countbytes[0] = (byte)(counter + '0');
				}
		} else {
			if (counter < 26)
				counter++;
			countbytes = new byte[1];
			countbytes[0] = (byte)('a' + (counter - 1));
		}
		return countbytes;
	}
	static public Karto_counter Try_new(byte[] key) {
		int mslen = key.length;
		if (mslen > 3) {
	  	if (key[0] == '-') {
	  		if (key[1] == 'n') {
					return new Karto_counter(key, true);
	  		} else if (key[1] == 'l') {
					return new Karto_counter(key, false);
				}
			}
		}
		return null;
	}
}
