package gov.nih.nci.cananolab.ui.sample;

/**
 * This class allows users to submit nanomaterial entity data under sample composition.
 *
 * @author pansu
 */

/* CVS $Id: NanomaterialEntityAction.java,v 1.54 2008-09-12 20:09:52 tanq Exp $ */

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
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

public class NanomaterialEntityAction extends CompositionAction {

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
		saveEntity(request, theForm, entityBean);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addNanomaterialEntity");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return summaryEdit(mapping, form, request, response);
	}

	private void saveEntity(HttpServletRequest request,
			DynaValidatorForm theForm, NanomaterialEntityBean entityBean)
			throws Exception {
		SampleBean sampleBean = setupSample(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Nanomaterial Entity");
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
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "Nanomaterial Entity");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", entityBean
						.getType(), "Nanomaterial Entity");
				entityBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			entityBean.setType(null);
		}

		CompositionService compositionService = new CompositionServiceLocalImpl();
		compositionService.saveNanomaterialEntity(sampleBean.getDomain(),
				entityBean.getDomainEntity());
		// save file data to file system and set visibility
		saveFilesToFileSystem(entityBean.getFiles());
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
			compositionService.assignNanomaterialEntityPublicVisibility(
					authService, entityBean.getDomainEntity());
		}

		// save to other particles
		FileService service = new FileServiceLocalImpl();
		Sample[] otherSamples = prepareCopy(request, theForm);
		if (otherSamples != null) {
			NanomaterialEntity copy = entityBean.getDomainCopy();
			for (Sample sample : otherSamples) {
				compositionService.saveNanomaterialEntity(sample, copy);
				// update copied filename and save content and set visibility
				if (copy.getFileCollection() != null) {
					for (File file : copy.getFileCollection()) {
						service.saveCopiedFileAndSetVisibility(file, user,
								sampleBean.getDomain().getName(), sample
										.getName());
					}
				}
			}
		}
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entityBean);
		request.setAttribute("location", "local");
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
		InitSampleSetup.getInstance().setSharedDropdowns(request);
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
		request.getSession().removeAttribute("compositionForm");
		String sampleId = request.getParameter("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
		setLookups(request);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String entityId = request.getParameter("dataId");
		if (entityId==null) {
			entityId=(String)request.getAttribute("dataId");
		}
		String sampleId = theForm.getString("sampleId");
		// set up other particles with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);

		CompositionService compService = new CompositionServiceLocalImpl();
		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId);
		compService.retrieveVisibility(entityBean, user);
		entityBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("nanomaterialEntity", entityBean);
		setLookups(request);
		theForm.set("otherSamples", new String[0]);
		String detailPage = null;
		if (entityBean.isWithProperties()) {
			detailPage = InitCompositionSetup.getInstance().getDetailPage(
					entityBean.getClassName(), "nanomaterialEntity");
		}
		request.setAttribute("entityDetailPage", detailPage);
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
		String entityClassName = request.getParameter("dataClassName");
		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(entityId, entityClassName);
		if (location.equals("local")) {
			compService.retrieveVisibility(entityBean, user);
		}
		entityBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("nanomaterialEntity", entityBean);
		return mapping.getInputForward();
	}

	public ActionForward saveComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		ComposingElementBean composingElement=entity.getTheComposingElement();
		entity.addComposingElement(composingElement);
		// save nanomaterial entity
		saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entity);
		//return to setupUpdate to retrieve the correct entity from database after saving to database.
		request.setAttribute("dataId", entity.getDomainEntity().getId().toString());
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward removeComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		ComposingElementBean element=entity.getTheComposingElement();
		entity.removeComposingElement(element);
		saveEntity(request, theForm, entity);
		InitCompositionSetup.getInstance().persistNanomaterialEntityDropdowns(
				request, entity);
		return mapping.getInputForward();
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
		saveEntity(request, theForm, entity);
		request.setAttribute("anchor", "file");
		request.setAttribute("dataId", entity.getDomainEntity().getId().toString());
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
		saveEntity(request, theForm, entity);
		request.setAttribute("anchor", "file");
		return mapping.getInputForward();
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		 * DynaValidatorForm theForm = (DynaValidatorForm) form;
		 * NanomaterialEntityBean entity = (NanomaterialEntityBean) theForm
		 * .get("nanomaterialEntity"); // update editable dropdowns HttpSession
		 * session = request.getSession();
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
		NanomaterialEntityBean entityBean = (NanomaterialEntityBean) theForm
				.get("nanomaterialEntity");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Nanomaterial Entity");
		entityBean.setupDomainEntity(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName(), internalUriPath);
		boolean canDelete = compositionService
				.checkChemicalAssociationBeforeDelete(entityBean);
		ActionMessages msgs = new ActionMessages();
		if (canDelete) {
			compositionService.deleteNanomaterialEntity(entityBean
					.getDomainEntity());
			sampleBean = setupSample(theForm, request, "local");
			ActionMessage msg = new ActionMessage(
					"message.deleteNanomaterialEntity");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.findForward("success");
		} else {
			ActionMessage msg = new ActionMessage(
					"error.deleteNanomaterialEntityWithChemicalAssociation",
					entityBean.getClassName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return mapping.getInputForward();
		}
	}

	protected boolean checkDelete(HttpServletRequest request,
			ActionMessages msgs, String id) throws Exception {
		CompositionService compService = new CompositionServiceLocalImpl();
		NanomaterialEntityBean entityBean = compService
				.findNanomaterialEntityById(id);
		if (!compService.checkChemicalAssociationBeforeDelete(entityBean)) {
			ActionMessage msg = new ActionMessage(
					"error.deleteNanomaterialEntityWithChemicalAssociation",
					entityBean.getClassName());
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		return true;
	}
}
