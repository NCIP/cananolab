package gov.nih.nci.cananolab.ui.particle;

/**
 * This class allows user to submit physical characterization data. 
 *  
 * @author pansu
 */

/* CVS $Id: PhysicalCharacterizationAction.java,v 1.25 2008-05-12 16:22:56 pansu Exp $ */

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Surface;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
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

	protected String setupDetailPage(CharacterizationBean charBean) {
		String includePage = null;
		if (charBean.getClassName().equals("PhysicalState")
				|| charBean.getClassName().equals("Shape")
				|| charBean.getClassName().equals("Solubility")
				|| charBean.getClassName().equals("Surface")) {
			includePage = "/particle/characterization/physical/body"
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
		PhysicalCharacterizationBean charBean = (PhysicalCharacterizationBean) theForm
				.get("achar");
		saveCharacterization(request, theForm, charBean);
		InitCharacterizationSetup.getInstance()
				.persistPhysicalCharacterizationDropdowns(request, charBean);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addPhysicalCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	protected CharacterizationBean getCharacterizationBean(
			DynaValidatorForm theForm, Characterization chara, UserBean user)
			throws Exception {
		PhysicalCharacterizationBean charBean = new PhysicalCharacterizationBean(
				(PhysicalCharacterization) chara);
		// set file visibility
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		charService.retrieveVisiblity(charBean, user);
		theForm.set("achar", charBean);
		return charBean;
	}

	protected void setLookups(HttpServletRequest request,
			CharacterizationBean charBean) throws Exception {
		super.setLookups(request, charBean);
		InitCharacterizationSetup.getInstance()
				.setPhysicalCharacterizationDropdowns(request);
	}

	protected void clearForm(DynaValidatorForm theForm) {
		theForm.set("achar", new PhysicalCharacterizationBean());
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PhysicalCharacterizationBean charBean = (PhysicalCharacterizationBean) theForm
				.get("achar");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		deleteCharacterization(request, theForm, charBean, user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.deletePhysicalCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		String particleId = theForm.getString("particleId");
		InitNanoparticleSetup.getInstance().getDataTree(particleId, request);
		return forward;
	}

	public ActionForward addSurfaceChemistry(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PhysicalCharacterizationBean achar = (PhysicalCharacterizationBean) theForm
				.get("achar");
		achar.getSurfaceBean().addSurfaceChemistry();
		setupDomainChar(request, theForm, achar);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		return mapping.getInputForward();
	}

	public ActionForward removeSurfaceChemistry(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String chemIndexStr = (String) request.getParameter("compInd");
		int chemInd = Integer.parseInt(chemIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PhysicalCharacterizationBean achar = (PhysicalCharacterizationBean) theForm
				.get("achar");
		achar.getSurfaceBean().removeSurfaceChemistry(chemInd);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);

		return mapping.getInputForward();
	}

}
