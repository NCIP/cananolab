package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.dto.common.PublicDataCountBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceRemoteImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceRemoteImpl;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author pansu
 * 
 */
public class PublicDataCountServiceJob implements Job {
	private static Logger logger = Logger
			.getLogger(PublicDataCountServiceJob.class.getName());
	private static PublicDataCountBean dataCounts = null;

	public PublicDataCountServiceJob() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// query for public data counts
		List<GridNodeBean> gridNodes = (List<GridNodeBean>) context
				.getJobDetail().getJobDataMap().get("gridNodes");
		if (gridNodes == null) {
			GridDiscoveryServiceJob discoveryJob = new GridDiscoveryServiceJob();
			gridNodes = discoveryJob.getAllGridNodes();
		}
		queryPublicDataCounts(gridNodes);
	}

	public void queryPublicDataCounts(List<GridNodeBean> gridNodes) {
		List<String> locations = new ArrayList<String>();
		// always queries local site
		locations.add(Constants.LOCAL_SITE);
		// add grid service url to locations

		for (GridNodeBean gridNode : gridNodes) {
			locations.add(gridNode.getAddress());
		}
		Integer sampleCount = 0;
		Integer sourceCount = 0;
		Integer publicationCount = 0;
		Integer protocolCount = 0;
		Integer charCount = 0;
		Integer physicoCharCount = 0;
		Integer invitroCharCount = 0;
		for (String location : locations) {
			sampleCount += getPublicSampleCount(location);
			sourceCount += getPublicSampleSourceCount(location);
			protocolCount += getPublicProtocolCount(location);
			publicationCount += getPublicPublicationCount(location);
			charCount += getPublicCharacterizationCount(location);
			physicoCharCount += getPublicPhysicoChemicalCharacterizationCount(location);
			invitroCharCount += getPublicInvitroCharacterizationCount(location);
		}
		dataCounts = new PublicDataCountBean();
		dataCounts.setNumOfPublicSamples(sampleCount);
		dataCounts.setNumOfPublicSources(sourceCount);
		dataCounts.setNumOfPublicPublications(publicationCount);
		dataCounts.setNumOfPublicProtocols(protocolCount);
		dataCounts.setNumOfPublicCharacterizations(charCount);
		dataCounts
				.setNumOfPublicPhysicoChemicalCharacterizations(physicoCharCount);
		dataCounts.setNumOfPublicInvitroCharacterizations(invitroCharCount);
	}

	private Integer getPublicSampleCount(String serviceUrl) {
		Integer count = 0;
		SampleService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new SampleServiceLocalImpl();
				count += service.getNumberOfPublicSamples();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public samples from local site.");
			}
		} else {
			try {
				service = new SampleServiceRemoteImpl(serviceUrl);
				count = service.getNumberOfPublicSamples();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public samples from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicSampleSourceCount(String serviceUrl) {
		Integer count = 0;
		SampleService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new SampleServiceLocalImpl();
				count += service.getNumberOfPublicSampleSources();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public sample sources from local site.");
			}
		} else {
			try {
				service = new SampleServiceRemoteImpl(serviceUrl);
				count = service.getNumberOfPublicSamples();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public sample sources from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicProtocolCount(String serviceUrl) {
		Integer count = 0;
		ProtocolService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new ProtocolServiceLocalImpl();
				count += service.getNumberOfPublicProtocols();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public protocols from local site.");
			}
		} else {
			try {
				service = new ProtocolServiceRemoteImpl(serviceUrl);
				count = service.getNumberOfPublicProtocols();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public protocols from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicPublicationCount(String serviceUrl) {
		Integer count = 0;
		PublicationService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new PublicationServiceLocalImpl();
				count += service.getNumberOfPublicPublications();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public publications from local site.");
			}
		} else {
			try {
				service = new PublicationServiceRemoteImpl(serviceUrl);
				count = service.getNumberOfPublicPublications();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public publications from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicCharacterizationCount(String serviceUrl) {
		Integer count = 0;
		CharacterizationService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new CharacterizationServiceLocalImpl();
				count += service.getNumberOfPublicCharacterizations();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public characterizations from local site.");
			}
		} else {
			try {
				service = new CharacterizationServiceRemoteImpl(serviceUrl);
				count = service.getNumberOfPublicCharacterizations();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public characterizations from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicPhysicoChemicalCharacterizationCount(
			String serviceUrl) {
		Integer count = 0;
		CharacterizationService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new CharacterizationServiceLocalImpl();
				count += service
						.getNumberOfPublicPhysicoChemicalCharacterizations();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public physico-chemical characterizations from local site.");
			}
		} else {
			try {
				service = new CharacterizationServiceRemoteImpl(serviceUrl);
				count = service
						.getNumberOfPublicPhysicoChemicalCharacterizations();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public  physico-chemical characterizations from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicInvitroCharacterizationCount(String serviceUrl) {
		Integer count = 0;
		CharacterizationService service = null;
		if (serviceUrl.equals(Constants.LOCAL_SITE)) {
			try {
				service = new CharacterizationServiceLocalImpl();
				count += service.getNumberOfPublicInvitroCharacterizations();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public in vitro characterizations from local site.");
			}
		} else {
			try {
				service = new CharacterizationServiceRemoteImpl(serviceUrl);
				count = service.getNumberOfPublicInvitroCharacterizations();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public in vitro characterizations from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	public PublicDataCountBean getPublicDataCounts() {
		return dataCounts;
	}
}
