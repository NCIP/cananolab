package gov.nih.nci.calab.service.workflow;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Assay;
import gov.nih.nci.calab.domain.AssayType;
import gov.nih.nci.calab.domain.InputFile;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Run;
import gov.nih.nci.calab.domain.RunSampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.util.file.FileNameConvertor;
import gov.nih.nci.calab.service.util.file.HttpUploadedFileData;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class ExecuteWorkflowService {
	private static Logger logger = Logger.getLogger(ExecuteWorkflowService.class);

    /**
	 * Retrieve assays by assayType
	 * 
	 * @return a list of all assays in certain type
	 */
	private List<AssayBean> getAssayByType(String assayTypeName, IDataAccess ida) throws Exception {
		// Detail here
		List<AssayBean> assays = new ArrayList<AssayBean>();
		try {
			String hqlString = "from Assay assay where assay.assayType ='" + assayTypeName +"'";
			List results = ida.search(hqlString);
			for (Object obj: results){
				Assay doAssay = (Assay)obj;
				AssayBean assayBean = new AssayBean();
				assayBean.setAssayId(doAssay.getId().toString());
				assayBean.setAssayName(doAssay.getName());
				assayBean.setAssayType(doAssay.getAssayType());
				
				Set runs = (Set)doAssay.getRunCollection();
				List<RunBean> runBeans = new ArrayList<RunBean>();
				for (Object run: runs) {
					Run doRun = (Run)run;
					RunBean runBean = new RunBean();
					runBean.setId(doRun.getId().toString());
					runBean.setName(doRun.getName());
					
					Set runAliquots = (Set)doRun.getRunSampleContainerCollection();
					List<AliquotBean> aliquotBeans= new ArrayList<AliquotBean>();
					for(Object runAliquot: runAliquots){
						RunSampleContainer doRunAliquot = (RunSampleContainer)runAliquot;
						// Have to load the class to get away the classcastexception (Cast Lazy loaded SampleContainer to Aliquot) 
						Aliquot container = (Aliquot)ida.load(Aliquot.class, doRunAliquot.getSampleContainer().getId());
//						System.out.println("container class type = " + container.getClass().getName());
						// TODO: suppose no need to check instanceof, since run only association with Aliquot
						if (container instanceof Aliquot) {
							Aliquot doAliquot = (Aliquot)container;							
							AliquotBean aliquotBean = new AliquotBean(doAliquot.getId().toString(), doAliquot.getName());;
							aliquotBeans.add(aliquotBean);
						}						
					}
					runBean.setAliquotBeans(aliquotBeans);
					
					Set inputFiles = (Set)doRun.getInputFileCollection();
					List<FileBean> inputFileBeans = new ArrayList<FileBean>();
					for (Object infile: inputFiles) {
						InputFile doInputFile = (InputFile)infile;
						FileBean infileBean = new FileBean();
						infileBean.setId(doInputFile.getId().toString());
						infileBean.setPath(doInputFile.getPath());
						inputFileBeans.add(infileBean);
					}
					runBean.setInputFileBeans(inputFileBeans);
					
					Set outputFiles = (Set)doRun.getOutputFileCollection();
					List<FileBean> outputFileBeans = new ArrayList<FileBean>();
					for (Object outfile: outputFiles) {
						OutputFile doOutputFile = (OutputFile)outfile;
						FileBean outfileBean = new FileBean();
						outfileBean.setId(doOutputFile.getId().toString());
						outfileBean.setPath(doOutputFile.getPath());
						outputFileBeans.add(outfileBean);
					}
					runBean.setOutputFileBeans(outputFileBeans);
					
					runBeans.add(runBean);
				}					
				assayBean.setRunBeans(runBeans);
				assays.add(assayBean);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving assay by assayType -- " + assayTypeName, e);
			throw new Exception("Error in retrieving assay by assayType -- " + assayTypeName);
		}
		return assays;
	}

	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * 
	 * @param runId
	 * @param aliquotIds
	 * @throws Exception
	 */
	public void saveRunAliquots(String runId, String[] aliquotIds,
			String comments, String creator, String creationDate) throws Exception {
		
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			//load run object
			logger.debug("ExecuteWorkflowService.saveRunAliquots(): run id = " + runId);
			Run doRun = (Run)ida.load(Run.class, StringUtils.convertToLong(runId));
		
			// Create RunSampleContainer collection
			for (int i=0;i<aliquotIds.length;i++) {
				// check if the aliquot has been assigned to the run, if it is, skip it
				String hqlString ="select count(runcontainer.id) from RunSampleContainer runcontainer where runcontainer.run.id='" + runId + 
								  "' and runcontainer.sampleContainer.id='" + aliquotIds[i] + "'";
				List results = ida.search(hqlString);
				if (((Integer)results.get(0)).intValue() > 0)
				{
					logger.debug("The aliquot id " + aliquotIds[i] + " is already assigned to this run, continue .... " );
					continue;
				}
				RunSampleContainer doRunSC = new RunSampleContainer();
				doRunSC.setComments(comments);
				doRunSC.setRun(doRun);
				logger.debug("ExecuteWorkflowService.saveRunAliquots(): aliquot id = " + aliquotIds[i]);
				Aliquot doAliquot = (Aliquot)ida.load(Aliquot.class, StringUtils.convertToLong(aliquotIds[i]));
				logger.debug("ExecuteWorkflowService.saveRunAliquots(): doAliquot = " + doAliquot);
				doRunSC.setSampleContainer(doAliquot);
				doRunSC.setCreatedBy(creator);
				doRunSC.setCreatedDate(StringUtils.convertToDate(creationDate, CalabConstants.DATE_FORMAT));
				ida.createObject(doRunSC);
			}
			ida.close();
		} catch (Exception e) {
			ida.rollback();
			logger.error("Error in saving Run Aliquot ", e);
			throw new RuntimeException("Error in saving Run Aliquot ");
		}finally {
			ida.close();
		}
	}

	public AliquotBean getAliquot(String aliquotId) throws Exception {
		
		AliquotBean aliquotBean = new AliquotBean();
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "from Aliquot aliquot where aliquot.id ='" + aliquotId+ "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Aliquot doAliquot = (Aliquot) obj;
				aliquotBean.setAliquotId(aliquotId); // name
				aliquotBean.setCreationDate(StringUtils.convertDateToString(doAliquot.getCreatedDate(), CalabConstants.DATE_FORMAT));
				aliquotBean.setCreator(doAliquot.getCreatedBy());
				aliquotBean.setHowCreated(doAliquot.getCreatedMethod());
				
				// ContainerBean
				ContainerBean containerBean = new ContainerBean();
				if (doAliquot.getConcentration() != null){
					containerBean.setConcentration(doAliquot.getConcentration().toString());
					containerBean.setConcentrationUnit(doAliquot.getConcentrationUnit());					
				}
				containerBean.setContainerComments(doAliquot.getComments());
				containerBean.setContainerType(doAliquot.getContainerType());
				if (doAliquot.getQuantity() != null) {
					containerBean.setQuantity(doAliquot.getQuantity().toString());
					containerBean.setQuantityUnit(doAliquot.getQuantityUnit());					
				}
				containerBean.setSafetyPrecaution(doAliquot.getSafetyPrecaution());
				containerBean.setSolvent(doAliquot.getDiluentsSolvent());
				containerBean.setStorageCondition(doAliquot.getStorageCondition());
				if (doAliquot.getVolume() != null) {
					containerBean.setVolume(doAliquot.getVolume().toString());
					containerBean.setVolumeUnit(doAliquot.getVolumeUnit());					
				}
				
//				containerBean.setStorageLocationStr();
				StorageLocation location = new StorageLocation();
				Set storageElements = (Set)doAliquot.getStorageElementCollection();
				for(Object storageObj: storageElements) {
					StorageElement element = (StorageElement)storageObj;
					if (element.getType().equals(CalabConstants.STORAGE_ROOM)) {
						location.setRoom(element.getLocation());
					} else if (element.getType().equals(CalabConstants.STORAGE_FREEZER)) {
						location.setFreezer(element.getLocation());
					} else if (element.getType().equals(CalabConstants.STORAGE_SHELF)) {
						location.setShelf(element.getLocation());
					} else if (element.getType().equals(CalabConstants.STORAGE_BOX)) {
						location.setBox(element.getLocation());
					} else if (element.getType().equals(CalabConstants.STORAGE_RACK)) {
						location.setRack(element.getLocation());
					} else if (element.getType().equals(CalabConstants.STORAGE_LAB)) {
						location.setLab(element.getLocation());
					}					
				}
				containerBean.setStorageLocation(location);
					
				aliquotBean.setContainer(containerBean);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving aliquot information with id -- " + aliquotId, e);
			throw new RuntimeException("Error in retrieving aliquot information with name -- " + aliquotId);
		} finally {
			ida.close();
		}
		return aliquotBean;
	}
	
	
	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * @param assayId
	 * @param runBy
	 * @param runDate
	 * @param createdBy
	 * @param createdDate
	 * @throws Exception
	 */
	public String saveRun(String assayId, String runBy, String runDate,String createdBy, String createdDate ) throws Exception {
		// Details of Saving to RUN Table		
		
		Long runId; // Run Id is the primary key of the saved Run
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		
		logger.debug("ExecuteWorkflowService.saveRun(): assayId = " + assayId);
		try {
			ida.open();
			
			Run doRun = new Run();
			
			// Retrieve the max sequence number for assay run
			String runName = CalabConstants.RUN + (getLastAssayRunNum(ida,assayId)+1);
			logger.debug("ExecuteWorkflowService.saveRun(): new run name = " + runName);
			doRun.setName(runName);
			doRun.setCreatedBy(createdBy);
			doRun.setCreatedDate(StringUtils.convertToDate(createdDate, CalabConstants.DATE_FORMAT));
			doRun.setRunBy(runBy);
			doRun.setRunDate(StringUtils.convertToDate(runDate, CalabConstants.DATE_FORMAT));
			doRun.setAssay((Assay)ida.load(Assay.class, StringUtils.convertToLong(assayId)));
			
			runId =  (Long)ida.createObject(doRun);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Error in creating Run for assay. ", e);
			throw new RuntimeException("Error in creating Run for assay. ");
		} finally {
			ida.close();
		}
		return runId.toString();
	}
	
	private int getLastAssayRunNum(IDataAccess ida, String assayId) {
		int runNum = 0;
		try {
			String hqlString = "select run.name from Run run join run.assay  assay where assay.id='" + assayId + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				String runName = (String)obj;
				int runSeqNum = Integer.parseInt(runName.substring(CalabConstants.RUN.length()).trim());
				if (runSeqNum > runNum) {
					runNum = runSeqNum;
				}
			}
		} catch (Exception e) {
			logger.error("Error in retrieving the last aliquot child aliquot number",e);
			throw new RuntimeException(
					"Error in retrieving the last aliquot child aliquot number");
		}
		return runNum;
	}

	/**
	 * Get the File information for the given Run Id.
	 * @param runId
	 * @throws Exception
	 */	
	public HashMap getWorkflowAssays() throws Exception {
		
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		HashMap<String, List<AssayBean>> typedAssayBeans = new HashMap<String, List<AssayBean>>();
		try {
			ida.open();
			// Get all assay for AssayType
			String hqlString = "from AssayType assayType order by assayType.executeOrder";
			List results = ida.search(hqlString);
			for (Object obj: results) {
				String assayTypeName = ((AssayType)obj).getName();
				typedAssayBeans.put(assayTypeName, getAssayByType(assayTypeName, ida));
			}			
		} catch (Exception e) {
			e.printStackTrace();		
			throw new RuntimeException("Error in retriving execute workflow objects ");
		} finally {
			ida.close();
		}
		return typedAssayBeans;
	}	
	
	public ExecuteWorkflowBean getExecuteWorkflowBean() throws Exception {
		
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		ExecuteWorkflowBean workflowBean = new ExecuteWorkflowBean();
		int assayTypeCount = 0;
		int assayCount= 0;
		int runCount = 0;
		int aliquotCount = 0;
		int inputFileCount = 0;
		
		try {
			ida.open();
			HashMap<String, List<AssayBean>> typedAssayBeans = new HashMap<String, List<AssayBean>>();
			// Get all assay for AssayType
			String hqlString = "from AssayType assayType order by assayType.executeOrder";
			List results = ida.search(hqlString);
			
			assayTypeCount = results.size();
			
			for (Object obj: results) {
				String assayTypeName = ((AssayType)obj).getName();
				
				List<AssayBean> assays = new ArrayList<AssayBean>();
				try {
					hqlString = "from Assay assay where assay.assayType ='" + assayTypeName +"'";
					List assayResults = ida.search(hqlString);
					assayCount = assayCount + assayResults.size();
					
					for (Object assay: assayResults){
						Assay doAssay = (Assay)assay;
						AssayBean assayBean = new AssayBean();
						assayBean.setAssayId(doAssay.getId().toString());
						assayBean.setAssayName(doAssay.getName());
						assayBean.setAssayType(doAssay.getAssayType());
						
						Set runs = (Set)doAssay.getRunCollection();
						
						runCount = runCount + runs.size();
						
						List<RunBean> runBeans = new ArrayList<RunBean>();
						for (Object run: runs) {
							Run doRun = (Run)run;
							RunBean runBean = new RunBean();
							runBean.setId(doRun.getId().toString());
							runBean.setName(doRun.getName());
							runBean.setAssayBean(assayBean);
							
							Set runAliquots = (Set)doRun.getRunSampleContainerCollection();
							aliquotCount = aliquotCount + runAliquots.size();
							List<AliquotBean> aliquotBeans= new ArrayList<AliquotBean>();
							for(Object runAliquot: runAliquots){
								RunSampleContainer doRunAliquot = (RunSampleContainer)runAliquot;
								// Have to load the class to get away the classcastexception (Cast Lazy loaded SampleContainer to Aliquot) 
								Aliquot container = (Aliquot)ida.load(Aliquot.class, doRunAliquot.getSampleContainer().getId());
//								System.out.println("container class type = " + container.getClass().getName());
								// TODO: suppose no need to check instanceof, since run only association with Aliquot
								if (container instanceof Aliquot) {
									Aliquot doAliquot = (Aliquot)container;							
									AliquotBean aliquotBean = new AliquotBean(doAliquot.getId().toString(), doAliquot.getName());;
									aliquotBeans.add(aliquotBean);
								}						
							}
							runBean.setAliquotBeans(aliquotBeans);
							
                            Set inputFiles = (Set)doRun.getInputFileCollection();
							
							inputFileCount = inputFileCount + inputFiles.size();
							
							List<FileBean> inputFileBeans = new ArrayList<FileBean>();
							for (Object infile: inputFiles) {
                                logger.info("run's object input file type = " + infile.getClass().getName());
								InputFile doInputFile = (InputFile)infile;
								FileBean infileBean = new FileBean();
								infileBean.setId(doInputFile.getId().toString());
								infileBean.setPath(doInputFile.getPath());
								inputFileBeans.add(infileBean);
							}
							runBean.setInputFileBeans(inputFileBeans);
							
							Set outputFiles = (Set)doRun.getOutputFileCollection();
							List<FileBean> outputFileBeans = new ArrayList<FileBean>();
							for (Object outfile: outputFiles) {
                                logger.info("run's object input file type = " + outfile.getClass().getName());
								OutputFile doOutputFile = (OutputFile)outfile;
								FileBean outfileBean = new FileBean();
								outfileBean.setId(doOutputFile.getId().toString());
								outfileBean.setPath(doOutputFile.getPath());
								outputFileBeans.add(outfileBean);
							}
							runBean.setOutputFileBeans(outputFileBeans);
							
							runBeans.add(runBean);
						}					
						assayBean.setRunBeans(runBeans);
						assays.add(assayBean);
					}
				} catch (Exception e) {
					logger.error("Error in retrieving assay by assayType -- " + assayTypeName, e);
					throw new Exception("Error in retrieving assay by assayType -- " + assayTypeName);
				}				
				typedAssayBeans.put(assayTypeName, assays);
			}			
			// Get all count
			workflowBean.setAssayTypeCount(assayTypeCount);
			workflowBean.setAssayCount(assayCount);
			workflowBean.setRunCount(runCount);
			workflowBean.setAliquotCount(aliquotCount);
			workflowBean.setInputFileCount(inputFileCount);
			workflowBean.setAssayBeanMap(typedAssayBeans);
			
		} catch (Exception e) {
			e.printStackTrace();		
			throw new RuntimeException("Error in retriving execute workflow objects ");
		} finally {
			ida.close();
		}
		
		return workflowBean;
	}	

	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * @param fileURI
	 * @param fileName
	 * @param runId
	 * @throws Exception
	 */
	public void saveFile(List<HttpUploadedFileData> fileList, String filepath, String runId, String inout, String creator) throws Exception {
		// fileList is a list of HttpUploadedFileData
		Date date = Calendar.getInstance().getTime();

		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
            ida.open();
			// TODO: only one run should  be returned
			Run doRun = (Run)ida.load(Run.class, StringUtils.convertToLong(runId));
//			String assayName = doRun.getAssay().getName();
//			String assayType = doRun.getAssay().getAssayType();

			for (HttpUploadedFileData fileData: fileList) {

				if (inout.equalsIgnoreCase(CalabConstants.INPUT)) {
					InputFile doInputFile = new InputFile();
					doInputFile.setRun(doRun);
					doInputFile.setCreatedBy(creator);
					doInputFile.setCreatedDate(date);
					System.out.println("ExecuteWorkflowService.saveFile(): input file created Date = " + date);
					//TODO: is a "/" needed between filepath and filename?
                    FileNameConvertor fconvertor = new FileNameConvertor();
					doInputFile.setPath(filepath + File.separator + fconvertor.getConvertedFileName(fileData.getFileName()));

					Object object = ida.createObject(doInputFile);
                    logger.info("Object object retruned from inputfile = " + object);
				} else if (inout.equalsIgnoreCase(CalabConstants.OUTPUT)) {
					OutputFile doOutputFile = new OutputFile();
					doOutputFile.setRun(doRun);
					doOutputFile.setCreatedBy(creator);
					doOutputFile.setCreatedDate(date);
					System.out.println("ExecuteWorkflowService.saveFile(): output file created Date = " + date);
					doOutputFile.setPath(filepath);

					ida.createObject(doOutputFile);
				}
			}
		} catch (Exception e) {
			ida.rollback();
			e.printStackTrace();
			throw new RuntimeException("Error in persist File information for  run -> " + runId + " |  " + inout);
		} finally {
            ida.close();
        }

	}

	public RunBean getAssayInfoByRun(ExecuteWorkflowBean workflowBean, String runId)
	{
		if (workflowBean != null) {
			Collection<List<AssayBean>> typedAssayBeans = workflowBean.getAssayBeanMap().values();
			for (List<AssayBean> assayBeans: typedAssayBeans) {
				for (AssayBean assayBean: assayBeans) {
					for (RunBean runBean: assayBean.getRunBeans()) {
						if (runBean.getId().equals(runId)) {
							return runBean;
						}
					}
				}
			}
			logger.debug("Could not find Run's assay info for Run ID = " + runId);
			return null;
		} else {
			return null;
		}
	}
}
