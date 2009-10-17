package test;

import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.LoginService;
import gov.nih.nci.cananolab.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;

public class TestConnectionPool {
	private static Logger logger = Logger.getLogger(TestConnectionPool.class);

	public void testPublicationService(String publicationType) {
		try {
			PublicationService service = new PublicationServiceLocalImpl();
			List<String> publicationIds = service.findPublicationIdsBy(null,
					publicationType, null, null, null, null, null, null, null,
					null, null, null, null, null, null);
			for (String id : publicationIds) {
				System.out.println("Publication: " + id);
				logger.info("Publication: " + id);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void testPublicationURLService(String publicationType) {
		PrintStream p = null;
		try {
			p = new PrintStream("C:\\temp\\publication\\urlLog.txt");
			PublicationService service = new PublicationServiceLocalImpl();
			List<String> publicationIds = service.findPublicationIdsBy(null,
					publicationType, null, null, null, null, null, null, null,
					null, null, null, null, null, null);
			for (String id : publicationIds) {
				// System.out.println("Publication: "
				// + publication.getDisplayName());
				// logger.info("Publication: " + publication.getDisplayName());
				PublicationBean publication = service.findPublicationById(id,
						null);
				if (!StringUtils.isEmpty(publication.getDomainFile().getUri())) {
					p.println(publication.getDomainFile().getUri());
					URL yahoo = new URL(publication.getDomainFile().getUri());
					URLConnection yc = yahoo.openConnection();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(yc.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						if (inputLine.indexOf("Page Not Found") != -1) {
							p.println("ERROR NOT FOUND:"
									+ publication.getDomainFile().getUri());
						}
					}
					in.close();
				}
			}

		} catch (Exception e) {
			logger.error(e);
			p.println("EXCEPTION ERROR NOT FOUND:");
		} finally {
			if (p != null) {
				p.close();
			}
		}
	}

	public void testCSM() {
		try {
			LoginService service = new LoginService(Constants.CSM_APP_NAME);
			List<UserBean> users = service.getAllUsers();
			for (UserBean user : users) {
				System.out.println("USer: " + user.getFullName());
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void main(String[] args) {
		TestConnectionPool test = new TestConnectionPool();
		test.testPublicationService("peer review article");
		// test.testCSM();
		// test.testPublicationURLService("peer review article");
		System.out.println("COMPLETED");

		System.exit(0);
	}
}
