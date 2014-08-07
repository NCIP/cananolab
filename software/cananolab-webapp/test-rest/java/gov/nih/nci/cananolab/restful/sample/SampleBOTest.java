package gov.nih.nci.cananolab.restful.sample;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import gov.nih.nci.cananolab.restful.view.edit.SampleEditGeneralBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-strutsless.xml"})
public class SampleBOTest {
	
	@Autowired
	SampleBO sampleBO;

	@Test
	public void testFindMatchSampleInSession() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCreateNewSample() {

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);

		when(request.getSession()).thenReturn(session);

		when(session.getAttribute("functionTypes")).thenReturn(new TreeSet<String>());
		when(session.getAttribute("nanomaterialEntityTypes")).thenReturn(new TreeSet<String>());
		when(session.getAttribute("functionalizingEntityTypes")).thenReturn(new TreeSet<String>());
		when(session.getAttribute("characterizationTypes")).thenReturn(new ArrayList<String>());
		try {
			SampleEditGeneralBean editBean = sampleBO.summaryEdit("44695553", request);
			editBean.setSampleId(44695553);
			editBean.setSampleName("SY-Monday2");
			editBean.setNewSampleName("CloneFromSY-Monday2");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
