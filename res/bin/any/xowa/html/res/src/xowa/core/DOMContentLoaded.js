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


function setaccess() {
    $('[accesskey]').updateTooltipAccessKeys();
}

function initCollapsible () {
    /* from jquery.makeCollapsible.css */
/*    xowa.css.add('.mw-collapsible-toggle{float:right;}li .mw-collapsible-toggle{float:none;}.mw-collapsible-toggle-li{list-style:none;}');
		var options = {};
    options.collapsed = xowa.cfg.get('collapsible-collapsed');
    $('table.collapsible, .mw-collapsible').makeCollapsible(options);*/
		var options = {};
    $('.mw-collapsible').makeCollapsible(options);
}

function initTablesorter () {
    //for jquery.tablesorter.js
    /* from jquery.tablesorter.css - changed to inline svg*/
    xowa.css.add('table.jquery-tablesorter th.headerSort{background-image:url("data:image/svg+xml,%3C%3Fxml version=\'1.0\' encoding=\'UTF-8\'%3F%3E%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'21\' height=\'9\' viewBox=\'0 0 21 9\'%3E%3Cpath d=\'M14.5 5l-4 4-4-4zm0-1l-4-4-4 4z\'/%3E%3C/svg%3E");cursor:pointer;background-repeat:no-repeat;background-position:center right;padding-right:21px}table.jquery-tablesorter th.headerSortUp{background-image:url("data:image/svg+xml,%3C%3Fxml version=\'1.0\' encoding=\'UTF-8\'%3F%3E%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'21\' height=\'4\' viewBox=\'0 0 21 4\'%3E%3Cpath d=\'M6.5 4l4-4 4 4z\'/%3E%3C/svg%3E");}table.jquery-tablesorter th.headerSortDown{background-image:url("data:image/svg+xml,%3C%3Fxml version=\'1.0\' encoding=\'UTF-8\'%3F%3E%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'21\' height=\'4\' viewBox=\'0 0 21 4\'%3E%3Cpath d=\'M14.5 0l-4 4-4-4z\'/%3E%3C/svg%3E");}');
    $('table.sortable').tablesorter();
}
function initReferenceTooltips () {
   xowa.css.add('.popupref {position: fixed; z-index: 1000; padding: 5px; border: 2px solid #00f; background-color: #def; max-width: 500px; overflow: auto;}');
}
function init () {
    jQuery.escapeRE = function (s) {
        return s.replace(/([\\{}()|.?*+\-\^$\[\]])/g, '\\$1');
    };
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery.ba-throttle-debounce.js');
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
      xowa.js.load_lib(xowa.root_dir + 'bin/any/jquery.client.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/jquery.accessKeyLabel.js', setaccess);
    if (needCollapsible) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/jquery/jquery.makeCollapsible.js', initCollapsible);
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery.makeCollapsible.styles.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/src/collapsible.css')
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
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-core-apex.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki_gen/oojs-ui.styles.icons-movement.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki_gen/oojs-ui.styles.icons-content.css')
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
    if (document.querySelectorAll('.mw-kartographer-container').length
        || document.querySelectorAll('.mw-kartographer-maplink').length
        || document.querySelectorAll('.mw-kartographer-error').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/karto/ext.kartographer.style.css')
    }
      
    // proofread (wikisource) pages
    if (document.querySelectorAll('table.pr_quality').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.base.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.article.css')
    }
    else if (document.querySelectorAll('.prp-page-container').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.base.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/proof/ext.proofreadpage.page.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/xtns/ProofreadPage/ext.proofreadpage.page.navigation.css')
    }

    if (document.querySelectorAll('.wikibase-entityview-main').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery.wikibase.css')
    }

    if (document.querySelectorAll('.mw-category-group').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/mediawiki.action.view.categoryPage.styles.css')
    }
    if (document.querySelectorAll('.CategoryTreeSection').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/xtns/Categorytree/ext.categoryTree.css')
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/xtns/Categorytree/ext.categoryTree.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.api.js');
    }

    if (document.querySelectorAll('.mw-templatedata-doc-params').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/templatedata/ext.templateData.css')
    }

    if (document.querySelectorAll('.oo-ui-widget').length) {
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs-ui/oojs-ui-core-wikimediaui.css')
      xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/html/res/lib/templatedata/ext.templateData.images.css')
    }

    if (document.querySelectorAll('.prettyprint').length) {
      //xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/google-code-prettify/run_prettify.js?lang=lua&lang=css&lang=js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/google-code-prettify/run_prettify.js?lang=lua&lang=css');
    }

// interactive maps?
    //if (document.querySelectorAll('#coordinates').length && xowa_global_values.mode_is_http) {
    //  xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/miniatlas/miniatlas.js');
    //}

// PageBanner stuff?
    if (document.querySelectorAll('.wpb-topbanner').length) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/xtns/WikidataPageBanner/resources/ext.WikidataPageBanner.positionBanner/ext.WikidataPageBanner.positionBanner.js');
      if (document.querySelectorAll('.mw-parser-output .toctitle').length == 0) {
        xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/xtns/WikidataPageBanner/resources/ext.WikidataPageBanner.toc.styles/ext.WikidataPageBanner.toc.css')
        xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/xtns/WikidataPageBanner/resources/ext.WikidataPageBanner.toc.styles/ext.WikidataPageBanner.bottomtoc.css')
      }
    }

    if (xowa_global_values['proofreadpage_source_href'] !== undefined) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/xtns/ProofreadPage/ext.proofreadpage.article.js');
    }

    if (document.querySelectorAll('.jcarousel-wrapper').length) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/carousel/ext.gadget.Carousel.js');
    }

    if (document.querySelectorAll('.read-more-container').length) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.api.python.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/oojs/oojs.jquery.js');
      xowa.js.load_lib(xowa.root_dir + 'bin/any/readmore/ext.relatedArticles.whole.js');
    }

    if (x_p.wiki == 'en.wikivoyage.org' || x_p.wiki.substring(3) == 'wikipedia.org' || x_p.wiki == 'simple.wikipedia.org') {
//      || x_p.wiki == 'it.wikipedia.org' || x_p.wiki == 'fr.wikipedia.org') {
      $(document).ready( xowa.js.load_lib(xowa.root_dir + 'bin/any/popup/ext.popups.images.js') );
    }
    if (document.querySelectorAll('.switcher-container').length) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/ext.gadget.switch.js');
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
