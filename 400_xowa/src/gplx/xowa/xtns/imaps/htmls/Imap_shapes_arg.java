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
package gplx.xowa.xtns.imaps.htmls; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.imaps.*;
import gplx.xowa.xtns.imaps.itms.*; import gplx.core.brys.fmtrs.*;
public class Imap_shapes_arg implements gplx.core.brys.Bfr_arg {
	private final    Imap_part_shape[] shapes;
	private final    Imap_shape_pts_arg pts_arg;
	public Imap_shapes_arg(Imap_part_shape[] shapes, double scale) {
		this.shapes = shapes;
		this.pts_arg = new Imap_shape_pts_arg(scale);
	}
	public void Bfr_arg__add(Bry_bfr bfr) {
		Bry_fmtr fmtr = Imap_html_fmtrs.Area;
		Bry_fmtr fmtr_notitle = Imap_html_fmtrs.Area_notitle;
		int len = shapes.length;
		for (int i = 0; i < len; ++i)
			Fmt_shape(bfr, fmtr, fmtr_notitle, pts_arg, shapes[i]);
	}
	public static void Fmt_shape(Bry_bfr bfr, Bry_fmtr fmtr, Bry_fmtr fmtr_notitle, Imap_shape_pts_arg pts_arg, Imap_part_shape shape) {
		pts_arg.Pts_(shape.Shape_pts());
		byte[] linktxt = Db_encode.Title(shape.Link_text());
		//byte[] linktxt = shape.Link_text();
		if (linktxt == null)
			fmtr_notitle.Bld_bfr_many(bfr
			, shape.Link_href()
			, Imap_part_.To_shape_key(shape.Part_tid())
			, pts_arg
			);
		else
			fmtr.Bld_bfr_many(bfr
			, shape.Link_href()
			, Imap_part_.To_shape_key(shape.Part_tid())
			, pts_arg
			, linktxt
			);
	}
}
