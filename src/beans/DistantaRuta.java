package beans;

public class DistantaRuta {

	private int pozitie;
	private long distanta;

	public int getPozitie() {
		return pozitie;
	}

	public void setPozitie(int pozitie) {
		this.pozitie = pozitie;
	}

	public long getDistanta() {
		return distanta;
	}

	public void setDistanta(long distanta) {
		this.distanta = distanta;
	}

	@Override
	public String toString() {
		return "Ruta [poz=" + pozitie + ", distM=" + distanta + "]";
	}
	
	

}
