/*jshint sub:true, scripturl:true, maxerr:1000*/
/************************************************************/
/* Fonctions générales (pallient les limites de javascript) */
/************************************************************/

 
 
/**
 * Traitement des classes 
 * Javascript ne fournit actuellement que .className qui est extrêmement limité
 * Utiliser http://api.jquery.com/category/manipulation/class-attribute/
 */
 
function isClass(element, classe) {
    return hasClass(element, classe);
}
 
function whichClass(element, classes) {
    var s=" "+element.className+" ";
    for(var i=0;i<classes.length;i++)
        if (s.indexOf(" "+classes[i]+" ")>=0) return i;
    return -1;
}
 
function hasClass(node, className) {
  if (node.className == className) {
    return true;
  }
  var reg = new RegExp('(^| )'+ className +'($| )');
  if (reg.test(node.className)) {
    return true;
  }
  return false;
}
 
function addClass(node, className) {
    if (hasClass(node, className)) {
        return false;
    }
    var cache = node.className;
    if (cache) {
        node.className = cache + ' ' + className;
    } else {
        node.className = className;
    }
    return true;
}
 
function removeClass(node, className) {
  if (!hasClass(node, className)) {
    return false;
  }
  node.className = eregReplace('(^|\\s+)'+ className +'($|\\s+)', ' ', node.className);
  return true;
}
 
function eregReplace(search, replace, subject) {
    return subject.replace(new RegExp(search,'g'), replace);
}

if(typeof ws == 'undefined') {
    ws = {};
}


//import du widget "citer le livre"
//if( $('div#citeBox').length === 0) {
//    mw.loader.load('//fr.wikisource.org/w/index.php?title=MediaWiki:Wikisource.citeBox.js&action=raw&ctype=text/javascript');
//}

/*********************
Indicateurs de qualité
Auteur: ThomasV
*********************/

var q0  = "/xowa/fsys/file/upload.wikimedia.org/wikipedia/commons/8/8f/00%25.png";
var q25 = "/xowa/fsys/file/upload.wikimedia.org/wikipedia/commons/5/5b/25%25.png";
var q50 = "/xowa/fsys/file/upload.wikimedia.org/wikipedia/commons/3/3a/50%25.png";
var q75 = "/xowa/fsys/file/upload.wikimedia.org/wikipedia/commons/c/cd/75%25.png";
var q100 = "/xowa/fsys/file/upload.wikimedia.org/wikipedia/commons/6/64/100%25.png";
var qvalid = "/xowa/fsys/file/upload.wikimedia.org/wikipedia/commons/thumb/7/79/Mozilla.svg/10px-Mozilla.svg.png";

function textQuality() {
        if ($.inArray(mw.config.get('skin'), [ 'monobook', 'vector' ]) == -1)
                return;

	if ($.inArray(mw.config.get('wgAction'), [ 'view', 'submit', 'purge' ]) == -1)
                return;

	if (mw.config.get('wgNamespaceNumber'))
                return;

	var a = document.getElementById("ca-nstab-main");
        if (!a)
                return;

	var pr_q = document.getElementById("pr_index");
	if( pr_q ) {
                /* Modèle:Index présent. */
		href = pr_q.firstChild;
                if (!href)
                    return;
		pr_q.removeChild(href);
		href.firstChild.innerHTML = "Source";
		href.setAttribute("title", "Source de ce texte (fac-simile)");
		var new_li = document.createElement("li");
		new_li.appendChild(href); 
		a.parentNode.insertBefore(new_li, a.nextSibling);
	} else {
                /* Porter depuis monobook.js, est-ce encore utile et qu'est-ce ? */
		if( mw.config.get('skin') == 'monobook' && !document.getElementById("pr_page")) {
			var c = document.getElementById("contentSub");
                        /* FIXME: très très mauvais */
			c.innerHTML = c.innerHTML + "<span></span>";
		}

		var q = document.getElementById("textquality");
		if (q) {
			var new_img = document.createElement("img");
			var t = q.className.split(' ')[0];
			switch (t) {
				case '_200':
					new_img.setAttribute("src", qvalid);
					break;
				case '_25':
					new_img.setAttribute("src", q25);
					break;
				case '_50':
					new_img.setAttribute("src", q50);
					break;
				case '_75':
					new_img.setAttribute("src", q75);
					break;
				case '_100':
					new_img.setAttribute("src", q100);
					break;
			}
			new_img.setAttribute("title", "état d'avancement : "+t.substring(1,t.length)+'%');
                        if (mw.config.get('skin') == 'vector')
        			a.firstChild.firstChild.appendChild(new_img);
                        else /* monobook */
        			a.firstChild.appendChild(new_img);
		}
	}
}

$(textQuality);

/*
 * Typographie : même lorsque l'on travaille en mode création de page
 * il n'est pas sur de changer aveuglément tout le texte, d'où le
 * le split du texte en fragment où les regexps sont appliqués
 */
function common_typographie_fragment(text, typo_def) {
    var lng = typo_def.length;
    if (lng % 2 !== 0)
        return text;  /* FIXME, il faut lever une exception ici ? */
    for (var i = 0; i < lng; i += 2) {
        search = new RegExp(typo_def[i + 0], "g");
        text = text.replace(search, typo_def[i + 1]);
    }
    return text;
}

function common_typographie(text, typo_def) {
    var new_text = '';
    var last_match = 0;
    // Split the text in part which are safe to transform, e.g we don't want
    // to apply common_typographie_fragment on a <math>.*</math> or a &nbsp; etc.
    var splitter = new RegExp("<math>.*</math>|<[a-zA-z0-9 =\"']>|[</[a-zA-z0-9 =\"']+>|style=\".*\"|&nbsp;|&mdash;|<!--.*-->|\n:[:]*|\n;[;]*|[[][[].*]]", "gm");
    while ((result = splitter.exec(text)) != null) {
        new_text += common_typographie_fragment(text.slice(last_match, splitter.lastIndex - result[0].length), typo_def);
        new_text += result;
        last_match = splitter.lastIndex;
    }
    new_text += common_typographie_fragment(text.slice(last_match), typo_def);
    return new_text;
}

function common_typographie_textbox(pagename_filter, typo_def) {
    if (mw.config.get('wgNamespaceNumber') != 104)
        return;

    if (!$('.mw-newarticletext').length && !$('.mw-newarticletextanon').length) {
        return;
    }

    var wpTextbox1 = document.getElementById("wpTextbox1");
    if (!wpTextbox1)
        return;

    typo_def = [
        "([^'])'([^'])", "$1’$2",
        "([^ ])([;:?!])", "$1 $2",
        "\n\n- ", "\n\n— "
    ];
 
    wpTextbox1.value = common_typographie(wpTextbox1.value, typo_def);
}

$(common_typographie_textbox);


/* traduction des messages javascript*/
self.ws_messages = { 
	'optlist':'Options d’affichage',
	'hide_page_numbers':'Liens vers les pages',
	'show_page_numbers':'Liens vers les pages',
	'layout':'Maquette',

	'author':'Auteur',
	'translator':'Traducteur', 
	'editor':'Editeur_scientifique', 
	'publisher':'Editeur', 
	'place':'Lieu', 
	'volume':'Volume', 
	'school':'School', /*ne pas traduire*/
	'book':'Livre', 
	'collection':'Recueil', 
	'journal':'Journal ou revue', 
	'phdthesis':'Thèse, rapport', 
	'dictionary':'Dictionnaire, encyclopédie, ouvrage de référence', 


	'progress':'Avancement', 
	'progress_T':'Terminé', 
	'progress_V':'À valider', 
	'progress_C':'À corriger', 
	'progress_MS':'Texte prêt à être découpé (match & split)', 
	'progress_OCR':'Ajouter une couche texte OCR', 
	'progress_X':'Source incomplète (extrait) ou compilation de sources différentes', 
	'progress_L':'Fichier défectueux (lacunes, pages dans le désordre, etc)',
	'progress_D':'Doublon, un autre fichier existe sur lequel il est préférable de travailler',

	'matching':'appariement en cours',
	'splitting':'découpage en cours',

	'corr_list':"Liste des corrections apportées à cette page",
	'corr_link':"Coquilles",
	'corr_one':"Une coquille </a> a été corrigée.",
	'corr_many':" coquilles</a> ont été corrigées.",
	'corr_close':'Fermer.',

	'iwtrans':'Son texte vient de', 
	'iwtrans2':'Son texte vient d’autres sous-domaines de Wikisource.'
};

self.ws_messages['progress_select'] = 
    "<option value='' selected=true></option>" + 
    "<option value='T'>"  + self.ws_messages['progress_T']   + "</option>" +
    "<option value='V'>"  + self.ws_messages['progress_V']   + "</option>" +
    "<option value='C'>"  + self.ws_messages['progress_C']   + "</option>" +
    "<option value='MS'>" + self.ws_messages['progress_MS']  + "</option>" +
    "<option value='OCR'>"+ self.ws_messages['progress_OCR'] + "</option>" +
    "<option value='X'>"  + self.ws_messages['progress_X']   + "</option>" +
    "<option value='L'>"  + self.ws_messages['progress_L']   + "</option>" +
    "<option value='D'>"  + self.ws_messages['progress_D']   + "</option>";

/* attributs de style dynamiques */
self.ws_layouts = {
 'Maquette 1':{'#text-wrap':"margin-left:3em;margin-right:3em;", 
      '#text-container':"position:relative;width:36em;margin:0px auto;" , 
      '#text':"text-align:justify;", 
      '#box-right':"position:absolute; right:-20em;top:0em;",
      '#box-toc':"position:absolute; right:-23em;top:0em; width:22em;",
      '.sidenote-right':"position:absolute; left:37em;width:15em;text-indent:0em;text-align:left;",
      '.sidenote-left':"position:absolute; right:37em;width:15em;text-indent:0em;text-align:right;",
      '.editsection':"display:none",
      '.headerlabel':"display:none",
      '.headertemplate-author':"font-size:100%",
      '.headertemplate-title':"font-size:120%;",
      '.headertemplate-reference':"font-size:90%",
      '#headertemplate':"" },
 'Maquette 2':{'#text-wrap':"margin-left:3em", 
      '#text-container':"" , 
      '#text':"text-align:justify;width:auto;", 
      '#box-right':"float:right;",
      '#box-toc':"margin:auto;width:100%;",
      '.sidenote-right':"float:right;margin:0.5em;padding:3px;border:solid 1px gray;max-width:9em;text-indent:0em;text-align:left;",
      '.sidenote-left':"float:left;margin:0.5em;padding:3px;border:solid 1px gray;max-width:9em;text-indent:0em;text-align:left;",
      '.editsection':"display:none",
      '.headerlabel':"display:none",
      '.headertemplate-author':"font-size:100%",
      '.headertemplate-title':"font-size:120%;",
      '.headertemplate-reference':"font-size:90%",
      '#headertemplate':"" },
 'Maquette 3':{'#text-wrap':"margin-left:3em",
      '#text-container':"position:relative; min-width:60em; float:left; width:100%; margin-right:-23em;" , 
      '#text':"text-align:justify;margin-right:23em; text-indent:0em; padding-left:0px; padding-right:0px;width:auto;position:relative;",
      '#box-right':"float:right;",
      '#box-toc':"margin:auto;width:100%;",
      '.sidenote-right':"position:absolute; right:-16em; width:15em; background-color:#eeeeee;text-indent:0em;text-align:left;line-height:normal;",
      '.sidenote-left': "position:absolute; right:-16em; width:15em; background-color:#eeeeee;text-indent:0em;text-align:left;line-height:normal;",
      '.editsection':"display:none",
      '.headerlabel':"display:inline",
      '.headertemplate-author':"font-size:100%;",
      '.headertemplate-title':"font-size:100%;",
      '.headertemplate-reference':"font-size:100%;",
      '#headertemplate':"position:absolute; top:0em; right:-23em; width:21em;float:right; text-align:left;" }
};

function ws_msg(name) {
   var m = self.ws_messages[name];
   if(m) return m; else return name;
}

/*var old_wgserver = '//wikisource.org';
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:Base.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:OCR.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:Hocr.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:PageNumbers.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:Corrections.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:DisplayFooter.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:InterWikiTransclusion.js&action=raw&ctype=text/javascript'); 
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:IndexForm.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:Dictionary.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:RegexpButton.js&action=raw&ctype=text/javascript');
mw.loader.load(old_wgserver + '/w/index.php?title=MediaWiki:DoubleWiki.js&action=raw&ctype=text/javascript');
*/
/* ----------------- MediaWiki:PageNumbers.js&action=raw&ctype=text/javascript ------------------- */
/*********************************
* page numbers for proofreadpage
* by [[user:ThomasV]]
**********************************/


// Until we can use dependencies to ensure load of Base.js first, we need this,
// window.ws_msg and window.get_opt_list in this script.
if(!self.ws_messages) self.ws_messages = { };

function ws_msg(name) {
   var m = self.ws_messages[name];
   if(m) return m; else return name;
}

function get_optlist() {
	var optlist = document.getElementById("optlist");
	if(!optlist) {
		var displayOptions = document.createElement("div");
		if (mw.config.get('skin')==='vector') {
			displayOptions.className = "portal collapsed";
			cl="body";
		} else {
			displayOptions.className = "portlet";
			cl="pBody";
		}
		displayOptions.innerHTML = '<h3>' + ws_msg('optlist') + '<\/h3><div class="'+cl+'"><ul id="optlist"></ul><\/div>';
                var ptb = document.getElementById("p-tb");
                ptb.parentNode.insertBefore(displayOptions,ptb);
                displayOptions.setAttribute("id","p-displayOptions");
                displayOptions.id="p-displayOptions"; /* */
		optlist = document.getElementById("optlist");
	}
	return optlist;
}

var layout_num = 0;

function toggle_layout() {
  var n=0, key;
  for( key in self.ws_layouts ) n++;
  layout_num = (layout_num+1) % n;
  mw.cookie.set("layout",""+layout_num);
  set_layout(layout_num);
}

if(!self.ws_layouts) self.ws_layouts = {};

function set_layout(layout_num) {
  var d, css, i=0, key;
  for( key in self.ws_layouts ) { if(i==layout_num) layout_name = key; i++; }
  d = self.ws_layouts[layout_name];
  function setStyle(){
     this.style.cssText = css;
  }
  for( key in d ) {
     css = d[key];
     if( key.substring(0,1) === '.' || key.substring(0,1) == '#' ) {
        $(key).each(setStyle);
     }
  }
  o_a = document.getElementById("option-textLayout");
  if(o_a) o_a.innerHTML = "<a href='javascript:toggle_layout();'>"+layout_name+"</a>";

  refresh_pagenumbers();
}

function add_page_container(){
        var i, t;
        if($.inArray(mw.config.get('wgNamespaceNumber'), [ 0, 2 ]) === -1) return; 
        var id = null;
        if($.inArray(mw.config.get('wgAction'), ["view", "purge"]) !== -1) id = "#bodyContent"; 
        else if(mw.config.get('wgAction')=="submit") id = "#wikiPreview"; 
        if (!id) return;
        if( document.getElementById("text-wrap") ) return;

        if( !mw.config.get('proofreadpage_source_href') && !$("span.pagenum").length ) return;

	$("div.text, div.lefttext, div.centertext, div.indented-page, div.prose")
		.removeClass();

        $(id).prepend($('#contentSub'));
        $('#contentSub').nextUntil('#catlinks').wrapAll("<div id='text-wrap'><div id='text-container'><div id='text'></div></div></div><div style='clear:both'></div>");
}

function set_layout_by_name(name){
	var key;
    if (self.ws_layouts[name]===undefined) return; //doesn't exists
 
    var i=0; for( key in self.ws_layouts ) { if(key==name) break; i++; }
    layout_num = i;
    set_layout(i);
}

function init_page_layout() {
	var key;
    if(self.debug_page_layout) return;

    if(document.URL.indexOf("match=") > 0 ) return;
    if(document.URL.indexOf("diff=") > 0 ) return;

    var k=false; 
    for( key in self.ws_layouts ) { k = true;}
    if(!k) return;

    add_page_container();

    if(document.getElementById("text-wrap")) {
//			mw.loader.using( 'mediawiki.util', function() {
       var optlist = get_optlist();
       mw.util.addPortletLink ('p-displayOptions', 'javascript:toggle_layout();', ws_msg('layout'), 'option-textLayout', '' );

        layout_num = 0;
        /* FIXME: the cookie also must store the selected layout by name, but it must be backward compatible with the storing by integer */
        layout = mw.cookie.get("layout") ;
        if (layout)
            layout_num = parseInt( layout );
        if(!layout_num || layout_num == NaN) layout_num = 0;
        set_layout(layout_num);
        
        //We make sure to stay to the hash (fixes a bug in Chrome and Firefox)
        var hash = window.location.hash;
        if(hash.indexOf("#") != -1) {
            window.location.href = hash;
        }
//	});
    }
        set_layout(0);
}

function hide_pagenumbers(){
   $('div.pagenumber, span.pagenumber').hide();
   o_a = document.getElementById("option-pageNumbers");
   if(o_a) {
     o_a.innerHTML = "<a href='javascript:show_pagenumbers();'>"+ws_msg('show_page_numbers')+"</a>"; 
   }
   mw.cookie.set("pagenum","0");
}

function show_pagenumbers(){
   $('div.pagenumber, span.pagenumber').show();
   o_a = document.getElementById("option-pageNumbers");
   if(o_a){
     o_a.innerHTML = "<a href='javascript:hide_pagenumbers();'>"+ws_msg('hide_page_numbers')+"</a>"; 
   }
   mw.cookie.set("pagenum","1");
}

function pagenum_over(ox,oy,prev_ox,prev_oy,h,w) {
  if(self.proofreadpage_disable_highlighting) return true;
  var ct = document.getElementById("ct-popup");
  if(!ct) return true;
  var dd = h/10;
  ct.style.top = (oy-h+dd)+"px";
  ct.style.height= "";

  ct.firstChild.style.width = (w-ox)+"px";
  ct.firstChild.nextSibling.style.height = (prev_oy-oy-h)+"px";
  ct.firstChild.nextSibling.nextSibling.style.width = prev_ox+"px";
  return true;

}

function pagenum_out(){
  if(self.proofreadpage_disable_highlighting) return true;
  var ct = document.getElementById("ct-popup");
  if(!ct) return true;
  ct.style.height= "0px";
  ct.firstChild.style.width = "0px";
  ct.firstChild.nextSibling.style.height = "0px";
  ct.firstChild.nextSibling.nextSibling.style.width = "0px";
  return true;
}



function init_page_numbers() {

	if(document.URL.indexOf("match=") > 0 ) return;

    self.pagenum_ml = $('span.pagenum').get();

    if( !mw.config.get('proofreadpage_source_href') || !self.pagenum_ml.length) return;

    var optlist = get_optlist();
    mw.util.addPortletLink ('p-displayOptions', 'javascript:hide_pagenumbers();', ws_msg('hide_page_numbers'), 'option-pageNumbers', '', 'n' );

    var cs = document.getElementById("mw-content-text");

    /* Measure the line height and the height of an empty span */
    var ct = document.createElement("div");
    ct.setAttribute("id","my-ct");
    cs.insertBefore(ct,cs.firstChild);
    var ss = document.createElement("div"); /*we need a div, not a span*/
    ss.innerHTML="&nbsp;<span></span>";     /*empty span following some text */
    ss.setAttribute("id","my-ss");
    cs.appendChild(ss);

    /* container for page numbers */
    var div_pagenums = document.createElement("div"); 
    div_pagenums.setAttribute("id","ct-pagenums");
    /* insert the container in the grandparent node, or parent, or self */
    var mcs = document.getElementById("text-wrap");
    if(!mcs) {
      if(cs.parentNode.parentNode.style.position=="relative") mcs = cs.parentNode.parentNode;
      else if(cs.parentNode.style.position=="relative") mcs = cs.parentNode; 
      else { mcs = cs; mcs.style.position = "relative"; }
    } 
    mcs.appendChild(div_pagenums);

    /* container for the highlight */
    var div_popup = document.createElement("div"); 
    var opacity="background-color:#000000;opacity:0.2;-ms-filter:alpha(opacity=20);filter:alpha(opacity=20);";
    div_popup.setAttribute("id","ct-popup");
    div_popup.style.cssText = "position:absolute;width:100%;";
    div_popup.innerHTML = "<div style=\""+opacity+"float:right;width:0px;\" >&nbsp;</div>"
      +"<div style=\""+opacity+"width:100%;height:0px;clear:both;\"></div>"
      +"<div style=\""+opacity+"width:0px;\">&nbsp;</div>";
    cs.appendChild(div_popup);

    refresh_pagenumbers();
}


function refresh_pagenumbers() {
    var ct = document.getElementById("my-ct");
    if(!ct) return;

    var ss = document.getElementById("my-ss");

    var cs = ct.parentNode;
    /* set it to relative because the highlight is of width 100% */
    cs.style.position = "relative"; /* fixme : this interacts with layouts */

    var lineheight = ss.offsetHeight; 
    var offset_h = ss.lastChild.offsetHeight;

    var linewidth = cs.offsetWidth; 
    var ct_o = $(ct).position();
    var ct_ox = ct_o.left; 
    var ct_oy = ct_o.top;
    var oo = $(ss).position(ss);
    var ox = oo.left - ct_ox; var oy = oo.top - ct_oy;

    var oh ="";
    for(var i=self.pagenum_ml.length-1; i>=0; i--) {
        var a = self.pagenum_ml[i];
        // Switch to URL encoding, except for hex-like sequences that would turn
        // into ASCII control characters.
        var num = a.id.replace(/\.([2-68-F][0-9A-F]|7[A-E])/g, "%$1");
        try {
            num = decodeURIComponent(num.replace(/_/g, " "));
        }
        catch (exc) {
            num = a.id;
        }
        var page = a.title;
        var pagetitle = decodeURI( page );
        var page_url = mw.config.get('wgArticlePath').replace("$1", encodeURIComponent( pagetitle.replace(/ /g,"_") ) );
        // encodeURIComponent encodes '/', which breaks subpages
        page_url = page_url.replace(/%2F/g, '/');
        var ll = a.parentNode.nextSibling;
        if(ll && ll.tagName=="A" && ll.className=="new") class_str=" class=\"new\" "; else class_str="";
        var link_str = "<a href=\""+page_url+"\"" + class_str + " title=\""+mw.html.escape(pagetitle)+"\">"+mw.html.escape(num)+"</a>";
        if(self.proofreadpage_numbers_inline){
                a.innerHTML = "&#x0020;<span class=\"pagenumber noprint\" style=\"color:#666666; display:inline; margin:0px; padding:0px;\">[<b>"+link_str+"</b>]</span>&#x0020;";
        }
        else { 
            prev_ox = ox;
            prev_oy = oy;
            a_o = $(a).position(); 
            ox = a_o.left - ct_ox;
            oy = a_o.top - ct_oy + offset_h;

            if(prev_oy - oy > 5) {
                  oh = oh + "<div class=\"pagenumber noprint\" onmouseover=\"pagenum_over("+ ox + "," + oy + "," + prev_ox + "," + prev_oy + "," + lineheight + "," + linewidth + ");\" onmouseout=\"pagenum_out();\" style=\"position:absolute; left:-4em; top:"+(oy+cs.offsetTop-lineheight)+"px; text-indent:0em; text-align:left; font-size:80%; font-weight:normal; font-style: normal;\">["+link_str+"]</div>";
            }
        }
     }
     if(!self.proofreadpage_numbers_inline) {
        var ct_pagenums = document.getElementById("ct-pagenums");
        ct_pagenums.innerHTML = oh;
    }
    pagenum_out();
    if( parseInt(mw.cookie.get("pagenum")) == 0 ) hide_pagenumbers();
}

if ($.inArray(mw.config.get('wgAction'), [ 'view', 'submit' ]) != -1) {
    //fixme : this is sensitive to order (detection of containers with "relative" position)
    //mw.loader.using('mediawiki.cookie', function() {
    	$(init_page_layout);
    	$(init_page_numbers);
    //});
}
/* ------------------------------------------------------------------------ */
/* Réécriture des titres
 *
 * Fonction utilisée par Modèle:TitreModifié et Modèle:Statut
 * 
 */
function rewritePageH1() {
      var realTitle = document.getElementById('RealTitle');
      var h1 = document.getElementsByTagName('h1')[0];
      if (realTitle && h1) {
        var titleText = realTitle.innerHTML;
        if (titleText == '') h1.style.display = 'none';
        else h1.innerHTML = titleText;
        realTitleBanner.style.display = 'none';
      }
}

$(rewritePageH1);


/*
Interwikiextra
Auteur:ThomasV et Tpt
*/
function menuExtra() {
    $(".interwiki-info").each(function(i, info) {
        var langLink = $("#p-lang .interwiki-" + info.id + " a");
        if(info.title == "(vo)") {
            langLink.after(' (<abbr title="Version originale">vo</abbr>)');
        } else {
            langLink.after(" " + info.title);
        }
    });
 
    //Autre versions
    if($(".AutreVersion").length) {
      var data = {
        sections: {
          version: {
            expanded: true,
            label: 'Autres versions',
            links: {}
          }
        }
      };
      $(".AutreVersion").each(function(i, info) {
        var p = info.title.indexOf("|");
        data.sections.version.links['interwiki-' + i] = {
          label: info.title.substr(p+1,this.title.length-1),
          href: '/wiki/' + encodeURIComponent( info.title.substr(0,p) )
        };
      });
      mw.panel(data);
    }

    //interprojets
    var interprojetsAAjouter = {};

    // extraction
    var interprojectNode = $( "#interProject" );
    if( interprojectNode.length !== 0 ) {
        interprojectNode.find( 'span' ).each( function( i, info ) {
            //on cherche l'id du site
            var idParts = $( this ).attr( 'id' ).split( '-' );
            if( idParts.length !== 2)
                return;
            var id = idParts[1];

            var link = $( this ).find( 'a' );
            interprojetsAAjouter['wb-otherproject-' + id + '-custom'] = {
                'label': link.text(),
                'href': link.attr('href'),
                'group': id,
                'class': 'wb-otherproject-link wb-otherproject-' + id
            };
        } );
    }

	//Wikidata item
	if( mw.config.get( 'wgWikibaseItemId' ) ) {
		interprojetsAAjouter['wb-otherproject-wikidata-item-custom'] = {
            'label': 'Élément Wikidata',
            'href': '//wikidata.org/wiki/' + mw.config.get( 'wgWikibaseItemId' ),
            'group': 'wikidata-item',
            'class': 'wb-otherproject-link wb-otherproject-wikidata'
        };
	}

    //ajout
    if( !$.isEmptyObject( interprojetsAAjouter ) ) {
        var autresProjectsNode = $( '#p-wikibase-otherprojects' );
        if( autresProjectsNode.length !== 0 ) {
            //on supprime les doublons
            $.each( interprojetsAAjouter, function( id, parameters ) {
                autresProjectsNode.find( '.wb-otherproject-' + parameters.group ).remove();
            } );
            mw.panel( {
                section: 'wikibase-otherprojects',
                links: interprojetsAAjouter
            } );
        } else {
            mw.panel( {
                sections: {
                    'wikibase-otherprojects': {
                        label: 'Autres projets',
                        links: interprojetsAAjouter
                    }
                }
            } );
        }
    }
}
$.when(
	$.ajax( {
	cache: true,
	url: mw.config.get('wgServer') + '/w/index.php?title=MediaWiki:MediaWiki.panel.js&action=raw&ctype=text/javascript'
	} ),
	$.ready
).done( menuExtra );


/*
Compatibilité du menu Option d’affichage avec Vector
Auteur : Tpt
*/
function VectorCompatibility() {
    if(document.getElementById("p-displayOptions")) {
        document.getElementById("p-displayOptions").childNodes[1].style.display = "block";
    }
}
$(VectorCompatibility);


// Remove "Auteur:" prefix in category, see /xowa/fr.wikisource.org/wiki/Cat%C3%A9gorie:Philosophes
$(function () {
   if (mw.config.get('wgNamespaceNumber') == 14) {
       $("li a[href^='/wiki/Auteur:']").each(function () {
           $(this).text($(this).text().substr("Auteur:".length));
       });
   }
});


function epubExportLink() {
   if ($.inArray("Bon pour export", mw.config.get('wgCategories')) >= 0 ) {
     mw.loader.using('mediawiki.util', function() {
     	mw.util.addPortletLink('p-coll-print_export', '//tools.wmflabs.org/wsexport/tool/book.php?lang=fr&format=epub&page=' + mw.config.get('wgPageName'), 'Télécharger en ePub', 'n-epubExport', 'Télécharger le livre au format ePub'); 
        $('#n-epubExport > a').attr('rel', 'nofollow');
        mw.util.addPortletLink('p-coll-print_export', '//tools.wmflabs.org/wsexport/tool/book.php?lang=fr&format=mobi&page=' + mw.config.get('wgPageName'), 'Télécharger en MOBI', 'n-mobiExport', 'Télécharger le livre au format MOBI'); 
        $('#n-mobiExport > a').attr('rel', 'nofollow');
     });
   }
}
$(epubExportLink);


/** 
 * Boîtes déroulantes
 *
 * Pour [[Modèle:Méta palette de navigation]]
 */
var autoCollapse = 2;
var collapseCaption = '[Enrouler]';
var expandCaption = '[Dérouler]';


function collapseTable( tableIndex ) {
  var Button = document.getElementById( "collapseButton" + tableIndex );
  var Table = document.getElementById( "collapsibleTable" + tableIndex );
  if ( !Table || !Button ) return false;
 
  var Rows = Table.getElementsByTagName( "tr" ); 
 
  if ( Button.firstChild.data == collapseCaption ) {
    for ( var i = 1; i < Rows.length; i++ ) {
      Rows[i].style.display = "none";
    }
    Button.firstChild.data = expandCaption;
  } else {
    for ( var i = 1; i < Rows.length; i++ ) {
      Rows[i].style.display = Rows[0].style.display;
    }
    Button.firstChild.data = collapseCaption;
  }
}
 
function createCollapseButtons() {
  var tableIndex = 0;
  var NavigationBoxes = {};
  var Tables = document.getElementsByTagName( "table" );
 
  for ( var i = 0; i < Tables.length; i++ ) {
    if ( hasClass( Tables[i], "collapsible" ) ) {
      NavigationBoxes[ tableIndex ] = Tables[i];
      Tables[i].setAttribute( "id", "collapsibleTable" + tableIndex );
 
      var Button     = document.createElement( "span" );
      var ButtonLink = document.createElement( "a" );
      var ButtonText = document.createTextNode( collapseCaption );
 
      Button.style.styleFloat = "right";
      Button.style.cssFloat = "right";
      Button.style.fontWeight = "normal";
      Button.style.textAlign = "right";
      Button.style.width = "6em";
 
      ButtonLink.setAttribute( "id", "collapseButton" + tableIndex );
      ButtonLink.setAttribute( "href", "javascript:collapseTable(" + tableIndex + ");" );
      ButtonLink.appendChild( ButtonText );
 
      Button.appendChild( ButtonLink );
 
      var Header = Tables[i].getElementsByTagName( "tr" )[0].getElementsByTagName( "th" )[0];
      /* only add button and increment count if there is a header row to work with */
      if (Header) {
        Header.insertBefore( Button, Header.childNodes[0] );
        tableIndex++;
      }
    }
  }
 
  for (var i = 0; i < tableIndex; i++) {
    if ( hasClass( NavigationBoxes[i], "collapsed" ) || ( tableIndex >= autoCollapse && hasClass( NavigationBoxes[i], "autocollapse" ) ) ) collapseTable( i );
  }
}
$(createCollapseButtons);
 
/**
 * Pour [[Modèle:Boîte déroulante]] 
 */
var NavigationBarShowDefault = 0;
 
function toggleNavigationBar(indexNavigationBar) {
  var NavToggle = document.getElementById("NavToggle" + indexNavigationBar);
  var NavFrame = document.getElementById("NavFrame" + indexNavigationBar);
 
  if (!NavFrame || !NavToggle) return;
 
  // surcharge des libellés dérouler/enrouler grâce a l'attribut title
  // exemple : title="[déroulade]/[enroulade]"
  var caption = [expandCaption, collapseCaption];
  if (NavFrame.title && NavFrame.title.length > 0) {
    caption = NavFrame.title.split("/");
    if (caption.length < 2) caption.push(collapseCaption);
  }
 
  // if shown now
  if (NavToggle.firstChild.data == caption[1]) {
    for ( var NavChild = NavFrame.firstChild; NavChild != null; NavChild = NavChild.nextSibling ) {
      if (hasClass(NavChild, 'NavPic')) NavChild.style.display = 'none';
      if (hasClass(NavChild, 'NavContent')) NavChild.style.display = 'none';
      if (hasClass(NavChild, 'NavToggle')) NavChild.firstChild.data = caption[0];
    }
 
  // if hidden now
  } else if (NavToggle.firstChild.data == caption[0]) {
    for ( var NavChild = NavFrame.firstChild; NavChild != null; NavChild = NavChild.nextSibling ) {
      if (hasClass(NavChild, 'NavPic')) NavChild.style.display = 'block';
      if (hasClass(NavChild, 'NavContent')) NavChild.style.display = 'block';
      if (hasClass(NavChild, 'NavToggle')) NavChild.firstChild.data = caption[1];
    }
  }
}
 
// adds show/hide-button to navigation bars
function createNavigationBarToggleButton() {
  var indexNavigationBar = 0;
  var NavFrame;
  // iterate over all < div >-elements
  for( var i=0; NavFrame = document.getElementsByTagName("div")[i]; i++ ) {
    // if found a navigation bar
    if (hasClass(NavFrame, "NavFrame")) {
      indexNavigationBar++;
      var NavToggle = document.createElement("a");
      NavToggle.className = 'NavToggle';
      NavToggle.setAttribute('id', 'NavToggle' + indexNavigationBar);
      NavToggle.setAttribute('href', 'javascript:toggleNavigationBar(' + indexNavigationBar + ');');
 
      // surcharge des libellés dérouler/enrouler grâce a l'attribut title
      var caption = collapseCaption;
      if (NavFrame.title && NavFrame.title.indexOf("/") > 0) {
         caption = NavFrame.title.split("/")[1];
      }
 
      var NavToggleText = document.createTextNode(caption);
      NavToggle.appendChild(NavToggleText);
 
      // add NavToggle-Button as first div-element 
      // in <div class="NavFrame">
      NavFrame.insertBefore( NavToggle, NavFrame.firstChild );
      NavFrame.setAttribute('id', 'NavFrame' + indexNavigationBar);
    }
  }
  // if more Navigation Bars found than Default: hide all
  if (NavigationBarShowDefault < indexNavigationBar) {
    for( var i=1; i<=indexNavigationBar; i++ ) {
      toggleNavigationBar(i);
    }
  }
}
$(createNavigationBarToggleButton);

/**
 * Menu de navigation, pour Modèle:Menu de navigation - user:Dodoïste
 */

$(function () { 
    // On cache les sous-menus 
    // sauf celui qui porte la classe "open_at_load" : 
    $(".navigation ul.subMenu:not('.open_at_load')").hide(); 
    // On sélectionne tous les items de liste portant la classe "toggleSubMenu" 
 
    // et on remplace l'élément span qu'ils contiennent par un lien : 
    $(".navigation li.toggleSubMenu span").each( function () { 
        // On stocke le contenu du span : 
        var TexteSpan = $(this).text(); 
        $(this).replaceWith('<a href="" class="subMenuA" title="Afficher le sous-menu">' + TexteSpan + '<\/a>') ; 
    } ) ; 
 
    // On modifie l'évènement "click" sur les liens dans les items de liste 
    // qui portent la classe "toggleSubMenu" : 
    $(".navigation li.toggleSubMenu > a").click( function () { 
        // Si le sous-menu était déjà ouvert, on le referme : 
        if ($(this).next("ul.subMenu:visible").length != 0) { 
            $(this).next("ul.subMenu").slideUp("normal", function () { $(this).parent().removeClass("open"); } ); 
        } 
        // Si le sous-menu est caché, on ferme les autres et on l'affiche : 
        else { 
            $(".navigation ul.subMenu").slideUp("normal", function () { $(this).parent().removeClass("open"); }); 
            $(this).next("ul.subMenu").slideDown("normal", function () { $(this).parent().addClass("open"); } ); 
        } 
        // On empêche le navigateur de suivre le lien : 
        return false; 
    }); 
 
} ) ;


/**
 ** construit un menu déroulant en dessous de la fenêtre d'édition pour MediaWiki:Edittools
**/
//importScript('MediaWiki:Gadget-Edittools.js');


// Cache le lien "Ajouter un sujet" dans les pages contenant le marqueur idoine,
// un <span id="suppression_lien_ajouter_un_sujet"></span> suffit.
$(function() {
    if ($('#suppression_lien_ajouter_un_sujet').length) {
        $('#ca-addsection').css('display', 'none');
        // Pas de raison de spliter sur ce type de page.
        $('#ca-split').css('display', 'none');
    }
});

/* Fonction dans l'extension, fichier proofread.js, adapté pour que lorsque
 * un utilisateur clique q3 alors que q3 est déjà le niveau, le nom de l'utilisateur
 * qui a marqué la page q3 ne soit pas changé, idem pour q4.
 */
/* -2 n'est pas un niveau de qualité valide. */
document.initial_proofreadpage_quality = -2;
document.initial_proofreadpage_username = "";
 
/* Quality buttons */
self.pr_add_quality = function( form, value ) {
	if (document.initial_proofreadpage_quality == -2) {
		document.initial_proofreadpage_quality = self.proofreadpage_quality;
                document.initial_proofreadpage_username = mw.config.get('wgUserName');
        }
	if (document.initial_proofreadpage_quality < 3 || value != document.initial_proofreadpage_quality)
		self.proofreadpage_username = proofreadPageUserName;
        else
                self.proofreadpage_username = document.initial_proofreadpage_username;
	self.proofreadpage_quality = value;
	var text = '';
	switch( value ) {
		case 0:
			text = mediaWiki.msg( 'proofreadpage_quality0_category' );
			break;
		case 1:
			text = mediaWiki.msg( 'proofreadpage_quality1_category' );
			break;
		case 2:
			text = mediaWiki.msg( 'proofreadpage_quality2_category' );
			break;
		case 3:
			text = mediaWiki.msg( 'proofreadpage_quality3_category' );
			break;
		case 4:
			text = mediaWiki.msg( 'proofreadpage_quality4_category' );
			break;
	}
	form.elements['wpSummary'].value = '/* ' + text + ' */ ';
	form.elements['wpProofreader'].value = self.proofreadpage_username;
};

/* Modèle:Titre sous-page */
$(function () {
    if ($('#titre-sous-page-seul').length) {
        var $titre =  $('#firstHeading');
        $titre.text($titre.text().split('/').slice(-1)[0]);
    }
});

/* DÉBUT DU CODE JAVASCRIPT DE "CADRE À ONGLETS"
    Fonctionnement du [[Modèle:Cadre à onglets]]
    Modèle implanté par User:Peleguer de http://ca.wikipedia.org
    Actualisé par User:Joanjoc de http://ca.wikipedia.org
    Traduction et adaptation User:Antaya de /xowa/fr.wikipedia.org
    Indépendance de classes CSS et nettoyage par User:Nemoi de /xowa/fr.wikipedia.org
*/
 
function CadreOngletInitN(){
 
  var Classeurs = $('div.classeur');
  for ( var i = 0; i < Classeurs.length; i++ ) {
      var Classeur = Classeurs[i];
 
      Classeur.setAttribute( "id", "classeur" + i );
 
      var vOgIni = -1; // pour connaître l’onglet renseigné
 
      var Onglets = $(Classeur).children("div").eq(0).children("div");
      var Feuillets = $(Classeur).children("div").eq(1).children("div");
 
      for ( var j = 0; j < Onglets.length; j++) {
        var Onglet = Onglets[j];
        var Feuillet = Feuillets[j];
 
        Onglet.setAttribute( "id", "classeur" + i + "onglet" + j );
        Feuillet.setAttribute( "id", "classeur" + i + "feuillet" + j );
        Onglet.onclick = CadreOngletVoirOngletN;
 
        if ( hasClass( Onglet, "ongletBoutonSel") ) vOgIni=j; 
      }
 
      // inutile sauf dans le cas où l’onglet de départ est *mal* renseigné
      if (vOgIni == -1) { 
        var vOgIni = Math.floor((Onglets.length)*Math.random()) ;
        document.getElementById("classeur"+i+"feuillet"+vOgIni).style.display = "block";
        document.getElementById("classeur"+i+"feuillet"+vOgIni).style.visibility = "visible";
        var vBtElem = document.getElementById("classeur"+i+"onglet"+vOgIni);
        removeClass(Onglet, "ongletBoutonNonSel");
        addClass(Onglet, "ongletBoutonSel");
        vBtElem.style.cursor="default";
        vBtElem.style.backgroundColor="inherit";
        vBtElem.style.borderTopColor="inherit";      // propriété par propriété sinon Chrome/Chromium se loupe
        vBtElem.style.borderRightColor="inherit";
        vBtElem.style.borderBottomColor="inherit";
        vBtElem.style.borderLeftColor="inherit";
      }
  }
}
 
function CadreOngletVoirOngletN(){
  var vOngletNom = this.id.substr(0,this.id.indexOf("onglet",1)); 
  var vOngletIndex = this.id.substr(this.id.indexOf("onglet",0)+6,this.id.length); 
 
  var rule1=$('#' + vOngletNom + ' .ongletBoutonNonSel')[0].style.backgroundColor.toString(); 
  var rule2=$('#' + vOngletNom + ' .ongletBoutonNonSel')[0].style.borderColor.toString();      //rule2=$('.ongletBoutonNonSel').css("border-color"); ne fonctionne pas sous Firefox
 
  var Onglets = $('#' + vOngletNom).children("div").eq(0).children("div");
 
  for ( var j = 0; j < Onglets.length; j++) {
    var Onglet = Onglets[j];
    var Feuillet = document.getElementById(vOngletNom + "feuillet" + j);
 
    if (vOngletIndex==j){ 
      Feuillet.style.display = "block";
      Feuillet.style.visibility = "visible";
      removeClass(Onglet, "ongletBoutonNonSel");
      addClass(Onglet, "ongletBoutonSel");
      Onglet.style.cursor="default";
      Onglet.style.backgroundColor="inherit";
      Onglet.style.borderTopColor="inherit";      // propriété par propriété sinon Chrome/Chromium se loupe
      Onglet.style.borderRightColor="inherit";
      Onglet.style.borderBottomColor="inherit";
      Onglet.style.borderLeftColor="inherit";
    } else {             
      Feuillet.style.display = "none";
      Feuillet.style.visibility = "hidden";
      removeClass(Onglet, "ongletBoutonSel");
      addClass(Onglet, "ongletBoutonNonSel");
      Onglet.style.cursor="pointer";
      Onglet.style.backgroundColor=rule1;
      Onglet.style.borderColor=rule2;
    }
  }
  return false; 
}
 
$(CadreOngletInitN);

/*FIN DU CODE JAVASCRIPT DE "CADRE À ONGLETS"*/

// RÉSULTATS DEPUIS WIKIDATA
// [[File:Wdsearch_script_screenshot.png]]
//if ( mw.config.get( 'wgCanonicalSpecialPageName' ) === 'Search' ||  ( mw.config.get( 'wgArticleId' ) === 0 && mw.config.get( 'wgCanonicalSpecialPageName' ) === false ) ) {
//        mw.loader.load("//en.wikipedia.org/w/index.php?title=MediaWiki:Wdsearch.js&action=raw&ctype=text/javascript");
//}
//--[[m:User:Nemo_bis|Nemo]] 13 décembre 2013 à 18:52 (UTC) ([[w:en:MediaWiki talk:Wdsearch.js|comments, translations and last instructions]])
//<!-- EdwardsBot 0661 -->

// add some xowa specific code to move the image on Page (fr:Page: == 104)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 104 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );

//mw.loader.implement("mediawiki.page.gallery@hs21z", function($, jQuery, require, module) {
    (function() {
        var $galleries, bound = !1, lastWidth = window.innerWidth, justifyNeeded = !1, isTouchScreen = !!(window.ontouchstart !== undefined || window.DocumentTouch !== undefined && document instanceof window.DocumentTouch);
        function justify() {
            var lastTop, rows = [], $gallery = $(this);
            $gallery.children('li.gallerybox').each(function() {
                var $img, imgWidth, imgHeight, outerWidth, captionWidth, top = Math.floor($(this).position().top), $this = $(this);
                if (top !== lastTop) {
                    rows.push([]);
                    lastTop = top;
                }
                $img = $this.find('div.thumb a.image img');
                if ($img.length && $img[0].height) {
                    imgHeight = $img[0].height;
                    imgWidth = $img[0].width;
                } else {
                    imgHeight = $this.children().children('div').first().height();
                    imgWidth = $this.children().children('div').first().width();
                }
                if (imgHeight < 30) {
                    imgHeight = 0;
                }
                captionWidth = $this.children().children('div.gallerytextwrapper').width();
                outerWidth = $this.outerWidth();
                rows[rows.length - 1].push({
                    $elm: $this,
                    width: outerWidth,
                    imgWidth: imgWidth,
                    aspect: imgWidth / imgHeight,
                    captionWidth: captionWidth,
                    height: imgHeight
                });
                $this.data('imgWidth', imgWidth);
                $this.data('imgHeight', imgHeight);
                $this.data('width', outerWidth);
                $this.data('captionWidth', captionWidth);
            });
            (function() {
                var maxWidth, combinedAspect, combinedPadding, curRow, curRowHeight, wantedWidth, preferredHeight, newWidth, padding, $outerDiv, $innerDiv, $imageDiv, $imageElm, imageElm, $caption, i, j, avgZoom, totalZoom = 0;
                for (i = 0; i < rows.length; i++) {
                    maxWidth = $gallery.width();
                    combinedAspect = 0;
                    combinedPadding = 0;
                    curRow = rows[i];
                    curRowHeight = 0;
                    for (j = 0; j < curRow.length; j++) {
                        if (curRowHeight === 0) {
                            if (isFinite(curRow[j].height)) {
                                curRowHeight = curRow[j].height;
                            }
                        }
                        if (curRow[j].aspect === 0 || !isFinite(curRow[j].aspect)) {
                            combinedPadding += curRow[j].width;
                        } else {
                            combinedAspect += curRow[j].aspect;
                            combinedPadding += curRow[j].width - curRow[j].imgWidth;
                        }
                    }
                    combinedPadding += 5 * curRow.length;
                    wantedWidth = maxWidth - combinedPadding;
                    preferredHeight = wantedWidth / combinedAspect;
                    if (preferredHeight > curRowHeight * 1.5) {
                        if (i === rows.length - 1) {
                            avgZoom = (totalZoom / (rows.length - 1)) * curRowHeight;
                            if (isFinite(avgZoom) && avgZoom >= 1 && avgZoom <= 1.5) {
                                preferredHeight = avgZoom;
                            } else {
                                preferredHeight = curRowHeight;
                            }
                        } else {
                            preferredHeight = 1.5 * curRowHeight;
                        }
                    }
                    if (!isFinite(preferredHeight)) {
                        continue;
                    }
                    if (preferredHeight < 5) {
                        continue;
                    }
                    if (preferredHeight / curRowHeight > 1) {
                        totalZoom += preferredHeight / curRowHeight;
                    } else {
                        totalZoom += 1;
                    }
                    for (j = 0; j < curRow.length; j++) {
                        newWidth = preferredHeight * curRow[j].aspect;
                        padding = curRow[j].width - curRow[j].imgWidth;
                        $outerDiv = curRow[j].$elm;
                        $innerDiv = $outerDiv.children('div').first();
                        $imageDiv = $innerDiv.children('div.thumb');
                        $imageElm = $imageDiv.find('img').first();
                        $caption = $outerDiv.find('div.gallerytextwrapper');
                        $imageDiv.children('div').css('margin', '0px auto');
                        if (newWidth < 60 || !isFinite(newWidth)) {
                            if (newWidth < 1 || !isFinite(newWidth)) {
                                $innerDiv.height(preferredHeight);
                                continue;
                            }
                        } else {
                            $outerDiv.width(newWidth + padding);
                            $innerDiv.width(newWidth + padding);
                            $imageDiv.width(newWidth);
                            $caption.width(curRow[j].captionWidth + (newWidth - curRow[j].imgWidth));
                        }
                        if ($imageElm[0]) {
                            imageElm = $imageElm[0];
                            imageElm.width = newWidth;
                            imageElm.height = preferredHeight;
                        } else {
                            $imageDiv.height(preferredHeight);
                        }
                    }
                }
            }());
        }
        function handleResizeStart() {
            if (lastWidth === window.innerWidth) {
                return;
            }
            justifyNeeded = !0;
            $galleries.css('min-height', function() {
                return $(this).height();
            });
            $galleries.children('li.gallerybox').each(function() {
                var imgWidth = $(this).data('imgWidth'), imgHeight = $(this).data('imgHeight'), width = $(this).data('width'), captionWidth = $(this).data('captionWidth'), $innerDiv = $(this).children('div').first(), $imageDiv = $innerDiv.children('div.thumb'), $imageElm, imageElm;
                $(this).width(width);
                $innerDiv.width(width);
                $imageDiv.width(imgWidth);
                $(this).find('div.gallerytextwrapper').width(captionWidth);
                $imageElm = $(this).find('img').first();
                if ($imageElm[0]) {
                    imageElm = $imageElm[0];
                    imageElm.width = imgWidth;
                    imageElm.height = imgHeight;
                } else {
                    $imageDiv.height(imgHeight);
                }
            });
        }
        function handleResizeEnd() {
            if (justifyNeeded) {
                justifyNeeded = !1;
                lastWidth = window.innerWidth;
                $galleries.css('min-height', '').each(justify);
            }
        }
        mw.hook('wikipage.content').add(function($content) {
            if (isTouchScreen) {
                $content.find('ul.mw-gallery-packed-hover').addClass('mw-gallery-packed-overlay').removeClass('mw-gallery-packed-hover');
            } else {
                $content.find('ul.mw-gallery-packed-hover li.gallerybox').on('focus blur', 'a', function(e) {
                    var gettingFocus = e.type !== 'blur' && e.type !== 'focusout';
                    $(this).closest('li.gallerybox').toggleClass('mw-gallery-focused', gettingFocus);
                });
            }
            $galleries = $content.find('ul.mw-gallery-packed-overlay, ul.mw-gallery-packed-hover, ul.mw-gallery-packed');
            setTimeout(function() {
                $galleries.each(justify);
                if (!bound) {
                    bound = !0;
                    $(window).on('resize', $.debounce(300, true, handleResizeStart)).on('resize', $.debounce(300, handleResizeEnd));
                }
            });
        });
    }());
//});

/*XOWA: https://wikisource.org/w/index.php?title=MediaWiki:DisplayFooter.js&action=raw&ctype=text/javascript */
/* see commented out stuff above */
/**********************
*** Automatically generate page footer from values in <nowiki>{{header}}</nowiki>
*** by [[user:GrafZahl]] and [[user:Tpt]]
**********************/
// <source lang="javascript">
 
$( document ).ready( function() {
	if( mediaWiki.config.get( 'wgNamespaceNumber' ) !== 0) { // || mediaWiki.util.getParamValue( 'match' ) !== null ) {
		return;
	}
	var $nofooterElt = $( '#nofooter' );
	var $hp = $( '#headerprevious' );
	var $hn = $( '#headernext' );
 	var $contentElt = $( '#mw-content-text' );
	if( $contentElt.length === 0 || ($hp.length === 0 && $hn.length === 0) || $nofooterElt.length !== 0 ) {
		return; 
	}
 
	var footer = '<div class="footertemplate ws-noexport noprint" id="footertemplate" style="margin-top:1em; clear:both;">';
	footer += '<div style="width:100%; padding-left:0px; padding-right:0px; background-color:transparent;">';
	if( $hp.length !== 0 ) {
		footer += '<div style="text-align:left; float:left; max-width:40%;"><span id="footerprevious">' + $hp.html() + '</span></div>';
	}
	if( $hn.length !== 0 ) {
		footer += '<div style="text-align:right; float:right; max-width:40%;"><span id="footernext">' + $hn.html() + '</span></div>';
	}
	footer += '<div style="text-align:center; margin-left:25%; margin-right:25%;"><a href="#top">' + ws_msg( '▲' ) + '</a></div>';
	footer += '</div><div style="clear:both;"></div></div>';
 
//		var $printlinksElt = $( 'div.printfooter' );
//		if( $printlinksElt.length !== 0 ) { 	// place footer before category box
//			$printlinksElt.after( footer );
//		} else {
//			$contentElt.after( footer );
//		}
	$contentElt.append( footer );
	}
);
 
