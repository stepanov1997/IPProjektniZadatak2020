package net.etfbl.lab.dto;

import java.io.Serializable;
import java.util.Date;

public class Obaveza implements Serializable {

	private static final long serialVersionUID = -5152941347101008809L;
	private int id;
	private String opis;
	private Date datum;
	private String kategorija;
	private String adresa;

	public Obaveza() {
		// TODO Auto-generated constructor stub
	}

	public Obaveza(int id, String opis, Date datum, String kategorija, String adresa) {
		super();
		this.id = id;
		this.opis = opis;
		this.datum = datum;
		this.kategorija = kategorija;
		this.adresa = adresa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

}
