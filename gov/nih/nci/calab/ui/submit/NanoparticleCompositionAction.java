package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up different input forms for different types of physical composition,
 * and allow users to submit data for physical compositions and update composing elements of each
 * physical composition.
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleCompositionAction.java,v 1.7 2006-09-05 21:31:38 pansu Exp $ */

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
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
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
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		CompositionBean composition = null;
		if (particleType.equalsIgnoreCase("dendrimer")) {
			composition = (DendrimerBean) theForm.get("dendrimer");
		} else if (particleType.equalsIgnoreCase("polymer")) {
			composition = (PolymerBean) theForm.get("polymer");
		} else if (particleType.equalsIgnoreCase("liposome")) {
			composition = (LiposomeBean) theForm.get("liposome");
		} else if (particleType.equalsIgnoreCase("carbon nanotube")) {
			composition = (CarbonNanotubeBean) theForm.get("carbonNanotube");
		} else if (particleType.equalsIgnoreCase("fullerene")) {
			composition = (FullereneBean) theForm.get("fullerene");
		} else if (particleType.equalsIgnoreCase("quantum dot")) {
			composition = (QuantumDotBean) theForm.get("quantumDot");
		} else if (particleType.equalsIgnoreCase("metal particle")) {
			composition = (MetalParticleBean) theForm.get("metalParticle");
		} else if (particleType.equalsIgnoreCase("emulsion")) {
			composition = (EmulsionBean) theForm.get("emulsion");
		} else if (particleType.equalsIgnoreCase("complex particle")) {
			composition = (ComplexParticleBean) theForm.get("complexParticle");
		}
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleComposition(particleType, particleName, composition);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleComposition");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		HttpSession session = request.getSession();

		if (particleType.equalsIgnoreCase("dendrimer")) {
			InitSessionSetup.getInstance().setAllDendrimerCores(session);
			InitSessionSetup.getInstance().setAllDendrimerSurfaceGroupNames(
					session);
		} else if (particleType.equalsIgnoreCase("metal particle")) {
			InitSessionSetup.getInstance().setAllMetalCompositions(session);
		} else if (particleType.equalsIgnoreCase("polymer")) {
			InitSessionSetup.getInstance().setAllPolymerInitiators(session);
		}
		theForm.set("particlePage", mapping.findForward(
				particleType.toLowerCase()).getPath());
		return mapping.getInputForward();
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");

		CompositionBean composition = null;

		if (particleType.equalsIgnoreCase("dendrimer")) {
			composition = (DendrimerBean) theForm.get("dendrimer");
			// update surface group info for dendrimers
			updateSurfaceGroups((DendrimerBean) composition);
			theForm.set("dendrimer", (DendrimerBean) composition);
		} else if (particleType.equalsIgnoreCase("polymer")) {
			composition = (PolymerBean) theForm.get("polymer");
			updateComposingElements(composition);
			theForm.set("polymer", (PolymerBean) composition);
		} else if (particleType.equalsIgnoreCase("liposome")) {
			composition = (LiposomeBean) theForm.get("liposome");
			updateComposingElements(composition);
			theForm.set("liposome", (LiposomeBean) composition);
		} else if (particleType.equalsIgnoreCase("fullerene")) {
			composition = (FullereneBean) theForm.get("fullerene");
			updateComposingElements(composition);
			theForm.set("fullerene", (FullereneBean) composition);
		} else if (particleType.equalsIgnoreCase("carbon nanotube")) {
			composition = (CarbonNanotubeBean) theForm.get("carbonNanotube");
			updateComposingElements(composition);
			theForm.set("carbonNanotube", (CarbonNanotubeBean) composition);
		} else if (particleType.equalsIgnoreCase("emulsion")) {
			composition = (EmulsionBean) theForm.get("emulsion");
		} else if (particleType.equalsIgnoreCase("complex particle")) {
			composition = (ComplexParticleBean) theForm.get("complexParticle");
			updateComposingElements(composition);
			theForm.set("complexParticle", (ComplexParticleBean) composition);
		} else if (particleType.equalsIgnoreCase("quantum dot")) {
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
				((MetalParticleBean) composition).setShells(shells);

				int coatingNum = Integer.parseInt(numberOfCoatings);
				List<ComposingElementBean> origCoatings = ((QuantumDotBean) composition)
						.getCoatings();
				List<ComposingElementBean> coatings = updateComposingElements(
						origCoatings, coatingNum);
				((QuantumDotBean) composition).setCoatings(coatings);
			} catch (Exception e) {
			}
			theForm.set("metalParticle", (MetalParticleBean) composition);
		} else if (particleType.equalsIgnoreCase("metal particle")) {
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

		return mapping.getInputForward();
	}

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
