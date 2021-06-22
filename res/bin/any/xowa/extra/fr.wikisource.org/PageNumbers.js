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

// Copy from MediaWiki:Base.js
function get_optlist() {
	var optlist = document.getElementById("optlist"),displayOptions,
      cl, ulCl;
	if(!optlist) {
		if (mw.config.get('skin')==='vector') {
	displayOptions = document.createElement("nav");
	displayOptions.className = "vector-menu vector-menu-portal portal collapsed";
	cl="vector-menu-content";
	ulCl="vector-menu-content-list";
} else {
	 displayOptions = document.createElement("div");
	displayOptions.className = "portlet";
	cl="pBody";
	ulCl="";
}
		displayOptions.innerHTML = '<h3>' + ws_msg('optlist') + '<\/h3><div class="'+cl+'"><ul id="optlist" class="'+ ulCl + '"></ul><\/div>';
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
		mw.loader.using( 'mediawiki.util', function() {
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
	});
    }
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
    var ct_o = $(ct).offset();
    var ct_ox = ct_o.left; 
    var ct_oy = ct_o.top;
    var oo = $(ss).offset();
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
            a_o = $(a).offset(); 
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
    mw.loader.using('mediawiki.cookie', function() {
    	$(init_page_layout);
    	$(init_page_numbers);
    });
}