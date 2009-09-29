package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for publication forms.
 *
 * @author tanq
 *
 */
public class InitPublicationSetup {
	private InitPublicationSetup() {
	}

	public static InitPublicationSetup getInstance() {
		return new InitPublicationSetup();
	}

	public void setPublicationDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "publicationCategories",
						"publication", "category", "otherCategory", true);
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "publicationStatuses",
						"publication", "status", "otherStatus", true);
		InitSetup.getInstance()
			.getDefaultAndOtherLookupTypes(request, "publicationResearchAreas",
				"publication", "researchArea", "otherResearchArea", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
	}

	public void persistPublicationDropdowns(HttpServletRequest request,
			PublicationBean publication) throws Exception {

		InitSetup.getInstance().persistLookup(request, "publication", "category",
				"otherCategory",
				((Publication) (publication.getDomainFile())).getCategory());
		InitSetup.getInstance().persistLookup(request, "publication", "status",
				"otherStatus",
				((Publication) (publication.getDomainFile())).getStatus());
		setPublicationDropdowns(request);
	}

	public void setDefaultResearchAreas(
			HttpServletRequest request) throws Exception {
		InitSetup.getInstance()
			.getDefaultAndOtherLookupTypes(request, "publicationResearchAreas",
			"publication", "researchArea", "otherResearchArea", true);
	}
}
