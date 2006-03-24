package gov.nih.nci.calab.service.administration;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: ManageAliquotService.java,v 1.6 2006-03-24 21:13:47 pansu Exp $ */

public class ManageAliquotService {

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<LabelValueBean> getAliquotCreateMethods() {
		List<LabelValueBean> createMethods = new ArrayList<LabelValueBean>();
		createMethods.add(new LabelValueBean("Solubilized", "http://www.google.com"));
		createMethods.add(new LabelValueBean("Liatholized", "http://www.cnn.com"));
		return createMethods;
	}
	
	public int getDefaultAliquotMatrixColumnNumber() {
		return 10;
	}
	
	/**
	 * 
	 * @param sampleId
	 * @param parentAliquotId
	 * @return the existing prefix for assigning a new aliquot ID.
	 */
	public String getAliquotPrefix(String sampleId, String parentAliquotId) {

		if (parentAliquotId.length()==0) {
			return sampleId+"-";
		}
		else {
			return parentAliquotId+"-";
		}
	}
	
	/**
	 * 
	 * @param sampleId
	 * @param parentAliquotId
	 * @return the first number for assigning a new aliquot IDs.
	 */
	public int getFirstAliquotNum(String sampleId, String parentAliquotId) {
		int aliquotNum=0;
		if (parentAliquotId.length()==0) {
			aliquotNum=getLastSampleNum()+1;
		}
		else {
			aliquotNum= getLastAliquotNum()+1;			
		}
		return aliquotNum;
	}
	
	private int getLastSampleNum() {
		//TODO query db for the actual data
		return 0;
	}
	 
	private int getLastAliquotNum() {
		//TODO query db for the actual data
		return 0;
	}
	
	/**
	 * Save the aliquots into the database
	 * @param sampleId
	 * @param parentAliquotId
	 * @param aliquotMatrix
	 * @param comments
	 * @throws Exception
	 */
	public void saveAliquots(String sampleId, String parentAliquotId, List aliquotMatrix, String comments) throws Exception {
		//TODO fill in details for saving aliquots
	}
}