package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the search study.
 *
 * @author lethai
 */
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.service.study.StudyService;
import gov.nih.nci.cananolab.service.study.impl.StudyServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class SearchStudyAction extends AbstractDispatchAction {

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("isAnimalStudy", false);
		InitStudySetup.getInstance().setStudyDropdowns(request);
		request.getSession().removeAttribute("studySearchResults");
		return mapping.getInputForward();
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		// get the page number from request
		int displayPage = getDisplayPage(request);

		// use one local impl to improve performance
		this.setServiceInSession(request);

		List<StudyBean> studyBeans = new ArrayList<StudyBean>();
		// retrieve from session if it's not null and not first page
		if (session.getAttribute("studySearchResults") != null
				&& displayPage > 0) {
			studyBeans = new ArrayList<StudyBean>((List) session
					.getAttribute("studySearchResults"));
		} else {
			studyBeans = queryStudyIds(form, request);
			if (studyBeans != null && !studyBeans.isEmpty()) {
				session.setAttribute("studySearchResults", studyBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.searchStudy.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}
		//testing
		/*Study domain = new Study();
		domain.setId(1L);
		domain.setName("MIT_KELLY")
		StudyBean study1 = new StudyBean();
		study1.setName("MIT_KELLY");
		study1.setTitle("in vitro profiling of nanoparticle libraries");
		study1.setStudySample(new SampleBean());
		study1.setOwnerName("michal");
		study1.setPointOfContact("MIT_MGH (Stanley Y Shaw)");

		StudyBean study2 = new StudyBean();
		study1.setName("Efficacy of nanoparticle Sample");
		//SampleBean studySample = new SampleBean();
		//studySample.se
		study2.setStudySample(new SampleBean());
		study2.setOwnerName("Guest2");
		study2.setPointOfContact("Guest2");
		List<StudyBean> studyBeansPerPage = new ArrayList<StudyBean>();
		studyBeansPerPage.add(study1);
		studyBeansPerPage.add(study2);
		request.setAttribute("studies", studyBeansPerPage);*/
		// get the total size of collection , required for display tag to
		// get the pagination to work
		/*request.setAttribute("resultSize", new Integer(2));
		return mapping.findForward("searchResult");*/
		
		
		//end testing.... 
		
		
		// load studyBean details 25 at a time for displaying
		// pass in page and size
		List<StudyBean> studyBeansPerPage = getStudiesPerPage(studyBeans,
				displayPage, Constants.DISPLAY_TAG_TABLE_SIZE, request);
		// in case any samples has been filtered during loading of sample
		// information. e.g. POC is missing
		if (studyBeansPerPage.isEmpty()) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"message.searchStudy.noresult");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			saveMessages(request, msgs);
			return mapping.getInputForward();
		}
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (user != null) {
			//loadUserAccess(request, studyBeansPerPage);
		}
		request.setAttribute("studies", studyBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request.setAttribute("resultSize", new Integer(studyBeans.size()));

		// allow user to go back to the search results via the cache
		response.setHeader("Cache-Control", "private");
		return mapping.findForward("searchResult");

		
	}
	/**
	 * load the study ids with the given parameters
	 * @param form
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private List<StudyBean> queryStudyIds(ActionForm form,
			HttpServletRequest request) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		List<StudyBean> studyBeans = new ArrayList<StudyBean>();
		String studyName =(String) theForm.get("studyName");
		String studyNameOperand = (String) theForm.get("studyNameOperand");
		if (studyNameOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(studyName)) {
			studyName = "*" + studyName + "*";
		}
		System.out.println("studyName: " + studyName);
		String studyPointOfContact = (String) theForm
		.get("studyPointOfContact");
		// strip wildcards at either end if entered by user
		studyPointOfContact = StringUtils.stripWildcards(studyPointOfContact);
		String pocOperand = (String) theForm.get("pocOperand");
		if (pocOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(studyPointOfContact)) {
			studyPointOfContact = "*" + studyPointOfContact + "*";
		}
		System.out.println("studyPOCName: " + studyPointOfContact
				);
		String studyType = (String)theForm.get("studyType");
		String studyDesignType = (String)theForm.get("studyDesignType");
		Boolean isAnimalStudy = (Boolean)theForm.get("isAnimalStudy");
		System.out.println("isAnimalStudy: " + isAnimalStudy);
		String sampleName = (String) theForm.get("sampleName");
		// strip wildcards at either end if entered by user
		sampleName = StringUtils.stripWildcards(sampleName);
		String sampleNameOperand = (String) theForm.get("sampleNameOperand");
		if (sampleNameOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(sampleName)) {
			sampleName = "*" + sampleName + "*";
		}
		
		System.out.println("sampleName: " + sampleName);
		
		String disease = (String) theForm.get("disease");
		// strip wildcards at either end if entered by user
		disease = StringUtils.stripWildcards(disease);
		String diseaseOperand = (String) theForm.get("diseaseOperand");
		if (diseaseOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(disease)) {
			disease = "*" + disease + "*";
		}
		
		System.out.println("disease: " + disease);
		
		String texts="";
		
		if(theForm != null){
			texts  = ((String) theForm.get("text")).trim();
		}
		List<String> wordList = StringUtils.parseToWords(texts, "\r\n");
		String[] words = null;
		if (wordList != null) {
			words = new String[wordList.size()];
			wordList.toArray(words);
		}
		String studyOwner = (String) theForm.get("studyOwner");
		// strip wildcards at either end if entered by user
		studyOwner = StringUtils.stripWildcards(studyOwner);
		String ownerOperand = (String) theForm.get("ownerOperand");
		if (ownerOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(studyOwner)) {
			studyOwner = "*" + studyOwner + "*";
		}		
		System.out.println("studyOwner: " + studyOwner);
		
		StudyService studyService = setServiceInSession(request);
		//String studyName, String studyPointOfContact, String studyType, String studyDesignType,
		//String sampleName, Boolean isAnimalStudy, String diseases, String text, String studyOwner
		List<String> studyIds = studyService.findStudyIdsBy(studyName, studyPointOfContact, studyType,
				studyDesignType,sampleName,isAnimalStudy, disease, words, studyOwner );
		
		for (String id : studyIds) {
			// empty studyBean that only has id;
			StudyBean studyBean = new StudyBean(id);
			studyBeans.add(studyBean);
		}

		return studyBeans;
	}
	/**
	 * load the study detail for each pape
	 * @param studyBeans
	 * @param displayPage
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private List<StudyBean> getStudiesPerPage(List<StudyBean> studyBeans,
			int page, int pageSize, HttpServletRequest request)
			throws Exception {
		List<StudyBean> loadedStudyBeans = new ArrayList<StudyBean>();
		StudyService service = null;
		if (request.getSession().getAttribute("studyService") != null) {
			service = (StudyService) request.getSession().getAttribute(
					"studyService");
		} else {
			service = this.setServiceInSession(request);
		}
		SecurityService securityService = (SecurityService) request
				.getSession().getAttribute("securityService");
		if (securityService == null) {
			securityService = new SecurityService(
					AccessibilityBean.CSM_APP_NAME);
		}
		
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if(i<studyBeans.size()){
				String studyId = studyBeans.get(i).getDomain().getId()
				.toString();
				StudyBean studyBean = service.findStudyById(studyId, false);
				if(studyBean != null){
					loadedStudyBeans.add(studyBean);
				}
			}
		}
		
		return loadedStudyBeans;
	}
	private StudyService setServiceInSession(HttpServletRequest request)
		throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		StudyService studyService = new StudyServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("studyService", studyService);
		return studyService;
	}
}
