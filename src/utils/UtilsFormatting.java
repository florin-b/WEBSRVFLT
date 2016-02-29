package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilsFormatting {
	public static String formatDateSap(String stringDate) {
		String formattedDate = "";

		if (stringDate != null && stringDate.trim().length() == 0)
			return "";

		try {

			String pattern = "dd-MMM-yy HH:mm:ss";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, Locale.US);
			Date date = formatInit.parse(stringDate);

			SimpleDateFormat formatFinal = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.US);

			formattedDate = formatFinal.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedDate;
	}

	public static String formatDateLocal(String stringDate) {
		String formattedDate = "";

		if (stringDate != null && stringDate.trim().length() == 0)
			return "";

		try {

			String pattern = "yyyyMMdd HHmmss";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, Locale.US);
			Date date = formatInit.parse(stringDate);

			SimpleDateFormat formatFinal = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);

			formattedDate = formatFinal.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return formattedDate;
	}

	public static String formatNrMasina(String nrMasina) {
		return nrMasina.replaceAll("-", "");
	}

	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		return dateFormat.format(new Date());
	}

}
