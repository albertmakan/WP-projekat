package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Korisnik;
import beans.Kupac;
import beans.Prodavac;
import dao.KorisnikDAO;

@Path("/korisnici")
public class KorisnikService {
	@Context
	ServletContext ctx;
	
	public KorisnikService() {
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("korisnikDAO") == null)
			ctx.setAttribute("korisnikDAO", new KorisnikDAO(ctx.getRealPath("")));
	}
	
	@GET
	@Path("/kupci/{korisnickoIme}")
	@Produces(MediaType.APPLICATION_JSON)
	public Kupac getKupac(@PathParam("korisnickoIme") String korIme) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		return (Kupac) dao.getKorisnik(korIme);
	}
	
	@GET
	@Path("/prodavac/{korisnickoIme}")
	@Produces(MediaType.APPLICATION_JSON)
	public Prodavac getProdavac(@PathParam("korisnickoIme") String korIme) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		return (Prodavac) dao.getKorisnik(korIme);
	}
	
	@GET
	@Path("/{korisnickoIme}")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik getKorisnik(@PathParam("korisnickoIme") String korIme) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		return dao.getKorisnik(korIme);
	}
	
	
}
