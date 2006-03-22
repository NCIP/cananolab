package gov.nih.nci.calab.service.workflow;

import java.util.ArrayList;
import java.util.List;

public class ExecuteWorkflowService {


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

	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * @param runId
	 * @param aliquotIds
	 * @throws Exception
	 */
	public void saveRunAliquots(String runId, String[] aliquotIds, String comments) throws Exception {
		//TODO fill in details for saving aliquotIds for the run
	}
}
