package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.ui.administration.ContainerInfoBean;

import java.util.ArrayList;
import java.util.List;

/* CVS $Id: ManageSampleService.java,v 1.2 2006-03-13 21:37:11 pansu Exp $ */
public class ManageSampleService {
  /**
   * 
   * @return all sample types
   */
  public List<String> getAllSampleTypes() {
		// tmp code to be replaced
		List<String> sampleTypes = new ArrayList();
		sampleTypes.add("Dentrimer");
		sampleTypes.add("Polymer");
		// end of tmp code
		
		return sampleTypes;
  }
  
  /**
   * 
   * @return auto-generated default value for sample ID
   */
  public String getDefaultSampleId() {
	  //tmp code to be replaced
	  String sampleId="NCL-6-1";
	  return sampleId;
  }
  
  /**
   * 
   * @return the default lot Id
   */
  public String getDefaultLotId() {
	  String lotId="1";
	  return lotId;
  }
  
  /**
   * 
   * @return the default container information in a form of ContainerInfoBean
   */
  public ContainerInfoBean getContainerInfo() {
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

		ContainerInfoBean containerInfo = new ContainerInfoBean(containerTypes,
				quantityUnits, concentrationUnits, volumeUnits, rooms,
				freezers);
		// end of tmp code
		return containerInfo;
  }
}
