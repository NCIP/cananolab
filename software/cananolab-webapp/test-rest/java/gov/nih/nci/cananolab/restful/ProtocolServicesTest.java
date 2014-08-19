package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;
import gov.nih.nci.cananolab.ui.form.SearchProtocolForm;
import gov.nih.nci.cananolab.ui.form.SearchPublicationForm;

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
import org.junit.Rule;



public class ProtocolServicesTest {

	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	Client client; 

	
	
	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient(new ClientConfig());
		//.register(MyClientResponseFilter.class)
		//.register(new AnotherClientFilter())
				
	}
	
	@Test
	public void testSetup() {

		String jsonString = client.target(urlbase)
				.register(PublicationServices.class)
				.path("protocol/setup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("in vitro assay"));
	}
	
	@Test
	public void testSearchProtocol() {

		
		SearchProtocolForm form = new SearchProtocolForm();
		form.setProtocolType("sterility");
		form.setTitleOperand("contains");
		form.setNameOperand("contains");
		form.setAbbreviationOperand("contains");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(ProtocolServices.class);
		
		WebTarget searchPublicationWebTarget = webTarget.path("protocol").path("searchProtocol");

		Response postResponse =
				searchPublicationWebTarget.request("application/json")
		         .post(Entity.json(form));
		
		assertNotNull(postResponse);
		System.out.println("Status: " + postResponse.getStatus());
		assertTrue(postResponse.getStatus() == 200);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertNotNull(json);
		assertTrue(json.contains("sterility"));
		System.out.println(json);
		
	}

	@Test
	public void testdownload() {
		String jsonString = client.target(urlbase)
				.register(ProtocolServices.class)
				.path("protocol/download")
				.queryParam("fileId", "23178496") 
				.request("application/pdf")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}
	
	
}
