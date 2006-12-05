package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up the nanoparticle general information page and allows users to submit/update
 * the general information.  
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleGeneralInfoAction.java,v 1.13 2006-12-05 17:21:52 pansu Exp $ */

import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

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
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		String keywords = (String) theForm.get("keywords");
		String[] visibilities = (String[]) theForm.get("visibilities");
		String[] keywordList = (keywords.length() == 0) ? null : keywords
				.split("\r\n");
		SubmitNanoparticleService submitNanoparticleService = new SubmitNanoparticleService();
		submitNanoparticleService.createNanoparticle(particleType,
				particleName, keywordList, visibilities);
		HttpSession session = request.getSession();
		
		//display default visible groups
		if (visibilities.length==0) {
			visibilities=CananoConstants.DEFAULT_VISIBLE_GROUPS;
		}
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.createNanoparticle.secure", StringUtils.join(
						visibilities, ", "));
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		session.setAttribute("canUserUpdateParticle", "true");
		session.setAttribute("newParticleCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setAllParticleTypeParticles(session);
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);
		// clear session data from the input forms
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.getMap().clear();
		return mapping.getInputForward();
	}

	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setAllParticleTypeParticles(session);
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String particleType = (String) theForm.get("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		SearchNanoparticleService searchtNanoparticleService = new SearchNanoparticleService();
		ParticleBean particle = searchtNanoparticleService.getGeneralInfo(
				particleName, particleType);
		theForm.set("particleName", particle.getSampleName());
		theForm.set("particleType", particle.getSampleType());
		theForm.set("keywords", StringUtils
				.join(particle.getKeywords(), "\r\n"));
		theForm.set("visibilities", particle.getVisibilityGroups());
		session.setAttribute("newParticleCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.findForward("update");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String particleType = (String) theForm.get("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		SearchNanoparticleService searchtNanoparticleService = new SearchNanoparticleService();
		ParticleBean particle = searchtNanoparticleService.getGeneralInfo(
				particleName, particleType);
		request.setAttribute("particle", particle);
		forward = mapping.findForward("view");
		request.getSession().setAttribute("newParticleCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
