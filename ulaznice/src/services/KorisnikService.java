/*package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
	
	@GET
	@Path("/pretraga")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Korisnik> pretraga(@QueryParam("ime") String tekst, @QueryParam("krit") int kriterijum) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		return dao.pretraga(tekst, kriterijum);
	}
	
	@POST
	@Path("/registracijaProdavca")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Prodavac registracija(Korisnik k) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		return dao.registracijaProdavca(k.getIme(), k.getPrezime(), k.getKorisnickoIme(), k.getLozinka(), k.getPol(), k.getDatumRodjenja());
	}
	
	@PUT
	@Path("/promenaPodataka")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik promenaPodataka(Korisnik noviPodaci) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		return dao.promenaPodataka(noviPodaci);
	}
	
	@GET
	@Path("/sumnjivi")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kupac> sumnjivi() {
		//TODO
		return null;
	}
	
	@PUT
	@Path("/blokiranje")
	@Consumes(MediaType.APPLICATION_JSON)
	public void blokiraj(Korisnik k) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		dao.blokirajKorisnika(k.getKorisnickoIme());
	}
	
}*/
