package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for Morphology characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleMorphologyAction.java,v 1.16 2007-07-03 17:35:32 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleMorphologyAction extends BaseCharacterizationAction {

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
		CharacterizationBean charBean = super.prepareCreate(request, theForm);
		MorphologyBean propBean = (MorphologyBean) theForm.get("morphology");
		MorphologyBean morphologyBean = new MorphologyBean(propBean, charBean);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleMorphology(particleType, particleName,
				morphologyBean);
		super.postCreate(request, theForm);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleMorphology");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");
		return forward;
	}
}
