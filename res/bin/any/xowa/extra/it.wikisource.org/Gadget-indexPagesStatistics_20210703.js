$(function() {
	
	function highlight(claxx, clean) {
		clean = typeof clean !== 'undefined' ? clean : true;
		if (clean)
			$('#pagineDellEdizioneInner a').css('background-color', '');
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
		if (claxx == 'quality1')
			highlight('new', false);
	}
	
	// controlli in ns0 che la qualità corrisponda
	if (mw.config.get("wgNamespaceNumber") === 0) {
		qualityNs0 = $('#textquality').attr('class');
		
		pagine75 = $('.tl-testo-quality.tl-testo-subpage').filter('[data-quality="75%"]').add('.prp-pagequality-3');
		pagine50 = $('.tl-testo-quality.tl-testo-subpage').filter('[data-quality="50%"]').add('.prp-pagequality-2');
		pagine25 = $('.tl-testo-quality.tl-testo-subpage').filter('[data-quality="25%"]').add('.prp-pagequality-1');
		
		showAvviso = false;
		showAvvisoOn = $();
		if (qualityNs0 == '100%' && (pagine75.length > 0 || pagine50.length > 0 || pagine25.length > 0)) {
			showAvviso = true;
			showAvvisoOn = showAvvisoOn.add(pagine75).add(pagine50).add(pagine25);
		}
		if (qualityNs0 == '75%' && (pagine50.length > 0 || pagine25.length > 0)) {
			showAvviso = true;
			showAvvisoOn = showAvvisoOn.add(pagine50).add(pagine25);
		}
		if (qualityNs0 == '50%' && pagine25.length > 0) {
			showAvviso = true;
			showAvvisoOn = showAvvisoOn.add(pagine25);
		}
		if (showAvviso) {
			showAvvisoOn.first().parent().attr('id', 'linkAvvisoSAL');
			avviso = '<span class="index-pages-stat-avviso">Attenzione: il testo è stato portato al ' + qualityNs0 
				+ ', ma ci sono delle pagine non ancora al ' + qualityNs0 + ' (<a href="#linkAvvisoSAL">vedi</a>)</span>';
			$('.quality-msg-text').html(avviso);
			showAvvisoOn.parent().css('border', '2px solid red').attr('title', 'Pagina non ancora portata al '+ qualityNs0);
			showAvvisoOn.parent().find('a').attr('title', 'Pagina non ancora portata al '+ qualityNs0);
		}
	}
	
	// statistiche nelle pagine Indice e controllo che la qualità corrisponda
	if (mw.config.get("wgNamespaceNumber") === 110 && mw.config.get('wgPageName').indexOf('/') == -1) {
		if ($('.prp-index-pagelist .error').length > 0) {
			return;
		}
		
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
		
		declaredQuality = $('#dati').data('qualita');
		effectiveQuality = '25%';
		if (tot1 === 0) {
			effectiveQuality = '50%';
			if (tot2 === 0) {
				effectiveQuality = '75%';
				if (tot3 === 0) {
					effectiveQuality = '100%';
					if (tot4 === 0) {
						effectiveQuality = '25%';
						if (totale === 0) {
							// ci sono solo pagine allo 0%
							effectiveQuality = '100%';
						}
					}
				}
			}
		}
		
		$('.pagineDellEdizione').append(
			$('<div id="index-pages-stat"></div>')
				.append($('<table></table>')
					.append($('<tr></tr>')
						.append('<td><a class="25" href="#" title="Pagine al 25% o non ancora create">Da trascrivere:</a></td>')
						.append('<td><span class="quality1">' + tot1 + '</span></td>')
						.append('<td>' + perc1 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td>&nbsp; di cui <a class="0" href="#" title="Pagine non ancora create">da creare:</a></td>')
						.append('<td><span class="new">' + totToCreate + '</span></td>')
						.append('<td>' + percToCreate + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a class="50" href="#" title="Pagine al 50%">Da controllare:</a></td>')
						.append('<td><span class="quality2">' + tot2 + '</span></td>')
						.append('<td>' + perc2 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a class="75" href="#" title="Pagine al 75%">Da rileggere:</a></td>')
						.append('<td><span class="quality3">' + tot3 + '</span></td>')
						.append('<td>' + perc3 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a class="100" href="#" title="Pagine al 100%">Rilette:</a></td>')
						.append('<td><span class="quality4">' + tot4 + '</span></td>')
						.append('<td>' + perc4 + '% </td>')
					)
					.append($('<tr></tr>')
						.append('<td><a href="#" title="Pagine allo 0%">Vuote:</a></td>')
						.append('<td><span class="quality0">' + tot0 + '</span></td>')
						.append('<td>-</td>')
					)
					.append($('<tr></tr>')
						.append('<td>Totale:</td>')
						.append('<td><span class="total">' + (totale + tot0) + '</span></td>')
						.append('<td>-</td>')
					)
					.append($('<tr></tr>')
						.append('<td><a href="#" title="Pagine al 75% o 100% non trascluse">Non trascluse:</a></td>')
						.append('<td><span class="notTranscluded">n.d.</span></td>')
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
			highlight(claxx);
		});
		
		// controlla pagine al 75% o 100% non trascluse
		mw.loader.using("mediawiki.api", function() {
			var api = new mw.Api();
			$('.quality-msg-text').append(' <span id="notTranscludedWarning"></span>');
			var count = 0;
			$('.prp-index-pagelist .prp-pagequality-3, .prp-index-pagelist .prp-pagequality-4').each(function() {
				var thisPageLink = $(this);
				var pageName = thisPageLink.attr('title');
				//console.log('check ' + pageName);
				api.get({
					action: 'query', 
					format: 'json', 
					list: 'embeddedin', 
					einamespace: '0', 
					eititle: pageName
				}).done(function (data) {
					var linkedNs0 = data.query.embeddedin;
					if (linkedNs0.length == 0) {
						count++;
						$(thisPageLink).addClass('notTranscluded');
						$('#notTranscludedWarning').html('Ci sono ' + count + ' pagine trascritte o rilette, ma non trascluse (<a id="showNotTranscluded" href="#">vedi</a> - <a href="/wiki/Aiuto:Transclusione_proofread">guida</a>)');
						$('#showNotTranscluded').click(function(e) {
							e.preventDefault();
							highlight('notTranscluded');
						});
						if (count >= 10) {
							$('#notTranscludedWarning').css('color', 'red');
						}
						//console.log('found ' + pageName);
					}
					$('#index-pages-stat .notTranscluded').html(count);
				});
			});
		});
	}
	
	// barra di avanzamento nelle categorie indice
	pagename = mw.config.get('wgPageName');
	if (pagename.startsWith('Categoria:Pagine_indice_SAL_') || pagename == 'Categoria:Sommari' 
		|| pagename == 'Categoria:Indici_non_trasclusi_in_ns0' || pagename == 'Categoria:Indici_senza_sommario') {
		$.ajax({
			url: "/w/index.php?&title=Speciale:PagineIndice&limit=5000&offset=0"
		}).done(function(response) {
			console.log("Speciale:PagineIndice caricato");
			content = $('.mw-spcontent ol.special', response);
			content.children().each(function(index) {
				titolo = $(this).find('a').attr('title');
				// console.log(titolo);
				quality = $(this).find('.pr_quality');
				quality.addClass('quality-bar');
				quality.find('td').filter(function() {
    				return $(this).css('width') == '0px';
				}).remove();
				
				$('.mw-category a').filter(function() {
    				return $(this).attr('title') == titolo;
				}).parent().append(quality);
			});
		});
	}
	
	// icona SAL dell'indice in tutte le categorie
	if (mw.config.get("wgCanonicalNamespace") == 'Category') {
		$('#mw-pages a').each(function(i) { 
			var title = $(this).attr('href').replace('/wiki/', '');
			if (title.startsWith('Indice')) {
				$.ajax({
					url: "/w/index.php?&title=" + title
				}).done(function(response) {
					icon = $('.quality-msg-image img', response);
					icon.attr('width', '9');
					icon.attr('height', '9');
					icon.css('margin-left', '3px');
					$('#mw-pages [href="/wiki/' + title + '"]').after(icon);
				});
			}
		});
	}
	
	if (pagename.startsWith('Wikisource:Rilettura_del_mese/')) {
		$('#mw-content-text li a').each(function(i) { 
			var title = $(this).attr('href').replace('/wiki/', '');
			if (title.startsWith('Indice')) {
				$.ajax({
					url: "/w/index.php?&title=" + title
				}).done(function(response) {
					icon = $('.quality-msg-image img', response);
					icon.attr('width', '9');
					icon.attr('height', '9');
					icon.css('margin-left', '3px');
					$('#mw-content-text li [href="/wiki/' + title + '"]').after(icon);
				});
			}
		});
	}
});