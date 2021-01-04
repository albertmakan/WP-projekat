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
import beans.TipKupca.ImeTipa;
import beans.Kupac;
import beans.Prodavac;
import beans.TipKupca;

public class KorisnikDAO {
	private HashMap<String, Uloga> ulogeKorisnika;
	private HashMap<String, Korisnik> korisnici;
	private String putanjaFoldera;
	private TipKupca[] tipovi = {
		new TipKupca(ImeTipa.ZELENI, 0, 0),
		new TipKupca(ImeTipa.BRONZANI, 1, 2000),
		new TipKupca(ImeTipa.SREBRNI, 3, 3000),
		new TipKupca(ImeTipa.ZLATNI, 5, 4000)
	};
	
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
				switch (u) {
				case ADMIN:
					Korisnik admin = jsonb.fromJson(new FileReader(putanjaFoldera+'/'+korIme+".json"), Korisnik.class);
			    	korisnici.put(admin.getKorisnickoIme(), admin);
			    	break;
				case KUPAC:
					Kupac kupac = jsonb.fromJson(new FileReader(putanjaFoldera+'/'+korIme+".json"), Kupac.class);
			    	azurirajTipKupca(kupac);
			    	korisnici.put(kupac.getKorisnickoIme(), kupac);
			    	break;
				case PRODAVAC:
					Prodavac prod = jsonb.fromJson(new FileReader(putanjaFoldera+'/'+korIme+".json"), Prodavac.class);
			    	korisnici.put(prod.getKorisnickoIme(), prod);
			    	break;
				default:
					break;
				}
			}
		} catch (JsonbException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void azurirajTipKupca(Kupac k) {
		if (k.getBodovi() < tipovi[1].getTrazeniBrojBodova())
			k.setTip(tipovi[0]);
		else if (k.getBodovi() < tipovi[2].getTrazeniBrojBodova())
			k.setTip(tipovi[1]);
		else if (k.getBodovi() < tipovi[3].getTrazeniBrojBodova())
			k.setTip(tipovi[2]);
		else
			k.setTip(tipovi[3]);
	}
	
	public boolean postoji(String korisnickoIme, String lozinka) {
		Korisnik k = korisnici.get(korisnickoIme);
		if (k != null)
			return k.getLozinka().equals(lozinka);
		return false;
	}
	
	public Prodavac registracijaProdavca(String ime, String prezime, String korisnickoIme, String lozinka, Pol pol, Date datumRodjenja) {
		Prodavac p = new Prodavac(korisnickoIme, lozinka, korisnickoIme, prezime, pol, datumRodjenja);
		sacuvajKorisnika(p);
		sacuvajUloge();
		return p;
	}
	
	public Kupac registracijaKupca(String ime, String prezime, String korisnickoIme, String lozinka, Pol pol, Date datumRodjenja) {
		Kupac k = new Kupac(korisnickoIme, lozinka, korisnickoIme, prezime, pol, datumRodjenja);
		azurirajTipKupca(k);
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
