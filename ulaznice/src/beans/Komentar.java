package beans;

public class Komentar {
	private String kupac;	// korisnicko ime
	private int idManifestacije;
	private String tekst;
	private int ocena;
	private boolean odobren;
	
	public Komentar() {
	}
	
	public Komentar(String kupac, int idManifestacije, String tekst, int ocena) {
		this.kupac = kupac;
		this.idManifestacije = idManifestacije;
		this.tekst = tekst;
		this.ocena = ocena;
		this.odobren = false;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public int getIdManifestacije() {
		return idManifestacije;
	}

	public void setIdManifestacije(int idManifestacije) {
		this.idManifestacije = idManifestacije;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public boolean isOdobren() {
		return odobren;
	}

	public void setOdobren(boolean odobren) {
		this.odobren = odobren;
	}
	
}
