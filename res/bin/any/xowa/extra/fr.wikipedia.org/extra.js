/* jshint scripturl:true, laxbreak:true, loopfunc:true */
/* global mw, $, importScript */
/**
 * N'importe quel JavaScript ici sera chargé pour n'importe quel utilisateur et pour chaque page accédée.
 *
 * ATTENTION : Avant de modifier cette page, veuillez tester vos changements avec votre propre
 * vector.js. Une erreur sur cette page peut faire bugger le site entier (et gêner l'ensemble des
 * visiteurs), même plusieurs heures après la modification !
 *
 * Prière de ranger les nouvelles fonctions dans les sections adaptées :
 * - Fonctions JavaScript
 * - Fonctions spécifiques pour MediaWiki
 * - Applications spécifiques à la fenêtre d'édition
 * - Applications qui peuvent être utilisées sur toute page
 * - Applications spécifiques à un espace de nom ou une page
 *
 * <nowiki> /!\ Ne pas retirer cette balise
 */


/**********************************************************************************************************/
/* Fonctions générales MediaWiki (pallient les limitations du logiciel)                                   */
/* Surveiller : https://git.wikimedia.org/history/mediawiki%2Fcore.git/HEAD/skins%2Fcommon%2Fwikibits.js  */
/**********************************************************************************************************/

/**
 * Projet JavaScript
 */
window.obtenir = function ( name ) {
	if ( mw.loader.getState( 'ext.gadget.' + name ) !== null ) {
		mw.loader.load( 'ext.gadget.' + name );
	} else {
		importScript( 'MediaWiki:Gadget-' + name + '.js' );
	}
};

/**
 * Transformer les pages du Bistro, du BA et les pages spécifiées en page de discussion
 */
if ( mw.config.get( 'wgNamespaceNumber' ) >= 2 ) {
	$( function ( $ ) {
		if (
			/^Wikipédia:(Le_Bistro|Bulletin_des_administrateurs|Questions_techniques)/.test( mw.config.get( 'wgPageName' ) ) ||
			$( '#transformeEnPageDeDiscussion' ).length
		) {
			$( 'body' ).removeClass( 'ns-subject' ).addClass( 'ns-talk' );
		}
	} );
}


/****************************************/
/* Applications pour l'ensemble du site */
/****************************************/

/**
 * Tout ce qui concerne la page d'édition
 */
if ( [ 'edit', 'submit' ].indexOf( mw.config.get( 'wgAction' ) ) !== -1 ) {

	// chargement de [[MediaWiki:Gadget-CommonEdit.js]]
	mw.loader.load( 'ext.gadget.CommonEdit' );

	// pour que les fonctions soient définies dès maintenant,
	// mais l'exécution réelle ne se fait qu'une fois le module chargé
	window.addSpecialCharset = function ( title, chars ) {
		mw.loader.using( 'ext.gadget.CommonEdit', function () {
			window.realAddSpecialCharset( title, chars );
		} );
	};
	window.addSpecialCharsetHTML = function ( title, charsHTML ) {
		mw.loader.using( 'ext.gadget.CommonEdit', function () {
			window.realAddSpecialCharsetHTML( title, charsHTML );
		} );
	};

	// fonction pour ajouter un bouton à la fin de la barre d'outils
	// permet d'utiliser [[MediaWiki:Gadget-MonobookToolbar.js]] sans se préoccuper de son chargement
	window.addCustomButton = ( function () {
		var promise;

		return function () {
			var buttonArguments = [].slice.call( arguments );

			if ( !promise ) {
				promise = mw.loader.using( 'ext.gadget.MonobookToolbar' );
			}

			promise.done( function () {
				MonobookToolbar.addButton.apply( MonobookToolbar, buttonArguments );
			} );
		};
	} )();

} else {
	// pour que les fonctions soient toujours définies,
	// afin d'éviter aux scripts utilisateur de planter
	window.addSpecialCharset = function () {};
	window.addSpecialCharsetHTML = function () {};
	window.addCustomButton = function () {};
}

/**
 * Réécriture des titres
 *
 * Fonction utilisée par [[Modèle:Titre incorrect]]
 *
 * La fonction cherche un bandeau de la forme
 * <div id="RealTitleBanner">
 *   <span id="RealTitle">titre</span>
 * </div>
 *
 * Un élément comportant id="DisableRealTitle" désactive la fonction
 */
function rewritePageTitle( $ ) {
	var $realTitle, titleHtml, $h1,
		$realTitleBanner = $( '#RealTitleBanner' );
	if ( $realTitleBanner.length && !$( '#DisableRealTitle' ).length ) {
		$realTitle = $( '#RealTitle' );
		$h1 = $( 'h1:first' );
		if ( $realTitle.length && $h1.length ) {
			titleHtml = $realTitle.html();
			if ( titleHtml === '' ) {
				$h1.hide();
			} else {
				$h1.html( titleHtml );
				if ( mw.config.get( 'wgAction' ) === 'view' ) {
					// using a callback for replacement, to prevent interpreting "$" characters that realTitle might contain
					document.title = document.title.replace( /^.+( [—–-] Wikipédia)$/, function ( match, p1 ) {
						return $realTitle.text() + p1;
					} );
				}
			}
			$realTitleBanner.hide();
			$( '<p>' ).css( 'font-size', '80%' )
				.append( 'Titre à utiliser pour créer un lien interne : ', $( '<b>' ).text( mw.config.get( 'wgPageName' ).replace( /_/g, ' ' ) ) )
				.insertAfter( $h1 );
		}
	}
}
$( rewritePageTitle );


/**
 * Ajout d'un sous-titre
 *
 * Fonction utilisée par [[Modèle:Sous-titre]] et quelques modules de taxobox
 *
 * La fonction cherche un élément de la forme
 * <span id="sous_titre_h1">Sous-titre</span>
 */

function sousTitreH1( $content ) {
	$( '#firstHeading > #sous_titre_h1' ).remove();
	var $span = $content.find( '#sous_titre_h1' );
	if ( $span.length ) {
		$span.prepend( ' ' );
		$( '#firstHeading' ).append( $span );
	}
}
mw.hook( 'wikipage.content' ).add( sousTitreH1 );


/**
 * Boîtes déroulantes
 *
 * Pour [[Modèle:Méta palette de navigation]]
 */

var Palette_Derouler = '[afficher]';
var Palette_Enrouler = '[masquer]';

var Palette_max = 1;

function Palette_toggle( $table ) {
	$table.find( 'tr:not(:first)' ).toggleClass( 'navboxHidden' );
}

function Palette( $content ) {

	var $tables = $content.find( 'table.collapsible' );
	var groups = {};

	$tables.each( function ( _, table ) {
		var group = table.dataset.autocollapseGroup || '__default__';
		groups[group] = ( groups[group] || 0 ) + 1;
	} );

	$tables.each( function ( _, table ) {
		var $table = $( table );

		var group = table.dataset.autocollapseGroup || '__default__';
		var autoCollapse = groups[group] > Palette_max;
		var collapsed = $table.hasClass( 'collapsed' ) || ( autoCollapse && $table.hasClass( 'autocollapse' ) );

		// le modèle dispose d'une classe "navbox-title",
		// sauf que les palettes "inlinées" (e.g. « {| class="navbox collapsible collapsed" ») n'ont pas cette classe
		$table.find( 'tr:first th:first' ).prepend(
			$( '<span class="navboxToggle">\u00a0</span>' ).append(
				$( '<a href="javascript:">' + (collapsed ? Palette_Derouler : Palette_Enrouler) + '</a>' ).click( function ( e ) {
					e.preventDefault();
					if ( this.textContent === Palette_Enrouler ) {
						this.textContent = Palette_Derouler;
					} else {
						this.textContent = Palette_Enrouler;
					}
					Palette_toggle( $table );
				} )
			)
		);
		if ( collapsed ) {
			Palette_toggle( $table );
		}
	} );

	// permet de dérouler/enrouler les palettes en cliquant n'importe où sur l'entête
	// (utilisation de la classe "navbox-title", comme ça seules les vraies palettes utilisant le modèle sont ciblées)
	$content.find( '.navbox-title' )
		.click( function ( e ) {
			if ( $( e.target ).closest( 'a' ).length ) {
				return;
			}
			$( this ).find( '.navboxToggle a' ).click();
		} )
		.css( 'cursor', 'pointer' );
}
mw.hook( 'wikipage.content' ).add( Palette );


/**
 * Pour [[Modèle:Boîte déroulante]]
 */

var BoiteDeroulante_Derouler = '[afficher]';
var BoiteDeroulante_Enrouler = '[masquer]';

function BoiteDeroulante_toggle(NavToggle){
	var NavFrame = NavToggle.parentNode;

	var caption = [];
	caption[0] = NavFrame.dataset.boiteDeroulanteDerouler;
	caption[1] = NavFrame.dataset.boiteDeroulanteEnrouler;

	var $NavContent = $(NavFrame).find('.NavContent').first();

	if ( NavToggle.textContent === caption[1] ) {
		NavToggle.textContent = caption[0];
		$NavContent.hide();
	} else {
		NavToggle.textContent = caption[1];
		$NavContent.show();
	}
}

function BoiteDeroulante( $content ) {

	$content.find( '.NavFrame' ).each( function ( _, NavFrame ) {
		var CustomTexts, Derouler, Enrouler, NavToggle;

		if (NavFrame.title && NavFrame.title.indexOf("/") !== -1) {
			CustomTexts = NavFrame.title.split("/");
			Derouler = CustomTexts[0];
			Enrouler = CustomTexts[1];
		} else {
			Derouler = BoiteDeroulante_Derouler;
			Enrouler = BoiteDeroulante_Enrouler;
		}
		NavFrame.title = '';
		NavFrame.dataset.boiteDeroulanteDerouler = Derouler;
		NavFrame.dataset.boiteDeroulanteEnrouler = Enrouler;

		NavToggle = document.createElement("a");
		NavToggle.className = 'NavToggle';
		NavToggle.href = 'javascript:';
		NavToggle.onclick = function (e) {
			e.preventDefault();
			BoiteDeroulante_toggle(e.target);
		};
		NavToggle.textContent = Enrouler;

		NavFrame.insertBefore(NavToggle, NavFrame.firstChild);

		BoiteDeroulante_toggle(NavToggle);
	} );

	// permet de dérouler/enrouler les boîtes en cliquant n'importe où sur l'entête
	$content.find( '.NavHead' )
		.click( function ( e ) {
			if ( $( e.target ).closest( 'a' ).length ) {
				return;
			}
			var toggle = $( this ).siblings( 'a.NavToggle' )[0];
			if ( toggle ) {
				toggle.click(); // pas du jquery, mais du vanilla js
			}
		} )
		.css( 'cursor', 'pointer' );
}

mw.hook( 'wikipage.content' ).add( BoiteDeroulante );


/**
 * Fonctionnement du [[Modèle:Animation]]
 * Le JavaScript principal se situe dans [[MediaWiki:Gadget-Diaporama.js]]
 */
mw.hook( 'wikipage.content' ).add( function ( $content ) {
	if ( $content.find( '.diaporama' ).length ) {
		mw.loader.using( 'ext.gadget.Diaporama', function () {
			Diaporama_Init( $content );
		} );
	}
} );


/**
 * Permet d'afficher les catégories cachées pour les contributeurs enregistrés, en ajoutant un (+) à la manière des boîtes déroulantes
 */
function hiddencat( $ ) {
	if (mw.util.getParamValue('printable') === 'yes') {
		return;
	}
	var cl = document.getElementById('catlinks');
	if (!cl) {
		return;
	}
	var $hc = $('#mw-hidden-catlinks');
	if ( !$hc.length ) {
		return;
	}
	if ( $hc.hasClass('mw-hidden-cats-user-shown') ) {
		return;
	}
	if ( $hc.hasClass('mw-hidden-cats-ns-shown') ) {
		$hc.addClass('mw-hidden-cats-hidden');
	}
	var nc = document.getElementById('mw-normal-catlinks');
	if ( !nc ) {
		var catline = document.createElement('div');
		catline.id = 'mw-normal-catlinks';
		var a = document.createElement('a');
		a.href = '/wiki/Catégorie:Accueil';
		a.title = 'Catégorie:Accueil';
		a.textContent = 'Catégories';
		catline.appendChild(a);
		catline.appendChild(document.createTextNode(' : '));
		nc = cl.insertBefore(catline, cl.firstChild);
	}
	var lnk = document.createElement('a');
	lnk.id = 'mw-hidden-cats-link';
	lnk.title = 'Cet article contient des catégories cachées';
	lnk.style.cursor = 'pointer';
	lnk.style.color = 'black';
	lnk.style.marginLeft = '0.3em';
	$(lnk).click(toggleHiddenCats);
	lnk.textContent = '[+]';
	nc.appendChild(lnk);
}

function toggleHiddenCats(e) {
	var $hc = $('#mw-hidden-catlinks');
	if ( $hc.hasClass('mw-hidden-cats-hidden') ) {
		$hc.removeClass('mw-hidden-cats-hidden');
		$hc.addClass('mw-hidden-cat-user-shown');
		e.target.textContent = '[–]';
	} else {
		$hc.removeClass('mw-hidden-cat-user-shown');
		$hc.addClass('mw-hidden-cats-hidden');
		e.target.textContent = '[+]';
	}
}

xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.util.js', function () {
//mw.loader.using('mediawiki.util', function () {
	$( hiddencat );
});


/**
 * Script pour alterner entre plusieurs cartes de géolocalisation
 */

function GeoBox_Init($content) {
	// noter qu'une classe "imgtoggle" (sans l'underscore) est aussi présente sur le wiki, sans rapport avec celle-ci
	$content.find( '.img_toggle' ).each( function ( i, Container ) {
		Container.id = 'img_toggle_' + i;
		var Boxes = $( Container ).find( '.geobox' );
		if (Boxes.length < 2) {
			return;
		}
		var ToggleLinksDiv = document.createElement('ul');
		ToggleLinksDiv.id = 'geoboxToggleLinks_' + i;
		Boxes.each( function ( a, ThisBox ) {
			ThisBox.id = 'geobox_' + i + '_' + a;
			var ThisAlt;
			var ThisImg = ThisBox.getElementsByTagName('img')[0];
			if (ThisImg) {
				ThisAlt = ThisImg.alt;
			}
			if (!ThisAlt) {
				ThisAlt = 'erreur : description non trouvée';
			}
			var toggle = document.createElement('a');
			toggle.id = 'geoboxToggle_' + i + '_' + a;
			toggle.textContent = ThisAlt;
			toggle.href = 'javascript:';
			toggle.onclick = function (e) {
				e.preventDefault();
				GeoBox_Toggle(this);
			};
			var Li = document.createElement('li');
			Li.appendChild(toggle);
			ToggleLinksDiv.appendChild(Li);
			if (a === (Boxes.length - 1)) {
				toggle.style.color = '#888';
				toggle.style.pointerEvents = 'none';
			} else {
				ThisBox.style.display = 'none';
			}
		} );
		Container.appendChild(ToggleLinksDiv);
	} );
}

function GeoBox_Toggle(link) {
	var ImgToggleIndex = link.id.replace('geoboxToggle_', '').replace(/_.*/g, '');
	var GeoBoxIndex = link.id.replace(/.*_/g, '');
	var ImageToggle = document.getElementById('img_toggle_' + ImgToggleIndex);
	var Links = document.getElementById('geoboxToggleLinks_' + ImgToggleIndex);
	var Geobox = document.getElementById('geobox_' + ImgToggleIndex + '_' + GeoBoxIndex);
	var Link = document.getElementById('geoboxToggle_' + ImgToggleIndex + '_' + GeoBoxIndex);
	if ( !ImageToggle || !Links || !Geobox || !Link ) {
		return;
	}
	$( ImageToggle ).find( '.geobox' ).each( function ( _, ThisgeoBox ) {
		if (ThisgeoBox.id === Geobox.id) {
			ThisgeoBox.style.display = '';
		} else {
			ThisgeoBox.style.display = 'none';
		}
	} );
	$( Links ).find( 'a' ).each( function ( _, thisToggleLink ) {
		if (thisToggleLink.id === Link.id) {
			thisToggleLink.style.color = '#888';
			thisToggleLink.style.pointerEvents = 'none';
		} else {
			thisToggleLink.style.color = '';
			thisToggleLink.style.pointerEvents = '';
		}
	} );
}

mw.hook( 'wikipage.content' ).add( GeoBox_Init );


/**
 * permet d'ajouter un petit lien (par exemple d'aide) à la fin du titre d'une page.
 * utilisé par [[Modèle:Aide contextuelle]]
 * known bug : conflit avec le changement de titre classique.
 * Pour les commentaires, merci de contacter [[user:Plyd|Plyd]].
 */
function rewritePageH1bis() {
	var helpPage = document.getElementById('helpPage');
	if (helpPage) {
		var h1 = document.getElementById('firstHeading');
		if (h1) {
			h1.innerHTML += '<span id="h1-helpPage">' + helpPage.innerHTML + '</span>';
		}
	}
}
$( rewritePageH1bis );

/**
 * Configuration du tri des diacritique dans les tables de class "sortable"
 */
mw.config.set( 'tableSorterCollation', {'à':'a', 'â':'a', 'æ':'ae', 'é':'e', 'è':'e', 'ê':'e', 'î':'i', 'ï':'i', 'ô':'o', 'œ':'oe', 'û':'u', 'ç':'c',  } );

/**
 * Direct imagelinks to Commons
 *
 * Required modules: mediawiki.util, user.options
 *
 * @source www.mediawiki.org/wiki/Snippets/Direct_imagelinks_to_Commons
 * @author Krinkle
 * @version 2015-06-23
 * Ajouté le 'uselang' ce 18 janvier 2016 — Ltrlg
 */
if ( mw.config.get( 'wgNamespaceNumber' ) >= 0 ) {
	mw.loader.using( [ 'mediawiki.util', 'user.options' ], function () {
		mw.hook( 'wikipage.content' ).add( function ( $content ) {
			var
				uploadBase = '//upload.wikimedia.org/wikipedia/commons/',

				fileNamespace = mw.config.get( 'wgFormattedNamespaces' )['6'],
				localBasePath = new RegExp( '^' + mw.util.escapeRegExp( mw.util.getUrl( fileNamespace + ':' ) ) ),
				localBaseScript = new RegExp( '^' + mw.util.escapeRegExp( mw.util.wikiScript() + '?title=' + mw.util.wikiUrlencode( fileNamespace + ':' ) ) ),

				commonsBasePath = '//commons.wikimedia.org/wiki/File:',
				commonsBaseScript = '//commons.wikimedia.org/w/index.php?title=File:',

				lang = mw.user.options.get( 'language' );

			$content.find( '.image' ).each( function ( i, link ) {
				if ( link.tagName !== 'A' ) {
					return;
				}

				var img = link.querySelector( 'img' );

				// attention : on lit l'attribut, et non la propriété (elle contient en plus le protocole)
				// (en prime, il est plus performant dans ce cas de lire l'attribut)
				if ( img && img.getAttribute( 'src' ).indexOf( uploadBase ) === 0 ) {
					var currVal = link.getAttribute( 'href' );
					if ( localBasePath.test( currVal ) ) {
						link.setAttribute( 'href', currVal.replace( localBasePath, commonsBasePath ) + '?uselang=' + lang );
					} else if ( localBaseScript.test( currVal ) ) {
						link.setAttribute( 'href', currVal.replace( localBaseScript, commonsBaseScript ) + '&uselang=' + lang );
					}
				}
			} );
		} );
	} );
}

/**
 * Ajout d'un lien « Ajouter un sujet » en bas de page
 */
if ( mw.config.get( 'wgAction' ) === 'view' ) {
	$( function ( $ ) {
		var addSection = document.getElementById( 'ca-addsection' );
		if ( !addSection ) { // pas d'onglet « Ajouter un sujet »
			return;
		}
		var addSectionLink = addSection.querySelector( 'a' );
		if ( !addSectionLink ) { // erreur : le markup a changé
			return;
		}

		var $container = $( '<div style="text-align:right; font-size:0.9em; margin:1em 0 -0.5em">' );

		var link = document.createElement( 'a' );
		link.href = addSectionLink.href; // ce href sert encore, pour les middle-click, Ctrl+click... (ouverture dans un nouvel onglet)
		link.title = addSectionLink.title;
		link.textContent = addSectionLink.textContent;

		// compatibilité avec la fonctionnalité beta "New Discussion Tool", voir [[mw:Extension:DiscussionTools]]
		link.addEventListener( 'click', function ( e ) {
			if ( !e.ctrlKey ) {
				e.preventDefault();
				addSectionLink.click(); // .click() JS natif, pour information le .click() jQuery ne fonctionne pas dans le cas présent
			}
		} );

		$container.append( link );

		$( '#mw-content-text' ).append( $container );
	} );
}

/**
 * Repositionnement de la page sur l'ancre avec laquelle elle a été appelée
 * après le repli des boîtes déroulantes, entre autres.
 */
if ( window.location.hash ) {
	$( function ( $ ) {
		setTimeout( function () {
			var currentTarget = document.getElementById( decodeURIComponent( window.location.hash.substring( 1 ) ) );
			if ( currentTarget ) {
				currentTarget.scrollIntoView();
			}
		}, 1 );
	} );
}


/************************************************************/
/* Function Strictement spécifiques à un espace de nom ou à une page */
/************************************************************/

// ESPACE DE NOM 'SPECIAL'
if ( mw.config.get( 'wgNamespaceNumber' ) === -1 ) {

/**
 * Ajoute le namespace aux filtres personnalisés sur [[Spécial:Pages liées]]
 * Voir aussi [[MediaWiki:Linkshere]]
 */
if ( mw.config.get( 'wgCanonicalSpecialPageName' ) === 'Whatlinkshere' ) {

	mw.loader.using( 'mediawiki.Uri', function () {
		$( function ( $ ) {

			var query = ( new mw.Uri( null, { overrideKeys: true } ) ).query;

			var append = ( query.namespace ? '&namespace=' + encodeURIComponent( query.namespace ) : '' )
				+ ( query.invert ? '&invert=' + encodeURIComponent( query.invert ) : '' );

			if ( append !== '' ) {
				$( '#whatlinkshere-customfilters' ).find( 'a' ).each( function () {
					this.href += append;
				} );
			}
		} );
	} );
}

/**
 * Affiche un modèle Information sur la page de téléchargement de fichiers [[Spécial:Téléchargement]]
 * Voir aussi [[MediaWiki:Onlyifuploading.js]]
 */
if ( mw.config.get( 'wgCanonicalSpecialPageName' ) === 'Upload' ) {
	importScript( 'MediaWiki:Onlyifuploading.js' );
}

} // Fin du code concernant l'espace de nom 'Special'


// ESPACE DE NOM 'UTILISATEUR'
if ( mw.config.get( 'wgNamespaceNumber' ) === 2 ) {

/*
 * Fonctionnement du [[Modèle:Cadre à onglets]]
 * Le JavaScript principal se situe dans [[MediaWiki:Gadget-CadreOnglets.js]]
 */
mw.hook( 'wikipage.content' ).add( function ( $content ) {
	if ( $content.find( '.cadre_a_onglets' ).length ) {
		mw.loader.using( 'ext.gadget.CadreOnglets', function () {
			CadreOnglets_Init( $content );
		} );
	}
} );

} // Fin du code concernant l'espace de nom 'Utilisateur'


// ESPACE DE NOM 'RÉFÉRENCE'
if ( mw.config.get( 'wgNamespaceNumber' ) === 104 ) {

/*
 * Choix du mode d'affichage des références
 * @note L'ordre de cette liste doit correspondre a celui de Modèle:Édition !
 */

var addBibSubsetMenu = function ( $content ) {
	var $specialBib = $content.find( '#specialBib' );
	if ( !$specialBib.length ) {
		return;
	}

	// select subsection of special characters
	var chooseBibSubset = function ( s ) {
		$content.find( '.edition-Liste' ).css( 'display', s === 0 ? 'block' : 'none' );
		$content.find( '.edition-WikiNorme' ).css( 'display', s === 1 ? 'block' : 'none' );
		$content.find( '.edition-BibTeX' ).css( 'display', s === 2 ? 'block' : 'none' );
		$content.find( '.edition-ISBD' ).css( 'display', s === 3 ? 'block' : 'none' );
		$content.find( '.edition-ISO690' ).css( 'display', s === 4 ? 'block' : 'none' );
	};

	var $menu = $( '<select>' )
		.css( 'display', 'inline' )
		.change( function () {
			chooseBibSubset( this.selectedIndex );
		} )
		.append(
			$( '<option>' ).text( 'Liste' ),
			$( '<option>' ).text( 'WikiNorme' ),
			$( '<option>' ).text( 'BibTeX' ),
			$( '<option>' ).text( 'ISBD' ),
			$( '<option>' ).text( 'ISO690' )
		);

	$specialBib.append( $menu );

	/* default subset - try to use a cookie some day */
	chooseBibSubset( 0 );
};

mw.hook( 'wikipage.content' ).add( addBibSubsetMenu );

} // Fin du code concernant l'espace de nom 'Référence'