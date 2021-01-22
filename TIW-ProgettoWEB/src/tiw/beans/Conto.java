package tiw.beans;

public class Conto {
	private int codice;
	private double saldo;
	private String nome;  //nome che puo' scegliere l'utente per distinguere i suoi
						 //diversi conto (esemp , conto principale, conto emergenze ,conto deposito)
	private int idUtente;
	
	
	public Conto(int codice,String nome,double saldo, int idUtente) {
		this.codice=codice;
		this.saldo=saldo;
		this.nome=nome;
		this.idUtente=idUtente;
				
	}
	
	public int getCodice() {
		return codice;
	}
	public void setCodice(int codice) {
		this.codice = codice;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}
}
