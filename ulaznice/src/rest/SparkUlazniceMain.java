package rest;

import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import com.google.gson.Gson;

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

public class SparkUlazniceMain {
	private static final String contextPath = "d:\\Peti_semestar\\Veb programiranje\\WP-projekat\\ulaznice\\WebContent";
	private static KartaDAO kartaDAO = new KartaDAO(contextPath);
	private static KomentarDAO komentarDAO = new KomentarDAO(contextPath);
	private static KorisnikDAO korisnikDAO = new KorisnikDAO(contextPath);
	private static ManifestacijaDAO manifestacijaDAO = new ManifestacijaDAO(contextPath);
	private static Gson gson = new Gson();

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
			res.redirect("");// TODO
			return "OK";
		});

		post("/registracija", (req, res) -> {
			Korisnik k = gson.fromJson(req.body(), Korisnik.class);
			Kupac kupac = korisnikDAO.registracijaKupca(k.getIme(), k.getPrezime(), k.getKorisnickoIme(),
					k.getLozinka(), k.getPol(), k.getDatumRodjenja());
			req.session().attribute("korisnik", kupac);
			res.redirect("");// TODO
			return "OK";
		});

		post("/logout", (req, res) -> {
			req.session().invalidate();
			return "OK";
		});

		get("/trenutniKorisnik", (req, res) -> {
			res.type("application/json");
			return gson.toJson(req.session().attribute("korisnik"));
		});

		// --------------------------------------------------------------------------------

		get("/korisnici/:korisnickoIme", (req, res) -> {
			res.type("application/json");
			return gson.toJson(korisnikDAO.getKorisnik(req.params("korisnickoIme")));
		});

		get("/korisnici/pretraga", (req, res) -> {
			res.type("application/json");
			String tekst = req.queryParams("ime");
			int kriterijum = Integer.parseInt(req.queryParams("krit"));
			return gson.toJson(korisnikDAO.pretraga(tekst, kriterijum));
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
			uploadaedfile = req.raw().getPart("myfile");
			InputStream is = uploadaedfile.getInputStream();
			manifestacijaDAO.dodajPoster(req.queryParams("id"), is);
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

		get("/manifestacije/:id", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.getManifestacija(Integer.parseInt(req.queryParams("id"))));
		});

		get("/manifestacije/pretraga/naziv", (req, res) -> {
			res.type("application/json");
			return gson.toJson(manifestacijaDAO.pretragaPoNazivu(req.queryParams("naziv")));
		});

		get("/manifestacije/pretraga", (req, res) -> {
			res.type("application/json");
			String mesto = req.queryParams("mesto");
			float cenaOd = Float.parseFloat(req.queryParams("cenaod"));
			float cenaDo = Float.parseFloat(req.queryParams("cenado"));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
			LocalDateTime datumOd = LocalDateTime.parse(req.queryParams("datumod") + " 00:00", formatter);
			LocalDateTime datumDo = LocalDateTime.parse(req.queryParams("datumdo") + "23:59", formatter);
			return gson.toJson(manifestacijaDAO.kombinovanaPretraga(mesto, cenaOd, cenaDo, datumOd, datumDo));
		});

		put("/manifestacije/odobri", (req, res) -> {
			Manifestacija m = gson.fromJson(req.body(), Manifestacija.class);
			manifestacijaDAO.odobri(m.getId());
			return "OK";
		});

		get("/manifestacije/:id/komentari", (req, res) -> {
			res.type("application/json");
			return gson.toJson(komentarDAO.getKomentari(Integer.parseInt(req.queryParams("id"))));
		});

		post("/manifestacije/dodajKomentar", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.attribute("korisnik");
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

		get("/karte/", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			return gson.toJson(kartaDAO.getKarte(kupac));
		});

		get("/karte/:id", (req, res) -> {
			res.type("application/json");
			return gson.toJson(kartaDAO.getKarte(Integer.parseInt(req.queryParams("id"))));
		});

		get("/karte/pretraga/manifestacija", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			return gson.toJson(kartaDAO.pretragaPoManifestaciji(kupac, req.queryParams("naziv")));
		});

		get("/karte/pretraga/cena", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			float cenaOd = Float.parseFloat(req.queryParams("cenaod"));
			float cenaDo = Float.parseFloat(req.queryParams("cenado"));
			return gson.toJson(kartaDAO.pretragaPoCeni(kupac, cenaOd, cenaDo));
		});

		get("/karte/pretraga/datum", (req, res) -> {
			res.type("application/json");
			Kupac kupac = req.session().attribute("korisnik");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
			LocalDateTime datumOd = LocalDateTime.parse(req.queryParams("datumod") + " 00:00", formatter);
			LocalDateTime datumDo = LocalDateTime.parse(req.queryParams("datumdo") + "23:59", formatter);
			return gson.toJson(kartaDAO.pretragaPoDatumu(kupac, datumOd, datumDo));
		});

		post("/karte/rezervacija", (req, res) -> {
			Kupac kupac = req.session().attribute("korisnik");
			ArrayList<Stavka> stavke = gson.fromJson(req.body(), new ArrayList<Manifestacija>() {
				private static final long serialVersionUID = 1L;
			}.getClass().getGenericSuperclass());
			for (Stavka stavka : stavke) {
				kartaDAO.rezervacijaKarata(kupac, manifestacijaDAO.getManifestacija(stavka.getIdManifestacije()),
						stavka.getTipKarte(), stavka.getKomada());
			}
			manifestacijaDAO.sacuvajManifestacije();
			korisnikDAO.azurirajTipKupca(kupac);
			korisnikDAO.sacuvajKorisnika(kupac);
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
}