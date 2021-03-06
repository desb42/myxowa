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
package gplx.gfui.controls.gxws; import gplx.*; import gplx.gfui.*; import gplx.gfui.controls.*;
public class GxwElemFactory_cls_mock extends GxwElemFactory_base {
	@Override public GxwElem control_() {return GxwElem_mock_base.new_();}
	@Override public GxwWin win_app_() {return MockForm.Instance;}
	@Override public GxwWin win_tool_(Keyval_hash ctorArgs)	{return MockForm.Instance;}
	@Override public GxwWin win_toaster_(Keyval_hash ctorArgs)	{return MockForm.Instance;}
	@Override public GxwElem lbl_() {return GxwElem_mock_base.new_();}
	@Override public GxwTextFld text_fld_() {return new MockTextBox();}
	@Override public GxwTextFld text_memo_() {return new MockTextBoxMulti();}
	@Override public GxwTextHtml text_html_() {return new MockTextBoxMulti();}
	@Override public GxwCheckListBox checkListBox_(Keyval_hash ctorArgs) {throw Err_.new_unimplemented(49);}
	@Override public GxwComboBox comboBox_() {return new MockComboBox();}
	@Override public GxwListBox listBox_() {return new MockListBox();}
}
