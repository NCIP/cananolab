package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up the submit a new nanoparticle sample page and submits a new nanoparticle sample
 *  
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.3 2008-04-11 14:42:23 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

public class SubmitNanoparticleAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particleSampleBean = (ParticleBean) theForm
				.get("particleSampleBean");
		UserBean user = new UserBean("Test");
		particleSampleBean.getParticleSample()
				.setCreatedBy(user.getLoginName());
		particleSampleBean.getParticleSample().setCreatedDate(new Date());

		NanoparticleSampleService service = new NanoparticleSampleService();

		service.saveNanoparticleSample(particleSampleBean);
		HttpSession session = request.getSession();

		// display default visible groups
		List<String> visList = new ArrayList<String>();
		visList.addAll(Arrays.asList(CaNanoLabConstants.VISIBLE_GROUPS));
		visList.addAll(Arrays.asList(particleSampleBean.getVisibilityGroups()));
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.submitNanoparticleSample", StringUtils.join(visList,
						", "));
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		session.setAttribute("newParticleCreated", "true");
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
		ParticleBean particleBean = service.findNanoparticleSampleBy(
				particleId, user);
		theForm.set("particleSampleBean", particleBean);
		session.setAttribute("newParticleCreated", "true");
		
		session.setAttribute("particleDataTree", InitNanoparticleSetup
				.getInstance().getDataTree(particleBean,
						session.getServletContext()));

		return mapping.findForward("update");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitNanoparticleSetup.getInstance().setAllNanoparticleSampleSources(
				request);
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
