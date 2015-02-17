/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

/**
 * This class searches canano metadata based on user supplied criteria
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.core.AbstractDispatchBO;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.util.SampleUtil;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.restful.view.SimpleSearchSampleBean;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.exception.SecurityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SearchSampleBO extends AbstractDispatchBO {
	private Logger logger = Logger.getLogger(SearchSampleBO.class);

	private DataAvailabilityService dataAvailabilityService;

	public List search(SearchSampleForm form, HttpServletRequest request) 
	 throws Exception {
		
		List<String> messages = new ArrayList<String>();
		
		HttpSession session = request.getSession();
		// get the page number from request
		
		//TODO
		int displayPage = form.getPage(); ///getDisplayPage(request);

		// use one local impl to improve performance
		this.setServiceInSession(request);

		List<SampleBean> sampleBeans = new ArrayList<SampleBean>();
		// retrieve from session if it's not null and not first page
		if (session.getAttribute("sampleSearchResults") != null
				&& displayPage > 0) {
			sampleBeans = new ArrayList<SampleBean>(
					(List) session.getAttribute("sampleSearchResults"));
		} else {
			sampleBeans = querySamples(form, request);
			if (sampleBeans != null && !sampleBeans.isEmpty()) {
				session.setAttribute("sampleSearchResults", sampleBeans);
			} else {
				messages.add(PropertyUtil.getProperty("sample", "message.searchSample.noresult"));
				return messages;
			}
		}

		// load sampleBean details 25 at a time for displaying
		// pass in page and size
		List<SampleBean> sampleBeansPerPage = getSamplesPerPage(sampleBeans,
				displayPage, Constants.DISPLAY_TAG_TABLE_SIZE, request);
		// in case any samples has been filtered during loading of sample
		// information. e.g. POC is missing
		
		if (sampleBeansPerPage.isEmpty()) {
			messages.add(PropertyUtil.getProperty("sample", "message.searchSample.noresult"));
			return messages;
		}
		
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user != null) {
			loadUserAccess(request, sampleBeansPerPage);
		}
		//set in sessionScope so user can go back to the result from the sample summary page
		request.getSession().setAttribute("samples", sampleBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work

		// set in sessionScope so user can go back to the result from the sample
		// summary page
		request.getSession().setAttribute("resultSize", new Integer(sampleBeans.size()));
		
		//return mapping.findForward("success");
		//UserBean user = (UserBean) (request.getSession().getAttribute("user"));
		List<SimpleSearchSampleBean> simpleBeans = transfertoSimpleSampleBeans(sampleBeansPerPage, (UserBean)session.getAttribute("user"));
		
		return simpleBeans;
	}
	
	/**
	 * 
	 */
	protected List<SimpleSearchSampleBean> transfertoSimpleSampleBeans(List<SampleBean> sampleBeans,
			UserBean user) {
		List<SimpleSearchSampleBean> simpleBeans = new ArrayList<SimpleSearchSampleBean>();
		
		for (SampleBean bean : sampleBeans) {
			
			SimpleSearchSampleBean simpleBean = new SimpleSearchSampleBean();
			simpleBean.transferSampleBeanForBasicResultView(bean, user);
			simpleBeans.add(simpleBean);
		}
		
		return simpleBeans;
	}

	private List<SampleBean> querySamples(SearchSampleForm form,
			HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		
		List<SampleBean> sampleBeans = new ArrayList<SampleBean>();
		String samplePointOfContact = (String) form.getSamplePointOfContact();
		// strip wildcards at either end if entered by user
		samplePointOfContact = StringUtils.stripWildcards(samplePointOfContact);
		//String pocOperand = Constants.STRING_OPERAND_CONTAINS; 
		String pocOperand = (String) form.getPocOperand();
		if (pocOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(samplePointOfContact)) {
			samplePointOfContact = "*" + samplePointOfContact + "*";
		}
		String sampleName = (String) form.getSampleName();
		// strip wildcards at either end if entered by user
		sampleName = StringUtils.stripWildcards(sampleName);
		//String nameOperand = Constants.STRING_OPERAND_CONTAINS; //(String) form.getNameOperand();
		String nameOperand = (String) form.getNameOperand(); 
		if (nameOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(sampleName)) {
			sampleName = "*" + sampleName + "*";
		}
		String[] nanomaterialEntityTypes = new String[0];
		String[] functionalizingEntityTypes = new String[0];
		String[] functionTypes = new String[0];
		String[] characterizations = new String[0];
		String texts = "";

		if (form != null) {
			nanomaterialEntityTypes = (String[]) form.getNanomaterialEntityTypes();
			if (nanomaterialEntityTypes == null || nanomaterialEntityTypes.length == 0) 
				nanomaterialEntityTypes = new String[0];
			
			functionalizingEntityTypes = (String[]) form.getFunctionalizingEntityTypes();
			if (functionalizingEntityTypes == null || functionalizingEntityTypes.length == 0) 
				functionalizingEntityTypes = new String[0];
			
			functionTypes = (String[]) form.getFunctionTypes();
			if (functionTypes == null || functionTypes.length == 0) 
				functionTypes = new String[0];
			
			characterizations = (String[]) form.getCharacterizations();
			if (characterizations == null || characterizations.length == 0) {
				characterizations = new String[0];
			}
				//characterizations = SampleUtil.getDefaultListFromSessionByType("nanomaterialEntityTypes", session);
			texts = form.getText().trim();

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
			if (className.length() == 0 || characterizations[i].startsWith("other") ) {
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
				samplePointOfContact,
				nanomaterialEntityClassNames.toArray(new String[0]),
				otherNanomaterialEntityTypes.toArray(new String[0]),
				functionalizingEntityClassNames.toArray(new String[0]),
				otherFunctionalizingTypes.toArray(new String[0]),
				functionClassNames.toArray(new String[0]),
				otherFunctionTypes.toArray(new String[0]),
				charaClassNames.toArray(new String[0]),
				otherCharacterizationTypes.toArray(new String[0]), words);
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
		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		// Map<String, Set<DataAvailabilityBean>> dataAvailabilityMapPerPage =
		// new HashMap<String, Set<DataAvailabilityBean>>();
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < sampleBeans.size()) {
				String sampleId = sampleBeans.get(i).getDomain().getId()
						.toString();

				//SampleBean sampleBean = service.findSampleBasicById(sampleId, false);
				SampleBean sampleBean = service.findSampleById(sampleId, false);
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
					Set<DataAvailabilityBean> dataAvailability = dataAvailabilityService
							.findDataAvailabilityBySampleId(sampleId,
									securityService);
					// dataAvailabilityMapPerPage.put(sampleId,
					// dataAvailability);
					if (!dataAvailability.isEmpty()
							&& dataAvailability.size() > 0) {
						sampleBean.setDataAvailability(dataAvailability);
						sampleBean.setHasDataAvailability(true);
						calculateDataAvailabilityScore(request, sampleBean,
								dataAvailability);
					}
					loadedSampleBeans.add(sampleBean);
				}
			}
		}
		// request.getSession().setAttribute("dataAvailabilityMapPerPage",
		// dataAvailabilityMapPerPage);
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

	public Map<String, List<String>> setup(HttpServletRequest request)
			throws Exception {

		InitSampleSetup.getInstance().setLocalSearchDropdowns(request);
		request.getSession().removeAttribute("sampleSearchResults");
		
		return SampleUtil.reformatLocalSearchDropdownsInSession(request.getSession());
		
	}

	public DataAvailabilityService getDataAvailabilityService() {
		return dataAvailabilityService;
	}

	public void setDataAvailabilityService(
			DataAvailabilityService dataAvailabilityService) {
		this.dataAvailabilityService = dataAvailabilityService;
	}

	private void calculateDataAvailabilityScore(HttpServletRequest request, SampleBean sampleBean,
			Set<DataAvailabilityBean> dataAvailability) {

		//ServletContext appContext = this.getServlet().getServletContext();
		ServletContext appContext = request.getSession().getServletContext();
		SortedSet<String> minchar = (SortedSet<String>) appContext
				.getAttribute("MINChar");
		Map<String, String> attributes = (Map<String, String>) appContext
				.getAttribute("caNano2MINChar");
		sampleBean.calculateDataAvailabilityScore(dataAvailability, minchar,
				attributes);
	}

	private SampleService setServiceInSession(HttpServletRequest request) {
		try {
			SecurityService securityService = super
					.getSecurityServiceFromSession(request);
			SampleService sampleService = new SampleServiceLocalImpl(
					securityService);
			request.getSession().setAttribute("sampleService", sampleService);
			return sampleService;
		} catch (SecurityException e) {
			logger.error("Unable to get SecurityServiceFromSession: " + e.getMessage());
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param httpRequest
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<String> getCharacterizationByType(HttpServletRequest httpRequest, String type) 
		throws Exception {
		SortedSet<String> charNames = InitCharacterizationSetup.getInstance()
				.getCharNamesByCharType(httpRequest,
						type);
		
		return new ArrayList<String>(charNames);
	}
}
