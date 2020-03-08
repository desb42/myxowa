// add some xowa specific code to move the image on Page (de:Page: == 102)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 102 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );

