package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 */
public class CharacterizationAction extends BaseAnnotationAction {

	// Partial URL for downloading characterization report file.
	public static final String DOWNLOAD_URL = "?dispatch=download&location=";

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
		// to preselect the same characterization type after returning to the
		// summary page
		List<String> allCharacterizationTypes = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);
		int ind = allCharacterizationTypes.indexOf(charBean
				.getCharacterizationType()) + 1;
		request.getSession().setAttribute(
				"onloadJavascript",
				"showSummary('" + ind + "', " + allCharacterizationTypes.size()
						+ ")");
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
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean charBean = charService.findCharacterizationById(
				charId, user);

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

		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		setupDomainChar(request, theForm, charBean);
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		charService.saveCharacterization(sampleBean, charBean, user);

		// save to other samples
		SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
				sampleBean);
		if (otherSampleBeans != null) {
			Boolean copyData = (Boolean) theForm.get("copyData");
			charService.copyAndSaveCharacterization(charBean, sampleBean,
					otherSampleBeans, copyData, user);
		}
		sampleBean = setupSample(theForm, request, Constants.LOCAL_SITE);
		request.setAttribute("sampleId", sampleBean.getDomain().getId());
		request.setAttribute(Constants.LOCATION, Constants.LOCAL_SITE);
	}

	private void deleteCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean,
			String createdBy) throws Exception {
		charBean.setupDomain(createdBy);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		charService.deleteCharacterization(charBean.getDomainChar(), user);
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
	private void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String sampleId = theForm.getString(SampleConstants.SAMPLE_ID);
		String location = theForm.getString(Constants.LOCATION);
		setupSample(theForm, request, location);
		CharacterizationService service = null;
		if (Constants.LOCAL_SITE.equals(location)) {
			service = new CharacterizationServiceLocalImpl();
		} else {
			String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
					request, location);
			service = new CharacterizationServiceRemoteImpl(serviceUrl);
		}
		List<CharacterizationBean> charBeans = service
				.findCharacterizationsBySampleId(sampleId, user);

		// set characterization types and retrieve visibility
		for (CharacterizationBean charBean : charBeans) {
			InitCharacterizationSetup.getInstance().setCharacterizationType(
					request, charBean);
			InitCharacterizationSetup.getInstance().setCharacterizationName(
					request, charBean);
		}
		CharacterizationSummaryViewBean summaryView = new CharacterizationSummaryViewBean(
				charBeans);
		request.setAttribute("characterizationSummaryView", summaryView);
		InitCharacterizationSetup.getInstance().setCharactierizationDropDowns(
				request, sampleId);
		if (request.getParameter("clearTab") != null
				&& request.getParameter("clearTab").equals("true")) {
			request.getSession().removeAttribute("onloadJavascript");
		}
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
	private void prepareCharacterizationTypes(ActionMapping mapping,
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
	 * Export Characterization Summary report.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = theForm.getString(Constants.LOCATION);
		SampleBean sampleBean = setupSample(theForm, request, location);
		CharacterizationSummaryViewBean charSummaryBean = (CharacterizationSummaryViewBean) request
				.getAttribute("characterizationSummaryView");
		Map<String, SortedSet<CharacterizationBean>> charBeanMap = charSummaryBean
				.getType2Characterizations();
		SortedSet<CharacterizationBean> charBeans = null;

		// Filter out un-selected types.
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			charBeans = charBeanMap.get(type);
			if (charBeans != null) {
				charBeanMap.clear();
				charBeanMap.put(type, charBeans);
			}
		}

		String fileName = ExportUtils.getExportFileName(sampleBean.getDomain()
				.getName(), "CharacterizationSummaryView", type);
		ExportUtils.prepareReponseForExcell(response, fileName);

		StringBuilder sb = new StringBuilder();
		sb.append(request.getRequestURL().toString());
		sb.append(DOWNLOAD_URL);
		sb.append(request.getParameter(location));

		CharacterizationServiceHelper.exportSummary(charSummaryBean, sb
				.toString(), response.getOutputStream());

		return null;
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
		CharacterizationService service = new CharacterizationServiceLocalImpl();
		service.saveExperimentConfig(configBean, user);
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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String theFindingId = request.getParameter("findingId");
		CharacterizationService service = new CharacterizationServiceLocalImpl();
		FindingBean findingBean = service.findFindingById(theFindingId, user);
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
		CharacterizationService service = new CharacterizationServiceLocalImpl();
		service.saveFinding(findingBean, user);
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
		SampleBean sampleBean = setupSample(theForm, request,
				Constants.LOCAL_SITE);
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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean dataSetBean = achar.getTheFinding();
		CharacterizationService service = new CharacterizationServiceLocalImpl();
		service.deleteFinding(dataSetBean.getDomain(), user);
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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		CharacterizationService service = new CharacterizationServiceLocalImpl();
		service.deleteExperimentConfig(configBean.getDomain(), user);
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
