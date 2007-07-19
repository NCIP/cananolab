package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.HibernateDataAccess;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.MeasureType;
import gov.nih.nci.calab.domain.MeasureUnit;
import gov.nih.nci.calab.domain.Protocol;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.characterization.CharacterizationTypeBean;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.ContainerInfoBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.collection.PersistentSet;

/**
 * The service to return prepopulated data that are shared across different
 * views.
 * 
 * @author zengje
 * 
 */
/* CVS $Id: LookupService.java,v 1.126 2007-07-19 21:19:57 pansu Exp $ */

public class LookupService {
	private static Logger logger = Logger.getLogger(LookupService.class);

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
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		Map<String, SortedSet<AliquotBean>> sampleAliquots = new HashMap<String, SortedSet<AliquotBean>>();
		try {
			ida.open();
			String hqlString = "select aliquot.id, aliquot.name, aliquot.sample.name from Aliquot aliquot where aliquot.dataStatus is null order by aliquot.name";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Object[] info = (Object[]) obj;
				AliquotBean aliquot = new AliquotBean(StringUtils
						.convertToString(info[0]), StringUtils
						.convertToString(info[1]),
						CaNanoLabConstants.ACTIVE_STATUS);
				String sampleName = (String) info[2];
				if (sampleAliquots.get(sampleName) != null) {
					aliquots = (SortedSet<AliquotBean>) sampleAliquots
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
			ida.close();
		}
		return sampleAliquots;
	}

	/**
	 * 
	 * @return a map between sample name and its sample containers
	 * @throws Exception
	 */
	public Map<String, SortedSet<ContainerBean>> getAllSampleContainers()
			throws Exception {
		SortedSet<ContainerBean> containers = null;
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		Map<String, SortedSet<ContainerBean>> sampleContainers = new HashMap<String, SortedSet<ContainerBean>>();
		try {
			ida.open();
			String hqlString = "select container, container.sample.name from SampleContainer container";
			List results = ida.search(hqlString);
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
			ida.close();
		}
		return sampleContainers;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getSampleContainerInfo() throws Exception {

		Map<String, SortedSet<String>> allUnits = getAllMeasureUnits();
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

	public SortedSet<String> getAllSampleContainerTypes() throws Exception {
		SortedSet<String> containerTypes = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct container.containerType from SampleContainer container order by container.containerType";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				containerTypes.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all sample container types", e);
			throw new RuntimeException(
					"Error in retrieving all sample container types.");
		} finally {
			ida.close();
		}
		containerTypes.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CONTAINER_TYPES));
		return containerTypes;
	}

	public SortedSet<String> getAllAliquotContainerTypes() throws Exception {
		SortedSet<String> containerTypes = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct aliquot.containerType from Aliquot aliquot order by aliquot.containerType";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				containerTypes.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot container types", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot container types.");
		} finally {
			ida.close();
		}
		containerTypes.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CONTAINER_TYPES));
		return containerTypes;
	}

	public Map<String, SortedSet<String>> getAllMeasureUnits() throws Exception {
		Map<String, SortedSet<String>> unitMap = new HashMap<String, SortedSet<String>>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "from MeasureUnit unit order by unit.name";
			List results = ida.search(hqlString);
			SortedSet<String> units = null;
			for (Object obj : results) {
				MeasureUnit unit = (MeasureUnit) obj;
				String type = unit.getType();
				if (unitMap.get(type) != null) {
					units = unitMap.get(type);
				} else {
					units = new TreeSet<String>();
					unitMap.put(type, units);
				}
				units.add(unit.getName());
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all measure units", e);
			throw new RuntimeException("Error in retrieving all measure units.");
		} finally {
			ida.close();
		}
		return unitMap;
	}

	private List<StorageElement> getAllStorageElements() throws Exception {
		List<StorageElement> storageElements = new ArrayList<StorageElement>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "from StorageElement where type in ('Room', 'Freezer', 'Shelf', 'Box') and location!='Other'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				storageElements.add((StorageElement) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all rooms and freezers", e);
			throw new RuntimeException(
					"Error in retrieving all rooms and freezers.");
		} finally {
			ida.close();
		}
		return storageElements;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getAliquotContainerInfo() throws Exception {
		return getSampleContainerInfo();
	}

	/**
	 * Get all samples in the database
	 * 
	 * @return a list of SampleBean containing sample Ids and names DELETE
	 */
	public List<SampleBean> getAllSamples() throws Exception {
		List<SampleBean> samples = new ArrayList<SampleBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {
			ida.open();
			String hqlString = "select sample.id, sample.name from Sample sample";
			List results = ida.search(hqlString);
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
			ida.close();
		}
		Collections.sort(samples,
				new CaNanoLabComparators.SampleBeanComparator());
		return samples;
	}

	/**
	 * Retrieve all Assay Types from the system
	 * 
	 * @return A list of all assay type
	 */
	public List getAllAssayTypes() throws Exception {
		List<String> assayTypes = new ArrayList<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select assayType.name from AssayType assayType order by assayType.executeOrder";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				assayTypes.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all assay types", e);
			throw new RuntimeException("Error in retrieving all assay types");
		} finally {
			ida.close();
		}
		return assayTypes;
	}

	/**
	 * 
	 * @return a map between assay type and its assays
	 * @throws Exception
	 */
	public Map<String, SortedSet<AssayBean>> getAllAssayTypeAssays()
			throws Exception {
		Map<String, SortedSet<AssayBean>> assayTypeAssays = new HashMap<String, SortedSet<AssayBean>>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select assay.id, assay.name, assay.assayType from Assay assay";
			List results = ida.search(hqlString);
			SortedSet<AssayBean> assays = null;
			for (Object obj : results) {
				Object[] objArray = (Object[]) obj;
				AssayBean assay = new AssayBean(
						((Long) objArray[0]).toString(), (String) objArray[1],
						(String) objArray[2]);
				if (assayTypeAssays.get(assay.getAssayType()) != null) {
					assays = (SortedSet<AssayBean>) assayTypeAssays.get(assay
							.getAssayType());
				} else {
					assays = new TreeSet<AssayBean>(
							new CaNanoLabComparators.AssayBeanComparator());
					assayTypeAssays.put(assay.getAssayType(), assays);
				}
				assays.add(assay);
			}
		} catch (Exception e) {
			// TODO: temp for debuging use. remove later
			e.printStackTrace();
			logger.error("Error in retrieving all assay beans. ", e);
			throw new RuntimeException("Error in retrieving all assays beans. ");
		} finally {
			ida.close();
		}
		return assayTypeAssays;
	}

	/**
	 * 
	 * @return all sample sources
	 */
	public SortedSet<String> getAllSampleSources() throws Exception {
		SortedSet<String> sampleSources = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select source.organizationName from Source source order by source.organizationName";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				sampleSources.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all sample sources", e);
			throw new RuntimeException("Error in retrieving all sample sources");
		} finally {
			ida.close();
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
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct aliquot.sample from Aliquot aliquot where aliquot.dataStatus is null";
			List results = ida.search(hqlString);
			SortedSet<SampleBean> samples = null;
			for (Object obj : results) {
				SampleBean sample = new SampleBean((Sample) obj);
				if (sampleSourceSamples.get(sample.getSampleSource()) != null) {
					// TODO need to make sample source a required field
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
			ida.close();
		}
		return sampleSourceSamples;
	}

	public List<String> getAllSampleSOPs() throws Exception {
		List<String> sampleSOPs = new ArrayList<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select sampleSOP.name from SampleSOP sampleSOP where sampleSOP.description='sample creation'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				sampleSOPs.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all Sample SOPs.");
			throw new RuntimeException("Problem to retrieve all Sample SOPs. ");
		} finally {
			ida.close();
		}
		return sampleSOPs;
	}

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<LabelValueBean> getAliquotCreateMethods() throws Exception {
		List<LabelValueBean> createMethods = new ArrayList<LabelValueBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select sop.name, file.uri from SampleSOP sop join sop.sampleSOPFileCollection file where sop.description='aliquot creation'";
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

	/**
	 * 
	 * @return all source sample IDs
	 */
	public List<String> getAllSourceSampleIds() throws Exception {
		List<String> sourceSampleIds = new ArrayList<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct sample.sourceSampleId from Sample sample order by sample.sourceSampleId";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				sourceSampleIds.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all source sample IDs", e);
			throw new RuntimeException(
					"Error in retrieving all source sample IDs");
		} finally {
			ida.close();
		}

		return sourceSampleIds;
	}

	public Map<String, SortedSet<String>> getAllParticleTypeParticles()
			throws Exception {
		// TODO fill in actual database query.
		Map<String, SortedSet<String>> particleTypeParticles = new HashMap<String, SortedSet<String>>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);

		try {
			ida.open();
			// String hqlString = "select particle.type, particle.name from
			// Nanoparticle particle";
			String hqlString = "select particle.type, particle.name from Nanoparticle particle where size(particle.characterizationCollection) = 0";
			List results = ida.search(hqlString);
			SortedSet<String> particleNames = null;
			for (Object obj : results) {
				Object[] objArray = (Object[]) obj;
				String particleType = (String) objArray[0];
				String particleName = (String) objArray[1];

				if (particleType != null) {
					// check if the particle already has visibility group
					// assigned, if yes, do NOT add to the list
					List<String> groups = userService.getAccessibleGroups(
							particleName, CaNanoLabConstants.CSM_READ_ROLE);
					if (groups.size() == 0) {
						if (particleTypeParticles.get(particleType) != null) {
							particleNames = (SortedSet<String>) particleTypeParticles
									.get(particleType);
						} else {
							particleNames = new TreeSet<String>(
									new CaNanoLabComparators.SortableNameComparator());
							particleTypeParticles.put(particleType,
									particleNames);
						}
						particleNames.add(particleName);
					}
				}
			}
		} catch (Exception e) {
			logger
					.error("Error in retrieving all particle type particles. ",
							e);
			throw new RuntimeException(
					"Error in retrieving all particle type particles. ");
		} finally {
			ida.close();
		}
		return particleTypeParticles;
	}

	public SortedSet<String> getAllDendrimerCores() {
		SortedSet<String> cores = new TreeSet<String>();
		cores.add("Diamine");
		cores.add("Ethyline");
		return cores;
	}

	public SortedSet<String> getAllDendrimerSurfaceGroupNames()
			throws Exception {
		SortedSet<String> names = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct name from SurfaceGroupType";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				names.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all Surface Group name.");
			throw new RuntimeException(
					"Problem to retrieve all Surface Group name. ");
		} finally {
			ida.close();
		}
		return names;
	}

	public SortedSet<String> getAllDendrimerBranches() throws Exception {
		SortedSet<String> branches = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct dendrimer.branch from DendrimerComposition dendrimer where dendrimer.branch is not null";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				branches.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all Dendrimer Branches.");
			throw new RuntimeException(
					"Problem to retrieve all Dendrimer Branches. ");
		} finally {
			ida.close();
		}
		branches.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_DENDRIMER_BRANCHES));

		return branches;
	}

	public SortedSet<String> getAllDendrimerGenerations() throws Exception {
		SortedSet<String> generations = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct dendrimer.generation from DendrimerComposition dendrimer where dendrimer.generation is not null ";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				generations.add(obj.toString());
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all Dendrimer Generations.");
			throw new RuntimeException(
					"Problem to retrieve all Dendrimer Generations. ");
		} finally {
			ida.close();
		}
		generations.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_DENDRIMER_GENERATIONS));

		return generations;
	}

	public String[] getAllMetalCompositions() {
		String[] compositions = new String[] { "Gold", "Sliver", "Iron oxide" };
		return compositions;
	}

	public SortedSet<String> getAllPolymerInitiators() throws Exception {
		SortedSet<String> initiators = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct polymer.initiator from PolymerComposition polymer where polymer.initiator is not null ";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				initiators.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all Polymer Initiator.");
			throw new RuntimeException(
					"Problem to retrieve all Polymer Initiator. ");
		} finally {
			ida.close();
		}
		initiators.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_POLYMER_INITIATORS));

		return initiators;
	}

	public SortedSet<String> getAllParticleSources() throws Exception {
		// TODO fill in db code
		return getAllSampleSources();
	}

	public String getParticleClassification(String particleType) {
		String classification = CaNanoLabConstants.PARTICLE_CLASSIFICATION_MAP
				.get(particleType);
		return classification;
	}

	/**
	 * 
	 * @return a map between a characterization type and its child
	 *         characterizations.
	 */
	public Map<String, List<String>> getCharacterizationTypeCharacterizations()
			throws Exception {
		Map<String, List<String>> charTypeChars = new HashMap<String, List<String>>();
		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		try {
			hda.open();
			List<String> chars = null;
			String query = "select distinct a.category, a.name from def_characterization_category a "
					+ "where a.name not in (select distinct b.category from def_characterization_category b) "
					+ "order by a.category, a.name";
			SQLQuery queryObj = hda.getNativeQuery(query);
			queryObj.addScalar("CATEGORY", Hibernate.STRING);
			queryObj.addScalar("NAME", Hibernate.STRING);
			List results = queryObj.list();
			for (Object obj : results) {
				Object[] objarr = (Object[]) obj;
				String type = objarr[0].toString();
				String name = objarr[1].toString();
				if (charTypeChars.get(type) != null) {
					chars = (List<String>) charTypeChars.get(type);
				} else {
					chars = new ArrayList<String>();
					charTypeChars.put(type, chars);
				}
				chars.add(name);
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve all characterization type characterizations. "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve all characteriztaion type characterizations. ");
		} finally {
			hda.close();
		}
		return charTypeChars;
	}

	public List<LabelValueBean> getAllInstrumentTypeAbbrs() throws Exception {
		List<LabelValueBean> instrumentTypeAbbrs = new ArrayList<LabelValueBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct instrumentType.name, instrumentType.abbreviation from InstrumentType instrumentType where instrumentType.name is not null";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				if (obj != null) {
					Object[] objs = (Object[]) obj;
					String label = "";
					if (objs[1] != null) {
						label = (String) objs[0] + "(" + objs[1] + ")";
					} else {
						label = (String) objs[0];
					}
					instrumentTypeAbbrs.add(new LabelValueBean(label,
							(String) objs[0]));
				}
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all instrumentTypes. " + e);
			throw new RuntimeException(
					"Problem to retrieve all intrument types. ");
		} finally {
			ida.close();
		}
		return instrumentTypeAbbrs;
	}

	public Map<String, SortedSet<String>> getAllInstrumentManufacturers()
			throws Exception {
		Map<String, SortedSet<String>> instrumentManufacturers = new HashMap<String, SortedSet<String>>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();

			String hqlString = "select distinct instrumentType.name, manufacturer.name from InstrumentType instrumentType join instrumentType.manufacturerCollection manufacturer ";
			List results = ida.search(hqlString);
			SortedSet<String> manufacturers = null;
			for (Object obj : results) {
				String instrumentType = ((Object[]) obj)[0].toString();
				String manufacturer = ((Object[]) obj)[1].toString();
				if (instrumentManufacturers.get(instrumentType) != null) {
					manufacturers = (SortedSet<String>) instrumentManufacturers
							.get(instrumentType);
				} else {
					manufacturers = new TreeSet<String>();
					instrumentManufacturers.put(instrumentType, manufacturers);
				}
				manufacturers.add(manufacturer);
			}
			List allResult = ida
					.search("select manufacturer.name from Manufacturer manufacturer where manufacturer.name is not null");
			SortedSet<String> allManufacturers = null;
			allManufacturers = new TreeSet<String>();
			for (Object obj : allResult) {
				String name = (String) obj;
				allManufacturers.add(name);
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve manufacturers for intrument types "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve manufacturers for intrument types.");
		} finally {
			ida.close();
		}
		return instrumentManufacturers;
	}

	public SortedSet<String> getAllLookupTypes(String lookupType)
			throws Exception {
		SortedSet<String> types = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct name from " + lookupType;
			List results = ida.search(hqlString);
			for (Object obj : results) {
				types.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all " + lookupType + " types.");
			throw new RuntimeException("Problem to retrieve all " + lookupType
					+ " types.");
		} finally {
			ida.close();
		}
		return types;
	}

	public Map<ProtocolBean, List<ProtocolFileBean>> getAllProtocolNameVersionByType(
			String type) throws Exception {
		Map<ProtocolBean, List<ProtocolFileBean>> nameVersions = new HashMap<ProtocolBean, List<ProtocolFileBean>>();
		Map<Protocol, ProtocolBean> keyMap = new HashMap<Protocol, ProtocolBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select protocolFile, protocolFile.protocol from ProtocolFile protocolFile"
					+ " where protocolFile.protocol.type = '" + type + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Object[] array = (Object[]) obj;
				Object key = null;
				Object value = null;
				for (int i = 0; i < array.length; i++) {
					if (array[i] instanceof Protocol) {
						key = array[i];
					} else if (array[i] instanceof ProtocolFile) {
						value = array[i];
					}
				}

				if (keyMap.containsKey((Protocol) key)) {
					ProtocolBean pb = keyMap.get((Protocol) key);
					List<ProtocolFileBean> localList = nameVersions.get(pb);
					ProtocolFileBean fb = new ProtocolFileBean();
					fb.setVersion(((ProtocolFile) value).getVersion());
					fb.setId(((ProtocolFile) value).getId().toString());
					localList.add(fb);
				} else {
					List<ProtocolFileBean> localList = new ArrayList<ProtocolFileBean>();
					ProtocolFileBean fb = new ProtocolFileBean();
					fb.setVersion(((ProtocolFile) value).getVersion());
					fb.setId(((ProtocolFile) value).getId().toString());
					localList.add(fb);
					ProtocolBean protocolBean = new ProtocolBean();
					Protocol protocol = (Protocol) key;
					protocolBean.setId(protocol.getId().toString());
					protocolBean.setName(protocol.getName());
					protocolBean.setType(protocol.getType());
					nameVersions.put(protocolBean, localList);
					keyMap.put((Protocol) key, protocolBean);
				}
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve all protocol names and their versions by type "
							+ type);
			throw new RuntimeException(
					"Problem to retrieve all protocol names and their versions by type "
							+ type);
		} finally {
			ida.close();
		}
		return nameVersions;
	}

	public SortedSet<String> getAllProtocolTypes() throws Exception {
		SortedSet<String> protocolTypes = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct protocol.type from Protocol protocol where protocol.type is not null";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				protocolTypes.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all protocol types.");
			throw new RuntimeException(
					"Problem to retrieve all protocol types.");
		} finally {
			ida.close();
		}
		return protocolTypes;
	}

	public SortedSet<ProtocolBean> getAllProtocols(UserBean user)
			throws Exception {
		SortedSet<ProtocolBean> protocolBeans = new TreeSet<ProtocolBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "from Protocol as protocol left join fetch protocol.protocolFileCollection";

			List results = ida.search(hqlString);
			for (Object obj : results) {
				Protocol p = (Protocol) obj;
				ProtocolBean pb = new ProtocolBean();
				pb.setId(p.getId().toString());
				pb.setName(p.getName());
				pb.setType(p.getType());
				PersistentSet set = (PersistentSet) p
						.getProtocolFileCollection();
				// HashSet hashSet = set.
				if (!set.isEmpty()) {
					List<ProtocolFileBean> list = new ArrayList<ProtocolFileBean>();
					for (Iterator it = set.iterator(); it.hasNext();) {
						ProtocolFile pf = (ProtocolFile) it.next();
						ProtocolFileBean pfb = new ProtocolFileBean();
						pfb.setId(pf.getId().toString());
						pfb.setVersion(pf.getVersion());
						list.add(pfb);
					}
					pb.setFileBeanList(filterProtocols(list, user));
				}
				if (!pb.getFileBeanList().isEmpty())
					protocolBeans.add(pb);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all protocol names and types.");
			throw new RuntimeException(
					"Problem to retrieve all protocol names and types.");
		} finally {
			ida.close();
		}
		return protocolBeans;
	}

	private List<ProtocolFileBean> filterProtocols(
			List<ProtocolFileBean> protocolFiles, UserBean user)
			throws Exception {
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<LabFileBean> tempList = new ArrayList<LabFileBean>();
		for (ProtocolFileBean pfb : protocolFiles) {
			tempList.add((LabFileBean) pfb);
		}
		List<LabFileBean> filteredProtocols = userService.getFilteredFiles(
				user, tempList);
		protocolFiles.clear();

		if (filteredProtocols == null || filteredProtocols.isEmpty())
			return protocolFiles;
		for (LabFileBean lfb : filteredProtocols) {
			protocolFiles.add((ProtocolFileBean) lfb);
		}
		return protocolFiles;
	}

	public Map<String, String[]> getAllAgentTypes() {
		Map<String, String[]> agentTypes = new HashMap<String, String[]>();

		String[] therapeuticsAgentTypes = new String[] { "Peptide",
				"Small Molecule", "Antibody", "DNA", "Probe",
				"Image Contrast Agent", "Other" };
		agentTypes.put("Therapeutic", therapeuticsAgentTypes);
		String[] targetingAgentTypes = new String[] { "Peptide",
				"Small Molecule", "Antibody", "DNA", "Probe",
				"Image Contrast Agent", "Other" };
		agentTypes.put("Targeting", targetingAgentTypes);
		String[] imagingAgentTypes = new String[] { "Peptide",
				"Small Molecule", "Antibody", "DNA", "Probe",
				"Image Contrast Agent", "Other" };
		agentTypes.put("Imaging", imagingAgentTypes);
		String[] reportingAgentTypes = new String[] { "Peptide",
				"Small Molecule", "Antibody", "DNA", "Probe",
				"Image Contrast Agent", "Other" };
		agentTypes.put("Reporting", reportingAgentTypes);
		return agentTypes;
	}

	public String[] getAllAgentTargetTypes() {
		String[] targetTypes = new String[] { "Receptor", "Antigen", "Other" };
		return targetTypes;
	}

	public String[] getAllActivationMethods() {
		String[] activationMethods = new String[] { "NMR", "MRI", "Radiation",
				"Ultrasound", "Ultraviolet Light" };
		return activationMethods;
	}

	public List<LabelValueBean> getAllSpecies() throws Exception {
		List<LabelValueBean> species = new ArrayList<LabelValueBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		species.add(new LabelValueBean("", ""));
		try {
			for (int i = 0; i < CaNanoLabConstants.SPECIES_COMMON.length; i++) {
				String specie = CaNanoLabConstants.SPECIES_COMMON[i];
				species.add(new LabelValueBean(specie, specie));
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all species. " + e);
			throw new RuntimeException(
					"Problem to retrieve all intrument types. ");
		} finally {
			ida.close();
		}

		return species;
	}

	public String[] getAllReportTypes() {
		String[] allReportTypes = new String[] { CaNanoLabConstants.REPORT,
				CaNanoLabConstants.ASSOCIATED_FILE };
		return allReportTypes;
	}

	/**
	 * Get other particles from the given particle source
	 * 
	 * @param particleSource
	 * @param particleName
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public SortedSet<String> getOtherParticles(String particleSource,
			String particleName, UserBean user) throws Exception {

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		SortedSet<String> otherParticleNames = new TreeSet<String>();
		try {
			ida.open();
			String hqlString = "select particle.name from Nanoparticle particle where particle.source.organizationName='"
					+ particleSource
					+ "' and particle.name !='"
					+ particleName
					+ "'";

			List results = ida.search(hqlString);

			for (Object obj : results) {
				String otherParticleName = (String) obj;
				// check if user can see the particle
				boolean status = userService.checkReadPermission(user,
						otherParticleName);
				if (status) {
					otherParticleNames.add(otherParticleName);
				}
			}
		} catch (Exception e) {
			logger
					.error("Error in retrieving all particle type particles. ",
							e);
			throw new RuntimeException(
					"Error in retrieving all particle type particles. ");
		} finally {
			ida.close();
		}
		return otherParticleNames;
	}

	public List<InstrumentBean> getAllInstruments() throws Exception {
		List<InstrumentBean> instruments = new ArrayList<InstrumentBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "from Instrument instrument where instrument.type is not null order by instrument.type";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Instrument instrument = (Instrument) obj;
				instruments.add(new InstrumentBean(instrument));
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all instruments. " + e);
			throw new RuntimeException("Problem to retrieve all intruments. ");
		} finally {
			ida.close();
		}
		return instruments;
	}

	public SortedSet<String> getAllCharacterizationFileTypes() throws Exception {
		SortedSet<String> fileTypes = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct fileType.name from CharacterizationFileType fileType order by fileType.name";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				String type = (String) obj;
				fileTypes.add(type);
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve all characterization file types. "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve all characterization file types. ");
		} finally {
			ida.close();
		}
		return fileTypes;
	}

	public List<CharacterizationTypeBean> getAllCharacterizationTypes()
			throws Exception {
		List<CharacterizationTypeBean> charTypes = new ArrayList<CharacterizationTypeBean>();
		HibernateDataAccess hda = HibernateDataAccess.getInstance();
		try {
			hda.open();
			String query = "select distinct category, has_action, indent_level, category_order from def_characterization_category order by category_order";
			SQLQuery queryObj = hda.getNativeQuery(query);
			queryObj.addScalar("CATEGORY", Hibernate.STRING);
			queryObj.addScalar("HAS_ACTION", Hibernate.INTEGER);
			queryObj.addScalar("INDENT_LEVEL", Hibernate.INTEGER);
			queryObj.addScalar("CATEGORY_ORDER", Hibernate.INTEGER);
			List results = queryObj.list();
			for (Object obj : results) {
				Object[] objarr = (Object[]) obj;
				String type = objarr[0].toString();
				boolean hasAction = ((Integer) objarr[1] == 0) ? false : true;
				int indentLevel = (Integer) objarr[2];
				CharacterizationTypeBean charType = new CharacterizationTypeBean(
						type, indentLevel, hasAction);
				charTypes.add(charType);
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve all characterization types. "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve all characteriztaion types. ");
		} finally {
			hda.close();
		}
		return charTypes;
	}

	public Map<String, SortedSet<String>> getDerivedDataCategoryMap(
			String characterizationName) throws Exception {
		Map<String, SortedSet<String>> categoryMap = new HashMap<String, SortedSet<String>>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select category.name, datumName.name from DerivedBioAssayDataCategory category left join category.datumNameCollection datumName where datumName.datumParsed=false and category.characterizationName='"
					+ characterizationName + "'";
			List results = ida.search(hqlString);
			SortedSet<String> datumNames = null;
			for (Object obj : results) {
				String categoryName = ((Object[]) obj)[0].toString();
				String datumName = ((Object[]) obj)[1].toString();
				if (categoryMap.get(categoryName) != null) {
					datumNames = categoryMap.get(categoryName);
				} else {
					datumNames = new TreeSet<String>();
					categoryMap.put(categoryName, datumNames);
				}
				datumNames.add(datumName);
			}

		} catch (Exception e) {
			logger
					.error("Problem to retrieve all derived bioassay data categories. "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve all derived bioassay data categories.");

		} finally {
			ida.close();
		}
		return categoryMap;
	}

	public SortedSet<String> getDerivedDatumNames(String characterizationName)
			throws Exception {
		SortedSet<String> datumNames = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct name from DatumName where datumParsed=false and characterizationName='"
					+ characterizationName + "'";
			List results = ida.search(hqlString);

			for (Object obj : results) {
				String datumName = obj.toString();
				datumNames.add(datumName);
			}

		} catch (Exception e) {
			logger
					.error("Problem to retrieve all derived bioassay datum names. "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve all derived bioassay datum names.");

		} finally {
			ida.close();
		}
		return datumNames;
	}

	public SortedSet<String> getDerivedDataCategories(
			String characterizationName) throws Exception {
		SortedSet<String> categories = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct name from DerivedBioAssayDataCategory where characterizationName='"
					+ characterizationName + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				String category = obj.toString();
				categories.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger
					.error("Problem to retrieve all derived bioassay data categories. "
							+ e);
			throw new RuntimeException(
					"Problem to retrieve all derived bioassay data categories.");

		} finally {
			ida.close();
		}
		return categories;
	}

	public SortedSet<String> getAllCharacterizationSources() throws Exception {
		SortedSet<String> sources = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct char.source from Characterization char where char.source is not null";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				sources.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all Characterization Sources.");
			throw new RuntimeException(
					"Problem to retrieve all Characterization Sources. ");
		} finally {
			ida.close();
		}
		sources.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CHARACTERIZATION_SOURCES));

		return sources;
	}

	public SortedSet<String> getAllManufacturers() throws Exception {
		SortedSet<String> manufacturers = new TreeSet<String>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct instrument.manufacturer from Instrument instrument";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				String manufacturer = (String) obj;
				manufacturers.add(manufacturer);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve all manufacturers. " + e);
			throw new RuntimeException(
					"Problem to retrieve all manufacturers. ");
		} finally {
			ida.close();
		}
		return manufacturers;
	}
}