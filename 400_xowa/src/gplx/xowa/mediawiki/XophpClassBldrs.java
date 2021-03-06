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
package gplx.xowa.mediawiki; import gplx.*; import gplx.xowa.*;
public class XophpClassBldrs {
	private final    Ordered_hash hash = Ordered_hash_.New();
	public void Add(String id, XophpClassBldr bldr) {
		hash.Add(id, bldr);
	}
	public XophpClassBldr Get_by_or_null(String id) {
		return (XophpClassBldr)hash.Get_by(id);
	}
}
