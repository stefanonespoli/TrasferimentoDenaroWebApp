package tiw.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tiw.beans.Trasferimento;
import tiw.exceptions.NoCorrelazioneUtenteContoException;
import tiw.exceptions.NonAbbastanzaDenaroException;

public class TrasferimentoDAO {
	private Connection con;
	

	public TrasferimentoDAO(Connection connection ) {
		this.con = connection;

	}
	
	public List<Trasferimento> trovaTrasferimentiConto(int codiceConto) throws SQLException {

		List<Trasferimento> transfers = new ArrayList<Trasferimento>();
		
		String query = "SELECT data, contoOrigine , contoDestinazione , causale, importo from trasferimento  where contoOrigine = ? OR  contoDestinazione = ? ORDER BY data DESC";
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, codiceConto);
			pstatement.setInt(2, codiceConto);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Trasferimento transf = new Trasferimento(result.getTimestamp("data"),result.getInt("contoOrigine"),result.getInt("contoDestinazione"),result.getString("causale"),result.getDouble("importo"));
					transfers.add(transf);
				}
			}
		}
		return transfers;
	}

	public void effettuaTrasferimento(int contoOrigine, int contoDestinazione, String causale
								,double importo,int idUtenteDestinatario) 
							throws SQLException,NoCorrelazioneUtenteContoException,NonAbbastanzaDenaroException {
		
		ContoDAO contoDAO=new ContoDAO(con);
		UtenteDAO userDAO=new UtenteDAO(con);
		// Check che quell' user ha quel conto      
		if (!userDAO.esistenzaUtenteConto(idUtenteDestinatario, contoDestinazione)) {
			throw new NoCorrelazioneUtenteContoException("Trasferimento non andato a buon fine: non esiste nessun codice Conto correlato all'utente specificato");
		}
		
		if(!contoDAO.saldoMaggioreUgualeImporto(contoOrigine, importo)) {
			throw new NonAbbastanzaDenaroException("Trasferimento non andato a buon fine: il conto di origine non ha abbastanza denaro");
		}
				
		
		String query = "INSERT into trasferimento (data, contoOrigine, contoDestinazione, causale , importo)   VALUES(?, ?, ?, ?, ?)";
		
		con.setAutoCommit(false); //transazione per garantire atomicità
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setTimestamp(1, new Timestamp(System.currentTimeMillis())); //veder se giusto
			pstatement.setInt(2, contoOrigine);
			pstatement.setInt(3, contoDestinazione);
			pstatement.setString(4, causale);
			pstatement.setDouble(5, importo);
			pstatement.executeUpdate();
			
			contoDAO.sottraiImportoAlSaldo(contoOrigine, importo);
			contoDAO.aggiungiImportoAlSaldo(contoDestinazione, importo);
			
			con.commit();
			
		}
		catch (SQLException e) {
			con.rollback(); 
			throw e;				// se anche uno solo fallisce roll back all work
		} finally {
			con.setAutoCommit(true);
		}
	}
}
