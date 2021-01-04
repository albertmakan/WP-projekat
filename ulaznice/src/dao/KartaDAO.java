package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import beans.Karta;
import beans.Karta.TipKarte;
import beans.Kupac;
import beans.Manifestacija;

public class KartaDAO {
	private HashMap<String, Karta> karte;
	private String putanjaFajla;
	
	public KartaDAO(String contextPath) {
		putanjaFajla = contextPath+"/data/karte.json";
		ucitajKarte();
	}

	private void ucitajKarte() {
		Jsonb jsonb = JsonbBuilder.create();		
		try (BufferedReader br = new BufferedReader(new FileReader(putanjaFajla))) {
			String line;
			while ((line = br.readLine()) != null) {
				Karta karta = jsonb.fromJson(line, Karta.class);
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
			Karta karta = new Karta(id, manifestacija.getId(), manifestacija.getDatumVreme(), cena, kupac.getIme()+" "+kupac.getPrezime(), false, tip);
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
		Jsonb jsonb = JsonbBuilder.create();	
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, true));
			out.println(jsonb.toJson(karta));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sacuvajKarte() {
		Jsonb jsonb = JsonbBuilder.create();	
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, false));
			for (Karta karta : karte.values())
				out.println(jsonb.toJson(karta));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
