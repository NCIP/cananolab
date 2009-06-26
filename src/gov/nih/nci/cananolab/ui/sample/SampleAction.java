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
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.BaseAnnotationAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class SampleAction extends BaseAnnotationAction {
	// logger
	private static Logger logger = Logger.getLogger(SampleAction.class);

	// SampleServiceHelper
	// SampleServiceHelper helper = new SampleServiceHelper();

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
		saveSample(request, sampleBean);
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
		SampleBean sampleBean = setupSample(theForm, request, location, false);
		theForm.set("sampleBean", sampleBean);
		return mapping.findForward("summaryView");
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
		// if session is expired or the url is clicked on directly.
		UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		if (user == null) {
			return summaryView(mapping, form, request, response);
		}
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String location = theForm.getString(Constants.LOCATION);
		if (StringUtils.isEmpty(location)) {
			location = Constants.LOCAL_SITE;
		}
		// "setupSample()" will retrieve and return the SampleBean.
		SampleBean sampleBean = setupSample(theForm, request, location, false);
		theForm.set("sampleBean", sampleBean);
		request.getSession().setAttribute("updateSample", "true");
		setupLookups(request, null);
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

	public boolean loginRequired() {
		return true;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PARTICLE);
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
		InitSampleSetup.getInstance().setPOCDropdowns(request);
	}

	public ActionForward savePointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		PointOfContactBean thePOC = sample.getThePOC();
		sample.addPointOfContact(thePOC);
		// save sample
		saveSample(request, sample);
		ActionForward forward = null;
		String updateSample = (String) request.getSession().getAttribute(
				"updateSample");
		if (updateSample == null) {
			forward = mapping.getInputForward();
		} else {
			// forward = summaryEdit(mapping, form, request, response);
			forward = mapping.findForward("summaryEdit");
			sample.setLocation(Constants.LOCAL_SITE);
			request.setAttribute("theSample", sample);
		}
		InitSampleSetup.getInstance().persistPOCDropdowns(request,
				sample.getDomain());
		return forward;
	}

	public ActionForward removePointOfContact(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sampleBean");
		PointOfContactBean thePOC = sample.getThePOC();
		sample.removePointOfContact(thePOC);
		ActionForward forward = null;
		if (sample.getDomain().getId() == null) {
			forward = mapping.getInputForward();
		} else {
			forward = summaryEdit(mapping, form, request, response);
		}
		// save sample
		saveSample(request, sample);
		return forward;
	}
}
