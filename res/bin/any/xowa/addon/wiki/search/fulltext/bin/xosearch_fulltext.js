(function (xo) {
  xo.search_fulltext = new function() {
    this.send = function(proc, args) {
      try {xo.server.send_by_bridge('xowa.wiki.search.fulltext', proc, args);}
      catch (err) {alert(err);}
    }
    this.search = function() {
      try {
        var data = 
        { query:                  xo.elem.get_val_or_null('query_box')
        , wikis:                  xo.elem.get_val_or_null('wikis_box')
        , namespaces:             xo.elem.get_val_or_null('namespaces')
        , auto_wildcard_bgn:      xo.elem.get('auto_wildcard_bgn_box').checked
        , auto_wildcard_end:      xo.elem.get('auto_wildcard_end_box').checked
        , max_pages_per_wiki:     xo.elem.get_val_or_null('max_pages_per_wiki_box')
        , max_snips_per_page:     xo.elem.get_val_or_null('max_snips_per_page_box')
        };
        xo.search_fulltext.results__clear();
        xo.search_fulltext.send('search', data);
        document.getElementById('search_btn').focus();
      } catch (err) {
        alert(err);
      }
    }
    this.search_keydown = function(e) {
      if(e.keyCode === 13){
        e.preventDefault(); // Ensure it is only this code that runs
        xo.search_fulltext.search();
      }      
    }

    this.results__clear = function() {
      var elem = document.getElementById('results_wikis');
      elem.innerHTML = "<div id='results_wikis_last'>&nbsp;</div>";      
    }
    this.results__wiki__add__recv = function(msg) {return xo.search_fulltext.results__wiki__add(JSON.parse(msg));}
    this.results__wiki__add = function(msg) {
      try {
        var wiki = msg.wiki;
        var found = 0;
        var searched = 0;
        
        xowa.js.doc.elem_append_above('results_wikis_last'
        , "<div id='results_wiki_" + wiki + "' style='width:100%'>"
        + "  <div style='background-color:#ffe273'>"
        + "    <a href='javascript:toggle5(\"results_wiki_" + wiki + "_content\", \"results_wiki_" + wiki + "_img\");'>" 
        + "      <span id=\"results_wiki_" + wiki + "_img\" class='xoimg_help_x24 xoimg_list_remove'>&nbsp;</span>"
        + "    </a>"
        + "    <a href='/site/" + wiki + "/wiki/'>" + wiki + "</a> (<span id='results_wiki_" + wiki + "_found'>" + found + "</span> found; <span id='results_wiki_" + wiki + "_searched'>" + searched + "</span> searched)"
        + "  </div>"
        + "  <div id='results_wiki_" + wiki + "_content' style='display:block;'>"
        + "    <span id='results_wiki_" + wiki + "_page_last' style='display:none'>&nbsp;</span>"
        + "  </div>"
        + "</div>"
        + "<div style='clear:both;display:none;'>&nbsp;</div>"
        );
      } catch (err) {
        alert(err);
      }
      return true;
    }
    this.results__page__add__recv = function(msg) {return xo.search_fulltext.results__page__add(JSON.parse(msg));}
    this.results__page__add = function(msg) {
      try {
        var wiki = msg.wiki;
        var page_ttl = msg.page_ttl;
        var page_id = msg.page_id;
        var found = 0;
        var searched = 0;
        
        // more page vars; not worth sending in message
        var page_href = page_ttl.replace(/\'/g, '%27'); // escape apos, else will break "href=''"
        var page_name = page_ttl.replace(/_/g, " ");    // replace _ for display purposes
        
        xowa.js.doc.elem_append_above("results_wiki_" + wiki + "_page_last"
        , "<div id='results_wiki_" + wiki + "_page_" + page_id + "' style='margin-left:20px;display:block;'>"
        + "  <div style='background-color:#d5ffd5;'>"
        + "    <a href='javascript:toggle5(\"results_wiki_" + wiki + "_page_" + page_id + "_content\", \"results_wiki_" + wiki + "_page_" + page_id + "_img\");'>" 
        + "      <span id=\"results_wiki_" + wiki + "_page_" + page_id + "_img\" class='xoimg_help_x24 xoimg_list_add'>&nbsp;</span>"
        + "    </a>"
        + "    <a href='/site/" + wiki + "/wiki/" + page_href + "'>" + page_name + "</a> (<span id='results_wiki_" + wiki + "_page_" + page_id + "_found'>" + found + "</span> found)"
        + "  </div>"
        + "  <div id='results_wiki_" + wiki + "_page_" + page_id + "_content' style='display:none;'>"
        + "    <span id='results_wiki_" + wiki + "_page_" + page_id + "_last' style='display:none'>&nbsp;</span>"
        + "  </div>"
        + "</div>"
        );      
      } catch (err) {
        alert(err + " msg=" + JSON.stringify(msg));
      }
      return true;
    }
    this.results__wiki__update__recv = function(msg) {return xo.search_fulltext.results__wiki__update(JSON.parse(msg));}
    this.results__wiki__update = function(msg) {
      try {
        var wiki = msg.wiki;
        var found = msg.found;
        var searched = msg.searched;
        
        var elem = document.getElementById("results_wiki_" + wiki + "_found");
        elem.textContent = found;

        elem = document.getElementById("results_wiki_" + wiki + "_searched");
        elem.textContent = searched;
        } catch (err) {
        alert(err + " msg=" + JSON.stringify(msg));
      }
      return true;      
    }
    this.results__page__update__recv = function(msg) {return xo.search_fulltext.results__page__update(JSON.parse(msg));}
    this.results__page__update = function(msg) {
      try {
        var wiki = msg.wiki;
        var page_id = msg.page_id;
        var found = msg.found;
        
        var elem = document.getElementById("results_wiki_" + wiki + "_page_" + page_id + "_found");
        elem.textContent = found;
      } catch (err) {
        alert(err + " msg=" + JSON.stringify(msg));
      }
      return true;      
    }
    this.results__line__add__recv = function(msg) {
      try {
        return xo.search_fulltext.results__line__add(JSON.parse(msg));
      } catch (err) {
        alert(err + " msg=" + JSON.stringify(msg));
        return false;
      }
    }
    this.results__line__add = function(msg) {
      try {
        var wiki = msg.wiki;
        var page_id = msg.page_id;
        var line = msg.line;
        var html = msg.html;
        
        xowa.js.doc.elem_append_above("results_wiki_" + wiki + "_page_" + page_id + "_last"
        , "<div id='results_wiki_" + wiki + "_page_" + page_id + "_line_" + line + "' style='margin-left:28px;display:flex;'>"
        + "  <div><code>Match " + line + "</code>:</div>"
        + "  <div>" + html + "</div>"
        + "</div>"
        );      
      } catch (err) {
        alert(err + " msg=" + JSON.stringify(msg));
      }
      return true;
    }
    this.results__recv = function(msg_str) {
      try {
        var msg = JSON.parse(msg_str);
        this.results__set(msg.msg_text);
        return true;      
      } catch (err) {
        alert(err);
        return false;
      }
    }
  }
}(window.xo = window.xo || {}));
/*
xo.search_fulltext.results__wiki__add({wiki:"home", page_count:123});
xo.search_fulltext.results__page__add({wiki:"home", page:'Lua', lines:345});
xo.search_fulltext.results__line__add({wiki:"home", page:'Lua', line:'1', html:'this <b>found</b> it'});
xo.search_fulltext.results__line__add({wiki:"home", page:'Lua', line:'8', html:'not found b'});
xo.search_fulltext.results__line__add({wiki:"home", page:'Lua', line:'8', html:'not found b'});
xo.search_fulltext.results__page__add({wiki:"home", page:'JS', lines:345});
xo.search_fulltext.results__line__add({wiki:"home", page:'JS', line:'1', html:'this found it'});
xo.search_fulltext.results__line__add({wiki:"home", page:'JS', line:'8', html:'not found b'});
xo.search_fulltext.results__wiki__add({wiki:"mine", page_count:123});
*/
xo.notify.elem_anchor = '#main_div';
document.getElementById("query_box").focus();
// http://www.randomsnippets.com/2008/02/12/how-to-hide-and-show-your-div/
function toggle5(showHideDiv, switchImgTag) {
  var ele = document.getElementById(showHideDiv);
  var imageEle = document.getElementById(switchImgTag);
  if(ele.style.display == "block") {
    ele.style.display = "none";
    imageEle.className = "xoimg_help_x24 xoimg_list_add";
  }
  else {
    ele.style.display = "block";
    imageEle.className = "xoimg_help_x24 xoimg_list_remove";
  }
}
