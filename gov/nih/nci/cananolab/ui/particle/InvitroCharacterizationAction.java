package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up input form for Invitro characterization. 
 *  
 * @author pansu
 */

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.InvitroCharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.PhysicalCharacterizationBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroCharacterizationAction extends BaseCharacterizationAction {
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
		InvitroCharacterizationBean charBean = (InvitroCharacterizationBean) theForm
				.get("achar");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		charBean.setupDomainChar(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName());
		ParticleBean particleBean = setupParticle(theForm, request);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		charService.saveCharacterization(
				particleBean.getDomainParticleSample(), charBean
						.getDomainChar());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addInvitroCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		setupDataTree(theForm, request);
		return forward;
	}

	protected CharacterizationBean setCharacterizationBean(
			DynaValidatorForm theForm, Characterization chara, UserBean user)
			throws Exception {
		InvitroCharacterizationBean charBean = new InvitroCharacterizationBean(
				(InvitroCharacterization) chara);
		// set file visibility
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		charService.retrieveVisiblity(charBean, user);
		theForm.set("achar", charBean);
		return charBean;
	}

	protected void setLookups(HttpServletRequest request, String charClass)
			throws Exception {
		super.setLookups(request, charClass);
	}

	protected void clearForm(DynaValidatorForm theForm) {
		theForm.set("achar", new InvitroCharacterizationBean());
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		InvitroCharacterizationBean charBean = (InvitroCharacterizationBean) theForm
				.get("achar");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		charBean.setupDomainChar(InitSetup.getInstance()
				.getDisplayNameToClassNameLookup(
						request.getSession().getServletContext()), user
				.getLoginName());
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		charService.deleteCharacterization(charBean.getDomainChar());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteInvitroCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		String particleId = theForm.getString("particleId");
		InitNanoparticleSetup.getInstance().getDataTree(particleId, request);
		return forward;
	}
}
