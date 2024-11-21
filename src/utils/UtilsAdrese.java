package utils;

public class UtilsAdrese {

	public static String getNumeJudet(String codJudet) {
		String retVal = "Nedefinit";

		if (codJudet.equals("01"))
			retVal = "ALBA";

		if (codJudet.equals("02"))
			retVal = "ARAD";

		if (codJudet.equals("03"))
			retVal = "ARGES";

		if (codJudet.equals("04"))
			retVal = "BACAU";

		if (codJudet.equals("05"))
			retVal = "BIHOR";

		if (codJudet.equals("06"))
			retVal = "BISTRITA-NASAUD";

		if (codJudet.equals("07"))
			retVal = "BOTOSANI";

		if (codJudet.equals("09"))
			retVal = "BRAILA";

		if (codJudet.equals("08"))
			retVal = "BRASOV";

		if (codJudet.equals("40"))
			retVal = "BUCURESTI";

		if (codJudet.equals("10"))
			retVal = "BUZAU";

		if (codJudet.equals("51"))
			retVal = "CALARASI";

		if (codJudet.equals("11"))
			retVal = "CARAS-SEVERIN";

		if (codJudet.equals("12"))
			retVal = "CLUJ";

		if (codJudet.equals("13"))
			retVal = "CONSTANTA";

		if (codJudet.equals("14"))
			retVal = "COVASNA";

		if (codJudet.equals("15"))
			retVal = "DAMBOVITA";

		if (codJudet.equals("16"))
			retVal = "DOLJ";

		if (codJudet.equals("17"))
			retVal = "GALATI";

		if (codJudet.equals("52"))
			retVal = "GIURGIU";

		if (codJudet.equals("18"))
			retVal = "GORJ";

		if (codJudet.equals("19"))
			retVal = "HARGHITA";

		if (codJudet.equals("20"))
			retVal = "HUNEDOARA";

		if (codJudet.equals("21"))
			retVal = "IALOMITA";

		if (codJudet.equals("22"))
			retVal = "IASI";

		if (codJudet.equals("23"))
			retVal = "ILFOV";

		if (codJudet.equals("24"))
			retVal = "MARAMURES";

		if (codJudet.equals("25"))
			retVal = "MEHEDINTI";

		if (codJudet.equals("26"))
			retVal = "MURES";

		if (codJudet.equals("27"))
			retVal = "NEAMT";

		if (codJudet.equals("28"))
			retVal = "OLT";

		if (codJudet.equals("29"))
			retVal = "PRAHOVA";

		if (codJudet.equals("31"))
			retVal = "SALAJ";

		if (codJudet.equals("30"))
			retVal = "SATU-MARE";

		if (codJudet.equals("32"))
			retVal = "SIBIU";

		if (codJudet.equals("33"))
			retVal = "SUCEAVA";

		if (codJudet.equals("34"))
			retVal = "TELEORMAN";

		if (codJudet.equals("35"))
			retVal = "TIMIS";

		if (codJudet.equals("36"))
			retVal = "TULCEA";

		if (codJudet.equals("38"))
			retVal = "VALCEA";

		if (codJudet.equals("37"))
			retVal = "VASLUI";

		if (codJudet.equals("39"))
			retVal = "VRANCEA";

		return retVal;
	}
	
	
	
	
	public static String getNumeJudetHU(String codJudet) {
		String retVal = "Nedefinit";

		if (codJudet.equals("01"))
			retVal = "Bacs-Kiskun";

		if (codJudet.equals("02"))
			retVal = "Baranya";

		if (codJudet.equals("03"))
			retVal = "Bekes";

		if (codJudet.equals("04"))
			retVal = "Bekescsaba";

		if (codJudet.equals("05"))
			retVal = "Borsod-Abauj-Zemplen";

		if (codJudet.equals("06"))
			retVal = "Budapest";

		if (codJudet.equals("07"))
			retVal = "Csongrad";
		
		if (codJudet.equals("08"))
			retVal = "Debrecen";

		if (codJudet.equals("09"))
			retVal = "Dunaujvaros";

		if (codJudet.equals("10"))
			retVal = "Eger";

		if (codJudet.equals("11"))
			retVal = "Fejer";

		if (codJudet.equals("12"))
			retVal = "Gyor";

		if (codJudet.equals("13"))
			retVal = "Gyor-Moson-Sopron";

		if (codJudet.equals("14"))
			retVal = "Hajdu-Bihar";

		if (codJudet.equals("15"))
			retVal = "Heves";

		if (codJudet.equals("16"))
			retVal = "Hodmezovasarhely";

		if (codJudet.equals("17"))
			retVal = "Jasz-Nagykun-Szolnok";

		if (codJudet.equals("18"))
			retVal = "Kaposvar";

		if (codJudet.equals("19"))
			retVal = "Kecskemet";

		if (codJudet.equals("20"))
			retVal = "Komarom-Esztergom";

		if (codJudet.equals("21"))
			retVal = "Miskolc";

		if (codJudet.equals("22"))
			retVal = "Nagykanizsa";

		if (codJudet.equals("23"))
			retVal = "Nograd";

		if (codJudet.equals("24"))
			retVal = "Nyiregyhaza";

		if (codJudet.equals("25"))
			retVal = "Pecs";

		if (codJudet.equals("26"))
			retVal = "Pest";

		if (codJudet.equals("27"))
			retVal = "Somogy";

		if (codJudet.equals("28"))
			retVal = "Sopron";

		if (codJudet.equals("29"))
			retVal = "Szabolcs-Szat.-Bereg";
		
		if (codJudet.equals("30"))
			retVal = "Szeged";

		if (codJudet.equals("31"))
			retVal = "Szekesfehervar";

		if (codJudet.equals("32"))
			retVal = "Szolnok";

		if (codJudet.equals("33"))
			retVal = "Szombathely";

		if (codJudet.equals("34"))
			retVal = "Tatabanya";

		if (codJudet.equals("35"))
			retVal = "Tolna";

		if (codJudet.equals("36"))
			retVal = "Vas";

		if (codJudet.equals("37"))
			retVal = "Veszprem";

		if (codJudet.equals("38"))
			retVal = "Zala";

		if (codJudet.equals("39"))
			retVal = "Zalaegerszeg";

		return retVal;
	}
	
	public static String getNumeJudetSerbia(String codJudet) {
		String retVal = "Nedefinit";

		if (codJudet.equals("01"))
			retVal = "Belgrad";
		
		else if (codJudet.equals("02"))
			retVal = "Syrmia";
		
		return retVal;
		
	}
	
	
	
}
