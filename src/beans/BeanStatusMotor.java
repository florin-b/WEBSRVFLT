package beans;

import enums.EnumStatusMotor;

public class BeanStatusMotor {

	private long idEveniment;
	private EnumStatusMotor status;
	private PozitieGps pozitie;

	public EnumStatusMotor getStatus() {
		return status;
	}

	public void setStatus(EnumStatusMotor status) {
		this.status = status;
	}

	public PozitieGps getPozitie() {
		return pozitie;
	}

	public void setPozitie(PozitieGps pozitie) {
		this.pozitie = pozitie;
	}

	public long getIdEveniment() {
		return idEveniment;
	}

	public void setIdEveniment(long idEveniment) {
		this.idEveniment = idEveniment;
	}

	@Override
	public String toString() {
		return "BeanStatusMotor [idEveniment=" + idEveniment + ", status=" + status + ", pozitie=" + pozitie + "]";
	}

}
