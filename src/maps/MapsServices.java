package maps;

import java.util.List;

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

}
