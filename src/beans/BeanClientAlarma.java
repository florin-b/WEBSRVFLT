package beans;

import java.io.Serializable;

public class BeanClientAlarma implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codBorderou;
	private String codClient;
	private String codAdresa;
	private String numeClient;

	public String getCodBorderou() {
		return codBorderou;
	}

	public void setCodBorderou(String codBorderou) {
		this.codBorderou = codBorderou;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	@Override
	public String toString() {
		return "BeanClientAlarma [codBorderou=" + codBorderou + ", codClient=" + codClient + ", codAdresa=" + codAdresa + ", numeClient=" + numeClient + "]";
	}

}
