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
package gplx.xowa.xtns.kartographers; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
class Maplink_xnde_atrs {
	public static final byte 
	  Tid__none			= 0
	, Tid__latitude		= 1
	, Tid__longitude		= 2
	, Tid__text		= 3
	, Tid__zoom		= 4
	;
	public static final    Hash_adp_bry Key_hash = Hash_adp_bry.ci_a7()
	.Add_str_byte("none"			, Tid__none)
	.Add_str_byte("latitude"			, Tid__latitude)
	.Add_str_byte("longitude"			, Tid__longitude)
	.Add_str_byte("text"			, Tid__text)
	.Add_str_byte("zoom"			, Tid__zoom)
	;
}
