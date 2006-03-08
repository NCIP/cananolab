package gov.nih.nci.calab.service.workflow;

import java.util.ArrayList;
import java.util.List;

public class CreateTreeService {

	/**
	 * Retrieve all Assay Types from the system
	 *
	 * @return A list of all assay type
	 */
	public List<String> getAllAssayTypes()
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


}

