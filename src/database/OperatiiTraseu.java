package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import beans.BeanBoundBorderou;
import beans.CoordonateGps;
import beans.DateBorderou;
import beans.PozitieClient;
import beans.StandardAddress;
import beans.TraseuBorderou;
import enums.EnumCoordClienti;
import enums.EnumTipClient;
import queries.SqlQueries;
import utils.MapUtils;
import utils.Utils;
import utils.UtilsAdrese;
import utils.UtilsFormatting;

public class OperatiiTraseu {

	private static final Logger logger = LogManager.getLogger(OperatiiTraseu.class);

	public List<TraseuBorderou> getTraseuBorderou(DateBorderou dateBorderou) throws SQLException {

		DBManager manager = new DBManager();

		
		String sqlString = " select to_char(c.record_time,'dd-Mon-yy hh24:mi:ss', 'NLS_DATE_LANGUAGE = AMERICAN') datarec , c.latitude, c.longitude, nvl(c.mileage,0) kilo, " 
				 + " nvl(c.speed,0) viteza from websap.gps_masini b, websap.gps_date c " 
				 + " where ( b.nr_masina = replace(:nrMasina,'-','') or b.nr_masina = (select distinct replace(nrleasing,'-','') " 
				 + " from sapprd.aufk where replace(ktext,'-','')=replace(:nrMasina,'-','') and trim(nrleasing) != '' and phas1='X')) " 
				 + " and c.device_id = b.id " 
				 + " and c.record_time between to_date(:dataEmitere,'dd-mm-yy hh24:mi:ss','NLS_DATE_LANGUAGE = AMERICAN') " 
				 + " and to_date(:dataEmitere,'dd-mm-yy hh24:mi:ss','NLS_DATE_LANGUAGE = AMERICAN') + 6 order by c.record_time ";
		

		List<TraseuBorderou> listTraseu = new ArrayList<>();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, dateBorderou.getNrMasina());
			stmt.setString(2, dateBorderou.getNrMasina());
			stmt.setString(3, dateBorderou.getDataEmitere());
			stmt.setString(4, dateBorderou.getDataEmitere());

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				TraseuBorderou pozitie = new TraseuBorderou();
				pozitie.setDataInreg(rs.getString("datarec"));
				pozitie.setLatitudine(rs.getDouble("latitude"));
				pozitie.setLongitudine(rs.getDouble("longitude"));
				pozitie.setKm(rs.getInt("kilo"));
				pozitie.setViteza(rs.getInt("viteza"));
				listTraseu.add(pozitie);

			}

		}

		return listTraseu;

	}

	public List<TraseuBorderou> getTraseuMasina(String nrAuto, String dataStart, String dataStop) {

		DBManager manager = new DBManager();

		List<TraseuBorderou> listTraseu = new ArrayList<>();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(SqlQueries.getTraseuMasina(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, nrAuto);
			stmt.setString(2, dataStart);
			stmt.setString(3, dataStop);

			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				TraseuBorderou pozitie = new TraseuBorderou();
				pozitie.setLatitudine(rs.getDouble("latitude"));
				pozitie.setLongitudine(rs.getDouble("longitude"));
				pozitie.setKm(rs.getInt("kilo"));
				listTraseu.add(pozitie);

			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return listTraseu;

	}

	public List<PozitieClient> getCoordClientiBorderou(String codBorderou, EnumCoordClienti stareClienti) throws SQLException {
		List<PozitieClient> listPozitii = new ArrayList<>();

		DBManager manager = new DBManager();

		String sqlString = " ";

		if (stareClienti == EnumCoordClienti.TOTI)
			sqlString = SqlQueries.getCoordClientiBorderouAll();

		if (stareClienti == EnumCoordClienti.NEVIZITATI)
			sqlString = SqlQueries.getCoordClientiNevisit();

		try (Connection conn = manager.getProdDataSource().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

			stmt.setString(1, codBorderou);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();
			while (rs.next()) {

				if (rs.getString("nume").contains("INCHIRIAT"))
					continue;

				PozitieClient pozitie = new PozitieClient();
				pozitie.setPoz(Integer.valueOf(rs.getString("poz")));
				pozitie.setCodClient(rs.getString("cod_client"));

				if (!rs.getString("lat_fil").equals("-1")) {
					pozitie.setLatitudine(Double.valueOf(rs.getString("lat_fil").replace(",", ".")));
					pozitie.setLongitudine(Double.valueOf(rs.getString("long_fil").replace(",", ".")));
				} else {
					pozitie.setLatitudine(Double.valueOf(rs.getString("latitudine")));
					pozitie.setLongitudine(Double.valueOf(rs.getString("longitudine")));
				}

				pozitie.setNumeClient(rs.getString("nume"));
				pozitie.setCodAdresa(rs.getString("cod_adresa"));
				pozitie.setNumeClientGed(rs.getString("nume"));
				pozitie.setTipClient(EnumTipClient.DISTRIBUTIE);

				listPozitii.add(pozitie);
			}

		}

		if (listPozitii.size() > 1) {

			// eliminare ultima etapa
			listPozitii.remove(listPozitii.size() - 1);

			List<PozitieClient> listBorder = null;
			try {
				listBorder = getStartStopBorderou(codBorderou);
			} catch (SQLException e) {
				logger.error(Utils.getStackTrace(e, codBorderou));
			}

			listPozitii.add(0, listBorder.get(0));
			listPozitii.add(listPozitii.size(), listBorder.get(1));
			listBorder.get(1).setPoz(listPozitii.size() - 1);

		}

		return listPozitii;
	}

	public List<PozitieClient> getCoordClientiBorderou1(String codBorderou, EnumCoordClienti stareClienti) {

		List<PozitieClient> listPozitii = null;
		DBManager manager = new DBManager();

		String sqlString = " ";

		if (stareClienti == EnumCoordClienti.TOTI)
			sqlString = SqlQueries.getCoordClientiBorderouAll();

		if (stareClienti == EnumCoordClienti.NEVIZITATI)
			sqlString = SqlQueries.getCoordClientiNevisit();

		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("codBorderou", codBorderou);

		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(manager.getProdDataSource());

		listPozitii = jdbc.query(sqlString, parameter, new RowMapper<PozitieClient>() {
			CoordonateGps coordonate = null;

			public PozitieClient mapRow(ResultSet rs, int rowNum) throws SQLException {

				PozitieClient pozitie = new PozitieClient();
				pozitie.setPoz(Integer.valueOf(rs.getString("poz")));
				pozitie.setCodClient(rs.getString("cod_client"));

				if (rs.getString("latitude").equals("-1")) {
					coordonate = getCoordonate(rs.getString("region"), rs.getString("city1"), rs.getString("house_num1"), rs.getString("street"));

					pozitie.setLatitudine(coordonate.getLatitude());
					pozitie.setLongitudine(coordonate.getLongitude());

				} else {
					pozitie.setLatitudine(Double.valueOf(rs.getString("latitude")));
					pozitie.setLongitudine(Double.valueOf(rs.getString("longitude")));
				}

				pozitie.setNumeClient(rs.getString("nume"));
				pozitie.setCodAdresa(rs.getString("cod_adresa"));
				pozitie.setNumeClientGed(rs.getString("name1"));
				pozitie.setTipClient(EnumTipClient.DISTRIBUTIE);
				return pozitie;
			}
		});

		List<PozitieClient> listBorder = null;
		try {
			listBorder = getStartStopBorderou(codBorderou);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codBorderou));
		}

		listPozitii.add(0, listBorder.get(0));
		listPozitii.add(listPozitii.size(), listBorder.get(1));
		listBorder.get(1).setPoz(listPozitii.size() - 1);

		return listPozitii;

	}

	public List<PozitieClient> getFilialaStartBorderou(String codBorderou) {

		List<PozitieClient> listPozitii = new ArrayList<>();

		List<PozitieClient> listBorder = null;
		try {
			listBorder = getStartStopBorderou(codBorderou);
		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codBorderou));
		}

		if (listBorder != null)
			listPozitii.add(0, listBorder.get(0));

		return listPozitii;

	}

	private CoordonateGps getCoordonate(String codJudet, String localitate, String strada, String nrStrada) {
		CoordonateGps coordonate = null;

		StandardAddress address = new StandardAddress();

		address.setStreet(strada);
		address.setNumber(nrStrada);
		address.setSector(UtilsAdrese.getNumeJudet(codJudet));
		address.setCity(localitate);

		try {
			coordonate = MapUtils.geocodeAddress(address);
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e, address.toString()));
		}

		if (coordonate == null)
			return new CoordonateGps();
		return coordonate;
	}

	public DateBorderou getDateBorderou(String codBorderou) throws SQLException {

		DBManager manager = new DBManager();
		DateBorderou dateBorderou = new DateBorderou();

		try (Connection conn = manager.getProdDataSource().getConnection();
				CallableStatement callableStatement = conn.prepareCall("{ call websap.set_borderou_context(?) }");) {

			callableStatement.setString(1, codBorderou);
			callableStatement.execute();

			PreparedStatement stmt = conn.prepareStatement(SqlQueries.getDataEmitereBorderou(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.executeQuery();

			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {

				dateBorderou.setDataEmitere(UtilsFormatting.formatDateLocal(rs.getString("dataEmitere")));
				dateBorderou.setNrMasina(rs.getString("masina"));

			}

			stmt.close();

		} catch (SQLException e) {
			logger.error(Utils.getStackTrace(e, codBorderou));
		}

		return dateBorderou;
	}

	public List<PozitieClient> getStartStopBorderou(String codBorderou) throws SQLException {

		DBManager manager = new DBManager();
		Connection conn = manager.getProdDataSource().getConnection();

		String sqlString = SqlQueries.getStartStopBorderou();

		PreparedStatement stmt = conn.prepareStatement(sqlString);

		stmt.setString(1, codBorderou);

		ResultSet rs = stmt.executeQuery();

		List<PozitieClient> listPozitii = new ArrayList<>();
		PozitieClient pozitie;
		String adresa;
		while (rs.next()) {

			pozitie = new PozitieClient();

			adresa = rs.getString("adr_plecare");

			pozitie.setPoz(0);
			pozitie.setCodClient(rs.getString("plecare"));
			pozitie.setTipClient(getTipClient(pozitie));

			if (Double.valueOf(rs.getString("plec_lat").replace(",", ".")) > 0) {
				pozitie.setLatitudine(Double.valueOf(rs.getString("plec_lat").replace(",", ".")));
				pozitie.setLongitudine(Double.valueOf(rs.getString("plec_long").replace(",", ".")));
			} else {
				setCoordBound(pozitie, adresa);
			}
			pozitie.setNumeClient("Start borderou " + getBoundBord(adresa).getNumeClient());
			pozitie.setStartBord(true);
			listPozitii.add(pozitie);

			pozitie = new PozitieClient();
			adresa = rs.getString("adr_sosire");

			pozitie.setPoz(100);
			pozitie.setCodClient(rs.getString("sosire") + " ");
			pozitie.setTipClient(getTipClient(pozitie));

			if (Double.valueOf(rs.getString("sosire_lat").replace(",", ".")) > 0) {
				pozitie.setLatitudine(Double.valueOf(rs.getString("sosire_lat").replace(",", ".")));
				pozitie.setLongitudine(Double.valueOf(rs.getString("sosire_long").replace(",", ".")));
			} else {
				setCoordBound(pozitie, adresa);
			}
			pozitie.setNumeClient("Stop borderou " + getBoundBord(adresa).getNumeClient());
			pozitie.setStopBord(true);
			listPozitii.add(pozitie);

		}

		DBManager.closeConnection(stmt, rs, conn);

		return listPozitii;

	}

	private void setCoordBound(PozitieClient pozitieClient, String dateCoord) {

		if (dateCoord != null && dateCoord.contains("#")) {

			CoordonateGps coord = new CoordonateGps(0, 0);

			try {
				coord = MapUtils.geocodeAddress(getAddress(dateCoord));
			} catch (Exception e) {
				String extraInfo = pozitieClient + " , " + dateCoord;
				logger.error(Utils.getStackTrace(e, extraInfo));
			}

			pozitieClient.setLatitudine(coord.getLatitude());
			pozitieClient.setLongitudine(coord.getLongitude());

		}

	}

	private BeanBoundBorderou getBoundBord(String adresa) {
		BeanBoundBorderou boundBord = new BeanBoundBorderou();

		if (adresa != null && adresa.contains("#")) {
			String[] tokAdresa = adresa.split("#");
			boundBord.setNumeClient(tokAdresa[0]);
			boundBord.setAddress(getAddress(adresa));

		}

		return boundBord;
	}

	private StandardAddress getAddress(String valAddress) {
		StandardAddress address = new StandardAddress();

		if (valAddress != null && valAddress.contains("#")) {
			String[] tokAdresa = valAddress.split("#");

			address.setSector(UtilsAdrese.getNumeJudet(tokAdresa[1]));
			address.setCity(tokAdresa[2]);

			if (tokAdresa.length >= 3)
				address.setStreet(tokAdresa[3]);

			if (tokAdresa.length >= 4)
				address.setNumber(tokAdresa[4]);

		}

		return address;
	}

	private EnumTipClient getTipClient(PozitieClient pozitieClient) {
		if (pozitieClient.getCodClient().trim().length() == 4)
			return EnumTipClient.FILIALA;
		else
			return EnumTipClient.DISTRIBUTIE;
	}

}
