package queries;

public class SqlQueries {

	public static String getStartStopBorderou() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.plecare, ");
		sqlString.append(" nvl((select b.latitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.plecare),'0,0') plec_lat, ");
		sqlString.append(" nvl((select b.longitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.plecare),'0,0') plec_long, a.adr_plecare, ");
		sqlString.append(" a.sosire, ");
		sqlString.append(" nvl((select b.latitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.sosire),'0,0') sosire_lat, ");
		sqlString.append(" nvl((select b.longitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and ");
		sqlString.append(" c.pct = a.sosire),'0,0') sosire_long, a.adr_sosire ");
		sqlString.append(" from websap.bord_ends a where a.tknum=?");

		return sqlString.toString();

	}

	public static String getBorderouActiv() {
		StringBuilder str = new StringBuilder();

		str.append(" select document from sapprd.zevenimentsofer where ");
		str.append(" document = (select numarb from ( ");
		str.append(" select numarb from websap.borderouri where sttrg in( 4, 6) ");
		str.append(" and cod_sofer =? order by sttrg desc,data_e asc) x where rownum<2) ");

		return str.toString();
	}

	public static String isBorderouMarkedStart() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select 1 from sapprd.zevenimentsofer where codSofer =? ");
		sqlString.append(" and document =? and client = document and eveniment = 'P' ");

		return sqlString.toString();
	}

	public static String getEvenimenteTableta() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select client, codadresa, eveniment, data, ora,  gps, fms from sapprd.zevenimentsofer ");
		sqlString.append(" where document =? order by data, ora");

		return sqlString.toString();
	}

	public static String getStareMotor() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(
				" select a.engine_on, c.latitude, c.longitude, a.id from gps_engine a, gps_masini b, gps_date c where b.nr_masina =? and a.device_id = b.id ");
		sqlString.append(" and c.id = a.id and a.id = (select max(id) from gps_engine where device_id = b.id) ");

		return sqlString.toString();
	}

	public static String getEvenimentStop() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select count(ideveniment) from  sapprd.zopririsoferi where codborderou =? and ideveniment=? ");

		return sqlString.toString();
	}

	public static String getCoordClientiBorderouAll() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.poz, c.nume, decode(a.cod_client,'', a.cod_furnizor, a.cod_client) cod_client, ");
		sqlString.append(" decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) cod_adresa,  b.city1, b.street, b.house_num1, b.region, a.name1, ");
		sqlString.append(" nvl(d.latitude,-1) latitude, nvl(d.longitude,-1) longitude ");
		sqlString.append(" from sapprd.zdocumentesms a, sapprd.adrc b, clienti c, sapprd.zcoordcomenzi d where a.nr_bord =:codBorderou ");
		sqlString.append(" and c.cod = a.cod_client ");
		sqlString.append(" and b.client = '900' and b.addrnumber = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) ");
		sqlString.append(" and d.idcomanda(+) = a.idcomanda order by a.poz ");

		return sqlString.toString();
	}

	public static String getCoordClientiNevisit() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select distinct a.poz, c.nume, decode(a.cod_client,'', a.cod_furnizor, a.cod_client) cod_client, ");
		sqlString.append(" decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) cod_adresa,  b.city1, b.street, b.house_num1, b.region, a.name1, ");
		sqlString.append(" nvl(d.latitude,-1) latitude, nvl(d.longitude,-1) longitude ");
		sqlString.append(" from sapprd.zdocumentesms a, sapprd.adrc b, clienti c, sapprd.zcoordcomenzi d, sapprd.zevenimentsofer z  ");
		sqlString.append(" where a.nr_bord =:codBorderou and c.cod = a.cod_client ");
		sqlString.append(" and b.client = '900' and b.addrnumber = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) ");
		sqlString.append(" and d.idcomanda(+) = a.idcomanda ");
		sqlString.append(" and z.document(+) = a.nr_bord and z.client(+) = a.cod_client ");
		sqlString.append(" and z.codadresa(+) = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) ");
		sqlString.append(" and  nvl(z.data,-1) = -1 ");
		sqlString.append(" order by a.poz ");

		return sqlString.toString();
	}

}
