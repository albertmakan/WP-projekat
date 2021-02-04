package beans;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class Kupac extends Korisnik {
	private ArrayList<String> sveKarte;	// identifikatori karata
	@Expose
	private int bodovi;
	@Expose
	private TipKupca tip;
	
	public Kupac() {
	}

	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.KUPAC);
		this.sveKarte = new ArrayList<String>();
		this.bodovi = 0;
		this.tip = null;
	}

	public ArrayList<String> getSveKarte() {
		return sveKarte;
	}

	public void setSveKarte(ArrayList<String> sveKarte) {
		this.sveKarte = sveKarte;
	}
	
	public void addKarta(String idKarte) {
		sveKarte.add(idKarte);
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
