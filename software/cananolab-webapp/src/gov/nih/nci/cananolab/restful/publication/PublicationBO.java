package gov.nih.nci.cananolab.restful.publication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitPublicationBean;
import gov.nih.nci.cananolab.security.AccessControlInfo;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationExporter;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.ui.form.PublicationForm;
import gov.nih.nci.cananolab.restful.publication.InitPublicationSetup;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("publicationBO")
public class PublicationBO extends BaseAnnotationBO
{
	private Logger logger = Logger.getLogger(PublicationBO.class);

	@Autowired
	private CurationService curationServiceDAO;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public List<String> create(SimpleSubmitPublicationBean simplePubBean, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
		SimplePublicationSummaryViewBean bean = new SimplePublicationSummaryViewBean();
		HttpSession session = request.getSession();
		PublicationBean publicationBean = transferSimpleSubmitPublicationBean(simplePubBean);

		String sampleId = simplePubBean.getSampleId().toString();
		session.setAttribute("sampleId", sampleId);
		Boolean newPub = true;
		if (publicationBean.getDomainFile().getId() != null && publicationBean.getDomainFile().getId() > 0) {
			newPub = false;
		}
		msgs = savePublication(request, publicationBean);
		request.getSession().removeAttribute("newFileData");

		if(msgs.size()>0){
			bean.setErrors(msgs);
			return msgs;
		}

		// retract from public if updating an existing public record and not
		// curator
		if (!newPub && !SpringSecurityUtil.getPrincipal().isCurator() && 
			springSecurityAclService.checkObjectPublic(publicationBean.getDomainFile().getId(), SecureClassesEnum.PUBLICATION.getClazz()))
		{
			//			retractFromPublic(sampleId, request, publicationBean.getDomainFile()
			//					.getId().toString(),
			//					((Publication) publicationBean.getDomainFile()).getTitle(),
			//					"publication");

			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS, request, publicationBean.getDomainFile()
					.getId().toString(), ((Publication) publicationBean.getDomainFile()).getTitle(), "publication");
			removePublicAccess(publicationBean, request);

			msgs.add("retract success");
			bean.setErrors(msgs);
			return msgs;

		} else {
			msgs.add("success");
			bean.setErrors(msgs);

		}

		//	if (!StringUtils.isEmpty(sampleId)) {
		//		return summaryEdit(sampleId, request);
		//	} 
		return msgs;
	}

	public List<String> savePublication(HttpServletRequest request, PublicationBean publicationBean) throws Exception 
	{
		List<String> msgs = new ArrayList<String>();
		HttpSession session = request.getSession();
		msgs = validateInputForPublication(publicationBean);
		if (msgs.size() > 0)
			return msgs;

		String sampleId = (String) request.getSession().getAttribute("sampleId");
		// validate publication file
		if (!validatePublicationFile(publicationBean)) {
			msgs.add(PropertyUtil.getProperty("publication", "publication.fileRequired"));
			//	return false;
			return msgs;
		}
		// validate associated sample names

		if (StringUtils.isEmpty(sampleId) && !validateAssociatedSamples(request, publicationBean)) {
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
			String[] otherSamples = (String[]) publicationBean.getSampleNames();
			if (otherSamples.length > 0) {
				sampleNames.addAll(Arrays.asList(otherSamples));
			}
			publicationBean.setSampleNames(sampleNames.toArray(new String[sampleNames.size()]));
		} else {
			publicationBean.setFromSamplePage(false);
		}

		publicationBean.setupDomain(Constants.FOLDER_PUBLICATION, SpringSecurityUtil.getLoggedInUserName());
		String timestamp = DateUtils.convertDateToString(new Date(), "yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
		if(newFileData!=null){
			publicationBean.setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
			publicationBean.getDomainFile().setUri(Constants.FOLDER_PUBLICATION+ "/" + timestamp + "_"
					+ publicationBean.getDomainFile().getName());
		}
		publicationService.savePublication(publicationBean);
		//	msgs.add("success");

		session.setAttribute("publicationBean", publicationBean);
		request.setAttribute("publicationId", publicationBean.getDomainFile().getId().toString());

		InitPublicationSetup.getInstance().persistPublicationDropdowns(request, publicationBean);

		if (!StringUtils.isEmpty(sampleId)) {
			SortedSet<String> publicationCategories = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
							"publicationCategories", "publication", "category", "otherCategory", true);
			List<String> allPublicationTypes = new ArrayList<String>(publicationCategories);
			int ind = allPublicationTypes.indexOf(((Publication) publicationBean.getDomainFile()).getCategory()) + 1;
			request.setAttribute("onloadJavascript", "showSummary('" + ind + "', " + allPublicationTypes.size() + ")");
			//msgs.add(PropertyUtil.getProperty("publication", "message.submitPublication"+publicationBean.getDomainFile().getTitle()));

		}
		return msgs;
	}

	private PublicationBean transferSimpleSubmitPublicationBean(SimpleSubmitPublicationBean bean)
	{
		// TODO Auto-generated method stub
		PublicationBean pubBean =  new PublicationBean();
		Publication pub = (Publication) pubBean.getDomainFile();
		HashSet<Author> authorCollection = new HashSet<Author>();
		for(int i=0;i<bean.getAuthors().size();i++){
			authorCollection.add(bean.getAuthors().get(i));
		}
		pub.setCategory(bean.getCategory());
		pub.setCreatedBy(bean.getCreatedBy());
		pub.setStatus(bean.getStatus());
		pub.setTitle(bean.getTitle());
		pub.setId(bean.getFileId());
		pub.setDescription(bean.getDescription());
		pub.setStartPage(bean.getStartPage());
		pub.setEndPage(bean.getEndPage());
		pub.setYear(bean.getYear());
		pub.setDigitalObjectId(bean.getDigitalObjectId());
		pub.setPubMedId(bean.getPubMedId());
		pub.setJournalName(bean.getJournalName());
		pub.setVolume(bean.getVolume());
		pub.setUri(bean.getUri());
		pub.setUriExternal(bean.getUriExternal());
		pub.setAuthorCollection(authorCollection);
		pubBean.setResearchAreas(bean.getResearchAreas());
		pubBean.setAuthors(bean.getAuthors());
		pubBean.setSampleNamesStr(bean.getSampleNamesStr());
		pubBean.setGroupAccesses(bean.getGroupAccesses());
		pubBean.setUserAccesses(bean.getUserAccesses());
		pubBean.setDomainFile(pub);
		pubBean.setUserDeletable(springSecurityAclService.currentUserHasDeletePermission(bean.getFileId(), SecureClassesEnum.PUBLICATION.getClazz()));
		pubBean.setKeywordsStr(bean.getKeywordsStr());
		pubBean.setTheAccess(bean.getTheAccess());
		pubBean.setExternalUrl(bean.getExternalUrl());
		return pubBean;
	}

	private List<String> validateInputForPublication(PublicationBean publicationBean) {
		List<String> errors = new ArrayList<String>();

		Publication publication = (Publication) publicationBean.getDomainFile();
		String category = publication.getCategory();
		if(category == null||category == ""){
			errors.add("Publication Type is required.");
		}
		if(!InputValidationUtil.isTextFieldWhiteList(category)){
			errors.add(PropertyUtil.getProperty("publication", "publication.category.invalid"));
		}
		String status = publication.getStatus();
		if(status == null||status == ""){
			errors.add("Publication Status is required.");
		}
		if(!InputValidationUtil.isTextFieldWhiteList(status)){
			errors.add(PropertyUtil.getProperty("publication", "publication.status.invalid"));
		}
		String title = publication.getTitle();
		if(title == null || title == ""){
			errors.add("Title is required.");
		}
		if(!InputValidationUtil.isTextFieldWhiteList(title)){
			errors.add(PropertyUtil.getProperty("publication", "publication.title.invalid"));
		}
		String uri = publication.getUri();
		if(!InputValidationUtil.isTextFieldWhiteList(uri)){
			errors.add(PropertyUtil.getProperty("publication", "file.uri.invalid"));
		}
		for(int i=0;i<publicationBean.getAuthors().size();i++){
			String firstName = publicationBean.getAuthors().get(i).getFirstName();
			if(!InputValidationUtil.isRelaxedAlphabetic(firstName)){
				errors.add(PropertyUtil.getProperty("publication", "publication.author.firstName.invalid"));
			}
			String lastName = publicationBean.getAuthors().get(i).getLastName();
			if(!InputValidationUtil.isRelaxedAlphabetic(lastName)){
				errors.add(PropertyUtil.getProperty("publication", "publication.author.lastName.invalid"));
			}
			String initial = publicationBean.getAuthors().get(i).getInitial();
			if (!InputValidationUtil.isRelaxedAlphabetic(initial)){
				errors.add(PropertyUtil.getProperty("publication", "publication.author.initial.invalid"));
			}
		}
		String digitalObjectId = publication.getDigitalObjectId();
		if (!InputValidationUtil.isDoiValid(digitalObjectId)){
			errors.add(PropertyUtil.getProperty("publication", "publication.doi.invalid"));
		}
		String externalUrl = publicationBean.getExternalUrl();
		if (!InputValidationUtil.isUrlValid(externalUrl)){
			errors.add("External URL is invalid");
		}
		return errors;
	}

	private boolean validateAssociatedSamples(HttpServletRequest request, PublicationBean publicationBean) throws Exception
	{
		// sample service has already been created
		for (String sampleName : publicationBean.getSampleNames()) {
			if (!StringUtils.isEmpty(sampleName)) {
				SampleBean sampleBean = sampleService.findSampleByName(sampleName);
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
	public SimplePublicationSummaryViewBean removeFromSample(SimpleSubmitPublicationBean simplePubBean, HttpServletRequest request) throws Exception
	{
		PublicationBean publicationBean = transferSimpleSubmitPublicationBean(simplePubBean);//(PublicationBean) form.getPublicationBean();
		String sampleId = simplePubBean.getSampleId().toString();  //form.getSampleId();
		SampleBean sampleBean = this.setupSampleById(sampleId, request);
		publicationService.removePublicationFromSample(sampleBean.getDomain().getName(), (Publication) publicationBean.getDomainFile());
		//	ActionMessages msgs = new ActionMessages();
		//	ActionMessage msg = new ActionMessage("message.deletePublication",
		//			publicationBean.getDomainFile().getTitle());
		//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	saveMessages(request, msgs);
		return summaryEdit(sampleId, request);
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
	public List<String> delete(SimpleSubmitPublicationBean simplePubBean, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
		PublicationBean publicationBean = transferSimpleSubmitPublicationBean(simplePubBean);

		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				publicationBean.getDomainFile().getId().toString(),
				publicationBean.getDomainFile().getTitle(), "publication");
		publicationService.deletePublication((Publication) publicationBean.getDomainFile());

		msgs.add("success");
		return msgs;
	}

	public void setupNew(PublicationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
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
			InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId, sampleService);
			//		forward = mapping.findForward("sampleSubmitPublication");
		}
		request.setAttribute("onloadJavascript", "updateSubmitFormBasedOnCategory()");
		request.getSession().removeAttribute("updatePublication");
		//		saveToken(request);
		//		return forward;
	}

	public SimpleSubmitPublicationBean setupUpdate(String publicationId, String sampleId, HttpServletRequest request) throws Exception
	{
		List<String> messages = new ArrayList<String>();
		SimpleSubmitPublicationBean bean = new SimpleSubmitPublicationBean();
		//		super.checkOpenAccessForm(request);
		publicationId = super.validateId(request, "publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(publicationId, true);
		PublicationForm form = new PublicationForm();
		form.setPublicationBean(pubBean);		
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		request.setAttribute("onloadJavascript",
				"updateSubmitFormBasedOnCategory();updateFormFields('" + publicationId + "')");
		setUpSubmitForReviewButton(request, pubBean.getDomainFile().getId().toString(), 
				springSecurityAclService.checkObjectPublic(pubBean.getDomainFile().getId(), SecureClassesEnum.PUBLICATION.getClazz()));
		request.getSession().setAttribute("updatePublication", "true");

		//detect whether request is related to sample
		//String sampleId = request.getParameter("sampleId");
		if (sampleId==null) {
			sampleId=(String)request.getSession().getAttribute("sampleId");
		}
		//saveToken(request);
		if (!StringUtils.isEmpty(sampleId)) {
			form.setSampleId(sampleId);
			//clear copy other samples
			InitSampleSetup.getInstance().getOtherSampleNames(request, sampleId, sampleService);
			messages.add(PropertyUtil.getProperty("sample", "message.submitPublication"));

		} else {
			messages.add(PropertyUtil.getProperty("publication", "message.submitPublication"));
		}

		transferPublicationBeanForEdit(pubBean, bean, request);
		return bean;
	}
	
	public void transferPublicationBeanForEdit(PublicationBean pubBean, SimpleSubmitPublicationBean bean, HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<Author> authors = new ArrayList<Author>();
		Publication pub = (Publication) pubBean.getDomainFile();
		bean.setCategory(pub.getCategory());
		bean.setStatus(pub.getStatus());
		bean.setPubMedId(pub.getPubMedId());
		bean.setDigitalObjectId(pub.getDigitalObjectId());
		bean.setTitle(pub.getTitle());
		bean.setJournalName(pub.getJournalName());
		bean.setYear(pub.getYear());
		bean.setVolume(pub.getVolume());
		bean.setStartPage(pub.getStartPage());
		bean.setEndPage(pub.getEndPage());
		for(int i=0;i<pubBean.getAuthors().size();i++)
		{	
			Author author = new Author();
			author.setFirstName(pubBean.getAuthors().get(i).getFirstName());
			author.setLastName(pubBean.getAuthors().get(i).getLastName());
			author.setInitial(pubBean.getAuthors().get(i).getInitial());
			author.setId(pubBean.getAuthors().get(i).getId());
			authors.add(author);
		}
		bean.setAuthors(authors);
		bean.setKeywordsStr(pubBean.getKeywordsStr());
		bean.setDescription(pub.getDescription());
		bean.setResearchAreas(pubBean.getResearchAreas());
		bean.setUri(pub.getUri());
		bean.setFileId(pub.getId());
		bean.setUriExternal(pub.getUriExternal());
		bean.setSampleNamesStr(pubBean.getSampleNamesStr());
		bean.setGroupAccesses(pubBean.getGroupAccesses());
		bean.setUserAccesses(pubBean.getUserAccesses());
		bean.setIsPublic(springSecurityAclService.checkObjectPublic(pub.getId(), SecureClassesEnum.PUBLICATION.getClazz()));
		bean.setIsOwner(springSecurityAclService.isOwnerOfObject(pub.getId(), SecureClassesEnum.PUBLICATION.getClazz()));
		bean.setCreatedBy(pub.getCreatedBy());
		bean.setUserDeletable(pubBean.getUserDeletable());
		List<String> otherSampleNamesList = (List<String>) request.getSession().getAttribute("otherSampleNames");
		bean.setOtherSampleNames(otherSampleNamesList);
		bean.setReview((Boolean) request.getAttribute("review"));
		bean.setExternalUrl(pubBean.getExternalUrl());
		
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
	public PublicationSummaryViewBean summaryPrint(String sampleId, HttpServletRequest request) throws Exception
	{
		// Marker that indicates page is for printing (hide tabs, links, etc).
		request.setAttribute("printView", Boolean.TRUE);

		PublicationSummaryViewBean summaryBean = (PublicationSummaryViewBean) request.getSession().getAttribute("publicationSummaryView");
		if (summaryBean == null) {
			// Prepare data.
			this.prepareSummary(sampleId, request);
			summaryBean = (PublicationSummaryViewBean) request.getSession().getAttribute("publicationSummaryView");
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
	public PublicationSummaryViewBean summaryView(String sampleId, HttpServletRequest request) throws Exception
	{
		// Prepare data.
		PublicationSummaryViewBean summaryView = this.prepareSummary(sampleId, request);
		HttpSession session = request.getSession();
		session.setAttribute("sampleId", sampleId);

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
	public SimplePublicationSummaryViewBean summaryEdit(String sampleId, HttpServletRequest request) throws Exception
	{
		// if session is expired or the url is clicked on directly
		PublicationSummaryViewBean summaryEdit = null;
		//String sampleId = form.getSampleId();
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			summaryEdit = summaryView(sampleId, request);
		}
		summaryEdit = this.prepareSummary(sampleId, request);
		//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		//	super.checkOpenAccessForm(form,request);
		//	saveToken(request);
		//	return mapping.findForward("summaryEdit");
		SimplePublicationSummaryViewBean bean = new SimplePublicationSummaryViewBean();
		bean.transferPublicationBeanForSummaryView(summaryEdit);
		return bean;
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
	public String summaryExport(String sampleId, String type, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute("theSample");
		PublicationSummaryViewBean summaryBean = (PublicationSummaryViewBean) request.getSession().getAttribute("publicationSummaryView");
		if (sampleBean == null || summaryBean == null) {
			// Prepare data.
			this.prepareSummary(sampleId, request);
			sampleBean = (SampleBean) request.getSession().getAttribute("theSample");
			summaryBean = (PublicationSummaryViewBean) request.getSession().getAttribute("publicationSummaryView");
		}
		this.filterType(request, summaryBean);

		// Get sample name for constructing file name.
		//		String type = request.getParameter("type");
		String fileName = ExportUtils.getExportFileName(sampleBean.getDomain().getName(), "PublicationSummaryView", type);
		ExportUtils.prepareReponseForExcel(response, fileName);
		PublicationExporter.exportSummary(summaryBean, response.getOutputStream());

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
	private PublicationSummaryViewBean prepareSummary(String sampleId, HttpServletRequest request) throws Exception
	{
		// Remove previous result from session.
		HttpSession session = request.getSession();
		session.removeAttribute("publicationSummaryView");
		session.removeAttribute("theSample");
		SampleBean sampleBean = setupSampleById(sampleId, request);
		InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
				"publicationCategories", "publication", "category", "otherCategory", true);
		/*
		 * if (!StringUtils.isEmpty(location) &&
		 * !location.equals(Constants.LOCAL_SITE)) { String serviceUrl =
		 * InitSetup.getInstance().getGridServiceUrl( request, location);
		 * publicationService = new PublicationServiceRemoteImpl(serviceUrl); }
		 */
		List<PublicationBean> publications = publicationService.findPublicationsBySampleId(sampleId);
		PublicationSummaryViewBean summaryView = new PublicationSummaryViewBean(publications);

		// Save sample & publication bean in session for printing/exporting.
		session.setAttribute("publicationSummaryView", summaryView);
		session.setAttribute("theSample", sampleBean);

		if ("true".equals(request.getParameter("clearTab"))) {
			request.getSession().removeAttribute("onloadJavascript");
		}
		return summaryView;
	}

	public PublicationBean printDetailView(PublicationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		/*
		 * if (!StringUtils.isEmpty(location) &&
		 * !location.equals(Constants.LOCAL_SITE)) { String serviceUrl =
		 * InitSetup.getInstance().getGridServiceUrl( request, location);
		 * publicationService = new PublicationServiceRemoteImpl(serviceUrl); }
		 */
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(publicationId, false);
		//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		form.setPublicationBean(pubBean);
		//		return mapping.findForward("publicationDetailPrintView");
		return pubBean;
	}

	public SimpleSubmitPublicationBean input(SimpleSubmitPublicationBean simplePubBean, HttpServletRequest request) throws Exception
	{
		PublicationForm form = new PublicationForm();
		// save new entered other types
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		super.checkOpenAccessForm(request);
		PublicationBean publicationBean = transferSimpleSubmitPublicationBean(simplePubBean);//(PublicationBean) form.getPublicationBean();
		// set empty year to null instead of the default 0
		Integer year = ((Publication) publicationBean.getDomainFile()).getYear();
		if (year != null && year == 0) {
			((Publication) publicationBean.getDomainFile()).setYear(null);
		}
		String selectedPublicationType = ((Publication) publicationBean.getDomainFile()).getCategory();
		if (selectedPublicationType != null) {
			SortedSet<String> types = (SortedSet<String>) request.getSession().getAttribute("publicationCategories");
			if (types != null) {
				types.add(selectedPublicationType);
				request.getSession().setAttribute("publicationCategories", types);
			}
		}
		String selectedPublicationStatus = ((Publication) publicationBean.getDomainFile()).getStatus();
		if (selectedPublicationStatus != null) {
			SortedSet<String> statuses = (SortedSet<String>) request.getSession().getAttribute("publicationStatuses");
			if (statuses != null) {
				statuses.add(selectedPublicationStatus);
				request.getSession().setAttribute("publicationStatuses", statuses);
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
			request.setAttribute("onloadJavascript", "updateSubmitFormBasedOnCategory();disableAutoFields(); toggleAccessNameLabel()");
		} else {
			request.setAttribute("onloadJavascript", "updateSubmitFormBasedOnCategory();enableAutoFields();toggleAccessNameLabel()");
		}
		//		return mapping.findForward("publicationSubmitPublication");
		//		return publicationBean;
		SimpleSubmitPublicationBean bean = new SimpleSubmitPublicationBean();
		transferPublicationBeanForEdit(publicationBean, bean, request);
		return bean;
	}

	public PublicationBean addAuthor(PublicationForm form, HttpServletRequest request) throws Exception 
	{
		//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean pbean = (PublicationBean) form.getPublicationBean();
		pbean.addAuthor();

		//		return mapping.getInputForward();
		return pbean;
	}

	private boolean validatePublicationFile(PublicationBean pubBean)
	{
		Publication publication = (Publication) pubBean.getDomainFile();
		// don't need the file or url if PubMed ID or DOI is entered
		if ((publication.getPubMedId() != null && publication.getPubMedId() != 0) || 
			!StringUtils.isEmpty(publication.getDigitalObjectId())) {
			return true;
		} else if (publication.getUriExternal() && !StringUtils.isEmpty(pubBean.getExternalUrl())) {
			return true;
		} else if (!publication.getUriExternal() && !StringUtils.isEmpty(pubBean.getDomainFile().getName())) {
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

	public String exportDetail(PublicationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String publicationId = request.getParameter("publicationId");
		PublicationBean pubBean = publicationService.findPublicationById(publicationId, false);
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
		nameParts.add(DateUtils.convertDateToString(Calendar.getInstance().getTime()));
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
	private void filterType(HttpServletRequest request, PublicationSummaryViewBean summaryBean)
	{
		// 1. Restore all data first as bean might be filtered before.
		SortedMap<String, List<PublicationBean>> dataMap = summaryBean.getCategory2Publications();
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

	public SimpleSubmitPublicationBean saveAccess(SimpleSubmitPublicationBean simplePubBean, HttpServletRequest request) throws Exception
	{
		//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean publication = (PublicationBean) transferSimpleSubmitPublicationBean(simplePubBean);//(PublicationBean) theForm.getPublicationBean();

		AccessControlInfo theAccess = publication.getTheAccess();
		List<String> errors = super.validateAccess(request, theAccess);
		if (errors.size() > 0) {
			SimpleSubmitPublicationBean bean =  new SimpleSubmitPublicationBean();
			bean.setErrors(errors);
			return bean;
		}
		//TODO: saveAccess() should return an object that contains a list of errors;
		// if publication is new, save publication first
		if (publication.getDomainFile().getId() == null || publication.getDomainFile().getId() != null && 
			publication.getDomainFile().getId() == 0) {
			errors = this.savePublication(request, publication);
			SimpleSubmitPublicationBean bean =  new SimpleSubmitPublicationBean();
			if(errors.size()>0){
				bean.setErrors(errors);
				return bean;
			}

		}
		// if publication is public, the access is not public, retract
		// public
		// privilege would be handled in the service method
		PublicationBean pub =  (PublicationBean) request.getSession().getAttribute("publicationBean");
		if(pub == null){
			pub = publication;
		}
		publicationService.assignAccessibility(theAccess, (Publication) pub.getDomainFile());
		// update status to retracted if the access is not public and
		// publication is public
		if (!CaNanoRoleEnum.ROLE_ANONYMOUS.toString().equalsIgnoreCase(theAccess.getRecipient()) && 
			springSecurityAclService.checkObjectPublic(pub.getDomainFile().getId(), SecureClassesEnum.PUBLICATION.getClazz()))
		{
			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS, request, pub.getDomainFile().getId().toString(),
					pub.getDomainFile().getTitle(), "publication");
			springSecurityAclService.retractObjectFromPublic(pub.getDomainFile().getId(), SecureClassesEnum.PUBLICATION.getClazz());
		}
		// if access is public, pending review status, update review
		// status to public
		if (CaNanoRoleEnum.ROLE_ANONYMOUS.toString().equalsIgnoreCase(theAccess.getRecipient())) {
			this.switchPendingReviewToPublic(request, pub.getDomainFile().getId().toString());
		}

		PublicationBean pBean = this.setAccesses(request, pub);
		request.setAttribute("publicationId", pBean.getDomainFile().getId().toString());
		errors.add(pub.getDomainFile().getId().toString());
		//check if sampleId exists in the form
		String sampleId = (String) simplePubBean.getSampleId();

		if (sampleId!=null) {
			request.setAttribute("sampleId", sampleId);
		}
		//		return setupUpdate(mapping, form, request, response);
		return setupUpdate(pBean.getDomainFile().getId().toString(),sampleId, request);
	}

	protected PublicationBean setAccesses(HttpServletRequest request, PublicationBean publicationBean) throws Exception
	{
		springSecurityAclService.loadAccessControlInfoForObject(publicationBean.getDomainFile().getId(), SecureClassesEnum.PUBLICATION.getClazz(), publicationBean);
		return publicationBean;
	}

	public SimpleSubmitPublicationBean deleteAccess(SimpleSubmitPublicationBean simplePubBean, HttpServletRequest request)
					throws Exception {
		//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		PublicationBean publication = transferSimpleSubmitPublicationBean(simplePubBean);

		AccessControlInfo theAccess = publication.getTheAccess();
		publicationService.removeAccessibility(theAccess, (Publication) publication.getDomainFile());
		this.setAccesses(request, publication);
		request.setAttribute("publicationId", publication.getDomainFile().getId().toString());

		//check if sampleId exists in the form
		if (simplePubBean.getSampleId()!=null) {
			request.setAttribute("sampleId", simplePubBean.getSampleId());
		}
		//	setupUpdate(form, request, response);
		return setupUpdate(publication.getDomainFile().getId().toString(),simplePubBean.getSampleId().toString(), request);
	}

	protected void removePublicAccess(PublicationBean publication, HttpServletRequest request) throws Exception
	{
		springSecurityAclService.retractObjectFromPublic(publication.getDomainFile().getId(), SecureClassesEnum.PUBLICATION.getClazz());
		//	PublicationBean publication = (PublicationBean) theForm.getPublicationBean();
	}

	public String download(String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		System.out.println("Inside download from publication BO");
		return downloadFile(publicationService, fileId, request, response);
	}

	public String[] getMatchedSampleNames(String searchStr, HttpServletRequest request)
	{
		if (!SpringSecurityUtil.isUserLoggedIn()) {
			return null;
		}
		
		try
		{
			List<String> sampleNames = sampleService.findSampleNamesBy(searchStr);
			Collections.sort(sampleNames, new Comparators.SortableNameComparator());
			return sampleNames.toArray(new String[sampleNames.size()]);
		} catch (Exception e) {
			logger.error("Problem getting all sample names for publication submission \n", e);
			return new String[] { "" };
		}
	}

	/**
	 * Delete a publication from MyWorkspace
	 * 
	 * @param mapping
	 * @param publicationId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<String> deletePublicationById(String publicationId, HttpServletRequest request) throws Exception
	{
		List<String> msgs = new ArrayList<String>();
		PublicationBean publicationBean = publicationService.findPublicationById(publicationId, true);

		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				publicationBean.getDomainFile().getId().toString(),
				publicationBean.getDomainFile().getTitle(), "publication");
		publicationService.deletePublication((Publication) publicationBean.getDomainFile());

		msgs.add("success");
		return msgs;
	}

	@Override
	public CurationService getCurationServiceDAO() {
		return curationServiceDAO;
	}

	@Override
	public SampleService getSampleService() {
		return sampleService;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

	@Override
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
}

