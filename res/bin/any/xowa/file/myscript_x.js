// 'popups_get_html', popup_itm, popup_tooltip, ''
function xowa_exec(act, arg1, arg2, arg3)
{
	if (act == 'get_search_suggestions')
	    getsuggestions(arg1);
	else if (act == 'popups_get_html')
	{
	    getwikix(arg1, arg2); //'<p>here</p>';
	}
}

function getsuggestions(word)
{
      var req = new XMLHttpRequest();
      var path = document.location.protocol + '//' + document.location.host + '/' + x_p.wiki + '/search/' + word;
      //var path = document.location + '?action=search&term=' + word;
      //var path = '/xowa/search/' + word;
      req.onload = function (e) {
      	var txt = req.responseText.replace(/\\'/g, "'");
      	var jtext = '[' + txt.substring(14, txt.length-2) + ']';
        var res = $.parseJSON(jtext);
        receiveSuggestions(res[0], res[1]);
      }
      req.open("GET", path, true); // 'false': synchronous.
      req.send(null);
}

function getwikix(popup_itm, ev)
{
      rettxt = '';
      var req = new XMLHttpRequest();
      var path = popup_itm.href + '?action=popup';
      req.onload = function (e) {
        var res = req.responseText;
        popup_itm.html = res;
        xowa_popups_gotpopup(popup_itm, ev);
      }
      req.open("GET", path, true); // 'false': synchronous.
      req.send(null);
}

// hacked from wikisource Mediawiki:Common.js

/**
 * Messages are configurable here
 */
if(!self.ws_messages) self.ws_messages = { };
 
window.ws_msg = function (name) {
	var m = self.ws_messages[name];
	if(m) return m; else return name;
};
 
self.ws_messages = {
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
	'?':'Return to the top of the page.',
 
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
		$( 'table.acContainer' ).insertAfter( $( 'div.printfooter' ) );
		$( 'div.licenseContainer' ).not( 'div.licenseContainer div.licenseContainer' ).insertBefore( $( 'div#catlinks' ) );
	}
} );
/**
 * Force Header &/or section heading matter out of Dynamic Layouts
 * Namespace coverage: Main (ns-0), Translation (ns-114)
 * See also _____
 * 
 * Ver 0.20, 2015-12-31
 */
jQuery( document ).ready( function ( $ ) {
	var nsHeaders = [ 0, 114 ];
	if ( $.inArray( mediaWiki.config.get( 'wgNamespaceNumber' ), nsHeaders ) !== -1 ) {
		$( 'div#headerContainer' ).prependTo( $( 'div#mw-content-text' ) );
		$( 'div#heederContainer' ).prependTo( $( 'div#mw-content-text' ) );
		$( 'div#heedertemplate' ).prependTo( $( 'div#mw-content-text' ) );
	}
} );
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


/**********************
*** Automatically generate page footer from values in <nowiki>{{header}}</nowiki>
*** by [[user:GrafZahl]] and [[user:Tpt]]
**********************/
// <source lang="javascript">
 
$( document ).ready( function() {
	if( mediaWiki.config.get( 'wgNamespaceNumber' ) !== 0) { // || mediaWiki.util.getParamValue( 'match' ) !== null ) {
		return;
	}
	var $nofooterElt = $( '#nofooter' );
	var $hp = $( '#headerprevious' );
	var $hn = $( '#headernext' );
 	var $contentElt = $( '#mw-content-text' );
	if( $contentElt.length === 0 || ($hp.length === 0 && $hn.length === 0) || $nofooterElt.length !== 0 ) {
		return; 
	}
 
	var footer = '<div class="footertemplate ws-noexport noprint" id="footertemplate" style="margin-top:1em; clear:both;">';
	footer += '<div style="width:100%; padding-left:0px; padding-right:0px; background-color:transparent;">';
	if( $hp.length !== 0 ) {
		footer += '<div style="text-align:left; float:left; max-width:40%;"><span id="footerprevious">' + $hp.html() + '</span></div>';
	}
	if( $hn.length !== 0 ) {
		footer += '<div style="text-align:right; float:right; max-width:40%;"><span id="footernext">' + $hn.html() + '</span></div>';
	}
	footer += '<div style="text-align:center; margin-left:25%; margin-right:25%;"><a href="#top">' + ws_msg( '?' ) + '</a></div>';
	footer += '</div><div style="clear:both;"></div></div>';
 
		var $printlinksElt = $( 'div.printfooter' );
		if( $printlinksElt.length !== 0 ) { 	// place footer before category box
			$printlinksElt.after( footer );
		} else {
			$contentElt.after( footer );
		}
	}
);
 
// </source>