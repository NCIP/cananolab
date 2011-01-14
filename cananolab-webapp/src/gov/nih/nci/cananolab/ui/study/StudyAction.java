package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houyh, lethai
 */
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.service.study.StudyService;
import gov.nih.nci.cananolab.service.study.impl.StudyServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class StudyAction extends BaseAnnotationAction {
	// logger
	// private static Logger logger = Logger.getLogger(StudyAction.class);
	public static final String PAGE_TITLE = "pageTitle";

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("inputForm");
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitStudySetup.getInstance().setStudyDropdowns(request);
		UserBean user=(UserBean)request.getSession().getAttribute("user");
		InitSampleSetup.getInstance().getAllOrganizationNames(request, user);
		InitSampleSetup.getInstance().setPOCDropdowns(request);
		request.setAttribute(PAGE_TITLE, "Submit New Study");
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute(PAGE_TITLE, "Update Study");
		return mapping.findForward("studyEdit");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("updateStudy", true);
		request.setAttribute("showDelete", true);
		request.setAttribute("deleteButtonName", "Delete");
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

	public ActionForward summaryEditPerSample(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("updateStudy", true);
		return mapping.findForward("summaryEditPerSample");
	}

	public ActionForward summaryViewPerSample(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("summaryViewPerSample");
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
