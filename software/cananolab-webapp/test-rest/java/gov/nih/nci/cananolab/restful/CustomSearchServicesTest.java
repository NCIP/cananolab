package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Before;
import org.junit.Test;

public class CustomSearchServicesTest {

	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	Client client; 

	
	
	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient();
						
	}
	@Test
	public void testSearch() {
		try{
		String jsonString = client.target(urlbase)
				.register(ProtocolServices.class)
				.path("customsearch/search")
				.queryParam("keyword", "test")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		assertNotNull(jsonString);
		assertTrue(jsonString.contains("test"));
		}catch(Exception e){
			assertTrue("NotAuthorizedException", e.toString().contains("Unauthorized"));
		}

	}
}
