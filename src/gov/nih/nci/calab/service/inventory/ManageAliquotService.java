package gov.nih.nci.calab.service.inventory;

import gov.nih.nci.calab.db.HibernateUtil;
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
import org.hibernate.Session;

/**
 * 
 * @author pansu
 * 
 */

/*
 * CVS $Id: ManageAliquotService.java,v 1.9 2007-08-18 02:05:10 pansu Exp $
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
		return parentName + "-";
	}

	/**
	 * 
	 * @param fromAliquot
	 * @param parentName
	 * @return the first number for assigning a new aliquot IDs.
	 */
	public int getFirstAliquotNum(boolean fromAliquot, String parentName)
			throws Exception {
		int aliquotNum = 0;
		if (!fromAliquot) {
			aliquotNum = getLastSampleContainerAliquotNum(parentName) + 1;
		} else {
			aliquotNum = getLastAliquotChildAliquotNum(parentName) + 1;
		}
		return aliquotNum;
	}

	private int getLastSampleContainerAliquotNum(String containerName)
			throws Exception {
		int aliquotNum = 0;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select child from Aliquot child join child.parentSampleContainerCollection parent where parent.name='"
					+ containerName + "'";
			List results = session.createQuery(hqlString).list();
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
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving the last sample container aliquot number",
							e);
			throw new RuntimeException(
					"Error in retrieving the last sample container aliquot number");
		} finally {
			HibernateUtil.closeSession();
		}
		return aliquotNum;
	}

	private int getLastAliquotChildAliquotNum(String parentAliquotName)
			throws Exception {
		int aliquotNum = 0;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select child from Aliquot parent join parent.childSampleContainerCollection child where parent.name='"
					+ parentAliquotName + "'";
			List results = session.createQuery(hqlString).list();
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
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving the last aliquot child aliquot number",
							e);
			throw new RuntimeException(
					"Error in retrieving the last aliquot child aliquot number");
		} finally {
			HibernateUtil.closeSession();
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
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			Aliquot parentAliquot = null;
			SampleContainer container = null;
			Sample sample = null;
			if (fromAliquot) {
				// Get aliqot ID and load the object
				List results = session.createQuery(
						"select aliquot, aliquot.sample from Aliquot aliquot where aliquot.name='"
								+ parentName + "'").list();
				parentAliquot = (Aliquot) ((Object[]) results.get(0))[0];
				sample = (Sample) ((Object[]) results.get(0))[1];
			} else {
				List results = session
						.createQuery(
								"select container, container.sample from SampleContainer container where container.name='"
										+ parentName + "'").list();
				container = (SampleContainer) ((Object[]) results.get(0))[0];
				sample = (Sample) ((Object[]) results.get(0))[1];
			}

			for (AliquotBean[] aliquotBeans : aliquotMatrix) {

				// aliquotBeans[i] != null is for the matrix is in between 10s
				for (int i = 0; i < aliquotBeans.length
						&& aliquotBeans[i] != null; i++) {
					// check if the same aliquot name exists in the system
					int total = session.createQuery(
							"from Aliquot aliquot where aliquot.name='"
									+ aliquotBeans[i].getAliquotName() + "'")
							.list().size();
					if (total > 0) {
						throw new DuplicateEntriesException(
								"The aliquot(s) already exists in the system.  Please use the Create Aliquot page to create new aliquots. ");
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
					doAliquot
							.setContainerType(containerBean.getContainerType());
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
					doAliquot.setCreatedMethod(getCreatedMethod(session,
							aliquotBean.getHowCreated()));
					doAliquot.setCreatedBy(aliquotBean.getCreator());
					doAliquot.setCreatedDate(aliquotBean.getCreationDate());
					doAliquot.setSample(sample);

					// Associations
					// 1. ParentAliquos or Sample
					if (fromAliquot) {
						doAliquot.getParentSampleContainerCollection().add(
								parentAliquot);
					} else {
						doAliquot.getParentSampleContainerCollection().add(
								container);
					}

					session.saveOrUpdate(doAliquot);

					// 3. StorageElement
					HashSet<StorageElement> storages = new HashSet<StorageElement>();

					String boxValue = containerBean.getStorageLocation()
							.getBox();
					if ((boxValue != null) && (boxValue.length() > 0)) {
						List existedSE = session.createQuery(
								"from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_BOX
										+ "' and se.location = '" + boxValue
										+ "'").list();
						StorageElement box = null;
						if (existedSE.size() > 0) {
							box = (StorageElement) existedSE.get(0);
						} else {
							box = new StorageElement();
							box.setLocation(boxValue);
							box.setType(CaNanoLabConstants.STORAGE_BOX);
							session.saveOrUpdate(box);
						}
						storages.add(box);
					}

					String shelfValue = containerBean.getStorageLocation()
							.getShelf();

					if ((shelfValue != null) && (shelfValue.length() > 0)) {
						List existedSE = session.createQuery(
								"from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_SHELF
										+ "' and se.location = '" + shelfValue
										+ "'").list();
						StorageElement shelf = null;
						if (existedSE.size() > 0) {
							shelf = (StorageElement) existedSE.get(0);
						} else {
							shelf = new StorageElement();
							shelf.setLocation(shelfValue);
							shelf.setType(CaNanoLabConstants.STORAGE_SHELF);
							session.saveOrUpdate(shelf);
						}
						storages.add(shelf);
					}

					String freezerValue = containerBean.getStorageLocation()
							.getFreezer();

					if ((freezerValue != null) && (freezerValue.length() > 0)) {
						List existedSE = session.createQuery(
								"from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_FREEZER
										+ "' and se.location = '"
										+ freezerValue + "'").list();
						StorageElement freezer = null;
						if (existedSE.size() > 0) {
							freezer = (StorageElement) existedSE.get(0);
						} else {
							freezer = new StorageElement();
							freezer.setLocation(freezerValue);
							freezer.setType(CaNanoLabConstants.STORAGE_FREEZER);
							session.saveOrUpdate(freezer);
						}
						storages.add(freezer);
					}

					String roomValue = containerBean.getStorageLocation()
							.getRoom();

					if ((roomValue != null) && (roomValue.length() > 0)) {
						List existedSE = session.createQuery(
								"from StorageElement se where se.type = '"
										+ CaNanoLabConstants.STORAGE_ROOM
										+ "' and se.location = '" + roomValue
										+ "'").list();
						StorageElement room = null;
						if (existedSE.size() > 0) {
							room = (StorageElement) existedSE.get(0);
						} else {
							room = new StorageElement();
							room.setLocation(roomValue);
							room.setType(CaNanoLabConstants.STORAGE_ROOM);
							session.saveOrUpdate(room);
						}
						storages.add(room);
					}
					doAliquot.setStorageElementCollection(storages);
					session.saveOrUpdate(doAliquot);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (DuplicateEntriesException ce) {
			HibernateUtil.rollbackTransaction();
			throw ce;
		} catch (Exception e) {
			logger.error("Error in saving aliquots", e);
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private String getCreatedMethod(Session session, String sopURI)
			throws Exception {
		String hqlString = "select sop.name from SampleSOP sop join sop.sampleSOPFileCollection sopFile where sopFile.uri='"
				+ sopURI + "'";
		List results = session.createQuery(hqlString).list();
		for (Object obj : results) {
			return (String) obj;
		}
		return "";
	}
}
