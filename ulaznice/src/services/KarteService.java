package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Karta;
import beans.Kupac;
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
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> getKarte(@Context HttpServletRequest request) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.getKarte(kupac);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Karta> getKarte(@PathParam("id") int id) {
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		return dao.getKarte(id);
	}
	
	@POST
	@Path("/rezervacija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Karta rezervacijaKarte(@Context HttpServletRequest request, Karta k) {
		//TODO vise karata
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		KorisnikDAO korisnikDao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		ManifestacijaDAO manifDao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		Karta karta = dao.rezervacijaKarte(kupac, manifDao.getManifestacija(k.getIdManifestacije()), k.getTipKarte());
		manifDao.sacuvajManifestacije();
		korisnikDao.sacuvajKorisnika(kupac);
		return karta;
	}
	
	@POST
	@Path("/odustanak")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Karta odustanak(@Context HttpServletRequest request, Karta k) {
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		KartaDAO dao = (KartaDAO) ctx.getAttribute("kartaDAO");
		KorisnikDAO korisnikDao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		Karta karta = dao.odustanak(kupac, k.getId());
		korisnikDao.sacuvajKorisnika(kupac);
		return karta;
	}
}
