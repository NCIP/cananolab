package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.MetalParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.QuantumDotComposition;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.MetalParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.characterization.composition.QuantumDotBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class includes service calls involved in creating nanoparticles and
 * adding functions and characterizations for nanoparticles.
 * 
 * @author pansu
 * 
 */
public class SubmitNanoparticleService {
	private static Logger logger = Logger
			.getLogger(SubmitNanoparticleService.class);

	/**
	 * Update keywords and visibilities for the particle with the given name and
	 * type
	 * 
	 * @param particleType
	 * @param particleName
	 * @param keywords
	 * @param visibilities
	 * @throws Exception
	 */
	public void createNanoparticle(String particleType, String particleName,
			String[] keywords, String[] visibilities) throws Exception {

		// save nanoparticle to the database
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			// get the existing particle from database created during sample
			// creation
			List results = ida.search("from Nanoparticle where name='"
					+ particleName + "' and type='" + particleType + "'");
			Nanoparticle particle = null;
			for (Object obj : results) {
				particle = (Nanoparticle) obj;
			}
			if (particle == null) {
				throw new CalabException("No such particle in the database");
			}

			particle.getKeywordCollection().clear();
			if (keywords != null) {
				for (String keyword : keywords) {
					Keyword keywordObj = new Keyword();
					keywordObj.setName(keyword);
					particle.getKeywordCollection().add(keywordObj);
				}
			}

		} catch (Exception e) {
			ida.rollback();
			logger
					.error("Problem updating particle with name: "
							+ particleName);
			throw e;
		} finally {
			ida.close();
		}

		// remove existing visiblities for the nanoparticle
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		List<String> currentVisibilities = userService.getAccessibleGroups(
				particleName, "R");
		for (String visiblity : currentVisibilities) {
			userService.removeAccessibleGroup(particleName, visiblity, "R");
		}
		// set new visibilities for the nanoparticle
		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			userService.secureObject(particleName, "NCL_PI", "R");
			userService.secureObject(particleName, "NCL_Researcher", "R");
			userService.secureObject(particleName, visibility, "R");
		}
	}

	/**
	 * Saves the particle composition to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param composition
	 * @throws Exception
	 */
	public void addParticleComposition(String particleType,
			String particleName, CompositionBean composition) throws Exception {
		// if ID is not set save to the database otherwise update

		Characterization doComp = null;
		if (composition instanceof CarbonNanotubeBean) {
			doComp = (CarbonNanotubeComposition) composition.getDomainObj();
		} else if (composition instanceof DendrimerBean) {
			doComp = (DendrimerComposition) composition.getDomainObj();
		} else if (composition instanceof EmulsionBean) {
			doComp = (EmulsionComposition) composition.getDomainObj();
		} else if (composition instanceof FullereneBean) {
			doComp = (FullereneComposition) composition.getDomainObj();
		} else if (composition instanceof LiposomeBean) {
			doComp = (LiposomeComposition) composition.getDomainObj();
		} else if (composition instanceof MetalParticleBean) {
			doComp = (MetalParticleComposition) composition.getDomainObj();
		} else if (composition instanceof PolymerBean) {
			doComp = (PolymerComposition) composition.getDomainObj();
		} else if (composition instanceof QuantumDotBean) {
			doComp = (QuantumDotComposition) composition.getDomainObj();
		} else {
			throw new CalabException(
					"Can't save composition for the given particle type: "
							+ composition.getClass().getName());
		}
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();

			// get the existing particle and compositions from database created
			// during sample
			// creation
			List results = ida
					.search("select particle from Nanoparticle particle left join particle.characterizationCollection characterization where particle.name='"
							+ particleName
							+ "' and particle.type='"
							+ particleType
							+ "' and characterization.name='Composition' ");
			Nanoparticle particle = null;
			for (Object obj : results) {
				particle = (Nanoparticle) obj;
			}
			if (particle == null) {
				throw new CalabException("No such particle in the database");
			}

			Collection<Characterization> existingChars = particle
					.getCharacterizationCollection();

			boolean hasViewTitle = false;
			for (Characterization aChar : existingChars) {
				if (aChar.getIdentificationName().equals(
						doComp.getIdentificationName())) {
					hasViewTitle = true;
					break;
				}
			}
			if (hasViewTitle) {
				throw new CalabException("The view title is already in use.  Please enter a different one.");				
			}
			else {
				existingChars.add(doComp);
			}
		} catch (Exception e) {
			ida.rollback();
			logger.error("Problem saving composition: ");
			throw e;
		} finally {
			ida.close();
		}
	}

	public void saveAssayResult(String particleName, String fileName,
			String title, String description, String comments, String[] keywords) {

	}

	public List<FileBean> getAllRunFiles(String particleName) {
		List<FileBean> runFiles = new ArrayList<FileBean>();
		// TODO fill in the database query code
		FileBean file = new FileBean();
		file.setId("1");
		file.setShortFilename("NCL_3_distri.jpg");

		runFiles.add(file);
		return runFiles;
	}
}
