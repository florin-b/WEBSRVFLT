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
		sqlString.append(" nvl((select b.latitude from sapprd.zgpsdepcoord b where b.tdlnr = a.sosire ");
		sqlString.append(" ),'0,0') sosire_lat, ");
		sqlString.append(" nvl((select b.longitude from sapprd.zgpsdepcoord b where b.tdlnr = a.sosire ");
		sqlString.append(" ),'0,0') sosire_long, a.adr_sosire ");
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
	
	
	public static String getBorderouActivMasina() {
		StringBuilder str = new StringBuilder();

		
		str.append(" select numarb from ( ");
		str.append(" select numarb from websap.borderouri where sttrg in( 4, 6) ");
		str.append(" and masina =? order by sttrg desc,data_e asc) x where rownum<2 ");

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
		sqlString.append(" where document =? order by data, ora ");

		return sqlString.toString();
	}

	public static String getStareMotor() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.engine_on, c.latitude, c.longitude, a.id from gps_engine a, gps_masini b, gps_date c where b.nr_masina =? "
				+ " and a.device_id = b.id and c.speed = 0 ");
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
		sqlString.append(" nvl(e.latitudine,'-1') latitudine, nvl(e.longitudine,'-1') longitudine, nvl(f.latitude,'-1') lat_fil, nvl(f.longitude,'-1') long_fil ");
		sqlString.append(" from sapprd.zdocumentesms a, sapprd.adrc b, clienti c, sapprd.zcoordcomenzi d, sapprd.zcoordadrese e, sapprd.ZGPSDEPCOORD f " );
		sqlString.append(" where a.nr_bord =:codBorderou ");
		sqlString.append(" and c.cod = a.cod_client and e.idadresa(+) = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) ");
		sqlString.append(" and b.client = '900' and f.tdlnr(+) = a.cod_client and b.addrnumber = decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) ");
		sqlString.append(" and d.idcomanda(+) = a.idcomanda order by a.poz ");

		
		return sqlString.toString();
	}

	public static String getCoordClientiNevisit() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select distinct a.poz, c.nume, decode(a.cod_client,'', a.cod_furnizor, a.cod_client) cod_client, ");
		sqlString.append(" decode(a.cod_client,'',a.adresa_furnizor, a.adresa_client) cod_adresa,  b.city1, b.street, b.house_num1, b.region, a.name1, ");
		sqlString.append(" decode(length(a.coord_gps_client),1,'0,0',a.coord_gps_client) coord_client ");
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

	public static String getCoordEventFromArchive() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select latitude, longitude, nvl(mileage,0) mileage from ( ");
		sqlString.append(" select latitude, longitude, mileage, ");
		sqlString.append(" (to_date(?, 'yyyymmdd hh24miss') - record_time) * 24 * 60  diff from gps_date where device_id = ");
		sqlString.append(" (select id from gps_masini where nr_masina = (select replace(masina, '-', '') from websap.borderouri where numarb =?)) and ");
		sqlString.append(" record_time between(to_date(?, 'yyyymmdd hh24miss') - 15 / (24 * 60)) and(to_date(?, 'yyyymmdd hh24miss') + 15 / (24 * 60)) ");
		sqlString.append(" and(to_date(?, 'yyyymmdd hh24miss') - record_time) * 24 * 60 >= 0 order by diff ) where rownum< 2 ");

		return sqlString.toString();
	}

	public static String getDateBorderouSofer() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.data, a.ora, b.masina from sapprd.zevenimentsofer a, borderouri b where a.document =? ");
		sqlString.append(" and a.client = a.document and a.eveniment = 'P' and b.numarb = a.document ");

		return sqlString.toString();
	}

}
