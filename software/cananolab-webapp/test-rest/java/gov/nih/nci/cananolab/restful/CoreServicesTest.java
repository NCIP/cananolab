package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItems;


import static org.junit.Assert.*;

public class CoreServicesTest {
	
	String urlbase = "http://localhost:8080/caNanoLab/rest/";
	Client client;
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitSetup() {
		client = ClientBuilder.newClient();

		String jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("core/initSetup")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
		assertTrue(jsonString.length() > 0);
		assertTrue(jsonString.contains("numOfPublicCharacterizations"));
		
	}
	
	
	@Test
	public void testGetTabs() {
		client = ClientBuilder.newClient();

		String jsonString = client.target(urlbase)
				.register(CoreServices.class)
				.path("core/getTabs")
				.request("application/json")
				.header("some-header", "true")
				.get(String.class);

		assertNotNull(jsonString);
	}

	@Test
	public void testGetFavorites(){
		String jsessionId = RestTestLoginUtil.loginTest();
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.expect()
				.body("samples.dataName", equalToIgnoringCase("test_HJ_11"))
						.when().get("http://localhost:8080/caNanoLab/rest/core/getFavorites");
		
		RestTestLoginUtil.logoutTest();

	}
	@Test
	public void testAddFavorites(){
		String jsessionId = RestTestLoginUtil.loginTest();
		FavoriteBean form = new FavoriteBean();
		form.setDataType("sample");
		form.setDataId("56229901");
		form.setDataName("test_HJ_12");
		Response res =
				given() .contentType("application/json").cookie("JSESSIONID=" + jsessionId).body(form)
				.expect().statusCode(200)
				.when().post("http://localhost:8080/caNanoLab/rest/core/addFavorite");
				
	}
	
	@Test
	public void testDeleteFavorites(){
		String jsessionId = RestTestLoginUtil.loginTest();
		FavoriteBean form = new FavoriteBean();
		form.setDataType("sample");
		form.setDataId("56229895");
		form.setDataName("test_HJ-10");
		form.setId(new Long(105283586));
		Response res =
				given() .contentType("application/json").cookie("JSESSIONID=" + jsessionId).body(form)
				.expect().statusCode(200)
				.when().post("http://localhost:8080/caNanoLab/rest/core/deleteFavorite");
				
	}
}
