package gov.nih.nci.cananolab.restful;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitProtocolBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.SearchProtocolForm;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Before;
import org.junit.Test;



public class ProtocolServicesTest {

	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	Client client; 

	
	
	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient(new ClientConfig());
						
	}
	
	@Test
	public void testSetup() {

		String jsonString = client.target(urlbase)
				.register(ProtocolServices.class)
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
	
	@Test
	public void testSubmitProtocol() {

		SimpleSubmitProtocolBean bean = new SimpleSubmitProtocolBean();
		bean.setType("sterility");
		bean.setName("Test Protocol 123");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(ProtocolServices.class);
		
		WebTarget submitProtocolWebTarget = webTarget.path("protocol").path("submitProtocol");

		Response postResponse =
				submitProtocolWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertTrue(json.contains("Session expired"));

		}
		
	
	@Test
	public void testEdit() {
		
			try{
		String jsonString = client.target(urlbase)
				.register(ProtocolServices.class)
				.path("protocol/edit")
				.queryParam("protocolId", "61472769") 
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		System.out.println(jsonString);
			}catch(Exception e){
				assertTrue("NotAuthorizedException", e.toString().contains("Unauthorized"));
			}

	}
	

	@Test
	public void testSaveAccess() {

		SimpleSubmitProtocolBean bean = new SimpleSubmitProtocolBean();
		bean.setType("sterility");
		bean.setName("Test Protocol 123");
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
		webTarget.register(ProtocolServices.class);
		
		WebTarget submitPublicationWebTarget = webTarget.path("protocol").path("saveAccess");

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

		SimpleSubmitProtocolBean bean = new SimpleSubmitProtocolBean();
		bean.setType("sterility");
		bean.setName("Test Protocol 123");
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
		webTarget.register(ProtocolServices.class);
		
		WebTarget submitProtocolWebTarget = webTarget.path("protocol").path("deleteAccess");

		Response postResponse =
				submitProtocolWebTarget.request("application/json")
		         .post(Entity.json(bean));
		
		assertNotNull(postResponse);
		assertTrue(postResponse.getStatus() == 401);
		
		postResponse.bufferEntity();
		String json = (String) postResponse.readEntity(String.class);
				
		assertTrue(json.contains("Session expired"));

		}
	
	@Test
	public void testDeletePublication() {

		List<AccessibilityBean> groupAccess = new ArrayList<AccessibilityBean>();

		SimpleSubmitProtocolBean bean = new SimpleSubmitProtocolBean();
		bean.setType("sterility");
		bean.setName("Test Protocol 123");
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
		webTarget.register(ProtocolServices.class);
		
		WebTarget submitPublicationWebTarget = webTarget.path("protocol").path("deleteProtocol");

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
	public void testGetProtocol() {
		try{
		String jsonString = client.target(urlbase)
				.register(ProtocolServices.class)
				.path("protocol/getProtocol")
				.queryParam("protocolType", "sterility")
				.queryParam("protocolName", "STE-2")
				.queryParam("protocolVersion", "1.0")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);
		}catch(Exception e){
			assertTrue("NotAuthorizedException", e.toString().contains("Unauthorized"));
		}

	}
	@Test
	public void testSubmitForReview() {
		DataReviewStatusBean bean = new DataReviewStatusBean();
		bean.setDataType("protocol");
		bean.setDataId("58097664");
		bean.setDataName("Test Protocol Review_rest");
		
		final Client aClient = ClientBuilder.newBuilder()
		        .register(ObjectMapperProvider.class)
		        .register(JacksonFeature.class)
		        .build();
		
		WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
		webTarget.register(ProtocolServices.class);
		
		WebTarget submitProtocolWebTarget = webTarget.path("protocol").path("submitForReview");

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
