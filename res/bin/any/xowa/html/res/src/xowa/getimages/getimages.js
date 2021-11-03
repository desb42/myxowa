/*
send a request of the form
 api/xoimg/TITLE/XOIMG
 with a response that is a 'link' (could be on disk file:// or from a url http(s):// which is a nginx static file (on disk))
*/
(function($){
	var elems;
	var atitle;
	var hrefs;
	var wiki = "/xowa/" + xowa.page.wiki + "/wiki/";
	function getImages_a() {
		var len = atitle.length;
		for (var i = 0; i < len; i++) {
			var at = atitle[i];
			txt= at.outerHTML.replace(wiki, "/wiki/");
			var pos = txt.search("data-xoimg=\"");
			if (pos < 0)
				continue;
			if (txt.charAt(pos+12) == "\"")
				continue;
			txt= txt.replace("/xowa/", "/site/");
			txt= txt.replace(/#/g, "%23").replace(/&/g, "%26");
			//console.log(txt);
			var path = xowa_global_values.wgPopupsRestGatewayEndpoint + '?action=image&image_a=' + encodeURI(txt) + "&image_item=" + i;
			var atj = $(at);
			atj.children()[0].removeAttribute("data-xoimg")
			//console.log(path)
			let req = new XMLHttpRequest();
			req.open("GET", path, true);
			req.responseType = 'json';
			req.onload = function() {
				if (req.readyState == 4) {
					let responseObj = req.response;
					foundImage_a(responseObj);
				}
			}
			req.send();
		}
	}
	function foundImage_a(msg) {
		//console.log(msg);
		var img = $('img', atitle[msg.item])
		img.attr("width", msg.width)
		img.attr("height", msg.height)
		img.attr("src", msg.src);
	}

	function getImages() {
		var len = elems.length;
		for (var i = 0; i < len; i++) {
			var at = elems[i];
			txt= at.outerHTML.replace(wiki, "/wiki/");
			var pos = txt.search("data-xoimg=\"");
			if (pos < 0)
				continue;
			if (txt.charAt(pos+12) == "\"")
				continue;
			txt= txt.replace("/xowa/", "/site/");
			txt= txt.replace(/#/g, "%23").replace(/&/g, "%26");
			//console.log(txt);
			var path = xowa_global_values.wgPopupsRestGatewayEndpoint + '?action=image&image_a=' + encodeURI(txt) + "&image_item=" + i;
			//console.log(path);
/*			var img = $(elems[i]);
			var title = img.data('xowa-title');
			var xoimg = img.data('xoimg');
			if (xoimg === undefined)
				continue;
			var path = xowa_global_values.wgPopupsRestGatewayEndpoint + '?action=image&image_title=' + title + '&image_xoimg=' + xoimg + "&image_item=" + i;
*/
			let req = new XMLHttpRequest();
			req.open("GET", path, true);
			req.responseType = 'json';
			req.onload = function() {
				if (req.readyState == 4) {
					let responseObj = req.response;
					foundImage(responseObj);
				}
			}
			req.send();
		}
	}

	function foundImage(msg) {
		//console.log(msg);
		var img = $(elems[msg.item]);
		img.attr("width", msg.width)
		img.attr("height", msg.height)
		img.attr("src", msg.src);
	}

	var list = [];
	function getReds() {
		var len = hrefs.length;
		var listpos = 0;
		var wikis = {}
		for (var i = 0; i < len; i++) {
			var aa = $(hrefs[i]);
			var href = aa.attr('href');
			//console.log(href);
			if (href.startsWith('/xowa/')) {
				var pos = href.search("/wiki/");
				if (pos < 0)
					continue;
				href_wiki = href.substring(6, pos)
				link = href.substring(pos + 6);
				var entry = {'href': link, 'item': i};
				//console.log(href_wiki, href)
				if (wikis[href_wiki])
					wikis[href_wiki][wikis[href_wiki].length] = entry;
				else
					wikis[href_wiki] = [entry];
				href = href.replace(wiki, "");
				href = href.replace("/xowa/", "/site/");
				var entry = {'href': href, 'item': i};
				list[listpos++] = entry;
			}
		}
		for (const [key, value] of Object.entries(wikis)) {
			//console.log(key, value);
			//if (key != "en.wikipedia.org") continue;
		var path = '/xowa/' + key + '/wiki/?action=redlink';
		console.log(path, JSON.stringify(value));
		let req = new XMLHttpRequest();
		req.open("POST", path, true);
		req.responseType = 'json';
		req.onload = function() {
			if (req.readyState == 4) {
				let responseObj = req.response;
				foundReds(responseObj);
			}
		}
		var text = JSON.stringify(value);
		//console.log(text);
		req.send(text + "\n");
		}
	}
	function foundReds(msg) {
		//console.log(msg);
		for (var i = 0; i < msg.length; i++) {
			var itm = msg[i];
			var lnk = itm[0], klass = itm[1];
			var aa = $(hrefs[lnk.substring(7)]);
			var href = aa.attr('href');
			aa.addClass(klass);
			var klasses = klass.split(' ');
			for (var j = 0; j < klasses.length; j++) {
				var kl = klasses[j];
				if (kl == "new") {
					aa.attr('title', xowa_global_values['red-link-title'].replace("\u007e{0}", aa.attr('title')));
					console.log(aa.text(), lnk);
				}
			}
		}
	}

$(document).ready(function() {
	atitle = $('a[xowa_title]');
	elems = $('img[data-xowa-title]');
	hrefs = $('a[href]');
	getImages_a();
	getImages();
	getReds();
});
})(jQuery);
