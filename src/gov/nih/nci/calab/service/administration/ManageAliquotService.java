package gov.nih.nci.calab.service.administration;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: ManageAliquotService.java,v 1.5 2006-03-20 21:50:50 pansu Exp $ */

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
	
	/**
	 * 
	 * @param sampleId
	 * @param lotId
	 * @param parentAliquotId
	 * @return the existing prefix for assigning a new aliquot ID.
	 */
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
	
	/**
	 * 
	 * @param sampleId
	 * @param lotId
	 * @param parentAliquotId
	 * @return the first number for assigning a new aliquot IDs.
	 */
	public int getFirstAliquotNum(String sampleId, String lotId, String parentAliquotId) {
		//tmp code to be replaced
		int aliquotNum=1;
		return aliquotNum;
	}
	
	 
	/**
	 * Save the aliquots into the database
	 * @param sampleId
	 * @param lotId
	 * @param parentAliquotId
	 * @param aliquotMatrix
	 * @param comments
	 * @throws Exception
	 */
	public void saveAliquots(String sampleId, String lotId, String parentAliquotId, List aliquotMatrix, String comments) throws Exception {
		//TODO fill in details for saving aliquots
	}
}