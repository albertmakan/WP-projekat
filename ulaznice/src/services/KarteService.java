/*package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Karta;
import beans.Kupac;
import beans.Stavka;
import dao.KartaDAO;
import dao.KorisnikDAO;
import dao.ManifestacijaDAO;

@Path("/karte")
public class KarteService {
	@Context
	ServletContext ctx;
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("kartaDAO") == null)
			ctx.setAttribute("kartaDAO", new KartaDAO(ctx.getRealPath("")));
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> getKarte(@Context HttpServletRequest request) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.getKarte(kupac);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> getKarte(@PathParam("id") int idManifestacije) {
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.getKarte(idManifestacije);
	}
	
	@GET
	@Path("/pretraga/manifestacija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> pretragaPoManifestaciji(@Context HttpServletRequest request,
			@QueryParam("naziv") String tekst) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.pretragaPoManifestaciji(kupac, tekst);
	}
	
	@GET
	@Path("/pretraga/cena")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> pretragaPoCeni(@Context HttpServletRequest request,
			@DefaultValue("0.0") @QueryParam("od") float cenaOd,
			@DefaultValue("100000.0") @QueryParam("do") float cenaDo) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.pretragaPoCeni(kupac, cenaOd, cenaDo);
	}
	
	@GET
	@Path("/pretraga/datum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> pretragaPoDatumu(@Context HttpServletRequest request,
			@DefaultValue("01.01.2010") @QueryParam("od") String strDatumOd,
			@DefaultValue("31.12.2030") @QueryParam("do") String strDatumDo) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		LocalDateTime datumOd = LocalDateTime.parse(strDatumOd+" 00:00", formatter);
		LocalDateTime datumDo = LocalDateTime.parse(strDatumDo+ "23:59", formatter);
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.pretragaPoDatumu(kupac, datumOd, datumDo);
	}
	
	@POST
	@Path("/rezervacija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void rezervacijaKarata(@Context HttpServletRequest request, ArrayList<Stavka> stavke) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		KorisnikDAO korisnikDao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		ManifestacijaDAO manifDao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		for (Stavka stavka : stavke) {
			dao.rezervacijaKarata(kupac, manifDao.getManifestacija(stavka.getIdManifestacije()), stavka.getTipKarte(), stavka.getKomada());
		}
		manifDao.sacuvajManifestacije();
		korisnikDao.azurirajTipKupca(kupac);
		korisnikDao.sacuvajKorisnika(kupac);
	}
	
	@PUT
	@Path("/odustanak")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Karta odustanak(@Context HttpServletRequest request, Karta k) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		KorisnikDAO korisnikDao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		Karta karta = dao.odustanak(kupac, k.getId());
		korisnikDao.azurirajTipKupca(kupac);
		korisnikDao.sacuvajKorisnika(kupac);
		return karta;
	}
}*/
