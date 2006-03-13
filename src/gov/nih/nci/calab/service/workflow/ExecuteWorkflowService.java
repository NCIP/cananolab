package gov.nih.nci.calab.service.workflow;

import java.util.ArrayList;
import java.util.List;

public class ExecuteWorkflowService {
	/**
	 * Retrieve all Assay Types from the system
	 *
	 * @return A list of all assay type
	 */
	public List getAllAssayTypes()
	{
		// Detail here... ...
			// if the return from DB are null or size zero
			// read from the xml

		List assayTypes = new ArrayList();
		assayTypes.add("Pre-screening Assay");
		assayTypes.add("In Vitro");
		assayTypes.add("In Vivo");
		return assayTypes;
	}

	/**
	 * Retrieve assays by assayType
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAssayByType(String assayTypeName)
	{
		// Detail here
		List assays = new ArrayList();
		if (assayTypeName.equals("Pre-screening Assay"))
		{
			assays.add("PCC-1");
			assays.add("STE-1");
			assays.add("STE-2");
			assays.add("STE-3");
		}
		return assays;
	}

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
