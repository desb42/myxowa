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
package gplx.xowa.xtns.wbases.core;
import gplx.xowa.xtns.wbases.claims.enums.*;
public class Wdata_dict_form {
	public static final byte
	  Tid__id										= 0
	, Tid__claims								= 1
	, Tid__representations			= 2
	, Tid__grammaticalFeatures	= 3
	;
	public static final    Wbase_enum_hash Reg = new Wbase_enum_hash("core.form", 4);
	public static final    Wbase_enum_itm
	  Itm__id										= Reg.Add(Tid__id		, "id")
	, Itm__claims								= Reg.Add(Tid__claims		, "claims")
	, Itm__representations			= Reg.Add(Tid__representations		, "representations")
	, Itm__grammaticalFeatures	= Reg.Add(Tid__grammaticalFeatures		, "grammaticalFeatures")
	;
}
