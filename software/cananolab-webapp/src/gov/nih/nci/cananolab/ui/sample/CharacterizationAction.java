package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationExporter;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.SampleConstants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
 * Base action for characterization actions
 *
 * @author pansu
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
		this.setServicesInSession(request);
		// Copy "isSoluble" property from char bean to mapping bean.
		this.copyIsSoluble(charBean);

		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, charBean);
		this.saveCharacterization(request, theForm, charBean);

		ActionMessages msgs = new ActionMessages();
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
		request.getSession().setAttribute("tab", String.valueOf(ind));

		return summaryEdit(mapping, form, request, response);
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		this.checkOpenForms(charBean, theForm, request);

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = charBean.getTheFinding().getTheFile();
		String charName = StringUtils.getOneWordLowerCaseFirstLetter(charBean
				.getCharacterizationName());
		preserveUploadedFile(request, theFile, charName);

		return mapping.findForward("inputForm");
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
		this.setServicesInSession(request);
		setupInputForm(request, theForm);
		// reset characterizationBean
		CharacterizationBean charBean = new CharacterizationBean();
		theForm.set("achar", charBean);
		String charType = request.getParameter("charType");
		if (!StringUtils.isEmpty(charType)) {
			charBean.setCharacterizationType(charType);
			SortedSet<String> charNames = InitCharacterizationSetup
					.getInstance().getCharNamesByCharType(request,
							charBean.getCharacterizationType());
			request.getSession().setAttribute("charTypeChars", charNames);
		}
		this.checkOpenForms(charBean, theForm, request);
		// clear copy to otherSamples
		clearCopy(theForm);
		return mapping.findForward("inputForm");
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
		String sampleId = theForm.getString("sampleId");
		String charType = request.getParameter("charType");
		if (charType == null) {
			charType = (String) request.getAttribute("charType");
		}
		if (!StringUtils.isEmpty(charType))
			InitProtocolSetup.getInstance().getProtocolsByChar(request,
					charType);
		InitCharacterizationSetup.getInstance().setPOCDropdown(request,
				sampleId);
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
		CharacterizationService charService = this
				.setServicesInSession(request);
		String charId = (String) getValueFromRequest(request, "charId");
		CharacterizationBean charBean = charService
				.findCharacterizationById(charId);
		// setup Characterization Name drop down.
		InitCharacterizationSetup.getInstance().getCharNamesByCharType(request,
				charBean.getCharacterizationType());

		// setup Assay Type drop down.
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"charNameAssays", charBean.getCharacterizationName(),
				"assayType", "otherAssayType", true);

		// TODO: Find out usage of "charNameDatumNames", not used in any JSPs.
		InitCharacterizationSetup.getInstance().getDatumNamesByCharName(
				request, charBean.getCharacterizationType(),
				charBean.getCharacterizationName(), charBean.getAssayType());

		request.setAttribute("achar", charBean);
		theForm.set("achar", charBean);
		this.setupInputForm(request, theForm);
		this.setupIsSoluble(charBean); // setup "isSoluble" property.

		this.checkOpenForms(charBean, theForm, request);
		// clear copy to otherSamples
		clearCopy(theForm);
		return mapping.findForward("inputForm");
	}

	private void clearCopy(DynaValidatorForm theForm) {
		theForm.set("otherSamples", new String[0]);
		theForm.set("copyData", false);
	}

	/**
	 * Setup, prepare and save characterization.
	 *
	 * @param request
	 * @param theForm
	 * @param charBean
	 * @throws Exception
	 */
	private void saveCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean)
			throws Exception {

		SampleBean sampleBean = setupSample(theForm, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		charBean.setupDomain(user.getLoginName());
		Boolean newChar = true;
		if (charBean.getDomainChar().getId() != null) {
			newChar = false;
		}
		// reuse the existing char service
		CharacterizationService charService = (CharacterizationService) request
				.getSession().getAttribute("characterizationService");
		charService.saveCharacterization(sampleBean, charBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newChar && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			ActionMessages messages = new ActionMessages();
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateSample.retractFromPublic");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		}
		// save to other samples (only when user click [Submit] button.)
		String dispatch = (String) theForm.get("dispatch");
		if ("create".equals(dispatch)) {
			SampleBean[] otherSampleBeans = prepareCopy(request, theForm,
					sampleBean);
			if (otherSampleBeans != null) {
				Boolean copyData = (Boolean) theForm.get("copyData");
				charService.copyAndSaveCharacterization(charBean, sampleBean,
						otherSampleBeans, copyData);
			}
		}
		sampleBean = setupSample(theForm, request);
		request.setAttribute("sampleId", sampleBean.getDomain().getId()
				.toString());
	}

	private void deleteCharacterization(HttpServletRequest request,
			DynaValidatorForm theForm, CharacterizationBean charBean,
			String createdBy) throws Exception {
		charBean.setupDomain(createdBy);
		CharacterizationService service = this.setServicesInSession(request);
		service.deleteCharacterization(charBean.getDomainChar());
		service.removeAccess(charBean.getDomainChar());
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

	/**
	 * summaryEdit() handles Edit request for Characterization Summary view.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Prepare data.
		this.prepareSummary(mapping, form, request, response);
		// prepare characterization tabs and forward to appropriate tab
		List<String> allCharacterizationTypes = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);
		String tab = (String) getValueFromRequest(request, "tab");
		if (tab == null) {
			tab = "ALL"; // default tab to all;
		}
		if (tab.equals("ALL")) {
			request.getSession().removeAttribute("onloadJavascript");
			request.getSession().removeAttribute("tab");
		} else {
			request.getSession().setAttribute(
					"onloadJavascript",
					"showSummary('" + tab + "', "
							+ allCharacterizationTypes.size() + ")");
		}
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
		return mapping.findForward("summaryView");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationService service = this.setServicesInSession(request);
		String charId = theForm.getString("charId");
		setupSample(theForm, request);
		CharacterizationBean charBean = service
				.findCharacterizationById(charId);
		request.setAttribute("charBean", charBean);
		return mapping.findForward("singleSummaryView");
	}

	/**
	 * Shared function for summaryView(), summaryPrint() and summaryEdit().
	 * Prepare CharacterizationBean based on Sample Id.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	private void prepareSummary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Remove previous result from session.
		request.getSession().removeAttribute("characterizationSummaryView");
		request.getSession().removeAttribute("theSample");

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String sampleId = theForm.getString(SampleConstants.SAMPLE_ID);
		CharacterizationService service = this.setServicesInSession(request);
		SampleBean sampleBean = setupSample(theForm, request);
		List<CharacterizationBean> charBeans = service
				.findCharacterizationsBySampleId(sampleId);
		CharacterizationSummaryViewBean summaryView = new CharacterizationSummaryViewBean(
				charBeans);
		// Save result bean in session for later use - export/print.
		request.getSession().setAttribute("characterizationSummaryView",
				summaryView);

		// Save sample bean in session for sample name in export/print.
		request.getSession().setAttribute("theSample", sampleBean);
	}

	/**
	 * Shared function for summaryView() and summaryPrint(). Keep submitted
	 * characterization types in the correct display order. Should be called
	 * after calling prepareSummary(), to avoid session timeout issue.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	private void prepareCharacterizationTypes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CharacterizationSummaryViewBean summaryView = (CharacterizationSummaryViewBean) request
				.getSession().getAttribute("characterizationSummaryView");
		// Keep submitted characterization types in the correct display order
		// prepare characterization tabs and forward to appropriate tab
		List<String> allCharacterizationTypes = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);
		Set<String> foundTypes = summaryView.getCharacterizationTypes();
		List<String> characterizationTypes = new ArrayList<String>();
		for (String charType : allCharacterizationTypes) {
			if (foundTypes.contains(charType)
					&& !characterizationTypes.contains(charType)) {
				characterizationTypes.add(charType);
			}
		}
		// other types that are not in allCharacterizationTypes, e.g. other
		// types from grid search
		for (String type : foundTypes) {
			if (!characterizationTypes.contains(type)) {
				characterizationTypes.add(type);
			}
		}
		request.setAttribute("characterizationTypes", characterizationTypes);
		String tab = (String) getValueFromRequest(request, "tab");
		if (tab == null) {
			tab = "ALL"; // default tab to all;
		}
		if (tab.equals("ALL")) {
			request.getSession().removeAttribute("onloadJavascript");
			request.getSession().removeAttribute("tab");
		} else {
			request.getSession().setAttribute(
					"onloadJavascript",
					"showSummary('" + tab + "', "
							+ characterizationTypes.size() + ")");
		}
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
	 */
	public ActionForward summaryPrint(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CharacterizationSummaryViewBean charSummaryBean = (CharacterizationSummaryViewBean) request
				.getSession().getAttribute("characterizationSummaryView");
		if (charSummaryBean == null) {
			// Retrieve data again when session timeout.
			this.prepareSummary(mapping, form, request, response);
			charSummaryBean = (CharacterizationSummaryViewBean) request
					.getSession().getAttribute("characterizationSummaryView");
		}
		this.prepareCharacterizationTypes(mapping, form, request, response);
		this.filterType(request); // Filter out un-selected types.

		// Marker that indicates page is for printing only, hide tabs/links.
		request.setAttribute("printView", Boolean.TRUE);

		return mapping.findForward("summaryPrintView");
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
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		CharacterizationSummaryViewBean charSummaryBean = (CharacterizationSummaryViewBean) request
				.getSession().getAttribute("characterizationSummaryView");
		if (sampleBean == null || charSummaryBean == null) {
			// Prepare data.
			this.prepareSummary(mapping, form, request, response);
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
			charSummaryBean = (CharacterizationSummaryViewBean) request
					.getSession().getAttribute("characterizationSummaryView");
		}
		this.prepareCharacterizationTypes(mapping, form, request, response);
		List<String> charTypes = this.filterType(request);

		String type = request.getParameter("type");
		String sampleName = sampleBean.getDomain().getName();
		String fileName = ExportUtils.getExportFileName(sampleName,
				"CharacterizationSummaryView", type);
		ExportUtils.prepareReponseForExcel(response, fileName);

		StringBuilder sb = getDownloadUrl(request);
		CharacterizationExporter.exportSummary(charTypes, charSummaryBean, sb
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
		CharacterizationService service = this.setServicesInSession(request);
		service.saveExperimentConfig(configBean);
		achar.addExperimentConfig(configBean);
		// also save characterization
		this.saveCharacterization(request, theForm, achar);
		service.assignAccesses(achar.getDomainChar(), configBean.getDomain());
		this.checkOpenForms(achar, theForm, request);
		// return to setupUpdate to retrieve the data matrix in the correct
		// form from database
		// after saving to database.
		request
				.setAttribute("charId", achar.getDomainChar().getId()
						.toString());
		request.setAttribute("charType", achar.getCharacterizationType());
		return setupUpdate(mapping, form, request, response);
	}

	public ActionForward getFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String theFindingId = request.getParameter("findingId");
		CharacterizationService service = this.setServicesInSession(request);
		FindingBean findingBean = service.findFindingById(theFindingId);
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.setTheFinding(findingBean);
		request.setAttribute("anchor", "submitFinding");
		this.checkOpenForms(achar, theForm, request);

		// Feature request [26487] Deeper Edit Links.
		if (findingBean.getFiles().size() == 1) {
			request.setAttribute("onloadJavascript", "setTheFile(0)");
		}
		request.setAttribute("disableOuterButtons", true);
		return mapping.findForward("inputForm");
	}

	public ActionForward resetFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		FindingBean theFinding = new FindingBean();
		// theFinding.setNumberOfColumns(1);
		// theFinding.setNumberOfRows(1);
		// theFinding.updateMatrix(theFinding.getNumberOfColumns(), theFinding
		// .getNumberOfRows());
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		achar.setTheFinding(theFinding);
		request.setAttribute("anchor", "submitFinding");
		this.checkOpenForms(achar, theForm, request);
		request.setAttribute("disableOuterButtons", true);
		return mapping.findForward("inputForm");
	}

	public ActionForward saveFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationService service = this.setServicesInSession(request);
		SampleBean sampleBean = setupSample(theForm, request);
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean findingBean = achar.getTheFinding();
		if (!validateEmptyFinding(findingBean)) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"achar.theFinding.emptyFinding");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		String theFindingId = (String) request.getAttribute("theFindingId");
		if (!StringUtils.isEmpty(theFindingId)) {
			findingBean.getDomain().setId(Long.valueOf(theFindingId));
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ '/'
				+ sampleBean.getDomain().getName()
				+ '/'
				+ StringUtils.getOneWordLowerCaseFirstLetter(achar
						.getCharacterizationName());

		findingBean.setupDomain(internalUriPath, user.getLoginName());
		service.saveFinding(findingBean);
		achar.addFinding(findingBean);

		// also save characterization
		this.saveCharacterization(request, theForm, achar);
		service.assignAccesses(achar.getDomainChar(), findingBean.getDomain());
		this.checkOpenForms(achar, theForm, request);
		request.setAttribute("anchor", "result");
		// return to setupUpdate to retrieve the data matrix in the correct
		// form from database
		// after saving to database.
		request
				.setAttribute("charId", achar.getDomainChar().getId()
						.toString());
		request.setAttribute("charType", achar.getCharacterizationType());
		return setupUpdate(mapping, form, request, response);
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

		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);
		this.setServicesInSession(request);

		// create a new copy before adding to finding
		FileBean newFile = theFile.copy();
		SampleBean sampleBean = setupSample(theForm, request);
		// setup domainFile uri for fileBeans
		String internalUriPath = Constants.FOLDER_PARTICLE
				+ '/'
				+ sampleBean.getDomain().getName()
				+ '/'
				+ StringUtils.getOneWordLowerCaseFirstLetter(achar
						.getCharacterizationName());
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		newFile.setupDomainFile(internalUriPath, user.getLoginName());
		findingBean.addFile(newFile, theFileIndex);
		request.setAttribute("anchor", "submitFinding");
		this.checkOpenForms(achar, theForm, request);
		return mapping.findForward("inputForm");
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
		this.checkOpenForms(achar, theForm, request);
		return mapping.findForward("inputForm");
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
			this.checkOpenForms(achar, theForm, request);
			return mapping.findForward("inputForm");
		} else if (request.getParameter("removeRow") != null) {
			int rowToRemove = Integer.parseInt(request
					.getParameter("removeRow"));
			findingBean.removeRow(rowToRemove);
			this.checkOpenForms(achar, theForm, request);
			return mapping.findForward("inputForm");
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
			this.checkOpenForms(achar, theForm, request);
			return mapping.getInputForward();
		}
		if (existingNumberOfRows > findingBean.getNumberOfRows()) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.addCharacterization.removeMatrixRow");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			findingBean.setNumberOfRows(existingNumberOfRows);
			this.checkOpenForms(achar, theForm, request);
			return mapping.getInputForward();
		}
		findingBean.updateMatrix(findingBean.getNumberOfColumns(), findingBean
				.getNumberOfRows());
		request.setAttribute("anchor", "submitFinding");
		this.checkOpenForms(achar, theForm, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward deleteFinding(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		FindingBean dataSetBean = achar.getTheFinding();
		CharacterizationService service = this.setServicesInSession(request);
		service.deleteFinding(dataSetBean.getDomain());
		service.removeAccesses(achar.getDomainChar(), dataSetBean.getDomain());
		achar.removeFinding(dataSetBean);
		request.setAttribute("anchor", "result");
		this.checkOpenForms(achar, theForm, request);
		return mapping.findForward("inputForm");
	}

	public ActionForward deleteExperimentConfig(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		CharacterizationService service = this.setServicesInSession(request);
		service.deleteExperimentConfig(configBean.getDomain());
		// TODO remove accessibility

		achar.removeExperimentConfig(configBean);
		// also save characterization
		this.saveCharacterization(request, theForm, achar);
		service.removeAccesses(achar.getDomainChar(), configBean.getDomain());
		this.checkOpenForms(achar, theForm, request);
		return mapping.findForward("inputForm");
	}

	// FR# [26194], matrix column order.
	public ActionForward updateColumnOrder(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean achar = (CharacterizationBean) theForm
				.get("achar");

		FindingBean findingBean = achar.getTheFinding();
		findingBean.updateColumnOrder();

		request.setAttribute("anchor", "submitFinding");
		this.checkOpenForms(achar, theForm, request);

		return mapping.findForward("inputForm");
	}

	private void checkOpenForms(CharacterizationBean achar,
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		HttpSession session = request.getSession();
		Boolean openFile = false, openExperimentConfig = false, openFinding = false;
		if (dispatch.equals("input") && browserDispatch.equals("addFile")) {
			openFile = true;
		}
		session.setAttribute("openFile", openFile);
		if (dispatch.equals("input")
				&& browserDispatch.equals("saveExperimentConfig")) {
			openExperimentConfig = true;
		}
		session.setAttribute("openExperimentConfig", openExperimentConfig);
		if (dispatch.equals("input")
				&& (browserDispatch.equals("saveFinding") || browserDispatch
						.equals("addFile")) || dispatch.equals("addFile")
				|| dispatch.equals("removeFile")
				|| dispatch.equals("drawMatrix")
				|| dispatch.equals("getFinding")
				|| dispatch.equals("resetFinding")
				|| dispatch.equals("updateColumnOrder")) {
			openFinding = true;
		}
		session.setAttribute("openFinding", openFinding);

		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, achar);

		/**
		 * If user entered customized Char Type/Name, Assay Type by selecting
		 * [other], we should show and highlight the value on the edit page.
		 */
		String currentCharType = achar.getCharacterizationType();
		setOtherValueOption(request, currentCharType, "characterizationTypes");

		String currentCharName = achar.getCharacterizationName();
		setOtherValueOption(request, currentCharName, "charTypeChars");

		String currentAssayType = achar.getAssayType();
		setOtherValueOption(request, currentAssayType, "charNameAssays");

		// setup detail page
		String detailPage = null;
		if (!StringUtils.isEmpty(achar.getCharacterizationType())
				&& !StringUtils.isEmpty(achar.getCharacterizationName())) {
			detailPage = InitCharacterizationSetup.getInstance().getDetailPage(
					achar.getCharacterizationType(),
					achar.getCharacterizationName());
		}
		request.setAttribute("characterizationDetailPage", detailPage);
	}

	/**
	 * Shared function for summaryExport() and summaryPrint(). Filter out
	 * unselected types when user selected one type for print/export.
	 *
	 * @param request
	 * @param compBean
	 */
	private List<String> filterType(HttpServletRequest request) {
		List<String> charTypes = (List<String>) request
				.getAttribute("characterizationTypes");
		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			charTypes.clear();
			charTypes.add(type);
		}
		return charTypes;
	}

	private boolean validateEmptyFinding(FindingBean finding) {
		if (finding.getFiles().isEmpty()
				&& finding.getColumnHeaders().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Copy "isSoluble" property from achar to Solubility entity.
	 *
	 * @param achar
	 */
	private void copyIsSoluble(CharacterizationBean achar) {
		Boolean soluble = null;
		String isSoluble = achar.getIsSoluble();
		if (!StringUtils.isEmpty(isSoluble)) {
			soluble = Boolean.valueOf(isSoluble);
		}
		if ("Solubility".equals(achar.getClassName())) {
			achar.getSolubility().setIsSoluble(soluble);
		}
	}

	/**
	 * Setup "isSoluble" property in achar from Solubility entity.
	 *
	 * @param achar
	 */
	private void setupIsSoluble(CharacterizationBean achar) {
		Boolean soluble = null;
		if ("Solubility".equals(achar.getClassName())) {
			soluble = achar.getSolubility().getIsSoluble();
		}
		if (soluble == null) {
			achar.setIsSoluble(null);
		} else {
			achar.setIsSoluble(soluble.toString());
		}
	}

	public Boolean canUserExecutePrivateDispatch(UserBean user)
			throws SecurityException {
		if (user == null) {
			return false;
		}
		return true;
	}

	private CharacterizationService setServicesInSession(
			HttpServletRequest request) throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		CharacterizationService charService = new CharacterizationServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("characterizationService",
				charService);
		ProtocolService protocolService = new ProtocolServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("protocolService", protocolService);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return charService;
	}

	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CharacterizationService service = (CharacterizationService) (request
				.getSession().getAttribute("characterizationService"));
		return downloadFile(service, mapping, form, request, response);
	}

}
