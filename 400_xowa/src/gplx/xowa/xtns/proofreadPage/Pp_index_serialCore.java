/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2020 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.proofreadPage;

import gplx.core.serials.binarys.BinaryLoadMgr;
import gplx.core.serials.core.SerialCoreFactory;
import gplx.core.serials.core.SerialLoadMgr;
import gplx.core.serials.core.SerialLoadWkr;
import gplx.core.serials.core.SerialSaveMgr;
import gplx.core.serials.core.SerialSaveWkr;
import gplx.objects.brys.Bry_;
import gplx.xowa.xtns.pagebanners.Pgbnr_itm;

public class Pp_index_serialCore {
    private static final int DATA_VERSION = 0;

    public static byte[] Save(byte[] txt) {
        // get wkr
        SerialSaveMgr mgr = SerialCoreFactory.NewSaveMgr(BinaryLoadMgr.CORE_VERSION);
        SerialSaveWkr wkr = mgr.NewSaveWkr();

        // save header
        wkr.SaveHdr(DATA_VERSION);

        // save item
        wkr.SaveBry(txt);

        return wkr.ToBry();
    }

    public static byte[] Load(byte[] data) {
        // get wkr
        SerialLoadMgr mgr = SerialCoreFactory.NewLoadMgr(data);
        SerialLoadWkr wkr = mgr.NewLoadWkr();

        // init
        wkr.Init(mgr.Data(), mgr.HeaderEnd());

        // load item
        return wkr.LoadBry();
    }
}
