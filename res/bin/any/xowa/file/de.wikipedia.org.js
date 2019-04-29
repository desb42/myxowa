/* Jedes JavaScript hier wird für alle Benutzer für jede Seite geladen. */

/**
* Sortierung von Umlauten und ß auch ohne [[Vorlage:SortKey]] ermöglichen
* For jquery.tablesorter.js
*/
//mw.config.set( 'tableSorterCollation', {'Ä':'A', 'Ö':'O', 'Ü':'U', 'ä':'a', 'ö':'o', 'ü':'u', 'ß':'ss'} );

/**
* load the Edittools on [[Special:Upload]] and prefill the summary textarea
* Load pages: [[MediaWiki:Gadget-uploadtools.js]]
*/
if (mw.config.get( 'wgCanonicalSpecialPageName' ) === 'Upload') {
mw.loader.load('ext.gadget.uploadtools');
}

/**
* Nachrichten aus [[MediaWiki:watchlist-summary]] auf der Beobachtungliste ausblenden
* Load page: [[MediaWiki:Common.js/watchlist.js]]
*/
if (mw.config.get( 'wgCanonicalSpecialPageName' ) === 'Watchlist') {
mw.loader.load('//de.wikipedia.org/w/index.php?title=MediaWiki:Common.js/watchlist.js&action=raw&ctype=text/javascript');
}

/*
## ProjektLinks ##
by Skript von [[user:Merlissimo]] (Idee basierend auf http://de.wiktionary.org/wiki/MediaWiki:Common.js von [[User:Pathoschild]] und [[wikt:de:User:Melancholie]])
erzeugt Sitebar-Interwiki zu Schwesterprojekten aufgrund von Vorlage [[Vorlage:InterProjekt]]
siehe auch Feature-Request [[bugzilla:708]]
*/
if( mw.config.get( 'wgNamespaceNumber' ) > 0 ) {
mw.loader.using( [ 'mediawiki.util' ], function() { $( function() {
var iProject = $( '#interProject' );
if( !iProject.length ) {
return;
}
var sistersibling = $( '#p-lang' );
if( !sistersibling.length ) {
sistersibling = $( '#p-tb' );
}
if( !sistersibling.length ) {
return;
}
//Link auf Parennode des Portletmenues
var sisterparent = sistersibling.parent();

//Erzeuge neues Portletmenue
var sisterprojectnav = $( document.createElement( 'div' ) );
sisterprojectnav.attr( 'id', 'p-sisterprojects' );
sisterprojectnav.attr( 'class', sistersibling.attr( 'class' ) );
var header = $( document.createElement( 'h3' ) );
header.text( $( '#sisterProjects:first' ).text() );
sisterprojectnav.append( header );
var portletDiv = $( document.createElement( 'div' ) );
var sistersiblingsub = sistersibling.find( 'div:first' );
if( sistersiblingsub.length ) {
portletDiv.attr( 'class', sistersiblingsub.attr( 'class' ) );
} else {
portletDiv.attr( 'class', 'pBody' );
}
sisterprojectnav.append( portletDiv );

//Wenn möglich vor den Interwikis einfügen
if ( sisterparent.has( '#p-lang' ).length ) {
sisterprojectnav.insertBefore( '#p-lang' );
} else {
sisterparent.append( sisterprojectnav );
}

//Schwesterlinks ermitteln und einfügen
iProject.find( 'a' ).each( function() {
$this = $( this );
var sistername = $this.text();
mw.util.addPortletLink(
'p-sisterprojects',
$this.attr( 'href' ) + '?uselang=' + mw.util.rawurlencode( mw.config.get( 'wgUserLanguage' ) ),
sistername,
'sister-' + sistername,
sistername
);
});
})});
}

/**
* Fügt einen Link "Alle Sprachen" auf der Hauptseite unter die Sprachverweise hinzu
*/
if( mw.config.get( 'wgIsMainPage' ) ) {
mw.loader.using( [ 'mediawiki.util' ], function() { $( function () {
mw.util.addPortletLink(
'p-lang',
mw.util.getUrl( 'Wikipedia:Sprachen' ),
'Alle Sprachen',
'interwiki-completelist',
'Liste aller Sprachversionen von Wikipedia'
);
})});
}

/**
* force the loading of another JavaScript file
* Deprecated function, function alias kept for backward compatibility
* mw.log.deprecate since 08.11.2013
*/
//mw.log.deprecate( window, 'includePage', importScript, 'includePage ist veraltet, verwende stattdessen importScript' );

//================================================================================
//*** Dynamic Navigation Bars

// set up max count of Navigation Bars on page,
// if there are more, all will be hidden
// mw.user.options.set( 'NavigationBarShowDefault', 0 ); // all bars will be hidden
// mw.user.options.set( 'NavigationBarShowDefault', 1 ); // on pages with more than 1 bar all bars will be hidden

// adds show/hide-button to navigation bars
// using 'jquery.makeCollapsible': for messages
// using 'user', 'mediawiki.user', 'user.options': wait for overrides in user.js
//mw.loader.using( [ 'jquery.makeCollapsible', 'user', 'mediawiki.user', 'user.options' ], function() { 
mw.hook( 'wikipage.content' ).add( 	function( $content ) {
	// allow setting NavigationBarShowDefault
//	var showDefaultCount = mw.user.options.get( 'NavigationBarShowDefault',
//		typeof NavigationBarShowDefault !== 'undefined' ? NavigationBarShowDefault : 1 );
	var showDefaultCount;
	// allow user overrides for b/c
	var textHide = typeof NavigationBarHide === 'string' ? NavigationBarHide : mw.msg( 'collapsible-collapse' );
	var textShow = typeof NavigationBarShow === 'string' ? NavigationBarShow : mw.msg( 'collapsible-expand' );

	// shows and hides content and picture (if available) of navigation bars
	// Parameters:
	//     indexNavigationBar: the index of navigation bar to be toggled
	function toggleNavigationBar(NavToggle, NavFrame)
	{
		if (!NavFrame || !NavToggle) {
			return false;
		}

		// if shown now
		if (NavToggle.firstChild.data === textHide) {
			for (
					var NavChild = NavFrame.firstChild;
					NavChild !== null;
					NavChild = NavChild.nextSibling
				) {
				if (NavChild.className === 'NavPic' || NavChild.className === 'NavContent') {
					NavChild.style.display = 'none';
				}
			}
			NavToggle.firstChild.data = textShow;

		// if hidden now
		} else if (NavToggle.firstChild.data === textShow) {
			for (
					var NavChild = NavFrame.firstChild;
					NavChild !== null;
					NavChild = NavChild.nextSibling
				) {
				if (NavChild.className === 'NavPic' || NavChild.className === 'NavContent') {
					NavChild.style.display = 'block';
				}
			}
			NavToggle.firstChild.data = textHide;
		}
	}

	function toggleNavigationBarFunction(NavToggle, NavFrame) {
		return function() {
			toggleNavigationBar(NavToggle, NavFrame);
			return false;
		};
	}
	// iterate over all NavFrames
	var NavFrames = $content.find( 'div.NavFrame' );

	// if more Navigation Bars found and not template namespace than Default: hide all
	var initiallyToggle	= showDefaultCount < NavFrames.length && mw.config.get( 'wgNamespaceNumber' ) !== 10;
	for (var i=0; i<NavFrames.length; i++) {
		var NavFrame = NavFrames[i];
		var NavToggle = document.createElement("a");
		NavToggle.className = 'NavToggle';
		NavToggle.setAttribute('href', '#');

		var NavToggleText = document.createTextNode(textHide);
		NavToggle.appendChild(NavToggleText);

		// add NavToggle-Button as first div-element
		// in < div class="NavFrame" >
		NavFrame.insertBefore(NavToggle, NavFrame.firstChild);

		NavToggle.onclick = toggleNavigationBarFunction(NavToggle, NavFrame);
		if (initiallyToggle) {
			toggleNavigationBar(NavToggle, NavFrame);
		} else { // make sure that 'display' is always set for every NavFrame
			for (
					var NavChild = NavFrame.firstChild;
					NavChild !== null;
					NavChild = NavChild.nextSibling
				) {
				if (NavChild.className === 'NavPic' || NavChild.className === 'NavContent') {
					NavChild.style.display = 'block';
				}
			}
		}
	}
})
//});

//================================================================================

/** Skript für [[Vorlage:Galerie]] */
$( function() {
if (document.URL.match(/printable/g)) return;

function toggleImageFunction(group,  remindex, shwindex) {
return function() {
document.getElementById("ImageGroupsGr" + group + "Im" + remindex).style["display"] = "none";
document.getElementById("ImageGroupsGr" + group + "Im" + shwindex).style["display"] = "block";
return false;
};
}

var divs = document.getElementsByTagName("div");
var i = 0, j = 0;
var units, search;
var currentimage;
var UnitNode;
for (i = 0; i < divs.length; i++) {
if (divs[i].className !== "ImageGroup") { continue; }
UnitNode = undefined;
search = divs[i].getElementsByTagName("div");
for (j = 0; j < search.length; j++) {
if (search[j].className !== "ImageGroupUnits") { continue; }
UnitNode=search[j];
break;
}
if (UnitNode === undefined) { continue; }
units = [];
for (j = 0 ; j < UnitNode.childNodes.length ; j++ ) {
var temp = UnitNode.childNodes[j];
if (temp.className === "center") { units.push(temp); }
}
var rightlink = undefined;
var commentText = undefined;
for (j = 0; j < units.length; j++) {
currentimage = units[j];
currentimage.id = "ImageGroupsGr" + i + "Im" + j;
var leftlink = document.createElement("a");
if (commentText !== undefined) {
leftlink.setAttribute("title", commentText);
}
var comment;
if (typeof(currentimage.getAttribute("title")) !== "string") {
commentText = (j+1) + "/" + units.length;
comment = document.createElement("tt").appendChild(document.createTextNode("("+ commentText + ")"));
} else {
commentText = currentimage.getAttribute("title");
comment = document.createElement("span").appendChild(document.createTextNode(commentText));
currentimage.removeAttribute("title");
}
if(rightlink !== undefined) {
rightlink.setAttribute("title", commentText);
}
var imghead = document.createElement("div");
rightlink = document.createElement("a");
if (j !== 0) {
leftlink.href = "#";
leftlink.onclick = toggleImageFunction(i, j, j-1);
leftlink.appendChild(document.createTextNode("?"));
}
if (j !== units.length - 1) {
rightlink.href = "#";
rightlink.onclick = toggleImageFunction(i, j, j+1);
rightlink.appendChild(document.createTextNode("?"));
}
imghead.style["fontSize"] = "110%";
imghead.style["fontweight"] = "bold";
imghead.appendChild(leftlink);
imghead.appendChild(document.createTextNode("\xA0"));
imghead.appendChild(comment);
imghead.appendChild(document.createTextNode("\xA0"));
imghead.appendChild(rightlink);
if (units.length > 1) {
currentimage.insertBefore(imghead,currentimage.childNodes[0]);
}
if (j !== 0) {
currentimage.style["display"] = "none";
}
}
}
});

/**
* Fügt eine Betreffzeile auf leeren Artikel-Diskussionsseiten ein
*/
if( mw.config.get( 'wgNamespaceNumber' ) === 0 || mw.config.get( 'wgNamespaceNumber' ) === 1 ) {
$(function() {
$( '#ca-talk.new a' ).attr( 'href', function( index, attr ) {
return attr + '&section=new';
});
});
}

/**
* Fügt bei SVG-Grafiken Links zu gerenderten PNGs in verschiedenen Breiten hinzu
*/
if (mw.config.get( 'wgNamespaceNumber' ) === 6) {
$( function() {
var file = $( '#file' ); // might fail if MediaWiki can't render the SVG
if( file.length && mw.config.get( 'wgIsArticle' ) && mw.config.get( 'wgTitle' ).match( /\.svg$/i ) ) {
var thumbsrc = file.find( 'img' ).attr( 'src' );
if( !thumbsrc ) {
return;
}

var svgAltSize = function( w, title ) {
var path = thumbsrc.replace( /\/\d+(px-[^\/]+$)/, "/" + w + "$1" );
var a = $( document.createElement("a") );
a.attr( 'href', path );
a.text( title );
return a;
};

var p = $( document.createElement("p") );
p.addClass( "SVGThumbs" );
p.append( document.createTextNode( "Aus SVG automatisch erzeugte PNG-Grafiken in verschiedenen Auflösungen"+": " ) );
var l = [ 200, 500, 1000, 2000 ];
for( var i = 0; i < l.length; i++ ) {
if( i !== 0 ) {
p.append( document.createTextNode( ", " ) );
}
p.append( svgAltSize( l[i], l[i] + "px" ) );
}
p.append( document.createTextNode( "." ) );
$( file.parent() ).find( 'div.fullMedia' ).append( p );
}
});
}

/**
* Ändere den Spenden-Link im Sidebar für Besucher aus Deutschland
*/
$( function() {
if ( typeof( Geo ) === "object" && Geo.country === 'DE' && mw.config.get( 'wgUserLanguage' ) === 'de' ) {
var baseUrl = 'https://spenden.wikimedia.de/';
var queryString = $.param({
'piwik_campaign': 'de.wikipedia.org',
'piwik_kwd': 'sidebar',
'language': mw.config.get( 'wgUserLanguage' ),
'country': Geo.country
});
$("#n-sitesupport a").attr("href", baseUrl + '?' + queryString);
}
});

/**
* erzeuge einen "Neuen Abschnitt"-Link an der letzten Überschrift
*/
//mw.loader.using( [ 'jquery.accessKeyLabel' ], function() { 
$( function() {
var newSectionLink = $( '#ca-addsection a' );
if( newSectionLink.length ) {
var link = newSectionLink.clone(); //create a copy
//avoid duplicate accesskey
link.removeAttr( 'accesskey' ).updateTooltipAccessKeys();
//add it within the brackets
var lastEditsectionLink = $( 'span.mw-editsection:last a:last' );
lastEditsectionLink.after( link );
lastEditsectionLink.after( ' | ' ); //see [[MediaWiki:Pipe-separator]]
}
})
//});

/**
* Entferne [[Vorlage:Anker]] aus der Zusammenfassungszeile,
* damit diese beim generieren der Auto-Zusammenfassung nicht das Linkziel mit beeinflusst
*/
if ( mw.config.get( 'wgAction' ) === 'edit' ) {
$( function() {
$( '#wpSummary' ).val( function( i, val ) {
//Nur aktiv werden, wenn es auch eine Autozusammenfassung gibt
if( val.length <= 2 || val.substring( 0, 2 ) !== '/*' ) {
return val;
}
return val.replace( /\{\{[\s_]*:?[\s_]*(?:(?:Template|Vorlage)[\s_]*:[\s_]*)?Anker[\s_]*\|[^}]*\}\}\s*/gi, '' );
});
});
}
