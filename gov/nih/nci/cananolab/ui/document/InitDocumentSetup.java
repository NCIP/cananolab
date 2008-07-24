package gov.nih.nci.cananolab.ui.document;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.ReportBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;

import javax.servlet.http.HttpServletRequest;

/**
 * This class sets up information required for document forms.
 * 
 * @author tanq
 * 
 */
public class InitDocumentSetup {
	private InitDocumentSetup() {
	}

	public static InitDocumentSetup getInstance() {
		return new InitDocumentSetup();
	}

	public void setPublicationDropdowns(HttpServletRequest request) throws Exception {
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "publicationCategories",
						"Publication", "category", "otherCategory", true);
		InitSetup.getInstance()
				.getDefaultAndOtherLookupTypes(request, "publicationStatuses",
						"Publication", "status", "otherStatus", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		InitNanoparticleSetup.getInstance().getAllNanoparticleSampleNames(
				request, user);
	}

	
	public void persistPublicationDropdowns(HttpServletRequest request,
			PublicationBean publication) throws Exception {
		//TODO to be verified by tanq, ,need LabFile?? done by report??
		InitSetup.getInstance().persistLookup(request, "LabFile", "type",
				"otherType", publication.getDomainFile().getType());		
		InitSetup.getInstance().persistLookup(request, "Publication", "category",
				"otherCategory",
				((Publication) (publication.getDomainFile())).getCategory());
		InitSetup.getInstance().persistLookup(request, "Publication", "status",
				"otherStatus",
				((Publication) (publication.getDomainFile())).getStatus());
		setPublicationDropdowns(request);
	}
}
