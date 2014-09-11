package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;

public class CompositionFileServicesTest {
	@Test
	public void testSetup() {

		Response res =
				given().contentType("application/json")
				.expect()
				.body("fileTypes", hasItems("document","graph","image","movie","spread sheet","txt"))
						.when().get("http://localhost:8080/caNanoLab/rest/compositionFile/setup");

		System.out.println(res.getBody().asString());
		
	}
	@Test
	public void testEdit() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sampleId", "57835522");
		parameters.put("dataId", "73433125");
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters(parameters).expect()
				.body("type", equalToIgnoringCase("graph"))
						.when().get("http://localhost:8080/caNanoLab/rest/compositionFile/edit");

		System.out.println(res.getBody().asString());
		RestTestLoginUtil.logoutTest();
		
	}
	
	@Test
	public void testSubmit() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		
		SimpleFileBean file = new SimpleFileBean();
		file.setType("movie");
		file.setTitle("TEST MoVIE");
		file.setUriExternal(false);
		file.setExternalUrl("http://www.cancer.gov");
		file.setSampleId("20917510");
		Response res = 
				given() .contentType("application/json").cookie("JSESSIONID=" + jsessionId).body(file)				
				.expect().body("type", equalToIgnoringCase("movie"))
				.when().post("http://localhost:8080/caNanoLab/rest/compositionFile/submit");
		System.out.println(res.getBody().asString());

		RestTestLoginUtil.logoutTest();
		
	}
}
