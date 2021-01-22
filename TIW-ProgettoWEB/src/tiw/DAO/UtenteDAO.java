package tiw.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tiw.beans.Utente;


public class UtenteDAO {
	private Connection connection;

	public UtenteDAO(Connection connection) {
		this.connection = connection;
	}
	//controlla se i dati forniti nella form siano corretti
	public Utente checkCredentials(String utente, String password) throws SQLException {
		String query = "SELECT idutente, nome FROM utenti WHERE nome=? AND password=?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1, utente);
			pstatement.setString(2, password); 
			result = pstatement.executeQuery();
			if (!result.isBeforeFirst()) // no results, credential check failed
				return null;
			else {
				result.next();
				Utente user = new Utente();
				user.setId(result.getInt("idutente"));
				user.setUsername(result.getString("nome"));
				return user;
			}	
		} finally   {
			try {
				result.close();
				pstatement.close();
			} catch (Exception e1) {
				System.out.println("Errore nella chiusura degli stati e connessione: " + e1);
			}
		}
	}
	//controlla l'esistenza di un utente con stesso username
	public Boolean checkUsername(String utente) throws SQLException {
		String query = "SELECT idutente, nome FROM utenti WHERE nome=?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1, utente);
			result = pstatement.executeQuery();
			if (!result.isBeforeFirst()) // nessun risultato, quindi posso procedere
				return true;
			else {
				return false; //c'è già un utente con lo stesso nome
			}	
		} finally  {
			try {
				result.close();
				pstatement.close();
			} catch (Exception e1) {
				System.out.println("Errore nella chiusura degli stati e connessione: " + e1);
			}
		}
	}
	//controlla l'esistenza di una mail identica
	public Boolean checkMail(String mail) throws SQLException {
		String query = "SELECT idutente, nome FROM utenti WHERE mail=?";
		PreparedStatement pstatement = null;
		ResultSet result = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1, mail);
			result = pstatement.executeQuery();
			if (!result.isBeforeFirst()) // nessun risultato quindi posso procedere
				return true;
			else {
				return false; //c'è già un utente con la stessa mail
			}	
		} finally  {
			try {
				result.close();
				pstatement.close();
			} catch (Exception e1) {
				System.out.println("Errore nella chiusura degli stati e connessione: " + e1);
			}
		}
	}
	//Insert nel DB nel caso di registrazione
	public void registra(String utente, String password, String mail) throws SQLException {
		String query = "INSERT INTO utenti (nome, password, mail) VALUES (?, ?, ?)";
		PreparedStatement pstatement = null;
		try {
			pstatement = connection.prepareStatement(query);
			pstatement.setString(1, utente);
			pstatement.setString(2, password);
			pstatement.setString(3, mail);
			pstatement.executeUpdate();
		} finally  {
			try {
				pstatement.close();
			} catch (Exception e1) {
				System.out.println("Errore nella chiusura degli stati e connessione: " + e1);
			}
		}
	}
	
	
	public boolean esistenzaUtenteConto(int idUtenteDestinatario,int codiceContoDestinatario) throws SQLException {
		String query = "SELECT * from utenti JOIN conto ON utenti.idutente=conto.idUtente where utenti.idutente = ? and conto.codice= ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			
			pstatement.setInt(1, idUtenteDestinatario);
			pstatement.setInt(2, codiceContoDestinatario);			
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					return true;
				}
			}
		}
		return false;
	}
}