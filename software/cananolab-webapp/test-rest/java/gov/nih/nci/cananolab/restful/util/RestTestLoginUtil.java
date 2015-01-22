package gov.nih.nci.cananolab.restful.util;

import static com.jayway.restassured.RestAssured.with;

import java.util.Properties;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cananolab.restful.util.RestTestLoginUtil;
import gov.nih.nci.cananolab.util.PropertyUtils;
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
			String username = RestTestLoginUtil.readUserNameProperty();
			String pwd = RestTestLoginUtil.readPasswordProperty();
			
			if (username == null || username.length() == 0 ||
					pwd == null || pwd.length() == 0)
				return null;
			
			Response response = with().parameters("username", username, "password", pwd)
					.expect().statusCode(200).when().get("http://localhost:8080/caNanoLab/rest/security/login");

			jsessionId = response.getCookie("JSESSIONID");
		}
		return jsessionId;
	}
	
	public static void logoutTest() {
		expect().statusCode(200).when().get("http://localhost:8080/caNanoLab/rest/security/logout");
		jsessionId = null;
	}
	
	public static String readUserNameProperty() {
		return PropertyUtils.getPropertyCached("local.properties", "user.name");
	}

	public static String readPasswordProperty() {
		return PropertyUtils.getPropertyCached("local.properties", "password");
	}
}
