package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleCompositionAction.java,v 1.19 2006-12-03 17:45:44 zengje Exp $ */

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
import gov.nih.nci.calab.dto.characterization.composition.BaseCoreShellCoatingBean;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.ComplexParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.ComposingElementBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.MetalParticleBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.dto.characterization.composition.QuantumDotBean;
import gov.nih.nci.calab.dto.characterization.composition.SurfaceGroupBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

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

public class NanoparticleCompositionAction extends BaseCharacterizationAction {

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

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) session.getAttribute("user");
		Date date = new Date();
		composition.setCreatedBy(user.getLoginName());
		composition.setCreatedDate(date);

		session.setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleComposition(particleType, particleName, composition);

		// In case there is other type of branch, generation, etc created during the creationg and update
		// InitSessionSetup.setSideParticleMenu() clear up the session attribute "newCharcterizationCreated"
		// So, it is better to refresh the particle type related session variable here.
		if (particleType.equalsIgnoreCase(CananoConstants.DENDRIMER_TYPE)) {
			InitSessionSetup.getInstance().setAllDendrimerCores(session);
			InitSessionSetup.getInstance().setAllDendrimerSurfaceGroupNames(
					session);
			InitSessionSetup.getInstance().setAllDendrimerBranches(session);
			InitSessionSetup.getInstance().setAllDendrimerGenerations(session);
		} else if (particleType.equalsIgnoreCase(CananoConstants.POLYMER_TYPE)) {
			InitSessionSetup.getInstance().setAllPolymerInitiators(session);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {
			InitSessionSetup.getInstance().setAllMetalCompositions(session);
		}
		
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleComposition");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return forward;
	}

	protected void clearMap(HttpSession session, DynaValidatorForm theForm,
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

	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();

		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		if (particleType.equalsIgnoreCase(CananoConstants.DENDRIMER_TYPE)) {
			InitSessionSetup.getInstance().setAllDendrimerCores(session);
			InitSessionSetup.getInstance().setAllDendrimerSurfaceGroupNames(
					session);
			InitSessionSetup.getInstance().setAllDendrimerBranches(session);
			InitSessionSetup.getInstance().setAllDendrimerGenerations(session);
		} else if (particleType.equalsIgnoreCase(CananoConstants.POLYMER_TYPE)) {
			InitSessionSetup.getInstance().setAllPolymerInitiators(session);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {
			InitSessionSetup.getInstance().setAllMetalCompositions(session);
		}
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
			updateCoreShellsCoatings((QuantumDotBean) composition);
			theForm.set("quantumDot", (QuantumDotBean) composition);
		} else if (particleType
				.equalsIgnoreCase(CananoConstants.METAL_PARTICLE_TYPE)) {
			composition = (MetalParticleBean) theForm.get("metalParticle");
			updateCoreShellsCoatings((MetalParticleBean) composition);
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

	/**
	 * Update shells and coatings for MetalParticle and QuantumDot
	 * 
	 * @param composition
	 * 
	 */
	private void updateCoreShellsCoatings(BaseCoreShellCoatingBean composition) {
		int shellNum = Integer.parseInt(composition.getNumberOfShells());
		int coatingNum = Integer.parseInt(composition.getNumberOfCoatings());

		List<ComposingElementBean> shells = new ArrayList<ComposingElementBean>();
		List<ComposingElementBean> coatings = new ArrayList<ComposingElementBean>();
		ComposingElementBean core = null;

		List<ComposingElementBean> origShells = composition.getShells();
		shells = updateElements(origShells, shellNum);
		composition.setShells(shells);
		List<ComposingElementBean> origCoatings = composition.getCoatings();
		coatings = updateElements(origCoatings, coatingNum);
		composition.setCoatings(coatings);
		core = composition.getCore();
		List<ComposingElementBean> elements = new ArrayList<ComposingElementBean>();
		elements.add(core);
		elements.addAll(shells);
		elements.addAll(coatings);
		composition.setComposingElements(elements);
	}

	private List<ComposingElementBean> updateElements(
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
		List<ComposingElementBean> elements = updateElements(origElements,
				elementNum);
		composition.setComposingElements(elements);
	}

	//not needed for composition		
	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		// TODO Auto-generated method stub
	}

	//not needed for composition
	protected void setLoadFileRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
	}
}
