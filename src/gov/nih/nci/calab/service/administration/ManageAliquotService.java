/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.exception.DuplicateEntriesException;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author pansu
 * 
 */

/*
 * CVS $Id: ManageAliquotService.java,v 1.22 2006-05-31 19:18:50 pansu Exp $
 */

public class ManageAliquotService {
	private static Logger logger = Logger.getLogger(ManageAliquotService.class);

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<LabelValueBean> getAliquotCreateMethods() throws Exception {
		List<LabelValueBean> createMethods = new ArrayList<LabelValueBean>();
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select sop.name, file.path from SampleSOP sop join sop.sampleSOPFileCollection file where sop.description='aliquot creation'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				String sopName = (String) ((Object[]) obj)[0];
				String sopURI = (String) ((Object[]) obj)[1];
				String sopURL = (sopURI == null) ? "" : sopURI;
				createMethods.add(new LabelValueBean(sopName, sopURL));
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all sample sources", e);
			throw new RuntimeException("Error in retrieving all sample sources");
		} finally {
			ida.close();
		}
		return createMethods;
	}

	public int getDefaultAliquotMatrixColumnNumber() {
		return 10;
	}

	/**
	 * 
	 * @param sampleName
	 * @param parentaliquotName
	 * @return the existing prefix for assigning a new aliquot ID.
	 */
	public String getAliquotPrefix(String sampleName, String parentaliquotName) {

		if (parentaliquotName.length() == 0) {
			return sampleName + "-";
		} else {
			return parentaliquotName + "-";
		}
	}

	/**
	 * 
	 * @param sampleName
	 * @param parentaliquotName
	 * @return the first number for assigning a new aliquot IDs.
	 */
	public int getFirstAliquotNum(String sampleName, String parentaliquotName) throws Exception {
		int aliquotNum = 0;
		if (parentaliquotName.length() == 0) {
			aliquotNum = getLastSampleAliquotNum(sampleName) + 1;
		} else {
			aliquotNum = getLastAliquotChildAliquotNum(parentaliquotName) + 1;
		}
		return aliquotNum;
	}

	private int getLastSampleAliquotNum(String sampleName) throws Exception {
		int aliquotNum = 0;
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select container from Sample sample join sample.sampleContainerCollection container where sample.name='"
					+ sampleName + "'";
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
			logger.error("Error in retrieving the last sample aliquot number",
					e);
			throw new RuntimeException(
					"Error in retrieving the last sample aliquot number");
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
	 * @param sampleName
	 * @param parentaliquotName
	 * @param aliquotMatrix
	 * @throws Exception
	 */

	public void saveAliquots(String sampleName, String parentAliquotName,
			List<AliquotBean[]> aliquotMatrix) throws Exception {
		// Check to if the aliquot is from Sample or Aliqot
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();

			Aliquot parentAliquot = null;
			Sample sample = null;
			if ((parentAliquotName != null) && (parentAliquotName.length() > 0)) {
				// Get aliqot ID and load the object
				List aliquots = ida
						.search("from Aliquot aliquot where aliquot.name='"
								+ parentAliquotName + "'");
				parentAliquot = (Aliquot) aliquots.get(0);
				sample = parentAliquot.getSample();
			} else {
				List samples = ida
						.search("from Sample sample where sample.name='"
								+ sampleName + "'");
				sample = (Sample) samples.get(0);
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
							CalabConstants.OTHER)) {
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

					// Associations
					// 1. ParentAliquos or Sample
					if ((parentAliquotName != null)
							&& (parentAliquotName.length() > 0)) {
						doAliquot.getParentSampleContainerCollection().add(
								parentAliquot);
					} 
					doAliquot.setSample(sample);


					ida.createObject(doAliquot);

					// 3. StorageElement
					HashSet<StorageElement> storages = new HashSet<StorageElement>();

					String boxValue = containerBean.getStorageLocation()
							.getBox();
					if ((boxValue != null) && (boxValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CalabConstants.STORAGE_BOX
										+ "' and se.location = '"
										+ boxValue
										+ "'");
						StorageElement box = null;
						if (existedSE.size() > 0) {
							box = (StorageElement) existedSE.get(0);
						} else {
							box = new StorageElement();
							box.setLocation(boxValue);
							box.setType(CalabConstants.STORAGE_BOX);
							ida.store(box);
						}
						storages.add(box);
					}

					String shelfValue = containerBean.getStorageLocation()
							.getShelf();
					if ((shelfValue != null) && (shelfValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CalabConstants.STORAGE_SHELF
										+ "' and se.location = '"
										+ shelfValue
										+ "'");
						StorageElement shelf = null;
						if (existedSE.size() > 0) {
							shelf = (StorageElement) existedSE.get(0);
						} else {
							shelf = new StorageElement();
							shelf.setLocation(shelfValue);
							shelf.setType(CalabConstants.STORAGE_SHELF);
							ida.store(shelf);
						}
						storages.add(shelf);
					}

					String freezerValue = containerBean.getStorageLocation()
							.getFreezer();
					if ((freezerValue != null) && (freezerValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CalabConstants.STORAGE_FREEZER
										+ "' and se.location = '"
										+ freezerValue + "'");
						StorageElement freezer = null;
						if (existedSE.size() > 0) {
							freezer = (StorageElement) existedSE.get(0);
						} else {
							freezer = new StorageElement();
							freezer.setLocation(freezerValue);
							freezer.setType(CalabConstants.STORAGE_FREEZER);
							ida.store(freezer);
						}
						storages.add(freezer);
					}

					String roomValue = containerBean.getStorageLocation()
							.getRoom();
					if ((roomValue != null) && (roomValue.length() > 0)) {
						List existedSE = ida
								.search("from StorageElement se where se.type = '"
										+ CalabConstants.STORAGE_ROOM
										+ "' and se.location = '"
										+ roomValue
										+ "'");
						StorageElement room = null;
						if (existedSE.size() > 0) {
							room = (StorageElement) existedSE.get(0);
						} else {
							room = new StorageElement();
							room.setLocation(roomValue);
							room.setType(CalabConstants.STORAGE_ROOM);
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
