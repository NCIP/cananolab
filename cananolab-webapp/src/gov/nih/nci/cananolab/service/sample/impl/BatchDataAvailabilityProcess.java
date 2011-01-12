package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class handles batch data availability generation in a thread
 *
 * @author pansu
 *
 */
public class BatchDataAvailabilityProcess extends LongRunningProcess {
	public static final String BATCH_OPTION1 = "generate all";
	public static final String BATCH_OPTION2 = "regenerate old";
	public static final String BATCH_OPTION3 = "delete all";

	private DataAvailabilityService dataAvailabilityService;
	private SecurityService securityService;
	private String batchOption;
	private Logger logger = Logger
			.getLogger(BatchDataAvailabilityProcess.class);
	private List<String> sampleIds = null; // sample Ids that need to run batch

	/**
	 * Find the sampleIds to process
	 *
	 * @param dataAvailabilityService
	 * @param securityService
	 * @param batchOption
	 * @param user
	 * @throws Exception
	 */
	public BatchDataAvailabilityProcess(
			DataAvailabilityService dataAvailabilityService,
			SecurityService securityService, String batchOption, UserBean user)
			throws Exception {
		this.dataAvailabilityService = dataAvailabilityService;
		this.securityService = securityService;
		this.batchOption = batchOption;
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		logger.info("New data availability batch process is created");
		// find all sampleIds
		List<String> allSampleIds = sampleService.findSampleIdsBy("", "", null,
				null, null, null, null, null, null, null, null);
		// find sampleIds with existing data availability
		List<String> dataAvailabilitySampleIds = dataAvailabilityService
				.findSampleIdsWithDataAvailability(securityService);
		if (batchOption.equals(BATCH_OPTION1)
				|| batchOption.equals(BATCH_OPTION3)) {
			sampleIds = allSampleIds;
		} else {
			sampleIds = dataAvailabilitySampleIds;
		}
		this.processType = ClassUtils
				.getDisplayName("BatchDataAvailabilityProcess");
	}

	/**
	 * Pass in the sample Ids that needs to run batch data availability process
	 *
	 * @param dataAvailabilityService
	 * @param sampleIds
	 * @param batchOption
	 * @param user
	 * @throws Exception
	 */
	public BatchDataAvailabilityProcess(
			DataAvailabilityService dataAvailabilityService,
			SecurityService securityService, List<String> sampleIds,
			String batchOption, UserBean user) throws Exception {
		this.dataAvailabilityService = dataAvailabilityService;
		this.securityService = securityService;
		this.batchOption = batchOption;
		this.sampleIds = sampleIds;
		securityService = new SecurityService(AccessibilityBean.CSM_APP_NAME,
				user);
		this.processType = ClassUtils
				.getDisplayName("BatchDataAvailabilityProcess");
		logger.info("New data availability batch process is created");
	}

	/**
	 * Triggers the long running process.
	 */
	public void run() {
		String optionMessage = "";
		if (batchOption.equals(BatchDataAvailabilityProcess.BATCH_OPTION1)) {
			optionMessage = "Generate data availability for all samples";
		} else if (batchOption
				.equals(BatchDataAvailabilityProcess.BATCH_OPTION2)) {
			optionMessage = "Regenerate data availability for samples with existing data availability";
		} else if (batchOption
				.equals(BatchDataAvailabilityProcess.BATCH_OPTION3)) {
			optionMessage = "Delete existing data availability";
		}
		this.statusMessage = "Running the option \"" + optionMessage + ".\"";
		logger.info("Running batch data availability process with option "
				+ batchOption);
		try {
			this.running = true;
			this.complete = false;
			long start = System.currentTimeMillis();
			// generate data availability for sampleIds
			int numFailures = 0;
			if (batchOption.equalsIgnoreCase(BATCH_OPTION1)
					|| batchOption.equalsIgnoreCase(BATCH_OPTION2)) {
				numFailures = dataAvailabilityService
						.saveBatchDataAvailability(sampleIds, securityService);
			} else if (batchOption.equalsIgnoreCase(BATCH_OPTION3)) {
				numFailures = dataAvailabilityService
						.deleteBatchDataAvailability(sampleIds, securityService);
			}
			long end = System.currentTimeMillis();
			long duration = (end - start) / 1000;
			int numSuccesses = sampleIds.size() - numFailures;

			this.statusMessage = "Completed the option \"" + optionMessage + "\" for "+ numSuccesses
					+ " out of " + sampleIds.size() + " samples after "+duration+" seconds.";
			logger.info("Data availability batch process with option "
					+ batchOption + " is completed after " + duration
					+ " second(s)");
			this.complete = true;
			this.running = false;
		} catch (Exception e) {
			this.statusMessage = "Data availability batch process with option "
					+ batchOption + "is terminated with error";
			this.complete = true;
			this.running = false;
			this.withError = true;
			logger.error(e);
			return;
		}
	}
}
