package beans;

public class Lokacija {
	private float geoDuzina, geoSirina;
	private String adresa;	// ulica i broj, mesto/grad, postanki broj
	
	public Lokacija() {
	}
	
	public Lokacija(float geoDuzina, float geoSirina, String adresa) {
		this.geoDuzina = geoDuzina;
		this.geoSirina = geoSirina;
		this.adresa = adresa;
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
