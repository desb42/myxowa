/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2022 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.scribunto.engines.luaj;

import gplx.Keyval;
import gplx.Keyval_;
import gplx.String_;
import gplx.xowa.Xoae_app;
import gplx.xowa.xtns.scribunto.Scrib_core;
import gplx.xowa.xtns.scribunto.Scrib_kv_utl_;
import gplx.xowa.xtns.scribunto.Scrib_lua_proc;
import gplx.xowa.xtns.scribunto.Scrib_xtn_mgr;
import gplx.xowa.xtns.scribunto.engines.Scrib_engine;
import gplx.xowa.xtns.scribunto.engines.Scrib_server;
import gplx.xowa.xtns.scribunto.procs.Scrib_proc;
import gplx.xowa.xtns.scribunto.procs.Scrib_proc_args;
import gplx.xowa.xtns.scribunto.procs.Scrib_proc_mgr;
import gplx.xowa.xtns.scribunto.procs.Scrib_proc_rslt;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LuaClosure;

public class Luaj_engine implements Scrib_engine {
	private Luaj_server_func_recv func_recv;
	private Luaj_server_func_recv_x func_recv_x;
	private final Luaj_server_func_dbg func_dbg;
	private final Scrib_proc_mgr proc_mgr;
	private final Scrib_core core;	
	private Luaj_server server;
	private final LuaTable goodmsg;
	private final LuaTable callmsg;
	private final LuaTable compilemsg;
	private final LuaTable loadmsg;
	private final LuaTable loadprotomsg;
	public Luaj_engine(Xoae_app app, Scrib_core core, boolean debug_enabled) {
		this.core = core;
		this.proc_mgr = core.Proc_mgr();
		this.func_recv = new Luaj_server_func_recv(this);
		this.func_recv_x = new Luaj_server_func_recv_x(this);
		this.func_dbg = new Luaj_server_func_dbg(core);
		this.server = new Luaj_server(func_recv, func_dbg, func_recv_x);
		this.goodmsg = LuaValue.tableOf();
		goodmsg.set("op", Val_rMessage);
		this.callmsg = LuaValue.tableOf();
		callmsg.set("op", Val_callFunction);
		this.compilemsg = LuaValue.tableOf();
		compilemsg.set("op", Val_compile);
		this.loadmsg = LuaValue.tableOf();
		loadmsg.set("op", Val_loadString);
		this.loadprotomsg = LuaValue.tableOf();
		loadprotomsg.set("op", Val_loadprotoString);
	}
	public Scrib_server Server() {return server;} public void Server_(Scrib_server v) {server = (Luaj_server)v;} 
	public boolean Dbg_print() {return dbg_print;} public void Dbg_print_(boolean v) {dbg_print = v;} private boolean dbg_print;
	public Scrib_lua_proc LoadString(String name, String text) {
/*
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", Val_loadString);
		msg.set("text", LuaValue.valueOf(text));
		msg.set("chunkName", LuaValue.valueOf(name));
		return load_dispatch(name, msg);
*/
		loadmsg.set("text", LuaValue.valueOf(text));
		loadmsg.set("chunkName", LuaValue.valueOf(name));
		return load_dispatch(name, loadmsg);
	}
	public Scrib_lua_proc LoadString(String name, byte[] text) {
		loadmsg.set("text", LuaValue.valueOf(text));
		loadmsg.set("chunkName", LuaValue.valueOf(name));
		return load_dispatch(name, loadmsg);
	}
	public Scrib_lua_proc LoadClosure(Object closure) {
		loadprotomsg.set("closure", (LuaClosure)closure); // somehow proto becomes a LuaTable
		return load_dispatch("closure", loadprotomsg);
	}
	private Scrib_lua_proc load_dispatch(String name, LuaTable msg) {
		LuaTable rsp = server.Dispatch(msg);
		// need to check for error
		String op = Luaj_value_.Get_val_as_str(rsp, "op");
		char firstchr = op.charAt(0);
		//if (String_.Eq(op, "error"))
		if (firstchr == 'e') {
			String err = Luaj_value_.Get_val_as_str(rsp, "value");
			throw Scrib_xtn_mgr.err_(err);
		}
		// assume OK

		LuaTable values_tbl = Luaj_value_.Get_val_as_lua_table(rsp, "values");
		LuaInteger proc_id = (LuaInteger)values_tbl.rawget(1);
		return new Scrib_lua_proc(name, proc_id.v);
	}
	public void RegisterLibrary(Keyval[] functions) {
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", Val_registerLibrary);
		msg.set("name", "mw_interface");
		msg.set("functions", Luaj_value_.Obj_to_lua_val(server, functions));
		server.Dispatch(msg);
	}
	public Keyval[] CallFunction(int id, Keyval[] args) {
		int args_len = args.length;
/*
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", Val_callFunction);
		msg.set("id", LuaValue.valueOf(id));
		msg.set("nargs", LuaValue.valueOf(args_len));
		msg.set("args", Luaj_value_.Obj_to_lua_val(server, args));
		return this.Dispatch_as_kv_ary(msg);
*/
		callmsg.set("id", LuaValue.valueOf(id));
		callmsg.set("nargs", LuaValue.valueOf(args_len));
		callmsg.set("args", Luaj_value_.Obj_to_lua_val(server, args));
		return this.Dispatch_as_kv_ary(callmsg);
	}
	public Keyval[] CompileFunction(String name) {
/*
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", Val_compile);
		msg.set("text", LuaValue.valueOf(name));
		return this.Dispatch_as_kv_ary(msg);
*/
		compilemsg.set("text", LuaValue.valueOf(name));
		return this.Dispatch_as_kv_ary(compilemsg);
	}
	public Keyval[] ExecuteModule(int mod_id) {
		return this.CallFunction(core.Lib_mw().Mod().Fncs_get_id("executeModule"), Scrib_kv_utl_.base1_obj_(new Scrib_lua_proc("", mod_id)));
	}
	public void CleanupChunks(Keyval[] ids) {
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", "cleanupChunks");
		msg.set("ids", Luaj_value_.Obj_to_lua_val(server, ids));
		this.Dispatch_as_kv_ary(msg);		
	}
	public void ClearChunks() {
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", "clearChunks");
		this.Dispatch_as_kv_ary(msg);		
	}
	public Keyval[] Dispatch_as_kv_ary(LuaTable msg) {
		while (true) {
			LuaTable rsp = server.Dispatch(msg);
			String op = Luaj_value_.Get_val_as_str(rsp, "op");
			char firstchr = op.charAt(0);
			//if		(String_.Eq(op, "return"))
			if		(firstchr == 'r')
				return Luaj_value_.Get_val_as_kv_ary(server, rsp, "values");
			//else if (String_.Eq(op, "call"))
			else if (firstchr == 'c')
				msg = Server_recv_call(rsp);
			//else if (String_.Eq(op, "error")) {
			else if (firstchr == 'e') {
				String err = Luaj_value_.Get_val_as_str(rsp, "value");
				core.Handle_error(err);
				return Keyval_.Ary_empty;
			}
			else
				return Keyval_.Ary_empty;
/*
			switch (firstchr) {
				case 'r':
					return Luaj_value_.Get_val_as_kv_ary(server, rsp, "values");
				case 'c':
					msg = Server_recv_call(rsp);
					break;
				case 'e':
					String err = Luaj_value_.Get_val_as_str(rsp, "value");
					core.Handle_error(err);
					return Keyval_.Ary_empty;
				default:
					return Keyval_.Ary_empty;
			}
*/
		}		
	}
	public LuaTable Server_recv_call(LuaTable rsp) {
		String proc_id = Luaj_value_.Get_val_as_str(rsp, "i"); // id
		Keyval[] args = Luaj_value_.Get_val_as_kv_ary_call(server, rsp, "a"); // args
                core.Ctx().Page().Stat_itm().Scrib().Inc_call_count();
/*
                String str_args = "";
                for (int i = 0; i < args.length; i++) {
                    if (i > 0)
                        str_args += ",";
                    String arg = args[i].To_str();
                    if (arg.length() > 100)
                        arg = arg.substring(0, 100) + " ...";
                    str_args += arg;
                }
		System.out.println(proc_id + " " + str_args);
                if ( (proc_id + " " + str_args).startsWith("mw.ustring|gsub")) {//mw.ustring|match 1=district2,2=^chancellor[%d]*$"
                    int a= 1;
                }
*/
		Scrib_proc proc = proc_mgr.Get_by_key(proc_id); if (proc == null) throw Scrib_xtn_mgr.err_("could not find proc with id of {0}", proc_id);
		Scrib_proc_args proc_args = new Scrib_proc_args(args);
		Scrib_proc_rslt proc_rslt = new Scrib_proc_rslt();
		proc.Proc_exec(proc_args, proc_rslt);
		String fail_msg = proc_rslt.Fail_msg();
		if (fail_msg == null) { 
			Keyval[] cbk_rslts = proc_rslt.Ary();
			return ReturnMessage(cbk_rslts);
		}
		else {
			return ReturnFail(fail_msg);			
		}
	}
	public LuaTable Server_recv_call_x(LuaTable rsp) {
		String proc_id = rsp.get(1).tojstring(); //Luaj_value_.Get_val_as_str(rsp, "i"); // id
                //System.out.println("call_x " + proc_id);
		//if (proc_id.equals("mw.language|lc")) {
                //int a=1;
              //}
		int tsize = rsp.length();
		String command = "";
		for (int i = 0; i < tsize; i++)
			command += rsp.get(i+1) + " ";
		System.out.println(command/*tbl_val.get(1)*/);
		Keyval[] args = Luaj_value_.Lua_tbl_to_kv_ary_x(server, rsp);
                core.Ctx().Page().Stat_itm().Scrib().Inc_call_count();
		Scrib_proc proc = proc_mgr.Get_by_key(proc_id); if (proc == null) throw Scrib_xtn_mgr.err_("could not find proc with id of {0}", proc_id);
		Scrib_proc_args proc_args = new Scrib_proc_args(args, args.length);
		Scrib_proc_rslt proc_rslt = new Scrib_proc_rslt();
		proc.Proc_exec(proc_args, proc_rslt);
		String fail_msg = proc_rslt.Fail_msg();
		if (fail_msg == null) { 
			Keyval[] cbk_rslts = proc_rslt.Ary();
			return ReturnMessage(cbk_rslts);
		}
		else {
			return ReturnFail(fail_msg);			
		}
	}
	private LuaTable ReturnMessage(Keyval[] values) {
/*
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", Val_rMessage);
		msg.set("nvalues", LuaValue.valueOf(values.length));
		msg.set("values", Luaj_value_.Obj_to_lua_val(server, values));
		return msg;
*/
		goodmsg.set("nvalues", LuaValue.valueOf(values.length));
		goodmsg.set("values", Luaj_value_.Obj_to_lua_val(server, values));
		return goodmsg;
	}
	private LuaTable ReturnFail(String fail_msg) {
		LuaTable msg = LuaValue.tableOf();
		msg.set("op", Val_error);
		msg.set("value", LuaValue.valueOf(fail_msg));
		return msg;
	}
	public void Term() {
		this.func_recv = null;
		this.func_recv_x = null;
	}
	private static final LuaValue 
	  Val_loadString 		= LuaValue.valueOf("loadString")
	, Val_loadprotoString 		= LuaValue.valueOf("loadClosure")
	, Val_registerLibrary 	= LuaValue.valueOf("registerLibrary")
	, Val_callFunction 		= LuaValue.valueOf("call")
	, Val_returnMessage 	= LuaValue.valueOf("return")
	, Val_rMessage 	= LuaValue.valueOf("r")
	, Val_error 			= LuaValue.valueOf("error")
	, Val_compile 			= LuaValue.valueOf("compile")
	;
}
