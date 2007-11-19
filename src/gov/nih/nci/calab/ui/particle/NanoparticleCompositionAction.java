package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleCompositionAction.java,v 1.5 2007-11-19 22:22:17 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.ComposingElementBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.characterization.composition.SurfaceGroupBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.particle.SearchNanoparticleService;
import gov.nih.nci.calab.service.particle.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
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

public class NanoparticleCompositionAction extends AbstractDispatchAction {

	/**
	 * Add or update the data to database
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		CompositionBean baseComposition = (CompositionBean) theForm
				.get("composition");

		CompositionBean composition = null;
		if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			composition = (DendrimerBean) theForm.get("dendrimer");
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			composition = (PolymerBean) theForm.get("polymer");
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_LIPOSOME_TYPE)) {
			composition = (LiposomeBean) theForm.get("liposome");
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_CARBON_NANOTUBE_TYPE)) {
			composition = (CarbonNanotubeBean) theForm.get("carbonNanotube");
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_FULLERENE_TYPE)) {
			composition = (FullereneBean) theForm.get("fullerene");
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_EMULSION_TYPE)) {
			composition = (EmulsionBean) theForm.get("emulsion");
		} else {
			composition = new CompositionBean();
		}
		composition.setParticleName(particleName);
		composition.setParticleType(particleType);
		composition.setCreatedBy(baseComposition.getCreatedBy());
		composition.setCreatedDate(baseComposition.getCreatedDate());
		composition.setCharacterizationSource(baseComposition
				.getCharacterizationSource());
		composition.setViewTitle(baseComposition.getViewTitle());
		composition.setDescription(baseComposition.getDescription());
		composition
				.setComposingElements(baseComposition.getComposingElements());
		composition.setId(baseComposition.getId());

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) session.getAttribute("user");
		Date date = new Date();
		composition.setCreatedBy(user.getLoginName());
		composition.setCreatedDate(date);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleComposition(composition, particleType);

		// In case there is other type of branch, generation, etc created during
		// the creationg and update
		// InitSessionSetup.setSideParticleMenu() clear up the session attribute
		// "newCharcterizationCreated"
		// So, it is better to refresh the particle type related session
		// variable here.

		InitParticleSetup.getInstance().setAllCompositionDropdowns(session);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleComposition");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			request.getSession().setAttribute("newDendrimerCreated", "true");
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			request.getSession().setAttribute("newPolymerCreated", "true");
		}

		return forward;
	}

	/**
	 * Set up the input form for adding new characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		clearMap(session, theForm);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	/**
	 * Prepare the form for viewing existing characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionBean composition = (CompositionBean) theForm
				.get("composition");
		// update editable dropdowns
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				composition.getCharacterizationSource(),
				"characterizationSources");

		PolymerBean polymer = (PolymerBean) theForm.get("polymer");
		updatePolymerEditable(session, polymer);

		DendrimerBean dendrimer = (DendrimerBean) theForm.get("dendrimer");
		updateDendrimerEditable(session, dendrimer);

		return mapping.findForward("setup");
	}

	protected void clearMap(HttpSession session, DynaValidatorForm theForm)
			throws Exception {
		// clear session data from the input forms
		theForm.set("dendrimer", new DendrimerBean());
		theForm.set("polymer", new PolymerBean());
		theForm.set("fullerene", new FullereneBean());
		theForm.set("carbonNanotube", new CarbonNanotubeBean());
		theForm.set("emulsion", new EmulsionBean());
		theForm.set("liposome", new LiposomeBean());
		theForm.set("composition", new CompositionBean());
	}

	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();
		clearMap(session, theForm);
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitParticleSetup.getInstance().setAllCompositionDropdowns(session);
		InitParticleSetup.getInstance()
				.setAllCharacterizationDropdowns(session);
	}

	/**
	 * Set up the input forms for updating data, overwrite parent
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String compositionId = (String) theForm.get("characterizationId");

		SearchNanoparticleService service = new SearchNanoparticleService();
		Characterization aChar = service.getCharacterizationBy(compositionId);
		if (aChar == null) {
			throw new Exception(
					"This characterization no longer exists in the database.  Please log in again to refresh.");
		}
		// HttpSession session = request.getSession();
		initSetup(request, theForm);
		CompositionBean composition = new CompositionBean(
				(ParticleComposition) aChar);
		theForm.set("composition", composition);
		if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			DendrimerBean dendrimer = new DendrimerBean(
					(DendrimerComposition) aChar);
			theForm.set("dendrimer", dendrimer);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			PolymerBean polymer = new PolymerBean((PolymerComposition) aChar);
			theForm.set("polymer", polymer);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_LIPOSOME_TYPE)) {
			LiposomeBean liposome = new LiposomeBean(
					(LiposomeComposition) aChar);
			theForm.set("liposome", liposome);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_FULLERENE_TYPE)) {
			FullereneBean fullerene = new FullereneBean(
					(FullereneComposition) aChar);
			theForm.set("fullerene", fullerene);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_CARBON_NANOTUBE_TYPE)) {
			CarbonNanotubeBean carbonNanotube = new CarbonNanotubeBean(
					(CarbonNanotubeComposition) aChar);
			theForm.set("carbonNanotube", carbonNanotube);
		} else if (particleType
				.equalsIgnoreCase(CaNanoLabConstants.COMPOSITION_EMULSION_TYPE)) {
			EmulsionBean emulsion = new EmulsionBean(
					(EmulsionComposition) aChar);
			theForm.set("emulsion", emulsion);
		}
		theForm.set("characterizationId", compositionId);
		return mapping.findForward("setup");
	}

	public ActionForward addSurfaceGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		DendrimerBean dendrimer = (DendrimerBean) theForm.get("dendrimer");
		List<SurfaceGroupBean> origSurfaceGroups = dendrimer.getSurfaceGroups();
		int origNum = (origSurfaceGroups == null) ? 0 : origSurfaceGroups
				.size();
		List<SurfaceGroupBean> surfaceGroups = new ArrayList<SurfaceGroupBean>();
		for (int i = 0; i < origNum; i++) {
			surfaceGroups.add(origSurfaceGroups.get(i));
		}
		// add a new one
		surfaceGroups.add(new SurfaceGroupBean());
		dendrimer.setSurfaceGroups(surfaceGroups);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward removeSurfaceGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String indexStr = (String) request.getParameter("groupInd");
		int ind = Integer.parseInt(indexStr);
		DendrimerBean dendrimer = (DendrimerBean) theForm.get("dendrimer");
		List<SurfaceGroupBean> origSurfaceGroups = dendrimer.getSurfaceGroups();
		int origNum = (origSurfaceGroups == null) ? 0 : origSurfaceGroups
				.size();
		List<SurfaceGroupBean> surfaceGroups = new ArrayList<SurfaceGroupBean>();
		for (int i = 0; i < origNum; i++) {
			surfaceGroups.add(origSurfaceGroups.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			surfaceGroups.remove(ind);
		}
		dendrimer.setSurfaceGroups(surfaceGroups);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward addComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionBean comp = (CompositionBean) theForm.get("composition");
		List<ComposingElementBean> origElements = comp.getComposingElements();
		int origNum = (origElements == null) ? 0 : origElements.size();
		List<ComposingElementBean> elements = new ArrayList<ComposingElementBean>();
		for (int i = 0; i < origNum; i++) {
			elements.add(origElements.get(i));
		}
		// add a new one
		elements.add(new ComposingElementBean());
		comp.setComposingElements(elements);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward removeComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = (String) request.getParameter("elementInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CompositionBean comp = (CompositionBean) theForm.get("composition");
		List<ComposingElementBean> origElements = comp.getComposingElements();
		int origNum = (origElements == null) ? 0 : origElements.size();

		List<ComposingElementBean> elements = new ArrayList<ComposingElementBean>();
		for (int i = 0; i < origNum; i++) {
			elements.add(origElements.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			elements.remove(ind);
		}
		comp.setComposingElements(elements);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public boolean loginRequired() {
		return true;
	}

	private void updateDendrimerEditable(HttpSession session,
			DendrimerBean dendrimer) throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				dendrimer.getGeneration(), "allDendrimerGenerations");
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				dendrimer.getBranch(), "allDendrimerBranches");
		for (SurfaceGroupBean surfaceGroup : dendrimer.getSurfaceGroups()) {
			InitSessionSetup.getInstance().updateEditableDropdown(session,
					surfaceGroup.getName(), "allDendrimerSurfaceGroupNames");
		}
	}

	private void updatePolymerEditable(HttpSession session, PolymerBean polymer)
			throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				polymer.getInitiator(), "allPolymerInitiators");
	}

	public ActionForward deleteConfirmed(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		String strCharId = theForm.getString("characterizationId");

		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.deleteCharacterizations(particleName, particleType,
				new String[] { strCharId });

		// signal the session that characterization has been changed
		request.getSession().setAttribute("newCharacterizationCreated", "true");

		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.delete.characterization");
		msgs.add("message", msg);
		saveMessages(request, msgs);

		return mapping.findForward("success");
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}
