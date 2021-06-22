/**
 * Keep code in MediaWiki:Common.js to a minimum as it is unconditionally
 * loaded for all users on every wiki page. If possible create a gadget that is
 * enabled by default instead of adding it here (since gadgets are fully
 * optimized ResourceLoader modules with possibility to add dependencies etc.)
 *
 * Since Common.js isn't a gadget, there is no place to declare its
 * dependencies, so we have to lazy load them with mw.loader.using on demand and
 * then execute the rest in the callback. In most cases these dependencies will
 * be loaded (or loading) already and the callback will not be delayed. In case a
 * dependency hasn't arrived yet it'll make sure those are loaded before this.
 */
'use strict';
// [[Category:Wiktionary scripts]] <nowiki>
/*jshint shadow:true, undef:true, latedef:true, unused:true, es3:true */
/*global jQuery, mw, importScript, importStylesheet, $ */


/** [[WT:PREFS]] v2.0 **/
//try {
//	(function() {
//
//		var prefs;
//		try {
//			prefs = window.localStorage.getItem('AGprefs');
//		} catch (e) {
//			prefs = jQuery.cookie('AGprefs');
//		}
//
//		prefs = prefs && jQuery.parseJSON(prefs);
//
//		if (mw.config.get('wgUserGroups').indexOf('autoconfirmed') !== -1)
//			return;
//
//		if (mw.config.get('wgUserGroups').indexOf('user') === -1) {
//			// XXX: [[Wiktionary:Preferences/V2]] is just a temporary page
//
//			mw.loader.using(['mediawiki.util'], function() {
//				mw.util.addPortletLink('p-personal', mw.util.getUrl('Wiktionary:Preferences/V2'),
//					'Preferences', 'pt-agprefs', 'Personalise Wiktionary (settings are kept per-browser).', '',
//					document.getElementById('pt-createaccount'));
//			});
//
//			if ((mw.config.get('wgAction') === 'view') && (mw.config.get('wgPageName') === 'Wiktionary:Preferences/V2')) {
//				mw.loader.load('ext.gadget.AGprefs'); // [[MediaWiki:Gadget-AGprefs.js]]
//			}
//		}
//
//		if (!prefs)
//			return;
//
//		mw.loader.state('the_pope_is_an_atheist_woman_alien', 'missing');
//		for (var key in prefs.modules) {
//			if (prefs.modules[key]) {
//				mw.loader.load([key]);
//			} else {
//				// unavoidable race condition. to prevent it, every enabled-by-default gadget should have "site" as a dependency
//				if (mw.loader.getState(key) !== 'ready') {
//					mw.loader.moduleRegistry[key].dependencies.push('the_pope_is_an_atheist_woman_alien');
//					mw.loader.state(key, 'missing');
//				} else {
//					// XXX
//					mw.log.warn(key + " could not be disabled; make sure it has 'site' declared as a dependency");
//				}
//			}
//		}
//
//		for (var key in prefs.sheets) {
//			importStylesheet('MediaWiki:Gadget-' + key);
//		}
//
//		for (var key in prefs.scripts) {
//			importScript('MediaWiki:Gadget-' + key);
//		}
//
//		if (mw.config.get('wgUserGroups').indexOf('user') !== -1)
//			mw.loader.using(['mediawiki.notify', 'mediawiki.api'], function() {
//				var changes = [];
//				for (var key in prefs.gadgets)
//					changes.push('gadget-' + key + '=' + (prefs.gadgets[key] ? '1' : '0'));
//
//				(new mw.Api()).postWithToken('options', {
//					action: 'options',
//					change: changes.join('|')
//				}).then(function() {
//					jQuery.cookie('AGprefs', null);
//					try {
//						window.localStorage.removeItem('AGprefs');
//					} catch (e) { /* */ }
//					mw.notify(
//						jQuery('<b>Your <a href="/wiki/Wiktionary:Preferences/V2">per-browser preferences</a> have been migrated</b><br/><br/>' +
//							'From now on, you should use your <a href="/wiki/Special:Preferences">user preferences page</a>. ' +
//							'Preferences will no longer apply after you log out.')
//					);
//				});
//			});
//
//	})();
//} catch (e) {
//	mw.log.warn(e);
//}

//mw.loader.using('mediawiki.util').done(function() {
//	/** &withmodule= query parameter **/
//	if (mw.util.getParamValue('withmodule'))
//		mw.loader.load(mw.util.getParamValue('withmodule').split(','));
//
//	/** &preloadtext= and &preloadminor= **/
//	if (mw.config.get('wgAction') === 'edit')
//		jQuery(document).ready(function() {
//			var wpTextbox1 = document.getElementById('wpTextbox1');
//			var wpMinoredit = document.getElementById('wpMinoredit');
//			if (!wpTextbox1)
//				return;
//
//			var preloadtext = mw.util.getParamValue('preloadtext');
//			var preloadminor = mw.util.getParamValue('preloadminor');
//
//			if (preloadtext && !wpTextbox1.value)
//				wpTextbox1.value = preloadtext;
//			if ((preloadminor !== null) && wpMinoredit)
//				wpMinoredit.checked = !/^(0|false|no|)$/i.test(preloadminor);
//		});
//
//	/** Monthly subpages; see [[Template:discussion recent months|discussion recent months]] **/
//	/*  See also: [[Special:AbuseFilter/43]]  */
//	if (/^Wiktionary:(Beer_parlour|Grease_pit|Tea_room|Etymology_scriptorium|Information_desk)$/.test(mw.config.get('wgPageName')))
//		jQuery(document).ready(function() {
//			var nNSR = document.getElementById('new-section-redirect').getElementsByTagName('a')[0];
//			var caAddSection = document.getElementById('ca-addsection');
//			if (!caAddSection) {
//				caAddSection = mw.util.addPortletLink(mw.config.get('skin') === 'vector' ? 'p-views' : 'p-cactions',
//					nNSR.href, '+', 'ca-addsection', "Start a new section", '+', document.getElementById('ca-history')
//				);
//			} else {
//				caAddSection.getElementsByTagName('a')[0].href = nNSR.href;
//			}
//		});
//});

// == Unsupported titles == 
// [[Special:PrefixIndex/Unsupported titles]]
// [[MediaWiki:UnsupportedTitles.js]]
//if ((mw.config.get('wgAction') === 'view' && /^(?:Talk:)?Unsupported_titles\//.test(mw.config.get('wgPageName')))
//		|| mw.config.get('wgCanonicalSpecialPageName') == 'Badtitle')
//	mw.loader.using('mediawiki.util', function(){importScript("MediaWiki:UnsupportedTitles.js");});


//if (mw.config.get("wgTitle").indexOf("by language") != -1)
//$("a.CategoryTreeLabelCategory").text(
//	function(index, content) {
//		return content.replace(
//			/^Requests for (?:verification|deletion|cleanup) in (.+) entries$/,
//			"$1"
//		);
//	}
//);

// removes "0 c" for categories that do not have subcategories
$(".CategoryTreeEmptyBullet + a + span")
	.html(
		function(index, content) {
			return content.replace(
				"0 c, ",
				""
			);
		}
	);

// [[User:Yair_rand/FindTrans.js]]
//if (mw.config.get('wgPageName') === 'Special:Search') {
//	mw.loader.load('/w/index.php?title=User:Yair_rand/FindTrans.js&action=raw&ctype=text/javascript');
//}

// == "Did you mean" auto redirect in 3 seconds ==
/**
 * doRedirect will redirect if a did you mean box is found, and create a
 * "redirected from X" if a rdfrom is passed in the get parameters
**/

//$.when(mw.loader.using("mediawiki.util"), $.ready).done(function(){
//var rdFromValue = mw.util.getParamValue("rdfrom");
//if (rdFromValue)
//{
//	rdFromValue = decodeURIComponent(rdFromValue);
//	$('#siteSub').after(
//		$('<div>').attr("id", 'contentSub')
//			.append(document.createTextNode("(Auto-redirected from "))
//			.append($('<a>', {
//				href: mw.util.getUrl(rdFromValue, {redirect:"no"}),
//				addClass: 'new'
//			}).text(rdFromValue))
//			.append(document.createTextNode(")")));
//}
//else 
//{
//	window.setTimeout(function () {	
//		var target = $('#did-you-mean a').html();
//		var	pagetitle = mw.config.get("wgTitle"); //$('h1').first().text().trim();
//		var canRedirect = mw.util.getParamValue("redirect") != "no";
//		
//		if (target && target !== pagetitle && canRedirect &&
//			(jQuery.cookie('WiktionaryDisableAutoRedirect') != 'true') &&
//			mw.config.get("wgAction") == "view" &&
//			mw.config.get('wgArticleId') === 0 &&
//			mw.config.get('wgNamespaceNumber') === 0 &&
//			!/Redirected from/.test(jQuery('#contentSub').html())
//		) {
//			window.location = mw.util.getUrl(target, { rdfrom: pagetitle });
//		}
//	}, 3000);
//}
//});

/* ==Page specific extensions== */
/* ===[[Wiktionary:Main Page]]=== */
/*mw.loader.using("mediawiki.util", function(){
	// Hide the title and "Redirected from" (maybe we should keep the redirected from so's people update their bookmarks ;)
	// Broken in IE!
	if (mw.config.get('wgIsMainPage') && !(mw.config.get('wgAction') === 'view' || mw.config.get('wgAction') === 'submit')) {
		mw.util.addCSS('.firstHeading { display: block !important; }');
		mw.util.addCSS('#contentSub { display: inline !important; }');
	}
	
	if (mw.config.get('wgIsMainPage')) {
		$(function(){
			mw.util.addPortletLink('p-lang', '//meta.wikimedia.org/wiki/Wiktionary#List_of_Wiktionaries',
				'Complete list', 'interwiki-completelist', 'Complete list of Wiktionaries');
		});
	}
});*/

/* ===Custom search engines=== */
if (mw.config.get('wgPageName') === 'Help:Tips_and_tricks') {
	importScript('MediaWiki:CustomSearch.js');
}

/* == [[WT:FEED]] == */
// used to be [[User:Conrad.Irwin/feedback.js]]
//if (true) {
//	$(function(){
//		var fb_comment_url = mw.config.get('wgScript') + "?title=Wiktionary:Feedback" +
//	"&action=edit&section=new" +	"&preload=Wiktionary:Feedback%2Fpreload" +	"&editintro=Wiktionary:Feedback%2Fintro" +
//	"&preloadtitle=" +	encodeURIComponent("[[:" + mw.config.get('wgPageName').replace(/_/g, ' ') + "]]");
//
//		var fb_comment = "If you have time, leave us a note.";
//		
//		$('<a>').attr('href', fb_comment_url).text(fb_comment)
//		.appendTo($('<p>').css('font-size', '80%')).parent()
//		.appendTo($('<div>').addClass("body")).parent().before($('<h3>Feedback</h3>'))
//		.appendTo($('<div>').addClass("portal expanded").attr("id", "p-feedback")).parent()
//		.appendTo($('#mw-panel'));
//	});
//}


/* == Toggle functionality only failed test == */
// all tests for module testcases
$(function () {
	$("table.unit-tests th.unit-tests-img-corner").on("click", function () {
		$(this).closest("table.unit-tests").toggleClass("unit-tests-hide-passing");
	});
});

// Text after → on history pages is plain wikitext but on the page it is expanded. Fixing only [[Template:temp]]
// Also update it in change summaries
$(function () {
	if (/\.7B\.7Btemp\.7C(.*?)\.7D\.7D/.test(location.href)) {
		window.location = location.href.replace(/\.7B\.7Btemp.7C/g, ".7B.7B");
	}
	
	if (mw.config.get('wgAction') !== 'edit')
		return;
	if (!/[?&]section=\d/.test(location.href))
		return;
	var wpSummary = document.getElementById('wpSummary');
	if (!wpSummary)
		return;
	if (wpSummary.value.substr(0, 3) !== '/* ')
		return;
	if (wpSummary.value.substr(wpSummary.value.length - 4) !== ' */ ')
		return;
	wpSummary.value = wpSummary.value.replace(/\{\{temp(late)?\|/g, '{{');
});


// </nowiki>
// The rest of the scripts are at [[MediaWiki:Gadget-legacy.js]].
// Most of them should be converted into gadgets as time and resources allow.

//xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/src/mediawiki/mediawiki.Title.js');

/* https://en.wiktionary.org/wiki/MediaWiki:Gadget-catfix.js */
/*
 * dependencies: mediawiki.Title
 */

//jQuery(function () {
//	'use strict';
//	
//	var wrapper;
//	
//	// Apply only to pages containing an element with the id "catfix".
//	if (!(wrapper = document.getElementById("catfix")))
//		return;
//	
//	// Apply only to Category: namespace
//	if (mw.config.get('wgNamespaceNumber') !== 14)
//		return;
//
//	// Get the language name and script wrapper.
//	var langname = wrapper.className.split("CATFIX-")[1];
//	wrapper = wrapper.getElementsByTagName("*")[0] || document.createElement("span");
//	
//	var anchor = "";
//	if (langname && langname.length > 0)
//		anchor = "#" + langname;
//	
//	// Process each link in the category listing.
//	jQuery("#mw-pages>.mw-content-ltr li>a, #newest-and-oldest-pages tr li>a")
//		.each(function () {
//			try {
//				var titleobj = new mw.Title(this.textContent || this.innerText);
//				var namespaceId = titleobj.getNamespaceId();
//				var pageName = titleobj.getNameText();
//				
//				/*
//				 * Continue if the link points to a page in the main or
//				 * reconstruction namespaces or is an appendix page
//				 * beginning with the language name (i.e. an entry for a
//				 * term in an appendix-only language).
//				 */
//				if (!([0, 1, 114, 118].indexOf(namespaceId) != -1
//					|| (namespaceId == 100
//						&& pageName.substring(0, langname.length + 1) == langname + "/")))
//					return;
//				
//				// Add the anchor if the link goes to a mainspace or reconstruction page.
//				if (namespaceId === 0 || namespaceId === 118)
//					this.setAttribute("href", this.getAttribute("href") + anchor);
//				
//				var textNodeToWrap;
//				if (namespaceId === 1 || namespaceId === 114) { // talk, citations
//					textNodeToWrap = document.createTextNode(pageName);
//					$(this).empty()
//						.append(titleobj.getNamespacePrefix())
//						.append(textNodeToWrap);
//				} else {
//					textNodeToWrap = this;
//				}
//				
//				// Insert the wrapper around the link.
//				var parent = textNodeToWrap.parentNode;
//				var clone = wrapper.cloneNode(false);
//				clone.appendChild(textNodeToWrap);
//				parent.appendChild(clone);
//			} catch (e) {
//				console.error(e);
//			}
//		});
//});

/******* en.wiktionary.org/wiki/MediaWiki:Gadget-VisibilityToggles.js *******/
/* eslint-env es5, browser, jquery */
/* eslint semi: "error" */
/* jshint esversion: 5, eqeqeq: true */
/* globals $, mw */
/* requires mw.cookie, mw.storage */
(function VisibilityTogglesIIFE () {
"use strict";

// Toggle object that is constructed so that `toggle.status = !toggle.status`
// automatically calls either `toggle.show()` or `toggle.hide()` as appropriate.
// Creating toggle also automatically calls either the show or the hide function.
function Toggle (showFunction, hideFunction) {
	this.show = showFunction, this.hide = hideFunction;
}

Toggle.prototype = {
	get status () {
		return this._status;
	},
	set status (newStatus) {
		if (typeof newStatus !== "boolean")
			throw new TypeError("Value of 'status' must be a boolean.");
		if (newStatus === this._status)
			return;

		this._status = newStatus;

		if (this._status !== this.toggleCategory.status)
			this.toggleCategory.updateToggle(this._status);

		if (this._status)
			this.show();
		else
			this.hide();
	},
};

/*
 * Handles storing a boolean value associated with a `name` stored in
 * localStorage under `key`.
 *
 * The `get` method returns `true`, `false`, or `undefined` (if the storage
 * hasn't been tampered with).
 * The `set` method only allows setting `true` or `false`.
 */
function BooleanStorage(key, name) {
	if (typeof key !== "string")
		throw new TypeError("Expected string");

	if (!(typeof name === "string" && name !== "")) {
		throw new TypeError("Expected non-empty string");
	}
	this.key = key; // key for localStorage
	this.name = name; // name of toggle category

	function convertOldCookie(cookie) {
		return cookie.split(';')
			.filter(function(e) { return e !== ''; })
			.reduce(function(memo, currentValue) {
				var match = /(.+?)=(\d)/.exec(currentValue); // only to test for temporary =[01] format
				if (match) {
					memo[match[1]] = Boolean(Number(match[2]));
				} else {
					memo[currentValue] = true;
				}
				return memo;
			}, {});
	}
	// Look for cookie in old format.
	var cookie = mw.cookie.get(key);
	if (cookie !== null) {
		this.obj = $.extend(this.obj, convertOldCookie(cookie));
		mw.cookie.set(key, null);  // Remove cookie.
	}
}

BooleanStorage.prototype = {
	get: function () {
		return this.obj[this.name];
	},

	set: function (value) {
		if (typeof value !== "boolean")
			throw new TypeError("Expected boolean");

		var obj = this.obj;
		if (obj[this.name] !== value) {
			obj[this.name] = value;
			this.obj = obj;
		}
	},

	// obj allows getting and setting the object version of the stored value.
	get obj() {
		if (typeof this.rawValue !== "string")
			return {};
		try {
			return JSON.parse(this.rawValue);
		} catch (e) {
			if (e instanceof SyntaxError) {
				return {};
			} else {
				throw e;
			}
		}
	},

	set obj(value) {
		// throws TypeError ("cyclic object value")
		this.rawValue = JSON.stringify(value);
	},

	// rawValue allows simple getting and setting of the stringified object.
	get rawValue () {
		return mw.storage.get(this.key);
	},

	set rawValue (value) {
		return mw.storage.set(this.key, value);
	},
};


// This is a version of the actual CSS identifier syntax (described here:
// https://stackoverflow.com/a/2812097), with only ASCII and that must begin
// with an alphabetic character.
var asciiCssIdentifierRegex = /^[a-zA-Z][a-zA-Z0-9_-]+$/;

function ToggleCategory (name, defaultStatus) {
	this.name = name;
	this.sidebarToggle = this.newSidebarToggle();
	this.storage = new BooleanStorage("Visibility", name);
	this.status = this.getInitialStatus(defaultStatus);
}

// Have toggle category inherit array methods.
ToggleCategory.prototype = [];

ToggleCategory.prototype.addToggle = function (showFunction, hideFunction) {
	var toggle = new Toggle(showFunction, hideFunction);
	toggle.toggleCategory = this;
	this.push(toggle);
	toggle.status = this.status;
	return toggle;
};

// Generate an identifier consisting of a lowercase ASCII letter and a random integer.
function randomAsciiCssIdentifier() {
	var digits = 9;
	var lowCodepoint = "a".codePointAt(0), highCodepoint = "z".codePointAt(0);
	return String.fromCodePoint(
			lowCodepoint + Math.floor(Math.random() * (highCodepoint - lowCodepoint)) - 1)
		+ String(Math.floor(Math.random() * Math.pow(10, digits)));
}

function getCssIdentifier(name) {
	name = name.replace(/\s+/g, "-");
	// Generate a valid ASCII CSS identifier.
	if (!asciiCssIdentifierRegex.test(name)) {
		// Remove characters that are invalid in an ASCII CSS identifier.
		name = name.replace(/^[^a-zA-Z]+/, "").replace(/[^a-zA-Z_-]+/g, "");
		if (!asciiCssIdentifierRegex.test(name))
			name = randomAsciiCssIdentifier();
	}
	return name;
}

// Add a new global toggle to the sidebar.
ToggleCategory.prototype.newSidebarToggle = function () {
	var name = getCssIdentifier(this.name);
	var id = "p-visibility-" + name;
	
	var sidebarToggle = $("#" + id);
	if (sidebarToggle.length > 0)
		return sidebarToggle;

	var listEntry = $("<li>");
	sidebarToggle = $("<a>", {
			id: id,
			href: "#visibility-" + this.name,
		})
		.click((function () {
			this.status = !this.status;
			this.storage.set(this.status);
			return false;
		}).bind(this));

	listEntry.append(sidebarToggle).appendTo(this.buttons);

	return sidebarToggle;
};

// Update the status of the sidebar toggle for the category when all of its
// toggles on the page are toggled one way.
ToggleCategory.prototype.updateToggle = function (status) {
	if (this.length > 0 && this.every(function (toggle) { return toggle.status === status; }))
		this.status = status;
};

// getInitialStatus is only called when a category is first created.
ToggleCategory.prototype.getInitialStatus = function (defaultStatus) {
	function isFragmentSet(name) {
		return location.hash.toLowerCase().split("_")[0] === "#" + name.toLowerCase();
	}

	function isHideCatsSet(name) {
		var match = /^.+?\?(?:.*?&)*?hidecats=(.+?)(?:&.*)?$/.exec(location.href);
		if (match !== null) {
			var hidecats = match[1].split(",");
			for (var i = 0; i < hidecats.length; ++i) {
				switch (hidecats[i]) {
					case name: case "all":
						return false;
					case "!" + name: case "none":
						return true;
				}
			}
		}
		return false;
	}

	function isWiktionaryPreferencesCookieSet() {
		return mw.cookie.get("WiktionaryPreferencesShowNav") === "true";
	}
	// TODO check category-specific cookies
	return isFragmentSet(this.name)
		|| isHideCatsSet(this.name)
		|| isWiktionaryPreferencesCookieSet()
		|| (function(storedValue) {
            return storedValue !== undefined ? storedValue : Boolean(defaultStatus);
        }(this.storage.get()));
};

Object.defineProperties(ToggleCategory.prototype, {
	status: {
		get: function () {
			return this._status;
		},
		set: function (status) {
			if (typeof status !== "boolean")
				throw new TypeError("Value of 'status' must be a boolean.");
			if (status === this._status)
				return;

			this._status = status;

			// Change the state of all Toggles in the ToggleCategory.
			for (var i = 0; i < this.length; i++)
				this[i].status = status;

			this.sidebarToggle.html((status ? "Hide " : "Show ") + this.name);
		},
	},

	buttons: {
		get: function () {
			var buttons = $("#p-visibility ul");
			if (buttons.length > 0)
				return buttons;
			buttons = $("<ul>");
			var collapsed = mw.cookie.get("vector-nav-p-visibility") === "false";
			var toolbox = $("<nav>", {
					"class": "vector-menu vector-menu-portal portal portlet",
					"id": "p-visibility"
				})
				.append($("<h3>Visibility</h3>"))
				.append($("<div>", { class: "pBody body" }).append(buttons));
			var insert = document.getElementById("p-lang") || document.getElementById("p-feedback");
			if (insert) {
				$(insert).before(toolbox);
			} else {
				var sidebar = document.getElementById("mw-panel") || document.getElementById("column-one");
				$(sidebar).append(toolbox);
			}

			return buttons;
		}
	}
});

function VisibilityToggles () {
	// table containing ToggleCategories
	this.togglesByCategory = {};
}

// Add a new toggle, adds a Show/Hide category button in the toolbar.
// Returns a function that when called, calls showFunction and hideFunction
// alternately and updates the sidebar toggle for the category if necessary.
VisibilityToggles.prototype.register = function (category, showFunction, hideFunction, defaultStatus) {
	if (!(typeof category === "string" && category !== ""))
		return;

	var toggle = this.addToggleCategory(category, defaultStatus)
					.addToggle(showFunction, hideFunction);

	return function () {
		toggle.status = !toggle.status;
	};
};

VisibilityToggles.prototype.addToggleCategory = function (name, defaultStatus) {
	return (this.togglesByCategory[name] = this.togglesByCategory[name] || new ToggleCategory(name, defaultStatus));
};

window.alternativeVisibilityToggles = new VisibilityToggles();
window.VisibilityToggles = window.alternativeVisibilityToggles;

})();
/******* en.wiktionary.org/wiki/MediaWiki:Gadget-defaultVisibilityToggles.js *******/
/* jshint undef: true, esversion: 5 */
/* globals $, jQuery, mw */

(function defaultVisibilityTogglesIIFE() {
"use strict";

/* == NavBars == */
var NavigationBarHide = "hide ▲";
var NavigationBarShow = "show ▼";

function getToggleCategory(element, defaultCategory) {
	if ($(element).find("table").first().is(".translations"))
		return "translations";
	
	var heading = element;
	while ((heading = heading.previousElementSibling)) {
		// tagName is always uppercase:
		// https://developer.mozilla.org/en-US/docs/Web/API/Element/tagName
		var num = heading.tagName.match(/H(\d)/);
		if (num)
			num = Number(num[1]);
		else
			continue;
		if (4 <= num && num <= 6) {
			if (heading.getElementsByTagName("span")[1])
				heading = heading.getElementsByTagName("span")[0];
			var text = jQuery(heading).text()
				.toLowerCase()
				// jQuery's .text() is inconsistent about whitespace:
				.replace(/^\s+|\s+$/g, "").replace(/\s+/g, " ")
				// remove numbers added by the "Auto-number headings" pref:
				.replace(/^[1-9][0-9.]+ ?/, "");
			// Toggle category must be convertible to a valid CSS identifier so
			// that it can be used in an id selector in jQuery in
			// ToggleCategory.prototype.newSidebarToggle
			// in [[MediaWiki:Gadget-VisibilityToggles.js]].
			// Spaces must later be converted to hyphens or underscores.
			// Reference: https://drafts.csswg.org/selectors-4/#id-selectors
			if (/^[a-zA-Z0-9\s_-]+$/.test(text))
				return text;
			else
				break;
		} else if (num)
			break;
	}
	
	return defaultCategory;
}

function createNavToggle(navFrame) {
	var navHead, navContent;
	for (var i = 0, children = navFrame.childNodes; i < children.length; ++i) {
		var child = children[i];
		if (child.nodeName === "DIV") {
			var classList = child.classList;
			if (classList.contains("NavHead"))
				navHead = child;
			if (classList.contains("NavContent"))
				navContent = child;
		}
	}
	if (!(navHead && navContent))
		return;
	
	// Step 1, don't react when a subitem is clicked.
	$(navHead).find("a").on("click", function (e) {
		e.stopPropagation();
	});
			
	// Step 2, toggle visibility when bar is clicked.
	// NOTE This function was chosen due to some funny behaviour in Safari.
	var $navToggle = $("<a>").attr("role", "button").attr("tabindex", "0");
	
	$("<span>").addClass("NavToggle")
		.append($navToggle)
		.prependTo(navHead);
	
	navHead.style.cursor = "pointer";
	var toggleCategory = $(navFrame).data("toggle-category")
		|| getToggleCategory(navFrame, "other boxes");
	navHead.onclick = window.VisibilityToggles.register(toggleCategory,
		function show() {
			$navToggle.html(NavigationBarHide);
			if (navContent)
				navContent.style.display = "block";
		},
		function hide() {
			$navToggle.html(NavigationBarShow);
			if (navContent)
				navContent.style.display = "none";
		});
}


/* ==Hidden Quotes== */
function setupHiddenQuotes(li) {
	var HQToggle, liComp, dl;
	var HQShow = "quotations ▼";
	var HQHide = "quotations ▲";
	function show() {
		HQToggle.html(HQHide);
		$(li).children("ul").show();
	}
	function hide() {
		HQToggle.html(HQShow);
		$(li).children("ul").hide();
	}
	
	for (var k = 0; k < li.childNodes.length; k++) {
		// Look at each component of the definition.
		liComp = li.childNodes[k];
		if (liComp.nodeName.toLowerCase() === "dl" && !dl) {
			dl = liComp;
		}
		// If we find a ul or dl, we have quotes or example sentences, and thus need a button.
		if (/^(ul|UL)$/.test(liComp.nodeName)) {
			HQToggle = $("<a>").attr("role", "button").attr("tabindex", "0");
			$(dl || liComp).before($("<span>").addClass("HQToggle").append(HQToggle));
			HQToggle.on("click", window.VisibilityToggles.register("quotations", show, hide));
			break;
		}
	}
}

/* == View Switching == */

function viewSwitching($rootElement) {
	var showButtonText = $rootElement.data("vs-showtext") || "more ▼";
	var hideButtonText = $rootElement.data("vs-hidetext") || "less ▲";
	
	var toSkip = $rootElement.find(".vsSwitcher").find("*");
	var elemsToHide = $rootElement.find(".vsHide").not(toSkip);
	var elemsToShow = $rootElement.find(".vsShow").not(toSkip);
	
	// Find the element to place the toggle button in.
	var toggleElement = $rootElement.find(".vsToggleElement").not(toSkip).first();
	
	// The toggleElement becomes clickable in its entirety, but
	// we need to prevent this if a contained link is clicked instead.
	toggleElement.children("a").on("click", function (e) {
		e.stopPropagation();
	});
	
	// Add the toggle button.
	var toggleButton = $("<a>").attr("role", "button").attr("tabindex", "0");
	
	$("<span>").addClass("NavToggle").append(toggleButton).prependTo(toggleElement);
	
	// Determine the visibility toggle category (for the links in the bar on the left).
	var toggleCategory = $rootElement.data("toggle-category");
	if (!toggleCategory) {
		var classNames = $rootElement.attr("class").split(/\s+/);
		
		for (var i = 0; i < classNames.length; ++i) {
			var className = classNames[i].split("-");
			
			if (className[0] == "vsToggleCategory") {
				toggleCategory = className[1];
			}
		}
	}
	
	if (!toggleCategory)
		toggleCategory = "others";
	
	// Register the visibility toggle.
	toggleElement.css("cursor", "pointer");
	toggleElement.on("click", window.VisibilityToggles.register(toggleCategory,
		function show() {
			toggleButton.html(hideButtonText);
			elemsToShow.hide();
			elemsToHide.show();
		},
		function hide() {
			toggleButton.html(showButtonText);
			elemsToShow.show();
			elemsToHide.hide();
		}));
}

/* ==List switching== */
// Number of rows of list items to show in the hidden state of a "list switcher" list.
// Customize by adding
// window.listSwitcherCount = <your preferred number of rows>;
// to your common.js before loading this script.
window.listSwitcherRowCount = window.listSwitcherRowCount || 3;

function getListItemsToHide($listItems, columnCount, rowsInShowState) {
	var count = $listItems.length;
	var itemsPerColumn = Math.ceil(count / columnCount);
	
	var $elemsToHide = $();
	if (itemsPerColumn > rowsInShowState) {
		for (var i = 0; i < columnCount; ++i) {
			var columnStart = i * itemsPerColumn;
			$elemsToHide = $elemsToHide
				.add($listItems.slice(columnStart + rowsInShowState, columnStart + itemsPerColumn));
		}
	}
	
	return $elemsToHide;
}

function enableListSwitch ($rootElement, rowsInShowState) {
	// Number of columns must be set in data-term-list-column-count attribute
	// of the element with class term-list.
	var $termList = $rootElement.find(".term-list");
	
	// Find the element to place the toggle button in.
	var $toggleElement = $rootElement.find(".list-switcher-element");
	
	var columnCount = parseInt($termList.data("column-count"), 10);
	if (!(columnCount && columnCount > 0)) {
		$toggleElement.hide();
		return;
	}
	
	var $listItems = $rootElement.find("ul").first().find("li");
	var $toHide = getListItemsToHide($listItems, columnCount, rowsInShowState);
	
	// Don't do anything if there aren't any items to hide.
	if ($toHide.length === 0) {
		$toggleElement.hide();
		return;
	}
	
	$toggleElement.css("cursor", "pointer");
	
	// Add the toggle button.
	var $toggleButton = $("<a>").attr("role", "button").attr("tabindex", "0");
	
	var rootBackgroundColor = $termList.css("background-color"),
		rootBackground = $termList.css("background");
	var $navToggle = $("<span>").addClass("NavToggle");
	if (rootBackgroundColor || rootBackground)
		$navToggle.css(rootBackgroundColor ? "background-color" : "background",
			rootBackgroundColor || rootBackground);
	
	// The $toggleElement becomes clickable in its entirety, but
	// we need to prevent this if a contained link is clicked instead.
	$toggleElement.children("a").on("click", function (e) {
		e.stopPropagation();
	});
	
	// Determine the visibility toggle category (for the links in the bar on the
	// left). It will either be the value of the "data-toggle-category"
	// attribute or will be based on the text of the closest preceding
	// fourth-to-sixth-level header.
	var toggleCategory = $rootElement.data("toggle-category")
		|| getToggleCategory($rootElement[0], "other lists");
	
	// Determine the text for the $toggleButton.
	var showButtonText = $toggleElement.data("showtext") || "more ▼";
	var hideButtonText = $toggleElement.data("hidetext") || "less ▲";
	
	// Register the visibility toggle.
	$toggleElement.on("click", window.VisibilityToggles.register(toggleCategory,
		function show() {
			$toggleButton.html(hideButtonText);
			$toHide.show();
		},
		function hide() {
			$toggleButton.html(showButtonText);
			$toHide.hide();
		}));
	
	// Add the toggle button to the DOM tree.
	$navToggle.append($toggleButton).prependTo($toggleElement);
	$toggleElement.show();
}

window.createNavToggle = createNavToggle;
window.setupHiddenQuotes = setupHiddenQuotes;
window.viewSwitching = viewSwitching;
window.getToggleCategory = getToggleCategory;

/* == Apply four functions defined above == */
mw.hook('wikipage.content').add(function($content) {
	// NavToggles
	$('.NavFrame', $content).each(function(){
		createNavToggle(this);
	});

	//quotes
	if (mw.config.get('wgNamespaceNumber') === 0) {
		// First, find all the ordered lists, i.e. all the series of definitions.
		$('ol > li', $content).each(function(){
			setupHiddenQuotes(this);
		});
	}

	//view switching
	$('.vsSwitcher', $content).each(function(){
		viewSwitching($(this));
	});

	// list switching
	$(".list-switcher", $content).each(function () {
		enableListSwitch($(this), window.listSwitcherRowCount);
	});
});

jQuery(mw).on("LivePreviewDone", function (ev, sels) {
	var ols = jQuery(sels.join(",")).find("ol");
	for (var i = 0; i < ols.length; i++) {
		for (var j = 0; j < ols[i].childNodes.length; j++) {
			var li = ols[i].childNodes[j];
			if (li.nodeName.toUpperCase() == "LI") {
				setupHiddenQuotes(li);
			}
		}
	}
});

})();
