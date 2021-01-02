package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;

import beans.Lokacija;
import beans.Manifestacija;

public class ManifestacijaDAO {
	private HashMap<Integer, Manifestacija> manifestacije;
	private String putanjaFajla;
	
	public ManifestacijaDAO(String contextPath) {
		putanjaFajla = contextPath+"/data/manifestacije.json";
		ucitajManifestacije();
	}

	private void ucitajManifestacije() {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			manifestacije = jsonb.fromJson(new FileReader(putanjaFajla), new HashMap<Integer, Manifestacija>(){
				private static final long serialVersionUID = 1L;}.getClass().getGenericSuperclass());
		} catch (JsonbException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Manifestacija getManifestacija(int id) {
		return manifestacije.get(id);
	}
	
	public Manifestacija kreirajManifestaciju(int id, String naziv, String tip, int brojMesta, LocalDateTime datumVreme, float cenaKarte,
			Lokacija lokacija) {
		Manifestacija m = new Manifestacija(id, naziv, tip, brojMesta, datumVreme, cenaKarte, lokacija);
		manifestacije.put(m.getId(), m);
		sacuvajManifestacije();
		return m;
	}
	
	private void sacuvajManifestacije() {
		Jsonb jsonb = JsonbBuilder.create();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(putanjaFajla);
			pw.write(jsonb.toJson(manifestacije));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}
	
	
}
