package gov.nih.nci.cananolab.service.common.helper;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.LabFile;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
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
	public LabFile findFile(String fileId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(LabFile.class).add(
				Property.forName("id").eq(new Long(fileId)));
		List result = appService.query(crit);
		LabFile file = null;
		if (!result.isEmpty()) {
			file = (LabFile) result.get(0);
		}
		return file;
	}

	public Collection<LabFile> findFilesByCompositionInfoId(String id,
			String className) {
		Collection<LabFile> files = new ArrayList<LabFile>();
		//TODO fill in HQL
		return files;
	}

	public Collection<Keyword> findKeywordsByFileId(String labFileId)
			throws ParticleCharacterizationException {
		Collection<Keyword> keywords = new ArrayList<Keyword>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select labFile.keywordCollection from gov.nih.nci.cananolab.domain.common.LabFile labFile where labFile.id = "
							+ labFileId);
			List results = appService.query(crit);
			for (Object obj : results) {
				Keyword keyword = (Keyword) obj;
				keywords.add(keyword);
			}
		} catch (Exception e) {
			logger.error("Problem to retrieve LabFile keyword.", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve retrieve LabFile keyword ");
		}
		return keywords;
	}

}
