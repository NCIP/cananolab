package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit functionalizing entity data under sample composition.
 *
 * @author pansu
 */

/* CVS $Id: FunctionalizingEntityAction.java,v 1.45 2008-09-12 20:09:52 tanq Exp $ */

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
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

public class FunctionalizingEntityAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		saveEntity(request, theForm, entityBean);
		if (!validateTargets(request, entityBean)) {
			return mapping.getInputForward();
		}

		if (!validateEntityFile(request, entityBean)) {
			return mapping.getInputForward();
		}

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		// to preselect functionalizing entity after returning to the summary
		// page
		request.getSession().setAttribute("onloadJavascript",
				"showSummary('2', 4)");
		return mapping.findForward("success");
	}

	private boolean validateTargets(HttpServletRequest request,
			FunctionalizingEntityBean entityBean) throws Exception {
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
					if (targetBean.getName() == null
							|| targetBean.getName().trim().length() == 0) {

						ActionMessages msgs = new ActionMessages();
						ActionMessage msg = new ActionMessage(
								"errors.required", "Target name");
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
			FunctionalizingEntityBean entityBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, msgs, filebean)) {
				return false;
			}
		}
		return true;
	}

	public void saveEntity(HttpServletRequest request,
			DynaValidatorForm theForm, FunctionalizingEntityBean entityBean)
			throws Exception {
		CompositionService compositionService = new CompositionServiceLocalImpl();
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Functionalizing Entity");
		try {
			entityBean.setupDomainEntity(InitSetup.getInstance()
					.getDisplayNameToClassNameLookup(
							request.getSession().getServletContext()), user
					.getLoginName(), internalUriPath);
		} catch (ClassCastException ex) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = null;
			if (ex.getMessage() != null && ex.getMessage().length() > 0
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msg = new ActionMessage("errors.invalidOtherType", ex
						.getMessage(), "Function");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "Functionalizing Entity");
				entityBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
		}

		compositionService.saveFunctionalizingEntity(sampleBean, entityBean,
				user);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entityBean);

		// save to other samples
		SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
				sampleBean);
		if (otherSampleBeans != null) {
			compositionService.copyAndSaveFunctionalizingEntity(entityBean,
					sampleBean, otherSampleBeans, user);
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
		request.getSession().removeAttribute("compositionForm");
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		this.setLookups(request);
		request.getSession().setAttribute("onloadJavascript",
				"setEntityInclude('feType', 'functionalizingEntity');");
		return mapping.getInputForward();
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		InitCompositionSetup.getInstance().setFunctionalizingEntityDropdowns(
				request);
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		String sampleId = theForm.getString("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		String entityId = request.getParameter("dataId");
		if (entityId == null) {
			entityId = (String) request.getAttribute("dataId");
		}

		UserBean user = (UserBean) session.getAttribute("user");
		CompositionService compService = new CompositionServiceLocalImpl();
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId, user);
		entityBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("functionalizingEntity", entityBean);
		this.setLookups(request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);
		return mapping.getInputForward();
	}

	public ActionForward saveFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function = entity.getTheFunction();
		entity.addFunction(function);
		saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entity);
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
		saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entity);

		return mapping.getInputForward();
	}

	public ActionForward saveFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FileBean theFile = entity.getTheFile();
		entity.addFile(theFile);
		// save the functionalizing entity
		saveEntity(request, theForm, entity);
		request.setAttribute("anchor", "file");
		return mapping.getInputForward();
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
		saveEntity(request, theForm, entity);
		return mapping.getInputForward();
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * DynaValidatorForm theForm = (DynaValidatorForm) form;
		 * FunctionalizingEntityBean entity = (FunctionalizingEntityBean)
		 * theForm .get("functionalizingEntity"); // update editable dropdowns
		 * HttpSession session = request.getSession();
		 * InitSampleSetup.getInstance().updateEditableDropdown(session,
		 * composition.getCharacterizationSource(), "characterizationSources");
		 * 
		 * PolymerBean polymer = (PolymerBean) theForm.get("polymer");
		 * updatePolymerEditable(session, polymer);
		 * 
		 * DendrimerBean dendrimer = (DendrimerBean) theForm.get("dendrimer");
		 * updateDendrimerEditable(session, dendrimer);
		 */
		return mapping.findForward("setup");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionService compositionService = new CompositionServiceLocalImpl();
		FunctionalizingEntityBean entityBean = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Functionalizing Entity");
		entityBean.setupDomainEntity(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName(), internalUriPath);
		ActionMessages msgs = new ActionMessages();
		compositionService.deleteFunctionalizingEntity(entityBean
				.getDomainEntity(), user);
		sampleBean = setupSample(theForm, request, Constants.LOCAL_SITE);
		ActionMessage msg = new ActionMessage(
				"message.deleteFunctionalizingEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		// save action messages in the session so composition.do know about
		// them
		request.getSession().setAttribute(ActionMessages.GLOBAL_MESSAGE, msgs);
		return mapping.findForward("success");
	}
}
