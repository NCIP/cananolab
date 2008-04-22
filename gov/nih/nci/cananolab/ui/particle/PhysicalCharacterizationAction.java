package gov.nih.nci.cananolab.ui.particle;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: PhysicalCharacterizationAction.java,v 1.1 2008-04-22 22:55:43 pansu Exp $ */

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.PhysicalCharacterizationBean;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.Date;

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

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		ParticleBean particleBean = initSetup(theForm, request);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
		charService.savePhysicalCharacterization(particleBean, charBean);

		// set createdDate and createdBy
		Date createdDate = new Date();
		String createdBy = user.getLoginName();
		Characterization domainChara = charBean.getDomainChar();
		if (domainChara.getId() != null && domainChara.getId() == 0) {
			domainChara.setId(null);
		}
		if (domainChara.getId() == null) {
			domainChara.setCreatedBy(createdBy);
			domainChara.setCreatedDate(createdDate);
			if (domainChara.getInstrumentConfiguration() != null
					&& domainChara.getInstrumentConfiguration().getId() == null) {
				domainChara.getInstrumentConfiguration()
						.setCreatedBy(createdBy);
				domainChara.getInstrumentConfiguration().setCreatedDate(
						createdDate);
			}
		}
		for (DerivedBioAssayData bioAssayData : charBean
				.getDerivedBioAssayDataList()) {
			if (bioAssayData.getId() == null) {
				bioAssayData.setCreatedBy(createdBy);
				bioAssayData.setCreatedDate(createdDate);
				if (bioAssayData.getLabFile().getId() != null) {
					bioAssayData.getLabFile().setCreatedBy(createdBy);
					bioAssayData.getLabFile().setCreatedDate(createdDate);
				}
				if (bioAssayData.getDerivedDatumCollection() != null) {
					for (DerivedDatum datum : bioAssayData
							.getDerivedDatumCollection()) {
						if (datum.getId() != null) {
							datum.setCreatedBy(createdBy);
							datum.setCreatedDate(createdDate);
						}
					}
				}
			}
		}
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
		request.setAttribute("updateDataTree", "true");
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		InitNanoparticleSetup.getInstance().setOtherParticleNames(
				request,
				particleBean.getParticleSample().getName(),
				particleBean.getParticleSample().getSource()
						.getOrganizationName(), user);
		ServletContext appContext = request.getSession().getServletContext();
		String submitType = request.getParameter("submitType");
		String charClass = InitSetup.getInstance().getObjectName(submitType,
				appContext);
		request.setAttribute("charClass", charClass);
		InitSetup.getInstance().setSharedDropdowns(appContext);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, charClass);
		InitCharacterizationSetup.getInstance()
				.setPhysicalCharacterizationDropdowns(request, charClass);

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
