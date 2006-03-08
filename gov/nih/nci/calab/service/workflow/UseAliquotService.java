package gov.nih.nci.calab.service.workflow;

import java.util.ArrayList;
import java.util.List;

public class UseAliquotService {
	
	/**
	 * Retriving all unmasked aliquot in the system, for use aliquot
	 * 
	 * @return a list of aliquot id
	 */
	public List<String> getAliquots()
	{
		// Need detail....
		
		
		List aliquotIds = new ArrayList();
		aliquotIds.add("NCL-0-1234");
		aliquotIds.add("NCL-0-1234-0");
		aliquotIds.add("NCL-1-1234-1");
		aliquotIds.add("NCL-1-1234");
		aliquotIds.add("NCL-2-1235");
		aliquotIds.add("NCL-5-1234");
		aliquotIds.add("NCL-5-1234-0");
		aliquotIds.add("NCL-5-1234-1");
		aliquotIds.add("NCL-6-1234");
		aliquotIds.add("NCL-6-1235");
		
		return aliquotIds;
	}

	/**
	 * Retrive aliquots associate with Assay
	 * 
	 * @return a list of aliquot id 
	 */
	public List<String> getAliquotsByAssay(String assayTypeName, String assayName)
	{
		// Detail impl needed
		
		List aliquotIds = new ArrayList();
		aliquotIds.add("NCL-0-1234");
		aliquotIds.add("NCL-0-1234-0");
		aliquotIds.add("NCL-1-1234-1");
		aliquotIds.add("NCL-1-1234");
		aliquotIds.add("NCL-2-1235");
		
		return aliquotIds;
	}
}
