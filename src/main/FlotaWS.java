package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import beans.BeanEvenimentStop;
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiBorderou;
import database.OperatiiMasina;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import model.CalculeazaTraseu;

public class FlotaWS {

	public Set<RezultatTraseu> getStareBorderou(String codBorderou) {

		Set<RezultatTraseu> rezultat = new HashSet<RezultatTraseu>();

		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		List<TraseuBorderou> traseuBorderou = operatiiTraseu.getTraseuBorderou(codBorderou);
		List<PozitieClient> pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.TOTI);

		CalculeazaTraseu calculeaza = new CalculeazaTraseu(codBorderou);
		calculeaza.setPozitiiClienti(pozitiiClienti);
		calculeaza.setTraseuBorderou(traseuBorderou);

		rezultat = calculeaza.getStareTraseu();
		System.out.println("Rezultat traseu: " + rezultat);

		return rezultat;
	}

	public String getEvenimentStop(String codBorderou) throws IOException {
		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();
		BeanEvenimentStop evenimentStop = new BeanEvenimentStop();
		List<PozitieClient> pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.NEVIZITATI);
		OperatiiMasina operatiiMasina = new OperatiiMasina();
		try {

			evenimentStop = operatiiMasina.getInfoEvenimentStop(codBorderou, pozitiiClienti);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return operatiiMasina.serializeEvenimentStop(evenimentStop);

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
			List<TraseuBorderou> traseuBorderou = operatiiTraseu.getTraseuBorderou(borderouActiv);
			List<PozitieClient> pozitiiClienti = operatiiTraseu.getFilialaStartBorderou(borderouActiv);

			CalculeazaTraseu calculeaza = new CalculeazaTraseu(borderouActiv);
			calculeaza.setPozitiiClienti(pozitiiClienti);
			calculeaza.setTraseuBorderou(traseuBorderou);

			rezultat = calculeaza.getStareTraseu();
			System.out.println("Rezultat traseu: " + rezultat);

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
