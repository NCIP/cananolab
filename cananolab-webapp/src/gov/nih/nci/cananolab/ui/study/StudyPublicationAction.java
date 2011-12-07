package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houyh, lethai
 */
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class StudyPublicationAction extends BaseAnnotationAction {
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
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		String studyId = request.getSession().getAttribute("studyId").toString();
		
		if (!StringUtils.isEmpty(studyId)) {
			theForm.set("studyId", studyId);
		} else {
			studyId = (String) request.getAttribute("studyId");
			if (studyId == null) {
				studyId = request.getSession().getAttribute("studyId").toString();
			}
		}
		
		// publication service has been created earlier
		PublicationService service = this.setServiceInSession(request);
		
		List<PublicationBean> pubsBean = service.findPublicationsByStudyId(studyId);
		if (pubsBean == null) {
			throw new NotExistException("No such publication for the study in the system");
		}
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				pubsBean);

		// Save sample & publication bean in session for printing/exporting.
		request.getSession().setAttribute("studyPublicationSummaryView", summaryView);
		request.setAttribute("studyPublications", pubsBean);
		return mapping.findForward("summaryEdit");
	}
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		String studyId = request.getSession().getAttribute("studyId").toString();
		
		if (!StringUtils.isEmpty(studyId)) {
			theForm.set("studyId", studyId);
		} else {
			studyId = (String) request.getAttribute("studyId");
			if (studyId == null) {
				studyId = request.getSession().getAttribute("studyId").toString();
			}
		}
		
		// publication service has been created earlier
		PublicationService service = this.setServiceInSession(request);
		
		List<PublicationBean> pubsBean = service.findPublicationsByStudyId(studyId);
		if (pubsBean == null) {
			throw new NotExistException("No such publication for the study in the system");
		}
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				pubsBean);

		// Save sample & publication bean in session for printing/exporting.
		request.getSession().setAttribute("studyPublicationSummaryView", summaryView);
		request.setAttribute("studyPublications", pubsBean);
		return mapping.findForward("summaryView");
	}
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Submit Study");
		return mapping.getInputForward();
	}
	
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Add Study Publication");
		return mapping.findForward("add");
	}
	
	private PublicationService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		PublicationService service = (PublicationService) request.getSession()
				.getAttribute("publicationService");
		if (service == null) {
			service = new PublicationServiceLocalImpl(securityService);
		}
		request.getSession().setAttribute("publicationService", service);
		return service;
	}
}
