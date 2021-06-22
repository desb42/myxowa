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
      var path = '/xowa/search/' + word;
      req.onload = function (e) {
        var res = $.parseJSON(req.responseText);
        receiveSuggestions(word, res);
      }
      req.open("GET", path, true); // 'false': synchronous.
      req.send(null);
}

function getwikix(popup_itm, ev)
{
      rettxt = '';
      var req = new XMLHttpRequest();
      var path = popup_itm.href.replace('/wiki/','/wikx/');
      req.onload = function (e) {
        var res = req.responseText;
        popup_itm.html = res;
        xowa_popups_gotpopup(popup_itm, ev);
      }
      req.open("GET", path, true); // 'false': synchronous.
      req.send(null);
}

