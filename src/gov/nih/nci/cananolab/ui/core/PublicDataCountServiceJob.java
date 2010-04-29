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

	public PublicDataCountBean queryPublicDataCounts(GridNodeBean gridNode) {
		Integer sampleCount = 0;
		Integer sourceCount = 0;
		Integer publicationCount = 0;
		Integer protocolCount = 0;
		Integer charCount = 0;
		Integer physicoCharCount = 0;
		Integer invitroCharCount = 0;
		Integer invivoCharCount = 0;
		Integer otherCharCount = 0;
		sampleCount = getPublicSampleCount(gridNode);
		sourceCount = getPublicSampleSourceCount(gridNode);
		protocolCount = getPublicProtocolCount(gridNode);
		publicationCount = getPublicPublicationCount(gridNode);
		charCount = getPublicCharacterizationCount("Characterization", gridNode);
		physicoCharCount = getPublicCharacterizationCount(
				"PhysicoChemicalCharacterization", gridNode);
		invitroCharCount = getPublicCharacterizationCount(
				"InvitroCharacterization", gridNode);
		invivoCharCount = getPublicCharacterizationCount(
				"InvivoCharacterization", gridNode);
		otherCharCount = getPublicCharacterizationCount(
				"OtherCharacterization", gridNode);

		PublicDataCountBean dataCountBean = new PublicDataCountBean();
		dataCountBean.setNumOfPublicSamples(sampleCount);
		dataCountBean.setNumOfPublicSources(sourceCount);
		dataCountBean.setNumOfPublicPublications(publicationCount);
		dataCountBean.setNumOfPublicProtocols(protocolCount);
		dataCountBean.setNumOfPublicCharacterizations(charCount);
		dataCountBean
				.setNumOfPublicPhysicoChemicalCharacterizations(physicoCharCount);
		dataCountBean.setNumOfPublicInvitroCharacterizations(invitroCharCount);
		dataCountBean.setNumOfPublicInvivoCharacterizations(invivoCharCount);
		dataCountBean.setNumOfPublicOtherCharacterizations(otherCharCount);
		return dataCountBean;
	}

	public void queryPublicDataCounts(List<GridNodeBean> gridNodes) {
		Integer sampleCount = 0;
		Integer sourceCount = 0;
		Integer publicationCount = 0;
		Integer protocolCount = 0;
		Integer charCount = 0;
		Integer physicoCharCount = 0;
		Integer invitroCharCount = 0;
		Integer invivoCharCount = 0;
		Integer otherCharCount = 0;
		// remove local grid node
		List<GridNodeBean> remoteNodes = new ArrayList<GridNodeBean>(gridNodes);
		for (GridNodeBean gridNode : gridNodes) {
			if (gridNode.getHostName().equals(Constants.LOCAL_SITE)) {
				remoteNodes.remove(gridNode);
			}
		}
		// add local site
		GridNodeBean localSite = new GridNodeBean();
		localSite.setHostName(Constants.LOCAL_SITE);
		remoteNodes.add(localSite);
		for (GridNodeBean gridNode : remoteNodes) {
			PublicDataCountBean gridDataCountBean = this
					.queryPublicDataCounts(gridNode);
			sampleCount += gridDataCountBean.getNumOfPublicSamples();
			sourceCount += gridDataCountBean.getNumOfPublicSources();
			protocolCount += gridDataCountBean.getNumOfPublicProtocols();
			publicationCount += gridDataCountBean.getNumOfPublicPublications();
			charCount += gridDataCountBean.getNumOfPublicCharacterizations();
			physicoCharCount += gridDataCountBean
					.getNumOfPublicPhysicoChemicalCharacterizations();
			invitroCharCount += gridDataCountBean
					.getNumOfPublicInvitroCharacterizations();
			invivoCharCount += gridDataCountBean
					.getNumOfPublicInvivoCharacterizations();
			otherCharCount += gridDataCountBean
					.getNumOfPublicOtherCharacterizations();
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
		dataCounts.setNumOfPublicInvivoCharacterizations(invivoCharCount);
		dataCounts.setNumOfPublicOtherCharacterizations(otherCharCount);
	}

	private Integer getPublicSampleCount(GridNodeBean gridNode) {
		Integer count = 0;
		SampleService service = null;
		String hostName = gridNode.getHostName();
		String serviceUrl = gridNode.getAddress();
		if (hostName.equals(Constants.LOCAL_SITE)) {
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

	private Integer getPublicSampleSourceCount(GridNodeBean gridNode) {
		Integer count = 0;
		SampleService service = null;
		String hostName = gridNode.getHostName();
		String serviceUrl = gridNode.getAddress();
		if (hostName.equals(Constants.LOCAL_SITE)) {
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
				count = service.getNumberOfPublicSampleSources();
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public sample sources from grid service at"
								+ serviceUrl);
			}
		}
		return count;
	}

	private Integer getPublicProtocolCount(GridNodeBean gridNode) {
		Integer count = 0;
		ProtocolService service = null;
		String hostName = gridNode.getHostName();
		String serviceUrl = gridNode.getAddress();
		if (hostName.equals(Constants.LOCAL_SITE)) {
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

	private Integer getPublicPublicationCount(GridNodeBean gridNode) {
		Integer count = 0;
		PublicationService service = null;
		String hostName = gridNode.getHostName();
		String serviceUrl = gridNode.getAddress();
		if (hostName.equals(Constants.LOCAL_SITE)) {
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

	private Integer getPublicCharacterizationCount(
			String characterizationClassName, GridNodeBean gridNode) {
		Integer count = 0;
		CharacterizationService service = null;
		String hostName = gridNode.getHostName();
		String serviceUrl = gridNode.getAddress();
		if (hostName.equals(Constants.LOCAL_SITE)) {
			try {
				service = new CharacterizationServiceLocalImpl();
				count += service
						.getNumberOfPublicCharacterizations(characterizationClassName);
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public characterizations of type "
								+ characterizationClassName
								+ " from local site.");
			}
		} else {
			try {
				service = new CharacterizationServiceRemoteImpl(serviceUrl);
				count = service
						.getNumberOfPublicCharacterizations(characterizationClassName);
			} catch (Exception e) {
				logger
						.error("Error obtaining counts of public characterizations of type "
								+ characterizationClassName
								+ " from grid service at" + serviceUrl);
			}
		}
		return count;
	}

	public PublicDataCountBean getPublicDataCounts() {
		return dataCounts;
	}
}
