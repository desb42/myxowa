// 'popups_get_html', popup_itm, popup_tooltip, ''
function xowa_exec(act, arg1, arg2, arg3)
{
	if (act == 'get_search_suggestions')
	    getsuggestions(arg1);
	else if (act == 'popups_get_html')
	{
	    getwikix(arg1, arg2); //'<p>here</p>';
	}
}

function getsuggestions(word)
{
      var req = new XMLHttpRequest();
      var path = document.location.protocol + '//' + document.location.host + '/' + x_p.wiki + '/search/' + word;
      //var path = document.location + '?action=search&term=' + word;
      //var path = '/xowa/search/' + word;
      req.onload = function (e) {
      	var txt = req.responseText.replace(/\\'/g, "'");
      	var jtext = '[' + txt.substring(14, txt.length-2) + ']';
        var res = $.parseJSON(jtext);
        receiveSuggestions(res[0], res[1]);
      }
      req.open("GET", path, true); // 'false': synchronous.
      req.send(null);
}

function getwikix(popup_itm, ev)
{
      rettxt = '';
      var req = new XMLHttpRequest();
      var path = popup_itm.href + '?action=popup';
      req.onload = function (e) {
        var res = req.responseText;
        popup_itm.html = res;
        xowa_popups_gotpopup(popup_itm, ev);
      }
      req.open("GET", path, true); // 'false': synchronous.
      req.send(null);
}

jQuery( document ).ready( function ( $ ) {
	  var locjs = '/';
    if (window.location.pathname.substring(0,5) == '/xowa')
      locjs = '/xowa/';
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/file/' + x_p.wiki + '.js');
    var pageurl = locjs + x_p.wiki + '/wiki/' + xowa_global_values.wgPageName;
    //if (pageurl != location.pathname)
    //  window.history.pushState('page2', xowa_global_values.wgTitle, pageurl);
} );

