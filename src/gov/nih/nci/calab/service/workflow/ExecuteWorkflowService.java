package gov.nih.nci.calab.service.workflow;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Run;
import gov.nih.nci.calab.domain.RunSampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.dto.workflow.ExecuteWorkflowBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class ExecuteWorkflowService {
	private static Logger logger = Logger.getLogger(ExecuteWorkflowService.class);


	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 *
	 * @param runId
	 * @param aliquotIds
	 * @throws Exception
	 */
	public void saveRunAliquots(String runId, String[] aliquotIds,
			String comments, String creator, String creationDate) throws Exception {
		// TODO:   Would be helpful if the aliquot ID are the primary key  and Run ID is the primary key
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			//load run object
			Run doRun = (Run)ida.load(Run.class, StringUtils.convertToLong(runId));

			// Create RunSampleContainer collection
			for (int i=0;i<aliquotIds.length;i++) {
				RunSampleContainer doRunSC = new RunSampleContainer();
				doRunSC.setComments(comments);
				doRunSC.setRun(doRun);
				doRunSC.setSampleContainer((Aliquot)ida.load(Aliquot.class, StringUtils.convertToLong(aliquotIds[i])));
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

		String runId;

		runId= "1";  // Run Id is the primary key of the saved Run

		return runId;
	}

	public ExecuteWorkflowBean getFileInfoForRunId(String runId) throws Exception {
		//TODO fill in details for saving RUN INFO for the run
		return null;
	}


	/**
	 * Save the aliquot IDs to be associated with the given run ID.
	 * @param fileURI
	 * @param fileName
	 * @param runId
	 * @throws Exception
	 */
	public void saveFileInfo(String fileURI, String[] fileName, String runId) throws Exception {
		//TODO fill in details for saving RUN INFO for the run
	}

	/**
	 * Get the File information for the given Run Id.
	 * @param runId
	 * @throws Exception
	 */
	public ExecuteWorkflowBean getAllWorkflows() throws Exception {
		//TODO fill in details for saving RUN INFO for the run
		return null;
	}

}
