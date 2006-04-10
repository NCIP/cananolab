package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.SampleSOP;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/* CVS $Id: ManageSampleService.java,v 1.15 2006-04-10 12:46:49 zengje Exp $ 
 */
public class ManageSampleService {
	private static Logger logger = Logger.getLogger(ManageSampleService.class);

	public List<String> getAllSampleSOPs() {
		List<String> sampleSOPs = new ArrayList<String>();
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
			logger.debug("ManageSampleService.getSampleSequenceId(): results.size = " + results.size());
			if (results.iterator().hasNext()) {
				Object obj = results.iterator().next();
				logger.debug("ManageSampleService.getSampleSequenceId(): obj = " + obj);
				seqId = (Long)obj;
			}
			logger.debug("ManageSampleService.getSampleSequenceId(): current seq id = " + seqId);
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
		if (lotId.equals("N/A")) {
			return sampleIdPrefix;
		}
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
			// Create Sample Object, save source, save sample, save storageElement, save SampleContainer, 
			// set storageElement in sampleContainer, save sampleContainer again
			
			
			// Create sample object
			Sample doSample = new Sample();
			
			// Front end source is a plain text, so just save the source object
			if (sample.getSampleSource() != null) {
				List existedSources = ida.search("from Source source where source.organizationName = '" + sample.getSampleSource() + "'");
				
				Source source = null;
				if (existedSources.size() > 0){
					source = (Source)existedSources.get(0);
				} else {
					source = new Source();
					source.setOrganizationName(sample.getSampleSource());
					ida.store(source);
				}
				// Create releationship between this source and this sample
				doSample.setSource(source);
			}
			
			doSample.setComments(sample.getGeneralComments());
			doSample.setCreatedBy(sample.getSampleSubmitter());
			doSample.setCreatedDate(StringUtils.convertToDate(sample.getAccessionDate(),CalabConstants.DATE_FORMAT));
			doSample.setDescription(sample.getSampleDescription());
			doSample.setLotDescription(sample.getLotDescription());
			doSample.setLotId(sample.getLotId());
			doSample.setName(sample.getSampleId());
			// TODO: ReceivedBy and Date are not in the wireframe.
			doSample.setReceivedBy("");
			doSample.setReceivedDate(null);
			// TODO: need to parse the sampleID to get the sequence id
			doSample.setSampleSequenceId(getUserDefinedSequenceId(sample.getSampleIdPrefix()));
			doSample.setSolubility(sample.getSolubility());
			doSample.setSourceSampleId(sample.getSourceSampleId());
			// TODO: Fill in the sample SOP info, if sampleBean can pass the primary key......
//			doSample.setSampleSOP(ida.load(SampleSop.class, sample.getSampleSOP().));
			String sopName = sample.getSampleSOP();
			if ( sopName != null)
			{
				List existedSOP = ida.search("from SampleSOP sop where sop.name = '" + sopName + "'");					
				SampleSOP  sop = (SampleSOP)existedSOP.get(0);
				doSample.setSampleSOP(sop);	
			}

			//Save Sample
			ida.createObject(doSample);

			// Container list
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
				doSampleContainer.setCreatedDate(StringUtils.convertToDate(sample.getAccessionDate(), CalabConstants.DATE_FORMAT));
				doSampleContainer.setDiluentsSolvent(containers[i].getSolvent());
				doSampleContainer.setQuantity(StringUtils.convertToFloat(containers[i].getQuantity()));
				doSampleContainer.setQuantityUnit(containers[i].getQuantityUnit());
				doSampleContainer.setSafetyPrecaution(containers[i].getSafetyPrecaution());
				doSampleContainer.setStorageCondition(containers[i].getStorageCondition());
				doSampleContainer.setVolume(StringUtils.convertToFloat(containers[i].getVolume()));
				doSampleContainer.setVolumeUnit(containers[i].getVolumeUnit());
				
				// TODO: relationship with storage need to be added too.
				HashSet<StorageElement> storages = new HashSet<StorageElement>();
				
				String boxValue = containers[i].getStorageLocation().getBox();
				if ( boxValue != null)
				{
					List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_BOX + 
													 "' and se.location = '" + boxValue + "'");					
					StorageElement box = null;
					if (existedSE.size() > 0){
						box = (StorageElement)existedSE.get(0);
					} else {
						box = new StorageElement();
						box.setLocation(boxValue);
						box.setType(CalabConstants.STORAGE_BOX);
						ida.store(box);
					}
					// Create releationship between this source and this sample
					storages.add(box);			
				}
				
				String shelfValue = containers[i].getStorageLocation().getShelf();
				if ( shelfValue != null)
				{
					List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_SHELF + 
													 "' and se.location = '" + shelfValue + "'");					
					StorageElement shelf = null;
					if (existedSE.size() > 0){
						shelf = (StorageElement)existedSE.get(0);
					} else {
						shelf = new StorageElement();
						shelf.setLocation(shelfValue);
						shelf.setType(CalabConstants.STORAGE_SHELF);
						ida.store(shelf);
					}
					// Create releationship between this source and this sample
					storages.add(shelf);			
				}
				
				String freezerValue = containers[i].getStorageLocation().getFreezer();
				if ( freezerValue != null)
				{
					List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_FREEZER + 
													 "' and se.location = '" + freezerValue + "'");					
					StorageElement freezer = null;
					if (existedSE.size() > 0){
						freezer = (StorageElement)existedSE.get(0);
					} else {
						freezer = new StorageElement();
						freezer.setLocation(freezerValue);
						freezer.setType(CalabConstants.STORAGE_FREEZER);
						ida.store(freezer);
					}
					// Create releationship between this source and this sample
					storages.add(freezer);			
				}

				String roomValue = containers[i].getStorageLocation().getRoom();
				if ( roomValue != null)
				{
					List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_ROOM + 
													 "' and se.location = '" + roomValue + "'");					
					StorageElement room = null;
					if (existedSE.size() > 0){
						room = (StorageElement)existedSE.get(0);
					} else {
						room = new StorageElement();
						room.setLocation(roomValue);
						room.setType(CalabConstants.STORAGE_ROOM);
						ida.store(room);
					}
					// Create releationship between this source and this sample
					storages.add(room);			
				}
							
				doSampleContainer.setSample(doSample);
				ida.store(doSampleContainer);
				
				System.out.println("ManageSampleService.saveSample(): same again with storage info");
				doSampleContainer.setStorageElementCollection(storages);
				ida.store(doSampleContainer);
			}
		}catch (Exception e){
			e.printStackTrace();
			
			ida.rollback();
			ida.close();
			throw new Exception (e.getMessage());
		}
		ida.close();
	  }

	
//	  private StorageElement retrieveContainerStorages(IDataAccess ida, StorageLocation location, String locationType)
//	  {
//			String boxValue = location.getBox();
//			if ( boxValue != null)
//			{
//				List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_BOX + 
//												 "' and se.location = '" + boxValue + "'");					
//				StorageElement box = null;
//				if (existedSE.size() > 0){
//					box = (StorageElement)existedSE.get(0);
//				} else {
//					box = new StorageElement();
//					box.setLocation(boxValue);
//					box.setType(CalabConstants.STORAGE_BOX);
//					ida.store(box);
//				}
//				// Create releationship between this source and this sample
//				storages.add(box);			
//			}
//
//		  return null;
//	  }
}
