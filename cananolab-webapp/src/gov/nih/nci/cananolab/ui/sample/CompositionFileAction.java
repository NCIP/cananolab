package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class allows users to submit composition files under sample composition.
 *
 * @author pansu
 */
public class CompositionFileAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionBean comp = (CompositionBean) theForm.get("comp");
		FileBean theFile = comp.getTheFile();
		Boolean newFile = true;

		CompositionService service = this.setServicesInSession(request);
		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);

		SampleBean sampleBean = setupSample(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/" + "compositionFile";

		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		if (theFile.getDomainFile().getId() != null) {
			newFile = true;
		}
		service.saveCompositionFile(sampleBean, theFile);
		ActionMessages msgs = new ActionMessages();
		// retract from public if updating an existing public record and not
		// curator
		if (!newFile && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateSample.retractFromPublic");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		}
		ActionMessage msg = new ActionMessage("message.addCompositionFile",
				theFile.getDomainFile().getTitle());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect composition file after returning to the summary page
		request.getSession().setAttribute("tab", "4");
		return mapping.findForward("success");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionBean comp = (CompositionBean) theForm.get("comp");
		FileBean fileBean = comp.getTheFile();
		CompositionService compService = this.setServicesInSession(request);
		SampleBean sampleBean = setupSample(theForm, request);
		compService.deleteCompositionFile(sampleBean.getDomain()
				.getSampleComposition(), fileBean.getDomainFile());
		compService.removeAccesses(comp.getDomain(), fileBean
				.getDomainFile());
		ActionMessages msgs = new ActionMessages();
		// retract from public if updating an existing public record and not
		// curator
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (!user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateSample.retractFromPublic");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		}

		ActionMessage msg = new ActionMessage("message.deleteCompositionFile");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);

		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);

		return mapping.findForward("success");
	}

	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServicesInSession(request);
		CompositionBean compBean = new CompositionBean();
		theForm.set("comp", compBean);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId = super.validateId(request, "dataId");
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		FileBean fileBean = compService.findFileById(fileId);
		CompositionBean compBean = (CompositionBean) theForm.get("comp");
		compBean.setTheFile(fileBean);
		ActionForward forward = mapping.getInputForward();
		return forward;
	}

	/**
	 * Handle input request, when validation failed this handler will be called
	 * too.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServicesInSession(request);
		setupSample(theForm, request);
		this.setLookups(request);
		CompositionBean comp = (CompositionBean) theForm.get("comp");
		InitCompositionSetup.getInstance().persistCompositionFileDropdowns(
				request, comp.getTheFile());

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = comp.getTheFile();
		preserveUploadedFile(request, theFile, "compositionFile");

		return mapping.findForward("inputForm");
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
	}

	private CompositionService setServicesInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		CompositionService compService = new CompositionServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("compositionService", compService);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return compService;
	}
}
