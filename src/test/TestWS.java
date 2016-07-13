package test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import beans.DateBorderou;
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiBorderou;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import model.CalculeazaTraseu;
import utils.MapUtils;

public class TestWS {

	public static void main(String[] args) {
		//System.out.println(testGps());
		testBorderou();

	}

	private static String testGps() {
		return MapUtils.getCoordAddress("17", "Galati", "STRADA ROSIORI, piata ancora NR 1", "");
	}

	private static void testBorderou() {
		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		System.out.println("Start");
		
		String codBorderou = "0001490570";

		// 0001471175
		
		DateBorderou dateBorderou = null;
		try {
			dateBorderou = operatiiTraseu.getDateBorderou(codBorderou);
		} catch (SQLException e) {
			System.out.println(e.getStackTrace().toString());
		}
		
		if (dateBorderou.getNrMasina() == null)
			return ;

		List<TraseuBorderou> traseuBorderou = null;

		try {
			traseuBorderou = operatiiTraseu.getTraseuBorderou(dateBorderou);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<PozitieClient> pozitiiClienti = null;

		try {
			pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.TOTI);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		

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
			e1.printStackTrace();
		}

		if (borderouActiv != null) {
			OperatiiTraseu operatiiTraseu = new OperatiiTraseu();
			
			DateBorderou dateBorderou = null;
			try {
				dateBorderou = operatiiTraseu.getDateBorderou(borderouActiv);
			} catch (SQLException e) {
				System.out.println(e.getStackTrace().toString());
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
					e.printStackTrace();
				}
			}

		}

		return isBordStarted ? isBordMarked : false;

	}

}
