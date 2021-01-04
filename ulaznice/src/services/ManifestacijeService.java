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

import beans.Komentar;
import beans.Kupac;
import beans.Manifestacija;
import beans.Prodavac;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.ManifestacijaDAO;

@Path("/manifestacije")
public class ManifestacijeService {
	@Context
	ServletContext ctx;
	
	public ManifestacijeService() {
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("manifestacijaDAO") == null)
			ctx.setAttribute("manifestacijaDAO", new ManifestacijaDAO(ctx.getRealPath("")));
		if (ctx.getAttribute("komentarDAO") == null)
			ctx.setAttribute("komentarDAO", new KomentarDAO(ctx.getRealPath("")));
	}
	
	@POST
	@Path("/nova")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestacija kreirajManifestaciju(@Context HttpServletRequest request, Manifestacija m) {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		KorisnikDAO korisnikDao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("korisnik");
		Manifestacija manifestacija = dao.kreirajManifestaciju(prodavac, m.getNaziv(), m.getTip(), m.getBrojMesta(), m.getDatumVreme(), m.getCenaKarte(), m.getLokacija());
		korisnikDao.sacuvajKorisnika(prodavac);
		return manifestacija;
	}
	
	@GET
	@Path("/moje")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manifestacija> getManifestacije(@Context HttpServletRequest request) {
		Prodavac prodavac = (Prodavac) request.getSession().getAttribute("korisnik");
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		return dao.getManifestacije(prodavac);
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manifestacija> getManifestacije() {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		return dao.getManifestacije();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestacija getManifestacija(@PathParam("id") int id) {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		return dao.getManifestacija(id);
	}
	
	@GET
	@Path("/{id}/komentari")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Komentar> getKomentare(@PathParam("id") int id) {
		KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
		return dao.getKomentari(id);
	}
	
	@POST
	@Path("/dodajKomentar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Komentar dodajKomentar(@Context HttpServletRequest request, Komentar k) {
		KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
		Kupac kupac = (Kupac) request.getSession().getAttribute("korisnik");
		return dao.dodajKomentar(kupac.getKorisnickoIme(), k.getIdManifestacije(), k.getTekst(), k.getOcena());
	}
}
