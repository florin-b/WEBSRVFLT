package enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumJudete {

	ALBA("ALBA", "01", "AB"), ARAD("ARAD", "02", "AR"), ARGES("ARGES", "03", "AG"), BACAU("BACAU", "04", "BC"), BIHOR("BIHOR", "05", "BH"), BISTRITA(
			"BISTRITA-NASAUD", "06", "BN"), BOTOSANI("BOTOSANI", "07", "BT"), BRAILA("BRAILA", "09", "BR"), BRASOV("BRASOV", "08", "BV"), BUCURESTI("BUCURESTI",
					"40", "B"), BUZAU("BUZAU", "10", "BZ"), CALARASI("CALARASI", "51", "CL"), CARAS("CARAS-SEVERIN", "11", "CS"), CLUJ("CLUJ", "12",
							"CJ"), CONSTANTA("CONSTANTA", "13", "CT"), COVASNA("COVASNA", "14", "CV"), DAMBOVITA("DAMBOVITA", "15", "DB"), DOLJ("DOLJ", "16",
									"DJ"), GALATI("GALATI", "17", "GL"), GIURGIU("GIURGIU", "52", "GR"), GORJ("GORJ", "18", "GJ"), HARGHITA("HARGHITA", "19",
											"HR"), HUNEDOARA("HUNEDOARA", "20", "HG"), IALOMITA("IALOMITA", "21", "IL"), IASI("IASI", "22", "IS"), ILFOV(
													"ILFOV", "23", "IF"), MARAMURES("MARAMURES", "24", "MM"), MEHEDINTI("MEHEDINTI", "25", "MH"), MURES("MURES",
															"26", "MS"), NEAMT("NEAMT", "27", "NT"), OLT("OLT", "28", "OT"), PRAHOVA("PRAHOVA", "29",
																	"PH"), SALAJ("SALAJ", "31", "SJ"), SATU_MARE("SATU-MARE", "30", "SM"), SIBIU("SIBIU", "32",
																			"SM"), SUCEAVA("SUCEAVA", "33", "SV"), TELEORMAN("TELEORMAN", "34", "TR"), TIMIS(
																					"TIMIS", "35", "TM"), TULCEA("TULCEA", "36", "TL"), VALCEA("VALCEA", "38",
																							"VL"), VASLUI("VASLUI", "37", "VS"), VRANCEA("VRANCEA", "39", "VN");

	private String nume;
	private String cod;
	private String abr;

	EnumJudete(String nume, String cod, String abr) {
		this.nume = nume;
		this.cod = cod;
		this.abr = abr;
	}

	public String toString() {
		return nume;
	}

	public String getCod() {
		return cod;
	}

	public String getNume() {
		return nume;
	}

	public static List<String> getRegionNames() {
		List<String> listValues = new ArrayList<String>();

		for (EnumJudete enumJ : EnumJudete.values())
			listValues.add(enumJ.nume);

		return listValues;
	}

	public static String getCodJudet(String numeJudet) {
		for (EnumJudete enumJ : EnumJudete.values()) {
			if (enumJ.nume.equals(numeJudet))
				return enumJ.cod;

		}
		return "";
	}

	public static String getNumeJudet(int codJudet) {
		for (EnumJudete enumJ : EnumJudete.values()) {
			if (Integer.valueOf(enumJ.cod) == codJudet)
				return enumJ.nume;

		}
		return "";
	}

	public static String getNumeJudet(String abr) {
		for (EnumJudete enumJ : EnumJudete.values()) {
			if (enumJ.abr.equals(abr))
				return enumJ.nume;

		}
		return "";
	}

}
