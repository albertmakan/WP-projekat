package beans;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;

public class Karta {
	public enum TipKarte {VIP, REGULAR, FAN_PIT}
	private String id;	// 10 karaktera
	private int idManifestacije;
	@JsonbDateFormat(JsonbDateFormat.TIME_IN_MILLIS)
	private LocalDateTime datumVreme;
	private float cena;
	private String kupac;	// ime i prezime
	private boolean odustanak;
	private TipKarte tipKarte;
	
	public Karta() {
	}

	public Karta(String id, int idManifestacije, LocalDateTime datumVreme, float cena, String kupac, boolean odustanak,
			TipKarte tipKarte) {
		this.id = id;
		this.idManifestacije = idManifestacije;
		this.datumVreme = datumVreme;
		this.cena = cena;
		this.kupac = kupac;
		this.odustanak = odustanak;
		this.tipKarte = tipKarte;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIdManifestacije() {
		return idManifestacije;
	}

	public void setIdManifestacije(int idManifestacije) {
		this.idManifestacije = idManifestacije;
	}

	public LocalDateTime getDatumVreme() {
		return datumVreme;
	}

	public void setDatumVreme(LocalDateTime datumVreme) {
		this.datumVreme = datumVreme;
	}

	public float getCena() {
		return cena;
	}

	public void setCena(float cena) {
		this.cena = cena;
	}

	public String getKupac() {
		return kupac;
	}

	public void setKupac(String kupac) {
		this.kupac = kupac;
	}

	public boolean isOdustanak() {
		return odustanak;
	}

	public void setOdustanak(boolean odustanak) {
		this.odustanak = odustanak;
	}

	public TipKarte getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(TipKarte tipKarte) {
		this.tipKarte = tipKarte;
	}
	
	
}
