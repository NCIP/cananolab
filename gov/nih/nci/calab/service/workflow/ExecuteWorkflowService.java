package gov.nih.nci.calab.service.workflow;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Assay;
import gov.nih.nci.calab.domain.InputFile;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Run;
import gov.nih.nci.calab.domain.RunSampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.StorageLocation;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.util.file.FileNameConvertor;
import gov.nih.nci.calab.service.util.file.HttpUploadedFileData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class ExecuteWorkflowService {
	private static Logger logger = Logger
			.getLogger(ExecuteWorkflowService.class);

	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * 
	 * @param runId
	 * @param aliquotIds
	 * @throws Exception
	 */
	public void saveRunAliquots(String runId, String[] aliquotNames,
			String comments, String creator, String creationDate)
			throws Exception {

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			// load run object
			logger.debug("ExecuteWorkflowService.saveRunAliquots(): run id = "
					+ runId);
			Run doRun = (Run) session.load(Run.class, StringUtils
					.convertToLong(runId));

			// Create RunSampleContainer collection
			for (int i = 0; i < aliquotNames.length; i++) {
				// check if the aliquot has been assigned to the run, if it is,
				// skip it
				String hqlString = "select count(runcontainer.id) from RunSampleContainer runcontainer where runcontainer.run.id='"
						+ runId
						+ "' and runcontainer.sampleContainer.name='"
						+ aliquotNames[i] + "'";
				List results = session.createQuery(hqlString).list();
				if (((Integer) results.get(0)).intValue() > 0) {
					logger
							.debug("The aliquot name "
									+ aliquotNames[i]
									+ " is already assigned to this run, continue .... ");
					continue;
				}
				RunSampleContainer doRunSC = new RunSampleContainer();
				doRunSC.setComments(comments);
				doRunSC.setRun(doRun);
				logger
						.debug("ExecuteWorkflowService.saveRunAliquots(): aliquot name = "
								+ aliquotNames[i]);

				List aliquotResults = session.createQuery(
						"from Aliquot aliquot where aliquot.name='"
								+ aliquotNames[i] + "'").list();
				Aliquot doAliquot = null;
				for (Object obj : aliquotResults) {
					doAliquot = (Aliquot) obj;
				}
				logger
						.debug("ExecuteWorkflowService.saveRunAliquots(): doAliquot = "
								+ doAliquot);
				doRunSC.setSampleContainer(doAliquot);
				doRunSC.setCreatedBy(creator);
				doRunSC.setCreatedDate(StringUtils.convertToDate(creationDate,
						CaNanoLabConstants.DATE_FORMAT));
				session.save(doRunSC);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Error in saving Run Aliquot ", e);
			throw new RuntimeException("Error in saving Run Aliquot ");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public AliquotBean getAliquot(String aliquotId) throws Exception {
		AliquotBean aliquotBean = new AliquotBean();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "from Aliquot aliquot where aliquot.id ='"
					+ aliquotId + "'";
			List results = session.createQuery(hqlString).list();
			for (Object obj : results) {
				Aliquot doAliquot = (Aliquot) obj;
				aliquotBean.setAliquotId(aliquotId); // name
				aliquotBean.setAliquotName(doAliquot.getName());
				aliquotBean.setCreationDate(doAliquot.getCreatedDate());
				aliquotBean.setCreator(doAliquot.getCreatedBy());
				aliquotBean.setHowCreated(doAliquot.getCreatedMethod());

				String maskStatus = (doAliquot.getDataStatus() == null) ? CaNanoLabConstants.ACTIVE_STATUS
						: CaNanoLabConstants.MASK_STATUS;
				aliquotBean.setMaskStatus(maskStatus);

				// ContainerBean
				ContainerBean containerBean = new ContainerBean();
				if (doAliquot.getConcentration() != null) {
					containerBean.setConcentration(doAliquot.getConcentration()
							.toString());
					containerBean.setConcentrationUnit(doAliquot
							.getConcentrationUnit());
				}
				containerBean.setContainerComments(doAliquot.getComments());
				containerBean.setContainerType(doAliquot.getContainerType());
				if (doAliquot.getQuantity() != null) {
					containerBean.setQuantity(doAliquot.getQuantity()
							.toString());
					containerBean.setQuantityUnit(doAliquot.getQuantityUnit());
				}
				containerBean.setSafetyPrecaution(doAliquot
						.getSafetyPrecaution());
				containerBean.setSolvent(doAliquot.getDiluentsSolvent());
				containerBean.setStorageCondition(doAliquot
						.getStorageCondition());
				if (doAliquot.getVolume() != null) {
					containerBean.setVolume(doAliquot.getVolume().toString());
					containerBean.setVolumeUnit(doAliquot.getVolumeUnit());
				}

				// containerBean.setStorageLocationStr();
				StorageLocation location = new StorageLocation();
				Set storageElements = (Set) doAliquot
						.getStorageElementCollection();
				for (Object storageObj : storageElements) {
					StorageElement element = (StorageElement) storageObj;
					if (element.getType().equals(
							CaNanoLabConstants.STORAGE_ROOM)) {
						location.setRoom(element.getLocation());
					} else if (element.getType().equals(
							CaNanoLabConstants.STORAGE_FREEZER)) {
						location.setFreezer(element.getLocation());
					} else if (element.getType().equals(
							CaNanoLabConstants.STORAGE_SHELF)) {
						location.setShelf(element.getLocation());
					} else if (element.getType().equals(
							CaNanoLabConstants.STORAGE_BOX)) {
						location.setBox(element.getLocation());
					} else if (element.getType().equals(
							CaNanoLabConstants.STORAGE_RACK)) {
						location.setRack(element.getLocation());
					} else if (element.getType().equals(
							CaNanoLabConstants.STORAGE_LAB)) {
						location.setLab(element.getLocation());
					}
				}
				containerBean.setStorageLocation(location);
				aliquotBean.setContainer(containerBean);
				HibernateUtil.commitTransaction();
			}
		} catch (Exception e) {
			logger.error("Error in retrieving aliquot information with id -- "
					+ aliquotId, e);
			throw new RuntimeException(
					"Error in retrieving aliquot information with name -- "
							+ aliquotId);
		} finally {
			HibernateUtil.closeSession();
		}
		if (aliquotBean.getAliquotId().length() == 0) {
			return null;
		}
		return aliquotBean;
	}

	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * 
	 * @param assayId
	 * @param runBy
	 * @param runDate
	 * @param createdBy
	 * @param createdDate
	 * @throws Exception *
	 */

	public RunBean saveRun(String assayName, String runBy, Date runDate,
			String createdBy, String createdDate) throws Exception {
		// Details of Saving to RUN Table

		Long runId; // Run Id is the primary key of the saved Run

		RunBean runBean = null;
		logger.debug("ExecuteWorkflowService.saveRun(): assayName = "
				+ assayName);
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			Run doRun = new Run();

			// Retrieve the max sequence number for assay run
			String runName = CaNanoLabConstants.RUN
					+ (getLastAssayRunNum(session, assayName) + 1);
			logger.debug("ExecuteWorkflowService.saveRun(): new run name = "
					+ runName);
			doRun.setName(runName);
			doRun.setCreatedBy(createdBy);
			doRun.setCreatedDate(StringUtils.convertToDate(createdDate,
					CaNanoLabConstants.DATE_FORMAT));
			doRun.setRunBy(runBy);
			doRun.setRunDate(runDate);

			Assay assay = null;
			List assayResults = session.createQuery(
					"from Assay assay where assay.name='" + assayName + "'")
					.list();
			for (Object obj : assayResults) {
				assay = (Assay) obj;
			}
			doRun.setAssay(assay);

			runId = (Long) session.save(doRun);
			doRun.setId(runId);
			runBean = new RunBean(doRun);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Error in creating Run for assay. ", e);
			throw new RuntimeException("Error in creating Run for assay. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return runBean;
	}

	private int getLastAssayRunNum(Session session, String assayName) {
		int runNum = 0;
		try {
			String hqlString = "select run.name from Run run join run.assay  assay where assay.name='"
					+ assayName + "'";
			List results = session.createQuery(hqlString).list();
			for (Object obj : results) {
				String runName = (String) obj;
				int runSeqNum = Integer.parseInt(runName.substring(
						CaNanoLabConstants.RUN.length()).trim());
				if (runSeqNum > runNum) {
					runNum = runSeqNum;
				}
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving the last aliquot child aliquot number",
							e);
			throw new RuntimeException(
					"Error in retrieving the last aliquot child aliquot number");
		}
		return runNum;
	}

	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * 
	 * @param fileURI
	 * @param fileName
	 * @param runId
	 * @throws Exception
	 */
	public void saveFile(List<HttpUploadedFileData> fileList, String filepath,
			String runId, String inout, String creator) throws Exception {
		// fileList is a list of HttpUploadedFileData
		Date date = Calendar.getInstance().getTime();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// TODO: only one run should be returned
			Run doRun = (Run) session.load(Run.class, StringUtils
					.convertToLong(runId));
			// String assayName = doRun.getAssay().getName();
			// String assayType = doRun.getAssay().getAssayType();

			for (HttpUploadedFileData fileData : fileList) {

				if (inout.equalsIgnoreCase(CaNanoLabConstants.INPUT)) {
					InputFile doInputFile = new InputFile();
					doInputFile.setRun(doRun);
					doInputFile.setCreatedBy(creator);
					doInputFile.setCreatedDate(date);
					FileNameConvertor fconvertor = new FileNameConvertor();
					String filename = fconvertor.getConvertedFileName(fileData
							.getFileName());
					doInputFile.setUri(filepath
							+ CaNanoLabConstants.URI_SEPERATOR + filename);
					doInputFile.setFilename(getShortFilename(filename));

					session.saveOrUpdate(doInputFile);
					// logger.info("Object object retruned from inputfile = " +
					// object);
				} else if (inout.equalsIgnoreCase(CaNanoLabConstants.OUTPUT)) {
					OutputFile doOutputFile = new OutputFile();
					doOutputFile.setRun(doRun);
					doOutputFile.setCreatedBy(creator);
					doOutputFile.setCreatedDate(date);
					System.out
							.println("ExecuteWorkflowService.saveFile(): output file created Date = "
									+ date);
					FileNameConvertor fconvertor = new FileNameConvertor();
					String filename = fconvertor.getConvertedFileName(fileData
							.getFileName());
					doOutputFile.setUri(filepath
							+ CaNanoLabConstants.URI_SEPERATOR
							+ fconvertor.getConvertedFileName(fileData
									.getFileName()));
					doOutputFile.setFilename(getShortFilename(filename));

					session.save(doOutputFile);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Error in persist File information for  run -> "
					+ runId + " |  " + inout, e);
			throw new RuntimeException(
					"Error in persist File information for  run -> " + runId
							+ " |  " + inout);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public List<FileBean> getLastestFileListByRun(String runId, String type)
			throws Exception {
		List<FileBean> fileBeans = new ArrayList<FileBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			Run doRun = (Run) session.load(Run.class, StringUtils
					.convertToLong(runId));
			if (type.equalsIgnoreCase("input")) {
				Set inputFiles = (Set) doRun.getInputFileCollection();

				for (Object infile : inputFiles) {
					InputFile doInputFile = (InputFile) infile;
					/*
					 * FileBean infileBean = new FileBean();
					 * infileBean.setId(doInputFile.getId().toString());
					 * infileBean.setPath(doInputFile.getPath());
					 * infileBean.setCreatedDate(doInputFile.getCreatedDate());
					 * String status = (doInputFile.getDataStatus() == null) ? "" :
					 * doInputFile.getDataStatus().getStatus();
					 * infileBean.setFileMaskStatus(status);
					 */
					FileBean infileBean = new FileBean(doInputFile);
					fileBeans.add(infileBean);
				}
			} else if (type.equalsIgnoreCase("output")) {
				Set outputFiles = (Set) doRun.getOutputFileCollection();
				for (Object outfile : outputFiles) {
					OutputFile doOutputFile = (OutputFile) outfile;
					/*
					 * FileBean outfileBean = new FileBean();
					 * outfileBean.setId(doOutputFile.getId().toString());
					 * outfileBean.setPath(doOutputFile.getPath());
					 * outfileBean.setCreatedDate(doOutputFile.getCreatedDate());
					 * String status = (doOutputFile.getDataStatus() == null) ? "" :
					 * doOutputFile.getDataStatus().getStatus();
					 * outfileBean.setFileMaskStatus(status);
					 */
					FileBean outfileBean = new FileBean(doOutputFile);
					fileBeans.add(outfileBean);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Error in retrieving updated file list. ", e);
			throw new RuntimeException(
					"Error in retrieving updated file list. ");
		} finally {
			HibernateUtil.closeSession();
		}

		return fileBeans;
	}

	private String getShortFilename(String longName) {
		String shortname = "";
		String[] tokens = longName.split("_");
		// longname format as 20060419_15-16-42-557_filename.ext
		int timeStampLength = (tokens[0].length() + tokens[1].length() + 2);
		shortname = longName.substring(timeStampLength);
		return shortname;
	}

	public RunBean getRunBeanById(String runId) throws Exception {

		RunBean runBean = new RunBean();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select run.name, assay.name, assay.assayType from Run run join run.assay assay where run.id='"
					+ runId + "'";
			List results = session.createQuery(hqlString).list();
			for (Object obj : results) {
				Object[] values = (Object[]) obj;
				runBean.setName(StringUtils.convertToString(values[0]));
				AssayBean assayBean = new AssayBean(StringUtils
						.convertToString(values[1]), StringUtils
						.convertToString(values[2]));
				runBean.setAssayBean(assayBean);
				break;
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error in getting run by ID", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return runBean;
	}

	public RunBean getCurrentRun(String runId) throws Exception {
		// long start = System.currentTimeMillis();

		SortedSet<AliquotBean> aliquots = new TreeSet<AliquotBean>(
				new CaNanoLabComparators.AliquotBeanComparator());

		SortedSet<FileBean> inFiles = new TreeSet<FileBean>(
				new CaNanoLabComparators.FileBeanComparator());
		;
		SortedSet<FileBean> outFiles = new TreeSet<FileBean>(
				new CaNanoLabComparators.FileBeanComparator());
		RunBean runBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// Get all assay for AssayType
			String hqlStringAliquot = "select run, aliquot, aliquotStatus"
					+ " from Run run left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus"
					+ " where run.id='" + runId + "'";
			List results = session.createQuery(hqlStringAliquot).list();

			String hqlStringIn = "select run, file, fileStatus"
					+ " from Run run left join run.inputFileCollection file left join file.dataStatus fileStatus"
					+ " where run.id='" + runId + "'";
			List resultsIn = session.createQuery(hqlStringIn).list();
			String hqlStringOut = "select run, file, fileStatus"
					+ " from Run run left join run.outputFileCollection file left join file.dataStatus fileStatus"
					+ " where run.id='" + runId + "'";

			List resultsOut = session.createQuery(hqlStringOut).list();
			results.addAll(resultsIn);
			results.addAll(resultsOut);

			for (Object obj : results) {
				Object[] items = (Object[]) obj;
				runBean = new RunBean((Run) items[0]);
				AliquotBean aliquotBean = null;
				FileBean inFileBean = null;
				FileBean outFileBean = null;
				String statusStr = (items[2] == null) ? CaNanoLabConstants.ACTIVE_STATUS
						: CaNanoLabConstants.MASK_STATUS;
				if (items[1] instanceof Aliquot) {
					aliquotBean = new AliquotBean((Aliquot) items[1]);
					aliquotBean.setMaskStatus(statusStr);
				} else if (items[1] instanceof InputFile) {
					inFileBean = new FileBean((InputFile) items[1]);
					inFileBean.setFileMaskStatus(statusStr);
				} else if (items[1] instanceof OutputFile) {
					outFileBean = new FileBean((OutputFile) items[1]);
					outFileBean.setFileMaskStatus(statusStr);
				}

				if (aliquotBean != null) {
					aliquots.add(aliquotBean);
				}

				if (inFileBean != null) {
					inFiles.add(inFileBean);
				}

				if (outFileBean != null) {
					outFiles.add(outFileBean);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error in retrieving the specified run object", e);
			throw new RuntimeException(
					"Error in retrieving the specified run object");
		} finally {
			HibernateUtil.closeSession();
		}
		if (runBean != null) {
			runBean.setAliquotBeans(new ArrayList<AliquotBean>(aliquots));
			runBean.setInputFileBeans(new ArrayList<FileBean>(inFiles));
			runBean.setOutputFileBeans(new ArrayList<FileBean>(outFiles));
		}

		// System.out.println(System.currentTimeMillis() - start);
		return runBean;
	}

	public List<RunBean> searchRun(String sampleSource, String assayName,
			String runBy, Date runDate) throws Exception {
		// TODO Auto-generated method stub
		String select = "select run, aliquot, aliquotStatus from Run run left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus ";
		// String select = "select run from Run run ";
		String where = "where ";
		String query = select;
		List<String> whereList = new ArrayList<String>();
		List<Object> paramList = new ArrayList<Object>();

		if (assayName.length() > 0) {
			whereList.add("run.assay.name=?");
			paramList.add(assayName);
		}
		if (runBy.length() > 0) {
			whereList.add("run.runBy=?");
			paramList.add(runBy);
		}
		if (runDate != null) {
			whereList.add("run.runDate=?");
			paramList.add(runDate);
		}
		where = where + StringUtils.join(whereList, " and ");
		if (whereList.size() > 0) {
			query = select + where;
		}

		Map<String, RunBean> runMap = new HashMap<String, RunBean>();
		Map<String, SortedSet<AliquotBean>> runAliquotMap = new HashMap<String, SortedSet<AliquotBean>>();
		SortedSet<AliquotBean> aliquots = null;
		List<RunBean> runs = new ArrayList<RunBean>();
		try {
			HibernateUtil.beginTransaction();
			List resultsOut = HibernateUtil
					.createQueryByParam(query, paramList).list();
			for (Object obj : resultsOut) {
				Object[] items = (Object[]) obj;
				RunBean run = new RunBean((Run) items[0]);
				runMap.put(run.getId(), run);
				AliquotBean aliquotBean = null;
				String statusStr = (items[2] == null) ? CaNanoLabConstants.ACTIVE_STATUS
						: CaNanoLabConstants.MASK_STATUS;
				if (items[1] instanceof Aliquot) {
					aliquotBean = new AliquotBean((Aliquot) items[1]);
					aliquotBean.setMaskStatus(statusStr);
				}
				if (runAliquotMap.get(run.getId()) != null) {
					aliquots = (SortedSet<AliquotBean>) runAliquotMap.get(run
							.getId());
				} else {
					aliquots = new TreeSet<AliquotBean>(
							new CaNanoLabComparators.AliquotBeanComparator());
					runAliquotMap.put(run.getId(), aliquots);
				}
				if (aliquotBean != null) {
					aliquots.add(aliquotBean);
				}
			}

			// filter by sampleSource and set aliquots for each run
			for (String runId : runAliquotMap.keySet()) {
				SortedSet<AliquotBean> runAliquots = (SortedSet<AliquotBean>) runAliquotMap
						.get(runId);
				List<AliquotBean> filteredAliquots = new ArrayList<AliquotBean>();
				for (AliquotBean aliquot : runAliquots) {
					if (sampleSource.length() == 0
							|| (aliquot.getSample().getSampleSource() != null && aliquot
									.getSample().getSampleSource().equals(
											sampleSource))) {
						filteredAliquots.add(aliquot);
					}
				}
				RunBean run = (RunBean) runMap.get(runId);
				if (!filteredAliquots.isEmpty()) {
					run.setAliquotBeans(filteredAliquots);
					runs.add(run);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error in searching runs", e);
			throw new RuntimeException("Error in searching runs");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(runs, new CaNanoLabComparators.RunBeanComparator());
		return runs;
	}
}
