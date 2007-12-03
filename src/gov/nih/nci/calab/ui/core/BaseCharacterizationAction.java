package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationSummaryBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.service.particle.NanoparticleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.particle.InitParticleSetup;
import gov.nih.nci.calab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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

		for (DerivedBioAssayDataBean derivedDataFileBean : charBean
				.getDerivedBioAssayDataList()) {
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
					if (!StringUtils.isDouble(datumBean.getValue())
							&& !StringUtils.isInteger(datumBean.getValue())) {
						throw new RuntimeException(
								"The datum value should be a number.");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		charBean.setParticle(particle);
		return charBean;
	}

	protected void postCreate(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {

		ParticleBean particle = (ParticleBean) theForm.get("particle");
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		// save new lookup up types in the database definition tables.
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addNewCharacterizationDataDropdowns(charBean, charBean
				.getName());

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		request.getSession().setAttribute("newCharacterizationSourceCreated",
				"true");
		request.getSession().setAttribute("newInstrumentCreated", "true");
		request.getSession().setAttribute("newCharacterizationFileTypeCreated",
				"true");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		request.setAttribute("theParticle", particle);
	}

	protected CharacterizationBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		Boolean copyData = (Boolean) theForm.get("copyData");
		CharacterizationBean[] charBeans = new CharacterizationBean[otherParticles.length];
		NanoparticleService service = new NanoparticleService();
		int i = 0;
		for (String particleName : otherParticles) {
			CharacterizationBean newCharBean = charBean.copy(copyData
					.booleanValue());
			// overwrite particle and particle type;
			newCharBean.getParticle().setSampleName(particleName);
			ParticleBean otherParticle = service.getParticleBy(particleName);
			newCharBean.getParticle().setSampleType(
					otherParticle.getSampleType());
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
					derivedBioAssayData.setUri(origUri.replace(particle
							.getSampleName(), particleName));
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

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitParticleSetup.getInstance()
				.setAllCharacterizationDropdowns(session);

		// set up particle
		String particleId = request.getParameter("particleId");
		if (particleId != null) {
			NanoparticleService particleService = new NanoparticleService();
			ParticleBean particle = particleService.getGeneralInfo(particleId);
			theForm.set("particle", particle);
			request.setAttribute("theParticle", particle);
			// set up other particles from the same source
			SortedSet<String> allOtherParticleNames = particleService
					.getOtherParticles(particle.getSampleSource(), particle
							.getSampleName(), user);
			session
					.setAttribute("allOtherParticleNames",
							allOtherParticleNames);

			InitParticleSetup.getInstance().setSideParticleMenu(request,
					particleId);
		} else {
			throw new RuntimeException("Particle ID is required.");
		}
		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitParticleSetup.getInstance().setAllInstruments(session);
		InitParticleSetup.getInstance().setAllDerivedDataFileTypes(session);
		InitParticleSetup.getInstance().setAllPhysicalDropdowns(session);
		InitParticleSetup.getInstance().setAllInvitroDropdowns(session);

		// set characterization
		String submitType = (String) request.getParameter("submitType");
		String characterizationId = request.getParameter("characterizationId");
		if (characterizationId != null) {
			NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
			CharacterizationBean charBean = charService
					.getCharacterizationBy(characterizationId);
			if (charBean == null) {
				throw new RuntimeException(
						"This characterization no longer exists in the database.  Please log in again to refresh.");
			}
			theForm.set("achar", charBean);
		}
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		if (achar != null && submitType != null) {
			achar.setName(submitType);
			InitParticleSetup.getInstance()
					.setAllCharacterizationMeasureUnitsTypes(session,
							submitType);
			InitParticleSetup.getInstance().setDerivedDatumNames(session,
					achar.getName());
			InitProtocolSetup.getInstance().setProtocolFilesByCharName(session,
					achar.getName());

			UserService userService = new UserService(
					CaNanoLabConstants.CSM_APP_NAME);

			// set up characterization files visibility, and whether file is an
			// image
			for (DerivedBioAssayDataBean fileBean : achar
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
				boolean imageStatus = false;
				if (fileBean.getType().length() > 0
						&& fileBean.getType().equalsIgnoreCase("Graph")
						|| fileBean.getType().equalsIgnoreCase("Image")) {
					imageStatus = true;
				} else if (fileBean.getName() != null) {
					imageStatus = StringUtils.isImgFileExt(fileBean.getName());
				}
				fileBean.setImage(imageStatus);
			}

			// set up protocol file visibility
			ProtocolFileBean protocolFileBean = achar.getProtocolFileBean();
			if (protocolFileBean != null) {
				boolean status = false;
				if (protocolFileBean.getId() != null) {
					status = userService.checkReadPermission(user,
							protocolFileBean.getId());
				}
				if (status) {
					protocolFileBean.setHidden(false);
				} else {
					protocolFileBean.setHidden(true);
				}
			}
		}
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
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		// set characterizations with additional information
		if (charBean instanceof ShapeBean) {
			theForm.set("shape", charBean);
		} else if (charBean instanceof MorphologyBean) {
			theForm.set("morphology", charBean);
		} else if (charBean instanceof SolubilityBean) {
			theForm.set("solubility", charBean);
		} else if (charBean instanceof SurfaceBean) {
			theForm.set("surface", charBean);
		} else if (charBean instanceof SolubilityBean) {
			theForm.set("solubility", charBean);
		} else if (charBean instanceof CytotoxicityBean) {
			theForm.set("cytotoxicity", charBean);
		}

		return mapping.findForward("setup");
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);
		return mapping.findForward("detailView");
	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);
		String submitType = request.getParameter("submitType");
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		List<CharacterizationSummaryBean> charSummaryBeans = service
				.getParticleCharacterizationSummaryByName(submitType, particle
						.getSampleId());
		if (charSummaryBeans == null) {
			throw new Exception(
					"There are no such characterizations in the database.");
		}

		// set data labels and file visibility, and whether file is an image
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		SortedSet<String> datumLabels = new TreeSet<String>();

		for (CharacterizationSummaryBean summaryBean : charSummaryBeans) {
			Map<String, String> datumMap = summaryBean.getDatumMap();
			if (datumMap != null && !datumMap.isEmpty()) {
				datumLabels.addAll(datumMap.keySet());
			}
			DerivedBioAssayDataBean fileBean = summaryBean.getCharFile();
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
			boolean imageStatus = false;
			if (fileBean.getType().length() > 0
					&& fileBean.getType().equalsIgnoreCase("Graph")
					|| fileBean.getType().equalsIgnoreCase("Image")) {
				imageStatus = true;
			} else if (fileBean.getName() != null) {
				imageStatus = StringUtils.isImgFileExt(fileBean.getName());
			}
			fileBean.setImage(imageStatus);
		}
		request.setAttribute("nameCharacterizationSummary", charSummaryBeans);
		request.setAttribute("datumLabels", datumLabels);

		return mapping.findForward("summaryView");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		request.setAttribute("loadFileForward", mapping.findForward("setup")
				.getPath());
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		int fileNum = Integer.parseInt(request.getParameter("fileNumber"));
		DerivedBioAssayDataBean derivedBioAssayDataBean = achar
				.getDerivedBioAssayDataList().get(fileNum);
		derivedBioAssayDataBean.setParticleName(particle.getSampleName());
		derivedBioAssayDataBean.setCharacterizationName(achar.getName());
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
		FileService service = new FileService();
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileIndexStr = (String) request.getParameter("compInd");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}

	public ActionForward addData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("compInd");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		return input(mapping, form, request, response);
	}

	public ActionForward removeData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String fileIndexStr = (String) request.getParameter("compInd");
		int fileInd = Integer.parseInt(fileIndexStr);
		String dataIndexStr = (String) request.getParameter("childCompInd");
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
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
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
		initSetup(request, theForm);
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		String charId = request.getParameter("characterizationId");

		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.deleteCharacterizations(new String[] { charId });

		// signal the session that characterization has been changed
		request.getSession().setAttribute("newCharacterizationCreated", "true");

		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
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

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}