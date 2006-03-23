package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.dto.search.WorkflowResultBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SearchWorkflowService.java,v 1.2 2006-03-23 21:33:03 pansu Exp $ */

public class SearchWorkflowService {
	/**
	 * 
	 * @return all file submitters
	 */
	public List<String> getAllFileSubmitters() {
		List<String> submitters = new ArrayList<String>();
		submitters.add("Jane Doe");
		submitters.add("John Doe");
		return submitters;
	}

	public List<WorkflowResultBean> searchWorkflows(String assayName,
			String assayType, Date assayRunDateBegin, Date assayRunDateEnd,
			String aliquotId, boolean includeMaskedAliquots, String fileName,
			boolean isFileIn, Date fileSubmissionDateBegin,
			Date fileSubmissionDateEnd, String fileSubmitter,
			boolean includeMaskedFiles) {
		List<WorkflowResultBean> workflows = new ArrayList<WorkflowResultBean>();
		workflows.add(new WorkflowResultBean(
				"PrescreeningAssays/STE_1?run_1/in/run1_input.ab1",
				"Prescreening Assay", "STE-1", null, "NCL6-3-15", "10/12/05",
				"Jane Doe", "Masked"));
		workflows.add(new WorkflowResultBean(
				"PrescreeningAssays/STE_1?run_1/in/run1_input.ab1",
				"Prescreening Assay", "STE-1", null, "NCL6-4-15", "10/12/05",
				"Jane Doe", "Masked"));

		return workflows;
	}
}