package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

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
		try (BufferedReader br = new BufferedReader(new FileReader(putanjaFajla))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(";");
				Komentar komentar = new Komentar(tokens[0], Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]));
				komentar.setOdobren(tokens[4].equals("o"));
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

	private void sacuvajKomentar(Komentar k) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(putanjaFajla, true));
			out.println(k.getKupac()+';'+k.getIdManifestacije()+';'+k.getTekst()+';'+k.getOcena()+(k.isOdobren()? ";o":";n"));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}