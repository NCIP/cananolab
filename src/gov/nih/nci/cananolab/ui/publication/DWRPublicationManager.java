package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.exception.PublicationException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.publication.PubMedXMLHandler;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRPublicationManager {

	Logger logger = Logger.getLogger(DWRPublicationManager.class);

	public DWRPublicationManager() {
	}

	public PublicationBean retrievePubMedInfo(String pubmedID) {
		PubMedXMLHandler phandler = PubMedXMLHandler.getInstance();
		WebContext wctx = WebContextFactory.get();
		PublicationForm form = (PublicationForm) wctx.getSession()
				.getAttribute("publicationForm");
		PublicationBean pbean = (PublicationBean) form.get("publication");

		if (pubmedID != null && pubmedID.length() > 0 && !pubmedID.equals("0")) {
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
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
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
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
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

	public PublicationBean addAuthor(Author author)
			throws PublicationException {
		DynaValidatorForm pubForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("publicationForm"));
		if (pubForm == null) {
			return null;
		}
		PublicationBean pubBean = (PublicationBean) (pubForm
				.get("publication"));
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
		PublicationBean pubBean = (PublicationBean) (pubForm
				.get("publication"));
		pubBean.removeAuthor(author);
		return pubBean;
	}
}