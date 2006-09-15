package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.SizeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.ArrayList;
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
		// by default, always set visibility to NCL_PI and NCL_Researcher to
		// be true
		userService.secureObject(particleName, "NCL_PI", "R");
		userService.secureObject(particleName, "NCL_Researcher", "R");
		if (visibilities != null){
			for (String visibility : visibilities) {				
				userService.secureObject(particleName, visibility, "R");
			}
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
		doComp = composition.getDomainObj();

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		Nanoparticle particle = null;
		int existingViewTitleCount = -1;
		try {
			ida.open();
			// check if viewTitle is already used the same type of
			// characterization for the same particle
			String viewTitleQuery = "";
			if (doComp.getId() == null) {
				viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
						+ particleName
						+ "' and particle.type='"
						+ particleType
						+ "' and achar.identificationName='"
						+ doComp.getIdentificationName()
						+ "' and achar.name='"
						+ doComp.getName() + "'";
			} else {
				viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
						+ particleName
						+ "' and particle.type='"
						+ particleType
						+ "' and achar.identificationName='"
						+ doComp.getIdentificationName()
						+ "' and achar.name='"
						+ doComp.getName()
						+ "' and achar.id!="
						+ doComp.getId();
			}
			List viewTitleResult = ida.search(viewTitleQuery);

			for (Object obj : viewTitleResult) {
				existingViewTitleCount = ((Integer) (obj)).intValue();
			}
			if (existingViewTitleCount == 0) {
				// if ID exists, do update
				if (doComp.getId() != null) {
					ida.store(doComp);
				} else {// get the existing particle and compositions
					// from database
					// created
					// during sample
					// creation
					List results = ida
							.search("select particle from Nanoparticle particle left join fetch particle.characterizationCollection where particle.name='"
									+ particleName
									+ "' and particle.type='"
									+ particleType + "'");

					for (Object obj : results) {
						particle = (Nanoparticle) obj;
					}

					if (particle != null) {
						particle.getCharacterizationCollection().add(doComp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving composition: ");
			throw e;
		} finally {
			ida.close();
		}
		if (existingViewTitleCount > 0) {
			throw new CalabException(
					"The view title is already in use.  Please enter a different one.");
		}
	}

	public void addParticleSize(String particleType,
			String particleName, SizeBean size) throws Exception {
		//TODO add database code, try to reuse code in addParticleComposition
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
