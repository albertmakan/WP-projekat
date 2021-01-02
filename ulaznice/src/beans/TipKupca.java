package beans;

public class TipKupca {
	public enum ImeTipa {ZLATNI, SREBRNI, BRONZANI}
	private ImeTipa imeTipa;
	private int popust;
	private int trazeniBrojBodova;
	
	public TipKupca() {
	}

	public TipKupca(ImeTipa imeTipa, int popust, int trazeniBrojBodova) {
		this.imeTipa = imeTipa;
		this.popust = popust;
		this.trazeniBrojBodova = trazeniBrojBodova;
	}

	public ImeTipa getImeTipa() {
		return imeTipa;
	}

	public void setImeTipa(ImeTipa imeTipa) {
		this.imeTipa = imeTipa;
	}

	public int getPopust() {
		return popust;
	}

	public void setPopust(int popust) {
		this.popust = popust;
	}

	public int getTrazeniBrojBodova() {
		return trazeniBrojBodova;
	}

	public void setTrazeniBrojBodova(int trazeniBrojBodova) {
		this.trazeniBrojBodova = trazeniBrojBodova;
	}
	
}
