package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class includes service calls involved in creating nanoparticles and
 * adding properties, functions and characterizations for nanoparticles.
 * 
 * @author pansu
 * 
 */
public class SubmitNanoparticleService {
	public void createNanoparticle(String particleType, String particleName,
			String[] keywords, String[] visibilities) throws Exception {
		
		LookupService lookupService=new LookupService();
		Map<String, String>type2Category=lookupService.getParticleTypeToParticleCategory();
		
		// save nanoparticle to the database
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
//		try {
//			ida.open();
//			//check if particle already exists in the database			
//			List results = ida.search("from Nanoparticle where particleName="+particleName);
//			if (results.size()>0) {
//				throw new DuplicateEntriesException("Nanoparticle alreay exists in the database");
//			}
//			
//			Nanoparticle particle=new Nanoparticle();			
//			particle.setParticleCategory(type2Category.get(particleType.toLowerCase()));			
//			ida.createObject(particle);
//						
//		} catch (Exception e) {
//			e.printStackTrace();
//			ida.rollback();
//			throw e;
//		} finally {
//			ida.close();
//		}
		
		// set visibilities for the nanoparticle
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			userService.secureObject(particleName, "NCL_PI", "R");
			userService.secureObject(particleName, "NCL_Researcher", "R");
			userService.secureObject(particleName, visibility, "R");
		}

	}

	public void addParticleComposition(String particleType, CharacterizationBean particle) {
		//TODO add database code
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
