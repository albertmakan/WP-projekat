/*package services;

//import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;

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
	
	@POST
	@Path("/poster")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void uploadImage(
		    @FormDataParam("file") InputStream uploadedInputStream,
		    @FormDataParam("file") FormDataContentDisposition fileDetails) {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		System.out.println(fileDetails.getFileName());
		dao.dodajPoster(0, uploadedInputStream);
	}
	
	@PUT
	@Path("/promenaPodataka")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Manifestacija promenaPodataka(Manifestacija noviPodaci) {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		return dao.promenaPodataka(noviPodaci);
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
	@Path("/pretraga/naziv")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manifestacija> pretragaPoNazivu(@QueryParam("naziv") String tekst) {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		return dao.pretragaPoNazivu(tekst);
	}
	
	@GET
	@Path("/pretraga")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manifestacija> kombinovanaPretraga(@QueryParam("mesto") String mesto,
			@DefaultValue("0.0") @QueryParam("cenaod") float cenaOd,
			@DefaultValue("100000.0") @QueryParam("cenado") float cenaDo,
			@DefaultValue("01.01.2010") @QueryParam("datumod") String strDatumOd,
			@DefaultValue("31.12.2030") @QueryParam("datumdo") String strDatumDo) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		LocalDateTime datumOd = LocalDateTime.parse(strDatumOd+" 00:00", formatter);
		LocalDateTime datumDo = LocalDateTime.parse(strDatumDo+ "23:59", formatter);
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		return dao.kombinovanaPretraga(mesto, cenaOd, cenaDo, datumOd, datumDo);
	}
	
	@PUT
	@Path("/odobri")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odobriManifestaciju(Manifestacija m) {
		ManifestacijaDAO dao = (ManifestacijaDAO) ctx.getAttribute("manifestacijaDAO");
		dao.ododbri(m.getId());
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
	
	@PUT
	@Path("/odobriKomentar")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odobriKomentar(Komentar k) {
		KomentarDAO dao = (KomentarDAO) ctx.getAttribute("komentarDAO");
		dao.odobri(k);
	}
}
*/