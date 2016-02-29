package beans;

import enums.EnumTipClient;

public class PozitieClient implements Comparable<PozitieClient> {

	private int poz;
	private String codClient;
	private String numeClient;
	private double latitudine;
	private double longitudine;
	private int distantaCamion;
	private boolean isStartBord;
	private boolean isStopBord;
	private EnumTipClient tipClient;
	private int kmBord;
	private String codAdresa;
	private String numeClientGed;

	public PozitieClient() {

	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public int getDistantaCamion() {
		return distantaCamion;
	}

	public void setDistantaCamion(int distantaCamion) {
		this.distantaCamion = distantaCamion;
	}

	public boolean isStartBord() {
		return isStartBord;
	}

	public void setStartBord(boolean isStartBord) {
		this.isStartBord = isStartBord;
	}

	public boolean isStopBord() {
		return isStopBord;
	}

	public void setStopBord(boolean isStopBord) {
		this.isStopBord = isStopBord;
	}

	public int getPoz() {
		return poz;
	}

	public void setPoz(int poz) {
		this.poz = poz;
	}

	public EnumTipClient getTipClient() {
		return tipClient;
	}

	public void setTipClient(EnumTipClient tipClient) {
		this.tipClient = tipClient;
	}

	public int getKmBord() {
		return kmBord;
	}

	public void setKmBord(int kmBord) {
		this.kmBord = kmBord;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public String getNumeClientGed() {
		return numeClientGed;
	}

	public void setNumeClientGed(String numeClientGed) {
		this.numeClientGed = numeClientGed;
	}

	@Override
	public String toString() {
		return "PozitieClient [poz=" + poz + ", codClient=" + codClient + ", numeClient=" + numeClient + ", latitudine=" + latitudine + ", longitudine="
				+ longitudine + ", distantaCamion=" + distantaCamion + ", isStartBord=" + isStartBord + ", isStopBord=" + isStopBord + ", tipClient="
				+ tipClient + ", kmBord=" + kmBord + ", codAdresa=" + codAdresa + ", numeClientGed=" + numeClientGed + "]";
	}

	@Override
	public int compareTo(PozitieClient o) {
		return this.poz - o.poz;
	}

}
