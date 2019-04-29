/* DOMContentLoaded.js: Adds sortable and collapsible behavior to pages
Copyright (C) 2013 Schnark (<https://de.wikipedia.org/wiki/Benutzer:Schnark>)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
document.addEventListener('DOMContentLoaded', function () {

	function importStylesheetURI( url, media ) {
		var l = document.createElement( 'link' );
		l.rel = 'stylesheet';
		l.href = url;
		if ( media ) {
			l.media = media;
		}
		document.head.appendChild( l );
		return l;
	}

function initCollapsible () {
    /* from jquery.makeCollapsible.css */
    xowa.css.add('.mw-collapsible-toggle{float:right;}li .mw-collapsible-toggle{float:none;}.mw-collapsible-toggle-li{list-style:none;}');
		var options = {};
    options.collapsed = xowa.cfg.get('collapsible-collapsed');
    $('table.collapsible, .mw-collapsible').makeCollapsible(options);
}

function initTablesorter () {
    //for jquery.tablesorter.js
    jQuery.escapeRE = function (s) {
        return s.replace(/([\\{}()|.?*+\-\^$\[\]])/g, '\\$1');
    };
    /* from jquery.tablesorter.css */
    xowa.css.add('table.jquery-tablesorter th.headerSort{background-image:url(data:image/gif;base64,R0lGODlhFQAJAIABAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4OEM2REYyN0ExMDhBNDJFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjdCNTAyODcwMEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjdCNTAyODZGMEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDE4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDE4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAAFQAJAAACF4yPgMsJ2mJ4VDKKrd4GVz5lYPeMiVUAADs=);cursor:pointer;background-repeat:no-repeat;background-position:center right;padding-right:21px}table.jquery-tablesorter th.headerSortUp{background-image:url(data:image/gif;base64,R0lGODlhFQAEAIABAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAzODAxMTc0MDcyMDY4MTE4OEM2REYyN0ExMDhBNDJFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjdCNTAyODc0MEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjdCNTAyODczMEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDM4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDM4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAAFQAEAAACDYwfoAvoz9qbZ9FrJC0AOw==);}table.jquery-tablesorter th.headerSortDown{background-image:url(data:image/gif;base64,R0lGODlhFQAEAIABAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAyODAxMTc0MDcyMDY4MTE4OEM2REYyN0ExMDhBNDJFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjhFNzNGQjI3MEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjhFNzNGQjI2MEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDI4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDI4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAAFQAEAAACDYyPAcmtsJyDVDKKWQEAOw==);}');
    $('table.sortable').tablesorter();
}
function initReferenceTooltips () {
   xowa.css.add('.popupref {position: fixed; z-index: 1000; padding: 5px; border: 2px solid #00f; background-color: #def; max-width: 500px; overflow: auto;}');
}
function init () {
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery.cookie.js');
    var needCollapsible = document.querySelectorAll('table.collapsible, .mw-collapsible').length,
        needSortable    = document.querySelectorAll('table.sortable').length,
        needReference   = document.querySelectorAll('sup.reference a, .reference sup a').length
        ;
    var needGalleryPacked    = xowa_global_values['gallery-packed-enabled']
    ,   needToc              = xowa_global_values['toc-enabled']
    ,   needNavframe         = xowa_global_values['navframe-enabled']
    ,   needGallerySlideshow = xowa_global_values['gallery-slideshow-enabled']
    ;    
    if (needCollapsible || needSortable || needReference || needGalleryPacked || needToc || needNavframe || needGallerySlideshow) {
      xowa.js.jquery.init();
      xowa.js.mediaWiki.init();
    }
    if (needCollapsible) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/jquery/jquery.makeCollapsible.js', initCollapsible);
    }
    if (needSortable) {
      //xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki.language/mediawiki.language.init.js');
      //xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki.language/mediawiki.language.months.js');
      //xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki.language/mediawiki.language.numbers.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/jquery/jquery.tablesorter.js', initTablesorter);
    }
    if (needToc) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.toc.js');
//      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.String.js');
    }
    if (needGalleryPacked) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki.page/mediawiki.page.gallery.js');
    }
    if (needGallerySlideshow) {
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-core-apex.css')
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki_gen/oojs-ui.styles.icons-movement.css')
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki_gen/oojs-ui.styles.icons-content.css')
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.Title.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.Title.phpCharToUpper.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs/oojs.jquery.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-router/oojs-router.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-core.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-windows.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-apex.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-widgets.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki.page/mediawiki.page.gallery-slideshow.js');
    }
// has been commented out - possibly for dewiki? 20190222
// done by wikis .js 20190321
//    if (needNavframe) {
//      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/gadgets/navframe/mediawiki.gadget.navframe.js');
//    }
    if (needReference) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/gadgets/reference-tooltips/jquery.reference-tooltips.js', initReferenceTooltips);
    }

    if (document.querySelectorAll('.mw-kartographer-maplink').length) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs/oojs.jquery.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-router/oojs-router.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-core.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-windows.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-apex.js');

      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery.ba-throttle-debounce.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/mapbox/mapbox-lib.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/wikimedia-mapdata.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/modules/box/data.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/modules/dialog/closefullscreen_control.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/modules/dialog/dialog.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/modules/dialog/index.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/modules/linkbox/Link.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/modules/maplink/maplink.js');
    }
      
    // proofread (wikisource) pages
    if (document.querySelectorAll('table.pr_quality').length) {
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.base.css')
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.article.css')
    }
    else if (document.querySelectorAll('.prp-page-container').length) {
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.base.css')
      importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.page.css')
    }

    if (document.querySelectorAll('.prettyprint').length) {
      //xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/google-code-prettify/run_prettify.js?lang=lua&lang=css&lang=js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/google-code-prettify/run_prettify.js?lang=lua&lang=css');
    }

// interactive maps?
    if (document.querySelectorAll('#coordinates').length && xowa_global_values.mode_is_http) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/miniatlas/miniatlas.js');
    }

/*
????? wikibooks
// Move icons and navigation to top of content area, which should place them right below the page title

mediaWiki.hook('wikipage.content').add(function($where) {
	var $content = mediaWiki.util.$content, $what = $where.find('.topicon').css('display', 'inline');
	
	if ( $what.length ) {
		$content.find(':header').eq(0).wrapInner('<span />').append( $('<span id="page-status" />').append($what) );
	}
	
	$what = $where.find('#top-navigation').remove().slice(0,1).addClass('subpages');
	if ( $what.length ) { $content.find('.subpages').eq(0).replaceWith($what); }
	
	$what = $where.find('#bottom-navigation').remove().slice(0,1);
	if ( $what.length ) { $where.append($what); }
});
*/
}
init();
}, false);
