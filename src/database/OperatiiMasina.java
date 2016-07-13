package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import beans.BeanClientAlarma;
import beans.BeanEvenimentStop;
import beans.BeanEvenimentTableta;
import beans.BeanStatusMotor;
import beans.PozitieClient;
import beans.PozitieGps;
import enums.EnumCoordClienti;
import enums.EnumStatusMotor;
import queries.SqlQueries;
import utils.Constants;
import utils.MapUtils;
import utils.UtilsFormatting;

public class OperatiiMasina {
	public BeanStatusMotor getStatusMotor(String nrMasina) throws SQLException {
		DBManager manager = new DBManager();
		BeanStatusMotor status = null;
		EnumStatusMotor enumStatus = null;

		try (Connection conn = manager.getProdDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlQueries.getStareMotor())) {

			stmt.setString(1, nrMasina);

			if (stmt.execute()) {
				ResultSet rs = stmt.getResultSet();

				while (rs.next()) {

					status = new BeanStatusMotor();

					if (rs.getString("engine_on").equals("0"))
						enumStatus = EnumStatusMotor.ON;
					else
						enumStatus = EnumStatusMotor.OFF;

					PozitieGps pozitie = new PozitieGps();
					pozitie.setLatitudine(rs.getDouble("latitude"));
					pozitie.setLongitudine(rs.getDouble("longitude"));

					status.setStatus(enumStatus);
					status.setPozitie(pozitie);
					status.setIdEveniment(rs.getLong("id"));
				}
			}

		}

		return status;

	}

	public boolean isEvenimentStop(String codBorderou, long idEveniment) throws SQLException {
		DBManager manager = new DBManager();
		boolean found = false;

		try (Connection conn = manager.getProdDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlQueries.getEvenimentStop())) {

			stmt.setString(1, codBorderou);
			stmt.setString(2, String.valueOf(idEveniment));

			if (stmt.execute()) {
				ResultSet rs = stmt.getResultSet();

				rs.next();
				found = rs.getInt(1) == 0 ? false : true;

			}

		}

		return found;

	}

	public String getIdDevice(String nrMasina) {
		DBManager manager = new DBManager();

		String deviceId = "";

		String sqlString = " select id from gps_masini where nr_masina = replace(?,'-','') ";

		try (Connection conn = manager.getProdDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlString)) {

			stmt.setString(1, nrMasina);

			stmt.execute();
			ResultSet rs = stmt.getResultSet();

			if (rs.next())
				deviceId = rs.getString("id");

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return deviceId;

	}

	public BeanEvenimentStop getInfoEvenimentStop(String codBorderou) throws SQLException {

		List<BeanClientAlarma> listaClientiAlarma = new ArrayList<>();

		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();
		BeanEvenimentStop evenimentStop = new BeanEvenimentStop();

		String nrMasina = UtilsFormatting.formatNrMasina(operatiiTraseu.getDateBorderou(codBorderou).getNrMasina());

		BeanStatusMotor status = new OperatiiMasina().getStatusMotor(nrMasina);

		if (status == null)
			return evenimentStop;

		boolean existaEvenimentStop = isEvenimentStop(codBorderou, status.getIdEveniment());

		if (status.getStatus() == EnumStatusMotor.OFF && !existaEvenimentStop) {

			List<PozitieClient> pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.NEVIZITATI);

			OperatiiBorderou operatiiBorderou = new OperatiiBorderou();
			List<BeanEvenimentTableta> listEvenimente = operatiiBorderou.getEvenimenteTableta(codBorderou);

			if (status != null) {
				double distanta = 0;

				for (PozitieClient pozitie : pozitiiClienti) {

					if (!pozitie.isStartBord() && !pozitie.isStopBord()) {
						distanta = MapUtils.distanceXtoY(status.getPozitie().getLatitudine(), status.getPozitie().getLongitudine(), pozitie.getLatitudine(),
								pozitie.getLongitudine(), "K");

						if (distanta >= Constants.RAZA_CLIENT_KM)
							if (!existaEveniment(listEvenimente, pozitie, status.getStatus())) {
								BeanClientAlarma clientAlarma = new BeanClientAlarma();
								clientAlarma.setCodBorderou(codBorderou);
								clientAlarma.setCodClient(pozitie.getCodClient());
								clientAlarma.setCodAdresa(pozitie.getCodAdresa());
								clientAlarma.setNumeClient(pozitie.getNumeClient());
								listaClientiAlarma.add(clientAlarma);
							}

					}

				}
			}

			evenimentStop.setIdEveniment(status.getIdEveniment());
			evenimentStop.setEvenimentSalvat(existaEvenimentStop);
			evenimentStop.setClientiAlarma(listaClientiAlarma);

		}

		return evenimentStop;

	}

	private boolean existaEveniment(List<BeanEvenimentTableta> listEvenimente, PozitieClient client, EnumStatusMotor statusMotor) {

		for (BeanEvenimentTableta eveniment : listEvenimente) {
			if (statusMotor == EnumStatusMotor.OFF) {
				if (eveniment.getClient().equals(client.getCodClient()) && eveniment.getCodAdresa().equals(client.getCodAdresa())
						&& eveniment.getEveniment().equalsIgnoreCase("S"))
					return true;

			}

		}

		return false;

	}

	public String serializeEvenimentStop(BeanEvenimentStop evenimentStop) {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("evenimentSalvat", evenimentStop.isEvenimentSalvat());
		jsonObject.put("idEveniment", evenimentStop.getIdEveniment());

		JSONArray jsonClienti = new JSONArray();

		Iterator<BeanClientAlarma> iterator = evenimentStop.getClientiAlarma().iterator();

		JSONObject objClient = null;
		BeanClientAlarma client = null;
		while (iterator.hasNext()) {
			client = iterator.next();
			objClient = new JSONObject();

			objClient.put("codBorderou", client.getCodBorderou());
			objClient.put("codClient", client.getCodClient());
			objClient.put("codAdresa", client.getCodAdresa());
			objClient.put("numeClient", client.getNumeClient());
			jsonClienti.put(objClient);
		}

		jsonObject.put("clienti", jsonClienti.toString());

		return jsonObject.toString();
	}

}
