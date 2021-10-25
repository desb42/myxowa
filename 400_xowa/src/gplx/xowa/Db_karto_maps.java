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
import gplx.langs.jsons.*;
import gplx.core.security.algos.*;
public class Db_karto_maps {
	private List_adp lst = List_adp_.New();
	private Xoa_ttl ttl;
	public Db_karto_maps(Xoa_ttl ttl) {
		this.ttl = ttl;
	}
	public byte[] Add(byte[] data) {
		Hash_algo md5_algo = Hash_algo_.New__md5();
		md5_algo.Update_digest(data, 0, data.length);
		byte[] key = md5_algo.To_hash_bry();
		int len = lst.Len();
		Karto_map map;
		for (int i = 0; i < len; i++) {
			map = (Karto_map)lst.Get_at(i);
			if (Bry_.Eq(map.Key(), key)) {
				return map.Sha1();
			}
		}
		byte[] norm = Bry_.new_u8(Buildkey(data));
                if (norm.length == 0) return null;
		byte[] sha1 = Hash_algo_utl.Calc_hash_as_bry(sha1_hash, norm);
		lst.Add(new Karto_map(key, data, norm, sha1));
		return sha1;
	}
	public int Count() { return lst.Len(); }
	public void Clear() {lst.Clear();}
	public void Dump(Bry_bfr bfr) {
		int len = lst.Len();
		if (len == 0) return;
		bfr.Add_str_a7(";\n config={\"karto_inf\":[");
		Karto_map map;
		for (int i = 0; i < len; i++) {
			map = (Karto_map)lst.Get_at(i);
			if (i > 0)
				bfr.Add_str_a7(",");
			bfr.Add_str_a7("{\"").Add(map.Key()).Add_str_a7("\":");
			bfr.Add(map.Data()).Add_str_a7("}\n");
			bfr.Add_str_a7("// ").Add(map.Norm()).Add_str_a7("\n// ").Add(map.Sha1()).Add_str_a7("\n");
		}
		bfr.Add_str_a7("]}\n");
	}
	private String Genproperty(Json_itm itm) {
		String val = "";
		int glen;
		switch (itm.Tid()) {
			case Json_itm_.Tid__bool:
				if (((Json_itm_bool)itm).Data_as_bool())
				  val = "true";
				else
				  val = "false";
				break;
			case Json_itm_.Tid__null:
				val = "null";
				break;
			case Json_itm_.Tid__decimal:
				val = String_.new_a7(Bry_.Zerotrim(itm.Data_bry()));
				break;
			case Json_itm_.Tid__ary:
				Json_ary gary = Json_ary.cast(itm);
				glen = gary.Len();
				val += "[";
				for (int i = 0; i < glen; i++) {
					if (i > 0)
						val += ",";
					itm = gary.Get_at(i);
					val += Genproperty(itm);
				}
				val += "]";
				break;
			case Json_itm_.Tid__nde:
				Json_nde gnde = Json_nde.Cast(itm);
				glen = gnde.Len();
				val += "{";
				for (int i = 0; i < glen; i++) {
					if (i > 0)
						val += ",";
					Json_kv kv = gnde.Get_at_as_kv(i);
					val += "\"" + kv.Key_as_str() + "\":" + Genproperty(kv.Val());
				}
				val += "}";
				break;
			case Json_itm_.Tid__long:
			case Json_itm_.Tid__int:
				val = String_.new_a7(itm.Data_bry());
				break;
			case Json_itm_.Tid__str:
				val = "\"" + String_.new_u8(itm.Data_bry()) + "\"";
				break;
			default:
				val = String_.new_u8(itm.Data_bry());
				break;
		}
		return val;
	}
	private String Genproperties(Json_nde nde, String service) {
		Json_nde props = nde.Get_as_nde("properties");
		if (props == null) return "";
		int len = props.Len();
		String txt = ",\"properties\":{";
		for (int i = 0; i < len; i++) {
			if (i > 0)
				txt += ",";
			Json_kv kv = props.Get_at_as_kv(i);
			Json_itm itm = kv.Val();
			txt += "\"" + kv.Key_as_str() + "\":" + Genproperty(itm);
		}
		txt += "}";
		if (service.equals("geoshape")) {
			// switch "stroke-width": and "stroke":
			int s1 = txt.indexOf("\"stroke-width\":");
			int s2 = txt.indexOf("\"stroke\":");
			if (s1 > 0 && s2 > 0 && s1 < s2) {
				int s3 = txt.indexOf(",", s1);
				int s4 = txt.indexOf(",", s2);
				txt = txt.substring(0, s1) + txt.substring(s2, s4+1) + txt.substring(s1, s3+1) + txt.substring(s4+1);
			}
		}
		// Clarendon_Park_Congregational_Church needs "marker-color" before "title"
		int s1 = txt.indexOf("\"title\":");
		int s2 = txt.indexOf("\"marker-color\":");
		if (s1 > 0 && s2 > 0 && s1 < s2) {
			int s3 = txt.indexOf(",", s1);
			int s4 = txt.indexOf(",", s2);
			if (s4 < 0)
				s4 = txt.indexOf("}", s2);
			txt = txt.substring(0, s1) + txt.substring(s2, s4) + "," + txt.substring(s1, s2-1) + txt.substring(s4);
		}
		return txt;
	}
	private String coordinates(Json_itm itm) {
		byte[] coords = Bry_.Empty;
		int glen = 0;
		switch (itm.Tid()) {
			case Json_itm_.Tid__ary:
				Json_ary gary = Json_ary.cast(itm);
				glen = gary.Len();
				coords = Bry_.Add(Bry_.Zerotrim(gary.Get_at(0).Data_bry()), Byte_ascii.Comma_bry, Bry_.Zerotrim(gary.Get_at(1).Data_bry()));
				if (glen == 3)
					coords = Bry_.Add(coords, Byte_ascii.Comma_bry, Bry_.Zerotrim(gary.Get_at(2).Data_bry()));
				break;
			case Json_itm_.Tid__nde:
				Json_nde gnde = Json_nde.Cast(itm);
				glen = gnde.Len();
				coords = Bry_.Add(Bry_.Zerotrim(gnde.Get_as_bry("0")), Byte_ascii.Comma_bry, Bry_.Zerotrim(gnde.Get_as_bry("1")));
				if (glen == 3)
					coords = Bry_.Add(coords, Byte_ascii.Comma_bry, Bry_.Zerotrim(gnde.Get_as_bry("2")));
				break;
			default:
				Xoa_app_.Usr_dlg().Log_many("", "", "karto: bad coordinate page=~{0}", ttl.Full_db());
		}
		return String_.new_a7(coords);
	}
	private String Buildfeature(Json_nde nde) {
		Json_nde geometry = nde.Get_as_nde("geometry");
		String gtype = geometry.Get_as_str("type");
		String coords = "";
		String geotxt = "";
		if (gtype.equals("Point")) {
			Json_kv kv = geometry.Get_kv("coordinates");
			Json_itm itm = kv.Val();
			coords = coordinates(itm);
			geotxt = "{\"type\":\"Feature\",\"geometry\":{\"coordinates\":[" + coords + "],\"type\":\"Point\"}" + Genproperties(nde, "") + "}";
		}
		else if (gtype.equals("LineString")) {
			Json_kv kv = geometry.Get_kv("coordinates");
			Json_itm itm = kv.Val();
			Json_ary points = Json_ary.cast(itm);
			int plen = points.Len();
			for (int k = 0; k < plen; k++) {
				if (k > 0)
					coords += ",";
				Json_itm pitm = points.Get_at(k);
				coords += "[" + coordinates(pitm) + "]";
			}
			geotxt = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[" + coords + "]}" + Genproperties(nde, "") + "}";
		}
		else if (gtype.equals("Polygon")) {
			Json_kv kv = geometry.Get_kv("coordinates");
			Json_itm itm = kv.Val();
			Json_ary gons = Json_ary.cast(itm);
			int glen = gons.Len();
                        coords = "[";
			for (int k = 0; k < glen; k++) {
				if (k > 0)
					coords += ",";
				Json_itm poitm = gons.Get_at(k);
                                Json_ary points = Json_ary.cast(poitm);
                                int plen = points.Len();
                                for (int j = 0; j < plen; j++) {
                                        if (j > 0)
                                                coords += ",";
                                        Json_itm pitm = points.Get_at(j);
                                        coords += "[" + coordinates(pitm) + "]";
                                }
                                coords += "]";
                        }
			geotxt = "{\"type\":\"Feature\"" + Genproperties(nde, "") + ",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[" + coords + "]}}";
                }
		else {
			Xoa_app_.Usr_dlg().Log_many("", "", "karto: bad feature page=~{0} type=~{1}", ttl.Full_db(), gtype);
		}
		return geotxt;
	}
	private byte[] Encode(byte[] src) {
		int src_len = src.length;
		int pos = 0;
		int startpos = 0;
		Bry_bfr bfr = null;
		byte[] cvtbytes;
		while (pos < src_len) {
			byte b = src[pos++];
			cvtbytes = null;
			switch(b) {
				case ' ':
					cvtbytes = Bry_plus;
					break;
				case '?':
					cvtbytes = Bry_qmark;
					break;
				case '(':
					cvtbytes = Bry_openr;
					break;
				case ')':
					cvtbytes = Bry_closer;
					break;
				case ',':
					cvtbytes = Bry_comma;
					break;
				case '/':
					cvtbytes = Bry_slash;
					break;
				case '#':
					cvtbytes = Bry_hash;
					break;
				case '\'':
					cvtbytes = Bry_quote;
					break;
				case ':':
					cvtbytes = Bry_colon;
					break;
				case '[':
					cvtbytes = Bry_opens;
					break;
				case ']':
					cvtbytes = Bry_closes;
					break;
				case '|':
					cvtbytes = Bry_pipe;
					break;
				case '{':
					cvtbytes = Bry_openq;
					break;
				case '}':
					cvtbytes = Bry_closeq;
					break;
				case '*':
					cvtbytes = Bry_star;
					break;
				case '<':
					cvtbytes = Bry_opena;
					break;
				case '>':
					cvtbytes = Bry_closea;
					break;
			}
			if (cvtbytes != null) {
				if (bfr == null)
					bfr = Bry_bfr_.New();
				bfr.Add_mid(src, startpos, pos-1);
				bfr.Add(cvtbytes);
				startpos = pos;
			}
		}
		if (startpos > 0) {
			bfr.Add_mid(src, startpos, src_len);
			return bfr.To_bry();
		}
		else
			return src;
	}
	private static byte[]
	  Bry_plus = Bry_.new_a7("+")
	, Bry_qmark = Bry_.new_a7("%3F")
	, Bry_openr = Bry_.new_a7("%28")
	, Bry_closer = Bry_.new_a7("%29")
	, Bry_comma = Bry_.new_a7("%2C")
	, Bry_slash = Bry_.new_a7("%2F")
	, Bry_hash = Bry_.new_a7("%23")
	, Bry_quote = Bry_.new_a7(("%27"))
	, Bry_colon = Bry_.new_a7(("%3A"))
	, Bry_opens = Bry_.new_a7("%5B")
	, Bry_closes = Bry_.new_a7("%5D")
	, Bry_pipe = Bry_.new_a7("%7C")
	, Bry_openq = Bry_.new_a7("%7B")
	, Bry_closeq = Bry_.new_a7("%7D")
	, Bry_star = Bry_.new_a7("%2A")
	, Bry_opena = Bry_.new_a7("%3C")
	, Bry_closea = Bry_.new_a7("%3E")
	;

	private String Buildone(Json_nde nde) {
		String type = nde.Get_as_str("type");
		String geotxt = "";
		if (type.equals("ExternalData")) {
			String service = nde.Get_as_str("service");
			geotxt = "{\"type\":\"ExternalData\",\"service\":\"" + service + "\",\"url\":\"";
			if (service.equals("geoline") || service.equals("geoshape") || service.equals("geomask")) {
				if (service.equals("geomask"))
					service = "geoshape";
				geotxt += "https://maps.wikimedia.org/" + service + "?getgeojson=1&";
				Json_kv kv = nde.Get_kv("ids");
				if (kv != null) {
					Json_itm itm = kv.Val();
					String ids = "";
					switch (itm.Tid()) {
						case Json_itm_.Tid__str:
							ids = ((Json_itm_str)itm).Data_as_str();
							// shoud strip any space!!!
							break;
						case Json_itm_.Tid__ary:
							Json_ary ids_ary = Json_ary.cast(itm);
							int alen = ids_ary.Len();
							for (int j = 0; j < alen; j++) {
								if (j > 0)
									ids += ",";
								ids += ((Json_itm_str)ids_ary.Get_at(j)).Data_as_str();
							}
							break;
						default:
							Xoa_app_.Usr_dlg().Log_many("", "", "karto: bad qids page=~{0}", ttl.Full_db());
					}
					geotxt += "ids=" + ids;
				}
				kv = nde.Get_kv("query");
				if (kv != null) {
					Json_itm itm = kv.Val();
					geotxt += "query=" + String_.new_u8(Encode(((Json_itm_str)itm).Data_bry()));
				}
				geotxt += "\"" + Genproperties(nde, service) + "}";
			}
		}
		else if (type.equals("Feature")) {
			geotxt = Buildfeature(nde);
		}
		else if (type.equals("FeatureCollection")) {
			Json_ary features = nde.Get_as_ary("features");
			int flen = features.Len();
			geotxt = "{\"type\":\"FeatureCollection\",\"features\":[";
			for (int i = 0; i < flen; i++) {
				if (i > 0)
					geotxt += ",";
				Json_nde fnde =  features.Get_as_nde(i);
				geotxt += Buildone(fnde); // recurse!
			}
			geotxt += "]}";
		}
		else {
			Xoa_app_.Usr_dlg().Log_many("", "", "karto: bad type page=~{0}", ttl.Full_db());
		}
		return geotxt;
	}
	private Hash_algo sha1_hash = Hash_algo_.New__sha1();
	public String Buildkey(byte[] src) {
		String txtgeo = "[";
		Json_parser jdoc_parser = new Json_parser();
		Json_doc jdoc = jdoc_parser.Parse(src);
		if (jdoc == null) {
			Xoa_app_.Usr_dlg().Log_many("", "", "karto: bad parse page=~{0}", ttl.Full_db());
			return "";
		}
		Json_ary ary = jdoc.Root_ary();
		if (ary != null) {
			int len = ary.Len();
			for (int i = 0; i < len; i++) {
				if (i > 0)
					txtgeo += ",";
				Json_nde nde = ary.Get_at_as_nde(i);
				txtgeo += Buildone(nde);
			}
		}
		else {
			txtgeo += Buildone(jdoc.Root_nde());
		}
		txtgeo += "]";
		return txtgeo;
	}
}
class Karto_map {
	private byte[] key;
	private byte[] data;
	private byte[] norm;
	private byte[] sha1;
	public Karto_map(byte[] key, byte[] data, byte[] norm, byte[] sha1) {
		this.key = key;
		this.data = data;
		this.norm = norm;
		this.sha1 = sha1;
	}
	public byte[] Key() { return key; }
	public byte[] Data() { return data; }
	public byte[] Norm() { return norm; }
	public byte[] Sha1() { return sha1; }
}
