package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.dto.search.WorkflowResultBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SearchWorkflowService.java,v 1.5 2006-04-06 21:10:01 pansu Exp $ */

public class SearchWorkflowService {
	private static Logger logger = Logger.getLogger(SearchWorkflowService.class);

	/**
	 * 
	 * @return all file submitters
	 */
	public List<String> getAllFileSubmitters() {
		List<String> submitters = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString1 = "select distinct createdBy from InputFile";
			List results1 = ida.search(hqlString1);
			for (Object obj : results1) {
				submitters.add((String) obj);
			}
			String hqlString2 = "select distinct createdBy from OutputFile";
			List results2 = ida.search(hqlString2);
			for (Object obj : results2) {
				submitters.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all file submitters", e);
			throw new RuntimeException("Error in retrieving all file submitters");
		}
		return submitters;
	}

	public List<WorkflowResultBean> searchWorkflows(String assayName,
			String assayType, Date assayRunDateBegin, Date assayRunDateEnd,
			String aliquotId, boolean includeMaskedAliquots, String fileName,
			boolean isFileIn, boolean isFileOut, Date fileSubmissionDateBegin,
			Date fileSubmissionDateEnd, String fileSubmitter,
			boolean includeMaskedFiles, String criteriaJoin) {
		List<WorkflowResultBean> workflows = new ArrayList<WorkflowResultBean>();
		workflows.add(new WorkflowResultBean(
				"PrescreeningAssays/STE_1?run_1/in/run1_input.ab1",
				"Prescreening Assay", "STE-1", "10/11/05", "NCL6-3-15", "10/12/05",
				"Jane Doe", "Masked"));
		workflows.add(new WorkflowResultBean(
				"PrescreeningAssays/STE_1?run_1/in/run1_input.ab1",
				"Prescreening Assay", "STE-1", "10/11/05", "NCL6-4-15", "10/12/05",
				"Jane Doe", "Masked"));

		return workflows;
	}
}