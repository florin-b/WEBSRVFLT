package model;

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

}
