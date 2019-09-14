// hacked from wikisource Mediawiki:Common.js

/**
 * PageNumbers loader
 *
 * Checks for presence of ProofReading extension by looking for pr_quality color status bar
 *
 */
//$( function ( $ ) {
//	var nsDynamicDisplay = [ 0, 114 ];
//	if ( $.inArray( mediaWiki.config.get( 'wgNamespaceNumber' ), nsDynamicDisplay ) !== -1 && !$( 'table.pr_quality' ).length ) {
//		$( 'div' ).remove( '#p-do' );
//	}
//} );

function init_setting ( variable_name, cookie_name, init ) {
/* Sets JS variable to (in order of preference):
	1. previously initialized JS variable value
	2. current cookie value
	3. provided init parameter
	4. false

	Then, if cookie was not previously set, set it to current value of JS variable (this bit currently disabled)
*/
	// get current value of appropriate cookie
	var cookie_val = mediaWiki.cookie.get( cookie_name );
//		some code so that people's cookies will change from digits to boolean values. remove after a week or so.
//		in case you are wondering, this is to avoid the mixed use of digits as numbers and as booleans. --Eliyak
//	if ( cookie_val === "0" ) { cookie_val = false; mediaWiki.cookie.set( cookie_name, false ); }
//		else if ( cookie_val === "1" ) { cookie_val = true; mediaWiki.cookie.set( cookie_name, true ); }

	// If JS variable was not previously initialized in this page load, set it now to the cookie value.
	self[ variable_name ] = self[ variable_name ] || cookie_val;

	// If JS variable still has no value, use provided init value. If no init value provided, use false.
	if ( typeof self[ variable_name ] === "undefined" || self[ variable_name ] === null ) self[ variable_name ] = init || false;

	// If cookie was not set, set it now to the current value of the JS variable.
//	disabling for now - cookie should not need to be set if value was initialized but not changed afterwards.
//	if ( typeof cookie_val === "undefined" ) mediaWiki.cookie.set( cookie_name, self[ variable_name ] );

	// If JS variable is now the string "false", convert to boolean false (to fix JS confusion where "false" string evaluates to true).
	if ( self[ variable_name ] === "false" ) self[ variable_name ] = false;
}

if(!self.ws_messages) self.ws_messages = { };

window.ws_msg = function (name) {
	var m = self.ws_messages[name];
	if(m) return m; else return name;
};

/**
 * Messages are configurable here
 */
self.ws_messages = {
	'do':'Display Options',
	'displayOptions':'Display Options',
	'optlist':'Display Options',
	'p-do':'Display Options',
	'page_numbers_hidden':'Page links hidden',
	'page_numbers_displayed':'Page links displayed',
	'page_numbers_inline':'Page links within text',
	'page_numbers_beside':'Page links beside text',
	'layout_name':'Layout',
	'layout':'Layout',
	'layout_1':'Layout 1',
	'layout_2':'Layout 2',
	'layout_3':'Layout 3',
	'layout_4':'Layout 4',
	'proposed_layout':'Proposed Layout',
};

// Dynamic layouts
if ( !self.ws_layouts ) {
	self.ws_layouts = { };
}
self.ws_layouts['Layout 1'] = {
	'#pageContainer':'',
	'#regionContainer':'',
	'#columnContainer':'',
	'.sidenote-right':'float:right; margin:0.5em; padding:3px; border:solid 1px gray; max-width:9em; text-indent:0em; text-align:left;',
	'.sidenote-left':'float:left; margin:0.5em; padding:3px; border:solid 1px gray; max-width:9em; text-indent:0em; text-align:left;',
	'.mw-editsection':'',
	'#headerContainer':''
};
self.ws_layouts['Layout 2'] = {
	'#pageContainer':'',
	'#regionContainer':'width:36em; margin:0 auto 0 auto; font-family:Georgia,serif;',
	'#columnContainer':'text-align:justify;',
	'.sidenote-right':'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
	'.sidenote-left':'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
	'.mw-editsection':'',
	'#headerContainer':'font-family:sans-serif;'
};
self.ws_layouts['Layout 3'] = {
	'#pageContainer':'',
	'#regionContainer':'min-width:60em; float:left; width:100%; margin-right:-23em;',
	'#columnContainer':'position:relative; text-align:justify; margin-right:23em; text-indent:0em; padding-left:0px; padding-right:0px; width:auto;',
	'.sidenote-right':'position:absolute; right:-10em; width:9em; background-color:#eeeeee; text-indent:0em; text-align:left;',
	'.sidenote-left':'position:absolute; right:-10em; width:9em; background-color:#eeeeee; text-indent:0em; text-align:left;',
	'.mw-editsection':'',
	'#headerContainer':'position:absolute; top:0em; right:-23em; width:21em; float:right; text-align:left;'
};
self.ws_layouts['Layout 4'] = {
	'#pageContainer':'',
	'#regionContainer':'width:540px; margin:0 auto 0 auto; font-family:sans-serif;',
	'#columnContainer':'text-align:justify;',
	'.sidenote-right':'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
	'.sidenote-left':'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
	'.mw-editsection':'',
	'#headerContainer':'font-family:sans-serif;'
};
self.ws_layouts['Proposed Layout'] = {
	'#pageContainer':'',
	'#regionContainer':'',
	'#columnContainer':'margin-right:calc(1rem * 9); text-align:justify;',
	'.sidenote-right':'position:absolute; right:-10em; width:9.00em; background-color:#eeeeee; text-indent:0.00em; text-align:left;',
	'.sidenote-left':'position:absolute; right:-10em; width:9.00em; background-color:#eeeeee; text-indent:0.00em; text-align:left;',
	'.mw-editsection':'',
	'#headerContainer':'font-family:sans-serif;'
};

var layout = function () {
	if ( !self.ws_layouts ) self.ws_layouts = {};
	else self.ws_layouts.names = [];
	var n = 0;
	for( var key in self.ws_layouts ) {
		self.ws_layouts[ key ].number = n;	// get number easily in the future
		self.ws_layouts.names[ n ] = key;	// get name from number easily as well
		n++;
	}
	n -= 1;
	function toggle () {
		set_by_number( ( self.ws_layouts[ self.layout_name ].number + 1 ) % n, true );
	}
	function set_by_number ( number, persist ) {
		set_by_name( self.ws_layouts.names[ number ], persist );
	}
	function set_by_name ( name, persist ) {
		var selected_layout = self.ws_layouts[ name ];
		if ( !selected_layout ) return;		//does not exist

		$.each( selected_layout, function ( selector, style ) {
			$( selector ).attr( "style", style );
		} );

		$( "#d-textLayout" ).children( "a" ).html( name );

		self.layout_name = name;
		if ( persist ) mediaWiki.cookie.set( "layout", name );

		pagenumbers.refresh_offsets();
	}
	function init () {
		var name;
		// do return if we're already set up
		if ( document.getElementById( "pageContainer" ) ) return;

		// get_optlist();
		var portletLink = mediaWiki.util.addPortletLink(
			"p-do",
			"#",
			ws_msg( "layout" ),
			"d-textLayout",
			"The designation of the dynamic layout being applied [alt-l]",
			"l"
		);
		$( portletLink ).click( function ( e ) {
			e.preventDefault();
			toggle();
		} );

		// Check for presence of Proofreading extension by looking for pr_quality color status bar
		if ( $( 'table.pr_quality' ).length ) {
			// remove all these classes to maintain backwards-compatibility
			$( '.text, .lefttext, .centertext, .indented-page, .prose' ).removeClass();
			// DynamicFlaw - a independent Div should have been the parent to this 3-into-1 step
			$( '#mw-content-text' ).contents().not( '#headerContainer, #heederContainer' ).wrapAll( '<div id="pageContainer"><div id="regionContainer"><div id="columnContainer"></div></div></div>' );
		}

		// If cookie is not set, default layout is first available option. Use index "0" in case layout name is ever changed.
		init_setting( "layout_name", "layout", "0" );

		if ( self.layout_overrides_have_precedence || ! mediaWiki.cookie.get( "layout") ) {
			name = $( "#dynamic_layout_overrider" ).text();
		}
		name = name || self.layout_name;
		if ( $.isNumeric( name ) )
			set_by_number( name, false );
		else if ( name )
			set_by_name( name, false );
		else
			set_by_number( 0, false );
	}
	return {
		init: init
	};
}();


var pagenumbers = function () {
 
	// some shared variables to avoid selecting these elements repeatedly
	var container;
	var div_pagenumbers;
	var dp_y;
	var y_prev;
	var pagenumbers_collection;
	var div_ss;
	var div_highlight;

	var show_params = { display: "", link_text: ws_msg( "page_numbers_displayed" ), visible: true };
	var hide_params = { display: "none", link_text: ws_msg( "page_numbers_hidden" ), visible: false };

	function toggle_visible () {
		var params = self.proofreadpage_numbers_visible ? hide_params : show_params;
		pagenumbers_collection.css( "display", params.display );
		$( "#d-pageNumbers_visible" ).children( "a" ).html( params.link_text );
		self.proofreadpage_numbers_visible = params.visible;
		mediaWiki.cookie.set( "pagenums_visible", params.visible );
	}

	function toggle_inline () {
		// toggle inline view unless layouts are not set up
		self.proofreadpage_numbers_inline = !layout || !self.proofreadpage_numbers_inline;
		$( "#d-pageNumbers_inline" ).children( "a" ).html( ws_msg( self.proofreadpage_numbers_inline ? "page_numbers_inline" : "page_numbers_beside" ) );
		mediaWiki.cookie.set( "pagenums_inline", self.proofreadpage_numbers_inline );
		refresh_display();
	}

	function pagenum_in () {
		if ( self.proofreadpage_disable_highlighting ) return false;
		if ( !div_highlight ) return false;		//could not find it
		var id = this.id.substring( 11 );

		var page_span = $( document.getElementById( id ) );
		var next = self.pagenum_ml.eq( self.pagenum_ml.index( page_span ) + 1 );
		if ( next.length === 0 ) next = div_ss;
		// we need to use document offsets in case a page break occurs within a positioned element
		var c_os = container.offset();
		var ps_os = page_span.offset();
		var n_os = next.offset();

		ps_os = { top: ps_os.top - c_os.top, left: ps_os.left - c_os.left };
		n_os = { top: n_os.top - c_os.top, left: n_os.left - c_os.left };

		div_highlight.css( { "display": "block", "top": ps_os.top + "px" } );
		div_highlight.children().eq( 0 ).css( {
			"height": page_span.height() + "px",
			"width": ( ps_os.left < 1 ) ? "100%" : ( ( container.width() - ps_os.left ) + "px" )
		} );
		// div_ss.height() ~= height of 1 line of text
		div_highlight.children().eq( 1 ).css( "height", ( n_os.top - ps_os.top - page_span.height() ) + "px" );
		div_highlight.children().eq( 2 ).css( { "height": next.height() + "px", "width": n_os.left + "px" } );
		return true;
	}

	function pagenum_out () {
		if( self.proofreadpage_disable_highlighting ) return false;
		if ( !div_highlight ) return false;		//could not find it
		div_highlight.css( "display", "none" );
		div_highlight.children().eq( 0 ).css( "width", "0px" );
		div_highlight.children().eq( 1 ).css( "height", "0px" );
		div_highlight.children().eq( 2 ).css( "width", "0px" );
		return true;
	}

	function init () {
		// skip if pagenumbers are already set up
		if ( pagenumbers_collection ) return false;

		// get_optlist();
		init_setting( "proofreadpage_numbers_visible", "pagenums_visible", true );
		var portletLink = mediaWiki.util.addPortletLink(
			"p-do",
			"#",
			self.proofreadpage_numbers_visible ? ws_msg( "page_numbers_displayed" ) : ws_msg( "page_numbers_hidden" ),
			"d-pageNumbers_visible",
			"The current state of embedded link visibility [alt-n]",
			"n"
		);
		$( portletLink ).on( "click", function ( e ) {
			e.preventDefault();
			toggle_visible();
		} );

		init_setting( "proofreadpage_numbers_inline", "pagenums_inline", false );
		// if layouts are not initialized show pagenumbers inline since "beside" view won't work
		if ( !layout ) self.proofreadpage_numbers_inline = true;
		portletLink = mediaWiki.util.addPortletLink(
			"p-do",
			"#",
			self.proofreadpage_numbers_inline ? ws_msg( "page_numbers_inline" ) : ws_msg( "page_numbers_beside" ),
			"d-pageNumbers_inline",
			"The current positioning used for embedded link presentation [alt-i]",
			"i"
		);
		$( portletLink ).on( "click", function ( e ) {
			e.preventDefault();
			toggle_inline();
		} );

		var opacity = "background-color:#000000; opacity:0.2; filter:alpha(opacity=20);";
		// store container for the highlight to shared variable "div_highlight"
		div_highlight = $( '<div id= "highlight-area" style= "display:none; position:absolute; width:100%;">'
		  + '<div style= "' + opacity + ' float:right; width:0px;"><div class= "clearFix"></div></div>'
		  + '<div style= "' + opacity + ' width:100%; height:0px; clear:both;"></div>'
		  + '<div style= "' + opacity + ' width:0px;"><div class= "clearFix" style= "float:left; clear:both;"></div></div>'
		  + '</div>' );

		// assign new div element to shared variable "div_ss"
		div_ss = $( '<div id= "my-ss"><div class= "clearFix"></div></div>' );	//empty span following some text

		// put divs in the innermost dynamic layout container
		if ( layout ) {
			container = $( "#columnContainer" );
			container.append( div_highlight );
			$( ".mw-content-ltr" ).append( div_ss );
		}
		else {
			$( ".mw-content-ltr" ).append( div_highlight, div_ss );
		}

		self.pagenum_ml = $( ".pagenum" );
		refresh_display();
	}

	function refresh_display () {
		// determine if we need to set things up
		var init = !pagenumbers_collection;

		// JQuery collection of all pagenumber elements
		if ( !init ) pagenumbers_collection.remove();
		pagenumbers_collection = $();

		if ( div_pagenumbers ) div_pagenumbers.remove();
		if ( !self.proofreadpage_numbers_inline ) {
			// html div container for page numbers stored in shared variable div_pagenumbers
			div_pagenumbers = $( '<div id= "ct-pagenumbers" style= "position:absolute; top:0; left:0;"></div>' ).appendTo( 'div#pageContainer' );	// put pagenumbers container div in the outermost layout container
			dp_y = div_pagenumbers.offset().top;
			y_prev = { val: -10 };
		}
		self.pagenum_ml.each( init ? init_elem : setup_elem );

		if ( self.proofreadpage_numbers_inline )
			pagenumbers_collection.off( "mouseenter mouseleave" );
		else {
			pagenumbers_collection.hover( pagenum_in, pagenum_out );
		}
	}

	function refresh_elem_offset ( page_span, pagenumber ) {
		var y = $( page_span ).offset().top;
		pagenumber.css( "top", y - dp_y );
		if ( self.proofreadpage_numbers_visible && y - y_prev.val > 5 ) {
			y_prev.val = y;
			pagenumber.css( "display", "" );
		}
		else {
			pagenumber.css( "display", "none" );
		}
	}

	function refresh_offsets () {
		// do nothing if container is not set up
		if ( self.proofreadpage_numbers_inline || !div_pagenumbers ) return false;

		dp_y = div_pagenumbers.offset().top;
		y_prev = { val: -10 };
		pagenumber = pagenumbers_collection.first();
		self.pagenum_ml.each( function ( i, page_span ) {
			refresh_elem_offset ( page_span, pagenumber );
			pagenumber = pagenumber.next();
		} );
		return true;
	}

	function init_elem ( i, page_span ) {
		var name = page_span.getAttribute("data-page-number") || page_span.id;

		// what if two pages have the same number? increment the id
		var pagenumber_id = "pagenumber_" + page_span.id;
		var count;
		if ( pagenumbers_collection.is( "#" + pagenumber_id ) ) {
			count = ( pagenumbers_collection.filter( "[id ^= '" + pagenumber_id + "']" ).length + 1 );
			page_span.id += ( "_" + count );
			pagenumber_id += ( "_" + count );
		}
		$.data( page_span, "pagenumber_id", pagenumber_id );
		var page_title = decodeURI( page_span.title ).replace(/%26/g, "&").replace(/%3F/g, "?");
		var page_url =
			mediaWiki.config.get( "wgArticlePath" )
			.replace( "$1", encodeURIComponent( page_title.replace( / /g, "_" ) ) )
			// encodeURIComponent encodes '/', which breaks subpages
			.replace( /%2F/g, "/" );

		// if transcluded Page: (ll) is a redlink then make page class (class_str) a redlink also
		var ll = page_span.parentNode.nextSibling;
		var class_str = '';
		var action_str = '';
		if ( ll && ll.tagName === "A" && ll.className === "new" ) {
			class_str = ' class="new" ';
			action_str = '?action=edit&redlink=1';
		}
		$.data(
			page_span,
			"link_str",
			'<a href= "' + page_url + action_str + '"' +
				class_str +
				' title= "' + mediaWiki.html.escape( page_title ) + '">' +
				mediaWiki.html.escape( name ) +
			'</a>'
		);
		setup_elem( i, page_span );
	}

	var inline_params = {
		elem: "span",
		style: "color:#666666; font-size:inherit; line-height:inherit; font-family:monospace; font-weight:600; text-shadow:0em 0em 0.25em #A8A; vertical-align:top;",
		link_pre: "&#x0020;[",
		link_post: "]"
	};
	var beside_params = {
		elem: "div",
		style: "position:absolute; font-size:calc(1rem - 5px); line-height:calc(1rem * 1); font-weight:normal; font-style:normal; text-indent:0em;",
		link_pre: "[",
		link_post: "]"
	};

	function setup_elem ( i, page_span ) {
		var params = self.proofreadpage_numbers_inline ? inline_params : beside_params;
		var pagenumber =
			$( '<' + params.elem + ' id= "' + $.data( page_span, "pagenumber_id" ) + '" class= "pagenumber noprint" '
			+ 'style= "' + ( self.proofreadpage_numbers_visible ? '' : 'display:none; ' ) + params.style + '">'
			+ params.link_pre + $.data( page_span, "link_str" ) + params.link_post
			+ '</' + params.elem + '>' );

		if ( !self.proofreadpage_numbers_inline ) refresh_elem_offset ( page_span, pagenumber );
		page_span.innerHTML = self.proofreadpage_numbers_inline ? '' : '';
		pagenumber.appendTo( self.proofreadpage_numbers_inline ? page_span : div_pagenumbers );

		pagenumbers_collection = pagenumbers_collection.add( pagenumber );
	}

	return {
		init: init,
		refresh_offsets: refresh_offsets
	};
}();

if ( $.inArray( mediaWiki.config.get( "wgAction" ), [ "view", "submit", "purge" ] ) !== -1 ) {
	if ( !self.debug_page_layout
		// don't do anything on DoubleWiki or difference comparison views
		&& document.URL.indexOf( "match=" ) === -1
		&& document.URL.indexOf( "diff=" ) === -1
		&& ( 'self.proofreadpage_source_href' ) ) {
			if ( !$.isEmptyObject( self.ws_layouts ) ) {     
				layout.init();
				//mediaWiki.loader.using( 'jquery.cookie' ).done( layout.init );
				//xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery.cookie.js', function () { layout.init(); });
			}
//			$( function() {
				if ( $( ".pagenum" ).length ) {
					//mediaWiki.loader.using( 'jquery.cookie' ).done( pagenumbers.init );
					//xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery.cookie.js', function () { pagenumbers.init(); });
					pagenumbers.init();
					//??$(window).load( pagenumbers.refresh_offsets() );
				}
//			}
//		);
	}
	var position = window.location.hash.substring( 1 );
	if ( position && document.getElementById( position ) ) {
		document.getElementById( position ).scrollIntoView();
	}
}

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

/*XOWA: en.wikisource.org/wiki/MediaWiki:DisplayFooter.js*/
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
	footer += '<div style="text-align:center; margin-left:25%; margin-right:25%;"><a href="#top">' + ws_msg( '▲' ) + '</a></div>';
	footer += '</div><div style="clear:both;"></div></div>';
 
		var $printlinksElt = $( 'div.printfooter' );
		if( $printlinksElt.length !== 0 ) { 	// place footer before category box
			$printlinksElt.after( footer );
		} else {
			$contentElt.after( footer );
		}
	}
);
 
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
	'▲':'Return to the top of the page.',
 
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

// add some xowa specific code to move the image on Page
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 104 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );
