package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.service.CSMCleanupJob;
import gov.nih.nci.cananolab.service.PublicDataCountJob;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Create a scheduler using Quartz when container starts. 
 * 
 * @author pansu
 * 
 */
public class SchedulerPlugin implements PlugIn {
	Logger logger = Logger.getLogger(SchedulerPlugin.class);
	private static Scheduler scheduler = null;
	private static final int DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS = 1;
	private static final int DEFAULT_PUBLIC_COUNT_PULL_INTERVAL_IN_HOURS = 24;

	public void init(ActionServlet actionServlet, ModuleConfig config)
			throws ServletException {
		logger.info("Initializing Scheduler Plugin for Jobs...");
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			if (scheduler != null) {
				scheduler.start();
				int csmCleanupIntervalInMinutes = getInterval(
						actionServlet.getServletConfig(),
						"csmCleanupIntervalInMinutes");
				initialiseCSMCleanupJob(csmCleanupIntervalInMinutes);

				int publicCountPullIntervalInHours = getInterval(
						actionServlet.getServletConfig(),
						"publicCountPullIntervalInHours");
				initialisePublicCountPullJob(publicCountPullIntervalInHours);
			}
		} catch (SchedulerException e) {
			logger.error("Error setting up scheduler", e);
		}
	}

	// This method will be called at application shutdown time
	public void destroy() {
		System.out.println("Entering SchedulerPlugin.destroy()");
		System.out.println("Exiting SchedulerPlugIn.destroy()");
	}

	private int getInterval(ServletConfig servletConfig, String parameterName) {
		Integer interval = 0;
		try {
			interval = new Integer(
					servletConfig.getInitParameter(parameterName));
		} catch (NumberFormatException e) {
			// use default
			if (parameterName.contains("csm")) {
				interval = DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS;
			} else if (parameterName.contains("publicCount")) {
				interval = DEFAULT_PUBLIC_COUNT_PULL_INTERVAL_IN_HOURS;
			}
		}
		return interval;
	}

	public void initialiseCSMCleanupJob(int intervalInMinutes) {
		try {
			if (intervalInMinutes == 0) {
				// default is 1 minute
				intervalInMinutes = DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS;
			}

			JobDetail jobDetail = new JobDetail("CSMCleanupJob", null,
					CSMCleanupJob.class);
			Trigger trigger = TriggerUtils.makeMinutelyTrigger(
					"CSMCleanupJobTrigger", intervalInMinutes,
					SimpleTrigger.REPEAT_INDEFINITELY);
			scheduler.scheduleJob(jobDetail, trigger);
			logger.info("CSM clean up scheduler started......");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void initialisePublicCountPullJob(int intervalInHours) {
		try {
			if (intervalInHours == 0) {
				// default is 24 hours
				intervalInHours = DEFAULT_PUBLIC_COUNT_PULL_INTERVAL_IN_HOURS;
			}
			JobDetail jobDetail = new JobDetail("publicCountJob", null,
					PublicDataCountJob.class);

			Trigger trigger = TriggerUtils.makeHourlyTrigger(
					"publicCountJobTrigger", intervalInHours,
					SimpleTrigger.REPEAT_INDEFINITELY);

			scheduler.scheduleJob(jobDetail, trigger);
			logger.info("Public data count scheduler started......");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
