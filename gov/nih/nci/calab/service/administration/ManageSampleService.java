package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/* CVS $Id: ManageSampleService.java,v 1.11 2006-04-06 17:31:03 zengje Exp $ 
 */
public class ManageSampleService {
	private static Logger logger = Logger.getLogger(ManageSampleService.class);

	public List<String> getAllSampleSOPs() {
		List<String> sampleSOPs = new ArrayList();
		sampleSOPs.add("Original");
		sampleSOPs.add("Testing");
		return sampleSOPs;
	}

	/**
	 * 
	 * @return auto-generated default value for sample ID prefix
	 */
	public String getDefaultSampleIdPrefix() throws Exception {
		// TODO: Get configurable sample format string
		String sampleIdPrefix = "NCL-" + (getSampleSequenceId()+1); 
		
		return sampleIdPrefix;
	}

	public Long getSampleSequenceId() throws Exception
	{
		Long seqId = (long)0;
		try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select max(sample.sampleSequenceId) from Sample sample";
			List results = ida.query(hqlString, Aliquot.class.getName());
			if (results.iterator().hasNext()) {
				Object obj = results.iterator().next();
				System.out.println("ManageSampleService.getDefaultSampleIdPrefix(): return object type = "
								+ obj.getClass().getName() + " | value = " + obj);
				seqId = (Long)obj;
			} else {
				seqId =  new Long(0);
			}
			ida.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception ("Problem to retrieve next sample sequence.");
		}
		return seqId;
		
	}
	/**
	 * 
	 * @param sampleIdPrefix
	 * @param lotId
	 * @return sampleId from sampleId prefix and lotId
	 */
	public String getSampleId(String sampleIdPrefix, String lotId) {
		return sampleIdPrefix + "-" + lotId;
	}

	/**
	 * 
	 * @return the default lot Id
	 */
	public String getDefaultLotId() {
		String lotId = "N/A";
		return lotId;
	}

	/**
	 * Saves the sample into the database
	 * 
	 * @throws Exception
	 */
public void saveSample(SampleBean sample, ContainerBean[] containers)
throws Exception{
	  // TODO fill in details to save the sample and associated containers.

	  // check if the smaple is exist
	  // For NCL, sampleId + lotId is unique -- in SampleBean, sampleId issampleName
	  try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString = "select count(*) from Sample sample where sample.name = '" + sample.getSampleId()+"'";
//			List results = ida.query(hqlString, Sample.class.getName());
			List results = ida.search(hqlString);
			if (results.iterator().hasNext())
			{
				Object obj = results.iterator().next();
				logger.debug("ManageSampleService.saveSample(): return object type = "
									+ obj.getClass().getName() + " | object value = " + obj);
				  // Yes, throws exception
				if (((Integer)obj).intValue() > 0)
				{
					throw new Exception("The the same sample is already exist in the system.");
				}
			}
			// No, save it

			// Create sample object
			Sample doSample = new Sample();

			// Container list
			Collection<SampleContainer> sampleContainers = new HashSet<SampleContainer>();
			for (int i=0; i<containers.length;i++)
			{
				SampleContainer doSampleContainer = new SampleContainer();
				// use Hibernate Hilo algorithm to generate the id
				doSampleContainer.setComments(containers[i].getContainerComments());
				doSampleContainer.setConcentration(StringUtils.convertToFloat(containers[i].getConcentration()));
				doSampleContainer.setConcentrationUnit(containers[i].getConcentrationUnit());
				doSampleContainer.setContainerType(containers[i].getContainerType());
				// Container is created by the same person who creates sample
				doSampleContainer.setCreatedBy(sample.getSampleSubmitter());
				doSampleContainer.setCreatedDate(StringUtils.convertToDate(sample.getAccessionDate(), "mm/dd/yyyy"));
				doSampleContainer.setDiluentsSolvent(containers[i].getSolvent());
				doSampleContainer.setQuantity(StringUtils.convertToFloat(containers[i].getQuantity()));
				doSampleContainer.setQuantityUnit(containers[i].getQuantityUnit());
				doSampleContainer.setSafetyPrecaution(containers[i].getSafetyPrecaution());
				doSampleContainer.setStorageCondition(containers[i].getStorageCondition());
				doSampleContainer.setVolume(StringUtils.convertToFloat(containers[i].getVolume()));
				doSampleContainer.setVolumeUnit(containers[i].getVolumeUnit());
//				containers[i].get
				// TODO: relationship with storage need to be added too.

				sampleContainers.add(doSampleContainer);
			}
			doSample.setSampleContainerCollection(sampleContainers);
			doSample.setComments(sample.getGeneralComments());
			doSample.setCreatedBy(sample.getSampleSubmitter());
			doSample.setCreatedDate(StringUtils.convertToDate(sample.getAccessionDate(),"mm/dd/yyyy"));
			doSample.setDescription(sample.getSampleDescription());
			doSample.setLotDescription(sample.getLotDescription());
			doSample.setLotId(sample.getLotId());
			doSample.setName(sample.getSampleId());
			// TODO: ReceivedBy and Date are not in the wireframe.
			doSample.setReceivedBy("");
			doSample.setReceivedDate(null);
			// TODO: need to parse the sampleID to get the sequence id
			doSample.setSampleSequenceId(getSampleSequenceId());
			// TODO: 
			doSample.setSampleSOP(null);
			doSample.setSolubility(sample.getSolubility());
			doSample.setSourceSampleId(sample.getSourceSampleId());
			// Front end source is a plain text, so just save the source object
			Source source = new Source();
			source.setOrganizationName(sample.getSampleSource());
			doSample.setSource(source);
			
//			ida.createObject(doSample);
			ida.store(doSample);
			ida.close();
		}catch (Exception e){
			
			throw new Exception (e.getMessage());
		}



  }}
