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
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
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
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
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
			logger.error("Error in saving Run Aliquot ", e);
			throw new RuntimeException("Error in saving Run Aliquot ");
		}
	}

	public AliquotBean getAliquot(String aliquotId) {
		AliquotBean aliquotBean = null;
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
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
				containerBean.setConcentration(doAliquot.getConcentration().toString());
				containerBean.setConcentrationUnit(doAliquot.getConcentrationUnit());
				containerBean.setContainerComments(doAliquot.getComments());
				containerBean.setContainerType(doAliquot.getContainerType());
				containerBean.setQuantity(doAliquot.getQuantity().toString());
				containerBean.setQuantityUnit(doAliquot.getQuantityUnit());
				containerBean.setSafetyPrecaution(doAliquot.getSafetyPrecaution());
				containerBean.setSolvent(doAliquot.getDiluentsSolvent());
				containerBean.setStorageCondition(doAliquot.getStorageCondition());
				containerBean.setVolume(doAliquot.getVolume().toString());
				containerBean.setVolumeUnit(doAliquot.getVolumeUnit());
				
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
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving aliquot information with name -- " + aliquotId, e);
			throw new RuntimeException("Error in retrieving aliquot information with name -- " + aliquotId);
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
			
			ida.close();
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			ida.close();
			logger.error("Error in creating Run for assay. ", e);
			throw new RuntimeException("Error in creating Run for assay. ");
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
	 * Save the aliquot IDs to be associated with the given run ID.
	 * @param fileURI
	 * @param fileName
	 * @param runId
	 * @throws Exception
	 */
	public void saveFileInfo(String fileURI, String fileName, String runId) throws Exception {
		//TODO fill in details for saving RUN INFO for the run
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
			ida.close();
		} catch (Exception e) {
			e.printStackTrace();
			ida.close();		
			throw new RuntimeException("Error in retriving execute workflow objects ");
		}
		return typedAssayBeans;
	}	
}
