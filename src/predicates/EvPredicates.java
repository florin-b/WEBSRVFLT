package predicates;

import java.util.function.Predicate;

import beans.BeanEvenimentTableta;
import beans.RezultatTraseu;

public class EvPredicates {

	public static Predicate<BeanEvenimentTableta> isEvPlecare(String codBorderou) {
		return p -> p.getEveniment().equals("P") && p.getClient().equals(codBorderou);
	}

	public static Predicate<BeanEvenimentTableta> isEvSosire(String codBorderou) {
		return p -> p.getEveniment().equals("S") && !p.getClient().equals(codBorderou);
	}

	public static Predicate<BeanEvenimentTableta> isEvTableta(String codBorderou) {
		return isEvPlecare(codBorderou).or(isEvSosire(codBorderou));
	}

	public static Predicate<RezultatTraseu> isPlecareDateNotNull() {
		return p -> p.getPlecare() != null;
	}
	
	
	public static Predicate<BeanEvenimentTableta> isDateNotNull() {
		return p -> p.getData() != null && p.getOra() != null;
	}
	

	
	
}
