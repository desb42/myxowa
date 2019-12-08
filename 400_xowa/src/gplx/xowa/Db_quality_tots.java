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
package gplx.xowa; import gplx.*;
import gplx.dbs.*;
import gplx.xowa.parsers.tmpls.*;
import gplx.xowa.wikis.data.Xow_db_mgr;
import gplx.xowa.wikis.data.tbls.Xowd_page_tbl;
public class Db_quality_tots {
	private int[] qualitycount = new int[6]; // 5 quality levels plus unknown
	private int qualitytot;
	public Db_quality_tots() {
		this.qualitytot = 0;
	}
	public void Clear() {
		for (int i = 0; i < 6; i++)
			qualitycount[i] = 0;
		qualitytot = 0;
	}
	public void Increment(int slot) {
		if (slot >= 0)
			qualitycount[slot]++;
		else
			qualitycount[5]++;

		qualitytot++;
	}
	public int[] Qualitycounts() { return qualitycount; }
	public int Qualitycount() { return qualitytot; }

	public byte[] Serialise() {
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		tmp_bfr
			 .Add_int_variable(qualitycount[0])
			 .Add_byte(Byte_ascii.Bang)
			 .Add_int_variable(qualitycount[1])
			 .Add_byte(Byte_ascii.Bang)
			 .Add_int_variable(qualitycount[2])
			 .Add_byte(Byte_ascii.Bang)
			 .Add_int_variable(qualitycount[3])
			 .Add_byte(Byte_ascii.Bang)
			 .Add_int_variable(qualitycount[4])
			 .Add_byte(Byte_ascii.Bang)
			 .Add_int_variable(qualitycount[5])
			 ;
		return tmp_bfr.To_bry();
	}
	public void Deserialise(byte[] serial) {
		this.qualitytot = 0;
                if (serial == null) return;
		int len = serial.length;
		int pos = 0, startpos;
		for (int i = 0; i < 5; i++) {
			startpos = pos;
			while (pos < len) {
				byte b = serial[pos++];
				if (b == Byte_ascii.Bang) {
					qualitycount[i] = makeint(serial, startpos, pos - 1);
					qualitytot += qualitycount[i];
					break;
				}
			}
		}
		qualitycount[5] = makeint(serial, pos, len);
		qualitytot += qualitycount[5];
	}
	private int makeint(byte[] b, int bgn, int end) {
		int result = 0;
		for (int i = bgn; i < end; i++) {
			result *= 10;
			result += b[i] - '0';
		}
		return result;
	}

	public static byte[] Generate_quality(Xowe_wiki wiki, Xoae_page page) {
		int qualitytot = page.Quality_tots().Qualitycount();
		if (qualitytot == 0) return null;
		int[] qualitycount = page.Quality_tots().Qualitycounts();
		Bry_bfr bfr = Bry_bfr_.New();
		int q0 = qualitycount[0] * 100 / qualitytot;
		int q1 = qualitycount[1] * 100 / qualitytot;
		int q2 = qualitycount[2] * 100 / qualitytot;
		int q3 = qualitycount[3] * 100 / qualitytot;
		int q4 = qualitycount[4] * 100 / qualitytot;
		int qe = qualitycount[5] * 100 / qualitytot;
		byte[] textualAlternative = wiki.Msg_mgr().Val_by_key_args(Bry_.new_a7("proofreadpage-indexquality-alt"), qualitycount[4], qualitycount[3], qualitycount[1] );
		bfr.Add_str_a7("<table class=\"pr_quality noprint\" title=\"").Add(textualAlternative);
		bfr.Add_str_a7("\">\n<tr>\n<td class=\"quality4\" style=\"width:").Add_long_variable(q4);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality3\" style=\"width:").Add_long_variable(q3);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality2\" style=\"width:").Add_long_variable(q2);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality1\" style=\"width:").Add_long_variable(q1);
		bfr.Add_str_a7("%;\"></td>\n<td class=\"quality0\" style=\"width:").Add_long_variable(q0);
		bfr.Add_str_a7("%;\"></td>\n");
		if (qe > 0)
			bfr.Add_str_a7("<td class=\"qualitye\" style=\"width:").Add_long_variable(qe).Add_str_a7("%;\"></td>\n");
		bfr.Add_str_a7("</tr>\n</table>\n");
		return bfr.To_bry();
	}
}
