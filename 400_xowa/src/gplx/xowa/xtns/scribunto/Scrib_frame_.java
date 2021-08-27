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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.parsers.tmpls.*;
public class Scrib_frame_ {
	public static final byte Tid_null = 0, Tid_current = 1, Tid_parent = 2, Tid_dynamic = 3;
	public static final int Arg_adj_current = 1, Arg_adj_owner = 0;
	public static int Get_frame_tid(String frame_id) {
		switch(frame_id.charAt(0)) {
			case 'c':
				return Tid_current;
			case 'p':
				return Tid_parent;
			default:
				return Tid_dynamic;
		}
/* replaces 20210814
		if		(firstchr == 'r')
		if		(String_.Eq(frame_id, "current"))	return Tid_current;
		else if (String_.Eq(frame_id, "parent"))	return Tid_parent;
		else										return Tid_dynamic;
*/
	}
	public static Xot_invk Get_frame(Scrib_core core, String frame_id) {
		switch(frame_id.charAt(0)) {
			case 'c': // "current"
				return core.Frame_current();
			case 'p': // "parent"
				Xot_invk invk = core.Frame_parent();
				// if parent is Mock should this return "current"? 20210814
				// eg en.wikipedia.org/wiki/Module:LuaCall/doc
				if (invk instanceof Xot_invk_mock) {
					byte[] ttl = invk.Frame_ttl();
					if (ttl.length == 6 && ttl[0] == 'f' && ttl[5] == '0') { // frame0 ?
						invk = core.Frame_current();
						Gfo_usr_dlg_.Instance.Log_many("", "", "return \"current\": ttl=~{0}", core.Page().Ttl().Full_db());
					}
				}
				return invk;
			case 'e': // "empty"
				return Xot_invk_mock.new_(core.Frame_current().Defn_tid(), 0, null, Keyval_.Ary_empty);	// not sure if it should return null title; DATE:2014-07-12
			default:
				return (Xot_invk)core.Frame_created_list().Get_by(frame_id);	// NOTE: can return null; some calls expect nil; EX:mw.lua and "currentFrame = newFrame( 'empty' )"; DATE:2014-07-12
		}
/* replaces 20210814
		if		(String_.Eq(frame_id, "current"))	return core.Frame_current();
		else if (String_.Eq(frame_id, "parent"))	return core.Frame_parent();
		else if (String_.Eq(frame_id, "empty"))		return Xot_invk_mock.new_(core.Frame_current().Defn_tid(), 0, null, Keyval_.Ary_empty);	// not sure if it should return null title; DATE:2014-07-12
		else {
			return (Xot_invk)core.Frame_created_list().Get_by(frame_id);	// NOTE: can return null; some calls expect nil; EX:mw.lua and "currentFrame = newFrame( 'empty' )"; DATE:2014-07-12
		}
*/
	}
	public static int Get_arg_adj(byte frame_tid) {
		if		(frame_tid == Tid_current)	return 1;		// arg_0 is at idx_1 b/c Module frames use 1 arg for name of proc; EX: {{#invoke:module|proc|arg_0}}
		else								return 0;		// arg_0 is at idx_0; EX: {{template|arg_0}}
	}
	public static Xot_invk Get_parent(Scrib_core core, byte frame_tid) {
                //if (frame_tid == Tid_null) {
                //    System.out.println("?");
                //}
		if		(frame_tid == Tid_current)	return core.Frame_parent();			// current frame has an owner frame
		else								return Xot_invk_mock.Null;			// all other frames do not
	}
}
