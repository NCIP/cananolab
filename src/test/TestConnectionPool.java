package test;

import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.List;

import org.apache.log4j.Logger;

public class TestConnectionPool {
	private static Logger logger = Logger.getLogger(TestConnectionPool.class);

	public void testPublicationService(String publicationType) {
		try {
			PublicationService service = new PublicationServiceLocalImpl();
			List<PublicationBean> publications = service.findPublicationsBy(
					null, publicationType, null, null, null, null, null, null,
					null, null, null, null, null, null);
			for (PublicationBean publication : publications) {
				System.out.println("Publication: "
						+ publication.getDisplayName());
				logger.info("Publication: " + publication.getDisplayName());
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void testCSM() {
		try {
			AuthorizationService service = new AuthorizationService(
					CaNanoLabConstants.CSM_APP_NAME);			
			List<UserBean> users = service.getAllUsers();
			for (UserBean user:users) {
				System.out.println("USer: "+user.getFullName());
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void main(String[] args) {
		TestConnectionPool test = new TestConnectionPool();
		test.testPublicationService("peer review article");
		test.testCSM();
		System.exit(0);
	}
}
