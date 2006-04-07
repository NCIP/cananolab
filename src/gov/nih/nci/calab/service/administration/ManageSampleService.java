package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.SampleSOP;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/* CVS $Id: ManageSampleService.java,v 1.12 2006-04-07 13:26:40 zengje Exp $ 
 */
public class ManageSampleService {
	private static Logger logger = Logger.getLogger(ManageSampleService.class);

	public List<String> getAllSampleSOPs() {
		List<String> sampleSOPs = new ArrayList();
		try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sampleSOP.name from SampleSOP sampleSOP where sampleSOP.description='sample creation'";
			List results = ida.query(hqlString, SampleSOP.class.getName());
			for (Object obj:results) {
				sampleSOPs.add((String)obj);
			}
			ida.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Problem to retrieve all Sample SOPs. ");
		}
		return sampleSOPs;
	}

	/**
	 * 
	 * @return auto-generated default value for sample ID prefix
	 */
	public String getDefaultSampleIdPrefix() throws Exception {
		// TODO: Get configurable sample format string
		String sampleIdPrefix = "NCL-";
		long seqId = 0;
		try {
			IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select max(sample.sampleSequenceId) from Sample sample";
			List results = ida.query(hqlString, Sample.class.getName());
			System.out.println("ManageSampleService.getSampleSequenceId(): results.size = " + results.size());
			if (results.iterator().hasNext()) {
				Object obj = results.iterator().next();
				System.out.println("ManageSampleService.getSampleSequenceId(): obj = " + obj);
				seqId = (Long)obj;
			}
			System.out.println("ManageSampleService.getSampleSequenceId(): current seq id = " + seqId);
			ida.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sampleIdPrefix + (seqId+1);
	}

	public Long getUserDefinedSequenceId(String userDefinedPrefix) {
		String newSequence = userDefinedPrefix.substring(userDefinedPrefix.lastIndexOf("-")+1);
		
		return StringUtils.convertToLong(newSequence);
		
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
	      IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
    	  try {
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
			
			// Front end source is a plain text, so just save the source object
			if (sample.getSampleSource() != null) {
				Source source = new Source();
				source.setOrganizationName(sample.getSampleSource());
				ida.store(source);			
				// Create releationship between this source and this sample
				doSample.setSource(source);
			}
			
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
			doSample.setSampleSequenceId((long)9);
			// TODO: Fill in the sample SOP info
			doSample.setSampleSOP(null);
			doSample.setSolubility(sample.getSolubility());
			doSample.setSourceSampleId(sample.getSourceSampleId());
			
			//Save Sample
//			ida.createObject(doSample);
			ida.store(doSample);

			// Container list
//			Collection<SampleContainer> sampleContainers = new HashSet<SampleContainer>();
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
				
				
				// TODO: relationship with storage need to be added too.
//				HashSet<StorageElement> storages = new HashSet<StorageElement>();
//				
//				StorageElement box = new StorageElement();
//				box.setLocation(containers[i].getStorageLocation().getBox());
//				box.setType(CalabConstants.STORAGE_BOX);
//				ida.store(box);
//				storages.add(box);
//				
//				StorageElement shelf = new StorageElement();
//				shelf.setLocation(containers[i].getStorageLocation().getShelf());
//				shelf.setType(CalabConstants.STORAGE_SHELF);
//				ida.store(shelf);
//				storages.add(shelf);
//				
//				StorageElement rack = new StorageElement();
//				rack.setLocation(containers[i].getStorageLocation().getRack());
//				rack.setType(CalabConstants.STORAGE_RACK);
//				ida.store(rack);
//				storages.add(rack);
//				
//				StorageElement freezer = new StorageElement();
//				freezer.setLocation(containers[i].getStorageLocation().getFreezer());
//				freezer.setType(CalabConstants.STORAGE_FREEZER);
//				ida.store(freezer);
//				storages.add(freezer);
//				
//				StorageElement room = new StorageElement();
//				room.setLocation(containers[i].getStorageLocation().getRoom());
//				room.setType(CalabConstants.STORAGE_ROOM);
//				ida.store(room);
//				storages.add(room);
//				
//				doSampleContainer.setStorageElementCollection(storages);
//				
				doSampleContainer.setSample(doSample);
				ida.store(doSampleContainer);
//				sampleContainers.add(doSampleContainer);
			}
//			doSample.setSampleContainerCollection(sampleContainers);
			
			
			
		}catch (Exception e){
			e.printStackTrace();
			
			ida.rollback();
			ida.close();
			throw new Exception (e.getMessage());
		}
		ida.close();
	  }
}
