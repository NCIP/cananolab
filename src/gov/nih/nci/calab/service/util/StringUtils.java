package gov.nih.nci.calab.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * This class contains a set of utilities for converting Strings to other
 * formats or converting other formats to String.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: StringUtils.java,v 1.1 2006-03-24 14:40:21 pansu Exp $ */

public class StringUtils {
	private static Logger logger=Logger.getLogger(StringUtils.class);
	
	public static Date convertToDate(String dateString, String dateFormat) {
		Date theDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			theDate = format.parse(dateString);
			return theDate;
		}
		catch (Exception e) {
			logger.error("Error parsing the given date String using the given dateFormat", e);
			throw new RuntimeException("Can't parse the given date String: "+dateString);
		}
	}
}
