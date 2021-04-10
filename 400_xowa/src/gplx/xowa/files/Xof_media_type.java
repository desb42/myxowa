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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
public class Xof_media_type {
	// enum 'UNKNOWN','BITMAP','DRAWING','AUDIO','VIDEO','MULTIMEDIA','OFFICE','TEXT','EXECUTABLE','ARCHIVE','3D'
	public static final byte Tid_null = 0 // aka UNKNOWN
	, Tid_bitmap = 1     // BITMAP
	, Tid_office = 2     // OFFICE
	, Tid_audio = 3      // AUDIO
	, Tid_drawing = 4    // DRAWING
	, Tid_video = 5      // VIDEO
	, Tid_3d = 6         // 3D
	, Tid_multimedia = 7 // MULTIMEDIA
	, Tid_text = 8       // TEXT
	, Tid_executable = 9 // EXECUTABLE
	, Tid_archive = 10   // ARCHIVE
	;
	public static int Xto_int(byte[] src, int val_bgn, int val_end) {
		byte b = src[val_bgn];
		switch (b) {
			case '3': return Tid_3d;
			case 'A': 
				if (src[val_bgn+1] == 'R')
					return Tid_archive;
				else
					return Tid_audio;
			case 'B': return Tid_bitmap;
			case 'D': return Tid_drawing;
			case 'M': return Tid_multimedia;
			case 'O': return Tid_office;
			case 'T': return Tid_text;
			case 'U': return Tid_null;
			case 'V': return Tid_video;
			default:
				Gfo_usr_dlg_.Instance.Log_many("", "", "no media_type code for ~{0}", Bry_.Mid(src, val_bgn, val_end));
			// log error
				return Tid_null;
		}
	}
}
