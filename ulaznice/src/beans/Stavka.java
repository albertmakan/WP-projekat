package beans;

import beans.Karta.TipKarte;

public class Stavka {
	private int idManifestacije;
	private TipKarte tipKarte;
	private int komada;
	
	public Stavka() {
	}
	
	public Stavka(int idManifestacije, TipKarte tipKarte, int komada) {
		super();
		this.idManifestacije = idManifestacije;
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
	
}
