package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.maps.model.LatLng;

import beans.DateBorderou;
import beans.DistantaRuta;
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiBorderou;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import model.CalculeazaTraseu;
import utils.MapUtils;
import utils.Utils;

public class TestWS {

	private static final Logger logger = LogManager.getLogger(TestWS.class);

	public static void main(String[] args) throws SQLException {

		 testBorderou();

		//System.out.println( MapUtils.getAdresaCoordonate(46.076 ,	24.412));

	}

	private static void testPoly() {
		List<LatLng> points = new ArrayList<>();

		points.add(new LatLng(45.456715, 27.989181));
		points.add(new LatLng(45.462809, 28.025017));
		points.add(new LatLng(45.453754, 28.053519));

		points.add(new LatLng(45.451049, 28.024775));
		points.add(new LatLng(45.426287, 28.032628));
		points.add(new LatLng(45.442641, 28.012405));
		points.add(new LatLng(45.429639, 27.990228));

		LatLng findPoint = new LatLng(45.437845, 27.997513);

		boolean contains = MapUtils.containsLocation(findPoint, points, true);

		System.out.println("Contains: " + contains);

	}

	private static void testGps() {
		System.out.println(MapUtils.getCoordAddressFromService("17", "Galati", "STRADA timisului", "1"));

	}

	private static void testBorderou() {
		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		System.out.println("Start");
		long startTime = System.currentTimeMillis();

		String codBorderou = "0001682184";

		DateBorderou dateBorderou = null;
		try {
			dateBorderou = operatiiTraseu.getDateBorderou(codBorderou);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codBorderou));
		}

		System.out.println(dateBorderou);

		if (dateBorderou.getNrMasina() == null)
			return;

		List<TraseuBorderou> traseuBorderou = null;

		try {
			traseuBorderou = operatiiTraseu.getTraseuBorderou(dateBorderou);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, dateBorderou.toString()));
		}

		List<PozitieClient> pozitiiClienti = null;

		try {
			pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.TOTI);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Diff = " + estimatedTime);

		CalculeazaTraseu calculeaza = new CalculeazaTraseu(codBorderou);
		calculeaza.setPozitiiClienti(pozitiiClienti);
		calculeaza.setTraseuBorderou(traseuBorderou);
		calculeaza.setDateBorderou(dateBorderou);

		Set<RezultatTraseu> rezultat = calculeaza.getStareTraseu();

		System.out.println(rezultat);

	}

	public boolean getStartBorderou(String codSofer) {
		Set<RezultatTraseu> rezultat = new HashSet<RezultatTraseu>();

		String borderouActiv = null;
		boolean isBordMarked = false;
		boolean isBordStarted = false;

		try {
			borderouActiv = new OperatiiBorderou().getBorderouActiv(codSofer);
		} catch (SQLException e1) {
			logger.error(Utils.getStackTrace(e1, borderouActiv));
		}

		if (borderouActiv != null) {
			OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

			DateBorderou dateBorderou = null;
			try {
				dateBorderou = operatiiTraseu.getDateBorderou(borderouActiv);
			} catch (SQLException e) {
				logger.error(Utils.getStackTrace(e, borderouActiv));
			}

			List<TraseuBorderou> traseuBorderou = null;

			try {
				traseuBorderou = operatiiTraseu.getTraseuBorderou(dateBorderou);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			List<PozitieClient> pozitiiClienti = operatiiTraseu.getFilialaStartBorderou(borderouActiv);

			CalculeazaTraseu calculeaza = new CalculeazaTraseu(borderouActiv);
			calculeaza.setPozitiiClienti(pozitiiClienti);
			calculeaza.setTraseuBorderou(traseuBorderou);

			rezultat = calculeaza.getStareTraseu();

			OperatiiBorderou opBorderou = new OperatiiBorderou();

			for (RezultatTraseu rez : rezultat) {

				if (rez.getPlecare() != null) {
					isBordStarted = true;
				}
			}

			if (isBordStarted) {
				try {
					isBordMarked = opBorderou.isBorderouMarkedStarted(codSofer, borderouActiv);
				} catch (SQLException e) {
					logger.error(Utils.getStackTrace(e, borderouActiv));
				}
			}

		}

		return isBordStarted ? isBordMarked : false;

	}

}
