package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for Purity characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticlePurityAction.java,v 1.7 2006-11-22 23:19:23 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Purity;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.physical.PurityBean;
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


public class NanoparticlePurityAction extends BaseCharacterizationAction {
	private static Logger logger = Logger.getLogger(NanoparticlePurityAction.class);

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
		PurityBean purityChar=(PurityBean) theForm.get("achar");

		if (purityChar.getId() == null || purityChar.getId() == "") {
			
			purityChar.setId( (String) theForm.get("characterizationId") );
			
		}
		
		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : purityChar.getDerivedBioAssayDataList()) {
			CharacterizationFileBean fileBean = (CharacterizationFileBean) request.getSession().getAttribute("characterizationFile" + fileNumber);
			if (fileBean != null) {		
				logger.info("************set fileBean to " + fileNumber);
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		
		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		purityChar.setCreatedBy(user.getLoginName());
		purityChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticlePurity(particleType, particleName, purityChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticlePurity");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		String selectedInstrumentType = null;
		
		if (purityChar.getInstrument().getOtherInstrumentType() != null && purityChar.getInstrument().getOtherInstrumentType() != "")
			selectedInstrumentType = purityChar.getInstrument().getOtherInstrumentType();
		else
			selectedInstrumentType = purityChar.getInstrument().getType();
		
		InitSessionSetup.getInstance().setManufacturerPerType(session, selectedInstrumentType);

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
		theForm.set("achar", new PurityBean());

		cleanSessionAttributes(session);
//	    for (Enumeration e = session.getAttributeNames(); e.hasMoreElements() ;) {
//	    	String element = (String) e.nextElement();
//	        if (element.startsWith(CananoConstants.CHARACTERIZATION_FILE)) {
//	        	session.removeAttribute(element);
//	        }
//	    }
	}

	public void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		String firstOption = InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		InitSessionSetup.getInstance().setAllPurityDistributionGraphTypes(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setAllAreaMeasureUnits(session);
		if (firstOption == "")
			firstOption =  CananoConstants.OTHER;
		InitSessionSetup.getInstance().setManufacturerPerType(session, firstOption);
		session.setAttribute("selectedInstrumentType", "");
	}

	protected void setFormCharacterizationBean(DynaValidatorForm theForm, Characterization aChar) throws Exception {
		PurityBean purity=new PurityBean((Purity)aChar);
		theForm.set("achar", purity);		
	}

	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "purity");
		request.setAttribute("loadFileForward", "purityInputForm");		
	}
}
