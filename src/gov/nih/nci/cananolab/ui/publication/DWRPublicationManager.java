package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.sample.InitSampleSetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRPublicationManager {

	Logger logger = Logger.getLogger(DWRPublicationManager.class);

	public DWRPublicationManager() {
	}

	public PublicationBean clearPublication() {		
		WebContext wctx = WebContextFactory.get();
		PublicationForm form = (PublicationForm) wctx.getSession()
				.getAttribute("publicationForm");
		PublicationBean pbean = new PublicationBean();
		form.set("publication", pbean);
		return pbean;
	}

	public PublicationBean retrievePubMedInfo(String pubmedID) {
		PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
		WebContext wctx = WebContextFactory.get();
		PublicationForm form = (PublicationForm) wctx.getSession()
				.getAttribute("publicationForm");
		PublicationBean pbean = (PublicationBean) form.get("publication");

		if (!StringUtils.isEmpty(pubmedID) && !pubmedID.equals("0")) {
			Long pubMedIDLong = 0L;
			try {
				pubMedIDLong = Long.valueOf(pubmedID);
			} catch (Exception ex) {
				logger.error("invalid PubMed ID");
				return null;
			}
			phandler.parsePubMedXML(pubMedIDLong, pbean);
		}
		return pbean;
	}

	public String[] getPublicationCategories(String searchLocations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if (Constants.LOCAL_SITE.equals(searchLocations)) {
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal) {
				types = InitSetup.getInstance().getDefaultAndOtherLookupTypes(
						request, "publicationCategories", "Publication",
						"category", "otherCategory", true);
			} else {
				types = LookupService.findLookupValues("Publication",
						"category");
			}
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem getting publication types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getPublicationStatuses(String searchLocations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if (Constants.LOCAL_SITE.equals(searchLocations)) {
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal) {
				types = InitSetup.getInstance().getDefaultAndOtherLookupTypes(
						request, "publicationStatuses", "Publication",
						"status", "otherStatus", true);
			} else {
				types = LookupService.findLookupValues("Publication", "status");
			}
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem getting publication statuses: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getAllSampleNames() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		try {
			SortedSet<String> allSampleNames = InitSampleSetup.getInstance()
					.getAllSampleNames(wctx.getHttpServletRequest(), user);
			return allSampleNames.toArray(new String[0]);
		} catch (Exception e) {
			logger
					.error(
							"Problem getting all sample names for publication submission \n",
							e);
			return new String[] { "" };
		}
	}

	public PublicationBean addAuthor(Author author) throws PublicationException {
		DynaValidatorForm pubForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("publicationForm"));
		if (pubForm == null) {
			return null;
		}
		PublicationBean pubBean = (PublicationBean) (pubForm.get("publication"));
		pubBean.addAuthor(author);
		return pubBean;
	}

	public PublicationBean deleteAuthor(Author author)
			throws ExperimentConfigException {
		DynaValidatorForm pubForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("publicationForm"));
		if (pubForm == null) {
			return null;
		}
		PublicationBean pubBean = (PublicationBean) (pubForm.get("publication"));
		pubBean.removeAuthor(author);
		return pubBean;
	}
	
	public String getPublicCounts(String[] locations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		request.getSession().removeAttribute("publicationSearchResults");
		if (locations.length==0){
			return null;
		}
		Integer counts = 0;
		PublicationService service = null;
		for (String location : locations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				try {
					service = new PublicationServiceLocalImpl();
					counts += service.getNumberOfPublicPublications();
				} catch (Exception e) {
					logger
							.error("Error obtaining counts of public publications from local site.");
				}
			} else {
				try {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);

					service = new PublicationServiceRemoteImpl(serviceUrl);
					counts += service.getNumberOfPublicPublications();
				} catch (Exception e) {
					logger
							.error("Error obtaining counts of public publications from "
									+ location);
				}
			}
		}
		return counts.toString();
	}
}