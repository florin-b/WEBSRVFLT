package beans;

import java.util.concurrent.TimeUnit;

import com.google.maps.GeoApiContext;

import utils.Constants;

public class GoogleContext {

	private static GeoApiContext instance;

	private static GeoApiContext instanceDist;

	private static GeoApiContext instanceRevGeo;

	private static GeoApiContext instancePuncte;

	private static GeoApiContext instance1;
	private static GeoApiContext instance2;
	private static GeoApiContext instance3;
	private static GeoApiContext instance4;
	private static GeoApiContext instance5;
	private static GeoApiContext instance6;
	private static GeoApiContext instance7;

	private static GeoApiContext instancePuncte1;
	private static GeoApiContext instancePuncte2;
	private static GeoApiContext instancePuncte3;
	private static GeoApiContext instancePuncte4;
	private static GeoApiContext instancePuncte5;
	private static GeoApiContext instancePuncte6;
	private static GeoApiContext instancePuncte7;

	private static GeoApiContext instanceLocalitati1;
	private static GeoApiContext instanceLocalitati2;
	private static GeoApiContext instanceLocalitati3;

	private GoogleContext() {

	}

	public static GeoApiContext getContext(int contextNumber) {

		System.out.println("context number: " + contextNumber);

		switch (contextNumber) {
		case 1:
			return getContext1();
		case 2:
			return getContext2();
		case 3:
			return getContext3();
		case 4:
			return getContext4();
		case 5:
			return getContext5();
		case 6:
			return getContext6();
		case 7:
			return getContext7();
		default:
			return getContext1();
		}
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

	public static GeoApiContext getContextDistanta() {
		if (instance == null) {
			instance = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY);
			instance.setQueryRateLimit(2);
			instance.setRetryTimeout(0, TimeUnit.SECONDS);
			instance.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance;
	}

	public static GeoApiContext getContextPuncte() {
		if (instancePuncte == null) {
			instancePuncte = new GeoApiContext().setApiKey("AIzaSyDuZrxbUtw7A0w15dqinnxOUcmfieGe-Kk");
			instancePuncte.setQueryRateLimit(2);
			instancePuncte.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte;
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

	public static GeoApiContext getContextRevGeo() {
		if (instanceRevGeo == null) {
			instanceRevGeo = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_REV_GEO);
			instanceRevGeo.setQueryRateLimit(2);
			instanceRevGeo.setRetryTimeout(0, TimeUnit.SECONDS);
			instanceRevGeo.setConnectTimeout(1, TimeUnit.SECONDS);
		}

		return instanceRevGeo;
	}

	public static GeoApiContext getContext1() {
		if (instance1 == null) {
			instance1 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_01);
			instance1.setQueryRateLimit(2);
			instance1.setRetryTimeout(0, TimeUnit.SECONDS);
			instance1.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance1;
	}

	public static GeoApiContext getContext2() {
		if (instance2 == null) {
			instance2 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_02);
			instance2.setQueryRateLimit(2);
			instance2.setRetryTimeout(0, TimeUnit.SECONDS);
			instance2.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance2;
	}

	public static GeoApiContext getContext3() {
		if (instance3 == null) {
			instance3 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_03);
			instance3.setQueryRateLimit(2);
			instance3.setRetryTimeout(0, TimeUnit.SECONDS);
			instance3.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance3;
	}

	public static GeoApiContext getContext4() {
		if (instance4 == null) {
			instance4 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_04);
			instance4.setQueryRateLimit(2);
			instance4.setRetryTimeout(0, TimeUnit.SECONDS);
			instance4.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance4;
	}

	public static GeoApiContext getContext5() {
		if (instance5 == null) {
			instance5 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_05);
			instance5.setQueryRateLimit(2);
			instance5.setRetryTimeout(0, TimeUnit.SECONDS);
			instance5.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance5;
	}

	public static GeoApiContext getContext6() {
		if (instance6 == null) {
			instance6 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_06);
			instance6.setQueryRateLimit(2);
			instance6.setRetryTimeout(0, TimeUnit.SECONDS);
			instance6.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance6;
	}

	public static GeoApiContext getContext7() {
		if (instance7 == null) {
			instance7 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_07);
			instance7.setQueryRateLimit(2);
			instance7.setRetryTimeout(0, TimeUnit.SECONDS);
			instance7.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instance7;
	}

	public static GeoApiContext getContextPuncte(int contextNumber) {

		System.out.println("Context " + contextNumber);

		switch (contextNumber) {
		case 1:
			return getContextPuncte1();
		case 2:
			return getContextPuncte2();
		case 3:
			return getContextPuncte3();
		case 4:
			return getContextPuncte4();
		case 5:
			return getContextPuncte5();
		case 6:
			return getContextPuncte6();
		case 7:
		default:
			return getContextPuncte7();

		}
	}

	public static GeoApiContext getContextPuncte1() {
		if (instancePuncte1 == null) {
			instancePuncte1 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_01);
			instancePuncte1.setQueryRateLimit(2);
			instancePuncte1.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte1.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte1;
	}

	public static GeoApiContext getContextPuncte2() {
		if (instancePuncte2 == null) {
			instancePuncte2 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_02);
			instancePuncte2.setQueryRateLimit(2);
			instancePuncte2.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte2.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte2;
	}

	public static GeoApiContext getContextPuncte3() {
		if (instancePuncte3 == null) {
			instancePuncte3 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_03);
			instancePuncte3.setQueryRateLimit(2);
			instancePuncte3.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte3.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte3;
	}

	public static GeoApiContext getContextPuncte4() {
		if (instancePuncte4 == null) {
			instancePuncte4 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_04);
			instancePuncte4.setQueryRateLimit(2);
			instancePuncte4.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte4.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte4;
	}

	public static GeoApiContext getContextPuncte5() {
		if (instancePuncte5 == null) {
			instancePuncte5 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_05);
			instancePuncte5.setQueryRateLimit(2);
			instancePuncte5.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte5.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte5;
	}

	public static GeoApiContext getContextPuncte6() {
		if (instancePuncte6 == null) {
			instancePuncte6 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_06);
			instancePuncte6.setQueryRateLimit(2);
			instancePuncte6.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte6.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte6;
	}

	public static GeoApiContext getContextPuncte7() {
		if (instancePuncte7 == null) {
			instancePuncte7 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_07);
			instancePuncte7.setQueryRateLimit(2);
			instancePuncte7.setRetryTimeout(0, TimeUnit.SECONDS);
			instancePuncte7.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instancePuncte7;
	}

	public static GeoApiContext getContextLocalitati1() {
		if (instanceLocalitati1 == null) {
			instanceLocalitati1 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_LOC_01);
			instanceLocalitati1.setQueryRateLimit(2);
			instanceLocalitati1.setRetryTimeout(0, TimeUnit.SECONDS);
			instanceLocalitati1.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instanceLocalitati1;
	}

	
	public static GeoApiContext getContextLocalitati(int contextNumber) {

		System.out.println("Context " + contextNumber);

		switch (contextNumber) {
		case 1:
			return getContextLocalitati1();
		case 2:
			return getContextLocalitati2();
		case 3:
		default:
			return getContextLocalitati3();

		}
	}
	
	
	public static GeoApiContext getContextLocalitati2() {
		if (instanceLocalitati2 == null) {
			instanceLocalitati2 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_LOC_02);
			instanceLocalitati2.setQueryRateLimit(2);
			instanceLocalitati2.setRetryTimeout(0, TimeUnit.SECONDS);
			instanceLocalitati2.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instanceLocalitati2;
	}

	public static GeoApiContext getContextLocalitati3() {
		if (instanceLocalitati3 == null) {
			instanceLocalitati3 = new GeoApiContext().setApiKey(Constants.GOOGLE_MAPS_API_KEY_DIST_LOC_03);
			instanceLocalitati3.setQueryRateLimit(2);
			instanceLocalitati3.setRetryTimeout(0, TimeUnit.SECONDS);
			instanceLocalitati3.setConnectTimeout(1, TimeUnit.SECONDS);

		}

		return instanceLocalitati3;
	}

}
