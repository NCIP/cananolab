package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.dto.search.WorkflowResultBean;
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.CalabConstants;
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

/* CVS $Id: SearchWorkflowService.java,v 1.22 2006-04-27 14:51:08 pansu Exp $ */

public class SearchWorkflowService {
	private static Logger logger = Logger
			.getLogger(SearchWorkflowService.class);

	/**
	 * 
	 * @return all file submitters
	 */
	public List<String> getAllFileSubmitters() throws Exception {
		SortedSet<String> submitters = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString1 = "select distinct createdBy from InputFile order by createdBy";
			List results1 = ida.search(hqlString1);
			for (Object obj : results1) {
				submitters.add((String) obj);
			}
			String hqlString2 = "select distinct createdBy from OutputFile order by createdBy";
			List results2 = ida.search(hqlString2);
			for (Object obj : results2) {
				submitters.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all file submitters", e);
			throw new RuntimeException(
					"Error in retrieving all file submitters");
		} finally {
			ida.close();
		}
		return new ArrayList<String>(submitters);
	}

	public List<WorkflowResultBean> searchWorkflows(String assayName,
			String assayType, Date assayRunDateBegin, Date assayRunDateEnd,
			String aliquotName, boolean includeMaskedAliquots, String fileName,
			boolean isFileIn, boolean isFileOut, Date fileSubmissionDateBegin,
			Date fileSubmissionDateEnd, String fileSubmitter,
			boolean includeMaskedFiles, String criteriaJoin) throws Exception {
		List<WorkflowResultBean> workflows = null;

		// construct where clause and parameter list
		Object[] whereParams = constructWhereParams(assayName, assayType,
				assayRunDateBegin, assayRunDateEnd, aliquotName, fileName,
				fileSubmissionDateBegin, fileSubmissionDateEnd, fileSubmitter,
				criteriaJoin);

		String hqlString = "select distinct file.path, assay.assayType, assay.name, run.name, run.createdDate, aliquot.name, aliquotStatus.status, file.createdDate, file.createdBy, fileStatus.status ";

		// get inputFile workflows first
		SortedSet<WorkflowResultBean> workflowSet = new TreeSet<WorkflowResultBean>(
				new CalabComparators.WorkflowResultBeanComparator());
		String inHqlString = hqlString
				+ ", '"
				+ CalabConstants.INPUT
				+ "' from Assay assay join assay.runCollection run left join run.inputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";

		inHqlString += whereParams[0];
		List<WorkflowResultBean> inWorkflows = getWorkflows(inHqlString,
				(List) whereParams[1]);
		workflowSet.addAll(inWorkflows);

		// get outputFile workflows next
		String outHqlString = hqlString
				+ ", '"
				+ CalabConstants.OUTPUT
				+ "' from Assay assay join assay.runCollection run left join run.outputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus  ";
		outHqlString += whereParams[0];
		List<WorkflowResultBean> outWorkflows = getWorkflows(outHqlString,
				(List) whereParams[1]);
		workflowSet.addAll(outWorkflows);
		workflows = filterWorkflows(new ArrayList<WorkflowResultBean>(
				workflowSet), isFileIn, isFileOut, includeMaskedAliquots,
				includeMaskedFiles);
		Collections.sort(workflows, new CalabComparators.WorkflowResultBeanComparator());
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
				whereList.add("file.path like ?");
			} else {
				whereList.add("file.path=?");
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
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {
			ida.open();

			List results = ida.searchByParam(hqlString, paramList);

			for (Object obj : results) {
				Object[] items = (Object[]) obj;
				String theFilePath = StringUtils.convertToString(items[0]);
				String theAssayType = StringUtils.convertToString(items[1]);
				String theAssayName = StringUtils.convertToString(items[2]);
				String theAssayRunName = StringUtils.convertToString(items[3]);
				String theAssayRunDate = StringUtils.convertDateToString(
						(Date) items[4], CalabConstants.DATE_FORMAT);
				String theAliquotName = StringUtils.convertToString(items[5]);
				String theAliquotStatus = StringUtils.convertToString(items[6]);
				String theFileSubmissionDate = StringUtils.convertDateToString(
						(Date) items[7], CalabConstants.DATE_FORMAT);
				String theFileSubmitter = StringUtils.convertToString(items[8]);
				String theFileStatus = StringUtils.convertToString(items[9]);
				// set inout type to empty string when file path is emtpy
				String theFileInoutType = StringUtils
						.convertToString(items[10]);
				// filter out rows with inouttype=out and empty file path
				if (!theFileInoutType.equals(CalabConstants.OUTPUT)
						|| theFilePath.length() > 0) {
					// set inout type=in to empty when no filePath
					theFileInoutType = (theFilePath.length() == 0) ? ""
							: theFileInoutType;
					workflows.add(new WorkflowResultBean(theFilePath,
							theAssayType, theAssayName, theAssayRunName,
							theAssayRunDate, theAliquotName, theAliquotStatus,
							theFileSubmissionDate, theFileSubmitter,
							theFileStatus, theFileInoutType));
				}
			}
		} catch (Exception e) {
			logger.error("Error in searching aliquots by the given parameters",
					e);
			throw new RuntimeException(
					"Error in searching aliquots by the given parameters");
		} finally {
			ida.close();
		}
		return workflows;
	}

	private List<WorkflowResultBean> filterWorkflows(
			List<WorkflowResultBean> origWorkflows, boolean isFileIn,
			boolean isFileOut, boolean includedMaskedAliquots,
			boolean includeMaskedFiles) {

		// no filtering
		if (!isFileIn && !isFileOut && includedMaskedAliquots
				&& includeMaskedFiles) {
			return origWorkflows;
		}

		String selectFileType = null;
		if (isFileIn && isFileOut) {
			selectFileType = "both";
		} else if (isFileIn) {
			selectFileType = CalabConstants.INPUT;
		} else if (isFileOut) {
			selectFileType = CalabConstants.OUTPUT;
		} else {
			selectFileType = "";
		}

		List<WorkflowResultBean> workflows = new ArrayList<WorkflowResultBean>();
		for (WorkflowResultBean workflow : origWorkflows) {
			String fileInOut = workflow.getFile().getInoutType();
			String fileStatus = workflow.getFile().getFileMaskStatus();
			String aliquotStatus = workflow.getAliquot().getMaskStatus();

			// filter out workflows when not satisfying masking criteria
			if (!includeMaskedFiles
					&& (fileStatus.equals(CalabConstants.MASK_STATUS))
					|| !includedMaskedAliquots
					&& aliquotStatus.equals(CalabConstants.MASK_STATUS)) {
			}
			// and filter out workflows when not satisfying in out criteria
			else if (selectFileType.equals("both")
					&& fileInOut.length() > 0
					|| selectFileType.equals("")
					|| (selectFileType.equals(CalabConstants.INPUT) || selectFileType
							.equals(CalabConstants.OUTPUT))
					&& selectFileType.equals(fileInOut)
					&& fileInOut.length() > 0) {
				workflows.add(workflow);
			}
		}
		
		return workflows;

	}
}