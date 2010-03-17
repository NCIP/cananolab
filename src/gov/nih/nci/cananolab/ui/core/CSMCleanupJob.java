package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.Constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Remove CSM entries for the objects that are deleted
 * 
 * @author pansu
 * 
 */
public class CSMCleanupJob implements Job {
	private static Logger logger = Logger.getLogger(CSMCleanupJob.class
			.getName());
	private static Set<String> secureObjectsToRemove = Collections
			.synchronizedSet(new HashSet<String>());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			cleanUpCSM();
		} catch (Exception e) {
			logger.info("Error cleaning up CSM records after deletion.");
		}
	}

	public void cleanUpCSM() throws Exception {
		AuthorizationService service = new AuthorizationService(
				Constants.CSM_APP_NAME);
		Set<String> entries = Collections.synchronizedSet(new HashSet<String>(
				secureObjectsToRemove));
		for (String securedData : secureObjectsToRemove) {
			service.removeCSMEntry(securedData);
			entries.remove(securedData);
		}
		secureObjectsToRemove = entries;
	}

	public Set<String> getAllSecureObjectsToRemove() {
		return secureObjectsToRemove;
	}
}
