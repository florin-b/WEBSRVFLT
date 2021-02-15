package beans;

public class BeanDateLocalitate {

	private int razaKm;
	private double lat;
	private double lon;

	public int getRazaKm() {
		return razaKm;
	}

	public void setRazaKm(int razaKm) {
		this.razaKm = razaKm;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "BeanDateLocalitate [razaKm=" + razaKm + ", lat=" + lat + ", lon=" + lon + "]";
	}
	
	

}
