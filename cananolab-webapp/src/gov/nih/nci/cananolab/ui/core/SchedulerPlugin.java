package gov.nih.nci.cananolab.ui.core;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Create a scheduler using Quartz when container starts. Borrowed concepts from
 * LSD browser application.
 *
 * @author pansu, sahnih
 *
 */
public class SchedulerPlugin implements PlugIn {
	Logger logger = Logger.getLogger(SchedulerPlugin.class);
	private static Scheduler scheduler = null;
	private static final int DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS = 1;

	public void init(ActionServlet actionServlet, ModuleConfig config)
			throws ServletException {
		System.out.println("Initializing Scheduler Plugin for Jobs...");
		ServletContext context = actionServlet.getServletContext();
		// Retrieve the factory from the ServletContext.
		// It will be put there by the Quartz Servlet
		StdSchedulerFactory factory = (StdSchedulerFactory) context
				.getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
		try {
			// // Start the scheduler in case, it isn't started yet
			// if (m_startOnLoad != null
			// && m_startOnLoad.equals(Boolean.TRUE.toString())) {
			// System.out.println("Scheduler Will start in "
			// + m_startupDelayString + " milliseconds!");
			// // wait the specified amount of time before
			// // starting the process.
			// Thread delayedScheduler = new Thread(
			// new DelayedSchedulerStarted(scheduler, m_startupDelay));
			// // give the scheduler a name. All good code needs a name
			// delayedScheduler.setName("Delayed_Scheduler");
			// // Start out scheduler
			// delayedScheduler.start();
			// }

			// Retrieve the scheduler from the factory
			scheduler = factory.getScheduler();
			if (scheduler != null) {
				scheduler.start();
				// int gridDiscoveryIntervalInMinutes = getIntervalInMinutes(
				// actionServlet.getServletConfig(),
				// "gridDiscoveryIntervalInMinutes");
				// initialiseGridDiscoveryJob(gridDiscoveryIntervalInMinutes);
				// ServletContext appContext =
				// actionServlet.getServletContext();
				// List<GridNodeBean> gridNodes = (List<GridNodeBean>)
				// appContext
				// .getAttribute("allGridNodes");
				// initialisePublicDataCountJob(gridNodes,
				// gridDiscoveryIntervalInMinutes);
				int csmCleanupIntervalInMinutes = getIntervalInMinutes(
						actionServlet.getServletConfig(),
						"csmCleanupIntervalInMinutes");
				initialiseCSMCleanupJob(csmCleanupIntervalInMinutes);
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

	private int getIntervalInMinutes(ServletConfig servletConfig,
			String parameterName) {
		Integer interval = 0;
		try {
			interval = new Integer(servletConfig
					.getInitParameter(parameterName));
		} catch (NumberFormatException e) {
			// use default
			interval = DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS;
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
			logger.debug("CSM clean up scheduler started......");
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
