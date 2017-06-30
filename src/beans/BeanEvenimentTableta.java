package beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import utils.UtilsFormatting;

public class BeanEvenimentTableta {

	private String client;
	private String eveniment;
	private String codAdresa;
	private String data;
	private String ora;
	private String gps;
	private int kmBord;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getEveniment() {
		return eveniment;
	}

	public void setEveniment(String eveniment) {
		this.eveniment = eveniment;
	}

	public String getData() {
		return data;
	}

	public Date getDateObj() {
		String dataEv = getData() + " " + getOra();
		Date evDate = null;
		try {
			evDate = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US).parse(UtilsFormatting.formatDateSimple(dataEv));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return evDate;

	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public int getKmBord() {
		return kmBord;
	}

	public void setKmBord(int kmBord) {
		this.kmBord = kmBord;
	}

	@Override
	public String toString() {
		return "BeanEvenimentTableta [client=" + client + ", eveniment=" + eveniment + ", codAdresa=" + codAdresa + ", data=" + data + ", ora=" + ora + ", gps="
				+ gps + ", kmBord=" + kmBord + "]";
	}

}
