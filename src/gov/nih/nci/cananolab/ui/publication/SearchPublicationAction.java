package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.ClassUtils;
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

/**
 * This class searches canano publication based on user supplied criteria
 * 
 * @author tanq
 */

public class SearchPublicationAction extends AbstractDispatchAction {

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// get the page number from request
		int displayPage = getDisplayPage(request);

		List<PublicationBean> publicationBeans = null;
		// retrieve from session if it's not null and not the first page
		if (session.getAttribute("publicationSearchResults") != null
				&& displayPage > 0) {
			publicationBeans = new ArrayList<PublicationBean>((List) session
					.getAttribute("publicationSearchResults"));
		} else {
			publicationBeans = queryPublications(form, request);
			if (publicationBeans != null && !publicationBeans.isEmpty()) {
				session.setAttribute("publicationSearchResults",
						publicationBeans);
			} else {
				ActionMessages msgs = new ActionMessages();
				ActionMessage msg = new ActionMessage(
						"message.searchPublication.noresult");
				msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
				saveMessages(request, msgs);
				return mapping.getInputForward();
			}
		}
		// load publicationBean details 25 at a time for displaying
		// pass in page and size
		List<PublicationBean> pubBeansPerPage = getPublicationsPerPage(
				publicationBeans, displayPage,
				Constants.DISPLAY_TAG_TABLE_SIZE, request);
		request.setAttribute("publications", pubBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request
				.setAttribute("resultSize",
						new Integer(publicationBeans.size()));
		return mapping.findForward("success");
	}

	private List<PublicationBean> getPublicationsPerPage(
			List<PublicationBean> publicationBeans, int page, int pageSize,
			HttpServletRequest request) throws Exception {
		List<PublicationBean> loadedPublicationBeans = new ArrayList<PublicationBean>();
		PublicationService service = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		for (int i = page * pageSize; i < (page + 1) * pageSize; i++) {
			if (i < publicationBeans.size()) {
				String location = publicationBeans.get(i).getLocation();
				if (location.equals(Constants.LOCAL_SITE)
						|| StringUtils.isEmpty(location)) {
					service = new PublicationServiceLocalImpl();
				} else {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);
					service = new PublicationServiceRemoteImpl(serviceUrl);
				}
				String publicationId = publicationBeans.get(i).getDomainFile()
						.getId().toString();
				PublicationBean pubBean = service.findPublicationById(
						publicationId, user);
				pubBean.setLocation(location);
				loadedPublicationBeans.add(pubBean);
			}
		}
		return loadedPublicationBeans;
	}

	public List<PublicationBean> queryPublications(ActionForm form,
			HttpServletRequest request) throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String title = "";
		// publication type
		String category = "";
		String[] keywords = new String[0];
		String pubMedId = "";
		String digitalObjectId = "";
		String[] authors = new String[0];
		String sampleName = "";
		String[] researchAreas = new String[0];
		String[] nanomaterialEntityTypes = new String[0];
		String[] functionalizingEntityTypes = new String[0];
		String[] functionTypes = new String[0];
		String[] searchLocations = new String[0];

		title = (String) theForm.get("title");
		// strip wildcards from either end of title if entered
		title = StringUtils.stripWildcards(title);
		String titleOperand = (String) theForm.get("titleOperand");
		if (titleOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(title)) {
			title = "*" + title + "*";
		}
		category = (String) theForm.get("category");
		String keywordsStr = (String) theForm.get("keywordsStr");
		List<String> wordList = StringUtils.parseToWords(keywordsStr, "\r\n");
		if (wordList != null) {
			keywords = new String[wordList.size()];
			wordList.toArray(keywords);
		}
		pubMedId = (String) theForm.get("pubMedId");
		digitalObjectId = (String) theForm.get("digitalObjectId");
		String authorsStr = (String) theForm.get("authorsStr");
		List<String> authorList = StringUtils.parseToWords(authorsStr, "\r\n");
		if (authorList != null) {
			authors = new String[authorList.size()];
			authorList.toArray(authors);
		}

		sampleName = (String) theForm.get("sampleName");

		researchAreas = (String[]) theForm.get("researchArea");
		// publicationOrReport = (String[]) theForm
		// .get("publicationOrReport");
		nanomaterialEntityTypes = (String[]) theForm
				.get("nanomaterialEntityTypes");
		functionalizingEntityTypes = (String[]) theForm
				.get("functionalizingEntityTypes");
		functionTypes = (String[]) theForm.get("functionTypes");
		searchLocations = (String[]) theForm.get("searchLocations");
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if(searchLocations.length == 0){
			searchLocations = new String[1];
			searchLocations[0] = Constants.APP_OWNER;
		}
		if (searchLocations[0].indexOf("~") != -1 && gridNodeHostStr != null
				&& gridNodeHostStr.trim().length() > 0) {
			searchLocations = gridNodeHostStr.split("~");
		}		

		List<String> nanomaterialEntityClassNames = new ArrayList<String>();
		List<String> otherNanomaterialEntityTypes = new ArrayList<String>();
		for (int i = 0; i < nanomaterialEntityTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(nanomaterialEntityTypes[i]);
			if (className.length() == 0) {
				className = "OtherNanomaterialEntity";
				otherNanomaterialEntityTypes.add(nanomaterialEntityTypes[i]);
			} else {
				nanomaterialEntityClassNames.add(className);
			}
		}
		List<String> functionalizingEntityClassNames = new ArrayList<String>();
		List<String> otherFunctionalizingTypes = new ArrayList<String>();
		for (int i = 0; i < functionalizingEntityTypes.length; i++) {
			String className = ClassUtils
					.getShortClassNameFromDisplayName(functionalizingEntityTypes[i]);
			if (className.length() == 0) {
				className = "OtherFunctionalizingEntity";
				otherFunctionalizingTypes.add(functionalizingEntityTypes[i]);
			} else {
				functionalizingEntityClassNames.add(className);
			}
		}

		List<String> functionClassNames = new ArrayList<String>();
		List<String> otherFunctionTypes = new ArrayList<String>();
		if (functionTypes != null) {
			for (int i = 0; i < functionTypes.length; i++) {
				String className = ClassUtils
						.getShortClassNameFromDisplayName(functionTypes[i]);
				if (className.length() == 0) {
					className = "OtherFunction";
					otherFunctionTypes.add(functionTypes[i]);
				} else {
					functionClassNames.add(className);
				}
			}
		}

		// Publication
		List<PublicationBean> publications = new ArrayList<PublicationBean>();
		PublicationService publicationService = null;
		for (String location : searchLocations) {
			if (Constants.LOCAL_SITE.equals(location)) {
				publicationService = new PublicationServiceLocalImpl();
			} else {
				String serviceUrl = InitSetup.getInstance().getGridServiceUrl(
						request, location);
				publicationService = new PublicationServiceRemoteImpl(
						serviceUrl);
			}
			List<String> publicationIds = publicationService
					.findPublicationIdsBy(
							title,
							category,
							sampleName,
							researchAreas,
							keywords,
							pubMedId,
							digitalObjectId,
							authors,
							nanomaterialEntityClassNames.toArray(new String[0]),
							otherNanomaterialEntityTypes.toArray(new String[0]),
							functionalizingEntityClassNames
									.toArray(new String[0]),
							otherFunctionalizingTypes.toArray(new String[0]),
							functionClassNames.toArray(new String[0]),
							otherFunctionTypes.toArray(new String[0]), user);
			for (String id : publicationIds) {
				PublicationBean pubBean = new PublicationBean(id, location);
				publications.add(pubBean);
			}
		}
		return publications;
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InitPublicationSetup.getInstance().setPublicationDropdowns(request);
		InitSetup.getInstance().getGridNodesInContext(request);

		String[] selectedLocations = new String[] { Constants.LOCAL_SITE };
		String gridNodeHostStr = (String) request
				.getParameter("searchLocations");
		if (!StringUtils.isEmpty(gridNodeHostStr)) {
			selectedLocations = gridNodeHostStr.split("~");
		}

		if (Constants.LOCAL_SITE.equals(selectedLocations[0])
				&& selectedLocations.length == 1) {
			InitSampleSetup.getInstance().setLocalSearchDropdowns(request);
		} else {
			InitSampleSetup.getInstance().setRemoteSearchDropdowns(request);
		}

		InitPublicationSetup.getInstance().setDefaultResearchAreas(request);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		theForm.set("searchLocations", selectedLocations);

		HttpSession session = request.getSession();
		session.removeAttribute("publicationSearchResults");
		return mapping.getInputForward();
	}

	public Boolean canUserExecutePrivateDispatch(UserBean user)
			throws SecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				Constants.CSM_PG_PUBLICATION);
	}
}
