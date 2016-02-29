package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.BeanEvenimentTableta;
import enums.EnumStatusMotor;
import queries.SqlQueries;

public class OperatiiBorderou {

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
			e.printStackTrace();
		}

		return listEvenimente;
	}

	

}
