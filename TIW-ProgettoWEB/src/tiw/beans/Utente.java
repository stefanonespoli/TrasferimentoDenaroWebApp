package tiw.beans;
//classe utente che contiene l'id e il nome dell'utente
public class Utente {
	private int id;
	private String utente;



	public int getId() {
		return id;
	}

	public String getUsername() {
		return utente;
	}
	
	public void setId(int i) {
		id = i;
	}

	public void setUsername(String un) {
		utente = un;
	}

}
