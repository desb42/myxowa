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
import gplx.xowa.*;
import gplx.xowa.langs.Xol_lang_itm;
import gplx.xowa.langs.msgs.*;
import gplx.xowa.xtns.wbases.Wdata_prop_val_visitor;
import gplx.xowa.xtns.wbases.Wdata_wiki_mgr;
import gplx.xowa.xtns.wbases.claims.itms.Wbase_claim_globecoordinate_;
// from https://github.com/wikimedia/mediawiki-extensions-Kartographer/includes/CoordFormatter.php
// used by formatValue
public class Db_coordformatter {
	public static void format(Bry_bfr bfr, Xowe_wiki wiki, byte[] page_url, Keyval[] kvs) {
		double lat = 0, lng = 0, prc = 0;
		String alt = null, glb = null;
		int len = kvs.length;
		for (int i = 0; i < len; ++i) {
			Keyval kv = kvs[i];
			byte val_tid = Wbase_claim_globecoordinate_.Reg.Get_tid_or_max_and_log(page_url, kv.Key()); if (val_tid == Byte_.Max_value_127) continue;
			switch (val_tid) {
				case Wbase_claim_globecoordinate_.Tid__latitude:		lat = Double_.cast(kv.Val()); break;
				case Wbase_claim_globecoordinate_.Tid__longitude:		lng = Double_.cast(kv.Val()); break;
				case Wbase_claim_globecoordinate_.Tid__altitude:		alt = String_.cast(kv.Val()); break;
				case Wbase_claim_globecoordinate_.Tid__precision:		prc = Double_.cast(kv.Val()); break;
				case Wbase_claim_globecoordinate_.Tid__globe:			glb = String_.cast(kv.Val()); break;
			}
		}
		Xow_msg_mgr msg_mgr = wiki.Msg_mgr();
		bfr.Add(format( lat, lng, msg_mgr ));
	}
/*
	public static function format( $lat, $lon, Language $language ) {
		$latStr = self::formatOneCoord( $lat, 'lat', $language );
		$lonStr = self::formatOneCoord( $lon, 'lon', $language );

		return wfMessage( 'kartographer-coord-combined' )
			->params( $latStr, $lonStr )
			->inLanguage( $language )
			->plain();
	}
*/
	public static byte[] format( double lat, double lon, Xow_msg_mgr msg_mgr ) {
		byte[] latStr = formatOneCoord( lat, Bool_.Y, msg_mgr );
		byte[] lonStr = formatOneCoord( lon, Bool_.N, msg_mgr );
		return msg_mgr.Val_by_key_args(karto_coord_combined, latStr, lonStr);
}
/*
	private static function formatOneCoord( $coord, $latLon, Language $language ) {
		$val = $sign = round( $coord * 3600 );
		$val = abs( $val );
		$degrees = floor( $val / 3600 );
		$minutes = floor( ( $val - $degrees * 3600 ) / 60 );
		$seconds = $val - $degrees * 3600 - $minutes * 60;
		$plusMinus = $sign < 0 ? 'negative' : 'positive';
		$text = wfMessage( 'kartographer-coord-dms' )
			->numParams( $degrees, $minutes, round( $seconds ) )
			->inLanguage( $language )
			->plain();

		// Messages that can be used here:
		// * kartographer-coord-lat-positive
		// * kartographer-coord-lat-negative
		// * kartographer-coord-lon-positive
		// * kartographer-coord-lon-negative
		return wfMessage( "kartographer-coord-$latLon-$plusMinus" )
			->params( $text )
			->inLanguage( $language )
			->plain();
	}
*/
	private static byte[] formatOneCoord( double coord, boolean latLon, Xow_msg_mgr msg_mgr ) {
		double sign = Math.round( coord * 3600 );
		double val = Math.abs( sign );
		int degrees = (int)Math.floor( val / 3600 );
		int minutes = (int)Math.floor( ( val - degrees * 3600 ) / 60 );
		long seconds = Math.round(val - degrees * 3600 - minutes * 60);
		int karto_key;
		if (latLon)
			karto_key = sign < 0 ? 1 : 0;
		else
			karto_key = sign < 0 ? 3 : 2;
		byte[] text = msg_mgr.Val_by_key_args(karto_coord_dms, degrees, minutes, seconds);
		return msg_mgr.Val_by_key_args(karto_coord_msgs[karto_key], text);
	}

	private static byte[][] karto_coord_msgs = new byte[][] {
		Bry_.new_a7("kartographer-coord-lat-positive")
	,	Bry_.new_a7("kartographer-coord-lat-negative")
	,	Bry_.new_a7("kartographer-coord-lon-positive")
	,	Bry_.new_a7("kartographer-coord-lon-negative")
	};
	private static byte[] karto_coord_dms = Bry_.new_a7("kartographer-coord-dms");
	private static byte[] karto_coord_combined = Bry_.new_a7("kartographer-coord-combined");
}