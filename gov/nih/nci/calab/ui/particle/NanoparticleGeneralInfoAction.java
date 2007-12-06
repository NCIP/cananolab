package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up the nanoparticle general information page and allows users to submit/update
 * the general information.  
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleGeneralInfoAction.java,v 1.6 2007-12-06 14:18:30 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.service.particle.NanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.Arrays;
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

public class NanoparticleGeneralInfoAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		NanoparticleService submitNanoparticleService = new NanoparticleService();
		submitNanoparticleService.addParticleGeneralInfo(particle);
		HttpSession session = request.getSession();
		// display default visible groups
		List<String> visList = new ArrayList<String>();
		visList.addAll(Arrays.asList(CaNanoLabConstants.VISIBLE_GROUPS));
		visList.addAll(Arrays.asList(particle.getVisibilityGroups()));
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.createNanoparticle.secure", StringUtils.join(visList,
						", "));
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		session.setAttribute("newParticleCreated", "true");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		request.setAttribute("theParticle", particle);
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();

		InitParticleSetup.getInstance().setNewParticleTypes(session);
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSecuritySetup.getInstance().setAllVisibilityGroups(session);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		NanoparticleService searchtNanoparticleService = new NanoparticleService();
		ParticleBean particle = searchtNanoparticleService
				.getGeneralInfo(particleId);
		theForm.set("particle", particle);
		session.setAttribute("newParticleCreated", "true");
		InitParticleSetup.getInstance()
				.setSideParticleMenu(request, particleId);
		request.setAttribute("theParticle", particle);
		return mapping.findForward("update");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		NanoparticleService searchNanoparticleService = new NanoparticleService();
		ParticleBean particle = searchNanoparticleService
				.getGeneralInfo(particleId);
		theForm.set("particle", particle);
		request.getSession().setAttribute("newParticleCreated", "true");
		InitParticleSetup.getInstance()
				.setSideParticleMenu(request, particleId);
		request.setAttribute("theParticle", particle);
		return mapping.findForward("view");
	}

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
