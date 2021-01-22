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

import org.apache.commons.lang.StringEscapeUtils;

import tiw.DAO.TrasferimentoDAO;
import tiw.beans.Conto;
import tiw.beans.Utente;
import tiw.exceptions.NoCorrelazioneUtenteContoException;
import tiw.exceptions.NonAbbastanzaDenaroException;
import tiw.utils.Common;

/**
 * 
 */
@WebServlet("/OrdinaTrasferimento")
@MultipartConfig
public class OrdinaTrasferimento extends HttpServlet {
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
		
		Conto selezionato=null;
		Utente uten=null;
				
		//selezionato=(Conto) request.getSession().getAttribute("contoSelezionato");  // o cosi dalla sessione o forse ancora con
		//codiceContoselezionato = Integer.parseInt(request.getParameter("codiceContoselezionato")); o campo hidden nell invio del form
		//oppure con sessione js lato client vedere
		
		//o vedere createExpenseRport fraternali  MEGLIO EH
		
		uten= (Utente) request.getSession().getAttribute("user");

		boolean isBadRequest=false;
		boolean stessoUtenteStessoConto=false;
		Integer idUtenteDest=null;
		Integer codiceContoDest=null;
		Integer idcontoselezionato=null;  //conto origine
		String causale=null;
		boolean trasfok=true;
		double importo=0;
		try{	
			
			idUtenteDest =Integer.parseInt( request.getParameter("idUtente"));
			idcontoselezionato=Integer.parseInt(request.getParameter("contoId"));
			codiceContoDest =Integer.parseInt( request.getParameter("codiceContoDest"));
			causale = request.getParameter("causale");
			importo = Double.parseDouble(request.getParameter("importo"));
			
			if( !Common.isInteger(request.getParameter("idUtente")) && !Common.isInteger(request.getParameter("codiceContoDest")) && request.getParameter("idUtente") == null && request.getParameter("codiceContoDest") == null && request.getParameter("causale") != null && request.getParameter("importo") == null ) //se paramentro valido
			{
				trasfok=false;
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println("Parametri errati");	
				return;
			}


		
			if (idUtenteDest <= 0 || codiceContoDest <= 0 || causale.isEmpty() || importo<=0 ) {
				isBadRequest=true;
			}
			
			if(causale.matches("\\d+")){ //se causale e' formata solo da numeri
				isBadRequest=true;
			}
				
		}catch (NumberFormatException | NullPointerException e) {
			trasfok=false;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Bisogna creare un conto prima di poter effettuare un trasferimento");
			e.printStackTrace(); //solo per debug poi da togliere
			return;
		}
		if (isBadRequest) {
			trasfok=false;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("La causale non può essere formata da soli numeri.");
			return;
		}
		

		
		//selezionato è il conto di origine trasferimento   
		if(uten.getId() == idUtenteDest && idcontoselezionato == codiceContoDest) {
			trasfok=false;
			stessoUtenteStessoConto=true;
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Trasferimento non andato a buon fine: Si sta tentando di versare denaro nel medesimo conto di origine");
			return;
		}
		
		//crea trasferimento nel db
		
		TrasferimentoDAO trasfDAO = new TrasferimentoDAO(connection);
		
		try {
			
			if(!stessoUtenteStessoConto) {
			trasfDAO.effettuaTrasferimento(idcontoselezionato,codiceContoDest, causale,importo, idUtenteDest);
			}
		} 
		catch (NoCorrelazioneUtenteContoException e2) {
			
			trasfok=false;
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(e2.getMessage());
						
		} catch (NonAbbastanzaDenaroException e3) {
			
			trasfok=false;
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(e3.getMessage());


			
		} 
		catch (SQLException e1) {
			
			trasfok=false;
			
			response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Fallimento money transfer transaction nel database");
			return;
		}
		
		finally {
			
		if(trasfok==true){  
	
		
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");  
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println("Trasferimento effettuato"); 
		}
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
