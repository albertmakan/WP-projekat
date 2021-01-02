package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import beans.Korisnik;
import beans.Korisnik.Pol;
import beans.Korisnik.Uloga;
import beans.Kupac;

public class KorisnikDAO {
	private HashMap<String, Uloga> ulogeKorisnika;
	private HashMap<String, Korisnik> korisnici;
	private String putanjaFoldera;
	
	public KorisnikDAO(String contextPath) {
		korisnici = new HashMap<String, Korisnik>();
		putanjaFoldera = contextPath+"/data/korisnici";
		ucitajKorisnike();
	}
	
	private void ucitajKorisnike() {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			ulogeKorisnika = jsonb.fromJson(new FileReader(putanjaFoldera+"/uloge.json"), new HashMap<String, Uloga>(){
				private static final long serialVersionUID = 1L;}.getClass().getGenericSuperclass());
			for (String korIme : ulogeKorisnika.keySet()) {
				Uloga u = ulogeKorisnika.get(korIme);
				if (u == Uloga.KUPAC) {
			    	Kupac k = jsonb.fromJson(new FileReader(putanjaFoldera+'/'+korIme+".json"), Kupac.class);
			    	korisnici.put(k.getKorisnickoIme(), k);
			    } else if (u == Uloga.ADMIN || u == Uloga.PRODAVAC) {
			    	Korisnik k = jsonb.fromJson(new FileReader(putanjaFoldera+'/'+korIme+".json"), Korisnik.class);
			    	korisnici.put(k.getKorisnickoIme(), k);
				}
			}
		} catch (JsonbException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Korisnik registracijaProdavca(String ime, String prezime, String korisnickoIme, String lozinka, Pol pol, Date datumRodjenja) {
		Korisnik p = new Korisnik(korisnickoIme, lozinka, korisnickoIme, prezime, pol, datumRodjenja, Uloga.PRODAVAC);
		sacuvajKorisnika(p);
		sacuvajUloge();
		return p;
	}
	
	public Kupac registracijaKupca(String ime, String prezime, String korisnickoIme, String lozinka, Pol pol, Date datumRodjenja) {
		Kupac k = new Kupac(korisnickoIme, lozinka, korisnickoIme, prezime, pol, datumRodjenja);
		sacuvajKorisnika(k);
		sacuvajUloge();
		return k;
	}
	
	public void sacuvajKorisnika(Korisnik k) {
		ulogeKorisnika.put(k.getKorisnickoIme(), k.getUloga());
		korisnici.put(k.getKorisnickoIme(), k);
		Jsonb jsonb = JsonbBuilder.create();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(putanjaFoldera+'/'+k.getKorisnickoIme()+".json");
			pw.write(jsonb.toJson(k));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}
	
	private void sacuvajUloge() {
		Jsonb jsonb = JsonbBuilder.create();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(putanjaFoldera+"/uloge.json");
			pw.write(jsonb.toJson(ulogeKorisnika));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}
	
	public boolean validnoKorisnickoIme(String korisnickoIme) {
		if (korisnickoIme.matches("[a-zA-Z0-9._]{4,30}")) {
			if (ulogeKorisnika.get(korisnickoIme) != null || korisnickoIme.equals("uloge"))
				return false;
			return true;
		}
		return false;
	}
	
	public Korisnik getKorisnik(String korisnickoIme) {
		return korisnici.get(korisnickoIme);
	}
}
