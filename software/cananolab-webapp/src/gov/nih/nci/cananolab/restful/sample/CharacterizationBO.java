/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.dto.common.ColumnHeader;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.restful.CharacterizationServices;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.protocol.InitProtocolSetup;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleCharacterizationEditBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleCharacterizationSummaryEditBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleExperimentBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFindingBean;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.CharacterizationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationExporter;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.SampleForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Base action for characterization actions
 * 
 * @author pansu
 */
public class CharacterizationBO extends BaseAnnotationBO {
	
	private Logger logger = Logger.getLogger(CharacterizationBO.class);

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
	public CharacterizationSummaryViewBean create(HttpServletRequest request, SimpleCharacterizationEditBean simpleEdit)
			throws Exception {
		
		CharacterizationBean charBean = (CharacterizationBean)request.getSession().getAttribute("theChar");
		
		simpleEdit.getErrors().clear();
		simpleEdit.getMessages().clear();
		
		//TODO: 
		simpleEdit.transferToCharacterizationBean(charBean);
		
		this.setServicesInSession(request);
		
		// Copy "isSoluble" property from char bean to mapping bean.
		this.copyIsSoluble(charBean);

		InitCharacterizationSetup.getInstance()
				.persistCharacterizationDropdowns(request, charBean);
		
		List<String> errs = new ArrayList<String>();
		if (!validateInputs(request, charBean, errs)) {
			CharacterizationSummaryViewBean summaryView = new CharacterizationSummaryViewBean(new ArrayList<CharacterizationBean>());
			summaryView.setErrors(errs);
			return summaryView;
		}
		
		this.saveCharacterization(request, charBean, simpleEdit);
		simpleEdit.getMessages().add(PropertyUtil.getPropertyReplacingToken("sample", "message.addCharacterization", "0", 
				charBean.getCharacterizationName()));
		// to preselect the same characterization type after returning to the
		// summary page
		List<String> allCharacterizationTypes = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);
		int ind = allCharacterizationTypes.indexOf(charBean
				.getCharacterizationType()) + 1;
		request.getSession().setAttribute("tab", String.valueOf(ind));

		//return summaryEdit(String.valueOf(simpleEdit.getParentSampleId()), request);
		
		return null;

	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		CharacterizationBean charBean = (CharacterizationBean) theForm
				.get("achar");
		charBean.updateEmptyFieldsToNull();
		this.checkOpenForms(charBean, theForm, request);

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = charBean.getTheFinding().getTheFile();
		escapeXmlForFileUri(theFile);
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
	public SimpleCharacterizationEditBean setupNew(HttpServletRequest request, String sampleId, String charType)
			throws Exception {
		if (StringUtils.isEmpty(sampleId))
			throw new Exception("Sample id is empty");
		
		if (StringUtils.isEmpty(charType))
			throw new Exception("Characterization type is empty");
		
		setServicesInSession(request);
		
		//This method sets tons of lookups. Need to see what's needed and what's not
		setupInputForm(request, sampleId, charType);
		
		CharacterizationBean charBean = new CharacterizationBean();		
		charBean.setCharacterizationType(charType);
		
		//this.checkOpenForms(charBean, theForm, request);
		// clear copy to otherSamples
//		clearCopy(theForm);
//		return mapping.findForward("inputForm");
		SimpleCharacterizationEditBean editBean = new SimpleCharacterizationEditBean();
		editBean.transferFromCharacterizationBean(request, charBean, sampleId);
		
		request.getSession().setAttribute("theEditChar", editBean);
		return editBean;
	}

	/**
	 * Set up drop-downs need for the input form
	 * 
	 * @param request
	 * @param theForm
	 * @throws Exception
	 */
	private void setupInputForm(HttpServletRequest request, String sampleId, String charType) throws Exception {

		if (StringUtils.isEmpty(sampleId))
			throw new Exception("Sample id is invalid");
		
		if (!StringUtils.isEmpty(charType))
			InitProtocolSetup.getInstance().getProtocolsByChar(request,
					charType);
		InitCharacterizationSetup.getInstance().setPOCDropdown(request,
				sampleId);
		InitCharacterizationSetup.getInstance().setCharacterizationDropdowns(
				request);

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
	public SimpleCharacterizationEditBean setupUpdate(HttpServletRequest request, String sampleId, String charId, 
			String charClassName, String charType)
			throws Exception {
		CharacterizationService charService = this.setServicesInSession(request);
		charId = super.validateCharId(charId);
		
		CharacterizationBean charBean = charService
				.findCharacterizationById(charId);
		
		
//		// setup Characterization Name drop down.
//		InitCharacterizationSetup.getInstance().getCharNamesByCharType(request,
//				charBean.getCharacterizationType());
//
//		// setup Assay Type drop down.
//		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
//				"charNameAssays", charBean.getCharacterizationName(),
//				"assayType", "otherAssayType", true);
//
//		// TODO: Find out usage of "charNameDatumNames", not used in any JSPs.
//		InitCharacterizationSetup.getInstance().getDatumNamesByCharName(
//				request, charBean.getCharacterizationType(),
//				charBean.getCharacterizationName(), charBean.getAssayType());
//
//		request.setAttribute("achar", charBean);
//		//theForm.set("achar", charBean);
		
		//This method sets tons of lookups. Need to see what's needed and what's not
		this.setupInputForm(request, sampleId, charType);	
		
		//What is this?
		this.setupIsSoluble(charBean); // setup "isSoluble" property.
		
		//SY: new
		request.getSession().setAttribute("theChar", charBean);
		logger.debug("Setting theChar in session: " + request.getSession().getId());;
		
		SimpleCharacterizationEditBean editBean = new SimpleCharacterizationEditBean();
		editBean.transferFromCharacterizationBean(request, charBean, sampleId);
		request.getSession().setAttribute("theEditChar", editBean);
		return editBean;
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
			CharacterizationBean charBean, SimpleCharacterizationEditBean simpleEdit)
			throws Exception {

		SampleBean sampleBean = setupSampleById(String.valueOf(simpleEdit.getParentSampleId()), request);
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		charBean.setupDomain(user.getLoginName());
		Boolean newChar = true;
		if (charBean.getDomainChar().getId() != null) {
			newChar = false;
		}
		logger.debug("Saving new char? " + newChar);
		
		// reuse the existing char service
		CharacterizationService charService = (CharacterizationService) request
				.getSession().getAttribute("characterizationService");
		charService.saveCharacterization(sampleBean, charBean);
		// retract from public if updating an existing public record and not
		// curator
		if (!newChar && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(String.valueOf(simpleEdit.getParentSampleId()), request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			
			simpleEdit.getErrors().add(PropertyUtil.getProperty("sample", "message.updateSample.retractFromPublic"));
		}
		
		// save to other samples (only when user click [Submit] button.)
		
		
		//String dispatch = (String) theForm.get("dispatch");
		
		//TODO: need a way to know if we're clicking submit
		boolean clickSumit = false;
		if (clickSumit) {
			SampleBean[] otherSampleBeans = prepareCopy(request, simpleEdit,
					sampleBean);
			if (otherSampleBeans != null) {
				charService.copyAndSaveCharacterization(charBean, sampleBean,
						otherSampleBeans, simpleEdit.isCopyToOtherSamples());
			}
		}
		
		sampleBean = setupSampleById(String.valueOf(simpleEdit.getParentSampleId()), request);
		request.setAttribute("sampleId", sampleBean.getDomain().getId()
				.toString());
		logger.debug("Done saving characterization: " + charBean.getDomainChar().getId());
	}

	private void deleteCharacterization(HttpServletRequest request,
			CharacterizationBean charBean,
			String createdBy) throws Exception {
		charBean.setupDomain(createdBy);
		CharacterizationService service = this.setServicesInSession(request);
		service.deleteCharacterization(charBean.getDomainChar());
		service.removeAccesses(charBean.getDomainChar());
	}

	public CharacterizationSummaryViewBean delete(HttpServletRequest request, 
			SimpleCharacterizationEditBean editBean)
			throws Exception {
		
		CharacterizationBean charBean = (CharacterizationBean)request.getSession().getAttribute("theChar");
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		
		deleteCharacterization(request, charBean, user.getLoginName());
		
		//TODO
//		ActionMessages msgs = new ActionMessages();
//		ActionMessage msg = new ActionMessage("message.deleteCharacterization");
//		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//		saveMessages(request, msgs);
		//ActionForward forward = mapping.findForward("success");
		return null;
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
	public SimpleCharacterizationSummaryEditBean summaryEdit(String sampleId,
			HttpServletRequest request)
			throws Exception {
		// Prepare data.
		CharacterizationSummaryViewBean sumBean = this.prepareSummary(sampleId, request);
		
		SimpleCharacterizationSummaryEditBean editBean = new SimpleCharacterizationSummaryEditBean();
		List<SimpleCharacterizationsByTypeBean> finalBeans = editBean.transferData(request, sumBean, sampleId);
		//List<Object> finalBeans = editBean.transferData(request, sumBean, sampleId);
		return editBean;
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
	public CharacterizationSummaryViewBean summaryView(String sampleId,
			HttpServletRequest request)
			throws Exception {
		
		if (sampleId == null || sampleId.length() == 0) 
			throw new Exception("sampleId can't be null or empty");
		
		CharacterizationSummaryViewBean viewBean = prepareSummary(sampleId, request);
		List<String> charTypes = prepareCharacterizationTypes(request);
		//setSummaryTab(request, charTypes.size());
		return viewBean;
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//TODO
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		CharacterizationService service = this.setServicesInSession(request);
//		String charId = super.validateId(request, "charId");
//		setupSample(theForm, request);
//		CharacterizationBean charBean = service
//				.findCharacterizationById(charId);
//		request.setAttribute("charBean", charBean);
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
	private CharacterizationSummaryViewBean prepareSummary(String sampleId, HttpServletRequest request)
			throws Exception {
		// Remove previous result from session.
		request.getSession().removeAttribute("characterizationSummaryView");
		request.getSession().removeAttribute("theSample");

		//DynaValidatorForm theForm = (DynaValidatorForm) form;
		//String sampleId = form.getSampleId();  //.getString(SampleConstants.SAMPLE_ID);
		CharacterizationService service = this.setServicesInSession(request);
		
		SampleBean sampleBean = setupSampleById(sampleId, request);
		
		List<CharacterizationBean> charBeans = service
				.findCharacterizationsBySampleId(sampleId);
		CharacterizationSummaryViewBean summaryView = new CharacterizationSummaryViewBean(
				charBeans);
		// Save result bean in session for later use - export/print.
		request.getSession().setAttribute("characterizationSummaryView",
				summaryView);

		// Save sample bean in session for sample name in export/print.
		request.getSession().setAttribute("theSample", sampleBean);
		
		return summaryView;
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
	private List<String> prepareCharacterizationTypes(HttpServletRequest request) throws Exception {
		
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
		return characterizationTypes;
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
	public CharacterizationSummaryViewBean summaryPrint(String sampleId, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CharacterizationSummaryViewBean charSummaryBean = (CharacterizationSummaryViewBean) request
				.getSession().getAttribute("characterizationSummaryView");
		if (charSummaryBean == null) {
			// Retrieve data again when session timeout.
			this.prepareSummary(sampleId, request);
			charSummaryBean = (CharacterizationSummaryViewBean) request
					.getSession().getAttribute("characterizationSummaryView");
		}
		
		//TODO: w
		List<String> charTypes = prepareCharacterizationTypes(request);
		
		this.filterType(request, "all", charTypes); // Filter out un-selected types.

		// Marker that indicates page is for printing only, hide tabs/links.
		request.setAttribute("printView", Boolean.TRUE);

		//return mapping.findForward("summaryPrintView");
		return charSummaryBean;
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
	public String summaryExport(String sampleId, String type,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		if (sampleId == null || sampleId.length() == 0)
			return "Sample Id can't be null or empty";
		
		if (type == null)
			type = "all";
		
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		CharacterizationSummaryViewBean charSummaryBean = (CharacterizationSummaryViewBean) request
				.getSession().getAttribute("characterizationSummaryView");
		if (sampleBean == null || charSummaryBean == null) {
			// Prepare data.
			this.prepareSummary(sampleId, request);
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
			charSummaryBean = (CharacterizationSummaryViewBean) request
					.getSession().getAttribute("characterizationSummaryView");
		}
		List<String> charTypes = prepareCharacterizationTypes(request);
		List<String> filteredCharTypes = this.filterType(request, type, charTypes);

		//String type = "all"; //request.getParameter("type");
		// per app scan
		if (!StringUtils.xssValidate(type)) {
			type = "";
		}
		String sampleName = sampleBean.getDomain().getName();
		String fileName = ExportUtils.getExportFileName(sampleName,
				"CharacterizationSummaryView", type);
		ExportUtils.prepareReponseForExcel(response, fileName);

		StringBuilder sb = getDownloadUrl(request);
		CharacterizationExporter.exportSummary(filteredCharTypes,
				charSummaryBean, sb.toString(), response.getOutputStream());

		return "success";
	}

	public SimpleCharacterizationEditBean saveExperimentConfig(HttpServletRequest request, 
			SimpleExperimentBean simpleExpConfig) 
			throws Exception {
		
		//DynaValidatorForm theForm = (DynaValidatorForm) form;
		
		logger.debug("Start saving experiment confg");
		logger.debug("Getting theChar in session: " + request.getSession().getId());;
		
		//editBean's charId could be null, indicating new char
		CharacterizationBean achar = (CharacterizationBean) request.getSession().getAttribute("theChar");
		SimpleCharacterizationEditBean editBean = 
				(SimpleCharacterizationEditBean) request.getSession().getAttribute("theEditChar");
		
		if (achar == null)
			logger.debug("theChar is null");
		
		if (editBean == null)
			logger.debug("theEditChar is null");
		
				
		//TODO: get the exp config object from "theChar" based on id
		// if id is null, it's a new exp config
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		simpleExpConfig.transferToExperimentConfigBean(configBean);
		
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		configBean.setupDomain(user.getLoginName());
		CharacterizationService service = this.setServicesInSession(request);
		logger.debug("Call saveExperimentConfig");
		service.saveExperimentConfig(configBean);
		logger.debug("Save exp config complete");
		achar.addExperimentConfig(configBean);
		
		// also save characterization
		if (!validateInputs(request, achar, editBean.getErrors())) {
			return editBean;
		}
		
		this.saveCharacterization(request, achar, editBean);
		logger.debug("Save char complete");
		service.assignAccesses(achar.getDomainChar(), configBean.getDomain());
		
		//TODO:
		//this.checkOpenForms(achar, theForm, request);
		
		// return to setupUpdate to retrieve the data matrix in the correct
		// form from database
		// after saving to database.
		request.setAttribute("charId", achar.getDomainChar().getId().toString());
		request.setAttribute("charType", achar.getCharacterizationType());
		
		
		return setupUpdate(request, String.valueOf(editBean.getParentSampleId()), achar.getDomainChar().getId().toString(), 
				achar.getClassName(), achar.getCharacterizationType());
		
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
		// remove columnHeaders stored in the session;
		request.getSession().removeAttribute("columnHeaders");
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
		request.getSession().removeAttribute("columnHeaders");
		return mapping.findForward("inputForm");
	}

	public SimpleCharacterizationEditBean saveFinding(HttpServletRequest request, 
			SimpleFindingBean simpleFinding)
			throws Exception {
		
		CharacterizationBean achar = (CharacterizationBean) request.getSession().getAttribute("theChar");
		SimpleCharacterizationEditBean editBean = 
				(SimpleCharacterizationEditBean) request.getSession().getAttribute("theEditChar");
		
		FindingBean findingBean = this.findMatchFindingBean(achar, simpleFinding);
		
		CharacterizationService service = this.setServicesInSession(request);
		
		SampleBean sampleBean = setupSampleById(String.valueOf(editBean.getParentSampleId()), request);
				
		//FindingBean findingBean = achar.getTheFinding();
		String theFindingId = String.valueOf(editBean.getParentSampleId());
		
		//This is not needed. To delete...
		if (!StringUtils.isEmpty(theFindingId)) {
			findingBean.getDomain().setId(Long.valueOf(theFindingId));
		}
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		simpleFinding.transferToFindingBean(findingBean, user);
		
		if (!validateEmptyFinding(request, findingBean, editBean.getErrors())) {
			return editBean;
		}
		
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
		if (!validateInputs(request, achar, editBean.getErrors())) {
			return editBean;
		}
		this.saveCharacterization(request, achar, editBean);
		service.assignAccesses(achar.getDomainChar(), findingBean.getDomain());
		//this.checkOpenForms(achar, theForm, request);
		request.setAttribute("anchor", "result");
		// return to setupUpdate to retrieve the data matrix in the correct
		// form from database
		// after saving to database.
		request.setAttribute("charId", achar.getDomainChar().getId().toString());
		request.setAttribute("charType", achar.getCharacterizationType());
		
		return setupUpdate(request, String.valueOf(editBean.getParentSampleId()), achar.getDomainChar().getId().toString(), 
				achar.getClassName(), achar.getCharacterizationType());
	}

	public ActionForward addFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		CharacterizationBean achar = (CharacterizationBean) theForm
//				.get("achar");
//		FindingBean findingBean = achar.getTheFinding();
//		FileBean theFile = findingBean.getTheFile();
//		int theFileIndex = findingBean.getTheFileIndex();
//
//		// restore previously uploaded file from session.
//		restoreUploadedFile(request, theFile);
//		this.setServicesInSession(request);
//
//		// create a new copy before adding to finding
//		FileBean newFile = theFile.copy();
//		SampleBean sampleBean = setupSample(theForm, request);
//		// setup domainFile uri for fileBeans
//		String internalUriPath = Constants.FOLDER_PARTICLE
//				+ '/'
//				+ sampleBean.getDomain().getName()
//				+ '/'
//				+ StringUtils.getOneWordLowerCaseFirstLetter(achar
//						.getCharacterizationName());
//		UserBean user = (UserBean) request.getSession().getAttribute("user");
//		newFile.setupDomainFile(internalUriPath, user.getLoginName());
//		findingBean.addFile(newFile, theFileIndex);
//		request.setAttribute("anchor", "submitFinding");
//		this.checkOpenForms(achar, theForm, request);
//		return mapping.findForward("inputForm");
		
		return null;
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

	/**
	 * 
	 * @param request
	 * @param simpleFinding
	 * @return
	 * @throws Exception
	 */
	public SimpleFindingBean drawMatrix(HttpServletRequest request, SimpleFindingBean simpleFinding)
			throws Exception {
		
		CharacterizationBean achar = (CharacterizationBean) request.getSession().getAttribute("theChar");
		SimpleCharacterizationEditBean editBean = 
				(SimpleCharacterizationEditBean) request.getSession().getAttribute("theEditChar");
		request.setAttribute("anchor", "result");
		
		FindingBean findingBean = this.findMatchFindingBean(achar, simpleFinding);
		simpleFinding.transferTableNumbersToFindingBean(findingBean);

//		if (request.getParameter("removeColumn") != null) {
//			int columnToRemove = Integer.parseInt(request
//					.getParameter("removeColumn"));
//			findingBean.removeColumn(columnToRemove);
//			this.checkOpenForms(achar, theForm, request);
//			return mapping.findForward("inputForm");
//		} else if (request.getParameter("removeRow") != null) {
//			int rowToRemove = Integer.parseInt(request
//					.getParameter("removeRow"));
//			findingBean.removeRow(rowToRemove);
//			this.checkOpenForms(achar, theForm, request);
//			return mapping.findForward("inputForm");
//		}
		int existingNumberOfColumns = findingBean.getColumnHeaders().size();
		int existingNumberOfRows = findingBean.getRows().size();
		
		if (existingNumberOfColumns > findingBean.getNumberOfColumns()) {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage(
//					"message.addCharacterization.removeMatrixColumn");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
//			
			
//			findingBean.setNumberOfColumns(existingNumberOfColumns);
//			//this.checkOpenForms(achar, theForm, request);
//			return mapping.getInputForward();
		}
		if (existingNumberOfRows > findingBean.getNumberOfRows()) {
//			ActionMessages msgs = new ActionMessages();
//			ActionMessage msg = new ActionMessage(
//					"message.addCharacterization.removeMatrixRow");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
//			findingBean.setNumberOfRows(existingNumberOfRows);
//			this.checkOpenForms(achar, theForm, request);
//			return mapping.getInputForward();
		}
		
		findingBean.updateMatrix(findingBean.getNumberOfColumns(),
				findingBean.getNumberOfRows());
		
		simpleFinding.transferFromFindingBean(findingBean);
		simpleFinding.setColumnHeaders(findingBean.getColumnHeaders());
		simpleFinding.setDefaultColumnNameForNullHeaders();
		
		request.setAttribute("anchor", "submitFinding");
		
		//this.checkOpenForms(achar, theForm, request);
		// set columnHeaders in the session so jsp can check duplicate columns
		request.getSession().setAttribute("columnHeaders",
				findingBean.getColumnHeaders());
		//return mapping.findForward("inputForm");
		
		return simpleFinding;
	}

	public SimpleCharacterizationEditBean deleteFinding(HttpServletRequest request,
			SimpleFindingBean simpleFinding)
			throws Exception {
		
		logger.debug("achar is not null yet");
		
		CharacterizationBean achar = (CharacterizationBean) request.getSession().getAttribute("theChar");
		SimpleCharacterizationEditBean editBean = 
				(SimpleCharacterizationEditBean) request.getSession().getAttribute("theEditChar");
		logger.debug("editBean is not null yet");
		if (achar == null)
			logger.debug("achar is null");
		if (editBean == null)
			logger.debug("editBean is null");
		
		FindingBean dataSetBean = this.findMatchFindingBean(achar, simpleFinding);
		
		logger.debug("1");
		
		CharacterizationService service = this.setServicesInSession(request);
		
		service.deleteFinding(dataSetBean.getDomain());
		logger.debug("2");
		service.removeAccesses(achar.getDomainChar(), dataSetBean.getDomain());
		logger.debug("3");
		achar.removeFinding(dataSetBean);
		request.setAttribute("anchor", "result");
		logger.debug("4");
		//this.checkOpenForms(achar, theForm, request);
		
		
		return setupUpdate(request, String.valueOf(editBean.getParentSampleId()), achar.getDomainChar().getId().toString(), 
				achar.getClassName(), achar.getCharacterizationType());
	}

	public SimpleCharacterizationEditBean deleteExperimentConfig(HttpServletRequest request,
			SimpleExperimentBean simpleExpConfig) throws Exception {
		
		CharacterizationBean achar = (CharacterizationBean) request.getSession().getAttribute("theChar");
		SimpleCharacterizationEditBean editBean = 
				(SimpleCharacterizationEditBean) request.getSession().getAttribute("theEditChar");
		
		
		
		ExperimentConfigBean configBean = achar.getTheExperimentConfig();
		simpleExpConfig.transferToExperimentConfigBean(configBean);
		
		CharacterizationService service = this.setServicesInSession(request);
		service.deleteExperimentConfig(configBean.getDomain());
		logger.debug("Experiment config deleted");
		achar.removeExperimentConfig(configBean);
		
		if (validateInputs(request, achar, editBean.getMessages())) {
			logger.debug("char validated");
			this.saveCharacterization(request, achar, editBean);
			logger.debug("Char saved");
		}	
		
		service.removeAccesses(achar.getDomainChar(), configBean.getDomain());
		logger.debug("Access removed");
		//this.checkOpenForms(achar, theForm, request);
		return editBean;
	}

	/**
	 * Method to support setColumnOrder rest service
	 * @param request
	 * @param simpleFinding
	 * @return
	 * @throws Exception
	 */
	public SimpleFindingBean updateColumnOrder(HttpServletRequest request,
			SimpleFindingBean simpleFinding) throws Exception {
		
		CharacterizationBean achar = (CharacterizationBean) request.getSession().getAttribute("theChar");
//		SimpleCharacterizationEditBean editBean = 
//				(SimpleCharacterizationEditBean) request.getSession().getAttribute("theEditChar");
		
		FindingBean findingBean = findMatchFindingBean(achar, simpleFinding);
		simpleFinding.transferColumnOrderToFindingBean(findingBean);

		findingBean.updateColumnOrder();
		
		simpleFinding.transferFromFindingBean(findingBean);

		request.setAttribute("anchor", "submitFinding");
		//this.checkOpenForms(achar, theForm, request);

		return simpleFinding;
	}
	
	protected FindingBean findMatchFindingBean(CharacterizationBean achar, 
			SimpleFindingBean simpleFinding) 
	throws Exception {
		
		if (simpleFinding.getFindingId() <= 0) //new finding
			return achar.getTheFinding();
		
		List<FindingBean> findingBeans = achar.getFindings();
		if (findingBeans == null)
			throw new Exception("Current characterization has no finding matching input finding id: " + simpleFinding.getFindingId());
		
		for (FindingBean finding : findingBeans) {
			if (finding.getDomain() != null && finding.getDomain().getId() != null) {
				Long id = finding.getDomain().getId(); 
				if (id.longValue() == simpleFinding.getFindingId())
					return finding;
			}
		}
		
		throw new Exception("Current characterization has no finding matching input finding id: " + simpleFinding.getFindingId());
	}

	private void checkOpenForms(CharacterizationBean achar,
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {
		
		achar.updateEmptyFieldsToNull();
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
		if (achar.isWithProperties()) {
			String detailPage = null;
			if (!StringUtils.isEmpty(achar.getCharacterizationType())
					&& !StringUtils.isEmpty(achar.getCharacterizationName())) {
				detailPage = InitCharacterizationSetup.getInstance()
						.getDetailPage(achar.getCharacterizationType(),
								achar.getCharacterizationName());
			}
			request.setAttribute("characterizationDetailPage", detailPage);
		}
		// if finding contains more than one column, set disableSetColumnOrder
		// false
		if (achar.getTheFinding().getNumberOfColumns() > 1
				&& dataMatrixSaved(achar.getTheFinding())) {
			request.setAttribute("setColumnOrder", true);
		} else {
			request.setAttribute("setColumnOrder", false);
		}
	}

	private Boolean dataMatrixSaved(FindingBean theFinding) {
		if (theFinding.getColumnHeaders() != null) {
			for (ColumnHeader header : theFinding.getColumnHeaders()) {
				if (header.getCreatedDate() == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Shared function for summaryExport() and summaryPrint(). Filter out
	 * unselected types when user selected one type for print/export.
	 * 
	 * @param request
	 * @param compBean
	 */
	private List<String> filterType(HttpServletRequest request, String type,
			List<String> charTypes) {
		//String type = request.getParameter("type");
		List<String> filteredTypes = new ArrayList<String>();
		if( !StringUtils.isEmpty(type) && type.equals("all")) {
			filteredTypes = charTypes;
		} else if (!StringUtils.isEmpty(type) && charTypes.contains(type)) {
			filteredTypes.add(type);
		}
		request.setAttribute("characterizationTypes", filteredTypes);
		return filteredTypes;
	}

	private boolean validateCharacterization(HttpServletRequest request,
			CharacterizationBean achar, List<String> errors) {
		ActionMessages msgs = new ActionMessages();
		boolean status = true;
		if (achar.getCharacterizationName().equalsIgnoreCase("shape")) {
			if (achar.getShape().getType() != null
					&& !StringUtils.xssValidate(achar.getShape().getType())) {
				
				errors.add(PropertyUtil.getProperty("sample", "achar.shape.type.invalid"));
				status = false;
			}
			if (achar.getShape().getMaxDimensionUnit() != null
					&& !achar.getShape().getMaxDimensionUnit()
							.matches(Constants.UNIT_PATTERN)) {
				
				errors.add(PropertyUtil.getProperty("sample", "achar.shape.maxDimensionUnit.invalid"));
				status = false;
			}
			if (achar.getShape().getMinDimensionUnit() != null
					&& !achar.getShape().getMinDimensionUnit()
							.matches(Constants.UNIT_PATTERN)) {
				
				errors.add(PropertyUtil.getProperty("sample", "achar.shape.minDimensionUnit.invalid"));
				status = false;
			}
		} else if (achar.getCharacterizationName().equalsIgnoreCase(
				"physical state")) {
			if (achar.getPhysicalState().getType() != null
					&& !StringUtils.xssValidate(achar.getPhysicalState()
							.getType())) {
				
				errors.add(PropertyUtil.getProperty("sample", "achar.physicalState.type.invalid"));
				status = false;
			}
		} else if (achar.getCharacterizationName().equalsIgnoreCase(
				"solubility")) {
			if (achar.getSolubility().getSolvent() != null
					&& !StringUtils.xssValidate(achar.getSolubility()
							.getSolvent())) {
				
				errors.add(PropertyUtil.getProperty("sample", "achar.solubility.solvent.invalid"));
				status = false;
			}
			if (achar.getSolubility().getCriticalConcentrationUnit() != null
					&& !achar.getSolubility().getCriticalConcentrationUnit()
							.matches(Constants.UNIT_PATTERN)) {
			
				errors.add(PropertyUtil.getProperty("sample", "achar.solubility.criticalConcentrationUnit.invalid"));
				status = false;
			}
		} else if (achar.getCharacterizationName().equalsIgnoreCase(
				"enzyme induction")) {
			if (achar.getEnzymeInduction().getEnzyme() != null
					&& !StringUtils.xssValidate(achar.getSolubility()
							.getSolvent())) {
				errors.add(PropertyUtil.getProperty("sample", "achar.enzymeInduction.enzyme.invalid"));
				status = false;
			}
		}
		return status;
	}

	private Boolean validateInputs(HttpServletRequest request,
			CharacterizationBean achar, List<String> errors) {
		if (!validateCharacterization(request, achar, errors)) {
			return false;
		}
		return true;
	}

	private boolean validateEmptyFinding(HttpServletRequest request,
			FindingBean finding, List<String> errors) {
		
		if (finding.getFiles().isEmpty()
				&& finding.getColumnHeaders().isEmpty()) {
			errors.add(PropertyUtil.getProperty("sample", "achar.theFinding.emptyFinding"));
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

	public java.io.File download(String fileId, HttpServletRequest request)
			throws Exception {
		CharacterizationService service = setServicesInSession(request);
		return downloadImage(service, fileId, request);
	}
	
	public String download(String fileId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CharacterizationService service = setServicesInSession(request);
		return downloadFile(service, fileId, request, response);
	}

}
