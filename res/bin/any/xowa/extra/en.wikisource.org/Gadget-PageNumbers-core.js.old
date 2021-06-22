function init_setting(variable_name, cookie_name, init) {
    /* Sets JS variable to (in order of preference):
        1. previously initialized JS variable value
        2. current cookie value
        3. provided init parameter
        4. false

        Then, if cookie was not previously set, set it to current value of JS variable (this bit currently disabled)
    */
    // get current value of appropriate cookie
    var cookie_val = mw.cookie.get(cookie_name);
    //      some code so that people's cookies will change from digits to boolean values. remove after a week or so.
    //      in case you are wondering, this is to avoid the mixed use of digits as numbers and as booleans. --Eliyak
    //  if ( cookie_val === "0" ) { cookie_val = false; mw.cookie.set( cookie_name, false ); }
    //      else if ( cookie_val === "1" ) { cookie_val = true; mw.cookie.set( cookie_name, true ); }

    // If JS variable was not previously initialized in this page load, set it now to the cookie value.
    self[variable_name] = self[variable_name] || cookie_val;

    // If JS variable still has no value, use provided init value. If no init value provided, use false.
    if (typeof self[variable_name] === "undefined" || self[variable_name] === null) {
        self[variable_name] = init || false;
    }

    // If cookie was not set, set it now to the current value of the JS variable.
    //  disabling for now - cookie should not need to be set if value was initialized but not changed afterwards.
    //  if ( typeof cookie_val === "undefined" ) mw.cookie.set( cookie_name, self[ variable_name ] );

    // If JS variable is now the string "false", convert to boolean false (to fix JS confusion where "false" string evaluates to true).
    if (self[variable_name] === "false") {
        self[variable_name] = false;
    }
}

if (!self.ws_messages) {
    self.ws_messages = {};
}

window.ws_msg = function(name) {
    var m = self.ws_messages[name];
    if (m) return m;
    else return name;
};

/**
 * Messages are configurable here
 */
self.ws_messages = {
    'do': 'Display Options',
    'displayOptions': 'Display Options',
    'optlist': 'Display Options',
    'p-do': 'Display Options',
    'page_numbers_hidden': 'Page links hidden',
    'page_numbers_displayed': 'Page links displayed',
    'page_numbers_inline': 'Page links within text',
    'page_numbers_beside': 'Page links beside text',
    'layout_name': 'Layout',
    'layout': 'Layout',
    'layout_1': 'Layout 1',
    'layout_2': 'Layout 2',
    'layout_3': 'Layout 3',
    'layout_4': 'Layout 4',
    'proposed_layout': 'Proposed Layout',
};

// Dynamic layouts
if (!self.ws_layouts) {
    self.ws_layouts = {};
}
self.ws_layouts['Layout 1'] = {
    '#pageContainer': '',
    '#regionContainer': '',
    '#columnContainer': '',
    '.sidenote-right': 'float:right; margin:0.5em; padding:3px; border:solid 1px gray; max-width:9em; text-indent:0em; text-align:left;',
    '.sidenote-left': 'float:left; margin:0.5em; padding:3px; border:solid 1px gray; max-width:9em; text-indent:0em; text-align:left;',
    '.mw-editsection': '',
    '#headerContainer': ''
};
self.ws_layouts['Layout 2'] = {
    '#pageContainer': '',
    '#regionContainer': 'width:36em; margin:0 auto 0 auto; font-family:Georgia,serif;',
    '#columnContainer': 'text-align:justify;',
    '.sidenote-right': 'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
    '.sidenote-left': 'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
    '.mw-editsection': '',
    '#headerContainer': 'font-family:sans-serif;'
};
self.ws_layouts['Layout 3'] = {
    '#pageContainer': '',
    '#regionContainer': 'min-width:60em; float:left; width:100%; margin-right:-23em;',
    '#columnContainer': 'position:relative; text-align:justify; margin-right:23em; text-indent:0em; padding-left:0px; padding-right:0px; width:auto;',
    '.sidenote-right': 'position:absolute; right:-10em; width:9em; background-color:#eeeeee; text-indent:0em; text-align:left;',
    '.sidenote-left': 'position:absolute; right:-10em; width:9em; background-color:#eeeeee; text-indent:0em; text-align:left;',
    '.mw-editsection': '',
    '#headerContainer': 'position:absolute; top:0em; right:-23em; width:21em; float:right; text-align:left;'
};
self.ws_layouts['Layout 4'] = {
    '#pageContainer': '',
    '#regionContainer': 'width:540px; margin:0 auto 0 auto; font-family:sans-serif;',
    '#columnContainer': 'text-align:justify;',
    '.sidenote-right': 'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
    '.sidenote-left': 'position:absolute; left:37em; width:16em; text-indent:0em; text-align:left;',
    '.mw-editsection': '',
    '#headerContainer': 'font-family:sans-serif;'
};
self.ws_layouts['Proposed Layout'] = {
    '#pageContainer': '',
    '#regionContainer': '',
    '#columnContainer': 'margin-right:calc(1rem * 9); text-align:justify;',
    '.sidenote-right': 'position:absolute; right:-10em; width:9.00em; background-color:#eeeeee; text-indent:0.00em; text-align:left;',
    '.sidenote-left': 'position:absolute; right:-10em; width:9.00em; background-color:#eeeeee; text-indent:0.00em; text-align:left;',
    '.mw-editsection': '',
    '#headerContainer': 'font-family:sans-serif;'
};

var layout = function() {
    if (!self.ws_layouts) {
        self.ws_layouts = {};
    } else {
        self.ws_layouts.names = [];
    }

    var n = 0;
    for (var key in self.ws_layouts) {
        self.ws_layouts[key].number = n; // get number easily in the future
        self.ws_layouts.names[n] = key; // get name from number easily as well
        n++;
    }
    n -= 1;

    function toggle() {
        set_by_number((self.ws_layouts[self.layout_name].number + 1) % n, true);
    }

    function set_by_number(number, persist) {
        set_by_name(self.ws_layouts.names[number], persist);
    }

    function set_by_name(name, persist) {
        var selected_layout = self.ws_layouts[name];
        if (!selected_layout) return; //does not exist

        $.each(selected_layout, function(selector, style) {
            $(selector).attr("style", style);
        });

        $("#d-textLayout").children("a").html(name);

        self.layout_name = name;
        if (persist) mw.cookie.set("layout", name);

        pagenumbers.refresh_offsets();
    }

    function init() {
        var name;
        // do return if we're already set up
        if (document.getElementById("pageContainer")) {
            return;
        }

        // get_optlist();
        var portletLink = mw.util.addPortletLink(
            "p-do",
            "#",
            ws_msg("layout"),
            "d-textLayout",
            "The designation of the dynamic layout being applied [alt-l]",
            "l"
        );
        $(portletLink).click(function(e) {
            e.preventDefault();
            toggle();
        });

        // Check for presence of Proofreading extension by looking for pr_quality color status bar
        // or any .prp-pages-output blocks
        if ($('table.pr_quality').length || $(".prp-pages-output").length) {
            // remove all these classes to maintain backwards-compatibility
            $('div.text, .lefttext, .centertext, .indented-page, .prose').removeClass();
            // DynamicFlaw - a independent Div should have been the parent to this 3-into-1 step
            $('#mw-content-text').contents().not('.dynlayout-exempt')
                .wrapAll('<div id="pageContainer"><div id="regionContainer"><div id="columnContainer"></div></div></div>');
        }

        // If cookie is not set, default layout is first available option. Use index "0" in case layout name is ever changed.
        init_setting("layout_name", "layout", "0");

        if (self.layout_overrides_have_precedence || !mw.cookie.get("layout")) {
            name = $("#dynamic_layout_overrider").text();
        }

        name = name || self.layout_name;

        if ($.isNumeric(name)) {
            set_by_number(name, false);
        } else if (name) {
            set_by_name(name, false);
        } else {
            set_by_number(0, false);
        }
    }

    return {
        init: init
    };
}();


var pagenumbers = function() {

    // some shared variables to avoid selecting these elements repeatedly
    var container;
    var div_pagenumbers;
    var dp_y;
    var y_prev;
    var pagenumbers_collection;
    var div_ss;
    var div_highlight;

    var show_params = {
        link_text: ws_msg("page_numbers_displayed"),
        visible: true
    };
    var hide_params = {
        link_text: ws_msg("page_numbers_hidden"),
        visible: false
    };

    function toggle_visible() {
        var params = self.proofreadpage_numbers_visible ? hide_params : show_params;

        pagenumbers_collection.toggleClass('pagenumber-invisible', !params.visible);
        $("#d-pageNumbers_visible").children("a").html(params.link_text);
        self.proofreadpage_numbers_visible = params.visible;
        mw.cookie.set("pagenums_visible", params.visible);
    }

    function toggle_inline() {
        // toggle inline view unless layouts are not set up
        self.proofreadpage_numbers_inline = !layout || !self.proofreadpage_numbers_inline;
        $("#d-pageNumbers_inline").children("a").html(ws_msg(self.proofreadpage_numbers_inline ? "page_numbers_inline" : "page_numbers_beside"));
        mw.cookie.set("pagenums_inline", self.proofreadpage_numbers_inline);
        refresh_display();
    }

    function pagenum_in() {
        if (self.proofreadpage_disable_highlighting) {
            return false;
        }
        if (!div_highlight) {
            return false; //could not find it
        }
        var id = this.id.substring(11);

        var page_span = $(document.getElementById(id));
        var next = self.pagenum_ml.eq(self.pagenum_ml.index(page_span) + 1);
        if (next.length === 0) {
            next = div_ss;
        }

        // we need to use document offsets in case a page break occurs within a positioned element
        var c_os = container.offset();
        var ps_os = page_span.offset();
        var n_os = next.offset();

        ps_os = {
            top: ps_os.top - c_os.top,
            left: ps_os.left - c_os.left
        };
        n_os = {
            top: n_os.top - c_os.top,
            left: n_os.left - c_os.left
        };

        div_highlight.css({
            "display": "block",
            "top": ps_os.top + "px"
        });
        div_highlight.children().eq(0).css({
            "height": page_span.height() + "px",
            "width": (ps_os.left < 1) ? "100%" : ((container.width() - ps_os.left) + "px")
        });
        // div_ss.height() ~= height of 1 line of text
        div_highlight.children().eq(1).css("height", (n_os.top - ps_os.top - page_span.height()) + "px");
        div_highlight.children().eq(2).css({
            "height": next.height() + "px",
            "width": n_os.left + "px"
        });
        return true;
    }

    function pagenum_out() {
        if (self.proofreadpage_disable_highlighting) {
            return false;
        }
        if (!div_highlight) {
            return false; //could not find it
        }
        div_highlight.css("display", "none");
        div_highlight.children().eq(0).css("width", "0px");
        div_highlight.children().eq(1).css("height", "0px");
        div_highlight.children().eq(2).css("width", "0px");
        return true;
    }

    function init() {
        // skip if pagenumbers are already set up
        if (pagenumbers_collection) {
            return false;
        }

        // get_optlist();
        init_setting("proofreadpage_numbers_visible", "pagenums_visible", true);
        var portletLink = mw.util.addPortletLink(
            "p-do",
            "#",
            self.proofreadpage_numbers_visible ? ws_msg("page_numbers_displayed") : ws_msg("page_numbers_hidden"),
            "d-pageNumbers_visible",
            "The current state of embedded link visibility [alt-n]",
            "n"
        );
        $(portletLink).on("click", function(e) {
            e.preventDefault();
            toggle_visible();
        });

        init_setting("proofreadpage_numbers_inline", "pagenums_inline", false);

        // if layouts are not initialized show pagenumbers inline since "beside" view won't work
        if (!layout) {
            self.proofreadpage_numbers_inline = true;
        }

        portletLink = mw.util.addPortletLink(
            "p-do",
            "#",
            self.proofreadpage_numbers_inline ? ws_msg("page_numbers_inline") : ws_msg("page_numbers_beside"),
            "d-pageNumbers_inline",
            "The current positioning used for embedded link presentation [alt-i]",
            "i"
        );

        $(portletLink).on("click", function(e) {
            e.preventDefault();
            toggle_inline();
        });

        var opacity = "background-color:#000000; opacity:0.2; filter:alpha(opacity=20);";
        // store container for the highlight to shared variable "div_highlight"
        div_highlight = $('<div id= "highlight-area" style= "display:none; position:absolute; width:100%;">' +
            '<div style= "' + opacity + ' float:right; width:0px;"><div class= "clearFix"></div></div>' +
            '<div style= "' + opacity + ' width:100%; height:0px; clear:both;"></div>' +
            '<div style= "' + opacity + ' width:0px;"><div class= "clearFix" style= "float:left; clear:both;"></div></div>' +
            '</div>');

        // assign new div element to shared variable "div_ss"
        div_ss = $('<div id= "my-ss"><div class= "clearFix"></div></div>'); //empty span following some text

        // put divs in the innermost dynamic layout container
        if (layout) {
            container = $("#columnContainer")
                .append(div_highlight);
            $(".mw-content-ltr").append(div_ss);
        } else {
            $(".mw-content-ltr").append(div_highlight, div_ss);
        }

        self.pagenum_ml = $(".pagenum");
        refresh_display();
    }

    function refresh_display() {
        // determine if we need to set things up
        var init = !pagenumbers_collection;

        // JQuery collection of all pagenumber elements
        if (!init) {
            pagenumbers_collection.remove();
        }
        pagenumbers_collection = $();

        if (div_pagenumbers) {
            div_pagenumbers.remove();
        }

        if (!self.proofreadpage_numbers_inline) {
            // html div container for page numbers stored in shared variable div_pagenumbers

            //  put pagenumbers container div in the outermost layout container
            div_pagenumbers = $('<div>')
                .attr('id', 'ct-pagenumbers')
                .appendTo('div#pageContainer');
            dp_y = div_pagenumbers.offset().top;
            y_prev = {
                val: -10
            };
        }
        self.pagenum_ml.each(init ? init_elem : setup_elem);

        if (self.proofreadpage_numbers_inline) {
            pagenumbers_collection.off("mouseenter mouseleave");
        } else {
            pagenumbers_collection.hover(pagenum_in, pagenum_out);
        }
    }

    function refresh_elem_offset(page_span, pagenumber) {
        var y = $(page_span).offset().top;
        pagenumber.css("top", y - dp_y);
        if (self.proofreadpage_numbers_visible && y - y_prev.val > 5) {
            y_prev.val = y;
            pagenumber.removeClass('pagenumber-invisible');
        } else {
            pagenumber.addClass('pagenumber-invisible');
        }
    }

    function refresh_offsets() {
        // do nothing if container is not set up
        if (self.proofreadpage_numbers_inline || !div_pagenumbers) {
            return false;
        }

        dp_y = div_pagenumbers.offset().top;
        y_prev = {
            val: -10
        };

        pagenumber = pagenumbers_collection.first();

        self.pagenum_ml.each(function(i, page_span) {
            refresh_elem_offset(page_span, pagenumber);
            pagenumber = pagenumber.next();
        });

        return true;
    }

    function init_elem(i, page_span) {
        var name = page_span.getAttribute("data-page-number") || page_span.id;

        // what if two pages have the same number? increment the id
        var pagenumber_id = "pagenumber_" + page_span.id;
        var count;

        if (pagenumbers_collection.is("#" + $.escapeSelector(pagenumber_id))) {
            count = (pagenumbers_collection.filter("[id ^= '" + pagenumber_id + "']").length + 1);
            page_span.id += ("_" + count);
            pagenumber_id += ("_" + count);
        }

        $.data(page_span, "pagenumber_id", pagenumber_id);
        var page_title = decodeURI(page_span.title).replace(/%26/g, "&").replace(/%3F/g, "?");
        var page_url =
            mw.config.get("wgArticlePath")
            .replace("$1", encodeURIComponent(page_title.replace(/ /g, "_")))
            // encodeURIComponent encodes '/', which breaks subpages
            .replace(/%2F/g, "/");

        // if transcluded Page: (ll) is a redlink then make page class (class_str) a redlink also
        var ll = page_span.parentNode.nextSibling;
        var class_str = '';
        var action_str = '';

        if (ll && ll.tagName === "A" && ll.className === "new") {
            class_str = ' class="new" ';
            action_str = '?action=edit&redlink=1';
        }

        $.data(
            page_span,
            "link_str",
            '<a href= "' + page_url + action_str + '"' +
            class_str +
            ' title= "' + mw.html.escape(page_title) + '">' +
            mw.html.escape(name) +
            '</a>'
        );

        setup_elem(i, page_span);
    }

    var inline_params = {
        elem: "span",
        link_pre: "&#x0020;[",
        link_post: "]"
    };
    var beside_params = {
        elem: "div",
        link_pre: "[",
        link_post: "]"
    };

    function setup_elem(i, page_span) {
        var params = self.proofreadpage_numbers_inline ? inline_params : beside_params;

        // styled also by classes: div.pagenumber or span.pagenumber
        var $pagenumber = $('<' + params.elem + '>')
            .attr('id' , $.data(page_span, 'pagenumber_id'))
            .addClass('pagenumber noprint')
            .append(params.link_pre + $.data(page_span, 'link_str') + params.link_post)
            .toggleClass('pagenumber-invisible', !self.proofreadpage_numbers_visible);

        if (!self.proofreadpage_numbers_inline) {
            refresh_elem_offset(page_span, $pagenumber);
        }

        // clear the span provided by [[MediaWiki:Proofreadpage pagenum template]]
        $(page_span).find('.pagenum-inner').empty();

        $pagenumber.appendTo(self.proofreadpage_numbers_inline ? page_span : div_pagenumbers);

        pagenumbers_collection = pagenumbers_collection.add($pagenumber);
    }

    return {
        init: init,
        refresh_offsets: refresh_offsets
    };
}();

if ($.inArray(mw.config.get("wgAction"), ["view", "submit", "purge"]) !== -1) {
    if (!self.debug_page_layout &&
            // don't do anything on DoubleWiki or difference comparison views
            document.URL.indexOf("match=") === -1 &&
            document.URL.indexOf("diff=") === -1 &&
            ('self.proofreadpage_source_href')) {

        if (!$.isEmptyObject(self.ws_layouts)) {
            layout.init();
        }

        $(function() {
            if ($(".pagenum").length) {
                pagenumbers.init();
                //$(window).load(pagenumbers.refresh_offsets);
                $(pagenumbers.refresh_offsets);
            }
        });
    }
    var position = window.location.hash.substring(1);
    if (position && document.getElementById(position)) {
        document.getElementById(position).scrollIntoView();
    }
}

/**
 * Install the DOM-ready hook to force header and footer content out of
 * Dynamic Layouts
*/
$(function() {

    $( '.acContainer' ).insertAfter( $( 'div.printfooter' ) );

    var $exempt_ftr = $("<div>")
        .addClass("dynlayout-exempt dynlayout-exempt-footer")
        .insertBefore('div#catlinks')
        .append($('.acContainer'))
        .append($( 'div.licenseContainer' ).not( 'div.licenseContainer div.licenseContainer' ))
        .append($('#editform'))
        .append($('#footertemplate'));

    var $exempt_hdr = $("<div>")
        .addClass("dynlayout-exempt dynlayout-exempt-header")
        .prependTo('div#mw-content-text')
        .prepend($('div#headerContainer'))
        .prepend($('div#heederContainer'))
        .prepend($('div#heedertemplate'))
        .prepend($('#mw-previewheader'))
});
