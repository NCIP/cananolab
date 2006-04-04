package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The service to return prepopulated data that are shared across different
 * views.
 * 
 * @author zengje
 * 
 */
/* CVS $Id: LookupService.java,v 1.8 2006-04-04 20:03:14 zengje Exp $ */

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
		aliquotIds.add("NCL-3-2345");
		aliquotIds.add("NCL-3-2345-0");
		aliquotIds.add("NCL-3-1234-1");
		aliquotIds.add("NCL-6-1234");
		aliquotIds.add("NCL-6-1235");
		
		try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select aliquot.name from Aliquot aliquot";
			List results = ida.query(hqlString, Aliquot.class.getName());
			for (Iterator iter=results.iterator(); iter.hasNext();)
			{
				Object obj = iter.next();
				System.out.println("sample name type = " + obj.getClass().getName());
				aliquotIds.add((String)obj);
			}
			ida.close();			
		}catch (Exception e){
			e.printStackTrace();
		}


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
				quantityUnits, concentrationUnits, volumeUnits, null, rooms, freezers);
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
				quantityUnits, concentrationUnits, volumeUnits, null, rooms, freezers);
		// end of tmp code
		
		return containerInfo;
	}

	/**
	 * 
	 * @return all sample Ids
	 */
	public List<String> getAllSampleIds() {
		// tmp code to be replaced
		List<String> sampleIds = new ArrayList<String>();
		sampleIds.add("NCL-6");
		sampleIds.add("NCL-3");
		// end of tmp code
		
		// Sample Name?
		try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sample.name from Sample sample";
			List results = ida.query(hqlString, Sample.class.getName());
			for (Iterator iter=results.iterator(); iter.hasNext();)
			{
				Object obj = iter.next();
				System.out.println("sample name type = " + obj.getClass().getName());
				sampleIds.add((String)obj);
			}
			ida.close();			
		}catch (Exception e){
			e.printStackTrace();
		}

		return sampleIds;
	}

	/**
	 * 
	 * @return all lot Ids
	 */
	public List<String> getAllLotIds() {
		// tmp code to be replaced
		List<String> lotIds = new ArrayList<String>();
		lotIds.add("NCL-6-1234");
		lotIds.add("NCL-3-2345");
		// end of tmp code

		return lotIds;
	}
	
	/**
	 * Retrieve all Assay Types from the system
	 *
	 * @return A list of all assay type
	 */
	public List getAllAssayTypes()
	{
		// Detail here... ...
			// if the return from DB are null or size zero
			// read from the xml

		List<String> assayTypes = new ArrayList<String>();
		assayTypes.add("Pre-screening Assay");
		assayTypes.add("In Vitro");
		assayTypes.add("In Vivo");
		return assayTypes;
	}
}
