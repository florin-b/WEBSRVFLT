package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import beans.LatLng;
import beans.TraseuBorderou;
import database.OperatiiTraseu;
import utils.MapUtils;
import utils.Utils;
import utils.UtilsFormatting;

public class OperatiiDiurna {

	private static final int MINUTE_DIURNA = 720;
	private static final double DIST_1_FILIALA_KM = 0.5;
	private static final int DIST_2_FILIALA_KM = 30;

	public List<String> calculeazaDiurna(String nrAuto, String filiala, String dataStart, String dataStop) {

		List<String> zileDiurna = new ArrayList<>();

		try {

			List<TraseuBorderou> traseuMasina = new OperatiiTraseu().getTraseuMasinaPlus(nrAuto, dataStart, dataStop);

			LatLng pozitieFiliala = new OperatiiTraseu().getCoordonateFiliala(filiala);

			Set<String> zileInterval = new HashSet<>();

			double distMasina;

			for (TraseuBorderou traseu : traseuMasina) {
				zileInterval.add(traseu.getDataInreg().split(" ")[0]);
			}

			boolean exitRaza;
			String timpExitRaza;
			String timpStop;
			List<Integer> minuteDiurna = new ArrayList<>();
			int totalMinuteDiurna;
			boolean calculDiurna;

			int intervalMin;

			for (String dataTraseu : zileInterval) {

				timpExitRaza = "";
				timpStop = "";
				exitRaza = false;
				calculDiurna = false;

				for (TraseuBorderou traseu : traseuMasina) {

					if (dataTraseu.equals(traseu.getDataInreg().split(" ")[0])) {

						distMasina = MapUtils.distanceXtoY(traseu.getLatitudine(), traseu.getLongitudine(), pozitieFiliala.getLat(), pozitieFiliala.getLng(),
								"K");

						if (distMasina >= DIST_1_FILIALA_KM && !exitRaza) {
							exitRaza = true;
							timpExitRaza = traseu.getDataInreg();
							timpStop = "";
						}

						if (distMasina < DIST_1_FILIALA_KM) {
							exitRaza = false;

							if (timpStop.isEmpty() && !timpExitRaza.isEmpty()) {
								timpStop = traseu.getDataInreg();
								intervalMin = Utils.dateDiffMinutes2(timpExitRaza, timpStop);
								minuteDiurna.add(intervalMin);
							}
						}

						if (distMasina >= DIST_2_FILIALA_KM)
							calculDiurna = true;

						if (exitRaza)
							timpStop = traseu.getDataInreg();

					}
				}

				if (!timpExitRaza.isEmpty() && !timpStop.isEmpty() && calculDiurna) {

					intervalMin = Utils.dateDiffMinutes2(timpExitRaza, timpStop);
					minuteDiurna.add(intervalMin);
					totalMinuteDiurna = minuteDiurna.stream().reduce(0, (a, b) -> a + b);

					if (totalMinuteDiurna >= MINUTE_DIURNA)
						zileDiurna.add(UtilsFormatting.formatDate(dataTraseu));

				}
			}

		} catch (Exception e) {
			zileDiurna = new ArrayList<>();
		}

		Collections.sort(zileDiurna);
		return zileDiurna;

	}

}
