package gov.nih.nci.cananolab.restful.util;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	
	/**
	 * Wrap an error message in a list
	 * 
	 * @param error
	 * @return
	 */
	public static List<String> wrapErrorMessageInList(String error) {
		List<String> msg = new ArrayList<String>();
		if (error == null)
			return msg;
		else
			msg.add(error);
		
		return msg;
	}
	
	/**
	 * Add "other" to the end of an option list
	 * 
	 * @param items
	 * @return
	 */
	public static List<String> addOtherToList(List<String> items) {
		List<String> theList = items;
		if (theList == null)
			theList = new ArrayList<String>();
		
		theList.add("other");
		
		return theList;
	}
}
