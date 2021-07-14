// add some xowa specific code to move the image on Page (it:Page: == 108)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 108 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );

if( mediaWiki.config.get( 'wgNamespaceNumber' ) == 0) {
	xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/it.wikisource.org/Gadget-indexPagesStatistics_20210703.js');
	xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/it.wikisource.org/Gadget-barraNavigazione_20210703.js');
}
