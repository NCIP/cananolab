package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for InVitro EnzymeInduction characterization. 
 *  
 * @author beasleyj
 */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.invitro.EnzymeInduction;
import gov.nih.nci.calab.dto.characterization.ConditionBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.EnzymeInductionBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroEnzymeInductionAction extends BaseCharacterizationAction {

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
		EnzymeInductionBean enzymeInductionChar = (EnzymeInductionBean) theForm
				.get("achar");

		if (enzymeInductionChar.getId() == null
				|| enzymeInductionChar.getId() == "") {
			enzymeInductionChar.setId((String) theForm
					.get("characterizationId"));
		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : enzymeInductionChar
				.getDerivedBioAssayDataList()) {
			
			// Vaidate the the nested data point entries
			for ( DatumBean dataPoint : obj.getDatumList() ) {
				if ( dataPoint.getValue() != null && dataPoint.getValue().equals("") ) {
					Exception updateConditionsException = new Exception(PropertyReader.getProperty(
							CalabConstants.SUBMISSION_PROPERTY, "enzymeInductionPercentageRequired"));							
					throw updateConditionsException;
				}
				else {
					if ( !StringUtils.isDouble(dataPoint.getValue()) ) {
						Exception updateConditionsException = new Exception(PropertyReader.getProperty(
							CalabConstants.SUBMISSION_PROPERTY, "enzymeInductionPercentageDouble"));							
						throw updateConditionsException;
					}
				}
				
				if ( dataPoint.getIsAControl().equals(CananoConstants.BOOLEAN_NO) ) {
					for ( ConditionBean condition : dataPoint.getConditionList() ) {
						if ( !StringUtils.isInteger(condition.getValue()) ) {
							Exception updateConditionsException = new Exception(PropertyReader.getProperty(
									CalabConstants.SUBMISSION_PROPERTY, "conditionValues"));							
							throw updateConditionsException;
						}
					}
				} 
			}
			
			LabFileBean fileBean = (LabFileBean) request
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
		enzymeInductionChar.setCreatedBy(user.getLoginName());
		enzymeInductionChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addEnzymeInduction(particleType, particleName,
				enzymeInductionChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage(
				"message.addInvitroEnzymeInduction");
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

	protected void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new EnzymeInductionBean());

		cleanSessionAttributes(session);
	}

	@Override
	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		EnzymeInductionBean charBean = new EnzymeInductionBean(
				(EnzymeInduction) aChar);
		theForm.set("achar", charBean);

	}

	@Override
	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "enzymeInduction");
		request.setAttribute("loadFileForward", "invitroEnzymeInductionForm");

	}

}
