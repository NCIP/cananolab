package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for InVitro EnzymeInduction characterization. 
 *  
 * @author beasleyj
 */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.ConditionBean;
import gov.nih.nci.calab.dto.characterization.ControlBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.invitro.EnzymeInductionBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class InvitroEnzymeInductionAction extends AbstractDispatchAction {

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
		EnzymeInductionBean enzymeInductionChar = (EnzymeInductionBean) theForm.get("achar");
		
		String viewTitle = (String) theForm.get("viewTitle");
		String description = (String) theForm.get("description");
		String characterizationSource = (String) theForm.get("characterizationSource");

		if (enzymeInductionChar.getId() == null || enzymeInductionChar.getId() == "") {			
			enzymeInductionChar.setId( (String) theForm.get("characterizationId") );			
		}
		
		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : enzymeInductionChar.getDerivedBioAssayData()) {
			CharacterizationFileBean fileBean = (CharacterizationFileBean) request.getSession().getAttribute("characterizationFile" + fileNumber);
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
		service.addEnzymeInduction(particleType, particleName, enzymeInductionChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addInvitroEnzymeInduction");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
				
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		String selectedInstrumentType = null;
		
		if (enzymeInductionChar.getInstrument().getOtherInstrumentType() != null && enzymeInductionChar.getInstrument().getOtherInstrumentType() != "")
			selectedInstrumentType = enzymeInductionChar.getInstrument().getOtherInstrumentType();
		else
			selectedInstrumentType = enzymeInductionChar.getInstrument().getType();
		
		InitSessionSetup.getInstance().setManufacturerPerType(session, selectedInstrumentType);

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
		theForm.set("achar", new EnzymeInductionBean());
	}

	private void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		String firstOption = InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		InitSessionSetup.getInstance().setAllSizeDistributionGraphTypes(session);
		InitSessionSetup.getInstance().setAllControlTypes(session);
		InitSessionSetup.getInstance().setAllConditionTypes(session);
		InitSessionSetup.getInstance().setAllConcentrationUnits(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		if (firstOption == "")
			firstOption =  CananoConstants.OTHER;
		InitSessionSetup.getInstance().setManufacturerPerType(session, firstOption);
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
		String particleType = (String) theForm.get("particleType");
		String compositionId = (String) theForm.get("characterizationId");

		SearchNanoparticleService service = new SearchNanoparticleService();
		Characterization aChar = service.getCharacterizationAndTableBy(compositionId);

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);

		theForm.set("characterizationId", compositionId);
		theForm.set("characterizationSource", aChar.getSource());
		theForm.set("viewTitle", aChar.getIdentificationName());
		theForm.set("description", aChar.getDescription());
		initSetup(request, theForm);
		
		int fileNumber = 0;		
		for (DerivedBioAssayData obj : aChar.getDerivedBioAssayDataCollection()) {
			
			if (obj.getFile() != null) {
				CharacterizationFileBean fileBean = new CharacterizationFileBean();
				fileBean.setName(obj.getFile().getFilename());
				fileBean.setPath(obj.getFile().getPath());
				fileBean.setId(Integer.toString(fileNumber)); 
	
				request.getSession().setAttribute("characterizationFile" + fileNumber,
						fileBean);
			} else {
				request.getSession().removeAttribute("characterizationFile" + fileNumber);
			}
			fileNumber++;
		}
				
		EnzymeInductionBean hChar = new EnzymeInductionBean(aChar);		
		theForm.set("achar", hChar);		
		initSetup(request, theForm);

		if (hChar.getInstrument() != null) {
			InitSessionSetup.getInstance().setManufacturerPerType(session, hChar.getInstrument().getType());
			session.setAttribute("selectedInstrumentType", hChar.getInstrument().getType());
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
		EnzymeInductionBean achar = (EnzymeInductionBean) theForm.get("achar");
		String index=(String)request.getParameter("index");	
		String type = (String)request.getParameter("type");
		
		if ( type != null && !type.equals("") && type.equals("charTables") ) {
			updateCharacterizationTables(achar);
		}
		if ( type != null && !type.equals("") && type.equals("addControl") ) {
			addControl(achar, index);
		}
		if ( type != null && !type.equals("") && type.equals("addConditions") ) {
			addConditions(achar, index);
		}
		if ( type != null && !type.equals("") && type.equals("updateConditions") ) {
			updateConditions(achar, index);
		}
		
		theForm.set("achar", achar);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		
		return mapping.getInputForward();
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
	public void addConditions(EnzymeInductionBean achar, String index) {
		ControlBean control = null;
		int tableIndex = new Integer(index).intValue();
		
		DerivedBioAssayDataBean derivedBioAssayData = (DerivedBioAssayDataBean)achar.getDerivedBioAssayData().get(tableIndex);
		DatumBean datum = (DatumBean)derivedBioAssayData.getDatumList().get(0);
		if ( datum.getControl() != null ) {
			datum.setControl(control);
		}
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean particleConcentrationCondition = new ConditionBean();
		particleConcentrationCondition.setType("Particle Concentration");
		conditions.add(particleConcentrationCondition);
		ConditionBean molecularConcentrationCondition = new ConditionBean();
		molecularConcentrationCondition.setType("Molecular Concentration");
		molecularConcentrationCondition.setValueUnit("uM");
		conditions.add(molecularConcentrationCondition);
		datum.setConditionList(conditions);
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
	public void addControl(EnzymeInductionBean achar, String index) {
		List<ConditionBean> conditionList = null;
		int tableIndex = new Integer(index).intValue();
		DerivedBioAssayDataBean derivedBioAssayData = (DerivedBioAssayDataBean)achar.getDerivedBioAssayData().get(tableIndex);
		DatumBean datum = (DatumBean)derivedBioAssayData.getDatumList().get(0);
		if ( datum.getConditionList() != null ) {
			datum.setConditionList(conditionList);
		}			
		ControlBean control = new ControlBean();
		datum.setControl(control);
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
	public void updateConditions(EnzymeInductionBean achar, String index) {
		int tableIndex = new Integer(index).intValue();
		DerivedBioAssayDataBean derivedBioAssayDataBean = (DerivedBioAssayDataBean)achar.getDerivedBioAssayData().get(tableIndex);
		DatumBean datumBean = (DatumBean)(derivedBioAssayDataBean.getDatumList().get(0));
		String numberOfConditions = datumBean.getNumberOfConditions();
		int conditionNum = Integer.parseInt(numberOfConditions);
		List<ConditionBean> origConditions = datumBean.getConditionList();
		int origNum = (origConditions == null) ? 0 : origConditions.size();
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < conditionNum; i++) {
				ConditionBean condition = new ConditionBean();
				conditions.add(condition);
			}
		}
		// use keep original table info
		else if (conditionNum <= origNum) {
			for (int i = 0; i < conditionNum; i++) {
				conditions.add((ConditionBean) origConditions.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				conditions.add((ConditionBean) origConditions.get(i));
			}
			for (int i = origNum; i < conditionNum; i++) {
				conditions.add(new ConditionBean());
			}
		}
		datumBean.setConditionList(conditions);
	}

	/**
	 * Set up information needed for loading a characterization file
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
		String particleName=(String)theForm.get("particleName");
		String fileNumber=(String)theForm.get("fileNumber");	
		request.setAttribute("particleName", particleName);	
		request.setAttribute("fileNumber", fileNumber);		
		request.setAttribute("characterization", "enzymeInduction");
		request.setAttribute("loadFileForward", "invitroEnzymeInductionForm");
		return mapping.findForward("loadFile");
	}
	
	public void updateCharacterizationTables(EnzymeInductionBean achar) {
		String numberOfDerivedBioAssayData = achar.getNumberOfDerivedBioAssayData();
		int tableNum = Integer.parseInt(numberOfDerivedBioAssayData);
		List<DerivedBioAssayDataBean> origTables = achar.getDerivedBioAssayData();
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

	public boolean loginRequired() {
		return true;
	}
	
}
