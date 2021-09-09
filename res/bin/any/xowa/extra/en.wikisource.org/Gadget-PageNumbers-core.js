/* eslint-disable camelcase */

( function ( mw, $ ) {

	function initSetting( cookie_name, init ) {
	/* Sets JS variable to (in order of preference):
		2. current cookie value
		3. provided init parameter
		4. false
	*/
		// get current value of appropriate cookie
		var cookie_val = mw.cookie.get( cookie_name );

		// If JS variable still has no value, use provided init value. If no init
		// value provided, use false.
		if ( typeof cookie_val === 'undefined' || cookie_val === null ) {
			cookie_val = init || false;
		}

		// If JS variable is now the string "false", convert to boolean false
		// (to fix JS confusion where "false" string evaluates to true).
		if ( cookie_val === 'false' ) {
			cookie_val = false;
		}

		return cookie_val;
	}

	function saveSetting( name, value ) {
		mw.cookie.set( name, value );
	}

	/**
	 * Messages are configurable here
	 */
	mw.messages.set( {
		do: 'Display Options',
		displayOptions: 'Display Options',
		optlist: 'Display Options',
		'p-do': 'Display Options',
		page_numbers_hidden: 'Page links hidden',
		page_numbers_displayed: 'Page links displayed',
		page_numbers_inline: 'Page links within text',
		page_numbers_beside: 'Page links beside text',
		layout_name: 'layout_1',
		layout: 'Layout',
		use_serif: 'Use serif fonts',
		use_sans_serif: 'Use sans-serif fonts',
		serif_text_title: 'Change between serif and sans-serif fonts',
		default_layout_on: 'Default layouts on',
		default_layout_off: 'Default layouts off',
		default_layout_title: 'Default layouts allow pages to choose a specific layout for you. Turn it off if you always want the layout you set.',
		default_layout_suffix: 'default',
		what_is_this_title: 'What is this?',
		what_is_this_symbol: '?'
	} );

	var standard_layouts = [
		{
			id: 'layout_1',
			name: 'Layout 1'
		},
		{
			id: 'layout_2',
			name: 'Layout 2'
		},
		{
			id: 'layout_3',
			name: 'Layout 3'
		},
		{
			id: 'layout_4',
			name: 'Layout 4'
		}
	];

	// eslint-disable-next-line no-jquery/no-global-selector
	var $classedContainer = $( '#mw-content-text' );

	var containers = {};

	function removeClassesWithPrefix( el, prefix ) {
		var classes = el.className.split( ' ' ).filter( function ( c ) {
			return c.lastIndexOf( prefix, 0 ) !== 0;
		} );
		// eslint-disable-next-line mediawiki/class-doc
		el.className = classes.join( ' ' ).trim();
	}

	/*
	 * The display control options:
	 *  * serif/sans serif fonts
	 */
	var display = ( function () {

		// My kingdom for Vuex
		var State = {
			serif: false,

			cache: {}
		};

		function updateSerifs() {
			// tthe relevant class on the content
			containers.$page.toggleClass( 'ws-display-serif', State.serif );

			var msg = mw.msg( State.serif ? 'use_sans_serif' : 'use_serif' );
			State.cache.$serifSwitch.children( 'a' ).html( msg );
		}

		function init() {

			State.cache.$serifSwitch = $( mw.util.addPortletLink(
				'p-do', '#', '', 'd-serif', mw.msg( 'serif_text_title' ) )
			)
				.on( 'click', function () {
					State.serif = !State.serif;
					updateSerifs();
					saveSetting( 'ws-display-serif', State.serif );
				} );

			State.serif = initSetting( 'ws-display-serif', false );
			updateSerifs();
		}

		return {
			init: init
		};
	}() );

	var layout = ( function () {
			var State = {
				allow_default: true,
				default_applied: false,
				layout_name: 'Layout 1',
				layouts: [],
				cache: {}
			};

			function set_by_name( name ) {

				var selected_layout;
				for ( var i = 0; i < State.layouts.length; ++i ) {
					if ( State.layouts[ i ].name === name ) {
						selected_layout = State.layouts[ i ];
					}
				}

				if ( !selected_layout ) {
					return; // does not exist
				}

				State.layout_name = selected_layout.name;

				var layoutText = selected_layout.name;
				if ( State.allow_default && State.default_applied ) {
					layoutText += ' (' + mw.msg( 'default_layout_suffix' ) + ')';
				}

				State.cache.$layoutSwitch.children( 'a' ).html( layoutText );

				removeClassesWithPrefix( $classedContainer[ 0 ], 'dynlayout-' );
				// eslint-disable-next-line mediawiki/class-doc
				$classedContainer.addClass( 'dynlayout-' + selected_layout.id );

				pagenumbers.refresh_offsets();
			}

			function updateLayout() {
				var name;
				State.default_applied = false;
				if ( State.allow_default || !mw.cookie.get( 'layout' ) ) {
					var overrider = State.cache.$overrider || $classedContainer.find( '#dynamic_layout_overrider' );
					name = overrider.text();
					if ( name ) {
						State.default_applied = true;
					}
				} else {
					name = State.layout_name || mw.cookie.get( 'layout' );
				}
				set_by_name( name || State.layout_name );
			}

			function set_by_number( number ) {
				State.layout_name = State.layouts[ number ].name;
				updateLayout();
			}

			function getLayoutIndexWithName( name ) {
				for ( var i = 0; i < State.layouts.length; ++i ) {
					if ( State.layouts[ i ].name === name ) {
						return i;
					}
				}

				return -1;
			}

			function toggle() {
				var cur = getLayoutIndexWithName( State.layout_name );

				// disable override for this page only (no persistence)
				State.allow_default = false;

				set_by_number( ( cur + 1 ) % State.layouts.length );

				// store the changed layout
				mw.cookie.set( 'layout', State.layout_name );
			}

			function updateDefault() {
				var msg = mw.msg( State.allow_default ? 'default_layout_on' : 'default_layout_off' );
				State.cache.$defaultLayoutSwitch.children( 'a' ).html( msg );

				updateLayout();
			}

			function init() {
				// do return if we're already set up
				if ( document.getElementById( 'pageContainer' ) ) {
					return;
				}

				// collect any user or other gadget layouts
				mw.hook( 'ws.layouts.register' ).fire( {
					layouts: standard_layouts
				} );
// XOWA HACK
layouts = standard_layouts
// end XOWA HACK

				State.layouts = standard_layouts;

				// If cookie is not set, default layout is first available option.
				// Use index "0" in case layout name is ever changed.
				State.layout_name = initSetting( 'layout', '0' );

				State.allow_default = initSetting( 'ws-display-default-layouts', true );

				State.cache.$layoutSwitch = $( mw.util.addPortletLink(
					'p-do',
					'#',
					mw.msg( 'layout' ),
					'd-textLayout',
					'The designation of the dynamic layout being applied',
					'l',
					'#d-defaultLayouts'
				) )
					.on( 'click', function ( e ) {

						e.preventDefault();
						toggle();
					} );

				State.cache.$defaultLayoutSwitch = $( mw.util.addPortletLink(
					'p-do',
					'#',
					'',
					'd-defaultLayouts',
					mw.msg( 'default_layout_title' )
				) )
					.on( 'click', function ( e ) {
						State.allow_default = !State.allow_default;

						// if we just turned the default off, use the cookie value
						if ( !State.allow_default ) {
							State.layout_name = mw.cookie.get( 'layout' );
						}

						updateDefault();
						saveSetting( 'ws-display-default-layouts', State.allow_default );
						e.preventDefault();
					} );

				// remove all these classes to maintain backwards-compatibility
				$classedContainer
					.find( 'div.text, .lefttext, .centertext, .indented-page, .prose' )
					.removeClass();

				// DynamicFlaw - a independent Div should have been the parent
				// to this 3-into-1 step
				var $parserOutput = $classedContainer.find( '.mw-parser-output' )
					.contents()
					.not( '.dynlayout-exempt' )
					.wrapAll( $( '<div>' )
						.attr( 'id', 'pageContainer' )
						.append( $( '<div>' )
							.attr( 'id', 'regionContainer' )
							.append( $( '<div>' )
								.attr( 'id', 'columnContainer' )
							)
						)
					);

				// cache the containers
				containers.$column = $parserOutput.parent();
				containers.$region = containers.$column.parent();
				containers.$page = containers.$region.parent();

				// If layouts have changed, the cookie might refer to a missing layout
				// in which case, set the first one
				if ( getLayoutIndexWithName( State.layout_name ) === -1 ) {
					set_by_number( 0, true );
				}

				// set the layout by default (override) layout, or from the user's setting
				updateDefault();

				mw.hook( 'ws.layouts.ready' ).fire();
// XOWA HACK
pagenumbers.doInit();
// end XOWA HACK
			}

			return {
				init: init
			};
		}() ),

		pagenumbers = ( function () {

			// some shared variables to avoid selecting these elements repeatedly
			var $div_pagenumbers,
				dp_y,
				y_prev,
				$pagenumbers_collection,
				$div_ss,
				$div_highlight,

				show_params = {
					link_text: mw.msg( 'page_numbers_displayed' ),
					visible: true
				},
				hide_params = {
					link_text: mw.msg( 'page_numbers_hidden' ),
					visible: false
				};

			function pagenum_in() {
				if ( self.proofreadpage_disable_highlighting ) {
					return false;
				}
				if ( !$div_highlight ) {
					return false; // could not find it
				}
				var id = this.id.substring( 11 ),

					$page_span = $( document.getElementById( id ) ),
					$next = self.$pagenum_ml.eq( self.$pagenum_ml.index( $page_span ) + 1 );
				if ( $next.length === 0 ) {
					$next = $div_ss;
				}

				var $container = containers.$column;

				// we need to use document offsets in case a page break occurs within
				// a positioned element
				var c_os = $container.offset(),
					ps_os = $page_span.offset(),
					n_os = $next.offset();

				ps_os = {
					top: ps_os.top - c_os.top,
					left: ps_os.left - c_os.left
				};
				n_os = {
					top: n_os.top - c_os.top,
					left: n_os.left - c_os.left
				};

				$div_highlight.css( {
					display: 'block',
					top: ps_os.top + 'px'
				} );
				$div_highlight.children().eq( 0 ).css( {
					height: $page_span.height() + 'px',
					width: ( ps_os.left < 1 ) ? '100%' : ( ( $container.width() - ps_os.left ) + 'px' )
				} );
				// div_ss.height() ~= height of 1 line of text
				$div_highlight.children().eq( 1 ).css( 'height', ( n_os.top - ps_os.top - $page_span.height() ) + 'px' );
				$div_highlight.children().eq( 2 ).css( {
					height: $next.height() + 'px',
					width: n_os.left + 'px'
				} );
				return true;
			}

			function pagenum_out() {
				if ( self.proofreadpage_disable_highlighting ) {
					return false;
				}
				if ( !$div_highlight ) {
					return false; // could not find it
				}
				$div_highlight.css( 'display', 'none' );
				$div_highlight.children().eq( 0 ).css( 'width', '0px' );
				$div_highlight.children().eq( 1 ).css( 'height', '0px' );
				$div_highlight.children().eq( 2 ).css( 'width', '0px' );
				return true;
			}

			function refresh_elem_offset( page_span, $pagenumber ) {
				var y = $( page_span ).offset().top;
				$pagenumber.css( 'top', y - dp_y );
				if ( self.proofreadpage_numbers_visible && y - y_prev.val > 5 ) {
					y_prev.val = y;
					$pagenumber.removeClass( 'pagenumber-invisible' );
				} else {
					$pagenumber.addClass( 'pagenumber-invisible' );
				}
			}

			function refresh_offsets() {
				// do nothing if container is not set up
				if ( self.proofreadpage_numbers_inline || !$div_pagenumbers ) {
					return false;
				}

				dp_y = $div_pagenumbers.offset().top;
				y_prev = {
					val: -10
				};

				var $pagenumber = $pagenumbers_collection.first();

				self.$pagenum_ml.each( function ( i, page_span ) {
					refresh_elem_offset( page_span, $pagenumber );
					$pagenumber = $pagenumber.next();
				} );

				return true;
			}

			var inline_params = {
					elem: 'span',
					link_pre: '&#x0020;[',
					link_post: ']'
				},
				beside_params = {
					elem: 'div',
					link_pre: '[',
					link_post: ']'
				};

			function setup_elem( i, page_span ) {
				var params = self.proofreadpage_numbers_inline ? inline_params : beside_params,

					// styled also by classes: div.pagenumber or span.pagenumber
					$pagenumber = $( '<' + params.elem + '>' )
						.attr( 'id', $.data( page_span, 'pagenumber_id' ) )
						.addClass( 'pagenumber noprint' )
						.append( params.link_pre + $.data( page_span, 'link_str' ) + params.link_post )
						.toggleClass( 'pagenumber-invisible', !self.proofreadpage_numbers_visible );

				if ( !self.proofreadpage_numbers_inline ) {
					refresh_elem_offset( page_span, $pagenumber );
				}

				// clear the span provided by [[MediaWiki:Proofreadpage pagenum template]]
				$( page_span ).find( '.pagenum-inner' ).empty();

				$pagenumber.appendTo(
					self.proofreadpage_numbers_inline ? page_span : $div_pagenumbers );

				$pagenumbers_collection = $pagenumbers_collection.add( $pagenumber );
			}

			function init_elem( i, page_span ) {
				var name = page_span.getAttribute( 'data-page-number' ) || page_span.id,

					// what if two pages have the same number? increment the id
					pagenumber_id = 'pagenumber_' + page_span.id,
					count;

				if ( $pagenumbers_collection.is( '#' + $.escapeSelector( pagenumber_id ) ) ) {
					count = ( $pagenumbers_collection.filter( "[id ^= '" + pagenumber_id + "']" ).length + 1 );
					page_span.id += ( '_' + count );
					pagenumber_id += ( '_' + count );
				}

				if ( !page_span.title ) {
					// there's no page to link to - just set plain text
					$.data( page_span, 'link_str', mw.html.escape( name ) );
				} else {
					$.data( page_span, 'pagenumber_id', pagenumber_id );
					var page_title = decodeURI( page_span.title ).replace( /%26/g, '&' ).replace( /%3F/g, '?' ),
						page_url =
				mw.config.get( 'wgArticlePath' )
					.replace( '$1', encodeURIComponent( page_title.replace( / /g, '_' ) ) )
				// encodeURIComponent encodes '/', which breaks subpages
					.replace( /%2F/g, '/' ),

						// if transcluded Page: (ll) is a redlink then make page class
						// (class_str) a redlink also
						ll = page_span.parentNode.nextSibling,
						class_str = '',
						action_str = '';

					if ( ll && ll.tagName === 'A' && ll.className === 'new' ) {
						class_str = ' class="new" ';
						action_str = '?action=edit&redlink=1';
					}

					$.data(
						page_span,
						'link_str',
						'<a href= "' + page_url + action_str + '"' +
				class_str +
				' title= "' + mw.html.escape( page_title ) + '">' +
				mw.html.escape( name ) +
				'</a>'
					);
				}

				setup_elem( i, page_span );
			}

			function refresh_display() {
				// determine if we need to set things up
				var inited = !$pagenumbers_collection;

				// JQuery collection of all pagenumber elements
				if ( !inited ) {
					$pagenumbers_collection.remove();
				}
				$pagenumbers_collection = $();

				if ( $div_pagenumbers ) {
					$div_pagenumbers.remove();
				}

				if ( !self.proofreadpage_numbers_inline ) {
					// html div container for page numbers stored in shared variable div_pagenumbers

					//  put pagenumbers container div in the outermost layout container
					$div_pagenumbers = $( '<div>' )
						.attr( 'id', 'ct-pagenumbers' )
						.appendTo( containers.$page );
					dp_y = $div_pagenumbers.offset().top;
					y_prev = {
						val: -10
					};
				}
				self.$pagenum_ml.each( inited ? init_elem : setup_elem );

				if ( self.proofreadpage_numbers_inline ) {
					$pagenumbers_collection.off( 'mouseenter mouseleave' );
				} else {
					$pagenumbers_collection.on( {
						mouseenter: pagenum_in,
						mouseleave: pagenum_out
					} );
				}
			}

			function toggle_visible() {
				var params = self.proofreadpage_numbers_visible ? hide_params : show_params;

				$pagenumbers_collection.toggleClass( 'pagenumber-invisible', !params.visible );
				$( '#d-pageNumbers_visible' ).children( 'a' ).html( params.link_text );
				self.proofreadpage_numbers_visible = params.visible;
				mw.cookie.set( 'pagenums_visible', params.visible );
			}

			function toggle_inline() {
				// toggle inline view unless layouts are not set up
				self.proofreadpage_numbers_inline = !layout || !self.proofreadpage_numbers_inline;
				$( '#d-pageNumbers_inline' ).children( 'a' )
					.html( mw.msg( self.proofreadpage_numbers_inline ? 'page_numbers_inline' : 'page_numbers_beside' ) );
				mw.cookie.set( 'pagenums_inline', self.proofreadpage_numbers_inline );
				refresh_display();
			}

			function doInit() {
				// Mark the container as having pagenumbers.
				// Some layouts can use that information.
				$( containers.$page )
					.addClass( 'dynlayout-haspagenums' );

				// get_optlist();
				self.proofreadpage_numbers_visible = initSetting( 'pagenums_visible', true );
				var portletLink = mw.util.addPortletLink(
					'p-do',
					'#',
					self.proofreadpage_numbers_visible ? mw.msg( 'page_numbers_displayed' ) : mw.msg( 'page_numbers_hidden' ),
					'd-pageNumbers_visible',
					'The current state of embedded link visibility',
					'n',
					'#d-serif'
				);
				$( portletLink ).on( 'click', function ( e ) {
					e.preventDefault();
					toggle_visible();
				} );

				self.proofreadpage_numbers_inline = initSetting( 'pagenums_inline', false );

				// if layouts are not initialized show pagenumbers inline since
				// "beside" view won't work
				if ( !layout ) {
					self.proofreadpage_numbers_inline = true;
				}

				portletLink = mw.util.addPortletLink(
					'p-do',
					'#',
					self.proofreadpage_numbers_inline ? mw.msg( 'page_numbers_inline' ) : mw.msg( 'page_numbers_beside' ),
					'd-pageNumbers_inline',
					'The current positioning used for embedded link presentation',
					'i',
					'#d-pageNumbers_visible'
				);

				$( portletLink ).on( 'click', function ( e ) {
					e.preventDefault();
					toggle_inline();
				} );

				// store container for the highlight to shared variable "div_highlight"
				$div_highlight = $( '<div id= "highlight-area">' +
					'<div style="float:right; width:0px;"><div class= "clearFix"></div></div>' +
					'<div style="width:100%; height:0px; clear:both;"></div>' +
					'<div style="width:0px;"><div class= "clearFix" style= "float:left; clear:both;"></div></div>' +
					'</div>'
				);

				// assign new div element to shared variable "div_ss"
				$div_ss = $( '<div id= "my-ss"><div class= "clearFix"></div></div>' ); // empty span following some text

				// put divs in the innermost dynamic layout container
				if ( layout ) {
					containers.$column
						.append( $div_highlight );
					$classedContainer.append( $div_ss );
				} else {
					$classedContainer.append( $div_highlight, $div_ss );
				}

				self.$pagenum_ml = $classedContainer.find( '.pagenum' );
				refresh_display();
			}

			function init() {
				// skip if pagenumbers are already set up
				if ( $pagenumbers_collection ) {
					return false;
				}

				// wait for the layouts code to signal that the containers are ready
				mw.hook( 'ws.layouts.ready' ).add( function () {
					doInit();
				} );
			}

			return {
				init: init,
				refresh_offsets: refresh_offsets
// XOWA HACK
				,doInit: doInit
// end XOWA HACK
			};
		}() );

	if ( [ 'view', 'submit', 'purge' ].indexOf( mw.config.get( 'wgAction' ) ) !== -1 ) {
		if ( !self.debug_page_layout &&
			// don't do anything on DoubleWiki or difference comparison views
			document.URL.indexOf( 'match=' ) === -1 ) {

			layout.init();
			display.init();

			$( function () {
				if ( $classedContainer.find( '.pagenum' ).length ) {
					pagenumbers.init();

					if ( document.readyState === 'complete' ) {
						$( pagenumbers.refresh_offsets );
					} else {
						$( window ).on( 'load', pagenumbers.refresh_offsets );
					}
				}
			} );

			// Add a "what's this" helper to display options
			// eslint-disable-next-line no-jquery/no-global-selector
			$( '#p-do-label' ).append( $( '<span>' )
				.css( { float: 'right' } )
				.append( $( '<a>' )
					.attr( {
						href: '/wiki/Help:Layout',
						title: mw.msg( 'what_is_this_title' )
					} )
					.append( mw.msg( 'what_is_this_symbol' ) )
				)
			);
		}
		var position = window.location.hash.substring( 1 );
		if ( position && document.getElementById( position ) ) {
			document.getElementById( position ).scrollIntoView();
		}

		/**
		 * Install the DOM-ready hook to force header and footer content out of
		 * Dynamic Layouts
		 */
		$( function () {

			var $c = $classedContainer;

			$c.find( '.acContainer' ).insertAfter( $c.find( 'div.printfooter' ) );

			$( '<div>' )
				.addClass( 'dynlayout-exempt dynlayout-exempt-footer' )
				.insertBefore( 'div#catlinks' )
				.append( $c.find( '.acContainer' ) )
				.append( $c.find( 'div.licenseContainer' ).not( 'div.licenseContainer div.licenseContainer' ) )
				.append( $c.find( '#editform' ) )
				.append( $c.find( '#footertemplate' ) );

			$( '<div>' )
				.addClass( 'dynlayout-exempt dynlayout-exempt-header' )
				.insertBefore( containers.$page )
				.prepend( $c.find( 'div#headerContainer' ) )
				.prepend( $c.find( 'div#heederContainer' ) )
				.prepend( $c.find( 'div#heedertemplate' ) )
				.prepend( $c.find( '#mw-previewheader' ) );
		} );
	}

/* eslint-disable-next-line no-undef */
}( mediaWiki, jQuery ) );
