package gov.nih.nci.cananolab.restful;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleComposingElementBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleNanomaterialEntityBean;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class NanomaterialEntityServiceTest {

	@Test
	public void testSetup() {

		Response res =
				given().contentType("application/json")
				.parameter("sampleId", "20917510").expect()
				.body("nanomaterialEntityTypes", hasItems("biopolymer","carbon","carbon black","carbon nanotube","dendrimer","emulsion","fullerene","liposome","metal oxide","metal particle","metalloid","nanohorn","nanolipogel","nanorod","nanoshell","polymer","quantum dot","silica"))
						.when().get("http://localhost:8080/caNanoLab/rest/nanomaterialEntity/setup");

		System.out.println(res.getBody().asString());
		
	}

	@Test
	public void testEdit() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sampleId", "20917506");
		parameters.put("dataId", "60260353");
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.parameters(parameters).expect()
				.body("description", equalToIgnoringCase("Test Nano Entity"))
						.when().get("http://localhost:8080/caNanoLab/rest/nanomaterialEntity/edit");

		System.out.println(res.getBody().asString());
		RestTestLoginUtil.logoutTest();
		
	}
	@Test
	public void testSaveComposingElement() {
		
		String jsessionId = RestTestLoginUtil.loginTest();
		
		SimpleNanomaterialEntityBean simpleNano = new SimpleNanomaterialEntityBean();
		simpleNano.setSampleId("20917510");
		simpleNano.setType("carbon");
		SimpleComposingElementBean comp = new SimpleComposingElementBean();
		comp.setType("monomer");
		comp.setName("TestChem");
		List<Map<String, Object>> inherent = new ArrayList<Map<String,Object>>();
		List<SimpleComposingElementBean> compList = new ArrayList<SimpleComposingElementBean>();
		comp.setInherentFunction(inherent);
		simpleNano.setSimpleCompBean(comp);
		simpleNano.setComposingElements(compList);
		Response res =
				given().contentType("application/json").cookie("JSESSIONID=" + jsessionId)
				.body(simpleNano).expect()
				.body("Type", equalToIgnoringCase("monomer"))
						.when().post("http://localhost:8080/caNanoLab/rest/nanomaterialEntity/saveComposingElement");

		System.out.println(res.getBody().asString());
		RestTestLoginUtil.logoutTest();
		
	}
}
