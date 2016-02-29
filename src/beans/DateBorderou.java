package beans;

public class DateBorderou {

	public String dataEmitere;
	public String nrMasina;

	public String getDataEmitere() {
		return dataEmitere;
	}

	public void setDataEmitere(String dataEmitere) {
		this.dataEmitere = dataEmitere;
	}

	public String getNrMasina() {
		return nrMasina;
	}

	public void setNrMasina(String nrMasina) {
		this.nrMasina = nrMasina;
	}

	@Override
	public String toString() {
		return "DateBorderou [dataEmitere=" + dataEmitere + ", nrMasina=" + nrMasina + "]";
	}

}
