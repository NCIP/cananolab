package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleCompositionAction.java,v 1.13 2006-09-11 21:18:47 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComplexComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.MetalParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.QuantumDotComposition;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.ComplexParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.ComposingElementBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.MetalParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.characterization.composition.QuantumDotBean;
import gov.nih.nci.calab.dto.characterization.composition.SurfaceGroupBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

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

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		String viewTitle = (String) theForm.get("viewTitle");
		String description = (String) theForm.get("description");
		String characterizationSource = (String) theForm
				.get("characterizationSource");
		CompositionBean composition = null;
		if (particleType.equalsIgnoreCase(CananoConstants.DENDRIMER_TYPE)) {
			composition = (DendrimerBean) theForm.get("dendrimer");
		} else if (particleType.equalsIgnoreCase(CananoConstants.POLYMER_TYPE)) {
			composition = (PolymerBean) theForm.get("polymer");
		} else if (particleType.equalsIgnoreCase(CananoConstants.LIPOSOME_TYPE)) {
			composition = (LiposomeBean) theForm.get("liposome");
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.CARBON_NANOTUBE_TYPE)) {
			composition = (CarbonNanotubeBean) theForm.get("carbonNanotube");
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.FULLERENE_TYPE)) {
			composition = (FullereneBean) theForm.get("fullerene");
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.QUANTUM_DOT_TYPE)) {
			composition = (QuantumDotBean) theForm.get("quantumDot");
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {
			composition = (MetalParticleBean) theForm.get("metalParticle");
		} else if (particleType.equalsIgnoreCase(CananoConstants.EMULSION_TYPE)) {
			composition = (EmulsionBean) theForm.get("emulsion");
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.COMPLEX_PARTICLE_TYPE)) {
			composition = (ComplexParticleBean) theForm.get("complexParticle");
		}
		composition.setViewTitle(viewTitle);
		composition.setDescription(description);
		composition.setCharacterizationSource(characterizationSource);
		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleComposition(particleType, particleName, composition);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleComposition");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return forward;
	}

	/**
	 * Set up the input forms for adding data
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
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	private void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();
		theForm.set("dendrimer", new DendrimerBean());
		theForm.set("polymer", new PolymerBean());
		theForm.set("fullerene", new FullereneBean());
		theForm.set("carbonNanotube", new CarbonNanotubeBean());
		theForm.set("emulsion", new EmulsionBean());
		theForm.set("complexParticle", new ComplexParticleBean());
		theForm.set("liposome", new LiposomeBean());
		theForm.set("quantumDot", new QuantumDotBean());
		theForm.set("metalParticle", new MetalParticleBean());
		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("particlePage", mapping.findForward(
				particleType.toLowerCase()).getPath());
	}

	private void initSetup(HttpServletRequest request, DynaValidatorForm theForm) throws Exception  {
		HttpSession session=request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		if (particleType.equalsIgnoreCase(CananoConstants.DENDRIMER_TYPE)) {
			InitSessionSetup.getInstance().setAllDendrimerCores(session);
			InitSessionSetup.getInstance().setAllDendrimerSurfaceGroupNames(
					session);
		} else if (particleType.equalsIgnoreCase(CananoConstants.POLYMER_TYPE)) {
			InitSessionSetup.getInstance().setAllPolymerInitiators(session);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {
			InitSessionSetup.getInstance().setAllMetalCompositions(session);
		}
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
	}

	/**
	 * Set up the input forms for updating data
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

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		if (particleType.equalsIgnoreCase(CananoConstants.DENDRIMER_TYPE)) {			
			DendrimerBean dendrimer = new DendrimerBean(
					(DendrimerComposition) aChar);
			theForm.set("dendrimer", dendrimer);
		} else if (particleType.equalsIgnoreCase(CananoConstants.POLYMER_TYPE)) {			
			PolymerBean polymer = new PolymerBean((PolymerComposition) aChar);
			theForm.set("polymer", polymer);
		} else if (particleType.equalsIgnoreCase(CananoConstants.LIPOSOME_TYPE)) {
			LiposomeBean liposome = new LiposomeBean(
					(LiposomeComposition) aChar);
			theForm.set("liposome", liposome);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.FULLERENE_TYPE)) {
			FullereneBean fullerene = new FullereneBean(
					(FullereneComposition) aChar);
			theForm.set("fullerene", fullerene);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.CARBON_NANOTUBE_TYPE)) {
			CarbonNanotubeBean carbonNanotube = new CarbonNanotubeBean(
					(CarbonNanotubeComposition) aChar);
			theForm.set("carbonNanotube", carbonNanotube);
		} else if (particleType.equalsIgnoreCase(CananoConstants.EMULSION_TYPE)) {
			EmulsionBean emulsion = new EmulsionBean(
					(EmulsionComposition) aChar);
			theForm.set("emulsion", emulsion);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.COMPLEX_PARTICLE_TYPE)) {
			ComplexParticleBean complexParticle = new ComplexParticleBean(
					(ComplexComposition) aChar);
			theForm.set("complexParticle", complexParticle);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.QUANTUM_DOT_TYPE)) {
			QuantumDotBean quantumDot = new QuantumDotBean(
					(QuantumDotComposition) aChar);
			theForm.set("quantumDot", quantumDot);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {			
			MetalParticleBean metalParticle = new MetalParticleBean(
					(MetalParticleComposition) aChar);
			theForm.set("metalParticle", metalParticle);
		}
		theForm.set("characterizationId", compositionId);
		theForm.set("characterizationSource", aChar.getSource());
		theForm.set("viewTitle", aChar.getIdentificationName());
		theForm.set("description", aChar.getDescription());
		initSetup(request, theForm);
		
		return mapping.getInputForward();
	}

	/**
	 * Set up the input fields for read only view data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		CompositionBean composition = null;

		if (particleType.equalsIgnoreCase(CananoConstants.DENDRIMER_TYPE)) {
			composition = (DendrimerBean) theForm.get("dendrimer");
			// update surface group info for dendrimers
			updateSurfaceGroups((DendrimerBean) composition);
			theForm.set("dendrimer", (DendrimerBean) composition);
		} else if (particleType.equalsIgnoreCase(CananoConstants.POLYMER_TYPE)) {
			composition = (PolymerBean) theForm.get("polymer");
			updateComposingElements(composition);
			theForm.set("polymer", (PolymerBean) composition);
		} else if (particleType.equalsIgnoreCase(CananoConstants.LIPOSOME_TYPE)) {
			composition = (LiposomeBean) theForm.get("liposome");
			updateComposingElements(composition);
			theForm.set("liposome", (LiposomeBean) composition);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.FULLERENE_TYPE)) {
			composition = (FullereneBean) theForm.get("fullerene");
			updateComposingElements(composition);
			theForm.set("fullerene", (FullereneBean) composition);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.CARBON_NANOTUBE_TYPE)) {
			composition = (CarbonNanotubeBean) theForm.get("carbonNanotube");
			updateComposingElements(composition);
			theForm.set("carbonNanotube", (CarbonNanotubeBean) composition);
		} else if (particleType.equalsIgnoreCase(CananoConstants.EMULSION_TYPE)) {
			composition = (EmulsionBean) theForm.get("emulsion");
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.COMPLEX_PARTICLE_TYPE)) {
			composition = (ComplexParticleBean) theForm.get("complexParticle");
			updateComposingElements(composition);
			theForm.set("complexParticle", (ComplexParticleBean) composition);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.QUANTUM_DOT_TYPE)) {
			composition = (QuantumDotBean) theForm.get("quantumDot");
			String numberOfShells = ((QuantumDotBean) composition)
					.getNumberOfShells();
			String numberOfCoatings = ((QuantumDotBean) composition)
					.getNumberOfCoatings();
			try {
				int shellNum = Integer.parseInt(numberOfShells);
				List<ComposingElementBean> origShells = ((QuantumDotBean) composition)
						.getShells();
				List<ComposingElementBean> shells = updateComposingElements(
						origShells, shellNum);
				((QuantumDotBean) composition).setShells(shells);

				int coatingNum = Integer.parseInt(numberOfCoatings);
				List<ComposingElementBean> origCoatings = ((QuantumDotBean) composition)
						.getCoatings();
				List<ComposingElementBean> coatings = updateComposingElements(
						origCoatings, coatingNum);
				((QuantumDotBean) composition).setCoatings(coatings);
			} catch (Exception e) {
			}
			theForm.set("quantumDot", (QuantumDotBean) composition);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {
			composition = (MetalParticleBean) theForm.get("metalParticle");
			String numberOfShells = ((MetalParticleBean) composition)
					.getNumberOfShells();
			String numberOfCoatings = ((MetalParticleBean) composition)
					.getNumberOfCoatings();
			try {
				int shellNum = Integer.parseInt(numberOfShells);
				List<ComposingElementBean> origShells = ((MetalParticleBean) composition)
						.getShells();
				List<ComposingElementBean> shells = updateComposingElements(
						origShells, shellNum);
				((MetalParticleBean) composition).setShells(shells);

				int coatingNum = Integer.parseInt(numberOfCoatings);
				List<ComposingElementBean> origCoatings = ((MetalParticleBean) composition)
						.getCoatings();
				List<ComposingElementBean> coatings = updateComposingElements(
						origCoatings, coatingNum);
				((MetalParticleBean) composition).setCoatings(coatings);
			} catch (Exception e) {
			}
			theForm.set("metalParticle", (MetalParticleBean) composition);
		}
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Update surface groups for Dendrimer only
	 * 
	 * @param particle
	 */
	private void updateSurfaceGroups(DendrimerBean particle) {
		String numberOfSurfaceGroups = particle.getNumberOfSurfaceGroups();
		int surfaceGroupNum = Integer.parseInt(numberOfSurfaceGroups);
		List<SurfaceGroupBean> origSurfaceGroups = particle.getSurfaceGroups();
		int origNum = (origSurfaceGroups == null) ? 0 : origSurfaceGroups
				.size();
		List<SurfaceGroupBean> surfaceGroups = new ArrayList<SurfaceGroupBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < surfaceGroupNum; i++) {
				SurfaceGroupBean surfaceGroup = new SurfaceGroupBean();
				surfaceGroups.add(surfaceGroup);
			}
		}
		// use keep original surface group info
		else if (surfaceGroupNum <= origNum) {
			for (int i = 0; i < surfaceGroupNum; i++) {
				surfaceGroups.add((SurfaceGroupBean) origSurfaceGroups.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				surfaceGroups.add((SurfaceGroupBean) origSurfaceGroups.get(i));
			}
			for (int i = origNum; i < surfaceGroupNum; i++) {
				surfaceGroups.add(new SurfaceGroupBean());
			}
		}
		particle.setSurfaceGroups(surfaceGroups);
	}

	private List<ComposingElementBean> updateComposingElements(
			List<ComposingElementBean> origElements, int elementNum) {
		int origNum = (origElements == null) ? 0 : origElements.size();
		List<ComposingElementBean> elements = new ArrayList<ComposingElementBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < elementNum; i++) {
				ComposingElementBean monomer = new ComposingElementBean();
				elements.add(monomer);
			}
		}
		// use keep original element info
		else if (elementNum <= origNum) {
			for (int i = 0; i < elementNum; i++) {
				elements.add((ComposingElementBean) origElements.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				elements.add((ComposingElementBean) origElements.get(i));
			}
			for (int i = origNum; i < elementNum; i++) {
				elements.add(new ComposingElementBean());
			}
		}
		return elements;
	}

	private void updateComposingElements(CompositionBean composition) {
		String numberOfElements = composition.getNumberOfElements();
		int elementNum = Integer.parseInt(numberOfElements);
		List<ComposingElementBean> origElements = composition
				.getComposingElements();
		List<ComposingElementBean> elements = updateComposingElements(
				origElements, elementNum);
		composition.setComposingElements(elements);
	}

	public boolean loginRequired() {
		return true;
	}
}
