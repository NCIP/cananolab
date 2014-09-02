package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class CharacterizationServicesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetupEdit() {
		String jsessionId = RestTestLoginUtil.loginTest();

		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameter("sampleId", "69500928").expect()
				.body("type", hasItems("physico-chemical characterization",
						"in vitro characterization",
						"ex vivo"))
						.when().get("http://localhost:8080/caNanoLab/rest/characterization/setupEdit");

		System.out.println(res.getBody().asString());
		
		RestTestLoginUtil.logoutTest();
	}

	@Test
	public void testSetupAdd() {
		String jsessionId = RestTestLoginUtil.loginTest();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("sampleId", "69500928");
		params.put("charType", "physico-chemical characterization");
		
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters(params)
				.expect()
				.body("charTypesLookup", hasItems("physico-chemical characterization",
						"in vitro characterization",
						"ex vivo"))
						.when().get("http://localhost:8080/caNanoLab/rest/characterization/setupAdd");

		System.out.println(res.getBody().asString());
		
		RestTestLoginUtil.logoutTest();
	}

	@Test
	public void testSetupUpdate() {
String jsessionId = RestTestLoginUtil.loginTest();
		
// http://localhost:8080/caNanoLab/characterization.do?dispatch=setupUpdate&sampleId=69500928&charId=69599238&charClassName=MolecularWeight&charType=physico-chemical%20characterization
		Map<String, String> params = new HashMap<String, String>();
		params.put("sampleId", "69500928");
		params.put("charId", "69599238");
		params.put("charType", "physico-chemical characterization");
		params.put("charClassName", "MolecularWeight");
		
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters(params)
				.expect()
				.body("assayType", equalTo("molecular weight"))
						.when().get("http://localhost:8080/caNanoLab/rest/characterization/setupUpdate");

		System.out.println(res.getBody().asString());
		
		RestTestLoginUtil.logoutTest();
	}
	
	
	
	@Test
	public void testGetCharNamesByCharType() {
		String jsessionId = RestTestLoginUtil.loginTest();


		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameter("charType", "physico-chemical characterization")
				.expect()
				.body("", hasItems("molecular weight"))
				.when().get("http://localhost:8080/caNanoLab/rest/characterization/getCharNamesByCharType");

		System.out.println(res.getBody().asString());

		RestTestLoginUtil.logoutTest();
	}
	
	
	@Test
	public void testGetAssayTypesByCharName() {
		String jsessionId = RestTestLoginUtil.loginTest();


		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameter("charName", "other_pc")
				.expect()
				.body("", hasItems("chelation stability"))
				.when().get("http://localhost:8080/caNanoLab/rest/characterization/getAssayTypesByCharName");

		System.out.println(res.getBody().asString());

		RestTestLoginUtil.logoutTest();
	}

}
