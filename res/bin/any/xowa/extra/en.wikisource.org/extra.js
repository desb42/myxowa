// hacked from wikisource Mediawiki:Common.js

// moved to beginning of file (to fire first?!)
/**
 * Force Footer &/or end matter out of Dynamic Layouts
 * Namespace coverage: Main (ns-0), Translation (ns-114)
 * See also _____
 * 
 * Ver 0.40, 2015-01-31
 */
jQuery( document ).ready( function ( $ ) {
	var nsFooters = [ 0, 114 ];
	if ( $.inArray( mediaWiki.config.get( 'wgNamespaceNumber' ), nsFooters ) !== -1 ) {
		$( '.acContainer' ).insertAfter( $( 'div.printfooter' ) );
		$( 'div.licenseContainer' ).not( 'div.licenseContainer div.licenseContainer' ).insertBefore( $( 'div#catlinks' ) );
	}
} );

/*XOWA: en.wikisource.org/wiki/MediaWiki:IndexForm.js*/
/********************************
  enhanced form for index pages
*********************************/


function set_index_field(name,value,on_book,on_collection,on_journal,on_phdthesis, on_dictionary){
	var f = document.getElementsByName('wpprpindex-' + name)[0];
	if(!f) return;
	f = f[0];
	if(f) {
		if( (value=="book" && on_book) || (value=="journal" && on_journal) || (value=="collection" && on_collection) || (value=="phdthesis" && on_phdthesis) || value=="dictionary" && on_dictionary ) {
			f.disabled=false;
			f.parentNode.parentNode.style.display="";
		} else {
			f.disabled=true;
			f.parentNode.parentNode.style.display="none";
		}
	}
}



function type_changed(f,value) {
	//see http://www.easybib.com/reference/guide/apa/dictionary
	set_index_field( ws_msg('author'),     value, 1, 1, 0, 1, 0);
	set_index_field( ws_msg('translator'), value, 1, 1, 0, 0, 0);
	set_index_field( ws_msg('editor'),     value, 1, 1, 1, 0, 1);
	set_index_field( ws_msg('place'),      value, 1, 1, 1, 0, 1);
	set_index_field( ws_msg('publisher'),     value, 1, 1, 1, 0, 1);
	set_index_field( ws_msg('volume'),     value, 1, 1, 1, 0, 1);
	set_index_field( ws_msg('school'),     value, 0, 0, 0, 1, 0);
}

function progress_select_value() {
    var select;
    if (!self.ws_messages || !self.ws_messages['progress_select']) {
        select = "<option value='' selected=true></option>" + 
                 "<option value='T'>" + ws_msg('progress_T') + "</option>" +
                 "<option value='V'>" + ws_msg('progress_V') + "</option>" +
                 "<option value='C'>" + ws_msg('progress_C') + "</option>" +
                 "<option value='MS'>"+ ws_msg('progress_MS')+ "</option>" +
                 "<option value='OCR'>"+ws_msg('progress_OCR')+"</option>" +
                 "<option value='X'>" + ws_msg('progress_X') + "</option>" +
                 "<option value='L'>" + ws_msg('progress_L') + "</option>";
   } else {
        select = self.ws_messages['progress_select'];
   }

   return select;
}

function index_choices(){
	if( mediaWiki.config.get( 'wgCanonicalNamespace' ) == "Index" ) {
		var f = document.editform;
		var value = "book";
		if(f) {
			var a = f['wpprpindex-Type'];
			if(a) {

				value = a.value;
				a.parentNode.innerHTML="<select onChange=\"type_changed(this.form,this.options[this.selectedIndex].value);\" name=\"wpprpindex-Type\">"
+"<option value=\"book\" selected=true>" + ws_msg('book') + "</option>"
+"<option value=\"collection\">" + ws_msg('collection') + "</option>"
+"<option value=\"journal\">" + ws_msg('journal') + "</option>"
+"<option value=\"phdthesis\">" + ws_msg('phdthesis') + "</option>"
+"<option value=\"dictionary\">" + ws_msg('dictionary') + "</option>"
+"</select>";
				a = f['wpprpindex-Type'];
				for (var i=0; i < a.length; i++) {
					if (a[i].value == value) a[i].selected = true;
				}
				if(value=="") value="book";
				type_changed(f,value);
			}
			var page = mediaWiki.config.get( 'wgPageName' );
			var suffix = page.substring( 'page.length-4', 'page.length' ).toLowerCase();
			if( suffix=='djvu' || suffix=='.pdf' ) {
				var m_source = 'wpprpindex-' + ws_msg('Source');
				set_index_field( m_source, value, 0, 0, 0, 0, 0);
				f = document.getElementsByName(m_source)[0];
				if(f) {
				 if( suffix=='djvu' ) f.value='djvu';
				 if( suffix=='.pdf' ) f.value='pdf';
                                }
				f = document.getElementsByName( 'wpprpindex-' + ws_msg('Image'))[0];
				if(f && f.value=="") f.value="1";
				f = document.getElementsByName( 'wpprpindex-' + ws_msg('Pages'))[0];
				if(f && f.value=="") f.value="<pagelist />";
			}
			var a = document.getElementsByName('wpprpindex-' + ws_msg('progress'))[0];
			if(a) {
				value = a.value;
				a.parentNode.innerHTML= "<select name='"+ 'wpprpindex-' + ws_msg('progress') + "'>" + 
							progress_select_value() + 
							"</select>";
				a = document.getElementsByName('wpprpindex-' + ws_msg('progress'))[0];
				for (var i=0; i < a.length; i++) {
					if (a[i].value == value) a[i].selected = true;
				}
			}

		}
	}
}
$(index_choices);

function addObj(obj1, obj2){
    for (var j in obj2) {
      obj1[j] = obj2[j];
    }
    return obj1;
};
 
/**
 * Messages are configurable here
 */
if(!self.ws_messages) self.ws_messages = { };
 
window.ws_msg = function (name) {
	var m = self.ws_messages[name];
	if(m) return m; else return name;
};
 
self.ws_messages = {
//self.ws_messages = addObj(self.ws_messages, {
	'author':'Author',
	'translator':'Translator',
	'editor':'Editor',
	'publisher':'Publisher',
	'place':'Place',
	'volume':'Volume',
	'school':'School',
	'book':'Book',
	'collection':'Collection',
	'journal':'Journal or magazine',
	'phdthesis':'Thesis, report',
	'dictionary':'Dictionary',
	'progress':'Progress',
	'progress_T':'Done',
	'progress_V':'To be validated',
	'progress_C':'To be proofread',
	'progress_MS':'Ready for Match & Split',
	'progress_OCR':'Source file needs an OCR text layer',
	'progress_L':'Source file is incorrect (missing pages, unordered pages, etc)',
	'progress_X':'Pagelist needed (to verify file is complete and correct before commencing proofreading)',
	'▲':'Return to the top of the page.',
	'top':'Return to the top of the page.',
 
	'corr_list':'List of typos identified on this page',
	'corr_link':'Typos Marked',
	'corr_one':'One typo</a> has been marked.',
	'corr_many':' typos</a> have been marked.',
	'corr_close':'Close.',
 
	'iwtrans':'Its text comes from',
	'iwtrans2':'Its text comes from other Wikisource subdomains.',
 
	'page_namespace_name':      'Page',
	'page_trascluded_in':       'Page trascluded in:',
	'text_number':              'Text',
 
	'compare_with':  'Comparison with:',
	'compare_texts': 'Compare texts'
};
//});
 
/* stop faux red links on fresh links */
$( 'div.mw-body a' ).removeClass( 'stub' );
 
/**
 * Envelope subNotes found in main navigation header derivatives
 * Namespace coverage: Main (ns-0), Translation (ns-114)
 * See also _____
 * 
 * Ver 0.10, 2015-01-31
 */
jQuery( document ).ready( function ( $ ) {
	var nsSubNotes = [ 0, 114 ];
	if ( $.inArray( mediaWiki.config.get( 'wgNamespaceNumber' ), nsSubNotes ) !== -1 ) {
		$( 'div.subNote' ).insertBefore( $( 'div#ws-data' ) ); 
	}
} );
/**
 * Envelope hatNotes & similar into main navigation header container
 * Namespace coverage: Main (ns-0), Translation (ns-114)
 * See also _____
 * 
 * Ver 0.30, 2015-12-31
 */
jQuery( document ).ready( function ( $ ) {
	var nsHatNotes = [ 0, 114 ];
	if ( $.inArray( mediaWiki.config.get( 'wgNamespaceNumber' ), nsHatNotes ) !== -1 ) {
		$( 'div.similar' ).prependTo( $( 'div#headerContainer' ) ); 
		$( 'table.ambox' ).prependTo( $( 'div#headerContainer' ) );
	}
} );


/*XOWA: en.wikisource.org/wiki/MediaWiki:PageNumbers.js*/
/**
 * PageNumbers   Dynamic Layouts helper
 *
 * removes sidebar Display Options menu from all ns except Main and Translation
 *
 */
jQuery( document ).ready( function ( $ ) {
	var nsDynamicLayouts = [ -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 100, 101, 102, 103, 104, 105, 106, 107, 115, 828, 829 ];
	if ( $.inArray( mediaWiki.config.get( 'wgNamespaceNumber' ), nsDynamicLayouts ) !== -1 ) {
		$( 'div' ).remove( '#p-do' );
	}
} );

// </source>

// add some xowa specific code to move the image on Page (en:Page: == 104)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 104 ) {
		//$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
		$( '#xowa_pp_image' ).children().children().appendTo( $( '.prp-page-image' ) );
	}
} );

if( mediaWiki.config.get( 'wgNamespaceNumber' ) == 0) {
	xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/en.wikisource.org/Gadget-PageNumbers-core.js');
	xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/en.wikisource.org/Corrections.js');

//	xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/en.wikisource.org/Gadget-DisplayFooter.js');
/**********************
*** Automatically generate page footer from values in <nowiki>{{header}}</nowiki>
*** by [[user:GrafZahl]] and [[user:Tpt]]
**********************/

/* eslint-disable one-var, vars-on-top, no-jquery/no-global-selector */

$( function () {
	if ( mw.config.get( 'wgNamespaceNumber' ) !== 0 ) { // || mw.util.getParamValue( 'match' ) !== null ) {
		return;
	}

	var $nofooterElt = $( '#nofooter' ),
		$hp = $( '#headerprevious' ),
		$hn = $( '#headernext' ),
		$contentElt = $( '#mw-content-text' );
	if ( $contentElt.length === 0 ||
			( $hp.length === 0 && $hn.length === 0 ) ||
			$nofooterElt.length !== 0 ) {
		return;
	}

	var footer = '<div class="footertemplate ws-noexport noprint dynlayout-exempt" id="footertemplate" style="margin-top:1em; clear:both;">';
	footer += '<div style="width:100%; padding-left:0px; padding-right:0px; background-color:transparent;">';
	if ( $hp.length !== 0 ) {
		footer += '<div style="text-align:left; float:left; max-width:40%;">←<span id="footerprevious">' + $hp.html() + '</span></div>';
	}
	if ( $hn.length !== 0 ) {
		footer += '<div style="text-align:right; float:right; max-width:40%;"><span id="footernext">' + $hn.html() + '</span>→</div>';
	}
	footer += '<div style="text-align:center; margin-left:25%; margin-right:25%;"><a href="#top">' + ws_msg( '▲' ) + '</a></div>';
	footer += '</div><div style="clear:both;"></div></div>';

	var $printlinksElt = $( 'div.printfooter' );
	if ( $printlinksElt.length !== 0 ) { // place footer before category box
		$printlinksElt.after( footer );
	} else {
		$contentElt.after( footer );
	}
} );
}
