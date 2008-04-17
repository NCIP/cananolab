package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up the submit a new nanoparticle sample page and submits a new nanoparticle sample
 *  
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.10 2008-04-17 21:30:23 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitNanoparticleAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) theForm
				.get("particleSampleBean");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		particleSampleBean.getParticleSample()
				.setCreatedBy(user.getLoginName());
		particleSampleBean.getParticleSample().setCreatedDate(new Date());

		// persist in the database
		NanoparticleSampleService service = new NanoparticleSampleService();
		service.saveNanoparticleSample(particleSampleBean);

		// set CSM visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		authService.setVisibility(particleSampleBean.getParticleSample()
				.getName(), particleSampleBean.getVisibilityGroups());
		theForm.set("particleSampleBean", particleSampleBean);
		forward = mapping.findForward("success");
		request.setAttribute("theParticle", particleSampleBean);
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleSampleBean,
				request);
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitNanoparticleSetup.getInstance().setAllNanoparticleSampleSources(
				request);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		UserBean user = (UserBean) session.getAttribute("user");
		NanoparticleSampleService service = new NanoparticleSampleService();
		ParticleBean particleSampleBean = service.findNanoparticleSampleBy(
				particleId, user);
		theForm.set("particleSampleBean", particleSampleBean);
		request.setAttribute("theParticle", particleSampleBean);
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleSampleBean,
				request);

		return mapping.findForward("update");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		InitNanoparticleSetup.getInstance().setNanoparticleSampleSources(
				request, user);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);

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
