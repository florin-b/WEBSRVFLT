package beans;

public class RezultatTraseu implements Comparable<RezultatTraseu> {

	private int poz;
	private String codClient;
	private String numeClient;
	private PozitieGps sosire;
	private PozitieGps plecare;
	private int kmBord;
	private String codAdresa;
	private String numeClientGed;

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public PozitieGps getSosire() {

		return sosire;
	}

	public void setSosire(PozitieGps sosire) {
		this.sosire = sosire;
	}

	public PozitieGps getPlecare() {
		return plecare;
	}

	public void setPlecare(PozitieGps plecare) {
		this.plecare = plecare;
	}

	public int getKmBord() {
		return kmBord;
	}

	public void setKmBord(int kmBord) {
		this.kmBord = kmBord;
	}

	public int getPoz() {
		return poz;
	}

	public void setPoz(int poz) {
		this.poz = poz;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codAdresa == null) ? 0 : codAdresa.hashCode());
		result = prime * result + ((codClient == null) ? 0 : codClient.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RezultatTraseu other = (RezultatTraseu) obj;
		if (codAdresa == null) {
			if (other.codAdresa != null)
				return false;
		} else if (!codAdresa.equals(other.codAdresa))
			return false;
		if (codClient == null) {
			if (other.codClient != null)
				return false;
		} else if (!codClient.equals(other.codClient))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RezultatTraseu [poz=" + poz + ", codClient=" + codClient + ", numeClient=" + numeClient + ", numeClientGed=" + numeClientGed + ", codAdresa="
				+ codAdresa + ", sosire=" + sosire + ", plecare=" + plecare + ", kmBord=" + kmBord + "]\n";
	}

	public int compareTo(RezultatTraseu that) {
		return this.poz - that.poz;
	}

}
