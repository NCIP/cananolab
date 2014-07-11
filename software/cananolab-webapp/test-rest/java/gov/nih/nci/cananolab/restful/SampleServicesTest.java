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
	Client client; 

	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient(new ClientConfig()
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				);
	}

	@Test
	public void testSetup() {

		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("sample/setup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}

	@Test
	public void testGetCharacterizationByType() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("sample/getCharacterizationByType")
				.queryParam("type", "ex vivo")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("imaging"));
	}

	//@Test
	public void testSearchSample() {
		
		SearchSampleForm form = new SearchSampleForm();
		form.setSampleName("ncl-23");
		
		Response jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("sample/searchSample")
				.queryParam("searchForm", form)
				.request("application/json")
				.header("some-header", "true").post(Entity.entity(SearchSampleForm.class, "application/xml"));
				//.g(String.class);

		assertNotNull(jsonString);
		//assertTrue(jsonString.contains("imaging"));
	}

	@Test
	public void testViewDataAvailability() {
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("sample/viewDataAvailability")
				.queryParam("sampleId", "20917507")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("surface chemistry"));
	}
	
	@Test
	public void testCharacterizationView() {
		
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("sample/characterizationView")
				.queryParam("sampleId", "2406323") //ncl-19
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}
}
