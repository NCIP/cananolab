package gov.nih.nci.calab.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * This class contains a set of utilities for converting Strings to other
 * formats or converting other formats to String.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: StringUtils.java,v 1.3 2006-04-06 15:41:48 pansu Exp $ */

public class StringUtils {
	private static Logger logger = Logger.getLogger(StringUtils.class);

	public static Date convertToDate(String dateString, String dateFormat) {
		if (dateString==null || dateString=="") {
			return null;
		}
		Date theDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			theDate = format.parse(dateString);
			return theDate;
		} catch (Exception e) {
			logger
					.error(
							"Error parsing the given date String using the given dateFormat",
							e);
			throw new RuntimeException("Can't parse the given date String: "
					+ dateString);
		}
	}

	public static String join(String[] stringArray, String delimiter) {
		String joinedStr = "";
		if (stringArray == null) {
			return joinedStr;
		}
		for (int i = 0; i < stringArray.length; i++) {
			String str = stringArray[i];
			if (str == null) {
				str = "";
			}
			if ((str.length() > 0)) {
				if (i < stringArray.length - 1) {
					joinedStr += str + delimiter;
				} else {
					joinedStr += str;
				}
			}
		}
		return joinedStr;
	}

	public static String convertDateToString(Date date, String format) {
		if (date==null) {
			return "";
		}
		String dateStr = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			dateStr = dateFormat.format(date);
		} catch (Exception e) {
			logger
					.error(
							"Error converting the given date using the given dateFormat",
							e);
			throw new RuntimeException("Can't format the given date: " + date);
		}
		return dateStr;
	}

	public static Float convertToFloat(String floatStr) {
		if (floatStr == null || floatStr=="") {
			return null;
		}
		try {
			Float floatNum = Float.parseFloat(floatStr);
			return floatNum;
		} catch (NumberFormatException e) {
			logger.error("Error converting the given string to a float number", e);
			throw new RuntimeException("Can't convert the given string to a float number: "+floatStr);		}
	}
}
