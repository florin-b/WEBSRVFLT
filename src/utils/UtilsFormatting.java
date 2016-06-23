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

	public static String formatDateSimple(String stringDate) {
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
	
	
	public static String formatDateSimpleYear(String stringDate) {
		String formattedDate = "";

		if (stringDate != null && stringDate.trim().length() == 0)
			return "";

		try {

			String pattern = "yyyyMMdd HHmmss";
			SimpleDateFormat formatInit = new SimpleDateFormat(pattern, Locale.US);
			Date date = formatInit.parse(stringDate);

			SimpleDateFormat formatFinal = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);

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
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", new Locale("en"));
		return dateFormat.format(new Date());
	}

	public static boolean isDateChronological(String strDate1, Date strDate2) {

		DateFormat format1 = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.US);
		Date date1 = null;

		try {
			date1 = format1.parse(strDate1);

			if (date1.compareTo(strDate2) < 0)
				return false;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return true;

	}

}
