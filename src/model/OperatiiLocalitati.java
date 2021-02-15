package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.BeanDateLocalitate;
import database.DBManager;
import queries.SqlQueries;
import utils.MapUtils;
import utils.Utils;

public class OperatiiLocalitati {

	private static final Logger logger = LogManager.getLogger(OperatiiLocalitati.class);

	public BeanDateLocalitate getDateLocalitate(String codJudet, String localitate) {

		BeanDateLocalitate dateLocalitate = new BeanDateLocalitate();

		try (Connection conn = new DBManager().getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getDateLocalitate())) {

			stmt.setString(1, codJudet);
			stmt.setString(2, localitate.trim().toUpperCase());

			stmt.execute();
			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				dateLocalitate.setRazaKm(rs.getInt("razaKm"));
				dateLocalitate.setLat(Double.valueOf(rs.getString("latitudine")));
				dateLocalitate.setLon(Double.valueOf(rs.getString("longitudine")));
			}

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codJudet + " , " + localitate));
		}

		return dateLocalitate;
	}

	public boolean isAdresaInRaza(double latitudine, double longitudine, String codJudet, String localitate) {

		BeanDateLocalitate dateLocalitate = getDateLocalitate(codJudet, localitate);

		double distXY = MapUtils.distanceXtoY(latitudine, longitudine, dateLocalitate.getLat(), dateLocalitate.getLon(), "K");

		return distXY <= dateLocalitate.getRazaKm();

	}

}
