package gov.nih.nci.cananolab.ui.particle;

/**
 * This class searches nanoparticle metadata based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchNanoparticleAction.java,v 1.3 2008-04-08 20:23:32 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;

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

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionForward forward = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleSource = (String) theForm.get("particleSource");

		String[] nanoparticleEntityTypes = (String[]) theForm
				.get("nanoparticleEntityTypes");
		String[] functionalizingEntityTypes = (String[]) theForm
				.get("functionalizingEntityTypes");
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		String[] characterizations = (String[]) theForm
				.get("characterizations");		
		String keywords = (String) theForm.get("keywords");		
		String summaries = theForm.getString("summaries");
		String[] keywordList = (keywords.length() == 0) ? null : keywords
				.split("\r\n");
		String[] summaryList = (summaries.length() == 0) ? null : summaries
				.split("\r\n");

		NanoparticleSampleService service = new NanoparticleSampleService();
		List<ParticleBean> particles = service.findNanoparticleSamplesBy(
				particleSource, nanoparticleEntityTypes,
				functionalizingEntityTypes, functionTypes, characterizations,
				keywordList, summaryList, user);

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
		InitNanoparticleSetup.getInstance().setAllNanoparticleSampleSources(
				request);
		InitNanoparticleSetup.getInstance().setFunctionTypes(request);
		InitNanoparticleSetup.getInstance().setFunctionalizingEntityTypes(
				request);
		InitNanoparticleSetup.getInstance().setNanoparticleEntityTypes(request);
		// InitParticleSetup.getInstance().setAllCharacterizationTypes(session);

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
