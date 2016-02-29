package test;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import beans.BeanStatusMotor;
import beans.PozitieClient;
import beans.RezultatTraseu;
import beans.TraseuBorderou;
import database.OperatiiMasina;
import database.OperatiiTraseu;
import enums.EnumCoordClienti;
import enums.EnumStatusMotor;
import model.CalculeazaTraseu;
import utils.UtilsFormatting;

public class TestWS {

	public static void main(String[] args) {

		OperatiiTraseu operatiiTraseu = new OperatiiTraseu();

		String codBorderou = "0001416905";

		// 0001400220
		// 0001400242

		List<TraseuBorderou> traseuBorderou = operatiiTraseu.getTraseuBorderou(codBorderou);
		List<PozitieClient> pozitiiClienti = operatiiTraseu.getCoordClientiBorderou(codBorderou, EnumCoordClienti.NEVIZITATI);

		/*
		 * try {
		 * 
		 * OperatiiMasina operatiiMasina = new OperatiiMasina();
		 * System.out.println(operatiiMasina.getInfoEvenimentStop(codBorderou,
		 * pozitiiClienti));
		 * 
		 * } catch (SQLException e) {
		 * 
		 * e.printStackTrace(); }
		 */

		CalculeazaTraseu calculeaza = new CalculeazaTraseu(codBorderou);
		calculeaza.setPozitiiClienti(pozitiiClienti);
		calculeaza.setTraseuBorderou(traseuBorderou);

		Set<RezultatTraseu> rezultat = calculeaza.getStareTraseu();

		System.out.println(rezultat);

	}

}
