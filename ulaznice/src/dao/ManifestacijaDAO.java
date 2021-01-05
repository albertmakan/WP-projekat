package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import beans.Lokacija;
import beans.Manifestacija;
import beans.Prodavac;

public class ManifestacijaDAO {
	private HashMap<Integer, Manifestacija> manifestacije;
	private HashMap<String, Integer> mesta;
	private HashSet<String> tipovi;
	private String putanjaFajla;
	private String putanjaPostera;
	
	public ManifestacijaDAO(String contextPath) {
		mesta = new HashMap<String, Integer>();
		putanjaFajla = contextPath+"/data/manifestacije.json";
		putanjaPostera = contextPath+"/data/posteri";
		ucitajManifestacije();
	}

	private void ucitajManifestacije() {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			ArrayList<Manifestacija> sveManifestacije = jsonb.fromJson(new FileReader(putanjaFajla), new ArrayList<Manifestacija>(){
				private static final long serialVersionUID = 1L;}.getClass().getGenericSuperclass());
			for (Manifestacija manifestacija : sveManifestacije) {
				manifestacije.put(manifestacija.getId(), manifestacija);
				String mesto = manifestacija.getLokacija().getMesto();
				if (mesta.get(mesto) == null)
					mesta.put(mesto, 1);
				else
					mesta.put(mesto, mesta.get(mesto)+1);
				tipovi.add(manifestacija.getTip());
			}
		} catch (JsonbException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Manifestacija getManifestacija(int id) {
		return manifestacije.get(id);
	}
	
	public Collection<Manifestacija> getManifestacije() {
		return manifestacije.values();
	}
	
	public Collection<Manifestacija> getManifestacije(Prodavac prodavac) {
		ArrayList<Manifestacija> manifestacijeProd = new ArrayList<Manifestacija>();
		for (int id : prodavac.getManifestacije())
			manifestacijeProd.add(manifestacije.get(id));
		return manifestacijeProd;
	}
	
	public Collection<Manifestacija> pretragaPoNazivu(String tekst) {
		ArrayList<Manifestacija> rezultat = new ArrayList<Manifestacija>();
		tekst = tekst.trim().toLowerCase();
		if (tekst.equals(""))
			return rezultat;
		for (Manifestacija m : manifestacije.values())
			if (m.getNaziv().toLowerCase().contains(tekst))
				rezultat.add(m);
		return rezultat;
	}
	
	public Collection<Manifestacija> kombinovanaPretraga(String mesto, float cenaOd, float cenaDo, LocalDateTime datumOd, LocalDateTime datumDo) {
		ArrayList<Manifestacija> rezultat = new ArrayList<Manifestacija>();
		for (Manifestacija m : manifestacije.values()) {
			if (m.getLokacija().getMesto().equals(mesto)) {
				if (m.getCenaKarte() >= cenaOd && m.getCenaKarte() <= cenaDo) {
					if (m.getDatumVreme().isAfter(datumOd) && m.getDatumVreme().isBefore(datumDo)) {
						rezultat.add(m);
					}
				}
			}
		}
		return rezultat;
	}
	
	// posle poziva ove metode potrebno je sacuvati prodavca
	public Manifestacija kreirajManifestaciju(Prodavac prodavac, String naziv, String tip, int brojMesta, LocalDateTime datumVreme, float cenaKarte,
			Lokacija lokacija) {
		int id = manifestacije.size();
		Manifestacija m = new Manifestacija(id, naziv, tip, brojMesta, datumVreme, cenaKarte, lokacija);
		manifestacije.put(m.getId(), m);
		String mesto = m.getLokacija().getMesto();
		if (mesta.get(mesto) == null)
			mesta.put(mesto, 1);
		else
			mesta.put(mesto, mesta.get(mesto)+1);
		tipovi.add(m.getTip());
		prodavac.addManifestacija(m.getId());
		sacuvajManifestacije();
		return m;
	}
	
	public void sacuvajManifestacije() {
		Jsonb jsonb = JsonbBuilder.create();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(putanjaFajla);
			pw.write(jsonb.toJson(manifestacije.values()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}

	public String getPutanjaPostera() {
		return putanjaPostera;
	}
	
	public void ododbri(int idManifestacije) {
		Manifestacija m = manifestacije.get(idManifestacije);
		if (m != null) {
			m.setAktivna(true);
			sacuvajManifestacije();
		}
	}
}
