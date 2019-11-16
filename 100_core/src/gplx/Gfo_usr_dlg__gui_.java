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
package gplx;
import gplx.core.consoles.*; import gplx.core.lists.rings.*;
import java.util.concurrent.locks.*;
public class Gfo_usr_dlg__gui_ {
	public static final    Gfo_usr_dlg__gui Noop		= new Gfo_usr_dlg__gui_noop();
	public static final    Gfo_usr_dlg__gui Console		= new Gfo_usr_dlg__gui_console();
	public static final    Gfo_usr_dlg__gui Test		= new Gfo_usr_dlg__gui_mock();
	public static final    Gfo_usr_dlg__gui Mem			= new Gfo_usr_dlg__gui_mem_string();
	public static String Mem_file() {return ((Gfo_usr_dlg__gui_mem_string)Mem).file;}
}
class Gfo_usr_dlg__gui_noop implements Gfo_usr_dlg__gui {
	public void Clear() {}
	public Ring__string Prog_msgs() {return ring;} Ring__string ring = new Ring__string().Max_(0);
	public void Write_prog(String text) {}
	public void Write_note(String text) {}
	public void Write_warn(String text) {}
	public void Write_stop(String text) {}
}
class Gfo_usr_dlg__gui_console implements Gfo_usr_dlg__gui {
	public static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final    Console_adp__sys console = Console_adp__sys.Instance;
	public void Clear() {}
	public Ring__string Prog_msgs() {return ring;} private final    Ring__string ring = new Ring__string().Max_(0);
	public void Write_prog(String text) {
            try {
		rwl.writeLock().lock();
                console.Write_tmp(text);
            }
            finally {
		rwl.writeLock().unlock();
            }
        }
	public void Write_note(String text) {
            try {
		rwl.writeLock().lock();
                console.Write_str_w_nl(text);
            }
            finally {
		rwl.writeLock().unlock();
            }
        }
	public void Write_warn(String text) {
            try {
		rwl.writeLock().lock();
                console.Write_str_w_nl(text);
            }
            finally {
		rwl.writeLock().unlock();
            }
        }
	public void Write_stop(String text) {
            try {
		rwl.writeLock().lock();
                console.Write_str_w_nl(text);
            }
            finally {
		rwl.writeLock().unlock();
            }
        }
}
class Gfo_usr_dlg__gui_mem_string implements Gfo_usr_dlg__gui {
	public String file = "";
	public void Clear() {file = "";}
	public Ring__string Prog_msgs() {return ring;} private final    Ring__string ring = new Ring__string().Max_(0);
	public void Write_prog(String text) {file += text + "\n";}
	public void Write_note(String text) {file += text + "\n";}
	public void Write_warn(String text) {file += text + "\n";}
	public void Write_stop(String text) {file += text + "\n";}
}
