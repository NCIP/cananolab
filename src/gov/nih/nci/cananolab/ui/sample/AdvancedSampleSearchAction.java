package gov.nih.nci.cananolab.ui.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.28 2008-10-01 18:41:26 tanq Exp $ */

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class AdvancedSampleSearchAction extends AbstractDispatchAction {

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) theForm
				.get("searchBean");
		// retrieve from session if it's not null
		List<AdvancedSampleBean> sampleBeans = new ArrayList<AdvancedSampleBean>();
		if (session.getAttribute("advancedSampleSearchResults") != null) {
			sampleBeans = new ArrayList<AdvancedSampleBean>((List) session
					.getAttribute("advancedSampleSearchResults"));
		} else {
			sampleBeans = querySamples(form, request);
			if (sampleBeans != null && !sampleBeans.isEmpty()) {
				session
						.setAttribute("advancedSampleSearchResults",
								sampleBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.advancedSampleSearch.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}
		// load advancedSampleBean details 25 at a time for displaying
		// get the page number from request
		int displayPage = getDisplayPage(request);
		// pass in page and size
		List<AdvancedSampleBean> sampleBeansPerPage = getSamplesPerPage(
				sampleBeans, displayPage, Constants.DISPLAY_TAG_TABLE_SIZE,
				request, searchBean);
		request.setAttribute("advancedSamples", sampleBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request.setAttribute("resultSize", new Integer(sampleBeans.size()));
		return mapping.findForward("success");
	}

	private List<AdvancedSampleBean> querySamples(ActionForm form,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		UserBean user = (UserBean) session.getAttribute("user");
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) theForm
				.get("searchBean");
		SampleService service = new SampleServiceLocalImpl();
		List<String> sampleNames = service.findSampleNamesByAdvancedSearch(
				searchBean, user);
		List<AdvancedSampleBean> sampleBeans = new ArrayList<AdvancedSampleBean>();
		for (String name : sampleNames) {
			AdvancedSampleBean sampleBean = new AdvancedSampleBean(name,
					Constants.APP_OWNER);
			sampleBeans.add(sampleBean);
		}
		return sampleBeans;
	}

	private List<AdvancedSampleBean> getSamplesPerPage(
			List<AdvancedSampleBean> sampleBeans, int page, int pageSize,
			HttpServletRequest request, AdvancedSampleSearchBean searchBean)
			throws Exception {
		List<AdvancedSampleBean> loadedSampleBeans = new ArrayList<AdvancedSampleBean>();
		SampleService service = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < sampleBeans.size()) {
				String location = sampleBeans.get(i).getLocation();
				String sampleName = sampleBeans.get(i).getSampleName();
				if (location.equals(Constants.LOCAL_SITE)) {
					service = new SampleServiceLocalImpl();
				} else {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);
					service = new SampleServiceRemoteImpl(serviceUrl);
				}
				AdvancedSampleBean loadedAdvancedSample = service
						.findAdvancedSampleByAdvancedSearch(sampleName,
								searchBean, user);
				loadedAdvancedSample.setLocation(location);
				loadedSampleBeans.add(loadedAdvancedSample);
			}
		}
		return loadedSampleBeans;
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// clear the search results and start over
		request.getSession().removeAttribute("advancedSampleSearchResults");
		request
				.setAttribute(
						"onloadJavascript",
						"displaySampleQueries(); displayCompositionQueries(); displayCharacterizationQueries()");
		return mapping.findForward("inputForm");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.getSession().removeAttribute("advancedSampleSearchForm");
		InitCharacterizationSetup.getInstance().getCharacterizationTypes(
				request);
		request.getSession().removeAttribute("advancedSampleSearchResults");
		return mapping.getInputForward();
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user) throws SecurityException {
		return true;
	}
}
