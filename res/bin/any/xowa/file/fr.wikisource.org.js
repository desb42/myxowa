// add some xowa specific code to move the image on Page (fr:Page: == 104)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 104 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );
