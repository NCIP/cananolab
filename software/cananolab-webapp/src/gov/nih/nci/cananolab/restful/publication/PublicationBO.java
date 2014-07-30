package gov.nih.nci.cananolab.restful.publication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationExporter;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.PublicationForm;
import gov.nih.nci.cananolab.restful.publication.InitPublicationSetup;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

public class PublicationBO extends BaseAnnotationBO{
	Logger logger = Logger.getLogger(PublicationBO.class);
	
		public void create(PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!validateToken(request)) {
			//return mapping.findForward("publicationMessage");
		}
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean publicationBean = (PublicationBean) form.getPublicationBean();
				
		Boolean newPub = true;
		if (publicationBean.getDomainFile().getId() != null
				&& publicationBean.getDomainFile().getId() > 0) {
			newPub = false;
		}
		//if (!this.savePublication(request, form)) {
		//	return input(mapping, form, request, response);
		//}

		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String sampleId = (String) form.getSampleId();
	//	ActionMessages messages = new ActionMessages();

		// retract from public if updating an existing public record and not
		// curator
		if (!newPub && !user.isCurator() && publicationBean.getPublicStatus()) {
		///	retractFromPublic(form, request, publicationBean.getDomainFile()
		///			.getId().toString(),
		//			((Publication) publicationBean.getDomainFile()).getTitle(),
		//			"publication");
		//	ActionMessage msg = null;
		//	msg = new ActionMessage(
		//			"message.updatePublication.retractFromPublic");
		//	messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	saveMessages(request, messages);
		} else {
		//	ActionMessage msg = new ActionMessage("message.submitPublication",
		//			publicationBean.getDomainFile().getTitle());
		//	messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	saveMessages(request, messages);
		}
		//resetToken(request);
		if (!StringUtils.isEmpty(sampleId)) {
		//	return summaryEdit(mapping, form, request, response);
		} else {
		//	return mapping.findForward("success");
		}
	}

	public List<String> savePublication(HttpServletRequest request,
			PublicationForm theForm) throws Exception {
		
		List<String> msgs = new ArrayList<String>();
		
		PublicationBean publicationBean = (PublicationBean) theForm.getPublicationBean();
		msgs = validateInputForPublication(publicationBean);
				
		PublicationService service = this.setServicesInSession(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String sampleId = (String) theForm.getSampleId();
		// validate publication file
		if (!validatePublicationFile(publicationBean)) {
			msgs.add(PropertyUtil.getProperty("publication", "publication.fileRequired"));
		//	return false;
			return msgs;
		}
		// validate associated sample names
		if (StringUtils.isEmpty(sampleId)
				&& !validateAssociatedSamples(request, publicationBean)) {
			msgs.add(PropertyUtil.getProperty("publication", "error.submitPublication.invalidSample"));
		//	return false;
			return msgs;
		}

		/**
		 * add current sample to associated samples if from sample publication
		 * page
		 */
		if (!StringUtils.isEmpty(sampleId)) {
			publicationBean.setFromSamplePage(true);
			Set<String> sampleNames = new HashSet<String>();
			SampleBean sampleBean = setupSampleById(sampleId, request);
			sampleNames.add(sampleBean.getDomain().getName());
			/**
			 * If user chosen other samples, need to add this pub to those
			 * samples.
			 */
			String[] otherSamples = (String[]) theForm.getOtherSamples();
					if (otherSamples.length > 0) {
				sampleNames.addAll(Arrays.asList(otherSamples));
			}
			publicationBean.setSampleNames(sampleNames
					.toArray(new String[sampleNames.size()]));
		} else {
			publicationBean.setFromSamplePage(false);
		}

		publicationBean.setupDomain(Constants.FOLDER_PUBLICATION,
				user.getLoginName());
		service.savePublication(publicationBean);

		InitPublicationSetup.getInstance().persistPublicationDropdowns(request,
				publicationBean);

		if (!StringUtils.isEmpty(sampleId)) {
			SortedSet<String> publicationCategories = InitSetup.getInstance()
					.getDefaultAndOtherTypesByLookup(request,
							"publicationCategories", "publication", "category",
							"otherCategory", true);
			List<String> allPublicationTypes = new ArrayList<String>(
					publicationCategories);
			int ind = allPublicationTypes
					.indexOf(((Publication) publicationBean.getDomainFile())
							.getCategory()) + 1;
			request.setAttribute("onloadJavascript", "showSummary('" + ind
					+ "', " + allPublicationTypes.size() + ")");
			msgs.add(PropertyUtil.getProperty("publication", "message.submitPublication"));
		}
		return msgs;
	}

	private List<String> validateInputForPublication(
			PublicationBean publicationBean) {
		List<String> errors = new ArrayList<String>();
		
		Publication publication = (Publication) publicationBean.getDomainFile();
		String category = publication.getCategory();
		if(category==null||InputValidationUtil.isTextFieldWhiteList(category)){
			errors.add(PropertyUtil.getProperty("publication", "publication.category.invalid"));
		}
		String status = publication.getStatus();
		if(status == null||InputValidationUtil.isTextFieldWhiteList(status)){
			errors.add(PropertyUtil.getProperty("publication", "publication.status.invalid"));
		}
		String title = publication.getTitle();
		if(title == null||InputValidationUtil.isTextFieldWhiteList(title)){
			errors.add(PropertyUtil.getProperty("publication", "publication.title.invalid"));
		}
		String externalUrl = publication.getUri();
		if(externalUrl == null||InputValidationUtil.isTextFieldWhiteList(externalUrl)){
			errors.add(PropertyUtil.getProperty("publication", "file.uri.invalid"));
		}
		for(int i=0;i<publicationBean.getAuthors().size();i++){
			String firstName = publicationBean.getAuthors().get(i).getFirstName();
			if(firstName == null||InputValidationUtil.isRelaxedAlphabetic(firstName)){
				errors.add(PropertyUtil.getProperty("publication", "publication.author.firstName.invalid"));
			}
			String lastName = publicationBean.getAuthors().get(i).getLastName();
			if(lastName == null||InputValidationUtil.isRelaxedAlphabetic(lastName)){
				errors.add(PropertyUtil.getProperty("publication", "publication.author.lastName.invalid"));
			}
			String initial = publicationBean.getAuthors().get(i).getInitial();
			if(initial == null||InputValidationUtil.isRelaxedAlphabetic(initial)){
				errors.add(PropertyUtil.getProperty("publication", "publication.author.initial.invalid"));
			}
		}
		String digitalObjectId = publication.getDigitalObjectId();
		if(digitalObjectId == null||InputValidationUtil.doi(digitalObjectId)){
			errors.add(PropertyUtil.getProperty("publication", "publication.doi.invalid"));
		}
		return errors;
	}

	private boolean validateAssociatedSamples(HttpServletRequest request,
			PublicationBean publicationBean) throws Exception {
		// sample service has already been created
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		for (String sampleName : publicationBean.getSampleNames()) {
			if (!StringUtils.isEmpty(sampleName)) {
				SampleBean sampleBean = service.findSampleByName(sampleName);
				if (sampleBean == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Handle delete request from Sample -> Publication -> Edit page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void removeFromSample(PublicationForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!validateToken(request)) {
//			return mapping.findForward("publicationMessage");
		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationService service = this.setServicesInSession(request);
		PublicationBean publicationBean = (PublicationBean) form.getPublicationBean();
		String sampleId = form.getSampleId();
		SampleBean sampleBean = this.setupSampleById(sampleId, request);
		service.removePublicationFromSample(sampleBean.getDomain().getName(),
				(Publication) publicationBean.getDomainFile());
	//	ActionMessages msgs = new ActionMessages();
	//	ActionMessage msg = new ActionMessage("message.deletePublication",
	//			publicationBean.getDomainFile().getTitle());
	//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	//	saveMessages(request, msgs);
	//	return summaryEdit(mapping, form, request, response);
	}

	/**
	 * Delete a publication from Publication update form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void delete(PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!validateToken(request)) {
//			return mapping.findForward("publicationMessage");
		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationService service = this.setServicesInSession(request);
		PublicationBean publicationBean = (PublicationBean) form.getPublicationBean();

		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				publicationBean.getDomainFile().getId().toString(),
				publicationBean.getDomainFile().getTitle(), "publication");
		service.deletePublication((Publication) publicationBean.getDomainFile());
	//	ActionMessages msgs = new ActionMessages();
	//	ActionMessage msg = new ActionMessage("message.deletePublication",
	//			publicationBean.getDomainFile().getTitle());
	//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	//	saveMessages(request, msgs);
	//	resetToken(request);
	//	return mapping.findForward("success");
	}

	public void setupNew(PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationBean pubBean = new PublicationBean();
		String sampleId = request.getParameter("sampleId");
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(request);
		form.setSampleId(sampleId);
		// clear copy to otherSamples
		form.setOtherSamples(new String[0]);

		String type = request.getParameter("type");
		if (!StringUtils.isEmpty(type)) {
			((Publication) pubBean.getDomainFile()).setCategory(type);
		}
		form.setPublicationBean(pubBean);
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
	//	ActionForward forward = mapping.getInputForward();
		if (!StringUtils.isEmpty(sampleId)) {
			InitSampleSetup.getInstance()
					.getOtherSampleNames(request, sampleId);
	//		forward = mapping.findForward("sampleSubmitPublication");
		}
		request.setAttribute("onloadJavascript",
				"updateSubmitFormBasedOnCategory()");
		request.getSession().removeAttribute("updatePublication");
//		saveToken(request);
//		return forward;
	}

	public PublicationBean setupUpdate(String publicationId,
			HttpServletRequest request)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<String> messages = new ArrayList<String>();
//		super.checkOpenAccessForm(request);
		publicationId = super.validateId(request, "publicationId");		
		PublicationService publicationService = this
				.setServicesInSession(request);
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, true);
		PublicationForm form = new PublicationForm();
		form.setPublicationBean(pubBean);		
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		request.setAttribute("onloadJavascript",
				"updateSubmitFormBasedOnCategory();updateFormFields('"
						+ publicationId + "')");
		setUpSubmitForReviewButton(request, pubBean.getDomainFile().getId()
				.toString(), pubBean.getPublicStatus());
		request.getSession().setAttribute("updatePublication", "true");
		
		//detect whether request is related to sample
		String sampleId = request.getParameter("sampleId");
		if (sampleId==null) {
			sampleId=(String)request.getAttribute("sampleId");
		}
		//saveToken(request);
		if (!StringUtils.isEmpty(sampleId)) {
			form.setSampleId(sampleId);
			//clear copy other samples
			form.setOtherSamples(new String[0]); 
			InitSampleSetup.getInstance()
					.getOtherSampleNames(request, sampleId);
			 messages.add(PropertyUtil.getProperty("sample", "message.submitPublication"));
	//		 return messages;
	//		return mapping.findForward("sampleSubmitPublication");
		} else {
	//		return mapping.findForward("publicationSubmitPublication");
			messages.add(PropertyUtil.getProperty("publication", "message.submitPublication"));
	//		return messages;
	//		return pubBean;
		}
		return pubBean;
	}

	/**
	 * Handle summary report print request.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 
	 * @return ActionForward
	 * @throws Exception
	 */
	public PublicationSummaryViewBean summaryPrint(String sampleId,
			HttpServletRequest request)
			throws Exception {
		// Marker that indicates page is for printing (hide tabs, links, etc).
		request.setAttribute("printView", Boolean.TRUE);

		PublicationSummaryViewBean summaryBean = (PublicationSummaryViewBean) request
				.getSession().getAttribute("publicationSummaryView");
		if (summaryBean == null) {
			// Prepare data.
			this.prepareSummary(sampleId, request);
			summaryBean = (PublicationSummaryViewBean) request.getSession()
					.getAttribute("publicationSummaryView");
		}

		// Filter out categories that not selected.
		this.filterType(request, summaryBean);

		return summaryBean;
	}

	/**
	 * Handle summary report view request.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public PublicationSummaryViewBean summaryView(String sampleId,
			HttpServletRequest request)
			throws Exception {
		// Prepare data.
		PublicationSummaryViewBean summaryView = this.prepareSummary(sampleId, request);

	//	return mapping.findForward("summaryView");
		return summaryView;
	}

	/**
	 * Handle summary report edit request.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	public PublicationSummaryViewBean summaryEdit(PublicationForm form,
			HttpServletRequest request)
			throws Exception {
		// if session is expired or the url is clicked on directly
		PublicationSummaryViewBean summaryEdit = null;
		String sampleId = form.getSampleId();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		System.out.println("Making sure the user is not null"+ user);
		if (user == null) {
			summaryEdit = summaryView(sampleId, request);
		}
		summaryEdit = this.prepareSummary(sampleId, request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(form,request);
	//	saveToken(request);
	//	return mapping.findForward("summaryEdit");
		return summaryEdit;
	}

	/**
	 * Handle summary report export request.
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
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute(
				"theSample");
		PublicationSummaryViewBean summaryBean = (PublicationSummaryViewBean) request
				.getSession().getAttribute("publicationSummaryView");
		if (sampleBean == null || summaryBean == null) {
			// Prepare data.
			this.prepareSummary(sampleId, request);
			sampleBean = (SampleBean) request.getSession().getAttribute(
					"theSample");
			summaryBean = (PublicationSummaryViewBean) request.getSession()
					.getAttribute("publicationSummaryView");
		}
		this.filterType(request, summaryBean);

		// Get sample name for constructing file name.
//		String type = request.getParameter("type");
		String fileName = ExportUtils.getExportFileName(sampleBean.getDomain()
				.getName(), "PublicationSummaryView", type);
		ExportUtils.prepareReponseForExcel(response, fileName);
		PublicationExporter.exportSummary(summaryBean,
				response.getOutputStream());

		return null;
	}

	/**
	 * Shared function for summaryView(), summaryEdit(), summaryExport() and
	 * summaryPrint().
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	private PublicationSummaryViewBean prepareSummary(String sampleId,
			HttpServletRequest request)
			throws Exception {
		// Remove previous result from session.
		HttpSession session = request.getSession();
		session.removeAttribute("publicationSummaryView");
		session.removeAttribute("theSample");

//		DynaValidatorForm theForm = (DynaValidatorForm) form;
//		String sampleId = form.getSampleId();
		PublicationService publicationService = this
				.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(sampleId, request);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"publicationCategories", "publication", "category",
				"otherCategory", true);
		/*
		 * if (!StringUtils.isEmpty(location) &&
		 * !location.equals(Constants.LOCAL_SITE)) { String serviceUrl =
		 * InitSetup.getInstance().getGridServiceUrl( request, location);
		 * publicationService = new PublicationServiceRemoteImpl(serviceUrl); }
		 */
		List<PublicationBean> publications = publicationService
				.findPublicationsBySampleId(sampleId);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(
				publications);
		
		// Save sample & publication bean in session for printing/exporting.
		session.setAttribute("publicationSummaryView", summaryView);
		session.setAttribute("theSample", sampleBean);

		if ("true".equals(request.getParameter("clearTab"))) {
			request.getSession().removeAttribute("onloadJavascript");
		}
		return summaryView;
	}

	public PublicationBean printDetailView(PublicationForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PublicationService publicationService = this
				.setServicesInSession(request);
		/*
		 * if (!StringUtils.isEmpty(location) &&
		 * !location.equals(Constants.LOCAL_SITE)) { String serviceUrl =
		 * InitSetup.getInstance().getGridServiceUrl( request, location);
		 * publicationService = new PublicationServiceRemoteImpl(serviceUrl); }
		 */
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, false);
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		form.setPublicationBean(pubBean);
//		return mapping.findForward("publicationDetailPrintView");
		return pubBean;
	}

	public PublicationBean input(ActionMapping mapping, PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// save new entered other types
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(request);
		PublicationBean publicationBean = (PublicationBean) form.getPublicationBean();
		// set empty year to null instead of the default 0
		Integer year = ((Publication) publicationBean.getDomainFile())
				.getYear();
		if (year != null && year == 0) {
			((Publication) publicationBean.getDomainFile()).setYear(null);
		}
		String selectedPublicationType = ((Publication) publicationBean
				.getDomainFile()).getCategory();
		if (selectedPublicationType != null) {
			SortedSet<String> types = (SortedSet<String>) request.getSession()
					.getAttribute("publicationCategories");
			if (types != null) {
				types.add(selectedPublicationType);
				request.getSession().setAttribute("publicationCategories",
						types);
			}
		}
		String selectedPublicationStatus = ((Publication) publicationBean
				.getDomainFile()).getStatus();
		if (selectedPublicationStatus != null) {
			SortedSet<String> statuses = (SortedSet<String>) request
					.getSession().getAttribute("publicationStatuses");
			if (statuses != null) {
				statuses.add(selectedPublicationStatus);
				request.getSession().setAttribute("publicationStatuses",
						statuses);
			}
		}
		form.setPublicationBean(publicationBean);

		/**
		 * Set PubMedId from default value 0 to null for better displaying
		 * result.
		 */
		Publication pub = (Publication) publicationBean.getDomainFile();
		if (pub.getPubMedId() != null && pub.getPubMedId() == 0) {
			pub.setPubMedId(null);
		}
		// disable PubMed fields from parsing and toggle access name label
		if (pub.getPubMedId() != null) {
			request.setAttribute(
					"onloadJavascript",
					"updateSubmitFormBasedOnCategory();disableAutoFields(); toggleAccessNameLabel()");
		} else {
			request.setAttribute("onloadJavascript",
					"updateSubmitFormBasedOnCategory();enableAutoFields();toggleAccessNameLabel()");
		}
//		return mapping.findForward("publicationSubmitPublication");
		return publicationBean;
	}

	public PublicationBean addAuthor(PublicationForm form,
			HttpServletRequest request)
			throws Exception {
		if (!validateToken(request)) {
//			return mapping.findForward("publicationMessage");
		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean pbean = (PublicationBean) form.getPublicationBean();
		pbean.addAuthor();

//		return mapping.getInputForward();
		return pbean;
	}

	private boolean validatePublicationFile(PublicationBean pubBean) {
		Publication publication = (Publication) pubBean.getDomainFile();
		// don't need the file or url if PubMed ID or DOI is entered
		if ((publication.getPubMedId() != null && publication.getPubMedId() != 0)
				|| !StringUtils.isEmpty(publication.getDigitalObjectId())) {
			return true;
		} else if (publication.getUriExternal()
				&& !StringUtils.isEmpty(pubBean.getExternalUrl())) {
			return true;
		} else if (!publication.getUriExternal()
				&& !StringUtils
						.isEmpty(pubBean.getUploadedFile().getFileName())) {
			return true;
		} else if (!StringUtils.isEmpty(publication.getUri())) {
			return true;
		}
		// doesn't require file if publication status is not "published"
		if (!publication.getStatus().equalsIgnoreCase("published")) {
			return true;
		}
		return false;
	}

	// private boolean validateResearchAreas(HttpServletRequest request,
	// String[] researchAreas) throws Exception {
	// ActionMessages msgs = new ActionMessages();
	// boolean noErrors = true;
	// if (researchAreas == null || researchAreas.length == 0) {
	// ActionMessage msg = new ActionMessage(
	// "submitDynaValidatorForm.publication.researchArea", "researchAreas");
	// msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	// this.saveErrors(request, msgs);
	// noErrors = false;
	// } else {
	// System.out.println("validateResearchAreas =="
	// + Arrays.toString(researchAreas));
	// }
	// return noErrors;
	// }

	public String exportDetail(PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PublicationService publicationService = this
				.setServicesInSession(request);

		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(
				publicationId, false);
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		form.setPublicationBean(pubBean);
		String title = pubBean.getDomainFile().getTitle();
		if (!StringUtils.isEmpty(title)) {
			title = title.substring(0, 10);
		}

		String fileName = this.getExportFileName(title, "detailView");
		ExportUtils.prepareReponseForExcel(response, fileName);
		PublicationExporter.exportDetail(pubBean, response.getOutputStream());

		return null;
	}

	private String getExportFileName(String titleName, String viewType) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(titleName);
		nameParts.add("Publication");
		nameParts.add(viewType);
		nameParts.add(DateUtils.convertDateToString(Calendar.getInstance()
				.getTime()));
		String exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}

	/**
	 * Shared function for summaryExport() and summaryPrint(). Filter out
	 * unselected types when user selected one type for print/export.
	 * 
	 * @param request
	 * @param compBean
	 */
	private void filterType(HttpServletRequest request,
			PublicationSummaryViewBean summaryBean) {
		// 1. Restore all data first as bean might be filtered before.
		SortedMap<String, List<PublicationBean>> dataMap = summaryBean
				.getCategory2Publications();
		summaryBean.setPublicationCategories(dataMap.keySet());

		// 2. Filter out categories that are not selected.
		String type = request.getParameter("type");
		Set<String> cats = new HashSet<String>(1);
		if( !StringUtils.isEmpty(type) && type.equals("all")) {
			cats.add("report");
			cats.add("review");
			summaryBean.setPublicationCategories(cats);
		}
		else {
			cats.add(type);
			summaryBean.setPublicationCategories(cats);
		}
	}

	private PublicationService setServicesInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		PublicationService publicationService = new PublicationServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("publicationService",
				publicationService);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return publicationService;
	}

	public void saveAccess(PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!validateToken(request)) {
	//		return mapping.findForward("publicationMessage");
		}
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean publication = (PublicationBean) form.getPublicationBean();
				
		AccessibilityBean theAccess = publication.getTheAccess();
		List<String> errors = super.validateAccess(request, theAccess);
		if (errors.size() > 0) {
			return; //TODO: saveAccess() should return an object that contains a list of errors;
		}

		PublicationService service = this.setServicesInSession(request);
		// if publication is new, save publication first
		if (publication.getDomainFile().getId() == null
				|| publication.getDomainFile().getId() != null
				&& publication.getDomainFile().getId() == 0) {
			this.savePublication(request, form);
		}
		// if publication is public, the access is not public, retract
		// public
		// privilege would be handled in the service method
		service.assignAccessibility(theAccess,
				(Publication) publication.getDomainFile());
		// update status to retracted if the access is not public and
		// publication is public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)
				&& publication.getPublicStatus()) {
			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS,
					request, publication.getDomainFile().getId().toString(),
					publication.getDomainFile().getTitle(), "publication");
		}
		// if access is public, pending review status, update review
		// status to public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
			this.switchPendingReviewToPublic(request, publication
					.getDomainFile().getId().toString());
		}

		this.setAccesses(request, publication);
		request.setAttribute("publicationId", publication.getDomainFile()
				.getId().toString());
		
		//check if sampleId exists in the form
		if (form.getSampleId()!=null) {
			request.setAttribute("sampleId", form.getSampleId());
		}
//		return setupUpdate(mapping, form, request, response);
	}

	protected void setAccesses(HttpServletRequest request,
			PublicationBean publicationBean) throws Exception {
		PublicationService service = (PublicationService) request.getSession()
				.getAttribute("publicationService");
		List<AccessibilityBean> groupAccesses = service
				.findGroupAccessibilities(publicationBean.getDomainFile()
						.getId().toString());
		List<AccessibilityBean> userAccesses = service
				.findUserAccessibilities(publicationBean.getDomainFile()
						.getId().toString());
		publicationBean.setUserAccesses(userAccesses);
		publicationBean.setGroupAccesses(groupAccesses);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		publicationBean.setUser(user);
	}

	public void deleteAccess(PublicationForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!validateToken(request)) {
	//		return mapping.findForward("publicationMessage");
		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean publication = (PublicationBean) form.getPublicationBean();
				
		AccessibilityBean theAccess = publication.getTheAccess();
		PublicationService service = this.setServicesInSession(request);
		service.removeAccessibility(theAccess,
				(Publication) publication.getDomainFile());
		this.setAccesses(request, publication);
		request.setAttribute("publicationId", publication.getDomainFile()
				.getId().toString());
		
		//check if sampleId exists in the form
		if (form.getSampleId()!=null) {
			request.setAttribute("sampleId", form.getSampleId());
		}
	//	setupUpdate(form, request, response);
	//	return setupUpdate(form, request, response);
	}

	protected void removePublicAccess(PublicationForm theForm,
			HttpServletRequest request) throws Exception {
		PublicationBean publication = (PublicationBean) theForm.getPublicationBean();
		PublicationService service = this.setServicesInSession(request);
		service.removeAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
				(Publication) publication.getDomainFile());
	}

	public String download(String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("Inside download from publication BO");
		PublicationService service = this.setServicesInSession(request);
		return downloadFile(service, fileId, request, response);
	}
	
	public String[] getMatchedSampleNames(String searchStr, HttpServletRequest request) {
		
			UserBean user = (UserBean) request.getSession().getAttribute("user");
			if (user == null) {
				return null;
			}
			SecurityService securityService = (SecurityService) request
					.getSession().getAttribute("securityService");
			PublicationServiceLocalImpl service = new PublicationServiceLocalImpl(securityService);
			try {
				SampleServiceHelper sampleHelper = (SampleServiceHelper) (service
						.getSampleHelper());
				List<String> sampleNames = sampleHelper
						.findSampleNamesBy(searchStr);
				Collections.sort(sampleNames,
						new Comparators.SortableNameComparator());
				return sampleNames.toArray(new String[sampleNames.size()]);
			} catch (Exception e) {
				logger.error(
						"Problem getting all sample names for publication submission \n",
						e);
				return new String[] { "" };
			}
		}

}

