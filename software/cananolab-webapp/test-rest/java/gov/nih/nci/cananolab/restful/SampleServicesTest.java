package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cananolab.restful.SampleServices;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
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
		assertTrue(jsonString.contains("physico-chemical characterization"));
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
				.queryParam("sampleId", "20917507") //ncl-23
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("Experiment Configurations"));
	}
	@Test
	public void testSummaryView() {
		
		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("sample/view")
				.queryParam("sampleId", "20917507") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}
	
	@Test
	public void testSearchSample() {

		//http://stackoverflow.com/questions/23601842/jersey-messagebodywriter-not-found-for-media-type-application-json-type-class
		
		SearchSampleForm form = new SearchSampleForm();
		form.setSampleName("ncl-23-1");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(SampleServices.class);
		
		WebTarget searchSampleWebTarget = webTarget.path("sample").path("searchSample");

		Response postResponse =
				searchSampleWebTarget.request("application/json")
		         .post(Entity.json(form));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 200);
		
	}
	
	
}
