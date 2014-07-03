package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.Before;
import org.junit.Test;



public class SampleServicesTest {
	
	String urlbase = "http://localhost:8080/caNanoLab/rest/";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetup() {
		Client client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);

		String jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("sample/setup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}

	@Test
	public void testGetCharacterizationByType() {
		Client client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);

		String jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("sample/getCharacterizationByType")
				.queryParam("type", "ex vivo")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("imaging"));
	}

	@Test
	public void testSearchSample() {
		Client client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);

		SearchSampleForm form = new SearchSampleForm();
		form.setSampleName("ncl-23");
		
		Response jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("sample/searchSample")
				.queryParam("searchForm", form)
				.request("application/json")
				.header("some-header", "true").post(Entity.entity(SearchSampleForm.class, "application/xml"));
				//.g(String.class);

		assertNotNull(jsonString);
		//assertTrue(jsonString.contains("imaging"));
	}

}
