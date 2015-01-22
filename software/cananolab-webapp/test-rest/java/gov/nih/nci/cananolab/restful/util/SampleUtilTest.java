package gov.nih.nci.cananolab.restful.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.nih.nci.cananolab.restful.util.SampleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SampleUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReformatLocalSearchDropdownsInSessionCheckNulls() {
		Map<String, List<String>> types = SampleUtil.reformatLocalSearchDropdownsInSession(null);
		assertTrue(types==null);
		
		HttpSession session = mock(HttpSession.class);
		
		when(session.getAttribute("functionTypes")).thenReturn(null );
		when(session.getAttribute("nanomaterialEntityTypes")).thenReturn(null );
		when(session.getAttribute("functionalizingEntityTypes")).thenReturn(null );
		when(session.getAttribute("characterizationTypes")).thenReturn(null );
		
		types = SampleUtil.reformatLocalSearchDropdownsInSession(session);
		assertTrue(types.get("functionTypes") == null);
		assertTrue(types.get("nanomaterialEntityTypes") == null);
		assertTrue(types.get("functionalizingEntityTypes") == null);
		assertTrue(types.get("characterizationTypes") == null);
		
	}

	@Test
	public void testReformatLocalSearchDropdownsInSessionHappy() {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		
		when(request.getSession()).thenReturn(session);
		
		when(session.getAttribute("functionTypes")).thenReturn(new TreeSet<String>());
		when(session.getAttribute("nanomaterialEntityTypes")).thenReturn(new TreeSet<String>());
		when(session.getAttribute("functionalizingEntityTypes")).thenReturn(new TreeSet<String>());
		when(session.getAttribute("characterizationTypes")).thenReturn(new ArrayList<String>());
		
		Map<String, List<String>> types = SampleUtil.reformatLocalSearchDropdownsInSession(session);
		assertTrue(types.get("functionTypes") != null);
		assertTrue(types.get("nanomaterialEntityTypes") != null);
		assertTrue(types.get("functionalizingEntityTypes") != null);
		assertTrue(types.get("characterizationTypes") != null);
		
	}
}
