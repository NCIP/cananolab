package gov.nih.nci.calab.service.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class contains a set of utilities for converting Strings to other
 * formats or converting other formats to String.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: StringUtils.java,v 1.16 2007-06-18 19:19:12 pansu Exp $ */

public class StringUtils {
	private static Logger logger = Logger.getLogger(StringUtils.class);

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
		if (joinedStr.endsWith(delimiter)) {
			joinedStr = joinedStr.substring(0, joinedStr.length() - 1);
		}
		return joinedStr;
	}

	public static String join(List<String> stringList, String delimiter) {
		String joinedStr = "";
		if (stringList == null || stringList.isEmpty()) {
			return joinedStr;
		}
		// remove null and empty item from the list
		for (String str : stringList) {
			if (str == null) {
				stringList.remove(str);
			}
			if (str.length() == 0) {
				stringList.remove(str);
			}
		}
		int i = 0;
		for (String str : stringList) {
			if (i < stringList.size() - 1) {
				joinedStr += str + delimiter;
			} else {
				joinedStr += str;
			}
			i++;
		}
		return joinedStr;
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
			logger
					.error(
							"Error converting the given date using the given dateFormat",
							e);
			throw new RuntimeException("Can't format the given date: " + date);
		}
		return dateStr;
	}

	public static Float convertToFloat(String floatStr) {
		if (floatStr == null || floatStr == "") {
			return null;
		}
		try {
			Float floatNum = Float.parseFloat(floatStr);
			return floatNum;
		} catch (NumberFormatException e) {
			logger.error("Error converting the given string to a float number",
					e);
			throw new RuntimeException(
					"Can't convert the given string to a float number: "
							+ floatStr);
		}
	}

	public static Long convertToLong(String longStr) {
		if (longStr == null || longStr == "") {
			return null;
		}
		try {
			Long longNum = Long.parseLong(longStr);
			return longNum;
		} catch (NumberFormatException e) {
			logger.error("Error converting the given string to a long number",
					e);
			throw new RuntimeException(
					"Can't convert the given string to a long number: "
							+ longStr);
		}
	}

	public static String convertToString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	public static boolean isInteger(String theStr) {
		if (theStr == null || theStr.length() == 0) {
			return false;
		} else {
			for (int i = 0; i < theStr.length(); i++) {
				if (!Character.isDigit(theStr.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	}

	public static boolean isDouble(String theStr) {
		int decimalCount = 0;

		if (theStr == null || theStr.length() == 0) {
			return false;
		} else {
			for (int i = 0; i < theStr.length(); i++) {
				if (!Character.isDigit(theStr.charAt(i))) {
					if (theStr.charAt(i) == ('.')) {
						decimalCount++;
						continue;
					} else {
						return false;
					}
				}
			}

			if (decimalCount == 1)
				return true;
			else
				return false;
		}
	}

	public static boolean contains(String[] array, String aString,
			boolean ignoreCase) {
		boolean containsString = false;

		for (int i = 0; i < array.length; i++) {
			if (ignoreCase) {
				if (array[i].equalsIgnoreCase(aString))
					containsString = true;
			} else {
				if (array[i].equals(aString))
					containsString = true;
			}
		}

		return containsString;
	}

	public static String[] add(String[] x, String aString) {
		String[] result = new String[x.length + 1];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i];
		}
		result[x.length] = aString;

		return result;
	}

	public static String getTimeAsString() {
		String time = null;
		Calendar calendar = Calendar.getInstance();
		time = "" + calendar.get(Calendar.YEAR);
		time = time
				+ (calendar.get(Calendar.MONTH) < 9 ? "0"
						+ (calendar.get(Calendar.MONTH) + 1) : ""
						+ (calendar.get(Calendar.MONTH) + 1));

		time = time
				+ (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
						+ calendar.get(Calendar.DAY_OF_MONTH) : ""
						+ calendar.get(Calendar.DAY_OF_MONTH)) + "_";
		time = time + calendar.get(Calendar.HOUR_OF_DAY) + "-";
		time = time
				+ (calendar.get(Calendar.MINUTE) < 10 ? "0"
						+ calendar.get(Calendar.MINUTE) : ""
						+ calendar.get(Calendar.MINUTE)) + "-";
		time = time
				+ (calendar.get(Calendar.SECOND) < 10 ? "0"
						+ calendar.get(Calendar.SECOND) : ""
						+ calendar.get(Calendar.SECOND)) + "-";
		time = time + calendar.get(Calendar.MILLISECOND);
		return time;
	}
	
	public static String getDateAsString() {
		
		Calendar calendar = Calendar.getInstance();
		String month=calendar.get(Calendar.MONTH) < 9 ? "0"
				+ (calendar.get(Calendar.MONTH) + 1) : ""
					+ (calendar.get(Calendar.MONTH) + 1);
		String day=calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ calendar.get(Calendar.DAY_OF_MONTH) : ""
					+ calendar.get(Calendar.DAY_OF_MONTH);
		String year=calendar.get(Calendar.YEAR)+"";
		return month+day+year;		
	}
	
    /**
     * Convert a string with multiple words separated by space to
     * one word, with first letter as lower case.
     * @param words
     * @return
     */
	public static String getOneWordLowerCaseFirstLetter(String words) {
		// remove space in words and make the first letter lower case.
		String firstLetter = words.substring(0, 1);
		String oneWord = words.replaceFirst(firstLetter,
				firstLetter.toLowerCase()).replace(" ", "");
		return oneWord;
	}
	
	   /**
     * Convert a string with multiple words separated by space to
     * one word, with first letter as lower case.
     * @param words
     * @return
     */
	public static String getOneWordUpperCaseFirstLetter(String words) {
		// remove space in words and make the first letter lower case.
		String firstLetter = words.substring(0, 1);
		String oneWord = words.replaceFirst(firstLetter,
				firstLetter.toUpperCase()).replace(" ", "");
		return oneWord;
	}
	
	public static void main(String[] args) {
		String dateString=StringUtils.getDateAsString();
		System.out.println(dateString);
	}
}
