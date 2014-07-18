package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;
import org.junit.Test;

public class PublicationServicesTest {

	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	Client client; 

	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);
	}
	@Test
	public void testSummaryView() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("publication/summaryView")
				.queryParam("sampleId", "20917507") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}
	@Test
	public void testdownload() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("publication/download")
				.queryParam("fileId", "23178496") 
				.request("application/pdf")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}

}
