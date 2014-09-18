package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import gov.nih.nci.cananolab.restful.util.SecurityUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitPublicationBean;
import gov.nih.nci.cananolab.service.security.UserBean;
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
				.register(PublicationServices.class)
				.path("publication/summaryView")
				.queryParam("sampleId", "20917506") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}
	@Test
	public void testdownload() {
		String jsonString = client.target(urlbase)
				.register(PublicationServices.class)
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
				.register(PublicationServices.class)
				.path("publication/setup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.contains("book chapter"));
	}
	
	@Test
	public void testSummaryEdit() {
		String jsonString = client.target(urlbase)
				.register(PublicationServices.class)
				.path("publication/summaryEdit")
				.queryParam("sampleId", "20917506") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}
	

	@Test
	public void testSearchPublication() {

		
		SearchPublicationForm form = new SearchPublicationForm();
		form.setCategory("report");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(PublicationServices.class);
		
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
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertTrue(json.contains("Session expired"));

		}
		
	
	@Test
	public void testEdit() {
		
//			try{
//		String jsonString = client.target(urlbase)
//				.register(PublicationServices.class)
//				.path("publication/edit")
//				.queryParam("publicationId", "44990464") 
//				.queryParam("sampleId", "20917509") 
//				.request("application/json")
//				.header("some-header", "true")
//				.get(String.class);
//		System.out.println(jsonString);
//			}catch(Exception e){
//				assertTrue("NoAccessException", e.toString().contains("Internal Server Error"));
//			}

			
			String jsessionId = RestTestLoginUtil.loginTest();
			
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("publicationId", "44990464");
			parameters.put("sampleId", "20917509");
			com.jayway.restassured.response.Response res =
					given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
					.parameters(parameters).expect()
					.body("category", equalToIgnoringCase("Test Pub Type"))
							.when().get("http://localhost:8080/caNanoLab/rest/publication/edit");

			System.out.println(res.getBody().asString());
			RestTestLoginUtil.logoutTest();
	}
	
	@Test
	public void testGetSamples() {
		try{
		String jsonString = client.target(urlbase)
				.register(PublicationServices.class)
				.path("publication/getSamples")
				.queryParam("searchStr", "ncl-24-1") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		}catch(Exception e){
			assertTrue("NotAuthorizedException", e.toString().contains("Unauthorized"));
		}

	}
	
	@Test
	public void testGetPubmedPublication() {
		try{
		String jsonString = client.target(urlbase)
				.register(PublicationServices.class)
				.path("publication/getPubmedPublication")
				.queryParam("pubmedId", "12345") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		}catch(Exception e){
			assertTrue("NotAuthorizedException", e.toString().contains("Unauthorized"));
		}

	}
	
	@Test
	public void testRetrievePubMedInfo() {
		try{
		String jsonString = client.target(urlbase)
				.register(PublicationServices.class)
				.path("publication/retrievePubMedInfo")
				.queryParam("pubmedId", "12345") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		}catch(Exception e){
			assertTrue("NotAuthorizedException", e.toString().contains("Unauthorized"));
		}

	}
	
	@Test
	public void testSaveAccess() {

		SimpleSubmitPublicationBean bean = new SimpleSubmitPublicationBean();
		bean.setCategory("report");
		bean.setStatus("published");
		bean.setTitle("Test Report");
		AccessibilityBean theAccess = new AccessibilityBean();
		theAccess.setAccessBy("group");
		theAccess.setGroupName("NCI group");
		theAccess.setRoleName("R");
		UserBean user = new UserBean();
		theAccess.setUserBean(user);
		bean.setTheAccess(theAccess);
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(PublicationServices.class);
		
		WebTarget submitPublicationWebTarget = webTarget.path("publication").path("saveAccess");

		Response postResponse =
				submitPublicationWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertTrue(json.contains("Session expired"));

		}
		
	
	@Test
	public void testDeleteAccess() {

		SimpleSubmitPublicationBean bean = new SimpleSubmitPublicationBean();
		bean.setCategory("report");
		bean.setStatus("published");
		bean.setTitle("Test Report");
		AccessibilityBean theAccess = new AccessibilityBean();
		theAccess.setAccessBy("group");
		theAccess.setGroupName("NCI group");
		theAccess.setRoleName("R");
		UserBean user = new UserBean();
		theAccess.setUserBean(user);
		bean.setTheAccess(theAccess);
		List<AccessibilityBean> groupAccess = new ArrayList<AccessibilityBean>();
		AccessibilityBean access = new AccessibilityBean();
		access.setAccessBy("group");
		access.setGroupName("NCI group");
		access.setRoleName("R");
		access.setUserBean(user);
		groupAccess.add(access);
		AccessibilityBean acces = new AccessibilityBean();
		acces.setAccessBy("group");
		acces.setGroupName("Curator");
		acces.setRoleName("CURD");
		acces.setUserBean(user);
		groupAccess.add(acces);
		bean.setGroupAccesses(groupAccess);
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(PublicationServices.class);
		
		WebTarget submitPublicationWebTarget = webTarget.path("publication").path("deleteAccess");

		Response postResponse =
				submitPublicationWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertTrue(json.contains("Session expired"));

		}
	
	@Test
	public void testDeletePublication() {

		SimpleSubmitPublicationBean bean = new SimpleSubmitPublicationBean();
		List<AccessibilityBean> groupAccess = new ArrayList<AccessibilityBean>();

		bean.setCategory("report");
		bean.setStatus("published");
		bean.setTitle("Test Report");
		AccessibilityBean acces = new AccessibilityBean();
		UserBean user = new UserBean();
		acces.setAccessBy("group");
		acces.setGroupName("Curator");
		acces.setRoleName("CURD");
		acces.setUserBean(user);
		groupAccess.add(acces);
		bean.setGroupAccesses(groupAccess);
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(PublicationServices.class);
		
		WebTarget submitPublicationWebTarget = webTarget.path("publication").path("deletePublication");

		Response postResponse =
				submitPublicationWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
		assertTrue(json.contains("Session expired"));
		
		}
		
	
	@Test
	public void testSubmitForReview() {
		DataReviewStatusBean bean = new DataReviewStatusBean();
		bean.setDataType("publication");
		bean.setDataId("58753024");
		bean.setDataName("Test DEV Pub 1");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(PublicationServices.class);
		
		WebTarget submitProtocolWebTarget = webTarget.path("publication").path("submitForReview");

		Response postResponse =
				submitProtocolWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
		assertTrue(json.contains("Session expired"));

	}
}



