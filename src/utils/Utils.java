package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {

	private static final Logger logger = LogManager.getLogger(Utils.class);

	public static String dateDiff(String dateStart, String dateStop) {

		StringBuilder result = new StringBuilder();

		if (dateStart.length() == 0 || dateStop.length() == 0)
			return result.toString();

		try {

			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss", new Locale("en"));

			Date d1 = dateFormat.parse(dateStart);
			Date d2 = dateFormat.parse(dateStop);

			long diff = d2.getTime() - d1.getTime();

			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			if (diffDays > 0) {
				result.append(diffDays);
				result.append(" zile ");
			}

			if (diffHours > 0) {
				result.append(diffHours);
				result.append(" ore ");
			}

			if (diffMinutes > 0) {
				result.append(diffMinutes);
				result.append(" minute");
			} else {
				if (diffMinutes != 0) {
					diffMinutes = 60 - Math.abs(diffMinutes);
					result.append(diffMinutes);
					result.append(" minute");
				}
			}

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}

		return result.toString();

	}

	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

}
