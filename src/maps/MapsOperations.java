package maps;

import java.util.List;

import beans.LatLng;

public class MapsOperations {

	public static boolean containsPoint(LatLng point, List<LatLng> polygon, boolean geodesic) {
		final int size = polygon.size();
		if (size == 0) {
			return false;
		}
		double lat3 = deg2rad(point.getLat());
		double lng3 = deg2rad(point.getLng());
		LatLng prev = polygon.get(size - 1);
		double lat1 = deg2rad(prev.getLat());
		double lng1 = deg2rad(prev.getLng());
		int nIntersect = 0;
		for (LatLng point2 : polygon) {
			double dLng3 = wrap(lng3 - lng1, -Math.PI, Math.PI);
			// Special case: point equal to vertex is inside.
			if (lat3 == lat1 && dLng3 == 0) {
				return true;
			}
			double lat2 = deg2rad(point2.getLat());
			double lng2 = deg2rad(point2.getLng());
			// Offset longitudes by -lng1.
			if (intersects(lat1, lat2, wrap(lng2 - lng1, -Math.PI, Math.PI), lat3, dLng3, geodesic)) {
				++nIntersect;
			}
			lat1 = lat2;
			lng1 = lng2;
		}
		return (nIntersect & 1) != 0;
	}

	static double wrap(double n, double min, double max) {
		return (n >= min && n < max) ? n : (mod(n - min, max - min) + min);
	}

	static double mod(double x, double m) {
		return ((x % m) + m) % m;
	}

	private static boolean intersects(double lat1, double lat2, double lng2, double lat3, double lng3,
			boolean geodesic) {

		if ((lng3 >= 0 && lng3 >= lng2) || (lng3 < 0 && lng3 < lng2)) {
			return false;
		}

		if (lat3 <= -Math.PI / 2) {
			return false;
		}

		if (lat1 <= -Math.PI / 2 || lat2 <= -Math.PI / 2 || lat1 >= Math.PI / 2 || lat2 >= Math.PI / 2) {
			return false;
		}
		if (lng2 <= -Math.PI) {
			return false;
		}
		double linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2;

		if (lat1 >= 0 && lat2 >= 0 && lat3 < linearLat) {
			return false;
		}

		if (lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat) {
			return true;
		}

		if (lat3 >= Math.PI / 2) {
			return true;
		}

		return geodesic ? Math.tan(lat3) >= tanLatGC(lat1, lat2, lng2, lng3)
				: mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3);
	}

	private static double tanLatGC(double lat1, double lat2, double lng2, double lng3) {
		return (Math.tan(lat1) * Math.sin(lng2 - lng3) + Math.tan(lat2) * Math.sin(lng3)) / Math.sin(lng2);
	}

	static double mercator(double lat) {
		return Math.log(Math.tan(lat * 0.5 + Math.PI / 4));
	}

	private static double mercatorLatRhumb(double lat1, double lat2, double lng2, double lng3) {
		return (mercator(lat1) * (lng2 - lng3) + mercator(lat2) * lng3) / lng2;
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

}
