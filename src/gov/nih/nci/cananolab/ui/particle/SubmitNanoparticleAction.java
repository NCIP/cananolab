package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up the submit a new nanoparticle sample page and submits a new nanoparticle sample
 *  
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.18 2008-04-29 21:16:31 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SubmitNanoparticleAction extends BaseAnnotationAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) theForm
				.get("particleSampleBean");
		particleSampleBean.setDomainParticleSample();
		// persist in the database
		NanoparticleSampleService service = new NanoparticleSampleService();
		service.saveNanoparticleSample(particleSampleBean
				.getDomainParticleSample());

		// set CSM visibility
		// add sample source as a new CSM_GROUP and assign the sample to
		// the new group
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		String[] visibleGroups = new String[particleSampleBean
				.getVisibilityGroups().length + 1];
		for (int i = 0; i < visibleGroups.length - 1; i++) {
			visibleGroups[i] = particleSampleBean.getVisibilityGroups()[i];
		}
		visibleGroups[visibleGroups.length - 1] = particleSampleBean
				.getDomainParticleSample().getSource().getOrganizationName();
		particleSampleBean.setVisibilityGroups(visibleGroups);

		authService.setVisibility(particleSampleBean.getDomainParticleSample()
				.getName(), visibleGroups);

		theForm.set("particleSampleBean", particleSampleBean);
		forward = mapping.findForward("success");
		request.setAttribute("theParticle", particleSampleBean);
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance()
				.getDataTree(
						particleSampleBean.getDomainParticleSample().getId()
								.toString(), request);
		InitNanoparticleSetup.getInstance().setAllNanoparticleSampleSources(
				request);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(request);
		return forward;
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = setupParticle(theForm, request);
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		// set visibility
		NanoparticleSampleService service = new NanoparticleSampleService();
		service.setVisibility(particleSampleBean, user);
		theForm.set("particleSampleBean", particleSampleBean);
		InitNanoparticleSetup.getInstance().setAllNanoparticleSampleSources(
				request);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(request);
		setupDataTree(theForm, request);
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
		InitNanoparticleSetup.getInstance().setAllNanoparticleSampleSources(
				request);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(request);
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
