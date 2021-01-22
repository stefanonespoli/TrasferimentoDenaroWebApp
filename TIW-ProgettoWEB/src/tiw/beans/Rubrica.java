package tiw.beans;

public class Rubrica {
	
	private int idUtenteDestinatario;
	private int contoDestinatario;
	private int proprietarioRubrica;
	


	public Rubrica( int idUtenteDestinatario, int contoDestinatario, int proprietarioRubrica) {

		this.idUtenteDestinatario = idUtenteDestinatario;
		this.contoDestinatario = contoDestinatario;
		this.proprietarioRubrica=proprietarioRubrica;
	}
	



	public int getIdUtenteDestinatario() {
		return idUtenteDestinatario;
	}




	public void setIdUtenteDestinatario(int idUtenteDestinatario) {
		this.idUtenteDestinatario = idUtenteDestinatario;
	}




	public int getContoDestinatario() {
		return contoDestinatario;
	}




	public void setContoDestinatario(int contoDestinatario) {
		this.contoDestinatario = contoDestinatario;
	}
	
	public int getProprietarioRubrica() {
		return proprietarioRubrica;
	}


	public void setProprietarioRubrica(int proprietarioRubrica) {
		this.proprietarioRubrica = proprietarioRubrica;
	}


	
	
}

