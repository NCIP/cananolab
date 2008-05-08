package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.ParticleDataLinkBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

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
		ParticleBean particleBean = setupParticle(theForm, request);
		assocBean.setupDomainAssociation(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName());
		ActionMessages msgs = new ActionMessages();
		if (assocBean.getAssociatedElementA().getDomainElement().getId()
				.equals(
						assocBean.getAssociatedElementB().getDomainElement()
								.getId())) {
			ActionMessage msg = new ActionMessage(
					"error.duplicateAssociatedElementsInAssociation");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return mapping.getInputForward();
		}

		// setup domainFile for fileBeans
		for (LabFileBean fileBean : assocBean.getFiles()) {
			String internalUri = InitSetup.getInstance()
					.getFileUriFromFormFile(fileBean.getUploadedFile(),
							CaNanoLabConstants.FOLDER_PARTICLE,
							particleBean.getDomainParticleSample().getName(),
							"Chemical Association");
			fileBean.setInternalUri(internalUri);
			fileBean.setupDomainFile(user.getLoginName());
		}
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		compService.saveChemicalAssociation(particleBean
				.getDomainParticleSample(), assocBean.getDomainAssociation());

		// save file data to file system and set visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);

		FileService fileService = new FileService();
		for (LabFileBean fileBean : assocBean.getFiles()) {
			fileService.writeFile(fileBean.getDomainFile(), fileBean
					.getFileData());
			authService.assignVisibility(fileBean.getDomainFile().getId()
					.toString(), fileBean.getVisibilityGroups());
		}

		InitCompositionSetup.getInstance().persistChemicalAssociationDropdowns(
				request, assocBean, false);
		ActionMessage msg = new ActionMessage("message.addChemicalAssociation");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(theForm, request);
		return forward;
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
		setupParticle(theForm, request);
		if (!setLookups(theForm, request)) {
			return mapping.findForward("particleMessage");
		}
		return mapping.getInputForward();
	}

	private boolean setLookups(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		Map<String, SortedSet<ParticleDataLinkBean>> dataTree = setupDataTree(
				theForm, request);
		SortedSet<ParticleDataLinkBean> particleEntities = dataTree
				.get("Nanoparticle Entity");
		SortedSet<ParticleDataLinkBean> functionalizingEntities = dataTree
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
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		SortedSet<ParticleDataLinkBean> particleEntitiesWithComposingElements = new TreeSet<ParticleDataLinkBean>();
		// particleEntitiesWithComposingElements = particleEntities;
		for (ParticleDataLinkBean dataLink : particleEntities) {
			NanoparticleEntityBean entityBean = compService
					.findNanoparticleEntityById(dataLink.getDataId());
			if (entityBean.getComposingElements().size() > 0) {
				particleEntitiesWithComposingElements.add(dataLink);
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
		request.getSession().setAttribute("hasNanoparticleEntity",
				hasFunctionalizingEntity);
		InitNanoparticleSetup.getInstance().setSharedDropdowns(request);
		InitCompositionSetup.getInstance().setChemicalAssociationDropdowns(
				request, hasFunctionalizingEntity);
		return true;
	}

	public void prepareEntityLists(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		Map<String, SortedSet<ParticleDataLinkBean>> dataTree = setupDataTree(
				theForm, request);
		SortedSet<ParticleDataLinkBean> particleEntitites = dataTree
				.get("Nanoparticle Entity");
		SortedSet<ParticleDataLinkBean> functionalizingEntities = dataTree
				.get("Functionalizing Entity");
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		// associated element A
		SortedSet<ParticleDataLinkBean> entityListA = null;
		HttpSession session = request.getSession();
		if (assocBean.getAssociatedElementA().getComposingElement().getId() != null) {
			entityListA = particleEntitites;
			for (ParticleDataLinkBean dataLink : particleEntitites) {
				if (assocBean.getAssociatedElementA().getComposingElement()
						.getNanoparticleEntity().getId().toString().equals(
								dataLink.getDataId())) {
					NanoparticleEntityBean entityBean = new NanoparticleEntityBean(
							assocBean.getAssociatedElementA()
									.getComposingElement()
									.getNanoparticleEntity());
					List<ComposingElementBean> ceListA = entityBean
							.getComposingElements();
					session.setAttribute("ceListA", ceListA);
				}
			}
		} else {
			entityListA = functionalizingEntities;
		}
		// associated element B
		SortedSet<ParticleDataLinkBean> entityListB = null;
		if (assocBean.getAssociatedElementB().getComposingElement().getId() != null) {
			entityListB = particleEntitites;
			for (ParticleDataLinkBean dataLink : particleEntitites) {
				if (assocBean.getAssociatedElementB().getComposingElement()
						.getNanoparticleEntity().getId().toString().equals(
								dataLink.getDataId())) {
					NanoparticleEntityBean entityBean = new NanoparticleEntityBean(
							assocBean.getAssociatedElementB()
									.getComposingElement()
									.getNanoparticleEntity());
					List<ComposingElementBean> ceListB = entityBean
							.getComposingElements();
					session.setAttribute("ceListB", ceListB);
				}
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
		setupParticle(theForm, request);
		setLookups(theForm, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String assocId = request.getParameter("dataId");
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssocationById(assocId);
		compService.retrieveVisibility(assocBean, user);
		assocBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("assoc", assocBean);
		prepareEntityLists(theForm, request);
		return mapping.getInputForward();
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		setLookups(theForm, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String assocId = request.getParameter("dataId");
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssocationById(assocId);
		compService.retrieveVisibility(assocBean, user);
		assocBean.updateType(InitSetup.getInstance()
				.getClassNameToDisplayNameLookup(session.getServletContext()));
		theForm.set("assoc", assocBean);

		return mapping.getInputForward();
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
		prepareEntityLists(theForm, request);
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
		prepareEntityLists(theForm, request);
		return mapping.getInputForward();
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		assocBean.setupDomainAssociation(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName());
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		compService.deleteChemicalAssociation(assocBean.getDomainAssociation());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteChemicalAssociation");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(theForm, request);
		return forward;
	}
}
