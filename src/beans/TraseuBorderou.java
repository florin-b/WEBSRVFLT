package beans;

public class TraseuBorderou {

	private String dataInreg;
	private double latitudine;
	private double longitudine;
	private int km;
	private int viteza;

	public TraseuBorderou() {

	}

	public String getDataInreg() {
		return dataInreg;
	}

	public void setDataInreg(String dataInreg) {
		this.dataInreg = dataInreg;
	}

	public double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public int getViteza() {
		return viteza;
	}

	public void setViteza(int viteza) {
		this.viteza = viteza;
	}

	public String toString() {
		return "dataInreg=" + dataInreg + ", latitudine=" + latitudine + ", longitudine=" + longitudine + ", km=" + km
				+ ", viteza=" + viteza + "";
	}

}
