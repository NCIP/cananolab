package gov.nih.nci.cananolab.ui.particle;

/**
 * This class searches nanoparticle metadata based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchNanoparticleAction.java,v 1.9 2008-04-17 19:48:33 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.StringUtils;

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
		String[] particleSources = (String[]) theForm.get("particleSources");

		String[] nanoparticleEntityTypes = (String[]) theForm
				.get("nanoparticleEntityTypes");
		// convert nanoparticle entity display names into short class names
		String[] nanoparticleEntityClassNames = new String[nanoparticleEntityTypes.length];
		for (int i = 0; i < nanoparticleEntityTypes.length; i++) {
			nanoparticleEntityClassNames[i] = InitSetup.getInstance()
					.getObjectName(nanoparticleEntityTypes[i],
							session.getServletContext());
		}
		String[] functionalizingEntityTypes = (String[]) theForm
				.get("functionalizingEntityTypes");
		// convert functionalizing entity display names into short class names
		String[] functionalizingEntityClassNames = new String[functionalizingEntityTypes.length];
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			functionalizingEntityClassNames[i] = InitSetup.getInstance()
					.getObjectName(functionalizingEntityTypes[i],
							session.getServletContext());
		}
		String[] functionTypes = (String[]) theForm.get("functionTypes");
		// convert function display names into short class names
		String[] functionClassNames = new String[functionTypes.length];
		for (int i = 0; i < functionTypes.length; i++) {
			functionClassNames[i] = InitSetup.getInstance().getObjectName(
					functionTypes[i], session.getServletContext());
		}
		String[] characterizations = (String[]) theForm
				.get("characterizations");
		// convert characterization display names into short class names
		String[] charaClassNames = new String[characterizations.length];
		for (int i = 0; i < characterizations.length; i++) {
			charaClassNames[i] = InitSetup.getInstance().getObjectName(
					characterizations[i], session.getServletContext());
		}
		String texts = ((String) theForm.get("text")).trim();
		List<String> wordList = StringUtils.parseToWords(texts);
		String[] words = null;
		if (wordList != null) {
			words = new String[wordList.size()];
			wordList.toArray(words);
		}
		NanoparticleSampleService service = new NanoparticleSampleService();
		List<ParticleBean> particles = service.findNanoparticleSamplesBy(
				particleSources, nanoparticleEntityClassNames,
				functionalizingEntityClassNames, functionClassNames,
				charaClassNames, words, user);

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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().setNanoparticleSampleSources(
				request, user);
		InitNanoparticleSetup.getInstance().setFunctionTypes(request);
		InitNanoparticleSetup.getInstance().setFunctionalizingEntityTypes(
				request);
		InitNanoparticleSetup.getInstance().setNanoparticleEntityTypes(request);
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
