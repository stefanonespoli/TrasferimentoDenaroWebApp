package tiw.DAO;


import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tiw.beans.Conto;

public class ContoDAO {
	private Connection con;
	

	public ContoDAO(Connection connection) {
		this.con = connection;

	}
											
	public List<Conto> trovaContiUtente(int idUtente) throws SQLException {
		List<Conto> conti = new ArrayList<Conto>();
		String query = "SELECT * FROM conto WHERE idUtente = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, idUtente);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Conto ct = new Conto(result.getInt("codice"),result.getString("nome"),result.getDouble("saldo"),result.getInt("idUtente"));
					conti.add(ct);
				}
			}
		}
		return conti;
	}

										
	public int creaConto(String nomeConto, double saldoIniziale, int idUtente) throws SQLException {
		String query = "INSERT into conto (nome, saldo, idUtente)   VALUES(?, ?, ?)";
		try (PreparedStatement pstatement = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);) {
			pstatement.setString(1, nomeConto);
			pstatement.setDouble(2, saldoIniziale);
			pstatement.setInt(3, idUtente);
			pstatement.executeUpdate();
		
			ResultSet generatedKeys = pstatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Errore base di dati nel creaConto");
			}
		
		}
	}
	
	
	//consegna: dati conti m
	//per privacy non vengono fatti vedere dati conti destinazione di un altro user
	public Conto trovaDettagliConto(int codiceConto) throws SQLException {
		String query = "SELECT  codice ,nome, saldo, idUtente FROM conto WHERE codice = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, codiceConto);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // se zero risultati
					return null;
				else {
					result.next();
					Conto ct = new Conto(result.getInt("codice"),result.getString("nome"),result.getDouble("saldo"),result.getInt("idUtente"));
					return ct;
				}
			}
		}
	}
	
	public boolean saldoMaggioreUgualeImporto (int codiceContoOrigine, double importo)throws SQLException{
		String query = "SELECT * from conto where codice = ? and saldo>= ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, codiceContoOrigine);
			pstatement.setDouble(2, importo);			
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					return true;
				}
			}
		}
		return false;
		
		
		
	}
	
	public void sottraiImportoAlSaldo(int contoOrigine, double importo) throws SQLException {
		String query = "UPDATE conto SET saldo = saldo - ? WHERE codice = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setDouble(1, importo);
			pstatement.setInt(2, contoOrigine);
			pstatement.executeUpdate();
		}
	}
	
	public void aggiungiImportoAlSaldo(int contoDestinazione, double importo) throws SQLException {
		String query = "UPDATE conto SET saldo = saldo + ? WHERE codice = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setDouble(1, importo);
			pstatement.setInt(2, contoDestinazione);
			pstatement.executeUpdate();
		}
	}
}
