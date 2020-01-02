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
class Map_atrs {
	public static final byte 
	  Tid__none			= 0
	, Tid__latitude		= 1
	, Tid__longitude		= 2
	, Tid__zoom		= 3
	, Tid__show		= 4
	, Tid__group		= 5
	, Tid__mapstyle		= 6
	, Tid__text		= 7
	, Tid__width		= 8
	, Tid__height		= 9
	, Tid__align		= 10
	, Tid__lang		= 11
	, Tid__frameless	= 12
	, Tid__class	= 13
	;
	public static final    Hash_adp_bry Key_hash = Hash_adp_bry.ci_a7()
	.Add_str_byte("none"			, Tid__none)
	.Add_str_byte("latitude"			, Tid__latitude)
	.Add_str_byte("longitude"			, Tid__longitude)
	.Add_str_byte("zoom"			, Tid__zoom)
	.Add_str_byte("show"			, Tid__show)
	.Add_str_byte("group"			, Tid__group)
	.Add_str_byte("mapstyle"			, Tid__mapstyle)

	.Add_str_byte("text"			, Tid__text) // only maplink

	.Add_str_byte("width"			, Tid__width)
	.Add_str_byte("height"			, Tid__height)
	.Add_str_byte("align"			, Tid__align)
	.Add_str_byte("lang"			, Tid__lang)
	.Add_str_byte("frameless"		, Tid__frameless)
	.Add_str_byte("class"		, Tid__class)
	;
}
