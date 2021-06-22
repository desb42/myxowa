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
package gplx.xowa; import gplx.*;
import java.io.OutputStream; import java.io.FileOutputStream; import java.io.File; import java.io.IOException;
import java.io.BufferedReader; import java.io.*;

public class Db_readwrite {

	public static void writeFile(String data, String filename) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(filename));
			byte[] db = data.getBytes();
			os.write(db, 0, db.length);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
