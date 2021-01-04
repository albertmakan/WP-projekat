package beans;

import java.util.ArrayList;
import java.util.Date;

public class Prodavac extends Korisnik{
	private ArrayList<Integer> manifestacije;	// id-ovi manifestacija; ako je Prodavac

	public Prodavac() {
	}

	public Prodavac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.PRODAVAC);
		manifestacije = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(ArrayList<Integer> manifestacije) {
		this.manifestacije = manifestacije;
	}
	
	public void addManifestacija(int idManifestacije) {
		manifestacije.add(idManifestacije);
	}

}
