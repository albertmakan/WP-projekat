package beans;

public class Komentar {
	private String kupac;	// ime i prezime
	private int idManifestacije;
	private String tekst;
	private int ocena;
	
	public Komentar() {
	}
	
	public Komentar(String kupac, int idManifestacije, String tekst, int ocena) {
		this.kupac = kupac;
		this.idManifestacije = idManifestacije;
		this.tekst = tekst;
		this.ocena = ocena;
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
	
}
