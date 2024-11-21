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
		case AG10:
			return "resources/AG10.xml";			
		case BC10:
			return "resources/BC10.xml";
		case BH10:
			return "resources/BH10.xml";
		case BU10:
			return "resources/BU10.xml";
		case BU11:
			return "resources/BU11.xml";
		case BU12:
			return "resources/BU12.xml";
		case BV10:
			return "resources/BV10.xml";
		case BZ10:
			return "resources/BZ10.xml";
		case CJ10:
			return "resources/CJ10.xml";
		case CT10:
			return "resources/CT10.xml";
		case DJ10:
			return "resources/DJ10.xml";
		case GL10:
		case GL90:
			return "resources/GL10.xml";
		case HD10:
			return "resources/HD10.xml";
		case IS10:
			return "resources/IS10.xml";
		case MM10:
			return "resources/MM10.xml";
		case MS10:
			return "resources/MS10.xml";
		case NT10:
			return "resources/NT10.xml";
		case PH10:
			return "resources/PH10.xml";
		case SB10:
			return "resources/SB10.xml";
		case SV10:
			return "resources/SV10.xml";
		case TM10:
			return "resources/TM10.xml";
		case VN10:
			return "resources/VN10.xml";			
		default:
			return null;

		}

	}
	
	
	
	public static String getArondareFile(EnumArondare arondare) {
		
		return "resources/" + arondare +".xml";
		

	}

}
