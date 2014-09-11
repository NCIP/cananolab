package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jayway.restassured.response.Response;

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
		
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters("compositionType", "nanomaterial entity").expect()
				.body("type", equalToIgnoringCase("dendrimer"))
						.when().post("http://localhost:8080/caNanoLab/rest/chemicalAssociation/getAssociatedElementOptions");

		System.out.println(res.getBody().asString());
		RestTestLoginUtil.logoutTest();
		
	}

	@Test
	public void testGetComposingElementsByNanomaterialEntityId() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters("id", "74022912").expect()
				.body("name", equalToIgnoringCase("chem Test"))
						.when().post("http://localhost:8080/caNanoLab/rest/chemicalAssociation/getComposingElementsByNanomaterialEntityId");

		System.out.println(res.getBody().asString());
		RestTestLoginUtil.logoutTest();
		
	}

}
