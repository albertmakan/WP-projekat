package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import com.google.gson.Gson;
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
		manifestacije = new HashMap<Integer, Manifestacija>();
		mesta = new HashMap<String, Integer>();
		tipovi = new HashSet<String>();
		putanjaFajla = contextPath + "/data/manifestacije.json";
		putanjaPostera = contextPath + "/data/posteri";
		ucitajManifestacije();
	}

	private void ucitajManifestacije() {
		Gson gson = new Gson();
		try {
			ArrayList<Manifestacija> sveManifestacije = gson.fromJson(new FileReader(putanjaFajla),
					new ArrayList<Manifestacija>() {
						private static final long serialVersionUID = 1L;
					}.getClass().getGenericSuperclass());
			for (Manifestacija manifestacija : sveManifestacije) {
				manifestacije.put(manifestacija.getId(), manifestacija);
				String mesto = manifestacija.getLokacija().getMesto();
				if (mesta.get(mesto) == null)
					mesta.put(mesto, 1);
				else
					mesta.put(mesto, mesta.get(mesto) + 1);
				tipovi.add(manifestacija.getTip());
			}
		} catch (FileNotFoundException e) {
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

	public Collection<Manifestacija> kombinovanaPretraga(String mesto, float cenaOd, float cenaDo,
			Date datumOd, Date datumDo) {
		ArrayList<Manifestacija> rezultat = new ArrayList<Manifestacija>();
		for (Manifestacija m : manifestacije.values()) {
			if (m.getLokacija().getMesto().contains(mesto)) {
				if (m.getCenaKarte() >= cenaOd && m.getCenaKarte() <= cenaDo) {
					if (m.getDatumVreme().after(datumOd) && m.getDatumVreme().before(datumDo)) {
						rezultat.add(m);
					}
				}
			}
		}
		return rezultat;
	}

	// posle poziva ove metode potrebno je sacuvati prodavca
	public Manifestacija kreirajManifestaciju(Prodavac prodavac, String naziv, String tip, int brojMesta,
			Date datumVreme, float cenaKarte, Lokacija lokacija) {
		int id = manifestacije.size();
		Manifestacija m = new Manifestacija(id, naziv, tip, brojMesta, datumVreme, cenaKarte, lokacija);
		manifestacije.put(m.getId(), m);
		String mesto = m.getLokacija().getMesto();
		if (mesta.get(mesto) == null)
			mesta.put(mesto, 1);
		else
			mesta.put(mesto, mesta.get(mesto) + 1);
		tipovi.add(m.getTip());
		prodavac.addManifestacija(m.getId());
		sacuvajManifestacije();
		return m;
	}

	// ako nije aktivna?
	public Manifestacija promenaPodataka(Manifestacija noviPodaci) {
		Manifestacija manifestacija = getManifestacija(noviPodaci.getId());
		if (noviPodaci.getNaziv() != null)
			manifestacija.setNaziv(noviPodaci.getNaziv());
		if (noviPodaci.getTip() != null)
			manifestacija.setTip(noviPodaci.getTip());
		if (noviPodaci.getBrojMesta() > 0) {
			manifestacija.setBrojMesta(noviPodaci.getBrojMesta());
			manifestacija.setBrojKarata(noviPodaci.getBrojMesta());
		}
		if (noviPodaci.getDatumVreme() != null)
			manifestacija.setDatumVreme(noviPodaci.getDatumVreme());
		if (noviPodaci.getCenaKarte() > 0)
			manifestacija.setCenaKarte(noviPodaci.getCenaKarte());
		if (noviPodaci.getLokacija() != null)
			manifestacija.setLokacija(noviPodaci.getLokacija());
		sacuvajManifestacije();
		return manifestacija;
	}
	
	public Collection<String> getTipovi() {
		return tipovi;
	}
	
	public Collection<String> getMesta() {
		return mesta.keySet();
	}

	public void sacuvajManifestacije() {
		Gson gson = new Gson();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(putanjaFajla);
			pw.write(gson.toJson(manifestacije.values()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}

	public String getPutanjaPostera() {
		return putanjaPostera;
	}

	public void odobri(int idManifestacije) {
		Manifestacija m = manifestacije.get(idManifestacije);
		if (m != null) {
			m.setAktivna(true);
			sacuvajManifestacije();
		}
	}

	public void dodajPoster(String id, InputStream is) {
		try {
			OutputStream out = new FileOutputStream(new File(putanjaPostera + "/" + id + ".png"));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = is.read(bytes)) != -1)
				out.write(bytes, 0, read);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
