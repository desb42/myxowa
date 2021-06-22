mediaWiki.panel = function(data) {
  var skin = mw.config.get('skin');
  var panelInfo = {};
  if(skin=="monobook" || skin=="chick" || skin=="myskin" || skin=="simple") {
    panelInfo = {
      'portletClassName': 'portlet',
      'pBodyClassName': 'pBody',
      'columnId': 'column-one'
    };
  } else if(skin=="modern") {
    panelInfo = {
      'portletClassName': 'portlet',
      'pBodyClassName': 'pBody',
      'columnId': 'mw_portlets'
    };
  } else if(skin=="vector") {
    panelInfo = {
      'portletClassName': 'portal expanded',
      'pBodyClassName': 'body',
      'columnId': 'mw-panel'
    };
  } else {
    return;
  }

  if(typeof data.sections != 'undefined') {
    var divBefore = $('#p-lang');
    $.each( data.sections, function( sectionId, section ) {
      if(divBefore.length !== 0) {
        divBefore.before('<div id="p-' + sectionId + '" class="' + panelInfo.portletClassName + '"><h3>' + section.label + '</h3><div class="' + panelInfo.pBodyClassName + '"></div></div>');
      } else {
        $('#' + panelInfo.columnId).append('<div id="p-' + sectionId + '" class="' + panelInfo.portletClassName + '"><h3>' + section.label + '</h3><div class="' + panelInfo.pBodyClassName + '"></div></div>');
      }
      if(typeof section.data != 'undefined') {
        $('div#p-' + sectionId + ' div.' + panelInfo.pBodyClassName).append(section.data);
      } else {
        $('div#p-' + sectionId + ' div.' + panelInfo.pBodyClassName).append("<ul>");
        var list = $('div#p-' + sectionId + ' ul');
        $.each( section.links, function( linkId, link ) {
          link['class'] = (typeof link['class'] != 'undefined') ? link['class'] : '';
          list.append('<li id="n-' + linkId + '" class="' + link['class'] + '"><a href="' + link.href + '">' + link.label + '</a></li>');
        } );
        if(skin == "vector") {
          if(typeof section.expanded == 'undefined' || section.expanded) {
            $('div#p-' + sectionId).addClass('expanded');
            $('div#p-' + sectionId + ' div.' + panelInfo.pBodyClassName).show();
          } else {
            $('div#p-' + sectionId).addClass('collapsed');
          }
        }
      }
    } );
  } else if(typeof data.section != 'undefined') {
    var list = $('div#p-' + data.section + ' ul');
    $.each( data.links, function( linkId, link ) {
      link['class'] = (typeof link['class'] != 'undefined') ? link['class'] : '';
      list.append('<li id="n-' + linkId + '" class="' + link['class'] + '"><a href="' + link.href + '">' + link.label + '</a></li>');
    } );
  }
};
mw.panel = mediaWiki.panel;