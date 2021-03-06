package gplx.xowa.xtns.related;

import gplx.core.serials.binarys.BinaryLoadMgr;
import gplx.core.serials.core.SerialCoreFactory;
import gplx.core.serials.core.SerialLoadMgr;
import gplx.core.serials.core.SerialLoadWkr;
import gplx.core.serials.core.SerialSaveMgr;
import gplx.core.serials.core.SerialSaveWkr;

import gplx.List_adp;
import gplx.List_adp_;

public class RelatedSerialCore {
    private static final int DATA_VERSION = 0;

    public static byte[] Save(List_adp related) {
        // get wkr
        SerialSaveMgr mgr = SerialCoreFactory.NewSaveMgr(BinaryLoadMgr.CORE_VERSION);
        SerialSaveWkr wkr = mgr.NewSaveWkr();

        // save header
        wkr.SaveHdr(DATA_VERSION);

        // save items
        int len = related.Count();
        wkr.SaveInt(len);
        for (int i = 0; i < len; i++) {
            byte[] rel = (byte[])related.Get_at(i);
            wkr.SaveBry(rel);
        }
        return wkr.ToBry();
    }

    public static List_adp Load(byte[] data) {
        // get wkr
        SerialLoadMgr mgr = SerialCoreFactory.NewLoadMgr(data);
        SerialLoadWkr wkr = mgr.NewLoadWkr();

        // init
        wkr.Init(mgr.Data(), mgr.HeaderEnd());

        // load items
        int len = wkr.LoadInt();
        List_adp list = List_adp_.New();
        for (int i = 0; i < len; i++) {
            list.Add(wkr.LoadBry());
        }
        return list;
    }
}
