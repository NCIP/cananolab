package gov.nih.nci.cananolab.ui.sample;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.NotExistException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		setServiceInSession(request);
		saveSample(request, sampleBean);
		request.getSession().setAttribute("updateSample", "true");
		request.setAttribute("theSample", sampleBean);

		return mapping.findForward("summaryEdit");
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
			return mapping.findForward("createInput");
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
		SampleBean sampleBean = setupSample(theForm, request);
		// set existing sample accessibility
		this.setSampleAccesses(request, sampleBean);
		Map<String, List<DataAvailabilityBean>> dataAvailabilityMapPerPage = (Map<String, List<DataAvailabilityBean>>) request
				.getSession().getAttribute("dataAvailabilityMapPerPage");

		List<DataAvailabilityBean> selectedSampleDataAvailability = dataAvailabilityMapPerPage
				.get(sampleBean.getDomain().getId().toString());

		if (!selectedSampleDataAvailability.isEmpty()
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

		return mapping.findForward("summaryEdit");
	}

	private void setSampleAccesses(HttpServletRequest request,
			SampleBean sampleBean) throws Exception {
		SampleService service = this.setServiceInSession(request);
		List<AccessibilityBean> groupAccesses = service
				.findGroupAccessibilities(sampleBean.getDomain().getId()
						.toString());
		List<AccessibilityBean> userAccesses = service
				.findUserAccessibilities(sampleBean.getDomain().getId()
						.toString());
		// exclude user who own the sample
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
		InitSecuritySetup.getInstance().getAllVisibilityGroupsWithoutOrg(
				request, sampleOrg);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
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
			((SampleServiceLocalImpl) service)
					.updateSampleVisibilityWithPOCChange(sample, oldPOCId
							.toString());
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
		List<String> csmEntriesToRemove = service.deleteSample(sampleBean
				.getDomain().getName(), false);
		InitSetup.getInstance().updateCSMCleanupEntriesInContext(
				csmEntriesToRemove, request);
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
		UserBean user = (UserBean) request.getSession().getAttribute("user");

		List<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.generateDataAvailability(sampleBean, user);
		sampleBean.setDataAvailability(dataAvailability);
		sampleBean.setHasDataAvailability(true);
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
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		List<DataAvailabilityBean> dataAvailability = dataAvailabilityService
				.saveDataAvailability(sampleBean, user);
		sampleBean.setDataAvailability(dataAvailability);
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
		dataAvailabilityService.deleteDataAvailability(sampleBean.getDomain()
				.getId().toString());
		sampleBean.setHasDataAvailability(false);
		sampleBean.setDataAvailability(new ArrayList<DataAvailabilityBean>());
		return mapping.findForward("summaryEdit");
	}

	public ActionForward saveAccess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		AccessibilityBean theAccess = sample.getTheAccess();
		SampleService service = this.setServiceInSession(request);
		service.assignAccessibility(theAccess, sample.getDomain());
		this.setSampleAccesses(request, sample);
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
		} else {
			request.setAttribute("sampleId", sample.getDomain().getId()
					.toString());
			forward = summaryEdit(mapping, form, request, response);
		}
		return forward;
	}

	public ActionForward deletePublicAccess(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (!user.isCurator()) {
			throw new NoAccessException(
					"You must be a Curator to be able to peform this function");
		}
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		SampleService service = this.setServiceInSession(request);
		service.removeAccessibility(Constants.CSM_PUBLIC_ACCESS, sample
				.getDomain());

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

	private SampleService setServiceInSession(HttpServletRequest request)
			throws Exception {
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		SampleService sampleService = new SampleServiceLocalImpl(user);
		request.getSession().setAttribute("sampleService", sampleService);
		return sampleService;
	}

	public Boolean canUserExecutePrivateLink(UserBean user, String protectedData)
			throws SecurityException {

		return false;
	}
}
