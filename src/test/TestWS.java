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
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiBorderou;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import enums.EnumZona;
import main.FlotaWS;
import maps.MapsOperations;
import maps.MapsServices;
import model.CalculeazaTraseu;
import model.DataLoad;
import utils.MapUtils;
import utils.Utils;

public class TestWS {

	private static final Logger logger = LogManager.getLogger(TestWS.class);

	public static void main(String[] args) throws SQLException {

		try {
			
			
			//new FlotaWS().getStareBorderou("0002456141");
			
		//	System.out.println(new OperatiiLocalitati().isAdresaInRaza(45.845362, 27.425546, "17", "Adam"));

			//testBorderou();
			
			//System.out.println("arondare filiala: " +  getArondareFiliala("24", "Baia Mare", "STRADA galati", ""));
			
			
		//	System.out.println("arondare filiala: " +  getArondareFiliala(45.057191, 28.689223));
			
			
			//System.out.println(Utils.dateDiff("27-Oct-20 08:46:00", "27-Oct-20 15:25:01"));
			
			
			//System.out.println("Coordonate: " +  new FlotaWS().getCoordAddress("40", "SECTOR 1", "STRADA CIOBANULUI", ""));
			
			
			//System.out.println("Coordonate: " +  new FlotaWS().getCoordAddress("40", "SECTOR 1", "STRADA CUTESCU-STORK CECI", ""));
			
			
			System.out.println("Adresa coord: " + new FlotaWS().getAdresaCoordonate(45.313,27.927));
			
			
			//System.out.println("Coordonate: " + new FlotaWS().getCoordonateLocalitate("17", "Tecuci", "", ""));
			
			
			//System.out.println("Zona Buc: " + new FlotaWS().getZonaBucuresti("40", "Bucuresti", "Piata Romana", ""));
			
		
		//	System.out.println(new MapsServices().traseuInPoligon("13-07-2021 00:00", "13-07-2021 14:00", "GL08ARS", "DJ10"));
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static EnumZona getZonaBucuresti(String codJudet, String localitate, String strada, String numar) {

		EnumZona zonaBuc = EnumZona.NEDEFINITA;

		try {

			String[] coords =  MapUtils.getCoordLocalitateFromService(codJudet, localitate, strada, numar).split(",");
			
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
	
	
	public static EnumZona getZonaBucuresti(beans.LatLng point) {

		EnumZona zonaBuc = EnumZona.NEDEFINITA;

		try {

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

		String codBorderou = "0002290524";

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

	public static String getArondareFiliala(String codJudet, String localitate, String strada, String numar) {
		return MapsServices.getArondareFiliala(codJudet, localitate, strada, numar).toString();
	}
	
	public static String getArondareFiliala(double lat, double lon) {
		return MapsServices.getArondareFiliala(lat, lon);
	}
	
}
