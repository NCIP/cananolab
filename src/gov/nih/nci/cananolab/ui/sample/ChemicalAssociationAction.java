package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This class allows users to submit chemical association data under sample
 * composition.
 *
 * @author pansu
 */
public class ChemicalAssociationAction extends CompositionAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Chemical Association");
		try {
			assocBean.setupDomainAssociation(InitSetup.getInstance()
					.getDisplayNameToClassNameLookup(
							request.getSession().getServletContext()), user
					.getLoginName(), internalUriPath);
		} catch (ClassCastException ex) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = null;
			if (ex.getMessage() != null && ex.getMessage().length() > 0
					&& !ex.getMessage().equalsIgnoreCase("java.lang.Object")) {
				msg = new ActionMessage("errors.invalidOtherType", ex
						.getMessage(), "Chemical Association");
			} else {
				msg = new ActionMessage("errors.invalidOtherType", assocBean
						.getType(), "Chemical Association");
				assocBean.setType(null);
			}
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			return mapping.getInputForward();
		}

		if (!validateAssociationFile(request, assocBean)) {
			return mapping.getInputForward();
		}

		ActionMessages msgs = new ActionMessages();
		boolean noErrors = true;
		// validate if composing element is null
		if (assocBean.getAssociatedElementA().getDomainElement() instanceof ComposingElement
				&& assocBean.getAssociatedElementA().getDomainElement().getId() == null) {
			ActionMessage msg = new ActionMessage(
					"error.nullComposingElementAInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			noErrors = false;
		}
		if (assocBean.getAssociatedElementB().getDomainElement() instanceof ComposingElement
				&& assocBean.getAssociatedElementB().getDomainElement().getId() == null) {
			ActionMessage msg = new ActionMessage(
					"error.nullComposingElementBInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			noErrors = false;
		}
		if (!noErrors) {
			return mapping.getInputForward();
		}
		// validate if the same associated elements are chosen on both sides
		if (assocBean.getAssociatedElementA().getDomainElement().getId()
				.equals(
						assocBean.getAssociatedElementB().getDomainElement()
								.getId())) {
			ActionMessage msg = new ActionMessage(
					"error.duplicateAssociatedElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			noErrors = false;
		}
		if (noErrors) {
			CompositionService compService = new CompositionServiceLocalImpl();
			compService.saveChemicalAssociation(sampleBean.getDomain(),
					assocBean.getDomainAssociation());
			// set visibility
			AuthorizationService authService = new AuthorizationService(
					Constants.CSM_APP_NAME);
			List<String> accessibleGroups = authService.getAccessibleGroups(
					sampleBean.getDomain().getName(),
					Constants.CSM_READ_PRIVILEGE);
			if (accessibleGroups != null
					&& accessibleGroups.contains(Constants.CSM_PUBLIC_GROUP)) {
				// set composition public
				authService.assignPublicVisibility(sampleBean.getDomain()
						.getSampleComposition().getId().toString());
				compService.assignChemicalAssociationPublicVisibility(
						authService, assocBean.getDomainAssociation());
			}
			// save file data to file system and set visibility
			saveFilesToFileSystem(assocBean.getFiles());

			InitCompositionSetup.getInstance()
					.persistChemicalAssociationDropdowns(request, assocBean,
							false);
			ActionMessage msg = new ActionMessage(
					"message.addChemicalAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			ActionForward forward = mapping.findForward("success");
			setupDataTree(sampleBean, request);
			return forward;
		} else {
			return mapping.getInputForward();
		}
	}

	private boolean validateAssociationFile(HttpServletRequest request,
			ChemicalAssociationBean entityBean) throws Exception {
		ActionMessages msgs = new ActionMessages();
		for (FileBean filebean : entityBean.getFiles()) {
			if (!validateFileBean(request, msgs, filebean)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Set up the input form for adding new chemical association
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
		request.getSession().removeAttribute("chemicalAssociationForm");
		setLookups(form, request);
		return mapping.findForward("setup");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = setupSample(theForm, request, "local");
		prepareEntityLists(theForm, sampleBean, request);
		return mapping.findForward("setup");
	}

	private boolean setLookups(ActionForm form, HttpServletRequest request)
			throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleId = theForm.getString("sampleId");
		CompositionService service = new CompositionServiceLocalImpl();
		CompositionBean compositionBean = service
				.findCompositionBySampleId(sampleId);
		// set entity type and association type
		HttpSession session=request.getSession();
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			entityBean.setType(InitSetup.getInstance().getDisplayName(
					entityBean.getClassName(), session.getServletContext()));
		}
		for (FunctionalizingEntityBean entityBean : compositionBean
				.getFunctionalizingEntities()) {
			entityBean.setType(InitSetup.getInstance().getDisplayName(
					entityBean.getClassName(), session.getServletContext()));
		}
		for (ChemicalAssociationBean assocBean : compositionBean
				.getChemicalAssociations()) {
			assocBean.setType(InitSetup.getInstance().getDisplayName(
					assocBean.getClassName(), session.getServletContext()));
		}
		// check if sample has functionalizing entities
		Boolean hasFunctionalizingEntity = false;
		if (!compositionBean.getFunctionalizingEntities().isEmpty()) {
			hasFunctionalizingEntity = true;
		}
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		// check if sample has nanomaterial entities
		ActionMessages msgs = new ActionMessages();
		if (compositionBean.getNanomaterialEntities().isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"empty.materialsEntitiesInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		// check whether nanomaterial entities has composing elements
		// store those that have composing elements
		int numberOfCE = 0;
		List<NanomaterialEntityBean> materialEntities = new ArrayList<NanomaterialEntityBean>();
		for (NanomaterialEntityBean entityBean : compositionBean
				.getNanomaterialEntities()) {
			if (!entityBean.getComposingElements().isEmpty()) {
				numberOfCE += entityBean.getComposingElements().size();
				materialEntities.add(entityBean);
			}
		}
		if (numberOfCE == 0) {
			ActionMessage msg = new ActionMessage(
					"empty.composingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		if (!hasFunctionalizingEntity && numberOfCE == 1) {
			ActionMessage msg = new ActionMessage(
					"one.composingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		if (!hasFunctionalizingEntity) {
			ActionMessage msg = new ActionMessage(
					"empty.functionalizingEntityInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		}
		request.getSession().setAttribute("sampleMaterialEntities",
				materialEntities);
		request.getSession().setAttribute("sampleFunctionalizingEntities",
				compositionBean.getFunctionalizingEntities());
		request.getSession().setAttribute("hasFunctionalizingEntity",
				hasFunctionalizingEntity);
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		return true;
	}

	public void prepareEntityLists(DynaValidatorForm theForm,
			SampleBean sampleBean, HttpServletRequest request) throws Exception {
		Map<String, SortedSet<DataLinkBean>> dataTree = setupDataTree(
				sampleBean, request);
		SortedSet<DataLinkBean> particleEntitites = dataTree
				.get("Nanomaterial Entity");
		SortedSet<DataLinkBean> functionalizingEntities = dataTree
				.get("Functionalizing Entity");
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		// associated element A
		SortedSet<DataLinkBean> entityListA = null;
		HttpSession session = request.getSession();
		CompositionService service = new CompositionServiceLocalImpl();
		if (assocBean.getAssociatedElementA().getCompositionType().equals(
				"Nanomaterial Entity")) {
			entityListA = particleEntitites;
			for (DataLinkBean dataLink : particleEntitites) {
				List<ComposingElementBean> ceListA = null;
				if (assocBean.getAssociatedElementA().getEntityId().toString()
						.equals(dataLink.getDataId())) {
					NanomaterialEntityBean entityBean = service
							.findNanomaterialEntityById(assocBean
									.getAssociatedElementA().getEntityId());
					ceListA = entityBean.getComposingElements();
				}
				session.setAttribute("ceListA", ceListA);
			}
		} else {
			entityListA = functionalizingEntities;
		}
		// associated element B
		SortedSet<DataLinkBean> entityListB = null;
		if (assocBean.getAssociatedElementB().getCompositionType().equals(
				"Nanomaterial Entity")) {
			entityListB = particleEntitites;
			for (DataLinkBean dataLink : particleEntitites) {
				List<ComposingElementBean> ceListB = null;
				if (assocBean.getAssociatedElementB().getEntityId().toString()
						.equals(dataLink.getDataId())) {
					NanomaterialEntityBean entityBean = service
							.findNanomaterialEntityById(assocBean
									.getAssociatedElementB().getEntityId());
					ceListB = entityBean.getComposingElements();
				}
				session.setAttribute("ceListB", ceListB);
			}
		} else {
			entityListB = functionalizingEntities;
		}
		session.setAttribute("entityListA", entityListA);
		session.setAttribute("entityListB", entityListB);
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		// SampleBean sampleBean = setupSample(theForm, request, "local");
		// setLookups(sampleBean, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String assocId = request.getParameter("dataId");
		CompositionService compService = new CompositionServiceLocalImpl();
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssociationById(assocId);
		compService.retrieveVisibility(assocBean, user);
		assocBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("assoc", assocBean);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = request.getParameter("location");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String assocId = request.getParameter("dataId");
		String sampleId = request.getParameter("sampleId");
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
		String assocClassName = request.getParameter("dataClassName");
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssociationById(sampleId, assocId, assocClassName);
		if (location.equals("local")) {
			compService.retrieveVisibility(assocBean, user);
		}
		assocBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("assoc", assocBean);
		return mapping.findForward("setup");
	}

	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assoc = (ChemicalAssociationBean) theForm
				.get("assoc");
		assoc.addFile();
		Boolean hasFunctionalizingEntity = (Boolean) request.getSession()
				.getAttribute("hasFunctionalizingEntity");
		InitCompositionSetup.getInstance().persistChemicalAssociationDropdowns(
				request, assoc, hasFunctionalizingEntity);
		return mapping.getInputForward();
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String indexStr = request.getParameter("compInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean entity = (ChemicalAssociationBean) theForm
				.get("assoc");
		entity.removeFile(ind);
		return mapping.getInputForward();
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleBean sampleBean = setupSample(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Chemical Association");
		assocBean.setupDomainAssociation(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName(), internalUriPath);
		CompositionService compService = new CompositionServiceLocalImpl();
		compService.deleteChemicalAssociation(assocBean.getDomainAssociation());
		sampleBean = setupSample(theForm, request, "local");
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteChemicalAssociation");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(sampleBean, request);
		return forward;
	}
}
