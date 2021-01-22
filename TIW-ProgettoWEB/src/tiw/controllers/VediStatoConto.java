package tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tiw.DAO.TrasferimentoDAO;
import tiw.beans.Trasferimento;
import tiw.beans.Utente;
import tiw.utils.Common;

/**
 *
 * Mostra i dettagli di un conto (lista trasferimenti) richiedendoli al DB
 */
@WebServlet("/VediStatoConto")
public class VediStatoConto extends HttpServlet {
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
		
		
		//questo qua sotto commentato ricordarsi di farlo da un altra parte,oppure non fare creaConto
				//request.getSession().setAttribute("risultatoCreazioneConto", "");	//serve alla pagina VaiAllaHome
				
				Integer codiceConto = null;
				try {
					codiceConto = Integer.parseInt(request.getParameter("codiceConto"));

				} catch (NumberFormatException | NullPointerException e) {
					// solo per debug e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println("param values errati");
					return;

				}
				
				Utente user = (Utente) request.getSession().getAttribute("user");
				request.getSession().setAttribute("codiceConto", codiceConto);

				TrasferimentoDAO tDAO= new TrasferimentoDAO(connection);

				List<Trasferimento> trasferimenti = new ArrayList<Trasferimento>();
			
				
					
				
				try {

					trasferimenti=tDAO.trovaTrasferimentiConto(codiceConto);
	
					

				} catch (SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Impossibile recuperare StatoDelConto");
					return;
				}
				
				
				
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();  //non serve datetime visto che nel db e' cosi il formato?

				String json = gson.toJson(trasferimenti);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);  //invia body risposta a chi ne ha fatto richiesta
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
