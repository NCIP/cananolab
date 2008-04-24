package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: PhysicalCharacterizationAction.java,v 1.4 2008-04-24 22:30:22 pansu Exp $ */

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.PhysicalCharacterizationBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class PhysicalCharacterizationAction extends BaseAnnotationAction {

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
		ParticleBean particleBean = initSetup(theForm, request);
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

	/**
	 * Set up the input form for adding new characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		request.getSession().removeAttribute("characterizationForm");
		ParticleBean particleBean = initSetup(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().setOtherParticleNames(
				request,
				particleBean.getDomainParticleSample().getName(),
				particleBean.getDomainParticleSample().getSource()
						.getOrganizationName(), user);
		ServletContext appContext = request.getSession().getServletContext();
		String submitType = request.getParameter("submitType");
		String charClass = InitSetup.getInstance().getObjectName(submitType,
				appContext);
		request.getSession().setAttribute("charClass", charClass);
		InitSetup.getInstance().setSharedDropdowns(appContext);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, charClass);
		InitCharacterizationSetup.getInstance()
				.setPhysicalCharacterizationDropdowns(request, charClass);
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		InitNanoparticleSetup.getInstance().getFileTypes(request);
		
		// ParticleBean particleBean = initSetup(theForm, request);
		// InitParticleSetup.getInstance()
		// .setAllCharacterizationMeasureUnitsTypes(session, submitType);
		// InitParticleSetup.getInstance().setDerivedDatumNames(session,
		// charBean.getName());
		// InitProtocolSetup.getInstance().setProtocolFilesByCharType(session,
		// charBean.getCharacterizationType());
		// InitParticleSetup.getInstance().setAllInstruments(session);
		// InitParticleSetup.getInstance().setAllDerivedDataFileTypes(session);
		return mapping.getInputForward();
	}
}
