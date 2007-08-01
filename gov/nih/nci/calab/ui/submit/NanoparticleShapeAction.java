package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for Shape characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleShapeAction.java,v 1.19 2007-08-01 20:49:36 pansu Exp $ */

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
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

public class NanoparticleShapeAction extends BaseCharacterizationAction {
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
		ShapeBean propBean = (ShapeBean) theForm.get("shape");
		ShapeBean shapeBean = new ShapeBean(propBean, charBean);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleShape(particleType, particleName, shapeBean);
		CharacterizationBean[] otherChars = super.prepareCopy(request, theForm,
				service);
		for (CharacterizationBean acharBean : otherChars) {
			ShapeBean aShapeBean = new ShapeBean(propBean, acharBean);
			service.addParticleShape(particleType, acharBean.getParticleName(),
					aShapeBean);
		}
		super.postCreate(request, theForm);
		request.getSession().setAttribute("newShapeCreated", "true");
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleShape");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		return forward;
	}
}
