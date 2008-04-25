package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: PhysicalCharacterizationAction.java,v 1.7 2008-04-25 23:35:38 pansu Exp $ */

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.PhysicalCharacterizationBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class PhysicalCharacterizationAction extends BaseCharacterizationAction {

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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PhysicalCharacterizationBean charBean = (PhysicalCharacterizationBean) theForm
				.get("achar");
		charBean.setDomainChar();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ParticleBean particleBean = setupParticle(theForm, request);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		charService.saveCharacterization(
				particleBean.getDomainParticleSample(), charBean
						.getDomainChar(), user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addPhysicalCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		return forward;
	}

	protected void setCharacterizationBean(DynaValidatorForm theForm,
			Characterization chara) throws Exception {
		PhysicalCharacterizationBean charBean = new PhysicalCharacterizationBean(
				(PhysicalCharacterization) chara);
		theForm.set("achar", charBean);
	}

	protected void setLookups(HttpServletRequest request, String charClass)
			throws Exception {
		super.setLookups(request, charClass);
		InitCharacterizationSetup.getInstance()
				.setPhysicalCharacterizationDropdowns(request, charClass);
	}
}
