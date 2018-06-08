package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.OverQueryLimitException;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import beans.CoordonateGps;
import beans.DistantaRuta;
import beans.GoogleContext;
import beans.StandardAddress;
import model.AdreseService;

public class MapUtils {

	private static final Logger logger = LogManager.getLogger(MapUtils.class);

	private static final int MAX_KEYS = 7;
	private static final int MAX_KEYS_PUNCTE = 7;

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
		CoordonateGps coordonateGps;

		double latitude = 0;
		double longitude = 0;

		try {

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

			GeoApiContext context = GoogleContext.getContext();

			GeocodingResult[] results = GeocodingApi.geocode(context, strAddress.toString()).await();

			if (results.length > 0) {
				latitude = results[0].geometry.location.lat;
				longitude = results[0].geometry.location.lng;
			}

		} catch (OverQueryLimitException q) {
			latitude = 0;
			longitude = 0;

		} catch (Exception e) {
			latitude = 0;
			longitude = 0;
			logger.error(Utils.getStackTrace(e, ""));
		}

		coordonateGps = new CoordonateGps(latitude, longitude);

		return coordonateGps;
	}

	public static String getCoordLocalitateFromService(String codJudet, String localitate, String strada, String numar) {

		StringBuilder strAddress;

		double latitude = 0;
		double longitude = 0;

		Random rand = new Random(System.currentTimeMillis());
		int value = rand.nextInt((MAX_KEYS - 1) + 1) + 1;

		strAddress = new StringBuilder();
		strAddress.append("Romania, ");

		if (codJudet != null && !codJudet.trim().equals("")) {
			strAddress.append(UtilsAdrese.getNumeJudet(codJudet.trim()));
		}

		if (localitate != null && !localitate.trim().equals("")) {
			strAddress.append(", ");
			strAddress.append(localitate.trim());
		}

		if (new AdreseService().isOras(codJudet, localitate, "")) {
			if (strada != null && !strada.trim().equals("")) {
				strAddress.append(", ");
				strAddress.append(strada.trim());

			}

			if (numar != null && !numar.trim().equals("")) {
				strAddress.append(", ");
				strAddress.append(numar.trim());
			}
		}

		GeoApiContext context = GoogleContext.getContext(value);

		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context, strAddress.toString()).await();

		} catch (OverQueryLimitException q) {
			latitude = -1;
			longitude = -1;
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e, strAddress.toString()));
			latitude = -1;
			longitude = -1;
			return e.toString();

		}

		if (results.length > 0) {
			latitude = results[0].geometry.location.lat;
			longitude = results[0].geometry.location.lng;
		}

		return latitude + "," + longitude;
	}

	public static String getCoordAddressFromService(String codJudet, String localitate, String strada, String numar) {

		StringBuilder strAddress;

		double latitude = 0;
		double longitude = 0;

		Random rand = new Random(System.currentTimeMillis());
		int value = rand.nextInt((MAX_KEYS - 1) + 1) + 1;

		strAddress = new StringBuilder();
		strAddress.append("Romania, ");

		if (codJudet != null && !codJudet.trim().equals("")) {
			strAddress.append(UtilsAdrese.getNumeJudet(codJudet.trim()));
		}

		if (localitate != null && !localitate.trim().equals("")) {
			strAddress.append(", ");
			strAddress.append(localitate.trim());
		}

		if (strada != null && !strada.trim().equals("")) {
			strAddress.append(", ");
			strAddress.append(strada.trim());

		}

		if (numar != null && !numar.trim().equals("")) {
			strAddress.append(", ");
			strAddress.append(numar.trim());
		}

		GeoApiContext context = GoogleContext.getContext(value);

		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context, strAddress.toString()).await();

		} catch (OverQueryLimitException q) {
			latitude = -1;
			longitude = -1;
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e, strAddress.toString()));
			latitude = -1;
			longitude = -1;
			return e.toString();

		}

		if (results.length > 0) {
			latitude = results[0].geometry.location.lat;
			longitude = results[0].geometry.location.lng;
		}

		return latitude + "," + longitude;
	}

	public static List<DistantaRuta> getDistantaPuncte(String strCoordonate) {

		List<DistantaRuta> listDistante = new ArrayList<>();

		Random rand = new Random(System.currentTimeMillis());
		int value = rand.nextInt((MAX_KEYS_PUNCTE - 1) + 1) + 1;

		if (strCoordonate == null)
			return listDistante;

		if (strCoordonate.isEmpty())
			return listDistante;

		if (!strCoordonate.contains(":"))
			return listDistante;

		if (!strCoordonate.contains("-"))
			return listDistante;

		String[] arrayCoords = strCoordonate.split("-");

		List<String> wayPoints = new ArrayList<>();

		DirectionsRoute[] routes = null;

		try {

			for (int i = 1; i < arrayCoords.length - 1; i++) {

				wayPoints.add(arrayCoords[i].replace(":", ","));

			}

			String[] arrayPoints = wayPoints.toArray(new String[wayPoints.size()]);

			GeoApiContext context = GoogleContext.getContextPuncte(value);

			LatLng start = new LatLng(Double.parseDouble(arrayCoords[0].split(":")[0]), Double.parseDouble(arrayCoords[0].split(":")[1]));

			LatLng stop = new LatLng(Double.parseDouble(arrayCoords[arrayCoords.length - 1].split(":")[0]),
					Double.parseDouble(arrayCoords[arrayCoords.length - 1].split(":")[1]));

			routes = DirectionsApi.newRequest(context).mode(TravelMode.DRIVING).origin(start).destination(stop).waypoints(arrayPoints).mode(TravelMode.DRIVING)
					.optimizeWaypoints(false).await();

			for (int i = 0; i < routes[0].legs.length; i++) {
				DistantaRuta distanta = new DistantaRuta();
				distanta.setPozitie(i);
				distanta.setDistanta(routes[0].legs[i].distance.inMeters);
				listDistante.add(distanta);

			}

		} catch (OverQueryLimitException q) {
		} catch (Exception ex) {
			MailOperations.sendMail("traseuBorderou: " + ex.toString());
		}

		return listDistante;

	}

	public static int getGoogleDistance(LatLng startPoint, LatLng stopPoint) throws Exception {

		DistanceMatrix req;

		int dist = 0;

		try {
			GeoApiContext context = GoogleContext.getContext();

			req = DistanceMatrixApi.newRequest(context).origins(startPoint).destinations(stopPoint).await();

			dist = (int) req.rows[0].elements[0].distance.inMeters;
		} catch (OverQueryLimitException q) {
		} catch (Exception ex) {
			MailOperations.sendMail("traseuBorderou: " + ex.toString());
		}

		return dist;

	}

	public static String getAdresaCoordonate(double lat, double lng) {

		String adresa = "";

		GeoApiContext context = GoogleContext.getContextRevGeo();

		try {
			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(lat, lng)).await();

			String localitate = "";
			String judet = "";

			for (int j = 0; j < results[0].addressComponents.length; j++) {

				AddressComponentType[] adrComponentType = results[0].addressComponents[j].types;

				for (int k = 0; k < adrComponentType.length; k++) {
					if (adrComponentType[k] == AddressComponentType.LOCALITY || adrComponentType[k] == AddressComponentType.SUBLOCALITY) {
						localitate = Utils.flattenToAscii(results[0].addressComponents[j].shortName);
						break;
					}

					if (adrComponentType[k] == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1) {
						judet = Utils.flattenToAscii(results[0].addressComponents[j].shortName);
						break;
					}

				}

				if (!judet.isEmpty()) {
					adresa = localitate + " / " + judet;

				}

			}

		} catch (Exception e) {
			MailOperations.sendMail("FlotaWS" + e.toString());
			adresa = e.toString();
		}

		return adresa;
	}

	public static boolean containsLocation(LatLng point, List<LatLng> polygon, boolean geodesic) {
		final int size = polygon.size();
		if (size == 0) {
			return false;
		}
		double lat3 = deg2rad(point.lat);
		double lng3 = deg2rad(point.lng);
		LatLng prev = polygon.get(size - 1);
		double lat1 = deg2rad(prev.lat);
		double lng1 = deg2rad(prev.lng);
		int nIntersect = 0;
		for (LatLng point2 : polygon) {
			double dLng3 = wrap(lng3 - lng1, -Math.PI, Math.PI);
			// Special case: point equal to vertex is inside.
			if (lat3 == lat1 && dLng3 == 0) {
				return true;
			}
			double lat2 = deg2rad(point2.lat);
			double lng2 = deg2rad(point2.lng);
			// Offset longitudes by -lng1.
			if (intersects(lat1, lat2, wrap(lng2 - lng1, -Math.PI, Math.PI), lat3, dLng3, geodesic)) {
				++nIntersect;
			}
			lat1 = lat2;
			lng1 = lng2;
		}
		return (nIntersect & 1) != 0;
	}

	/**
	 * Wraps the given value into the inclusive-exclusive interval between min
	 * and max.
	 * 
	 * @param n
	 *            The value to wrap.
	 * @param min
	 *            The minimum.
	 * @param max
	 *            The maximum.
	 */
	static double wrap(double n, double min, double max) {
		return (n >= min && n < max) ? n : (mod(n - min, max - min) + min);
	}

	/**
	 * Returns the non-negative remainder of x / m.
	 * 
	 * @param x
	 *            The operand.
	 * @param m
	 *            The modulus.
	 */
	static double mod(double x, double m) {
		return ((x % m) + m) % m;
	}

	/**
	 * Computes whether the vertical segment (lat3, lng3) to South Pole
	 * intersects the segment (lat1, lng1) to (lat2, lng2). Longitudes are
	 * offset by -lng1; the implicit lng1 becomes 0.
	 */
	private static boolean intersects(double lat1, double lat2, double lng2, double lat3, double lng3, boolean geodesic) {
		// Both ends on the same side of lng3.
		if ((lng3 >= 0 && lng3 >= lng2) || (lng3 < 0 && lng3 < lng2)) {
			return false;
		}
		// Point is South Pole.
		if (lat3 <= -Math.PI / 2) {
			return false;
		}
		// Any segment end is a pole.
		if (lat1 <= -Math.PI / 2 || lat2 <= -Math.PI / 2 || lat1 >= Math.PI / 2 || lat2 >= Math.PI / 2) {
			return false;
		}
		if (lng2 <= -Math.PI) {
			return false;
		}
		double linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2;
		// Northern hemisphere and point under lat-lng line.
		if (lat1 >= 0 && lat2 >= 0 && lat3 < linearLat) {
			return false;
		}
		// Southern hemisphere and point above lat-lng line.
		if (lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat) {
			return true;
		}
		// North Pole.
		if (lat3 >= Math.PI / 2) {
			return true;
		}
		// Compare lat3 with latitude on the GC/Rhumb segment corresponding to
		// lng3.
		// Compare through a strictly-increasing function (tan() or mercator())
		// as convenient.
		return geodesic ? Math.tan(lat3) >= tanLatGC(lat1, lat2, lng2, lng3) : mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3);
	}

	/**
	 * Returns tan(latitude-at-lng3) on the great circle (lat1, lng1) to (lat2,
	 * lng2). lng1==0. See http://williams.best.vwh.net/avform.htm .
	 */
	private static double tanLatGC(double lat1, double lat2, double lng2, double lng3) {
		return (Math.tan(lat1) * Math.sin(lng2 - lng3) + Math.tan(lat2) * Math.sin(lng3)) / Math.sin(lng2);
	}

	/**
	 * Returns mercator Y corresponding to latitude. See
	 * http://en.wikipedia.org/wiki/Mercator_projection .
	 */
	static double mercator(double lat) {
		return Math.log(Math.tan(lat * 0.5 + Math.PI / 4));
	}

	/**
	 * Returns mercator(latitude-at-lng3) on the Rhumb line (lat1, lng1) to
	 * (lat2, lng2). lng1==0.
	 */
	private static double mercatorLatRhumb(double lat1, double lat2, double lng2, double lng3) {
		return (mercator(lat1) * (lng2 - lng3) + mercator(lat2) * lng3) / lng2;
	}

}
