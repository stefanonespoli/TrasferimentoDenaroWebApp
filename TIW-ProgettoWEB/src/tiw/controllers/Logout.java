package tiw.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Logout() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	//Termina la sessione dell'utente
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			response.setStatus(HttpServletResponse.SC_OK);
			System.out.println("Logout effettuato");
			
			String path = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(path);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			System.out.println("Problemi durante il logout...");
		}
	}
}

