package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.administration.StorageLocation;
import gov.nih.nci.calab.service.util.CalabComparators;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author pansu
 * 
 */
/* CVS $Id: SearchSampleService.java,v 1.16 2006-04-27 18:58:08 pansu Exp $ */

public class SearchSampleService {
	private static Logger logger = Logger.getLogger(SearchSampleService.class);

	/**
	 * 
	 * @return all sample sources
	 */
	public List<String> getAllSampleSources() throws Exception {
		List<String> sampleSources = new ArrayList<String>();
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select source.organizationName from Source source order by source.organizationName";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				sampleSources.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all sample sources", e);
			throw new RuntimeException("Error in retrieving all sample sources");
		} finally {
			ida.close();
		}

		return sampleSources;
	}

	/**
	 * 
	 * @return all source sample IDs
	 */
	public List<String> getAllSourceSampleIds() throws Exception {
		List<String> sourceSampleIds = new ArrayList<String>();
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String hqlString = "select distinct sample.sourceSampleId from Sample sample order by sample.sourceSampleId";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				sourceSampleIds.add((String) obj);
			}
		} catch (Exception e) {
			logger.error("Error in retrieving all source sample IDs", e);
			throw new RuntimeException(
					"Error in retrieving all source sample IDs");
		} finally {
			ida.close();
		}

		return sourceSampleIds;
	}

	/**
	 * Search database for sample container information based on sample ID
	 * 
	 * @param sampleId
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return a list of SampleBean
	 */
	public List<SampleBean> searchSamplesBySampleName(String sampleName,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) throws Exception {
		List<SampleBean> samples = new ArrayList<SampleBean>();
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);

		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String storageFrom = "";

			if (sampleName.length()>0) {				
				where = "where ";
				if (sampleName.indexOf("*") != -1) {
					sampleName = sampleName.replace('*', '%');
					whereList.add("sample.name like ?");
				} else {
					whereList.add("sample.name=?");
				}
				paramList.add(sampleName);
			}
			if (sampleType.length()>0) {
				paramList.add(sampleType);
				where = "where ";
				whereList.add("sample.type=?");
			}

			if (sampleSource.length()>0) {
				paramList.add(sampleSource);
				where = "where ";
				whereList.add("sample.source.organizationName=?");
			}

			if (sourceSampleId.length()>0) {
				paramList.add(sourceSampleId);
				where = "where ";
				whereList.add("sample.sourceSampleId=?");
			}

			if (dateAccessionedBegin != null) {
				paramList.add(dateAccessionedBegin);
				where = "where ";
				whereList.add("sample.createdDate>=?");
			}

			if (dateAccessionedEnd != null) {
				paramList.add(dateAccessionedEnd);
				where = "where ";
				whereList.add("sample.createdDate<=?");
			}
			if (sampleSubmitter.length()>0) {
				paramList.add(sampleSubmitter);
				where = "where ";
				whereList.add("sample.createdBy=?");
			}

			if (storageLocation.getRoom().length()>0) {
				paramList.add(storageLocation.getRoom());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Room' and storage.location=?");
			}

			if (storageLocation.getFreezer().length()>0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Freezer' and storage.location=?");
			}

			if (storageLocation.getFreezer().length()>0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add(" storage.type='Shelf' and storage.location=?");
			}

			if (storageLocation.getBox().length()>0) {
				paramList.add(storageLocation.getBox());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Box' and storage.location=?");
			}

			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select sample from Sample sample "
					+ storageFrom + where + whereStr;

			ida.open();

			List<? extends Object> results = (List<? extends Object>)ida.searchByParam(hqlString, paramList);
			for (Object obj : new HashSet<Object>(results)) {
				Sample sample = (Sample) obj;
				samples.add(new SampleBean(sample));
			}
		} catch (Exception e) {
			logger
					.error("Error in searching sample by the given parameters",
							e);
			throw new RuntimeException(
					"Error in searching sample by the given parameters");
		} finally {
			ida.close();
		}
        
		Collections.sort(samples, new CalabComparators.SampleBeanComparator());
		return samples;
	}

	/**
	 * 
	 * @param aliquotName
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
public List<AliquotBean> searchAliquotsByAliquotName(String aliquotName,
			String sampleType, String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) throws Exception {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String storageFrom = "";
			if (aliquotName.length()>0) {								
				where = "where ";
				if (aliquotName.indexOf("*") != -1) {
					aliquotName = aliquotName.replace('*', '%');
					whereList.add("aliquot.name like ?");
				} else {
					whereList.add("aliquot.name=?");
				}
				paramList.add(aliquotName);
			}
			if (sampleType.length()>0) {
				paramList.add(sampleType);
				where = "where ";
				whereList.add("aliquot.sample.type=?");
			}

			if (sampleSource.length()>0) {
				paramList.add(sampleSource);
				where = "where ";
				whereList.add("aliquot.sample.source.organizationName=?");
			}

			if (sourceSampleId.length()>0) {
				paramList.add(sourceSampleId);
				where = "where ";
				whereList.add("aliquot.sample.sourceSampleId=?");
			}

			if (dateAccessionedBegin != null) {
				paramList.add(dateAccessionedBegin);
				where = "where ";
				whereList.add("aliquot.createdDate>=?");
			}

			if (dateAccessionedEnd != null) {
				paramList.add(dateAccessionedEnd);
				where = "where ";
				whereList.add("aliquot.createdDate<=?");
			}
			if (sampleSubmitter.length()>0) {
				paramList.add(sampleSubmitter);
				where = "where ";
				whereList.add("aliquot.createdBy=?");
			}

			if (storageLocation.getRoom().length()>0) {
				paramList.add(storageLocation.getRoom());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Room' and storage.location=?");
			}

			if (storageLocation.getFreezer().length()>0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Freezer' and storage.location=?");
			}

			if (storageLocation.getFreezer().length()>0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add(" storage.type='Shelf' and storage.location=?");
			}

			if (storageLocation.getBox().length()>0) {
				paramList.add(storageLocation.getBox());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Box' and storage.location=?");
			}

			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select aliquot from Aliquot aliquot "
					+ storageFrom + where + whereStr;

			ida.open();

			List<? extends Object> results = (List<? extends Object>)ida.searchByParam(hqlString, paramList);
			for (Object obj : new HashSet<Object>(results)) {
				Aliquot aliquot = (Aliquot) obj;
				aliquots.add(new AliquotBean(aliquot));				
			}
		} catch (Exception e) {
			logger
					.error("Error in searching aliquots by the given parameters",
							e);
			throw new RuntimeException(
					"Error in searching aliquots by the given parameters");
		} finally {
			ida.close();
		}

		Collections.sort(aliquots, new CalabComparators.AliquotBeanComparator());
		return aliquots;
	}
	/**
	 * Search database for sample container information based on other
	 * parameters
	 * 
	 * @param sampleType
	 * @param sampleSource
	 * @param sourceSampleId
	 * @param dateAccessionedBegin
	 * @param dateAccessionedEnd
	 * @param sampleSubmitter
	 * @param storageLocation
	 * @return
	 */
	public List<SampleBean> searchSamples(String sampleType,
			String sampleSource, String sourceSampleId,
			Date dateAccessionedBegin, Date dateAccessionedEnd,
			String sampleSubmitter, StorageLocation storageLocation) throws Exception {

		return searchSamplesBySampleName("", sampleType, sampleSource,
				sourceSampleId, dateAccessionedBegin, dateAccessionedEnd,
				sampleSubmitter, storageLocation);
	}
}
