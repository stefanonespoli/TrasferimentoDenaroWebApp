(function() {     

	// page components
	var dettagliConto , listaConti, creaConto,
	pageOrchestrator = new PageOrchestrator(); // main controller lato client

	window.addEventListener("load", () => {
		if (sessionStorage.getItem("username") == null) {
			window.location.href = "index.html";
		} else {
			pageOrchestrator.start(); // inizializza
			pageOrchestrator.refresh();
		} // mostra contenuto inziale
	}, false);


	// Costruttore view component

	function PersonalMessage(_username, messagecontainer) {
		this.username = _username;
		this.show = function() {
			messagecontainer.textContent = this.username;
		}
	}
	//listaConti
	function listaConti(_alert, _listcontainer, _listcontainerbody) {   //TODO VA CHE FUNZIONA PER QUESTO TUTTO IL JS, il trattino basso!!! metterlo anche in dettagli conto()
		this.alert = _alert;
		this.listcontainer = _listcontainer;
		this.listcontainerbody = _listcontainerbody;

		this.reset = function() {
			this.listcontainer.style.visibility = "hidden";
		}

		this.show = function(next) {
			var self = this;
			makeCall("GET", "VaiAllaHome", null,    
					function(req) {										
				if (req.readyState == 4) {
					var message = req.responseText;
					if (req.status == 200) {
						var contiTrovati = JSON.parse(req.responseText);
						if (contiTrovati.length == 0) {
							self.alert.textContent = "Nessun conto per questo utente";
							return;
						}
						self.alert.textContent="";
						self.update(contiTrovati); 
						if (next) next(); // mostra default element della lista se presente
					}
				} else {
					self.alert.textContent = message;
				}
			}
			);
		};

		//arrayConti
		this.update = function(arrayConti) {
			var elem, i, row, codicecell, nomecell, linkcell, saldocell, anchor;
			this.listcontainerbody.innerHTML = ""; //svuoto il  table body
			// costruisco lista aggiornata
			var self = this;						
			arrayConti.forEach(function(conto) { // self visible here, not this
				row = document.createElement("tr");

				codicecell = document.createElement("td");
				codicecell.textContent = conto.codice;
				row.appendChild(codicecell);

				nomecell = document.createElement("td");
				nomecell.textContent = conto.nome;    //TUTTI GLI attributi del beans conto
				row.appendChild(nomecell);

				saldocell = document.createElement("td");
				saldocell.textContent = conto.saldo;
				row.appendChild(saldocell);

				linkcell = document.createElement("td");
				anchor = document.createElement("a");
				linkcell.appendChild(anchor);
				linkText = document.createTextNode("Vedi");
				anchor.appendChild(linkText);
				//anchor.contoId = conto.id; 
				anchor.setAttribute('contoId', conto.codice); // set a custom HTML attribute
				anchor.addEventListener("click", (e) => {
					// dependency via module parameter
					dettagliConto.show(e.target.getAttribute("contoId"));  
				}, false);									//codice conto
				anchor.href = "#";
				row.appendChild(linkcell);
				self.listcontainerbody.appendChild(row);
			});
			this.listcontainer.style.visibility = "visible";

		}

		this.autoclick = function(contoId) {  //dipende se vogliamo farla anche noi l autoclick iniziale o No
			var e = new Event("click");							// sost tutto con contoId
			var selector = "a[contoId='" + contoId + "']";
			var anchorToClick =
				(contoId) ? document.querySelector(selector) : this.listcontainerbody.querySelectorAll("a")[0];
				if (anchorToClick) 
					anchorToClick.dispatchEvent(e);		 //query selector all cercarlo su internet
		}

	}


	//TODO forse qua sotto in parentesi al posto di options devi mettere le variabili
	//come in function listaConti (....) queste var qua <--   ...nei puntini

	
	function dettagliConto(options) {  //DettagliConto ovvero lista dei trasferimenti di quel conto
		this.alert = options['alert'];
		this.detailcontainer = options['detailcontainer'];   //fare questi assegnamenti perche le altre sottofunzioni che trovi di seguito non
		this.detailcontainerbody = options['detailcontainerbody'];   //vedono options{datailcontainer ecc e ste robe qua ma se te le salvi in this.detailcont così allora si e a posto
		this.transferform = options['transferform'];  //ORDINA tRANSF FORM



		this.registerEvents = function(orchestrator) {
			this.transferform.querySelector("input[type='button']").addEventListener('click', (e) => {
				var form = e.target.closest("form");
				if (form.checkValidity()) {
					var self = this;

					//contoToReport, cioe per quale conto fare il trasferimento (contoOrigine)
					contoToReport = form.querySelector("input[type = 'hidden']").value;

					var salvatoIdUtenDest= self.transferform.idUtente.value;
					var salvatoCodContoDest= self.transferform.codiceContoDest.value;

					makeCall("POST", 'OrdinaTrasferimento', form, //TODO prova a metter i doppi apicetti
							function(req1) {
						if (req1.readyState == 4) {
							var message = req1.responseText;
							if (req1.status == 200) {
								//qua makecallPost al CreaSuggerimentoRubrica perche è qua che il sugg
								//e' anadato a buon fine oppure setti a true var booleana che usi da un altra parte del codice per poi inserire chiamare il creaSuggRubrica

								orchestrator.refresh(contoToReport);  //contoToReport

								makeCall("GET", "ControllaSuggGiaInRubrica?idUtente="+salvatoIdUtenDest+"&codiceContoDest="+salvatoCodContoDest, null,    //sost forse con vaiAllaHome e paragonare servlet VaiAllahOME
										function(req2) {										
									if (req2.readyState == 4) {
										var message = req2.responseText; //responseText è 1 o 0 della servlet
										if (req2.status == 200) {
											var esito = parseInt(req2.responseText.slice(0,-1));
											if (esito == 0) {
												if(confirm("Vuoi inserire il contatto dell'utente destinatario del trasferimento appena effettuato in rubrica?")){
													//qui metto la richiesta ajax post a CreaSuggerimentoRubrica

													document.getElementById("idUtente").value = salvatoIdUtenDest;
													document.getElementById("codiceContoDest").value = salvatoCodContoDest;

													makeCall("POST", 'CreaSuggerimentoRubrica' , form,    
															function(req) {										
														if (req.readyState == 4) {
															var message = req.responseText;
															if (req.status == 200) {
																//var esito = JSON.parse(req.responseText);
																// self.alert.textContent=""; 

															}
														}  else { 
															self.alert.textContent = message;
														}
													});	//makeCall CreaSuggerimentoRubrica end
												}
											}
										}
									}

								});		//makeCall ControllaSuggGiaInRubrica

							} else {
								self.alert.textContent = message; //ritorna il messaggio risultato dato dalla servlet quindi sia ok sia errore 
							}
						}
					});	//makeCall OrdinaTrasferimento end

				} else {
					form.reportValidity();
				}
			});	//addEventListener end
		}

		this.show = function(contoId) {
			var self = this;
			//VediStatoConto			//codice conto non conto id
			makeCall("GET", "VediStatoConto?codiceConto=" + contoId, null,
					function(req) {
				if (req.readyState == 4) {
					var message = req.responseText;
					if (req.status == 200) {
						var trasferimentiTrovati = JSON.parse(req.responseText); //var conto non e' un array di trasferimenti in realta? forse errore di concetto qua
						//TODO >>: OCIO'  qua sopra non è var conto ma è  var listaTrasferimenti

						if (trasferimentiTrovati.length == 0) {
							self.alert.textContent = "Conto "+contoId+": Nessun trasferimento relativo a questo conto";
							document.getElementById("id_contoSelez").innerHTML = "";
							self.detailcontainer.style.visibility = "hidden";
							self.detailcontainerbody.style.visibility = "hidden";
							self.transferform.contoId.value = contoId;
							return;
						}	
						else{
							document.getElementById("id_contoSelez").innerHTML ="Stai visualizzando i movimenti del conto " +  contoId;

						}
						//conto
						self.alert.textContent="";
						self.update(trasferimentiTrovati,contoId); 
						// se non funziona cancella contoId qua sopra e lascia solo trasfTrovati
						self.detailcontainer.style.visibility = "visible";
						self.detailcontainerbody.style.visibility = "visible";

						self.transferform.style.visibility = "visible";

						self.transferform.contoId.value = contoId;   //contoId hidden nel trasnferForm è contoOrigine


						//if (contoId) contoId();  penso che NON ci voglia questo

					} else {
						self.alert.textContent = message;

					}
				}
			}
			);
		};




		this.reset = function() {
			this.detailcontainer.style.visibility = "hidden";
			this.detailcontainerbody.style.visibility = "hidden";
			this.transferform.style.visibility = "hidden";
		}

		//c è trasferimentiTrovati qua sotto
		this.update = function(c,contoId) { //vedi missio, qua mettere solo le cose che stan dentro il detailcontainer di un conto mi sa

			var elem, i, row, datacell, originecell, destinazcell, causalecell, importocell, entratacell;
			this.detailcontainerbody.innerHTML = ""; 


			var self = this;						
			c.forEach(function(trasferimento) { 

				row = document.createElement("tr");  //ocio xchè conto che a noi interessa puo essere sia in contoOrigine che in contoDestinazione (attributi di trasferimento)

				datacell = document.createElement("td");
				datacell.textContent = trasferimento.data;
				row.appendChild(datacell);

				originecell = document.createElement("td");
				originecell.textContent = trasferimento.contoOrigine;    //TUTTI GLI attributi del beans trasferim
				row.appendChild(originecell);

				destinazcell = document.createElement("td");
				destinazcell.textContent = trasferimento.contoDestinazione;
				row.appendChild(destinazcell);

				causalecell = document.createElement("td");
				causalecell.textContent = trasferimento.causale;
				row.appendChild(causalecell);

				importocell = document.createElement("td");
				importocell.textContent = trasferimento.importo;
				row.appendChild(importocell);

				entratacell = document.createElement("td");

				if(trasferimento.contoOrigine == contoId){
					entratacell.textContent = "Uscita";
					entratacell.className = "uscita";
				}
				else{

					entratacell.textContent = "Entrata";
					entratacell.className = "entrata";

				}

				row.appendChild(entratacell);



				self.detailcontainerbody.appendChild(row);  //cambiare
			});


			this.detailcontainer.style.visibility = "visible";  //cambiare

		}
	}




	function CreaConto(Id, alert) {  

		this.creaConto = Id;  
		this.alert = alert;


		this.registerEvents = function(orchestrator) {


			this.creaConto.querySelector("input[type='button'].submit").addEventListener('click', (e) => {
				var form = e.target.closest("form");  //prende il form di creazione spese
				if (form.checkValidity()) { ///qui posso controllare tutto il form in una botta sola
					var self = this,
					form = event.target.closest("form");
					makeCall("POST", 'CreaConto', form,
							function(req) {
						if (req.readyState == 4) {
							var message = req.responseText;
							if (req.status == 200) {
								orchestrator.refresh(message);
							} else {
								self.alert.textContent = message;
							}
						}
					}
					);
				} else {
					form.reportValidity();  //se non è corretto riporto errori
				}
			});


		};  //NON ELIMINAARE QUA

	}


	function PageOrchestrator() {
		var alertContainer = document.getElementById("id_alert");
		this.start = function() {
			personalMessage = new PersonalMessage(sessionStorage.getItem('username'),
					document.getElementById("id_username"));
			personalMessage.show();

			listaConti = new listaConti(  
					alertContainer,
					document.getElementById("id_listcontainer"),
					document.getElementById("id_listcontainerbody")); 

			dettagliConto = new dettagliConto({ 
				


				alert: alertContainer,   
				detailcontainer: document.getElementById("id_detailcontainer"),
				detailcontainerbody: document.getElementById("id_detailcontainerbody"),
				transferform: document.getElementById("id_transferform")  //OrdinaTrasferimento form


			});
			dettagliConto.registerEvents(this);  //DETTAGLI CONTO 

			creaConto = new CreaConto(document.getElementById("id_creacontoform"), alertContainer);
			creaConto.registerEvents(this);	


			document.querySelector("a[href='Logout']").addEventListener('click', () => {
				window.sessionStorage.removeItem('username');  //se cè questo mi sa che non serve allora
			})										// il file logout.js
		};


	
		this.refresh = function(currentconto) { //GIA VISTO QUESTO GIA FATTO DOVREBBE ESSERE GIUSTO
			alertContainer.textContent = "";      //CI SONO PICCOLE DIFFERENZE Xò TRA QUESTO FILE E QUELLO SU NOTEPAD
			listaConti.reset();  //conti list
			dettagliConto.reset();  //conto dettagli ovvero i suoi trasferimeti
			document.getElementById("id_transferform").style.visibility = "visible";
			listaConti.show(function() {
				listaConti.autoclick(currentconto);  //conti list  e currentConto
			});  
		};
	}
})();
