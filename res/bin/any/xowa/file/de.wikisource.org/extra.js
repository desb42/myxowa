// add some xowa specific code to move the image on Page (de:Page: == 102)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 102 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );

 /*******************************************************************************************/
 /* <pre><nowiki>  MediaWiki:PageviewCounter.js                                             */
 /*-----------------------------------------------------------------------------------------*/
 /* ProofreadPage Extension for Images, Djvu and Pdf                                        */
 /* Author : ThomasV, Xarax                                                                 */
 /*******************************************************************************************/
 
function moveheaders(){
 
	var pq = document.getElementById("pagequality_message");
	if(!pq) pq = document.getElementById("pagequality");
	var pr_bstand = document.getElementById("pr_bstand");
	if(pq && pr_bstand) pr_bstand.appendChild(pq.removeChild(pq.firstChild));
 
	var i = document.getElementById("pr_container");
	if (!i) { return; }
 
	//i.childNodes[0].width = self.proofreadPageWidth/2;
 
	var a = document.getElementById("content");
	var c = document.getElementById("catlinks");
	if (c) var b = c.parentNode;
	if (b && a) a.appendChild(b.removeChild(c));
 
    if(!proofreadPageIsEdit) {
	var s = document.getElementById("bstand");
	if (s) {
		var t = document.getElementById("textBoxTable");
		t.parentNode.insertBefore(s, t);
	}
 
	s = document.getElementById("zitierhilfe");
	if (s) {
		t = document.getElementById("textBoxTable");
		t.parentNode.insertBefore(s, t.nextSibling);
	}
    }
 
    if (!wgCurRevisionId && wgAction == "view" && self.ifEmptyEdit) {
        t = document.getElementById("ca-edit");
        if (t) window.location.href = t.firstChild.href;
    }
}
 
$(moveheaders);
 
/* removed rest */