package gov.nih.nci.cananolab.ui.particle;

/**
 * This class searches nanoparticle metadata based on user supplied criteria
 * 
 * @author pansu
 */

/* CVS $Id: SearchNanoparticleAction.java,v 1.25 2008-06-16 16:03:40 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
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

		String[] nanoparticleEntityTypes = new String[0];
		String[] functionalizingEntityTypes = new String[0];
		String[] functionTypes = new String[0];
		String[] characterizations = new String[0];
		String texts = "";
		String[] searchLocations = new String[0];
		
		if (theForm != null) {
			nanoparticleEntityTypes = (String[]) theForm
					.get("nanoparticleEntityTypes");
			functionalizingEntityTypes = (String[]) theForm
					.get("functionalizingEntityTypes");
			functionTypes = (String[]) theForm.get("functionTypes");
			characterizations = (String[]) theForm.get("characterizations");
			texts = ((String) theForm.get("text")).trim();
			searchLocations = (String[]) theForm.get("searchLocations");
		}

		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (searchLocations[0].indexOf("~") != -1 && 
			gridNodeHostStr != null && gridNodeHostStr.trim().length() > 0) {
			searchLocations = gridNodeHostStr.split("~");
		}
	
		// convert nanoparticle entity display names into short class names and
		// other types
		List<String> nanoparticleEntityClassNames = new ArrayList<String>();
		List<String> otherNanoparticleEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanoparticleEntityTypes.length; i++) {
			String className = InitSetup.getInstance().getObjectName(
					nanoparticleEntityTypes[i], session.getServletContext());
			if (className.length() == 0) {
				className = "OtherNanoparticleEntity";
				otherNanoparticleEntityTypes.add(nanoparticleEntityTypes[i]);
			} else {
				nanoparticleEntityClassNames.add(className);
			}
		}

		// convert functionalizing entity display names into short class names
		// and other types
		List<String> functionalizingEntityClassNames = new ArrayList<String>();
		List<String> otherFunctionalizingTypes = new ArrayList<String>();
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			String className = InitSetup.getInstance().getObjectName(
					functionalizingEntityTypes[i], session.getServletContext());
			if (className.length() == 0) {
				className = "OtherFunctionalizingEntity";
				otherFunctionalizingTypes.add(functionalizingEntityTypes[i]);
			} else {
				functionalizingEntityClassNames.add(className);
			}
		}

		// convert function display names into short class names and other types
		List<String> functionClassNames = new ArrayList<String>();
		List<String> otherFunctionTypes = new ArrayList<String>();
		for (int i = 0; i < functionTypes.length; i++) {
			String className = InitSetup.getInstance().getObjectName(
					functionTypes[i], session.getServletContext());
			if (className.length() == 0) {
				className = "OtherFunction";
				otherFunctionTypes.add(functionTypes[i]);
			} else {
				functionClassNames.add(className);
			}
		}

		// convert characterization display names into short class names
		String[] charaClassNames = new String[characterizations.length];
		for (int i = 0; i < characterizations.length; i++) {
			charaClassNames[i] = InitSetup.getInstance().getObjectName(
					characterizations[i], session.getServletContext());
		}

		List<String> wordList = StringUtils.parseToWords(texts);
		String[] words = null;
		if (wordList != null) {
			words = new String[wordList.size()];
			wordList.toArray(words);
		}

		List<ParticleBean> foundParticles = new ArrayList<ParticleBean>();
		for (String location : searchLocations) {
			List<ParticleBean> particles = null;
			NanoparticleSampleService service = null;
			if (location.equals("local")) {
				service = new NanoparticleSampleServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				service = new NanoparticleSampleServiceRemoteImpl(serviceUrl);
			}
			particles = service.findNanoparticleSamplesBy(particleSource,
					nanoparticleEntityClassNames.toArray(new String[0]),
					otherNanoparticleEntityTypes.toArray(new String[0]),
					functionalizingEntityClassNames.toArray(new String[0]),
					otherFunctionalizingTypes.toArray(new String[0]),
					functionClassNames.toArray(new String[0]),
					otherFunctionTypes.toArray(new String[0]), charaClassNames,
					words);
			for (ParticleBean particle : particles) {
				particle.setLocation(location);
			}
			if (location.equals("local")) {
				List<ParticleBean> filteredParticles = new ArrayList<ParticleBean>();
				// set visibility
				for (ParticleBean particle : particles) {
					service.retrieveVisibility(particle, user);
					if (!particle.isHidden()) {
						filteredParticles.add(particle);
					}
				}
				foundParticles.addAll(filteredParticles);
			} else {
				foundParticles.addAll(particles);
			}
		}
		if (foundParticles != null && !foundParticles.isEmpty()) {
			request.setAttribute("particles", foundParticles);
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

		String[] selectedLocations = new String[0];
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (gridNodeHostStr != null && gridNodeHostStr.length() > 0) {
			selectedLocations = gridNodeHostStr.split("~");
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("searchLocations", selectedLocations);

		if ("local".equals(selectedLocations[0]) &&
				selectedLocations.length == 1) {
				
			InitCompositionSetup.getInstance()
					.getNanoparticleEntityTypes(request);
			
			InitCompositionSetup.getInstance().getFunctionalizingEntityTypes(request);
			InitCompositionSetup.getInstance().getFunctionTypes(request);
		} else {
			InitCompositionSetup.getInstance()
					.getDefaultNanoparticleEntityTypes(request);
			
			InitCompositionSetup.getInstance().getDefaultFunctionalizingEntityTypes(request);
			InitCompositionSetup.getInstance().getDefaultFunctionTypes(request);
		}

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
