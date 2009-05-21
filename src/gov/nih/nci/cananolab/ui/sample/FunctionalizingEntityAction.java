package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit functionalizing entity data under sample composition.
 *
 * @author pansu
 */

/* CVS $Id: FunctionalizingEntityAction.java,v 1.45 2008-09-12 20:09:52 tanq Exp $ */

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class FunctionalizingEntityAction extends CompositionAction {
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
		saveMessages(request, msgs);
		request.setAttribute("location", "local");
		return summaryEdit(mapping, form, request, response);
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
		SampleBean sampleBean = setupSample(theForm, request, "local");
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

		compositionService.saveFunctionalizingEntity(sampleBean.getDomain(),
				entityBean.getDomainEntity());

		// set visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		List<String> accessibleGroups = authService.getAccessibleGroups(
				sampleBean.getDomain().getName(), Constants.CSM_READ_PRIVILEGE);
		if (accessibleGroups != null
				&& accessibleGroups.contains(Constants.CSM_PUBLIC_GROUP)) {
			// set composition public
			authService.assignPublicVisibility(sampleBean.getDomain()
					.getSampleComposition().getId().toString());
			compositionService.assignFunctionalizingEntityPublicVisibility(
					authService, entityBean.getDomainEntity());
		}
		// save file data to file system and set visibility
		saveFilesToFileSystem(entityBean.getFiles());

		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entityBean);
		// save to other particles
		FileService service = new FileServiceLocalImpl();
		Sample[] otherSamples = prepareCopy(request, theForm);
		if (otherSamples != null) {
			FunctionalizingEntity copy = entityBean.getDomainCopy();
			for (Sample sample : otherSamples) {
				compositionService.saveFunctionalizingEntity(sample, copy);
				if (copy.getFileCollection() != null) {
					for (File file : copy.getFileCollection()) {
						service.saveCopiedFileAndSetVisibility(file, user,
								sampleBean.getDomain().getName(), sample
										.getName());
					}
				}
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
		request.getSession().removeAttribute("compositionForm");
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		setLookups(request);
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
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		String entityId = request.getParameter("dataId");
		CompositionService compService = new CompositionServiceLocalImpl();
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId);
		UserBean user = (UserBean) session.getAttribute("user");
		compService.retrieveVisibility(entityBean, user);
		entityBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("functionalizingEntity", entityBean);
		setLookups(request);
		// clear copy to otherSamples
		theForm.set("otherSamples", new String[0]);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		setupSample(theForm, request, location);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String entityId = request.getParameter("dataId");
		String entityClassName = request.getParameter("dataClassName");
		CompositionService compService = null;
		if (location.equals("local")) {
			compService = new CompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			// TODO update grid service
			// compService = new CompositionServiceRemoteImpl(
			// serviceUrl);
		}
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(entityId, entityClassName);
		if (location.equals("local")) {
			compService.retrieveVisibility(entityBean, user);
		}
		entityBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("functionalizingEntity", entityBean);
		return mapping.getInputForward();
	}

	public ActionForward saveFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function=entity.getTheFunction();
		entity.addFunction(function);
		saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entity);

		return mapping.getInputForward();
	}

	public ActionForward removeFunction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		FunctionBean function=entity.getTheFunction();
		entity.removeFunction(function);
		saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance()
				.persistFunctionalizingEntityDropdowns(request, entity);

		return mapping.getInputForward();
	}

	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FunctionalizingEntityBean entity = (FunctionalizingEntityBean) theForm
				.get("functionalizingEntity");
		int theFileIndex = entity.getTheFileIndex();
		// create a new copy before adding to functionalizing entity
		FileBean theFile = entity.getTheFile();
		FileBean newFile = theFile.copy();
		entity.addFile(newFile, theFileIndex);
		//save the functionalizing entity
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
		int theFileIndex = entity.getTheFileIndex();
		entity.removeFile(theFileIndex);
		entity.setTheFile(new FileBean());
		request.setAttribute("anchor", "file");
		//save the functionalizing entity
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
		SampleBean sampleBean = setupSample(theForm, request, "local");
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
		boolean canDelete = compositionService
				.checkChemicalAssociationBeforeDelete(entityBean);
		ActionMessages msgs = new ActionMessages();
		if (canDelete) {
			compositionService.deleteFunctionalizingEntity(entityBean
					.getDomainEntity());
			sampleBean = setupSample(theForm, request, "local");
			ActionMessage msg = new ActionMessage(
					"message.deleteFunctionalizingEntity");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.findForward("success");
		} else {
			ActionMessage msg = new ActionMessage(
					"error.deleteFunctionalizingEntityWithChemicalAssociation",
					entityBean.getClassName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return mapping.getInputForward();
		}
	}

	protected boolean checkDelete(HttpServletRequest request,
			ActionMessages msgs, String id) throws Exception {
		CompositionService compService = new CompositionServiceLocalImpl();
		FunctionalizingEntityBean entityBean = compService
				.findFunctionalizingEntityById(id);
		if (!compService.checkChemicalAssociationBeforeDelete(entityBean)) {
			ActionMessage msg = new ActionMessage(
					"error.deleteFunctionalizingEntityWithChemicalAssociation",
					entityBean.getClassName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		return true;
	}
}
