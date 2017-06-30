package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.CoordonateGps;
import database.DBManager;
import database.DatabaseUtils;
import utils.MapUtils;
import utils.Utils;

public class AdreseService {

	private static final Logger logger = LogManager.getLogger(AdreseService.class);

	private boolean saveCoordAdresa(Connection conn, String codAdresa, CoordonateGps coords) {
		boolean status = false;

		String sqlString = " insert into sapprd.zcoordadrese (mandt, idadresa, latitudine, longitudine) values ('900',?,?,?) ";

		try {
			PreparedStatement stmt = conn.prepareStatement(sqlString);

			stmt.setString(1, codAdresa);
			stmt.setString(2, String.valueOf(coords.getLatitude()));
			stmt.setString(3, String.valueOf(coords.getLongitude()));

			status = stmt.execute();

			DatabaseUtils.closeConnections(stmt);

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codAdresa));
		}

		return status;

	}

	private CoordonateGps getCoordAddressFromDB(Connection conn, String codAdresa) {
		CoordonateGps coords = new CoordonateGps();

		String sqlString = " select latitudine, longitudine from sapprd.zcoordadrese where idadresa = ? ";

		try {
			PreparedStatement stmt = conn.prepareStatement(sqlString);

			stmt.setString(1, codAdresa);

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				coords.setLatitude(Double.parseDouble(rs.getString("latitudine")));
				coords.setLongitude(Double.parseDouble(rs.getString("longitudine")));
			}

			DatabaseUtils.closeConnections(rs, stmt);

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codAdresa));
		}

		return coords;
	}

	public String getCoordAddress(String codJudet, String localitate, String strada, String numar, String codAddress) throws SQLException {

		String coordonate;

		Connection conn = new DBManager().getTestDataSource1().getConnection();

		CoordonateGps coords = getCoordAddressFromDB(conn, codAddress);

		if (coords.getLatitude() == 0) {
			coordonate = MapUtils.getCoordAddressFromService(codJudet, localitate, strada, numar);
			CoordonateGps coordsNew = new CoordonateGps(Double.parseDouble(coordonate.split(",")[0]), Double.parseDouble(coordonate.split(",")[1]));
			saveCoordAdresa(conn, codAddress, coordsNew);

		} else {
			coordonate = coords.getLatitude() + "," + coords.getLongitude();
		}

		conn.close();

		return coordonate;
	}

}
