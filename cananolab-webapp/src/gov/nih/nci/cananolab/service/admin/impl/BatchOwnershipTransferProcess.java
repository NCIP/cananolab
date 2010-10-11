package gov.nih.nci.cananolab.service.admin.impl;

import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.admin.OwnershipTransferService;
import gov.nih.nci.cananolab.service.common.LongRunningProcess;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class handles batch ownership transfer in a thread
 *
 * @author pansu
 *
 */
public class BatchOwnershipTransferProcess extends LongRunningProcess {
	private OwnershipTransferService transferService;
	private BaseService dataService;
	private Logger logger = Logger
			.getLogger(BatchOwnershipTransferProcess.class);
	private List<String> dataIds = null; // data Ids that need to run batch
	private String dataType;
	private String currentOwner, newOwner;

	/**
	 * Find the sampleIds to process
	 *
	 * @param dataAvailabilityService
	 * @param securityService
	 * @param batchOption
	 * @param user
	 * @throws Exception
	 */
	public BatchOwnershipTransferProcess(
			OwnershipTransferService transferService, BaseService dataService,
			String dataType, List<String> dataIds, String currentOwner,
			String newOwner) throws Exception {
		logger.info("New transfer ownership batch process is created");
		this.transferService = transferService;
		this.dataService = dataService;
		this.dataType = dataType;
		this.dataIds = dataIds;
		this.currentOwner = currentOwner;
		this.newOwner = newOwner;
		this.processType = ClassUtils
				.getDisplayName("OwnershipTransferService");
	}

	/**
	 * Triggers the long running process.
	 */
	public void run() {
		String message = "transfering ownership for data type: " + dataType;
		this.statusMessage = "Running " + message;
		logger.info(statusMessage);
		try {
			this.running = true;
			this.complete = false;
			long start = System.currentTimeMillis();
			// generate data availability for sampleIds
			int numFailures = 0;
			transferService.transferOwner(dataService, dataIds, currentOwner,
					newOwner);
			long end = System.currentTimeMillis();
			long duration = (end - start) / 1000;
			int numSuccesses = dataIds.size() - numFailures;
			this.statusMessage = "Completed " + message + "\" for "
					+ numSuccesses + " out of " + dataIds.size() + " after "
					+ duration + " seconds.";
			logger.info(statusMessage);
			this.complete = true;
			this.running = false;
		} catch (Exception e) {
			this.statusMessage = message + "is terminated with error";
			this.complete = true;
			this.running = false;
			this.withError = true;
			logger.error(e);
			return;
		}
	}
}
