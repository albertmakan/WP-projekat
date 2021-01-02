package beans;

import java.util.ArrayList;
import java.util.Date;

import beans.TipKupca.ImeTipa;

public class Kupac extends Korisnik {
	private ArrayList<String> sveKarte;	// identifikatori karata
	private int bodovi;
	private TipKupca tip;
	
	public Kupac() {
	}

	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.KUPAC);
		this.sveKarte = new ArrayList<String>();
		this.bodovi = 0;
		this.tip = new TipKupca(ImeTipa.ZELENI, 0, 2000);
	}

	public ArrayList<String> getSveKarte() {
		return sveKarte;
	}

	public void setSveKarte(ArrayList<String> sveKarte) {
		this.sveKarte = sveKarte;
	}

	public int getBodovi() {
		return bodovi;
	}

	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
	}

	public TipKupca getTip() {
		return tip;
	}

	public void setTip(TipKupca tip) {
		this.tip = tip;
	}
	
}
