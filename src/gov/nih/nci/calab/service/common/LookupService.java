package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.dto.administration.ContainerInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * The service to return prepopulated data that are shared across different
 * views.
 * 
 * @author zengje
 * 
 */
/* CVS $Id: LookupService.java,v 1.4 2006-03-16 21:50:15 pansu Exp $ */

public class LookupService {

	/**
	 * Retriving all unmasked aliquot in the system, for views use aliquot,
	 * create run, create aliquot, search sample.
	 * 
	 * @return a list of aliquot id
	 */
	public List<String> getAliquots() {
		// Need detail....

		List aliquotIds = new ArrayList();
		aliquotIds.add("NCL-0-1234");
		aliquotIds.add("NCL-0-1234-0");
		aliquotIds.add("NCL-1-1234-1");
		aliquotIds.add("NCL-1-1234");
		aliquotIds.add("NCL-2-1235");
		aliquotIds.add("NCL-5-1234");
		aliquotIds.add("NCL-5-1234-0");
		aliquotIds.add("NCL-5-1234-1");
		aliquotIds.add("NCL-6-1234");
		aliquotIds.add("NCL-6-1235");

		return aliquotIds;
	}

	/**
	 * Get all the SampleType
	 */
	public List<String> getAllSampleTypes() {
		// Detail here
		// Retrieve data from Sample_Type table

		List sampleTypes = new ArrayList();
		sampleTypes.add("Dendrimer");
		sampleTypes.add("Quantom Dot");
		sampleTypes.add("Ploymer");
		sampleTypes.add("Metal Colloid");
		sampleTypes.add("Fullerence");
		sampleTypes.add("Liposome");
		sampleTypes.add("Nanotubes");

		return sampleTypes;
	}

	/**
	 * 
	 * @return the default sample container information in a form of ContainerInfoBean
	 */
	public ContainerInfoBean getSampleContainerInfo() {
		// tmp code to be replaced
		List containerTypes = new ArrayList();
		containerTypes.add("Tube");
		containerTypes.add("Vial");
		containerTypes.add("Other");

		List quantityUnits = new ArrayList();
		quantityUnits.add("g");
		quantityUnits.add("mg");

		List concentrationUnits = new ArrayList();
		concentrationUnits.add("g/ml");
		concentrationUnits.add("mg/ml");

		List volumeUnits = new ArrayList();
		volumeUnits.add("ml");
		volumeUnits.add("ul");

		List rooms = new ArrayList();
		rooms.add("250");
		rooms.add("117");

		List freezers = new ArrayList();
		freezers.add("F1");
		freezers.add("F2");
		
		//set labs and racks to null for now
		ContainerInfoBean containerInfo = new ContainerInfoBean(containerTypes,
				quantityUnits, concentrationUnits, volumeUnits, null, rooms,
				null, freezers);
		// end of tmp code
		
		return containerInfo;
	}

	/**
	 * 
	 * @return the default sample container information in a form of ContainerInfoBean
	 */
	public ContainerInfoBean getAliquotContainerInfo() {
		// tmp code to be replaced
		List containerTypes = new ArrayList();
		containerTypes.add("Tube");
		containerTypes.add("Vial");
		containerTypes.add("Other");

		List quantityUnits = new ArrayList();
		quantityUnits.add("g");
		quantityUnits.add("mg");

		List concentrationUnits = new ArrayList();
		concentrationUnits.add("g/ml");
		concentrationUnits.add("mg/ml");

		List volumeUnits = new ArrayList();
		volumeUnits.add("ml");
		volumeUnits.add("ul");

		List rooms = new ArrayList();
		rooms.add("250");
		rooms.add("117");

		List freezers = new ArrayList();
		freezers.add("F1");
		freezers.add("F2");
		
		//set labs and racks to null for now
		ContainerInfoBean containerInfo = new ContainerInfoBean(containerTypes,
				quantityUnits, concentrationUnits, volumeUnits, null, rooms,
				null, freezers);
		// end of tmp code
		
		return containerInfo;
	}

	/**
	 * 
	 * @return all sample Ids
	 */
	public List<String> getAllSampleIds() {
		// tmp code to be replaced
		List<String> sampleIds = new ArrayList();
		sampleIds.add("NCL-6");
		sampleIds.add("NCL-3");
		// end of tmp code

		return sampleIds;
	}

	/**
	 * 
	 * @return all lot Ids
	 */
	public List<String> getAllLotIds() {
		// tmp code to be replaced
		List<String> lotIds = new ArrayList();
		lotIds.add("NCL-6-1");
		lotIds.add("NCL-3-1");
		// end of tmp code

		return lotIds;
	}
}
