/**************
 * corrections 
 **************/

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

if(!String.HTMLize){
  String.prototype.HTMLize = function() {
    var chars = new Array('&','<','>','"');
    var entities = new Array('amp','lt','gt','quot');
    var string = this;
    for (var i=0; i<chars.length; i++) {
      var regex = new RegExp(chars[i], "g");
      string = string.replace(regex, '&' + entities[i] + ';');
    }
    return string;
  };
}


function close_corr_summary() {
	var summary = document.getElementById("corr_summary");
	if(!summary) return;
	summary.innerHTML = "";
	summary.parentNode.removeChild(summary);
}

function scroll_to_correction(k) {
	var selectedPosX = 0;
	var selectedPosY = 0;
        var spanlist = $('span.coquille');
	for (var i = 0; i < spanlist.length; i++) {
		var item = spanlist[i];
		if(i==k) {
			element=item;
			while(element != null){
				selectedPosX += element.offsetLeft;
				selectedPosY += element.offsetTop;
				element = element.offsetParent;
			}
			window.scrollTo(selectedPosX,selectedPosY);
		}
	}
}

function pr_popup(){
	var corr_container = document.getElementById("corr_summary");
	if(corr_container) { close_corr_summary(); return; }
	corr_container = document.createElement("div");
	corr_container.setAttribute("id","corr_summary");

	var content= document.getElementById("text");
	var spanlist = document.getElementsByTagName("span");
	if(!content) for(var i=0; i< spanlist.length; i++) { if( spanlist[i].className=="text") content=item; }
	if(content) {
		corr_container.style.cssText="position:relative;";
		corr_container.innerHTML = "<div style=\"position:absolute;right:-0.5em;width:0em;\"><div style=\"position:fixed;min-width:16em;max-width:35em;max-height:35em;overflow:auto;background:#ffffff;z-index:1;border:1px solid;padding:5px\">"+self.corr_list+"</div></div>";
		content.insertBefore(corr_container,content.firstChild);
	} else {
		corr_container.style.cssText="position:fixed;min-width:16em;max-width:35em;max-height:35em;overflow:auto;scroll:auto;right:0.5em;background:#ffffff;z-index:1;border:1px solid;padding:5px";
		corr_container.innerHTML = self.corr_list;
		content = document.getElementById("bodyContent");
		content.insertBefore(corr_container,content.firstChild);
	}
}

function pr_typos() {

	if( ! ( mw.config.get('wgNamespaceNumber')==0 || mw.config.get( 'wgCanonicalNamespace' ) === 'Page' ) ) return;

	var str = '<div style="float:right"><a href="javascript:close_corr_summary()"><img src="//upload.wikimedia.org/wikipedia/commons/9/97/WikEd_close.png"/></a></div><h4>'+ ws_msg('corr_list') + " :</h4><ul>"; 
	var spanlist = $('span.coquille');
	for(var i=0; i< spanlist.length; i++) {
		var item = spanlist[i];
		w1=w2='';
		if(item.previousSibling && item.previousSibling.data) { 
			context1=item.previousSibling.data.split(' ');
			if(context1.length>1) w1 = context1[context1.length - 2];
		}
		if(item.nextSibling && item.nextSibling.data) { 
			context2=item.nextSibling.data.split(' ');
			if(context2.length>1) w2 = context2[1]; 
		}
		//hidden "m" char is to avoid rtl languages firefox bug
		var newline = '<li>'+'<a style=\"color:#000000;\" href=\"javascript:scroll_to_correction('+i+');\"> « '+w1+' <span style=\"color:#ff0000;\">#pre#</span> '+ w2.HTMLize() + ' »  →<span style=\"color:#ffffff;\">m</span>« ' + w1.HTMLize() + ' <span style=\"color:#00A000;\">#post#</span> ' + w2 +' » </a></li>'; 
		newline = newline.replace("#pre#",item.title.HTMLize());
		newline = newline.replace("#post#",item.innerHTML);
		str = str + newline; 
	}
	str=str+'</ul>';
	self.corr_list = str;

	if (spanlist.length) {
		mw.loader.using( 'mediawiki.util', function() {
		var optlist = get_optlist();
		mw.util.addPortletLink ('p-displayOptions', 'javascript:pr_popup();', ws_msg('corr_link')+" ("+spanlist.length+")", 'option-corrections', '' );
		cs = document.getElementById("corr-info");
		if(cs) {
			if (spanlist.length == 1)
				cs.innerHTML += " <a href='javascript:pr_popup();'>" + ws_msg('corr_one');
			else
				cs.innerHTML += " <a href='javascript:pr_popup();'>" + spanlist.length + ws_msg('corr_many'); 
		}
		});
	}
}

$(pr_typos);