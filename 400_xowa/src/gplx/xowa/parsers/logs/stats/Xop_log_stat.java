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
package gplx.xowa.parsers.logs.stats;
import gplx.*;
import gplx.core.brys.fmtrs.Bry_fmtr;
public class Xop_log_stat {
	//public boolean Enabled() {return enabled;} private boolean enabled;
	public int Wkr_uid() {return wkr_uid;} private int wkr_uid = -1;
	public Xop_log_time_count Scrib() {return scrib;} private final    Xop_log_time_count scrib = new Xop_log_time_count();
	public long Tidy_time;
	public int Image_count;
	public int Audio_count;
	public int Video_count;
	public int Media_count;
	public int Hdr_count;
	public int Lnki_count;
	public int Lnke_count;
	public int Math_count;
	public int Imap_count;
	public int Hiero_count;
	public int Gallery_count;
	public int Gallery_packed_count;
	public int Tmpl_count;
	public int Tmpl1_count;
	public int Tmpl2_count;
	public int Tmpl3_count;
	//public void Init(int v) {
	//	enabled = true;
	//	wkr_uid = v;
	//}
	public void Add_stats(Bry_bfr bfr) {
		counts_fmtr.Bld_bfr_many(bfr
			, Tidy_time
			, Image_count
			, Audio_count
			, Video_count
			, Media_count
			, Hdr_count
			, Lnki_count
			, Lnke_count
			, Math_count
			, Imap_count
			, Hiero_count
			, Gallery_count
			, Gallery_packed_count
			, Tmpl_count
			, Tmpl1_count
			, Tmpl2_count
			, Tmpl3_count
			, scrib.Time()
			, scrib.Count()
			, scrib.Depth_max()
			, Scrib().PageTime()
		);
	}
	private static final Bry_fmtr counts_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "<!--"
	, "Counts:"
	, " Tidy_time: ~{t1}"
	, " Image_count: ~{t2}"
	, " Audio_count: ~{t3}"
	, " Video_count: ~{t4}"
	, " Media_count: ~{t5}"
	, " Hdr_count: ~{t6}"
	, " Lnki_count: ~{t7}"
	, " Lnke_count: ~{t8}"
	, " Math_count: ~{t9}"
	, " Imap_count: ~{t10}"
	, " Hiero_count: ~{t11}"
	, " Gallery_count: ~{t12}"
	, " Gallery_packed_count: ~{t13}"
	, " Tmpl_count: ~{t14}"
	, " Tmpl1_count: ~{t15}"
	, " Tmpl2_count: ~{t16}"
	, " Tmpl3_count: ~{t17}"
	, "Scribunto:"
	, " Time: ~{s1}"
	, " Count: ~{s2}"
	, " Max_depth: ~{s3}"
	, "Total time:"
	, "  ~{p1}"
	, "-->"
	), "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11", "t12", "t13", "t14", "t15", "t16", "t17", 
	   "s1", "s2", "s3", "p1");
}
