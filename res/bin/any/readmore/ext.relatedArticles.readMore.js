mw.loader.implement("ext.relatedArticles.readMore@v1o9p",function($,jQuery,require,module){(function(){mw.loader.using('ext.relatedArticles.cards').done(function(){var CardModel=mw.cards.CardModel,CardView=mw.cards.CardView,CardListView=mw.cards.CardListView;function getCards(pages){return pages.map(function(page){var result={title:page.title,url:mw.util.getUrl(page.title),hasThumbnail:!1,extract:(page.description||page.extract||(page.pageprops?page.pageprops.description:''))};if(page.thumbnail){result.hasThumbnail=!0;result.thumbnailUrl=page.thumbnail.source;result.isThumbnailPortrait=page.thumbnail.height>=page.thumbnail.width;}return new CardView(new CardModel(result));});}mw.trackSubscribe('ext.relatedArticles.init',function(_,pages){var $readMore,cards;cards=new CardListView(getCards(pages));$readMore=$('<aside>').addClass('ra-read-more noprint').append($('<h2></h2>').text(mw.msg('relatedarticles-read-more-heading'))).append(cards.$el);$('.read-more-container').append(
$readMore);});});}());},{"css":[".ra-read-more h2{color:#54595d;border-bottom:0;padding-bottom:0.5em;font-size:0.8em;font-weight:normal;letter-spacing:1px;text-transform:uppercase}.ra-read-more{padding:1em 0 0}.ve-activated .ra-read-more{display:none}.ra-read-more .ext-related-articles-card-list{margin-left:0}.ext-related-articles-card-list{margin-right:1.5em}"]},{"relatedarticles-read-more-heading":"Related pages"});