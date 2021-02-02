package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;

import beans.Karta;
import beans.Karta.TipKarte;
import beans.Kupac;
import beans.Manifestacija;

public class KartaDAO {
	private HashMap<String, Karta> karte;
	private String putanjaFajla;
	
	public KartaDAO(String contextPath) {
		karte = new HashMap<String, Karta>();
		putanjaFajla = contextPath+"/data/karte.json";
		ucitajKarte();
	}

	private void ucitajKarte() {
		Gson gson = new Gson();
		try (BufferedReader br = new BufferedReader(new FileReader(putanjaFajla))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() < 3) continue;
				Karta karta = gson.fromJson(line, Karta.class);
				karte.put(karta.getId(), karta);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<Karta> getKarte(Kupac k) {
		ArrayList<Karta> karteKupca = new ArrayList<Karta>();
		for (String id : k.getSveKarte())
			karteKupca.add(karte.get(id));
		return karteKupca;
	}
	
	public Collection<Karta> getKarte(int idManifestacije) {
		ArrayList<Karta> karteManifestacije = new ArrayList<Karta>();
		for (Karta karta : karte.values())
			if (karta.getIdManifestacije() == idManifestacije)
				karteManifestacije.add(karta);
		return karteManifestacije;
	}
	
	public Karta getKarta(String idKarte) {
		return karte.get(idKarte);
	}
	
	public Collection<Karta> pretragaPoManifestaciji(Kupac kupac, String tekst) {
		ArrayList<Karta> rezultat = new ArrayList<Karta>();
		tekst = tekst.trim().toLowerCase();
		if (tekst.equals(""))
			return rezultat;
		for (String id : kupac.getSveKarte())
			if (karte.get(id).getNazivManifestacije().toLowerCase().contains(tekst))
				rezultat.add(karte.get(id));
		return rezultat;
	}
	
	public Collection<Karta> pretragaPoCeni(Kupac kupac, float cenaOd, float cenaDo) {
		ArrayList<Karta> rezultat = new ArrayList<Karta>();
		for (String id : kupac.getSveKarte()) {
			Karta karta = karte.get(id);
			if (karta.getCena() >= cenaOd && karta.getCena() <= cenaDo)
				rezultat.add(karta);
		}
		return rezultat;
	}
	
	public Collection<Karta> pretragaPoDatumu(Kupac kupac, Date datumOd, Date datumDo) {
		ArrayList<Karta> rezultat = new ArrayList<Karta>();
		for (String id : kupac.getSveKarte()) {
			Karta karta = karte.get(id);
			if (karta.getDatumVreme().after(datumOd) && karta.getDatumVreme().before(datumDo))
				rezultat.add(karta);
		}
		return rezultat;
	}
	
	// posle poziva ove metode potrebno je sacuvati kupca i manifestacije
	public void rezervacijaKarata(Kupac kupac, Manifestacija manifestacija, TipKarte tip, int kom) {
		int len = (""+manifestacija.getBrojMesta()).length();
		float cena = manifestacija.getCenaKarte();
		if (tip == TipKarte.FAN_PIT)
			cena *= 2;
		else if (tip == TipKarte.VIP)
			cena *= 4;
		cena -= kupac.getTip().getPopust()*cena;
		for (int i = 0; i < kom; i++) {
			String id = manifestacija.getNaziv().substring(0, 10-len).toUpperCase().replace(' ', '_')
					+ String.format("%0"+len+"d", manifestacija.getBrojMesta()-manifestacija.getBrojKarata());
			Karta karta = new Karta(id, manifestacija, cena, kupac.getIme()+" "+kupac.getPrezime(), tip);
			karte.put(karta.getId(), karta);
			kupac.addKarta(karta.getId());
			manifestacija.setBrojKarata(manifestacija.getBrojKarata()-1);
			sacuvajKartu(karta);
		}
		int brojBodova = (int) (kom*cena/1000*133);
		kupac.setBodovi(kupac.getBodovi()+brojBodova);
	}
	
	// posle poziva ove metode potrebno je sacuvati kupca
	public Karta odustanak(Kupac kupac, String idKarte) {
		Karta karta = karte.get(idKarte);
		karta.setOdustanak(true);
		int izgubljeniBodovi = (int) (karta.getCena()/1000*133*4);
		kupac.setBodovi(kupac.getBodovi()-izgubljeniBodovi);
		sacuvajKarte();
		return karta;
	}
	
	private void sacuvajKartu(Karta karta) {
		Gson gson = new Gson();	
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, true));
			out.println(gson.toJson(karta));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sacuvajKarte() {
		Gson gson = new Gson();	
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, false));
			for (Karta karta : karte.values())
				out.println(gson.toJson(karta));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
