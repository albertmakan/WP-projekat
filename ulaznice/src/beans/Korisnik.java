package beans;

import java.util.ArrayList;
import java.util.Date;

import javax.json.bind.annotation.JsonbDateFormat;

public class Korisnik {
	public enum Pol {M, Z}
	public enum Uloga {ADMIN, PRODAVAC, KUPAC}
	private String korisnickoIme, lozinka, ime, prezime;
	private Pol pol;
	@JsonbDateFormat(JsonbDateFormat.TIME_IN_MILLIS)
	private Date datumRodjenja;
	private Uloga uloga;
	private ArrayList<Integer> manifestacije;	// id-ovi manifestacija; ako je Prodavac
	private boolean blokiran;
	
	public Korisnik() {
	}
	
	public Korisnik(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja,
			Uloga uloga) {
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.uloga = uloga;
		if (uloga == Uloga.PRODAVAC)
			manifestacije = new ArrayList<Integer>();
		else
			manifestacije = null;
		this.blokiran = false;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
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

	public Pol getPol() {
		return pol;
	}

	public void setPol(Pol pol) {
		this.pol = pol;
	}

	public Date getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

	public ArrayList<Integer> getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(ArrayList<Integer> manifestacije) {
		this.manifestacije = manifestacije;
	}

	public boolean isBlokiran() {
		return blokiran;
	}

	public void setBlokiran(boolean blokiran) {
		this.blokiran = blokiran;
	}
	
}
