package beans;

import java.util.ArrayList;

public class Kupac extends Korisnik {
	private ArrayList<Karta> sveKarte;
	private int bodovi;
	private TipKupca tip;
	
	public Kupac() {
	}

	public Kupac(ArrayList<Karta> sveKarte, int bodovi, TipKupca tip) {
		super();
		this.sveKarte = sveKarte;
		this.bodovi = bodovi;
		this.tip = tip;
	}

	public ArrayList<Karta> getSveKarte() {
		return sveKarte;
	}

	public void setSveKarte(ArrayList<Karta> sveKarte) {
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
