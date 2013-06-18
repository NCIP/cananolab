/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houy, lethai, pansu
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class StudyAction extends BaseAnnotationAction {
	public static final String PAGE_TITLE = "pageTitle";

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		checkOpenForms(theForm, request);
		String updateStudy = (String) request.getSession().getAttribute(
				"updateStudy");
		if (updateStudy == null) {
			return mapping.findForward("inputForm");
		} else {
			return mapping.findForward("summaryEdit");
		}
	}

	private void checkOpenForms(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openPOC = false;
		if (dispatch.equals("input")
				&& browserDispatch.equals("savePointOfContact")) {
			openPOC = true;
		}
		session.setAttribute("openPOC", openPOC);
		super.checkOpenAccessForm(theForm, request);
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitStudySetup.getInstance().setStudyDropdowns(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitSampleSetup.getInstance().getAllOrganizationNames(request, user);
		InitSampleSetup.getInstance().setPOCDropdowns(request);
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

		DynaValidatorForm theForm = (DynaValidatorForm) form;

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

		// get studies by sample id

		String sampleId = request.getParameter("sampleId");

		// study service has been created earlier
		StudyService service = (StudyService) request.getSession()
				.getAttribute("studyService");
		List<StudyBean> studiesBean = service.findStudiesBySampleId(sampleId);
		if (studiesBean == null) {
			throw new NotExistException("No such study in the system");
		}
		request.setAttribute("studies", studiesBean);

		request.setAttribute("sampleStudies", true);
		request.setAttribute("updateStudy", true);
		return mapping.findForward("summaryEditPerSample");
	}

	public ActionForward summaryViewPerSample(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// get studies by sample id

		String sampleId = request.getParameter("sampleId");

		// study service has been created earlier
		StudyService service = (StudyService) request.getSession()
				.getAttribute("studyService");
		List<StudyBean> studiesBean = service.findStudiesBySampleId(sampleId);
		if (studiesBean == null) {
			throw new NotExistException("No such study in the system");
		}
		request.setAttribute("studies", studiesBean);

		request.setAttribute("sampleStudies", true);
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
