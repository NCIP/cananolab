package gov.nih.nci.cananolab.ui.sample;

/**
 * This class sets up the submit a new sample page and submits a new sample
 *
 * @author pansu
 */

/* CVS $Id: SubmitNanoparticleAction.java,v 1.37 2008-09-18 21:35:25 cais Exp $ */

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

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
	// private static Logger logger = Logger.getLogger(SampleAction.class);

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
		this.saveSample(request, sampleBean);
		request.getSession().setAttribute("updateSample", "true");
		sampleBean.setLocation(Constants.LOCAL_SITE);
		request.setAttribute("theSample", sampleBean);

		return mapping.findForward("summaryEdit");
	}

	private void saveSample(HttpServletRequest request, SampleBean sampleBean)
			throws Exception {
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		sampleBean.setupDomain(user.getLoginName());
		// persist in the database
		SampleService service = new SampleServiceLocalImpl();
		service.saveSample(sampleBean, user);

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
		String location = (String) getValueFromRequest(request,
				Constants.LOCATION);

		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(theForm, request, location);
		theForm.set("sampleBean", sampleBean);
		return mapping.findForward("summaryView");
	}

	public ActionForward setupView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = (String) getValueFromRequest(request,
				Constants.LOCATION);

		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(theForm, request, location);
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
		String location = theForm.getString(Constants.LOCATION);
		if (StringUtils.isEmpty(location)) {
			location = Constants.LOCAL_SITE;
		}
		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(theForm, request, location);
		theForm.set("sampleBean", sampleBean);
		request.getSession().setAttribute("updateSample", "true");
		setupLookups(request, sampleBean.getPrimaryPOCBean().getDomain()
				.getOrganization().getName());

		// Feature request [26487] Deeper Edit Links.
		String dispatch = request.getParameter("dispatch"); // as the function
		// is shared.
		if ("summaryEdit".equals(dispatch)
				|| "removePointOfContact".equals(dispatch)) {
			if (sampleBean.getPrimaryPOCBean() != null
					&& sampleBean.getOtherPOCBeans().isEmpty()) {
				StringBuilder sb = new StringBuilder();
				sb.append("setThePointOfContact(");
				sb.append(sampleBean.getPrimaryPOCBean().getDomain().getId());
				sb.append(", true)");
				request.setAttribute("onloadJavascript", sb.toString());
			}
		}

		return mapping.findForward("summaryEdit");
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
		return mapping.getInputForward();
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
		}
		else {
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
		SampleService service = new SampleServiceLocalImpl();
		// have to save POC separately because the same organizations can not be
		// save in the same session
		service.savePointOfContact(thePOC, user);
		sample.addPointOfContact(thePOC);
		// save sample
		saveSample(request, sample);
		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.getInputForward();
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
		saveSample(request, sample);
		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.getInputForward();
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
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		ActionMessages messages = new ActionMessages();
		SampleBean sampleBean = (SampleBean) theForm.get("sampleBean");
		SampleService service = new SampleServiceLocalImpl();
		try {
			service.cloneSample(sampleBean.getCloningSampleName(), sampleBean
					.getDomain().getName(), user);
		} catch (DuplicateEntriesException e) {
			ActionMessage err = new ActionMessage(
					"error.cloneSample.duplicateSample", sampleBean
							.getCloningSampleName(), sampleBean.getDomain()
							.getName());
			messages.add(ActionMessages.GLOBAL_MESSAGE, err);
			saveErrors(request, messages);
			return mapping.findForward("cloneInput");
		} catch (SampleException e) {
			ActionMessage err = new ActionMessage("error.cloneSample",
					sampleBean.getCloningSampleName(), sampleBean.getDomain()
							.getName());
			messages.add(ActionMessages.GLOBAL_MESSAGE, err);
			saveErrors(request, messages);
			return mapping.findForward("cloneInput");
		}

		ActionMessage msg = null;
		msg = new ActionMessage("message.cloneSample", sampleBean
				.getCloningSampleName(), sampleBean.getDomain().getName());
		messages.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, messages);

		return summaryEdit(mapping, form, request, response);
	}

}
