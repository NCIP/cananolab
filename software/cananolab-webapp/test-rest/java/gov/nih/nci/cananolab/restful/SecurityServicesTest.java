package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;
import org.junit.Test;

public class SecurityServicesTest {
	
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
	public void testLogin() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("security/login")
				.queryParam("username", "canano_res").queryParam("password", "sfsfasf")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.length() > 0);
		
		jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("security/logout")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		
		//test with fake password
		assertNotNull(jsonString);
		assertTrue(!jsonString.contains("success"));
	}


	@Test
	public void testLogout() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("security/logout")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		
		assertNotNull(jsonString);
		assertTrue(jsonString.contains("success"));
	}

	@Test
	public void testGetUserGroups() {
		fail("Not yet implemented");
	}

}
