/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.sample.InitSampleSetup;
import gov.nih.nci.cananolab.restful.util.InputValidationUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.validator.RestValidator;
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
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Class migrated from SampleAction, to support sample related rest services.
 * 
 * @author yangs8
 *
 */
public class SampleBO extends BaseAnnotationBO {
	
	private static Logger logger = Logger.getLogger(SampleBO.class);

	private DataAvailabilityService dataAvailabilityService;
	
	/**
	 * 
	 * Method to support the Update button on Update Sample page. Given the current implementation, when it gets here, 
	 * a saveAccess or savePOC has been called and the sample's id has been generated. 
	 * <br><br>
	 * Revisit if the above workflow changes.
	 * 
	 * @param simpleEditBean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean update(SampleEditGeneralBean simpleEditBean, HttpServletRequest request)
			throws Exception {
		
		long sampleId = simpleEditBean.getSampleId();
		if (sampleId <= 0)
			throw new Exception("No valid sample id found. Unable to update sample");
		
		SampleBean sampleBean = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		
		if (sampleBean == null) {
			System.out.println("No Sample in session");
			return wrapErrorInEditBean("No valid sample in session matching given sample id. Unable to update the sample.");
		}
		
		// transfer keyword and sample name from simple Edit bean
		simpleEditBean.populateDataForSavingSample(sampleBean);
		
		setServiceInSession(request);
		saveSample(request, sampleBean);
		
		// retract from public if updating an existing public record and not curator
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		if (!user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(String.valueOf(simpleEditBean.getSampleId()), request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			
			return wrapErrorInEditBean(PropertyUtil.getProperty("sample", "message.updateSample.retractFromPublic"));
			
		}
		
		//request.getSession().setAttribute("updateSample", "true");
	
		return summaryEdit(String.valueOf(sampleBean.getDomain().getId()), request);
	}
	
	/**
	 * This is to support the "Submit" button in new sample submission page.
	 * <br><br>
	 * 2 scenarios when we get here: 1) new sample has been created successfully when saving a primary POC 
	 * from new sample submission page, in such case, the sample in session has an id. 2) new sample failed to be
	 * created when saving a primary POC from new sample submission page. In such case, the sample in session
	 * doesn't have an id.
	 * 
	 * 
	 * @param simpleEditBean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean submit(SampleEditGeneralBean simpleEditBean, HttpServletRequest request)
			throws Exception {
		
		long sampleId = simpleEditBean.getSampleId(); //String.valueOf();
		Boolean newSample = (sampleId <= 0);
		
		SampleBean sampleBean = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		
		if (sampleBean == null) {
			System.out.println("No Sample in session");
			return wrapErrorInEditBean("No valid sample in session matching given sample id. Unable to update the sample.");
		}
		
		// transfer keyword and sample name from simple Edit bean
		simpleEditBean.populateDataForSavingSample(sampleBean);
		
		setServiceInSession(request);
		saveSample(request, sampleBean);		
		
		request.getSession().setAttribute("updateSample", "true");
	
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
		//SampleBean sampleBean = setupSample(form, request);
		
		SampleBean sampleBean = setupSampleById(sampleId, request);
		
		if (hasNullPOC(request, sampleBean, simpleBean.getErrors())) {
			return simpleBean;
		}
		form.setSampleBean(sampleBean);		
		
		request.getSession().setAttribute("theSample", sampleBean);
		simpleBean.transferSampleBeanForSummaryView(sampleBean);
		return simpleBean;
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

//	public SampleBean setupView(SampleForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//	//	DynaValidatorForm theForm = (DynaValidatorForm) form;
//		this.setServiceInSession(request);
//		// "setupSample()" will retrieve and return the SampleBean.
//		SampleBean sampleBean = setupSample(form, request);
//		form.setSampleBean(sampleBean);
//		return sampleBean;
//	}

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
		this.setServiceInSession(request);

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
		
		//Set collaboration group names in session for later use. The list should never change
		//unless a curator added new group
		if (request.getSession().getAttribute("allGroupNames") == null) {
			SampleService sampleService = (SampleService) request.getSession().getAttribute("sampleService");
			List<String> availGroupNames = sampleService.findGroupNames("");
			request.getSession().setAttribute("allGroupNames", availGroupNames);
		}

		sampleEdit.transferSampleBeanData(request, this.getCurationService(), sampleBean, availableEntityNames);
		
		//request.getSession().setAttribute("updateSample", "true");
		
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
	public SampleEditGeneralBean setupNew( HttpServletRequest request)
			throws Exception {
		SampleEditGeneralBean sampleEdit = new SampleEditGeneralBean();
		
		this.setServiceInSession(request);
		
		request.getSession().removeAttribute("theSample");

		sampleEdit.setupLookups(request);
	
		return sampleEdit;
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
	 * Get the POC list from simpleSampleBean, find the dirty one to do an add/update.
	 * 
	 * @param simpleSampleBean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean savePointOfContactList(SampleEditGeneralBean simpleSampleBean, 
			HttpServletRequest request) 
			throws Exception {
		
		List<SimplePointOfContactBean> pocList = simpleSampleBean.getPointOfContacts();
		if (pocList == null || pocList.size() == 0) 
			return this.wrapErrorInEditBean("POC list is empty. Unable to update POC");
		
		SimplePointOfContactBean thePOC = findDirtyPOC(pocList);
		if (thePOC == null)
			return this.wrapErrorInEditBean("Unable to find the dirty POC to update");
		
		return savePointOfContact(simpleSampleBean, thePOC, request);
	}
	
	/**
	 * Find the "dirty" SimplePointOfContactBean from a list
	 * @param pocList
	 * @return
	 */
	protected SimplePointOfContactBean findDirtyPOC(List<SimplePointOfContactBean> pocList) {
		if (pocList == null)
			return null;
		
		for (SimplePointOfContactBean poc : pocList) {
			if (poc.isDirty())
				return poc;
		}
		
		return null;
	}
	
	/**
	 * Find the "dirty" SimpleAccessBean from a list
	 * @param accessList
	 * @return
	 */
	protected SimpleAccessBean findDirtyAccess(Map<String, List<SimpleAccessBean>> accessMap) {
		if (accessMap == null)
			return null;
			
		Iterator<String> ite = accessMap.keySet().iterator();
		while (ite.hasNext()) {
			List<SimpleAccessBean> accesses = accessMap.get(ite.next());
			for (SimpleAccessBean access : accesses) {
				if (access.isDirty()) 
					return access;
			}
		}
		
		return null;
	}
	
	protected void removeMatchingPOC(SampleBean sample, SimplePointOfContactBean simplePOC) {
		List<PointOfContactBean> otherPOCs = sample.getOtherPOCBeans();
		if (otherPOCs != null) {
			for (PointOfContactBean poc : otherPOCs) {
				if (poc.getDomain().getId() == simplePOC.getId()) {
					logger.debug("Removing poc " + poc.getDisplayName() + " from sample " + sample.getDomain().getName());
					sample.removePointOfContact(poc);
					logger.debug("POC removed");
					break;
				}
			}
		}
	
	}
	
	
	
	/**
	 * Save a new or existing POC with updates.
	 * 
	 * For Rest call: 1. Update Sample: when add POC and save are clicked
	 * 				  2. Update Sample: when edit POC and save are clicked
	 * 				  3. Submit Sample: when add POC and save are clicked. In this case, sample only has a name.
	 * 
	 * Updating an existing POC with a new organization name is the equivalent of creating new and deleting old
	 * 
	 * @param simplePOC
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean savePointOfContact(SampleEditGeneralBean simpleSampleBean,
			SimplePointOfContactBean simplePOC, HttpServletRequest request) 
			throws Exception {

		logger.debug("========== Start saving POC");
		List<String> errors = validatePointOfContactInput(simplePOC);
		if (errors.size() > 0) {
			return wrapErrorsInEditBean(errors, "POC");
		}
		
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		SampleBean sample = (SampleBean)request.getSession().getAttribute("theSample");
		
		long sampleId = simpleSampleBean.getSampleId();
		String sampleName = simpleSampleBean.getSampleName();
		
		boolean newSample = false;
		if (sample == null) { 
			if (sampleName == null || sampleName.length() == 0) 
				return this.wrapErrorInEditBean("Sample object in session is not valid for sample update operation");
			else { //add poc in submit new sample workflow
				sample = new SampleBean();
				sample.getDomain().setName(sampleName);
				newSample = true;
				
			}
		} else if (sampleId <= 0) {
			sample.getDomain().setName(sampleName);
			newSample = true;
		} else {
			if (sample.getDomain().getId() != sampleId) 
				return this.wrapErrorInEditBean("Current sample id doesn't match sample id in session");
		}
		
		logger.debug("========== Resolving Input");
		PointOfContactBean thePOC = resolveThePOCToSaveFromInput(sample, simplePOC, user.getLoginName());
		Long oldPOCId = thePOC.getDomain().getId();
		determinePrimaryPOC(thePOC, sample, newSample);
		
		
		// set up one sampleService
		SampleService service = setServiceInSession(request);
		// have to save POC separately because the same organizations can not be
		// saved in the same session
		service.savePointOfContact(thePOC);
		sample.addPointOfContact(thePOC, oldPOCId);
		
		logger.debug("========== Done saving POC");

		// if the oldPOCId is different from the one after POC save
		if (oldPOCId != null && !oldPOCId.equals(thePOC.getDomain().getId())) {
			// update characterization POC associations
			((SampleServiceLocalImpl) service)
					.updatePOCAssociatedWithCharacterizations(sample
							.getDomain().getName(), oldPOCId, thePOC
							.getDomain().getId());
		}
		
		try {
		// save sample
			logger.debug("========== Saving Sample with POC");
			saveSample(request, sample);
			logger.debug("========== Done Saving Sample with POC");
		} catch (NoAccessException e) {
			if (newSample)
				simpleSampleBean.getPointOfContacts().clear();
			
			request.getSession().setAttribute("theSample", sample);
			simpleSampleBean.getErrors().add("User has no access to edit this sample");
			simpleSampleBean.transferPointOfContactData(sample);;
			return simpleSampleBean;
		} catch (DuplicateEntriesException e) {
			if (newSample)
				simpleSampleBean.getPointOfContacts().clear();
			
			request.getSession().setAttribute("theSample", sample);
			simpleSampleBean.getErrors().add(PropertyUtil.getProperty("sample", "error.duplicateSample"));
			simpleSampleBean.transferPointOfContactData(sample);;
			return simpleSampleBean;
			//return this.wrapErrorInEditBean(PropertyUtil.getProperty("sample", "error.duplicateSample"));
		} catch (Exception e) {
			if (newSample)
				simpleSampleBean.getPointOfContacts().clear();
			
			request.getSession().setAttribute("theSample", sample);
			simpleSampleBean.getErrors().add(e.getMessage());
			simpleSampleBean.transferPointOfContactData(sample);;
			return simpleSampleBean;
		}
		
		if (newSample)
			this.setAccesses(request, sample); //this will assign default curator access to this sample.
		
		InitSampleSetup.getInstance().persistPOCDropdowns(request, sample);
		
		logger.debug("========== Populating UpdateSample data");
		return summaryEdit(sample.getDomain().getId().toString(), request);
	}
	
	protected void determinePrimaryPOC(PointOfContactBean thePOC, SampleBean sample, boolean newSample) {
		if (newSample == true) {
			if (sample.getDomain().getPrimaryPointOfContact() == null)
				thePOC.setPrimaryStatus(true);
		}
	}

	/**
	 * Delete a non-primary POC from a sample
	 * 
	 * @param simpleEditBean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean deletePointOfContact(SimplePointOfContactBean simplePOC, HttpServletRequest request) 
			throws Exception {
		
		long sampleId = simplePOC.getSampleId();
		
		SampleBean sample = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		if (sample == null) {
			System.out.println("No Sample in session");
			return wrapErrorInEditBean("No valid sample in session matching given sample id. Unable to update delete POC to the sample.");
		}
		
		if (simplePOC.isPrimaryContact())
			return wrapErrorInEditBean(PropertyUtil.getProperty("sample", "message.deletePrimaryPOC"));
		
		removeMatchingPOC(sample, simplePOC);

		setServiceInSession(request);
		saveSample(request, sample);
	
//		String updateSample = (String) request.getSession().getAttribute(
//				"updateSample");
//		if (updateSample == null) {
//	//		forward = mapping.findForward("createInput");
//			setupLookups(request);
//		} else {
//			request.setAttribute("sampleId", sample.getDomain().getId()
//					.toString());
//	//		forward = summaryEdit(mapping, form, request, response);
//		}
		return summaryEdit(String.valueOf(sample.getDomain().getId()), request);
	}

	/**
	 * Make a copy of an existing sample based on sample name.
	 * 
	 * @param simpleEditBean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean clone(SampleEditGeneralBean simpleEditBean,
			HttpServletRequest request)
			throws Exception {
		
		String newNameForClone = simpleEditBean.getNewSampleName();
		String orgSampleName = simpleEditBean.getSampleName();
		String error = validateSampleName(newNameForClone);
		if (error.length() > 0) 
			return this.wrapErrorInEditBean(error);
		
		SampleBean clonedSampleBean = null;
		SampleService service = this.setServiceInSession(request);
		
		try {
			clonedSampleBean = service.cloneSample(orgSampleName, newNameForClone);
		} catch (NotExistException e) {
			error =  PropertyUtil.getPropertyReplacingToken("sample", "error.cloneSample.noOriginalSample", 
					"0", orgSampleName);
			return wrapErrorInEditBean(error);
		} catch (DuplicateEntriesException e) {
			error =  PropertyUtil.getProperty("sample", "error.cloneSample.duplicateSample");
			return wrapErrorInEditBean(error);
		} catch (SampleException e) {
			error =  PropertyUtil.getProperty("sample", "error.cloneSample");
			return wrapErrorInEditBean(error);
		}

	
		//what's this for?
		request.setAttribute("sampleId", clonedSampleBean.getDomain().getId()
				.toString());
		
		return summaryEdit(String.valueOf(clonedSampleBean.getDomain().getId()), request);
	}

	public String delete(String sampleId, HttpServletRequest request)
			throws Exception {
		
		SampleBean sampleBean = findMatchSampleInSession(request, Long.parseLong(sampleId));
		if (sampleBean == null)
			return "Error: unable to find a valid sample in session with id . Sample deletion failed";
		
		String sampleName = sampleBean.getDomain().getName();
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
		request.getSession().removeAttribute("theSample");
		
		String msg = PropertyUtil.getPropertyReplacingToken("sample", "message.deleteSample", "0", sampleName);
		
		return msg;
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
		
		SampleBean sampleBean = findMatchSampleInSession(request, Long.parseLong(sampleId));
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
	public SampleEditGeneralBean deleteDataAvailability(SampleEditGeneralBean simpleEditBean, HttpServletRequest request) 
			throws Exception {
		
		long sampleId = simpleEditBean.getSampleId();
		
		SampleBean sampleBean = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		if (sampleBean == null) {
			System.out.println("No Sample in session");
			return wrapErrorInEditBean("No valid sample in session matching given sample id. Unable to delete Data Availability to sample.");
		}
		
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		dataAvailabilityService.deleteDataAvailability(sampleBean.getDomain()
				.getId().toString(), securityService);
		sampleBean.setHasDataAvailability(false);
		sampleBean.setDataAvailability(new HashSet<DataAvailabilityBean>());
		return summaryEdit(String.valueOf(sampleBean.getDomain().getId()), request);
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

	/**
	 * Save access info for a sample
	 * @param simpleAccess
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SampleEditGeneralBean saveAccess(SampleEditGeneralBean simpleEditBean, HttpServletRequest request)
			throws Exception {
		
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		SampleBean sample = (SampleBean)request.getSession().getAttribute("theSample");
		
		if (sample == null) {
			throw new Exception("Sample object is not valid in session for saving /updating access");
		}
		
		AccessibilityBean theAccess = simpleEditBean.getTheAccess();
		List<String> errors = validateAccess(request, theAccess);
		if (errors.size() > 0) {
			return this.wrapErrorsInEditBean(errors);
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
		
		simpleEditBean.populateDataForSavingSample(sample);
		saveSample(request, sample);

		//if (request.getSession().getAttribute("allGroupNames") == null) {
		//refresh groupNames in case the new access was a "other"
		
		if (theAccess.getAccessBy().equals("group")) {
			List<String> groupNames = (List<String>) request.getSession().getAttribute("allGroupNames");
			if (groupNames == null || !groupNames.contains(theAccess.getGroupName())) {
				SampleService sampleService = (SampleService) request.getSession().getAttribute("sampleService");
				List<String> availGroupNames = sampleService.findGroupNames("");
				request.getSession().setAttribute("allGroupNames", availGroupNames);
			}
		}
		//}
		
		return summaryEdit(sample.getDomain().getId()
				.toString(), request);
	}

	public SampleEditGeneralBean deleteAccess(SampleEditGeneralBean simpleEditBean, HttpServletRequest request)
			throws Exception {
		
		long sampleId = simpleEditBean.getSampleId();
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		
		SampleBean sample = (SampleBean) this.findMatchSampleInSession(request, sampleId);
		if (sample == null) {
			System.out.println("No Sample in session");
			return wrapErrorInEditBean("No valid sample in session matching given sample id. Unable to update delete accecc to the sample.");
		}
		
		//SimpleAccessBean simpleAccess = this.findDirtyAccess(simpleEditBean.getAccessToSample());
		
		AccessibilityBean theAccess = simpleEditBean.getTheAccess();
		sample.setTheAccess(theAccess);
		//this.populateAccessBeanWithInput(simpleAccess, theAccess, user.getLoginName());
		SampleService service = this.setServiceInSession(request);
		service.removeAccessibility(theAccess, sample.getDomain());

		return summaryEdit(String.valueOf(sample.getDomain().getId()), request);
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
		
		List<String> errors = new ArrayList<String>();
		
		if (simplePOC == null) {
			errors.add("Input point of contact object invalid"); //shouldn't happen
			return errors;
		}
		
		//errors = RestValidator.validate(simplePOC);
		
		
		SimpleOrganizationBean simpleOrg = simplePOC.getOrganization();
		if (simpleOrg != null) {
			String orgName = simpleOrg.getName();
			if (orgName == null || !InputValidationUtil.isTextFieldWhiteList(orgName))
				errors.add(PropertyUtil.getProperty("sample", "organization.name.invalid"));
		} else
			errors.add("Organization Name is a required field");
		
		SimpleAddressBean addrBean = simplePOC.getAddress();
		if (addrBean != null) {
			String val = addrBean.getLine1();
			if (val != null && val.length() > 0 && !InputValidationUtil.isTextFieldWhiteList(val))
				errors.add(PropertyUtil.getProperty("sample", "organization.address1.invalid"));
			
			val = addrBean.getLine2();
			if (val != null && val.length() > 0 && !InputValidationUtil.isTextFieldWhiteList(val))
				errors.add(PropertyUtil.getProperty("sample", "organization.address2.invalid"));
			val = addrBean.getCity();
			if (val != null && val.length() > 0 && !InputValidationUtil.isRelaxedAlphabetic(val))
				errors.add(PropertyUtil.getProperty("sample", "organization.city.invalid"));
			
			val = addrBean.getStateProvince();
			if (val != null && val.length() > 0 && !InputValidationUtil.isRelaxedAlphabetic(val))
				errors.add(PropertyUtil.getProperty("sample", "organization.state.invalid"));
			
			val = addrBean.getCountry();
			if (val != null && val.length() > 0 && !InputValidationUtil.isRelaxedAlphabetic(val))
				errors.add(PropertyUtil.getProperty("sample", "organization.country.invalid"));
			
			val = addrBean.getZip();
			if (val != null && val.length() > 0 && !InputValidationUtil.isZipValid(addrBean.getZip()))
				errors.add(PropertyUtil.getProperty("sample", "postalCode.invalid"));
		}
		
		String name = simplePOC.getFirstName();
		if (name != null && name.length() > 0 && !InputValidationUtil.isRelaxedAlphabetic(name))
			errors.add(PropertyUtil.getProperty("sample", "firstName.invalid"));
		
		name = simplePOC.getLastName();
		if (name != null && name.length() > 0 && !InputValidationUtil.isRelaxedAlphabetic(name))
			errors.add(PropertyUtil.getProperty("sample", "lastName.invalid"));
		
		name = simplePOC.getMiddleInitial();
		if (name != null && name.length() > 0 && !InputValidationUtil.isRelaxedAlphabetic(name))
			errors.add(PropertyUtil.getProperty("sample", "middleInitial.invalid"));
		
		String phone = simplePOC.getPhoneNumber();
		if ( phone.length() > 0 && !InputValidationUtil.isPhoneValid(phone))
			errors.add(PropertyUtil.getProperty("sample", "phone.invalid"));
//			
		String email = simplePOC.getEmail();
		EmailValidator emailValidator = EmailValidator.getInstance();
		if (email != null && email.length() > 0 && !emailValidator.isValid(email))
			errors.add("Email is invalid");
		
		return errors;
	}
	
	protected String validateSampleName(String sampleName) {
		return (!InputValidationUtil.isTextFieldWhiteList(sampleName)) ?
				 PropertyUtil.getProperty("sample", "cloningSample.name.invalid")
				 : "";
				
	}
	
	protected PointOfContactBean resolveThePOCToSaveFromInput(SampleBean sample, SimplePointOfContactBean simplePOC, String createdBy) {
//		PointOfContactBean newPOC = new PointOfContactBean();
//		return getPointOfContactBeanFromInput(newPOC, simplePOC, createdBy);
		
		PointOfContactBean newPOC = new PointOfContactBean();
		
		PointOfContactBean primary = sample.getPrimaryPOCBean();
		List<PointOfContactBean> others = sample.getOtherPOCBeans();
		long pocId = simplePOC.getId();
		
		if (primary == null || pocId == 0) { //new sample for submission or adding new POC
			return getPointOfContactBeanFromInput(newPOC, simplePOC, createdBy);
		} 
		
		if (primary.getDomain().getId().longValue() == simplePOC.getId()) {
			newPOC.getDomain().setCreatedBy(primary.getDomain().getCreatedBy());
			return getPointOfContactBeanFromInput(newPOC, simplePOC, createdBy);
		}
		
		if (others != null) {
			for (PointOfContactBean poc : others) {
				if (pocId == poc.getDomain().getId().longValue()) {
					newPOC.getDomain().setCreatedBy(poc.getDomain().getCreatedBy());
					return getPointOfContactBeanFromInput(newPOC, simplePOC, createdBy);
				}
			}
		}
		
		return null;
	}
	
//	protected PointOfContactBean getPointOfContactBeanFromInput(SimplePointOfContactBean simplePOC, String createdBy) {
//		PointOfContactBean newPOC = new PointOfContactBean();
//		return getPointOfContactBeanFromInput(newPOC, simplePOC, createdBy);
//	}
	protected PointOfContactBean getPointOfContactBeanFromInput(PointOfContactBean pocBean, SimplePointOfContactBean simplePOC, String createdBy) {
		
		
		pocBean.setupDomain(createdBy);
		
		Organization org = pocBean.getDomain().getOrganization();
		if (org == null)
			org = new Organization();
		
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
		//pocBean.setupDomain(createdBy);
		
		pocBean.getDomain().setFirstName(simplePOC.getFirstName());
		pocBean.getDomain().setLastName(simplePOC.getLastName());
		pocBean.getDomain().setMiddleInitial(simplePOC.getMiddleInitial());
		
		pocBean.getDomain().setPhone(simplePOC.getPhoneNumber());
		pocBean.getDomain().setEmail(simplePOC.getEmail());
		
		pocBean.setPrimaryStatus(simplePOC.isPrimaryContact());
		
		return pocBean;
	}
	
	protected void populateAccessBeanWithInput(SimpleAccessBean simpleAccess, AccessibilityBean theAccess, String loginName) {
		
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
	
	protected SampleBean findMatchSampleInSession(HttpServletRequest request, long sampleId) {
		
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute("theSample");
		if (sampleBean == null) {
			logger.error("No sample in session"); //should not happen
			return null;
		}
			
		Long domainSampleId = sampleBean.getDomain().getId();
		if (domainSampleId == null) {
			if (sampleId == 0)
				return sampleBean; // from a failed save, incomplete sampleBean
			else {
				logger.error("Sample in session doesn't seem to be valid");
				return null;
			}
		}
		
		if (sampleId != domainSampleId.longValue()) {
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
	
	protected SampleEditGeneralBean wrapErrorsInEditBean(List<String> errors) {
		SampleEditGeneralBean simpleBean = new SampleEditGeneralBean();
		simpleBean.setErrors(errors);
		return simpleBean;
	}
	
	protected SampleEditGeneralBean wrapErrorsInEditBean(List<String> errors, String errorType) {
		SampleEditGeneralBean simpleBean = new SampleEditGeneralBean();
		simpleBean.setErrors(errors);
		simpleBean.setErrorType(errorType);
		return simpleBean;
	}
	
	public List<String> getMatchedSampleNames(HttpServletRequest request, String searchStr) throws Exception {
		
		String[] nameArray = new String[] { "" };
		try {
			SampleService service = setServiceInSession(request);

			List<String> names = ((SampleServiceLocalImpl) service)
					.getHelper().findSampleNamesBy(searchStr);
			Collections.sort(names, new Comparators.SortableNameComparator());

			if (!names.isEmpty()) {
				nameArray = names.toArray(new String[names.size()]);
			}
		} catch (Exception e) {
			logger.error("Problem getting matched sample names", e);
		}
		List<String> names = new ArrayList(Arrays.asList(nameArray));
		return names;
	}

	@Override
	public String submitForReview(HttpServletRequest request,
			DataReviewStatusBean dataReviewStatusBean) throws Exception {
		String message = super.submitForReview(request, dataReviewStatusBean);
		
		return (message.equals("success")) ?
			PropertyUtil.getProperty("sample", "message.submitReview") 
			: "Error while submitting your sample for review";
			
	}

	public String getCurrentSampleNameInSession(HttpServletRequest request, String sampleId) 
	throws Exception {
		
		if (sampleId == null)
			throw new Exception("Input sample id is null");
		
		SampleBean sampleBean = (SampleBean) request.getSession().getAttribute("theSample");
		if (sampleBean == null)
			throw new Exception("No sample in session matching sample id: "  + sampleId);
		
		Long id = sampleBean.getDomain().getId();
		if (id == null)
			throw new Exception("Sample in session has null id");
		
		if (Long.parseLong(sampleId) != id.longValue())
			throw new Exception("Sample in session doesn't match input sample id");
		
		return sampleBean.getDomain().getName();
		
	}
	
	public boolean isSampleEditableByCurrentUser(HttpServletRequest request, String sampleId) 
	throws Exception {
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user == null)
			return false;
		
		if (sampleId == null || sampleId.length() == 0)
			return false;
		
		SampleBean sampleBean = new SampleBean();
		sampleBean.getDomain().setId(Long.parseLong(sampleId));
		
		SampleService service = this.setServiceInSession(request);
		service.loadAccessesForSampleBean(sampleBean);
		
		sampleBean.setUser(user);
		return sampleBean.getUserUpdatable();
		
	}
	
}
