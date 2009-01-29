package gov.nih.nci.cananolab.ui.particle;

/**
 * This class allows user to submit invitro characterization data. 
 *  
 * @author pansu
 */

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.InvitroCharacterizationBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroCharacterizationAction extends BaseCharacterizationAction {

	protected String setupDetailPage(CharacterizationBean charBean) {
		String includePage = null;
		if (charBean.getClassName().equals("Caspase3Activation")
				|| charBean.getClassName().equals("CellViability")) {
			includePage = "/particle/characterization/invitro/body"
					+ charBean.getClassName() + "Info.jsp";
		}
		return includePage;
	}

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
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, charBean);
		InitCharacterizationSetup.getInstance()
				.persistInvitroCharacterizationDropdowns(request, charBean);
		//TODO::
//		if (!validateDerivedDatum(request, charBean)) {
//			return mapping.getInputForward();
//		}

		saveCharacterization(request, theForm, charBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addInvitroCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	protected CharacterizationBean getCharacterizationBean(
			DynaValidatorForm theForm, Characterization chara, UserBean user,
			String location) throws Exception {
		InvitroCharacterizationBean charBean = new InvitroCharacterizationBean(
				(InvitroCharacterization) chara);
		if (location.equals("local")) {
			// set file visibility
			NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
			charService.retrieveVisiblity(charBean, user);
		}
		theForm.set("achar", charBean);
		return charBean;
	}

	protected void setLookups(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		super.setLookups(request, charBean);
		// InitCharacterizationSetup.getInstance()
		// .setInvitroCharacterizationDropdowns(request);
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
		deleteCharacterization(request, theForm, charBean, user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deleteInvitroCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		String particleId = theForm.getString("particleId");
		NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
		ParticleBean particleBean = sampleService
				.findNanoparticleSampleById(particleId);
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		return forward;
	}
}
