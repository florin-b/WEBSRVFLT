package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.BeanEvenimentStop;
import beans.DateBorderou;
import beans.DistantaRuta;
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiBorderou;
import database.OperatiiMasina;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import maps.MapsServices;
import model.CalculeazaTraseu;
import model.Distanta;
import model.OperatiiLocalitati;
import utils.MailOperations;
import utils.MapUtils;
import utils.Utils;

public class FlotaWS {

	private static final Logger logger = LogManager.getLogger(FlotaWS.class);

	public Set<RezultatTraseu> getStareBorderou(String codBorderou) {

		Set<RezultatTraseu> rezultat;

		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		List<TraseuBorderou> traseuBorderou = null;
		List<PozitieClient> pozitiiClienti = null;

		DateBorderou dateBorderou = null;
		try {
			dateBorderou = operatiiTraseu.getDateBorderou(codBorderou);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codBorderou));

			MailOperations.sendMail("getStareBorderou: " + e.toString());
		}

		if (dateBorderou.getNrMasina() == null)
			return null;

		try {
			traseuBorderou = operatiiTraseu.getTraseuBorderou(dateBorderou);
			pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.TOTI);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codBorderou));
			MailOperations.sendMail("getStareBorderou: " + e.toString());
		}

		CalculeazaTraseu calculeaza = new CalculeazaTraseu(codBorderou);
		calculeaza.setPozitiiClienti(pozitiiClienti);
		calculeaza.setTraseuBorderou(traseuBorderou);
		calculeaza.setDateBorderou(dateBorderou);

		rezultat = calculeaza.getStareTraseu();

		System.out.println("Rezultat:" + rezultat);
		
		return rezultat;
	}

	public String getEvenimentStop(String codBorderou) throws IOException {

		BeanEvenimentStop evenimentStop = new BeanEvenimentStop();
		OperatiiMasina operatiiMasina = new OperatiiMasina();

		return operatiiMasina.serializeEvenimentStop(evenimentStop);

	}

	public String getDistantaTraseu(String dataStart, String dataStop, String nrMasina) {

		String retVal = "";
		Distanta distanta = new Distanta();

		try {
			retVal = distanta.getDistanta(dataStart, dataStop, nrMasina);
		} catch (SQLException e) {
			String extraInfo = dataStart + " , " + dataStop + " , " + nrMasina;
			logger.error(Utils.getStackTrace(e, extraInfo));
		}

		return retVal;
	}

	public String getCoordonateLocalitate(String codJudet, String localitate, String strada, String numar) {
		return MapUtils.getCoordAddressFromService(codJudet, localitate, strada, numar);
	}

	public String getCoordAddress(String codJudet, String localitate, String strada, String numar) {
		return MapUtils.getCoordAddressFromService(codJudet, localitate, strada, numar);
	}



	public boolean getStartBorderou(String codSofer) {
		Set<RezultatTraseu> rezultat = new HashSet<RezultatTraseu>();

		String borderouActiv = null;
		boolean isBordMarked = false;
		boolean isBordStarted = false;

		try {
			borderouActiv = new OperatiiBorderou().getBorderouActiv(codSofer);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codSofer));
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
			} catch (SQLException e) {
				logger.error(Utils.getStackTrace(e, dateBorderou.toString()));
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

	public List<DistantaRuta> getDistantaPuncte(String coordonate) {
		return MapUtils.getDistantaPuncte(coordonate);
	}

	public String getAdresaCoordonate(double lat, double lng) {
		return MapUtils.getAdresaCoordonate(lat, lng);
	}

	public int getDistantaLocalitatiKM(String jud1, String loc1, String jud2, String loc2) {
		return MapUtils.getDistantaLocalitatiKM(jud1, loc1, jud2, loc2);
	}

	public String getZonaBucuresti(String codJudet, String localitate, String strada, String numar) {
		return MapsServices.getZonaBucuresti(codJudet, localitate, strada, numar).toString();
	}
	
	public String getArondareFiliala(String codJudet, String localitate, String strada, String numar) {
		return MapsServices.getArondareFiliala(codJudet, localitate, strada, numar).toString();
	}
	
	public String getArondareFilialaCoords(double lat, double lon) {
		return MapsServices.getArondareFiliala(lat, lon);
	}
	
	public boolean isAdresaInRaza(double lat, double lon, String codJudet, String localitate){
		return new OperatiiLocalitati().isAdresaInRaza(lat, lon, codJudet, localitate);
	}
	
	public int traseuInPoligon(String dataStart, String dataStop, String nrAuto, String filiala){
		return new MapsServices().traseuInPoligon(dataStart, dataStop, nrAuto, filiala);
	}

}
