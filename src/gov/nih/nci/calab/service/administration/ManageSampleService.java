package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.administration.SampleBean;

import java.util.ArrayList;
import java.util.List;

/* CVS $Id: ManageSampleService.java,v 1.7 2006-03-20 21:51:01 pansu Exp $ */
public class ManageSampleService {
 
  public List<String> getAllSampleSOPs() {
	  List<String> sampleSOPs=new ArrayList();
	  sampleSOPs.add("http://www.cnn.com");
	  sampleSOPs.add("http://www.google.com");
	  return sampleSOPs;
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
	  String lotId="N/A";
	  return lotId;
  }
  
  /**
   * Saves the sample into the database
   * @throws Exception
   */
  public void saveSample(SampleBean sample, ContainerBean[] containers, String comments) throws Exception{
	  //TODO fill in details to save the sample and associated containers.
  }
}
