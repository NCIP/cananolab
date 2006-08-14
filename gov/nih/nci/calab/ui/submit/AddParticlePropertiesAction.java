package gov.nih.nci.calab.ui.submit;

/**
 * This class creates nanoparticle general information and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: AddParticlePropertiesAction.java,v 1.6 2006-08-14 17:59:23 pansu Exp $ */

import gov.nih.nci.calab.dto.particle.BuckeyballBean;
import gov.nih.nci.calab.dto.particle.DendrimerBean;
import gov.nih.nci.calab.dto.particle.FullereneBean;
import gov.nih.nci.calab.dto.particle.LiposomeBean;
import gov.nih.nci.calab.dto.particle.MetalParticleBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.particle.PolymerBean;
import gov.nih.nci.calab.dto.particle.QuantumDotBean;
import gov.nih.nci.calab.dto.particle.SurfaceGroupBean;
import gov.nih.nci.calab.service.submit.AddParticlePropertiesService;
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

public class AddParticlePropertiesAction extends AbstractDispatchAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		ParticleBean particle = null;
		if (particleType.equalsIgnoreCase("dendrimer")) {
			particle = (DendrimerBean) theForm.get("dendrimer");
		} else if (particleType.equalsIgnoreCase("polymer")) {
			particle = (PolymerBean) theForm.get("polymer");
		} else if (particleType.equalsIgnoreCase("liposome")) {
			particle = (LiposomeBean) theForm.get("liposome");
		} else if (particleType.equalsIgnoreCase("buckeyball")) {
			particle = (BuckeyballBean) theForm.get("buckyball");
		} else if (particleType.equalsIgnoreCase("fullerene")) {
			particle = (FullereneBean) theForm.get("fullerene");
		} else if (particleType.equalsIgnoreCase("quantum dot")) {
			particle = (QuantumDotBean) theForm.get("quantumDot");
		} else if (particleType.equalsIgnoreCase("metal particle")) {			
			particle = (MetalParticleBean) theForm.get("metalParticle");
		}
		AddParticlePropertiesService service = new AddParticlePropertiesService();
		service.addParticleProperties(particleType, particle);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleProperties");
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
			DendrimerBean particle = (DendrimerBean) theForm.get("dendrimer");
			updateSurfaceGroups(particle);
			theForm.set("dendrimer", particle);
			theForm.set("particlePage", mapping.findForward(
					particleType.toLowerCase()).getPath());
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

	public boolean loginRequired() {
		return true;
	}
}
