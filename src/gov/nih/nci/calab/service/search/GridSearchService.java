package gov.nih.nci.calab.service.search;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabSvcClient;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.common.GridNodeBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Remote search calls across the grid.
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
					particles.add(new ParticleBean(particle, gridNode
							.getHostName()));
				}
			}
		}
		return particles;
	}
}
