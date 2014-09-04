package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleExperimentBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleInstrumentBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	@Test
	public void testSaveExperimentConfigAdd() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
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
		
		SimpleExperimentBean form = new SimpleExperimentBean();
		form.setDisplayName("electron microprobe analysis(EMPA)");
		List<SimpleInstrumentBean> insts = new ArrayList<SimpleInstrumentBean>();
		SimpleInstrumentBean inst = new SimpleInstrumentBean();
		inst.setModelName("SY-Best");
		inst.setManufacturer("MyFactory");
		inst.setType("SYType");
		insts.add(inst);
		inst.setModelName("SY-Best2");
		inst.setManufacturer("MyFactory");
		insts.add(inst);
		inst.setType("SYType2");
		form.setInstruments(insts);
		
		
		
		res = //with().parameters("sampleName", "SY-NCL-23-1")
		given() .contentType("application/json").cookie("JSESSIONID=" + jsessionId).body(form)
		.expect().body("charTypesLookup", hasItems("physico-chemical characterization",
				"in vitro characterization",
				"ex vivo"))
		.when().post("http://localhost:8080/caNanoLab/rest/characterization/saveExperimentConfig");
		
		
		
		System.out.println(res.getBody().asString());
	}
	
	@Test
	public void testRemoveExperimentConfig() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
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
		
		
//		"id":69632002,
//        "displayName":"sfaer927034wqw34(SEC-MALLS)",
//        "abbreviation":null,
//        "description":"SY Testing",
//        "instruments":[  
//           {  
//              "manufacturer":"Agilent",
//              "modelName":"",
//              "type":""
//           },
//           {  
//              "manufacturer":"ACT GmbH",
//              "modelName":"",
//              "type":""
//           }
		
		
		
		
		SimpleExperimentBean form = new SimpleExperimentBean();
		form.setId(69632002);
		form.setDisplayName("sfaer927034wqw34(SEC-MALLS)");
		form.setDescription("SY Testing");
		
		List<SimpleInstrumentBean> insts = new ArrayList<SimpleInstrumentBean>();
		SimpleInstrumentBean inst = new SimpleInstrumentBean();
		inst.setModelName("");
		inst.setManufacturer("Agilent");
		inst.setType("");
		insts.add(inst);
		inst.setModelName("");
		inst.setManufacturer("ACT GmbH");
		inst.setType("");
		insts.add(inst);
		
		form.setInstruments(insts);
		
		
		
		res = //with().parameters("sampleName", "SY-NCL-23-1")
		given() .contentType("application/json").cookie("JSESSIONID=" + jsessionId).body(form)
		.expect().body("charTypesLookup", hasItems("physico-chemical characterization",
				"in vitro characterization",
				"ex vivo"))
		.when().post("http://localhost:8080/caNanoLab/rest/characterization/removeExperimentConfig");
		
		
		
		System.out.println(res.getBody().asString());
	}

	@Test
	public void testGetInstrumentTypesByTechniqueType() {
		Response res =
				with() //.contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters("techniqueType", "differential centrifugal sedimentation")
				.expect()
				.body("", hasItems("differential centrifugal sedimentation instrument"))
				.when().get("http://localhost:8080/caNanoLab/rest/characterization/getInstrumentTypesByTechniqueType");

		System.out.println(res.getBody().asString());
	}
}
