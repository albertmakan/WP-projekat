package beans;

public class Lokacija {
	private float geoDuzina, geoSirina;
	private String naziv, adresa, mesto, postBroj;
	
	public Lokacija() {
	}
	
	public Lokacija(float geoDuzina, float geoSirina, String naziv, String adresa, String mesto, String postBroj) {
		super();
		this.geoDuzina = geoDuzina;
		this.geoSirina = geoSirina;
		this.naziv = naziv;
		this.adresa = adresa;
		this.mesto = mesto;
		this.postBroj = postBroj;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getMesto() {
		return mesto;
	}

	public void setMesto(String mesto) {
		this.mesto = mesto;
	}

	public String getPostBroj() {
		return postBroj;
	}

	public void setPostBroj(String postBroj) {
		this.postBroj = postBroj;
	}

	public float getGeoDuzina() {
		return geoDuzina;
	}

	public void setGeoDuzina(float geoDuzina) {
		this.geoDuzina = geoDuzina;
	}

	public float getGeoSirina() {
		return geoSirina;
	}

	public void setGeoSirina(float geoSirina) {
		this.geoSirina = geoSirina;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

}
