/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.core;

import gov.nih.nci.cananolab.service.CSMCleanupJob;
import gov.nih.nci.cananolab.service.IndexWriter;
import gov.nih.nci.cananolab.service.PublicDataCountJob;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Create a scheduler using Quartz when container starts. 
 * 
 * @author pansu
 * 
 */
public class QuartzScheduler implements InitializingBean {
	Logger logger = Logger.getLogger(QuartzScheduler.class);
	private static Scheduler scheduler = null;
	private static final int DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS = 15;
	private static final int DEFAULT_PUBLIC_COUNT_PULL_INTERVAL_IN_HOURS = 24;
	private static final int DEFAULT_INDEX_INTERVAL_IN_HOURS = 24;

	int csmCleanupIntervalInMinutes = 15;
	int publicCountPullIntervalInHours = 24;
	int indexIntervalInHours = 24;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.info("Initializing Scheduler Plugin for Jobs...");
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			if (scheduler != null) {
				scheduler.start();
//				int csmnMinutes = getInterval("csm", csmCleanupIntervalInMinutes);
//				initialiseCSMCleanupJob(csmnMinutes);

				int publicCountInHours = getInterval("publicCount", publicCountPullIntervalInHours);
				initialisePublicCountPullJob(publicCountInHours);
				
				int indexInHours = getInterval("luceneIndex", indexIntervalInHours);
				initialiseLuceneIndexing(indexInHours);
			}
		} catch (SchedulerException e) {
			logger.error("Error setting up scheduler", e);
		}
	}
	
	

private void initialiseLuceneIndexing(int indexInHours) {
	try {
		if (indexInHours == 0) {
			// default is 24 hours
			indexInHours = DEFAULT_INDEX_INTERVAL_IN_HOURS;
		}
		JobDetail jobDetail = new JobDetail("luceneIndexJob", null,
				IndexWriter.class);

		Trigger trigger = TriggerUtils.makeHourlyTrigger(
				"luceneIndexJobTrigger", indexInHours,
				SimpleTrigger.REPEAT_INDEFINITELY);

		scheduler.scheduleJob(jobDetail, trigger);
		logger.info("Lucene Index scheduler started......");
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
	}
		
	}



//	public void init(ActionServlet actionServlet, ModuleConfig config)
//			throws ServletException {
//		logger.info("Initializing Scheduler Plugin for Jobs...");
//		try {
//			scheduler = new StdSchedulerFactory().getScheduler();
//			if (scheduler != null) {
//				scheduler.start();
//				int csmCleanupIntervalInMinutes = getInterval(
//						actionServlet.getServletConfig(),
//						"csmCleanupIntervalInMinutes");
//				initialiseCSMCleanupJob(csmCleanupIntervalInMinutes);
//
//				int publicCountPullIntervalInHours = getInterval(
//						actionServlet.getServletConfig(),
//						"publicCountPullIntervalInHours");
//				initialisePublicCountPullJob(publicCountPullIntervalInHours);
//			}
//		} catch (SchedulerException e) {
//			logger.error("Error setting up scheduler", e);
//		}
//	}

	public int getCsmCleanupIntervalInMinutes() {
		return csmCleanupIntervalInMinutes;
	}



	public void setCsmCleanupIntervalInMinutes(int csmCleanupIntervalInMinutes) {
		this.csmCleanupIntervalInMinutes = csmCleanupIntervalInMinutes;
	}



	public int getPublicCountPullIntervalInHours() {
		return publicCountPullIntervalInHours;
	}



	public void setPublicCountPullIntervalInHours(int publicCountPullIntervalInHours) {
		this.publicCountPullIntervalInHours = publicCountPullIntervalInHours;
	}



	public int getIndexIntervalInHours() {
		return indexIntervalInHours;
	}



	public void setIndexIntervalInHours(int indexIntervalInHours) {
		this.indexIntervalInHours = indexIntervalInHours;
	}



	// This method will be called at application shutdown time
	public void destroy() {
		System.out.println("Entering SchedulerPlugin.destroy()");
		System.out.println("Exiting SchedulerPlugIn.destroy()");
	}

//	private int getInterval(ServletConfig servletConfig, String parameterName) {
//		Integer interval = 0;
//		try {
//			interval = new Integer(
//					servletConfig.getInitParameter(parameterName));
//		} catch (NumberFormatException e) {
//			// use default
//			if (parameterName.contains("csm")) {
//				interval = DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS;
//			} else if (parameterName.contains("publicCount")) {
//				interval = DEFAULT_PUBLIC_COUNT_PULL_INTERVAL_IN_HOURS;
//			}
//		}
//		return interval;
//	}
	
	private int getInterval(String type, int configuredHours) {
		Integer interval = 0;
		try {
			interval = new Integer(configuredHours);
		} catch (NumberFormatException e) {
			// use default
			if (type.contains("csm")) {
				interval = DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS;
			} else if (type.contains("publicCount")) {
				interval = DEFAULT_PUBLIC_COUNT_PULL_INTERVAL_IN_HOURS;
			} else if (type.contains("luceneIndex")) {
				interval = DEFAULT_INDEX_INTERVAL_IN_HOURS;
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
