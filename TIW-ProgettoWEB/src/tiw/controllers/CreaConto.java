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

import tiw.DAO.ContoDAO;
import tiw.beans.Utente;
import tiw.utils.Common;

/**
 * Servlet 
 */
@WebServlet("/CreaConto")
@MultipartConfig
public class CreaConto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

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

			
					Utente user = (Utente) request.getSession().getAttribute("user");

		//Recupero dati dalla form
		String nomeConto = request.getParameter("nomeConto");
		double importoIniziale= -1;
		
		try {
			importoIniziale = Double.parseDouble(request.getParameter("importoIniziale"));;
			} catch (NumberFormatException e) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("Importo deve essere un numero");
				return;
			}
		
		if (nomeConto == null || importoIniziale <0 ||nomeConto.isEmpty()  ) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Operazioni non concesse nella creazione del conto");
			return;
		}
		
		int idUtente = user.getId();
		
		//adesso passiamo i dati al DAO che fa la query
		ContoDAO creaConto = new ContoDAO(connection);
		int nuovoContoId=0;
		try {
			
			nuovoContoId=creaConto.creaConto(nomeConto, importoIniziale, idUtente);
			
		} catch (SQLException e) {
			
			//se arrivo qui si è verificato un errore

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Si è verificato un problema nella creazione del nuovo conto!");
			//per debugging e.printStackTrace();
		}
		
		if(nuovoContoId !=0)		{
		response.setStatus(HttpServletResponse.SC_OK); 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");  
		response.getWriter().print(nuovoContoId); //newContoID (quello appena creato)
					
		}
		}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
