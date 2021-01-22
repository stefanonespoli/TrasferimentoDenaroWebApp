/**
 * Gestione registrazione
 */ 

(function() { // avoid variables ending up in the global scope

	document.getElementById("registerbutton").addEventListener('click', (e) => {
		//validità form
		//if(document.getElementById("psw_reg").value == document.getElementById("psw2_reg").value 
		//	&& validateEmail(document.getElementById("mail_reg").value))
		var form = e.target.closest("form");
		if (form.checkValidity()) {
			if(document.getElementById("psw_reg").value != document.getElementById("psw2_reg").value)
			{
				document.getElementById("messageRegister").textContent = "Le password non corrispondono";
				return;
			}
			var xhttp;
			xhttp=new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4) {
					rispostaServer(this);
				}
			};
			xhttp.open("POST", "Registrazione", true);
			xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			//xhttp.send(new FormData(formElement));
			var usr = document.getElementById("user_reg").value;
			var mail = document.getElementById("mail_reg").value;
			var psw = document.getElementById("psw_reg").value;
			var psw2 = document.getElementById("psw2_reg").value;

			xhttp.send("utente="+usr+"&pwd="+psw+"&pwdCk="+psw2+"&mail="+mail);
		} else {
			document.getElementById("messageRegister").textContent = form.validationMessage;
		}
		
		function rispostaServer(xhttp) {
			var message = xhttp.responseText;
			switch (xhttp.status) {
			case 200: //successful
				document.getElementById("messageRegister").textContent = message;
				form.reset();
				break;
			case 400: // bad request
				document.getElementById("messageRegister").textContent = message;
				break;
			case 401: // unauthorized
				document.getElementById("messageRegister").textContent = message;
				break;
			case 500: // server error
				document.getElementById("messageRegister").textContent = message;
				break;
			}

		}
	});

})();