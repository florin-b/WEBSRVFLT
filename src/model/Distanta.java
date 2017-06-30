package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.maps.model.LatLng;

import beans.BeanDistanta;
import database.DBManager;
import queries.SqlQueries;
import utils.MapUtils;
import utils.Utils;

public class Distanta {

	private static final Logger logger = LogManager.getLogger(Distanta.class);
	
	public String getDistanta(String dataStart, String dataStop, String nrMasina) throws SQLException {

		DBManager manager = new DBManager();

		BeanDistanta distanta = new BeanDistanta();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getDateCalculDistanta(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, dataStart);
			stmt.setString(2, dataStop);
			stmt.setString(3, nrMasina);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			LatLng startPoint = new LatLng(0, 0);
			LatLng stopPoint = new LatLng(0, 0);

			int distTemp = 0;
			double distTotal = 0;
			double bordStart = 0, bordStop = 0;
			while (rs.next()) {

				if (startPoint.lat == 0) {
					startPoint.lat = rs.getDouble(2);
					startPoint.lng = rs.getDouble(3);

					bordStart = rs.getDouble(4);

				}

				if (rs.getDouble(2) != startPoint.lat) {
					stopPoint.lat = rs.getDouble(2);
					stopPoint.lng = rs.getDouble(3);

					try {
						distTemp = MapUtils.getGoogleDistance(startPoint, stopPoint);
						distTotal += distTemp;
					} catch (Exception e) {
						String extraInfo = dataStart + " , " + dataStop + " , " + nrMasina;
						logger.error(Utils.getStackTrace(e, extraInfo));

					}

					startPoint = stopPoint;
					stopPoint = new LatLng(0, 0);

					bordStop = rs.getDouble(4);

				}

			}

			distanta.setKmGoogle(distTotal / 1000);
			distanta.setKmBord(bordStop - bordStart);

			return distanta.toString();

		}

	}

}
