package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for Morphology characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleMorphologyAction.java,v 1.9 2006-12-03 17:52:47 zengje Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
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

public class NanoparticleMorphologyAction extends BaseCharacterizationAction {
	private static Logger logger = Logger
			.getLogger(NanoparticleMorphologyAction.class);

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
		MorphologyBean morphologyChar = (MorphologyBean) theForm.get("achar");
		if (morphologyChar.getId() == null || morphologyChar.getId() == "") {

			morphologyChar.setId((String) theForm.get("characterizationId"));

		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : morphologyChar
				.getDerivedBioAssayDataList()) {
			CharacterizationFileBean fileBean = (CharacterizationFileBean) request
					.getSession().getAttribute(
							"characterizationFile" + fileNumber);
			if (fileBean != null) {
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		morphologyChar.setCreatedBy(user.getLoginName());
		morphologyChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleMorphology(particleType, particleName,
				morphologyChar);
		if (morphologyChar.getType().equals(CananoConstants.OTHER)){
			InitSessionSetup.getInstance().addSessionAttributeElement(request.getSession(),"allMorphologyTypes", morphologyChar.getOtherType());
		}
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleMorphology");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		String selectedInstrumentType = null;

		if (morphologyChar.getInstrument().getOtherInstrumentType() != null
				&& morphologyChar.getInstrument().getOtherInstrumentType() != "")
			selectedInstrumentType = morphologyChar.getInstrument()
					.getOtherInstrumentType();
		else
			selectedInstrumentType = morphologyChar.getInstrument().getType();

		InitSessionSetup.getInstance().setManufacturerPerType(session,
				selectedInstrumentType);

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
		theForm.set("achar", new MorphologyBean());

		cleanSessionAttributes(session);
//		for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
//			String element = (String) e.nextElement();
//			if (element.startsWith(CananoConstants.CHARACTERIZATION_FILE)) {
//				session.removeAttribute(element);
//			}
//		}
	}

	public void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		String firstOption = InitSessionSetup.getInstance()
				.setAllInstrumentTypes(session);
		InitSessionSetup.getInstance().setAllMorphologyDistributionGraphTypes(
				session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setAllMorphologyTypes(session);
		if (firstOption == "")
			firstOption = CananoConstants.OTHER;
		InitSessionSetup.getInstance().setManufacturerPerType(session,
				firstOption);
		session.setAttribute("selectedInstrumentType", "");
	}

	protected void setFormCharacterizationBean(DynaValidatorForm theForm, Characterization aChar) throws Exception {
		MorphologyBean morphology=new MorphologyBean((Morphology)aChar);
		theForm.set("achar", morphology);
	}

	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "morphology");
		request.setAttribute("loadFileForward", "morphologyInputForm");		
	}
}
