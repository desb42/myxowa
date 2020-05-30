package gplx.xowa.xtns.related;

import gplx.core.serials.binarys.BinaryLoadMgr;
import gplx.core.serials.core.SerialCoreFactory;
import gplx.core.serials.core.SerialLoadMgr;
import gplx.core.serials.core.SerialLoadWkr;
import gplx.core.serials.core.SerialSaveMgr;
import gplx.core.serials.core.SerialSaveWkr;

public class RelatedSerialCore {
    private static final int DATA_VERSION = 0;

    public static byte[] Save(byte[] isin) {
        // get wkr
        SerialSaveMgr mgr = SerialCoreFactory.NewSaveMgr(BinaryLoadMgr.CORE_VERSION);
        SerialSaveWkr wkr = mgr.NewSaveWkr();

        // save header
        wkr.SaveHdr(DATA_VERSION);

        // save item
        wkr.SaveBry(isin);
        return wkr.ToBry();
    }

    public static byte[] Load(byte[] data) {
        // get wkr
        SerialLoadMgr mgr = SerialCoreFactory.NewLoadMgr(data);
        SerialLoadWkr wkr = mgr.NewLoadWkr();

        // init
        wkr.Init(mgr.Data(), mgr.HeaderEnd());

        // load items
        return wkr.LoadBry();
    }
}
