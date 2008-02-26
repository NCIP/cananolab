package gov.nih.nci.calab.ui.particle;

/**
 * This class searches nanoparticle metadata based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchNanoparticleAction.java,v 1.5 2008-02-26 18:49:22 cais Exp $ */

import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.ReportBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.service.particle.NanoparticleService;
import gov.nih.nci.calab.service.protocol.SearchProtocolService;
import gov.nih.nci.calab.service.report.SearchReportService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.io.PrintWriter;
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

public class SearchNanoparticleAction extends AbstractDispatchAction {
	public ActionForward publicCounts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		String particleSource = "";
		String particleType = "";
		String[] functionTypes = new String[0];
		String[] characterizations = new String[0];
		String keywordType = "";
		
		String summaryType = "";
		String[] keywordList = null;
		String[] summaryList = null;

		NanoparticleService searchParticleService = new NanoparticleService();
		List<ParticleBean> particles = searchParticleService.basicSearch(
				particleSource, particleType, functionTypes, characterizations,
				keywordList, keywordType, summaryList, summaryType, user);

		int particleCount = particles.size();
		
		//report count
		String reportTitle = "";
		String reportType = "";
		SearchReportService searchReportService = new SearchReportService();
		List<ReportBean> reports = searchReportService.searchReports(
				reportTitle, reportType, particleType, functionTypes, user);
		int reportCount = reports.size();
		
		//protocol count
		String fileTitle = "";
		String protocolType = "";
		String protocolName = "";
		SearchProtocolService searchProtocolService = new SearchProtocolService();
		List<ProtocolFileBean> protocols = searchProtocolService
				.searchProtocols(fileTitle, protocolType, protocolName, user);
		int protocolCount = protocols.size();
		
		PrintWriter out = response.getWriter();
	    out.print(particleCount + "\t" + reportCount + "\t" + protocolCount);
		return null;
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleSource = (String) theForm.get("particleSource");
		String particleType = (String) theForm.get("particleType");
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		String[] characterizations = (String[]) theForm
				.get("characterizations");
		String keywordType = (String) theForm.get("keywordType");
		String keywords = (String) theForm.get("keywords");

		String summaryType = theForm.getString("summaryType");
		String summaries = theForm.getString("summaries");
		String[] keywordList = (keywords.length() == 0) ? null : keywords
				.split("\r\n");
		String[] summaryList = (summaries.length() == 0) ? null : summaries
				.split("\r\n");

		NanoparticleService searchParticleService = new NanoparticleService();
		List<ParticleBean> particles = searchParticleService.basicSearch(
				particleSource, particleType, functionTypes, characterizations,
				keywordList, keywordType, summaryList, summaryType, user);

		if (particles != null && !particles.isEmpty()) {
			request.setAttribute("particles", particles);
			forward = mapping.findForward("success");
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchNanoparticle.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);

			forward = mapping.getInputForward();
		}

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitParticleSetup.getInstance().setAllParticleSources(session);
		InitParticleSetup.getInstance().setAllFunctionTypes(session);
		InitParticleSetup.getInstance().setAllCharacterizationTypes(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);

		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return true;
	}
}
