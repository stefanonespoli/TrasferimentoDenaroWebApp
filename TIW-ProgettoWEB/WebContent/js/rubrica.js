(function() {

	document.getElementById("codiceContoDest").addEventListener("input", (e) =>{
		var value = e.target.value;  //sto value qua è l id del contoDest scritto nel form dall user
		//o facevi la label document.getelembyId.register event (onkeyup) -->
		//qua fare la richiesta get ajax a EstraiSuggRubrica passandogli value e otterrai UN SOLO intero (1 versione del suggerimento)
		// e non serve l array e mostrerai sto intero senza ciclo create element option e tutte le istruzioni che vedi giu sotto

		var self = this;
		makeCall("GET", "EstraiSuggerimentoRubrica?valoreIndicato=" + value + "&criterioRicerca=" + 1, null,    
				function(req) {										
			if (req.readyState == 4) {
				var message = req.responseText;
				if (req.status == 200) {
					if(req.responseText != ""){
						var idUtenTrovato = JSON.parse(req.responseText);

						document.getElementById('datalistIdUtente').innerHTML = ''; 

						var node = document.createElement("option"); 
						var val = document.createTextNode(idUtenTrovato); 
						node.appendChild(val); 
						document.getElementById("datalistIdUtente").appendChild(node);

						self.alert.textContent="";
					}else
					{
						document.getElementById('datalistIdUtente').innerHTML = '';
						document.getElementById("datalistIdUtente").appendChild(document.createElement("option"));	//resetto la lista sostituendone con una vuota

					}
				}
			} else {
				self.alert.textContent = message;
			}
		}
		);

	}); 


	//sto value qua è l id del contoDest scritto nel form dall user
	//o facevi la label document.getelembyId.register event (onkeyup) -->
	//qua fare la richiesta get ajax a EstraiSuggRubrica passandogli value e otterrai UN SOLO intero (1 versione del suggerimento)
	// e non serve l array e mostrerai sto intero senza ciclo create element option e tutte le istruzioni che vedi giu sotto

	document.getElementById("idUtente").addEventListener("input", (e) =>{
		var value = e.target.value;

		var self = this;
		makeCall("GET", "EstraiSuggerimentoRubrica?valoreIndicato=" + value + "&criterioRicerca=" + 0, null,    
				function(req) {										
			if (req.readyState == 4) {
				var message = req.responseText;
				if (req.status == 200) {
					if(req.responseText != ""){
						var codiciContoTrovati = JSON.parse(req.responseText);	//dati dal server

						document.getElementById('datalistCodiciConto').innerHTML = '';
						var index = 0;

						do{
							var node = document.createElement("option"); 
							var val = document.createTextNode(codiciContoTrovati[index]); 
							node.appendChild(val); 
							document.getElementById("datalistCodiciConto").appendChild(node);

							index = index + 1;
						}while(index < codiciContoTrovati.length)

							self.alert.textContent="";
					}else
					{
						document.getElementById('datalistCodiciConto').innerHTML = '';
						document.getElementById("datalistCodiciConto").appendChild(document.createElement("option"));	//resettiamo la lista sostituendone con una vuota
					}
				}
			} else {
				self.alert.textContent = message;
			}
		}
		);

	});
})();