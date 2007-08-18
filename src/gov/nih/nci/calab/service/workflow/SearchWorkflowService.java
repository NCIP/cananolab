package gov.nih.nci.calab.service.workflow;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.dto.workflow.WorkflowResultBean;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * This class implements middle-tier methods for the search workflow page
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SearchWorkflowService.java,v 1.3 2007-08-18 02:05:09 pansu Exp $ */

public class SearchWorkflowService {
	private static Logger logger = Logger
			.getLogger(SearchWorkflowService.class);

	public List<WorkflowResultBean> searchWorkflows(String assayName,
			String assayType, Date assayRunDateBegin, Date assayRunDateEnd,
			String aliquotName, boolean excludeMaskedAliquots, String fileName,
			boolean isFileIn, boolean isFileOut, Date fileSubmissionDateBegin,
			Date fileSubmissionDateEnd, String fileSubmitter,
			boolean excludeMaskedFiles, String criteriaJoin) throws Exception {
		List<WorkflowResultBean> workflows = null;

		// construct where clause and parameter list
		Object[] whereParams = constructWhereParams(assayName, assayType,
				assayRunDateBegin, assayRunDateEnd, aliquotName, fileName,
				fileSubmissionDateBegin, fileSubmissionDateEnd, fileSubmitter,
				criteriaJoin);

		String hqlString = "select distinct file.uri, assay.assayType, assay.name, run.id, run.name, run.createdDate, aliquot.name, aliquotStatus.status, file.createdDate, file.createdBy, fileStatus.status, file.id, file.filename ";

		// get inputFile workflows first
		SortedSet<WorkflowResultBean> workflowSet = new TreeSet<WorkflowResultBean>(
				new CaNanoLabComparators.WorkflowResultBeanComparator());
		String inHqlString = hqlString
				+ ", '"
				+ CaNanoLabConstants.INPUT
				+ "' from Assay assay join assay.runCollection run left join run.inputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";

		inHqlString += whereParams[0];
		List<WorkflowResultBean> inWorkflows = getWorkflows(inHqlString,
				(List) whereParams[1]);
		workflowSet.addAll(inWorkflows);

		// get outputFile workflows next
		String outHqlString = hqlString
				+ ", '"
				+ CaNanoLabConstants.OUTPUT
				+ "' from Assay assay join assay.runCollection run left join run.outputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus  ";
		outHqlString += whereParams[0];
		List<WorkflowResultBean> outWorkflows = getWorkflows(outHqlString,
				(List) whereParams[1]);
		workflowSet.addAll(outWorkflows);
		workflows = filterWorkflows(new ArrayList<WorkflowResultBean>(
				workflowSet), isFileIn, isFileOut, excludeMaskedAliquots,
				excludeMaskedFiles);
		Collections.sort(workflows,
				new CaNanoLabComparators.WorkflowResultBeanComparator());
		return workflows;
	}

	private Object[] constructWhereParams(String assayName, String assayType,
			Date assayRunDateBegin, Date assayRunDateEnd, String aliquotName,
			String fileName, Date fileSubmissionDateBegin,
			Date fileSubmissionDateEnd, String fileSubmitter, String join) {

		List<Object> paramList = new ArrayList<Object>();
		List<String> whereList = new ArrayList<String>();

		String where = "";

		if (assayName.length() > 0) {
			if (assayName.indexOf("*") != -1) {
				assayName = assayName.replace('*', '%');
				whereList.add("assay.name like ?");
			} else {
				whereList.add("assay.name=?");
			}
			paramList.add(assayName);
			where = "where ";
		}
		if (assayType.length() > 0) {
			if (assayType.indexOf("*") != -1) {
				assayType = assayType.replace('*', '%');
				whereList.add("assay.assayType like ?");
			} else {
				whereList.add("assay.assayType =?");
			}
			paramList.add(assayType);
			where = "where ";
		}

		if (assayRunDateBegin != null && assayRunDateEnd != null) {
			paramList.add(assayRunDateBegin);
			paramList.add(assayRunDateEnd);
			whereList.add("run.runDate>=? and run.runDate<=?");
			where = "where ";
		} else if (assayRunDateEnd != null) {
			paramList.add(assayRunDateEnd);
			whereList.add("run.runDate<=?");
			where = "where ";
		} else if (assayRunDateBegin != null) {
			paramList.add(assayRunDateBegin);
			whereList.add("run.runDate>=?");
			where = "where ";
		}

		if (aliquotName.length() > 0) {
			if (aliquotName.indexOf("*") != -1) {
				aliquotName = aliquotName.replace('*', '%');
				whereList.add("aliquot.name like ?");
			} else {
				whereList.add("aliquot.name=?");
			}
			paramList.add(aliquotName);
			where = "where ";
		}

		if (fileName.length() > 0) {
			if (fileName.indexOf("*") != -1) {
				fileName = fileName.replace('*', '%');
				whereList.add("file.uri like ?");
			} else {
				whereList.add("file.uri=?");
			}
			paramList.add(fileName);
			where = "where ";
		}

		if (fileSubmissionDateBegin != null) {
			paramList.add(fileSubmissionDateBegin);
			whereList.add("file.createdDate>=?");
			where = "where ";
		}
		if (fileSubmissionDateEnd != null) {
			paramList.add(fileSubmissionDateEnd);
			whereList.add("file.createdDate<=?");
			where = "where ";
		}
		if (fileSubmitter.length() > 0) {
			paramList.add(fileSubmitter);
			whereList.add("file.createdBy=?");
			where = "where ";
		}

		String whereStr = "";
		if (join.equals("and")) {
			whereStr = StringUtils.join(whereList, " and ");
		} else {
			whereStr = StringUtils.join(whereList, " or ");
		}
		where = where + whereStr;
		Object[] objs = new Object[] { where, paramList };

		return objs;
	}

	private List<WorkflowResultBean> getWorkflows(String hqlString,
			List paramList) throws Exception {
		List<WorkflowResultBean> workflows = new ArrayList<WorkflowResultBean>();
		try {
			
			HibernateUtil.beginTransaction();
			List results = HibernateUtil.createQueryByParam(hqlString, paramList).list();

			for (Object obj : results) {
				Object[] items = (Object[]) obj;
				String theFilePath = StringUtils.convertToString(items[0]);
				String theAssayType = StringUtils.convertToString(items[1]);
				String theAssayName = StringUtils.convertToString(items[2]);
				String theAssayRunId = StringUtils.convertToString(items[3]);
				String theAssayRunName = StringUtils.convertToString(items[4]);
				Date theAssayRunDate = (Date) items[5];
				String theAliquotName = StringUtils.convertToString(items[6]);
				String theAliquotStatus = StringUtils.convertToString(items[7]);
				Date theFileSubmissionDate = (Date) items[8];
				String theFileSubmitter = StringUtils.convertToString(items[9]);
				String theFileStatus = StringUtils.convertToString(items[10]);
				String fileId = StringUtils.convertToString(items[11]);
				String filename = StringUtils.convertToString(items[12]);
				// set inout type to empty string when file path is emtpy
				String theFileInoutType = StringUtils
						.convertToString(items[13]);
				// filter out rows with inouttype=out and empty file path
				if (!theFileInoutType.equals(CaNanoLabConstants.OUTPUT)
						|| theFilePath.length() > 0) {
					// set inout type=in to empty when no filePath
					theFileInoutType = (theFilePath.length() == 0) ? ""
							: theFileInoutType;
					workflows.add(new WorkflowResultBean(fileId, theFilePath,
							filename, theAssayType, theAssayName,
							theAssayRunId, theAssayRunName, theAssayRunDate,
							theAliquotName, theAliquotStatus,
							theFileSubmissionDate, theFileSubmitter,
							theFileStatus, theFileInoutType));
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error in searching aliquots by the given parameters",
					e);
			throw new RuntimeException(
					"Error in searching aliquots by the given parameters");
		} finally {
			HibernateUtil.closeSession();
		}
		return workflows;
	}

	private List<WorkflowResultBean> filterWorkflows(
			List<WorkflowResultBean> origWorkflows, boolean isFileIn,
			boolean isFileOut, boolean excludeMaskedAliquots,
			boolean excludeMaskedFiles) {

		// no filtering
		if (!isFileIn && !isFileOut && !excludeMaskedAliquots
				&& !excludeMaskedFiles) {
			return origWorkflows;
		}

		String selectFileType = null;
		if (isFileIn && isFileOut) {
			selectFileType = "both";
		} else if (isFileIn) {
			selectFileType = CaNanoLabConstants.INPUT;
		} else if (isFileOut) {
			selectFileType = CaNanoLabConstants.OUTPUT;
		} else {
			selectFileType = "";
		}

		List<WorkflowResultBean> workflows = new ArrayList<WorkflowResultBean>();
		for (WorkflowResultBean workflow : origWorkflows) {
			String fileInOut = workflow.getFile().getInoutType();
			String fileStatus = workflow.getFile().getFileMaskStatus();
			String aliquotStatus = workflow.getAliquot().getMaskStatus();

			// filter out workflows when not satisfying masking criteria
			if (excludeMaskedFiles
					&& (fileStatus.equals(CaNanoLabConstants.MASK_STATUS))
					|| excludeMaskedAliquots
					&& aliquotStatus.equals(CaNanoLabConstants.MASK_STATUS)) {
			}
			// and filter out workflows when not satisfying in out criteria
			else if (selectFileType.equals("both")
					&& fileInOut.length() > 0
					|| selectFileType.equals("")
					|| (selectFileType.equals(CaNanoLabConstants.INPUT) || selectFileType
							.equals(CaNanoLabConstants.OUTPUT))
					&& selectFileType.equals(fileInOut)
					&& fileInOut.length() > 0) {
				workflows.add(workflow);
			}
		}

		return workflows;

	}
}