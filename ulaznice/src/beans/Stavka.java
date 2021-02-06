package beans;

import beans.Karta.TipKarte;

public class Stavka {
	private int idManifestacije;
	private String naziv;
	private float cena;
	private TipKarte tipKarte;
	private int komada;
	
	public Stavka() {
	}
	
	public Stavka(Manifestacija m, Kupac k, TipKarte tipKarte, int komada) {
		super();
		idManifestacije = m.getId();
		setNaziv(m.getNaziv());
		cena = m.getCenaKarte();
		if (tipKarte == TipKarte.FAN_PIT) cena *= 2;
		else if (tipKarte == TipKarte.VIP) cena *= 4;
		cena -= k.getTip().getPopust()/100.0*cena;
		this.tipKarte = tipKarte;
		this.komada = komada;
	}

	public int getIdManifestacije() {
		return idManifestacije;
	}

	public void setIdManifestacije(int idManifestacije) {
		this.idManifestacije = idManifestacije;
	}

	public TipKarte getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(TipKarte tipKarte) {
		this.tipKarte = tipKarte;
	}

	public int getKomada() {
		return komada;
	}

	public void setKomada(int komada) {
		this.komada = komada;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public float getCena() {
		return cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}
	
}
