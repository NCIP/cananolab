package gov.nih.nci.calab.service.submit;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.calab.dto.particle.DendrimerBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

/**
 * This class includes service calls invovled in creating nanoparticles and
 * adding properties, functions and characterizations for nanoparticles.
 * 
 * @author pansu
 * 
 */
public class SubmitNanoparticleService {
	public void createNanoparticle(String particleType, String particleName,
			String[] keywords, String[] visibilities) throws Exception {

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		http: // www.aahealth.org/news.asp?id=145
		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			userService.secureObject(particleName, "NCL_PI", "R");
			userService.secureObject(particleName, "NCL_Researcher", "R");
			userService.secureObject(particleName, visibility, "R");
		}
		// TODO add database code to save the nanoparticle
	}

	public void addParticleProperties(String particleType, ParticleBean particle) {
		if (particleType.equalsIgnoreCase("dendrimer")) {
			DendrimerBean dendrimer = (DendrimerBean) particle;
			// TODO update the dendrimer object in the db.
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
