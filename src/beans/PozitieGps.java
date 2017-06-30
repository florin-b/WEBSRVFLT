package beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import utils.UtilsFormatting;

public class PozitieGps {

	private String data;
	private double latitudine;
	private double longitudine;

	public PozitieGps() {

	}

	public PozitieGps(String data, double latitudine, double longitudine) {
		this.data = data;
		this.latitudine = latitudine;
		this.longitudine = longitudine;
	}

	public String getData() {
		if (data == null || data.isEmpty())
			return "";
		return UtilsFormatting.formatDateSmall(data);
	}

	public String getFormattedData() {
		return UtilsFormatting.formatDateAfis(getData());
	}

	public Date getDateObject() {

		Date date = null;
		try {
			date = new SimpleDateFormat("dd-MMM-yyyy HH:mm", new Locale("en")).parse(getData());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public void setData(String data) {
		this.data = data;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitudine);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitudine);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PozitieGps other = (PozitieGps) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (Double.doubleToLongBits(latitudine) != Double.doubleToLongBits(other.latitudine))
			return false;
		if (Double.doubleToLongBits(longitudine) != Double.doubleToLongBits(other.longitudine))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocatieGps [data=" + UtilsFormatting.formatDateSmall(data) + ", latitudine=" + latitudine + ", longitudine=" + longitudine + "]";
	}

}
