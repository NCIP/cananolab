package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for Stability characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleStabilityAction.java,v 1.10 2007-01-04 23:21:58 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Stability;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.physical.StabilityBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class NanoparticleStabilityAction extends BaseCharacterizationAction {
	private static Logger logger = Logger
			.getLogger(NanoparticleStabilityAction.class);

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
		StabilityBean stabilityChar = (StabilityBean) theForm.get("achar");
		if (stabilityChar.getId() == null || stabilityChar.getId() == "") {

			stabilityChar.setId((String) theForm.get("characterizationId"));

		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : stabilityChar
				.getDerivedBioAssayDataList()) {
			LabFileBean fileBean = (LabFileBean) request
					.getSession().getAttribute(
							"characterizationFile" + fileNumber);
			if (fileBean != null) {
				logger.info("************set fileBean to " + fileNumber);
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		stabilityChar.setCreatedBy(user.getLoginName());
		stabilityChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleStability(particleType, particleName, stabilityChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleStability");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllInstrumentTypes(session);

		InitSessionSetup.getInstance().setAllInstrumentTypeManufacturers(
				session);
		return forward;
	}

	public void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new StabilityBean());

		cleanSessionAttributes(session);
		// for (Enumeration e = session.getAttributeNames();
		// e.hasMoreElements();) {
		// String element = (String) e.nextElement();
		// if (element.startsWith(CaNanoLabConstants.CHARACTERIZATION_FILE)) {
		// session.removeAttribute(element);
		// }
		// }
	}

	public void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		super.initSetup(request, theForm);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllStabilityDistributionGraphTypes(
				session);
		InitSessionSetup.getInstance().setAllStressorTypes(session);
		InitSessionSetup.getInstance().setAllTimeUnits(session);
		InitSessionSetup.getInstance().setAllAreaMeasureUnits(session);
	}

	@Override
	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		StabilityBean stability = new StabilityBean((Stability) aChar);
		theForm.set("achar", stability);
	}

	@Override
	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "stability");
		request.setAttribute("loadFileForward", "stabilityInputForm");
	}
}
