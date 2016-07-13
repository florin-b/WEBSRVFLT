package utils;

import java.util.concurrent.TimeUnit;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import beans.StandardAddress;
import beans.CoordonateGps;

public class MapUtils {

	public static double distanceXtoY(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static CoordonateGps geocodeAddress(StandardAddress address) throws Exception {
		CoordonateGps coordonateGps = null;

		StringBuilder strAddress = new StringBuilder();

		if (address.getStreet() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getStreet());
			strAddress.append(",");
		}

		if (address.getNumber() != null && !address.getStreet().equals("")) {
			strAddress.append(address.getNumber());
			strAddress.append(",");
		}

		if (address.getSector() != null && !address.getSector().equals("")) {
			strAddress.append(address.getSector());
			strAddress.append(",");
		}

		if (address.getCity() != null && !address.getCity().equals("")) {
			strAddress.append(address.getCity());
			strAddress.append(",");
		}

		strAddress.append(address.getCountry());

		GeoApiContext context = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY);
		context.setRetryTimeout(0, TimeUnit.SECONDS);
		GeocodingResult[] results = GeocodingApi.geocode(context, strAddress.toString()).await();

		double latitude = results[0].geometry.location.lat;
		double longitude = results[0].geometry.location.lng;

		coordonateGps = new CoordonateGps(latitude, longitude);

		return coordonateGps;
	}

	public static String getCoordAddress(String codJudet, String localitate, String strada, String numar) {

		StringBuilder strAddress = new StringBuilder();

		double latitude = 0;
		double longitude = 0;

		if (strada != null && !strada.trim().equals("")) {
			strAddress.append(strada.trim());
			strAddress.append(",");
		}

		if (numar != null && !numar.trim().equals("")) {
			strAddress.append(numar.trim());
			strAddress.append(",");
		}

		if (codJudet != null && !codJudet.trim().equals("")) {
			strAddress.append(UtilsAdrese.getNumeJudet(codJudet.trim()));
			strAddress.append(",");
		}

		if (localitate != null && !localitate.trim().equals("")) {
			strAddress.append(localitate.trim());
			strAddress.append(",");
		}

		strAddress.append("Romania");

		GeoApiContext context = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY);
		context.setRetryTimeout(0, TimeUnit.SECONDS);
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context, strAddress.toString()).await();
		} catch (Exception e) {
			e.printStackTrace();
			latitude = -1;
			longitude = -1;
		}

		if (results.length > 0) {
			latitude = results[0].geometry.location.lat;
			longitude = results[0].geometry.location.lng;
		}

		return latitude + "," + longitude;
	}

}
