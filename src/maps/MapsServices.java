package maps;

import java.util.ArrayList;
import java.util.List;

import beans.PunctPoligon;
import beans.TraseuBorderou;
import database.OperatiiTraseu;
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

	public static String getArondareFiliala(double lat, double lon) {
		StringBuilder zoneArondate = new StringBuilder();

		beans.LatLng point = new beans.LatLng(lat, lon);

		try {
			for (EnumArondare ar : EnumArondare.values()) {

				if (ar == EnumArondare.NEDEFINIT)
					continue;

				List<beans.LatLng> pointsList = DataLoad.getArondare(ar);

				boolean containsPoint = MapsOperations.containsPoint(point, pointsList, true);

				if (containsPoint) {
					if (zoneArondate.toString().isEmpty())
						zoneArondate.append(ar.toString());
					else
						zoneArondate.append(",").append(ar.toString());
				}

			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return zoneArondate.toString();
	}

	public int traseuInPoligon(String dataStart, String dataStop, String nrAuto, String filiala) {

		List<TraseuBorderou> traseuMasina = new OperatiiTraseu().getTraseuMasina(nrAuto, dataStart, dataStop);
		List<PunctPoligon> innerPoints = new ArrayList<>();
		int distanta = 0;
		int totalDist = 0;

		try {

			List<beans.LatLng> polygonPoints = DataLoad.getArondare(EnumArondare.valueOf(filiala));
			boolean pointIn = false;

			for (TraseuBorderou traseu : traseuMasina) {

				beans.LatLng point = new beans.LatLng(traseu.getLatitudine(), traseu.getLongitudine());

				if (MapsOperations.containsPoint(point, polygonPoints, true)) {
					PunctPoligon punct = new PunctPoligon();
					punct.setPunct(point);
					punct.setPointIn(pointIn);
					punct.setKm(traseu.getKm());
					innerPoints.add(punct);
					pointIn = true;
				} else
					pointIn = false;

			}

			int crntPos = 0;
			for (PunctPoligon pnct : innerPoints) {

				if (!pnct.isPointIn()) {
					crntPos++;
					continue;
				}

				distanta = pnct.getKm() - innerPoints.get(crntPos - 1).getKm();

				totalDist += distanta;

				crntPos++;

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return totalDist;
	}

}
