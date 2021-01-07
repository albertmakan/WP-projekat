package beans;

import java.time.LocalDateTime;

public class Manifestacija {
	private int id;
	private String naziv;
	private String tip;
	private int brojMesta;
	private int brojKarata;
	private LocalDateTime datumVreme;
	private float cenaKarte;
	private boolean aktivna;
	private Lokacija lokacija;
	// Poster (slika)
	
	public Manifestacija() {
	}
	
	public Manifestacija(int id, String naziv, String tip, int brojMesta, LocalDateTime datumVreme, float cenaKarte,
			Lokacija lokacija) {
		this.id = id;
		this.naziv = naziv;
		this.tip = tip;
		this.brojMesta = brojMesta;
		this.brojKarata = brojMesta;
		this.datumVreme = datumVreme;
		this.cenaKarte = cenaKarte;
		this.aktivna = false;
		this.lokacija = lokacija;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public int getBrojMesta() {
		return brojMesta;
	}

	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}

	public int getBrojKarata() {
		return brojKarata;
	}

	public void setBrojKarata(int brojKarata) {
		this.brojKarata = brojKarata;
	}

	public LocalDateTime getDatumVreme() {
		return datumVreme;
	}

	public void setDatumVreme(LocalDateTime datumVreme) {
		this.datumVreme = datumVreme;
	}

	public float getCenaKarte() {
		return cenaKarte;
	}

	public void setCenaKarte(float cenaKarte) {
		this.cenaKarte = cenaKarte;
	}

	public boolean isAktivna() {
		return aktivna;
	}

	public void setAktivna(boolean aktivna) {
		this.aktivna = aktivna;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}
	
}
