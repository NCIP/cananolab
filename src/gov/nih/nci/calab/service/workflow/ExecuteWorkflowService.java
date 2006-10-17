package gov.nih.nci.calab.service.workflow;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
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
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.CalabConstants;
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

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			// load run object
			logger.debug("ExecuteWorkflowService.saveRunAliquots(): run id = "
					+ runId);
			Run doRun = (Run) ida.load(Run.class, StringUtils
					.convertToLong(runId));

			// Create RunSampleContainer collection
			for (int i = 0; i < aliquotNames.length; i++) {
				// check if the aliquot has been assigned to the run, if it is,
				// skip it
				String hqlString = "select count(runcontainer.id) from RunSampleContainer runcontainer where runcontainer.run.id='"
						+ runId
						+ "' and runcontainer.sampleContainer.name='"
						+ aliquotNames[i] + "'";
				List results = ida.search(hqlString);
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

				List aliquotResults = ida
						.search("from Aliquot aliquot where aliquot.name='"
								+ aliquotNames[i] + "'");
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
						CalabConstants.DATE_FORMAT));
				ida.createObject(doRunSC);
			}
			ida.close();
		} catch (Exception e) {
			ida.rollback();
			logger.error("Error in saving Run Aliquot ", e);
			throw new RuntimeException("Error in saving Run Aliquot ");
		} finally {
			ida.close();
		}
	}

	public AliquotBean getAliquot(String aliquotId) throws Exception {
		AliquotBean aliquotBean = new AliquotBean();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "from Aliquot aliquot where aliquot.id ='"
					+ aliquotId + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Aliquot doAliquot = (Aliquot) obj;
				aliquotBean.setAliquotId(aliquotId); // name
				aliquotBean.setAliquotName(doAliquot.getName());
				aliquotBean.setCreationDate(doAliquot.getCreatedDate());
				aliquotBean.setCreator(doAliquot.getCreatedBy());
				aliquotBean.setHowCreated(doAliquot.getCreatedMethod());

				String maskStatus = (doAliquot.getDataStatus() == null) ? CalabConstants.ACTIVE_STATUS
						: CalabConstants.MASK_STATUS;
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
					if (element.getType().equals(CalabConstants.STORAGE_ROOM)) {
						location.setRoom(element.getLocation());
					} else if (element.getType().equals(
							CalabConstants.STORAGE_FREEZER)) {
						location.setFreezer(element.getLocation());
					} else if (element.getType().equals(
							CalabConstants.STORAGE_SHELF)) {
						location.setShelf(element.getLocation());
					} else if (element.getType().equals(
							CalabConstants.STORAGE_BOX)) {
						location.setBox(element.getLocation());
					} else if (element.getType().equals(
							CalabConstants.STORAGE_RACK)) {
						location.setRack(element.getLocation());
					} else if (element.getType().equals(
							CalabConstants.STORAGE_LAB)) {
						location.setLab(element.getLocation());
					}
				}
				containerBean.setStorageLocation(location);

				aliquotBean.setContainer(containerBean);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving aliquot information with id -- "
					+ aliquotId, e);
			throw new RuntimeException(
					"Error in retrieving aliquot information with name -- "
							+ aliquotId);
		} finally {
			ida.close();
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
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		RunBean runBean = null;
		logger.debug("ExecuteWorkflowService.saveRun(): assayName = "
				+ assayName);
		try {
			ida.open();

			Run doRun = new Run();

			// Retrieve the max sequence number for assay run
			String runName = CalabConstants.RUN
					+ (getLastAssayRunNum(ida, assayName) + 1);
			logger.debug("ExecuteWorkflowService.saveRun(): new run name = "
					+ runName);
			doRun.setName(runName);
			doRun.setCreatedBy(createdBy);
			doRun.setCreatedDate(StringUtils.convertToDate(createdDate,
					CalabConstants.DATE_FORMAT));
			doRun.setRunBy(runBy);
			doRun.setRunDate(runDate);

			Assay assay = null;
			List assayResults = ida
					.search("from Assay assay where assay.name='" + assayName
							+ "'");
			for (Object obj : assayResults) {
				assay = (Assay) obj;
			}
			doRun.setAssay(assay);

			runId = (Long) ida.createObject(doRun);
			doRun.setId(runId);
			runBean = new RunBean(doRun);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Error in creating Run for assay. ", e);
			throw new RuntimeException("Error in creating Run for assay. ");
		} finally {
			ida.close();
		}
		return runBean;
	}

	private int getLastAssayRunNum(IDataAccess ida, String assayName) {
		int runNum = 0;
		try {
			String hqlString = "select run.name from Run run join run.assay  assay where assay.name='"
					+ assayName + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				String runName = (String) obj;
				int runSeqNum = Integer.parseInt(runName.substring(
						CalabConstants.RUN.length()).trim());
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

	/*
	 * public ExecuteWorkflowBean getExecuteWorkflowBean() throws Exception { //
	 * long start = System.currentTimeMillis();
	 * 
	 * IDataAccess ida = (new DataAccessProxy())
	 * .getInstance(IDataAccess.HIBERNATE);
	 * 
	 * ExecuteWorkflowBean workflowBean = new ExecuteWorkflowBean();
	 * 
	 * Map<String, SortedSet<AssayBean>> assayTypeAssayMap = new HashMap<String,
	 * SortedSet<AssayBean>>(); Map<String, SortedSet<RunBean>> assayRunMap =
	 * new HashMap<String, SortedSet<RunBean>>(); Map<String, SortedSet<AliquotBean>>
	 * runAliquotMap = new HashMap<String, SortedSet<AliquotBean>>(); Map<String,
	 * SortedSet<FileBean>> runInFileMap = new HashMap<String, SortedSet<FileBean>>();
	 * Map<String, SortedSet<FileBean>> runOutFileMap = new HashMap<String,
	 * SortedSet<FileBean>>();
	 * 
	 * SortedSet<AssayBean> assays; SortedSet<RunBean> runs; SortedSet<AliquotBean>
	 * aliquots; SortedSet<FileBean> inFiles; SortedSet<FileBean> outFiles;
	 * 
	 * int assayCount = 0; int runCount = 0; int aliquotCount = 0; int
	 * inFileCount = 0;
	 * 
	 * try { ida.open(); // Get all assay for AssayType String hqlStringAliquot =
	 * "select assay, run, aliquot, aliquotStatus" + " from Assay assay left
	 * join assay.runCollection run left join run.runSampleContainerCollection
	 * runSampleContainer left join runSampleContainer.sampleContainer aliquot
	 * left join aliquot.dataStatus aliquotStatus"; List results =
	 * ida.search(hqlStringAliquot);
	 * 
	 * String hqlStringIn = "select assay, run, file, fileStatus" + " from Assay
	 * assay left join assay.runCollection run left join run.inputFileCollection
	 * file left join file.dataStatus fileStatus"; List resultsIn =
	 * ida.search(hqlStringIn); String hqlStringOut = "select assay, run, file,
	 * fileStatus" + " from Assay assay left join assay.runCollection run left
	 * join run.outputFileCollection file left join file.dataStatus fileStatus";
	 * 
	 * List resultsOut = ida.search(hqlStringOut); results.addAll(resultsIn);
	 * results.addAll(resultsOut);
	 * 
	 * for (Object obj : results) { Object[] items = (Object[]) obj; AssayBean
	 * assayBean = new AssayBean((Assay) items[0]); RunBean runBean = null; if
	 * (items[1] != null) { runBean = new RunBean((Run) items[1]); } AliquotBean
	 * aliquotBean = null; FileBean inFileBean = null; FileBean outFileBean =
	 * null; String statusStr = (items[3] == null) ?
	 * CalabConstants.ACTIVE_STATUS : CalabConstants.MASK_STATUS; if (items[2]
	 * instanceof Aliquot) { aliquotBean = new AliquotBean((Aliquot) items[2]);
	 * aliquotBean.setMaskStatus(statusStr); } else if (items[2] instanceof
	 * InputFile) { inFileBean = new FileBean((InputFile) items[2]);
	 * inFileBean.setFileMaskStatus(statusStr); } else if (items[2] instanceof
	 * OutputFile) { outFileBean = new FileBean((OutputFile) items[2]);
	 * outFileBean.setFileMaskStatus(statusStr); }
	 * 
	 * if (assayTypeAssayMap.get(assayBean.getAssayType()) == null) { assays =
	 * new TreeSet<AssayBean>( new CalabComparators.AssayBeanComparator());
	 * assayTypeAssayMap.put(assayBean.getAssayType(), assays); } else { assays =
	 * assayTypeAssayMap.get(assayBean.getAssayType()); } assays.add(assayBean);
	 * 
	 * if (runBean != null) { if (assayRunMap.get(assayBean.getAssayId()) ==
	 * null) { runs = new TreeSet<RunBean>( new
	 * CalabComparators.RunBeanComparator());
	 * assayRunMap.put(assayBean.getAssayId(), runs); } else { runs =
	 * assayRunMap.get(assayBean.getAssayId()); } runs.add(runBean); }
	 * 
	 * if (aliquotBean != null) { if (runAliquotMap.get(runBean.getId()) ==
	 * null) { aliquots = new TreeSet<AliquotBean>( new
	 * CalabComparators.AliquotBeanComparator());
	 * runAliquotMap.put(runBean.getId(), aliquots); } else { aliquots =
	 * runAliquotMap.get(runBean.getId()); } aliquots.add(aliquotBean); }
	 * 
	 * if (inFileBean != null) { if (runInFileMap.get(runBean.getId()) == null) {
	 * inFiles = new TreeSet<FileBean>( new
	 * CalabComparators.FileBeanComparator()); runInFileMap.put(runBean.getId(),
	 * inFiles); } else { inFiles = runInFileMap.get(runBean.getId()); }
	 * inFiles.add(inFileBean); }
	 * 
	 * if (outFileBean != null) { if (runOutFileMap.get(runBean.getId()) ==
	 * null) { outFiles = new TreeSet<FileBean>( new
	 * CalabComparators.FileBeanComparator());
	 * runOutFileMap.put(runBean.getId(), outFiles); } else { outFiles =
	 * runOutFileMap.get(runBean.getId()); } outFiles.add(outFileBean); } } //
	 * Get all counts and parent child associations
	 * workflowBean.setAssayTypeCount(assayTypeAssayMap.keySet().size()); for
	 * (String assayType : assayTypeAssayMap.keySet()) { SortedSet<AssayBean>
	 * typeAssays = assayTypeAssayMap .get(assayType); assayCount +=
	 * typeAssays.size(); for (AssayBean assay : typeAssays) { SortedSet<RunBean>
	 * assayRuns = assayRunMap.get(assay .getAssayId()); if (assayRuns != null) {
	 * runCount += assayRuns.size(); assay.setRunBeans(new ArrayList<RunBean>(assayRuns));
	 * for (RunBean run : assayRuns) { SortedSet<AliquotBean> runAliquots =
	 * runAliquotMap .get(run.getId()); if (runAliquots != null) { aliquotCount +=
	 * runAliquots.size(); run.setAliquotBeans(new ArrayList<AliquotBean>(
	 * runAliquots)); } SortedSet<FileBean> runInFiles = runInFileMap
	 * .get(run.getId()); if (runInFiles != null) { inFileCount +=
	 * runInFiles.size(); run.setInputFileBeans(new ArrayList<FileBean>(
	 * runInFiles)); } SortedSet<FileBean> runOutFiles = runOutFileMap
	 * .get(run.getId()); if (runOutFiles != null) { run.setOutputFileBeans(new
	 * ArrayList<FileBean>( runOutFiles)); } } } } }
	 * workflowBean.setAssayCount(assayCount);
	 * workflowBean.setRunCount(runCount);
	 * workflowBean.setAliquotCount(aliquotCount);
	 * workflowBean.setInputFileCount(inFileCount);
	 * workflowBean.setAssayBeanMap(assayTypeAssayMap); } catch (Exception e) {
	 * logger.error("Error in retriving execute workflow objects", e); throw new
	 * RuntimeException( "Error in retriving execute workflow objects "); }
	 * finally { ida.close(); } // System.out.println(System.currentTimeMillis() -
	 * start); return workflowBean; }
	 */

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

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			// TODO: only one run should be returned
			Run doRun = (Run) ida.load(Run.class, StringUtils
					.convertToLong(runId));
			// String assayName = doRun.getAssay().getName();
			// String assayType = doRun.getAssay().getAssayType();

			for (HttpUploadedFileData fileData : fileList) {

				if (inout.equalsIgnoreCase(CalabConstants.INPUT)) {
					InputFile doInputFile = new InputFile();
					doInputFile.setRun(doRun);
					doInputFile.setCreatedBy(creator);
					doInputFile.setCreatedDate(date);
					FileNameConvertor fconvertor = new FileNameConvertor();
					String filename = fconvertor.getConvertedFileName(fileData
							.getFileName());
					doInputFile.setPath(filepath + CalabConstants.URI_SEPERATOR
							+ filename);
					doInputFile.setFilename(getShortFilename(filename));

					ida.store(doInputFile);
					// logger.info("Object object retruned from inputfile = " +
					// object);
				} else if (inout.equalsIgnoreCase(CalabConstants.OUTPUT)) {
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
					doOutputFile.setPath(filepath
							+ CalabConstants.URI_SEPERATOR
							+ fconvertor.getConvertedFileName(fileData
									.getFileName()));
					doOutputFile.setFilename(getShortFilename(filename));

					ida.store(doOutputFile);
				}
			}
		} catch (Exception e) {
			ida.rollback();
			e.printStackTrace();
			throw new RuntimeException(
					"Error in persist File information for  run -> " + runId
							+ " |  " + inout);
		} finally {
			ida.close();
		}
	}

	public List<FileBean> getLastestFileListByRun(String runId, String type)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		List<FileBean> fileBeans = new ArrayList<FileBean>();
		try {
			ida.open();
			Run doRun = (Run) ida.load(Run.class, StringUtils
					.convertToLong(runId));
			if (type.equalsIgnoreCase("input")) {
				Set inputFiles = (Set) doRun.getInputFileCollection();

				for (Object infile : inputFiles) {
					InputFile doInputFile = (InputFile) infile;
					FileBean infileBean = new FileBean();
					infileBean.setId(doInputFile.getId().toString());
					infileBean.setPath(doInputFile.getPath());
					infileBean.setCreatedDate(doInputFile.getCreatedDate());
					String status = (doInputFile.getDataStatus() == null) ? ""
							: doInputFile.getDataStatus().getStatus();
					infileBean.setFileMaskStatus(status);
					fileBeans.add(infileBean);
				}
			} else if (type.equalsIgnoreCase("output")) {
				Set outputFiles = (Set) doRun.getOutputFileCollection();
				for (Object outfile : outputFiles) {
					OutputFile doOutputFile = (OutputFile) outfile;
					FileBean outfileBean = new FileBean();
					outfileBean.setId(doOutputFile.getId().toString());
					outfileBean.setPath(doOutputFile.getPath());
					outfileBean.setCreatedDate(doOutputFile.getCreatedDate());
					String status = (doOutputFile.getDataStatus() == null) ? ""
							: doOutputFile.getDataStatus().getStatus();
					outfileBean.setFileMaskStatus(status);
					fileBeans.add(outfileBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Error in retrieving updated file list. ", e);
			throw new RuntimeException(
					"Error in retrieving updated file list. ");
		} finally {
			ida.close();
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
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		RunBean runBean = new RunBean();
		try {
			ida.open();
			String hqlString = "select run.name, assay.name, assay.assayType from Run run join run.assay assay where run.id='"
					+ runId + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Object[] values = (Object[]) obj;
				runBean.setName(StringUtils.convertToString(values[0]));
				AssayBean assayBean = new AssayBean(StringUtils
						.convertToString(values[1]), StringUtils
						.convertToString(values[2]));
				runBean.setAssayBean(assayBean);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ida.close();
		}
		return runBean;
	}

	public RunBean getCurrentRun(String runId) throws Exception {
		// long start = System.currentTimeMillis();

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		SortedSet<AliquotBean> aliquots = new TreeSet<AliquotBean>(
				new CalabComparators.AliquotBeanComparator());

		SortedSet<FileBean> inFiles = new TreeSet<FileBean>(
				new CalabComparators.FileBeanComparator());
		;
		SortedSet<FileBean> outFiles = new TreeSet<FileBean>(
				new CalabComparators.FileBeanComparator());
		RunBean runBean = null;
		try {
			ida.open();
			// Get all assay for AssayType
			String hqlStringAliquot = "select run, aliquot, aliquotStatus"
					+ " from Run run left join run.runSampleContainerCollection runSampleContainer left join runSampleContainer.sampleContainer aliquot left join aliquot.dataStatus aliquotStatus"
					+ " where run.id='" + runId + "'";
			List results = ida.search(hqlStringAliquot);

			String hqlStringIn = "select run, file, fileStatus"
					+ " from Run run left join run.inputFileCollection file left join file.dataStatus fileStatus"
					+ " where run.id='" + runId + "'";
			List resultsIn = ida.search(hqlStringIn);
			String hqlStringOut = "select run, file, fileStatus"
					+ " from Run run left join run.outputFileCollection file left join file.dataStatus fileStatus"
					+ " where run.id='" + runId + "'";

			List resultsOut = ida.search(hqlStringOut);
			results.addAll(resultsIn);
			results.addAll(resultsOut);

			for (Object obj : results) {
				Object[] items = (Object[]) obj;
				runBean = new RunBean((Run) items[0]);
				AliquotBean aliquotBean = null;
				FileBean inFileBean = null;
				FileBean outFileBean = null;
				String statusStr = (items[2] == null) ? CalabConstants.ACTIVE_STATUS
						: CalabConstants.MASK_STATUS;
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

		} catch (Exception e) {
			logger.error("Error in retrieving the specified run object", e);
			throw new RuntimeException(
					"Error in retrieving the specified run object");
		} finally {
			ida.close();
		}
		runBean.setAliquotBeans(new ArrayList<AliquotBean>(aliquots));
		runBean.setInputFileBeans(new ArrayList<FileBean>(inFiles));
		runBean.setOutputFileBeans(new ArrayList<FileBean>(outFiles));

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
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		Map<String, RunBean> runMap = new HashMap<String, RunBean>();
		Map<String, SortedSet<AliquotBean>> runAliquotMap = new HashMap<String, SortedSet<AliquotBean>>();
		SortedSet<AliquotBean> aliquots = null;
		List<RunBean> runs = new ArrayList<RunBean>();
		try {
			ida.open();
			List resultsOut = ida.searchByParam(query, paramList);
			for (Object obj : resultsOut) {
				Object[] items = (Object[]) obj;
				RunBean run = new RunBean((Run) items[0]);
				runMap.put(run.getId(), run);
				AliquotBean aliquotBean = null;
				String statusStr = (items[2] == null) ? CalabConstants.ACTIVE_STATUS
						: CalabConstants.MASK_STATUS;
				if (items[1] instanceof Aliquot) {
					aliquotBean = new AliquotBean((Aliquot) items[1]);
					aliquotBean.setMaskStatus(statusStr);
				}
				if (runAliquotMap.get(run.getId()) != null) {
					aliquots = (SortedSet<AliquotBean>) runAliquotMap.get(run
							.getId());
				} else {
					aliquots = new TreeSet<AliquotBean>(
							new CalabComparators.AliquotBeanComparator());
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
		} catch (Exception e) {
			logger.error("Error in searching runs", e);
			throw new RuntimeException("Error in searching runs");
		} finally {
			ida.close();
		}

		Collections.sort(runs, new CalabComparators.RunBeanComparator());
		return runs;
	}
}
