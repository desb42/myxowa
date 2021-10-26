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
package gplx.xowa.addons.bldrs.files.cmds; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.bldrs.files.*;
import org.junit.*; import gplx.core.ios.*; import gplx.xowa.files.*;
public class Xobldr__image__create_tst {
	private Xobldr__image__create_fxt fxt = new Xobldr__image__create_fxt();
	@Test  public void Basic()					{fxt.Init_("A.png"	, Xof_media_type.Tid_bitmap	, Xof_ext_.Bry_png, 220, 110)		.Test(Xof_ext_.Id_png);}	// A.png -> png
	@Test  public void Ogg_VIDEO()				{fxt.Init_("A.ogg"	, Xof_media_type.Tid_video		, Xof_ext_.Bry_ogg, 220, 110)		.Test(Xof_ext_.Id_ogv);}	// A.ogg and VIDEO -> ogv
	@Test  public void Ogg_VIDEO_null_size()	{fxt.Init_("A.ogg"	, Xof_media_type.Tid_video		, Xof_ext_.Bry_ogg,   0,   0)		.Test(Xof_ext_.Id_ogg);}	// A.ogg but 0,0 -> ogg (not ogv)
	@Test  public void Png_is_jpg()				{fxt.Init_("A.png"	, Xof_media_type.Tid_bitmap	, Xof_ext_.Bry_jpg, 220, 110)		.Test(Xof_ext_.Id_jpg);}	// A.png and jpg -> jpg
	@Test  public void Jpeg_is_jpeg()			{fxt.Init_("A.jpeg"	, Xof_media_type.Tid_bitmap	, Xof_ext_.Bry_jpg, 220, 110)		.Test(Xof_ext_.Id_jpeg);}	// A.jpeg and jpg -> jpeg (unchanged)
}
class Xobldr__image__create_fxt {
	private byte[] name; int w, h, media_type_id;
	private Xof_ext mime_ext;
	public Xobldr__image__create_fxt Init_png() {Name_("A.png").Media_type_(Xof_media_type.Tid_bitmap).Minor_mime_(Xof_ext_.Bry_png).W_(220).H_(110);
		return this;
	}
	public Xobldr__image__create_fxt Init_(String name, int media_type_id, byte[] minor_mime, int w, int h) {
		Name_(name);
		Media_type_(media_type_id); 
		Minor_mime_(minor_mime);
		W_(w);
		H_(h);
		return this;
	}
	public Xobldr__image__create_fxt Name_(String v) {name = Bry_.new_a7(v); return this;}
	public Xobldr__image__create_fxt Media_type_(int v) {media_type_id = v; return this;}
	public Xobldr__image__create_fxt Minor_mime_(byte[] v) {mime_ext = Xof_mime_minor_.ext_(v); return this;}
//	public Xobldr__image__create_fxt Minor_mime_(int v) {return Minor_mime_(v);}
	public Xobldr__image__create_fxt W_(int v) {w = v; return this;}
	public Xobldr__image__create_fxt H_(int v) {h = v; return this;}
	public Xobldr__image__create_fxt Test(int expd) {
		Tfds.Eq(expd, Xobldr__image__create.Calc_ext_id(Gfo_usr_dlg_.Noop, name, media_type_id, mime_ext, w, h));
		return this;
	}
}
