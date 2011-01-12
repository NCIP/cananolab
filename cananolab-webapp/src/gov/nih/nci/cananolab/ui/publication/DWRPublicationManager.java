package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRPublicationManager {

	Logger logger = Logger.getLogger(DWRPublicationManager.class);
	PublicationServiceLocalImpl service;
	SecurityService securityService;

	private PublicationServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		securityService = (SecurityService) wctx.getSession().getAttribute(
				"securityService");
		service = new PublicationServiceLocalImpl(securityService);
		return service;
	}

	public PublicationBean clearPublication() {
		WebContext wctx = WebContextFactory.get();
		DynaValidatorForm form = (DynaValidatorForm) wctx.getSession()
				.getAttribute("publicationForm");
		PublicationBean pbean = new PublicationBean();
		if (form != null) {
			form.set("publication", pbean);
		}
		return pbean;
	}

	public PublicationBean searchPubMedById(String pubmedID) {
		// New a pubBean each time, so we know if parsing is success or not.
		PublicationBean newPubBean = new PublicationBean();
		if (!StringUtils.isEmpty(pubmedID) && !pubmedID.equals("0")) {
			try {
				newPubBean = getService().getPublicationFromPubMedXML(pubmedID);
			} catch (Exception ex) {
				logger.warn("Invalid PubMed ID: " + pubmedID);
			}
		}
		return newPubBean;
	}

	public PublicationBean retrievePubMedInfo(String pubmedID) {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		DynaValidatorForm form = (DynaValidatorForm) wctx.getSession()
				.getAttribute("publicationForm");
		if (form == null) {
			return null;
		}
		PublicationBean publicationBean = (PublicationBean) form
				.get("publication");

		// New a pubBean each time, so we know if parsing is success or not.
		PublicationBean newPubBean = searchPubMedById(pubmedID);
		// Copy PubMed data into form bean
		publicationBean.copyPubMedFieldsFromPubMedXML(newPubBean);
		return publicationBean;
	}

	public String getExistingPubMedPublication(String pubmedID) {
		String publicationId = null;
		try {
			Publication publication = getService().getHelper()
					.findPublicationByKey("pubMedId", new Long(pubmedID));
			if (publication != null) {
				publicationId = publication.getId().toString();
			}
		} catch (NoAccessException ne) {
			logger.info("User can't access the publication with Pub Med ID "
					+ pubmedID);
		} catch (Exception e) {
			logger.info("Error in retrieving publication with Pub Med ID "
					+ pubmedID);
		}
		return publicationId;
	}

	public String getExistingDOIPublication(String doi) {
		String publicationId = null;
		try {
			Publication publication = getService().getHelper()
					.findPublicationByKey("digitalObjectId", doi);
			if (publication != null) {
				publicationId = publication.getId().toString();
			}
		} catch (NoAccessException ne) {
			logger.info("User can't access the publication with DOI " + doi);
		} catch (Exception e) {
			logger.info("Error in retrieving publication with DOI " + doi);
		}
		return publicationId;
	}

	public String getExistingNonPubMedDOIPublication(String category,
			String title, Author firstAuthor) {
		String publicationId = null;
		try {
			Publication publication = getService().getHelper()
					.findNonPubMedNonDOIPublication(category, title,
							firstAuthor.getFirstName(),
							firstAuthor.getLastName());
			if (publication != null) {
				publicationId = publication.getId().toString();
			}
		} catch (NoAccessException ne) {
			logger.info("User can't access the publication");
		} catch (Exception e) {
			logger.info("Error in retrieving publication");
		}
		return publicationId;
	}

	public PublicationBean getExistingPublicationById(String id) {
		PublicationBean dbPubBean = null;
		try {
			dbPubBean = getService().findPublicationById(id, false);
		} catch (NoAccessException ne) {
			logger.info("User can't access the publication with ID " + id);
		} catch (Exception e) {
			logger.info("Error in retrieving publication with ID " + id);
		}
		return dbPubBean;
	}

	/**
	 * Return current PublicationBean for displaying Author list on page when no
	 * PubMedId exists.
	 *
	 * @return PublicationBean
	 */
	public PublicationBean retrieveCurrentPub() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		DynaValidatorForm form = (DynaValidatorForm) wctx.getSession()
				.getAttribute("publicationForm");
		if (form == null) {
			return null;
		}
		return (PublicationBean) form.get("publication");
	}

	public String[] getPublicationCategories(String searchLocations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		try {
			SortedSet<String> types = InitSetup.getInstance()
					.getDefaultAndOtherTypesByLookup(request,
							"publicationCategories", "publication", "category",
							"otherCategory", true);
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem getting publication types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getPublicationStatuses() {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		try {
			SortedSet<String> types = InitSetup.getInstance()
					.getDefaultAndOtherTypesByLookup(request,
							"publicationStatuses", "publication", "status",
							"otherStatus", true);
			types.add("");
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem getting publication statuses: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getMatchedSampleNames(String searchStr) {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		try {
			SampleServiceHelper sampleHelper = (SampleServiceHelper) (getService()
					.getSampleHelper());
			List<String> sampleNames = sampleHelper
					.findSampleNamesBy(searchStr);
			Collections.sort(sampleNames,
					new Comparators.SortableNameComparator());
			return sampleNames.toArray(new String[sampleNames.size()]);
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

	public String getPublicCounts() {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		request.getSession().removeAttribute("publicationSearchResults");

		Integer counts = 0;

		try {
			counts = getService().getNumberOfPublicPublications();
		} catch (Exception e) {
			logger
					.error("Error obtaining counts of public publications from local site.");
		}
		return counts.toString() + " Publications";
	}
}