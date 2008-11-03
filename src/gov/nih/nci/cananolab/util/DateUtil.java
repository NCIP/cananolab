package gov.nih.nci.cananolab.util;

import java.util.Calendar;
import java.util.Date;

/**
 * This class contains a set of utilities for manipulating dates
 * 
 * @author pansu
 * 
 */
public class DateUtil {
	public static Date addSecondsToCurrentDate(int seconds) {
		// get a calendar with current time
		Calendar cal = Calendar.getInstance();
		// add 5 seconds to it.
		cal.add(Calendar.SECOND, seconds);
		Date newDate = new Date(cal.getTimeInMillis());
		return newDate;
	}
}
