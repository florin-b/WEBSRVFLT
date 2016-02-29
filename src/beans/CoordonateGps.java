package beans;

public class CoordonateGps {

	private double latitude;
	private double longitude;

	public CoordonateGps() {

	}
	
	public CoordonateGps(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "CoordonateGps [latitude=" + latitude + ", longitude=" + longitude + "]";
	}

}
