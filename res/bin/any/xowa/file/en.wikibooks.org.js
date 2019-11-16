var mw = mediaWiki;
// importScript('MediaWiki:Common.js/Relics.js');
// This page is for keeping track of JavaScript that may no longer be useful/functional someday.
/*
mw.log.deprecate(window, 'addLoadEvent', function(fn) {
	jQuery.ready(fn);
}, 'Use jQuery.ready instead.' );

window.import_script = function(name) {
	mw.loader.load(
		mw.config.get('wgServer')
		+ mw.config.get('wgScript')
		+ '?title=' + mw.util.wikiUrlencode(name)
		+ '&action=raw&ctype=text/javascript'
	);
};

window.import_style = function(name) {
	mw.loader.load(
		mw.config.get('wgServer')
		+ mw.config.get('wgScript')
		+ '?title=' + mw.util.wikiUrlencode(name)
		+ '&action=raw&ctype=text/css'
	);
};

// Removes the default no-license option for uploads.
function remove_no_license()
{
	if ( mw.config.get('wgCanonicalSpecialPageName') !== 'Upload' )
		return;
	$('#wpLicense').find('option').eq(0).remove();
}

$(document).ready(remove_no_license);
*/
// importScript('MediaWiki:Common.js/ExtraTools.js');
(function(mw, $) {
	window.add_toolbox_link = function(action, name, id) {
		var $tools = $('#p-tb ul');
		if ( !$tools.length ) return;
		if (typeof action === "string") {
			$tools.append('<li id="t-' + (id || name) + '"><a href="' + action + '">' + name + '</a></li>');
		} else if (typeof action === "function") {
			$('<li id="t-' + (id || name) + '"><a href="#">' + name + '</a></li>').appendTo($tools).click(action);
		}
	};
	mw.hook('wikibooks.panels.tools').fire(mw, $);
})(mediaWiki, jQuery);

// importScript('MediaWiki:Common.js/CollapseElements.js');
//<source lang="javascript">
// Faster Collapsible Containers
// Maintainer: [[User:Darklama]]

// images to use for hide/show states
var collapse_action_hide = '//upload.wikimedia.org/wikipedia/commons/1/10/MediaWiki_Vector_skin_action_arrow.png';
var collapse_action_show = '//upload.wikimedia.org/wikipedia/commons/4/41/MediaWiki_Vector_skin_right_arrow.png';
 
// toggle state of collapsible boxes
function collapsible_boxes()
{
	$('div.collapsible').each( function() {
		var $that = $(this), css_width = $that.css('width'), attr_width = $that.attr('width');
		var which = $that.hasClass('selected') ? collapse_action_show : collapse_action_hide;

		if ( (!css_width || css_width == 'auto') && (!attr_width || attr_width == 'auto') ) {
			$that.css('width', $that.width() );
		}

		$(this).children('.title').each( function() {
			$(this).prepend('<span class="action"><a><img src="'+which+'" /></a></span>').click( function() {
				var which = $that.toggleClass('selected').hasClass('selected')
					? collapse_action_show : collapse_action_hide;
				$(this).find('span.action img').attr('src', which);
				if ( which == collapse_action_show ) {
					$(this).siblings(':not(.title)').stop(true, true).fadeOut();
				} else {
					$(this).siblings(':not(.title)').stop(true, true).fadeIn();
				}
			}).click();
		});
	});

	$( "table.collapsible" ).each( function() {
		var $table = $(this), rows = this.rows, cell = rows.item(0).cells.item(0);
		var which = $table.hasClass('selected') ? collapse_action_show : collapse_action_hide;
		var css_width = $table.css('width'), attr_width = $table.attr('width');

		if ( (!css_width || css_width == 'auto') && (!attr_width || attr_width == 'auto') ) {
			$table.css('width', $table.width() );
		}

		$(cell).prepend('<span class="action"><a><img src="'+which+'" /></a></span>');
		$(rows.item(0)).click( function() {
			var which = $table.toggleClass('selected').hasClass('selected')
				? collapse_action_show : collapse_action_hide;
			$(cell).find('span.action img').attr('src', which);
			if ( which == collapse_action_show ) {
				$(rows).next().stop(true, true).fadeOut();
			} else {
				$(rows).next().stop(true, true).fadeIn();
			}
		}).click();
	});
}

$(document).ready( collapsible_boxes );

//</source>

// importScript('MediaWiki:Common.js/NavigationTabs.js');
// Navigate Tabs. Allows for lots of information to be displayed on a page in a more compact form.
// Maintained by [[User:Darklama]]
 
function Navigate_Tabs()
{
	function clicked_tab( e )
	{
		var $target = $( e.target ), id = e.target.hash;

		if ( !$target.is( 'a' ) || !id ) {
			return true;
		}

		$target = $(this).siblings( id );

		if ( !$target.hasClass( 'contents' ) || !$target.parent().hasClass( 'navtabs' ) ) {
			return true;
		}

		e.preventDefault();

		$target.parent().children( '.tabs' ).find( 'a' ).each( function() {
			if ( this.hash !== id ) {
				$(this).parent().addClass( 'inactive' ).removeClass( 'selected' );
			} else {
				$(this).parent().addClass( 'selected' ).removeClass( 'inactive' );
			}
		} );

		$target.parent().children( '.contents' ).hide();
		$target.show();
	}

	mw.util.$content.find('.navtabs').each( function() {
		var $this = $(this), $p = $this.children( 'p' ), $tabs, $any;

		// remove any surrounding paragraph first
		$p.has( '.tabs' ).before( $p.children( '.tabs' ) ).remove();

		// deal with clicks, and show default
		$tabs = $this.children( '.tabs' ).click( clicked_tab );
		$any = $tabs.children( '.selected' ).find('a[href^="#"]').click();

		if ( !$any.length ) {
			$tabs.children(':first-child').find('a[href^="#"]').click();
		}
	} );
}
 
$(document).ready(Navigate_Tabs);

// importScript('MediaWiki:Common.js/Displaytitle.js');
function change_displaytitle()
{
	var text = $("#displaytitle").attr('title'), what;

	if ( text ) {
		what = $("#ca-nstab-" + ( mw.config.get('wgCanonicalNamespace').toLowerCase() || 'main' ) );
		what.find('a').text(text);
	}
}
 
$(document).ready(change_displaytitle);

// importScript('MediaWiki:Common.js/Perbook.js');
/* Per-book JavaScript. 
  * Maintained by [[User:Darklama]]
  * Use book-specific stylesheet and JavaScript.
  * You can ask an administrator to add or update a global book specific Stylesheet or JavaScript.
  */

(function( mw ) {
	var	ns = mw.config.get( 'wgNameSpaceNumber' ),
		user = mw.config.get( 'wgUserName', false ),
		book = mw.config.get( 'wgBookName' );

	if ( ns === 8 || mw.config.get( 'wgIsArticle' ) === false ) {
		return; 	/* Disable in MediaWiki space and when not viewing book material */
	} else if ( ns === 2 ) {
		/* Find correct book name in User space */
		book = mw.config.get( 'wgPageName' ).split( '/', 2 )[1];

		if ( book === 'per_book' ) {
			return; /* Disable within reserved spaces */
		}
	}

	/* global styling */
//	importStylesheet( 'MediaWiki:Perbook/' + book + '.css' );
//	importScript( 'MediaWiki:Perbook/' + book + '.js' );

	/* user styling */
	if ( user ) {
		importStylesheet( 'User:' + user + '/per_book/' + book + '.css' );
		importScript( 'User:' + user + '/per_book/' + book + '.js' );
	}
})( mediaWiki );

// importScript('MediaWiki:Common.js/tabs.js');
jQuery( function() {
	var pagename = mw.config.get( 'wgPageName' );
	// Main Page
	if ( pagename == 'Main_Page' || pagename == 'Talk:Main_Page' ) {
		$('#ca-nstab-main a').text( 'Main Page' );
	// Wikijunior
	} else if ( pagename == 'Wikijunior' || pagename == 'Talk:Wikijunior' ) {
		$('#ca-nstab-main a').text( 'Wikijunior' );
	// Cookbook:Table of Contents
	} else if ( pagename == 'Cookbook:Table_of_Contents' || pagename == 'Cookbook_talk:Table_of_Contents' ) {
		$('#ca-nstab-cookbook a').text( 'Cookbook' );
	}
});

// importScript('MediaWiki:Common.js/top.js');
// Move icons and navigation to top of content area, which should place them right below the page title

mw.hook('wikipage.content').add(function($where) {
	//var $content = mw.util.$content,  // more hackery
	var $content = jQuery('.mw-body'), 
		$what = $where.find('.topicon').css('display', 'inline');
	
	if ( $what.length ) {
		$content.find(':header').eq(0).wrapInner('<span />').append( $('<span id="page-status" />').append($what) );
	}
	
	$what = $where.find('#top-navigation').remove().slice(0,1).addClass('subpages');
	if ( $what.length ) { $content.find('.subpages').eq(0).replaceWith($what); }
	
	$what = $where.find('#bottom-navigation').remove().slice(0,1);
	if ( $what.length ) { $where.append($what); }
});

// importScript('MediaWiki:Common.js/review.js');
// Force short review box to be on its own line.

$(document).ready( function($) {
  $('.flaggedrevs_short').wrap('<div style="display:block; clear:both; height:20px; line-height:18px; margin:5px;" />');
});

// importScript('MediaWiki:Common.js/Categories.js');
/**
 * Hide prefix in category
 *
 * @source: http://www.mediawiki.org/wiki/Snippets/Hide_prefix_in_category
 * @rev: 2
 * @author: Krinkle
 */

mw.hook('wikipage.content').add(function($content) {
  var prefix = $content.find( '#mw-cat-hideprefix' ).text();
  if ( $.trim( prefix ) === '' ) {
    prefix = mw.config.get( 'wgTitle' ) + '/';
  }
  $content.find( '#mw-pages' ).add( '#mw-subcategories' ).find( 'a' ).text( function( i, val ) {
    return val.replace( new RegExp( '^' + $.escapeRE( prefix ) ), '' );
  });
});

// importScript('MediaWiki:Common.js/use.js');
/* SKIP */
// importScript('MediaWiki:Common.js/Slideshows.js');
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

