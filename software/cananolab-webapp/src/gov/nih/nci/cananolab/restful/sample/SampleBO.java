/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.restful.view.edit.SampleEditGeneralBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleAccessBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleAddressBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleOrganizationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimplePointOfContactBean;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.SampleForm;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

public class SampleBO extends BaseAnnotationBO {
	
	private static Logger logger = Logger.getLogger(SampleBO.class);

	private DataAvailabilityService dataAvailabilityService;
	
	/**
	 * This is actually an update. Given the current implementation, when it gets here, a saveAccess or savePOC has been called and
	 * the sample's id has been generated. (In the new sample submission form, if you only enter a sample name, the "Submit" button
	 * is grayed out.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean create(SampleEditGeneralBean simpleEditBean,
			HttpServletRequest request)
			throws Exception {
		
		String sampleId = String.valueOf(simpleEditBean.getSampleId());
		Boolean newSample = true;
		if (sampleId == null || sampleId.length() == 0) {
			newSample = false;
		}
		
		SampleBean sampleBean = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		
		if (sampleBean == null) {
			return wrapErrorInEditBean("No valid sample in session matching given sample id. Unable to update the sample.");
		}
		
		////////////// transfer keyword and sample name from simple Edit bean
		sampleBean.getDomain().setName(simpleEditBean.getSampleName());
		
		//When saving keywords, current implementation is to replace the whole set
		//ref. SampleServiceLocalImpl.saveSample()
		List<String> keywords = simpleEditBean.getKeywords();
		if (keywords != null) {
			Collection<Keyword> keywordColl = new HashSet<Keyword>();
			for (String keyword : keywords) {
				Keyword kw = new Keyword();
				kw.setName(keyword);
				keywordColl.add(kw);
			}
			
			sampleBean.getDomain().setKeywordCollection(keywordColl);
		}
		
		//////////////////////////////////
		
		setServiceInSession(request);
		saveSample(request, sampleBean);
		
		// retract from public if updating an existing public record and not
		// curator
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		if (!newSample && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(sampleId, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			
				SampleEditGeneralBean simpleBean = new SampleEditGeneralBean();
				simpleBean.getErrors().add(PropertyUtil.getProperty("sample", "message.updateSample.retractFromPublic"));
				return simpleBean;
		}
		request.getSession().setAttribute("updateSample", "true");
//		request.setAttribute("theSample", sampleBean);
//		request.setAttribute("sampleId", sampleBean.getDomain().getId()
//				.toString());
	
		return summaryEdit(String.valueOf(sampleBean.getDomain().getId()), request);
	}
	
	

	private void saveSample(HttpServletRequest request, SampleBean sampleBean)
			throws Exception {
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		sampleBean.setupDomain(user.getLoginName());
		// persist in the database
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		service.saveSample(sampleBean);

	//	ActionMessages messages = new ActionMessages();
	//	ActionMessage msg = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (!StringUtils.isEmpty(updateSample)) {
	//		msg = new ActionMessage("message.updateSample");
	//		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
	//		saveMessages(request, messages);
		}
	}

	/**
	 * Handle view sample request on sample search result page (read-only view).
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public SimpleSampleBean summaryView(String sampleId,
			HttpServletRequest request)
			throws Exception {
		
		this.setServiceInSession(request);
		SampleForm form = new SampleForm();
		form.setSampleId(sampleId);
		
		SimpleSampleBean simpleBean = new SimpleSampleBean();
		
		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(form, request);
		if (hasNullPOC(request, sampleBean, simpleBean.getErrors())) {
			return simpleBean;
		}
		form.setSampleBean(sampleBean);		
		
		simpleBean.transferSampleBeanForSummaryView(sampleBean);
		return simpleBean;
	}

	public String input(SampleForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		checkOpenForms(form, request);
		String browserDispatch = getBrowserDispatch(request);
		// from cloning form
		if (browserDispatch.equals("clone")) {
	//		return mapping.findForward("cloneInput");
			return null;
		} else {
			String updateSample = (String) request.getSession().getAttribute(
					"updateSample");
			if (updateSample == null) {				
	//			return mapping.findForward("createInput");
				return null;
			} else {
	//			return mapping.findForward("summaryEdit");
				return null;
			}
		}
	}

	private void checkOpenForms(SampleForm theForm,
			HttpServletRequest request) throws Exception {
		String dispatch = request.getParameter("dispatch");
		String browserDispatch = getBrowserDispatch(request);
		
		HttpSession session = request.getSession();
		Boolean openPOC = false;
		if (dispatch.equals("input")
				&& browserDispatch.equals("savePointOfContact")) {
			openPOC = true;
		}
		session.setAttribute("openPOC", openPOC);
		super.checkOpenAccessForm(request);
	}

	public SampleBean setupView(SampleForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServiceInSession(request);
		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(form, request);
		form.setSampleBean(sampleBean);
		return sampleBean;
	}

	/**
	 * 
	 * @param request
	 * @param sampleBean
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	private Boolean hasNullPOC(HttpServletRequest request, SampleBean sampleBean, List<String> errors)
			throws Exception {
		
		//What's the buss logic here?
		
		if (sampleBean.getPrimaryPOCBean().getDomain() == null) {
			SampleService service = setServiceInSession(request);
			service.deleteSample(sampleBean.getDomain().getName());
	
			if (sampleBean.getPrimaryPOCBean().getDomain() == null) {
				errors.add(PropertyUtil.getProperty("sample", "message.sample.null.POC.delete"));
			} else
				errors.add("Sample invalid");
			return true;
		}
		
		return false;
	}

	/**
	 * Handle edit sample request on sample search result page (curator view).
	 * 
	 * After a savePOC, saveAccess or updateDataAvailability operation, this method will 
	 * be called again to retrieve the updated sample data
	 * 
	 * 
	 * @param sampleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean summaryEdit(String sampleId, HttpServletRequest request)
			throws Exception {
	
		SampleEditGeneralBean sampleEdit = new SampleEditGeneralBean();
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null) {
			sampleEdit.getErrors().add("User session invalidate. Session may have been expired");
			return sampleEdit;
		}
	
		this.setServiceInSession(request);

		// "setupSample()" will retrieve and return the SampleBean.
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		
		SampleBean sampleBean = setupSampleById(sampleId, request);
		
		if (hasNullPOC(request, sampleBean, sampleEdit.getErrors())) {
			return sampleEdit;
		}
		
		Set<DataAvailabilityBean> selectedSampleDataAvailability = dataAvailabilityService
				.findDataAvailabilityBySampleId(sampleBean.getDomain().getId()
						.toString(), securityService);

		String[] availableEntityNames = null;
		if (selectedSampleDataAvailability != null
				&& !selectedSampleDataAvailability.isEmpty()
				&& selectedSampleDataAvailability.size() > 0) {
			sampleBean.setHasDataAvailability(true);
			sampleBean.setDataAvailability(selectedSampleDataAvailability);
			calculateDataAvailabilityScore(sampleBean,
					selectedSampleDataAvailability, request);
			int idx = 0;
			availableEntityNames = new String[selectedSampleDataAvailability.size()];
			for (DataAvailabilityBean bean : selectedSampleDataAvailability) {
				availableEntityNames[idx++] = bean.getAvailableEntityName()
						.toLowerCase();
			}
		}

		sampleEdit.transferSampleBeanData(request, this.getCurationService(), sampleBean, availableEntityNames);
		
		request.getSession().setAttribute("updateSample", "true");
		
		//need to save sampleBean in session for other edit feature.
		//new in rest implement
		request.getSession().setAttribute("theSample", sampleBean);
		return sampleEdit;
	}
	
	

	private void setAccesses(HttpServletRequest request, SampleBean sampleBean)
			throws Exception {
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		List<AccessibilityBean> groupAccesses = service
				.findGroupAccessibilities(sampleBean.getDomain().getId()
						.toString());
		List<AccessibilityBean> userAccesses = service
				.findUserAccessibilities(sampleBean.getDomain().getId()
						.toString());
		sampleBean.setUserAccesses(userAccesses);
		sampleBean.setGroupAccesses(groupAccesses);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		sampleBean.setUser(user);
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void setupNew(SampleForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("sampleForm");
		request.getSession().removeAttribute("updateSample");
		setupLookups(request);
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		checkOpenForms(form, request);
	//	saveToken(request);
	//	return mapping.findForward("createInput");
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void setupClone(SampleForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) form.getSampleBean();
		if (request.getParameter("cloningSample") != null) {
			String cloningSampleName = request.getParameter("cloningSample");
			sampleBean.setCloningSampleName(cloningSampleName);
			sampleBean.getDomain().setName(null);
		} else {
			sampleBean.setCloningSampleName(null);
			sampleBean.getDomain().setName(null);
		}
	//	saveToken(request);
	//	return mapping.findForward("cloneInput");
	}

	/**
	 * Retrieve all POCs and Groups for POC drop-down on sample edit page.
	 * 
	 * @param request
	 * @param sampleOrg
	 * @throws Exception
	 */
	private void setupLookups(HttpServletRequest request) throws Exception {
		InitSampleSetup.getInstance().setPOCDropdowns(request);
	}
	
	/**
	 * Save a new or existing POC with updates.
	 * For Rest call: 1. when add POC and save are clicked
	 * 				  2. when edit POC and save are clicked
	 * 
	 * @param simplePOC
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean savePointOfContact(SimplePointOfContactBean simplePOC, HttpServletRequest request) 
			throws Exception {

		List<String> errors = validatePointOfContactInput(simplePOC);
		if (errors.size() > 0) {
			SampleEditGeneralBean errorBean = new SampleEditGeneralBean();
			errorBean.setErrors(errors);
			return errorBean;
		}
		
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		SampleBean sample = (SampleBean)request.getSession().getAttribute("theSample");
		
		PointOfContactBean thePOC = getPointOfContactBeanFromInput(simplePOC, user.getLoginName());
		
		if (sample == null) {			
			//for real
			throw new Exception("Sample object is not valid");
		}
		
		Long oldPOCId = thePOC.getDomain().getId();
		// set up one sampleService
		SampleService service = setServiceInSession(request);
		// have to save POC separately because the same organizations can not be
		// saved in the same session
		service.savePointOfContact(thePOC);
		sample.addPointOfContact(thePOC, oldPOCId);

		// if the oldPOCId is different from the one after POC save
		if (oldPOCId != null && !oldPOCId.equals(thePOC.getDomain().getId())) {
			// update characterization POC associations
			((SampleServiceLocalImpl) service)
					.updatePOCAssociatedWithCharacterizations(sample
							.getDomain().getName(), oldPOCId, thePOC
							.getDomain().getId());
		}
		// save sample
		saveSample(request, sample);		
		
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
		//	forward = mapping.findForward("createInput");
			setupLookups(request);
			this.setAccesses(request, sample);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
		//	forward = summaryEdit(mapping, form, request, response);
		}
		
		//TODO: check on this
		InitSampleSetup.getInstance().persistPOCDropdowns(request, sample);
		// return forward;
		
		return summaryEdit(sample.getDomain().getId()
					.toString(), request);
	}

	public void removePointOfContact(SampleForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!validateToken(request)) {
		//	return mapping.findForward("sampleMessage");
		}
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) form.getSampleBean();
		PointOfContactBean thePOC = sample.getThePOC();
//		ActionMessages msgs = new ActionMessages();
		if (thePOC.getPrimaryStatus()) {
		//	ActionMessage msg = new ActionMessage("message.deletePrimaryPOC");
		//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		//	saveMessages(request, msgs);
		}
		sample.removePointOfContact(thePOC);
		// save sample
		setServiceInSession(request);
		saveSample(request, sample);
	//	ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
	//		forward = mapping.findForward("createInput");
			setupLookups(request);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
	//		forward = summaryEdit(mapping, form, request, response);
		}
	//	return forward;
	}

	public SampleEditGeneralBean clone(SampleEditGeneralBean simpleEditBean,
			HttpServletRequest request)
			throws Exception {
		
		String sampleId = String.valueOf(simpleEditBean.getSampleId());
		String cloningSampleName = simpleEditBean.getCloningSampleName();
		SampleBean sampleBean = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		sampleBean.setCloningSampleName(cloningSampleName);
		
		SampleBean clonedSampleBean = null;
		SampleService service = this.setServiceInSession(request);
		
		try {
			clonedSampleBean = service.cloneSample(
					sampleBean.getCloningSampleName(), sampleBean.getDomain()
							.getName().trim());
		} catch (NotExistException e) {
			String error =  PropertyUtil.getPropertyReplacingToken("sample", "error.cloneSample.noOriginalSample", 
					"0", sampleBean.getCloningSampleName());
			return wrapErrorInEditBean(error);
		} catch (DuplicateEntriesException e) {
			String error =  PropertyUtil.getProperty("sample", "error.cloneSample.duplicateSample");
			return wrapErrorInEditBean(error);
		} catch (SampleException e) {
			String error =  PropertyUtil.getProperty("sample", "error.cloneSample");
			return wrapErrorInEditBean(error);
		}

	
		request.setAttribute("sampleId", clonedSampleBean.getDomain().getId()
				.toString());
		
		//After cloning, all we need is a success message to return to web page
		
		SampleEditGeneralBean sampleEdit = new SampleEditGeneralBean();
		//sampleEdit.transferSampleBeanData(request, this.getCurationService(), sampleBean, availableEntityNames);
		
		return sampleEdit;
	}

	public void delete(SampleForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!validateToken(request)) {
		//	return mapping.findForward("sampleMessage");
		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) form.getSampleBean();
		SampleService service = this.setServiceInSession(request);
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		// remove all access associated with sample takes too long. Set up the
		// delete job in scheduler
		InitSampleSetup.getInstance().updateCSMCleanupEntriesInContext(
				sampleBean.getDomain(), request);

		// update data review status to "DELETED"
		updateReviewStatusTo(DataReviewStatusBean.DELETED_STATUS, request,
				sampleBean.getDomain().getId().toString(), sampleBean
						.getDomain().getName(), "sample");
		if (sampleBean.getHasDataAvailability()) {
			dataAvailabilityService.deleteDataAvailability(sampleBean
					.getDomain().getId().toString(), securityService);
		}
		service.deleteSample(sampleBean.getDomain().getName());

	//	ActionMessages msgs = new ActionMessages();
	//	ActionMessage msg = new ActionMessage("message.deleteSample",
	//			sampleBean.getDomain().getName());
	//	msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
	//	saveMessages(request, msgs);
		sampleBean = new SampleBean();
	//	ActionForward forward = mapping.findForward("sampleMessage");
	//	resetToken(request);
	//	return forward;
	}

	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}

	/**
	 * generate data availability for the sample
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void generateDataAvailability(SampleForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!validateToken(request)) {
		//	return mapping.findForward("sampleMessage");
		}
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) form.getSampleBean();

		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.saveDataAvailability(sampleBean, securityService);
		sampleBean.setDataAvailability(dataAvailability);
		sampleBean.setHasDataAvailability(true);
		calculateDataAvailabilityScore(sampleBean, dataAvailability, request);

		/*
		 * Map<String, List<DataAvailabilityBean>> dataAvailabilityMapPerPage =
		 * (Map<String, List<DataAvailabilityBean>>) request
		 * .getSession().getAttribute("dataAvailabilityMapPerPage");
		 * 
		 * if (dataAvailabilityMapPerPage != null) {
		 * dataAvailabilityMapPerPage.remove(sampleBean.getDomain().getId()
		 * .toString());
		 * dataAvailabilityMapPerPage.put(sampleBean.getDomain().getId()
		 * .toString(), dataAvailability);
		 * 
		 * request.getSession().setAttribute("dataAvailabilityMapPerPage",
		 * dataAvailabilityMapPerPage); }
		 */
		request.setAttribute("onloadJavascript", "manageDataAvailability('"
				+ sampleBean.getDomain().getId()
				+ "', 'sample', 'dataAvailabilityView')");
	//		return mapping.findForward("summaryEdit");
	}

	/**
	 * update data availability for the sample. This is to support the "Regenerate" button
	 * on Sample Edit page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean updateDataAvailability(String sampleId, HttpServletRequest request) 
			throws Exception {
		
		SampleBean sampleBean = findMatchSampleInSession(request, sampleId);
		if (sampleBean == null) {
			SampleEditGeneralBean simpleBean = new SampleEditGeneralBean();
			simpleBean.getErrors().add("No valid sample in session matching given sample id. Unable to update data availabilty.");
			return simpleBean;
		}
		
	
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.saveDataAvailability(sampleBean, securityService);
		sampleBean.setDataAvailability(dataAvailability);
		// recalculate the score
		calculateDataAvailabilityScore(sampleBean, dataAvailability, request);
		return this.summaryEdit(sampleId, request);
	}

	/**
	 * delete data availability for the sample
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void deleteDataAvailability(SampleForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (!validateToken(request)) {
		//	return mapping.findForward("sampleMessage");
		}
//		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) form.getSampleBean();
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		dataAvailabilityService.deleteDataAvailability(sampleBean.getDomain()
				.getId().toString(), securityService);
		sampleBean.setHasDataAvailability(false);
		sampleBean.setDataAvailability(new HashSet<DataAvailabilityBean>());
//		return mapping.findForward("summaryEdit");
	}

	/**
	 * Support viewDataAvailability rest service
	 * 
	 * @param sampleId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SimpleSampleBean dataAvailabilityView(String sampleId, HttpServletRequest request) throws Exception {

		//Make sure service has been created
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		if (service == null) 
			service = setServiceInSession(request);
		
		SampleBean sampleBean = setupSampleById(sampleId, request);
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		
		Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.findDataAvailabilityBySampleId(sampleBean.getDomain().getId()
						.toString(), securityService);

		//sampleBean.setDataAvailability(dataAvailability);
		String[] availEntityNames = null;
		if (!dataAvailability.isEmpty() && dataAvailability.size() > 0) {
			sampleBean.setHasDataAvailability(true);
			calculateDataAvailabilityScore(sampleBean, dataAvailability, request);
			availEntityNames = new String[dataAvailability.size()];
			int i = 0;
			for (DataAvailabilityBean bean : dataAvailability) {
				availEntityNames[i++] = bean.getAvailableEntityName()
						.toLowerCase();
			}
			
			//setAvailableEntityNames(availEntityNames);
			//request.setAttribute("availableEntityNames", availableEntityNames);
		}
		request.setAttribute("sampleBean", sampleBean);
		
		String styleId = request.getParameter("styleId");
		if (styleId != null) {
	//		return mapping.findForward("dataAvailabilityView");
		} else {
	//		return mapping.findForward("dataAvailabilityEdit");
		}
		
		SimpleSampleBean simpleBean = transferDataAvailabilityToSimpleSampleBean(sampleBean, request, availEntityNames);
		return simpleBean;
	}
	
	protected SimpleSampleBean transferDataAvailabilityToSimpleSampleBean(SampleBean sampleBean, 
			HttpServletRequest request, String[] availEntityNames) {
		SimpleSampleBean simpleBean = new SimpleSampleBean();
		
		simpleBean.transferSampleBeanForDataAvailability(sampleBean, request);
		simpleBean.setAvailableEntityNames(availEntityNames);
		
		return simpleBean;
	}

	private void calculateDataAvailabilityScore(SampleBean sampleBean,
			Set<DataAvailabilityBean> dataAvailability, HttpServletRequest request) {

		ServletContext appContext = request.getSession().getServletContext();
		
		SortedSet<String> minchar = (SortedSet<String>) appContext
				.getAttribute("MINChar");
		Map<String, String> attributes = (Map<String, String>) appContext
				.getAttribute("caNano2MINChar");
		sampleBean.calculateDataAvailabilityScore(dataAvailability, minchar,
				attributes);   
	}

	/*
	 * public ActionForward manageDataAvailability(ActionMapping mapping,
	 * ActionForm form, HttpServletRequest request, HttpServletResponse
	 * response) throws Exception {
	 * 
	 * DynaValidatorForm theForm = (DynaValidatorForm) form; SampleBean
	 * sampleBean = setupSample(theForm, request); SecurityService
	 * securityService = (SecurityService) request
	 * .getSession().getAttribute("securityService");
	 * 
	 * List<DataAvailabilityBean> dataAvailability = dataAvailabilityService
	 * .findDataAvailabilityBySampleId(sampleBean.getDomain().getId()
	 * .toString(), securityService);
	 * 
	 * sampleBean.setDataAvailability(dataAvailability); if
	 * (!dataAvailability.isEmpty() && dataAvailability.size() > 0) {
	 * sampleBean.setHasDataAvailability(true); } return
	 * mapping.findForward("summaryEdit"); }
	 */

	public SampleEditGeneralBean saveAccess(SimpleAccessBean simpleAccess, HttpServletRequest request)
			throws Exception {
		if (!validateToken(request)) {
		//	return mapping.findForward("sampleMessage");
		}
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		//SampleBean sample = (SampleBean) form.getSampleBean();
		
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		SampleBean sample = (SampleBean)request.getSession().getAttribute("theSample");
		if (sample == null) {
			
			//////////debug
//			String sampleId = "24063238"; //ncl-49
//			sample = summaryView(sampleId, request);
//			request.getSession().setAttribute("theSample", sample);
			////////// debug
			
			//for real
			throw new Exception("Sample object is not valid");
		}
		
		AccessibilityBean theAccess = sample.getTheAccess();
		populateAccessBeanWithInput(simpleAccess, theAccess, user.getLoginName());
	
		List<String> errors = validateAccess(request, theAccess);
		if (errors.size() > 0) {
			SampleEditGeneralBean errorBean = new SampleEditGeneralBean();
			errorBean.setErrors(errors);
			return errorBean;
		}
		SampleService service = this.setServiceInSession(request);
		// if sample is public, the access is not public, retract public
		// privilege would be handled in the service method
		service.assignAccessibility(theAccess, sample.getDomain());
		// update status to retracted if the access is not public and sample is
		// public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)
				&& sample.getPublicStatus()) {
			updateReviewStatusTo(DataReviewStatusBean.RETRACTED_STATUS,
					request, sample.getDomain().getId().toString(), sample
							.getDomain().getName(), "sample");
		}
		// if access is public, pending review status, update review
		// status to public
		if (theAccess.getGroupName().equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
			this.switchPendingReviewToPublic(request, sample.getDomain()
					.getId().toString());
		}

	//	ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
	//		forward = mapping.findForward("createInput");
			setupLookups(request);
			this.setAccesses(request, sample);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
	//		forward = summaryEdit(mapping, form, request, response);
		}
		return summaryEdit(sample.getDomain().getId()
				.toString(), request);
	}

	public void deleteAccess(SampleForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!validateToken(request)) {
		//	return mapping.findForward("sampleMessage");
		}
	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) form.getSampleBean();
		AccessibilityBean theAccess = sample.getTheAccess();
		SampleService service = this.setServiceInSession(request);
		service.removeAccessibility(theAccess, sample.getDomain());

//		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
	//		forward = mapping.findForward("createInput");
			setupLookups(request);
			this.setAccesses(request, sample);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
	//		forward = summaryEdit(mapping, form, request, response);
		}
//		return forward;
	}

	protected void removePublicAccess(SampleForm theForm,
			HttpServletRequest request) throws Exception {
		SampleBean sample = (SampleBean) theForm.getSampleBean();
		SampleService service = this.setServiceInSession(request);
		service.removeAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS,
				sample.getDomain());
	}

	// creates a new sample service and put it in the session
	// a new service needs to be created when there had been updates to the data
	// that lead to changes in accessibleData
	private SampleService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return sampleService;
	}

	public Boolean canUserExecutePrivateLink(UserBean user, String protectedData)
			throws SecurityException {
		return false;
	}
	
	protected List<String> validatePointOfContactInput(SimplePointOfContactBean simplePOC) {
		
		/*
		 * Only org name is required
		 * */
		List<String> errors = new ArrayList<String>();
		
		if (simplePOC == null) {
			errors.add("Input point of contact object invalid");
			return errors;
		}
		
		SimpleOrganizationBean simpleOrg = simplePOC.getOrganization();
		if (simpleOrg != null) {
			String orgName = simpleOrg.getName();
			if (orgName == null || !InputValidationUtil.isTextFieldWhiteList(orgName))
				errors.add(PropertyUtil.getProperty("sample", "organization.name.invalid"));
		} else
			errors.add("Organization Name is a required field");
		
		SimpleAddressBean addrBean = simplePOC.getAddress();
		if (addrBean != null) {
			if (!InputValidationUtil.isTextFieldWhiteList(addrBean.getLine1()))
				errors.add(PropertyUtil.getProperty("sample", "organization.address1.invalid"));
			if (!InputValidationUtil.isTextFieldWhiteList(addrBean.getLine2()))
				errors.add(PropertyUtil.getProperty("sample", "organization.address2.invalid"));
			
			if (!InputValidationUtil.isRelaxedAlphabetic(addrBean.getCity()))
				errors.add(PropertyUtil.getProperty("sample", "organization.city.invalid"));
			
			if (!InputValidationUtil.isRelaxedAlphabetic(addrBean.getStateProvince()))
				errors.add(PropertyUtil.getProperty("sample", "organization.state.invalid"));
			
			if (!InputValidationUtil.isRelaxedAlphabetic(addrBean.getCountry()))
				errors.add(PropertyUtil.getProperty("sample", "organization.country.invalid"));
			
			if (addrBean.getZip().length() > 0 && !InputValidationUtil.isZipValid(addrBean.getZip()))
				errors.add(PropertyUtil.getProperty("sample", "postalCode.invalid"));
		}
		
		return errors;
	}
	
	protected PointOfContactBean getPointOfContactBeanFromInput(SimplePointOfContactBean simplePOC, String createdBy) {
		//TODO: only id can be null. Other fields need to be set "" if no value
		
		PointOfContactBean pocBean = new PointOfContactBean();
		pocBean.setupDomain(createdBy);
		
		Organization org = new Organization();
		SimpleOrganizationBean simpleOrg = simplePOC.getOrganization();
		
		org.setName(simpleOrg.getName());
		if (simpleOrg.getId() > 0)
			org.setId(simpleOrg.getId());
		
		SimpleAddressBean addrBean = simplePOC.getAddress();
		if (addrBean == null) {
			addrBean = new SimpleAddressBean();
		}
			org.setCity(addrBean.getCity());
			org.setCountry(addrBean.getCountry());
			org.setPostalCode(addrBean.getZip());
			org.setStreetAddress1(addrBean.getLine1());
			org.setStreetAddress2(addrBean.getLine2());
			org.setState(addrBean.getStateProvince());
		
		pocBean.getDomain().setOrganization(org);
		pocBean.getDomain().setRole(simplePOC.getRole());
		
		if (simplePOC.getId() > 0)
			pocBean.getDomain().setId(simplePOC.getId());
		pocBean.setupDomain(createdBy);
		
		pocBean.getDomain().setFirstName(simplePOC.getFirstName());
		pocBean.getDomain().setLastName(simplePOC.getLastName());
		pocBean.getDomain().setMiddleInitial(simplePOC.getMiddleInitial());
		
		pocBean.getDomain().setPhone(simplePOC.getPhoneNumber());
		pocBean.getDomain().setEmail(simplePOC.getEmail());
		
		return pocBean;
	}
	
	protected void populateAccessBeanWithInput(SimpleAccessBean simpleAccess, AccessibilityBean theAccess, String loginName) {
//		simpleAccess.getAccessBy();
//		simpleAccess.getAccessRight();
//		simpleAccess.getGroupName();
//		simpleAccess.getLoginName();
//		simpleAccess.getRoleDisplayName();
//		simpleAccess.getSampleId();
		
		String accessBy = simpleAccess.getAccessBy();
		theAccess.setAccessBy(accessBy);
		if (accessBy.equals(AccessibilityBean.ACCESS_BY_GROUP))
			theAccess.setGroupName(simpleAccess.getGroupName());
		else if (accessBy.equals(AccessibilityBean.ACCESS_BY_USER)) {
			String selectedLoginName = simpleAccess.getLoginName();
			if (selectedLoginName != null && selectedLoginName.length() > 0) {
				UserBean user = new UserBean();
				user.setLoginName(selectedLoginName);
				theAccess.setUserBean(user);
			}
		}
		
		theAccess.setRoleName(simpleAccess.getRoleName());
	}
	
	protected SampleBean findMatchSampleInSession(HttpServletRequest request, String sampleId) {
		if (sampleId == null || sampleId.length() == 0) {
			logger.error("Input sample id is null or empty");
			return null;
		}
		
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute("theSample");
		if (sampleBean == null) {
			logger.error("No sample in session");
			return null;
		}
		
		if (Long.valueOf(sampleId).longValue() != sampleBean.getDomain().getId().longValue()) {
			logger.error("The given sample id doesn't match the sample id in session");
			return null;
		}
		
		return sampleBean;
			 
	}
	
	protected SampleEditGeneralBean wrapErrorInEditBean(String error) {
		SampleEditGeneralBean simpleBean = new SampleEditGeneralBean();
		simpleBean.getErrors().add(error);
		return simpleBean;
	}
}
