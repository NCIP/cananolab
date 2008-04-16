package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleEntityAction.java,v 1.3 2008-04-16 13:49:09 pansu Exp $ */

import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.Dendrimer;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleEntityAction extends AbstractDispatchAction {

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
		ActionForward forward = null;

		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = new UserBean("Test");
		NanoparticleCompositionService compositionService = new NanoparticleCompositionService();
		// REMOVE test code
		NanoparticleEntityBean entityBean = (NanoparticleEntityBean) theForm
				.get("entity");
		entityBean.setType("dendrimer");
		entityBean.setClassName(InitSetup.getInstance().getObjectName(
				"dendrimer", session.getServletContext()));
		entityBean.setDescription("This is a test");
		Dendrimer dendrimer = new Dendrimer();
		dendrimer.setBranch("1-4");
		dendrimer.setGeneration((float) 1.5);
		dendrimer.setCreatedBy(user.getLoginName());
		dendrimer.setCreatedDate(new Date());
		entityBean.setDendrimer(dendrimer);
		ComposingElementBean composingElementBean = new ComposingElementBean();
		composingElementBean.getDomainComposingElement().setType("core");
		composingElementBean.getDomainComposingElement().setCreatedBy(
				user.getLoginName());
		composingElementBean.getDomainComposingElement().setCreatedDate(
				new Date());
		FunctionBean functionBean = new FunctionBean();
		functionBean.setType("imaging");
		functionBean.setDescription("test");
		List<FunctionBean> inherentFunctions = new ArrayList<FunctionBean>();
		inherentFunctions.add(functionBean);
		composingElementBean.setInherentFunctions(inherentFunctions);
		List<ComposingElementBean> composingElements = new ArrayList<ComposingElementBean>();
		composingElements.add(composingElementBean);
		entityBean.setComposingElements(composingElements);
		// END OF REMOVE

		ParticleBean particleBean = (ParticleBean) session
				.getAttribute("particleSampleBean");
		compositionService.saveNanoparticleEntity(particleBean, entityBean);
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
		String particleId = request.getParameter("particleId");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		NanoparticleSampleService service = new NanoparticleSampleService();
		ParticleBean particleBean = service.findNanoparticleSampleBy(
				particleId, user);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("particleSampleBean", particleBean);
		request.setAttribute("newParticleCreated", "true");
		session.setAttribute("particleDataTree", InitNanoparticleSetup
				.getInstance().getDataTree(particleBean, request));
		InitNanoparticleSetup.getInstance().setNanoparticleEntityTypes(request);
		InitNanoparticleSetup.getInstance().setEmulsionComposingElementTypes(
				request);
		InitNanoparticleSetup.getInstance().setComposingElementTypes(request);
		InitNanoparticleSetup.getInstance().setFunctionTypes(request);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		NanoparticleSampleService sampleService = new NanoparticleSampleService();
		ParticleBean particleBean = sampleService.findNanoparticleSampleBy(
				particleId, user);

		theForm.set("particleSampleBean", particleBean);
		request.setAttribute("theParticle", particleBean);
		String entityId = request.getParameter("entityId");
		NanoparticleCompositionService compService = new NanoparticleCompositionService();
		NanoparticleEntityBean entityBean = compService
				.findNanoparticleEntityBy(entityId, user);
		String entityType = InitSetup.getInstance().getDisplayName(
				entityBean.getClassName(), session.getServletContext());
		entityBean.setType(entityType);
		theForm.set("entity", entityBean);
		request.setAttribute("newParticleCreated", "true");
		session.setAttribute("particleDataTree", InitNanoparticleSetup
				.getInstance().getDataTree(particleBean, request));
		InitNanoparticleSetup.getInstance().setNanoparticleEntityTypes(request);
		InitNanoparticleSetup.getInstance().setEmulsionComposingElementTypes(
				request);
		InitNanoparticleSetup.getInstance().setComposingElementTypes(request);
		InitNanoparticleSetup.getInstance().setFunctionTypes(request);
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
