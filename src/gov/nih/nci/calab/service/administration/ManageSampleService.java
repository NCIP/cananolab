package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.dto.administration.ContainerInfoBean;

import java.util.ArrayList;
import java.util.List;

/* CVS $Id: ManageSampleService.java,v 1.6 2006-03-20 17:00:46 pansu Exp $ */
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
  
 
}
