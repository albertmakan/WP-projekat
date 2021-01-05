package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import beans.Komentar;

public class KomentarDAO {
	private HashMap<Integer, ArrayList<Komentar>> komentari;
	private String putanjaFajla;
	
	public KomentarDAO(String contextPath) {
		komentari = new HashMap<Integer, ArrayList<Komentar>>();
		putanjaFajla = contextPath+"/data/komentari.csv";
		ucitajKomentare();
	}

	private void ucitajKomentare() {
		Jsonb jsonb = JsonbBuilder.create();
		try (BufferedReader br = new BufferedReader(new FileReader(putanjaFajla))) {
			String line;
			while ((line = br.readLine()) != null) {
				Komentar komentar = jsonb.fromJson(line, Komentar.class);
				if (komentari.get(komentar.getIdManifestacije()) == null)
					komentari.put(komentar.getIdManifestacije(), new ArrayList<Komentar>());
				komentari.get(komentar.getIdManifestacije()).add(komentar);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Komentar> getKomentari(int idManifestacije) {
		return komentari.get(idManifestacije);
	}
	
	public Komentar dodajKomentar(String kupac, int idManifestacije, String tekst, int ocena) {
		Komentar komentar = new Komentar(kupac, idManifestacije, tekst, ocena);
		komentari.get(idManifestacije).add(komentar);
		sacuvajKomentar(komentar);
		return komentar;
	}

	private void sacuvajKomentar(Komentar komentar) {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, true));
			out.println(jsonb.toJson(komentar));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sacuvajKomentare() {
		Jsonb jsonb = JsonbBuilder.create();	
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, false));
			for (int id : komentari.keySet())
				for (Komentar k : komentari.get(id))
					out.println(jsonb.toJson(k));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void odobri(Komentar k) {
		for (Komentar komentar : komentari.get(k.getIdManifestacije()))
			if (komentar.getKupac().equals(k.getKupac()))
				if (komentar.getOcena() == k.getOcena())
					if (komentar.getTekst().equals(k.getTekst())) {
						komentar.setOdobren(true);
						sacuvajKomentare();
						break;
					}
	}
}