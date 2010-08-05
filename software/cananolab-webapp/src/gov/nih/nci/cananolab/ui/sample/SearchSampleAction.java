package gov.nih.nci.cananolab.ui.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

/* CVS $Id: SearchSampleAction.java,v 1.28 2008-10-01 18:41:26 tanq Exp $ */

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchSampleAction extends AbstractDispatchAction {

	private DataAvailabilityService dataAvailabilityService;

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// get the page number from request
		int displayPage = getDisplayPage(request);

		// use one local impl to improve performance
		this.setServiceInSession(request);

		List<SampleBean> sampleBeans = new ArrayList<SampleBean>();
		// retrieve from session if it's not null and not first page
		if (session.getAttribute("sampleSearchResults") != null
				&& displayPage > 0) {
			sampleBeans = new ArrayList<SampleBean>((List) session
					.getAttribute("sampleSearchResults"));
		} else {
			sampleBeans = querySamples(form, request);
			if (sampleBeans != null && !sampleBeans.isEmpty()) {
				session.setAttribute("sampleSearchResults", sampleBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.searchSample.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}

		// load sampleBean details 25 at a time for displaying
		// pass in page and size
		List<SampleBean> sampleBeansPerPage = getSamplesPerPage(sampleBeans,
				displayPage, Constants.DISPLAY_TAG_TABLE_SIZE, request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user != null) {
			loadUserAccess(request, sampleBeansPerPage);
		}
		request.setAttribute("samples", sampleBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request.setAttribute("resultSize", new Integer(sampleBeans.size()));
		return mapping.findForward("success");
	}

	private List<SampleBean> querySamples(ActionForm form,
			HttpServletRequest request) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<SampleBean> sampleBeans = new ArrayList<SampleBean>();
		String samplePointOfContact = (String) theForm
				.get("samplePointOfContact");
		// strip wildcards at either end if entered by user
		samplePointOfContact = StringUtils.stripWildcards(samplePointOfContact);
		String pocOperand = (String) theForm.get("pocOperand");
		if (pocOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(samplePointOfContact)) {
			samplePointOfContact = "*" + samplePointOfContact + "*";
		}
		String sampleName = (String) theForm.get("sampleName");
		// strip wildcards at either end if entered by user
		sampleName = StringUtils.stripWildcards(sampleName);
		String nameOperand = (String) theForm.get("nameOperand");
		if (pocOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(sampleName)) {
			sampleName = "*" + sampleName + "*";
		}
		String[] nanomaterialEntityTypes = new String[0];
		String[] functionalizingEntityTypes = new String[0];
		String[] functionTypes = new String[0];
		String[] characterizations = new String[0];
		String texts = "";

		if (theForm != null) {
			nanomaterialEntityTypes = (String[]) theForm
					.get("nanomaterialEntityTypes");
			functionalizingEntityTypes = (String[]) theForm
					.get("functionalizingEntityTypes");
			functionTypes = (String[]) theForm.get("functionTypes");
			characterizations = (String[]) theForm.get("characterizations");
			texts = ((String) theForm.get("text")).trim();

		}

		// convert nanomaterial entity display names into short class names
		// and
		// other types
		List<String> nanomaterialEntityClassNames = new ArrayList<String>();
		List<String> otherNanomaterialEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanomaterialEntityTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(nanomaterialEntityTypes[i]);
			Class clazz = ClassUtils.getFullClass("nanomaterial." + className);
			if (clazz == null) {
				className = "OtherNanomaterialEntity";
				otherNanomaterialEntityTypes.add(nanomaterialEntityTypes[i]);
			} else {
				nanomaterialEntityClassNames.add(className);
			}
		}

		// convert functionalizing entity display names into short class
		// names
		// and other types
		List<String> functionalizingEntityClassNames = new ArrayList<String>();
		List<String> otherFunctionalizingTypes = new ArrayList<String>();
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(functionalizingEntityTypes[i]);
			Class clazz = ClassUtils.getFullClass("agentmaterial." + className);
			if (clazz == null) {
				className = "OtherFunctionalizingEntity";
				otherFunctionalizingTypes.add(functionalizingEntityTypes[i]);
			} else {
				functionalizingEntityClassNames.add(className);
			}
		}

		// convert function display names into short class names and other
		// types
		List<String> functionClassNames = new ArrayList<String>();
		List<String> otherFunctionTypes = new ArrayList<String>();
		for (int i = 0; i < functionTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(functionTypes[i]);
			Class clazz = ClassUtils.getFullClass("function." + className);
			if (clazz == null) {
				className = "OtherFunction";
				otherFunctionTypes.add(functionTypes[i]);
			} else {
				functionClassNames.add(className);
			}
		}

		// convert characterization display names into short class names and
		// other types
		List<String> charaClassNames = new ArrayList<String>();
		List<String> otherCharacterizationTypes = new ArrayList<String>();
		for (int i = 0; i < characterizations.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(characterizations[i]);
			if (className.length() == 0) {
				className = "OtherCharacterization";
				otherCharacterizationTypes.add(characterizations[i]);
			} else {
				charaClassNames.add(className);
			}
		}

		List<String> wordList = StringUtils.parseToWords(texts, "\r\n");
		String[] words = null;
		if (wordList != null) {
			words = new String[wordList.size()];
			wordList.toArray(words);
		}
		SampleService service = service = (SampleService) request.getSession()
				.getAttribute("sampleService");
		List<String> sampleIds = service.findSampleIdsBy(sampleName,
				samplePointOfContact, nanomaterialEntityClassNames
						.toArray(new String[0]), otherNanomaterialEntityTypes
						.toArray(new String[0]),
				functionalizingEntityClassNames.toArray(new String[0]),
				otherFunctionalizingTypes.toArray(new String[0]),
				functionClassNames.toArray(new String[0]), otherFunctionTypes
						.toArray(new String[0]), charaClassNames
						.toArray(new String[0]), otherCharacterizationTypes
						.toArray(new String[0]), words);
		for (String id : sampleIds) {
			// empty sampleBean that only has id;
			SampleBean sampleBean = new SampleBean(id);
			sampleBeans.add(sampleBean);
		}
		return sampleBeans;
	}

	private List<SampleBean> getSamplesPerPage(List<SampleBean> sampleBeans,
			int page, int pageSize, HttpServletRequest request)
			throws Exception {
		List<SampleBean> loadedSampleBeans = new ArrayList<SampleBean>();
		SampleService service = null;
		if (request.getSession().getAttribute("sampleService") != null) {
			service = (SampleService) request.getSession().getAttribute(
					"sampleService");
		} else {
			service = this.setServiceInSession(request);
		}
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		if(securityService == null){
			securityService = new SecurityService(Constants.CSM_APP_NAME);
		}
		Map<String, List<DataAvailabilityBean>> dataAvailabilityMapPerPage = new HashMap<String, List<DataAvailabilityBean>>();
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < sampleBeans.size()) {
				String sampleId = sampleBeans.get(i).getDomain().getId()
						.toString();

				SampleBean sampleBean = service.findSampleById(sampleId);
				if (sampleBean != null) {
					Sample sample = sampleBean.getDomain();
					SampleServiceHelper helper = ((SampleServiceLocalImpl) service)
							.getHelper();
					// load summary information
					sampleBean.setCharacterizationClassNames(helper
							.getStoredCharacterizationClassNames(sample)
							.toArray(new String[0]));
					sampleBean.setFunctionalizingEntityClassNames(helper
							.getStoredFunctionalizingEntityClassNames(sample)
							.toArray(new String[0]));
					sampleBean.setNanomaterialEntityClassNames(helper
							.getStoredNanomaterialEntityClassNames(sample)
							.toArray(new String[0]));
					sampleBean.setFunctionClassNames(helper
							.getStoredFunctionClassNames(sample).toArray(
									new String[0]));
					// get data availability for the samples
					List<DataAvailabilityBean> dataAvailability = dataAvailabilityService
							.findDataAvailabilityBySampleId(sampleId,
									securityService);
					dataAvailabilityMapPerPage.put(sampleId, dataAvailability);
					if (!dataAvailability.isEmpty()
							&& dataAvailability.size() > 0) {
						sampleBean.setDataAvailability(dataAvailability);
						sampleBean.setHasDataAvailability(true);
						calculateDataAvailabilityScore(sampleBean,
								dataAvailability);
					}
					loadedSampleBeans.add(sampleBean);
				}
			}
		}
		request.getSession().setAttribute("dataAvailabilityMapPerPage",
				dataAvailabilityMapPerPage);
		return loadedSampleBeans;
	}

	private void loadUserAccess(HttpServletRequest request,
			List<SampleBean> sampleBeans) throws Exception {
		List<String> sampleIds = new ArrayList<String>();
		for (SampleBean sampleBean : sampleBeans) {
			sampleIds.add(sampleBean.getDomain().getId().toString());
		}
		SecurityService securityService = getSecurityServiceFromSession(request);
		Map<String, List<String>> privilegeMap = securityService
				.getPrivilegeMap(sampleIds);
		for (SampleBean sampleBean : sampleBeans) {
			List<String> privileges = privilegeMap.get(sampleBean.getDomain()
					.getId().toString());
			if (privileges.contains(AccessibilityBean.CSM_UPDATE_PRIVILEGE)) {
				sampleBean.setUserUpdatable(true);
			} else {
				sampleBean.setUserUpdatable(false);
			}
			if (privileges.contains(AccessibilityBean.CSM_DELETE_PRIVILEGE)) {
				sampleBean.setUserDeletable(true);
			} else {
				sampleBean.setUserDeletable(false);
			}
		}
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// InitSetup.getInstance().getGridNodesInContext(request);

		InitSampleSetup.getInstance().setLocalSearchDropdowns(request);

		request.getSession().removeAttribute("sampleSearchResults");
		return mapping.getInputForward();
	}

	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}

	private void calculateDataAvailabilityScore(SampleBean sampleBean,
			List<DataAvailabilityBean> dataAvailability) {

		ServletContext appContext = this.getServlet().getServletContext();
		SortedSet<String> minchar = (SortedSet<String>) appContext
				.getAttribute("MINChar");
		Map<String, String> attributes = (Map<String, String>) appContext
				.getAttribute("caNano2MINChar");
		sampleBean.calculateDataAvailabilityScore(dataAvailability, minchar,
				attributes);
	}

	private SampleService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return sampleService;
	}
}
