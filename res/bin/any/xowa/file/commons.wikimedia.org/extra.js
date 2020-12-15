/**
 * WikiMiniAtlas
 *
 * Description: WikiMiniAtlas is a popup click and drag world map.
 *              This script causes all of our coordinate links to display the WikiMiniAtlas popup button.
 *              The script itself is located on meta because it is used by many projects.
 *              See [[Meta:WikiMiniAtlas]] for more information.
 * Note - use of this service is recommended to be repalced with mw:Help:Extension:Kartographer
 */
( function () {
	var require_wikiminiatlas = false;
	var coord_filter = /geohack/;
	$( function () {
		$( 'a.external.text' ).each( function( key, link ) {
			if ( link.href && coord_filter.exec( link.href ) ) {
				require_wikiminiatlas = true;
				// break from loop
				return false;
			}
		} );
		if ( $( 'div.kmldata' ).length ) {
			require_wikiminiatlas = true;
		}
		if ( require_wikiminiatlas ) {
      xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/miniatlas/miniatlas.js');
//			mw.loader.load( '//meta.wikimedia.org/w/index.php?title=MediaWiki:Wikiminiatlas.js&action=raw&ctype=text/javascript' );
		}
// hopefully loaded elsewhere
	} );
} )();
