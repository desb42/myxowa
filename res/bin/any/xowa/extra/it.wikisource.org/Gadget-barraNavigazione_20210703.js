/* se nella pagina è presente sotto l'intestazione la barra di navigazione,
 * la duplica e mostra il duplicato a fondo pagina (con in più un link per tornare in cima)
 */

function creaBoxEdizione() {
	// crea box "edizione" clonando quello dell'intestazione
	box = $('#box_intestazione').clone().attr('id', 'box_edizione').empty();
	
	if ($('#barra_navigazione_sotto').length > 0) {
		box.insertAfter($('#barra_navigazione_sotto'));
	} else {
		box.appendTo($('#box_esterno'));
		$('#box_edizione').css('margin-top', '3em');	
	}
	return box;
}
 
$(document).ready(function () {
	if (mw.config.get("wgNamespaceNumber") === 0 && $('#barra_navigazione_sotto').length === 0) {
		$("table#barra_navigazione").clone().attr('id', 'barra_navigazione_sotto').appendTo($("table#barra_navigazione").parent());
		$('#box_esterno').prepend('<a href="top"></a>');
		$('#barra_navigazione_sotto td:nth-child(2)').after(
			'<td style="padding:0"><a href="#top" style="color:#808080" title="Torna in cima">&#x25B2;</a></td>');
		
		// se il testo e' proofread, carica le informazioni sull'edizione dalla pagina Indice
		if ($('#ws-scan').length > 0) {
			
			//urlPagina = "/w/index.php?&title=" + 'Indice:' + $('#ws-scan').text();
			urlPagina = xowa_global_values.wgPopupsRestGatewayEndpoint + 'Indice:' + $('#ws-scan').text();
			$.ajax({
				url: urlPagina
			}).done(function(response) {
				d = $('#dati', response);
				box = creaBoxEdizione();
				box.append('<b><a href="' + urlPagina + '">Edizione</a></b>: ');

				if (d.data('autore').length > 0) {
					box.append(d.data('autore').replace(/\//g, ', ') + '. ');
				}
				
				titolo = d.data('nomepagina');
				if (d.data('titolooriginale').length > 0) {
					titolo = d.data('titolooriginale').replace(/\[\[(.*\|)?/g, '').replace(/\]\]/g, '');
				} else if (d.data('titolo').length > 0) {
					titolo = d.data('titolo');
				}
				box.append('<i>' + titolo + '</i>');

				sottotitolo = $('#dati-sottotitolo', response);
				if (sottotitolo.length > 0) {
					box.append(', <i>' + sottotitolo.html() + '</i>. ');
				} else box.append('. ');
				
				if (d.data('curatore').length > 0) {
					box.append('A cura di ' + d.data('curatore').replace(/\//g, ', ') + '. ');
				}

				if (d.data('citta').length > 0) {
					box.append(d.data('citta') + ', ');
				}
				if (d.data('editore').length > 0) {
					box.append(d.data('editore') + ', ');
				}
				if (d.data('anno')) {
					box.append(d.data('anno') + '.');
				} else {
					box.append('senza data.');
				}
				
				fonte = $('#dati-fonte', response);
				if (fonte.length > 0) {
					box.append(' Fonte: ' + fonte.html());
				}
			});
		} else {
			pagename = mw.config.get('wgPageName');
			if (pagename.indexOf('/') != -1)
				pagename = pagename.substring(0, pagename.indexOf('/'));
			talkpage = 'Discussione:' + pagename;
			//urlPagina = "/w/index.php?title=" + talkpage;
			urlPagina = xowa_global_values.wgPopupsRestGatewayEndpoint + talkpage;
			$.ajax({
				url: urlPagina
			}).done(function(response) {
				edizione = $('#dati-edizione', response);
				fonte = $('#dati-fonte', response);
				
				box = creaBoxEdizione();
				box.append('<b><a href="' + urlPagina + '">Edizione</a></b>: ');
				if (edizione.length > 0) {
					box.append(edizione.html());
				} else {
					box.append('<i>non disponibile</i>');
				}
			});
		}
	}
});