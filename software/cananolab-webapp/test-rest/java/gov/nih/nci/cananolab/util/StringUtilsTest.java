package gov.nih.nci.cananolab.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testGetCamelCaseFormatInWords() {
		String words = "pending review";
		
		String upper = StringUtils.getCamelCaseFormatInWords(words);
		assertTrue(upper.equals("Pending Review"));
		
		upper = StringUtils.getCamelCaseFormatInWords(null);
		assertNull(upper);
		
		words = "pending ";
		upper = StringUtils.getCamelCaseFormatInWords(words);
		assertTrue(upper.equals("Pending"));
	}

}
