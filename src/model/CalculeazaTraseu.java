package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import beans.BeanEvenimentTableta;
import beans.DateBorderou;
import beans.PozitieClient;
import beans.PozitieGps;
import beans.RezultatTraseu;
import beans.TraseuBorderou;

import database.OperatiiBorderou;
import database.OperatiiMasina;
import enums.EnumTipClient;
import utils.Constants;
import utils.MapUtils;
import utils.Utils;
import utils.UtilsFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CalculeazaTraseu {

	private List<PozitieClient> pozitiiClienti;
	private List<TraseuBorderou> traseuBorderou;
	private List<RezultatTraseu> rezultatTraseu;
	private List<RezultatTraseu> stareTraseu;
	private TreeSet<RezultatTraseu> traseuFinal;
	private String dataStartBorderou;
	private String codBorderou;
	private DateBorderou dateBorderou;

	private List<BeanEvenimentTableta> listEvenimenteTableta;

	private static final Logger logger = LogManager.getLogger(CalculeazaTraseu.class);

	private enum EvenimentClient {
		SOSIRE, PLECARE
	};

	public CalculeazaTraseu(String codBorderou) {
		this.codBorderou = codBorderou;
		this.rezultatTraseu = new ArrayList<RezultatTraseu>();
	}

	public List<PozitieClient> getPozitiiClienti() {
		return pozitiiClienti;
	}

	public void setPozitiiClienti(List<PozitieClient> pozitiiClienti) {
		this.pozitiiClienti = pozitiiClienti;
	}

	public List<TraseuBorderou> getTraseuBorderou() {
		return traseuBorderou;
	}

	public void setTraseuBorderou(List<TraseuBorderou> traseuBorderou) {
		this.traseuBorderou = traseuBorderou;
	}

	public void setRezultatTraseu(List<RezultatTraseu> rezultatTraseu) {
		this.rezultatTraseu = rezultatTraseu;
	}

	public void setDateBorderou(DateBorderou dateBorderou) {
		this.dateBorderou = dateBorderou;
	}

	private void descoperaEvenimente() {

		OperatiiBorderou opBorderou = new OperatiiBorderou();
		opBorderou.setIdDevice(new OperatiiMasina().getIdDevice(dateBorderou.getNrMasina()));

		listEvenimenteTableta = opBorderou.getEvenimenteTableta(codBorderou);

		Date maxEventDate = opBorderou.getMaxDateEveniment(listEvenimenteTableta, codBorderou);

		double distanta = 0;
		for (TraseuBorderou traseu : traseuBorderou) {
			for (PozitieClient pozitieClient : pozitiiClienti) {

				distanta = MapUtils.distanceXtoY(traseu.getLatitudine(), traseu.getLongitudine(), pozitieClient.getLatitudine(), pozitieClient.getLongitudine(),
						"K");

				if (distanta < 700){
				//	System.out.println(pozitieClient.getCodClient()+ " , " + traseu.getDataInreg() + " , " + distanta + " , " + traseu.getViteza());
				//	System.out.println(rezultatTraseu);
				}

				if (conditiiSosire(traseu, distanta, pozitieClient, maxEventDate)) {
					pozitieClient.setKmBord(traseu.getKm());
					adaugaEveniment(pozitieClient, traseu, EvenimentClient.SOSIRE);

				}

				if (conditiiPlecare(traseu, pozitieClient)) {
					adaugaEveniment(pozitieClient, traseu, EvenimentClient.PLECARE);

				}

			}

		}

	}

	private boolean conditiiSosire(TraseuBorderou traseu, double distanta, PozitieClient pozitieClient, Date maxDate) {

		if (pozitieClient.isStopBord()) {
			if (borderouNotStarted(traseu, pozitieClient) || rezultatTraseu.size() == 1) {
				return false;
			}

			//if (!UtilsFormatting.isDateChronological(traseu.getDataInreg(), maxDate))
			//	return false;

		}

		if (traseu.getViteza() == 0 && distanta <= getDistanta(pozitieClient.getTipClient()) && condSuplimentSosire(traseu, pozitieClient))
			return true;

		return false;
	}

	private Date getMaxDateTraseu() {
		Date maxDate = new Date();

		try {
			maxDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH).parse(dataStartBorderou);

			for (RezultatTraseu traseu : rezultatTraseu) {

				if (traseu.getPlecare() != null) {
					Date compDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH).parse(traseu.getPlecare().getData());

					if (maxDate.compareTo(compDate) < 0)
						maxDate = compDate;
				}

			}

		} catch (ParseException e) {
			logger.error(Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}

		return maxDate;
	}

	private boolean condSuplimentSosire(TraseuBorderou traseuBorderou, PozitieClient pozitieClient) {

		if (getCoordEveniment(pozitieClient.getCodClient(), EvenimentClient.SOSIRE) != null)
			return false;

		if (dataStartBorderou == null)
			return true;

		try {

			Date dateLow = getMaxDateTraseu();

			Date dateHigh = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH).parse(traseuBorderou.getDataInreg());

			if (dateLow.compareTo(dateHigh) < 0)
				return true;

		} catch (ParseException e) {
			logger.error(Utils.getStackTrace(e));
		}

		return false;
	}

	private boolean borderouNotStarted(TraseuBorderou traseu, PozitieClient pozitieClient) {

		for (RezultatTraseu rezultat : rezultatTraseu) {

			for (PozitieClient pozitie : pozitiiClienti) {
				if (pozitie.isStartBord()) {
					if (rezultat.getCodClient().equals(pozitie.getCodClient())) {

						if (rezultat != null && rezultat.getPlecare() == null)
							return true;
						else if (rezultat.getPlecare().getData().equals(traseu.getDataInreg()))
							return true;

					}
				}

			}

		}

		return false;

	}

	private double getDistanta(EnumTipClient tipClient) {
		switch (tipClient) {
		case DISTRIBUTIE:
			return Constants.RAZA_CLIENT_KM;
		case FILIALA:
			return Constants.RAZA_FILIALA_KM;
		case APROVIZIONARE:
			return Constants.RAZA_FURNIZOR_KM;
		default:
			return 0;
		}
	}

	private double getViteza(EnumTipClient tipClient) {
		switch (tipClient) {
		case DISTRIBUTIE:
			return Constants.VITEZA_MINIMA_PLECARE_CLIENT;
		case FILIALA:
			return Constants.VITEZA_MINIMA_PLECARE_FILIALA;
		default:
			return 0;
		}
	}

	private boolean conditiiPlecare(TraseuBorderou traseu, PozitieClient pozitieClient) {
		if (traseu.getViteza() > getViteza(pozitieClient.getTipClient()) && getCoordEveniment(pozitieClient.getCodClient(), EvenimentClient.SOSIRE) != null
				&& getCoordEveniment(pozitieClient.getCodClient(), EvenimentClient.PLECARE) == null)
			return true;

		return false;
	}

	private void adaugaEveniment(PozitieClient pozitieClient, TraseuBorderou traseuBord, EvenimentClient tipEveniment) {

		boolean found = false;

		if (rezultatTraseu.size() == 0) {
			RezultatTraseu evenim = new RezultatTraseu();
			evenim.setPoz(pozitieClient.getPoz());
			evenim.setCodClient(pozitieClient.getCodClient());
			evenim.setNumeClient(pozitieClient.getNumeClient());
			evenim.setSosire(new PozitieGps(traseuBord.getDataInreg(), traseuBord.getLatitudine(), traseuBord.getLongitudine()));

			evenim.setKmBord(pozitieClient.getKmBord());
			evenim.setCodAdresa(pozitieClient.getCodAdresa());
			evenim.setNumeClientGed(pozitieClient.getNumeClientGed());

			rezultatTraseu.add(evenim);
			found = true;
		}

		else {
			for (RezultatTraseu rez : rezultatTraseu) {
				if (rez.getCodClient().equals(pozitieClient.getCodClient())) {

					if (tipEveniment == EvenimentClient.SOSIRE) {
						if (rez.getSosire() != null)
							found = true;
					}

					if (tipEveniment == EvenimentClient.PLECARE) {
						if (rez.getPlecare() != null)
							found = true;
					}

				}
			}

		}

		if (!found) {

			if (tipEveniment == EvenimentClient.SOSIRE) {
				RezultatTraseu evenim = new RezultatTraseu();
				evenim.setPoz(pozitieClient.getPoz());
				evenim.setCodClient(pozitieClient.getCodClient());
				evenim.setNumeClient(pozitieClient.getNumeClient());
				evenim.setSosire(new PozitieGps(traseuBord.getDataInreg(), traseuBord.getLatitudine(), traseuBord.getLongitudine()));

				evenim.setKmBord(pozitieClient.getKmBord());
				evenim.setCodAdresa(pozitieClient.getCodAdresa());
				evenim.setNumeClientGed(pozitieClient.getNumeClientGed());
				rezultatTraseu.add(evenim);
			}

			if (tipEveniment == EvenimentClient.PLECARE) {
				for (RezultatTraseu traseu : rezultatTraseu) {
					if (traseu.getCodClient().equals(pozitieClient.getCodClient())) {
						traseu.setPlecare(new PozitieGps(traseuBord.getDataInreg(), traseuBord.getLatitudine(), traseuBord.getLongitudine()));
						break;
					}
				}

				if (pozitieClient.isStartBord()) {
					dataStartBorderou = traseuBord.getDataInreg();
				}

			}

		}

	}

	public List<RezultatTraseu> getRezultatTraseu() {
		return rezultatTraseu;
	}

	public Set<RezultatTraseu> getStareTraseu() {

		descoperaEvenimente();

		stareTraseu = new ArrayList<RezultatTraseu>();

		RezultatTraseu tempRez = null;
		Collections.sort(pozitiiClienti);
		Collections.sort(rezultatTraseu);

		for (PozitieClient client : pozitiiClienti) {
			tempRez = new RezultatTraseu();
			tempRez.setPoz(client.getPoz());
			tempRez.setCodClient(client.getCodClient());
			tempRez.setNumeClient(client.getNumeClient());
			tempRez.setCodAdresa(client.getCodAdresa());
			tempRez.setNumeClientGed(client.getNumeClientGed());
			tempRez.setSosire(null);
			tempRez.setPlecare(null);

			for (RezultatTraseu rezultat : rezultatTraseu) {
				if (rezultat.getCodClient().equals(client.getCodClient()) && rezultat.getPoz() == client.getPoz()) {
					tempRez.setSosire(rezultat.getSosire());
					tempRez.setPlecare(rezultat.getPlecare());
					tempRez.setKmBord(rezultat.getKmBord());
					break;

				}
			}

			stareTraseu.add(tempRez);
		}

		Set<RezultatTraseu> setStare = new HashSet<RezultatTraseu>(stareTraseu);

		traseuFinal = new TreeSet<RezultatTraseu>();
		traseuFinal.addAll(setStare);

		completeazaDateSofer();
		corecteazaTraseuBorderou();

		return traseuFinal;

	}

	private void corecteazaTraseuBorderou() {
		Iterator<TraseuBorderou> iterator = traseuBorderou.iterator();

		String dataStartBorderou = "";
		String dataStopBorderou = "";

		for (RezultatTraseu traseu : traseuFinal) {

			for (PozitieClient client : pozitiiClienti) {
				if (client.getCodClient().equals(traseu.getCodClient())) {
					if (client.isStartBord()) {
						if (traseu.getPlecare() != null) {
							dataStartBorderou = traseu.getPlecare().getData();
						}

					}
					if (client.isStopBord()) {
						if (traseu.getSosire() != null) {
							dataStopBorderou = traseu.getSosire().getData();
						}
					}
				}
			}

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);

		Date dateStart = null;
		Date dateStop = null;

		if (dataStartBorderou.length() == 0 || dataStopBorderou.length() == 0)
			return;

		try {
			dateStart = sdf.parse(dataStartBorderou);
			dateStop = sdf.parse(dataStopBorderou);
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}

		Date dateTraseu = null;

		TraseuBorderou traseu = null;
		while (iterator.hasNext()) {
			traseu = iterator.next();

			try {
				dateTraseu = sdf.parse(traseu.getDataInreg());

				if (dateStart != null && dateTraseu.getTime() < dateStart.getTime())
					iterator.remove();

				if (dateStop != null && dateTraseu.getTime() > dateStop.getTime())
					iterator.remove();

			} catch (Exception e) {
				logger.error(Utils.getStackTrace(e));
			}

		}

	}

	private void completeazaDateSofer() {

		String dataStopBorderou;

		if (traseuFinal.last().getSosire() != null)
			dataStopBorderou = traseuFinal.last().getSosire().getData();
		else
			dataStopBorderou = UtilsFormatting.getCurrentDate();

		for (RezultatTraseu traseu : traseuFinal) {

			if (traseu.getPlecare() == null || compareDates(traseu.getPlecare().getData(), dataStopBorderou) > 0)
				preiaEvenimentSofer(traseu, listEvenimenteTableta, EvenimentClient.PLECARE);

			if (traseu.getSosire() == null || compareDates(traseu.getSosire().getData(), dataStopBorderou) > 0)
				preiaEvenimentSofer(traseu, listEvenimenteTableta, EvenimentClient.SOSIRE);

		}

	}

	private int compareDates(String date1, String date2) {

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", new Locale("en"));

		Date d1 = new Date();
		Date d2 = new Date();

		try {
			d1 = df.parse(date1);
			d2 = df.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return d1.compareTo(d2);

	}

	private void preiaEvenimentSofer(RezultatTraseu traseu, List<BeanEvenimentTableta> listEvenimenteTableta, EvenimentClient eveniment) {

		for (BeanEvenimentTableta evenimTableta : listEvenimenteTableta) {

			if (isConditieEveniment(evenimTableta, traseu, eveniment)) {

				PozitieGps pozitie = new PozitieGps();
				pozitie.setData(UtilsFormatting.formatDateLocal(evenimTableta.getData() + " " + evenimTableta.getOra()));
				String[] coords = evenimTableta.getGps().split(",");

				if (coords[0].equals("0")) {
					String archivedCoords = OperatiiBorderou.getCoordFromArchive(codBorderou, evenimTableta.getData());

					if (archivedCoords.contains(",")) {
						String[] arrayVals = archivedCoords.split(",");
						pozitie.setLatitudine(Double.valueOf(arrayVals[0]));
						pozitie.setLongitudine(Double.valueOf(arrayVals[1].trim()));
						traseu.setKmBord(Integer.valueOf(arrayVals[2].trim()));

					}

				} else {
					pozitie.setLatitudine(Double.valueOf(coords[0]));
					pozitie.setLongitudine(Double.valueOf(coords[1]));
					traseu.setKmBord(evenimTableta.getKmBord());
				}
				if (eveniment == EvenimentClient.PLECARE)
					traseu.setPlecare(pozitie);
				else
					traseu.setSosire(pozitie);

				break;
			}

		}

	}

	private boolean isConditieEveniment(BeanEvenimentTableta evenimTableta, RezultatTraseu traseu, EvenimentClient eveniment) {
		if (eveniment == EvenimentClient.PLECARE) {

			if (traseu.getNumeClient().toLowerCase().contains("start")) {
				return evenimTableta.getClient().equals(codBorderou) && evenimTableta.getEveniment().toUpperCase().equals("P");

			} else {
				return evenimTableta.getClient().equals(traseu.getCodClient()) && evenimTableta.getEveniment().toUpperCase().equals("P")
						&& evenimTableta.getCodAdresa().equals(traseu.getCodAdresa());
			}
		} else {

			if (traseu.getNumeClient().toLowerCase().contains("stop")) {
				return evenimTableta.getClient().equals(codBorderou) && evenimTableta.getEveniment().toUpperCase().equals("S");

			} else {
				return evenimTableta.getClient().equals(traseu.getCodClient()) && evenimTableta.getEveniment().toUpperCase().equals("S")
						&& evenimTableta.getCodAdresa().equals(traseu.getCodAdresa());
			}
		}

	}

	public PozitieGps getCoordEveniment(String codClient, EvenimentClient eveniment) {

		PozitieGps evenimentTraseu = null;

		for (RezultatTraseu tras : rezultatTraseu) {
			if (tras.getCodClient().equals(codClient))
				switch (eveniment) {
				case SOSIRE:
					evenimentTraseu = tras.getSosire();
					break;
				case PLECARE:
					evenimentTraseu = tras.getPlecare();
					break;
				default:
					evenimentTraseu = null;

				}

		}

		return evenimentTraseu;

	}

}
