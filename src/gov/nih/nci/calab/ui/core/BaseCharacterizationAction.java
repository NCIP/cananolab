package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.ConditionBean;
import gov.nih.nci.calab.dto.characterization.ControlBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This action serves as the base action for all characterization related action
 * classes. It includes common operations such as download, updateManufacturers.
 * 
 * @author pansu
 */

/* CVS $Id: BaseCharacterizationAction.java,v 1.5 2006-11-20 22:29:28 pansu Exp $ */

public abstract class BaseCharacterizationAction extends AbstractDispatchAction {
	/**
	 * clear session data from the input form
	 * 
	 * @param session
	 * @param theForm
	 * @param mapping
	 * @throws Exception
	 */
	protected abstract void clearMap(HttpSession session,
			DynaValidatorForm theForm, ActionMapping mapping) throws Exception;

	/**
	 * Pepopulate data for the form
	 * 
	 * @param request
	 * @param theForm
	 * @throws Exception
	 */
	protected abstract void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception;

	/**
	 * Set the appropriate type of characterization bean in the form 
	 * from the chararacterization domain obj.
	 * @param theForm
	 * @param aChar
	 * @throws Exception
	 */
	protected abstract void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception;	
	
	
	/**
	 * Clean the session attribture
	 * @param sessioin
	 * @throws Exception
	 */
	protected void cleanSessionAttributes(HttpSession session) throws Exception {
		for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
			String element = (String) e.nextElement();
			if (element.startsWith(CananoConstants.CHARACTERIZATION_FILE)) {
				session.removeAttribute(element);
			}
		}
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

		HttpSession session = request.getSession();
		clearMap(session, theForm, mapping);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	/**
	 * Set request attributes required in load file for different types of characterizations
	 * @param request
	 */
	protected abstract void setLoadFileRequest(HttpServletRequest request);
		
	/**
	 * Set up the form for updating existing characterization
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
		initSetup(request, theForm);
		String characterizationId = (String) theForm.get("characterizationId");

		SearchNanoparticleService service = new SearchNanoparticleService();
		Characterization aChar = service
				.getCharacterizationAndTableBy(characterizationId);

		if (aChar == null)
			// aChar = service.getCharacterizationBy(compositionId);
			aChar = service.getCharacterizationAndTableBy(characterizationId);

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		theForm.set("characterizationId", characterizationId);

		int fileNumber = 0;

		for (DerivedBioAssayData obj : aChar.getDerivedBioAssayDataCollection()) {

			if (obj.getFile() != null) {
				CharacterizationFileBean fileBean = new CharacterizationFileBean();
				fileBean.setName(obj.getFile().getFilename());
				fileBean.setPath(obj.getFile().getPath());
				fileBean.setId((obj.getFile().getId().toString()));

				request.getSession().setAttribute(
						"characterizationFile" + fileNumber, fileBean);
			} else {
				request.getSession().removeAttribute(
						"characterizationFile" + fileNumber);
			}
			fileNumber++;
		}

		if (aChar.getInstrument() != null) {
			String instrumentType = aChar.getInstrument().getInstrumentType()
					.getName();
			InitSessionSetup.getInstance().setManufacturerPerType(session,
					instrumentType);
			session.setAttribute("selectedInstrumentType", instrumentType);
		}
		initSetup(request, theForm);
		setFormCharacterizationBean(theForm, aChar);
		return mapping.getInputForward();
	}

	/**
	 * Prepare the form for viewing existing characterization
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	/**
	 * Load file action for characterization file loading.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward  loadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = (String) theForm.get("particleName");
		String fileNumber = (String) theForm.get("fileNumber");
		request.setAttribute("particleName", particleName);
		request.setAttribute("fileNumber", fileNumber);
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		List<CharacterizationFileBean> files = service
				.getAllRunFiles(particleName);
		request.setAttribute("allRunFiles", files);
		setLoadFileRequest(request);		
		return mapping.findForward("loadFile");
	}
	
	/**
	 * Download action to handle characterization file download and viewing
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		CharacterizationFileBean fileBean = service.getFile(fileId);
		String fileRoot = PropertyReader.getProperty(
				CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator + fileBean.getPath());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileBean.getName());
			response.setHeader("Cache-Control", "no-cache");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			throw new Exception("ERROR: file not found.");
		}
		return null;
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
	public ActionForward updateManufacturers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		CharacterizationBean aChar = (CharacterizationBean) theForm
				.get("achar");
		if (aChar.getInstrument() != null) {
			String type = aChar.getInstrument().getType();
			session.setAttribute("selectedInstrumentType", type);
			// logger.info("***************Action: getting manufacturers for " +
			// type);
			InitSessionSetup.getInstance().setManufacturerPerType(session,
					aChar.getInstrument().getType());
		}

		return mapping.getInputForward();
	}

	public void updateCharacterizationTables(CharacterizationBean achar) {
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
	public void addConditions(CharacterizationBean achar, String index) {
		ControlBean control = null;
		int tableIndex = new Integer(index).intValue();

		DerivedBioAssayDataBean derivedBioAssayData = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayData().get(tableIndex);
		DatumBean datum = (DatumBean) derivedBioAssayData.getDatumList().get(0);
		if (datum.getControl() != null) {
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
	public void addControl(CharacterizationBean achar, String index) {
		List<ConditionBean> conditionList = null;
		int tableIndex = new Integer(index).intValue();
		DerivedBioAssayDataBean derivedBioAssayData = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayData().get(tableIndex);
		DatumBean datum = (DatumBean) derivedBioAssayData.getDatumList().get(0);
		if (datum.getConditionList() != null) {
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
	public void updateConditions(CharacterizationBean achar, String index) {
		int tableIndex = new Integer(index).intValue();
		DerivedBioAssayDataBean derivedBioAssayDataBean = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayData().get(tableIndex);
		DatumBean datumBean = (DatumBean) (derivedBioAssayDataBean
				.getDatumList().get(0));
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

	public boolean loginRequired() {
		return true;
	}
}