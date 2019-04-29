/* Any JavaScript here will be loaded for all users on every page load. */
var mw=mediaWiki;
var importScript = '';
/* deprecated */
var	hasClass = function(e, c) { return $(e).hasClass(c); },
	import_script = importScript;


/* XOWA MediaWiki:Navigation.js */
/** Collapsible tables *********************************************************
 *
 *  Description: Allows tables to be collapsed, showing only the header. See
 *                         http://www.mediawiki.org/wiki/Manual:Collapsible_tables.
 *  Maintainers: [[en:User:R. Koot]]
 */
 
var autoCollapse = 2;
var collapseCaption = 'hide';
var expandCaption = 'show';
 
function collapseTable( tableIndex ) {
	var Button = document.getElementById( 'collapseButton' + tableIndex );
	var Table = document.getElementById( 'collapsibleTable' + tableIndex );

	if ( !Table || !Button ) {
		return false;
	}

	var Rows = Table.rows;
	var i;
	var $row0 = $(Rows[0]);

	if ( Button.firstChild.data === collapseCaption ) {
		for ( i = 1; i < Rows.length; i++ ) {
			Rows[i].style.display = 'none';
		}
		Button.firstChild.data = expandCaption;
	} else {
		for ( i = 1; i < Rows.length; i++ ) {
			Rows[i].style.display = $row0.css( 'display' );
		}
		Button.firstChild.data = collapseCaption;
	}
}
 
function createCollapseButtons() {
        var tableIndex = 0;
        var NavigationBoxes = {};
        var Tables = document.getElementsByTagName( 'table' );
 
        for ( var i = 0; i < Tables.length; i++ ) {
                if ( hasClass( Tables[i], 'collapsible' ) ) {
 
                        /* only add button and increment count if there is a header row to work with */
                        var HeaderRow = Tables[i].getElementsByTagName( 'tr' )[0];
                        if ( !HeaderRow ) {
                                continue;
                        }
                        var Header = HeaderRow.getElementsByTagName( 'th' )[0];
                        if ( !Header ) {
                                continue;
                        }
 
                        NavigationBoxes[tableIndex] = Tables[i];
                        Tables[i].setAttribute( 'id', 'collapsibleTable' + tableIndex );
 
                        var Button = document.createElement( 'span' );
                        var ButtonLink = document.createElement( 'a' );
                        var ButtonText = document.createTextNode( collapseCaption );
 
                        Button.className = 'collapseButton'; // Styles are declared in [[MediaWiki:Common.css]]
 
                        ButtonLink.style.color = Header.style.color;
                        ButtonLink.setAttribute( 'id', 'collapseButton' + tableIndex );
                        ButtonLink.setAttribute( 'href', "javascript:collapseTable(" + tableIndex + ");" );
                        ButtonLink.appendChild( ButtonText );
 
                        Button.appendChild( document.createTextNode( '[' ) );
                        Button.appendChild( ButtonLink );
                        Button.appendChild( document.createTextNode( ']' ) );
 
                        Header.insertBefore( Button, Header.childNodes[0] );
                        tableIndex++;
                }
        }
 
        for ( var i = 0;  i < tableIndex; i++ ) {
                if ( hasClass( NavigationBoxes[i], 'collapsed' ) || ( tableIndex >= autoCollapse && hasClass( NavigationBoxes[i], 'autocollapse' ) ) ) {
                        collapseTable( i );
                } else if ( hasClass( NavigationBoxes[i], 'innercollapse' ) ) {
                        var element = NavigationBoxes[i];
                        while ( element = element.parentNode ) {
                                if ( hasClass( element, 'outercollapse' ) ) {
                                        collapseTable( i );
                                        break;
                                }
                        }
                }
        }
}
 
$( createCollapseButtons );

// Faster Dynamic Navigation
 
// set up the words in your language
var NavigationBarHide = '▲';
var NavigationBarShow = '▼';
var NavigationTitleHide = 'hide contents';
var NavigationTitleShow = 'show contents';
 
// adds show/hide-button to collapsible navigation
function collapsible_navigation()
{
	$('div.NavFrame').each( function() {
		var	$that = $(this), css_width = $that.css( 'width' ), attr_width = $that.attr( 'width' ),
			which = $that.hasClass( 'selected' ) ? NavigationBarShow : NavigationBarHide;

		if ( (!css_width || css_width === 'auto') && (!attr_width || attr_width === 'auto') ) {
			$that.css( 'width', $that.width() );
		}

		$that.children('.NavHead').each( function() {
			$(this).append( '<a class="NavToggle">' + which + '</a>' ).click( function() {
				var which = $that.toggleClass('selected').hasClass( 'selected' ), $this = $(this);

				$this.find( '.NavToggle' ).text( which ? NavigationBarShow : NavigationBarHide );

				if ( which ) {
					$this.attr( 'title', NavigationTitleShow )
						.siblings( ':not(.NavHead)' ).stop( true, true ).fadeOut();
				} else {
					$this.attr( 'title', NavigationTitleHide )
						.siblings( ':not(.NavHead)' ).stop( true, true ).fadeIn();
				}
			}).click();
		});
	});
}

$(document).ready( collapsible_navigation );
/* XOWA MediaWiki:Sidebar.js */
//Modify Sidebar.  Code based on example at https://www.mediawiki.org/wiki/Manual:Interface/Sidebar.

function ModifySidebar( action, section, name, link ) {
	try {
		switch ( section ) {
			case 'languages':
				var target = 'p-lang';
				break;
			case 'toolbox':
				var target = 'p-tb';
				break;
			case 'navigation':
				var target = 'p-navigation';
				break;
			default:
				var target = 'p-' + section;
				break;
		}

		if ( action == 'add' ) {
			var node = document.getElementById( target )
							   .getElementsByTagName( 'div' )[0]
							   .getElementsByTagName( 'ul' )[0];

			var aNode = document.createElement( 'a' );
			var liNode = document.createElement( 'li' );

			aNode.appendChild( document.createTextNode( name ) );
			aNode.setAttribute( 'href', link );
			liNode.appendChild( aNode );
			liNode.className = 'plainlinks';
			node.appendChild( liNode );
		}

		if ( action == 'remove' ) {
			var list = document.getElementById( target )
							   .getElementsByTagName( 'div' )[0]
							   .getElementsByTagName( 'ul' )[0];

			var listelements = list.getElementsByTagName( 'li' );

			for ( var i = 0; i < listelements.length; i++ ) {
				if (
					listelements[i].getElementsByTagName( 'a' )[0].innerHTML == name ||
					listelements[i].getElementsByTagName( 'a' )[0].href == link
				)
				{
					list.removeChild( listelements[i] );
				}
			}
		}

	} catch( e ) {
		// let's just ignore what's happened
		return;
	}
}

function CustomizeModificationsOfSidebar() {
	if(mw.config.get( 'wgNamespaceNumber') == 0) //Add for main namespace only.
	{
		ModifySidebar( 'add', 'toolbox', 
			'Cite this page', 'https://en.wikiversity.org/wiki/Special:CiteThisPage?page=' + 
			mw.config.get( 'wgPageName' ) );
	}
}

//Code is loading without the jQuery call.
//CusomizeModificationsOfSidebar must be called somewhere else.
//jQuery( CustomizeModificationsOfSidebar );
/* XOWA MediaWiki:EditToolbar.js */
if (mw.toolbar && mw.toolbar.addButtons) {
	mw.toolbar.addButtons({
    "imageFile": "//upload.wikimedia.org/wikipedia/en/c/c8/Button_redirect.png",
    "speedTip": "Redirect",
    "tagOpen": "#REDIRECT [[",
    "tagClose": "]]",
    "sampleText": "Insert text"
   }, {
    "imageFile": "//upload.wikimedia.org/wikipedia/commons/8/88/Btn_toolbar_enum.png",
    "speedTip": "# list",
    "tagOpen": "#",
    "tagClose": "\n",
    "sampleText": "list text"
   }, {
    "imageFile": "//upload.wikimedia.org/wikipedia/commons/1/11/Btn_toolbar_liste.png",
    "speedTip": "* item",
    "tagOpen": "*",
    "tagClose": "\n",
    "sampleText": "item text"
   }, {
    "imageFile": "//upload.wikimedia.org/wikipedia/commons/b/b4/Button_category03.png",
    "speedTip": "Category",
    "tagOpen": "[[Category:",
    "tagClose": wgPageName + "]]",
    "sampleText": "insert category"
   }, {
    "imageFile": "//upload.wikimedia.org/wikipedia/commons/3/3b/Button_template_alt.png",
    "speedTip": "{{Template}}",
    "tagOpen": "{{",
    "tagClose": "}}",
    "sampleText": "sofixit"
   }, {
    "imageFile": "//upload.wikimedia.org/wikipedia/commons/f/fd/Button_underline.png",
    "speedTip": "Underline",
    "tagOpen": "<u>",
    "tagClose": "</u>",
    "sampleText": "underline text"
   }, {
     "imageFile": "//upload.wikimedia.org/wikipedia/en/c/c9/Button_strike.png",
     "speedTip": "Strike",
     "tagOpen": "<s>",
     "tagClose": "</s>",
     "sampleText": "strikeout text"
   }, {
     "imageFile": "//upload.wikimedia.org/wikipedia/en/8/80/Button_upper_letter.png",
     "speedTip": "Superscript",
     "tagOpen": "<sup>",
     "tagClose": "</sup>",
     "sampleText": "Superscript text"
   }, {
     "imageFile": "//upload.wikimedia.org/wikipedia/en/7/70/Button_lower_letter.png",
     "speedTip": "Subscript",
     "tagOpen": "<sub>",
     "tagClose": "</sub>",
     "sampleText": "Subscript text"
   }, {
     "imageFile": "//upload.wikimedia.org/wikipedia/commons/1/17/Button_small_2.png",
     "speedTip": "small text",
     "tagOpen": "<small>",
     "tagClose": "</small>",
     "sampleText": "Small Text"
   }, {
     "imageFile": " //upload.wikimedia.org/wikipedia/commons/5/56/Button_big.png",
     "speedTip": "BIG text",
     "tagOpen": "<big>",
     "tagClose": "</big>",
     "sampleText": "big"
   }, {
     "imageFile": "//upload.wikimedia.org/wikipedia/commons/7/79/Button_reflink.png",
     "speedTip": "<ref>",
     "tagOpen": "<ref>",
     "tagClose": "</ref>",
     "sampleText": "Insert reference material"
   }, {
     "imageFile":  "//upload.wikimedia.org/wikipedia/commons/a/a0/Button_references_alt.png",
     "speedTip": "Reference footer",
     "tagOpen": "<references/>",
     "tagClose": "",
     "sampleText": ""
   }, {
     "imageFile": "//upload.wikimedia.org/wikipedia/commons/f/fd/Button_blockquote.png",
     "speedTip": "block of quoted text",
     "tagOpen": "<blockquote>\n",
     "tagClose": "\n</blockquote>",
     "sampleText": "Block quote"
   }, {
     "imageFile":  "//upload.wikimedia.org/wikipedia/commons/e/ea/Button_align_left.png",
     "speedTip": "align left",
     "tagOpen": "<p style=\"text-align:left;\">",
     "tagClose": "</p>",
     "sampleText": "left text"
   }, {
     "imageFile":  "//upload.wikimedia.org/wikipedia/commons/5/5f/Button_center.png",
     "speedTip": "align center",
     "tagOpen": "<p style=\"text-align:center;\">",
     "tagClose": "</p>",
     "sampleText": "center text"
   }, {
     "imageFile":  "//upload.wikimedia.org/wikipedia/commons/a/a5/Button_align_right.png",
     "speedTip": "align right",
     "tagOpen": "<p style=\"text-align:right;\">",
     "tagClose": "</p>",
     "sampleText": "right text"
   });
}
/* XOWA MediaWiki:Displayname.js */
function change_displaytitle()
{
	var text = $( '#displaytitle' ).attr( 'title' ), what;
 
	if ( text ) {
		text = text.match( "(?:tab:\\s*([^|]+)\\|?)?\\s*(?:title:\\s*(.+))?" );
	}
 
	if ( !text ) {
		return;
	}
 
	if ( text[1] ) {
		what = $("#ca-nstab-" + ( mw.config.get('wgCanonicalNamespace').toLowerCase() || 'main' ) );
		what.find('a').text(text[1]);
	}
 
	if ( text[2] ) {
		what = $('h1');
 
		if ( what.hasClass('firstHeading') || what.hasClass('pagetitle') ) {
			what.text(text[2]);
		}
	}
}
 
$(document).ready(change_displaytitle);
/* XOWA MediaWiki:Tables.js */
$(document).ready( function($) {
	$('table.table').each( function() {
		var $table = $(this), c = 0;

		$table.find('> tr, > tbody > tr').each( function() {
			var $row = $(this);

			if ( $row.children('th').length ) {
				c = 0;
				return true;
			}

			if ( c % 2 ) {
				$row.addClass( 'odd' );
			} else {
				$row.addClass( 'even' );
			}

			++c;
		});
	});
});
/* XOWA MediaWiki:DebateTree.js */
/**
 * Initialization script for DebateTree
 * Documentation at https://commons.wikimedia.org/wiki/Help:DebateTree
 */

// Only load on debate pages
if ( $( '.debatetree' ).length ) {

	// Load the CSS directly from Commons
	mw.loader.load( '//commons.wikimedia.org/w/index.php?title=MediaWiki:DebateTree.css&action=raw&ctype=text/css', 'text/css' );

	// Only load the script when viewing or previewing
	var action = mw.config.get( 'wgAction' );
	if ( action === 'view' || action === 'submit' ) {
		mw.loader.load( '//commons.wikimedia.org/w/index.php?title=MediaWiki:DebateTree.js&action=raw&ctype=text/javascript' );
	}
}
/* XOWA MediaWiki:Common.js/WatchlistNotice.js */
/*
 *  Description: Hide the watchlist message for one week.
 *  Maintainers: [[w:User:Ruud Koot|Ruud Koot]]
 *  Updated by:  [[User:Darklama|darklama]]
 */
 
function addDismissButton() {
	var $watchlistMessage = $('#watchlist-message');
 
	if ( !$watchlistMessage.length ) return;
	if ( $.cookie('hidewatchlistmessage') === 'yes' ) {
		$watchlistMessage.hide();
		return;
	}
 
	$('<span>[<a id="dismissButton" title="Hide this message for one week">dismiss</a>]</span>')
		.appendTo( $watchlistMessage )
		.click( function() {
			$.cookie('hidewatchlistmessage', 'yes', { 'expires': 7, 'path': '/' });
			$watchlistMessage.hide();
		});
}
 
if ( mw.config.get('wgCanonicalSpecialPageName') === 'Watchlist') $(document).ready( addDismissButton );
/* XOWA MediaWiki:Common.js/Slideshows.js */
$.fn.slideshow = ( function() {
	return this.each( function() {
		var $ss = $(this), $sl = $ss.children( '.slide' ), $actions;

		if ( $sl.length < 2 ) {
			return;
		}

		$sl.slice(1).hide();
		$actions = $('<div class="slide-actions"><span class="slide-prev"></span><span class="slide-next"></span></div>');
		$ss.data( 'slides', { 'at': 0, 'total': $sl.length }).append( $actions ).click( function(e) {
			var $where = $( e.target ), $ss, $sl, data;

			if ( $where.is( '.slide-prev' ) ) {
				e.stopPropagation();
				$ss = $(this); $sl = $ss.children( '.slide' ); data = $ss.data( 'slides' );
				if ( data.at > 0 ) {
					--data.at;
					$sl.eq( data.at + 1).fadeOut(1000).end().eq( data.at ).delay(1000).fadeIn(1000);
					$ss.data( 'slides', data );
				}
			} else if ( $where.is( '.slide-next' ) ) {
				e.stopPropagation();
				$ss = $(this); $sl = $ss.children( '.slide' ); data = $ss.data( 'slides' );
				if ( data.at < data.total - 1 ) {
					++data.at;
					$sl.eq( data.at - 1).fadeOut(1000).end().eq( data.at ).delay(1000).fadeIn(1000);
					$ss.data( 'slides', data );
				}
			}
		});
	});
});

$(document).ready( function() { $( '.slides' ).slideshow(); } );
/* XOWA MediaWiki:Common.js/addin-mooc.js */


/**
 * @source https://www.mediawiki.org/wiki/Snippets/Load_JS_and_CSS_by_URL
 * @revision 2016-03-26
 */
/*
mw.loader.using( ['mediawiki.util', 'mediawiki.notify'], function () {
	var extraCSS = mw.util.getParamValue( 'withCSS' ),
		extraJS = mw.util.getParamValue( 'withJS' ),
		extraModule = mw.util.getParamValue( 'withModule' );

	if ( extraCSS ) {
		if ( /^MediaWiki:[^&<>=%#]*\.css$/.test( extraCSS ) ) {
			mw.loader.load( '/w/index.php?title=' + extraCSS + '&action=raw&ctype=text/css', 'text/css' );
		} else {
			mw.notify( 'Only pages from the MediaWiki namespace are allowed.', { title: 'Invalid withCSS value' } );
		}
	}

	if ( extraJS ) {
		if ( /^MediaWiki:[^&<>=%#]*\.js$/.test( extraJS ) ) {
			mw.loader.load( '/w/index.php?title=' + extraJS + '&action=raw&ctype=text/javascript' );
		} else {
			mw.notify( 'Only pages from the MediaWiki namespace are allowed.', { title: 'Invalid withJS value' } );
		}
	}

	if ( extraModule ) {
		if ( /^ext\.gadget\.[^,\|]+$/.test( extraModule ) ) {
			mw.loader.load( extraModule );
		} else {
			mw.notify( 'Only gadget modules are allowed.', { title: 'Invalid withModule value' } );
		}
	}
});
*/