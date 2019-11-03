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
package gplx.xowa.xtns.inputbox; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.core.brys.*;
import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.htmls.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.xndes.*;
public class Xtn_inputbox_nde implements Xox_xnde {
	private int mFound, mBgcolor_bgn, mBgcolor_end, mBreak_bgn, mBreak_end, mButtonlabel_bgn, mButtonlabel_end, mDefault_bgn, mDefault_end, mDir_bgn, mDir_end, mEditintro_bgn, mEditintro_end, mFulltextbutton_bgn, mFulltextbutton_end, mHidden_bgn, mHidden_end, mId_bgn, mId_end, mInline_bgn, mInline_end, mLabeltext_bgn, mLabeltext_end, mMinor_bgn, mMinor_end, mNamespaces_bgn, mNamespaces_end, mNosummary_bgn, mNosummary_end, mPage_bgn, mPage_end, mPlaceholder_bgn, mPlaceholder_end, mPrefix_bgn, mPrefix_end, mPreload_bgn, mPreload_end, mSearchbuttonlabel_bgn, mSearchbuttonlabel_end, mSearchfilter_bgn, mSearchfilter_end, mSummary_bgn, mSummary_end, mTour_bgn, mTour_end, mType_bgn, mType_end, mUseve_bgn, mUseve_end, mWidth_bgn, mWidth_end;

	public void Xtn_parse(Xowe_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Inputbox_tryexact = wiki.Msg_mgr().Val_by_key_args(Key_inputbox_tryexact);
		Inputbox_createarticle = wiki.Msg_mgr().Val_by_key_args(Key_inputbox_createarticle);
		Inputbox_postcomment = wiki.Msg_mgr().Val_by_key_args(Key_inputbox_postcomment);

		int itm_bgn = xnde.Tag_open_end(), itm_end = xnde.Tag_close_bgn();
		if (itm_bgn == src.length)	return;  // NOTE: handle inline where there is no content to parse; EX: <inputbox/>
		if (itm_bgn >= itm_end)		return;  // NOTE: handle inline where there is no content to parse; EX: a<inputbox/>b
		Parse_lines(src, itm_bgn, itm_end);
		}
	public void Xtn_write(Bry_bfr bfr, Xoae_app app, Xop_ctx ctx, Xoh_html_wtr html_wtr, Xoh_wtr_ctx hctx, Xoae_page wpg, Xop_xnde_tkn xnde, byte[] src) {
		if (mFound == -1) return;	// inline inputbox; write nothing; EX: <inputbox/>
		render(bfr, src);
		}

	private void Lowercase(byte[] src, int src_bgn, int src_end) {
		for (int i = src_bgn; i < src_end; i++) {
			byte b = src[i];
			if (b > 64 && b < 91)
				src[i] += 32;	// lowercase
		}
	}

	private void Parse_lines(byte[] src, int src_bgn, int src_end) {
		mFound = mBgcolor_bgn = mBgcolor_end = mBreak_bgn = mBreak_end = mButtonlabel_bgn = mButtonlabel_end = mDefault_bgn = mDefault_end = mDir_bgn = mDir_end = mEditintro_bgn = mEditintro_end = mFulltextbutton_bgn = mFulltextbutton_end = mHidden_bgn = mHidden_end = mId_bgn = mId_end = mInline_bgn = mInline_end = mLabeltext_bgn = mLabeltext_end = mMinor_bgn = mMinor_end = mNamespaces_bgn = mNamespaces_end = mNosummary_bgn = mNosummary_end = mPage_bgn = mPage_end = mPlaceholder_bgn = mPlaceholder_end = mPrefix_bgn = mPrefix_end = mPreload_bgn = mPreload_end = mSearchbuttonlabel_bgn = mSearchbuttonlabel_end = mSearchfilter_bgn = mSearchfilter_end = mSummary_bgn = mSummary_end = mTour_bgn = mTour_end = mType_bgn = mType_end = mUseve_bgn = mUseve_end = mWidth_bgn = mWidth_end = -1;
		int line_bgn = src_bgn; boolean line_is_1st = true;
		while (line_bgn < src_end) {																		// iterate over each \n
			int line_end = Bry_find_.Find_fwd(src, Byte_ascii.Nl, line_bgn, src_end);						// find end "\n"
			if (line_end == Bry_find_.Not_found) line_end = src_end;											// no "\n"; use eos;
			int eq_pos = Bry_find_.Find_fwd(src, Byte_ascii.Eq, line_bgn, line_end);
			if (eq_pos != Bry_find_.Not_found) {
				mFound++;
				// skip any leading whitespace
				int term_bgn = Bry_find_.Find_fwd_while_space_or_tab(src, line_bgn, line_end);
				int term_end = Bry_find_.Trim_bwd_space_tab(src, eq_pos, term_bgn);
				Lowercase(src, term_bgn, term_end);
				// move past '=' and skip whitespace
				eq_pos = Bry_find_.Find_fwd_while_space_or_tab(src, eq_pos+1, line_end);
				// trim trailing whitespace
				int val_end = Bry_find_.Trim_bwd_space_tab(src, line_end, eq_pos);
				if (Bry_.Has_at_bgn(src, inputbox_bgcolor, term_bgn, line_end)) {
					mBgcolor_bgn = eq_pos;
					mBgcolor_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_break, term_bgn, line_end)) {
					mBreak_bgn = eq_pos;
					mBreak_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_buttonlabel, term_bgn, line_end)) {
					mButtonlabel_bgn = eq_pos;
					mButtonlabel_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_default, term_bgn, line_end)) {
					mDefault_bgn = eq_pos;
					mDefault_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_dir, term_bgn, line_end)) {
					mDir_bgn = eq_pos;
					mDir_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_editintro, term_bgn, line_end)) {
					mEditintro_bgn = eq_pos;
					mEditintro_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_fulltextbutton, term_bgn, line_end)) {
					mFulltextbutton_bgn = eq_pos;
					mFulltextbutton_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_hidden, term_bgn, line_end)) {
					mHidden_bgn = eq_pos;
					mHidden_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_id, term_bgn, line_end)) {
					mId_bgn = eq_pos;
					mId_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_inline, term_bgn, line_end)) {
					mInline_bgn = eq_pos;
					mInline_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_labeltext, term_bgn, line_end)) {
					mLabeltext_bgn = eq_pos;
					mLabeltext_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_minor, term_bgn, line_end)) {
					mMinor_bgn = eq_pos;
					mMinor_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_namespaces, term_bgn, line_end)) {
					mNamespaces_bgn = eq_pos;
					mNamespaces_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_nosummary, term_bgn, line_end)) {
					mNosummary_bgn = eq_pos;
					mNosummary_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_page, term_bgn, line_end)) {
					mPage_bgn = eq_pos;
					mPage_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_placeholder, term_bgn, line_end)) {
					mPlaceholder_bgn = eq_pos;
					mPlaceholder_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_prefix, term_bgn, line_end)) {
					mPrefix_bgn = eq_pos;
					mPrefix_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_preload, term_bgn, line_end)) {
					mPreload_bgn = eq_pos;
					mPreload_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_searchbuttonlabel, term_bgn, line_end)) {
					mSearchbuttonlabel_bgn = eq_pos;
					mSearchbuttonlabel_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_searchfilter, term_bgn, line_end)) {
					mSearchfilter_bgn = eq_pos;
					mSearchfilter_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_summary, term_bgn, line_end)) {
					mSummary_bgn = eq_pos;
					mSummary_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_tour, term_bgn, line_end)) {
					mTour_bgn = eq_pos;
					mTour_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_type, term_bgn, line_end)) {
					mType_bgn = eq_pos;
					mType_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_useve, term_bgn, line_end)) {
					mUseve_bgn = eq_pos;
					mUseve_end = val_end;
				}
				else if (Bry_.Has_at_bgn(src, inputbox_width, term_bgn, line_end)) {
					mWidth_bgn = eq_pos;
					mWidth_end = val_end;
				}
			}
			line_bgn = line_end + 1;														// +1 to skip over end "\n"
		}
		/*
		// Validate the width; make sure it's a valid, positive integer
		$this->mWidth = intval( $this->mWidth <= 0 ? 50 : $this->mWidth );
		*/

	}

	private void render(Bry_bfr bfr, byte[] src) {
		if (mType_bgn != -1) {
			if (Bry_.Has_at_bgn(src, type_create, mType_bgn, mType_end)) {
				//$this->mParser->getOutput()->addModules( 'ext.inputBox' );
				getCreateForm(bfr, src, 0);
				return;
			}
			if (Bry_.Has_at_bgn(src, type_comment, mType_bgn, mType_end)) {
				//$this->mParser->getOutput()->addModules( 'ext.inputBox' );
				getCreateForm(bfr, src, 1);
				return;
			}
			else if (Bry_.Has_at_bgn(src, type_move, mType_bgn, mType_end)) {
				getMoveForm(bfr, src);
				return;
			}
			else if (Bry_.Has_at_bgn(src, type_commenttitle, mType_bgn, mType_end)) {
				getCommentForm(bfr, src);
				return;
			}
			else if (Bry_.Has_at_bgn(src, type_search2, mType_bgn, mType_end)) {
				getSearchForm2( bfr, src );
				return;
			}
			else if (Bry_.Has_at_bgn(src, type_search, mType_bgn, mType_end)) {
				getSearchForm( bfr, src, 0 /*'search'*/ );
				return;
			}
			else if (Bry_.Has_at_bgn(src, type_fulltext, mType_bgn, mType_end)) {
				getSearchForm( bfr, src, 1 /*'fulltext'*/ );
				return;
			}
		}
		bfr.Add(Div_err_bgn);
		if (mType_bgn != -1)
			bfr.Add(inputbox_error_bad_type).Add_mid(src, mType_bgn, mType_end);
		else
			bfr.Add(inputbox_error_no_type);
		bfr.Add(Div_err_end);
	}

	private void bgColorStyle(Bry_bfr bfr, byte[] src) {
		if (mBgcolor_bgn != -1)
			bfr.Add_mid(src, mBgcolor_bgn, mBgcolor_end);
		//else
			//bfr.Add(transparent);
	}

	private void getCreateForm(Bry_bfr bfr, byte[] src, int ftype) {
/*
		$htmlOut = Xml::openElement( 'div',
			[
				'class' => 'mw-inputbox-centered',
				'style' => $this->bgColorStyle(),
			]
		);
		
		<div class="mw-inputbox-centered" style="background-color: inherit;">
		<form name="createbox" class="createbox" action="/w/index.php" method="get">
		<input type="hidden" value="edit" name="action" />
		<input type="hidden" value="Template:Taxonomy/preload" name="preload" />
		<input type="text" name="title" class="mw-ui-input mw-ui-input-inline createboxInput" value="Template:Taxonomy/" placeholder="" size="20" dir="ltr" />
		 <input type="submit" name="create" class="mw-ui-button mw-ui-progressive createboxButton" value="Add" />
		</form>
		</div>
*/
		bfr.Add(Div_bgn);
		bgColorStyle(bfr, src);
/*
		$createBoxParams = [
			'name' => 'createbox',
			'class' => 'createbox',
			'action' => $wgScript,
			'method' => 'get'
		];
		if ( $this->mID !== '' ) {
			$createBoxParams['id'] = Sanitizer::escapeIdForAttribute( $this->mID );
		}
		$htmlOut .= Xml::openElement( 'form', $createBoxParams );
		$editArgs = $this->getEditActionArgs();
		$htmlOut .= Html::hidden( $editArgs['name'], $editArgs['value'] );
*/
		bfr.Add(Form_create_bgn);
/*
		if ( $this->mPreload !== null ) {
			$htmlOut .= Html::hidden( 'preload', $this->mPreload );
		}
*/
		if ( mPreload_bgn != -1 )
			hidden( bfr, inputbox_preload, src, mPreload_bgn, mPreload_end );
/*
		if ( is_array( $this->mPreloadparams ) ) {
			foreach ( $this->mPreloadparams as $preloadparams ) {
				$htmlOut .= Html::hidden( 'preloadparams[]', $preloadparams );
			}
		}
		IGNORING
*/
/*
		if ( $this->mEditIntro !== null ) {
			$htmlOut .= Html::hidden( 'editintro', $this->mEditIntro );
		}
*/
		if ( mEditintro_bgn != -1 )
			hidden( bfr, inputbox_editintro, src, mEditintro_bgn, mEditintro_end );
/*
		if ( $this->mSummary !== null ) {
			$htmlOut .= Html::hidden( 'summary', $this->mSummary );
		}
*/
		if ( mSummary_bgn != -1 )
			hidden( bfr, inputbox_summary, src, mSummary_bgn, mSummary_end );
/*
		if ( $this->mNosummary !== null ) {
			$htmlOut .= Html::hidden( 'nosummary', $this->mNosummary );
		}
*/
		if ( mNosummary_bgn != -1 )
			hidden( bfr, inputbox_nosummary, src, mNosummary_bgn, mNosummary_end );
/*
		if ( $this->mPrefix !== '' ) {
			$htmlOut .= Html::hidden( 'prefix', $this->mPrefix );
		}
*/
		if ( mPrefix_bgn != -1 )
			hidden( bfr, inputbox_prefix, src, mPrefix_bgn, mPrefix_end );
/*
		if ( $this->mMinor !== null ) {
			$htmlOut .= Html::hidden( 'minor', $this->mMinor );
		}
*/
		if ( mMinor_bgn != -1 )
			hidden( bfr, inputbox_minor, src, mMinor_bgn, mMinor_end );
/*
		if ( $this->mType == 'comment' ) {
			$htmlOut .= Html::hidden( 'section', 'new' );
		}
*/
		if ( ftype == 1 )  // comment
			bfr.Add(Hidden_create);
/*
		$htmlOut .= Xml::openElement( 'input',
			[
				'type' => $this->mHidden ? 'hidden' : 'text',
				'name' => 'title',
				'class' => $this->getLinebreakClasses() .
					'mw-ui-input mw-ui-input-inline createboxInput',
				'value' => $this->mDefaultText,
				'placeholder' => $this->mPlaceholderText,
				'size' => $this->mWidth,
				'dir' => $this->mDir,
			]
		);
*/
		bfr.Add(Inp_create);	// "<input type=\""
		if (mHidden_bgn != -1)
			bfr.Add(Inp_hidden);
		else
			bfr.Add(Inp_text);
		bfr.Add(Inp_create_tween1);		// "\" name=\"title\" class=\""
		getLinebreakClasses(bfr);
		bfr.Add(Inp_create_tween2);		// "mw-ui-input mw-ui-input-inline createboxInput\" value=\""
		if (mDefault_bgn != -1)
			bfr.Add_mid(src, mDefault_bgn, mDefault_end);
		bfr.Add(Inp_tween2); // "\" placeholder=\""
		if (mPlaceholder_bgn != -1)
			bfr.Add_mid(src, mPlaceholder_bgn, mPlaceholder_end);
		bfr.Add(Inp_tween3); // "\" size=\""
		if (mWidth_bgn != -1)
			bfr.Add_mid(src, mWidth_bgn, mWidth_end);
		else
			bfr.Add(Width_50);
		bfr.Add(Inp_tween4); // "\" dir=\""
		if (mDir_bgn != -1)
			bfr.Add_mid(src, mDir_bgn, mDir_end);
		else
			bfr.Add(dir_ltr);
		bfr.Add(Inp_end); // "\" />"

//??		$htmlOut .= $this->mBR;

/*
		$htmlOut .= Xml::openElement( 'input',
			[
				'type' => 'submit',
				'name' => 'create',
				'class' => 'mw-ui-button mw-ui-progressive createboxButton',
				'value' => $this->mButtonLabel
			]
		);
*/
		bfr.Add(Inp_create2);	// "<input type=\"submit\" name=\"create\" class=\"mw-ui-button mw-ui-progressive createboxButton\" value=\""
		if ( mButtonlabel_bgn != -1 )
			bfr.Add_mid(src, mButtonlabel_bgn, mButtonlabel_end);
		else if ( ftype == 1 )  // comment
			bfr.Add(Inputbox_postcomment);
		else
			bfr.Add(Inputbox_createarticle);
		bfr.Add(Inp_end); // "\" />"
/*
		$htmlOut .= Xml::closeElement( 'form' );
		$htmlOut .= Xml::closeElement( 'div' );
*/
		bfr.Add(Form_close);
	}
	private void getMoveForm(Bry_bfr bfr, byte[] src) {
	}
	private void getCommentForm(Bry_bfr bfr, byte[] src) {
	}
	private void getSearchForm(Bry_bfr bfr, byte[] src, int ftype) {
		// Build HTML
		bfr.Add_bry_many(Div_bgn);
		bgColorStyle(bfr, src);
		bfr.Add(Form_search_bgn);
		bfr.Add_bry_many(Inp_bgn); //"<input class=\""
		getLinebreakClasses(bfr);
		bfr.Add(Inp_classes); // "searchboxInput mw-ui-input mw-ui-input-inline\" name=\"search\" type=\""
		if (mHidden_bgn != -1)
			bfr.Add(Inp_hidden);
		else
			bfr.Add(Inp_text);
		bfr.Add(Inp_tween1); // "\" value=\""
		if (mDefault_bgn != -1)
			bfr.Add_mid(src, mDefault_bgn, mDefault_end);
		bfr.Add(Inp_tween2); // "\" placeholder=\""
		if (mPlaceholder_bgn != -1)
			bfr.Add_mid(src, mPlaceholder_bgn, mPlaceholder_end);
		bfr.Add(Inp_tween3); // "\" size=\""
		if (mWidth_bgn != -1)
			bfr.Add_mid(src, mWidth_bgn, mWidth_end);
		else
			bfr.Add(Width_50);
		bfr.Add(Inp_tween4); // "\" dir=\""
		if (mDir_bgn != -1)
			bfr.Add_mid(src, mDir_bgn, mDir_end);
		else
			bfr.Add(dir_ltr);
		bfr.Add(Inp_end); // "\" />"

		if ( mPrefix_bgn != -1 )
			hidden( bfr, inputbox_prefix, src, mPrefix_bgn, mPrefix_end );

		if ( mSearchfilter_bgn != -1 )
			hidden( bfr, inputbox_searchfilter, src, mSearchfilter_bgn, mSearchfilter_end );

		if ( mTour_bgn != -1 )
			hidden( bfr, inputbox_tour, src, mTour_bgn, mTour_end );

		if (mNamespaces_bgn != -1) {
                    //int a=1;
		}
		else if (ftype == 0) {
			// Go button
			bfr.Add(Go_bgn); //"<input type=\"submit\" name=\"go\" class=\"mw-ui-button\" value=\""
			if ( mButtonlabel_bgn != -1 )
				bfr.Add_mid(src, mButtonlabel_bgn, mButtonlabel_end);
			else
				bfr.Add(Inputbox_tryexact);
			bfr.Add(Inp_end); // "\" />"
		}
		// Search button
		bfr.Add(Search_bgn); // "<input type=\"submit\" name=\"fulltext\" class=\"mw-ui-button\" value=\""
		if ( mSearchbuttonlabel_bgn != -1)
			bfr.Add_mid(src, mSearchbuttonlabel_bgn, mSearchbuttonlabel_end);
		else
			bfr.Add(inputbox_searchfulltext);
		bfr.Add(Inp_end); // "\" />"


		// Hidden fulltext param for IE (bug 17161)
		if ( ftype == 1 ) //$type == 'fulltext' )
			hidden( bfr, Fulltext, Search, 0, Search.length );

		bfr.Add(Form_close);
	}

	private void hidden(Bry_bfr bfr, byte[] name, byte[] src, int bgn, int end) {
		bfr.Add(Hid_bgn); // "<input type=\"hidden\" name=\""
		bfr.Add(name);
		bfr.Add(Hid_tween); // "\" value=\""
		bfr.Add_mid(src, bgn, end);
		bfr.Add(Hid_end); // "\" />"
	}
	private void getSearchForm2(Bry_bfr bfr, byte[] src) {
		/*
		// Use button label fallbacks
		if ( !$this->mButtonLabel ) {
			$this->mButtonLabel = wfMessage( 'inputbox-tryexact' )->text();
		}

		if ( $this->mID !== '' ) {
			$unescapedID = $this->mID;
		} else {
			// The label element needs a unique id, use
			// random number to avoid multiple input boxes
			// having conflicts.
			$unescapedID = wfRandom();
		}
		$id = Sanitizer::escapeIdForAttribute( $unescapedID );
		$htmlLabel = '';
		if ( isset( $this->mLabelText ) && strlen( trim( $this->mLabelText ) ) ) {
			$htmlLabel = Xml::openElement( 'label', [ 'for' => 'bodySearchInput' . $id ] );
			$htmlLabel .= $this->mParser->recursiveTagParse( $this->mLabelText );
			$htmlLabel .= Xml::closeElement( 'label' );
		}
		$htmlOut = Xml::openElement( 'form',
			[
				'name' => 'bodySearch' . $id,
				'id' => 'bodySearch' . $id,
				'class' => 'bodySearch' . ( $this->mInline ? ' mw-inputbox-inline' : '' ),
				'action' => SpecialPage::getTitleFor( 'Search' )->getLocalUrl(),
			]
		); */
		///////
		bfr.Add_str_a7("<form name=\"bodySearch1\" id=\"bodySearch1\" class=\"bodySearch");
		if (mInline_bgn != -1)
			bfr.Add_str_a7(" mw-inputbox-inline");
		bfr.Add_str_a7("\" action=\"/wiki/Special:Search\">");
		//////
		/*$htmlOut .= Xml::openElement( 'div',
			[
				'class' => 'bodySearchWrap' . ( $this->mInline ? ' mw-inputbox-inline' : '' ),
				'style' => $this->bgColorStyle(),
			]
		);*/
		//////
		bfr.Add_str_a7("<div class=\"bodySearchWrap");
		if (mInline_bgn != -1)
			bfr.Add_str_a7(" mw-inputbox-inline");
		bfr.Add_str_a7("\" style=\"");
		bgColorStyle(bfr, src);
		bfr.Add_str_a7("\">");
		//////
		/*$htmlLabel = '';
		if ( isset( $this->mLabelText ) && strlen( trim( $this->mLabelText ) ) ) {
			$htmlLabel = Xml::openElement( 'label', [ 'for' => 'bodySearchInput' . $id ] );
			$htmlLabel .= $this->mParser->recursiveTagParse( $this->mLabelText );
			$htmlLabel .= Xml::closeElement( 'label' );
		}
		$htmlOut .= $htmlLabel;*/
		//////
		if (mLabeltext_bgn != -1) {
			bfr.Add_str_a7("<label for=\"bodySearchInput1\">");
			//recursiveTagParse(???
			bfr.Add_mid(src, mLabeltext_bgn, mLabeltext_end);
			bfr.Add_str_a7("</label>");
		}
		//////
		/*$htmlOut .= Xml::element( 'input',
			[
				'type' => $this->mHidden ? 'hidden' : 'text',
				'name' => 'search',
				'class' => 'mw-ui-input mw-ui-input-inline',
				'size' => $this->mWidth,
				'id' => 'bodySearchInput' . $id,
				'dir' => $this->mDir,
			]
		);*/
		//////
		bfr.Add(Inp_create);	// "<input type=\""
		if (mHidden_bgn != -1)
			bfr.Add(Inp_hidden);
		else
			bfr.Add(Inp_text);
		bfr.Add_str_a7("\" name=\"search\" class=\"mw-ui-input mw-ui-input-inline\" size=\"");
		if (mWidth_bgn != -1)
			bfr.Add_mid(src, mWidth_bgn, mWidth_end);
		else
			bfr.Add(Width_50);
		bfr.Add_str_a7("\" id=\"bodySearchInput1\" dir=\"");
		if (mDir_bgn != -1)
			bfr.Add_mid(src, mDir_bgn, mDir_end);
		else
			bfr.Add(dir_ltr);
		bfr.Add_str_a7("\" />");
		//////
		/*$htmlOut .= '&#160;' . Xml::element( 'input',
			[
				'type' => 'submit',
				'name' => 'go',
				'value' => $this->mButtonLabel,
				'class' => 'mw-ui-button',
			]
		);*/
		//////
		bfr.Add_str_a7("&#160;<input type=\"submit\" name=\"go\" value=\"");
		if ( mButtonlabel_bgn != -1 )
			bfr.Add_mid(src, mButtonlabel_bgn, mButtonlabel_end);
		else
			bfr.Add(Inputbox_tryexact);
		bfr.Add_str_a7("\" class=\"mw-ui-button\" />");
		//////

		/*// Better testing needed here!
		if ( !empty( $this->mFullTextButton ) ) {
			$htmlOut .= Xml::element( 'input',
				[
					'type' => 'submit',
					'name' => 'fulltext',
					'class' => 'mw-ui-button',
					'value' => $this->mSearchButtonLabel
				]
			);
		}

		$htmlOut .= Xml::closeElement( 'div' );
		$htmlOut .= Xml::closeElement( 'form' );

		// Return HTML
		return $htmlOut;*/
		bfr.Add_str_a7("</div></form>\n");
	}
	private void getLinebreakClasses(Bry_bfr bfr) {
	}

	private static byte[]
	  Div_inputbox_bgn = Bry_.new_a7("<div class=\"mw-inputbox-centered\" style=\"background-color: #ffffff;\">")
	, Div_inputbox_end = Bry_.new_a7("\n</div>")
	, Div_err_bgn = Bry_.new_a7("<div><strong class=\"error\">")
	, Div_err_end = Bry_.new_a7("</strong></div>")
	, Inp_bgn = Bry_.new_a7("<input class=\"")
	, Inp_classes = Bry_.new_a7("searchboxInput mw-ui-input mw-ui-input-inline\" name=\"search\" type=\"")
	, Inp_text = Bry_.new_a7("text")
	, Inp_hidden = Bry_.new_a7("hidden")
	, Inp_tween1 = Bry_.new_a7("\" value=\"")
	, Inp_tween2 = Bry_.new_a7("\" placeholder=\"")
	, Inp_tween3 = Bry_.new_a7("\" size=\"")
	, Inp_tween4 = Bry_.new_a7("\" dir=\"")
	, Inp_end = Bry_.new_a7("\" />")
	, Inp_create = Bry_.new_a7("<input type=\"")
	, Inp_create_tween1 = Bry_.new_a7("\" name=\"title\" class=\"")
	, Inp_create_tween2 = Bry_.new_a7("mw-ui-input mw-ui-input-inline createboxInput\" value=\"")
	, Inp_create2 = Bry_.new_a7("<input type=\"submit\" name=\"create\" class=\"mw-ui-button mw-ui-progressive createboxButton\" value=\"")
	, dir_ltr = Bry_.new_a7("ltr")
	, Hid_bgn = Bry_.new_a7("<input type=\"hidden\" name=\"")
	, Hid_tween = Bry_.new_a7("\" value=\"")
	, Hid_end = Bry_.new_a7("\" />")
	, Go_bgn = Bry_.new_a7("<input type=\"submit\" name=\"go\" class=\"mw-ui-button\" value=\"")
	, Search_bgn = Bry_.new_a7("<input type=\"submit\" name=\"fulltext\" class=\"mw-ui-button\" value=\"")
	, Search = Bry_.new_a7("Search")
	, Fulltext = Bry_.new_a7("fulltext")
	, inputbox_bgcolor = Bry_.new_a7("bgcolor")
	, inputbox_break = Bry_.new_a7("break")
	, inputbox_buttonlabel = Bry_.new_a7("buttonlabel")
	, inputbox_default = Bry_.new_a7("default")
	, inputbox_dir = Bry_.new_a7("dir")
	, inputbox_editintro = Bry_.new_a7("editintro")
	, inputbox_fulltextbutton = Bry_.new_a7("fulltextbutton")
	, inputbox_hidden = Bry_.new_a7("hidden")
	, inputbox_id = Bry_.new_a7("id")
	, inputbox_inline = Bry_.new_a7("inline")
	, inputbox_labeltext = Bry_.new_a7("labeltext")
	, inputbox_minor = Bry_.new_a7("minor")
	, inputbox_namespaces = Bry_.new_a7("namespaces")
	, inputbox_nosummary = Bry_.new_a7("nosummary")
	, inputbox_page = Bry_.new_a7("page")
	, inputbox_placeholder = Bry_.new_a7("placeholder")
	, inputbox_prefix = Bry_.new_a7("prefix")
	, inputbox_preload = Bry_.new_a7("preload")
	, inputbox_searchbuttonlabel = Bry_.new_a7("searchbuttonlabel")
	, inputbox_searchfilter = Bry_.new_a7("searchfilter")
	, inputbox_summary = Bry_.new_a7("summary")
	, inputbox_tour = Bry_.new_a7("tour")
	, inputbox_type = Bry_.new_a7("type")
	, inputbox_useve = Bry_.new_a7("useve")
	, inputbox_width = Bry_.new_a7("width")
	, type_create = Bry_.new_a7("create")
	, type_comment = Bry_.new_a7("comment")
	, type_move = Bry_.new_a7("move")
	, type_commenttitle = Bry_.new_a7("commenttitle")
	, type_search = Bry_.new_a7("search")
	, type_search2 = Bry_.new_a7("search2")
	, type_fulltext = Bry_.new_a7("fulltext")
	, Div_bgn = Bry_.new_a7("<div class=\"mw-inputbox-centered\" style=\"background-color: ")
	, Form_search_bgn = Bry_.new_a7("\">\n<form name=\"searchbox\" class=\"searchbox\" action=\"/wiki/Special:Search\">")
	, Form_close = Bry_.new_a7("</form>\n</div>\n")
	, Form_create_bgn = Bry_.new_a7("\">\n<form name=\"createbox\" class=\"createbox\" action=\"/wiki/Special:Create\" method=\"get\">\n<input type=\"hidden\" value=\"edit\" name=\"action\" />")
	, Hidden_create = Bry_.new_a7("<input type=\"hidden\" name=\"section\" value=\"new\">")
	, transparent = Bry_.new_a7("transparent")
	, Width_50 = Bry_.new_a7("50")
	// language sensitive
	, inputbox_desc = Bry_.new_a7("Allow inclusion of predefined HTML forms")
	, inputbox_error_no_type = Bry_.new_a7("You have not specified the type of input box to create.")
	, inputbox_error_bad_type = Bry_.new_a7("Input box type \"$1\" not recognized.\nPlease specify \"create\", \"comment\", \"search\", \"search2\" or \"fulltext\".")

	, Key_inputbox_tryexact = Bry_.new_a7("inputbox-tryexact") // "Try exact match"
	, Inputbox_tryexact
	, inputbox_searchfulltext = Bry_.new_a7("Search full text")
	, Key_inputbox_createarticle = Bry_.new_a7("inputbox-createarticle") //"Create page"
	, Inputbox_createarticle
	, inputbox_movearticle = Bry_.new_a7("Move page")
	, Key_inputbox_postcomment = Bry_.new_a7("inputbox-postcomment") // "New section"
	, Inputbox_postcomment
	, inputbox_postcommenttitle = Bry_.new_a7("New section")
	, inputbox_ns_main = Bry_.new_a7("Main")
	//
	;
}
