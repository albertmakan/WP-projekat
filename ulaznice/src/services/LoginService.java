/*package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Korisnik;
import beans.Kupac;
import dao.KorisnikDAO;

@Path("/user")
public class LoginService {
	@Context
	ServletContext ctx;
	
	public LoginService() {
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("korisnikDAO") == null)
			ctx.setAttribute("korisnikDAO", new KorisnikDAO(ctx.getRealPath("")));
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Korisnik korisnik, @Context HttpServletRequest request) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		if (!dao.postoji(korisnik.getKorisnickoIme(), korisnik.getLozinka()))
			return Response.status(400).entity("Invalid username and/or password").build();
		Korisnik ulogovanKorisnik = dao.getKorisnik(korisnik.getKorisnickoIme());
		if (ulogovanKorisnik.isBlokiran())
			return Response.status(400).entity("User blocked").build();
		request.getSession().setAttribute("korisnik", ulogovanKorisnik);
		return Response.status(200).build();
	}
	
	@POST
	@Path("/registracija")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registracija(Korisnik k, @Context HttpServletRequest request) {
		KorisnikDAO dao = (KorisnikDAO) ctx.getAttribute("korisnikDAO");
		Kupac kupac = dao.registracijaKupca(k.getIme(), k.getPrezime(), 
				k.getKorisnickoIme(), k.getLozinka(), k.getPol(), k.getDatumRodjenja());
		request.getSession().setAttribute("korisnik", kupac);
		return Response.status(200).build();
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}
	
	@GET
	@Path("/trenutniKorisnik")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik trenutniKorisnik(@Context HttpServletRequest request) {
		return (Korisnik) request.getSession().getAttribute("korisnik");
	}
}
*/