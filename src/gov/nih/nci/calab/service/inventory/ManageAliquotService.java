package gov.nih.nci.calab.service.inventory;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.exception.DuplicateEntriesException;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author pansu
 * 
 */

/*
 * CVS $Id: ManageAliquotService.java,v 1.6 2007-01-09 22:54:36 pansu Exp $
 */

public class ManageAliquotService {
	private static Logger logger = Logger.getLogger(ManageAliquotService.class);


	public int getDefaultAliquotMatrixColumnNumber() {
		return 10;
	}

	/**
	 *  
	 * @param parentName
	 * @return the existing prefix for assigning a new aliquot ID.
	 */
	public String getAliquotPrefix(String parentName) {
		return parentName+"-";
	}

	/**
	 * 
	 * @param fromAliquot
	 * @param parentName
	 * @return the first number for assigning a new aliquot IDs.
	 */
	public int getFirstAliquotNum(boolean fromAliquot, String parentName) throws Exception {
		int aliquotNum = 0;
		if (!fromAliquot) {
			aliquotNum = getLastSampleContainerAliquotNum(parentName) + 1;
		} else {
			aliquotNum = getLastAliquotChildAliquotNum(parentName) + 1;
		}
		return aliquotNum;
	}

	private int getLastSampleContainerAliquotNum(String containerName) throws Exception {
		int aliquotNum = 0;
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select child from Aliquot child join child.parentSampleContainerCollection parent where parent.name='"
				+ containerName + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				SampleContainer container = (SampleContainer) obj;
				if (container instanceof Aliquot) {
					String aliquotName = ((Aliquot) container).getName();
					String[] aliquotNameParts = aliquotName.split("-");
					int aliquotSeqNum = Integer
							.parseInt(aliquotNameParts[aliquotNameParts.length - 1]);
					if (aliquotSeqNum > aliquotNum) {
						aliquotNum = aliquotSeqNum;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in retrieving the last sample container aliquot number",
					e);
			throw new RuntimeException(
					"Error in retrieving the last sample container aliquot number");
		} finally {
			ida.close();
		}
		return aliquotNum;
	}

	private int getLastAliquotChildAliquotNum(String parentAliquotName) throws Exception{
		int aliquotNum = 0;
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select child from Aliquot parent join parent.childSampleContainerCollection child where parent.name='"
					+ parentAliquotName + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				SampleContainer container = (SampleContainer) obj;
				if (container instanceof Aliquot) {
					String aliquotName = ((Aliquot) container).getName();
					String[] aliquotNameParts = aliquotName.split("-");
					int aliquotSeqNum = Integer
							.parseInt(aliquotNameParts[aliquotNameParts.length - 1]);
					if (aliquotSeqNum > aliquotNum) {
						aliquotNum = aliquotSeqNum;
					}
				}
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving the last aliquot child aliquot number",
							e);
			throw new RuntimeException(
					"Error in retrieving the last aliquot child aliquot number");
		} finally {
			ida.close();
		}
		return aliquotNum;
	}

	/**
	 * Save the aliquots into the database
	 * 
	 * @param fromAliquot
	 * @param parentName
	 * @param aliquotMatrix
	 * @throws Exception
	 */

	public void saveAliquots(boolean fromAliquot, String parentName, 
			List<AliquotBean[]> aliquotMatrix) throws Exception {
		// Check to if the aliquot is from Sample or Aliqot
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();

			Aliquot parentAliquot = null;
			SampleContainer container = null;
			Sample sample=null;
			if (fromAliquot) {
				// Get aliqot ID and load the object
				List results = ida
						.search("select aliquot, aliquot.sample from Aliquot aliquot where aliquot.name='"
								+ parentName + "'");
				parentAliquot = (Aliquot) ((Object[])results.get(0))[0];
				sample=(Sample)((Object[])results.get(0))[1];
			} else {
				List results = ida
						.search("select container, container.sample from SampleContainer container where container.name='"
								+ parentName + "'");
				container = (SampleContainer) ((Object[])results.get(0))[0];
				sample=(Sample)((Object[])results.get(0))[1];
			}

			for (AliquotBean[] aliquotBeans : aliquotMatrix) {
				
				// aliquotBeans[i] != null is for the matrix is in between 10s
				for (int i = 0; i < aliquotBeans.length
						&& aliquotBeans[i] != null; i++) {
					// check if the same aliquot name exists in the system
					int total = ida.search("from Aliquot aliquot where aliquot.name='" + aliquotBeans[i].getAliquotName() + "'").size();
					if (total > 0){
						throw new DuplicateEntriesException ("The aliquot(s) already exists in the system.  Please use the Create Aliquot page to create new aliquots. ");
					}
					
					AliquotBean aliquotBean = aliquotBeans[i];
					Aliquot doAliquot = new Aliquot();
					// use Hibernate Hilo algorithm to generate the id

					// Attributes
					ContainerBean containerBean = aliquotBean.getContainer();
					doAliquot.setComments(containerBean.getContainerComments());
					doAliquot.setConcentration(StringUtils
							.convertToFloat(containerBean.getConcentration()));
					doAliquot.setConcentrationUnit(containerBean
							.getConcentrationUnit());
					if (containerBean.getContainerType().equals(
							CaNanoLabConstants.OTHER)) {
						doAliquot.setContainerType(containerBean
								.getOtherContainerType());
					} else {
						doAliquot.setContainerType(containerBean
								.getContainerType());
					}

					doAliquot.setDiluentsSolvent(containerBean.getSolvent());
					// TODO: construct the name (AliquotID is the whole name?)
					doAliquot.setName(aliquotBean.getAliquotName());
					doAliquot.setQuantity(StringUtils
							.convertToFloat(containerBean.getQuantity()));
					doAliquot.setQuantityUnit(containerBean.getQuantityUnit());
					doAliquot.setStorageCondition(containerBean
							.getStorageCondition());
					doAliquot.setSafetyPrecaution(containerBean
							.getSafetyPrecaution());
					doAliquot.setVolume(StringUtils
							.convertToFloat(containerBean.getVolume()));
					doAliquot.setVolumeUnit(containerBean.getVolumeUnit());
					doAliquot.setCreatedMethod(getCreatedMethod(ida, aliquotBean.getHowCreated()));
					doAliquot.setCreatedBy(aliquotBean.getCreator());
					doAliquot.setCreatedDate(aliquotBean.getCreationDate());
					doAliquot.setSample(sample);

					// Associations
					// 1. ParentAliquos or Sample
					if (fromAliquot) {
						doAliquot.getParentSampleContainerCollection().add(
								parentAliquot);
					}
					else {
						doAliquot.getParentSampleContainerCollection().add(container);
					}


					ida.createObject(doAliquot);

					// 3. StorageElement
					HashSet<StorageElement> storages = new HashSet<StorageElement>();

					String boxValue = null;
					if ((containerBean.getStorageLocation().getBox()
							.equals(CaNanoLabConstants.OTHER))) {
						boxValue=containerBean.getStorageLocation().getOtherBox();
					} else {
						boxValue=containerBean.getStorageLocation().getBox();
					}
					if ((boxValue != null) && (boxValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_BOX
										+ "' and se.location = '"
										+ boxValue
										+ "'");
						StorageElement box = null;
						if (existedSE.size() > 0) {
							box = (StorageElement) existedSE.get(0);
						} else {
							box = new StorageElement();
							box.setLocation(boxValue);
							box.setType(CaNanoLabConstants.STORAGE_BOX);
							ida.store(box);
						}
						storages.add(box);
					}

					String shelfValue = null;
					if ((containerBean.getStorageLocation().getShelf()
							.equals(CaNanoLabConstants.OTHER))) {
						shelfValue=containerBean.getStorageLocation().getOtherShelf();
					} else {
						shelfValue=containerBean.getStorageLocation().getShelf();
					}
					if ((shelfValue != null) && (shelfValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_SHELF
										+ "' and se.location = '"
										+ shelfValue
										+ "'");
						StorageElement shelf = null;
						if (existedSE.size() > 0) {
							shelf = (StorageElement) existedSE.get(0);
						} else {
							shelf = new StorageElement();
							shelf.setLocation(shelfValue);
							shelf.setType(CaNanoLabConstants.STORAGE_SHELF);
							ida.store(shelf);
						}
						storages.add(shelf);
					}

					String freezerValue = null;
					if ((containerBean.getStorageLocation().getFreezer()
							.equals(CaNanoLabConstants.OTHER))) {
						freezerValue=containerBean.getStorageLocation().getOtherFreezer();
					} else {
						freezerValue=containerBean.getStorageLocation().getFreezer();
					}
					if ((freezerValue != null) && (freezerValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_FREEZER
										+ "' and se.location = '"
										+ freezerValue + "'");
						StorageElement freezer = null;
						if (existedSE.size() > 0) {
							freezer = (StorageElement) existedSE.get(0);
						} else {
							freezer = new StorageElement();
							freezer.setLocation(freezerValue);
							freezer.setType(CaNanoLabConstants.STORAGE_FREEZER);
							ida.store(freezer);
						}
						storages.add(freezer);
					}

					String roomValue = null;
					if ((containerBean.getStorageLocation().getRoom()
							.equals(CaNanoLabConstants.OTHER))) {
						roomValue=containerBean.getStorageLocation().getOtherRoom();
					} else {
						roomValue=containerBean.getStorageLocation().getRoom();
					}
					if ((roomValue != null) && (roomValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_ROOM
										+ "' and se.location = '"
										+ roomValue
										+ "'");
						StorageElement room = null;
						if (existedSE.size() > 0) {
							room = (StorageElement) existedSE.get(0);
						} else {
							room = new StorageElement();
							room.setLocation(roomValue);
							room.setType(CaNanoLabConstants.STORAGE_ROOM);
							ida.store(room);
						}
						storages.add(room);
					}
					doAliquot.setStorageElementCollection(storages);
					ida.store(doAliquot);
				}
			}
		}
		catch (DuplicateEntriesException ce) {
			throw ce;
		}
		catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			throw e;
		} finally {
			ida.close();
		}
	}
	
	private String getCreatedMethod(IDataAccess ida, String sopURI) throws Exception {
		String hqlString = "select sop.name from SampleSOP sop join sop.sampleSOPFileCollection sopFile where sopFile.path='" + sopURI + "'";
		List results = ida.search(hqlString);
		for (Object obj: results) {
			return (String)obj;
		}
		return "";
	}
}
