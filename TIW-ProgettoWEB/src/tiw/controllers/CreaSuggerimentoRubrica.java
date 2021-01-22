package tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tiw.DAO.RubricaDAO;
import tiw.beans.Utente;
import tiw.utils.Common;
/**
 * Servlet
 *
 */
@WebServlet("/CreaSuggerimentoRubrica")
@MultipartConfig
public class CreaSuggerimentoRubrica extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

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
			e.printStackTrace();
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UnavailableException("Couldn't get db connection");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Integer codiceContoDest=null;
		Integer idUtenteDest=null;
		Utente user = (Utente) request.getSession().getAttribute("user"); //qsto ci vuole anche in creaSuggRubrica

		
		
		
		// (QUESTA SERVLET VIENE INVOCATA SUBITO DOPO CHE UN ORDINETRANSF SIA ANDATO A BUON FINE)
		
		
		idUtenteDest =Integer.parseInt( request.getParameter("idUtente"));
		codiceContoDest =Integer.parseInt( request.getParameter("codiceContoDest"));
		// e ma non le invia il f0rm f0rse bisogna farle var di sessione
		//capire se l utente deve dare l ok per creare il suggerimeto o no form li si allora
		
		//non so fai una var di sessione o di js che se è true allora il trasf è andato a buon fine
		//e puoi far partire questa servlet (java o js boh vedere)
		
		RubricaDAO r= new RubricaDAO(connection);
		try {
		
							
				try {
			r.salvaDatiDestinatario(idUtenteDest,codiceContoDest,user.getId());
				
				
			}
			catch (SQLException e) {
				
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("Errore nell'inserimento in rubrica (database error)");

				
			}
			
			//se arrivo qui è andato tutto bene
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("ContattoInserito");

			
		} catch (IOException e1) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("database error");
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
