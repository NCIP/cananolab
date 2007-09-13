package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up the nanoparticle general information page and allows users to submit/update
 * the general information.  
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleGeneralInfoAction.java,v 1.25 2007-09-13 22:12:57 pansu Exp $ */

import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.search.SearchReportService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		String keywords = (String) theForm.get("keywords");
		String[] visibilities = (String[]) theForm.get("visibilities");
		String[] keywordList = (keywords.length() == 0) ? null : keywords
				.split("\r\n");
		SubmitNanoparticleService submitNanoparticleService = new SubmitNanoparticleService();
		ParticleBean particleBean=submitNanoparticleService.addParticleGeneralInfo(particleType,
				particleName, keywordList, visibilities);
		HttpSession session = request.getSession();		
		//display default visible groups
		List<String > visList = new ArrayList<String>();
		visList.addAll(Arrays.asList(CaNanoLabConstants.VISIBLE_GROUPS));
		visList.addAll(Arrays.asList(visibilities));	
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.createNanoparticle.secure", StringUtils.join(
						visList, ", "));
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		session.setAttribute("newParticleCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		request.getSession().setAttribute("newRunFileCreated", "true");
		request.setAttribute("particleSource", particleBean.getSampleSource());
		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);
		InitSessionSetup.getInstance().clearInventorySession(session);

		InitSessionSetup.getInstance().setParticleTypeParticles(session);
		InitSessionSetup.getInstance().setAllVisibilityGroups(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
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
		theForm.set("particleSource", particle.getSampleSource());
		theForm.set("keywords", StringUtils
				.join(particle.getKeywords(), "\r\n"));
		theForm.set("visibilities", particle.getVisibilityGroups());
		session.setAttribute("newParticleCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		request.getSession().setAttribute("newRunFileCreated", "true");
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
		SearchNanoparticleService searchNanoparticleService = new SearchNanoparticleService();
		ParticleBean particle = searchNanoparticleService.getGeneralInfo(
				particleName, particleType);
		// for disclaimer report list
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SearchReportService searchReportService = new SearchReportService();
		Collection<LabFileBean> reports = searchReportService.getReportByParticle(particleName,particleType, user);
		request.setAttribute("particleReports", reports);
		
		request.setAttribute("particle", particle);
		forward = mapping.findForward("view");
		request.getSession().setAttribute("newParticleCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		request.getSession().setAttribute("newParticleCreated", "true");
		return forward;
	}
	
	public ActionForward viewDisclaimer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String particleType = (String) theForm.get("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		SearchReportService searchReportService = new SearchReportService();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Collection<LabFileBean> reports = searchReportService.getReportByParticle(particleName,particleType, user);
		request.setAttribute("particleReports", reports);
		forward = mapping.findForward("viewDisclaimer");
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}
}
