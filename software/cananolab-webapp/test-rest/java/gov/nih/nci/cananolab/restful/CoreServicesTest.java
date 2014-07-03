package gov.nih.nci.cananolab.restful;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreServicesTest {
	
	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitSetup() {
		Client client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);

		String jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("core/initSetup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.length() > 0);
		assertTrue(jsonString.contains("numOfPublicCharacterizations"));
		
	}
	
	
	@Test
	public void testGetTabs() {
		Client client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);

		String jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("core/getTabs")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}

}
