package queries;

public class SqlQueries {

	public static String getStartStopBorderou() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.plecare,");
		sqlString.append(" (case a.tip_plecare ");
		sqlString.append(
				"   when 'VSTEL' then (nvl((select b.latitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and c.pct = a.plecare),'0,0')) ");
		sqlString.append("    when 'WERKA' then (nvl((select b.latitude from sapprd.zgpsdepcoord b where b.tdlnr = a.plecare),'0,0')) ");
		sqlString.append("   else '0,0' end) plec_lat, ");
		sqlString.append(" (case a.tip_plecare ");
		sqlString.append(
				"    when 'VSTEL' then (nvl((select b.longitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and c.pct = a.plecare),'0,0')) ");
		sqlString.append("   when 'WERKA' then (nvl((select b.longitude from sapprd.zgpsdepcoord b where b.tdlnr = a.plecare),'0,0')) ");
		sqlString.append("    else '0,0' end) plec_long, ");
		sqlString.append(" a.adr_plecare, ");
		sqlString.append(" a.sosire, ");
		sqlString.append("(case a.tip_sosire ");
		sqlString.append(
				"   when 'VSTEZ' then (nvl((select b.latitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and c.pct = a.sosire),'0,0')) ");
		sqlString.append("   when 'WERKZ' then (nvl((select b.latitude from sapprd.zgpsdepcoord b where b.tdlnr = a.sosire),'0,0')) ");
		sqlString.append("    else '0,0' end) sosire_lat, ");
		sqlString.append(" (case a.tip_sosire ");
		sqlString.append(
				"   when 'VSTEZ' then (nvl((select b.longitude from sapprd.zgpsdepcoord b, sapprd.zgpsdep c where b.id = c.gpsid and c.pct = a.sosire),'0,0')) ");
		sqlString.append("  when 'WERKZ' then (nvl((select b.longitude from sapprd.zgpsdepcoord b where b.tdlnr = a.sosire),'0,0')) ");
		sqlString.append("  else '0,0' end) sosire_long, ");
		sqlString.append(" a.adr_sosire ");
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

	public static String getCoordClientiBorderouAll_old() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select a.poz, a.nume, a.cod cod_client,");
		sqlString.append(" a.adresa cod_adresa,  b.city1, b.street,");
		sqlString.append(" b.house_num1, b.region,  nvl(trim(e.latitude),'-1') latitudine,");
		sqlString.append(" nvl(trim(e.longitude),'-1') longitudine, nvl(f.latitude,'-1') lat_fil,");
		sqlString.append(" nvl(f.longitude,'-1') long_fil  from sapprd.zdocumentebord a,");
		sqlString.append(" sapprd.adrc b,   sapprd.zadreseclienti e,");
		sqlString.append(" sapprd.ZGPSDEPCOORD f  where a.nr_bord =?  ");
		sqlString.append(" and e.codclient(+) = a.cod and e.codadresa(+)= a.adresa ");
		sqlString.append(" and b.client = '900' and f.tdlnr(+) = a.cod ");
		sqlString.append(" and b.addrnumber = a.adresa ");
		sqlString.append(" order by a.poz ");

		return sqlString.toString();
	}
	
	
	public static String getCoordClientiBorderouAll() {
		
		StringBuilder sqlString = new StringBuilder();
		
		sqlString.append(" select a.poz, a.nume, a.cod cod_client, a.adresa cod_adresa,  b.city1, b.street, b.house_num1, b.region, ");
		sqlString.append(" nvl(trim(e.latitude),nvl( ");
		sqlString.append(" (select am.lat_ ");
		sqlString.append(" from sapprd.zlips_amb am join sapprd.lips l on am.mandt=l.mandt and am.vbeln=l.vbeln and am.posnr=l.posnr ");
		sqlString.append(" join sapprd.vbpa p on p.mandt=l.mandt and p.vbeln=l.vbeln and p.PARVW = 'WE' ");
		sqlString.append(" join sapprd.vttp b on am.mandt=b.mandt and am.vbeln=b.vbeln ");
		sqlString.append(" where am.LONG_ <>' ' and am.lat_ <> ' ' and  am.LONG_ <>'0' and am.lat_ <> '0'  and am.mandt='900' and ROWNUM = 1 ");
		sqlString.append(" and b.tknum = :bord and p.kunnr = a.cod and p.adrnr = a.adresa), ");
		sqlString.append(" nvl( ");
		sqlString.append(" (select am.LAT_PRED ");
		sqlString.append(" from sapprd.ZCOM_AMB am join sapprd.ZCOMM l on am.mandt=l.mandt and am.docn=l.docn and am.DOCP=l.DOCP and am.ETENR=l.ETENR ");
		sqlString.append(" join sapprd.zcomdti b on am.mandt=b.mandt and l.nrcom=b.nr ");
		sqlString.append(" where am.Long_PRED <>' ' and am.LAT_PRED <> ' ' and  am.Long_PRED <>'0' and am.LAT_PRED <> '0'  and am.mandt='900' and ROWNUM = 1 ");
		sqlString.append(" and b.nrborderou = :bord and ((l.LIFNA = a.cod and l.ADRNA = a.adresa) or(l.KUNNZ = a.cod and l.ADRNZ = a.adresa))) ");
		sqlString.append(" ,'-1'))) latitudine, ");
		sqlString.append(" nvl(trim(e.longitude),nvl((select am.long_ ");
		sqlString.append(" from sapprd.zlips_amb am join sapprd.lips l on am.mandt=l.mandt and am.vbeln=l.vbeln and am.posnr=l.posnr ");
		sqlString.append(" join sapprd.vbpa p on p.mandt=l.mandt and p.vbeln=l.vbeln and p.PARVW = 'WE' ");
		sqlString.append(" join sapprd.vttp b on am.mandt=b.mandt and am.vbeln=b.vbeln ");
		sqlString.append(" where am.LONG_ <>' ' and am.lat_ <> ' ' and  am.LONG_ <>'0' and am.lat_ <> '0'  and am.mandt='900' and ROWNUM = 1 ");
		sqlString.append(" and b.tknum = :bord and p.kunnr = a.cod and p.adrnr = a.adresa), ");
		sqlString.append(" nvl( ");
		sqlString.append(" (select am.Long_PRED ");
		sqlString.append(" from sapprd.ZCOM_AMB am join sapprd.ZCOMM l on am.mandt=l.mandt and am.docn=l.docn and am.DOCP=l.DOCP and am.ETENR=l.ETENR ");
		sqlString.append(" join sapprd.zcomdti b on am.mandt=b.mandt and l.nrcom=b.nr ");
		sqlString.append(" where am.Long_PRED <>' ' and am.LAT_PRED <> ' ' and  am.Long_PRED <>'0' and am.LAT_PRED <> '0'  and am.mandt='900' and ROWNUM = 1 ");
		sqlString.append(" and b.nrborderou = :bord and ((l.LIFNA = a.cod and l.ADRNA = a.adresa) or(l.KUNNZ = a.cod and l.ADRNZ = a.adresa))) ");
		sqlString.append(" ,'-1'))) longitudine, nvl(f.latitude,'-1') lat_fil, ");
		sqlString.append(" nvl(f.longitude,'-1') long_fil  from sapprd.zdocumentebord a, sapprd.adrc b,   sapprd.zadreseclienti e, ");
		sqlString.append(" sapprd.ZGPSDEPCOORD f  where a.nr_bord = :bord   and e.codclient(+) = a.cod and e.codadresa(+)= a.adresa ");
		sqlString.append(" and b.client = '900' and f.tdlnr(+) = a.cod  and b.addrnumber = a.adresa  order by a.poz ");
		
		
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

	public static String getDateBorderou() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select max(to_char(trunc(to_date(data_e,'yyyymmdd')),'DD-MM-YYYY')||' '||to_char(to_date(ora_e,'HH24:MI:SS'),'HH24:MI:SS'))  ");
		sqlString.append(" as dataemitere,masina from ( ");
		sqlString.append(" select * from ( ");
		sqlString.append(" select daten as data_e, uaten as ora_e,m.exidv as masina ");
		sqlString.append(" from sapprd.vttk k join sapprd.vtpa p on p.vbeln = k.tknum and p.mandt=k.mandt ");
		sqlString.append(" join sapprd.vekp m on m.vpobjkey = k.tknum and m.mandt=k.mandt ");
		sqlString.append(" where p.pernr = (select distinct pernr from sapprd.vtpa where vbeln = ? and parvw = 'ZF') and k.mandt = '900' ");
		sqlString.append(" and daten||uaten > (select distinct data_e||ora_e from sapprd.zdocumentebord where nr_bord =?) and tknum <> ? ");
		sqlString.append(" order by daten asc, uaten asc) ");
		sqlString.append(" where rownum = 1 ");
		sqlString.append(" union ");
		sqlString.append(" select distinct d.data_e, d.ora_e,m.exidv as masina from sapprd.zdocumentebord d join sapprd.vekp m ");
		sqlString.append(" on m.vpobjkey = d.nr_bord and m.mandt=d.mandt ");
		sqlString.append(" where nr_bord =?) ");
		sqlString.append(" group by masina ");

		return sqlString.toString();
	}

	public static String getDataSosireFiliala() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(
				" select to_char(trunc(to_date(daten,'yyyymmdd')),'DD-Mon-YY','NLS_DATE_LANGUAGE = AMERICAN')||' '||to_char(to_date(uaten,'HH24:MI:SS'),'HH24:MI:SS') ");
		sqlString.append(" sosire from sapprd.vttk where mandt = '900' and daten <> '00000000' and tknum =? ");

		return sqlString.toString();
	}

	public static String getDataEmitereBorderou() {
		StringBuilder sqlString = new StringBuilder();
		sqlString.append("select masina, dataemitere from websap.date_borderou");
		return sqlString.toString();
	}

	public static String getDateCalculDistanta() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select to_char(record_time,'dd-Mon-yy hh24:mi:ss') data_rec, latitude, longitude, mileage ");
		sqlString.append(" from gps_date where ");
		sqlString.append(" record_time between to_date(?,'dd-mm-yy hh24:mi:ss') and to_date(?,'dd-mm-yy hh24:mi:ss') ");
		sqlString.append(" and device_id =(select distinct id from gps_masini where nr_masina=?) and mileage is not null  order by record_time ");

		return sqlString.toString();
	}

	public static String getDateLocalitate() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append("select a.razakm, b.latitudine, b.longitudine from SAPPRD.zoraseromania a, SAPPRD.zcoordlocalitati b ");
		sqlString.append(" where a.mandt='900' and b.mandt='900' and ");
		sqlString.append(" a.codjudet =? and a.numejudet = b.judet and  trim(upper(a.oras)) =? and a.oras = b.localitate");

		return sqlString.toString();
	}

	public static String getTraseuMasina() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select c.latitude, ");
		sqlString.append(" c.longitude, nvl(c.mileage,0) kilo from gps_masini b, gps_date c ");
		sqlString.append(" where b.nr_masina = replace(:nrMasina,'-','') and c.device_id = b.id  ");
		sqlString.append(" and c.record_time between to_date(:dataStart,'dd-mm-yy hh24:mi','NLS_DATE_LANGUAGE = AMERICAN') and ");
		sqlString.append(" to_date(:dataStop,'dd-mm-yy hh24:mi','NLS_DATE_LANGUAGE = AMERICAN') and c.mileage is not null order by c.record_time");

		return sqlString.toString();
	}
	
	public static String getTraseuMasinaPlus() {
		StringBuilder sqlString = new StringBuilder();

		sqlString.append(" select c.latitude, ");
		sqlString.append(" c.longitude, nvl(c.mileage,0) kilo, c.record_time, c.speed from gps_masini b, gps_date c ");
		sqlString.append(" where b.nr_masina = replace(:nrMasina,'-','') and c.device_id = b.id  ");
		sqlString.append(" and trunc(c.record_time) between to_date(:dataStart,'dd-mm-yy') and ");
		sqlString.append(" to_date(:dataStop,'dd-mm-yy') and c.mileage is not null order by c.record_time");

		return sqlString.toString();
	}
	
	public static String getCoordonateFiliala(){
		StringBuilder sqlString = new StringBuilder();
		sqlString.append(" select latitude, longitude from sapprd.ZGPSDEPCOORD where mandt = '900' ");
		sqlString.append(" and tdlnr = ? ");
		
		return sqlString.toString();
	}
	
	public static String getCoordonatePoligon(){
		StringBuilder sqlString = new StringBuilder();
		sqlString.append(" select latitude, longitude from sapprd.zpoligon_det d, sapprd.zpoligon_head h  ");
		sqlString.append(" where d.mandt = '900' and h.mandt = '900' and h.idpoligon = d.idpoligon and  ");
		sqlString.append(" h.pct = ? and h.tippoligon = ? and h.tip = ? ");
		sqlString.append(" order by poz ");
		
		return sqlString.toString();
		
	}

}
