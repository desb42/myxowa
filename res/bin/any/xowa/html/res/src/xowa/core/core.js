// Add Event Manager for simple pub sub pattern; DATE:2018-11-11
var XoEvtMgr = (function(){
  var funcs = [];
  function XoEvtMgr() {
  }
  
  XoEvtMgr.prototype.sub = function(func) {
    funcs.push(func);
  }
  
  XoEvtMgr.prototype.pub = function() {
    var funcsIdx = 0;
    var funcsLen = funcs.length;
    for (funcsIdx = 0; funcsIdx < funcsLen; funcsIdx++) {
      var func = funcs[funcsIdx];
      func(arguments);
    }
  }
  
  return XoEvtMgr;
}());

/*
window.mwNow = ( function () {
	var perf = window.performance,
		navStart = perf && perf.timing && perf.timing.navigationStart;
	return navStart && typeof perf.now === 'function' ?
		function () { return navStart + perf.now(); } :
		function () { return Date.now(); };
}() );
*/
window.mwNow = function () { return Date.now(); };

if (!window.xowa) {
  window.xowa = {
    root_dir : xowa_root_dir,
    js : {
      jquery : {
        init_done: false,
      },
      mediaWiki: {
        init_done: false,
      },
      doc: {
      },
      win: {
      },
      selection : {
      },
      xtn : {
      },
    },
    css : {},
    cfg : {},
    cookie: {},
    gfs : {},
    app : {},
    page : {},
    server : {},
  };

  xowa.css.add = function(css) {
    var s = document.createElement('style');
    document.getElementsByTagName('head')[0].appendChild(s);
    s.appendChild(document.createTextNode(css));
  };

  // borrowed from mediawiki\resources\src\mediawiki\mediawiki.js L143
  xowa.cfg.get = function(selection, fallback) {
			var results, i;
			var hasOwn = Object.prototype.hasOwnProperty;
			fallback = arguments.length > 1 ? fallback : null;

			if ( Array.isArray( selection ) ) {
				results = {};
				for ( i = 0; i < selection.length; i++ ) {
					if ( typeof selection[ i ] === 'string' ) {
						results[ selection[ i ] ] = hasOwn.call( window.xowa_global_values, selection[ i ] ) ?
							window.xowa_global_values[ selection[ i ] ] :
							fallback;
					}
				}
				return results;
			}

			if ( typeof selection === 'string' ) {
  	console.log(selection, window.xowa_global_values[ selection ])
				return hasOwn.call( window.xowa_global_values, selection ) ?
					window.xowa_global_values[ selection ] :
					fallback;
			}

			if ( selection === undefined ) {
				results = {};
				for ( i in window.xowa_global_values ) {
					results[ i ] = window.xowa_global_values[ i ];
				}
				return results;
			}

			// Invalid selection key
			return fallback;
    //return window.xowa_global_values[key] || null;
  };
  xowa.cfg.set = function(selection, value) {
    var s;
    if (arguments.length > 1) {
        if (typeof selection === 'string') {
            window.xowa_global_values[selection] = value;
            return !0;
        }
    } else if (typeof selection === 'object') {
        for (s in selection) {
            window.xowa_global_values[s] = selection[s];
        }
        return !0;
    }
    return !1;
  };

  xowa.cookie = function(key, val, cookieData) {
    if (val == null)  // accessor; EX: $.cookie('key');
        return xowa.cfg.get(key);
    else              // mutator;  EX: $.cookie('key', 'val');
        xowa.cfg.set(key, val);
  };

  xowa.js.load_lib = function(file, callback) {
    var script = document.createElement('script');
    if (callback) {
      script.onload = callback;
    }
    script.async = false;
    script.setAttribute('src', file);
    document.getElementsByTagName('head')[0].appendChild(script);
  };

  xowa.js.importStylesheetURI = function( url, media ) {
    var l = document.createElement( 'link' );
    l.rel = 'stylesheet';
    l.href = url;
    if ( media ) {
      l.media = media;
    }
    document.head.appendChild( l );
    return l;
  }
  xowa.js.jquery.init_callback = function() {
    jQuery.cookie = xowa.cookie;
    jQuery.ready(); //fire the ready event
  };
  xowa.js.jquery.init = function(document) {
  	if (jQuery.cookie !== undefined) return;
    //return; 
    jQuery.cookie = xowa.cookie;
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery.webfonts.js');
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/ext/ext.uls.preferences.js');
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/ext/ext.uls.webfonts.repository.js');
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/ext/ext.uls.webfonts.js');
    // loaded elsewhere
    return;
/*    if (xowa.js.jquery.init_done) return;
    //xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery-1.11.3.min.js', xowa.js.jquery.init_callback);
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/html/res/lib/jquery/jquery-3.2.1.js', xowa.js.jquery.init_callback);
    xowa.js.jquery.init_done = true;
*/
  };
    
	/**
	 * Private helper function used by util.escapeId*()
	 * @ignore
	 *
	 * @param {string} str String to be encoded
	 * @param {string} mode Encoding mode, see documentation for $wgFragmentMode
	 *     in DefaultSettings.php
	 * @return {string} Encoded string
	 */
	function escapeIdInternal( str, mode ) {
		str = String( str );

		switch ( mode ) {
			case 'html5':
				return str.replace( / /g, '_' );
			case 'html5-legacy':
				str = str.replace( /[ \t\n\r\f_'"&#%]+/g, '_' )
					.replace( /^_+|_+$/, '' );
				if ( str === '' ) {
					str = '_';
				}
				return str;
			case 'legacy':
				return rawurlencode( str.replace( / /g, '_' ) )
					.replace( /%3A/g, ':' )
					.replace( /%/g, '.' );
			default:
				throw new Error( 'Unrecognized ID escaping mode ' + mode );
		}
	}
	/**
	 * Encode the string like PHP's rawurlencode
	 * @ignore
	 *
	 * @param {string} str String to be encoded.
	 * @return {string} Encoded string
	 */
	function rawurlencode( str ) {
		str = String( str );
		return encodeURIComponent( str )
			.replace( /!/g, '%21' ).replace( /'/g, '%27' ).replace( /\(/g, '%28' )
			.replace( /\)/g, '%29' ).replace( /\*/g, '%2A' ).replace( /~/g, '%7E' );
	}

  xowa.js.mediaWiki.init = function(){
    if (xowa.js.mediaWiki.init_done) return;
  	window.mw = 
    window.mediaWiki = {
        msg: xowa.cfg.get,
        messages: {
        	set: xowa.cfg.set
        },
        log: function () {},
        config: {
            get: xowa.cfg.get
        },
        html: {
        	escape: function(val) { return val; }
        },
        experiments: {
		getBucket: function ( experiment, token ) {
			return 'control';
		}
        },
        user: {
    getPageviewToken: function () {
    	return "boohoo";
    },
		/**
		 * Get the current user's name
		 *
		 * @return {string|null} User name string or null if user is anonymous
		 */
		getName: function () {
			return mw.config.get( 'wgUserName' );
		},
		/**
		 * Generate a random user session ID.
		 *
		 * This information would potentially be stored in a cookie to identify a user during a
		 * session or series of sessions. Its uniqueness should not be depended on unless the
		 * browser supports the crypto API.
		 *
		 * Known problems with Math.random():
		 * Using the Math.random function we have seen sets
		 * with 1% of non uniques among 200,000 values with Safari providing most of these.
		 * Given the prevalence of Safari in mobile the percentage of duplicates in
		 * mobile usages of this code is probably higher.
		 *
		 * Rationale:
		 * We need about 64 bits to make sure that probability of collision
		 * on 500 million (5*10^8) is <= 1%
		 * See https://en.wikipedia.org/wiki/Birthday_problem#Probability_table
		 *
		 * @return {string} 64 bit integer in hex format, padded
		 */
		generateRandomSessionId: function () {
			var rnds, i,
				hexRnds = new Array( 2 ),
				// Support: IE 11
				crypto = window.crypto || window.msCrypto;

			if ( crypto && crypto.getRandomValues && typeof Uint32Array === 'function' ) {
				// Fill an array with 2 random values, each of which is 32 bits.
				// Note that Uint32Array is array-like but does not implement Array.
				rnds = new Uint32Array( 2 );
				crypto.getRandomValues( rnds );
			} else {
				rnds = [
					Math.floor( Math.random() * 0x100000000 ),
					Math.floor( Math.random() * 0x100000000 )
				];
			}
			// Convert number to a string with 16 hex characters
			for ( i = 0; i < 2; i++ ) {
				// Add 0x100000000 before converting to hex and strip the extra character
				// after converting to keep the leading zeros.
				hexRnds[ i ] = ( rnds[ i ] + 0x100000000 ).toString( 16 ).slice( 1 );
			}

			// Concatenation of two random integers with entropy n and m
			// returns a string with entropy n+m if those strings are independent
			return hexRnds.join( '' );
		},
		/**
		 * Whether the current user is anonymous
		 *
		 * @return {boolean}
		 */
		isAnon: function () {
			return mw.user.getName() === null;
		},

		/**
		 * Get an automatically generated random ID (persisted in sessionStorage)
		 *
		 * This ID is ephemeral for everyone, staying in their browser only until they
		 * close their browsing session.
		 *
		 * @return {string} Random session ID
		 */
		sessionId: function () {
			return sessionId = mw.user.generateRandomSessionId();
			var sessionId = mw.storage.session.get( 'mwuser-sessionId' );
			if ( !sessionId ) {
				sessionId = mw.user.generateRandomSessionId();
				mw.storage.session.set( 'mwuser-sessionId', sessionId );
			}
			return sessionId;
		},

		/**
		 * Get the current user's name or the session ID
		 *
		 * Not to be confused with #getId.
		 *
		 * @return {string} User name or random session ID
		 */
		id: function () {
			return mw.user.getName() || mw.user.sessionId();
		},

      	},
        util: {
        escapeRegExp: function (str) {
    return str.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
//        	return str.replace('$', '\\$');
        },
        	$content: window.jQuery ? jQuery('#mw-content-text') : null,
        	//$content: window.jQuery ? jQuery('#content') : null,
		/**
		 * Add a link to a portlet menu on the page, such as:
		 *
		 * p-cactions (Content actions), p-personal (Personal tools),
		 * p-navigation (Navigation), p-tb (Toolbox)
		 *
		 * The first three parameters are required, the others are optional and
		 * may be null. Though providing an id and tooltip is recommended.
		 *
		 * By default the new link will be added to the end of the list. To
		 * add the link before a given existing item, pass the DOM node
		 * (e.g. `document.getElementById( 'foobar' )`) or a jQuery-selector
		 * (e.g. `'#foobar'`) for that item.
		 *
		 *     util.addPortletLink(
		 *         'p-tb', 'https://www.mediawiki.org/',
		 *         'mediawiki.org', 't-mworg', 'Go to mediawiki.org', 'm', '#t-print'
		 *     );
		 *
		 *     var node = util.addPortletLink(
		 *         'p-tb',
		 *         new mw.Title( 'Special:Example' ).getUrl(),
		 *         'Example'
		 *     );
		 *     $( node ).on( 'click', function ( e ) {
		 *         console.log( 'Example' );
		 *         e.preventDefault();
		 *     } );
		 *
		 * @param {string} portlet ID of the target portlet ( 'p-cactions' or 'p-personal' etc.)
		 * @param {string} href Link URL
		 * @param {string} text Link text
		 * @param {string} [id] ID of the new item, should be unique and preferably have
		 *  the appropriate prefix ( 'ca-', 'pt-', 'n-' or 't-' )
		 * @param {string} [tooltip] Text to show when hovering over the link, without accesskey suffix
		 * @param {string} [accesskey] Access key to activate this link (one character, try
		 *  to avoid conflicts. Use `$( '[accesskey=x]' ).get()` in the console to
		 *  see if 'x' is already used.
		 * @param {HTMLElement|jQuery|string} [nextnode] Element or jQuery-selector string to the item that
		 *  the new item should be added before, should be another item in the same
		 *  list, it will be ignored otherwise
		 *
		 * @return {HTMLElement|null} The added element (a ListItem or Anchor element,
		 * depending on the skin) or null if no element was added to the document.
		 */
		addPortletLink: function ( portlet, href, text, id, tooltip, accesskey, nextnode ) {
			var $item, $link, $portlet, $ul;

			// Check if there's at least 3 arguments to prevent a TypeError
			if ( arguments.length < 3 ) {
				return null;
			}
			// Setup the anchor tag
			$link = $( '<a>' ).attr( 'href', href ).text( text );
			if ( tooltip ) {
				$link.attr( 'title', tooltip );
			}

			// Select the specified portlet
			$portlet = $( '#' + portlet );
			if ( $portlet.length === 0 ) {
				return null;
			}
			// Select the first (most likely only) unordered list inside the portlet
			$ul = $portlet.find( 'ul' ).eq( 0 );

			// If it didn't have an unordered list yet, create it
			if ( $ul.length === 0 ) {

				$ul = $( '<ul>' );

				// If there's no <div> inside, append it to the portlet directly
				if ( $portlet.find( 'div:first' ).length === 0 ) {
					$portlet.append( $ul );
				} else {
					// otherwise if there's a div (such as div.body or div.pBody)
					// append the <ul> to last (most likely only) div
					$portlet.find( 'div' ).eq( -1 ).append( $ul );
				}
			}
			// Just in case..
			if ( $ul.length === 0 ) {
				return null;
			}

			// Unhide portlet if it was hidden before
			$portlet.removeClass( 'emptyPortlet' );

			// Wrap the anchor tag in a list item (and a span if $portlet is a Vector tab)
			// and back up the selector to the list item
			if ( $portlet.hasClass( 'vectorTabs' ) ) {
				$item = $link.wrap( '<li><span></span></li>' ).parent().parent();
			} else {
				$item = $link.wrap( '<li></li>' ).parent();
			}

			// Implement the properties passed to the function
			if ( id ) {
				$item.attr( 'id', id );
			}

			if ( accesskey ) {
				$link.attr( 'accesskey', accesskey );
			}

			if ( tooltip ) {
				$link.attr( 'title', tooltip );
			}

			if ( nextnode ) {
				// Case: nextnode is a DOM element (was the only option before MW 1.17, in wikibits.js)
				// Case: nextnode is a CSS selector for jQuery
				if ( nextnode.nodeType || typeof nextnode === 'string' ) {
					nextnode = $ul.find( nextnode );
				} else if ( !nextnode.jquery ) {
					// Error: Invalid nextnode
					nextnode = undefined;
				}
				if ( nextnode && ( nextnode.length !== 1 || nextnode[ 0 ].parentNode !== $ul[ 0 ] ) ) {
					// Error: nextnode must resolve to a single node
					// Error: nextnode must have the associated <ul> as its parent
					nextnode = undefined;
				}
			}

			// Case: nextnode is a jQuery-wrapped DOM element
			if ( nextnode ) {
				nextnode.before( $item );
			} else {
				// Fallback (this is the default behavior)
				$ul.append( $item );
			}

			// Update tooltip for the access key after inserting into DOM
			// to get a localized access key label (T69946).
			//$link.updateTooltipAccessKeys();

			return $item[ 0 ];
		},
		/**
		 * Encode page titles for use in a URL
		 *
		 * We want / and : to be included as literal characters in our title URLs
		 * as they otherwise fatally break the title.
		 *
		 * The others are decoded because we can, it's prettier and matches behaviour
		 * of `wfUrlencode` in PHP.
		 *
		 * @param {string} str String to be encoded.
		 * @return {string} Encoded string
		 */
		wikiUrlencode: function ( str ) {
			//return util.rawurlencode( str )
			return rawurlencode( str )
				.replace( /%20/g, '_' )
				// wfUrlencode replacements
				.replace( /%3B/g, ';' )
				.replace( /%40/g, '@' )
				.replace( /%24/g, '$' )
				.replace( /%21/g, '!' )
				.replace( /%2A/g, '*' )
				.replace( /%28/g, '(' )
				.replace( /%29/g, ')' )
				.replace( /%2C/g, ',' )
				.replace( /%2F/g, '/' )
				.replace( /%7E/g, '~' )
				.replace( /%3A/g, ':' );
		},

		/**
		 * Get the link to a page name (relative to `wgServer`),
		 *
		 * @param {string|null} [pageName=wgPageName] Page name
		 * @param {Object} [params] A mapping of query parameter names to values,
		 *  e.g. `{ action: 'edit' }`
		 * @return {string} Url of the page with name of `pageName`
		 */
		getUrl: function ( pageName, params ) {
			var titleFragmentStart, url, query,
				fragment = '',
				title = typeof pageName === 'string' ? pageName : mw.config.get( 'wgPageName' );

			// Find any fragment
			titleFragmentStart = title.indexOf( '#' );
			if ( titleFragmentStart !== -1 ) {
				fragment = title.slice( titleFragmentStart + 1 );
				// Exclude the fragment from the page name
				title = title.slice( 0, titleFragmentStart );
			}

			// Produce query string
			if ( params ) {
				query = $.param( params );
			}
			if ( query ) {
				url = title ?
					util.wikiScript() + '?title=' + mw.util.wikiUrlencode( title ) + '&' + query :
					util.wikiScript() + '?' + query;
			} else {
				url = mw.config.get( 'wgArticlePath' )
					.replace( '$1', mw.util.wikiUrlencode( title ).replace( /\$/g, '$$$$' ) );
			}

			// Append the encoded fragment
			if ( fragment.length ) {
				url += '#' + mw.util.escapeIdForLink( fragment );
			}

			return url;
		},
		escapeIdForLink: function ( str ) {
			var mode = 'html5';

			return escapeIdInternal( str, mode );
		},

        },
        //simulate mediaWiki.hook: Execute functions queued for 'wikipage.content' directly, and ignore anything else
        hook: function (name) {
            return name === 'wikipage.content' ? {
              add: function (f) {
                     f(window.jQuery ? jQuery('#mw-content-text') : null);
              },
              remove: function () {},
              fire: function () {}
            } : name === 'wikipage.collapsibleContent' ? {
              add: function (f) {
              	//fc = f;
              	f(mw.collapsibleContent);
//                     f(window.jQuery ? $collapsible.find( '> .mw-collapsible-content' ) : null);
              },
              remove: function () {},
              fire: function (t) {
              	// different order!!!! wrong order HACK
              	mw.collapsibleContent = t;
              	//fc(t);
              	}
            } : {
              add: function () {},
              remove: function () {},
              fire: function () {}
            };
        },
        //simulate medaWiki.cookie
        cookie: {
        	get: function (key) {   return xowa.cfg.get(key); },
        	set: function (key, val) { xowa.cfg.set(key, val); }
        }
    };
    //xowa.js.jquery.init_callback() // possibly
    xowa.js.mediaWiki.init_done = true;
  }

  xowa.gfs.arg_yn = function(v) {return v ? "'y'" : "'n'";}
  xowa.gfs.arg_str = function(v) {return "'" + v.replace("'", "''") + "'";}
  xowa.gfs.http_cmd_url = null;
  xowa.gfs.exec = function(cbk, cmd) {
    if (xowa.cfg.get('mode_is_gui')) {
      var rv = xowa_exec('cmd', cmd);
      if (cbk != null)
        cbk(rv);
    }
    else {
      if (xowa.cfg.get('mode_is_http')) {
        var http_cmd_url = xowa.gfs.http_cmd_url;
        if (http_cmd_url == null) {
          xowa.js.jquery.init();
          http_cmd_url = 'http://localhost:' + xowa.cfg.get('http-port') + '/xowa-cmd:';
          xowa.gfs.http_cmd_url = http_cmd_url;
        }
        $.get(http_cmd_url + cmd).done(function(data) {
          if (cbk != null)
            cbk(rv);
        });
      }
      else
        throw 'xowa offline';
    }
  };

  // PURPOSE: focus body so that keyboard up / down will scroll html content after page loads
  xowa.js.win.focus_body = function() {
    var body_elems = document.getElementsByTagName('body');
    if (body_elems == null || body_elems.length == 0) {return false;} // no body found; shouldn't happen
    var body_elem = body_elems[0];
    if (body_elem.contentEditable == 'true') {
      body_elem.selectionStart = 0;
      body_elem.selectionEnd = 0;
      window.setTimeout(function(){body_elem.focus();}, 250);
    }
    else {
      var active_element_name = document.activeElement.nodeName.toLowerCase();
      if (active_element_name == 'html' || active_element_name == 'body') { // no anchor selected
        var elems = document.getElementsByTagName("a");
        if (elems != null && elems.length > 0) {
          elems[0].focus(); // focus first
        }
      }
    }
  };

  // PURPOSE: get vpos for restoring page position when moving forward / backward through pages
  xowa.js.win.vpos_get = function() {
    var getIndex = function(node) {
     var parent = node.parentNode, i = -1, child;
     while (parent && (child = parent.childNodes[++i]))
      if (child == node) return i;
     return -1;
    }
    var getPath = function(node) {
     var parent, path = [], index = getIndex(node);
     (parent = node.parentNode) && (path = getPath(parent));
     index > -1 && path.push(index);
     return path;
    }
    var sel = window.getSelection();
    if (sel.rangeCount == 0) return document.documentElement.scrollTop + '|0';  // occurs during view-only mode
    var rng = sel.getRangeAt(0);
    var rng_bgn = rng.startContainer;
    var pos = getPath(rng_bgn);
    return document.documentElement.scrollTop + '|' + pos.toString();
  };

  // PURPOSE: set vpos for restoring page position when moving forward / backward through pages
  xowa.js.win.vpos_set = function(node_path, scroll_top) {
    var getNode = function(path) {
      var node = document.documentElement, i = 0, index = 0;
      while((index = path[++i]) > -1) {
        node = node.childNodes[index];
      }
      return node;
    }
    var sel = window.getSelection();
    var rng = document.createRange();
    var path = new Array();
    path = [node_path];
    var nde = getNode(path);

    rng.selectNodeContents(nde);
    rng.setEnd(nde, 0); // removes selection
    sel.removeAllRanges();
    sel.addRange(rng);
    document.documentElement.scrollTop = scroll_top;
    return true;
  };

  // PURPOSE: find text in textarea (Edit / HTML modes)
  xowa.js.win.find_in_textarea_ctx = {};
  xowa.js.win.find_in_textarea = function(find_text, dir_fwd, case_match, wrap_find) {
    var find_in_textarea_main = function(find_text, dir_fwd, case_match, wrap_find) {
      var text_area = document.getElementById('xowa_edit_data_box');
      var full_txt = text_area.value;
      var ctx = xowa.js.win.find_in_textarea_ctx;
      var find_text_is_same = find_text == ctx.prv_find_text;
      ctx.prv_find_text = find_text;
      if (!case_match) {
        find_text = find_text.toLowerCase();
        full_txt = full_txt.toLowerCase();
      }
      var find_bgn = dir_fwd ? 0 : 9999999;
      var sel_bgn = text_area.selectionStart; 
      if (sel_bgn > -1 && find_text_is_same)  // selection active and find_text_is same; move pos forward to select next item
        find_bgn = sel_bgn + (dir_fwd ? 1 : -1);
      var found = find_text_in_textarea(text_area, dir_fwd, find_text, full_txt, find_bgn);
      if (!found && wrap_find) {
        find_bgn = dir_fwd ? 0 : 9999999;
        found = find_text_in_textarea(text_area, dir_fwd, find_text, full_txt, find_bgn);
      }  
      return find_bgn;
    }
    var find_text_in_textarea = function(text_area, dir_fwd, find_text, full_txt, find_bgn) {
      if (dir_fwd)
        find_bgn = full_txt.indexOf(find_text, find_bgn);
      else
        find_bgn = full_txt.lastIndexOf(find_text, find_bgn);
      if (find_bgn == -1) return false;
      var find_end = find_bgn + find_text.length;

      text_area.setSelectionRange(find_bgn, find_bgn + 1);  // select first character of find
      var ev = document.createEvent('KeyEvents');
      ev.initKeyEvent('keypress', true, true, window, false, false, false, false, 0, text_area.value.charCodeAt(find_bgn));  // simulate keypress to replace firstchar with itself;
      text_area.dispatchEvent(ev); // scrolls
      text_area.setSelectionRange(find_bgn, find_end);
      return true;
    }
    return find_in_textarea_main(find_text, dir_fwd, case_match, wrap_find);
  };
  
  // PURPOSE: scroll elem with id into view; used when navigating to Page#Header and scrolling page to #Header
  xowa.js.win.scroll_elem_into_view = function(elem_id) {
    var select_by_id_proc = function(id) {
      var sel = window.getSelection();
      sel.removeAllRanges();

      var nde = document.getElementById(id);
      if (nde == null) return false;
      highlight_nde(nde);
      return true;
    }
    var highlight_nde = function(nde) {  
      var rng = document.createRange();
      if (nde.childNodes.length > 0) nde = nde.childNodes[0];
      rng.selectNodeContents(nde);
      rng.setStart(nde, 0); // removes selection
      rng.setEnd(nde, 0); // removes selection

      // set selection
      var sel = window.getSelection();
      sel.removeAllRanges();
      sel.addRange(rng);

      // scroll to selection NOTE: this will not work for <p>'s that are taller than the window
      // window.setTimeout(function(){scrollIntoView_proc(nde);}, 100);
      scrollIntoView_proc(nde);
    }
    var scrollIntoView_proc = function (t) {
      if (typeof(t) != 'object') return;

      if (t.getRangeAt) { // t is selection
        if (t.rangeCount == 0) return;
        t = t.getRangeAt(0);
      }

      // if t is not an element, then move up hierarchy until element which accepts scrollIntoView()
      o = t;
      while (o && o.nodeType != 1) o = o.previousSibling;
      t = o || t.parentNode;
      if (t) t.scrollIntoView(true);
    }
    return select_by_id_proc(elem_id);
  };
  
  // PURPOSE: print preview
  xowa.js.win.print_preview = function() {
    window.print();
  };

  // PURPOSE: copy
  xowa.js.selection.get_text_or_href = function() {
    var sel = window.getSelection();            // check if anything is highlighted
    if (sel != null) {
      var sel_str = sel.toString();
      if (sel_str != "")
        return "1|" + sel;
    }
    var nde = document.activeElement;           // default to active anchor; typically this will be whatever is "clicked"
    if (nde == null) return "0|";
    return (nde.tagName == "A")                 // nothing clicked
      ? "2|" + nde.href
      : "0|"
      ;  
  };

  // PURPOSE: open selected text in new tab; Ctrl+G,Ctrl+F
  xowa.js.selection.get_href_or_text = function() {
    var sel_obj = window.getSelection(); if (sel_obj == null) return "0|";
    var sel_text = "1|" + sel_obj.toString();
    if (sel_obj.rangeCount == 0) return sel_text;
    var range = sel_obj.getRangeAt(0); if (range == null) return sel_text;
    var count = 0;
    var node = range.commonAncestorContainer;
    while (count < 8) { // NOTE: needs to be at least 3 to handle nested html inside anchor; EX.WP:[[Portal:Baseball/Categories and Main topics|<span style="color: black;">'''Categories & Topics'''</span>]]; also EX.WP:Wikipedia:Featured topics/Battlecruisers of Germany;
      if (node == null) return sel_text;
      if (node.attributes != null) {
        var atr = node.attributes['href'];
        if (atr != null) return "2|" + atr.value;
      }
      count = count + 1;
      node = node.parentNode;
    }
    return sel_text;
  };
  
  // PURPOSE: (1) middle-click on link to open in new tab; (2) highlight text and open in new tab
  xowa.js.selection.get_active_or_selection = function() {
    var nde = document.activeElement;           // default to active anchor; typically this will be whatever is "clicked"
    if (nde != null && nde.tagName == "A")      // anchor clicked
      return "2|" + nde.href;                   // return anchor's href
    var sel = window.getSelection();            // check if anything is highlighted
    if (sel != null) {
      var sel_str = sel.toString();
      if (sel_str != "")
        return "1|" + sel;                      // return highlighted text
    }
    return "0|";                                // return nothing
  };
  
  // PURPOSE: save file as
  xowa.js.selection.get_src_or_empty = function() {
    var nde = document.activeElement;           // default to active anchor; typically this will be whatever is "right-clicked"
    if (nde == null) return '';
    if (nde.tagName != 'A') return '';
    var subElements = nde.getElementsByTagName('IMG');
    if (subElements == null) return '';
    var subElement = subElements[0];
    if (subElement == null) return '';
    return subElement.src;
  };
  
  // PURPOSE: keyboard command: Ctrl+G,Ctrl+G
  xowa.js.selection.toggle_focus_for_anchor = function() {
    // selects node with blue background around text (same as highlighting text with mouse)
    var select_nde = function(nde2) {  
      // create rng
      var rng = document.createRange();
      rng.selectNodeContents(nde2);
      var child_len = nde2.childNodes.length;
      if (child_len == 0) {   // no children; shouldn't happen
        rng.setStart(nde2, 0);
        rng.setEnd(nde2, nde2.textContent.length);
      }
      else {                  // children exists; select 1st and nth;
        rng.setStart(nde2.childNodes[0], 0);
        var nth_nde = nde2.childNodes[child_len - 1];
        rng.setEnd(nth_nde, nth_nde.textContent.length);
      }

      // set selection
      var sel = window.getSelection();
      sel.removeAllRanges();
      sel.addRange(rng);    
    }
    var sel = window.getSelection();
    if (sel.rangeCount == 0) return;  // nothing selected; exit
    var rng = sel.getRangeAt(0);
    var nde1 = rng.startContainer;
    var count = 0;
    while (count < 8) {         // traverse up dom from selected node; 8 is arbitrary limit
      if (nde1 == null) return; // null-check, but also guards against not finding <a> in current selection;
      if (nde1.attributes != null) {
        var href = nde1.attributes['href'];
        if (href != null) {     // nde1 has href
          if (count == 0) {     // nde1 was <a> when count == 0; indicates that <a> was selected
            select_nde(nde1);   // select "internal text"
            nde1.blur();
            document.getElementsByTagName('body')[0].focus();
          }
          else {                // nde1 was "internal text" when count == 0; note that nde1 is now <a> at count == 1
            nde1.focus();       // focus nde1 which is now an <a>
          }
          return;
        }
      }
      count = count + 1;
      nde1 = nde1.parentNode;
    }
  }

  // PURPOSE: get active link when in editable mode (needs changes to Swt_browser) DATE:2015-06-23
  xowa.js.selection.get_active_for_editable_mode = function(atr_key, or_val) {
    var sel_obj = window.getSelection(); if (sel_obj == null) return or_val;
    if (sel_obj.rangeCount == 0) return or_val;
    var range = sel_obj.getRangeAt(0); if (range == null) return or_val;
    var count = 0;
    var node = range.commonAncestorContainer;
    // NOTE: needs to be at least 3 to handle nested html inside anchor
    // PAGE:en.w:Portal:Baseball/Categories and Main topics|<span style="color: black;">'''Categories & Topics'''</span>]]
    // PAGE:en.w:Wikipedia:Featured topics/Battlecruisers of Germany
    while (count < 8) { 
      if (node == null) return or_val;
      if (node.attributes != null) {
        var atr = node.attributes[atr_key];
        if (atr != null) return atr.value;
      }
      count = count + 1;
      node = node.parentNode;
    }
    return or_val;
  };

  xowa.js.doc.evtElemAdd = new XoEvtMgr();
  
  // PURPOSE: used when clicking on file to get xowa_title
  xowa.js.doc.root_html_get = function() {
    return document.getElementsByTagName("html")[0].innerHTML;
  };
  
  // PURPOSE: focuses html_box when leaving other gui widgets (escape key)
  xowa.js.doc.elem_focus = function(elem_id) {
    var nde = document.getElementById(elem_id);
    if (nde != null)
      nde.focus();
  };

  // PURPOSE: Math and Score to delete <pre> after replacing with content
  xowa.js.doc.elem_delete = function(elem_id) {
    var elem = document.getElementById(elem_id);
    elem.parentNode.removeChild(elem);
    return true;
  };

  // PURPOSE: async search
  xowa.js.doc.elem_append_above = function(elem_id, html) {
    var elem = document.getElementById(elem_id);
    elem.insertAdjacentHTML('beforebegin', html);
    
    xowa.js.doc.process_new_elem(elem.parentNode);  // NOTE: elem is placeholder item; html is inserted after it; need to call process_new_elem on parentNode; DATE:2015-08-03
    return true;
  };
/*
  xowa.js.doc.ElemAdd.publish()
*/
  // PURPOSE: process new element such as adding bindings; DATE:2015-07-09
  xowa.js.doc.process_new_elem = function(elem) {
     xowa.js.doc.evtElemAdd.pub(elem);
  }
  
  // PURPOSE: async search; gallery; imap
  xowa.js.doc.elem_replace_html = function(elem_id, html) {
    var elem = document.getElementById(elem_id);
    elem.insertAdjacentHTML('beforebegin', html);
    elem.parentNode.removeChild(elem); 
  };

  // PURPOSE: show image on page
  xowa.js.doc.elem_img_update = function(elem_id, elem_src, elem_width, elem_height) {
    if (document == null) return false;
    elem_src = decodeURIComponent(elem_src);
    //alert("esrc:" + elem_src);
    var elem = document.getElementById(elem_id);
    if (elem == null) return false;
    elem.src = elem_src;
    elem.width = elem_width;
    elem.height = elem_height;
    return true;
  };

  // PURPOSE: prefs_mgr; get edit_box val
  xowa.js.doc.atr_get_as_obj = function(elem_id, atr_key) {
    return document.getElementById(elem_id)[atr_key];
  };
  
  // PURPOSE: prefs_mgr; note that toString() is needed for bool which somehow comes back strangely through SWT
  xowa.js.doc.atr_get_to_str = function(elem_id, atr_key) {
    return document.getElementById(elem_id)[atr_key].toString();
  };

  // PURPOSE: search; set cancel_icon
  xowa.js.doc.atr_set = function(elem_id, atr_key, atr_val) {
    document.getElementById(elem_id).setAttribute(atr_key, atr_val);
    return true;
  };

  // PURPOSE: append val or set it if empty; used by redlink
  xowa.js.doc.atr_append_or_set = function(elem_id, atr_key, val) {
    var elem = document.getElementById(elem_id);
    var atr_val = elem[atr_key];
    if (atr_val && atr_val.length > 0)
      atr_val = atr_val + ' ' + val;
    else
      atr_val = val;
    elem.setAttribute(atr_key, atr_val);
    return true;
  };

  // PURPOSE: called by packed gallery. EX: en.w:National_Gallery_of_Art
  xowa.js.xtn.gallery_packed_exec = function() {
    gallery_packed_exec(jQuery, mediaWiki);
  };
  
  // PURPOSE: get or create object by path; EX: "obj__get('xowa', 'usr', 'bookmarks');" will create window.xowa.usr.bookmarks; DATE:2015-07-19
  obj__get = function(owner) {
    var len = arguments.length;
    for (var i = 1; i < len; ++i) {
      var arg = arguments[i];
      var next_owner = owner[arg];
      if (!next_owner) {
        next_owner = {};
        owner[arg] = next_owner;
      }
      owner = next_owner;
    }
    return owner;
  }

  function xowa_toggle_visible(prefix, visible) {
    var icon = document.getElementById(prefix + '-toggle-icon');
    var elem = document.getElementById(prefix + '-toggle-elem');
    if (visible == null)                       // visible not passed
      visible = !(elem.style.display === '');  // default to reverse of current visible state
    if (visible) {
      elem.style.display = '';
      icon.title = xowa_global_values.show;      
      icon.src = xowa_root_dir + 'bin/any/xowa/extra/app.general/twisty_down.png';
    }
    else {
      elem.style.display = 'none';
      icon.title = xowa_global_values.hide;
      icon.src = xowa_root_dir + 'bin/any/xowa/extra/app.general/twisty_right.png';
    }
    // xowa.gfs.exec(null, 'xowa.api.html.page.toggles.get(' + xowa.gfs.arg_str(prefix) + ').visible = ' + xowa.gfs.arg_yn(visible) + ';');
    xowa.gfs.exec(null, "app.cfg.set('app', 'xowa.html.toggles." + prefix + "', " + xowa.gfs.arg_yn(visible) + ");");
  };
  
  function xowa_elem_select(elem) {
    var rng = document.createRange();
    rng.selectNodeContents(elem);// select entire range
    rng.setEnd(elem, 0); // remove selection
    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(rng);
  }
  window.navigate_to = function(href) { // XOWA: expose publicly for alertify
    window.location = href;
  };

	function addCss(style) {
		if (style)
			xowa.css.add(style.css[0]);
	}
	function addMessages(messages) {
		if (messages) {
			for (key in messages) {
	    	xowa.cfg.set(key, messages[key]);
	    }
	  }
  }
	function addScript(script) {
		if (script)
			script( $, $ );
			//script( $, $, mw.loader.require, registry[ module ].module );
	}

	function xowa_implement( module, script, style, messages, templates ) {
		addMessages(messages || null);
		addScript(script || null);
		addCss(style || null);
//					registry[ name ].templates = templates || null;
	}


window.xowa_global_values['wgNamespaceIds'] = {
				media: -2,
				special: -1,
				'': 0,
				talk: 1,
				user: 2,
				user_talk: 3,
				wikipedia: 4,
				wikipedia_talk: 5,
				file: 6,
				file_talk: 7,
				mediawiki: 8,
				mediawiki_talk: 9,
				template: 10,
				template_talk: 11,
				help: 12,
				help_talk: 13,
				category: 14,
				category_talk: 15,
				image: 6,
				image_talk: 7,
				project: 4,
				project_talk: 5,
			};
window.xowa_global_values['wgFormattedNamespaces'] = {
				'-2': 'Media',
				'-1': 'Special',
				0: '',
				1: 'Talk',
				2: 'User',
				3: 'User talk',
				4: 'Wikipedia',
				5: 'Wikipedia talk',
				6: 'File',
				7: 'File talk',
				8: 'MediaWiki',
				9: 'MediaWiki talk',
				10: 'Template',
				11: 'Template talk',
				12: 'Help',
				13: 'Help talk',
				14: 'Category',
				15: 'Category talk',
			};
window.xowa_global_values['skin'] = "vector";

}


/* ------------------------------------ */
function obj__merge(lhs, rhs) {
  var rv = lhs;
  for (prop in rhs) {
    lhs[prop] = rhs[prop];
  }
  return rv;
}
function Xocmds() {
  this.cmds = {};
  this.add = function(key, cmd) {
    cmd.key = key;
    this.cmds[key] = cmd;
  }
  this.get = function(key) {
    var rv = this.cmds[key];
    if (rv == null) throw 'cmd not found: ' + key;
    if (rv.exec == null) throw 'cmd does not have function "exec": ' + key;
    return rv;
  }
  this.exec_by_str = function(cmd_key, data_as_str) {
    var data = null;
    try {data = JSON.parse(data_as_str);}
    catch(err) {alert(err);}
    this.exec(cmd_key, data);
  }
  this.exec = function(cmd_key, data) {
    var cmd = this.get(cmd_key);
    cmd.exec(data);
  }
  this.recv = function(orig_data, cbk_key, msg_str) {
    // deserialize msg
    var msg = null;
    try         {msg = JSON.parse(msg_str);}
    catch (err) {throw 'parse err: msg=' + msg_str + ' stack=' + err.stack;} 
    // do any notifications
    if (msg.notify) {
      this.exec('xowa.notify', {text:msg.notify.text, status:msg.notify.status});
    }
    if (msg.rslt && !msg.rslt.pass) return;
    // identify cmd to run; prefer server cmd over gui
    var cmd_key = msg.cmd;
    if (!cmd_key) {
      cmd_key = cbk_key;
      if (!cmd_key) return; // note that some msgs may not ever have anything to execute
    }
    // run cmd
    var msg_data = obj__merge(orig_data, msg.data);
    var cmd = this.get(cmd_key);
    cmd.exec(msg_data);
  }
  this.send = function(fmt, cmd, data, cbk) {
    try {
      var send_msg_str = cmd;     // if 'gfs', then msg == cmd; note that gfs which doesn't use data
      if (fmt == 'json') {
        var send_msg_obj = 
        { 'client' : xowa.client  // NOTE: values set by http_server in <head>
        , 'cmd'    : cmd
        , 'data'   : data
        };
        send_msg_str = JSON.stringify(send_msg_obj);
      }
      if (xowa.app.mode == 'gui') {
        var response = xowa_exec('exec_' + fmt, send_msg_str);
        try {xowa.cmds.recv(data, cbk, response);}
        catch (err) {throw Err_.msg(err, 'exec.send', 'gui callback failed', 'response', JSON.parse_safe(response));}
      }
      else {
        var xreq = new XMLHttpRequest();
        xreq.onreadystatechange = function() {
          if (xreq.readyState == 4 && xreq.status == 200) {
            var response = xreq.responseText;
            try {xowa.cmds.recv(data, cbk, response);}
            catch (err) {Err_.print(err, 'exec.send', 'async callback failed', 'cmd', cmd, 'data', data, 'response', JSON.parse_safe(response));}
          }
        };      
        var post_url = 'http://' + xowa.client.server_host + '/exec/' + fmt; // EX:http://localhost:8080/exec/json
        xreq.open('POST', post_url, true);
        var form_data = new FormData();
        form_data.append('msg', send_msg_str);
        form_data.append('app_mode', xowa.app.mode);
        xreq.send(form_data);
      }
    } catch (err) {Err_.print(err, 'exec.send', 'send failed', 'cmd', cmd, 'data', data);}
  };
};
JSON.parse_safe = function(s) {
  try {return JSON.parse(s);}
  catch (err) {return s;}
}
function Err_() {
  var frame_list = [];
  var frame;
  this.add_frame = function(type, hdr) {
    frame = {};
    frame_list.push(frame);
    frame.type = type;
    frame.hdr = hdr;
    frame.args = [];
  }
  this.add_args = function(args) {
    var args_len = args.length;
    for (var i = 0; i < args_len; i += 2) {
      var key = args[i];
      var val = i + 1 < args_len ? args[i+1] : "NULL_VAL";
      frame.args.push({key: key, val: val});
    }
  }
  this.to_json = function() {
    var rv = {};
    var len = frame_list.length;
    for (var i = 0; i < len; ++i) {
      var frame = frame_list[i];
      var sub = {};
      rv[i] = sub;
      sub.msg = '[' + frame.type + '] ' + frame.hdr;
      var args = frame.args;
      for (var arg_idx in args) {
        var arg = args[arg_idx];
        sub[arg.key] = arg.val;
      }
    }
    return JSON.stringify(rv, null, 2);
  }
}
Err_.msg = function(err, type, hdr) {
  var err_data = Err_.get_data(err);
  err_data.add_frame(type, hdr);
  err_data.add_args(Array.prototype.slice.call(arguments, 3));
  return err;
}
Err_.get_data = function(err) {
  var data = err.data;
  if (data == null) {
    data = new Err_();
    err.data = data;
  }
  return data;
}
function html__escape(s) {
  return s
       .replace(/&/g, "&amp;")
       .replace(/</g, "&lt;")
       .replace(/>/g, "&gt;")
       .replace(/"/g, "&quot;")
       .replace(/'/g, "&#039;");
}
Err_.print = function(err, type, hdr) {
  err_data = Err_.get_data(err);
  err_data.add_frame(type, hdr);
  err_data.add_args(Array.prototype.slice.call(arguments, 3));
  err__add
  ( err.message 
  + '\n' + html__escape(err_data.to_json())
  + '\n' + err.stack
  );
}

window.xowa.cmds = new Xocmds();

function Xonotify() {
  var loaded = false;
  this.exec = function(data) {
     if (!loaded) {      
      loaded = true;
      var notify_cmd = this;
      xowa.js.jquery.init();
      xowa.js.load_lib(xowa_root_dir + 'bin/any/xowa/html/res/lib/notifyjs/notifyjs-0.3.1.js'
      , function delayed_notify_exec() {
        notify_cmd.notify(data.text, data.status);
      }
      );
    }
    else
      this.notify(data.text, data.status);
  }
  this.notify = function(msg, type) {
    try {$.notify(msg, {className:type, globalPosition:'top center'});}
    catch (err) {Err_.print(err, 'xonotify', 'notify failed', 'msg', msg, 'type', type);}
  }
}
window.xowa.cmds.add('xowa.notify', new Xonotify());

      xowa.js.jquery.init();
      xowa.js.mediaWiki.init();


/* **** add storage ****************************** */
( function ( mw ) {
	'use strict';

	// Catch exceptions to avoid fatal in Chrome's "Block data storage" mode
	// which throws when accessing the localStorage property itself, as opposed
	// to the standard behaviour of throwing on getItem/setItem. (T148998)
	var
		localStorage = ( function () {
			try {
				return window.localStorage;
			} catch ( e ) {}
		}() ),
		sessionStorage = ( function () {
			try {
				return window.sessionStorage;
			} catch ( e ) {}
		}() );

	/**
	 * A wrapper for an HTML5 Storage interface (`localStorage` or `sessionStorage`)
	 * that is safe to call on all browsers.
	 *
	 * @class mw.SafeStorage
	 * @private
	 * @param {Object|undefined} store The Storage instance to wrap around
	 */
	function SafeStorage( store ) {
		this.store = store;
	}

	/**
	 * Retrieve value from device storage.
	 *
	 * @param {string} key Key of item to retrieve
	 * @return {string|null|boolean} String value, null if no value exists, or false
	 *  if localStorage is not available.
	 */
	SafeStorage.prototype.get = function ( key ) {
		try {
			return this.store.getItem( key );
		} catch ( e ) {}
		return false;
	};

	/**
	 * Set a value in device storage.
	 *
	 * @param {string} key Key name to store under
	 * @param {string} value Value to be stored
	 * @return {boolean} Whether the save succeeded or not
	 */
	SafeStorage.prototype.set = function ( key, value ) {
		try {
			this.store.setItem( key, value );
			return true;
		} catch ( e ) {}
		return false;
	};

	/**
	 * Remove a value from device storage.
	 *
	 * @param {string} key Key of item to remove
	 * @return {boolean} Whether the save succeeded or not
	 */
	SafeStorage.prototype.remove = function ( key ) {
		try {
			this.store.removeItem( key );
			return true;
		} catch ( e ) {}
		return false;
	};

	/**
	 * A wrapper for the HTML5 `localStorage` interface
	 * that is safe to call on all browsers.
	 *
	 * @class
	 * @singleton
	 * @extends mw.SafeStorage
	 */
	mw.storage = new SafeStorage( localStorage );

	/**
	 * A wrapper for the HTML5 `sessionStorage` interface
	 * that is safe to call on all browsers.
	 *
	 * @class
	 * @singleton
	 * @extends mw.SafeStorage
	 */
	mw.storage.session = new SafeStorage( sessionStorage );

}( window.mediaWiki ) );
/** end of storage **/

function amend_imgs() {
	
function ascii_to_hexa(str)
{
	var arr1 = [];
	for (var n = 0, l = str.length; n < l; n ++) 
     {
		var hex = Number(str.charCodeAt(n)).toString(16);
		arr1.push('%' + hex);
	 }
	return arr1.join('');
}

	if (xowa_global_values.mode_is_gui) {
    // go thru all images
    var i;
    var imgs = document.images;
    var ilen = imgs.length;
    var srcs = '';
    for (i = 0; i < ilen; i++) {
    	var img = imgs[i];
    	var isrc = img.src;
    	//var nsrc = decodeURIComponent(isrc);
    	var nsrc = decodeURIComponent(ascii_to_hexa(isrc));
    	if (isrc != nsrc) {
    		img.src = nsrc;
    		//alert(nsrc);
    	}
    }
  }
}
 jQuery( document ).ready( function ( $ ) {
	  var locjs = '/';
    if (window.location.pathname.substring(0,5) == '/xowa')
      locjs = '/xowa/';
    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/' + x_p.wiki + '/extra.js');
    xowa.js.importStylesheetURI(xowa.root_dir + 'bin/any/xowa/extra/' + x_p.wiki + '/extra.css')
//    xowa.js.load_lib(xowa.root_dir + 'bin/any/xowa/extra/' + x_p.wiki + '.js');
    var pageurl = locjs + x_p.wiki + '/wiki/' + xowa_global_values.wgPageName;
    //if (pageurl != location.pathname)
    //  window.history.pushState('page2', xowa_global_values.wgTitle, pageurl);
    
    amend_imgs()
   
} );
