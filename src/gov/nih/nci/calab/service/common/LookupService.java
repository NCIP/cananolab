package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.AssayType;
import gov.nih.nci.calab.domain.MeasureUnit;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.SampleType;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The service to return prepopulated data that are shared across different
 * views.
 * 
 * @author zengje
 * 
 */
/* CVS $Id: LookupService.java,v 1.18 2006-04-19 19:53:25 pansu Exp $ */

public class LookupService {
	private static Logger logger = Logger.getLogger(LookupService.class);

	/**
	 * Retriving all aliquot in the system, for views view aliquot, create run,
	 * search sample.
	 * 
	 * @return a list of AliquotBeans containing aliquot ID and aliquot name
	 */
	public List<AliquotBean> getAliquots() {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select aliquot.id, aliquot.name from Aliquot aliquot order by aliquot.name";
			List results = ida.query(hqlString, Aliquot.class.getName());
			for (Object obj : results) {
				Object[] aliquotInfo = (Object[]) obj;
				aliquots.add(new AliquotBean(StringUtils
						.convertToString(aliquotInfo[0]), StringUtils
						.convertToString(aliquotInfo[1])));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot Ids and names", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot Ids and names");
		}		
		return aliquots;
	}

	/**
	 * Retrieving all unmasked aliquots for views use aliquot and create
	 * aliquot.
	 * 
	 * @return a list of AliquotBeans containing unmasked aliquot IDs and names.
	 * 
	 */

	public List<AliquotBean> getUnmaskedAliquots() {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select aliquot.id, aliquot.name from Aliquot aliquot where aliquot.dataStatus is null order by aliquot.name";
			List results = ida.query(hqlString, Aliquot.class.getName());
			for (Object obj : results) {
				Object[] aliquotInfo = (Object[]) obj;
				aliquots.add(new AliquotBean(StringUtils
						.convertToString(aliquotInfo[0]), StringUtils
						.convertToString(aliquotInfo[1])));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot Ids and names", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot Ids and names");
		}
		return aliquots;
	}

	/**
	 * Retrieving all sample types.
	 * 
	 * @return a list of all sample types
	 */
	public List<String> getAllSampleTypes() {
		// Detail here
		// Retrieve data from Sample_Type table
		List<String> sampleTypes = new ArrayList<String>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sampleType.name from SampleType sampleType order by sampleType.name";
			List results = ida.query(hqlString, SampleType.class.getName());
			for (Object obj : results) {
				sampleTypes.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample types", e);
			throw new RuntimeException("Error in retrieving all sample types");
		}

		return sampleTypes;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getSampleContainerInfo() {
		// tmp code to be replaced
		List<String> containerTypes = getAllContainerTypes();
		List<MeasureUnit> units = getAllMeasureUnits();
		List<StorageElement> storageElements = getAllRoomAndFreezers();
		List<String> quantityUnits = new ArrayList<String>();
		List<String> concentrationUnits = new ArrayList<String>();
		List<String> volumeUnits = new ArrayList<String>();
		List<String> rooms = new ArrayList<String>();
		List<String> freezers = new ArrayList<String>();

		for (MeasureUnit unit : units) {
			if (unit.getType().equalsIgnoreCase("Quantity")) {
				quantityUnits.add(unit.getName());
			} else if (unit.getType().equalsIgnoreCase("Volume")) {
				volumeUnits.add(unit.getName());
			} else if (unit.getType().equalsIgnoreCase("Concentration")) {
				concentrationUnits.add(unit.getName());
			}
		}

		for (StorageElement storageElement : storageElements) {
			if (storageElement.getType().equalsIgnoreCase("Room")) {
				rooms.add((storageElement.getLocation()));
			} else if (storageElement.getType().equalsIgnoreCase("Freezer")) {
				freezers.add((storageElement.getLocation()));
			}
		}

		// set labs and racks to null for now
		ContainerInfoBean containerInfo = new ContainerInfoBean(containerTypes,
				quantityUnits, concentrationUnits, volumeUnits, null, rooms,
				freezers);

		return containerInfo;
	}

	private List<String> getAllContainerTypes() {
		List<String> containerTypes = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select distinct container.containerType from SampleContainer container order by container.containerType";
			List results = ida
					.query(hqlString, SampleContainer.class.getName());
			for (Object obj : results) {
				containerTypes.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all container types", e);
			throw new RuntimeException(
					"Error in retrieving all container types.");
		}
		return containerTypes;
	}

	private List<MeasureUnit> getAllMeasureUnits() {
		List<MeasureUnit> units = new ArrayList<MeasureUnit>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "from MeasureUnit";
			List results = ida.query(hqlString, MeasureUnit.class.getName());
			for (Object obj : results) {
				units.add((MeasureUnit) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all measure units", e);
			throw new RuntimeException("Error in retrieving all measure units.");
		}
		return units;
	}

	private List<StorageElement> getAllRoomAndFreezers() {
		List<StorageElement> storageElements = new ArrayList<StorageElement>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "from StorageElement where type in ('Room', 'Freezer')";
			List results = ida.query(hqlString, StorageElement.class.getName());
			for (Object obj : results) {
				storageElements.add((StorageElement) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all rooms and freezers", e);
			throw new RuntimeException(
					"Error in retrieving all rooms and freezers.");
		}
		return storageElements;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getAliquotContainerInfo() {
		return getSampleContainerInfo();
	}

	/**
	 * Get all samples in the database
	 * 
	 * @return a list of SampleBean containing sample Ids and names
	 */
	public List<SampleBean> getAllSamples() {
		List<SampleBean> samples = new ArrayList<SampleBean>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sample.id, sample.name from Sample sample order by sample.name";
			List results = ida.query(hqlString, Sample.class.getName());
			for (Object obj : results) {
				Object[] sampleInfo = (Object[]) obj;
				samples.add(new SampleBean(StringUtils
						.convertToString(sampleInfo[0]), StringUtils
						.convertToString(sampleInfo[1])));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample IDs and names", e);
			throw new RuntimeException(
					"Error in retrieving all sample IDs and names");
		}

		return samples;
	}

	/**
	 * Retrieve all Assay Types from the system
	 * 
	 * @return A list of all assay type
	 */
	public List getAllAssayTypes() {
		List<String> assayTypes = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select assayType.name from AssayType assayType order by assayType.executeOrder";
			List results = ida.query(hqlString, AssayType.class.getName());
			for (Object obj : results) {
				assayTypes.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all assay types", e);
			throw new RuntimeException("Error in retrieving all assay types");
		}
		return assayTypes;
	}
	

	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllAssignedAliquots() {
		// Detail here
		List<String> aliquots = new ArrayList<String>();
		return aliquots;
	}
	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllInFiles() {
		// Detail here
		List<String> allInFiles = new ArrayList<String>();
		return allInFiles;
	}
	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllOutFiles() {
		// Detail here
		List<String> allOutFiles = new ArrayList<String>();
		return allOutFiles;
	}

	
	/**
	 * Retrieve assays by assayType
	 *
	 * @return a list of all assays in certain type
	 */

	public List<AssayBean> getAllAssayBeans()
	{
		List<AssayBean> assayBeans = new ArrayList<AssayBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString = "select assay.id, assay.name, assay.assayType from Assay assay";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Object[] objArray = (Object[])obj;
				AssayBean assay = new AssayBean(((Long)objArray[0]).toString(), (String)objArray[1], (String)objArray[2]);
				assayBeans.add(assay);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all assay beans. ", e);
			throw new RuntimeException("Error in retrieving all assays beans. ");
		}
		return assayBeans;
	}
}
