package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit functionalizing entity data under sample composition.
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class FunctionalizingEntityAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		if (!validateInputs(request, entityBean)) {
			return mapping.getInputForward();
		}
		this.saveEntity(request, theForm, entityBean);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entityBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect functionalizing entity after returning to the summary
		// page
		request.getSession().setAttribute("tab", "2");
		return mapping.findForward("success");
	}

	private boolean validateTargets(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		for (FunctionBean funcBean : entityBean.getFunctions()) {
			if ("TargetingFunction".equals(funcBean.getClassName())) {
				for (TargetBean targetBean : funcBean.getTargets()) {
					if (targetBean.getType() == null
							|| targetBean.getType().trim().length() == 0) {
						ActionMessages msgs = new ActionMessages();
						ActionMessage msg = new ActionMessage(
								"errors.required", "Target type");
						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
						this.saveErrors(request, msgs);
						return false;
					}

				}
			}
		}
		return true;
	}

	private boolean validateEntityFile(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, msgs, filebean)) {
				return false;
			}
		}
		return true;
	}

	private void saveEntity(HttpServletRequest request,
			DynaValidatorForm theForm, FunctionalizingEntityBean entityBean)
			throws Exception {
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		SampleBean sampleBean = setupSample(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Boolean newEntity = true;
		try {
			entityBean.setupDomainEntity(user.getLoginName());
			if (entityBean.getDomainEntity().getId() != null) {
				newEntity = false;
			}
		} catch (ClassCastException ex) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = null;
			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msg = new ActionMessage("errors.invalidOtherType", ex
						.getMessage(), "Function");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "functionalizing entity");
				entityBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
		}

		compService.saveFunctionalizingEntity(sampleBean, entityBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newEntity && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			ActionMessages messages = new ActionMessages();
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateSample.retractFromPublic");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		}
		// save to other samples (only when user click [Submit] button.)
		String dispatch = (String) theForm.get("dispatch");
		if ("create".equals(dispatch)) {
			SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
					sampleBean);
			if (otherSampleBeans != null) {
				compService.copyAndSaveFunctionalizingEntity(entityBean,
						sampleBean, otherSampleBeans);
			}
		}
	}

	/**
	 * Set up the input form for adding new nanomaterial entity
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean();
		theForm.set("functionalizingEntity", entityBean);
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		request.getSession().setAttribute("onloadJavascript",
				"setEntityInclude('feType', 'functionalizingEntity');");
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);

		return mapping.findForward("inputForm");
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitCompositionSetup.getInstance().setFunctionalizingEntityDropdowns(
				request);
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entityId = super.validateId(request, "dataId");
		CompositionService compService = this.setServicesInSession(request);

		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId);
		request.setAttribute("functionalizingEntity", entityBean);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "functionalizingEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
		return mapping.findForward("singleSummaryView");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleId = theForm.getString("sampleId");
		CompositionService compService = this.setServicesInSession(request);
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		String entityId = super.validateId(request, "dataId");
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId);
		theForm.set("functionalizingEntity", entityBean);
		this.setLookups(request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);
		checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward saveFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function = entity.getTheFunction();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		function.setupDomainFunction(user.getLoginName(), 0);
		entity.addFunction(function);
		if (!validateInputs(request, entity)) {
			return mapping.getInputForward();
		}
		this.saveEntity(request, theForm, entity);

		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(function.getDomainFunction());

		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		// return to setupUpdate to get the correct entity from database
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward removeFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function = entity.getTheFunction();
		entity.removeFunction(function);
		if (!validateInputs(request, entity)) {
			return mapping.getInputForward();
		}
		this.saveEntity(request, theForm, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity(), function
				.getDomainFunction());
		checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward saveFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		this.setServicesInSession(request);
		FileBean theFile = entity.getTheFile();
		SampleBean sampleBean = setupSample(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/"
				+ "functionalizingEntity";
		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		entity.addFile(theFile);

		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);

		// save the functionalizing entity
		if (!validateInputs(request, entity)) {
			return mapping.getInputForward();
		}
		this.saveEntity(request, theForm, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);

		return mapping.findForward("inputForm");
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FileBean theFile = entity.getTheFile();
		entity.removeFile(theFile);
		request.setAttribute("anchor", "file");
		// save the functionalizing entity
		if (!validateInputs(request, entity)) {
			return mapping.getInputForward();
		}
		this.saveEntity(request, theForm, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	// per app scan, can not easily validate in the validation.xml
	private boolean validateEntity(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		ActionMessages msgs = new ActionMessages();
		boolean status = true;
		if (entityBean.getType().equalsIgnoreCase("biopolymer")) {
			if (entityBean.getBiopolymer().getType() != null
					&& !entityBean.getBiopolymer().getType().matches(
							Constants.TEXTFIELD_WHITELIST_PATTERN)) {
				ActionMessage msg = new ActionMessage(
						"functionalizingEntity.biopolymer.type.invalid");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveErrors(request, msgs);
				status = false;
			}
		}
		if (entityBean.getType().equalsIgnoreCase("antibody")) {
			if (entityBean.getAntibody().getType() != null
					&& !entityBean.getAntibody().getType().matches(
							Constants.TEXTFIELD_WHITELIST_PATTERN)) {
				ActionMessage msg = new ActionMessage(
						"functionalizingEntity.antibody.type.invalid");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveErrors(request, msgs);
				status = false;
			}
			if (entityBean.getAntibody().getIsotype() != null
					&& !entityBean.getAntibody().getIsotype().matches(
							Constants.TEXTFIELD_WHITELIST_PATTERN)) {
				ActionMessage msg = new ActionMessage(
						"functionalizingEntity.antibody.isotype.invalid");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveErrors(request, msgs);
				status = false;
			}
		}
		if (entityBean.getType().equalsIgnoreCase("small molecule")) {
			if (entityBean.getSmallMolecule().getAlternateName() != null
					&& !entityBean.getSmallMolecule().getAlternateName()
							.matches(Constants.TEXTFIELD_WHITELIST_PATTERN)) {
				ActionMessage msg = new ActionMessage(
						"functionalizingEntity.smallMolecule.alternateName.invalid");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveErrors(request, msgs);
				status = false;
			}
		}
		return status;
	}

	private Boolean validateInputs(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		if (!validateEntity(request, entityBean)) {
			return false;
		}
		if (!validateTargets(request, entityBean)) {
			return false;
		}
		if (!validateEntityFile(request, entityBean)) {
			return false;
		}
		return true;
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entity.getTheFile();
		preserveUploadedFile(request, theFile, "functionalizingEntity");

		this.checkOpenForms(entity, request);

		return mapping.findForward("inputForm");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionService compositionService = this
				.setServicesInSession(request);
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		compositionService.deleteFunctionalizingEntity(entityBean
				.getDomainEntity());
		compositionService.removeAccesses(entityBean.getDomainEntity());

		ActionMessage msg = new ActionMessage(
				"message.deleteFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about
		// them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		return mapping.findForward("success");
	}

	private void checkOpenForms(FunctionalizingEntityBean entity,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openFile = false, openFunction = false;
		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
			openFile = true;
		}
		session.setAttribute("openFile", openFile);
		if (dispatch.equals("input")
				&& browserDispatch.equals("saveFunction")
				|| ((dispatch.equals("setupNew") || dispatch
						.equals("setupUpdate")) && entity.getFunctions()
						.isEmpty())) {
			openFunction = true;
		}
		session.setAttribute("openFunction", openFunction);

		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entity);

		/**
		 * If user entered customized value selecting [other] on previous page,
		 * we should show and highlight the entered value on the edit page.
		 */
		// Functional Entity Type
		String entityType = entity.getType();
		setOtherValueOption(request, entityType, "functionalizingEntityTypes");

		// Functional Entity Function Type
		String functionType = entity.getTheFunction().getType();
		setOtherValueOption(request, functionType, "functionTypes");
		String detailPage = null;
		if (!StringUtils.isEmpty(entity.getType())) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entity.getType(), "functionalizingEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
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
