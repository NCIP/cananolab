package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.administration.SampleBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* CVS $Id: ManageSampleService.java,v 1.10 2006-04-04 20:02:49 zengje Exp $ */
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
	    String sampleIdPrefix="hardcode-6";
		try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select max(sample.sampleSequenceId) from Sample sample";
			List results = ida.query(hqlString, Aliquot.class.getName());
			for (Iterator iter=results.iterator(); iter.hasNext();)
			{
				Object obj = iter.next();
				System.out.println("ManageSampleService.getDefaultSampleIdPrefix(): return object type = "
									+ obj.getClass().getName());
				sampleIdPrefix = "NCL-" + obj;
			}
			ida.close();			
		}catch (Exception e){
			e.printStackTrace();
		}

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
