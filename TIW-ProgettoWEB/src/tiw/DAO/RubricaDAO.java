package tiw.DAO;


import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class RubricaDAO {

	private Connection con;

	public RubricaDAO(Connection connection) {
		this.con = connection;
	}


	public void salvaDatiDestinatario(int idUtenteDest, int codiceContoDest,int proprietarioRubrica) throws SQLException {
		//proprietario rubrica è l ID dell utente loggato
		String query = "INSERT into rubrica (idUtenteDestinazione, contoDestinazione, proprietarioRubrica)   VALUES(?, ?, ?)";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, idUtenteDest);
			pstatement.setInt(2,  codiceContoDest);
			pstatement.setInt(3,  proprietarioRubrica);
			pstatement.executeUpdate();
		}
	}

	//proprietario rubrica è l ID dell utente loggato
	public int trovaIdUtenteDestDaContoDest(int idProprietarioRubrica,int codiceContoDestinatario) throws SQLException {
		//nel DB la relazione Rubrica contiene tutti i suggerimenti per tutti gli utenti
		String query = "SELECT idUtenteDestinazione from rubrica  where contoDestinazione= ? and proprietarioRubrica=?  ";

		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, codiceContoDestinatario);
			pstatement.setInt(2, idProprietarioRubrica);	
			try (ResultSet result = pstatement.executeQuery()) {

				result.next();	//Muoviamo il cursore sulla prima riga di risultati

				return result.getInt(1);//selezioniamo la prima colonna(e l' unica) 
			}
		}
	}


	//proprietario rubrica è l ID dell utente loggato
	public List<Integer> trovaCodiceContoDestDaIdUtenteDest(int idProprietarioRubrica,int idUtenteDest) throws SQLException {
		//nel DB la relazione Rubrica contiene tutti i suggerimenti per tutti gli utenti
		String query = "SELECT contoDestinazione from rubrica  where idUtenteDestinazione= ? and proprietarioRubrica=?  ";

		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, idUtenteDest);
			pstatement.setInt(2, idProprietarioRubrica);	
			try (ResultSet result = pstatement.executeQuery()) {

				List<Integer> idTrovati =  new ArrayList<Integer>();	//dichiaro l' array che conterrà i valori

				while(result.next())	//Muoviamo il cursore sulla prossima riga di risultati
				{
					idTrovati.add(result.getInt(1));
				}

				return idTrovati;//selezioniamo la prima colonna(e l' unica) e la convertiamo in un array
			}
		}
	}

	public boolean controllaSuggerimentoGiaInRubrica(int idProprietarioRubrica,int idUtenteDestinazione,int codiceContoDestinatario) throws SQLException {
		//nel DB la relazione Rubrica contiene tutti i suggerimenti per tutti gli utenti
		String query = "SELECT *  from rubrica where idUtenteDestinazione= ? and contoDestinazione= ? and proprietarioRubrica= ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, idUtenteDestinazione);		
			pstatement.setInt(2, codiceContoDestinatario);
			pstatement.setInt(3, idProprietarioRubrica);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					return true;
				}
			}
		}
		return false;
	}


}
