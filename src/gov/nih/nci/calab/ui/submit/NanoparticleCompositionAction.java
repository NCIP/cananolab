package gov.nih.nci.calab.ui.submit;

/**
 * This class creates nanoparticle general information and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleCompositionAction.java,v 1.3 2006-08-26 01:53:47 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.composition.BuckeyballBean;
import gov.nih.nci.calab.dto.characterization.composition.ComposingElementBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
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
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		CharacterizationBean composition = null;
		if (particleType.equalsIgnoreCase("dendrimer")) {
			composition = (DendrimerBean) theForm.get("dendrimer");
		} else if (particleType.equalsIgnoreCase("polymer")) {
			composition = (PolymerBean) theForm.get("polymer");
		} else if (particleType.equalsIgnoreCase("liposome")) {
			composition = (LiposomeBean) theForm.get("liposome");
		} else if (particleType.equalsIgnoreCase("buckeyball")) {
			composition = (BuckeyballBean) theForm.get("buckyball");
		} else if (particleType.equalsIgnoreCase("fullerene")) {
			composition = (FullereneBean) theForm.get("fullerene");
		} else if (particleType.equalsIgnoreCase("quantum dot")) {
			composition = (QuantumDotBean) theForm.get("quantumDot");
		} else if (particleType.equalsIgnoreCase("metal particle")) {			
			composition = (MetalParticleBean) theForm.get("metalParticle");
		}
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleComposition(particleType, composition);

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
		HttpSession session=request.getSession();
		
		if (particleType.equalsIgnoreCase("dendrimer")) {
			InitSessionSetup.getInstance().setAllDendrimerCores(session);
			InitSessionSetup.getInstance().setAllDendrimerSurfaceGroupNames(session);
		} else if (particleType.equalsIgnoreCase("metal particle")) {
			InitSessionSetup.getInstance().setAllMetalCompositions(session);
		}
		else if (particleType.equalsIgnoreCase("polymer")) {
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

		// update surface group info for dendrimers
		if (particleType.equalsIgnoreCase("dendrimer")) {
			DendrimerBean composition = (DendrimerBean) theForm.get("dendrimer");
			updateSurfaceGroups(composition);
			theForm.set("dendrimer", composition);
		}
		// update monomer info on polymers
		else if (particleType.equalsIgnoreCase("polymer")) {
			PolymerBean composition = (PolymerBean) theForm.get("polymer");
			updateComposingElements(composition);
			theForm.set("polymer", composition);
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

	private void updateComposingElements(CompositionBean composition) {
		String numberOfElements = composition.getNumberOfElements();
		int elementNum = Integer.parseInt(numberOfElements);
		List<ComposingElementBean> origMonomers = composition.getComposingElements();
		int origNum = (origMonomers == null) ? 0 : origMonomers
				.size();
		List<ComposingElementBean> elements = new ArrayList<ComposingElementBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < elementNum; i++) {
				ComposingElementBean monomer = new ComposingElementBean();
				elements.add(monomer);
			}
		}
		// use keep original monomer info
		else if (elementNum <= origNum) {
			for (int i = 0; i < elementNum; i++) {
				elements.add((ComposingElementBean) origMonomers.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				elements.add((ComposingElementBean) origMonomers.get(i));
			}
			for (int i = origNum; i < elementNum; i++) {
				elements.add(new ComposingElementBean());
			}
		}
		composition.setComposingElements(elements);
	}

	public boolean loginRequired() {
		return true;
	}
}
