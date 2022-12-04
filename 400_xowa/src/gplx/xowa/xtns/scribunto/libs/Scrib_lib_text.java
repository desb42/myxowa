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
package gplx.xowa.xtns.scribunto.libs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
import gplx.core.bits.*; import gplx.core.btries.*;
import gplx.xowa.langs.msgs.*;
import gplx.xowa.xtns.scribunto.procs.*;
import gplx.objects.strings.unicodes.Ustring;
import gplx.objects.strings.unicodes.Ustring_;
import gplx.xowa.xtns.scribunto.libs.patterns.Scrib_pattern_matcher;
public class Scrib_lib_text implements Scrib_lib {
	private final	Scrib_lib_text__json_util json_util = new Scrib_lib_text__json_util();
	private final	Scrib_lib_text__nowiki_util nowiki_util = new Scrib_lib_text__nowiki_util();
	private final	Scrib_core core;
	private final	Btrie_slim_mgr trie;
	public Scrib_lib_text(Scrib_core core) {
		this.core = core;
		this.trie = nowiki_util.Make_trie(gplx.xowa.parsers.xndes.Xop_xnde_tag_.Tag__nowiki.Name_bry());
	}
	public String Key() {return "mw.text";}
	public Scrib_lua_mod Mod() {return mod;} private Scrib_lua_mod mod;
	public Scrib_lib Init() {procs.Init_by_lib(this, Proc_names); return this;}
	public Scrib_lib Clone_lib(Scrib_core core) {return new Scrib_lib_text(core);}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		//mod = core.RegisterInterface(this, "mw.text.lua", core.Core_mgr().Get_text(script_dir, "mw.text.lua"));
		mod = core.RegisterInterface(this, "mw.text.lua", core.Fsys_mgr().Get_or_null("mw.text"));
		//mod = core.RegisterInterface(this, script_dir.GenSubFil("mw.text.lua"));
		notify_wiki_changed_fnc = mod.Fncs_get_by_key("notify_wiki_changed");
		return mod;
	}	private Scrib_lua_proc notify_wiki_changed_fnc;
	public Scrib_proc_mgr Procs() {return procs;} private final	Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc_unstrip:							return Unstrip(args, rslt);
			case Proc_unstripNoWiki:					return UnstripNoWiki(args, rslt);
			case Proc_killMarkers:						return KillMarkers(args, rslt);
			case Proc_getEntityTable:					return GetEntityTable(args, rslt);
			case Proc_init_text_for_wiki:				return Init_text_for_wiki(args, rslt);
			case Proc_jsonEncode:						return JsonEncode(args, rslt);
			case Proc_jsonDecode:						return JsonDecode(args, rslt);
			case Proc_nowiki:						return Nowiki(args, rslt);
			case Proc_split:						return Split(args, rslt);
			default: throw Err_.new_unhandled(key);
		}
	}
	private static final int Proc_unstrip = 0, Proc_unstripNoWiki = 1, Proc_killMarkers = 2, Proc_getEntityTable = 3, Proc_init_text_for_wiki = 4, Proc_jsonEncode = 5, Proc_jsonDecode = 6, Proc_nowiki = 7, Proc_split = 8;
	public static final String
	  Invk_unstrip = "unstrip"
	, Invk_unstripNoWiki = "unstripNoWiki"
	, Invk_killMarkers = "killMarkers"
	, Invk_getEntityTable = "getEntityTable"
	, Invk_init_text_for_wiki = "init_text_for_wiki"
	, Invk_jsonEncode = "jsonEncode"
	, Invk_jsonDecode = "jsonDecode"
	, Invk_nowiki = "nowiki"
	, Invk_split = "split"
	;
	private static final String[] Proc_names = String_.Ary(
	  Invk_unstrip
	, Invk_unstripNoWiki
	, Invk_killMarkers
	, Invk_getEntityTable
	, Invk_init_text_for_wiki
	, Invk_jsonEncode
	, Invk_jsonDecode
	, Invk_nowiki
	, Invk_split
	);
	public boolean Unstrip(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		// NOTE: https://www.mediawiki.org/wiki/Extension:Scribunto/Lua_reference_manual#mw.text.unstrip
		byte[] src = args.Pull_bry(0);
		return rslt.Init_obj(core.Ctx().Wiki().Parser_mgr().Uniq_mgr().Parse(false, src));
	}
	public boolean UnstripNoWiki(Scrib_proc_args args, Scrib_proc_rslt rslt)	{
		// NOTE: https://www.mediawiki.org/wiki/Extension:Scribunto/Lua_reference_manual#mw.text.unstripNoWiki
		byte[] src = args.Pull_bry(0);
		// BIG HACK 
		// strip <nowiki> tags
		int len = src.length;
		int pos = 0;
		Bry_bfr tmp_bfr = Bry_bfr_.New();
		int start = 0;
		while (pos < len) {
			byte b = src[pos++];
			if (b == '<') {
				if (pos + 6 < len
						&& src[pos]   == 'n'
						&& src[pos+1] == 'o'
						&& src[pos+2] == 'w'
						&& src[pos+3] == 'i'
						&& src[pos+4] == 'k'
						&& src[pos+5] == 'i' 
						&& src[pos+6] == '>') {
					int xpos = pos + 7;
					while (xpos < len) {
						b = src[xpos++];
						if (b == '<') { // </nowiki>
							if (xpos + 7 < len
									&& src[xpos]   == '/'
									&& src[xpos+1] == 'n'
									&& src[xpos+2] == 'o'
									&& src[xpos+3] == 'w'
									&& src[xpos+4] == 'i'
									&& src[xpos+5] == 'k'
									&& src[xpos+6] == 'i'
									&& src[xpos+7] == '>') {
								tmp_bfr.Add_mid(src, start, pos - 1);
								tmp_bfr.Add_mid(src, pos + 7, xpos - 1);
								pos = xpos + 8;
								start = pos;
								xpos = len; // break out of inner loop
							}
						}
					}
				}
			}
		}
		if (start > 0) {
			tmp_bfr.Add_mid(src, start, len);
			src = tmp_bfr.To_bry();
		}

		return rslt.Init_obj(core.Ctx().Wiki().Parser_mgr().Uniq_mgr().Partial_Parse(src, true));
//		byte[] src = args.Pull_bry(0);
//		return rslt.Init_obj(nowiki_util.Strip_tag(core.Page().Url_bry_safe(), src, trie));
	}
	public boolean KillMarkers(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		// NOTE: https://www.mediawiki.org/wiki/Extension:Scribunto/Lua_reference_manual#mw.text.KillMarkers
		byte[] src = args.Pull_bry(0);
		return rslt.Init_obj(core.Ctx().Wiki().Parser_mgr().Uniq_mgr().Partial_Parse(src, false));
	}
	public boolean GetEntityTable(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		if (html_entities == null) html_entities = Scrib_lib_text_html_entities.new_();
		return rslt.Init_obj(html_entities);
	}
	private static Keyval[] html_entities;
	public boolean JsonEncode(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Object itm = args.Pull_obj(0);

		// check if json is primitive, and return that; see NOTE below; PAGE:en.w:Template:Format_TemplateData ISSUE#:301; DATE:2019-01-13
		int itm_type_id = Type_ids_.To_id_by_obj(itm);
		switch (itm_type_id) {
			case Type_ids_.Id__bool:
			case Type_ids_.Id__byte:
			case Type_ids_.Id__short:
			case Type_ids_.Id__int:
			case Type_ids_.Id__long:
			case Type_ids_.Id__float:
			case Type_ids_.Id__double:
			case Type_ids_.Id__char:
			case Type_ids_.Id__str:
			case Type_ids_.Id__bry:
			case Type_ids_.Id__date:
			case Type_ids_.Id__decimal:
				return rslt.Init_obj(itm);
		}

		// try to determine if node or array; EX: {a:1, b:2} vs [a:1, b:2]
		Keyval[] itm_as_nde = null;
		Object itm_as_ary = null;	
		Class<?> itm_type = itm.getClass();
		boolean itm_is_nde = Type_.Eq(itm_type, Keyval[].class);

		// additional logic to classify "[]" as ary, not nde; note that this is done by checking len of itm_as_nde
		if (itm_is_nde) {
			itm_as_nde = (Keyval[])itm;
			int itm_as_nde_len = itm_as_nde.length;
			if (itm_as_nde_len == 0) {	// Keyval[0] could be either "[]" or "{}"; for now, classify as "[]" per TextLibraryTests.lua; 'json encode, empty table (could be either [] or {}, but change should be announced)'; DATE:2016-08-01
				itm_as_nde = null;
				itm_is_nde = false;
			}					
		}
		if	(!itm_is_nde)
			itm_as_ary = Array_.cast(itm);

		// reindex ndes unless preserve_keys
		int flags = args.Cast_int_or(1, 0);
		if (	itm_is_nde
			&&	!Bitmask_.Has_int(flags, Scrib_lib_text__json_util.Flag__preserve_keys)
			) {
			Scrib_lib_text__reindex_data reindex_data = new Scrib_lib_text__reindex_data();
			json_util.Reindex_arrays(reindex_data, itm_as_nde, true);
			if (reindex_data.Rv_is_kvy()) {
				itm_as_nde = reindex_data.Rv_as_kvy();
				itm_as_ary = null;
			}
			else {
				itm_as_ary = reindex_data.Rv_as_ary();
				itm_as_nde = null;
				itm_is_nde = false;
			}
		}

		// encode and return 
		byte[] rv = itm_is_nde
			? json_util.Encode_as_nde(itm_as_nde, flags & Scrib_lib_text__json_util.Flag__pretty, Scrib_lib_text__json_util.Skip__all)
			: json_util.Encode_as_ary(itm_as_ary, flags & Scrib_lib_text__json_util.Flag__pretty, Scrib_lib_text__json_util.Skip__all)
			;
		if (rv == null)
			throw Err_.new_("scribunto",  "mw.text.jsonEncode: Unable to encode value");
		return rslt.Init_obj(rv);
	}
	public boolean JsonDecode(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		// init
		byte[] json = args.Pull_bry(0);

		// check if json is primitive, and return that; see NOTE below; PAGE:en.w:Template:Format_TemplateData ISSUE#:301; DATE:2019-01-13
		int json_len = json.length;
		boolean is_json_like = false;
		boolean is_numeric = true;
		for (int i = 0; i < json_len; i++) {
			byte json_byte = json[i];
			switch (json_byte) {
				case Byte_ascii.Brack_bgn:
				case Byte_ascii.Brack_end:
				case Byte_ascii.Curly_bgn:
				case Byte_ascii.Curly_end:
					is_json_like = true;
					is_numeric = false;
					i = json_len;
					break;
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					break;
				default:
					is_numeric = false;
					break;
			}
		}
		if (!is_json_like) {
			if (is_numeric) {
				return rslt.Init_obj(Bry_.To_int(json));
			}
			else {
				if (Bry_.Eq(json, Bool_.True_bry))
					return rslt.Init_obj(true);
				else if (Bry_.Eq(json, Bool_.False_bry))
					return rslt.Init_obj(false);
				else {
					return rslt.Init_obj(json);
				}
			}
		}			

		int flags = args.Cast_int_or(1, 0);
		int opts = Scrib_lib_text__json_util.Opt__force_assoc;
		if (Bitmask_.Has_int(flags, Scrib_lib_text__json_util.Flag__try_fixing))
			opts = Bitmask_.Add_int(opts, Scrib_lib_text__json_util.Flag__try_fixing);

		Keyval[] rv = JsonDecodeStatic(args, core, json_util, json, opts, flags);
		return rslt.Init_obj(rv);
	}
	public static Keyval[] JsonDecodeStatic
		( Scrib_proc_args args, Scrib_core core, Scrib_lib_text__json_util json_util
		, byte[] json, int opts, int flags) {
		// decode json to Object; note that Bool_.Y means nde and Bool_.N means ary
		byte rv_tid = json_util.Decode(json, opts);
		if (rv_tid == Bool_.__byte) throw Err_.new_("scribunto",  "mw.text.jsonEncode: Unable to decode String " + String_.new_u8(json));
		if (rv_tid == Bool_.Y_byte) {
			Keyval[] rv_as_kvy = (Keyval[])json_util.Decode_rslt_as_nde();

			// reindex unless preserve_keys passed
			if (!(Bitmask_.Has_int(flags, Scrib_lib_text__json_util.Flag__preserve_keys))) {
				Scrib_lib_text__reindex_data reindex_data = new Scrib_lib_text__reindex_data();
				json_util.Reindex_arrays(reindex_data, rv_as_kvy, false);
				rv_as_kvy = reindex_data.Rv_is_kvy() ? (Keyval[])reindex_data.Rv_as_kvy() : (Keyval[])reindex_data.Rv_as_ary();
			}					
			return rv_as_kvy;
		}
		else
			return json_util.Decode_rslt_as_ary();
	}

	public void Notify_wiki_changed() {if (notify_wiki_changed_fnc != null) core.Interpreter().CallFunction(notify_wiki_changed_fnc.Id(), Keyval_.Ary_empty);}
	public boolean Init_text_for_wiki(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xow_msg_mgr msg_mgr = core.Wiki().Msg_mgr();
		Keyval[] rv = new Keyval[4];
		rv[0] = Keyval_.new_("comma", Init_lib_text_get_msg(msg_mgr, "comma-separator"));
		rv[1] = Keyval_.new_("and", Init_lib_text_get_msg(msg_mgr, "and") + Init_lib_text_get_msg(msg_mgr, "word-separator"));
		rv[2] = Keyval_.new_("ellipsis", Init_lib_text_get_msg(msg_mgr, "ellipsis"));
		rv[3] = Keyval_.new_("nowiki_protocols", Keyval_.Ary_empty);	// NOTE: code implemented, but waiting for it to be used; DATE:2014-03-20
		return rslt.Init_obj(rv);
	}
	private String Init_lib_text_get_msg(Xow_msg_mgr msg_mgr, String msg_key) {
		return String_.new_u8(msg_mgr.Val_by_key_obj(Bry_.new_u8(msg_key)));
	}

	public boolean Nowiki(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] src = args.Pull_bry(0);
		int len = src.length;
		int pos = 0;
		Bry_bfr bfr = Bry_bfr_.New();
		int startpos = 0;
		boolean linebreakfound = false;
		byte b, c;
		while (pos < len) {
			b = src[pos++];
			switch (b) {
			case '"':
			case '&':
			case '\\':
			case '\'':
			case '<':
			case '=':
			case '>':
			case '[':
			case ']':
			case '{':
			case '|':
			case '}':
				bfr.Add_mid(src, startpos, pos - 1);
				bfr.Add(amp_hash).Add_int_variable(b).Add_byte(Byte_ascii.Semic);
				startpos = pos;
				break;
	
			case '#':
			case '*':
			case ';':
			case ' ':
				if (!linebreakfound) {
					if (pos == 1 || (c = src[pos-2]) == '\r' || c == '\n') {
						bfr.Add_mid(src, startpos, pos - 1);
						bfr.Add(amp_hash).Add_int_variable(b).Add_byte(Byte_ascii.Semic);
						startpos = pos;
					}
				}
				break;
	
			case ':':  // ^n: or ://
				if (pos + 2 < len && src[pos] == '/' && src[pos+1] == '/') {
					bfr.Add_mid(src, startpos, pos - 1);
					bfr.Add(apm58slashslash);
					pos += 2;
					startpos = pos;
				}
				else {
					if (!linebreakfound) {
						if (pos == 1 || (c = src[pos-2]) == '\r' || c == '\n') {
							bfr.Add_mid(src, startpos, pos - 1);
							bfr.Add(amp_hash).Add_int_variable(b).Add_byte(Byte_ascii.Semic);
							startpos = pos;
						}
					}
				}
				break;
	
			case '\n':
			case '\r':
			case '\t':
				if (!linebreakfound) {
					c = '\n';
					if (pos == 1 || (c = src[pos-2]) == '\r' || c == '\n') {
						if (pos > 1)
							bfr.Add_mid(src, startpos, pos - 2);
						if (c == '\r' && b == '\n')
							bfr.Add(slashRslashN); // "&#13;\n"
						else
							bfr.Add_byte(c).Add(amp_hash).Add_int_variable(b).Add_byte(Byte_ascii.Semic);
						startpos = pos;
						linebreakfound = true;
						continue;
					}
				}
				break;
	
			case '-': //%-%-%-%-
				c = '\n';
				if (pos == 1 || (c = src[pos-2]) == '\r' || c == '\n') {
					if (pos + 3 < len && src[pos] == '-' && src[pos + 1] == '-' && src[pos + 2] == '-') {
						bfr.Add_mid(src, startpos, pos - 1);
						bfr.Add(amp45hyphens); // "%1&#45;---"
						pos += 3;
						startpos = pos;
					}
				}
				break;
			case '_': //__
				if (pos + 1 < len && src[pos] == '_') {
					bfr.Add_mid(src, startpos, pos - 1);
					bfr.Add(underscoreamp95); // "_&#95;"
					pos += 1;
					startpos = pos;
				}
				break;
			case 'I': //ISBN%s
				if (pos + 4 < len && src[pos] == 'S' && src[pos+1] == 'B' && src[pos+2] == 'N') {
					if ((c = src[pos+3]) == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f') {
						bfr.Add_mid(src, startpos, pos - 1);
						bfr.Add(ISBN).Add(amp_hash).Add_int_variable(c).Add_byte(Byte_ascii.Semic);
						pos += 4;
						startpos = pos;
					}
				}
				break;
			case 'R': //RFC%s
				if (pos + 3 < len && src[pos] == 'F' && src[pos+1] == 'C') {
					if ((c = src[pos+2]) == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f') {
						bfr.Add_mid(src, startpos, pos - 1);
						bfr.Add(RFC).Add(amp_hash).Add_int_variable(c).Add_byte(Byte_ascii.Semic);
						pos += 3;
						startpos = pos;
					}
				}
				break;
			case 'P': //PMID%s
				if (pos + 4 < len && src[pos] == 'M' && src[pos+1] == 'I' && src[pos+2] == 'D') {
					if ((c = src[pos+3]) == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f') {
						bfr.Add_mid(src, startpos, pos - 1);
						bfr.Add(PMID).Add(amp_hash).Add_int_variable(c).Add_byte(Byte_ascii.Semic);
						pos += 4;
						startpos = pos;
					}
				}
				break;
			}
			linebreakfound = false;
		}
		if (startpos > 0) {
			bfr.Add_mid(src, startpos, len);
			return rslt.Init_obj(bfr.To_str());
		}
		return rslt.Init_obj(args.Pull_str(0));
	}
	private static byte[]
			amp_hash = Bry_.new_a7("&#")
			, PMID = Bry_.new_a7("PMID")
			, RFC = Bry_.new_a7("RFC")
			, ISBN = Bry_.new_a7("ISBN")
			, underscoreamp95 = Bry_.new_a7("_&#95;")
			, amp45hyphens = Bry_.new_a7("&#45;---")
			, slashRslashN = Bry_.new_a7("&#13;\n")
			, apm58slashslash = Bry_.new_a7("&#58;//")
			;

	public boolean Split(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		try {
		// get args
		String text_str        = args.Xstr_str_or_null(0);
		String find_str        = args.Pull_str(1);
		boolean plain          = args.Cast_bool_or_n(2);

		// init text vars
		Ustring text_ucs = Ustring_.New_codepoints(text_str); // NOTE: must count codes for supplementaries; PAGE:en.d:iglesia DATE:2017-04-23

		List_adp list;
		int text_len = text_ucs.Len_in_data();
		if (find_str.length() == 0) { // break into single characters
			list = List_adp_.New();
			for (int i = 0; i < text_len; i++)
				list.Add(text_ucs.Substring(i, i + 1));
		}
		// if plain, just do literal match of find and exit
		else if (plain) {
			list = List_adp_.New();
			// find pos by literal match
			Ustring find_ucs = Ustring_.New_codepoints(find_str);
			int start = 0;
			int pos = 0;
			while (pos < text_len) {
				pos = text_ucs.Index_of(find_ucs, pos);

				// if nothing (else) found, return
				if (pos == String_.Find_none)
					break;
				list.Add(text_ucs.Substring(start, pos));
				pos += find_str.length();
				start = pos;
			}
			list.Add(text_ucs.Substring(start, text_len));
		}
		else {
			// run regex;
			Scrib_pattern_matcher matcher = Scrib_pattern_matcher.New(core.Page_url());
			list = matcher.Split(text_ucs, find_str);
		}
		int list_len = list.Len();
		Keyval[] rv = new Keyval[list_len];
		for (int i = 0; i < list_len; i++)
			rv[i] = Keyval_.int_(i + 1, (String)list.Get_at(i));
		return rslt.Init_obj(rv);
		}
		catch (Exception e) {
			int a=1;
		}
		return rslt.Init_null();
	}
}
/*
jsonDecode

NOTE: this code is adhoc; MW calls PHP's jsonDecode
jsonDecode has very liberal rules for decoding which seems to include
* auto-converting bools and ints from strings
* throwing syntax errors if text looks like JSON but is not

This code emulates some of the above rules

REF: http://php.net/manual/en/function.json-decode.php
REF: https://doc.wikimedia.org/mediawiki-core/master/php/FormatJson_8php_source.html
*/
