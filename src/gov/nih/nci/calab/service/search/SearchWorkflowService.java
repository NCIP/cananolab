package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.InputFile;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.dto.search.WorkflowResultBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * This class implements middle-tier methods for the search workflow page
 * 
 * @author pansu
 * 
 */

/* CVS $Id: SearchWorkflowService.java,v 1.17 2006-04-21 13:44:11 pansu Exp $ */

public class SearchWorkflowService {
	private static Logger logger = Logger
			.getLogger(SearchWorkflowService.class);

	/**
	 * 
	 * @return all file submitters
	 */
	public List<String> getAllFileSubmitters() {
		SortedSet<String> submitters = new TreeSet<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString1 = "select distinct createdBy from InputFile order by createdBy";
			List results1 = ida.query(hqlString1, InputFile.class.getName());
			for (Object obj : results1) {
				submitters.add((String) obj);
			}
			String hqlString2 = "select distinct createdBy from OutputFile order by createdBy";
			List results2 = ida.query(hqlString2, OutputFile.class.getName());
			for (Object obj : results2) {
				submitters.add((String) obj);
			}
			ida.close();

		} catch (Exception e) {
			logger.error("Error in retrieving all file submitters", e);
			throw new RuntimeException(
					"Error in retrieving all file submitters");
		}
		return new ArrayList<String>(submitters);
	}

	public List<WorkflowResultBean> searchWorkflows(String assayName,
			String assayType, Date assayRunDateBegin, Date assayRunDateEnd,
			String aliquotName, boolean includeMaskedAliquots, String fileName,
			boolean isFileIn, boolean isFileOut, Date fileSubmissionDateBegin,
			Date fileSubmissionDateEnd, String fileSubmitter,
			boolean includeMaskedFiles, String criteriaJoin) {
		List<WorkflowResultBean> workflows = null;

		// construct where clause and parameter list
		Object[] whereParams = null;
		if (criteriaJoin.equals("and")) {
			whereParams = constructWhereParams(assayName, assayType,
					assayRunDateBegin, assayRunDateEnd, aliquotName,
					includeMaskedAliquots, fileName, fileSubmissionDateBegin,
					fileSubmissionDateEnd, fileSubmitter, includeMaskedFiles,
					"and");
		} else {
			whereParams = constructWhereParams(assayName, assayType,
					assayRunDateBegin, assayRunDateEnd, aliquotName,
					includeMaskedAliquots, fileName, fileSubmissionDateBegin,
					fileSubmissionDateEnd, fileSubmitter, includeMaskedFiles,
					"or");
		}

		String hqlString = "select distinct file.path, assay.assayType, assay.name, run.name, run.createdDate, aliquot.name, file.createdDate, file.createdBy, fileStatus.status ";

		// search either input file or output file, not both
		if (!(isFileIn && isFileOut)) {
			boolean isInputFile = (isFileIn == true) ? true : false;
			{
				// if select an item, need to include in join
				if (isInputFile) {
					hqlString += ", '"+ CalabConstants.INPUT+ "' from Assay assay join assay.runCollection run left join run.inputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";
				} else {
					hqlString += ", '"+ CalabConstants.OUTPUT+"' from Assay assay join assay.runCollection run left join run.outputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";
				}
				hqlString += whereParams[0];
				workflows = getWorkflows(hqlString, (List) whereParams[1]);
				Collections.sort(workflows,
						new CalabComparators.WorkflowResultBeanComparator());
			}
		} else {
			// get inputFile workflows first
			SortedSet<WorkflowResultBean> workflowSet = new TreeSet<WorkflowResultBean>(
					new CalabComparators.WorkflowResultBeanComparator());
			String inHqlString = hqlString
					+ ", '"+ CalabConstants.INPUT+"' from Assay assay join assay.runCollection run left join run.inputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";
			inHqlString += whereParams[0];
			List<WorkflowResultBean> inWorkflows = getWorkflows(inHqlString,
					(List) whereParams[1]);
			workflowSet.addAll(inWorkflows);

			// get outputFile workflows next
			String outHqlString = hqlString
					+ ", '"+ CalabConstants.OUTPUT+"' from Assay assay join assay.runCollection run left join run.outputFileCollection file left join file.dataStatus fileStatus left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";
			outHqlString += whereParams[0];
			List<WorkflowResultBean> outWorkflows = getWorkflows(outHqlString,
					(List) whereParams[1]);
			workflowSet.addAll(outWorkflows);
			workflows = new ArrayList<WorkflowResultBean>(workflowSet);
		}

		return workflows;
	}

	private Object[] constructWhereParams(String assayName, String assayType,
			Date assayRunDateBegin, Date assayRunDateEnd, String aliquotName,
			boolean includeMaskedAliquots, String fileName,
			Date fileSubmissionDateBegin, Date fileSubmissionDateEnd,
			String fileSubmitter, boolean includeMaskedFiles, String join) {

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

		if (!includeMaskedAliquots) {
			whereList.add("aliquotStatus.status is null");
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

		if (!includeMaskedFiles) {
			whereList.add("fileStatus.status is null");
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
			List paramList) {
		List<WorkflowResultBean> workflows = new ArrayList<WorkflowResultBean>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
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
				String theFileSubmissionDate = StringUtils.convertDateToString(
						(Date) items[6], CalabConstants.DATE_FORMAT);
				String theFileSubmitter = StringUtils.convertToString(items[7]);
				String theFileStatus = StringUtils.convertToString(items[8]);
				//set inout type to empty string when file path is emtpy
				String theFileInoutType=(theFilePath.equals(""))?"":StringUtils.convertToString(items[9]);
				workflows
						.add(new WorkflowResultBean(theFilePath, theAssayType,
								theAssayName, theAssayRunName, theAssayRunDate, theAliquotName,
								theFileSubmissionDate, theFileSubmitter,
								theFileStatus, theFileInoutType));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in searching aliquots by the given parameters",
					e);
			throw new RuntimeException(
					"Error in searching aliquots by the given parameters");
		}
		return workflows;
	}}