package gov.nih.nci.cananolab.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * This class contains a set of utilities for manipulating dates
 *
 * @author pansu
 *
 */
public class DateUtils {
	private static Logger logger = Logger.getLogger(DateUtils.class);

	public static Date addSecondsToCurrentDate(int seconds) {
		// get a calendar with current time
		Calendar cal = Calendar.getInstance();
		// add 5 seconds to it.
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	public static String convertDateToString(Date date) {
		return convertDateToString(date, Constants.EXPORT_FILE_DATE_FORMAT);
	}
	
	public static String convertDateToString(Date date, String format) {
		if (date == null) {
			return "";
		}
		String dateStr = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			dateStr = dateFormat.format(date);
		} catch (Exception e) {
			logger.error(
				"Error converting the given date using the given dateFormat.", e);
			throw new RuntimeException("Can't format the given date: " + date);
		}
		return dateStr;
	}

	public static Date convertToDate(String dateString, String dateFormat) {
		if (dateString == null || dateString == "") {
			return null;
		}
		Date theDate = null;
		try {
			ParsePosition pos = new ParsePosition(0);
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			theDate = format.parse(dateString, pos);
			// method parse doesn't throw an exception when parsing is partial.
			// e.g. date 5/11/200w will
			// be parsed as 5/11/200 !!!
			if (pos.getIndex() != dateString.length()) {
				throw new RuntimeException(
						"The date String is not completely parsed");
			}
			return theDate;
		} catch (Exception e) {
			logger
					.error(
							"Error parsing the given date String using the given dateFormat",
							e);
			throw new RuntimeException("The date String " + dateString
					+ " can't be parsed against the date format:" + dateFormat);
		}
	}

	public static void main(String[] args) {
		try {
			String dateString = DateUtils.convertDateToString(new Date(),
					"yyyyMMdd_HH-mm-ss-SSS");
			System.out.println(dateString);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
