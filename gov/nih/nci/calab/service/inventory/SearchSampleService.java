package gov.nih.nci.calab.service.inventory;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.dto.inventory.StorageLocation;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * 
 * @author pansu
 * 
 */
/* CVS $Id: SearchSampleService.java,v 1.2 2007-08-18 02:05:10 pansu Exp $ */

public class SearchSampleService {
	private static Logger logger = Logger.getLogger(SearchSampleService.class);

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
			String sampleSubmitter, StorageLocation storageLocation)
			throws Exception {
		List<SampleBean> samples = new ArrayList<SampleBean>();

		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String storageFrom = "";

			if (sampleName != null && sampleName.length() > 0) {
				where = "where ";
				if (sampleName.indexOf("*") != -1) {
					sampleName = sampleName.replace('*', '%');
					whereList.add("sample.name like ?");
				} else {
					whereList.add("sample.name=?");
				}
				paramList.add(sampleName);
			}
			if (sampleType != null && sampleType.length() > 0) {
				paramList.add(sampleType);
				where = "where ";
				whereList.add("sample.type=?");
			}

			if (sampleSource != null && sampleSource.length() > 0) {
				paramList.add(sampleSource);
				where = "where ";
				whereList.add("sample.source.organizationName=?");
			}

			if (sourceSampleId != null && sourceSampleId.length() > 0) {
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
			if (sampleSubmitter != null && sampleSubmitter.length() > 0) {
				paramList.add(sampleSubmitter);
				where = "where ";
				whereList.add("sample.createdBy=?");
			}

			if (storageLocation != null
					&& storageLocation.getRoom().length() > 0) {
				paramList.add(storageLocation.getRoom());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Room' and storage.location=?");
			}

			if (storageLocation != null
					&& storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Freezer' and storage.location=?");
			}

			if (storageLocation != null
					&& storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add(" storage.type='Shelf' and storage.location=?");
			}

			if (storageLocation != null
					&& storageLocation.getBox().length() > 0) {
				paramList.add(storageLocation.getBox());
				where = "where ";
				storageFrom = "join sample.sampleContainerCollection container join container.storageElementCollection storage ";
				whereList.add("storage.type='Box' and storage.location=?");
			}

			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select sample from Sample sample "
					+ storageFrom + where + whereStr;

			HibernateUtil.beginTransaction();

			List<? extends Object> results = (List<? extends Object>) (HibernateUtil
					.createQueryByParam(hqlString, paramList).list());
			for (Object obj : new HashSet<Object>(results)) {
				Sample sample = (Sample) obj;
				samples.add(new SampleBean(sample));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger
					.error("Error in searching sample by the given parameters",
							e);
			throw new RuntimeException(
					"Error in searching sample by the given parameters");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(samples,
				new CaNanoLabComparators.SampleBeanComparator());
		return samples;
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
			String sampleSubmitter, StorageLocation storageLocation)
			throws Exception {

		return searchSamplesBySampleName("", sampleType, sampleSource,
				sourceSampleId, dateAccessionedBegin, dateAccessionedEnd,
				sampleSubmitter, storageLocation);
	}

	public Sample searchSampleBy(String charId) throws Exception {
		Sample sample = null;
		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session.createQuery(
					" from Nanoparticle nano left join fetch nano.characterizationCollection chara"
							+ " where chara.id=" + charId).list();
			for (Object obj : results) {
				sample = (Sample) obj;
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization");
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

		return sample;
	}

	public List<LabFile> searchLabFilesBy(String charId) throws Exception {
		Sample sample = this.searchSampleBy(charId);

		List<LabFile> labFiles = new ArrayList<LabFile>();

		if (sample != null) {
			LabFile labFile;
			try {

				Session session = HibernateUtil.currentSession();
				HibernateUtil.beginTransaction();
				List results = session
						.createQuery(
								" from Sample sample left join fetch sample.sampleContainerCollection"
										+ " left join fetch sample.sampleContainerCollection.runSampleContainerCollection"
										+ " left join fetch sample.sampleContainerCollection.runSampleContainerCollection.run"
										+ " left join fetch sample.sampleContainerCollection.runSampleContainerCollection.run.outputFileCollection"
										+ " where sample.id=" + sample.getId())
						.list();
				for (Object obj : results) {
					labFile = (LabFile) obj;
					labFiles.add(labFile);
				}
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				logger.error("Problem finding characterization", e);
				throw e;
			} finally {
				HibernateUtil.closeSession();
			}
		}
		return labFiles;
	}
}
