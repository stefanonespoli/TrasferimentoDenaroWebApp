package tiw.controllers;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tiw.utils.Common;
import tiw.utils.SecureUtils;
import tiw.DAO.UtenteDAO;
/**
 * Servlet che si occupa della registrazione di un utente
 */
@WebServlet("/Registrazione")
public class Registrazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public Registrazione() {
		super();
	}
	//inizializzazione connessione db
	public void init() throws ServletException {
		try {
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't get db connection");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recupero i parametri necessari dai campi
		String usrn = null;
		String mail = null;
		String pwd = null;
		String pwdCk = null;
		String secpwd= null;
		usrn = request.getParameter("utente");
		mail = request.getParameter("mail");
		pwd = request.getParameter("pwd");
		pwdCk = request.getParameter("pwdCk");
		
		//Verifico che i campi non siano nulli		
		if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty() || mail == null || pwdCk == null || mail.isEmpty() || pwdCk.isEmpty() ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Le credenziali non devono essere nulle");
			return;
		}
		//Verifico che password e conferma coincidano
		if (!pwd.equals(pwdCk)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Le Password devono coincidere");
			return;
		}
		//Verifico la validità sintattica della mail
		if (!Common.isValid(mail)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Scrivere la mail in formato corretto");
			return;
		}
		
		UtenteDAO userDao = new UtenteDAO(connection);
		Boolean user = false;
		Boolean email = false;
		//Controllo di non avere già un altro utente registrato con la mail inserita
		try {
			email = userDao.checkMail(mail);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Mail già in uso");
			return;
		}

		//Controllo di non avere già un altro utente registrato con nome utente inserito
		try {
			user = userDao.checkUsername(usrn);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Username già in uso");
			return;
		}

		if ((user == true)&&(email == true)) {
			try {				//CONFERMA REGISTRAZIONE
				secpwd= SecureUtils.getSecurePassword(pwd);
				userDao.registra(usrn, secpwd, mail);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("Registrazione avvenuta con successo.");
			} catch (SQLException e) { //PROBLEMI REGISTRAZIONE
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("Errore nella registrazione");
				return;
			}
		} else if (user == false){ //USERNAME IN UTILIZZO
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Username già esistente");
		} else if (email == false) { //MAIL IN UTILIZZO
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Indirizzo Mail già esistente");
		}
	}

	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqle) {
		}
	}
}