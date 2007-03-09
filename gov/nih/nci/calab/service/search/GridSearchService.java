package gov.nih.nci.calab.service.search;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabSvcClient;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.remote.RemoteQueryFacade;
import gov.nih.nci.calab.service.remote.RemoteQuerySystemPropertyConfigurer;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Remote search calls across the grid.
 * 
 * @author pansu
 * 
 */
public class GridSearchService {

	/**
	 * Search reports across the grid.
	 * 
	 * @param reportTitle
	 * @param reportType
	 * @param particleType
	 * @param functionTypes
	 * @param gridNodes
	 * @return
	 * @throws Exception
	 */
	public List<LabFileBean> getRemoteReports(String reportTitle,
			String reportType, String particleType, String[] functionTypes,
			GridNodeBean[] gridNodes) throws Exception {
		List<LabFileBean> reports = new ArrayList<LabFileBean>();

		for (GridNodeBean gridNode : gridNodes) {
			CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
					.getAddress());
			if (reportType.equals(CaNanoLabConstants.REPORT)) {
				Report[] gridReports = gridClient.getReports(reportTitle,
						particleType, functionTypes);
				if (gridReports != null) {
					for (Report report : gridReports) {
						reports.add(new LabFileBean(report,
								CaNanoLabConstants.REPORT, gridNode
										.getHostName()));
					}
				}
			} else if (reportType.equals(CaNanoLabConstants.ASSOCIATED_FILE)) {
				AssociatedFile[] gridAssociatedFiles = gridClient
						.getOtherAssociatedFiles(reportTitle, particleType,
								functionTypes);
				if (gridAssociatedFiles != null) {
					for (AssociatedFile report : gridAssociatedFiles) {
						reports.add(new LabFileBean(report,
								CaNanoLabConstants.ASSOCIATED_FILE, gridNode
										.getHostName()));
					}
				}
			} else {
				Report[] gridReports = gridClient.getReports(reportTitle,
						particleType, functionTypes);
				if (gridReports != null) {
					for (Report report : gridReports) {
						reports.add(new LabFileBean(report,
								CaNanoLabConstants.REPORT, gridNode
										.getHostName()));
					}
				}
				AssociatedFile[] gridAssociatedFiles = gridClient
						.getOtherAssociatedFiles(reportTitle, particleType,
								functionTypes);
				if (gridAssociatedFiles != null) {
					for (AssociatedFile report : gridAssociatedFiles) {
						reports.add(new LabFileBean(report,
								CaNanoLabConstants.ASSOCIATED_FILE, gridNode
										.getHostName()));
					}
				}
			}
		}
		return reports;
	}

	/**
	 * Search nanoparticles across the grid
	 * 
	 * @param particleType
	 * @param functionTypes
	 * @param characterizationTypes
	 * @param gridNodes
	 * @return
	 * @throws Exception
	 */
	public List<ParticleBean> getRemoteNanoparticles(String particleType,
			String[] functionTypes, String[] characterizationTypes,
			GridNodeBean[] gridNodes) throws Exception {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();
		for (GridNodeBean gridNode : gridNodes) {
			CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
					.getAddress());
			Nanoparticle[] gridParticles = gridClient.getNanoparticles(
					particleType, characterizationTypes, functionTypes);
			if (gridParticles != null) {
				for (Nanoparticle particle : gridParticles) {
					Function[] gridFunctions = gridClient
							.getFunctionsByParticleName(particle.getName());
					if (gridFunctions != null) {
						particle.setFunctionCollection(Arrays
								.asList(gridFunctions));
					}
					Source gridSource = gridClient
							.getSourceByParticleName(particle.getName());
					if (gridSource != null)
						particle.setSource(gridSource);
					particles.add(new ParticleBean(particle, gridNode
							.getHostName()));
				}
			}
		}
		return particles;
	}

	/**
	 * Get the remote file content from the grid
	 * 
	 * @param fileId
	 * @param gridNode
	 * @return
	 * @throws Exception
	 */
	public byte[] getRemoteFileContent0(String fileId, GridNodeBean gridNode)
			throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		byte[] fileContent = gridClient.getFileContent(Long.parseLong(fileId));
		return fileContent;
	}

	public byte[] getRemoteFileContent(String fileId, GridNodeBean gridNode)
			throws Exception {
		String remoteCodeBase = RemoteQuerySystemPropertyConfigurer
				.getRemoteServiceUrlCodebase(gridNode.getAppServiceURL());
		//dynamically set system property to contain the remote caNanoLab hostname
		System.getProperties().put("remote.codebase", remoteCodeBase);
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"caNanoLabClientContext.xml");
		RemoteQueryFacade remoteQueryFacade = (RemoteQueryFacade) ctx
				.getBean("remoteQueryProxy");
		byte[] fileContent = remoteQueryFacade.retrievePublicFileContent(Long
				.parseLong(fileId));
		return fileContent;
	}
}
