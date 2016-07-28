/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.security.enums.CharacterizationEnum;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.util.DateUtils;

/**
 * A Scheduler job for pulling public data counts from the database
 * 
 * @author pansu
 * 
 */
public class PublicDataCountJob extends QuartzJobBean
{
	private static Logger logger = Logger.getLogger(PublicDataCountJob.class.getName());
	
	private static PublicDataCountBean dataCounts;

	private SampleService sampleService;
	private ProtocolService protocolService;
	private PublicationService publicationService;
	private CharacterizationService characterizationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void executeInternal(JobExecutionContext context) throws JobExecutionException 
	{
		try {
			queryPublicDataCounts();
		} catch (Exception e) {
			new JobExecutionException(e);
		}
		
	}

	public void queryPublicDataCounts() {
		PublicDataCountBean dataCountBean = new PublicDataCountBean();
		dataCountBean.setNumOfPublicSamples(getPublicSampleCount());
		dataCountBean.setNumOfPublicSources(getPublicSampleSourceCount());
		dataCountBean.setNumOfPublicPublications(getPublicPublicationCount());
		dataCountBean.setNumOfPublicProtocols(getPublicProtocolCount());
		
		int physicoCharacterizationCount = getPublicCharacterizationCount(CharacterizationEnum.PHYSICO);
		dataCountBean.setNumOfPublicPhysicoChemicalCharacterizations(physicoCharacterizationCount);
		
		int invitroCharacterizationCount = getPublicCharacterizationCount(CharacterizationEnum.INVITRO);
		dataCountBean.setNumOfPublicInvitroCharacterizations(invitroCharacterizationCount);
		
		int invivoCharacterizationCount = getPublicCharacterizationCount(CharacterizationEnum.INVIVO);
		dataCountBean.setNumOfPublicInvivoCharacterizations(invivoCharacterizationCount);
		
		int otherCharacterizationCount = getPublicCharacterizationCount(CharacterizationEnum.OTHER);
		dataCountBean.setNumOfPublicOtherCharacterizations(otherCharacterizationCount);
		
		//dataCountBean.setNumOfPublicCharacterizations(getPublicCharacterizationCount("Characterization"));
		dataCountBean.setNumOfPublicCharacterizations(physicoCharacterizationCount + invitroCharacterizationCount + invivoCharacterizationCount + otherCharacterizationCount);
		
		//current time
		dataCountBean.setCountDateString(DateUtils.now());
		dataCounts = dataCountBean;		
	}

	private Integer getPublicSampleCount() 
	{
		Integer count = 0;
		try {
			count = sampleService.getNumberOfPublicSamplesForJob();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public samples from local site.");
		}
		return count;
	}

	private Integer getPublicSampleSourceCount() {
		Integer count = 0;
		try {
			count = sampleService.getNumberOfPublicSampleSourcesForJob();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public sample sources from local site.");
		}
		return count;
	}

	private Integer getPublicProtocolCount() {
		Integer count = 0;
		try {
			count = protocolService.getNumberOfPublicProtocolsForJob();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public protocols from local site.");
		}
		return count;
	}

	private Integer getPublicPublicationCount() {
		Integer count = 0;
		try {
			count = publicationService.getNumberOfPublicPublicationsForJob();
		} catch (Exception e) {
			logger.error("Error obtaining counts of public publications from local site.");
		}
		return count;
	}

	private int getPublicCharacterizationCount(CharacterizationEnum charEnum)
	{
		int count = 0;
		try {
			count = characterizationService.getNumberOfPublicCharacterizationsForJob(charEnum.getSubChars());
		} catch (Exception e) {
			logger.error("Error obtaining counts of public characterizations of type " + charEnum + " from local site.");
		}
		return count;
	}

	public PublicDataCountBean getPublicDataCounts() {
		return dataCounts;
	}

	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}

	public void setProtocolService(ProtocolService protocolService) {
		this.protocolService = protocolService;
	}

	public void setPublicationService(PublicationService publicationService) {
		this.publicationService = publicationService;
	}

	public void setCharacterizationService(CharacterizationService characterizationService) {
		this.characterizationService = characterizationService;
	}

}
