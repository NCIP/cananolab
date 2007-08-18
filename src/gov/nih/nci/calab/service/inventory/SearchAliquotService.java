package gov.nih.nci.calab.service.inventory;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
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
/* CVS $Id: SearchAliquotService.java,v 1.2 2007-08-18 02:05:10 pansu Exp $ */

public class SearchAliquotService {
	private static Logger logger = Logger.getLogger(SearchAliquotService.class);

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
			String sampleSubmitter, StorageLocation storageLocation)
			throws Exception {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();

			String where = "";
			String storageFrom = "";
			if (aliquotName.length() > 0) {
				where = "where ";
				if (aliquotName.indexOf("*") != -1) {
					aliquotName = aliquotName.replace('*', '%');
					whereList.add("aliquot.name like ?");
				} else {
					whereList.add("aliquot.name=?");
				}
				paramList.add(aliquotName);
			}
			if (sampleType.length() > 0) {
				paramList.add(sampleType);
				where = "where ";
				whereList.add("aliquot.sample.type=?");
			}

			if (sampleSource.length() > 0) {
				paramList.add(sampleSource);
				where = "where ";
				whereList.add("aliquot.sample.source.organizationName=?");
			}

			if (sourceSampleId.length() > 0) {
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
			if (sampleSubmitter.length() > 0) {
				paramList.add(sampleSubmitter);
				where = "where ";
				whereList.add("aliquot.createdBy=?");
			}

			if (storageLocation.getRoom().length() > 0) {
				paramList.add(storageLocation.getRoom());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Room' and storage.location=?");
			}

			if (storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Freezer' and storage.location=?");
			}

			if (storageLocation.getFreezer().length() > 0) {
				paramList.add(storageLocation.getFreezer());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add(" storage.type='Shelf' and storage.location=?");
			}

			if (storageLocation.getBox().length() > 0) {
				paramList.add(storageLocation.getBox());
				where = "where ";
				storageFrom = "join aliquot.storageElementCollection storage ";
				whereList.add("storage.type='Box' and storage.location=?");
			}

			String whereStr = StringUtils.join(whereList, " and ");
			String hqlString = "select aliquot from Aliquot aliquot "
					+ storageFrom + where + whereStr;		
			HibernateUtil.beginTransaction();

			List<? extends Object> results = (List<? extends Object>) (HibernateUtil
					.createQueryByParam(hqlString, paramList).list());
			for (Object obj : new HashSet<Object>(results)) {
				Aliquot aliquot = (Aliquot) obj;
				aliquots.add(new AliquotBean(aliquot));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Error in searching aliquots by the given parameters",
					e);
			throw new RuntimeException(
					"Error in searching aliquots by the given parameters");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(aliquots,
				new CaNanoLabComparators.AliquotBeanComparator());
		return aliquots;
	}

	public List<AliquotBean> searchAliquotsByContainer(String containerId)
			throws Exception {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select aliquot from Aliquot aliquot join aliquot.parentSampleContainerCollection container where container.id="
					+ containerId;
			List results = session.createQuery(hqlString).list();
			for (Object obj : results) {
				Aliquot aliquot = (Aliquot) obj;
				aliquots.add(new AliquotBean(aliquot));
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error(
					"Error in searching aliquots by the given container ID", e);
			throw new RuntimeException(
					"Error in searching aliquots by the given container ID");
		} finally {
			HibernateUtil.closeSession();
		}

		Collections.sort(aliquots,
				new CaNanoLabComparators.AliquotBeanComparator());
		return aliquots;
	}
}
