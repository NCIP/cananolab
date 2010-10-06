package gov.nih.nci.cananolab.service.common;

import org.apache.log4j.Logger;

/**
 * This abstract class handles a long running process in a thread
 *
 * @author pansu
 *
 */
public class LongRunningProcess implements Runnable {
	private Logger logger = Logger.getLogger(LongRunningProcess.class);
	protected boolean complete = false;
	protected boolean running = false;
	protected String statusMessage = "Process has not been started";
	protected boolean withError = false;
	protected String processType;

	/**
	 * Hides threading details from the web app.
	 */
	public void process() {
		if (!isRunning()) {
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
					logger
							.info("Trying to start a new thread for the long running process");
				}
			} catch (Exception e) {
				statusMessage = "The long running process can't be started in a separate thread";
				withError = true;
				e.printStackTrace();
				logger.error(statusMessage, e);
			}
		}
	}

	/**
	 * Triggers the long running process. To be overwritten by child classes
	 */
	public void run() {
		this.statusMessage = "Running process";
		try {
			this.running = true;
			this.complete = false;
			long start = System.currentTimeMillis();
			// add process details
			long end = System.currentTimeMillis();
			long duration = (end - start) / 1000;
			this.statusMessage = "Completed after " + duration + " seconds .";
			this.complete = true;
			this.running = false;
		} catch (Exception e) {
			this.statusMessage = "Process is terminated with error";
			this.complete = true;
			this.running = false;
			this.withError = true;
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

	public String getProcessType() {
		return processType;
	}
}
