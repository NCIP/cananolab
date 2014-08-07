package gov.nih.nci.cananolab.restful.validator;

import static org.junit.Assert.*;

import java.util.List;

import gov.nih.nci.cananolab.restful.view.edit.SimpleAddressBean;

import org.junit.Test;

public class RestValidatorTest {

	@Test
	public void testValidate() {
		SimpleAddressBean address = new SimpleAddressBean();
		address.setZip(null);
		
		List<String> errors = RestValidator.validate(address);
		
		assertNotNull(errors);
		assertTrue(errors.size() == 0);
		
		address.setZip("");
		
		errors = RestValidator.validate(address);
		
		assertNotNull(errors);
		assertTrue(errors.size() == 0);
		
		
		address.setZip("fasfal#&*");
		
		errors = RestValidator.validate(address);
		
		assertNotNull(errors);
		assertTrue(errors.size() >  0);
		
		System.out.println(errors.get(0));
	}

}
