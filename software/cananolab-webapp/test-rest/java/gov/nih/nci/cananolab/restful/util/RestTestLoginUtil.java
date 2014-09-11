package gov.nih.nci.cananolab.restful.util;

import static com.jayway.restassured.RestAssured.with;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.hamcrest.Matchers.hasItems;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import static com.jayway.restassured.RestAssured.expect;

import org.junit.Test;

import com.jayway.restassured.response.Response;

/**
 * 
 * @author yangs8
 *
 */
public class RestTestLoginUtil {
	
	public static String jsessionId = null;
	
	/**
	 * Need to set correct credentials before running test. Do not check in this file
	 * with credential!!
	 * 
	 * @return
	 */
	public static String loginTest() {

		if (jsessionId == null) {
			Response response = with().parameters("username", "yangs8", "password", "")
					.expect().statusCode(200).when().get("http://localhost:8080/caNanoLab/rest/security/login");

			jsessionId = response.getCookie("JSESSIONID");
		}
		return jsessionId;
	}
	
	public static void logoutTest() {
		expect().statusCode(200).when().get("http://localhost:8080/caNanoLab/rest/security/logout");
		jsessionId = null;
	}

}
