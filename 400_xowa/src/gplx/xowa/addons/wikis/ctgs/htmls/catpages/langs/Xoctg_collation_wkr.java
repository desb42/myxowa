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
package gplx.xowa.addons.wikis.ctgs.htmls.catpages.langs;
import gplx.*; import gplx.xowa.*;
import gplx.core.intls.Utf8_;
import gplx.xowa.addons.wikis.ctgs.enums.Xoctg_collation_enum;
import gplx.core.intls.ucas.Uca_ltr_extractor;
public interface Xoctg_collation_wkr {
	String Type_name();
	String Wm_name();
	byte[] Get_sortkey(byte[] src);
	byte[] Get_firstchar(byte[] key, byte[] handle);
	int Type_id();
}
class Xoctg_collation_wkr__uppercase implements Xoctg_collation_wkr {
	private final    Xow_wiki wiki;
	public Xoctg_collation_wkr__uppercase(Xow_wiki wiki) {this.wiki = wiki;}
	@Override public String Type_name() {return "uppercase";}
	@Override public int Type_id() {return Xoctg_collation_enum.Tid__uppercase;}
	@Override public String Wm_name() {return this.Type_name();}
	@Override public byte[] Get_sortkey(byte[] src) {
		return wiki.Lang().Case_mgr().Case_build_upper(src);
	}
	@Override public byte[] Get_firstchar(byte[] key, byte[] handle) {
		// get the first char from src utf8
		byte[] rv = null;
		if (key.length > 0) {
			int b_len = Utf8_.Len_of_char_by_1st_byte(key[0]);
			rv = new byte[b_len];
			for (int i = 0; i < b_len; i++)
				rv[i] = key[i];
		}
		return rv;
	}
}
class Xoctg_collation_wkr__identity implements Xoctg_collation_wkr {
	@Override public String Type_name() {return "identity";}
	@Override public String Wm_name() {return this.Type_name();}
	@Override public int Type_id() {return Xoctg_collation_enum.Tid__identity;}
	@Override public byte[] Get_sortkey(byte[] src) {
		return src;
	}
	@Override public byte[] Get_firstchar(byte[] key, byte[] handle) {
		// get the first char from key utf8
		byte[] rv = null;
		if (key.length > 0) {
			int b_len = Utf8_.Len_of_char_by_1st_byte(key[0]);
			rv = new byte[b_len];
			for (int i = 0; i < b_len; i++)
				rv[i] = key[i];
		}
		return rv;
	}
}
class Xoctg_collation_wkr__uca implements Xoctg_collation_wkr {
	private gplx.core.intls.ucas.Uca_collator collator;
	private Uca_ltr_extractor ltr_extractor;
	public Xoctg_collation_wkr__uca(String wm_name, String icu_locale) {
		// REF:"includes/collation/Collation.php|factory" "includes/collation/IcuCollation.php|__construct"
		this.wm_name = wm_name;
		// remove anything after "@"; EX: 'svwikisource' => 'uca-sv@collation=standard', // T48058
		int at_pos = String_.FindFwd(icu_locale, "@");
		if (at_pos != String_.Find_none)
			icu_locale = String_.Mid(icu_locale, 0, at_pos);

		// handle "default-u-kn"
		if (String_.Eq(icu_locale, "default-u-kn"))
			this.icu_locale = "en";
		else if (String_.Eq(icu_locale, "root"))
			this.icu_locale = "en";
		else
			this.icu_locale = icu_locale;
		this.numeric_sorting = String_.Has_at_end(icu_locale, "-u-kn");
	}
	@Override public String Type_name() {return wm_name;}	private final    String wm_name;
	@Override public int Type_id() {return Xoctg_collation_enum.Tid__uca;}
	@Override public String Wm_name() {return this.Type_name();}
	public String Icu_locale() {return icu_locale;} private final    String icu_locale;
	public boolean Numeric_sorting() {return numeric_sorting;} private final    boolean numeric_sorting;
	@Override public byte[] Get_sortkey(byte[] src) {
		if (collator == null) {
			collator = gplx.core.intls.ucas.Uca_collator_.New(icu_locale, numeric_sorting);
		}
		return collator.Get_sortkey(String_.new_u8(src));
	}
	@Override public byte[] Get_firstchar(byte[] key, byte[] handle) {
		if (ltr_extractor == null) {
			ltr_extractor = new Uca_ltr_extractor(numeric_sorting);
		}
		byte[] ltr_head = ltr_extractor.Get_1st_ltr(handle);
		ltr_head = DB_case_mgr.Case_build_1st(true, ltr_head);
		return ltr_head;
	}
/*
            // decode the first collation entity!! - TOO HARD
            byte[] rv = null;
            if (src.length > 0) {
                byte b = src[0];
                if (b >= 42 && b <= 94) {
                    rv = new byte[1];
                    rv[0] = (byte)((b - 42)/2 + 'A');
                    return rv;
                }
                else if (b == 5) {
                    rv = new byte[1];
                    rv[0] = '-';
                    return rv;
                }
                else if (b == 8) {
                    rv = new byte[1];
                    rv[0] = '.';
                    return rv;
                }
                else if (b == 4) {
                    rv = new byte[1];
                    rv[0] = ' ';
                    return rv;
                }
                else if (b == 9) {
                    rv = new byte[1];
                    switch (src[1]) {
                        case 106: rv[0] = '\''; break;
                        case -110: rv[0] = '['; break;
                        case -112: rv[0] = ')'; break;
                        case -114: rv[0] = '('; break;
                        case -118: rv[0] = '/'; break;
                        case 92: // possibly further bytes to test?
                            rv = new byte[]{(byte)0xc2, (byte)0xb7};
                            break;

                        default:
                            int a=1;
                            rv[0] = '?';
                            break;
                    }
                    return rv;
                }
                else if (b == 10) {
                    rv = new byte[1];
                    rv[0] = '#';
                    return rv;
                }
                else if (b == 13) {
                    rv = new byte[1];
                    rv[0] = '$';
                    return rv;
                }
                else if (b == 12) {
                    rv = new byte[1];
                    rv[0] = '+';
                    return rv;
                }
                else if (b == 15) {
                    rv = Bry_.new_a7("0-9"); // this should be msg key
                    return rv;
                }
                else {
                    int a=1; // eeeeeeek
                    rv = new byte[1];
                    rv[0] = 'x';
                }
            }
		return rv;
	}
*/
}
