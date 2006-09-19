package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Run;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.SizeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

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
		if (visibilities != null) {
			for (String visibility : visibilities) {
				userService.secureObject(particleName, visibility, "R");
			}
		}

	}

	/**
	 * Save characterization to the database.
	 * 
	 * @param particleType
	 * @param particleName
	 * @param achar
	 * @throws Exception
	 */
	private void addParticleCharacterization(String particleType,
			String particleName, Characterization achar) throws Exception {
		// if ID is not set save to the database otherwise update
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		Nanoparticle particle = null;
		int existingViewTitleCount = -1;
		try {
			ida.open();
			// check if viewTitle is already used the same type of
			// characterization for the same particle
			String viewTitleQuery = "";
			if (achar.getId() == null) {
				viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
						+ particleName
						+ "' and particle.type='"
						+ particleType
						+ "' and achar.identificationName='"
						+ achar.getIdentificationName()
						+ "' and achar.name='"
						+ achar.getName() + "'";
			} else {
				viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
						+ particleName
						+ "' and particle.type='"
						+ particleType
						+ "' and achar.identificationName='"
						+ achar.getIdentificationName()
						+ "' and achar.name='"
						+ achar.getName() + "' and achar.id!=" + achar.getId();
			}
			List viewTitleResult = ida.search(viewTitleQuery);

			for (Object obj : viewTitleResult) {
				existingViewTitleCount = ((Integer) (obj)).intValue();
			}
			if (existingViewTitleCount == 0) {
				// if ID exists, do update
				if (achar.getId() != null) {
					ida.store(achar);
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
						particle.getCharacterizationCollection().add(achar);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization: ");
			throw e;
		} finally {
			ida.close();
		}
		if (existingViewTitleCount > 0) {
			throw new CalabException(
					"The view title is already in use.  Please enter a different one.");
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

		Characterization doComp = composition.getDomainObj();
		addParticleCharacterization(particleType, particleName, doComp);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param size
	 * @throws Exception
	 */
	public void addParticleSize(String particleType, String particleName,
			SizeBean size) throws Exception {
		Characterization doSize = size.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doSize);
	}

	/**
	 * Save the characterization file into the database and file system
	 * 
	 * @param particleName
	 * @param file
	 * @param title
	 * @param description
	 * @param comments
	 * @param keywords
	 * @param visibilities
	 */
	public CharacterizationFileBean saveCharacterizationFile(
			String particleName, FormFile file, String title,
			String description, String comments, String[] keywords,
			String[] visibilities) throws Exception {

		// TODO saves file to the file system
		// TODO daves file to the database
		CharacterizationFileBean fileBean = new CharacterizationFileBean();
		fileBean.setName("new test file");
		fileBean.setId("1");
		fileBean.setVisibilityGroups(visibilities);
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		String fileName = fileBean.getName();
		if (visibilities != null) {
			for (String visibility : visibilities) {
				// by default, always set visibility to NCL_PI and
				// NCL_Researcher to
				// be true
				// TODO once the files is successfully saved, use fileId instead
				// of fileName
				userService.secureObject(fileName, "NCL_PI", "R");
				userService.secureObject(fileName, "NCL_Researcher", "R");
				userService.secureObject(fileName, visibility, "R");
			}
		}
		return fileBean;
	}

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public CharacterizationFileBean getFile(String fileId) throws Exception {
		// TODO query from database.
		CharacterizationFileBean fileBean = new CharacterizationFileBean();
		fileBean.setName("existing test file");
		fileBean.setId("2");
		return fileBean;
	}

	/**
	 * Get the list of all run output files associated with a particle 
	 * @param particleName
	 * @return
	 * @throws Exception
	 */
	public List<CharacterizationFileBean> getAllRunFiles(String particleName)
			throws Exception {
		List<CharacterizationFileBean> runFiles = new ArrayList<CharacterizationFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String query = "from Run run join fetch run.outputFileCollection join run.runSampleContainerCollection runContainer where runContainer.sampleContainer.sample.name='"
					+ particleName + "'";
			List results = ida.search(query);

			for (Object obj : results) {
				Run run = (Run) obj;
				for (Object fileObj : run.getOutputFileCollection()) {
					OutputFile file = (OutputFile) fileObj;
					// active status only
					if (file.getDataStatus() == null) {
						CharacterizationFileBean fileBean = new CharacterizationFileBean();
						fileBean.setId(file.getId().toString());
						fileBean.setName(file.getFilename());
						fileBean.setPath(file.getPath());
						runFiles.add(fileBean);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem getting run files for particle: "
					+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		return runFiles;
	}
}
