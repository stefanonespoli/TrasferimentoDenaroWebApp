package tiw.beans;

import java.sql.Timestamp;

public class Trasferimento {

	private Timestamp data;
	private String causale;
	private int contoOrigine;
	private int contoDestinazione;
	private double importo;
	
	public Trasferimento(Timestamp data,int contoOrigine,int contoDestinazione,String causale,double importo) {
		
		this.data=data;
		this.causale=causale;
		this.contoOrigine=contoOrigine;
		this.contoDestinazione=contoDestinazione;
		this.importo=importo;
				
	}
	
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public int getContoOrigine() {
		return contoOrigine;
	}
	public void setContoOrigine(int contoOrigine) {
		this.contoOrigine = contoOrigine;
	}
	public int getContoDestinazione() {
		return contoDestinazione;
	}
	public void setContoDestinazione(int contoDestinazione) {
		this.contoDestinazione = contoDestinazione;
	}
	public Timestamp getData() {
		return data;
	}
	public void setData(Timestamp data) {
		this.data = data;
	}

}
