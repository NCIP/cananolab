package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.domain.nano.characterization.physical.Shape;
import gov.nih.nci.calab.domain.nano.characterization.physical.Solubility;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.domain.nano.characterization.toxicity.Cytotoxicity;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
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
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * CVS $Id: BaseCharacterizationAction.java,v 1.73 2007/08/02 21:41:47 zengje
 * Exp $
 */

public abstract class BaseCharacterizationAction extends AbstractDispatchAction {
	protected CharacterizationBean prepareCreate(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");

		// validate that characterization file/derived data can't be empty
		for (DerivedBioAssayDataBean derivedDataFileBean : charBean
				.getDerivedBioAssayDataList()) {
			if (derivedDataFileBean.getType().length() == 0
					&& derivedDataFileBean.getCategories().length == 0
					&& derivedDataFileBean.getDisplayName().length() == 0
					&& derivedDataFileBean.getDatumList().size() == 0) {
				throw new RuntimeException(
						"has an empty section for characterization file/derived data. Please remove it prior to saving.");
			}
		}

		// retrieve file content
		FileService fileService = new FileService();
		for (DerivedBioAssayDataBean derivedDataFileBean : charBean
				.getDerivedBioAssayDataList()) {
			if (derivedDataFileBean.getId() != null) {
				byte[] content = fileService.getFileContent(new Long(
						derivedDataFileBean.getId()));
				if (content != null) {
					derivedDataFileBean.setFileContent(content);
				}
			}

			Map<String, Integer> uniqueDatumMap = new HashMap<String, Integer>();
			for (DatumBean datumBean : derivedDataFileBean.getDatumList()) {
				// validate that neither name nor value can be empty
				if (datumBean.getName().length() == 0
						|| datumBean.getValue().length() == 0) {
					throw new RuntimeException(
							"Derived data name and value can't be empty.");
				}

				if (datumBean.getStatisticsType().equalsIgnoreCase("boolean")) {
					if (!datumBean.getValue().equalsIgnoreCase("true")
							&& !datumBean.getValue().equalsIgnoreCase("false")
							&& !datumBean.getValue().equalsIgnoreCase("yes")
							&& !datumBean.getValue().equalsIgnoreCase("no")) {
						throw new RuntimeException(
								"The datum value for boolean type should be 'True'/'False' or 'Yes'/'No'.");
					}
				} else {
					if (!StringUtils.isDouble(datumBean.getValue()) && !StringUtils.isInteger(datumBean.getValue())) {
						throw new RuntimeException("The datum value should be a number.");
					}
				}
				
				

				// validate derived data has unique name, statistics type and
				// category
				String uniqueStr = datumBean.getName() + ":"
						+ datumBean.getStatisticsType() + ":"
						+ datumBean.getCategory();
				if (uniqueDatumMap.get(uniqueStr) != null) {
					throw new RuntimeException(
							"no two derived data can have the same name, statistics type and category.");
				} else {
					uniqueDatumMap.put(uniqueStr, 1);
				}
			}
		}

		// set createdBy and createdDate for the characterization
		UserBean user = (UserBean) session.getAttribute("user");
		Date date = new Date();
		charBean.setCreatedBy(user.getLoginName());
		charBean.setCreatedDate(date);
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		charBean.setParticleName(particleName);
		charBean.setParticleType(particleType);
		return charBean;
	}

	protected void postCreate(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {

		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		String charName = theForm.getString("charName");
		// save new lookup up types in the database definition tables.
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addNewCharacterizationDataDropdowns(charBean, charName);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		request.getSession().setAttribute("newCharacterizationSourceCreated",
				"true");
		request.getSession().setAttribute("newInstrumentCreated", "true");
		request.getSession().setAttribute("newCharacterizationFileTypeCreated",
				"true");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
	}

	protected CharacterizationBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm, SubmitNanoparticleService service)
			throws Exception {
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		String origParticleName = theForm.getString("particleName");
		charBean.setParticleName(origParticleName);
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		Boolean copyData = (Boolean) theForm.get("copyData");
		CharacterizationBean[] charBeans = new CharacterizationBean[otherParticles.length];
		SearchNanoparticleService searchService=new SearchNanoparticleService();
		int i = 0;
		for (String particleName : otherParticles) {
			CharacterizationBean newCharBean = charBean.copy(copyData
					.booleanValue());
			newCharBean.setParticleName(particleName);
			Nanoparticle otherParticle=searchService.getParticleBy(particleName);
			newCharBean.setParticleType(otherParticle.getType());
			// reset view title
			String timeStamp = StringUtils.convertDateToString(new Date(),
					"MMddyyHHmmssSSS");
			String autoTitle = CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_TITLE_PREFIX
					+ timeStamp;

			newCharBean.setViewTitle(autoTitle);
			List<DerivedBioAssayDataBean> dataList = newCharBean
					.getDerivedBioAssayDataList();
			// replace particleName in path and uri with new particleName
			for (DerivedBioAssayDataBean derivedBioAssayData : dataList) {
				String origUri = derivedBioAssayData.getUri();
				if (origUri != null)
					derivedBioAssayData.setUri(origUri.replace(
							origParticleName, particleName));
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
		theForm.set("copyData", false);
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
		String particleSource = theForm.getString("particleSource");
		String charName = theForm.getString("charName");
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setAllInstruments(session);
		InitSessionSetup.getInstance().setAllDerivedDataFileTypes(session);
		InitSessionSetup.getInstance().setAllPhysicalDropdowns(session);
		InitSessionSetup.getInstance().setAllInvitroDropdowns(session);
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
					session, "In Vitro assay");

		// set up other particle names from the same source
		LookupService service = new LookupService();
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		SortedSet<String> allOtherParticleNames = service.getOtherParticles(
				particleSource, particleName, user);
		session.setAttribute("allOtherParticleNames", allOtherParticleNames);

		InitSessionSetup.getInstance().setDerivedDatumNames(session, charName);
		InitSessionSetup.getInstance().setAllCharacterizationDropdowns(session);
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

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		// update editable dropdowns
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		ShapeBean shape = (ShapeBean) theForm.get("shape");
		MorphologyBean morphology = (MorphologyBean) theForm.get("morphology");
		CytotoxicityBean cyto = (CytotoxicityBean) theForm.get("cytotoxicity");
		SolubilityBean solubility = (SolubilityBean) theForm.get("solubility");
		SurfaceBean surface = (SurfaceBean) theForm.get("surface");
		HttpSession session = request.getSession();
		updateAllCharEditables(session, achar);
		updateShapeEditable(session, shape);
		updateMorphologyEditable(session, morphology);
		updateCytotoxicityEditable(session, cyto);
		updateSolubilityEditable(session, solubility);
		// updateSurfaceEditable(session, surface);
		return mapping.findForward("setup");
	}

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
				.getCharacterizationAndDerivedDataBy(characterizationId);
		if (aChar == null) {
			throw new Exception(
					"This characterization no longer exists in the database.  Please log in again to refresh.");
		}
		CharacterizationBean charBean = new CharacterizationBean(aChar);
		theForm.set("achar", charBean);

		// set characterizations with additional information
		if (aChar instanceof Shape) {
			theForm.set("shape", new ShapeBean((Shape) aChar));
		} else if (aChar instanceof Morphology) {
			theForm.set("morphology", new MorphologyBean((Morphology) aChar));
		} else if (aChar instanceof Solubility) {
			theForm.set("solubility", new SolubilityBean((Solubility) aChar));
		} else if (aChar instanceof Surface) {
			theForm.set("surface", new SurfaceBean((Surface) aChar));
		} else if (aChar instanceof Solubility) {
			theForm.set("solubility", new SolubilityBean((Solubility) aChar));
		} else if (aChar instanceof Cytotoxicity) {
			theForm.set("cytotoxicity", new CytotoxicityBean(
					(Cytotoxicity) aChar));
		}

		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		// set up charaterization files visibility
		for (DerivedBioAssayDataBean fileBean : charBean
				.getDerivedBioAssayDataList()) {
			boolean status = userService.checkReadPermission(user, fileBean
					.getId());
			if (status) {
				List<String> accessibleGroups = userService
						.getAccessibleGroups(fileBean.getId(),
								CaNanoLabConstants.CSM_READ_ROLE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				fileBean.setVisibilityGroups(visibilityGroups);
				fileBean.setHidden(false);
			} else {
				fileBean.setHidden(true);
			}
		}
		return mapping.findForward("setup");
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

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName = theForm.getString("particleName");
		request.setAttribute("loadFileForward", mapping.findForward("setup")
				.getPath());
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		int fileNum = Integer.parseInt(request.getParameter("fileNumber"));
		DerivedBioAssayDataBean derivedBioAssayDataBean = achar
				.getDerivedBioAssayDataList().get(fileNum);
		derivedBioAssayDataBean.setParticleName(particleName);
		derivedBioAssayDataBean.setCharacterizationName(theForm
				.getString("charName"));
		request.setAttribute("file", derivedBioAssayDataBean);
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
		File dFile = new File(fileRoot + File.separator + fileBean.getUri());
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
			tables.add(origTables.get(i));
		}
		// add a new one
		tables.add(new DerivedBioAssayDataBean());
		achar.setDerivedBioAssayDataList(tables);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileIndexStr = (String) request.getParameter("fileInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		List<DerivedBioAssayDataBean> origTables = achar
				.getDerivedBioAssayDataList();
		int origNum = (origTables == null) ? 0 : origTables.size();
		List<DerivedBioAssayDataBean> tables = new ArrayList<DerivedBioAssayDataBean>();
		for (int i = 0; i < origNum; i++) {
			tables.add(origTables.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			tables.remove(fileInd);
		}
		achar.setDerivedBioAssayDataList(tables);
		String particleName = theForm.getString("particleName");
		String particleType = theForm.getString("particleType");
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return input(mapping, form, request, response);
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
		return input(mapping, form, request, response);
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
		return input(mapping, form, request, response);
		// return mapping.getInputForward(); this gives an
		// IndexOutOfBoundException in the jsp page
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

	// add edited option to all editable dropdowns
	private void updateAllCharEditables(HttpSession session,
			CharacterizationBean achar) throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				achar.getCharacterizationSource(), "characterizationSources");
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				achar.getInstrumentConfigBean().getInstrumentBean().getType(),
				"allInstrumentTypes");
		InitSessionSetup.getInstance().updateEditableDropdown(
				session,
				achar.getInstrumentConfigBean().getInstrumentBean()
						.getManufacturer(), "allManufacturers");
		for (DerivedBioAssayDataBean derivedBioAssayDataBean : achar
				.getDerivedBioAssayDataList()) {
			InitSessionSetup.getInstance().updateEditableDropdown(session,
					derivedBioAssayDataBean.getType(),
					"allDerivedDataFileTypes");
			if (derivedBioAssayDataBean != null) {
				for (String category : derivedBioAssayDataBean.getCategories()) {
					InitSessionSetup.getInstance().updateEditableDropdown(
							session, category, "derivedDataCategories");
				}

				for (DatumBean datum : derivedBioAssayDataBean.getDatumList()) {
					InitSessionSetup.getInstance().updateEditableDropdown(
							session, datum.getName(), "datumNames");
					InitSessionSetup.getInstance().updateEditableDropdown(
							session, datum.getStatisticsType(),
							"charMeasureTypes");
					InitSessionSetup.getInstance().updateEditableDropdown(
							session, datum.getUnit(), "charMeasureUnits");
				}
			}
		}
	}

	// add edited option to all editable dropdowns
	private void updateShapeEditable(HttpSession session, ShapeBean shape)
			throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				shape.getType(), "allShapeTypes");
	}

	private void updateMorphologyEditable(HttpSession session,
			MorphologyBean morphology) throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				morphology.getType(), "allMorphologyTypes");
	}

	private void updateCytotoxicityEditable(HttpSession session,
			CytotoxicityBean cyto) throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				cyto.getCellLine(), "allCellLines");
	}

	private void updateSolubilityEditable(HttpSession session,
			SolubilityBean solubility) throws Exception {
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				solubility.getSolvent(), "allSolventTypes");
	}

	// private void updateSurfaceEditable(HttpSession session,
	// SurfaceBean surface) throws Exception {
	// }

	public boolean loginRequired() {
		return true;
	}
}