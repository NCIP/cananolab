package gov.nih.nci.cananolab.restful.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class FunctionalizingEntityBO extends BaseAnnotationBO{
	public List<String> create(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entityBean = form
				.getFunctionalizingEntity();
		List<String> msgs = new ArrayList<String>();
		msgs = validateInputs(request, entityBean);
		if (msgs.size()>0) {
			return msgs;
		}
		this.saveEntity(request, form, entityBean);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entityBean);

//		msgs.add(PropertyUtil.getProperty("sample", "message.addFunctionalizingEntity"));
//		// save action messages in the session so composition.do know about them
//		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect functionalizing entity after returning to the summary
		// page
		request.getSession().setAttribute("tab", "2");
//		return mapping.findForward("success");
		return msgs;
	}

	private List<String> validateTargets(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		List<String> msgs = new ArrayList<String>();
		for (FunctionBean funcBean : entityBean.getFunctions()) {
			if ("TargetingFunction".equals(funcBean.getClassName())) {
				for (TargetBean targetBean : funcBean.getTargets()) {
					if (StringUtils.isEmpty(targetBean.getType())) {
//						ActionMessages msgs = new ActionMessages();
//						ActionMessage msg = new ActionMessage(
//								"errors.required", "Target type");
//						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//						this.saveErrors(request, msgs);
//						return false;
					} else if (!StringUtils.xssValidate(targetBean.getType())) {
//						ActionMessages msgs = new ActionMessages();
//						ActionMessage msg = new ActionMessage(
//								"functionalizingEntity.target.type.invalid");
//						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//						this.saveErrors(request, msgs);
//						return false;
					}
				}
			}
		}
		return msgs;
	}

	private List<String> validateEntityFile(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		List<String> msg = new ArrayList<String>();
		for (FileBean filebean : entityBean.getFiles()) {
			msg = validateFileBean(request, filebean);
			if (msg.size()>0) {
				return msg;
			}
		}
		return msg;
	}

	private List<String> saveEntity(HttpServletRequest request,
			CompositionForm theForm, FunctionalizingEntityBean entityBean)
			throws Exception {
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		String sampleId = theForm.getSampleId();
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(sampleId, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Boolean newEntity = true;
		try {
			entityBean.setupDomainEntity(user.getLoginName());
			if (entityBean.getDomainEntity().getId() != null) {
				newEntity = false;
			}
		} catch (ClassCastException ex) {
			
			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
//				msg = new ActionMessage("errors.invalidOtherType",
//						ex.getMessage(), "Function");
				msgs.add(PropertyUtil.getProperty("sample", "errors.invalidOtherType"));
			} else {
//				msg = new ActionMessage("errors.invalidOtherType",
//						entityBean.getType(), "functionalizing entity");
				msgs.add(PropertyUtil.getProperty("sample", "errors.invalidOtherType"));

				entityBean.setType(null);
			}
		//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	this.saveErrors(request, msgs);
			return msgs;
		}

		compService.saveFunctionalizingEntity(sampleBean, entityBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newEntity && !user.isCurator() && sampleBean.getPublicStatus()) {
//			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
//					.toString(), sampleBean.getDomain().getName(), "sample");
//			ActionMessages messages = new ActionMessages();
//			ActionMessage msg = null;
//			msg = new ActionMessage("message.updateSample.retractFromPublic");
//			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, messages);
		}
		// save to other samples (only when user click [Submit] button.)
//		String dispatch = (String) theForm.get("dispatch");
//		if ("create".equals(dispatch)) {
//			SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
//					sampleBean);
//			if (otherSampleBeans != null) {
//				compService.copyAndSaveFunctionalizingEntity(entityBean,
//						sampleBean, otherSampleBeans);
//			}
//		}
		return msgs;
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
	public Map<String, Object> setupNew(String sampleId,
			HttpServletRequest request)
			throws Exception {
		FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean();
//		form.setFunctionalizingEntity(entityBean);
//		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		request.getSession().setAttribute("onloadJavascript",
				"setEntityInclude('feType', 'functionalizingEntity')");
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
//		form.setOtherSamples(new String[0]);

		return CompositionUtil.reformatLocalSearchDropdownsInSessionForFunctionalizingEntity(request.getSession());
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitCompositionSetup.getInstance().setFunctionalizingEntityDropdowns(
				request);
	}

	public void setupView(CompositionForm form,
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
	//	return mapping.findForward("singleSummaryView");
	}

	public FunctionalizingEntityBean setupUpdate(String sampleId, String dataId,
			HttpServletRequest request)
			throws Exception {
		CompositionService compService = this.setServicesInSession(request);
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		String entityId = super.validateId(request, "dataId");
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId);
		this.setLookups(request);
		// clear copy to otherSamples
	//	form.setOtherSamples(new String[0]);
		checkOpenForms(entityBean, request);
//		return mapping.findForward("inputForm");
		return entityBean;
	}

	public void saveFunction(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = form
				.getFunctionalizingEntity();
		List<String> msgs = new ArrayList<String>();
		FunctionBean function = entity.getTheFunction();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		function.setupDomainFunction(user.getLoginName(), 0);
		entity.addFunction(function);
		msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
		//	return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);

		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(function.getDomainFunction());

		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		// return to setupUpdate to get the correct entity from database
	//	return setupUpdate(mapping, form, request, response);
	}

	public void removeFunction(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		FunctionalizingEntityBean entity = form
				.getFunctionalizingEntity();
		FunctionBean function = entity.getTheFunction();
		entity.removeFunction(function);
		msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
			//return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity(),
				function.getDomainFunction());
		checkOpenForms(entity, request);
	//	return mapping.findForward("inputForm");
	}

	public void saveFile(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FunctionalizingEntityBean entity = form
				.getFunctionalizingEntity();
		this.setServicesInSession(request);
		FileBean theFile = entity.getTheFile();
		String sampleId = form.getSampleId();
		List<String> msgs = new ArrayList<String>();
		SampleBean sampleBean = setupSampleById(sampleId, request);
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
		msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
		//	return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);

	//	return mapping.findForward("inputForm");
	}

	public void removeFile(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FunctionalizingEntityBean entity = form
				.getFunctionalizingEntity();
		FileBean theFile = entity.getTheFile();
		List<String> msgs = new ArrayList<String>();
		entity.removeFile(theFile);
		request.setAttribute("anchor", "file");
		// save the functionalizing entity;
		msgs = validateInputs(request, entity);
		if (msgs.size()>0) {
			//return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		checkOpenForms(entity, request);
	//	return mapping.findForward("inputForm");
	}

	// per app scan, can not easily validate in the validation.xml
	private List<String> validateEntity(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		List<String> msgs = new ArrayList<String>();
		boolean status = true;
		if (entityBean.getType().equalsIgnoreCase("biopolymer")) {
			if (entityBean.getBiopolymer().getType() != null
					&& !StringUtils.xssValidate(entityBean.getBiopolymer()
							.getType())) {
//				ActionMessage msg = new ActionMessage(
//						"functionalizingEntity.biopolymer.type.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
//				status = false;
			}
		}
		if (entityBean.getType().equalsIgnoreCase("antibody")) {
			if (entityBean.getAntibody().getType() != null
					&& !StringUtils.xssValidate(entityBean.getAntibody()
							.getType())) {
//				ActionMessage msg = new ActionMessage(
//						"functionalizingEntity.antibody.type.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
//				status = false;
			}
			if (entityBean.getAntibody().getIsotype() != null
					&& !StringUtils.xssValidate(entityBean.getAntibody()
							.getIsotype())) {
//				ActionMessage msg = new ActionMessage(
//						"functionalizingEntity.antibody.isotype.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
//				status = false;
			}
		}
		if (entityBean.getType().equalsIgnoreCase("small molecule")) {
			if (entityBean.getSmallMolecule().getAlternateName() != null
					&& !StringUtils.xssValidate(entityBean.getSmallMolecule()
							.getAlternateName())) {
//				ActionMessage msg = new ActionMessage(
//						"functionalizingEntity.smallMolecule.alternateName.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
//				status = false;
			}
		}
		return msgs;
	}

	private List<String> validateInputs(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) {
		List<String> msgs = new ArrayList<String>();
		msgs = validateEntity(request, entityBean);
		if (msgs.size()>0) {
			return msgs;
		}
		msgs = validateTargets(request, entityBean);
		if (msgs.size()>0) {
			return msgs;
		}
		msgs = validateEntityFile(request, entityBean);
		if (msgs.size()>0) {
			return msgs;
		}
		return msgs;
	}

	public void input(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		FunctionalizingEntityBean entity = form
				.getFunctionalizingEntity();
		escapeXmlForFileUri(entity.getTheFile());
		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entity.getTheFile();
		preserveUploadedFile(request, theFile, "functionalizingEntity");

		this.checkOpenForms(entity, request);

//		return mapping.findForward("inputForm");
	}

	public List<String> delete(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		CompositionService compositionService = this
				.setServicesInSession(request);
		FunctionalizingEntityBean entityBean = form
				.getFunctionalizingEntity();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		compositionService.deleteFunctionalizingEntity(entityBean
				.getDomainEntity());
		compositionService.removeAccesses(entityBean.getDomainEntity());

//		ActionMessage msg = new ActionMessage(
//				"message.deleteFunctionalizingEntity");
//		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about
		// them
//		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		return msgs;
	}

	private void checkOpenForms(FunctionalizingEntityBean entity,
			HttpServletRequest request) throws Exception {
//		String dispatch = request.getParameter("dispatch");
//		String browserDispatch = getBrowserDispatch(request);
//		HttpSession session = request.getSession();
//		Boolean openFile = false, openFunction = false;
//		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
//			openFile = true;
//		}
//		session.setAttribute("openFile", openFile);
//		if (dispatch.equals("input")
//				&& browserDispatch.equals("saveFunction")
//				|| ((dispatch.equals("setupNew") || dispatch
//						.equals("setupUpdate")) && entity.getFunctions()
//						.isEmpty())) {
//			openFunction = true;
//		}
//		session.setAttribute("openFunction", openFunction);

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
		if (entity.isWithProperties()) {
			if (!StringUtils.isEmpty(entity.getType())) {
				detailPage = InitCompositionSetup.getInstance().getDetailPage(
						entity.getType(), "functionalizingEntity");
			}
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
