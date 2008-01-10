package gov.nih.nci.calab.ui.core;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationSummaryBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.InvalidSessionException;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.calab.service.particle.NanoparticleService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.ui.particle.InitParticleSetup;
import gov.nih.nci.calab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
	protected ActionForward prepareCreate(ActionMapping mapping,
			HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");

		// check if viewTitle is already used the same type of
		// characterization for the same particle
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		boolean viewTitleUsed = service
				.isCharacterizationViewTitleUsed(charBean);
		ActionMessages msgs = new ActionMessages();
		if (viewTitleUsed) {
			ActionMessage msg = new ActionMessage("error.viewTitleUsed");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveErrors(request, msgs);
			return mapping.getInputForward();
		}

		// validate that characterization file/derived data can't be empty
		for (DerivedBioAssayDataBean derivedDataFileBean : charBean
				.getDerivedBioAssayDataList()) {
			if (derivedDataFileBean.getType().length() == 0
					&& derivedDataFileBean.getCategories().length == 0
					&& derivedDataFileBean.getDisplayName().length() == 0
					&& derivedDataFileBean.getDatumList().size() == 0) {
				ActionMessage msg = new ActionMessage(
						"error.emptyCharacterizationFile");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveErrors(request, msgs);
				return mapping.getInputForward();
			}
		}

		for (DerivedBioAssayDataBean derivedDataFileBean : charBean
				.getDerivedBioAssayDataList()) {
			Map<String, Integer> uniqueDatumMap = new HashMap<String, Integer>();
			for (DatumBean datumBean : derivedDataFileBean.getDatumList()) {
				// validate that neither name nor value can be empty
				if (datumBean.getName().length() == 0
						|| datumBean.getValue().length() == 0) {
					ActionMessage msg = new ActionMessage(
							"error.emptyDerivedDatum");
					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
					saveErrors(request, msgs);
					return mapping.getInputForward();
				}

				if (datumBean.getStatisticsType().equalsIgnoreCase("boolean")) {
					if (!datumBean.getValue().equalsIgnoreCase("true")
							&& !datumBean.getValue().equalsIgnoreCase("false")
							&& !datumBean.getValue().equalsIgnoreCase("yes")
							&& !datumBean.getValue().equalsIgnoreCase("no")) {
						ActionMessage msg = new ActionMessage(
								"error.booleanDerivedDatum");
						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
						saveErrors(request, msgs);
						return mapping.getInputForward();
					}
				} else {
					if (!StringUtils.isDouble(datumBean.getValue())
							&& !StringUtils.isInteger(datumBean.getValue())) {
						ActionMessage msg = new ActionMessage(
								"error.derivedDatumFormat");
						msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
						saveErrors(request, msgs);
						return mapping.getInputForward();
					}
				}

				// validate derived data has unique name, statistics type and
				// category
				String uniqueStr = datumBean.getName() + ":"
						+ datumBean.getStatisticsType() + ":"
						+ datumBean.getCategory();
				if (uniqueDatumMap.get(uniqueStr) != null) {
					ActionMessage msg = new ActionMessage(
							"error.uniqueDerivedDatum");
					msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
					saveErrors(request, msgs);
					return mapping.getInputForward();
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
		return null;
	}

	protected ActionForward postCreate(HttpServletRequest request,
			DynaValidatorForm theForm, ActionMapping mapping) throws Exception {

		ParticleBean particle = (ParticleBean) theForm.get("particle");
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		// save new lookup up types in the database definition tables.
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.addNewCharacterizationDataDropdowns(charBean, charBean
				.getName());
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		service.setCharacterizationUserVisiblity(charBean, user);
		request.getSession().setAttribute("newCharacterizationCreated", "true");
		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		request.setAttribute("theParticle", particle);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addCharacterization",
				charBean.getName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return mapping.findForward("success");
	}

	protected CharacterizationBean[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		Boolean copyData = (Boolean) theForm.get("copyData");
		CharacterizationBean[] charBeans = new CharacterizationBean[otherParticles.length];
		if (otherParticles.length==0) {
			return charBeans;
		}
		//retrieve file contents
		FileService fileService=new FileService();
		for (DerivedBioAssayDataBean file: charBean.getDerivedBioAssayDataList()) {
			byte[] content=fileService.getFileContent(new Long(file.getId()));
			file.setFileContent(content);
		}
		
		NanoparticleService service = new NanoparticleService();
		int i = 0;
		for (String particleName : otherParticles) {
			CharacterizationBean newCharBean = charBean.copy(copyData
					.booleanValue());
			// overwrite particle
			ParticleBean otherParticle = service.getParticleBy(particleName);
			newCharBean.setParticle(otherParticle);
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
		theForm.set("charSummary", new CharacterizationSummaryBean());
		theForm.set("morphology", new MorphologyBean());
		theForm.set("shape", new ShapeBean());
		theForm.set("surface", new SurfaceBean());
		theForm.set("solubility", new SolubilityBean());
		theForm.set("cytotoxicity", new CytotoxicityBean());
		cleanSessionAttributes(session);
	}

	private void setParticleInForm(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// set up particle
		String particleId = request.getParameter("particleId");
		NanoparticleService particleService = new NanoparticleService();
		ParticleBean particle = particleService.getParticleInfo(particleId,
				user);
		theForm.set("particle", particle);
		request.setAttribute("theParticle", particle);
		InitParticleSetup.getInstance()
				.setSideParticleMenu(request, particleId);
	}

	private void setCharacterizationInForm(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String characterizationId = request.getParameter("characterizationId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = null;
		String submitType = request.getParameter("submitType");
		if (characterizationId != null) {
			NanoparticleCharacterizationService charService = new NanoparticleCharacterizationService();
			charBean = charService.getCharacterizationBy(characterizationId,
					user);
			theForm.set("achar", charBean);
			if (charBean == null) {
				throw new InvalidSessionException(
						"This characterization no longer exists in the database.  Please log in again to refresh.");
			}
		} else {
			charBean = (CharacterizationBean) theForm.get("achar");
		}
		charBean.setName(submitType);
		// set characterization type whether physical or in vitro
		String charType = InitParticleSetup.getInstance().getCharType(
				request.getSession(), charBean.getName());
		charBean.setCharacterizationType(charType);

		// set particle to be a part of charBean so saving characterizations
		// would work
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		// set up other particles from the same source
		NanoparticleService particleService = new NanoparticleService();
		SortedSet<String> allOtherParticleNames = particleService
				.getOtherParticles(particle.getSampleSource(), particle
						.getSampleName(), user);
		request.getSession().setAttribute("allOtherParticleNames",
				allOtherParticleNames);
	}

	private void setCharacterizationSummaryInForm(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		CharacterizationSummaryBean charSummaryBean = (CharacterizationSummaryBean) theForm
				.get("charSummary");
		// set characterization type whether physical or in vitro
		String submitType = (String) request.getParameter("submitType");
		charSummaryBean.setCharacterizationName(submitType);
		String charType = InitParticleSetup.getInstance()
				.getCharType(request.getSession(),
						charSummaryBean.getCharacterizationName());
		charSummaryBean.setCharacterizationType(charType);
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		charSummaryBean.setParticle(particle);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		List<CharacterizationSummaryRowBean> charSummaryRows = service
				.getParticleCharacterizationSummaryByName(charSummaryBean
						.getCharacterizationName(), particle.getSampleId(),
						user);
		if (charSummaryRows == null) {
			throw new InvalidSessionException(
					"These characterizations no longer exist in the database.  Please log in again to refresh");
		}
		charSummaryBean.setSummaryRows(charSummaryRows);
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

		InitParticleSetup.getInstance()
				.setAllCharacterizationDropdowns(session);
		setParticleInForm(request, theForm);
		setCharacterizationInForm(request, theForm);
		String submitType = request.getParameter("submitType");
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		ParticleBean particleBean = (ParticleBean) theForm.get("particle");
		charBean.setParticle(particleBean);
		InitParticleSetup.getInstance()
				.setAllCharacterizationMeasureUnitsTypes(session, submitType);
		InitParticleSetup.getInstance().setDerivedDatumNames(session,
				charBean.getName());
		InitProtocolSetup.getInstance().setProtocolFilesByCharType(session,
				charBean.getCharacterizationType());

		InitSessionSetup.getInstance().setApplicationOwner(session);
		InitParticleSetup.getInstance().setAllInstruments(session);
		InitParticleSetup.getInstance().setAllDerivedDataFileTypes(session);
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

		HttpSession session = request.getSession();
		// update editable dropdowns

		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		String charNameOneWord = StringUtils
				.getOneWordLowerCaseFirstLetter(achar.getName());
		String setupPage = charNameOneWord + "Setup";
		if (achar.getName().equals(Characterization.PHYSICAL_SHAPE)) {
			ShapeBean shape = (ShapeBean) theForm.get("shape");
			updateShapeEditable(session, shape);
		} else if (achar.getName().equals(Characterization.PHYSICAL_MORPHOLOGY)) {
			MorphologyBean morphology = (MorphologyBean) theForm
					.get("morphology");
			updateMorphologyEditable(session, morphology);
		} else if (achar.getName().equals(Characterization.PHYSICAL_SOLUBILITY)) {
			SolubilityBean solubility = (SolubilityBean) theForm
					.get("solubility");
			updateSolubilityEditable(session, solubility);
		} else if (achar.getName().equals(Characterization.PHYSICAL_SURFACE)) {
		} else if (achar.getName().equals(
				Characterization.CYTOTOXICITY_CELL_VIABILITY)
				|| achar.getName().equals(
						Characterization.CYTOTOXICITY_CASPASE3_ACTIVIATION)) {
			CytotoxicityBean cyto = (CytotoxicityBean) theForm
					.get("cytotoxicity");
			updateCytotoxicityEditable(session, cyto);
			setupPage = "cytotoxicitySetup";
		} else {
			setupPage = "setup";
		}
		updateAllCharEditables(session, achar);

		// updateSurfaceEditable(session, surface);
		return mapping.findForward(setupPage);
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
		return input(mapping, form, request, response);
	}

	public ActionForward detailView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);
		String particleId = request.getParameter("particleId");
		String characterizationId = request.getParameter("characterizationId");
		String submitType = request.getParameter("submitType");
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		String printLinkURL = "/caNanoLab/" + charBean.getActionName()
				+ ".do?page=0&dispatch=printDetailView&particleId="
				+ particleId + "&characterizationId=" + characterizationId
				+ "&submitType=" + submitType;
		request.getSession().setAttribute("printDetailViewLinkURL", printLinkURL);

		return mapping.findForward("detailView");
	}

	public ActionForward exportDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);

		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename="
				+ achar.getExportFileName() + ".xls");
		OutputStream out = response.getOutputStream();
		HSSFWorkbook wb = new HSSFWorkbook();
		service.exportDetailService(achar, wb, user);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}

		return null;
	}

	public ActionForward printDetailView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		initSetup(request, theForm);
		return mapping.findForward("detailPrintView");
	}

	// used for summaryViews
	protected void summaryInitSetup(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		HttpSession session = request.getSession();
		clearMap(session, theForm);
		setParticleInForm(request, theForm);
		setCharacterizationSummaryInForm(request, theForm);
	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		summaryInitSetup(request, theForm);
		ParticleBean particle = (ParticleBean) theForm.get("particle");
		CharacterizationSummaryBean charSummaryBean = (CharacterizationSummaryBean) theForm
				.get("charSummary");
		String printLinkURL = "/caNanoLab/" + charSummaryBean.getActionName()
				+ ".do?page=0&dispatch=printSummaryView&particleId="
				+ particle.getSampleId() + "&submitType="
				+ charSummaryBean.getCharacterizationName();
		String printAllLinkURL = "/caNanoLab/"
				+ charSummaryBean.getActionName()
				+ ".do?page=0&dispatch=printFullSummaryView&particleId="
				+ particle.getSampleId() + "&submitType="
				+ charSummaryBean.getCharacterizationName();
		request.setAttribute("printSummaryViewLinkURL", printLinkURL);
		request.setAttribute("printFullSummaryViewLinkURL", printAllLinkURL);
		return mapping.findForward("summaryView");
	}

	public ActionForward printSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		summaryInitSetup(request, theForm);
		return mapping.findForward("summaryPrintView");
	}

	public ActionForward printFullSummaryView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		summaryInitSetup(request, theForm);
		return mapping.findForward("summaryPrintAllView");
	}

	public ActionForward exportSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		summaryInitSetup(request, theForm);
		CharacterizationSummaryBean charSummaryBean = (CharacterizationSummaryBean) theForm
				.get("charSummary");
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename="
				+ charSummaryBean.getExportFileName() + ".xls");
		OutputStream out = response.getOutputStream();
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.exportSummaryService(charSummaryBean, out);
		return null;
	}

	public ActionForward exportFullSummary(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		summaryInitSetup(request, theForm);
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		CharacterizationSummaryBean charSummaryBean = (CharacterizationSummaryBean) theForm
				.get("charSummary");
		response.setContentType("application/vnd.ms-execel");
		response.setHeader("cache-control", "Private");
		response.setHeader("Content-disposition", "attachment;filename="
				+ charSummaryBean.getFullExportFileName() + ".xls");
		OutputStream out = response.getOutputStream();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		service.exportFullSummaryService(charSummaryBean, user, out);
		return null;
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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService service = new FileService();
		LabFileBean fileBean = service.getFile(fileId, user);
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
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"error.noCharacterizationFile");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			return mapping.findForward("particleMessage");
		}
		return null;
	}

	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//if user pressed cancel in load characterization file
		String cancel=request.getParameter("cancel");
		if (cancel!=null) {
			return input(mapping, form, request, response);
		}
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
		//if user pressed cancel in load characterization file
		String cancel=request.getParameter("cancel");
		if (cancel!=null) {
			return input(mapping, form, request, response);
		}
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
		//if user pressed cancel in load characterization file
		String cancel=request.getParameter("cancel");
		if (cancel!=null) {
			return input(mapping, form, request, response);
		}
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
		//if user pressed cancel in load characterization file
		String cancel=request.getParameter("cancel");
		if (cancel!=null) {
			return input(mapping, form, request, response);
		}
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
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		ParticleBean particle = (ParticleBean) theForm.get("particle");

		NanoparticleCharacterizationService service = new NanoparticleCharacterizationService();
		service.deleteCharacterizations(new String[] { charBean.getId() });

		// signal the session that characterization has been changed
		request.getSession().setAttribute("newCharacterizationCreated", "true");

		InitParticleSetup.getInstance().setSideParticleMenu(request,
				particle.getSampleId());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.delete.characterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);

		return mapping.findForward("particleMessage");
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

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}
}