package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRPublicationManager {

	Logger logger = Logger.getLogger(DWRPublicationManager.class);
	PublicationService service;

	public DWRPublicationManager() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		service = new PublicationServiceLocalImpl(user);
	}

	public PublicationBean clearPublication() {
		WebContext wctx = WebContextFactory.get();
		PublicationForm form = (PublicationForm) wctx.getSession()
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
				newPubBean = service.getPublicationFromPubMedXML(pubmedID);
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
		PublicationForm form = (PublicationForm) wctx.getSession()
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

	public PublicationBean updatePubMedWithExistingPublication(String pubmedID) {
		PublicationBean publicationBean = this.retrievePubMedInfo(pubmedID);
		// search database record for pubMed ID
		try {
			PublicationBean dbPubBean = service.findPublicationByKey(
					"pubMedId", new Long(pubmedID));
			if (dbPubBean != null) {
				// update form publication with data stored in the databbase
				publicationBean.copyNonPubMedFieldsFromDatabase(dbPubBean);
				return publicationBean;
			}
		} catch (NoAccessException ne) {
			logger.info("User can't access the publication with Pub Med ID "
					+ pubmedID);
		} catch (Exception e) {
			logger.info("Error in retrieving publication with Pub Med ID "
					+ pubmedID);
		}
		return null;
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
		PublicationForm form = (PublicationForm) wctx.getSession()
				.getAttribute("publicationForm");
		if (form == null) {
			return null;
		}
		return (PublicationBean) form.get("publication");
	}

	public PublicationBean updateWithExistingDOIPublication(String doi) {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		PublicationForm form = (PublicationForm) wctx.getSession()
				.getAttribute("publicationForm");
		if (form == null) {
			return null;
		}
		PublicationBean publicationBean = (PublicationBean) form
				.get("publication");

		// search database record for DOI
		try {
			PublicationBean dbPubBean = service.findPublicationByKey(
					"digitalObjectId", doi);
			if (dbPubBean != null) {
				// update form publication with data stored in the databbase
				publicationBean.copyFromDatabase(dbPubBean);
				return publicationBean;
			}
		} catch (NoAccessException ne) {
			logger.info("User can't access the publication with DOI " + doi);
		} catch (Exception e) {
			logger.info("Error in retrieving publication with DOI " + doi);
		}
		return null;
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
				types = InitSetup.getInstance()
						.getDefaultAndOtherTypesByLookup(request,
								"publicationCategories", "publication",
								"category", "otherCategory", true);
			} else {
				types = InitSetup.getInstance().getDefaultTypesByLookup(
						wctx.getServletContext(),
						"defaultPublicationCategories", "publication",
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
				types = InitSetup.getInstance()
						.getDefaultAndOtherTypesByLookup(request,
								"publicationStatuses", "publication", "status",
								"otherStatus", true);
			} else {
				types = InitSetup.getInstance().getDefaultTypesByLookup(
						wctx.getServletContext(), "defaultPublicationStatuses",
						"publication", "status");
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

	public String[] getMatchedSampleNames(String searchStr) {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		try {
			SampleServiceLocalImpl sampleService = (SampleServiceLocalImpl) (((PublicationServiceLocalImpl) service)
					.getSampleService());
			List<String> sampleNames = sampleService.getHelper()
					.findSampleNamesBy(searchStr);
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

	public String getPublicCounts(String[] locations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		request.getSession().removeAttribute("publicationSearchResults");
		if (locations.length == 0) {
			locations = new String[1];
			locations[0] = Constants.APP_OWNER;
			// return null;
		}
		Integer counts = 0;
		for (String location : locations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				try {
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
		return counts.toString() + " Publications";
	}

	public PublicationBean updateWithExistingNonPubMedDOIPublication(
			String category, String title, Author firstAuthor) throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		PublicationForm form = (PublicationForm) wctx.getSession()
				.getAttribute("publicationForm");
		if (form == null) {
			return null;
		}
		PublicationBean publicationBean = (PublicationBean) form
				.get("publication");
		Publication publication = (Publication) publicationBean.getDomainFile();

		// check whether a publication exists based on the publication type,
		// title, first author
		String firstName = null;
		String lastName = null;
		if (firstAuthor != null) {
			firstName = firstAuthor.getFirstName();
			lastName = firstAuthor.getLastName();
		}
		PublicationBean dbBean = service.findNonPubMedNonDOIPublication(
				category, title, lastName, firstName);
		if (dbBean != null) {
			publicationBean.copyFromDatabase(dbBean);
			return publicationBean;
		} else {
			return null;
		}
	}
}