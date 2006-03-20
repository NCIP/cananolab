package gov.nih.nci.calab.service.administration;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: ManageAliquotService.java,v 1.4 2006-03-20 17:00:37 pansu Exp $ */

public class ManageAliquotService {

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<String> getAliquotCreateMethods() {
		List createMethods = new ArrayList();
		createMethods.add("Solubilized");
		createMethods.add("Liatholized");
		return createMethods;
	}
	
	public int getDefaultAliquotMatrixColumnNumber() {
		return 10;
	}
	
	public String getAliquotPrefix(String sampleId, String lotId, String parentAliquotId) {
		if (lotId.equals("N/A")) {
			lotId=null;
		}
		if (parentAliquotId.length()==0) {
			parentAliquotId=null;
		}
		String aliquotPrefix=sampleId+"-";
		if (lotId!=null) {
			String[] lotIdParts=lotId.split("-");
			String lotNum=lotIdParts[lotIdParts.length-1];
		    aliquotPrefix+=lotNum+"-";
		}
		if (parentAliquotId!=null) {
			String[] aliquotIdParts=parentAliquotId.split("-");
			String aliquotNum=aliquotIdParts[aliquotIdParts.length-1];
			aliquotPrefix+=aliquotNum+"-";
		}
		return aliquotPrefix;
	}
	
	public int getFirstAliquotNum(String sampleId, String lotId, String parentAliquotId) {
		//tmp code to be replaced
		int aliquotNum=1;
		return aliquotNum;
	}
}