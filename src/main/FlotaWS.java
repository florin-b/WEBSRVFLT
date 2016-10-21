package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import beans.BeanEvenimentStop;
import beans.DateBorderou;
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiBorderou;
import database.OperatiiMasina;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import model.CalculeazaTraseu;

import utils.MapUtils;
import utils.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlotaWS {

	private static final Logger logger = LogManager.getLogger(FlotaWS.class);

	public Set<RezultatTraseu> getStareBorderou(String codBorderou) {

		Set<RezultatTraseu> rezultat = new HashSet<RezultatTraseu>();

		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		List<TraseuBorderou> traseuBorderou = null;
		List<PozitieClient> pozitiiClienti = null;

		DateBorderou dateBorderou = null;
		try {
			dateBorderou = operatiiTraseu.getDateBorderou(codBorderou);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e));
		}

		if (dateBorderou.getNrMasina() == null)
			return null;

		try {
			traseuBorderou = operatiiTraseu.getTraseuBorderou(dateBorderou);
			pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.TOTI);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e));
		}

		CalculeazaTraseu calculeaza = new CalculeazaTraseu(codBorderou);
		calculeaza.setPozitiiClienti(pozitiiClienti);
		calculeaza.setTraseuBorderou(traseuBorderou);
		calculeaza.setDateBorderou(dateBorderou);

		rezultat = calculeaza.getStareTraseu();

		return rezultat;
	}

	public String getEvenimentStop(String codBorderou) throws IOException {

		BeanEvenimentStop evenimentStop = new BeanEvenimentStop();
		OperatiiMasina operatiiMasina = new OperatiiMasina();

		return operatiiMasina.serializeEvenimentStop(evenimentStop);

	}

	public String getCoordAddress(String codJudet, String localitate, String strada, String numar) {
		return MapUtils.getCoordAddress(codJudet, localitate, strada, numar);
	}

	public boolean getStartBorderou(String codSofer) {
		Set<RezultatTraseu> rezultat = new HashSet<RezultatTraseu>();

		String borderouActiv = null;
		boolean isBordMarked = false;
		boolean isBordStarted = false;

		try {
			borderouActiv = new OperatiiBorderou().getBorderouActiv(codSofer);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e));
		}

		if (borderouActiv != null) {
			OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

			DateBorderou dateBorderou = null;
			try {
				dateBorderou = operatiiTraseu.getDateBorderou(borderouActiv);
			} catch (SQLException e) {
				logger.error(Utils.getStackTrace(e));
			}

			List<TraseuBorderou> traseuBorderou = null;

			try {
				traseuBorderou = operatiiTraseu.getTraseuBorderou(dateBorderou);
			} catch (SQLException e) {
				logger.error(Utils.getStackTrace(e));
			}
			List<PozitieClient> pozitiiClienti = operatiiTraseu.getFilialaStartBorderou(borderouActiv);

			CalculeazaTraseu calculeaza = new CalculeazaTraseu(borderouActiv);
			calculeaza.setPozitiiClienti(pozitiiClienti);
			calculeaza.setTraseuBorderou(traseuBorderou);
			calculeaza.setDateBorderou(dateBorderou);

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

	public boolean sendSms(String nrTelefon, String mesaj) {
		return false;
	}

}
