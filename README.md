## Overview
XOWA is an offline Wikipedia application that lets you run Wikipedia on your computer.

## Myxowa
This version includes all changes up to gnosygnu/xowa/commit/5db81504fb418be6a07f96eee64244127afa96fe
and includes changes in the source code for:

* Some changes are just extra lines to add breakpoints more easily
* Some changes are changing single quotes to double quotes (for html generation)
* reseting the Len_ in Bry_bfr (used to convert Image:->File:)
* a correction to Find_bwd__skip_ws (used to remove trailing whitespace in templates)
* Int_ary_.java number parser allows .2c to represent a comma (used in wikisource)
* Bry_diff ignore whitespace differences
* Creation of Pfxtp_roman to allow access to the Roman numeral handling (mainly wikisource)
* Change to Xoa_ttl to handle convertion of Image: to File:
* Xomp_prog_mgr and Xomp_load_wkr display the memory usage
* Xoh_sidebar_itm at some point in the past the class prefix has change from 'n-' to 'p-' (needed for some javascript code, wikisource)
* Xoh_toc_htmlr change the code generation to support the new(ish) hide/show content mechanism
* various changes to support counting the number of categories and pages (when displaying Category pages)
* Xoctg_collation_enum add a 'numeric' collation
* Hide the 'Hidden categories' in the frwiki
* Srch_search_cmd check that an object is not null
* Added some logic to tell when a startup .gfs file does not exist (currently xowa fails silently)
* Change copywrite year (on startup)
* Http_server_mgr various changes to support popup mode and search suggest popup (search popup does not seem to be working at the moment)
* Http_server_wrk various changes to support some of my experimentations 
* Http_url_parser make sure that the string returned still has the querystring - because it may be used later on (by another url parser!)
* Xof_xfer_itm - some experimenation in using svg files directly (not fully working)
* Various changes to support synchronous redlink processing (and colour processing for wikisource)
* Changes to support syntax highlighting
* Some test css minification code
* Adding graph to top level to trigger extra javascript (this has been superceeded by other mechanisms)
* Moved footer (including categories and footer) to its own section (necessitated by wikisource changes)
* Some experimental code to look at removing extra space around \<h?> headers, \<li>, \<td> and \<th>
* Thumbnail handling has a few extra +2 sprinkled about
* "Expected newline before caption" is not a fatal error
* Add logic to support gallery sideshow
* Add logic to support extra web fonts (part of ULS)
* Support graph v1 and v2 on the same page
* Always include jquery.js
* Xow_head_wtr when writing a quoted string try to guess the best quote (double if a single found else single)
* Xow_portal_mgr change the navigation links depeding on namespace (used by non enwiki javascript)
* Add alt-shift to the popup strings (does not take other OS's into consideration)
* sychronisation around fetchLanguageNames
* allow for chem within mathjax
* Mwh_atr_parser change handling of dangling quotes in attributes of an element
* Allow for vertical alignment in thumbnails
* Xop_lnki_arg_parser bounds check
* Allow for thumb alternate (thumb=... in thumbnail)
* Xow_page_tid hack way to determine if Module namespace is lua or wikitext (for syntac highlight)
* When a page is updated the length is also updated (Xowd_page_tbl and Xowd_save_mgr_sql)
* Ref_html_wrt add an extra class if number of refs greater than 10
* Use wiki specific citation
* Implement mode in dynamiclist
* Gallery_parser hack to change File:File to File:
* Changes to imap processing
* An implementation of \<inputbox>
* Pft_func_formatdate expose date formater so this can be used in displaying the modifyed date on page
* proofread pages considerable changes to Index, Page and implentation of \<pagelist> \<pages> and \<pagequality>
* Scribe_invoke_func return error messages more in line with mediawiki
* A lot of changes to wikidata to add Lexeme and Sense (still ongoing)

there are also changes to the bin and user directories

'bin' major changes to xowa.gfs a reworking of en.gfs, de.gfs, it.gfs
lots of javascript rearrangement
introduced the concept of a wiki specific javascript (eg en.wikisource.org.js)

'user' contains different versions of xowa_common and xowa_wiki for the wikis I have worked with (NOT the version stored within the core database file)

## License
XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

See LICENSE.txt for more information.
