package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the submit a new study page and submits a new study.
 *
 * @author houyh
 */
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
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

public class StudyProtocolAction extends BaseAnnotationAction {
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
		ProtocolService service = this.setServiceInSession(request);
		
		List<ProtocolBean> protocolBeans = service.findProtocolsByStudyId(studyId);
		if (protocolBeans == null) {
			throw new NotExistException("No such protocol for the study in the system");
		}
		/*PubSummaryViewBean summaryView = new PublicationSummaryViewBean(
				pubsBean);*/

		// Save sample & publication bean in session for printing/exporting.
		//request.getSession().setAttribute("studyProtocolSummaryView", summaryView);
		request.setAttribute("studyProtocols", protocolBeans);
		return mapping.findForward("summaryView");
	}
	
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Submit Study Protocol");
		return mapping.getInputForward();
	}
	
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("pageTitle", "Add Study Protocol");
		return mapping.findForward("add");
	}

	private ProtocolService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		ProtocolService service = (ProtocolService) request.getSession()
				.getAttribute("protocolService");
		if (service == null) {
			service = new ProtocolServiceLocalImpl(securityService);
		}
		request.getSession().setAttribute("protocolService", service);
		return service;
	}
}
