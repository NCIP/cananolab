package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit nanomaterial entity data under sample composition.
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.exception.ChemicalAssociationViolationException;
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

public class NanomaterialEntityAction extends BaseAnnotationAction {

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
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		if (!validateInherentFunctionType(request, entityBean)) {
			return mapping.getInputForward();
		}
		if (!validateEntityFile(request, entityBean)) {
			return mapping.getInputForward();
		}
		// Copy "polymerized" property from entity bean to mapping bean.
		this.copyIsPolymerized(entityBean);
		this.setServicesInSession(request);
		this.saveEntity(request, theForm, entityBean);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entityBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addNanomaterialEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect nanomaterial entity after returning to the summary page
		request.getSession().setAttribute("tab", "1");
		return mapping.findForward("success");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = entityBean.getTheFile();
		preserveUploadedFile(request, theFile, "nanomaterialEntity");

		// set pubChemId and value for composing element to be null
		// if they were default to zero from the form
		ComposingElementBean ce = entityBean.getTheComposingElement();
		if (ce.getDomain().getPubChemId() != null
				&& ce.getDomain().getPubChemId() == 0) {
			ce.getDomain().setPubChemId(null);
		}
		if (ce.getDomain().getValue() != null && ce.getDomain().getValue() == 0) {
			ce.getDomain().setValue(null);
		}
		this.checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	private void saveEntity(HttpServletRequest request,
			DynaValidatorForm theForm, NanomaterialEntityBean entityBean)
			throws Exception {
		SampleBean sampleBean = setupSample(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Boolean newEntity=true;
		try {
			entityBean.setupDomainEntity(user.getLoginName());
			if (entityBean.getDomainEntity().getId()!=null) {
				newEntity=false;
			}
		} catch (ClassCastException ex) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = null;
			if (!StringUtils.isEmpty(ex.getMessage())
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "nanomaterial entity");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "nanomaterial entity");
				entityBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			entityBean.setType(null);
		}
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.saveNanomaterialEntity(sampleBean, entityBean);
		// save to other samples (only when user click [Submit] button.)
		String dispatch = (String) theForm.get("dispatch");
		if ("create".equals(dispatch)) {
			SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
					sampleBean);
			if (otherSampleBeans != null) {
				compService.copyAndSaveNanomaterialEntity(entityBean,
						sampleBean, otherSampleBeans);
			}
		}
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
	}

	private boolean validateInherentFunctionType(HttpServletRequest request,
			NanomaterialEntityBean entityBean) throws Exception {

		for (ComposingElementBean composingElementBean : entityBean
				.getComposingElements()) {
			for (FunctionBean functionBean : composingElementBean
					.getInherentFunctions()) {
				if (functionBean.getType() == null
						|| functionBean.getType().trim().length() == 0) {

					ActionMessages msgs = new ActionMessages();
					ActionMessage msg = new ActionMessage("errors.required",
							"Inherent function type");
					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
					this.saveErrors(request, msgs);

					return false;
				}
			}
		}
		return true;
	}

	private boolean validateEntityFile(HttpServletRequest request,
			NanomaterialEntityBean entityBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, msgs, filebean)) {
				return false;
			}
		}
		return true;
	}

	private void setLookups(HttpServletRequest request) throws Exception {
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
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entityBean = new NanomaterialEntityBean();
		theForm.set("nanomaterialEntity", entityBean);
		// request.getSession().removeAttribute("compositionForm");
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		this.checkOpenForms(entityBean, request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);

		return mapping.findForward("inputForm");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String entityId = super.validateId(request, "dataId");
		String sampleId = theForm.getString("sampleId");
		// set up other particles with the same primary point of contact
		CompositionService compService = this.setServicesInSession(request);
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		theForm.set("nanomaterialEntity", entityBean);
		theForm.set("otherSamples", new String[0]);
		this.setLookups(request);
		this.setupIsPolymerized(entityBean);
		this.checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
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
		return mapping.findForward("singleSummaryView");
	}

	public ActionForward saveComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		ComposingElementBean composingElement = entity.getTheComposingElement();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		composingElement.setupDomain(user.getLoginName());
		entity.addComposingElement(composingElement);
		// save nanomaterial entity
		this.saveEntity(request, theForm, entity);

		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(composingElement.getDomain());

		// return to setupUpdate to retrieve the correct entity from database
		// after saving to database.
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward removeComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		ComposingElementBean composingElement = entity.getTheComposingElement();
		// check if composing element is associated with an association
		CompositionServiceLocalImpl compService = (CompositionServiceLocalImpl) (this
				.setServicesInSession(request));
		if (!compService.checkChemicalAssociationBeforeDelete(entity
				.getDomainEntity())) {
			throw new ChemicalAssociationViolationException(
					"The composing element is used in a chemical association.  Please delete the chemcial association first before deleting the nanomaterial entity.");
		}
		entity.removeComposingElement(composingElement);
		this.saveEntity(request, theForm, entity);
		compService.removeAccesses(entity.getDomainEntity(),
				composingElement.getDomain());
		this.checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward saveFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		FileBean theFile = entity.getTheFile();
		this.setServicesInSession(request);
		SampleBean sampleBean = setupSample(theForm, request);
		// setup domainFile uri for fileBean
		String internalUriPath = Constants.FOLDER_PARTICLE + '/'
				+ sampleBean.getDomain().getName() + '/' + "nanomaterialEntity";
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		entity.addFile(theFile);

		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);

		// save nanomaterial entity to save file because inverse="false"
		this.saveEntity(request, theForm, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.assignAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());

		request.setAttribute("anchor", "file");
		request.setAttribute("dataId", entity.getDomainEntity().getId()
				.toString());
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		FileBean theFile = entity.getTheFile();
		entity.removeFile(theFile);
		entity.setTheFile(new FileBean());
		// save nanomaterial entity
		this.saveEntity(request, theForm, entity);
		// comp service has already been created
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		compService.removeAccesses(entity.getDomainEntity()
				.getSampleComposition(), theFile.getDomainFile());
		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionService compositionService = this
				.setServicesInSession(request);
		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		entityBean.setupDomainEntity(user.getLoginName());
		compositionService.deleteNanomaterialEntity(entityBean
				.getDomainEntity());
		compositionService.removeAccesses(entityBean.getDomainEntity());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteNanomaterialEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about
		// them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		return mapping.findForward("success");
	}

	private void checkOpenForms(NanomaterialEntityBean entity,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openFile = false, openComposingElement = false;
		if (dispatch.equals("input") && browserDispatch.equals("saveFile")) {
			openFile = true;
		}
		session.setAttribute("openFile", openFile);
		if (dispatch.equals("input")
				&& browserDispatch.equals("saveComposingElement")
				|| ((dispatch.equals("setupNew") || dispatch
						.equals("setupUpdate")) && entity
						.getComposingElements().isEmpty())) {
			openComposingElement = true;
		}
		session.setAttribute("openComposingElement", openComposingElement);

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
		if (!StringUtils.isEmpty(entity.getType())) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entity.getType(), "nanomaterialEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
	}

	/**
	 * Copy "polymerized" property from entityBean to Emulsion or Liposome.
	 *
	 * @param entityBean
	 */
	private void copyIsPolymerized(NanomaterialEntityBean entityBean) {
		Boolean polymerized = null;
		String isPolymerized = entityBean.getIsPolymerized();
		if (!StringUtils.isEmpty(isPolymerized)) {
			polymerized = Boolean.valueOf(isPolymerized);
		}
		String entityType = entityBean.getType();
		if ("emulsion".equals(entityType)) {
			entityBean.getEmulsion().setPolymerized(polymerized);
		} else if ("liposome".equals(entityType)) {
			entityBean.getLiposome().setPolymerized(polymerized);
		}
	}

	/**
	 * Setup "polymerized" property in entityBean from Emulsion or Liposome.
	 *
	 * @param entityBean
	 */
	private void setupIsPolymerized(NanomaterialEntityBean entityBean) {
		Boolean polymerized = null;
		String entityType = entityBean.getType();
		if ("emulsion".equals(entityType)) {
			polymerized = entityBean.getEmulsion().getPolymerized();
		} else if ("liposome".equals(entityType)) {
			polymerized = entityBean.getLiposome().getPolymerized();
		}
		if (polymerized == null) {
			entityBean.setIsPolymerized(null);
		} else {
			entityBean.setIsPolymerized(polymerized.toString());
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

	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CompositionService service = (CompositionService) (request.getSession()
				.getAttribute("compositionService"));
		return downloadFile(service, mapping, form, request, response);
	}
}
