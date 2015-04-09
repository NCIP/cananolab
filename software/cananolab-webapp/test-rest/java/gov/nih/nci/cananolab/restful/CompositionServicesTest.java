package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Before;
import org.junit.Test;

public class CompositionServicesTest {

	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	Client client; 

	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient();
	}
	@Test
	public void testSummaryView() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("composition/summaryView")
				.queryParam("sampleId", "20917507") //NCL-23
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}

}
