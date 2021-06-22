// add some xowa specific code to move the image on Page (it:Page: == 108)
jQuery( document ).ready( function ( $ ) {
	if ( mediaWiki.config.get( 'wgNamespaceNumber' ) === 108 ) {
		$( '#xowa_pp_image' ).appendTo( $( '.prp-page-image' ) );
	}
} );

/* from gadgets */
$(function() {
	
	function highlight(claxx) {
		pages = $('#pagineDellEdizioneInner .' + claxx);
		if (pages.length) {
			pages.css('background-color', 'orange');
			offset = $(pages.get(0)).offset().top;
			screenTop = $(window).scrollTop();
			screenBottom = screenTop + $(window).innerHeight();
			if (offset < screenTop || offset > screenBottom) {
				$('html, body').animate( {scrollTop: offset }, 'fast');
			}
		}
	}
	
	// ns0
	if (mw.config.get("wgNamespaceNumber") === 0) {
		qualityNs0 = $('#textquality').attr('class');
		
		pagine75 = $('.tl-testo-quality').filter('[data-quality="75%"]').length + $('.prp-pagequality-3').length;
		pagine50 = $('.tl-testo-quality').filter('[data-quality="50%"]').length + $('.prp-pagequality-2').length;
		pagine25 = $('.tl-testo-quality').filter('[data-quality="25%"]').length + $('.prp-pagequality-1').length;
		
		avviso = '<span class="index-pages-stat-avviso">Attenzione: il testo è stato portato al ' + qualityNs0 + ', ma ci sono delle pagine non ancora al ' + qualityNs0 + '</span>';
		showAvviso = false;
		if (qualityNs0 == '100%' && (pagine75 > 0 || pagine50 > 0 || pagine25 > 0)) {
			showAvviso = true;
		}
		if (qualityNs0 == '75%' && (pagine50 > 0 || pagine25 > 0)) {
			showAvviso = true;
		}
		if (qualityNs0 == '50%' && pagine25 > 0) {
			showAvviso = true;
		}
		if (showAvviso) {
			$('.quality-msg-text').html(avviso);
		}
	}
	
	// pagine Indice
	if (mw.config.get("wgNamespaceNumber") === 110) {
		tot0 = $('.pagineDellEdizione .prp-pagequality-0').length;
		totToCreate = $('.pagineDellEdizione a.new').length;
		tot1 = $('.pagineDellEdizione .prp-pagequality-1').length + totToCreate;
		tot2 = $('.pagineDellEdizione .prp-pagequality-2').length;
		tot3 = $('.pagineDellEdizione .prp-pagequality-3').length;
		tot4 = $('.pagineDellEdizione .prp-pagequality-4').length;
		totale = tot1 + tot2 + tot3 + tot4;
		percToCreate = Math.round(totToCreate / totale * 100) || 0;
		perc1 = Math.round(tot1 / totale * 100) || 0;
		perc2 = Math.round(tot2 / totale * 100) || 0;
		perc3 = Math.round(tot3 / totale * 100) || 0;
		perc4 = Math.round(tot4 / totale * 100) || 0;
		
		declaredQuality = $('#dati').data('qualità');
		effectiveQuality = '25%';
		if (tot1 === 0) {
			effectiveQuality = '50%';
			if (tot2 === 0) {
				effectiveQuality = '75%';
				if (tot3 === 0) {
					effectiveQuality = '100%';
				}
			}
		}
		
		$('.pagineDellEdizione').append(
			$('<div id="index-pages-stat"></div>')
				.append($('<table></table>')
					.append($('<tr></tr>')
						.append('<td><a class="25" href="#">Da trascrivere:</a></td>')
						.append('<td><span class="quality1">' + tot1 + '</span></td>')
						.append('<td>' + perc1 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td>&nbsp; di cui <a class="0" href="#">da creare:</a></td>')
						.append('<td><span class="new">' + totToCreate + '</span></td>')
						.append('<td>' + percToCreate + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a class="50" href="#">Da controllare:</a></td>')
						.append('<td><span class="quality2">' + tot2 + '</span></td>')
						.append('<td>' + perc2 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a class="75" href="#">Da rileggere:</a></td>')
						.append('<td><span class="quality3">' + tot3 + '</span></td>')
						.append('<td>' + perc3 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a class="100" href="#">Rilette:</a></td>')
						.append('<td><span class="quality4">' + tot4 + '</span></td>')
						.append('<td>' + perc4 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a href="#">Vuote:</a></td>')
						.append('<td><span class="quality0">' + tot0 + '</span></td>')
						.append('<td>-</td>')
					)
				)
		);
		
		if (declaredQuality !== undefined && declaredQuality != effectiveQuality) {
			$('.quality-msg-text').html('<span class="index-pages-stat-avviso">Attenzione: l\'indice è al ' 
				+ declaredQuality + ', ma andrebbe portato al ' + effectiveQuality + '</span> ' 
				+ '(<a id="highlightLink" class="' + effectiveQuality.replace('%', '') + '" href="#">vedi le pagine al ' + effectiveQuality + '</a>)');
			
			$('#highlightLink').click(function(e) {
				e.preventDefault();
				c = $(this).attr('class');
				$('#index-pages-stat a.' + c).click();
			});
		}
		
		$('#index-pages-stat a').click(function(e) {
			e.preventDefault();
			claxx = $(this).parent().parent().find('span').attr('class');
			$('#pagineDellEdizioneInner a').css('background-color', '');
			highlight(claxx);
			if (claxx == 'quality1')
				highlight('new');
		});
	}
});
