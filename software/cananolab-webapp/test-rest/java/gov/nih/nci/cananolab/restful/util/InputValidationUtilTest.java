package gov.nih.nci.cananolab.restful.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class InputValidationUtilTest {

	@Test
	public void testIsAlphabetic() {
		boolean is = InputValidationUtil.isAlphabetic("sfiealaea");
		assertTrue(is);
	}
	
	@Test
	public void testIsAlphabeticNullInput() {
		boolean is = InputValidationUtil.isAlphabetic(null);
		assertFalse(is);
	}

	@Test
	public void testIsAlphabeticNotMatch() {
		boolean is = InputValidationUtil.isAlphabetic("daasl4");
		assertFalse(is);
	}
	
	@Test
	public void testIsDOI() {
		boolean is = InputValidationUtil.isDoiValid(null);
		assertTrue(is);
	}
	
	@Test
	public void testIsZipValid() {
		String zip = "22222";
		boolean is = InputValidationUtil.isZipValid(zip);
		assertTrue(is);
	}
	
	@Test
	public void testIsEmailValid() {
		String email = "s.y@nih.gov";
		boolean is = InputValidationUtil.isEmailValid(email);
		assertTrue(is);
		
		email = null;
		is = InputValidationUtil.isEmailValid(email);
		assertTrue(is);
		
		email = "";
		is = InputValidationUtil.isEmailValid(email);
		assertTrue(is);
		
		email = "s.y";
		is = InputValidationUtil.isEmailValid(email);
		assertFalse(is);
		
		email = "@nih.gov";
		is = InputValidationUtil.isEmailValid(email);
		assertFalse(is);
	}
 }
