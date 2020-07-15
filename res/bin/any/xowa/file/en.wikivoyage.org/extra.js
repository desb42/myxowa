/* currently the only thing taken from Mediawiki:Common.js */

$(".geo, #GPX-track").parents("#content").find("#mw-indicator-GPX").css("display","inline");

/* from toctree https://github.com/wikimedia/mediawiki-extensions-TocTree/blob/master/modules/ext.toctree.js */
/*
 * JavaScript functions for the TocTree extension
 * to display the toc structure
 *
 * @package MediaWiki
 * @subpackage Extensions
 * @author Roland Unger
 * @author Matthias Mullie
 * @copyright © 2007 Roland Unger
 * v1.0 of 2007/11/04
 * @licence GNU General Public Licence 2.0 or later
 */

( function () {
	function processClickEvent() {
		var $ul = $( this ).parent().parent().children( 'ul' );
		$ul.toggle();

		if ( $ul.is( ':visible' ) ) {
			$( this )
				.text( '-' )
				.attr( 'title', mw.msg( 'hidetoc' ) );
		} else {
			$( this )
				.text( '+' )
				.attr( 'title', mw.msg( 'showtoc' ) );
		}
	}

	function init( $content ) {
		var $toc = $content.find( '.toc' ).addBack( '.toc' ),
			$mainList = $toc.children( 'ul' ).children( 'li.toclevel-1' );

		//if ( mw.user.options.get( 'toc-floated' ) ) {
			$toc.addClass( 'tocFloat' );
		//}

		$mainList.each( function () {
			var $subList, $toggleSymbol, $toggleSpan;

			$( this ).css( 'position', 'relative' );
			$subList = $( this ).children( 'ul' );

			if ( $subList.length > 0 ) {
				$( this ).parent().addClass( 'tocUl' );

				$toggleSymbol = $( '<span>' ).addClass( 'toggleSymbol' );

				//if ( mw.user.options.get( 'toc-expand' ) ) {
//					$toggleSymbol
//						.text( '-' )
//						.attr( 'title', mw.msg( 'hidetoc' ) );

//					$subList.show();
				//} else {
					$toggleSymbol
						.text( '+' )
						.attr( 'title', mw.msg( 'showtoc' ) );

					$subList.hide();
				//}
				$toggleSymbol.click( processClickEvent );

				$toggleSpan = $( '<span>' ).addClass( 'toggleNode' );
				$toggleSpan.append( '[', $toggleSymbol, ']' );

				$( this ).prepend( $toggleSpan );
			}
		} );
	}

	mw.hook( 'wikipage.content' ).add( init );
}() );

xowa_implement("ext.toctree@o0bv8",null,{"css":[
".toc .tocUl{padding-left:2em} .toc.tocFloat{float:left;margin:0 2em 1em 0;width:20em;max-width:20em}.noFloat .toc.tocFloat{float:none;margin:0;width:auto;max-width:auto}.toggleSymbol{color:#00f;cursor:pointer}.toggleNode{position:absolute;top:0;left:-2em}@media print{.toc.tocFloat{background:#fff}.toggleNode{display:none}.toc .tocUl{padding-left:0}}"]});

