package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BeanEvenimentStop implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean evenimentSalvat = true;
	private long idEveniment = 0;
	private List<BeanClientAlarma> clientiAlarma = new ArrayList<>();

	public boolean isEvenimentSalvat() {
		return evenimentSalvat;
	}

	public void setEvenimentSalvat(boolean evenimentSalvat) {
		this.evenimentSalvat = evenimentSalvat;
	}

	public long getIdEveniment() {
		return idEveniment;
	}

	public void setIdEveniment(long idEveniment) {
		this.idEveniment = idEveniment;
	}

	public List<BeanClientAlarma> getClientiAlarma() {
		return clientiAlarma;
	}

	public void setClientiAlarma(List<BeanClientAlarma> clientiAlarma) {
		this.clientiAlarma = clientiAlarma;
	}

	@Override
	public String toString() {
		return "BeanEvenimentStop [evenimentSalvat=" + evenimentSalvat + ", idEveniment=" + idEveniment + ", clientiAlarma=" + clientiAlarma + "]";
	}

}
