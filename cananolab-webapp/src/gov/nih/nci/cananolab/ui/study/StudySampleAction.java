package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the study sample submission page and submits a new sample.
 *
 * @author houyh
 */
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.study.StudyService;
import gov.nih.nci.cananolab.service.study.impl.StudyServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class StudySampleAction extends BaseAnnotationAction {
	// logger
	// private static Logger logger = Logger.getLogger(StudyAction.class);

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("inputForm");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("summaryEdit");
	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServiceInSession(request);

		String studyId = request.getParameter("studyId");
		if (!StringUtils.isEmpty(studyId)) {
			theForm.set("studyId", studyId);
		} else {
			studyId = (String) request.getAttribute("studyId");
			if (studyId == null) {
				studyId = theForm.getString("studyId");
			}
		}
		// study service has been created earlier
		StudyService service = (StudyService) request.getSession()
				.getAttribute("studyService");

		StudyBean studyBean = service.findStudyById(studyId, true);
		if (studyBean == null) {
			throw new NotExistException("No such study in the system");
		}
		request.setAttribute("theStudy", studyBean);

		theForm.set("studyBean", studyBean);
		return mapping.findForward("summaryView");
	}
	
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.getInputForward();
	}
	
	public ActionForward sampleAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("sampleAdd");
	}
	private StudyService setServiceInSession(HttpServletRequest request)
		throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		StudyService studyService = new StudyServiceLocalImpl(securityService);
		request.getSession().setAttribute("studyService", studyService);
		return studyService;
	}
}
