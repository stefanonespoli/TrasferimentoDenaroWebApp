package tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.Gson;

import tiw.DAO.RubricaDAO;
import tiw.beans.Conto;
import tiw.beans.Utente;



/**
 * 
 *
 */

@WebServlet("/EstraiSuggerimentoRubrica")
@MultipartConfig
public class EstraiSuggerimentoRubrica extends HttpServlet {
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

		if(request.getParameter("valoreIndicato") == "") //per evitare di ritornare al js degli errori
			return;
		
		Integer codiceContoDest = null;
		Integer idUtenteDest = null;
		Integer criterioRicerca = null;


		//CONTROLLO SE PER QUESTO UTENTE LOGGATO ESISTE UN SUGGERIMENTO, PRENDO IL CODICE CONTO DEST
		//DAL FORM, (DIGITATO DALL USER) E GUARDO SE Cè UN SUGG DI CODICE UTENTE DEST DA POTER RITORNARE
		//SE SI LO INVIO SE NON NIENTE , O TORNO NULL O NIENTE BOH POI VEDERE

		Utente user = (Utente) request.getSession().getAttribute("user"); //qsto ci vuole anche in creaSuggRubrica
		try{
            criterioRicerca = Integer.parseInt( request.getParameter("criterioRicerca") );
        }catch(NumberFormatException e)
        {
            return; //il js è stato manomesso, usciamo
        }

		if(criterioRicerca == 1) //utente ha specificato codiceconto
			codiceContoDest = Integer.parseInt( request.getParameter("valoreIndicato"));
		else //utente ha specificato idUtenteDest
			idUtenteDest = Integer.parseInt( request.getParameter("valoreIndicato"));
		

		if(codiceContoDest != null)
		{
			RubricaDAO r= new RubricaDAO(connection);
			int idUtenDestTrovati = -1;
            //in realtà è uno solo

			//qui andiamo a cercare l' Id utrente dest dato il codice conto
			try {
				idUtenDestTrovati = r.trovaIdUtenteDestDaContoDest(user.getId(),codiceContoDest);
			} catch (SQLException e2) {

				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("database error");
				//e2.printStackTrace();
			}

			//controlliamo che ci siano dei risultati
			if(idUtenDestTrovati == -1) {

				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("database error");

			}else
			{

				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");	
				response.getWriter().println(idUtenDestTrovati);  
			}			
			
		}else if (idUtenteDest != null)
		{
			RubricaDAO r = new RubricaDAO(connection);
			List<Integer> codiciContiTrovati = new ArrayList<Integer>();


			//qui andiamo a cercare i codici conto dest dato l' id utente
			try {
				codiciContiTrovati = r.trovaCodiceContoDestDaIdUtenteDest(user.getId(),idUtenteDest);
			} catch (SQLException e2) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("database error");
				
			}

			//controlliamo che ci siano dei risultati
			if(codiciContiTrovati.isEmpty()) {

				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("database error");

			}else 
			{
				
				Gson gson = new Gson();
				String json = gson.toJson(codiciContiTrovati);
				
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");	
				response.getWriter().println(json); 
			}			
		}else return;	//è stato manomesso il js, usciamo


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
