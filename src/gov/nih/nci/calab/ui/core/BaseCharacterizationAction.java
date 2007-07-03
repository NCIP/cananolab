package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.ConditionBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

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

/* CVS $Id: BaseCharacterizationAction.java,v 1.25.2.4 2007-07-03 18:55:30 zengje Exp $ */

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
	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();

		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		InitSessionSetup.getInstance()
				.setAllSizeDistributionGraphTypes(session);
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
//		InitSessionSetup.getInstance().setAllInstrumentTypes(session);
//		InitSessionSetup.getInstance().setAllInstrumentTypeManufacturers(
//				session);
		InitSessionSetup.getInstance().setAllInstruments(session);
		InitSessionSetup.getInstance().setAllControlTypes(session);
		InitSessionSetup.getInstance().setAllConditionTypes(session);
		InitSessionSetup.getInstance().setAllConditionUnits(session);
		InitSessionSetup.getInstance().setAllConcentrationUnits(session);
		InitSessionSetup.getInstance().setAllTimeUnits(session);
		InitSessionSetup.getInstance().setAllCharacterizationSources(session);
	}

	protected void postCreate(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		request.getSession().setAttribute("newInstrumentCreated", "true");
		request.getSession().setAttribute("newCharacterizationSourceCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
	}
	
	/**
	 * Set the appropriate type of characterization bean in the form from the
	 * chararacterization domain obj.
	 * 
	 * @param theForm
	 * @param aChar
	 * @throws Exception
	 */
	protected abstract void setFormCharacterizationBean(
			DynaValidatorForm theForm, Characterization aChar) throws Exception;

	/**
	 * Clean the session attribture
	 * 
	 * @param sessioin
	 * @throws Exception
	 */
	protected void cleanSessionAttributes(HttpSession session) throws Exception {
		for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();) {
			String element = (String) e.nextElement();
			if (element.startsWith(CaNanoLabConstants.CHARACTERIZATION_FILE)) {
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
	 * Set request attributes required in load file for different types of
	 * characterizations
	 * 
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

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		theForm.set("characterizationId", characterizationId);

		UserService userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		int fileNumber = 0;
		for (DerivedBioAssayData obj : aChar.getDerivedBioAssayDataCollection()) {
			if (obj.getFile() != null) {
				LabFileBean fileBean = new LabFileBean(obj.getFile(),
						CaNanoLabConstants.OUTPUT);
				boolean status = userService.checkReadPermission(user, fileBean
						.getId());
				if (status) {
					List<String> accessibleGroups = userService
							.getAccessibleGroups(fileBean.getId(),
									CaNanoLabConstants.CSM_READ_ROLE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					fileBean.setVisibilityGroups(visibilityGroups);
					request.getSession().setAttribute(
							"characterizationFile" + fileNumber, fileBean);				}
			} else {
				request.getSession().removeAttribute(
						"characterizationFile" + fileNumber);
			}
			fileNumber++;
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
	public ActionForward loadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		LabFileBean fileBean = service.getFile(fileId, CaNanoLabConstants.OUTPUT);
		String fileRoot = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator + fileBean.getPath());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileBean.getName());
			response.setHeader("cache-control", "Private");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			throw new CalabException(
					"File to download doesn't exist on the server");
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
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String index = (String) request.getParameter("index");
		String type = (String) request.getParameter("type");
		String dataPointIndex = (String) request.getParameter("dataPointIndex");
		if (type != null && !type.equals("") && type.equals("charTables")) {
			updateCharacterizationTables(achar);
		}
		if (type != null && !type.equals("") && type.equals("dataPoints")) {
			updateChartDataPoints(achar, index);
		}
		if (type != null && !type.equals("") && type.equals("conditions")) {
			if (!updateConditions(achar, index, dataPointIndex)) {

				Exception updateConditionsException = new Exception(
						PropertyReader.getProperty(
								CaNanoLabConstants.SUBMISSION_PROPERTY,
								"numberOfConditions"));
				throw updateConditionsException;

			}
		}
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return mapping.getInputForward();
	}

	public void updateCharacterizationTables(CharacterizationBean achar) {
		String numberOfCharacterizationTables = achar
				.getNumberOfDerivedBioAssayData();
		int tableNum = Integer.parseInt(numberOfCharacterizationTables);

		List<DerivedBioAssayDataBean> origTables = achar
				.getDerivedBioAssayDataList();
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
		achar.setDerivedBioAssayDataList(tables);
	}

	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return boolean
	 * @throws Exception
	 */
	public boolean updateConditions(CharacterizationBean achar, String index,
			String dataPointIndex) {
		int tableIndex = new Integer(index).intValue();
		int dataIndex = new Integer(dataPointIndex).intValue();
		DerivedBioAssayDataBean derivedBioAssayDataBean = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayDataList().get(tableIndex);
		DatumBean datumBean = (DatumBean) (derivedBioAssayDataBean
				.getDatumList().get(dataIndex));
		String numberOfConditions = datumBean.getNumberOfConditions();
		// Validate the number of conditions entry
		if (StringUtils.isInteger(numberOfConditions)) {
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
			return true;
		} else {
			return false;
		}
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
	public void updateChartDataPoints(CharacterizationBean achar, String index) {
		int tableIndex = new Integer(index).intValue();
		DerivedBioAssayDataBean derivedBioAssayDataBean = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayDataList().get(tableIndex);
		String numberOfDataPoints = derivedBioAssayDataBean
				.getNumberOfDataPoints();
		int dataPointNum = Integer.parseInt(numberOfDataPoints);
		List<DatumBean> origDataList = derivedBioAssayDataBean.getDatumList();
		int origNum = (origDataList == null) ? 0 : origDataList.size();
		List<DatumBean> dataList = new ArrayList<DatumBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < dataPointNum; i++) {
				DatumBean dataPoint = new DatumBean();
				dataList.add(dataPoint);
			}
		}
		// use keep original table info
		else if (dataPointNum <= origNum) {
			for (int i = 0; i < dataPointNum; i++) {
				dataList.add((DatumBean) origDataList.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				dataList.add((DatumBean) origDataList.get(i));
			}
			for (int i = origNum; i < dataPointNum; i++) {
				dataList.add(new DatumBean());
			}
		}
		derivedBioAssayDataBean.setDatumList(dataList);
	}

	public boolean loginRequired() {
		return true;
	}
}