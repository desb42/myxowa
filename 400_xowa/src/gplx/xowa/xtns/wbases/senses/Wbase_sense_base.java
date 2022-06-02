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
package gplx.xowa.xtns.wbases.senses; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*; import gplx.xowa.xtns.wbases.claims.*;
import gplx.xowa.xtns.wbases.claims.enums.*;
import gplx.xowa.xtns.wbases.core.Wdata_list_label;
public class Wbase_sense_base implements CompareAble {
	public Wbase_sense_base(byte[] id, Ordered_hash claims, Wdata_list_label glosses) {
		this.id = id;
		this.claims = claims;
		this.glosses = glosses;
	}
	public byte[]					Id()				{return id;} private final    byte[] id;
	public Ordered_hash		Claims()		{return claims;} private final    Ordered_hash claims;
	public Wdata_list_label				Glosses()			{return glosses;} private Wdata_list_label glosses;

	public int compareTo(Object obj) {
		Wbase_sense_base comp = (Wbase_sense_base)obj;
		return Bry_.Compare(id, comp.id);
	}
}
