xowa_implement("ext.toctree@aj2zd", function($, jQuery, require, module) {
    (function() {
        function processClickEvent() {
            var $ul = $(this).parent().parent().children('ul');
            $ul.toggle();
            if ($ul.css('display') !== 'none') {
                $(this).text('-').attr('title', mw.msg('hidetoc'));
            } else {
                $(this).text('+').attr('title', mw.msg('showtoc'));
            }
        }
        function init($content) {
            var $toc = $content.find('.toc').addBack('.toc')
              , $mainList = $toc.children('ul').children('li.toclevel-1');
//            if (mw.user.options.get('toc-floated')) {
//                $toc.addClass('tocFloat');
//            }
            $mainList.each(function() {
                var $subList, $toggleSymbol, $toggleSpan;
                $(this).css('position', 'relative');
                $subList = $(this).children('ul');
                if ($subList.length > 0) {
                    $(this).parent().addClass('tocUl');
                    $toggleSymbol = $('<span>').addClass('toggleSymbol');
//                    if (mw.user.options.get('toc-expand')) {
//                        $toggleSymbol.text('-').attr('title', mw.msg('hidetoc'));
//                        $subList.css('display', '');
//                    } else {
                        $toggleSymbol.text('+').attr('title', mw.msg('showtoc'));
                        $subList.css('display', 'none');
//                    }
                    $toggleSymbol.on('click', processClickEvent);
                    $toggleSpan = $('<span>').addClass('toggleNode');
                    $toggleSpan.append('[', $toggleSymbol, ']');
                    $(this).prepend($toggleSpan);
                }
            });
        }
        mw.hook('wikipage.content').add(init);
    }());
}, {
    "css": [".toc .tocUl{padding-left:2em} .toc.tocFloat{float:left;margin:0 2em 1em 0;width:20em;max-width:20em}.noFloat .toc.tocFloat{float:none;margin:0;width:auto;max-width:auto}.toggleSymbol{color:#00f;cursor:pointer}.toggleNode{position:absolute;top:0;left:-2em}@media print{.toc.tocFloat{background:#fff}.toggleNode{display:none}.toc .tocUl{padding-left:0}}"]
}, {
    "hidetoc": "Verbergen",
    "showtoc": "Anzeigen"
});
