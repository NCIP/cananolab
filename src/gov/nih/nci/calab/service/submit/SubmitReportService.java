package gov.nih.nci.calab.service.submit;

import org.apache.struts.upload.FormFile;

import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

/**
 * This class include services to submit the report to the file system and saves
 * the path to the database and sets user visibility for the report
 * 
 * @author pansu
 *
 */
public class SubmitReportService {
	public void submit(String[] particleNames, FormFile report, String title,
			String description, String[] visibilities) throws Exception {

		// TODO saves reportFile to the file system
		// TODO daves reportFile path to the database
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		String fileName=report.getFileName();
		
		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			// TODO once the files is successfully saved, use fileId instead of
			// fileName
			userService.secureObject(fileName, "NCL_PI", "R");
			userService.secureObject(fileName, "NCL_Researcher", "R");
			userService.secureObject(fileName, visibility, "R");
		}
	}

}
