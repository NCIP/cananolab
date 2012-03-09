package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.util.DateUtils;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * A Scheduler job for pulling public data counts from the database
 * 
 * @author pansu
 * 
 */
public class PublicDataCountJob implements Job {
	private static Logger logger = Logger
			.getLogger(PublicDataCountJob.class.getName());
	private static PublicDataCountBean dataCounts;

	public PublicDataCountJob() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		queryPublicDataCounts();
	}

	public void queryPublicDataCounts() {
		PublicDataCountBean dataCountBean = new PublicDataCountBean();
		dataCountBean.setNumOfPublicSamples(getPublicSampleCount());
		dataCountBean.setNumOfPublicSources(getPublicSampleSourceCount());
		dataCountBean.setNumOfPublicPublications(getPublicPublicationCount());
		dataCountBean.setNumOfPublicProtocols(getPublicProtocolCount());
		dataCountBean
				.setNumOfPublicCharacterizations(getPublicCharacterizationCount("Characterization"));
		dataCountBean
				.setNumOfPublicPhysicoChemicalCharacterizations(getPublicCharacterizationCount("PhysicoChemicalCharacterization"));
		dataCountBean
				.setNumOfPublicInvitroCharacterizations(getPublicCharacterizationCount("InvitroCharacterization"));
		dataCountBean
				.setNumOfPublicInvivoCharacterizations(getPublicCharacterizationCount("InvivoCharacterization"));
		dataCountBean
				.setNumOfPublicOtherCharacterizations(getPublicCharacterizationCount("OtherCharacterization"));
		//current time
		dataCountBean.setCountDateString(DateUtils.now());
		dataCounts = dataCountBean;		
	}

	private Integer getPublicSampleCount() {
		Integer count = 0;
		SampleService service = new SampleServiceLocalImpl();
		try {
			count = service.getNumberOfPublicSamples();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public samples from local site.");
		}
		return count;
	}

	private Integer getPublicSampleSourceCount() {
		Integer count = 0;
		SampleService service = null;
		try {
			service = new SampleServiceLocalImpl();
			count = service.getNumberOfPublicSampleSources();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public sample sources from local site.");
		}
		return count;
	}

	private Integer getPublicProtocolCount() {
		Integer count = 0;
		ProtocolService service = null;
		try {
			service = new ProtocolServiceLocalImpl();
			count = service.getNumberOfPublicProtocols();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public protocols from local site.");
		}
		return count;
	}

	private Integer getPublicPublicationCount() {
		Integer count = 0;
		PublicationService service = null;
		try {
			service = new PublicationServiceLocalImpl();
			count = service.getNumberOfPublicPublications();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public publications from local site.");
		}
		return count;
	}

	private Integer getPublicCharacterizationCount(
			String characterizationClassName) {
		Integer count = 0;
		CharacterizationService service = null;
		try {
			service = new CharacterizationServiceLocalImpl();
			count = service
					.getNumberOfPublicCharacterizations(characterizationClassName);
		} catch (Exception e) {
			logger.error("Error obtaining counts of public characterizations of type "
					+ characterizationClassName + " from local site.");
		}
		return count;
	}

	public PublicDataCountBean getPublicDataCounts() {
		return dataCounts;
	}
}
