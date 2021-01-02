package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import beans.Komentar;

public class KomentarDAO {
	private HashMap<Integer, ArrayList<Komentar>> komentari;
	private String putanjaFajla;
	
	public KomentarDAO(String contextPath) {
		putanjaFajla = contextPath+"/data/komentari.json";
		ucitajKomentare();
	}

	private void ucitajKomentare() {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			komentari = jsonb.fromJson(new FileReader(putanjaFajla), new HashMap<Integer, ArrayList<Komentar>>(){
				private static final long serialVersionUID = 1L;}.getClass().getGenericSuperclass());
		} catch (JsonbException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Komentar> getKomentari(int idManifestacije) {
		return komentari.get(idManifestacije);
	}
	
	public Komentar dodajKomentar(String kupac, int idManifestacije, String tekst, int ocena) {
		Komentar komentar = new Komentar(kupac, idManifestacije, tekst, ocena);
		komentari.get(idManifestacije).add(komentar);
		sacuvajKomentare();
		return komentar;
	}

	private void sacuvajKomentare() {
		Jsonb jsonb = JsonbBuilder.create();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(putanjaFajla);
			pw.write(jsonb.toJson(komentari));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}
	
	
}