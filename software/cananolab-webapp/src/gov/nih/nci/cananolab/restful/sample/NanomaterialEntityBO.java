package gov.nih.nci.cananolab.restful.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.util.PublicationUtil;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanomaterialEntityBO extends BaseAnnotationBO{
	/**
	 * Add or update the data to database
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public List<String> create(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<String> msgs = new ArrayList<String>();
		NanomaterialEntityBean entityBean = form.getNanomaterialEntity();
		this.setServicesInSession(request);
		if (!validateInputs(request, entityBean)) {
			//return mapping.getInputForward();
		}
		this.saveEntity(request, form, entityBean);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entityBean);

	//	ActionMessages msgs = new ActionMessages();
	//	ActionMessage msg = new ActionMessage("message.addNanomaterialEntity");
		msgs.add(PropertyUtil.getProperty("sample", "message.addNanomaterialEntity"));
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect nanomaterial entity after returning to the summary page
		request.getSession().setAttribute("tab", "1");
	//	return mapping.findForward("success");
		return msgs;
	}

	private Boolean validateInputs(HttpServletRequest request,
			NanomaterialEntityBean entityBean) {
		if (!validateEntity(request, entityBean)) {
			return false;
		}
		if (!validateInherentFunctionType(request, entityBean)) {
			return false;
		}
		if (!validateEntityFile(request, entityBean)) {
			return false;
		}
		return true;
	}

	public void input(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;

		NanomaterialEntityBean entityBean = form.getNanomaterialEntity();
		escapeXmlForFileUri(entityBean.getTheFile());
		entityBean.updateEmptyFieldsToNull();
		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entityBean.getTheFile();
		preserveUploadedFile(request, theFile, "nanomaterialEntity");

		this.checkOpenForms(entityBean, request);
	//	return mapping.findForward("inputForm");
	}

	private List<String> saveEntity(HttpServletRequest request,
			CompositionForm form, NanomaterialEntityBean entityBean)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		String sampleId = form.getSampleId();
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
//						entityBean.getType(), "nanomaterial entity");
				msgs.add(entityBean.getType()+" is an invalid nanomaterial entity type. It is a pre-defined composition type.");
			} else {
//				msg = new ActionMessage("errors.invalidOtherType",
//						entityBean.getType(), "nanomaterial entity");
				msgs.add(entityBean.getType()+" is an invalid nanomaterial entity type. It is a pre-defined composition type.");

				entityBean.setType(null);
			}
		//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	this.saveErrors(request, msgs);
			entityBean.setType(null);
		}
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.saveNanomaterialEntity(sampleBean, entityBean);
		// save to other samples (only when user click [Submit] button.)
		String dispatch = form.getDispatch();//("dispatch");
		if ("create".equals(dispatch)) {
			SampleBean[] otherSampleBeans = prepareCopy(request, form,
					sampleBean);
			if (otherSampleBeans != null) {
				compService.copyAndSaveNanomaterialEntity(entityBean,
						sampleBean, otherSampleBeans);
			}
		}
		// retract from public if updating an existing public record and not
		// curator
		if (!newEntity && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(sampleId, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			ActionMessages messages = new ActionMessages();
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateSample.retractFromPublic");
			msgs.add(PropertyUtil.getProperty("sample", "message.updateSample.retractFromPublic"));
		//	messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	saveMessages(request, messages);
		}
		return msgs;
	}

	private boolean validateInherentFunctionType(HttpServletRequest request,
			NanomaterialEntityBean entityBean) {

		for (ComposingElementBean composingElementBean : entityBean
				.getComposingElements()) {
			for (FunctionBean functionBean : composingElementBean
					.getInherentFunctions()) {
				if (StringUtils.isEmpty(functionBean.getType())) {
//					ActionMessages msgs = new ActionMessages();
//					ActionMessage msg = new ActionMessage("errors.required",
//							"Inherent function type");
//					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//					this.saveErrors(request, msgs);
					return false;
				} else if (!StringUtils.xssValidate(functionBean.getType())) {
//					ActionMessages msgs = new ActionMessages();
//					ActionMessage msg = new ActionMessage(
//							"function.type.invalid");
//					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//					this.saveErrors(request, msgs);
					return false;
				}
			}
		}
		return true;
	}

	// per app scan, can not easily validate in the validation.xml
	private boolean validateEntity(HttpServletRequest request,
			NanomaterialEntityBean entityBean) {
		ActionMessages msgs = new ActionMessages();
		boolean status = true;
		if (entityBean.getType().equalsIgnoreCase("biopolymer")) {
			if (entityBean.getBiopolymer().getName() != null
					&& !StringUtils.xssValidate(entityBean.getBiopolymer()
							.getName())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.biopolymer.name.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
			if (entityBean.getBiopolymer().getType() != null
					&& !StringUtils.xssValidate(entityBean.getBiopolymer()
							.getType())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.biopolymer.type.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		} else if (entityBean.getType().equalsIgnoreCase("liposome")) {
			if (entityBean.getLiposome().getPolymerName() != null
					&& !StringUtils.xssValidate(entityBean.getLiposome()
							.getPolymerName())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.liposome.polymerName.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		} else if (entityBean.getType().equalsIgnoreCase("emulsion")) {
			if (entityBean.getEmulsion().getPolymerName() != null
					&& !StringUtils.xssValidate(entityBean.getEmulsion()
							.getPolymerName())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.emulsion.polymerName.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		} else if (entityBean.getType().equalsIgnoreCase("polymer")) {
			if (entityBean.getPolymer().getInitiator() != null
					&& !StringUtils.xssValidate(entityBean.getPolymer()
							.getInitiator())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.polymer.initiator.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		} else if (entityBean.getType().equalsIgnoreCase("dendrimer")) {
			if (entityBean.getDendrimer().getBranch() != null
					&& !StringUtils.xssValidate(entityBean.getDendrimer()
							.getBranch())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.dendrimer.branch.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		} else if (entityBean.getType().equalsIgnoreCase("carbon nanotube")) {
			if (entityBean.getCarbonNanotube().getAverageLengthUnit() != null
					&& !entityBean.getCarbonNanotube().getAverageLengthUnit()
							.matches(Constants.UNIT_PATTERN)) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.carbonNanotube.averageLengthUnit.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
			if (entityBean.getCarbonNanotube().getChirality() != null
					&& !StringUtils.xssValidate(entityBean.getCarbonNanotube()
							.getChirality())) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.carbonNanotube.chirality.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
			if (entityBean.getCarbonNanotube().getDiameterUnit() != null
					&& !entityBean.getCarbonNanotube().getDiameterUnit()
							.matches(Constants.UNIT_PATTERN)) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.carbonNanotube.diameterUnit.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		} else if (entityBean.getType().equalsIgnoreCase("fullerene")) {
			if (entityBean.getFullerene().getAverageDiameterUnit() != null
					&& !entityBean.getFullerene().getAverageDiameterUnit()
							.matches(Constants.UNIT_PATTERN)) {
//				ActionMessage msg = new ActionMessage(
//						"nanomaterialEntityForm.fullerene.averageDiameterUnit.invalid");
//				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//				saveErrors(request, msgs);
				status = false;
			}
		}
		return status;
	}

	private boolean validateEntityFile(HttpServletRequest request,
			NanomaterialEntityBean entityBean) {
		//ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, filebean)) {
				return false;
			}
		}
		return true;
	}

	public void setLookups(HttpServletRequest request) throws Exception {
		InitCompositionSetup.getInstance().setNanomaterialEntityDropdowns(
				request);

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
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionForm form = new CompositionForm();
		NanomaterialEntityBean entityBean = new NanomaterialEntityBean();
		form.setNanomaterialEntity(entityBean);
		// request.getSession().removeAttribute("compositionForm");
	//	String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
		form.setOtherSamples(new String[0]);
		return CompositionUtil.reformatLocalSearchDropdownsInSession(request.getSession());


	//	return mapping.findForward("inputForm");
	}

	public void setupUpdate(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		String entityId = super.validateId(request, "dataId");
		String sampleId = form.getSampleId();
		// set up other particles with the same primary point of contact
		CompositionService compService = this.setServicesInSession(request);
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		form.setNanomaterialEntity(entityBean);
		form.setOtherSamples(new String[0]);
		this.setLookups(request);
		this.checkOpenForms(entityBean, request);
	//	return mapping.findForward("inputForm");
	}

	public void setupView(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
		String entityId = super.validateId(request, "dataId");
		CompositionService compService = this.setServicesInSession(request);

		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		request.setAttribute("nanomaterialEntity", entityBean);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "nanomaterialEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
	//	return mapping.findForward("singleSummaryView");
	}

	public void saveComposingElement(CompositionForm form, HttpServletRequest request) throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = form
				.getNanomaterialEntity();//("nanomaterialEntity");
		ComposingElementBean composingElement = entity.getTheComposingElement();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		composingElement.setupDomain(user.getLoginName());
		entity.addComposingElement(composingElement);
		// save nanomaterial entity
		if (!validateInputs(request, entity)) {
			//return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);

		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(composingElement.getDomain());

		// return to setupUpdate to retrieve the correct entity from database
		// after saving to database.
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		//return setupUpdate(mapping, form, request, response);
	}

	public void removeComposingElement(CompositionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = form
				.getNanomaterialEntity(); //("nanomaterialEntity");
		ComposingElementBean composingElement = entity.getTheComposingElement();
		// check if composing element is associated with an association
		CompositionServiceLocalImpl compService = (CompositionServiceLocalImpl) (this
				.setServicesInSession(request));
		if (!compService.checkChemicalAssociationBeforeDelete(entity
				.getDomainEntity().getSampleComposition(), composingElement
				.getDomain())) {
			throw new ChemicalAssociationViolationException(
					"The composing element is used in a chemical association.  Please delete the chemcial association first before deleting the nanomaterial entity.");
		}
		entity.removeComposingElement(composingElement);
		if (!validateInputs(request, entity)) {
			//return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);
		compService.removeAccesses(entity.getDomainEntity(),
				composingElement.getDomain());
		this.checkOpenForms(entity, request);
		//return mapping.findForward("inputForm");
	}

	public void saveFile(CompositionForm form,
			HttpServletRequest request)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = form
				.getNanomaterialEntity(); //("nanomaterialEntity");
		FileBean theFile = entity.getTheFile();
		this.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(form.getSampleId(), request); //(theForm, request);
		// setup domainFile uri for fileBean
		String internalUriPath = Constants.FOLDER_PARTICLE + '/'
				+ sampleBean.getDomain().getName() + '/' + "nanomaterialEntity";
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		entity.addFile(theFile);

		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);

		// save nanomaterial entity to save file because inverse="false"
		if (!validateInputs(request, entity)) {
			//return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
//		return setupUpdate(mapping, form, request, response);
	}

	public void removeFile(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = form
				.getNanomaterialEntity(); //("nanomaterialEntity");
		FileBean theFile = entity.getTheFile();
		entity.removeFile(theFile);
		entity.setTheFile(new FileBean());
		// save nanomaterial entity
		if (!validateInputs(request, entity)) {
	//		return mapping.getInputForward();
		}
		this.saveEntity(request, form, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());
		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);
//		return mapping.findForward("inputForm");
	}

	public List<String> delete(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<String> msgs = new ArrayList<String>();
		CompositionService compositionService = this
				.setServicesInSession(request);
		NanomaterialEntityBean entityBean = form
				.getNanomaterialEntity(); //("nanomaterialEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		compositionService.deleteNanomaterialEntity(entityBean
				.getDomainEntity());
		compositionService.removeAccesses(entityBean.getDomainEntity());
//		ActionMessages msgs = new ActionMessages();
//		ActionMessage msg = new ActionMessage(
//				"message.deleteNanomaterialEntity");
		msgs.add(PropertyUtil.getProperty("sample", "message.deleteNanomaterialEntity"));
		// save action messages in the session so composition.do know about
		// them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		//return mapping.findForward("success");
		return msgs;
	}

	private void checkOpenForms(NanomaterialEntityBean entity,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
//		Boolean openFile = false, openComposingElement = false;
//		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
//			openFile = true;
//		}
//		session.setAttribute("openFile", openFile);
//		if (dispatch.equals("input")
//				&& browserDispatch.equals("saveComposingElement")
//				|| ((dispatch.equals("setupNew") || dispatch
//						.equals("setupUpdate")) && entity
//						.getComposingElements().isEmpty())) {
//			openComposingElement = true;
//		}
//		session.setAttribute("openComposingElement", openComposingElement);

		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entity);
		/**
		 * other nanomaterial entity types are not stored in the lookup are
		 * retrieved through reflection only after saving to the database. Need
		 * to update session variable before saving to the database
		 */
		// Nanomaterial Entity Type
		String entityType = entity.getType();
		setOtherValueOption(request, entityType, "nanomaterialEntityTypes");
		// function type
		String functionType = entity.getTheComposingElement().getTheFunction()
				.getType();
		setOtherValueOption(request, functionType, "functionTypes");

		String detailPage = null;
		if (entity.isWithProperties()) {
			if (!StringUtils.isEmpty(entity.getType())) {
				detailPage = InitCompositionSetup.getInstance().getDetailPage(
						entity.getType(), "nanomaterialEntity");
			}
			request.setAttribute("entityDetailPage", detailPage);
		}
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

	public String download(String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CompositionService service = (CompositionService) (request.getSession()
				.getAttribute("compositionService"));
		return downloadFile(service, fileId, request, response);
	}
}
