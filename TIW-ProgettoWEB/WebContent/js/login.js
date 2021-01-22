/**
 * Gestione login
 */ 

(function() { // avoid variables ending up in the global scope

	document.getElementById("loginbutton").addEventListener('click', (e) => {
		var formElement = e.target.closest("form");
		var usr = document.getElementById("loginUsername").value;
		var psw = document.getElementById("loginPwd").value;
		if (!(usr === null || psw === null)) {
		var xhttp;
		xhttp=new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4) {
				risposta(this);
			}
		};
		xhttp.open("POST", "CheckLogin", true);
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		var usr = document.getElementById("loginUsername").value;
		var psw = document.getElementById("loginPwd").value;
		xhttp.send("utente="+usr+"&pwd="+psw);
		} else {
			document.getElementById("messageLogin").textContent = "Username e psw nulli.";
		}
		
		function risposta(xhttp) {
			var message = xhttp.responseText;
			switch (xhttp.status) {
			case 200:
				sessionStorage.setItem('username', usr);
				window.location.href = "HomeCS.html";
				break;
			case 400: // bad request
				document.getElementById("messageLogin").textContent = message;
				break;
			case 401: // unauthorized
				document.getElementById("messageLogin").textContent = message;
				break;
			case 500: // server error
				document.getElementById("messageLogin").textContent = message;
				break;
			}

		}		
	});

})();