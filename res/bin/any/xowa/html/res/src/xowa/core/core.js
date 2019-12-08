
/* Add Event Manager for simple pub sub pattern; DATE:2018-11-11 */
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

window.mwNow = ( function () {
	var perf = window.performance,
		navStart = perf && perf.timing && perf.timing.navigationStart;
	return navStart && typeof perf.now === 'function' ?
		function () { return navStart + perf.now(); } :
		function () { return Date.now(); };
}() );

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
  xowa.cfg.set = function(key, val) {
    window.xowa_global_values[key] = val;
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

  xowa.js.jquery.init_callback = function() {
    jQuery.cookie = xowa.cookie;
    jQuery.ready(); //fire the ready event
  };
  xowa.js.jquery.init = function(document) {
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
    
  xowa.js.mediaWiki.init = function(){
    if (xowa.js.mediaWiki.init_done) return;
  	window.mw = 
    window.mediaWiki = {
        msg: xowa.cfg.get,
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
        	return str.replace('$', '\\$');
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
		}

        },
        //simulate mediaWiki.hook: Execute functions queued for 'wikipage.content' directly, and ignore anything else
        hook: function (name) {
            return name === 'wikipage.content' ? {
              add: function (f) {
                     f(window.jQuery ? jQuery('#mw-content-text') : null);
              },
              remove: function () {},
              fire: function () {}
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
      icon.src = xowa_root_dir + 'bin/any/xowa/file/app.general/twisty_down.png';
    }
    else {
      elem.style.display = 'none';
      icon.title = xowa_global_values.hide;
      icon.src = xowa_root_dir + 'bin/any/xowa/file/app.general/twisty_right.png';
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

/* ********************************** */
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

slice = Array.prototype.slice;
	/**
	 * Object constructor for messages.
	 *
	 * Similar to the Message class in MediaWiki PHP.
	 *
	 * Format defaults to 'text'.
	 *
	 *     @example
	 *
	 *     var obj, str;
	 *     mw.messages.set( {
	 *         'hello': 'Hello world',
	 *         'hello-user': 'Hello, $1!',
	 *         'welcome-user': 'Welcome back to $2, $1! Last visit by $1: $3'
	 *     } );
	 *
	 *     obj = new mw.Message( mw.messages, 'hello' );
	 *     mw.log( obj.text() );
	 *     // Hello world
	 *
	 *     obj = new mw.Message( mw.messages, 'hello-user', [ 'John Doe' ] );
	 *     mw.log( obj.text() );
	 *     // Hello, John Doe!
	 *
	 *     obj = new mw.Message( mw.messages, 'welcome-user', [ 'John Doe', 'Wikipedia', '2 hours ago' ] );
	 *     mw.log( obj.text() );
	 *     // Welcome back to Wikipedia, John Doe! Last visit by John Doe: 2 hours ago
	 *
	 *     // Using mw.message shortcut
	 *     obj = mw.message( 'hello-user', 'John Doe' );
	 *     mw.log( obj.text() );
	 *     // Hello, John Doe!
	 *
	 *     // Using mw.msg shortcut
	 *     str = mw.msg( 'hello-user', 'John Doe' );
	 *     mw.log( str );
	 *     // Hello, John Doe!
	 *
	 *     // Different formats
	 *     obj = new mw.Message( mw.messages, 'hello-user', [ 'John "Wiki" <3 Doe' ] );
	 *
	 *     obj.format = 'text';
	 *     str = obj.toString();
	 *     // Same as:
	 *     str = obj.text();
	 *
	 *     mw.log( str );
	 *     // Hello, John "Wiki" <3 Doe!
	 *
	 *     mw.log( obj.escaped() );
	 *     // Hello, John &quot;Wiki&quot; &lt;3 Doe!
	 *
	 * @class mw.Message
	 *
	 * @constructor
	 * @param {mw.Map} map Message store
	 * @param {string} key
	 * @param {Array} [parameters]
	 */
	function Message( map, key, parameters ) {
		this.format = 'text';
		this.map = map;
		this.key = key;
		this.parameters = parameters === undefined ? [] : slice.call( parameters );
		return this;
	}

	Message.prototype = {
		/**
		 * Get parsed contents of the message.
		 *
		 * The default parser does simple $N replacements and nothing else.
		 * This may be overridden to provide a more complex message parser.
		 * The primary override is in the mediawiki.jqueryMsg module.
		 *
		 * This function will not be called for nonexistent messages.
		 *
		 * @return {string} Parsed message
		 */
		parser: function () {
			return this.parameters[0];
			//return mw.format.apply( null, [ this.map.get( this.key ) ].concat( this.parameters ) );
		},

		// eslint-disable-next-line valid-jsdoc
		/**
		 * Add (does not replace) parameters for `$N` placeholder values.
		 *
		 * @param {Array} parameters
		 * @chainable
		 */
		params: function ( parameters ) {
			var i;
			for ( i = 0; i < parameters.length; i++ ) {
				this.parameters.push( parameters[ i ] );
			}
			return this;
		},

		/**
		 * Convert message object to its string form based on current format.
		 *
		 * @return {string} Message as a string in the current form, or `<key>` if key
		 *  does not exist.
		 */
		toString: function () {
			var text;

			if ( !this.exists() ) {
				// Use ?key? as text if key does not exist
				// Err on the side of safety, ensure that the output
				// is always html safe in the event the message key is
				// missing, since in that case its highly likely the
				// message key is user-controlled.
				// '?' is used instead of '<' to side-step any
				// double-escaping issues.
				// (Keep synchronised with Message::toString() in PHP.)
				return '?' + mw.html.escape( this.key ) + '?';
			}

			if ( this.format === 'plain' || this.format === 'text' || this.format === 'parse' ) {
				text = this.parser();
			}

			if ( this.format === 'escaped' ) {
				text = this.parser();
				text = mw.html.escape( text );
			}

			return text;
		},

		/**
		 * Change format to 'parse' and convert message to string
		 *
		 * If jqueryMsg is loaded, this parses the message text from wikitext
		 * (where supported) to HTML
		 *
		 * Otherwise, it is equivalent to plain.
		 *
		 * @return {string} String form of parsed message
		 */
		parse: function () {
			this.format = 'parse';
			return this.toString();
		},

		/**
		 * Change format to 'plain' and convert message to string
		 *
		 * This substitutes parameters, but otherwise does not change the
		 * message text.
		 *
		 * @return {string} String form of plain message
		 */
		plain: function () {
			this.format = 'plain';
			return this.toString();
		},

		/**
		 * Change format to 'text' and convert message to string
		 *
		 * If jqueryMsg is loaded, {{-transformation is done where supported
		 * (such as {{plural:}}, {{gender:}}, {{int:}}).
		 *
		 * Otherwise, it is equivalent to plain
		 *
		 * @return {string} String form of text message
		 */
		text: function () {
			this.format = 'text';
			return this.toString();
		},

		/**
		 * Change the format to 'escaped' and convert message to string
		 *
		 * This is equivalent to using the 'text' format (see #text), then
		 * HTML-escaping the output.
		 *
		 * @return {string} String form of html escaped message
		 */
		escaped: function () {
			this.format = 'escaped';
			return this.toString();
		},

		/**
		 * Check if a message exists
		 *
		 * @see mw.Map#exists
		 * @return {boolean}
		 */
		exists: function () {
			return true;
			//return this.map.exists( this.key );
		}
	};

/* ******************************* */
/**
 * Library for simple URI parsing and manipulation.
 *
 * Intended to be minimal, but featureful; do not expect full RFC 3986 compliance. The use cases we
 * have in mind are constructing 'next page' or 'previous page' URLs, detecting whether we need to
 * use cross-domain proxies for an API, constructing simple URL-based API calls, etc. Parsing here
 * is regex-based, so may not work on all URIs, but is good enough for most.
 *
 * You can modify the properties directly, then use the #toString method to extract the full URI
 * string again. Example:
 *
 *     var uri = new mw.Uri( 'http://example.com/mysite/mypage.php?quux=2' );
 *
 *     if ( uri.host == 'example.com' ) {
 *         uri.host = 'foo.example.com';
 *         uri.extend( { bar: 1 } );
 *
 *         $( 'a#id1' ).attr( 'href', uri );
 *         // anchor with id 'id1' now links to http://foo.example.com/mysite/mypage.php?bar=1&quux=2
 *
 *         $( 'a#id2' ).attr( 'href', uri.clone().extend( { bar: 3, pif: 'paf' } ) );
 *         // anchor with id 'id2' now links to http://foo.example.com/mysite/mypage.php?bar=3&quux=2&pif=paf
 *     }
 *
 * Given a URI like
 * `http://usr:pwd@www.example.com:81/dir/dir.2/index.htm?q1=0&&test1&test2=&test3=value+%28escaped%29&r=1&r=2#top`
 * the returned object will have the following properties:
 *
 *     protocol  'http'
 *     user      'usr'
 *     password  'pwd'
 *     host      'www.example.com'
 *     port      '81'
 *     path      '/dir/dir.2/index.htm'
 *     query     {
 *                   q1: '0',
 *                   test1: null,
 *                   test2: '',
 *                   test3: 'value (escaped)'
 *                   r: ['1', '2']
 *               }
 *     fragment  'top'
 *
 * (N.b., 'password' is technically not allowed for HTTP URIs, but it is possible with other kinds
 * of URIs.)
 *
 * Parsing based on parseUri 1.2.2 (c) Steven Levithan <http://stevenlevithan.com>, MIT License.
 * <http://stevenlevithan.com/demo/parseuri/js/>
 *
 * @class mw.Uri
 */

/* eslint-disable no-use-before-define */

( function ( mw, $ ) {
	var parser, properties;

	/**
	 * Function that's useful when constructing the URI string -- we frequently encounter the pattern
	 * of having to add something to the URI as we go, but only if it's present, and to include a
	 * character before or after if so.
	 *
	 * @private
	 * @static
	 * @param {string|undefined} pre To prepend
	 * @param {string} val To include
	 * @param {string} post To append
	 * @param {boolean} raw If true, val will not be encoded
	 * @return {string} Result
	 */
	function cat( pre, val, post, raw ) {
		if ( val === undefined || val === null || val === '' ) {
			return '';
		}

		return pre + ( raw ? val : mw.Uri.encode( val ) ) + post;
	}

	/**
	 * Regular expressions to parse many common URIs.
	 *
	 * As they are gnarly, they have been moved to separate files to allow us to format them in the
	 * 'extended' regular expression format (which JavaScript normally doesn't support). The subset of
	 * features handled is minimal, but just the free whitespace gives us a lot.
	 *
	 * @private
	 * @static
	 * @property {Object} parser
	 */
	parser = {
    strict: "^\n(?:(?<protocol>[^:/?#]+):)?\n(?://(?:\n\t(?:\n\t\t(?<user>[^:@/?#]*)\n\t\t(?::(?<password>[^:@/?#]*))?\n\t)?@)?\n\t(?<host>[^:/?#]*)\n\t(?::(?<port>\\d*))?\n)?\n(?<path>(?:[^?#/]*/)*[^?#]*)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n",
    loose: /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?(?:(?:([^:@\/?#]*)(?::([^:@\/?#]*))?)?@)?([^:\/?#]*)(?::(\\d*))?((?:\/(?:[^?#](?![^?#\/]*\\.[^?#\/.]+(?:[?#]|$)))*\/?)?[^?#/]*)(?:\\?([^#]*))?(?:\\#(.*))?/
    //loose: /^(?:(?![^:@]+:[^:@\/]*@)(?<protocol>[^:\/?#.]+):)?(?:\/\/)?(?:(?:(?<user>[^:@\/?#]*)(?::(?<password>[^:@\/?#]*))?)?@)?(?<host>[^:\/?#]*)(?::(?<port>\\d*))?((?:\/(?:[^?#](?![^?#\/]*\\.[^?#\/.]+(?:[?#]|$)))*\/?)?[^?#/]*)(?:\\?(?<query>[^#]*))?(?:\\#(?<fragment>.*))?/
    //loose: /^\n(?:\n\t(?![^:@]+:[^:@/]*@)\n\t(?<protocol>[^:/?#.]+):\n)?\n(?://)?\n(?:(?:\n\t(?<user>[^:@/?#]*)\n\t(?::(?<password>[^:@/?#]*))?\n)?@)?\n(?<host>[^:/?#]*)\n(?::(?<port>\\d*))?\n(\n\t(?:/\n\t\t(?:[^?#]\n\t\t\t(?![^?#/]*\\.[^?#/.]+(?:[?#]|$))\n\t\t)*/?\n\t)?\n\t[^?#/]*\n)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n/
    //"strict.regexp": "^\n(?:(?<protocol>[^:/?#]+):)?\n(?://(?:\n\t(?:\n\t\t(?<user>[^:@/?#]*)\n\t\t(?::(?<password>[^:@/?#]*))?\n\t)?@)?\n\t(?<host>[^:/?#]*)\n\t(?::(?<port>\\d*))?\n)?\n(?<path>(?:[^?#/]*/)*[^?#]*)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n",
    //"loose.regexp": "^\n(?:\n\t(?![^:@]+:[^:@/]*@)\n\t(?<protocol>[^:/?#.]+):\n)?\n(?://)?\n(?:(?:\n\t(?<user>[^:@/?#]*)\n\t(?::(?<password>[^:@/?#]*))?\n)?@)?\n(?<host>[^:/?#]*)\n(?::(?<port>\\d*))?\n(\n\t(?:/\n\t\t(?:[^?#]\n\t\t\t(?![^?#/]*\\.[^?#/.]+(?:[?#]|$))\n\t\t)*/?\n\t)?\n\t[^?#/]*\n)\n(?:\\?(?<query>[^#]*))?\n(?:\\#(?<fragment>.*))?\n"
		//strict: mw.template.get( 'mediawiki.Uri', 'strict.regexp' ).render(),
		//loose: mw.template.get( 'mediawiki.Uri', 'loose.regexp' ).render()
	};

	/**
	 * The order here matches the order of captured matches in the `parser` property regexes.
	 *
	 * @private
	 * @static
	 * @property {Array} properties
	 */
	properties = [
		'protocol',
		'user',
		'password',
		'host',
		'port',
		'path',
		'query',
		'fragment'
	];

	/**
	 * @property {string} protocol For example `http` (always present)
	 */
	/**
	 * @property {string|undefined} user For example `usr`
	 */
	/**
	 * @property {string|undefined} password For example `pwd`
	 */
	/**
	 * @property {string} host For example `www.example.com` (always present)
	 */
	/**
	 * @property {string|undefined} port For example `81`
	 */
	/**
	 * @property {string} path For example `/dir/dir.2/index.htm` (always present)
	 */
	/**
	 * @property {Object} query For example `{ a: '0', b: '', c: 'value' }` (always present)
	 */
	/**
	 * @property {string|undefined} fragment For example `top`
	 */

	/**
	 * A factory method to create a Uri class with a default location to resolve relative URLs
	 * against (including protocol-relative URLs).
	 *
	 * @method
	 * @param {string|Function} documentLocation A full url, or function returning one.
	 *  If passed a function, the return value may change over time and this will be honoured. (T74334)
	 * @member mw
	 * @return {Function} Uri class
	 */
	mw.UriRelative = function ( documentLocation ) {
		var getDefaultUri = ( function () {
			// Cache
			var href, uri;

			return function () {
				var hrefCur = typeof documentLocation === 'string' ? documentLocation : documentLocation();
				if ( href === hrefCur ) {
					return uri;
				}
				href = hrefCur;
				uri = new Uri( href );
				return uri;
			};
		}() );

		/**
		 * Construct a new URI object. Throws error if arguments are illegal/impossible, or
		 * otherwise don't parse.
		 *
		 * @class mw.Uri
		 * @constructor
		 * @param {Object|string} [uri] URI string, or an Object with appropriate properties (especially
		 *  another URI object to clone). Object must have non-blank `protocol`, `host`, and `path`
		 *  properties. If omitted (or set to `undefined`, `null` or empty string), then an object
		 *  will be created for the default `uri` of this constructor (`location.href` for mw.Uri,
		 *  other values for other instances -- see mw.UriRelative for details).
		 * @param {Object|boolean} [options] Object with options, or (backwards compatibility) a boolean
		 *  for strictMode
		 * @param {boolean} [options.strictMode=false] Trigger strict mode parsing of the url.
		 * @param {boolean} [options.overrideKeys=false] Whether to let duplicate query parameters
		 *  override each other (`true`) or automagically convert them to an array (`false`).
		 */
		function Uri( uri, options ) {
			var prop, hrefCur,
				hasOptions = ( options !== undefined ),
				defaultUri = getDefaultUri();

			options = typeof options === 'object' ? options : { strictMode: !!options };
			options = $.extend( {
				strictMode: false,
				overrideKeys: false
			}, options );

			if ( uri !== undefined && uri !== null && uri !== '' ) {
				if ( typeof uri === 'string' ) {
					this.parse( uri, options );
				} else if ( typeof uri === 'object' ) {
					// Copy data over from existing URI object
					for ( prop in uri ) {
						// Only copy direct properties, not inherited ones
						if ( uri.hasOwnProperty( prop ) ) {
							// Deep copy object properties
							if ( Array.isArray( uri[ prop ] ) || $.isPlainObject( uri[ prop ] ) ) {
								this[ prop ] = $.extend( true, {}, uri[ prop ] );
							} else {
								this[ prop ] = uri[ prop ];
							}
						}
					}
					if ( !this.query ) {
						this.query = {};
					}
				}
			} else if ( hasOptions ) {
				// We didn't get a URI in the constructor, but we got options.
				hrefCur = typeof documentLocation === 'string' ? documentLocation : documentLocation();
				this.parse( hrefCur, options );
			} else {
				// We didn't get a URI or options in the constructor, use the default instance.
				return defaultUri.clone();
			}

			// protocol-relative URLs
			if ( !this.protocol ) {
				this.protocol = defaultUri.protocol;
			}
			// No host given:
			if ( !this.host ) {
				this.host = defaultUri.host;
				// port ?
				if ( !this.port ) {
					this.port = defaultUri.port;
				}
			}
			if ( this.path && this.path[ 0 ] !== '/' ) {
				// A real relative URL, relative to defaultUri.path. We can't really handle that since we cannot
				// figure out whether the last path component of defaultUri.path is a directory or a file.
				throw new Error( 'Bad constructor arguments' );
			}
			if ( !( this.protocol && this.host && this.path ) ) {
				throw new Error( 'Bad constructor arguments' );
			}
		}

		/**
		 * Encode a value for inclusion in a url.
		 *
		 * Standard encodeURIComponent, with extra stuff to make all browsers work similarly and more
		 * compliant with RFC 3986. Similar to rawurlencode from PHP and our JS library
		 * mw.util.rawurlencode, except this also replaces spaces with `+`.
		 *
		 * @static
		 * @param {string} s String to encode
		 * @return {string} Encoded string for URI
		 */
		Uri.encode = function ( s ) {
			return encodeURIComponent( s )
				.replace( /!/g, '%21' ).replace( /'/g, '%27' ).replace( /\(/g, '%28' )
				.replace( /\)/g, '%29' ).replace( /\*/g, '%2A' )
				.replace( /%20/g, '+' );
		};

		/**
		 * Decode a url encoded value.
		 *
		 * Reversed #encode. Standard decodeURIComponent, with addition of replacing
		 * `+` with a space.
		 *
		 * @static
		 * @param {string} s String to decode
		 * @return {string} Decoded string
		 */
		Uri.decode = function ( s ) {
			return decodeURIComponent( s.replace( /\+/g, '%20' ) );
		};

		Uri.prototype = {

			/**
			 * Parse a string and set our properties accordingly.
			 *
			 * @private
			 * @param {string} str URI, see constructor.
			 * @param {Object} options See constructor.
			 */
			parse: function ( str, options ) {
				var q, matches,
					uri = this,
					hasOwn = Object.prototype.hasOwnProperty;

				// Apply parser regex and set all properties based on the result
				//matches = parser[ options.strictMode ? 'strict' : 'loose' ].exec( str );
				matches = str.match(parser[ options.strictMode ? 'strict' : 'loose' ]);
				properties.forEach( function ( property, i ) {
					uri[ property ] = matches[ i + 1 ];
				} );

				// uri.query starts out as the query string; we will parse it into key-val pairs then make
				// that object the "query" property.
				// we overwrite query in uri way to make cloning easier, it can use the same list of properties.
				q = {};
				// using replace to iterate over a string
				if ( uri.query ) {
					uri.query.replace( /(?:^|&)([^&=]*)(?:(=)([^&]*))?/g, function ( $0, $1, $2, $3 ) {
						var k, v;
						if ( $1 ) {
							k = Uri.decode( $1 );
							v = ( $2 === '' || $2 === undefined ) ? null : Uri.decode( $3 );

							// If overrideKeys, always (re)set top level value.
							// If not overrideKeys but this key wasn't set before, then we set it as well.
							if ( options.overrideKeys || !hasOwn.call( q, k ) ) {
								q[ k ] = v;

							// Use arrays if overrideKeys is false and key was already seen before
							} else {
								// Once before, still a string, turn into an array
								if ( typeof q[ k ] === 'string' ) {
									q[ k ] = [ q[ k ] ];
								}
								// Add to the array
								if ( Array.isArray( q[ k ] ) ) {
									q[ k ].push( v );
								}
							}
						}
					} );
				}
				uri.query = q;

				// Decode uri.fragment, otherwise it gets double-encoded when serializing
				if ( uri.fragment !== undefined ) {
					uri.fragment = Uri.decode( uri.fragment );
				}
			},

			/**
			 * Get user and password section of a URI.
			 *
			 * @return {string}
			 */
			getUserInfo: function () {
				return cat( '', this.user, cat( ':', this.password, '' ) );
			},

			/**
			 * Get host and port section of a URI.
			 *
			 * @return {string}
			 */
			getHostPort: function () {
				return this.host + cat( ':', this.port, '' );
			},

			/**
			 * Get the userInfo, host and port section of the URI.
			 *
			 * In most real-world URLs this is simply the hostname, but the definition of 'authority' section is more general.
			 *
			 * @return {string}
			 */
			getAuthority: function () {
				return cat( '', this.getUserInfo(), '@' ) + this.getHostPort();
			},

			/**
			 * Get the query arguments of the URL, encoded into a string.
			 *
			 * Does not preserve the original order of arguments passed in the URI. Does handle escaping.
			 *
			 * @return {string}
			 */
			getQueryString: function () {
				var args = [];
				$.each( this.query, function ( key, val ) {
					var k = Uri.encode( key ),
						vals = Array.isArray( val ) ? val : [ val ];
					vals.forEach( function ( v ) {
						if ( v === null ) {
							args.push( k );
						} else if ( k === 'title' ) {
							args.push( k + '=' + mw.util.wikiUrlencode( v ) );
						} else {
							args.push( k + '=' + Uri.encode( v ) );
						}
					} );
				} );
				return args.join( '&' );
			},

			/**
			 * Get everything after the authority section of the URI.
			 *
			 * @return {string}
			 */
			getRelativePath: function () {
				return this.path + cat( '?', this.getQueryString(), '', true ) + cat( '#', this.fragment, '' );
			},

			/**
			 * Get the entire URI string.
			 *
			 * May not be precisely the same as input due to order of query arguments.
			 *
			 * @return {string} The URI string
			 */
			toString: function () {
				return this.protocol + '://' + this.getAuthority() + this.getRelativePath();
			},

			/**
			 * Clone this URI
			 *
			 * @return {Object} New URI object with same properties
			 */
			clone: function () {
				return new Uri( this );
			},

			/**
			 * Extend the query section of the URI with new parameters.
			 *
			 * @param {Object} parameters Query parameters to add to ours (or to override ours with) as an
			 *  object
			 * @return {Object} This URI object
			 */
			extend: function ( parameters ) {
				$.extend( this.query, parameters );
				return this;
			}
		};

		return Uri;
	};

	// Default to the current browsing location (for relative URLs).
	mw.Uri = mw.UriRelative( function () {
		return location.href;
	} );

}( mediaWiki, jQuery ) );

mw.message = function ( key ) {
			var parameters = xowa.cfg.get(key);
			return new Message( null, key, [parameters] );
		}

function fakenewTitle(title) {
		var t = Object.create( mw.Title.prototype ); //new Object();
		var n = title.indexOf("#");
		if (n >= 0) {
			t.title = title.substr(0, n);
			t.fragment = title.substr(n+1);
		} else {
			t.title = title;
			t.fragment = null;
		}
		t.namespace = 0;
		t.ext = null;
		return t;
}

mw.Title = function (txt) {
	return fakenewTitle(txt);
}
mw.Title.newFromText = fakenewTitle;
mw.Title.prototype.getNamespaceId = function () {
			return this.namespace;
		};
mw.Title.prototype.getMainText = function () {
			return this.title;
		};
mw.Title.prototype.getPrefixedDb = function () {
			return this.title;
		};
mw.Title.prototype.getUrl = function () {
			return this.title;
		};
mw.Title.prototype.getFragment = function () {
			return this.fragment;
		};

mw.now = mwNow;

window.xowa_global_values['wgContentNamespaces'] = [
	0
];
