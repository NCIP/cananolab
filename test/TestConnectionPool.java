package test;

import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;

import java.util.List;

public class TestConnectionPool {
	public void testPublicationService(String publicationType) {
		try {
			PublicationService service = new PublicationServiceLocalImpl();
			List<PublicationBean> publications = service.findPublicationsBy(
					null, publicationType, null, null, null, null, null,
					null, null, null, null, null, null, null);
			for (PublicationBean publication : publications) {
				System.out.println("Publication: "
						+ publication.getDisplayName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestConnectionPool test = new TestConnectionPool();
		test.testPublicationService("peer review article");
	}
}
