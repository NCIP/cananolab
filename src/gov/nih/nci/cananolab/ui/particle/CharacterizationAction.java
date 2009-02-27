package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.ExperimentConfigServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationResultServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleSampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Base action for characterization actions
 *
 * @author pansu
 *
 */
public class CharacterizationAction extends BaseAnnotationAction {
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
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		if (charBean.getAssayCategory().equals(
				"Phsico-Chemical Characterization")) {
			InitCharacterizationSetup.getInstance()
					.persistCharacterizationDropdowns(request, charBean);
			InitCharacterizationSetup
					.getInstance()
					.persistPhysicalCharacterizationDropdowns(request, charBean);
		} else if (charBean.getAssayCategory().equals(
				"Invitro Characterization")) {
			InitCharacterizationSetup.getInstance()
					.persistCharacterizationDropdowns(request, charBean);
			InitCharacterizationSetup.getInstance()
					.persistInvitroCharacterizationDropdowns(request, charBean);
		}
		// TODO::
		// if (!validateDerivedDatum(request, charBean)) {
		// return mapping.getInputForward();
		// }

		saveCharacterization(request, theForm, charBean);

		ActionMessages msgs = new ActionMessages();
		// validate number by javascript filterFloatingNumber
		// validateNumber(request, charBean, msgs);
		ActionMessage msg = new ActionMessage("message.addCharacterization",
				charBean.getCharacterizationName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return summaryEdit(mapping, form, request, response);
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
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		setupInputForm(request, theForm);
		// reset characterizationBean
		CharacterizationBean charBean = new CharacterizationBean();
		theForm.set("achar", charBean);
		String charType = request.getParameter("charType");
		if (charType != null) {
			charBean.setCharacterizationType(charType);
			SortedSet<String> charNames = InitCharacterizationSetup
					.getInstance().getCharNamesByCharType(request,
							charBean.getCharacterizationType());
			request.getSession().setAttribute("charTypeChars", charNames);
		}
		return mapping.getInputForward();
	}

	/**
	 * Set up drop-downs need for the input form
	 *
	 * @param request
	 * @param theForm
	 * @throws Exception
	 */
	private void setupInputForm(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String particleId = request.getParameter("particleId");
		String charType = request.getParameter("charType");
		InitNanoparticleSetup.getInstance().setSharedDropdowns(request);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, particleId);
		InitExperimentConfigSetup.getInstance().setExperimentConfigDropDowns(
				request);
		InitProtocolSetup.getInstance().getProtocolFilesByChar(request,
				charType);
		InitCharacterizationSetup.getInstance()
				.setPhysicalCharacterizationDropdowns(request);
		InitCharacterizationSetup.getInstance()
				.setInvitroCharacterizationDropdowns(request);
		// String detailPage = setupDetailPage(charBean);
		// request.getSession().setAttribute("characterizationDetailPage",
		// detailPage);

		// set up other particles with the same primary point of contact
		InitNanoparticleSetup.getInstance().getOtherParticleNames(request,
				particleId);
	}

	/**
	 * Set up the input form for editing existing characterization
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
		String charId = request.getParameter("charId");
		String charClass = request.getParameter("charClassName");
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		Characterization chara = charService.findCharacterizationById(charId,
				charClass);
		CharacterizationBean charBean = new CharacterizationBean(chara);
		// setup correct display for characterization name and characterization
		// type
		InitCharacterizationSetup.getInstance().setCharacterizationName(
				request, charBean);
		InitCharacterizationSetup.getInstance().setCharacterizationType(
				request, charBean);
		// setup dropdown for existing characterization
		SortedSet<String> charNames = InitCharacterizationSetup.getInstance()
				.getCharNamesByCharType(request,
						charBean.getCharacterizationType());
		request.getSession().setAttribute("charTypeChars", charNames);
		SortedSet<String> assayEndpoints = InitCharacterizationSetup
				.getInstance().getAssayTypesByCharName(request,
						charBean.getCharacterizationName());
		request.getSession().setAttribute("charNameAssays", assayEndpoints);
		theForm.set("achar", charBean);
		setupInputForm(request, theForm);

		return mapping.getInputForward();
	}

	private String setupDetailPage(CharacterizationBean charBean) {
		String includePage = null;
		if (charBean.getClassName().equals("PhysicalState")
				|| charBean.getClassName().equals("Shape")
				|| charBean.getClassName().equals("Solubility")
				|| charBean.getClassName().equals("Surface")) {
			includePage = "/particle/characterization/physical/body"
					+ charBean.getClassName() + "Info.jsp";
		} else if (charBean.getClassName().equals("Cytotoxicity")) {
			includePage = "/particle/characterization/invitro/body"
					+ charBean.getClassName() + "Info.jsp";
		}
		return includePage;
	}

	private void saveToOtherParticles(HttpServletRequest request,
			Characterization copy, UserBean user, String particleSampleName,
			NanoparticleSample[] otherSamples) throws Exception {
		FileService fileService = new FileServiceLocalImpl();
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		for (NanoparticleSample sample : otherSamples) {
			charService.saveCharacterization(sample, copy);
			// update copied filename and save content and set visibility
			// TODO
			// if (copy.getDerivedBioAssayDataCollection() != null) {
			// for (DerivedBioAssayData bioassay : copy
			// .getDerivedBioAssayDataCollection()) {
			// if (bioassay.getFile() != null) {
			// fileService.saveCopiedFileAndSetVisibility(bioassay
			// .getFile(), user, particleSampleName, sample
			// .getName());
			// }
			// }
			// }
		}
	}

	private void setupDomainChar(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean)
			throws Exception {
		// setup domainFile for fileBeans
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
				+ "/"
				+ StringUtils.getOneWordLowerCaseFirstLetter(InitSetup
						.getInstance().getDisplayName(charBean.getClassName(),
								request.getSession().getServletContext()));
		if (charBean.getClassName() == null) {
			String className = InitSetup.getInstance().getClassName(
					charBean.getCharacterizationName(),
					request.getSession().getServletContext());
			charBean.setClassName(className);
		}
		charBean.setupDomainChar(user.getLoginName(), internalUriPath);
	}

	// TODO for datum and condition
	// protected boolean validateDerivedDatum(HttpServletRequest request,
	// CharacterizationBean charBean) throws Exception {
	//
	// ActionMessages msgs = new ActionMessages();
	// boolean noErrors = true;
	// for (DerivedBioAssayDataBean derivedBioassayDataBean : charBean
	// .getDerivedBioAssayDataList()) {
	//
	// List<DerivedDatumBean> datumList = derivedBioassayDataBean
	// .getDatumList();
	// FileBean lfBean = derivedBioassayDataBean.getFileBean();
	//
	// // error, if no data input from either the lab file or derived datum
	// boolean noFileError = true;
	// if (datumList == null || datumList.size() == 0) {
	// noFileError = validateFileBean(request, msgs, lfBean);
	// if (!noFileError) {
	// ActionMessage msg = new ActionMessage("errors.required",
	// "If no derived datum entered, the file data");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// this.saveErrors(request, msgs);
	// noErrors = false;
	// }
	// }
	//
	// for (DerivedDatumBean datum : datumList) {
	// // if value field is populated, so does the name field.
	// if (datum.getDomainDerivedDatum().getName().length() == 0) {
	// ActionMessage msg = new ActionMessage("errors.required",
	// "Derived data name");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// this.saveErrors(request, msgs);
	// noErrors = false;
	// }
	// try {
	// Float value = new Float(datum.getValueStr());
	// // for boolean type, the value must be 0/1
	// if (datum.getDomainDerivedDatum().getValueType()
	// .equalsIgnoreCase("boolean")
	// && value != 0.0 && value != 1.0) {
	// ActionMessage msg = new ActionMessage(
	// "error.booleanValue", "Derived data value");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// saveErrors(request, msgs);
	// noErrors = false;
	// }
	// } catch (NumberFormatException e) {
	// // for boolean type, the value must be true/false
	// if (datum.getDomainDerivedDatum().getValueType()
	// .equalsIgnoreCase("boolean")
	// && !datum.getValueStr().equalsIgnoreCase("true")
	// && !datum.getValueStr().equalsIgnoreCase("false")) {
	// ActionMessage msg = new ActionMessage(
	// "error.booleanValue", "Derived data value");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// saveErrors(request, msgs);
	// noErrors = false;
	// } else if (!datum.getDomainDerivedDatum().getValueType()
	// .equalsIgnoreCase("boolean")) {
	// ActionMessage msg = new ActionMessage(
	// "error.derivedDatumValue", "Derived data value");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// saveErrors(request, msgs);
	// noErrors = false;
	// }
	// }
	// }
	// }
	// return noErrors;
	// }

	private void saveCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean)
			throws Exception {
		// setup domainFile for fileBeans
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		setupDomainChar(request, theForm, charBean);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		charService.saveCharacterization(
				particleBean.getDomainParticleSample(), charBean
						.getDomainChar());

		// set public visibility
		AuthorizationService authService = new AuthorizationService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = authService.getAccessibleGroups(
				particleBean.getDomainParticleSample().getName(),
				CaNanoLabConstants.CSM_READ_PRIVILEGE);
		if (accessibleGroups != null
				&& accessibleGroups
						.contains(CaNanoLabConstants.CSM_PUBLIC_GROUP)) {
			charService.assignPublicVisibility(authService, charBean
					.getDomainChar());
		}

		// save file data to file system and set visibility
		List<FileBean> files = new ArrayList<FileBean>();
		// TODO::
		// for (DerivedBioAssayDataBean bioassay : charBean
		// .getDerivedBioAssayDataList()) {
		// if (bioassay.getFileBean() != null) {
		// files.add(bioassay.getFileBean());
		// }
		// }
		saveFilesToFileSystem(files);

		// save to other particles
		NanoparticleSample[] otherSamples = prepareCopy(request, theForm);
		if (otherSamples != null) {
			Boolean copyData = (Boolean) theForm.get("copyData");
			Characterization copy = charBean.getDomainCopy(copyData);
			saveToOtherParticles(request, copy, user, particleBean
					.getDomainParticleSample().getName(), otherSamples);
		}
		particleBean = setupParticle(theForm, request, "local");
	}

	private void deleteCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean,
			String createdBy) throws Exception {
		ParticleBean particleBean = setupParticle(theForm, request, "local");
		// setup domainFile uri for fileBeans
		String internalUriPath = CaNanoLabConstants.FOLDER_PARTICLE
				+ "/"
				+ particleBean.getDomainParticleSample().getName()
				+ "/"
				+ StringUtils.getOneWordLowerCaseFirstLetter(InitSetup
						.getInstance().getDisplayName(charBean.getClassName(),
								request.getSession().getServletContext()));
		charBean.setupDomainChar(createdBy, internalUriPath);
		NanoparticleCharacterizationService charService = new NanoparticleCharacterizationServiceLocalImpl();
		charService.deleteCharacterization(charBean.getDomainChar());
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		deleteCharacterization(request, theForm, charBean, user.getLoginName());
		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteCharacterization");
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		ActionForward forward = mapping.findForward("success");
		request.setAttribute("updateDataTree", "true");
		String particleId = theForm.getString("particleId");
		NanoparticleSampleService sampleService = new NanoparticleSampleServiceLocalImpl();
		ParticleBean particleBean = sampleService
				.findNanoparticleSampleById(particleId);
		InitNanoparticleSetup.getInstance().getDataTree(particleBean, request);
		return forward;
	}

	public void validateNumber(HttpServletRequest request,
			CharacterizationBean charBean, ActionMessages msgs)
			throws Exception {
		if (charBean.getSolubility().getCriticalConcentration() == 0.0) {
			ActionMessage msg = new ActionMessage("message.invalidNumber");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		}
	}

	private void setCharacterizationFileFullPath(HttpServletRequest request,
			CharacterizationBean charBean, String location) throws Exception {
		if (location.equals("local")) {
			// TODO::
			// set file full path
			// for (DerivedBioAssayDataBean bioassayBean : charBean
			// .getDerivedBioAssayDataList()) {
			// if (bioassayBean.getFileBean() != null) {
			// FileBean fileBean = bioassayBean.getFileBean();
			// if (!fileBean.getDomainFile().getUriExternal()) {
			// String fileRoot = PropertyReader.getProperty(
			// CaNanoLabConstants.FILEUPLOAD_PROPERTY,
			// "fileRepositoryDir");
			// fileBean.setFullPath(fileRoot + File.separator
			// + fileBean.getDomainFile().getUri());
			// } else {
			// fileBean.setFullPath(fileBean.getDomainFile().getUri());
			// }
			// }
			// }
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);

			URL localURL = new URL(request.getRequestURL().toString());
			String actionPath = localURL.getPath();
			URL remoteUrl = new URL(serviceUrl);
			String remoteServerHostUrl = remoteUrl.getProtocol() + "://"
					+ remoteUrl.getHost() + ":" + remoteUrl.getPort();
			String remoteDownloadUrlBase = remoteServerHostUrl + actionPath
					+ "?dispatch=download&location=local&fileId=";
			// TODO::
			// for (DerivedBioAssayDataBean bioassayBean : charBean
			// .getDerivedBioAssayDataList()) {
			// if (bioassayBean.getFileBean() != null) {
			// FileBean fileBean = bioassayBean.getFileBean();
			// String remoteDownloadUrl = remoteDownloadUrlBase
			// + fileBean.getDomainFile().getId().toString();
			// fileBean.setFullPath(remoteDownloadUrl);
			// }
			// }
		}
	}

	private CharacterizationSummaryBean setupCharSummary(
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {

		String submitType = request.getParameter("submitType");
		String className = InitSetup.getInstance().getClassName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();
		String location = request.getParameter("location");
		ParticleBean particleBean = setupParticle(theForm, request, location);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		NanoparticleCharacterizationService service = null;
		if (location.equals("local")) {
			service = new NanoparticleCharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new NanoparticleCharacterizationServiceRemoteImpl(
					serviceUrl);
		}
		CharacterizationSummaryBean charSummary = service
				.getParticleCharacterizationSummaryByClass(particleBean
						.getDomainParticleSample().getName(), fullClassName,
						user);
		request.setAttribute("charSummary", charSummary);
		return charSummary;

	}

	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		// setupCharSummary(theForm, request);

		String location = request.getParameter("location");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl + "?page=0&particleId=" + particleId
				+ "&submitType=" + "&dispatch=printSummaryView" + "&location="
				+ location;
		String printAllLinkURL = requestUrl + "?page=0&particleId="
				+ particleId + "&dispatch=printFullSummaryView" + "&location="
				+ location;
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		request.getSession().setAttribute("printFullSummaryViewLinkURL",
				printAllLinkURL);
		return mapping.findForward("summaryView");
	}

	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleId = request.getParameter("particleId");
		NanoparticleCharacterizationService service = new NanoparticleCharacterizationServiceLocalImpl();
		List<CharacterizationBean> charBeans = service
				.findCharsByParticleSampleId(particleId);
		// set characterization types
		for (CharacterizationBean charBean : charBeans) {
			InitCharacterizationSetup.getInstance().setCharacterizationType(
					request, charBean);
			InitCharacterizationSetup.getInstance().setCharacterizationName(
					request, charBean);
		}
		CharacterizationSummaryViewBean summaryView = new CharacterizationSummaryViewBean(
				charBeans);
		request.setAttribute("characterizationSummaryView", summaryView);
		// setupCharSummary(theForm, request);

		String location = request.getParameter("location");
		String requestUrl = request.getRequestURL().toString();
		String printLinkURL = requestUrl + "?page=0&particleId=" + particleId
				+ "&submitType=" + "&dispatch=printSummaryView" + "&location="
				+ location;
		String printAllLinkURL = requestUrl + "?page=0&particleId="
				+ particleId + "&dispatch=printFullSummaryView" + "&location="
				+ location;
		request.getSession().setAttribute("printSummaryViewLinkURL",
				printLinkURL);
		request.getSession().setAttribute("printFullSummaryViewLinkURL",
				printAllLinkURL);
		return mapping.findForward("summaryEdit");
	}

	public ActionForward saveExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		// setupDomainChar(request, theForm, achar);
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		configBean.setupDomain(user.getLoginName());
		ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();
		service.saveExperimentConfig(configBean.getDomain());
		achar.addExperimentConfig(configBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		InitExperimentConfigSetup.getInstance()
				.persistExperimentConfigDropdowns(request, configBean);

		return mapping.getInputForward();
	}

	public ActionForward saveDataSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		setupDomainChar(request, theForm, achar);
		DataSetBean dataSetBean = achar.getTheDataSet();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		dataSetBean.setupDomain(user.getLoginName());
		NanoparticleCharacterizationResultService service = new NanoparticleCharacterizationResultServiceLocalImpl();
		service.saveData(dataSetBean.getData());
		achar.addDataSet(dataSetBean);

		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		return mapping.getInputForward();
	}

	public ActionForward deleteExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		setupDomainChar(request, theForm, achar);
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();
		service.deleteExperimentConfig(configBean.getDomain());
		achar.removeExperimentConfig(configBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		return mapping.getInputForward();
	}
}
