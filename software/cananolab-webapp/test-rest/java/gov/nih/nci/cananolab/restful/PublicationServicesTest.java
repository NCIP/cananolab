package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitPublicationBean;
import gov.nih.nci.cananolab.ui.form.PublicationForm;
import gov.nih.nci.cananolab.ui.form.SearchPublicationForm;
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
	
	@Test
	public void testSetup() {

		String jsonString = client.target(urlbase)
				.register(SampleServices.class)
				.path("publication/setup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("book chapter"));
	}

	@Test
	public void testSearchSample() {

		
		SearchPublicationForm form = new SearchPublicationForm();
		//Because "contains" operand is not set, exact name is needed;
		form.setCategory("report");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(SampleServices.class);
		
		WebTarget searchPublicationWebTarget = webTarget.path("publication").path("searchPublication");

		Response postResponse =
				searchPublicationWebTarget.request("application/json")
		         .post(Entity.json(form));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 200);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertNotNull(json);
		assertTrue(json.contains("report"));
		
		//should check other things
	}

	@Test
	public void testSubmitPublication() {

		SimpleSubmitPublicationBean bean = new SimpleSubmitPublicationBean();
		bean.setCategory("report");
		bean.setStatus("published");
		bean.setTitle("Test Report");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(PublicationServices.class);
		
		WebTarget submitPublicationWebTarget = webTarget.path("publication").path("submitPublication");

		Response postResponse =
				submitPublicationWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 200);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertNotNull(json);
		}
		
	}



