package rest;

import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import beans.Karta;
import beans.Komentar;
import beans.Korisnik;
import beans.Kupac;
import beans.Stavka;
import beans.Manifestacija;
import beans.Prodavac;
import dao.KartaDAO;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.ManifestacijaDAO;
import spark.Request;
import spark.Session;

public class SparkUlazniceMain {
	private static final String contextPath = "d:\\Peti_semestar\\Veb programiranje\\WP-projekat\\ulaznice\\WebContent";
	private static KartaDAO kartaDAO = new KartaDAO(contextPath);
	private static KomentarDAO komentarDAO = new KomentarDAO(contextPath);
	private static KorisnikDAO korisnikDAO = new KorisnikDAO(contextPath);
	private static ManifestacijaDAO manifestacijaDAO = new ManifestacijaDAO(contextPath);
	
	private static Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return new Date(json.getAsJsonPrimitive().getAsLong());
		}
	}).registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
		public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) {
			return new JsonPrimitive(date.getTime());
		}
	}).create();
	private static Gson filteredGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	private static boolean isEmptyOrNull(String str) {
		return str == null || str.trim().isEmpty();
	}

	public static void main(String[] args) throws IOException {
		port(8090);
		staticFiles.externalLocation(new File("./WebContent").getCanonicalPath());

		post("/login", (req, res) -> {
			Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			if (!korisnikDAO.postoji(k.getKorisnickoIme(), k.getLozinka()))
				return "Nevalidno korisnicko ime i/ili lozinka";
			Korisnik ulogovanKorisnik = korisnikDAO.getKorisnik(k.getKorisnickoIme());
			if (ulogovanKorisnik.isBlokiran())
				return "Korisnik je blokiran";
			req.session().attribute("korisnik", ulogovanKorisnik);
			return ulogovanKorisnik.getUloga().toString();
		});

		post("/registracija", (req, res) -> {
			Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			if (korisnikDAO.validnoKorisnickoIme(k.getKorisnickoIme())) {
				Kupac kupac = korisnikDAO.registracijaKupca(k.getIme(), k.getPrezime(), k.getKorisnickoIme(),
						k.getLozinka(), k.getPol(), k.getDatumRodjenja());
				req.session().attribute("korisnik", kupac);
				return "OK";
			}
			return "Vec postoji korisnik sa tim korisnickim imenom.";
		});

		get("/logout", (req, res) -> {
			req.session().invalidate();
			return true;
		});

		get("/trenutniKorisnik", (req, res) -> {
			res.type("application/json");
			return gson.toJson((Korisnik)req.session().attribute("korisnik"));
		});

		// --------------------------------------------------------------------------------
		
		get("/korisnici/", (req, res) -> {
			res.type("application/json");
			return filteredGson.toJson(korisnikDAO.getKorisnike());
		});

		get("/korisnici/:korisnickoIme", (req, res) -> {
			res.type("application/json");
			return gson.toJson(korisnikDAO.getKorisnik(req.params("korisnickoIme")));
		});

		get("/korisnici/pretraga/", (req, res) -> {
			res.type("application/json");
			String tekst = req.queryParams("ime");
			int kriterijum = Integer.parseInt(req.queryParams("krit"));
			return filteredGson.toJson(korisnikDAO.pretraga(tekst, kriterijum));
		});

		post("/korisnici/registracijaProdavca", (req, res) -> {
			res.type("application/json");
			Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			return gson.toJson(korisnikDAO.registracijaProdavca(k.getIme(), k.getPrezime(), k.getKorisnickoIme(),
					k.getLozinka(), k.getPol(), k.getDatumRodjenja()));
		});

		put("/korisnici/promenaPodataka", (req, res) -> {
			res.type("application/json");
			Korisnik noviPodaci = gson.fromJson(req.body(), Korisnik.class);
			return gson.toJson(korisnikDAO.promenaPodataka(noviPodaci));
		});

		get("/korisnici/sumnjivi", (req, res) -> {
			res.type("application/json");
			// TODO
			return null;
		});

		put("/korisnici/blokiranje", (req, res) -> {
			Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			korisnikDAO.blokirajKorisnika(k.getKorisnickoIme());
			return "OK";
		});

		// --------------------------------------------------------------------------------

		post("/manifestacije/nova", (req, res) -> {
			res.type("application/json");
			Prodavac prodavac = req.session().attribute("korisnik");
			Manifestacija m = gson.fromJson(req.body(), Manifestacija.class);
			Manifestacija manifestacija = manifestacijaDAO.kreirajManifestaciju(prodavac, m.getNaziv(), m.getTip(),
					m.getBrojMesta(), m.getDatumVreme(), m.getCenaKarte(), m.getLokacija());
			korisnikDAO.sacuvajKorisnika(prodavac);
			return gson.toJson(manifestacija);
		});

		post("/manifestacije/poster/:id", (req, res) -> {
			req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
			Part uploadaedfile = null;
			uploadaedfile = req.raw().getPart("file");
			InputStream is = uploadaedfile.getInputStream();
			manifestacijaDAO.dodajPoster(req.params("id"), is);
			return "success";
		});

		put("/manifestacije/promenaPodataka", (req, res) -> {
			res.type("application/json");
			Manifestacija noviPodaci = gson.fromJson(req.body(), Manifestacija.class);
			return gson.toJson(manifestacijaDAO.promenaPodataka(noviPodaci));
		});

		get("/manifestacije/moje", (req, res) -> {
			res.type("application/json");
			Prodavac prodavac = req.session().attribute("korisnik");
			return gson.toJson(manifestacijaDAO.getManifestacije(prodavac));
		});

		get("/manifestacije/", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.getManifestacije());
		});
		
		get("/manifestacije/tipovi", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.getTipovi().toArray());
		});
		
		get("/manifestacije/mesta", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.getMesta());
		});

		get("/manifestacije/:id", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.getManifestacija(Integer.parseInt(req.params("id"))));
		});

		get("/manifestacije/pretraga/naziv", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.pretragaPoNazivu(req.queryParams("naziv")));
		});

		get("/manifestacije/pretraga/komb", (req, res) -> {
			res.type("application/json");
			String mesto = req.queryParams("mesto")==null? "":req.queryParams("mesto");
			float cenaOd = isEmptyOrNull(req.queryParams("cenaod"))? 0:Float.parseFloat(req.queryParams("cenaod"));
			float cenaDo = isEmptyOrNull(req.queryParams("cenado"))? 100000:Float.parseFloat(req.queryParams("cenado"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date datumOd = isEmptyOrNull(req.queryParams("datumod"))? new Date(0L):formatter.parse(req.queryParams("datumod") + " 00:00");
			Date datumDo = isEmptyOrNull(req.queryParams("datumdo"))? new Date(1700000000000L):formatter.parse(req.queryParams("datumdo") + " 23:59");
			return gson.toJson(manifestacijaDAO.kombinovanaPretraga(mesto, cenaOd, cenaDo, datumOd, datumDo));
		});

		put("/manifestacije/odobri", (req, res) -> {
			Manifestacija m = gson.fromJson(req.body(), Manifestacija.class);
			manifestacijaDAO.odobri(m.getId());
			return "OK";
		});

		get("/manifestacije/:id/komentari", (req, res) -> {
			res.type("application/json");
			return gson.toJson(komentarDAO.getKomentari(Integer.parseInt(req.params("id"))));
		});

		post("/manifestacije/dodajKomentar", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			Komentar k = gson.fromJson(req.body(), Komentar.class);
			return gson.toJson(komentarDAO.dodajKomentar(kupac.getKorisnickoIme(), k.getIdManifestacije(), k.getTekst(),
					k.getOcena()));
		});

		put("/manifestacije/odobriKomentar", (req, res) -> {
			Komentar k = gson.fromJson(req.body(), Komentar.class);
			komentarDAO.odobri(k);
			return "OK";
		});

		// --------------------------------------------------------------------------------
		
		post("/dodajUKorpu", (req, res) -> {
			Stavka s = gson.fromJson(req.body(), Stavka.class);
			Kupac kupac = req.session().attribute("korisnik");
			if (kupac == null) return "Nije OK";
			mojaKorpa(req).add(new Stavka(manifestacijaDAO.getManifestacija(s.getIdManifestacije()), kupac, s.getTipKarte(), s.getKomada()));
			return "OK";
		});
		
		get("/isprazniKorpu", (req, res) -> {
			mojaKorpa(req).clear();
			return "OK";
		});
		
		get("/korpa", (req, res) -> {
			res.type("application/json");
			return gson.toJson(mojaKorpa(req));
		});

		// --------------------------------------------------------------------------------

		get("/karte/", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			return gson.toJson(kartaDAO.getKarte(kupac));
		});

		get("/karte/:id", (req, res) -> {
			res.type("application/json");
			return gson.toJson(kartaDAO.getKarte(Integer.parseInt(req.params("id"))));
		});

		get("/karte/pretraga/manifestacija", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			return gson.toJson(kartaDAO.pretragaPoManifestaciji(kupac, req.queryParams("naziv")));
		});

		get("/karte/pretraga/cena", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			float cenaOd = isEmptyOrNull(req.queryParams("cenaod"))? 0:Float.parseFloat(req.queryParams("cenaod"));
			float cenaDo = isEmptyOrNull(req.queryParams("cenado"))? 100000:Float.parseFloat(req.queryParams("cenado"));
			return gson.toJson(kartaDAO.pretragaPoCeni(kupac, cenaOd, cenaDo));
		});

		get("/karte/pretraga/datum", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date datumOd = isEmptyOrNull(req.queryParams("datumod"))? new Date(0L):formatter.parse(req.queryParams("datumod") + " 00:00");
			Date datumDo = isEmptyOrNull(req.queryParams("datumdo"))? new Date(1700000000000L):formatter.parse(req.queryParams("datumdo") + " 23:59");
			return gson.toJson(kartaDAO.pretragaPoDatumu(kupac, datumOd, datumDo));
		});

		post("/karte/rezervacija", (req, res) -> {
			Kupac kupac = req.session().attribute("korisnik");
			ArrayList<Stavka> stavke = gson.fromJson(req.body(), new ArrayList<Stavka>() {
				private static final long serialVersionUID = 1L;
			}.getClass().getGenericSuperclass());
			for (Stavka stavka : stavke) {
				kartaDAO.rezervacijaKarata(kupac, manifestacijaDAO.getManifestacija(stavka.getIdManifestacije()),
						stavka.getTipKarte(), stavka.getKomada());
			}
			manifestacijaDAO.sacuvajManifestacije();
			korisnikDAO.azurirajTipKupca(kupac);
			korisnikDAO.sacuvajKorisnika(kupac);
			mojaKorpa(req).clear();
			return "OK";
		});

		put("/karte/odustanak", (req, res) -> {
			res.type("application/json");
			Karta k = gson.fromJson(req.body(), Karta.class);
			Kupac kupac = req.session().attribute("korisnik");
			Karta karta = kartaDAO.odustanak(kupac, k.getId());
			korisnikDAO.azurirajTipKupca(kupac);
			korisnikDAO.sacuvajKorisnika(kupac);
			return gson.toJson(karta);
		});
	}
	
	private static ArrayList<Stavka> mojaKorpa(Request req) {
		Session ss = req.session(true);
		ArrayList<Stavka> korpa = ss.attribute("korpa"); 
		if (korpa == null) {
			korpa = new ArrayList<Stavka>();
			ss.attribute("korpa", korpa);
		}
		return korpa;
	}
}
