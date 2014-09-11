package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;

import java.util.HashMap;
import java.util.Map;

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
				.queryParam("id", "74022912")
						.when().post("http://localhost:8080/caNanoLab/rest/chemicalAssociation/getComposingElementsByNanomaterialEntityId")
		.then().body(containsString("modifier"));
		RestTestLoginUtil.logoutTest();
		
	}

}
