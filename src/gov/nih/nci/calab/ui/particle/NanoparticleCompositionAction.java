package gov.nih.nci.calab.ui.particle;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleCompositionAction.java,v 1.9 2007-11-29 19:20:06 pansu Exp $ */

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
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.calab.service.particle.NanoparticleService;
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		CompositionBean baseComposition = (CompositionBean) theForm
				.get("composition");

		CompositionBean composition = null;
		if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			composition = (DendrimerBean) theForm.get("dendrimer");
		} else if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			composition = (PolymerBean) theForm.get("polymer");
		} else if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_LIPOSOME_TYPE)) {
			composition = (LiposomeBean) theForm.get("liposome");
		} else if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_CARBON_NANOTUBE_TYPE)) {
			composition = (CarbonNanotubeBean) theForm.get("carbonNanotube");
		} else if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_FULLERENE_TYPE)) {
			composition = (FullereneBean) theForm.get("fullerene");
		} else if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_EMULSION_TYPE)) {
			composition = (EmulsionBean) theForm.get("emulsion");
		} else {
			composition = new CompositionBean();
		}
		composition.setParticle(particle);
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
		NanoparticleCompositionService service = new NanoparticleCompositionService();
		service.addParticleComposition(composition, particle.getSampleType());

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
				particle.getSampleId());
		if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			request.getSession().setAttribute("newDendrimerCreated", "true");
		} else if (particle.getSampleType().equalsIgnoreCase(
				CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			request.getSession().setAttribute("newPolymerCreated", "true");
		}
		request.setAttribute("theParticle", particle);
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
		String particleId = request.getParameter("particleId");
		NanoparticleService searchtNanoparticleService = new NanoparticleService();
		ParticleBean particle = searchtNanoparticleService
				.getParticleInfo(particleId);
		theForm.set("particle", particle);
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
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
		String particleId = request.getParameter("particleId");
		NanoparticleService searchtNanoparticleService = new NanoparticleService();
		ParticleBean particle = searchtNanoparticleService
				.getParticleInfo(particleId);
		theForm.set("particle", particle);

		String compositionId = request.getParameter("characterizationId");
		NanoparticleCompositionService service = new NanoparticleCompositionService();
		CompositionBean composition = service.getCompositionBy(compositionId);
		if (composition == null) {
			throw new Exception(
					"This characterization no longer exists in the database.  Please log in again to refresh.");
		}

		initSetup(request, theForm);
		theForm.set("composition", composition);
		if (composition instanceof DendrimerBean) {
			theForm.set("dendrimer", composition);
		} else if (composition instanceof PolymerBean) {
			theForm.set("polymer", composition);
		} else if (composition instanceof LiposomeBean) {
			theForm.set("liposome", composition);
		} else if (composition instanceof FullereneBean) {
			theForm.set("fullerene", composition);
		} else if (composition instanceof CarbonNanotubeBean) {
			theForm.set("carbonNanotube", composition);
		} else if (composition instanceof EmulsionBean) {
			theForm.set("emulsion", composition);
		}
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}

	public ActionForward removeSurfaceGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String indexStr = request.getParameter("compInd");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}

	public ActionForward removeComposingElement(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String indexStr = request.getParameter("compInd");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		String strCharId = request.getParameter("characterizationId");

		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.deleteCharacterizations(new String[] { strCharId });

		// signal the session that characterization has been changed
		request.getSession().setAttribute("newCharacterizationCreated", "true");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());

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
