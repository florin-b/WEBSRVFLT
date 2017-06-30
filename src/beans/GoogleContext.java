package beans;

import java.util.concurrent.TimeUnit;

import com.google.maps.GeoApiContext;

import utils.Constants;

public class GoogleContext {

	private static GeoApiContext instance;

	private static GeoApiContext instanceDist;

	private GoogleContext() {

	}

	public static GeoApiContext getContext() {
		if (instance == null) {
			instance = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY);
			instance.setQueryRateLimit(2);
			instance.setRetryTimeout(0, TimeUnit.SECONDS);
			instance.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance;
	}

	public static void destroyContext() {
		if (instance != null) {

			instance = null;

		}

	}

	public static GeoApiContext getContextDist() {
		if (instanceDist == null) {
			instanceDist = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST);
			instanceDist.setQueryRateLimit(2);
			instanceDist.setRetryTimeout(0, TimeUnit.SECONDS);
			instanceDist.setConnectTimeout(1, TimeUnit.SECONDS);
		}

		return instanceDist;
	}

}
