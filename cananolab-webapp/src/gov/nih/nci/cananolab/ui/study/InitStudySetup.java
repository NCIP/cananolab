package gov.nih.nci.cananolab.ui.study;

import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.study.StudyService;
import gov.nih.nci.cananolab.service.study.impl.StudyServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for canano study forms.
 *
 * @author pansu
 *
 */
public class InitStudySetup {
	private InitStudySetup() {
	}

	public static InitStudySetup getInstance() {
		return new InitStudySetup();
	}

	public void setStudyDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"studyTypes", "study", "type",
				"otherType", true);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"studyDesignTypes", "study", "designType",
				"otherType", true);
	}

	private StudyService getServiceFromSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		if (request.getSession().getAttribute("studyService") != null) {
			return (StudyService) request.getSession().getAttribute(
					"studyService");
		} else {
			return new StudyServiceLocalImpl(securityService);
		}
	}
}
