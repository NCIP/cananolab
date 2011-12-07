package gov.nih.nci.cananolab.service;

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
			BaseServiceLocalImpl service = new BaseServiceLocalImpl();
			cleanUpCSM(service);
		} catch (Exception e) {
			logger.info("Error cleaning up CSM records after deletion.");
		}
	}

	public void cleanUpCSM(BaseServiceLocalImpl service) throws Exception {
		Set<String> entries = Collections.synchronizedSet(new HashSet<String>(
				secureObjectsToRemove));
		if (entries.size() > 0) {
			logger.info("Deleting " + entries.size() + " CSM entries..");
		}
		for (String securedData : secureObjectsToRemove) {
			service.accessUtils.removeCSMEntries(securedData);
			entries.remove(securedData);
		}
		secureObjectsToRemove = entries;
	}

	public Set<String> getAllSecureObjectsToRemove() {
		return secureObjectsToRemove;
	}
}
