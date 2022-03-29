/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2021 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa;
import gplx.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import gplx.core.brys.fmtrs.Bry_fmtr;
import gplx.core.ios.IoEngine_system;
public class Db_logger {
	private FileChannel fc;
	private FileOutputStream fos;
	private Io_url url;
	//private final Bry_bfr tmp_bfr = Bry_bfr_.New();
	private final Bry_fmtr tmp_fmtr = Bry_fmtr.New__tmp().Fail_when_invalid_escapes_(false);	// do not fail b/c msgs may contain excerpt of random text; EX:[[User:A|~A~]] DATE:2014-11-28

	protected void finalize() {
		Close();
	}
	public Db_logger(Io_url url, int wrk_id) {
		this.url = url;
		if (!Io_mgr.Instance.ExistsDir(url.OwnerDir()))
			Io_mgr.Instance.CreateDir(url.OwnerDir());
		try {
			fos = new FileOutputStream(url.Xto_api(), true);
		}
		catch (
			FileNotFoundException e) {throw IoEngine_system.Err_Fil_NotFound(e, url);
		}
		fc = fos.getChannel();
	}
	public void Log_many(String fmt, Object... args) {
		byte[] textBytes = Bld_msg_many( fmt, args );
		try {
			fc.write(ByteBuffer.wrap(textBytes));
		}
		catch	(IOException e) {
			Close();
			throw Err_.new_exc(e, "io", "write data to file failed", "url", url.Xto_api());
		}
	}
	public void Close() {
		try {
			fc.close();
			fos.close();
		}
		catch (IOException e) {
			//
		}
	}
	private byte[] Bld_msg_many(String fmt, Object[] args) {
		try {
                    Bry_bfr tmp_bfr = Bry_bfr_.New();
			tmp_bfr.Add_str_u8(Datetime_now.Get_force().XtoUtc().XtoStr_fmt_yyyyMMdd_HHmmss_fff());
			tmp_bfr.Add_byte(Byte_ascii.Space);
			tmp_fmtr.Fmt_(fmt).Bld_bfr_many(tmp_bfr, args);
			tmp_bfr.Add_byte(Byte_ascii.Nl);
			return tmp_bfr.To_bry_and_clear();
		}
		catch (Exception e) {	// NOTE: can fail if fmt has ~{}; callers should proactively remove, but for now, just return fmt if fails; EX:Page_sync and en.w:Web_crawler; DATE:2016-11-17
			Err_.Noop(e);
			return Bry_.new_u8(fmt);
		}
	}
}
