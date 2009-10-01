package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit nanomaterial entity data under sample composition.
 *
 * @author pansu
 */

/* CVS $Id: NanomaterialEntityAction.java,v 1.54 2008-09-12 20:09:52 tanq Exp $ */

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
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
		
		this.saveEntity(request, theForm, entityBean);
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
		String detailPage = null;
		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		if (!StringUtils.isEmpty(entityBean.getType())) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getType(), "nanomaterialEntity");
			request.setAttribute("entityDetailPage", detailPage);
		}
		// set pubChemId and value for composing element to be null if they
		// were
		// default to zero from the form
		ComposingElementBean ce = entityBean.getTheComposingElement();
		if (ce.getDomain().getPubChemId() != null
				&& ce.getDomain().getPubChemId() == 0) {
			ce.getDomain().setPubChemId(null);
		}
		if (ce.getDomain().getValue() != null && ce.getDomain().getValue() == 0) {
			ce.getDomain().setValue(null);
		}
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entityBean);
		this.checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	private void saveEntity(HttpServletRequest request,
			DynaValidatorForm theForm, NanomaterialEntityBean entityBean)
			throws Exception {
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + '/'
				+ sampleBean.getDomain().getName() + '/' + "nanomaterialEntity";
		try {
			entityBean.setupDomainEntity(user.getLoginName(), internalUriPath);
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
		CompositionService compositionService = new CompositionServiceLocalImpl();
		compositionService.saveNanomaterialEntity(sampleBean, entityBean, user);

		// save to other samples
		SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
				sampleBean);
		if (otherSampleBeans != null) {
			compositionService.copyAndSaveNanomaterialEntity(entityBean,
					sampleBean, otherSampleBeans, user);
		}
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entityBean);
		request.setAttribute(Constants.LOCATION, Constants.LOCAL_SITE);
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
		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		request.getSession().removeAttribute("compositionForm");
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		this.checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String entityId = request.getParameter("dataId");
		if (entityId == null) {
			entityId = (String) request.getAttribute("dataId");
		}
		String sampleId = theForm.getString("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		CompositionService compService = new CompositionServiceLocalImpl();
		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId, user);
		theForm.set("nanomaterialEntity", entityBean);
		theForm.set("otherSamples", new String[0]);
		this.setLookups(request);
		this.setupIsPolymerized(entityBean);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "nanomaterialEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
		this.checkOpenForms(entityBean, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String entityId = request.getParameter("dataId");
		String location = theForm.getString(Constants.LOCATION);
		if (entityId == null) {
			entityId = (String) request.getAttribute("dataId");
		}		
		CompositionService compService = new CompositionServiceLocalImpl();		
		if (Constants.LOCAL_SITE.equals(location)) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			compService = new CompositionServiceRemoteImpl(serviceUrl);
		}
		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId, user);
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
		entity.addComposingElement(composingElement);
		// save nanomaterial entity
		this.saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entity);
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
		ComposingElementBean element = entity.getTheComposingElement();
		entity.removeComposingElement(element);
		this.saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entity);
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
		entity.addFile(theFile);
		// save nanomaterial entity
		this.saveEntity(request, theForm, entity);
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
		request.setAttribute("anchor", "file");
		this.checkOpenForms(entity, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionService compositionService = new CompositionServiceLocalImpl();
		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE + '/'
				+ sampleBean.getDomain().getName() + '/' + "nanomaterialEntity";
		entityBean.setupDomainEntity(user.getLoginName(), internalUriPath);
		compositionService.deleteNanomaterialEntity(entityBean
				.getDomainEntity(), user);
		sampleBean = setupSample(theForm, request, Constants.LOCAL_SITE);
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
			HttpServletRequest request) {
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
						.getComposingElements().isEmpty())
				|| (!StringUtils.isEmpty(entity.getTheComposingElement()
						.getDisplayName()) && !dispatch
						.equals("saveComposingElement"))) {
			openComposingElement = true;
		}
		session.setAttribute("openComposingElement", openComposingElement);

		/**
		 * other nanomaterial entity types are not stored in the lookup
		 * are retrieved through reflection only after saving to the database.
		 * Need to update session variable before saving to the database 
		 */
		// Nanomaterial Entity Type
		String entityType = entity.getType();
		setOtherValueOption(request, entityType, "nanomaterialEntityTypes");
		//function type
		String functionType=entity.getTheComposingElement().getTheFunction().getType();
		setOtherValueOption(request, functionType, "functionTypes");
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
}
