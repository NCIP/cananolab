package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSizeAction.java,v 1.20 2006-12-05 23:41:51 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Size;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.physical.SizeBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
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

public class NanoparticleSizeAction extends BaseCharacterizationAction {

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
		SizeBean sizeChar = (SizeBean) theForm.get("achar");

		if (sizeChar.getId() == null || sizeChar.getId() == "") {
			sizeChar.setId((String) theForm.get("characterizationId"));
		}

		// Validation
		for (DerivedBioAssayDataBean dataBean : sizeChar
				.getDerivedBioAssayDataList()) {
			for (DatumBean datumBean : dataBean.getDatumList()) {
				try {
					if (datumBean.getValue() != null
							&& datumBean.getValue().trim().length() > 0) {
						Float.parseFloat(datumBean.getValue());
						Float.parseFloat(datumBean.getValue());
					}
				} catch (NumberFormatException formatE) {
					ActionMessages msgs = new ActionMessages();
					ActionMessage msg = new ActionMessage(
							"errors.float",
							new String[] { "Average/Mean, Z-Average, and PDI " });
					msgs.add("message", msg);
					saveMessages(request, msgs);
					forward = mapping.findForward("input");
					return forward;
				}
			}
		}

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : sizeChar
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
		sizeChar.setCreatedBy(user.getLoginName());
		sizeChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleSize(particleType, particleName, sizeChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleSize");
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
		theForm.set("achar", new SizeBean());

		cleanSessionAttributes(session);
		// for (Enumeration e = session.getAttributeNames();
		// e.hasMoreElements();) {
		// String element = (String) e.nextElement();
		// if (element.startsWith(CananoConstants.CHARACTERIZATION_FILE)) {
		// session.removeAttribute(element);
		// }
		// }
	}

	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {

		super.initSetup(request, theForm);
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance()
				.setAllSizeDistributionGraphTypes(session);		
	}

	protected void setLoadFileRequest(HttpServletRequest request) {
		request.setAttribute("characterization", "size");
		request.setAttribute("loadFileForward", "sizeInputForm");
	}

	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		SizeBean size = new SizeBean((Size) aChar);
		theForm.set("achar", size);
	}
}
