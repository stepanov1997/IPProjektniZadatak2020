package net.etfbl.lab.dto;

public class Korisnik {

	private int id;
	private String username;
	private String password;
	private String ime;
	private String prezime;

	public Korisnik() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Korisnik(int id, String username, String password, String ime, String prezime) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

}
