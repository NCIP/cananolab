package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.service.BaseService;

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
			BaseService service = (BaseService) context.getJobDetail()
					.getJobDataMap().get("csmCleanupService");
			cleanUpCSM(service);
		} catch (Exception e) {
			logger.info("Error cleaning up CSM records after deletion.");
		}
	}

	public void cleanUpCSM(BaseService service) throws Exception {
		Set<String> entries = Collections.synchronizedSet(new HashSet<String>(
				secureObjectsToRemove));
		if (entries.size() > 0) {
			logger.info("Deleting " + entries.size() + " CSM entries..");
		}
		for (String securedData : secureObjectsToRemove) {
			service.removeAllAccesses(securedData);
			entries.remove(securedData);
		}
		secureObjectsToRemove = entries;
	}

	public Set<String> getAllSecureObjectsToRemove() {
		return secureObjectsToRemove;
	}
}
