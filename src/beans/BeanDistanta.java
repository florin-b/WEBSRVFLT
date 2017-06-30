package beans;

public class BeanDistanta {

	private double kmGoogle;
	private double kmBord;

	public double getKmGoogle() {
		return kmGoogle;
	}

	public void setKmGoogle(double kmGoogle) {
		this.kmGoogle = kmGoogle;
	}

	public double getKmBord() {
		return kmBord;
	}

	public void setKmBord(double kmBord) {
		this.kmBord = kmBord;
	}

	@Override
	public String toString() {
		return "Distanta [kmGoogle=" + kmGoogle + ", kmBord=" + kmBord + "]";
	}

}
