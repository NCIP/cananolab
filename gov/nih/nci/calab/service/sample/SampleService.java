package gov.nih.nci.calab.service.sample;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.SampleSOP;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.ContainerInfoBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.dto.sample.StorageLocation;
import gov.nih.nci.calab.exception.DuplicateEntriesException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
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
import org.hibernate.Session;

/**
 * 
 * @author pansu
 * 
 */
/* CVS $Id: SampleService.java,v 1.1 2007-11-29 19:18:11 pansu Exp $ */

public class SampleService {
	private static Logger logger = Logger.getLogger(SampleService.class);

	/**
	 * Search database for sample container information based on sample ID
	 * 
	 * @param sampleId
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return a list of SampleBean
	 */
	public List<SampleBean> searchSamplesBySampleName(String sampleName,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation)
			throws Exception {
		List<SampleBean> samples = new ArrayList<SampleBean>();

		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String storageFrom = "";

			if (sampleName != null && sampleName.length() > 0) {
				where = "where ";
				if (sampleName.indexOf("*") != -1) {
					sampleName = sampleName.replace('*', '%');
					whereList.add("sample.name like ?");
				} else {
					whereList.add("sample.name=?");
				}
				paramList.add(sampleName);
			}
			if (sampleType != null && sampleType.length() > 0) {
				paramList.add(sampleType);
				where = "where ";
				whereList.add("sample.type=?");
			}

			if (sampleSource != null && sampleSource.length() > 0) {
				paramList.add(sampleSource);
				where = "where ";
				whereList.add("sample.source.organizationName=?");
			}

			if (sourceSampleId != null && sourceSampleId.length() > 0) {
				paramList.add(sourceSampleId);
				where = "where ";
				whereList.add("sample.sourceSampleId=?");
			}

			if (dateAccessionedBegin != null) {
				paramList.add(dateAccessionedBegin);
				where = "where ";
				whereList.add("sample.createdDate>=?");
			}

			if (dateAccessionedEnd != null) {
				paramList.add(dateAccessionedEnd);
				where = "where ";
				whereList.add("sample.createdDate<=?");
			}
			if (sampleSubmitter != null && sampleSubmitter.length() > 0) {
				paramList.add(sampleSubmitter);
				where = "where ";
				whereList.add("sample.createdBy=?");
			}

			if (storageLocation != null
					&& storageLocation.getRoom().length() > 0) {
				paramList.add(storageLocation.getRoom());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Room' and storage.location=?");
			}

			if (storageLocation != null
					&& storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Freezer' and storage.location=?");
			}

			if (storageLocation != null
					&& storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add(" storage.type='Shelf' and storage.location=?");
			}

			if (storageLocation != null
					&& storageLocation.getBox().length() > 0) {
				paramList.add(storageLocation.getBox());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Box' and storage.location=?");
			}

			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select sample from Sample sample "
					+ storageFrom + where + whereStr;

			HibernateUtil.beginTransaction();

			List<? extends Object> results = (HibernateUtil.createQueryByParam(
					hqlString, paramList).list());
			for (Object obj : new HashSet<Object>(results)) {
				Sample sample = (Sample) obj;
				samples.add(new SampleBean(sample));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error("Error in searching sample by the given parameters",
							e);
			throw new RuntimeException(
					"Error in searching sample by the given parameters");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(samples,
				new CaNanoLabComparators.SampleBeanComparator());
		return samples;
	}

	/**
	 * Search database for sample container information based on other
	 * parameters
	 * 
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
	public List<SampleBean> searchSamples(String sampleType,
			String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation)
			throws Exception {

		return searchSamplesBySampleName("", sampleType, sampleSource,
				sourceSampleId, dateAccessionedBegin, dateAccessionedEnd,
				sampleSubmitter, storageLocation);
	}

	public Sample searchSampleBy(String charId) throws Exception {
		Sample sample = null;
		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session.createQuery(
					" from Nanoparticle nano left join fetch nano.characterizationCollection chara"
							+ " where chara.id=" + charId).list();
			for (Object obj : results) {
				sample = (Sample) obj;
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization");
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

		return sample;
	}

	public List<LabFile> searchLabFilesBy(String charId) throws Exception {
		Sample sample = this.searchSampleBy(charId);

		List<LabFile> labFiles = new ArrayList<LabFile>();

		if (sample != null) {
			LabFile labFile;
			try {

				Session session = HibernateUtil.currentSession();
				HibernateUtil.beginTransaction();
				List results = session
						.createQuery(
								" from Sample sample left join fetch sample.sampleContainerCollection"
										+ " left join fetch sample.sampleContainerCollection.runSampleContainerCollection"
										+ " left join fetch sample.sampleContainerCollection.runSampleContainerCollection.run"
										+ " left join fetch sample.sampleContainerCollection.runSampleContainerCollection.run.outputFileCollection"
										+ " where sample.id=" + sample.getId())
						.list();
				for (Object obj : results) {
					labFile = (LabFile) obj;
					labFiles.add(labFile);
				}
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				logger.error("Problem finding characterization", e);
				throw e;
			} finally {
				HibernateUtil.closeSession();
			}
		}
		return labFiles;
	}

	/**
	 * 
	 * @return a map between sample name and its sample containers
	 * @throws Exception
	 */
	public Map<String, SortedSet<ContainerBean>> getAllSampleContainers()
			throws Exception {
		SortedSet<ContainerBean> containers = null;
		Map<String, SortedSet<ContainerBean>> sampleContainers = new HashMap<String, SortedSet<ContainerBean>>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select container, container.sample.name from SampleContainer container";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] info = (Object[]) obj;
				if (!(info[0] instanceof Aliquot)) {
					ContainerBean container = new ContainerBean(
							(SampleContainer) info[0]);

					String sampleName = (String) info[1];
					if (sampleContainers.get(sampleName) != null) {
						containers = (SortedSet<ContainerBean>) sampleContainers
								.get(sampleName);
					} else {
						containers = new TreeSet<ContainerBean>(
								new CaNanoLabComparators.ContainerBeanComparator());
						sampleContainers.put(sampleName, containers);
					}
					containers.add(container);
				}
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all containers", e);
			throw new RuntimeException("Error in retrieving all containers");
		} finally {
			HibernateUtil.closeSession();
		}
		return sampleContainers;
	}

	public SortedSet<String> getAllSampleContainerTypes() throws Exception {
		SortedSet<String> containerTypes = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct container.containerType from SampleContainer container order by container.containerType";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				containerTypes.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all sample container types", e);
			throw new RuntimeException(
					"Error in retrieving all sample container types.");
		} finally {
			HibernateUtil.closeSession();
		}
		containerTypes.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CONTAINER_TYPES));
		return containerTypes;
	}

	/**
	 * Get all samples in the database
	 * 
	 * @return a list of SampleBean containing sample Ids and names DELETE
	 */
	public List<SampleBean> getAllSamples() throws Exception {
		List<SampleBean> samples = new ArrayList<SampleBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select sample.id, sample.name from Sample sample";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] sampleInfo = (Object[]) obj;
				samples.add(new SampleBean(StringUtils
						.convertToString(sampleInfo[0]), StringUtils
						.convertToString(sampleInfo[1])));
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all sample IDs and names", e);
			throw new RuntimeException(
					"Error in retrieving all sample IDs and names");
		} finally {
			HibernateUtil.closeSession();
		}
		Collections.sort(samples,
				new CaNanoLabComparators.SampleBeanComparator());
		return samples;
	}

	public List<String> getAllSampleSOPs() throws Exception {
		List<String> sampleSOPs = new ArrayList<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select sampleSOP.name from SampleSOP sampleSOP where sampleSOP.description='sample creation'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				sampleSOPs.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all Sample SOPs.", e);
			throw new RuntimeException("Problem to retrieve all Sample SOPs. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return sampleSOPs;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getSampleContainerInfo() throws Exception {

		Map<String, SortedSet<String>> allUnits = LookupService
				.getAllMeasureUnits();
		List<StorageElement> storageElements = getAllStorageElements();
		SortedSet<String> quantityUnits = (allUnits.get("Quantity") == null) ? new TreeSet<String>()
				: allUnits.get("Quantity");
		SortedSet<String> concentrationUnits = (allUnits.get("Concentration") == null) ? new TreeSet<String>()
				: allUnits.get("Concentration");
		SortedSet<String> volumeUnits = (allUnits.get("Volume") == null) ? new TreeSet<String>()
				: allUnits.get("Volume");
		SortedSet<String> rooms = new TreeSet<String>();
		SortedSet<String> freezers = new TreeSet<String>();
		SortedSet<String> shelves = new TreeSet<String>();
		SortedSet<String> boxes = new TreeSet<String>();

		for (StorageElement storageElement : storageElements) {
			if (storageElement.getType().equalsIgnoreCase("Room")
					&& !rooms.contains(storageElement.getLocation())) {
				rooms.add((storageElement.getLocation()));
			} else if (storageElement.getType().equalsIgnoreCase("Freezer")
					&& !freezers.contains(storageElement.getLocation())) {
				freezers.add((storageElement.getLocation()));
			} else if (storageElement.getType().equalsIgnoreCase("Shelf")
					&& !shelves.contains(storageElement.getLocation())) {
				shelves.add((storageElement.getLocation()));
			} else if (storageElement.getType().equalsIgnoreCase("Box")
					&& !boxes.contains(storageElement.getLocation())) {
				boxes.add((storageElement.getLocation()));
			}
		}

		// set labs and racks to null for now
		ContainerInfoBean containerInfo = new ContainerInfoBean(quantityUnits,
				concentrationUnits, volumeUnits, null, rooms, freezers,
				shelves, boxes);

		return containerInfo;
	}

	/**
	 * 
	 * @return all source sample IDs
	 */
	public List<String> getAllSourceSampleIds() throws Exception {
		List<String> sourceSampleIds = new ArrayList<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct sample.sourceSampleId from Sample sample order by sample.sourceSampleId";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				sourceSampleIds.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all source sample IDs", e);
			throw new RuntimeException(
					"Error in retrieving all source sample IDs");
		} finally {
			HibernateUtil.closeSession();
		}

		return sourceSampleIds;
	}

	/**
	 * 
	 * @return all sample sources
	 */
	public SortedSet<String> getAllSampleSources() throws Exception {
		SortedSet<String> sampleSources = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select source.organizationName from Source source order by source.organizationName";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				sampleSources.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all sample sources", e);
			throw new RuntimeException("Error in retrieving all sample sources");
		} finally {
			HibernateUtil.closeSession();
		}

		return sampleSources;
	}

	/**
	 * 
	 * @return a map between sample source and samples with unmasked aliquots
	 * @throws Exception
	 */
	public Map<String, SortedSet<SampleBean>> getSampleSourceSamplesWithUnmaskedAliquots()
			throws Exception {
		Map<String, SortedSet<SampleBean>> sampleSourceSamples = new HashMap<String, SortedSet<SampleBean>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct aliquot.sample from Aliquot aliquot where aliquot.dataStatus is null";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			SortedSet<SampleBean> samples = null;
			for (Object obj : results) {
				SampleBean sample = new SampleBean((Sample) obj);
				if (sampleSourceSamples.get(sample.getSampleSource()) != null) {
					if (sample.getSampleSource().length() > 0) {
						samples = (SortedSet<SampleBean>) sampleSourceSamples
								.get(sample.getSampleSource());
					}
				} else {
					samples = new TreeSet<SampleBean>(
							new CaNanoLabComparators.SampleBeanComparator());
					if (sample.getSampleSource().length() > 0) {
						sampleSourceSamples.put(sample.getSampleSource(),
								samples);
					}
				}
				samples.add(sample);
			}

		} catch (Exception e) {
			logger.error(
					"Error in retrieving sample beans with unmasked aliquots ",
					e);
			throw new RuntimeException(
					"Error in retrieving all sample beans with unmasked aliquots. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return sampleSourceSamples;
	}

	private List<StorageElement> getAllStorageElements() throws Exception {
		List<StorageElement> storageElements = new ArrayList<StorageElement>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "from StorageElement where type in ('Room', 'Freezer', 'Shelf', 'Box') and location!='Other'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				storageElements.add((StorageElement) obj);
			}

		} catch (Exception e) {
			logger.error("Error in retrieving all rooms and freezers", e);
			throw new RuntimeException(
					"Error in retrieving all rooms and freezers.");
		} finally {
			HibernateUtil.closeSession();
		}
		return storageElements;
	}

	/**
	 * 
	 * @return auto-generated default value for sample name
	 */
	public String getDefaultSampleName() throws Exception {
		// read from properties file first
		String sampleNamePrefix = PropertyReader.getProperty(
				CaNanoLabConstants.CANANOLAB_PROPERTY, "samplePrefix");
		// if not available, use the default
		if (sampleNamePrefix == null)
			sampleNamePrefix = CaNanoLabConstants.DEFAULT_SAMPLE_PREFIX;

		long seqId = 0;

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select max(sample.sampleSequenceId) from Sample sample";
			List results = session.createQuery(hqlString).list();
			logger
					.debug("ManageSampleService.getSampleSequenceId(): results.size = "
							+ results.size());
			if (results.iterator().hasNext()) {
				Object obj = results.iterator().next();
				logger
						.debug("ManageSampleService.getSampleSequenceId(): obj = "
								+ obj);
				if (obj != null) {
					seqId = (Long) obj;
				}
			}
			logger
					.debug("ManageSampleService.getSampleSequenceId(): current seq id = "
							+ seqId);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem in retrieving default sample ID prefix.", e);
			throw new RuntimeException(
					"Problem in retrieving default sample ID prefix.");
		} finally {
			HibernateUtil.closeSession();
		}

		return sampleNamePrefix + (seqId + 1);
	}

	public Long getUserDefinedSequenceId(String userDefinedPrefix) {
		String newSequence = userDefinedPrefix.substring(userDefinedPrefix
				.lastIndexOf("-") + 1);

		return StringUtils.convertToLong(newSequence);

	}

	/**
	 * 
	 * @param sampleName
	 * @param lotId
	 * @return full sample name from sampleName and lotId
	 */
	public String getSampleName(String sampleName, String lotId) {
		if (lotId.trim().length() == 0) {
			return sampleName;
		}
		return sampleName + "-" + lotId;
	}

	/**
	 * return the container prefix
	 * 
	 * @param sampleNamePrefix
	 * @param lotId
	 * @return
	 */
	public String getContainerPrefix(String sampleName, String lotId) {
		if (lotId.trim().length() == 0) {
			return sampleName + "-0";
		}
		return sampleName + "-" + lotId;
	}

	/**
	 * Saves the sample into the database
	 * 
	 * @throws Exception
	 */
	public void saveSample(SampleBean sample) throws Exception {

		// check if the smaple is exist
		// For NCL, sampleId + lotId is unique -- in SampleBean, sampleId
		// issampleName

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select count(*) from Sample sample where sample.name = '"
					+ sample.getSampleName() + "'";
			List results = session.createQuery(hqlString).list();
			if (results.iterator().hasNext()) {
				Object obj = results.iterator().next();
				logger
						.debug("ManageSampleService.saveSample(): return object type = "
								+ obj.getClass().getName()
								+ " | object value = " + obj);
				// Yes, throws exception
				if (((Integer) obj).intValue() > 0) {
					throw new DuplicateEntriesException(
							"The same sample already exists in the system.");
				}
			}
			// No, save it
			// Create Sample Object, save source, save sample, save
			// storageElement, save SampleContainer,
			// set storageElement in sampleContainer, save sampleContainer again

			// Create sample object
			Nanoparticle doSample = new Nanoparticle();

			// Front end source is a plain text, so just save the source object
			String sampleSourceName = sample.getSampleSource();
			if ((sampleSourceName != null)
					&& (sampleSourceName.trim().length() > 0)) {
				List existedSources = session.createQuery(
						"from Source source where source.organizationName = '"
								+ sampleSourceName + "'").list();

				Source source = null;
				if (existedSources.size() > 0) {
					source = (Source) existedSources.get(0);
				} else {
					source = new Source();
					source.setOrganizationName(sampleSourceName);
					session.save(source);
				}
				// Create releationship between this source and this sample
				doSample.setSource(source);
			}

			doSample.setComments(sample.getGeneralComments());
			doSample.setCreatedBy(sample.getSampleSubmitter());
			doSample.setCreatedDate(sample.getAccessionDate());
			doSample.setDescription(sample.getSampleDescription());
			doSample.setLotDescription(sample.getLotDescription());
			doSample.setLotId(sample.getLotId());
			doSample.setName(sample.getSampleName());

			doSample.setType(sample.getSampleType());

			doSample.setReceivedBy("");
			doSample.setReceivedDate(sample.getDateReceived());
			doSample.setSampleSequenceId(getUserDefinedSequenceId(sample
					.getSampleNamePrefix()));
			doSample.setSolubility(sample.getSolubility());
			doSample.setSourceSampleId(sample.getSourceSampleId());
			String sopName = sample.getSampleSOP();
			if ((sopName != null) && (sopName.length() > 0)) {
				List existedSOP = session
						.createQuery(
								"from SampleSOP sop where sop.name = '"
										+ sopName + "'").list();
				SampleSOP sop = (SampleSOP) existedSOP.get(0);
				doSample.setSampleSOP(sop);
			}
			session.save(doSample);

			// Container list
			for (ContainerBean containerBean : sample.getContainers()) {
				SampleContainer doSampleContainer = new SampleContainer();
				// use Hibernate Hilo algorithm to generate the id
				doSampleContainer.setComments(containerBean
						.getContainerComments());
				doSampleContainer.setConcentration(StringUtils
						.convertToFloat(containerBean.getConcentration()));
				doSampleContainer.setConcentrationUnit(containerBean
						.getConcentrationUnit());

				doSampleContainer.setContainerType((containerBean
						.getContainerType()));

				// Container is created by the same person who creates sample
				doSampleContainer.setCreatedBy(sample.getSampleSubmitter());
				doSampleContainer.setCreatedDate(sample.getAccessionDate());
				doSampleContainer
						.setDiluentsSolvent(containerBean.getSolvent());
				doSampleContainer.setQuantity(StringUtils
						.convertToFloat(containerBean.getQuantity()));
				doSampleContainer.setQuantityUnit(containerBean
						.getQuantityUnit());
				doSampleContainer.setSafetyPrecaution(containerBean
						.getSafetyPrecaution());
				doSampleContainer.setStorageCondition(containerBean
						.getStorageCondition());
				doSampleContainer.setVolume(StringUtils
						.convertToFloat(containerBean.getVolume()));
				doSampleContainer.setVolumeUnit(containerBean.getVolumeUnit());
				doSampleContainer.setName(containerBean.getContainerName());

				HashSet<StorageElement> storages = new HashSet<StorageElement>();

				String boxValue = containerBean.getStorageLocation().getBox();

				if ((boxValue != null) && (boxValue.trim().length() > 0)) {
					List existedSE = session.createQuery(
							"from StorageElement se where se.type = '"
									+ CaNanoLabConstants.STORAGE_BOX
									+ "' and se.location = '" + boxValue + "'")
							.list();
					StorageElement box = null;
					if (existedSE.size() > 0) {
						box = (StorageElement) existedSE.get(0);
					} else {
						box = new StorageElement();
						box.setLocation(boxValue);
						box.setType(CaNanoLabConstants.STORAGE_BOX);
						session.save(box);
					}
					// Create releationship between this source and this sample
					storages.add(box);
				}

				String shelfValue = containerBean.getStorageLocation()
						.getShelf();

				if ((shelfValue != null) && (shelfValue.trim().length() > 0)) {
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
					// Create releationship between this source and this sample
					storages.add(shelf);
				}

				String freezerValue = containerBean.getStorageLocation()
						.getFreezer();

				if ((freezerValue != null) && (freezerValue.length() > 0)) {
					List existedSE = session.createQuery(
							"from StorageElement se where se.type = '"
									+ CaNanoLabConstants.STORAGE_FREEZER
									+ "' and se.location = '" + freezerValue
									+ "'").list();
					StorageElement freezer = null;
					if (existedSE.size() > 0) {
						freezer = (StorageElement) existedSE.get(0);
					} else {
						freezer = new StorageElement();
						freezer.setLocation(freezerValue);
						freezer.setType(CaNanoLabConstants.STORAGE_FREEZER);
						session.save(freezer);
					}
					// Create releationship between this source and this sample
					storages.add(freezer);
				}

				String roomValue = containerBean.getStorageLocation().getRoom();

				if ((roomValue != null) && (roomValue.length() > 0)) {
					List existedSE = session
							.createQuery(
									"from StorageElement se where se.type = '"
											+ CaNanoLabConstants.STORAGE_ROOM
											+ "' and se.location = '"
											+ roomValue + "'").list();
					StorageElement room = null;
					if (existedSE.size() > 0) {
						room = (StorageElement) existedSE.get(0);
					} else {
						room = new StorageElement();
						room.setLocation(roomValue);
						room.setType(CaNanoLabConstants.STORAGE_ROOM);
						session.save(room);
					}
					// Create releationship between this source and this sample
					storages.add(room);
				}

				doSampleContainer.setSample(doSample);
				session.saveOrUpdate(doSampleContainer);

				logger
						.debug("ManageSampleService.saveSample(): same again with storage info");
				doSampleContainer.setStorageElementCollection(storages);
				session.saveOrUpdate(doSampleContainer);
			}
			HibernateUtil.commitTransaction();
		} catch (DuplicateEntriesException ce) {
			throw ce;
		} catch (Exception e) {
			logger.error("Problem saving the sample.", e);
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
