package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCompositionServiceRemoteImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.DataLinkBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
public class ChemicalAssociationAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
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
			NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
			compService.saveChemicalAssociation(particleBean
					.getDomainParticleSample(), assocBean
					.getDomainAssociation());
			// set visibility
			AuthorizationService authService = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);
			List<String> accessibleGroups = authService.getAccessibleGroups(
					particleBean.getDomainParticleSample().getName(),
					CaNanoLabConstants.CSM_READ_PRIVILEGE);
			if (accessibleGroups != null
					&& accessibleGroups
							.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
				// set composition public
				authService.assignPublicVisibility(particleBean
						.getDomainParticleSample().getSampleComposition()
						.getId().toString());
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
			setupDataTree(particleBean, request);
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
	 * Set up the input form for adding new nanoparticle entity
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("chemicalAssociationForm");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		if (!setLookups(particleBean, request)) {
			return mapping.findForward("particleMessage");
		}
		return mapping.findForward("setup");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		prepareEntityLists(theForm, particleBean, request);
		return mapping.findForward("setup");
	}

	private boolean setLookups(ParticleBean particleBean,
			HttpServletRequest request) throws Exception {
		Map<String, SortedSet<DataLinkBean>> dataTree = setupDataTree(
				particleBean, request);
		SortedSet<DataLinkBean> particleEntities = dataTree
				.get("Nanoparticle Entity");
		SortedSet<DataLinkBean> functionalizingEntities = dataTree
				.get("Functionalizing Entity");
		ActionMessages msgs = new ActionMessages();
		if (particleEntities == null || particleEntities.isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"empty.particleEntitiesInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}

		// check where particle entities has composing elements
		int numberOfCE = 0;
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
		SortedSet<DataLinkBean> particleEntitiesWithComposingElements = new TreeSet<DataLinkBean>(
				particleEntities);
		for (DataLinkBean dataLink : particleEntities) {
			NanoparticleEntityBean entityBean = compService
					.findNanoparticleEntityById(dataLink.getDataId());
			if (entityBean.getComposingElements().size() == 0) {
				particleEntitiesWithComposingElements.remove(dataLink);
			} else {
				numberOfCE += entityBean.getComposingElements().size();
			}
		}
		if (particleEntitiesWithComposingElements.isEmpty()) {
			ActionMessage msg = new ActionMessage(
					"empty.composingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		if (functionalizingEntities == null
				|| functionalizingEntities.isEmpty() && numberOfCE == 1) {
			ActionMessage msg = new ActionMessage(
					"one.composingElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return false;
		}
		boolean hasFunctionalizingEntity = true;
		if (functionalizingEntities.isEmpty()) {
			hasFunctionalizingEntity = false;
			ActionMessage msg = new ActionMessage(
					"empty.functionalizingEntityInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		}
		request.getSession().setAttribute("particleEntities",
				particleEntitiesWithComposingElements);
		request.getSession().setAttribute("functionalizingEntities",
				functionalizingEntities);
		request.getSession().setAttribute("hasFunctionalizingEntity",
				hasFunctionalizingEntity);
		InitNanoparticleSetup.getInstance().setSharedDropdowns(request);
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		return true;
	}

	public void prepareEntityLists(DynaValidatorForm theForm,
			ParticleBean particleBean, HttpServletRequest request)
			throws Exception {
		Map<String, SortedSet<DataLinkBean>> dataTree = setupDataTree(
				particleBean, request);
		SortedSet<DataLinkBean> particleEntitites = dataTree
				.get("Nanoparticle Entity");
		SortedSet<DataLinkBean> functionalizingEntities = dataTree
				.get("Functionalizing Entity");
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		// associated element A
		SortedSet<DataLinkBean> entityListA = null;
		HttpSession session = request.getSession();
		NanoparticleCompositionService service = new NanoparticleCompositionServiceLocalImpl();
		if (assocBean.getAssociatedElementA().getCompositionType().equals(
				"Nanoparticle Entity")) {
			entityListA = particleEntitites;
			for (DataLinkBean dataLink : particleEntitites) {
				List<ComposingElementBean> ceListA = null;
				if (assocBean.getAssociatedElementA().getEntityId().toString()
						.equals(dataLink.getDataId())) {
					NanoparticleEntityBean entityBean = service
							.findNanoparticleEntityById(assocBean
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
				"Nanoparticle Entity")) {
			entityListB = particleEntitites;
			for (DataLinkBean dataLink : particleEntitites) {
				List<ComposingElementBean> ceListB = null;
				if (assocBean.getAssociatedElementB().getEntityId().toString()
						.equals(dataLink.getDataId())) {
					NanoparticleEntityBean entityBean = service
							.findNanoparticleEntityById(assocBean
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
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		setLookups(particleBean, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String assocId = request.getParameter("dataId");
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
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
		String particleId = request.getParameter("particleId");
		NanoparticleCompositionService compService = null;
		if (location.equals("local")) {
			compService = new NanoparticleCompositionServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			compService = new NanoparticleCompositionServiceRemoteImpl(
					serviceUrl);
		}
		String assocClassName = request.getParameter("dataClassName");
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssociationById(particleId, assocId,
						assocClassName);
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
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
				+ "/"
				+ StringUtils
						.getOneWordLowerCaseFirstLetter("Chemical Association");
		assocBean.setupDomainAssociation(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName(), internalUriPath);
		NanoparticleCompositionService compService = new NanoparticleCompositionServiceLocalImpl();
		compService.deleteChemicalAssociation(assocBean.getDomainAssociation());
		particleBean = setupParticle(theForm, request, "local");
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteChemicalAssociation");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(particleBean, request);
		return forward;
	}
}
