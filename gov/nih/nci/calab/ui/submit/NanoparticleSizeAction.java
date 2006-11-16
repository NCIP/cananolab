package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSizeAction.java,v 1.14 2006-11-16 16:55:34 pansu Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.physical.SizeBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.ui.core.BaseCharacterizationAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

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

public class NanoparticleSizeAction extends BaseCharacterizationAction {
	private static Logger logger = Logger
			.getLogger(NanoparticleSizeAction.class);

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

		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : sizeChar.getDerivedBioAssayData()) {
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
		String selectedInstrumentType = null;

		if (sizeChar.getInstrument().getOtherInstrumentType() != null
				&& sizeChar.getInstrument().getOtherInstrumentType() != "")
			selectedInstrumentType = sizeChar.getInstrument()
					.getOtherInstrumentType();
		else
			selectedInstrumentType = sizeChar.getInstrument().getType();

		InitSessionSetup.getInstance().setManufacturerPerType(session,
				selectedInstrumentType);

		return forward;
	}

	/**
	 * Set up the input forms for adding data
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

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	private void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new SizeBean());

		for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
			String element = (String) e.nextElement();
			if (element.startsWith(CananoConstants.CHARACTERIZATION_FILE)) {
				session.removeAttribute(element);
			}
		}
	}

	private void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		String firstOption = InitSessionSetup.getInstance()
				.setAllInstrumentTypes(session);
		InitSessionSetup.getInstance()
				.setAllSizeDistributionGraphTypes(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		if (firstOption == "")
			firstOption = CananoConstants.OTHER;
		InitSessionSetup.getInstance().setManufacturerPerType(session,
				firstOption);
		session.setAttribute("selectedInstrumentType", "");
	}

	/**
	 * Set up the input forms for updating data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String compositionId = (String) theForm.get("characterizationId");

		SearchNanoparticleService service = new SearchNanoparticleService();
		Characterization aChar = service
				.getCharacterizationAndTableBy(compositionId);

		if (aChar == null)
			// aChar = service.getCharacterizationBy(compositionId);
			aChar = service.getCharacterizationAndTableBy(compositionId);

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);

		theForm.set("characterizationId", compositionId);

		int fileNumber = 0;

		for (DerivedBioAssayData obj : aChar.getDerivedBioAssayDataCollection()) {

			if (obj.getFile() != null) {
				CharacterizationFileBean fileBean = new CharacterizationFileBean();
				fileBean.setName(obj.getFile().getFilename());
				fileBean.setPath(obj.getFile().getPath());
				fileBean.setId(Integer.toString(fileNumber));

				request.getSession().setAttribute(
						"characterizationFile" + fileNumber, fileBean);
			} else {
				request.getSession().removeAttribute(
						"characterizationFile" + fileNumber);
			}
			fileNumber++;
		}

		SizeBean sChar = new SizeBean(aChar);

		theForm.set("achar", sChar);

		initSetup(request, theForm);

		if (sChar.getInstrument() != null) {
			InitSessionSetup.getInstance().setManufacturerPerType(session,
					sChar.getInstrument().getType());
			session.setAttribute("selectedInstrumentType", sChar
					.getInstrument().getType());
		}

		return mapping.getInputForward();
	}

	/**
	 * Set up the input fields for read only view data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		SizeBean achar = (SizeBean) theForm.get("achar");
		updateCharacterizationTables(achar);
		theForm.set("achar", achar);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Set up information needed for loading a characterization file
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward loadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String fileNumber = (String) theForm.get("fileNumber");
		request.setAttribute("particleName", particleName);
		request.setAttribute("fileNumber", fileNumber);
		request.setAttribute("characterization", "size");
		request.setAttribute("loadFileForward", "sizeInputForm");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		List<CharacterizationFileBean> files = service
				.getAllRunFiles(particleName);
		request.setAttribute("allRunFiles", files);
		return mapping.findForward("loadFile");
	}

	public void updateCharacterizationTables(SizeBean achar) {
		String numberOfCharacterizationTables = achar
				.getNumberOfDerivedBioAssayData();
		int tableNum = Integer.parseInt(numberOfCharacterizationTables);
		List<DerivedBioAssayDataBean> origTables = achar
				.getDerivedBioAssayData();
		int origNum = (origTables == null) ? 0 : origTables.size();
		List<DerivedBioAssayDataBean> tables = new ArrayList<DerivedBioAssayDataBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < tableNum; i++) {
				DerivedBioAssayDataBean table = new DerivedBioAssayDataBean();
				tables.add(table);
			}
		}
		// use keep original table info
		else if (tableNum <= origNum) {
			for (int i = 0; i < tableNum; i++) {
				tables.add((DerivedBioAssayDataBean) origTables.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				tables.add((DerivedBioAssayDataBean) origTables.get(i));
			}
			for (int i = origNum; i < tableNum; i++) {
				tables.add(new DerivedBioAssayDataBean());
			}
		}
		achar.setDerivedBioAssayData(tables);
	}
}
