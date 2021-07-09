package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.jdbc.pool.OracleDataSource;
import utils.Utils;

public class DBManager {

	private static final Logger logger = LogManager.getLogger(DBManager.class);

	public DataSource getProdDataSource1() {
		InitialContext initContext;
		DataSource ds = null;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/myoracle_prod");
		} catch (NamingException e) {
			logger.error(Utils.getStackTrace(e, null));
		}

		return ds;
	}

	public DataSource getProdDataSource_old() {

		OracleDataSource oracleDS = null;
		try {
			oracleDS = new OracleDataSource();
			oracleDS.setURL("jdbc:oracle:thin:@10.1.3.94:1521:prd002");
			oracleDS.setUser("WEBSAP");
			oracleDS.setPassword("2INTER7");
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e, null));
		}
		return oracleDS;
	}
	
	
	public DataSource getProdDataSource() {

		OracleDataSource oracleDS = null;
		try {

			oracleDS = new OracleDataSource();
			oracleDS.setURL("jdbc:oracle:thin:@10.1.3.76:1521/PRD");
			oracleDS.setUser("WEBSAP");
			oracleDS.setPassword("2INTER7");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return oracleDS;
	}	
	

	public DataSource getTestDataSource() {
		InitialContext initContext;
		DataSource ds = null;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/myoracle_test");
		} catch (NamingException e) {
			logger.error(Utils.getStackTrace(e, null));
		}

		return ds;
	}

	public DataSource getTestDataSource1() {
		Properties props = new Properties();
		FileInputStream fis = null;
		OracleDataSource oracleDS = null;
		try {
			fis = new FileInputStream("db.properties");
			props.load(fis);
			oracleDS = new OracleDataSource();
			oracleDS.setURL("jdbc:oracle:thin:@10.1.3.89:1527:tes");
			oracleDS.setUser("WEBSAP");
			oracleDS.setPassword("2INTER7");
		} catch (IOException | SQLException e) {
			logger.error(Utils.getStackTrace(e, null));
		}
		return oracleDS;
	}

	public static void closeConnection(ResultSet rs, Connection conn) {

		try {

			if (rs != null)
				rs.close();

			if (conn != null)
				conn.close();

		} catch (Exception ex) {
			logger.error(Utils.getStackTrace(ex, null));
		}

	}

	public static void closeConnection(PreparedStatement stmt, ResultSet rs, Connection conn) {

		try {

			if (stmt != null)
				stmt.close();

			if (rs != null)
				rs.close();

			if (conn != null)
				conn.close();

		} catch (Exception ex) {
			logger.error(Utils.getStackTrace(ex, null));
		}

	}

}
