xowa_implement("ext.relatedArticles.cards@9hpbl",function($,jQuery,require,module){(function(){'use strict';mw.cards={models:{},views:{}};}());(function(){'use strict';function CardModel(attributes){CardModel.super.apply(this,arguments);this.attributes=attributes;}OO.inheritClass(CardModel,OO.EventEmitter);CardModel.prototype.set=function(key,value,silent){var event={};this.attributes[key]=value;if(!silent){event[key]=value;this.emit('change',event);}};CardModel.prototype.get=function(key){return this.attributes[key];};mw.cards.CardModel=CardModel;}());(function(){'use strict';function CardView(model){this.model=model;this.model.on('change',this.render.bind(this));this.$el=$(this._render());}OO.initClass(CardView);CardView.prototype.render=function(){this.$el.replaceWith(this._render());};CardView.prototype._render=function(){var $listItem=$('<li>'),attributes=$.extend({},this.model.attributes);attributes.thumbnailUrl=CSS.escape(attributes.thumbnailUrl);$listItem.attr({title:
attributes.title,class:'ext-related-articles-card'});$listItem.append($('<div>').addClass('ext-related-articles-card-thumb').addClass(attributes.hasThumbnail?'':'ext-related-articles-card-thumb-placeholder').css('background-image',attributes.hasThumbnail?'url('+attributes.thumbnailUrl+')':null),$('<a>').attr({href:attributes.url,'aria-hidden':'true',tabindex:-1}),$('<div>').attr({class:'ext-related-articles-card-detail'}).append($('<h3>').append($('<a>').attr({href:attributes.url}).text(attributes.title)),$('<p>').attr({class:'ext-related-articles-card-extract'}).text(attributes.extract)));return $listItem;};mw.cards.CardView=CardView;}());(function(){'use strict';function CardListView(cardViews){var self=this;this.cardViews=cardViews||[];this.$el=$('<ul>').attr({class:'ext-related-articles-card-list'});this.cardViews.forEach(function(cardView){self.$el.append(cardView.$el);});}OO.initClass(CardListView);mw.cards.CardListView=CardListView;}());},{"css":[
".ext-related-articles-card-list{display:-webkit-flex;display:-moz-flex;display:-ms-flexbox;display:flex;flex-flow:row wrap;font-size:1em;list-style:none;overflow:hidden;position:relative;padding-left:0}.ext-related-articles-card-list h3{font-family:inherit;font-size:1em;max-height:2.6em;line-height:1.3;margin:0;overflow:hidden;padding:0;position:relative;font-weight:500}.ext-related-articles-card-list h3 a{color:#000}.ext-related-articles-card-list h3:after{content:' ';position:absolute;right:0;bottom:0;width:25%;height:1.3em;background-color:transparent;background-image:-webkit-linear-gradient(right,rgba(255,255,255,0),#ffffff 50%);background-image:-moz-linear-gradient(right,rgba(255,255,255,0),#ffffff 50%);background-image:linear-gradient(to right,rgba(255,255,255,0),#ffffff 50%)}.ext-related-articles-card-list .ext-related-articles-card{background-color:#fff;box-sizing:border-box;margin:0;height:80px;position:relative;width:100%;border:1px solid rgba(0,0,0,0.2)}.ext-related-articles-card-list .ext-related-articles-card + .ext-related-articles-card{border-top:0}.ext-related-articles-card-list .ext-related-articles-card:first-child{border-radius:2px 2px 0 0}.ext-related-articles-card-list .ext-related-articles-card:last-child{border-radius:0 0 2px 2px}.ext-related-articles-card-list .ext-related-articles-card \u003E a{position:absolute;top:0;right:0;bottom:0;left:0;z-index:1}.ext-related-articles-card-list .ext-related-articles-card \u003E a:hover{box-shadow:0 1px 1px rgba(0,0,0,0.1)}.ext-related-articles-card-list .ext-related-articles-card-detail{position:relative;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.ext-related-articles-card-list .ext-related-articles-card-extract{color:#72777d;font-size:0.8em;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;margin-top:2px}.ext-related-articles-card-list .ext-related-articles-card-thumb{background-color:#eaecf0;background-repeat:no-repeat;background-position:center center;background-size:cover;float:left;height:100%;width:80px;margin-right:10px}.ext-related-articles-card-list .ext-related-articles-card-thumb-placeholder{background-image:url(/xowa/fsys/bin/any/readmore/article.png);background-image:linear-gradient(transparent,transparent),url(/xowa/fsys/bin/any/readmore/article.svg);background-size:40px 40px}@media all and (min-width:720px){.ext-related-articles-card-list{border-top:0}.ext-related-articles-card-list .ext-related-articles-card{border:1px solid rgba(0,0,0,0.2);margin-right:1%;margin-bottom:10px;width:32.66666667%}.ext-related-articles-card-list .ext-related-articles-card,.ext-related-articles-card-list .ext-related-articles-card:first-child,.ext-related-articles-card-list .ext-related-articles-card:last-child{border-radius:2px}.ext-related-articles-card-list .ext-related-articles-card:last-child{margin-right:0}.ext-related-articles-card-list .ext-related-articles-card + .ext-related-articles-card{border:1px solid rgba(0,0,0,0.2)}.ext-related-articles-card-list .ext-related-articles-card:nth-child(3n+3){margin-right:0}}"
]});
xowa_implement("ext.relatedArticles.lib@1lq2u",function($,jQuery,require,module){;(function(root){if(!root.CSS){root.CSS={};}var CSS=root.CSS;var InvalidCharacterError=function(message){this.message=message;};InvalidCharacterError.prototype=new Error;InvalidCharacterError.prototype.name='InvalidCharacterError';if(!CSS.escape){CSS.escape=function(value){var string=String(value);var length=string.length;var index=-1;var codeUnit;var result='';var firstCodeUnit=string.charCodeAt(0);while(++index<length){codeUnit=string.charCodeAt(index);if(codeUnit==0x0000){throw new InvalidCharacterError('Invalid character: the input contains U+0000.');}if((codeUnit>=0x0001&&codeUnit<=0x001F)||codeUnit==0x007F||(index==0&&codeUnit>=0x0030&&codeUnit<=0x0039)||(index==1&&codeUnit>=0x0030&&codeUnit<=0x0039&&firstCodeUnit==0x002D)){result+='\\'+codeUnit.toString(16)+' ';continue;}if(index==0&&length==1&&codeUnit==0x002D){result+='\\'+string.charAt(index);continue;}if(codeUnit>=0x0080||codeUnit==0x002D||
codeUnit==0x005F||codeUnit>=0x0030&&codeUnit<=0x0039||codeUnit>=0x0041&&codeUnit<=0x005A||codeUnit>=0x0061&&codeUnit<=0x007A){result+=string.charAt(index);continue;}result+='\\'+string.charAt(index);}return result;};}}(typeof global!='undefined'?global:this));});
xowa_implement("ext.relatedArticles.readMore.gateway@vbs1t",function($,jQuery,require,module){(function(){mw.relatedPages={};function RelatedPagesGateway(api,currentPage,editorCuratedPages,useCirrusSearch,onlyUseCirrusSearch,descriptionSource){this.api=api;this.currentPage=currentPage;this.useCirrusSearch=useCirrusSearch;this.descriptionSource=descriptionSource;if(onlyUseCirrusSearch){editorCuratedPages=[];}this.editorCuratedPages=editorCuratedPages||[];}OO.initClass(RelatedPagesGateway);function getPages(result){return result&&result.query&&result.query.pages?result.query.pages:[];}RelatedPagesGateway.prototype.getForCurrentPage=function(limit){var parameters={action:'query',formatversion:2,prop:'pageimages',piprop:'thumbnail',pithumbsize:160},relatedPages=this.editorCuratedPages.slice(0,limit);switch(this.descriptionSource){case'wikidata':parameters.prop+='|description';break;case'textextracts':parameters.prop+='|extracts';parameters.exsentences='1';parameters.exintro='1';
parameters.explaintext='1';break;case'pagedescription':parameters.prop+='|pageprops';parameters.ppprop='description';break;}if(relatedPages.length){parameters.pilimit=relatedPages.length;parameters.continue='';parameters.titles=relatedPages;}else if(this.useCirrusSearch){parameters.pilimit=limit;parameters.generator='search';parameters.gsrsearch='morelike:'+this.currentPage;parameters.gsrnamespace='0';parameters.gsrlimit=limit;parameters.gsrqiprofile='classic_noboostlinks';parameters.uselang='content';parameters.smaxage=86400;parameters.maxage=86400;}else{return $.Deferred().resolve([]);}return this.api.get(parameters).then(getPages);};mw.relatedPages.RelatedPagesGateway=RelatedPagesGateway;}());});
xowa_implement("ext.relatedArticles.readMore@v1o9p",function($,jQuery,require,module){(function(){/*mw.loader.using('ext.relatedArticles.cards').done(function(){*/var CardModel=mw.cards.CardModel,CardView=mw.cards.CardView,CardListView=mw.cards.CardListView;function getCards(pages){return pages.map(function(page){var result={title:page.title,url:mw.util.getUrl(page.title),hasThumbnail:!1,extract:(page.description||page.extract||(page.pageprops?page.pageprops.description:''))};if(page.thumbnail){result.hasThumbnail=!0;result.thumbnailUrl=page.thumbnail.source;result.isThumbnailPortrait=page.thumbnail.height>=page.thumbnail.width;}return new CardView(new CardModel(result));});}
//mw.trackSubscribe('ext.relatedArticles.init',function(_,pages)
mw.cards.ext_relatedArticles_init = function(pages)
{var $readMore,cards;cards=new CardListView(getCards(pages));$readMore=$('<aside>').addClass('ra-read-more noprint').append($('<h2></h2>').text(mw.msg('relatedarticles-read-more-heading'))).append(cards.$el);$('.read-more-container').append(
$readMore);}
/*);*/
/*});*/}());},{"css":[".ra-read-more h2{color:#54595d;border-bottom:0;padding-bottom:0.5em;font-size:0.8em;font-weight:normal;letter-spacing:1px;text-transform:uppercase}.ra-read-more{padding:1em 0 0}.ve-activated .ra-read-more{display:none}.ra-read-more .ext-related-articles-card-list{margin-left:0}.ext-related-articles-card-list{margin-right:1.5em}"]},{"relatedarticles-read-more-heading":"Related pages"});
/* */
/* eslint-disable no-jquery/no-global-selector */
( function () {

	var data = {"useCirrusSearch":!1,"onlyUseCirrusSearch":!1,"descriptionSource":"wikidata"},
		relatedPages = new mw.relatedPages.RelatedPagesGateway(
			new mw.Api(),
			mw.config.get( 'wgPageName' ),
			mw.config.get( 'wgRelatedArticles' ),
			data.useCirrusSearch,
			data.onlyUseCirrusSearch,
			data.descriptionSource
		),
		// Make sure this is never undefined as I'm paranoid
		//LIMIT = mw.config.get( 'wgRelatedArticlesCardLimit', 3 ),
		LIMIT = 9,
		debouncedLoad = $.debounce( 100, function () {
			loadRelatedArticles(); // eslint-disable-line no-use-before-define
		} ),
		$window = $( window );

	/**
	 * Load related articles when the user scrolls past half of the window height.
	 *
	 * @ignore
	 */
	function loadRelatedArticles() {
		var readMore = $( '.read-more-container' ).get( 0 ),
			scrollThreshold = $window.height() * 2;

//			pages = relatedPages.getForCurrentPage( LIMIT );
//			mw.cards.ext_relatedArticles_init( pages );
//		if ( mw.viewport.isElementCloseToViewport( readMore, scrollThreshold ) ) {
			$.when(
				// Note we load dependencies here rather than ResourceLoader
				// to avoid PHP exceptions when Cards not installed
				// which should never happen given the if statement.
//				mw.loader.using( [
//					'ext.relatedArticles.cards',
//					'ext.relatedArticles.readMore'
//				] ),
				relatedPages.getForCurrentPage( LIMIT )
			).then( function ( pages ) {
				//pages = relatedPages.getPages();
				if ( pages.length ) {
					//mw.track( 'ext.relatedArticles.init', pages );
					mw.cards.ext_relatedArticles_init( pages );
				} else {
					$( readMore ).remove();
				}
			} );
			// detach handler to stop subsequent loads on scroll
			$window.off( 'scroll', debouncedLoad );
//		}
	}

	function showReadMore() {
		// try related articles load on scroll
		$window.on( 'scroll', debouncedLoad );
		// try an initial load, in case of no scroll
		loadRelatedArticles();
	}

	$( showReadMore );
}() );
