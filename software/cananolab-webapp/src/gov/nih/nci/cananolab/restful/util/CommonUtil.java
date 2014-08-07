package gov.nih.nci.cananolab.restful.util;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	
	public static List<String> wrapErrorMessageInList(String error) {
		List<String> msg = new ArrayList<String>();
		if (error == null)
			return msg;
		else
			msg.add(error);
		
		return msg;
	}
	

}
