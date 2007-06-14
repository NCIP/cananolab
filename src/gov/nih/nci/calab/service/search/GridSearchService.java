package gov.nih.nci.calab.service.search;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabSvcClient;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.Source;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComposingElement;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.SurfaceGroup;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.remote.RemoteQueryFacade;
import gov.nih.nci.calab.service.remote.RemoteQuerySystemPropertyConfigurer;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * Retrieve remote reports files from the given grid node.
	 * 
	 * @param reportTitle
	 * @param reportType
	 * @param particleType
	 * @param functionTypes
	 * @param gridNode
	 * @return
	 * @throws Exception
	 */
	public List<LabFileBean> getRemoteReports(String reportTitle,
			String reportType, String particleType, String[] functionTypes,
			GridNodeBean gridNode) throws Exception {
		List<LabFileBean> reports = new ArrayList<LabFileBean>();

		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		if (reportType.equals(CaNanoLabConstants.REPORT)) {
			Report[] gridReports = gridClient.getReports(reportTitle,
					particleType, functionTypes);
			if (gridReports != null) {
				for (Report report : gridReports) {
					reports.add(new LabFileBean(report,
							CaNanoLabConstants.REPORT, gridNode.getHostName()));
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
							CaNanoLabConstants.REPORT, gridNode.getHostName()));
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
		return reports;
	}

	/**
	 * Retrieve nanoparticles from the given grid node
	 * 
	 * @param particleType
	 * @param functionTypes
	 * @param characterizationTypes
	 * @param gridNode
	 * @return
	 * @throws Exception
	 */
	public List<ParticleBean> getRemoteNanoparticles(String particleType,
			String[] functionTypes, String[] characterizationTypes,
			GridNodeBean gridNode) throws Exception {
		List<ParticleBean> particles = new ArrayList<ParticleBean>();

		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		Nanoparticle[] gridParticles = gridClient.getNanoparticles(
				particleType, characterizationTypes, functionTypes);
		if (gridParticles != null) {
			for (Nanoparticle particle : gridParticles) {
				Characterization[] gridCharacterizations = gridClient
						.getCharacterizationsByParticleName(particle.getName());
				if (gridCharacterizations != null) {
					particle.setCharacterizationCollection(Arrays
							.asList(gridCharacterizations));
				}
				Function[] gridFunctions = gridClient
						.getFunctionsByParticleName(particle.getName());
				if (gridFunctions != null) {
					particle
							.setFunctionCollection(Arrays.asList(gridFunctions));
				}
				Source gridSource = gridClient.getSourceByParticleName(particle
						.getName());
				if (gridSource != null)
					particle.setSource(gridSource);
				particles
						.add(new ParticleBean(particle, gridNode.getHostName()));
			}
		}
		return particles;
	}

	public Nanoparticle getRemoteNanoparticle(String particleName,
			GridNodeBean gridNode) throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		Nanoparticle particle = gridClient.getNanoparticle(particleName);
		Characterization[] gridCharacterizations = gridClient
				.getCharacterizationsByParticleName(particle.getName());
		if (gridCharacterizations != null) {
			particle.setCharacterizationCollection(Arrays
					.asList(gridCharacterizations));
		}
		Function[] gridFunctions = gridClient
				.getFunctionsByParticleName(particle.getName());
		if (gridFunctions != null) {
			particle.setFunctionCollection(Arrays.asList(gridFunctions));
		}
		Source gridSource = gridClient.getSourceByParticleName(particle
				.getName());
		if (gridSource != null)
			particle.setSource(gridSource);

		return particle;
	}

	/**
	 * Get a lookup table with key being characterization type and value being a
	 * list of CharacterizationBeans the given particle has.
	 * 
	 * @param particleName
	 * @param gridNode
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<CharacterizationBean>> getRemoteCharacterizationMap(
			String particleName, GridNodeBean gridNode) throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		Characterization[] gridCharacterizations = gridClient
				.getCharacterizationsByParticleName(particleName);

		Map<String, List<CharacterizationBean>> charTypeChars = new HashMap<String, List<CharacterizationBean>>();

		// set up lookup table, key characterization type, value
		// characterizationBeans for the side menu. Keep the
		// characterizations types in order as in lookupService.
		if (gridCharacterizations != null) {
			LookupService lookupService = new LookupService();
			Map<String, List<String>> orderedCharTypeCharStrings = lookupService
					.getCharacterizationTypeCharacterizations();

			for (String charType : orderedCharTypeCharStrings.keySet()) {
				List<CharacterizationBean> newCharBeans = new ArrayList<CharacterizationBean>();
				List<String> charStringList = orderedCharTypeCharStrings.get(charType);
				for (Characterization chara : gridCharacterizations) {
					if (charStringList.contains(chara.getName())) {
						newCharBeans.add(new CharacterizationBean(chara));
					}
				}
				if (!newCharBeans.isEmpty()) {
					charTypeChars.put(charType, newCharBeans);
				}
			}
		}
		return charTypeChars;
	}

	public Map<String, List<FunctionBean>> getRemoteFunctionMap(
			String particleName, GridNodeBean gridNode) throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		Function[] gridFunctions = gridClient
				.getFunctionsByParticleName(particleName);

		Map<String, List<FunctionBean>> funcTypeFuncs = new HashMap<String, List<FunctionBean>>();
		List<FunctionBean> funcs = new ArrayList<FunctionBean>();
		if (gridFunctions != null) {
			for (Function func : gridFunctions) {
				if (funcTypeFuncs.get(func.getType()) != null) {
					funcs = (List<FunctionBean>) (funcTypeFuncs.get(func
							.getType()));
				} else {
					funcs = new ArrayList<FunctionBean>();
					funcTypeFuncs.put(func.getType(), funcs);
				}
				funcs.add(new FunctionBean(func));
			}
		}
		return funcTypeFuncs;
	}

	public List<LabFileBean> getRemoteReports(String particleName,
			GridNodeBean gridNode) throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());

		List<LabFileBean> reports = new ArrayList<LabFileBean>();
		Report[] gridReports = gridClient
				.getReportsByParticleName(particleName);
		if (gridReports != null) {
			for (Report report : gridReports) {
				reports.add(new LabFileBean(report, CaNanoLabConstants.REPORT,
						gridNode.getHostName()));
			}
		}
		return reports;
	}

	public List<LabFileBean> getRemoteAssociatedFiles(String particleName,
			GridNodeBean gridNode) throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());

		List<LabFileBean> files = new ArrayList<LabFileBean>();
		AssociatedFile[] gridFiles = gridClient
				.getOtherAssociatedFilesByParticleName(particleName);
		if (gridFiles != null) {
			for (AssociatedFile file : gridFiles) {
				files.add(new LabFileBean(file,
						CaNanoLabConstants.ASSOCIATED_FILE, gridNode
								.getHostName()));
			}
		}
		return files;
	}

	public ParticleComposition getRemoteComposition(String compositionId,
			String particleName, GridNodeBean gridNode) throws Exception {
		CaNanoLabSvcClient gridClient = new CaNanoLabSvcClient(gridNode
				.getAddress());
		ParticleComposition comp = (ParticleComposition) gridClient
				.getCharacterization(Long.parseLong(compositionId),
						particleName);
		ComposingElement[] composingElements = gridClient.getComposingElements(
				Long.parseLong(compositionId), particleName);
		if (comp != null && composingElements != null) {
			comp
					.setComposingElementCollection(Arrays
							.asList(composingElements));
		}
		if (comp instanceof DendrimerComposition) {
			SurfaceGroup[] surfaceGroups = gridClient.getSurfaceGroups(Long
					.parseLong(compositionId), particleName);
			if (comp != null && surfaceGroups != null) {
				((DendrimerComposition) comp).setSurfaceGroupCollection(Arrays
						.asList(surfaceGroups));
			}
		}
		return comp;
	}

	/**
	 * Retrieve the content of the remote file directly from the webapp hosting
	 * the file (not going through the grid).
	 * 
	 * @param fileId
	 * @param gridNode
	 * @return
	 * @throws Exception
	 */
	public byte[] getRemoteFileContent(String fileId, GridNodeBean gridNode)
			throws Exception {
		String remoteCodeBase = RemoteQuerySystemPropertyConfigurer
				.getRemoteServiceUrlCodebase(gridNode.getAppServiceURL());
		// dynamically set system property to contain the remote caNanoLab
		// hostname
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
