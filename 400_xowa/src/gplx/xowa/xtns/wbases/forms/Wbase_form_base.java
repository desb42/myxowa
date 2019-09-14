/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2019 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.wbases.forms; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*; import gplx.xowa.xtns.wbases.claims.*;
import gplx.xowa.xtns.wbases.claims.enums.*;
public class Wbase_form_base implements CompareAble {
	public Wbase_form_base(byte[] id, Ordered_hash claims, Ordered_hash reps, int[] grams) {
		this.id = id;
		this.claims = claims;
		this.reps = reps;
		this.grams = grams;
	}
	public byte[]					Id()				{return id;} private final    byte[] id;
	public Ordered_hash		Claims()		{return claims;} private final    Ordered_hash claims;
	public Ordered_hash				Reps()			{return reps;} private Ordered_hash reps;
	public int[]					Grams()			{return grams;} private final int[] grams;

	public int compareTo(Object obj) {
		Wbase_form_base comp = (Wbase_form_base)obj;
		return Bry_.Compare(id, comp.id);
	}
}
