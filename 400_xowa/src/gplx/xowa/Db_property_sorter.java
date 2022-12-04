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
package gplx.xowa;

import gplx.*;
import gplx.core.lists.ComparerAble;
import gplx.xowa.xtns.scribunto.Scrib_core;
import gplx.xowa.xtns.wbases.dbs.Xowb_prop_tbl_itm;
import gplx.xowa.xtns.wbases.stores.Wbase_prop_mgr;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class Db_property_sorter {
	private final List_adp lst;
	private final Wbase_prop_mgr prop_mgr;
	public Db_property_sorter(Wbase_prop_mgr prop_mgr) {
		lst = List_adp_.New();
		this.prop_mgr = prop_mgr;
	}
	public void Add(int sort_pos, String propertyId) {
		lst.Add(new proporder(sort_pos, propertyId));
	}
	public boolean Add(String propertyId) {
		Xowb_prop_tbl_itm tbl_itm = prop_mgr.Get_or_null(propertyId, null);
		if (tbl_itm == null)
			return false;
		lst.Add(new proporder(tbl_itm.Sort_pos(), propertyId));
		return true;
	}
	public void Sort() {
		lst.Sort_by(new propsorter());
	}
	public String Get_at(int i) {
		proporder prop = (proporder)lst.Get_at(i);
		return prop.propertyId;
	}
	public int Count() {
		return lst.Count();
	}
	public static LuaTable OrderProperties_lua(Keyval[] propertyIds, Scrib_core core) {
		int prop_len = propertyIds.length;
		LuaTable rv = LuaValue.tableOf();
		if (prop_len == 1) {
			rv.set(1, LuaValue.valueOf((String)propertyIds[0].Val()));
		}
		else if (prop_len > 1) {
			Wbase_prop_mgr prop_mgr = core.App().Wiki_mgr().Wdata_mgr().Prop_mgr();
			Db_property_sorter propsort = new Db_property_sorter(prop_mgr);
			List_adp list_unordered = List_adp_.New();
//
//			// item is [{P1,1}]
//			XophpArray propertyOrder = this.getPropertyOrderProvider().getPropertyOrder();
// order from www.wikidata.org/wiki/MediaWiki:Wikibase-SortedProperties
			for (int i = 0; i < prop_len; i++) {
				Keyval propertyIdKv = propertyIds[i];
				// item is [{0,P1}]
				String propertyId = propertyIdKv.Val_to_str_or_empty();
				if (!propsort.Add(propertyId))
					list_unordered.Add(propertyId);
			}
			propsort.Sort();
			int pos = 1;
			int len = propsort.Count();
			for (int i = 0; i < len; i++) {
				rv.set(pos++, LuaValue.valueOf(propsort.Get_at(i)));
			}
			len = list_unordered.Count();
			for (int i = 0; i < len; i++) {
				rv.set(pos++, LuaValue.valueOf((String)list_unordered.Get_at(i)));
			}
		}
		return rv;
	}

	public static Keyval[] OrderProperties(Keyval[] propertyIds, Scrib_core core) {
		int prop_len = propertyIds.length;
//
		if (prop_len > 1) {
			Wbase_prop_mgr prop_mgr = core.App().Wiki_mgr().Wdata_mgr().Prop_mgr();
			List_adp list_ordered = List_adp_.New();
			List_adp list_unordered = List_adp_.New();
//
//			// item is [{P1,1}]
//			XophpArray propertyOrder = this.getPropertyOrderProvider().getPropertyOrder();
// order from www.wikidata.org/wiki/MediaWiki:Wikibase-SortedProperties
			for (int i = 0; i < prop_len; i++) {
				Keyval propertyIdKv = propertyIds[i];
				// item is [{0,P1}]
				String propertyId = propertyIdKv.Val_to_str_or_empty();
				Xowb_prop_tbl_itm tbl_itm = prop_mgr.Get_or_null(propertyId, null);
				if (tbl_itm != null) {
					list_ordered.Add(new proporder(tbl_itm.Sort_pos(), propertyId));
				} else {
					list_unordered.Add(propertyId);
				}
			}
			list_ordered.Sort_by(new propsorter());
			int pos = 0;
			int len = list_ordered.Count();
			for (int i = 0; i < len; i++) {
				proporder prop = (proporder)list_ordered.Get_at(i);
				propertyIds[pos] = Keyval_.int_(pos+1, prop.propertyId);
				pos++;
			}
			len = list_unordered.Count();
			for (int i = 0; i < len; i++) {
				propertyIds[pos] = Keyval_.int_(pos+1, (String)list_unordered.Get_at(i));
				pos++;
			}
		}
		return propertyIds;
	}
}
class proporder {
	protected int sort_pos;
	protected String propertyId;
	protected proporder(int sort_pos, String propertyId) {
		this.sort_pos = sort_pos;
		this.propertyId = propertyId;
	}
}
class propsorter implements ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		proporder lhs = (proporder)lhsObj, rhs = (proporder)rhsObj;
		return Int_.Compare(lhs.sort_pos, rhs.sort_pos);
	}
}
