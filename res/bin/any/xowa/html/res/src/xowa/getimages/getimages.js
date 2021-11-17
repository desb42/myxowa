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
	function getImages() {
		var alen = atitle.length;
		if (alen > 1000)
			alen = 1000; // limit to 1000 images
		var images = []
		for (var i = 0; i < alen; i++) {
			var at = atitle[i];
			txt= at.outerHTML.replace(wiki, "/wiki/");
			var pos = txt.search("data-xoimg=\"");
			if (pos < 0)
				continue;
			if (txt.charAt(pos+12) == "\"")
				continue;
			txt= txt.replace("/xowa/", "/site/");
			var entry = {'txt': txt, 'item': i};
			images[images.length] = entry;
			var atj = $(at);
			atj.children()[0].removeAttribute("data-xoimg")
		}
		var elen = elems.length;
		if (elen > 1000)
			elen = 1000; // limit to 1000 images
		for (var i = 0; i < elen; i++) {
			var at = elems[i];
			txt= at.outerHTML.replace(wiki, "/wiki/");
			var pos = txt.search("data-xoimg=\"");
			if (pos < 0)
				continue;
			if (txt.charAt(pos+12) == "\"")
				continue;
			var entry = {'txt': txt, 'item': i + alen};
			images[images.length] = entry;
		}
		if (images.length == 0)
			return;
		var path = xowa_global_values.wgPopupsRestGatewayEndpoint + '?action=image';
		let req = new XMLHttpRequest();
		req.open("POST", path, true);
		req.responseType = 'json';
		req.onload = function() {
			if (req.readyState == 4) {
				let responseObj = req.response;
				foundImages(responseObj);
			}
		}
		var text = JSON.stringify(images);
		console.log(images.length, text);
		req.send(text + "\n");
	}
	function foundImages(msg) {
		var alen = atitle.length;
		console.log(msg);
		for (var i = 0; i < msg.length; i++) {
			var itm = msg[i];
			if (itm.item >= alen)
				var img = $('img', elems[itm.item - alen]);
			else
				var img = $('img', atitle[itm.item]);
			img.attr("width", itm.width)
			img.attr("height", itm.height)
			img.attr("src", itm.src);
		}
	}

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
				if (link.length == 0)
					continue;
				if (link.startsWith(xowa_global_values.wgPageName))
					continue;
				var entry = {'href': link, 'item': i};
				//console.log(href_wiki, href)
				if (wikis[href_wiki])
					wikis[href_wiki][wikis[href_wiki].length] = entry;
				else
					wikis[href_wiki] = [entry];
			}
		}
		var path = xowa_global_values.wgPopupsRestGatewayEndpoint + '?action=redlink';
		let req = new XMLHttpRequest();
		req.open("POST", path, true);
		req.responseType = 'json';
		req.onload = function() {
			if (req.readyState == 4) {
				let responseObj = req.response;
				foundReds(responseObj);
			}
		}
		var text = JSON.stringify(wikis);
		console.log(text);
		req.send(text + "\n");
	}
	function foundReds(msg) {
		//console.log(msg);
		for (var k = 0; k < msg.length; k++) {
			var itm = msg[k];
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
	getImages();
	getReds();
});
})(jQuery);
