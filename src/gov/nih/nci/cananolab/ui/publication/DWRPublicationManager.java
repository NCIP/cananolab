package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceRemoteImpl;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
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

	public PublicationBean searchPubMedById(String pubmedID) {
		// New a pubBean each time, so we know if parsing is success or not.
		PublicationBean newPubBean = new PublicationBean();
		Publication newPub = (Publication) newPubBean.getDomainFile();
		if (!StringUtils.isEmpty(pubmedID) && !pubmedID.equals("0")) {
			try {
				Long pubMedIDLong = Long.valueOf(pubmedID.trim());
				PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
				if (phandler.parsePubMedXML(pubMedIDLong, newPubBean)) {
					newPub.setPubMedId(pubMedIDLong);
				}
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
		publicationBean.copyPubMedData(newPubBean);
		return publicationBean;
	}

	public PublicationBean updatePubMedWithExistingPublication(String pubmedID) {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		PublicationBean publicationBean = this.retrievePubMedInfo(pubmedID);
		Publication publication = (Publication) publicationBean.getDomainFile();
			// search database record for pubMed ID
		try {
			PublicationService service = new PublicationServiceLocalImpl();
			PublicationBean dbPubBean = service.findPublicationByKey(
					"pubMedId", new Long(pubmedID), user);
			if (dbPubBean != null) {
				// update form publication with data stored in the databbase
				Publication dbPub = (Publication) dbPubBean.getDomainFile();
				publication.setId(dbPub.getId());
				publication.setCreatedBy(dbPub.getCreatedBy());
				publication.setCreatedDate(dbPub.getCreatedDate());
				publication.setCategory(dbPub.getCategory());
				publication.setDescription(dbPub.getDescription());
				publication.setKeywordCollection(dbPub.getKeywordCollection());
				publication.setResearchArea(dbPub.getResearchArea());
				publication.setStatus(dbPub.getStatus());
				publication.setType(dbPub.getType());
				publicationBean
						.setSampleNamesStr(dbPubBean.getSampleNamesStr());
				publicationBean.setVisibilityGroups(dbPubBean.getVisibilityGroups());
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
			SampleServiceHelper sampleHelper = new SampleServiceHelper();
			List<String> sampleNames = sampleHelper.findSampleNamesBy(
					searchStr, user);
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