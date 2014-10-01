package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import gov.nih.nci.cananolab.dto.particle.composition.AssociatedElementBean;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleAssociatedElement;
import gov.nih.nci.cananolab.restful.view.edit.SimpleChemicalAssociationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFunctionBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFunctionalizingEntityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;

public class ChemicalAssociationServicesTest {

	@Test
	public void testSetup() {

		Response res =
				given().contentType("application/json")
				.parameter("sampleId", "20917510").expect()
				.body("chemicalAssociationTypes", hasItems("Association","attachment","encapsulation","entrapment","intercalation"))
						.when().get("http://localhost:8080/caNanoLab/rest/chemicalAssociation/setup");

		System.out.println(res.getBody().asString());
		
	}
	@Test
	public void testEdit() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sampleId", "20917508");
		parameters.put("dataId", "73793538");
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters(parameters).expect()
				.body("type", equalToIgnoringCase("attachment"))
						.when().get("http://localhost:8080/caNanoLab/rest/chemicalAssociation/edit");

		System.out.println(res.getBody().asString());
		RestTestLoginUtil.logoutTest();
		
	}
	@Test
	public void testGetAssociatedElementOptions() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("compositionType", "nanomaterial entity");
		this.testSetup();
		ValidatableResponse res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.param("compositionType", "nanomaterial entity")
						.when().post("http://localhost:8080/caNanoLab/rest/chemicalAssociation/getAssociatedElementOptions")
						.then().body(containsString("[]"));

		RestTestLoginUtil.logoutTest();
		
	}

	@Test
	public void testGetComposingElementsByNanomaterialEntityId() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("id", "84377600");
		ValidatableResponse res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.queryParam("id", "84377600")
						.when().post("http://localhost:8080/caNanoLab/rest/chemicalAssociation/getComposingElementsByNanomaterialEntityId")
		.then().body(containsString("RNA"));
		RestTestLoginUtil.logoutTest();
		
	}
	
	
@Test
public void testsaveFile() {

	SimpleChemicalAssociationBean bean = new SimpleChemicalAssociationBean();
	bean.setSampleId("20917510");
	bean.setType("association");
	SimpleFileBean file = new SimpleFileBean();
	file.setType("movie");
	file.setTitle("TEST MoVIE");
	file.setUriExternal(false);
	file.setExternalUrl("http://www.cancer.gov");
	file.setSampleId("20917510");
	bean.setSimpleFile(file);
	SimpleAssociatedElement beanA = new SimpleAssociatedElement();
	beanA.setCompositionType("nanomaterial entity");
	beanA.setEntityDisplayName("dendrimer");
	beanA.setEntityId("21376281");
	bean.setAssociatedElementA(beanA);
	SimpleAssociatedElement beanB = new SimpleAssociatedElement();

	beanB.setCompositionType("functionalizing entity");
	beanB.setEntityDisplayName("antibody");
	beanB.setEntityId("85196800");
	bean.setAssociatedElementA(beanB);
	
	String jsessionId = RestTestLoginUtil.loginTest();

	final Client aClient = ClientBuilder.newBuilder()
	        .register(ObjectMapperProvider.class)
	        .register(JacksonFeature.class)
	        .build();
	
	WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
	webTarget.register(ChemicalAssociationServices.class);
	
	WebTarget submitWebTarget = webTarget.path("chemicalAssociation").path("saveFile");

	javax.ws.rs.core.Response postResponse =
			submitWebTarget.request("application/json")
	         .post(Entity.json(bean));
	
	assertNotNull(postResponse);
	assertTrue(postResponse.getStatus() == 401);
	
	postResponse.bufferEntity();
	String json = (String) postResponse.readEntity(String.class);
			
	assertTrue(json.contains("Session expired"));
	RestTestLoginUtil.logoutTest();

	}
	
@Test
public void testRemoveFile() {
	
	SimpleChemicalAssociationBean bean = new SimpleChemicalAssociationBean();
	bean.setSampleId("20917510");
	bean.setType("association");
	SimpleFileBean file = new SimpleFileBean();
	file.setType("movie");
	file.setTitle("TEST MoVIE");
	file.setUriExternal(false);
	file.setExternalUrl("http://www.cancer.gov");
	file.setSampleId("20917510");
	bean.setSimpleFile(file);
	SimpleAssociatedElement beanA = new SimpleAssociatedElement();
	beanA.setCompositionType("nanomaterial entity");
	beanA.setEntityDisplayName("dendrimer");
	beanA.setEntityId("21376281");
	bean.setAssociatedElementA(beanA);
	SimpleAssociatedElement beanB = new SimpleAssociatedElement();
	List<SimpleFileBean> list = new ArrayList<SimpleFileBean>();
	list.add(file);
	beanB.setCompositionType("functionalizing entity");
	beanB.setEntityDisplayName("antibody");
	beanB.setEntityId("85196800");
	bean.setAssociatedElementA(beanB);
	bean.setFiles(list);
	
	String jsessionId = RestTestLoginUtil.loginTest();

	final Client aClient = ClientBuilder.newBuilder()
	        .register(ObjectMapperProvider.class)
	        .register(JacksonFeature.class)
	        .build();
	
	WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
	webTarget.register(ChemicalAssociationServices.class);
	
	WebTarget submitWebTarget = webTarget.path("chemicalAssociation").path("removeFile");

	javax.ws.rs.core.Response postResponse =
			submitWebTarget.request("application/json")
	         .post(Entity.json(bean));
	
	assertNotNull(postResponse);
	assertTrue(postResponse.getStatus() == 401);
	
	postResponse.bufferEntity();
	String json = (String) postResponse.readEntity(String.class);
			
	assertTrue(json.contains("Session expired"));
	RestTestLoginUtil.logoutTest();

	}

@Test
public void testSubmit() {

	SimpleChemicalAssociationBean bean = new SimpleChemicalAssociationBean();
	bean.setSampleId("20917510");
	bean.setType("association");
	SimpleFileBean file = new SimpleFileBean();
	file.setType("movie");
	file.setTitle("TEST");
	file.setUriExternal(false);
	file.setExternalUrl("http://www.cancer.gov");
	file.setSampleId("20917510");
	SimpleAssociatedElement beanA = new SimpleAssociatedElement();
	beanA.setCompositionType("nanomaterial entity");
	beanA.setEntityDisplayName("dendrimer");
	beanA.setEntityId("21376281");
	bean.setAssociatedElementA(beanA);
	SimpleAssociatedElement beanB = new SimpleAssociatedElement();
	List<SimpleFileBean> list = new ArrayList<SimpleFileBean>();
	list.add(file);
	beanB.setCompositionType("functionalizing entity");
	beanB.setEntityDisplayName("antibody");
	beanB.setEntityId("85196800");
	bean.setAssociatedElementA(beanB);
	bean.setFiles(list);
	
	String jsessionId = RestTestLoginUtil.loginTest();

	final Client aClient = ClientBuilder.newBuilder()
	        .register(ObjectMapperProvider.class)
	        .register(JacksonFeature.class)
	        .build();
	
	WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
	webTarget.register(ChemicalAssociationServices.class);
	
	WebTarget submitWebTarget = webTarget.path("chemicalAssociation").path("submit");

	javax.ws.rs.core.Response postResponse =
			submitWebTarget.request("application/json")
	         .post(Entity.json(bean));
	
	assertNotNull(postResponse);
	assertTrue(postResponse.getStatus() == 401);
	
	postResponse.bufferEntity();
	String json = (String) postResponse.readEntity(String.class);
			
	assertTrue(json.contains("Session expired"));
	RestTestLoginUtil.logoutTest();

	}
	
@Test
public void testDelete() {

	SimpleChemicalAssociationBean bean = new SimpleChemicalAssociationBean();
	bean.setSampleId("20917510");
	bean.setType("association");
	SimpleFileBean file = new SimpleFileBean();
	file.setType("movie");
	file.setTitle("TEST");
	file.setUriExternal(false);
	file.setExternalUrl("http://www.cancer.gov");
	file.setSampleId("20917510");
	SimpleAssociatedElement beanA = new SimpleAssociatedElement();
	beanA.setCompositionType("nanomaterial entity");
	beanA.setEntityDisplayName("dendrimer");
	beanA.setEntityId("21376281");
	bean.setAssociatedElementA(beanA);
	SimpleAssociatedElement beanB = new SimpleAssociatedElement();
	List<SimpleFileBean> list = new ArrayList<SimpleFileBean>();
	list.add(file);
	beanB.setCompositionType("functionalizing entity");
	beanB.setEntityDisplayName("antibody");
	beanB.setEntityId("85196800");
	bean.setAssociatedElementA(beanB);
	bean.setFiles(list);
	
			
	String jsessionId = RestTestLoginUtil.loginTest();

	final Client aClient = ClientBuilder.newBuilder()
	        .register(ObjectMapperProvider.class)
	        .register(JacksonFeature.class)
	        .build();
	
	WebTarget webTarget = aClient.target("http://localhost:8080/caNanoLab/rest");
	webTarget.register(ChemicalAssociationServices.class);
	
	WebTarget submitWebTarget = webTarget.path("chemicalAssociation").path("delete");

	javax.ws.rs.core.Response postResponse =
			submitWebTarget.request("application/json")
	         .post(Entity.json(bean));
	
	assertNotNull(postResponse);
	assertTrue(postResponse.getStatus() == 401);
	
	postResponse.bufferEntity();
	String json = (String) postResponse.readEntity(String.class);
			
	assertTrue(json.contains("Session expired"));
	RestTestLoginUtil.logoutTest();

	}

}
