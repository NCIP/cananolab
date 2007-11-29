package gov.nih.nci.calab.service.sample;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.ContainerInfoBean;
import gov.nih.nci.calab.dto.sample.StorageLocation;
import gov.nih.nci.calab.exception.DuplicateEntriesException;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Session;

/**
 * 
 * @author pansu
 * 
 */
/* CVS $Id: AliquotService.java,v 1.1 2007-11-29 19:18:11 pansu Exp $ */

public class AliquotService {
	private static Logger logger = Logger.getLogger(AliquotService.class);

	/**
	 * 
	 * @param aliquotName
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
	public List<AliquotBean> searchAliquotsByAliquotName(String aliquotName,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation)
			throws Exception {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String storageFrom = "";
			if (aliquotName.length() > 0) {
				where = "where ";
				if (aliquotName.indexOf("*") != -1) {
					aliquotName = aliquotName.replace('*', '%');
					whereList.add("aliquot.name like ?");
				} else {
					whereList.add("aliquot.name=?");
				}
				paramList.add(aliquotName);
			}
			if (sampleType.length() > 0) {
				paramList.add(sampleType);
				where = "where ";
				whereList.add("aliquot.sample.type=?");
			}

			if (sampleSource.length() > 0) {
				paramList.add(sampleSource);
				where = "where ";
				whereList.add("aliquot.sample.source.organizationName=?");
			}

			if (sourceSampleId.length() > 0) {
				paramList.add(sourceSampleId);
				where = "where ";
				whereList.add("aliquot.sample.sourceSampleId=?");
			}

			if (dateAccessionedBegin != null) {
				paramList.add(dateAccessionedBegin);
				where = "where ";
				whereList.add("aliquot.createdDate>=?");
			}

			if (dateAccessionedEnd != null) {
				paramList.add(dateAccessionedEnd);
				where = "where ";
				whereList.add("aliquot.createdDate<=?");
			}
			if (sampleSubmitter.length() > 0) {
				paramList.add(sampleSubmitter);
				where = "where ";
				whereList.add("aliquot.createdBy=?");
			}

			if (storageLocation.getRoom().length() > 0) {
				paramList.add(storageLocation.getRoom());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Room' and storage.location=?");
			}

			if (storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Freezer' and storage.location=?");
			}

			if (storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add(" storage.type='Shelf' and storage.location=?");
			}

			if (storageLocation.getBox().length() > 0) {
				paramList.add(storageLocation.getBox());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Box' and storage.location=?");
			}

			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select aliquot from Aliquot aliquot "
					+ storageFrom + where + whereStr;
			HibernateUtil.beginTransaction();

			List<? extends Object> results = (HibernateUtil.createQueryByParam(
					hqlString, paramList).list());
			for (Object obj : new HashSet<Object>(results)) {
				Aliquot aliquot = (Aliquot) obj;
				aliquots.add(new AliquotBean(aliquot));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error in searching aliquots by the given parameters",
					e);
			throw new RuntimeException(
					"Error in searching aliquots by the given parameters");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(aliquots,
				new CaNanoLabComparators.AliquotBeanComparator());
		return aliquots;
	}

	public List<AliquotBean> searchAliquotsByContainer(String containerId)
			throws Exception {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select aliquot from Aliquot aliquot join aliquot.parentSampleContainerCollection container where container.id="
					+ containerId;
			List results = session.createQuery(hqlString).list();
			for (Object obj : results) {
				Aliquot aliquot = (Aliquot) obj;
				aliquots.add(new AliquotBean(aliquot));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error(
					"Error in searching aliquots by the given container ID", e);
			throw new RuntimeException(
					"Error in searching aliquots by the given container ID");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(aliquots,
				new CaNanoLabComparators.AliquotBeanComparator());
		return aliquots;
	}

	/**
	 * Retrieving all unmasked aliquots for use in views create run, use aliquot
	 * and create aliquot.
	 * 
	 * @return a Map between sample name and its associated unmasked aliquots
	 * @throws Exception
	 */

	public Map<String, SortedSet<AliquotBean>> getUnmaskedSampleAliquots()
			throws Exception {
		SortedSet<AliquotBean> aliquots = null;
		Map<String, SortedSet<AliquotBean>> sampleAliquots = new HashMap<String, SortedSet<AliquotBean>>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select aliquot.id, aliquot.name, aliquot.sample.name from Aliquot aliquot where aliquot.dataStatus is null order by aliquot.name";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] info = (Object[]) obj;
				AliquotBean aliquot = new AliquotBean(StringUtils
						.convertToString(info[0]), StringUtils
						.convertToString(info[1]),
						CaNanoLabConstants.ACTIVE_STATUS);
				String sampleName = (String) info[2];
				if (sampleAliquots.get(sampleName) != null) {
					aliquots = sampleAliquots
							.get(sampleName);
				} else {
					aliquots = new TreeSet<AliquotBean>(
							new CaNanoLabComparators.AliquotBeanComparator());
					sampleAliquots.put(sampleName, aliquots);
				}
				aliquots.add(aliquot);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot Ids and names", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot Ids and names");
		} finally {
			HibernateUtil.closeSession();
		}
		return sampleAliquots;
	}

	public SortedSet<String> getAllAliquotContainerTypes() throws Exception {
		SortedSet<String> containerTypes = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct aliquot.containerType from Aliquot aliquot order by aliquot.containerType";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				containerTypes.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot container types", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot container types.");
		} finally {
			HibernateUtil.closeSession();
		}
		containerTypes.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CONTAINER_TYPES));
		return containerTypes;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getAliquotContainerInfo() throws Exception {
		SampleService sampleService = new SampleService();
		return sampleService.getSampleContainerInfo();
	}

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

					session.save(doAliquot);

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
							session.save(box);
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
							session.save(shelf);
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
							session.save(freezer);
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
							session.save(room);
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

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<LabelValueBean> getAliquotCreateMethods() throws Exception {
		List<LabelValueBean> createMethods = new ArrayList<LabelValueBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select sop.name, file.uri from SampleSOP sop join sop.sampleSOPFileCollection file where sop.description='aliquot creation'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
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
			HibernateUtil.closeSession();
		}
		return createMethods;
	}
}
