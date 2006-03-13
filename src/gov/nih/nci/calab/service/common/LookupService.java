package gov.nih.nci.calab.service.common;

import java.util.ArrayList;
import java.util.List;

/**
 * The service to return all the prepopulate table and data
 * 
 * @author zengje
 *
 */
public class LookupService {
	
	/**
	 * Get all the SampleType
	 */
	public List<String> getAllSampleTypes()
	{
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
	 * Get all Quantity Unit
	 */
	public List<String> getQuantityUnits()
	{
		// Detail here  retrieve data from 
		
		List quantityUnits = new ArrayList();
		quantityUnits.add("g");
		quantityUnits.add("mg");
		quantityUnits.add("ug");
		
		return quantityUnits;
	}
	
	/**
	 * Get all Concentration Unit
	 */
	public List<String> getConcentrationUnits()
	{
		// Detail here  retrieve data from 
		
		List concentrationUnits = new ArrayList();
		concentrationUnits.add("g/ml");
		concentrationUnits.add("mg/ml");
		concentrationUnits.add("ug/ml");
		concentrationUnits.add("ug/ul");
		
		return concentrationUnits;
	}
	
	/**
	 * Get all Volume Unit
	 */
	public List<String> getVolumeUnits()
	{
		// Detail here  retrieve data from 
		
		List volumeUnits = new ArrayList();
		volumeUnits.add("ml");
		volumeUnits.add("ul");
		
		return volumeUnits;
	}

}
