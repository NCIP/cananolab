package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.ParticleDataLinkBean;
import gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

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

public class ChemicalAssociationAction extends BaseAnnotationAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean assocBean = (ChemicalAssociationBean) theForm
				.get("assoc");
		assocBean.setDomainAssociation();
		ParticleBean particleBean = setupParticle(theForm, request);
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		compService.saveChemicalAssociation(particleBean
				.getDomainParticleSample(), assocBean.getDomainAssociation());
		ActionMessages msgs = new ActionMessages();
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
		request.getSession().removeAttribute("chemicalAssocationForm");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		setLookups(theForm, request);
		return mapping.getInputForward();
	}

	private void setLookups(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		Map<String, SortedSet<ParticleDataLinkBean>> dataTree = setupDataTree(
				theForm, request);
		SortedSet<ParticleDataLinkBean> particleEntitites = dataTree
				.get("Nanoparticle Entity");
		SortedSet<ParticleDataLinkBean> functionalizingEntitites = dataTree
				.get("Functionalizing Entity");
		request.getSession()
				.setAttribute("particleEntities", particleEntitites);
		request.getSession().setAttribute("functionalizingEntitites",
				functionalizingEntitites);
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupParticle(theForm, request);
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String assocId = request.getParameter("dataId");
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		ChemicalAssociationBean assocBean = compService
				.findChemicalAssocationById(assocId);
		compService.setVisibility(assocBean, user);
		String assocType = InitSetup.getInstance().getDisplayName(
				assocBean.getClassName(), session.getServletContext());
		assocBean.setType(assocType);
		theForm.set("assoc", assocBean);
		setLookups(theForm, request);
		return mapping.getInputForward();
	}
	
	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ChemicalAssociationBean entity = (ChemicalAssociationBean) theForm
				.get("assoc");
		entity.addFile();
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
}
