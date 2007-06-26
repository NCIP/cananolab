package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This action serves as the base action for all characterization related action
 * classes. It includes common operations such as download, updateManufacturers.
 * 
 * @author pansu
 */

/*
 * CVS $Id: BaseCharacterizationAction.java,v 1.27 2007/05/15 13:33:05 chenhang
 * Exp $
 */

public abstract class BaseCharacterizationAction extends AbstractDispatchAction {
	protected CharacterizationBean prepareCreate(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");

		// set createdBy and createdDate for the characterization
		UserBean user = (UserBean) session.getAttribute("user");
		Date date = new Date();
		charBean.setCreatedBy(user.getLoginName());
		charBean.setCreatedDate(date);

		// set characterization files
		int fileNumber = 0;
		for (DerivedBioAssayDataBean obj : charBean
				.getDerivedBioAssayDataList()) {
			DerivedBioAssayDataBean fileBean = (DerivedBioAssayDataBean) request
					.getSession().getAttribute(
							"characterizationFile" + fileNumber);
			obj = fileBean;
			fileNumber++;
		}
		return charBean;
	}

	protected void postCreate(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		request.getSession().setAttribute("newInstrumentCreated", "true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
	}

	protected CharacterizationBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm, SubmitNanoparticleService service)
			throws Exception {
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		Boolean copyData = (Boolean) theForm.get("copyData");

		CharacterizationBean[] charBeans = new CharacterizationBean[otherParticles.length];
		int i = 0;
		for (String particleName : otherParticles) {
			CharacterizationBean newCharBean = charBean.copy(copyData
					.booleanValue());
			// reset view title
			String autoTitle = particleName
					+ "_"
					+ CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_TITLE_SUFFIX;
			String autoColor = CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_COLOR;
			newCharBean.setViewTitle(autoTitle);
			newCharBean.setViewColor(autoColor);
			// save the files to the database and file system
			if (!copyData) {
				List<DerivedBioAssayDataBean> dataList = newCharBean
						.getDerivedBioAssayDataList();
				for (DerivedBioAssayDataBean derivedBioAssayData : dataList) {
					service.saveCharacterizationFile(derivedBioAssayData);
				}
			}
			charBeans[i] = newCharBean;
			i++;
		}
		return charBeans;
	}

	/**
	 * clear session data from the input form
	 * 
	 * @param session
	 * @param theForm
	 * @param mapping
	 * @throws Exception
	 */
	protected void clearMap(HttpSession session, DynaValidatorForm theForm)
			throws Exception {
		// reset achar and otherParticles
		theForm.set("otherParticles", new String[0]);
		theForm.set("achar", new CharacterizationBean());
		theForm.set("morphology", new MorphologyBean());
		theForm.set("shape", new ShapeBean());
		theForm.set("surface", new SurfaceBean());
		theForm.set("solubility", new SolubilityBean());
		theForm.set("cytotoxicity", new CytotoxicityBean());
		cleanSessionAttributes(session);
	}

	/**
	 * Prepopulate data for the input form
	 * 
	 * @param request
	 * @param theForm
	 * @throws Exception
	 */
	protected void initSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();
		clearMap(session, theForm);
		String submitType = (String) request.getParameter("submitType");
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		String particleSource = request.getParameter("particleSource");
		String charName = request.getParameter("charName");
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setAllInstruments(session);
		InitSessionSetup.getInstance().setAllDerivedDataFileTypes(session);
		InitSessionSetup.getInstance().setAllMorphologyTypes(session);
		InitSessionSetup.getInstance().setAllShapeTypes(session);
		InitSessionSetup.getInstance().setAllConcentrationUnits(session);
		InitSessionSetup.getInstance().setAllCellLines(session);
		InitSessionSetup.getInstance().setAllCharacterizationMeasureUnitsTypes(
				session, charName);
		// TODO If there are more types of charactizations, add their
		// corresponding
		// protocol type here.
		if (submitType.equalsIgnoreCase("physical"))
			InitSessionSetup.getInstance().setAllProtocolNameVersionsByType(
					session, "Physical assay");
		else
			InitSessionSetup.getInstance().setAllProtocolNameVersionsByType(
					session, "In vitro assay");

		// set up other particle names from the same source
		LookupService service = new LookupService();
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		particleSource = "DNT";
		SortedSet<String> allOtherParticleNames = service.getOtherParticles(
				particleSource, particleName, user);
		session.setAttribute("allOtherParticleNames", allOtherParticleNames);

		InitSessionSetup.getInstance().setDerivedDataCategoriesDatumNames(
				session, charName);
	}

	/**
	 * Set the appropriate type of characterization bean in the form from the
	 * chararacterization domain obj.
	 * 
	 * @param theForm
	 * @param aChar
	 * @throws Exception
	 */
	protected void setFormCharacterizationBean(DynaValidatorForm theForm,
			Characterization aChar) throws Exception {
		CharacterizationBean aCharBean = new CharacterizationBean(aChar);
		theForm.set("achar", aCharBean);
	}

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
		String characterizationId = request.getParameter("characterizationId");
		SearchNanoparticleService service = new SearchNanoparticleService();
		Characterization aChar = service
				.getCharacterizationAndTableBy(characterizationId);
		if (aChar == null) {
			throw new Exception(
					"This characterization no longer exists in the database.  Please log in again to refresh.");
		}
		theForm.set("achar", new CharacterizationBean(aChar));

		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		// set up charaterization files in the session
		int fileNumber = 0;
		for (DerivedBioAssayData obj : aChar.getDerivedBioAssayDataCollection()) {
			DerivedBioAssayDataBean fileBean = new DerivedBioAssayDataBean(obj);
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
						"characterizationFile" + fileNumber, fileBean);
			}

			fileNumber++;
		}

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
		LabFileBean fileBean = service.getFile(fileId);
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

	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		List<DerivedBioAssayDataBean> origTables = achar
				.getDerivedBioAssayDataList();
		int origNum = (origTables == null) ? 0 : origTables.size();
		List<DerivedBioAssayDataBean> tables = new ArrayList<DerivedBioAssayDataBean>();
		for (int i = 0; i < origNum; i++) {
			tables.add((DerivedBioAssayDataBean) origTables.get(i));
		}
		// add a new one
		tables.add(new DerivedBioAssayDataBean());
		achar.setDerivedBioAssayDataList(tables);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return mapping.getInputForward();
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String findIndexStr = (String) request.getParameter("fileInd");
		int fileInd = Integer.parseInt(findIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		List<DerivedBioAssayDataBean> origTables = achar
				.getDerivedBioAssayDataList();
		int origNum = (origTables == null) ? 0 : origTables.size();
		List<DerivedBioAssayDataBean> tables = new ArrayList<DerivedBioAssayDataBean>();
		for (int i = 0; i < origNum; i++) {
			tables.add((DerivedBioAssayDataBean) origTables.get(i));
		}
		// remove the one at findInd
		if (origNum > 0) {
			tables.remove(fileInd);
		}
		achar.setDerivedBioAssayDataList(tables);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return mapping.getInputForward();
	}

	public ActionForward addData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("fileInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		DerivedBioAssayDataBean derivedBioAssayDataBean = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayDataList().get(fileInd);
		List<DatumBean> origDataList = derivedBioAssayDataBean.getDatumList();
		int origNum = (origDataList == null) ? 0 : origDataList.size();
		List<DatumBean> dataList = new ArrayList<DatumBean>();
		for (int i = 0; i < origNum; i++) {
			DatumBean dataPoint = (DatumBean) origDataList.get(i);
			dataList.add(dataPoint);
		}
		dataList.add(new DatumBean());
		derivedBioAssayDataBean.setDatumList(dataList);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return mapping.getInputForward();
	}

	public ActionForward removeData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("fileInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		String dataIndexStr = (String) request.getParameter("dataInd");
		int dataInd = Integer.parseInt(dataIndexStr);
		DerivedBioAssayDataBean derivedBioAssayDataBean = (DerivedBioAssayDataBean) achar
				.getDerivedBioAssayDataList().get(fileInd);
		List<DatumBean> origDataList = derivedBioAssayDataBean.getDatumList();
		int origNum = (origDataList == null) ? 0 : origDataList.size();
		List<DatumBean> dataList = new ArrayList<DatumBean>();
		for (int i = 0; i < origNum; i++) {
			DatumBean dataPoint = (DatumBean) origDataList.get(i);
			dataList.add(dataPoint);
		}
		if (origNum > 0)
			dataList.remove(dataInd);
		derivedBioAssayDataBean.setDatumList(dataList);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return mapping.getInputForward();
	}

	/**
	 * Pepopulate data for the form
	 * 
	 * @param request
	 * @param theForm
	 * @throws Exception
	 */

	public ActionForward deleteConfirmed(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		String strCharId = theForm.getString("characterizationId");

		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.deleteCharacterizations(particleName, particleType,
				new String[] { strCharId });

		// signal the session that characterization has been changed
		request.getSession().setAttribute("newCharacterizationCreated", "true");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.delete.characterization");
		msgs.add("message", msg);
		saveMessages(request, msgs);

		return mapping.findForward("success");
	}

	public boolean loginRequired() {
		return true;
	}
}