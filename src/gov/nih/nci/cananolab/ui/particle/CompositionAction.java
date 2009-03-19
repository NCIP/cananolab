package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.service.particle.CompositionService;
import gov.nih.nci.cananolab.service.particle.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class CompositionAction extends BaseAnnotationAction {
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		//setupSample(theForm, request, location);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String sampleId = request.getParameter("sampleId");
		CompositionService compService = null;
		if (location.equals("local")) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			//TODO update grid service
//			compService = new CompositionServiceRemoteImpl(
//					serviceUrl);
		}
		CompositionBean compositionBean = compService.findCompositionBySampleId(sampleId);
		theForm.set("comp", compositionBean);
		return mapping.findForward("summaryView");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		//setupSample(theForm, request, location);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String sampleId = request.getParameter("sampleId");
		CompositionService compService = null;
		if (location.equals("local")) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			//TODO update grid service
//			compService = new CompositionServiceRemoteImpl(
//					serviceUrl);
		}
		CompositionBean compositionBean = compService.findCompositionBySampleId(sampleId);
		theForm.set("comp", compositionBean);
		return mapping.findForward("summaryEdit");
	}
}
