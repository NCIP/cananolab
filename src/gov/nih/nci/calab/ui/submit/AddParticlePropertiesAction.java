package gov.nih.nci.calab.ui.submit;

/**
 * This class creates nanoparticle general information and assigns visibility  
 *  
 * @author pansu
 */

/* CVS $Id: AddParticlePropertiesAction.java,v 1.4 2006-08-11 21:31:53 pansu Exp $ */

import gov.nih.nci.calab.dto.particle.BuckeyballBean;
import gov.nih.nci.calab.dto.particle.DendrimerBean;
import gov.nih.nci.calab.dto.particle.FullereneBean;
// import gov.nih.nci.calab.dto.particle.GoldParticleBean;
import gov.nih.nci.calab.dto.particle.LiposomeBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.dto.particle.PolymerBean;
import gov.nih.nci.calab.dto.particle.QuantumDotBean;
import gov.nih.nci.calab.service.submit.AddParticlePropertiesService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		theForm.set("particlePage", mapping.findForward(
				particleType.toLowerCase()).getPath());
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return true;
	}
}
