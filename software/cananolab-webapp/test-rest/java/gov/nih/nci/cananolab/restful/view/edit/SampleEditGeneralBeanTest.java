package gov.nih.nci.cananolab.restful.view.edit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;

public class SampleEditGeneralBeanTest {

	@Test
	public void testPopulateDataForSavingSample() {
		SampleEditGeneralBean editBean = new SampleEditGeneralBean();
		
		editBean.setSampleName("SomeName");
	}
	
	@Test
	public void testSetSubmitForReviewButton() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		when(request.getAttribute("submitSample")).thenReturn(null );
		SampleEditGeneralBean editBean = new SampleEditGeneralBean();
		editBean.setSubmitForReviewButton(request);
		assertFalse(editBean.isShowSubmitForReviewButton());
		
		when(request.getAttribute("submitSample")).thenReturn("false");
		editBean.setSubmitForReviewButton(request);
		assertFalse(editBean.isShowSubmitForReviewButton());
		
		when(request.getAttribute("submitSample")).thenReturn("true");
		editBean.setSubmitForReviewButton(request);
		assertTrue(editBean.isShowSubmitForReviewButton());
	}

}
