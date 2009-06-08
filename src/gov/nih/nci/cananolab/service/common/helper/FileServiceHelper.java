package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Utility service for file retrieving and writing.
 *
 * @author pansu, tanq
 *
 */
public class FileServiceHelper {
	Logger logger = Logger.getLogger(FileServiceHelper.class);

	public FileServiceHelper() {
	}

	/**
	 * Load the file for the given fileId from the database
	 *
	 * @param fileId
	 * @return
	 */
	public File findFileById(String fileId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(File.class).add(
				Property.forName("id").eq(new Long(fileId)));
		crit.setFetchMode("keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		File file = null;
		if (!result.isEmpty()) {
			file = (File) result.get(0);
		}
		return file;
	}

	public List<Keyword> findKeywordsByFileId(String FileId) throws Exception {
		List<Keyword> keywords = new ArrayList<Keyword>();

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select File.keywordCollection from gov.nih.nci.cananolab.domain.common.File File where File.id = "
						+ FileId);
		List results = appService.query(crit);
		for (Object obj : results) {
			Keyword keyword = (Keyword) obj;
			keywords.add(keyword);
		}
		return keywords;
	}
}
