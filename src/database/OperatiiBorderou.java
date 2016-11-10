package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import beans.BeanEvenimentTableta;
import queries.SqlQueries;
import utils.Utils;
import utils.UtilsFormatting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OperatiiBorderou {

	private String idDevice;
	private static final Logger logger = LogManager.getLogger(OperatiiBorderou.class);

	public String getBorderouActiv(String codSofer) throws SQLException {

		String borderouActiv = null;

		DBManager manager = new DBManager();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getBorderouActiv(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codSofer);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			if (rs.next()) {
				borderouActiv = rs.getString(1);
			}
		}

		return borderouActiv;
	}

	public boolean isBorderouMarkedStarted(String codSofer, String codBorderou) throws SQLException {

		boolean isMarked = false;

		DBManager manager = new DBManager();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement prep = conn.prepareStatement(SqlQueries.isBorderouMarkedStart(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {
			prep.setString(1, codSofer);
			prep.setString(2, codBorderou);

			prep.execute();
			isMarked = prep.getResultSet().last();

		}

		return isMarked;
	}

	public List<BeanEvenimentTableta> getEvenimenteTableta(String codBorderou) {

		List<BeanEvenimentTableta> listEvenimente = new ArrayList<>();

		DBManager manager = new DBManager();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getEvenimenteTableta(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codBorderou);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			BeanEvenimentTableta eveniment = null;
			while (rs.next()) {
				eveniment = new BeanEvenimentTableta();
				eveniment.setClient(rs.getString("client"));
				eveniment.setCodAdresa(rs.getString("codadresa"));
				eveniment.setEveniment(rs.getString("eveniment"));
				eveniment.setData(rs.getString("data"));
				eveniment.setOra(rs.getString("ora"));
				eveniment.setGps(rs.getString("gps"));
				eveniment.setKmBord(Integer.valueOf(rs.getString("fms")));
				listEvenimente.add(eveniment);

			}

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e));
		}

		verificaPlecariClient(listEvenimente);

		return listEvenimente;
	}

	private void verificaPlecariClient(List<BeanEvenimentTableta> listEvenimente) {

		ListIterator<BeanEvenimentTableta> iterator = listEvenimente.listIterator();

		while (iterator.hasNext()) {

			BeanEvenimentTableta ev = iterator.next();

			if (ev.getEveniment().equals("S")) {
				setPlecareClient(listEvenimente, ev, iterator);
			}
		}

	}

	private void setPlecareClient(List<BeanEvenimentTableta> listEvenimente, BeanEvenimentTableta eveniment, ListIterator<BeanEvenimentTableta> iterator) {

		for (BeanEvenimentTableta ev : listEvenimente) {

			if (ev.getClient().equals(eveniment) && ev.getEveniment().equals("P") && ev.getCodAdresa().equals(eveniment.getCodAdresa())) {
				return;
			}

		}

		if (eveniment.getData() != null)
			setEvenimentPlecare(listEvenimente, eveniment, iterator);

	}

	private void setEvenimentPlecare(List<BeanEvenimentTableta> listEvenimente, BeanEvenimentTableta eveniment, ListIterator<BeanEvenimentTableta> iterator) {

		DBManager manager = new DBManager();

		String dateComp = null;

		String sqlString = " select to_char(record_time,'yyyymmdd') data, to_char(record_time,'HH24miss') ora ,latitude, longitude, mileage "
				+ " from gps_date where device_id =? "
				+ " and speed > 0 and record_time > to_date(?,'dd-mm-yyyy HH24:mi:ss') and rownum < 2 order by record_time ";

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			dateComp = UtilsFormatting.formatDateSimpleYear(eveniment.getData() + " " + eveniment.getOra());

			stmt.setString(1, idDevice);
			stmt.setString(2, dateComp);

			stmt.execute();
			ResultSet rs = stmt.getResultSet();

			BeanEvenimentTableta evPlecare = new BeanEvenimentTableta();
			evPlecare.setClient(eveniment.getClient());
			evPlecare.setCodAdresa(eveniment.getCodAdresa());
			evPlecare.setEveniment("P");

			if (rs.next()) {
				evPlecare.setData(rs.getString("data"));
				evPlecare.setOra(rs.getString("ora"));
				evPlecare.setGps(rs.getDouble("latitude") + "," + rs.getDouble("longitude"));
				evPlecare.setKmBord(rs.getInt("mileage"));
			}

			iterator.add(evPlecare);

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e) + " device = " + idDevice + " date = " + dateComp);
		}

	}

	public Date getMaxDateEveniment(List<BeanEvenimentTableta> listEvenimente, String codBorderou) {

		DateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse("01-Jan-70 00:00:00");
		} catch (Exception e) {

		}

		for (BeanEvenimentTableta eveniment : listEvenimente) {

			if ((eveniment.getEveniment().equals("P") && eveniment.getClient().equals(codBorderou))
					|| (eveniment.getEveniment().equals("S") && !eveniment.getClient().equals(codBorderou))) {

				if (eveniment.getData() == null)
					continue;

				String dataEv = eveniment.getData() + " " + eveniment.getOra();
				try {
					date2 = sdf.parse(UtilsFormatting.formatDateSimple(dataEv));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (date2.compareTo(date1) > 0)
					date1 = date2;
			}

		}

		return UtilsFormatting.addDays(date2, 0);
	}

	public static String getCoordFromArchive(String codBorderou, String data) {

		DBManager manager = new DBManager();
		String results = "";

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getCoordEventFromArchive(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, data);
			stmt.setString(2, codBorderou);
			stmt.setString(3, data);
			stmt.setString(4, data);
			stmt.setString(5, data);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				results = String.valueOf(rs.getDouble("latitude"));
				results += ", " + String.valueOf(rs.getDouble("longitude"));
				results += ", " + String.valueOf(rs.getLong("mileage"));

			}

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e));
		}

		return results;

	}

	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}

}
