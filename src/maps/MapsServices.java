package maps;

import java.util.List;

import enums.EnumArondare;
import enums.EnumZona;
import model.DataLoad;
import utils.MapUtils;

public class MapsServices {

	public static EnumZona getZonaBucuresti(String codJudet, String localitate, String strada, String numar) {

		EnumZona zonaBuc = EnumZona.NEDEFINITA;

		try {

			String[] coords = MapUtils.getCoordLocalitateFromService(codJudet, localitate, strada, numar).split(",");

			beans.LatLng point = new beans.LatLng(Double.valueOf(coords[0]), Double.valueOf(coords[1]));

			for (EnumZona zn : EnumZona.values()) {
				List<beans.LatLng> pointsList = DataLoad.getZona(zn);

				boolean contains = MapsOperations.containsPoint(point, pointsList, true);

				if (contains)
					return zn;

			}
		} catch (Exception e) {

		}

		return zonaBuc;
	}

	public static EnumArondare getArondareFiliala(String codJudet, String localitate, String strada, String numar) {
		EnumArondare zonaArondata = EnumArondare.NEDEFINIT;

		try {

			String[] coords = MapUtils.getCoordLocalitateFromService(codJudet, localitate, strada, numar).split(",");

			beans.LatLng point = new beans.LatLng(Double.valueOf(coords[0]), Double.valueOf(coords[1]));

			for (EnumArondare ar : EnumArondare.values()) {
				
				if (ar == EnumArondare.NEDEFINIT)
					continue;
				
				List<beans.LatLng> pointsList = DataLoad.getArondare(ar);

				boolean contains = MapsOperations.containsPoint(point, pointsList, true);

				if (contains)
					return ar;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return zonaArondata;
	}

}
