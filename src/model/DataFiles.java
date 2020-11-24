package model;

import enums.EnumArondare;
import enums.EnumZona;

public class DataFiles {

	public static String getDataFile(EnumZona zona) {

		switch (zona) {
		case ZONA_A:
			return "resources/BUC_ZONA_A.xml";
		case ZONA_B:
			return "resources/BUC_ZONA_B.xml";
		case ZONA_C:
			return "resources/BUC_ZONA_C.xml";
		default:
			return null;

		}

	}
	
	
	
	public static String getArondareFile(EnumArondare arondare) {

		switch (arondare) {
		case BU12:
			return "resources/BU12.xml";
		case BU10:
			return "resources/BU10.xml";
		case BU11:
			return "resources/BU11.xml";
		default:
			return null;

		}

	}

}
