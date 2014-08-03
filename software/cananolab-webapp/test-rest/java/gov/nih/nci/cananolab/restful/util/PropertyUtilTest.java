package gov.nih.nci.cananolab.restful.util;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertyUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadProperty() {
		Properties props = PropertyUtil.loadProperty("sample.properties");
		assertNotNull(props);
		
		String val = PropertyUtil.getProperty("sample", "sampleForm.sampleName");
		assertTrue(val.startsWith("Sample"));
	}
	
	@Test
	public void testGetProperty() {
		
		String val = PropertyUtil.getProperty("sample", "sampleForm.sampleName");
		assertNotNull(val);
		assertTrue(val.startsWith("Sample"));
	}

	@Test
	public void testGetPropertyReplacingToken() {
		String val = PropertyUtil.getPropertyReplacingToken("sample", "error.cloneSample.noOriginalSample", "0", "TheSampleName");
		assertNotNull(val);
		assertTrue(!val.contains("{0}"));
		assertTrue(val.contains("TheSampleName"));
	}
}
