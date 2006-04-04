package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.administration.SampleBean;

import java.util.ArrayList;
import java.util.List;

/* CVS $Id: ManageSampleService.java,v 1.9 2006-04-04 15:32:54 pansu Exp $ */
public class ManageSampleService {
 
  public List<String> getAllSampleSOPs() {
	  List<String> sampleSOPs=new ArrayList();
	  sampleSOPs.add("Original");
	  sampleSOPs.add("Testing");
	  return sampleSOPs;
  }

  /**
   * 
   * @return auto-generated default value for sample ID prefix
   */
  public String getDefaultSampleIdPrefix() {
	  //tmp code to be replaced
	  String sampleIdPrefix="NCL-6";
	  return sampleIdPrefix;
  }
 
  /**
   * 
   * @param sampleIdPrefix
   * @param lotId
   * @return sampleId from sampleId prefix and lotId
   */
  public String getSampleId(String sampleIdPrefix, String lotId) {
	  return sampleIdPrefix+"-"+lotId;
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
