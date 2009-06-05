package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationResultService;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.ExperimentConfigService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationResultServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.ExperimentConfigServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
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
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, charBean);
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
		String sampleId = request.getParameter("sampleId");
		String charType = request.getParameter("charType");
		InitSampleSetup.getInstance().setSharedDropdowns(request);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, sampleId);
		InitExperimentConfigSetup.getInstance().setExperimentConfigDropDowns(
				request);
		if (charType != null)
			InitProtocolSetup.getInstance().getProtocolsByChar(request,
					charType);
		InitCharacterizationSetup.getInstance().setCharacterizationDropdowns(
				request);
		// String detailPage = setupDetailPage(charBean);
		// request.getSession().setAttribute("characterizationDetailPage",
		// detailPage);

		// set up other samples with the same primary point of contact
		InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId);
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
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		Characterization chara = charService.findCharacterizationById(charId,
				charClass);
		CharacterizationBean charBean = new CharacterizationBean(chara);
		// retrieve visibility
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		charService.retrieveVisiblity(charBean, user);
		// setup correct display for characterization name and characterization
		// type
		InitCharacterizationSetup.getInstance().setCharacterizationName(
				request, charBean);
		InitCharacterizationSetup.getInstance().setCharacterizationType(
				request, charBean);
		// setup dropdown for existing characterization
		InitCharacterizationSetup.getInstance().getCharNamesByCharType(request,
				charBean.getCharacterizationType());
		InitCharacterizationSetup.getInstance().getAssayTypesByCharName(
				request, charBean.getCharacterizationName());
		InitCharacterizationSetup.getInstance().getDatumNamesByCharName(
				request, charBean.getCharacterizationName());

		request.setAttribute("achar", charBean);
		theForm.set("achar", charBean);
		setupInputForm(request, theForm);
		String detailPage = null;
		if (charBean.isWithProperties()) {
			detailPage = setupDetailPage(charBean);
		}
		request.setAttribute("characterizationDetailPage", detailPage);

		return mapping.getInputForward();
	}

	private String setupDetailPage(CharacterizationBean charBean) {
		String includePage = null;
		if (charBean.getClassName().equals("PhysicalState")
				|| charBean.getClassName().equals("Shape")
				|| charBean.getClassName().equals("Solubility")
				|| charBean.getClassName().equals("Surface")) {
			includePage = "physical/body" + charBean.getClassName()
					+ "Info.jsp";
		} else if (charBean.getClassName().equals("Cytotoxicity")
				|| charBean.getClassName().equals("EnzymeInduction")
				|| charBean.getClassName().equals("Transfection")) {
			includePage = "invitro/body" + charBean.getClassName() + "Info.jsp";
		}
		return includePage;
	}

	private void saveToOtherSamples(HttpServletRequest request,
			Characterization copy, UserBean user, String sampleName,
			Sample[] otherSamples) throws Exception {
		FileService fileService = new FileServiceLocalImpl();
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		for (Sample sample : otherSamples) {
			charService.saveCharacterization(sample, copy);
			// update copied filename and save content and set visibility
			// TODO
			// if (copy.getDerivedBioAssayDataCollection() != null) {
			// for (DerivedBioAssayData bioassay : copy
			// .getDerivedBioAssayDataCollection()) {
			// if (bioassay.getFile() != null) {
			// fileService.saveCopiedFileAndSetVisibility(bioassay
			// .getFile(), user, sampleName, sample
			// .getName());
			// }
			// }
			// }
		}
	}

	private void setupDomainChar(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (charBean.getClassName() == null
				|| charBean.getClassName().length() == 0) {
			String className = InitSetup.getInstance().getClassName(
					charBean.getCharacterizationName(),
					request.getSession().getServletContext());
			charBean.setClassName(className);
		}
		charBean.setupDomain(user.getLoginName());
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

		SampleBean sampleBean = setupSample(theForm, request, Constants.LOCAL);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		setupDomainChar(request, theForm, charBean);
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		charService.saveCharacterization(sampleBean.getDomain(), charBean
				.getDomainChar());

		// set public visibility
		AuthorizationService authService = new AuthorizationService(
				Constants.CSM_APP_NAME);
		List<String> accessibleGroups = authService.getAccessibleGroups(
				sampleBean.getDomain().getName(), Constants.CSM_READ_PRIVILEGE);
		if (accessibleGroups != null
				&& accessibleGroups.contains(Constants.CSM_PUBLIC_GROUP)) {
			charService.assignPublicVisibility(authService, charBean
					.getDomainChar());
		}

		// save to other samples
		Sample[] otherSamples = prepareCopy(request, theForm);
		if (otherSamples != null) {
			Boolean copyData = (Boolean) theForm.get("copyData");
			Characterization copy = charBean.getDomainCopy(copyData);
			saveToOtherSamples(request, copy, user, sampleBean.getDomain()
					.getName(), otherSamples);
		}
		sampleBean = setupSample(theForm, request, Constants.LOCAL);
		request.setAttribute("sampleId", sampleBean.getDomain().getId());
		request.setAttribute("location", Constants.LOCAL);
	}

	private void deleteCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean,
			String createdBy) throws Exception {
		charBean.setupDomain(createdBy);
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
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
		String sampleId = theForm.getString("sampleId");
		SampleService sampleService = new SampleServiceLocalImpl();
		SampleBean sampleBean = sampleService.findSampleById(sampleId);
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
		if (location.equals(Constants.LOCAL)) {
			// TODO::
			// set file full path
			// for (DerivedBioAssayDataBean bioassayBean : charBean
			// .getDerivedBioAssayDataList()) {
			// if (bioassayBean.getFileBean() != null) {
			// FileBean fileBean = bioassayBean.getFileBean();
			// if (!fileBean.getDomainFile().getUriExternal()) {
			// String fileRoot = PropertyReader.getProperty(
			// Constants.FILEUPLOAD_PROPERTY,
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

	/**
	 * summaryEdit() handles Edit request for Characterization Summary view.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error occurred.
	 */
	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);

		// "actionName" is for constructing the Print/Export URL.
		request.setAttribute("actionName", request.getRequestURL().toString());

		return mapping.findForward("summaryEdit");
	}

	/**
	 * summaryView() handles View request for Characterization Summary report.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error occurred.
	 */
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);
		this.prepareCharacterizationTypes(mapping, form, request, response);

		// "actionName" is for constructing the Print/Export URL.
		request.setAttribute("actionName", request.getRequestURL().toString());

		return mapping.findForward("summaryView");
	}

	/**
	 * summaryPrint() handles Print request for Characterization Summary report.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error occurred.
	 */
	public ActionForward summaryPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);
		this.prepareCharacterizationTypes(mapping, form, request, response);

		// Filter out un-selected types.
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			List<String> characterizationTypes = (List<String>) request
					.getAttribute("characterizationTypes");
			characterizationTypes.clear();
			characterizationTypes.add(type);
		}
		return mapping.findForward("summaryPrintView");
	}

	/**
	 * Shared function for summaryView(), summaryPrint() and summaryEdit().
	 * Prepare CharacterizationBean based on SampleId.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error occurred.
	 */
	protected void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleId = theForm.getString("sampleId");
		String location = theForm.getString("location");
		CharacterizationService service = null;
		if (Constants.LOCAL.equals(location)) {
			service = new CharacterizationServiceLocalImpl();
		} else {
			// TODO model change
			// String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
			// request, location);
			// service = new CharacterizationServiceRemoteImpl(
			// serviceUrl);
		}
		List<CharacterizationBean> charBeans = service
				.findCharsBySampleId(sampleId);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// set characterization types and retrieve visibility
		for (CharacterizationBean charBean : charBeans) {
			InitCharacterizationSetup.getInstance().setCharacterizationType(
					request, charBean);
			InitCharacterizationSetup.getInstance().setCharacterizationName(
					request, charBean);
			service.retrieveVisiblity(charBean, user);
		}
		CharacterizationSummaryViewBean summaryView = new CharacterizationSummaryViewBean(
				charBeans);
		request.setAttribute("characterizationSummaryView", summaryView);
	}

	/**
	 * Shared function for summaryView() and summaryPrint(). Keep submitted
	 * characterization types in the correct display order. Should be called
	 * after calling prepareSummary().
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error occurred.
	 */
	protected void prepareCharacterizationTypes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CharacterizationSummaryViewBean summaryView = (CharacterizationSummaryViewBean) request
				.getAttribute("characterizationSummaryView");

		// Keep submitted characterization types in the correct display order
		List<String> allCharacterizationTypes = new ArrayList<String>(
				(List<? extends String>) request.getSession().getAttribute(
						"characterizationTypes"));
		List<String> characterizationTypes = new ArrayList<String>();
		for (String charType : allCharacterizationTypes) {
			if (summaryView.getCharacterizationTypes().contains(charType)
					&& !characterizationTypes.contains(charType)) {
				characterizationTypes.add(charType);
			}
		}
		request.setAttribute("characterizationTypes", characterizationTypes);
	}

	/**
	 * summaryExport() handles Export request for Characterization Summary
	 * report.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 *             if error occurred.
	 */
	public ActionForward summaryExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);

		CharacterizationSummaryViewBean charSummaryBean = (CharacterizationSummaryViewBean) request
				.getAttribute("characterizationSummaryView");
		SortedMap<String, List<CharacterizationBean>> charBeanMap = charSummaryBean
				.getType2Characterizations();
		List<CharacterizationBean> charBeans = null;

		// Filter out un-selected types.
		String type = request.getParameter("type");
		String location = request.getParameter("location");
		if (!StringUtils.isEmpty(type)) {
			charBeans = charBeanMap.get(type);
			if (charBeans != null) {
				charBeanMap.clear();
				charBeanMap.put(type, charBeans);
			}
		}

		// Get sample name for constructing file name.
		charBeans = charBeanMap.get(charBeanMap.firstKey());
		CharacterizationBean charBean = (CharacterizationBean) charBeans.get(0);

		String fileName = this.getExportFileName(charBean.getDomainChar()
				.getSample().getName(), "summaryView", charBean.getClassName());
		ExportUtils.prepareReponseForExcell(response, fileName);
		CharacterizationService service = null;
		if (Constants.LOCAL.equals(location)) {
			service = new CharacterizationServiceLocalImpl();
		} else {
			// TODO: Implement remote service.
		}
		service.exportSummary(charSummaryBean, request, response
				.getOutputStream());

		return null;
	}

	private String getExportFileName(String sampleName, String viewType,
			String charClass) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(sampleName);
		nameParts.add(charClass);
		nameParts.add(viewType);
		nameParts.add(DateUtils.convertDateToString(Calendar.getInstance()
				.getTime()));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}

	public ActionForward saveExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
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
		// also save characterization
		saveCharacterization(request, theForm, achar);
		return mapping.getInputForward();
	}

	public ActionForward getFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String theFindingId = request.getParameter("findingId");
		CharacterizationResultService service = new CharacterizationResultServiceLocalImpl();
		Finding finding = service.findFindingById(theFindingId);
		FindingBean findingBean = new FindingBean(finding);
		// retrieve file visibility
		FileService fileService = new FileServiceLocalImpl();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		for (FileBean fileBean : findingBean.getFiles()) {
			fileService.retrieveVisibility(fileBean, user);
		}
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.setTheFinding(findingBean);
		request.setAttribute("anchor", "submitFinding");
		return mapping.getInputForward();
	}

	public ActionForward resetFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FindingBean findingBean = new FindingBean();
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.setTheFinding(findingBean);
		request.setAttribute("anchor", "submitFinding");
		return mapping.getInputForward();
	}

	public ActionForward saveFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean findingBean = achar.getTheFinding();
		String theFindingId = (String) theForm.get("theFindingId");
		if (theFindingId != null && !theFindingId.equals("null")
				&& theFindingId.trim().length() > 0) {
			findingBean.getDomain().setId(new Long(theFindingId));
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		findingBean.setupDomain(user.getLoginName());
		CharacterizationResultService service = new CharacterizationResultServiceLocalImpl();
		service.saveFinding(findingBean.getDomain());
		saveFilesToFileSystem(findingBean.getFiles());
		achar.addFinding(findingBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		// also save characterization
		saveCharacterization(request, theForm, achar);
		request.setAttribute("anchor", "result");
		return mapping.getInputForward();
	}

	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean findingBean = achar.getTheFinding();
		FileBean theFile = findingBean.getTheFile();
		int theFileIndex = findingBean.getTheFileIndex();
		// create a new copy before adding to finding
		FileBean newFile = theFile.copy();
		SampleBean sampleBean = setupSample(theForm, request, Constants.LOCAL);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ "/"
				+ sampleBean.getDomain().getName()
				+ "/"
				+ StringUtils.getOneWordLowerCaseFirstLetter(achar
						.getCharacterizationName());
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		newFile.setupDomainFile(internalUriPath, user.getLoginName(), 0);
		findingBean.addFile(newFile, theFileIndex);
		request.setAttribute("anchor", "submitFinding");
		return mapping.getInputForward();
	}

	public ActionForward removeFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean findingBean = achar.getTheFinding();
		int theFileIndex = findingBean.getTheFileIndex();
		findingBean.removeFile(theFileIndex);
		findingBean.setTheFile(new FileBean());
		request.setAttribute("anchor", "submitFinding");
		return mapping.getInputForward();
	}

	public ActionForward drawMatrix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		request.setAttribute("anchor", "result");
		FindingBean findingBean = achar.getTheFinding();
		if (request.getParameter("removeColumn") != null) {
			int columnToRemove = Integer.parseInt(request
					.getParameter("removeColumn"));
			findingBean.removeColumn(columnToRemove);
			return mapping.getInputForward();
		} else if (request.getParameter("removeRow") != null) {
			int rowToRemove = Integer.parseInt(request
					.getParameter("removeRow"));
			findingBean.removeRow(rowToRemove);
			return mapping.getInputForward();
		}
		int existingNumberOfColumns = findingBean.getColumnHeaders().size();
		int existingNumberOfRows = findingBean.getRows().size();
		if (existingNumberOfColumns > findingBean.getNumberOfColumns()) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.addCharacterization.removeMatrixColumn");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			findingBean.setNumberOfColumns(existingNumberOfColumns);
			return mapping.getInputForward();
		}
		if (existingNumberOfRows > findingBean.getNumberOfRows()) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.addCharacterization.removeMatrixRow");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			findingBean.setNumberOfRows(existingNumberOfRows);
			return mapping.getInputForward();
		}
		findingBean.updateMatrix(findingBean.getNumberOfColumns(), findingBean
				.getNumberOfRows());
		request.setAttribute("anchor", "submitFinding");
		return mapping.getInputForward();
	}

	public ActionForward deleteFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean dataSetBean = achar.getTheFinding();
		CharacterizationResultService service = new CharacterizationResultServiceLocalImpl();
		service.deleteFinding(dataSetBean.getDomain());
		achar.removeFinding(dataSetBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		request.setAttribute("anchor", "result");
		return mapping.getInputForward();
	}

	public ActionForward deleteExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();
		service.deleteExperimentConfig(configBean.getDomain());
		achar.removeExperimentConfig(configBean);
		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);
		InitExperimentConfigSetup.getInstance()
				.persistExperimentConfigDropdowns(request, configBean);
		// also save characterization
		saveCharacterization(request, theForm, achar);
		return mapping.getInputForward();
	}
}
