package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class handles batch data availability generation in a thread
 *
 * @author pansu
 *
 */
public class BatchDataAvailabilityProcess implements Runnable {
	public static final String BATCH_OPTION1 = "generate all";
	public static final String BATCH_OPTION2 = "regenerate old";
	public static final String BATCH_OPTION3 = "delete all";

	private DataAvailabilityService dataAvailabilityService;
	private String batchOption;
	private SecurityService securityService;
	private SampleService sampleService;

	private Logger logger = Logger
			.getLogger(BatchDataAvailabilityProcess.class);
	private boolean complete = false;
	private boolean running = false;
	private String statusMessage = "Data availability batch process has not been started";
	private boolean withError = false;

	public BatchDataAvailabilityProcess(
			DataAvailabilityService dataAvailabilityService,
			String batchOption, UserBean user) throws Exception {
		this.dataAvailabilityService = dataAvailabilityService;
		this.batchOption = batchOption;
		securityService = new SecurityService(AccessibilityBean.CSM_APP_NAME,
				user);
		sampleService = new SampleServiceLocalImpl(securityService);
		logger.info("New data availability batch process is created");
	}

	/**
	 * Hides threading details from the web app.
	 */
	public void process() {
		if (!isRunning()) {
			this.statusMessage = "Starting data availability batch process with option "
					+ batchOption;
			try {
				Thread t = new Thread(this);
				// Daemon threads are used for background supporting tasks and
				// are only needed while normal threads are executing. If normal
				// threads are not running and remaining threads are daemon
				// threads then the JVM exits.
				t.setDaemon(true);
				t.start();

				//
				// don't return until the new thread is running.
				//
				while (false == this.isRunning()) {
					logger.info("Trying to run the new thread");
				}
			} catch (Exception e) {
				this.statusMessage = "Data availability batch process with option "
						+ batchOption + " is terminated with error";
				this.withError = true;
				e.printStackTrace();
				logger.error(this.statusMessage, e);
			}
		}
	}

	/**
	 * Triggers the long running process.
	 */
	public void run() {
		this.statusMessage = "Running batch data availability process with option "
				+ batchOption;
		logger.info(this.statusMessage);
		try {
			this.running = true;
			this.complete = false;
			// find all sampleIds
			List<String> sampleIds = sampleService.findSampleIdsBy("", "",
					null, null, null, null, null, null, null, null, null);

			long start = System.currentTimeMillis();
			// generate data availability for all sampleIds
			if (batchOption.equalsIgnoreCase(BATCH_OPTION1)) {
				dataAvailabilityService.saveBatchDataAvailability(sampleIds,
						securityService);
			}
			// regenerate data availability for sampleIds with existing ones
			else if (batchOption.equalsIgnoreCase(BATCH_OPTION2)) {
				// find sampleIds with existing data availability
				List<String> dataAvailabilitySampleIds = dataAvailabilityService
						.findSampleIdsWithDataAvailability(securityService);
				dataAvailabilityService.saveBatchDataAvailability(
						dataAvailabilitySampleIds, securityService);
			} else if (batchOption.equalsIgnoreCase(BATCH_OPTION3)) {
				dataAvailabilityService.deleteBatchDataAvailability(sampleIds,
						securityService);
			}
			long end = System.currentTimeMillis();
			long duration = (end - start) / 1000;
			logger.info("total time to run option " + batchOption + ": "
					+ duration + " seconds.");
			this.statusMessage = "Data availability batch process with option "
					+ batchOption + " is completed after " + duration
					+ " seccond(s)";
			this.complete = true;
			this.running = false;
		} catch (Exception e) {
			this.statusMessage = "Data availability batch process with option "
					+ batchOption + "is terminated with error";
			this.complete = true;
			this.running = false;
			this.withError = true;
			e.printStackTrace();
			logger.error(e);
			return;
		}
	}

	public boolean isComplete() {
		return complete;
	}

	public boolean isRunning() {
		return running;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public boolean isWithError() {
		return withError;
	}
}
