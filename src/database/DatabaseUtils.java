package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.Utils;

public class DatabaseUtils {

	private static final Logger logger = LogManager.getLogger(DatabaseUtils.class);

	public static void closeConnections(PreparedStatement stmt) {

		try {

			if (stmt != null)
				stmt.close();

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e, null));
		}

	}

	public static void closeConnections(ResultSet rs, PreparedStatement stmt) {

		try {

			if (rs != null)
				rs.close();

			if (stmt != null)
				stmt.close();

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e, null));
		}

	}

}
