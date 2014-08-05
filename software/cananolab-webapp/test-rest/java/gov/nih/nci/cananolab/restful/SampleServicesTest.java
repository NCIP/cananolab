package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;
import gov.nih.nci.cananolab.restful.view.edit.SampleEditGeneralBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.SearchSampleForm;
import gov.nih.nci.cananolab.util.Constants;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
	
		client = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
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
		//Because "contains" operand is not set, exact name is needed;
		form.setSampleName("ncl-23-1");
		
		WebTarget webTarget = client.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(SampleServices.class);
		
		WebTarget searchSampleWebTarget = webTarget.path("sample").path("searchSample");

		Response postResponse =
				searchSampleWebTarget.request("application/json")
		         .post(Entity.json(form));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 200);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertNotNull(json);
		assertTrue(json.contains("NCL-23-1"));
		
		//should check other things
	}
	
	@Test
	public void testUpdateSample() {
		WebTarget webTarget = client.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(SampleServices.class);
		
		WebTarget searchSampleWebTarget = webTarget.path("sample").path("updateSample");
			
		SampleEditGeneralBean editBean = new SampleEditGeneralBean();
		editBean.setSampleId(44695553);
		editBean.setSampleName("SY-New Sample");
		editBean.getKeywords().add("NewKeywork-" + System.currentTimeMillis());
		
		Response postResponse =
				searchSampleWebTarget.request("application/json")
		         .post(Entity.json(editBean));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 500);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
		assertTrue(json.contains("User session"));
	}
	
	@Test
	public void testCopySample() {

		WebTarget webTarget = client.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(SampleServices.class);
		
		WebTarget searchSampleWebTarget = webTarget.path("sample").path("copySample");
			
		SampleEditGeneralBean editBean = new SampleEditGeneralBean();
		editBean.setSampleId(44695553);
		editBean.setSampleName("SY-New Sample");
		editBean.getKeywords().add("NewKeywork-" + System.currentTimeMillis());
		
		Response postResponse =
				searchSampleWebTarget.request("application/json")
		         .post(Entity.json(editBean));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 500);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
		assertTrue(json.contains("User session"));
	}
	
	
}
