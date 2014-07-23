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
}
