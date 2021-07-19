package beans;

public class PunctPoligon {
	private beans.LatLng punct;
	private int km;
	private boolean pointIn;

	public beans.LatLng getPunct() {
		return punct;
	}

	public void setPunct(beans.LatLng punct) {
		this.punct = punct;
	}

	public boolean isPointIn() {
		return pointIn;
	}

	public void setPointIn(boolean pointIn) {
		this.pointIn = pointIn;
	}

	public int getKm() {
		return km;
	}

	public void setKm(int km) {
		this.km = km;
	}

	@Override
	public String toString() {
		return "PunctPoligon [punct=" + punct + ", pointIn=" + pointIn + "]";
	}

}
