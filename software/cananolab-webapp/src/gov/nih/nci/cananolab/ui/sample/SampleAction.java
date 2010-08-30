package gov.nih.nci.cananolab.ui.sample;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SampleAction extends BaseAnnotationAction {
	// logger
	// private static Logger logger = Logger.getLogger(ReviewDataAction.class);

	private DataAvailabilityService dataAvailabilityService;

	/**
	 * Save or update POC data.
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
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		Boolean newSample = true;
		if (sampleBean.getDomain().getId() != null) {
			newSample = false;
		}
		setServiceInSession(request);
		saveSample(request, sampleBean);
		// retract from public if updating an existing public record and not
		// curator
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		if (!newSample && !user.isCurator() && sampleBean.getPublicStatus()) {
			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
					.toString(), sampleBean.getDomain().getName(), "sample");
			ActionMessages messages = new ActionMessages();
			ActionMessage msg = null;
			msg = new ActionMessage("message.updateSample.retractFromPublic");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
		}
		request.getSession().setAttribute("updateSample", "true");
		request.setAttribute("theSample", sampleBean);
		request.setAttribute("sampleId", sampleBean.getDomain().getId()
				.toString());
		return summaryEdit(mapping, form, request, response);
	}

	private void saveSample(HttpServletRequest request, SampleBean sampleBean)
			throws Exception {
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		sampleBean.setupDomain(user.getLoginName());
		// persist in the database
		SampleService service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		service.saveSample(sampleBean);

		ActionMessages messages = new ActionMessages();
		ActionMessage msg = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (!StringUtils.isEmpty(updateSample)) {
			msg = new ActionMessage("message.updateSample");
			messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, messages);
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
	public ActionForward summaryView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServiceInSession(request);

		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(theForm, request);
		theForm.set("sampleBean", sampleBean);
		return mapping.findForward("summaryView");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String browserDispatch = getBrowserDispatch(request);
		// from cloning form
		if (browserDispatch.equals("clone")) {
			return mapping.findForward("cloneInput");
		} else {
			String updateSample = (String) request.getSession().getAttribute(
					"updateSample");
			if (updateSample == null) {
				return mapping.findForward("createInput");
			} else {
				return mapping.findForward("summaryEdit");
			}
		}
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServiceInSession(request);
		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(theForm, request);
		theForm.set("sampleBean", sampleBean);
		return mapping.findForward("bareSummaryView");
	}

	/**
	 * Handle edit sample request on sample search result page (curator view).
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward summaryEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		this.setServiceInSession(request);

		// "setupSample()" will retrieve and return the SampleBean.
		SecurityService securityService = (SecurityService) request
		.getSession().getAttribute("securityService");
		SampleBean sampleBean = setupSample(theForm, request);
		Set<DataAvailabilityBean> selectedSampleDataAvailability = dataAvailabilityService.findDataAvailabilityBySampleId(
						sampleBean.getDomain().getId().toString(), securityService);
		
		if (selectedSampleDataAvailability != null
				&& !selectedSampleDataAvailability.isEmpty()
				&& selectedSampleDataAvailability.size() > 0) {
			sampleBean.setHasDataAvailability(true);
			sampleBean.setDataAvailability(selectedSampleDataAvailability);
		}
		
		theForm.set("sampleBean", sampleBean);
		request.getSession().setAttribute("updateSample", "true");
		setupLookups(request, sampleBean.getPrimaryPOCBean().getDomain()
				.getOrganization().getName());
		//

		// Feature request [26487] Deeper Edit Links.
		// String dispatch = request.getParameter("dispatch"); // as the
		// function
		// // is shared.
		// if ("summaryEdit".equals(dispatch)
		// || "removePointOfContact".equals(dispatch)) {
		// if (sampleBean.getPrimaryPOCBean() != null
		// && sampleBean.getOtherPOCBeans().isEmpty()) {
		// StringBuilder sb = new StringBuilder();
		// sb.append("openOnePointOfContact(");
		// sb.append(sampleBean.getPrimaryPOCBean().getDomain().getId());
		// sb.append(", true)");
		// request.setAttribute("onloadJavascript", sb.toString());
		// }
		// }
		setUpSubmitForReviewButton(request, sampleBean.getDomain().getId()
				.toString(), sampleBean.getPublicStatus());
		return mapping.findForward("summaryEdit");
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
	public ActionForward setupNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.getSession().removeAttribute("sampleForm");
		request.getSession().removeAttribute("updateSample");
		setupLookups(request, null);
		return mapping.findForward("createInput");
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
	public ActionForward setupClone(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		if (request.getParameter("cloningSample") != null) {
			String cloningSampleName = request.getParameter("cloningSample");
			sampleBean.setCloningSampleName(cloningSampleName);
			sampleBean.getDomain().setName(null);
		} else {
			sampleBean.setCloningSampleName(null);
			sampleBean.getDomain().setName(null);
		}
		return mapping.findForward("cloneInput");
	}

	/**
	 * Retrieve all POCs and Groups for POC drop-down on sample edit page.
	 *
	 * @param request
	 * @param sampleOrg
	 * @throws Exception
	 */
	private void setupLookups(HttpServletRequest request, String sampleOrg)
			throws Exception {
		InitSampleSetup.getInstance().setPOCDropdowns(request);
	}

	public ActionForward savePointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		SampleBean sample = (SampleBean) theForm.get("sampleBean");

		PointOfContactBean thePOC = sample.getThePOC();
		thePOC.setupDomain(user.getLoginName());
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
			// remove oldOrg from sample visibility
			// ((SampleServiceLocalImpl) service)
			// .updateSampleVisibilityWithPOCChange(sample, oldPOCId
			// .toString());
		}
		// save sample
		saveSample(request, sample);
		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.findForward("createInput");
			setupLookups(request, sample.getPrimaryPOCBean().getDomain()
					.getOrganization().getName());
			this.setAccesses(request, sample);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
			forward = summaryEdit(mapping, form, request, response);
		}
		InitSampleSetup.getInstance().persistPOCDropdowns(request, sample);
		return forward;
	}

	public ActionForward removePointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		PointOfContactBean thePOC = sample.getThePOC();
		ActionMessages msgs = new ActionMessages();
		if (thePOC.getPrimaryStatus()) {
			ActionMessage msg = new ActionMessage("message.deletePrimaryPOC");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
		}
		sample.removePointOfContact(thePOC);
		// save sample
		setServiceInSession(request);
		saveSample(request, sample);
		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.findForward("createInput");
			setupLookups(request, sample.getPrimaryPOCBean().getDomain()
					.getOrganization().getName());
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
			forward = summaryEdit(mapping, form, request, response);
		}
		return forward;
	}

	public ActionForward clone(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		ActionMessages messages = new ActionMessages();
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		SampleBean clonedSampleBean = null;
		SampleService service = this.setServiceInSession(request);
		try {
			clonedSampleBean = service.cloneSample(sampleBean
					.getCloningSampleName(), sampleBean.getDomain().getName()
					.trim());
		} catch (NotExistException e) {
			ActionMessage err = new ActionMessage(
					"error.cloneSample.noOriginalSample", sampleBean
							.getCloningSampleName());
			messages.add(ActionMessages.GLOBAL_MESSAGE, err);
			saveErrors(request, messages);
			return mapping.findForward("cloneInput");
		} catch (DuplicateEntriesException e) {
			ActionMessage err = new ActionMessage(
					"error.cloneSample.duplicateSample", sampleBean
							.getCloningSampleName(), sampleBean.getDomain()
							.getName());
			messages.add(ActionMessages.GLOBAL_MESSAGE, err);
			saveErrors(request, messages);
			return mapping.findForward("cloneInput");
		} catch (SampleException e) {
			ActionMessage err = new ActionMessage("error.cloneSample");
			messages.add(ActionMessages.GLOBAL_MESSAGE, err);
			saveErrors(request, messages);
			return mapping.findForward("cloneInput");
		}

		ActionMessage msg = new ActionMessage("message.cloneSample", sampleBean
				.getCloningSampleName(), sampleBean.getDomain().getName());
		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, messages);
		request.setAttribute("sampleId", clonedSampleBean.getDomain().getId()
				.toString());
		return summaryEdit(mapping, form, request, response);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		SampleService service = this.setServiceInSession(request);
		// remove all access associated with sample takes too long. Set up the
		// delete job in scheduler
		InitSampleSetup.getInstance().updateCSMCleanupEntriesInContext(
				sampleBean.getDomain(), request);
		service.deleteSample(sampleBean.getDomain().getName());

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.deleteSample",
				sampleBean.getDomain().getName());
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		sampleBean = new SampleBean();
		ActionForward forward = mapping.findForward("sampleMessage");
		return forward;
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
	public ActionForward generateDataAvailability(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.generateDataAvailability(sampleBean, securityService);
		sampleBean.setDataAvailability(dataAvailability);
		sampleBean.setHasDataAvailability(true);

		/*Map<String, List<DataAvailabilityBean>> dataAvailabilityMapPerPage = (Map<String, List<DataAvailabilityBean>>) request
				.getSession().getAttribute("dataAvailabilityMapPerPage");

		if (dataAvailabilityMapPerPage != null) {
			dataAvailabilityMapPerPage.remove(sampleBean.getDomain().getId()
					.toString());
			dataAvailabilityMapPerPage.put(sampleBean.getDomain().getId()
					.toString(), dataAvailability);

			request.getSession().setAttribute("dataAvailabilityMapPerPage",
					dataAvailabilityMapPerPage);
		}*/
		request.setAttribute("onloadJavascript", "manageDataAvailability('" + sampleBean.getDomain().getId() + "', 'sample', 'dataAvailabilityView')");
		
		return mapping.findForward("summaryEdit");
	}

	/**
	 * update data availability for the sample
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateDataAvailability(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.saveDataAvailability(sampleBean, securityService);
		sampleBean.setDataAvailability(dataAvailability);
		request.getSession().setAttribute("dataAvailabilityGenerated", false);
		return mapping.findForward("summaryEdit");
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
	public ActionForward deleteDataAvailability(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		dataAvailabilityService.deleteDataAvailability(sampleBean.getDomain()
				.getId().toString(), securityService);
		sampleBean.setHasDataAvailability(false);
		sampleBean.setDataAvailability(new HashSet<DataAvailabilityBean>());
		request.getSession().setAttribute("dataAvailabilityGenerated", false);
		return mapping.findForward("summaryEdit");
	}

	public ActionForward dataAvailabilityView(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sampleBean = setupSample(theForm, request);
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.findDataAvailabilityBySampleId(sampleBean.getDomain().getId()
						.toString(), securityService);

		sampleBean.setDataAvailability(dataAvailability);
		if (!dataAvailability.isEmpty() && dataAvailability.size() > 0) {
			sampleBean.setHasDataAvailability(true);
			calculateDataAvailabilityScore(sampleBean, dataAvailability);
			String[] availableEntityNames = new String[dataAvailability.size()];
			int i = 0;
			for (DataAvailabilityBean bean : dataAvailability) {
				// bean.getAvailableEntityName();
				availableEntityNames[i++] = bean.getAvailableEntityName()
						.toLowerCase();
			}
			request.setAttribute("availableEntityNames", availableEntityNames);
		}
		request.setAttribute("sampleBean", sampleBean);

		return mapping.findForward("dataAvailabilityView");
	}

	private void calculateDataAvailabilityScore(SampleBean sampleBean,
			Set<DataAvailabilityBean> dataAvailability) {

		ServletContext appContext = this.getServlet().getServletContext();
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

	public ActionForward saveAccess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		AccessibilityBean theAccess = sample.getTheAccess();
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

		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.findForward("createInput");
			setupLookups(request, sample.getPrimaryPOCBean().getDomain()
					.getOrganization().getName());
			this.setAccesses(request, sample);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
			forward = summaryEdit(mapping, form, request, response);
		}
		return forward;
	}

	public ActionForward deleteAccess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		AccessibilityBean theAccess = sample.getTheAccess();
		SampleService service = this.setServiceInSession(request);
		service.removeAccessibility(theAccess, sample.getDomain());

		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.findForward("createInput");
			setupLookups(request, sample.getPrimaryPOCBean().getDomain()
					.getOrganization().getName());
			this.setAccesses(request, sample);
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
			forward = summaryEdit(mapping, form, request, response);
		}
		return forward;
	}

	protected void removePublicAccess(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		SampleService service = this.setServiceInSession(request);
		service.removeAccessibility(AccessibilityBean.CSM_PUBLIC_ACCESS, sample
				.getDomain());
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
	
	public ActionForward generateBatchDataAvailability(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		SecurityService securityService = (SecurityService) request
		.getSession().getAttribute("securityService");
		
		SampleService service = (SampleService) request.getSession()
		.getAttribute("sampleService");
		
		if(service == null){
			service = setServiceInSession(request);
		}
		System.out.println("Generate data availability by batch ");
		List<String> sampleIds = service.findSampleIdsBy("", "", null, null, null, null, null, null, null, null, null);
		int sampleIdsSize = sampleIds.size();
		int i=0;
		System.out.println("sampleIdsSize: " + sampleIdsSize);
		if(sampleIdsSize > 50){		
			int subListValue = 50;
			while(true){				
				List<String> tempSampleIds = sampleIds.subList(i, i+subListValue);
				System.out.println("subList size: " + tempSampleIds.size());
				dataAvailabilityService.generateBatch(securityService, tempSampleIds);
				i = i + subListValue;
				System.out.println("i = " + i);
				if(i + subListValue > sampleIdsSize){
					subListValue = sampleIdsSize - i;
					System.out.println("subListValue = " + subListValue);
				}
				if(i == sampleIdsSize){
					break;
				}
			}
		}else{
			dataAvailabilityService.generateBatch(securityService, sampleIds);			
		}
		ActionMessages messages = new ActionMessages();
		ActionMessage message = new ActionMessage("message.generateDataAvailability");
		messages.add("message", message);
		saveMessages(request, messages);
		return mapping.findForward("manageCuration");
		
	}
}
